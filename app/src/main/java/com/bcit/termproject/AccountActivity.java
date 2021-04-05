package com.bcit.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    FirebaseUser currAuthUser;
    User currUser;
    TextView userName;
    String employStatus;

    MaterialDatePicker<?> datePicker;

    RecyclerView rvAccDetails;

    private HashMap<String, String> accDetailMap = new HashMap<>();
    private List<String> accDetailList = new ArrayList<>();

    DatabaseReference dbUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().hide();

        rvAccDetails = findViewById(R.id.rv_account_details);

        currAuthUser = FirebaseAuth.getInstance().getCurrentUser();
        userName = findViewById(R.id.textView_userName_account);

        dbUserInfo = FirebaseDatabase.getInstance().getReference("user");
    }

    /**
     * Update a user detail field for the current user to the value passed in.
     * @param field: a String, the user field to be updated in the database
     * @param updateValue: a String, the new value to be set
     */
    public void updateUser(String field, String updateValue) {

        Task<Void> setValueTask;

        if (field.equals("employed")) {
            boolean employment = updateValue.equals("Employed");
            setValueTask = dbUserInfo.child(currAuthUser.getUid()).child(field).setValue(employment);
        } else {
           setValueTask = dbUserInfo.child(currAuthUser.getUid()).child(field).setValue(updateValue);
        }

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(AccountActivity.this, "Updated " + field, Toast.LENGTH_LONG).show();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Populate the activity layout with the user details retrieved from
     * the database.
     */
    @Override
    protected void onStart() {
        super.onStart();
        dbUserInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currUser = snapshot.child(currAuthUser.getUid()).getValue(User.class);
                userName.setText(currUser.getName());

                setAccountDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     *  Set the RecyclerView to display user account details.
     */
    private void setAccountDetails() {
        accDetailList.clear();
        accDetailMap.clear();

        accDetailList.add("name");
        accDetailList.add("DOB");
        accDetailList.add("gender");
        accDetailList.add("yearlyIncome");
        accDetailList.add("nationality");
        accDetailList.add("employed");

        accDetailMap.put("name", currUser.getName());
        accDetailMap.put("DOB", currUser.getDOB());
        accDetailMap.put("gender", currUser.getGender());
        accDetailMap.put("yearlyIncome", currUser.getYearlyIncome());
        accDetailMap.put("nationality", currUser.getNationality());

        employStatus = currUser.getEmployed() ? "Employed" : "Unemployed";
        accDetailMap.put("employed", employStatus);

        AccountAdapter adapter = new AccountAdapter(accDetailMap, accDetailList);

        adapter.setOnAdapterItemListener(new AccountAdapter.OnAdapterItemListener() {
            @Override
            public void OnClick(String label) {

                // Show the corresponding layout for the label passed in
                switch(label) {
                    case "DOB":
                        showDOBDialog();
                        return;
                    case "name":
                        showFormDialog(label, currUser.getName());
                        return;
                    case "yearlyIncome":
                        showFormDialog(label, currUser.getYearlyIncome());
                        return;
                    case "gender":
                        showSpinnerDialog(label, currUser.getGender());
                        return;
                    case "nationality":
                        showSpinnerDialog(label, currUser.getNationality());
                        return;
                    default:
                        showRadioDialog(label, employStatus);
                }
            }
        });

        rvAccDetails.setAdapter(adapter);
        rvAccDetails.setLayoutManager(new LinearLayoutManager(AccountActivity.this));
    }

    /**
     * Displays a dialog with an EditText if the user chooses to edit their name or yearly income.
     */
    private void showFormDialog(String label, String value) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = setDialogView(R.layout.account_detail_form, label, value);
        dialogBuilder.setView(dialogView);

        Button cancel = dialogView.findViewById(R.id.button_cancelEdit);
        Button saveAccount = dialogView.findViewById(R.id.button_saveAccount);

        EditText editTextAccount = dialogView.findViewById(R.id.editText_account);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(label, editTextAccount.getText().toString());
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * Set the values for the dialogView and Views that will be displayed in the dialogs
     * for editing account details.
     *
     * @param formId: an int, the id of the form layout
     * @param label: String label for the user field
     * @param value: String value for the user field
     * @return dialogView as a DialogView
     */
    private View setDialogView(int formId, String label, String value){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(formId, null);

        TextView accDetail = dialogView.findViewById(R.id.textView_account);
        TextView accValue = dialogView.findViewById(R.id.textView_accDetail_value);
        accDetail.setText(label);
        accValue.setText(value);

        return dialogView;
    }

    /**
     * Display a spinner dialog if the user chooses to edit nationality or gender.
     * @param label: String label for the user field
     * @param value: String value for the user field
     */
    private void showSpinnerDialog(String label, String value) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = setDialogView(R.layout.account_detail_spinner, label, value);
        dialogBuilder.setView(dialogView);

        Button cancel = dialogView.findViewById(R.id.button_cancelEdit);
        Button saveAccount = dialogView.findViewById(R.id.button_saveAccount);

        Spinner spinnerForm = dialogView.findViewById(R.id.spinner_account);
        String[] items;

        if (label.equals("gender")) {
            items = new String[]{"Male", "Female", "Other"};
        } else {
            items = new String[]{"Canadian", "American", "Other"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerForm.setAdapter(adapter);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(label, spinnerForm.getSelectedItem().toString());
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    /**
     * Display a dialog with radio buttons if the user chooses to edit their employment status.
     * @param label: String label for the user field
     * @param value: String value for the user field
     */
    private void showRadioDialog(String label, String value) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = setDialogView(R.layout.account_detail_radio, label, value);
        dialogBuilder.setView(dialogView);

        Button cancel = dialogView.findViewById(R.id.button_cancelEdit);
        Button saveAccount = dialogView.findViewById(R.id.button_saveAccount);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup_employment);
        int currChecked = currUser.getEmployed()? R.id.radioButton_employed: R.id.radioButton_unemployed;
        radioGroup.check(currChecked);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton selectedButton = dialogView.findViewById(radioGroup.getCheckedRadioButtonId());
                updateUser(label, selectedButton.getText().toString());
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * Display a MaterialDatePicker for selecting Date of Birth.
     */
    private void showDOBDialog() {
        datePicker = MaterialDatePicker.Builder.datePicker()
                        .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                        .setTitleText("Date of Birth: " + currUser.getDOB())
                        .build();

        datePicker.show(getSupportFragmentManager(), "tag");

        datePicker.addOnPositiveButtonClickListener(selection -> updateUser("dob", datePicker.getHeaderText())
        );
    }
}

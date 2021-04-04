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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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

        Log.d("current user", currAuthUser.getUid());

        dbUserInfo = FirebaseDatabase.getInstance().getReference("user");
    }

    public void updateUser(String field, String updateValue) {

        Log.d("Updating: ", field + " to " + updateValue);

        Task<Void> setValueTask = dbUserInfo.child(currAuthUser.getUid()).child(field).setValue(updateValue);

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
     *
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

        String employStatus = currUser.getEmployed() ? "Employed" : "Unemployed";
        accDetailMap.put("employed", employStatus);

        AccountAdapter adapter = new AccountAdapter(accDetailMap, accDetailList);

        adapter.setOnAdapterItemListener(new AccountAdapter.OnAdapterItemListener() {
            @Override
            public void OnClick(String label) {

                int formId;

                // Show the corresponding layout for the label passed in
                switch(label) {
                    case "DOB":
                        showDOBDialog();
                        return;
                    case "name":
                    case "yearlyIncome":
                        formId = R.layout.account_detail_form;
                        break;
                    case "gender":
                    case "nationality":
                        formId = R.layout.account_detail_spinner;
                        break;
                    default:
                        // This should be a radio button for employment status
                        formId = R.layout.account_detail_form;
                }

                showFormDialog(formId, label);
            }
        });

        rvAccDetails.setAdapter(adapter);
        rvAccDetails.setLayoutManager(new LinearLayoutManager(AccountActivity.this));
    }

    /**
     * For name and income
     */
    private void showFormDialog(int formId, String label) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        Log.d("formId", String.valueOf(formId));

        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(formId, null);
        dialogBuilder.setView(dialogView);

        TextView accDetail = dialogView.findViewById(R.id.textView_account);
        TextView accValue = dialogView.findViewById(R.id.textView_accDetail_value);

        Button cancel = dialogView.findViewById(R.id.button_cancelEdit);

        accDetail.setText(label);
        accValue.setText(currUser.getName());

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

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
                        .setTitleText("Updating DOB")
                        .build();

        datePicker.show(getSupportFragmentManager(), "tag");

        datePicker.addOnPositiveButtonClickListener(selection -> updateUser("dob", datePicker.getHeaderText())
        );
    }
}

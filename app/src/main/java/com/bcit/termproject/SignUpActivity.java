package com.bcit.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    Spinner dropdown;
    TextView email;
    EditText password;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference ref;
    MaterialDatePicker<?> datePicker;
    TextView dobTV;
    ImageButton dobButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email_ET_signup);
        password = findViewById(R.id.password_ET_signup);
        database = FirebaseDatabase.getInstance();
        dobTV = (TextView)findViewById(R.id.DOB_TV_signup);

        setupDatePicker();
        setupSpinner();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
//            currentUser.reload();
        }
    }

    private void setupDatePicker(){
        dobButton = findViewById(R.id.dob_button);
        dobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDOBDialogInActivity();

            }
        });
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SINGUP", "New user registration: " + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed. " + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {
                            currentUser = mAuth.getCurrentUser();
                            getAdditionalUserInfo();
                        }
                    }

                });
    }

    private void setupSpinner() {
        //get the spinner from the xml.
        dropdown = findViewById(R.id.nationality_spinner_signup);

        //create a list of items for the spinner.
        String[] items = new String[]{"Canadian", "American", "Other"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
    }

    public void signUp(View v) {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        createAccount(emailStr, passwordStr);


    }

    private void showDOBDialogInActivity(){
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTitleText("Date of Birth")
                .build();

        datePicker.show(getSupportFragmentManager(), "tag");

        datePicker.addOnPositiveButtonClickListener(selection -> dobTV.setText(datePicker.getHeaderText()));
    }


    private void getAdditionalUserInfo () {
        RadioGroup genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radiogroup_signup);
        int selectedGender = genderRadioGroup.getCheckedRadioButtonId();

        String name = ((EditText)findViewById(R.id.name_ET_signup)).getText().toString();
        String DOB = (dobTV).getText().toString();
        String gender = ((RadioButton)findViewById(selectedGender)).getText().toString();
        String yearlyIncome = ((EditText)findViewById(R.id.yearlyincome_ET_signup)).getText().toString();
        String gpa = ((EditText)findViewById(R.id.gpa_ET_signup)).getText().toString();
        String nationality= ((TextView)findViewById(R.id.nationality_TV_signup)).getText().toString();
        boolean employed = ((CheckBox)findViewById(R.id.employed_checkBox)).isChecked();

        ArrayList<String> bookmarks = new ArrayList<>();

        User user = new User();
        user.setName(name);
        user.setDOB(DOB);
        user.setGender(gender);
        user.setYearlyIncome(yearlyIncome);
        user.setGPA(gpa);
        user.setNationality(nationality);
        user.setEmployed(employed);
        user.setBookmarked(bookmarks);

        Log.d("REGISTER", user.toString());

        Log.d("REGISTER", currentUser.getUid());
        DatabaseReference ref = database.getReference().child("user").child(currentUser.getUid());
        ref.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                if (databaseError != null) {
                    Log.d("REGISTER", "Failed to write message", databaseError.toException());
                } else{

                }
            }

        });
        SignUpActivity.this.startActivity(new Intent(SignUpActivity.this, MainActivity.class));


    }

}
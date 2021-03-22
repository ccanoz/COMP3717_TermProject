package com.bcit.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    Spinner dropdown;
    TextView email;
    EditText password;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference ref;


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


        setupSpinner();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
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
                            getAdditionalUserInfo();
//                            SignUpActivity.this.startActivity(new Intent(SignUpActivity.this, FeedActivity.class));
//                            SignUpActivity.this.finish();
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

    private void getAdditionalUserInfo() {
        RadioGroup genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radiogroup_signup);
        int selectedGender = genderRadioGroup.getCheckedRadioButtonId();

        String name = ((EditText)findViewById(R.id.name_ET_signup)).getText().toString();
        String DOB = ((EditText)findViewById(R.id.DOB_ET_signup)).getText().toString();
        String gender = ((RadioButton)findViewById(selectedGender)).getText().toString();
        String yearlyIncome = ((EditText)findViewById(R.id.yearlyincome_ET_signup)).getText().toString();
        String nationality= ((TextView)findViewById(R.id.nationality_TV_signup)).getText().toString();
        boolean employed = ((CheckBox)findViewById(R.id.employed_checkBox)).isChecked();

        User user = new User();
        user.setName(name);
        user.setDOB(DOB);
        user.setGender(gender);
        user.setYearlyIncome(yearlyIncome);
        user.setNationality(nationality);
        user.setEmployed(employed);

        Log.d("REGISTER", user.toString());

        DatabaseReference ref = database.getReference().child("user").child(currentUser.getUid());
        ref.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                if (databaseError != null) {
                    Log.d("REGISTER", "Failed to write message", databaseError.toException());
                }
            }
        });

    }

}
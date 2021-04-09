package com.bcit.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private Spinner dropdown;
    private TextView email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private MaterialDatePicker<?> datePicker;
    private TextView dobTV;
    private ImageButton dobButton;
    private String name;
    private String DOB;
    private String gender;
    private String yearlyIncome;
    private String gpa;
    private String nationality;
    private boolean employed;
    private ArrayList<String> bookmarks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        email = findViewById(R.id.email_ET_signup);
        password = findViewById(R.id.password_ET_signup);
        dobTV = findViewById(R.id.DOB_TV_signup);
        dobButton = findViewById(R.id.dob_button);
        dropdown = findViewById(R.id.nationality_spinner_signup);


        setupDatePicker();
        setupSpinner();


    }

    @Override
    public void onStart() {
        super.onStart();
        // get current user if there is one
        currentUser = mAuth.getCurrentUser();
    }

    /**
     * Setup the date of birth date picker.
     */
    private void setupDatePicker(){
        dobButton.setOnClickListener(v -> showDOBDialogInActivity());
    }

    /**
     * Creates an account from the email and password inputs.
     * @param email a String
     * @param password a String
     */
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Authentication failed. " + task.getException(), Toast.LENGTH_SHORT).show();
                    } else {
                        currentUser = mAuth.getCurrentUser();
                        getAdditionalUserInfo();
                    }
                });
    }

    /**
     * Set up the nationality spinner.
     */
    private void setupSpinner() {
        //create a list of items for the spinner.
        String[] items = new String[]{"Canadian", "American", "Other"};

        // create an adapter to describe how the items are displayed, adapters are used in several
        // places in android.
        // There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
    }

    /**
     * Click listener for the signup button. Retrieves the input from the text fields and creates
     * an account with the email and password fields.
     * @param v a View, the signup button
     */
    public void signUp(View v) {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        createAccount(emailStr, passwordStr);


    }

    /**
     * Display a dialog with a date picker.
     */
    private void showDOBDialogInActivity(){
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTitleText("Date of Birth")
                .build();

        datePicker.show(getSupportFragmentManager(), "tag");

        datePicker.addOnPositiveButtonClickListener(selection -> dobTV.setText(
                datePicker.getHeaderText()));
    }

    /**
     * Extract the string values from the input fields and store them in private global variables;
     */
    private void extractFieldStrings(){
        RadioGroup genderRadioGroup = findViewById(R.id.gender_radiogroup_signup);
        int selectedGender = genderRadioGroup.getCheckedRadioButtonId();
        String selectedNationality = dropdown.getSelectedItem().toString();

        // get the values of all the fields
        name = ((EditText)findViewById(R.id.name_ET_signup)).getText().toString();
        DOB = (dobTV).getText().toString();
        gender = ((RadioButton)findViewById(selectedGender)).getText().toString();
        yearlyIncome = ((EditText)findViewById(R.id.yearlyincome_ET_signup)).getText().toString();
        gpa = ((EditText)findViewById(R.id.gpa_ET_signup)).getText().toString();
        nationality = selectedNationality;
        employed = ((CheckBox)findViewById(R.id.employed_checkBox)).isChecked();

        bookmarks = new ArrayList<>();
    }

    /**
     * Creates a user from the text fields values.
     * @return a User object
     */
    private User createUser(){
        User user = new User();
        user.setName(name);
        user.setDOB(DOB);
        user.setGender(gender);
        user.setIncome(yearlyIncome);
        user.setGPA(gpa);
        user.setNationality(nationality);
        user.setEmployed(employed);
        user.setBookmarked(bookmarks);
        return user;
    }

    /**
     * Helper method that takes in a User object and adds it to the database.
     * @param user a newly created User object
     * @return true if successful, false if any errors occur.
     */
    private boolean addUserToDB(User user){
        DatabaseReference ref = database.getReference().child("user").child(currentUser.getUid());
        final String[] dbError = {null};
        ref.setValue(user, (databaseError, reference) -> {
            if (databaseError != null) {
                Log.d("REGISTER", "Failed to write message", databaseError.toException());
                dbError[0] = databaseError.toException().toString();
            }
        });
        return dbError[0] == null;
    }


    /**
     * Gets additional user information besides email and password.
     * Email and password are used for authentication, while the rest of the fields are stored in
     * our database in order for the app to be able to provide its core functionalities.
     */
    private void getAdditionalUserInfo () {
        extractFieldStrings();
        User user = createUser();
        if (addUserToDB(user)) {
            SignUpActivity.this.startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        } else{
            Toast.makeText(this, "Could not add user to database.", Toast.LENGTH_SHORT).show();
        }
    }

}
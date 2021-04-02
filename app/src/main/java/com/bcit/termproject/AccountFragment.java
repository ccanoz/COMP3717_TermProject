package com.bcit.termproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    View accountDetailAge;
    View accountDetailGender;
    View accountDetailIncome;
    View currToggled;

    TextView ageLabel;
    TextView genderLabel;
    TextView incomeLabel;

    Button saveAge;
    Button saveGender;
    Button saveIncome;

    DatabaseReference dbUserInfo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        accountDetailAge = view.findViewById(R.id.account_detail_age);
        accountDetailGender = view.findViewById(R.id.account_detail_gender);
        accountDetailIncome = view.findViewById(R.id.account_detail_income);

        ageLabel = accountDetailAge.findViewById(R.id.textView_accDetail_value);
        genderLabel = accountDetailGender.findViewById(R.id.textView_accDetail_value);
        incomeLabel  = accountDetailIncome.findViewById(R.id.textView_accDetail_value);

        saveAge = accountDetailAge.findViewById(R.id.button_saveAccount);
        saveGender = accountDetailGender.findViewById(R.id.button_saveAccount);
        saveIncome = accountDetailIncome.findViewById(R.id.button_saveAccount);

        setListeners();
        setFormInformation();

        // TestUser TpBUQaOpqiP4wTykld49PrxD1m62
        dbUserInfo = FirebaseDatabase.getInstance().getReference("user").child("TpBUQaOpqiP4wTykld49PrxD1m62");

        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void updateUser(String field, String updateValue) {

        Log.d("Updating: ", field + " to " + updateValue);

        Task<Void> setValueTask = dbUserInfo.child(field).setValue(updateValue);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getContext(), "Updated " + field, Toast.LENGTH_LONG).show();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Populate the activity layout with the user details retrieved from
     * the database.
     */
    @Override
    public void onStart() {
        super.onStart();
        dbUserInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String age = snapshot.child("dob").getValue(String.class);
                String gender = snapshot.child("gender").getValue(String.class);
                String income = snapshot.child("yearlyIncome").getValue(String.class);

                ageLabel.setText(age);
                genderLabel.setText(gender);
                incomeLabel.setText(income);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setFormInformation() {

        TextView tvAge = accountDetailAge.findViewById(R.id.textView_account);
        tvAge.setText("Age");

        TextView tvGender = accountDetailGender.findViewById(R.id.textView_account);
        tvGender.setText("Gender");

        TextView tvIncome = accountDetailIncome.findViewById(R.id.textView_account);
        tvIncome.setText("Income");
    }

    public void setListeners() {

        Button editAge = accountDetailAge.findViewById(R.id.button_editAccount);
        editAge.setOnClickListener(onClickEditAge);

        Button editGender = accountDetailGender.findViewById(R.id.button_editAccount);
        editGender.setOnClickListener(onClickEditGender);

        Button editIncome = accountDetailIncome.findViewById(R.id.button_editAccount);
        editIncome.setOnClickListener(onClickEditIncome);

        saveAge.setOnClickListener(onEditAge);
        saveGender.setOnClickListener(onEditGender);
        saveIncome.setOnClickListener(onEditIncome);
    }

    public View.OnClickListener onEditAge= new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            EditText editTextAccount = accountDetailAge.findViewById(R.id.editText_account);
            String updateValue = editTextAccount.getText().toString().trim();
            updateUser("dob", updateValue);
        }
    };

    public View.OnClickListener onEditGender= new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            EditText editTextAccount = accountDetailGender.findViewById(R.id.editText_account);
            String updateValue = editTextAccount.getText().toString().trim();
            updateUser("gender", updateValue);
        }
    };

    public View.OnClickListener onEditIncome= new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            EditText editTextAccount = accountDetailIncome.findViewById(R.id.editText_account);
            String updateValue = editTextAccount.getText().toString().trim();
            updateUser("yearlyIncome", updateValue);
        }
    };

    public void setVisibility() {
        LinearLayout linearLayout = currToggled.findViewById(R.id.linearLayout_account);
        if (linearLayout.getVisibility() == View.GONE) { ;
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    public View.OnClickListener onClickEditAge = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currToggled = accountDetailAge;
            setVisibility();
        }
    };

    public View.OnClickListener onClickEditGender = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currToggled = accountDetailGender;
            setVisibility();
        }
    };

    public View.OnClickListener onClickEditIncome = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            currToggled = accountDetailIncome;
            setVisibility();
        }
    };
}
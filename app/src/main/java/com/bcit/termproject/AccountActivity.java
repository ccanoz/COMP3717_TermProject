package com.bcit.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {

    View accountDetailAge;
    View accountDetailGender;
    View accountDetailIncome;
    View currToggled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().hide();

        accountDetailAge = findViewById(R.id.account_detail_age);
        accountDetailGender = findViewById(R.id.account_detail_gender);
        accountDetailIncome = findViewById(R.id.account_detail_income);
        setListeners();
        setFormInformation();
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

    }

    public View.OnClickListener onClickEditAge = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            currToggled = accountDetailAge;
            Log.d("Clicked Button: ", String.valueOf(id));
            LinearLayout linearLayout = currToggled.findViewById(R.id.linearLayout_account);
            if (linearLayout.getVisibility() == View.GONE) {
                Log.d("Visibility: ", "Set Visible");
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
                Log.d("Visibility: ", "Set Gone");
            }
        }
    };

    public View.OnClickListener onClickEditGender = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            currToggled = accountDetailGender;
            Log.d("Clicked Button: ", String.valueOf(id));
            LinearLayout linearLayout = currToggled.findViewById(R.id.linearLayout_account);
            if (linearLayout.getVisibility() == View.GONE) {
                Log.d("Visibility: ", "Set Visible");
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
                Log.d("Visibility: ", "Set Gone");
            }
        }
    };

    public View.OnClickListener onClickEditIncome = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            int id = v.getId();
            currToggled = accountDetailIncome;
            Log.d("Clicked Button: ", String.valueOf(id));
            LinearLayout linearLayout = currToggled.findViewById(R.id.linearLayout_account);
            if (linearLayout.getVisibility() == View.GONE) {
                Log.d("Visibility: ", "Set Visible");
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
                Log.d("Visibility: ", "Set Gone");
            }
        }
    };

}

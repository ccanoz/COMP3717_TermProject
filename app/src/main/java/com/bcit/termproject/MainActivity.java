package com.bcit.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static FirebaseUser currAuthUser;
    public static User currUser;
    public static DatabaseReference dbUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        currAuthUser = FirebaseAuth.getInstance().getCurrentUser();
        dbUserInfo = FirebaseDatabase.getInstance().getReference("user");

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationView);
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                item -> {
                    switch (item.getItemId()) {
                        case R.id.home:
                            openFragment(FeedFragment.newInstance("", ""));
                            return true;
                        case R.id.search:
                            openFragment(ItemsListingFragment.newInstance("", ""));
                            return true;
                        case R.id.account:
                            openFragment(AccountFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                };
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    currUser = snapshot.child(currAuthUser.getUid()).getValue(User.class);
                }catch (NullPointerException e){
                    Log.d("AUTHBUG", "going to landing1");
                    startActivity(new Intent(MainActivity.this, LandingActivity.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (currAuthUser != null || currUser != null) {
            openFragment(FeedFragment.newInstance("", ""));
        } else{
            Log.d("AUTHBUG", "going to landing2");
            startActivity(new Intent(MainActivity.this, LandingActivity.class));
        }

    }

    /**
     * Helper method to open different fragments in the main activity.
     * @param fragment a Fragment, the different pages in our app (home, search or account
     *                 fragments)
     */
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Click listener for the sign out image button.
     * @param v a View, the sign out image button
     */
    public void signOut(View v){
        currUser = null;
        FirebaseAuth.getInstance().signOut();
        Log.d("AUTHBUG", "going to landing3");
        startActivity(new Intent(this, LandingActivity.class));
    }
}
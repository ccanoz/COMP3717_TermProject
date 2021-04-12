package com.bcit.termproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ValueEventListener {

    public static FirebaseUser currAuthUser;
    public static User currUser;
    public static DatabaseReference dbUserInfo;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        currAuthUser = FirebaseAuth.getInstance().getCurrentUser();
        dbUserInfo = FirebaseDatabase.getInstance().getReference("user");
        currUser = null; // reset user
        bottomNavigation = findViewById(R.id.bottomNavigationView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbUserInfo.addValueEventListener(this);
        if (currAuthUser != null || currUser != null) {
            openFragment(FeedFragment.newInstance());
        } else {
            startActivity(new Intent(MainActivity.this, LandingActivity.class));
        }
    }

    /**
     * Sets the fragments that each item in the bottom navigation should be linked to.
     */
    private void setFragments() {
        @SuppressLint("NonConstantResourceId") BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                item -> {
                    switch (item.getItemId()) {
                        case R.id.home:
                            openFragment(FeedFragment.newInstance());
                            return true;
                        case R.id.search:
                            openFragment(ItemsListingFragment.newInstance());
                            return true;
                        case R.id.account:
                            openFragment(AccountFragment.newInstance());
                            return true;
                    }
                    return false;
                };
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
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
    public void signOut(View v) {
        currUser = null;
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LandingActivity.class));
    }

    /**
     * ValueEventListener method that listens to changes in the dbUserInfo variable (that represents
     * the Firebase database)
     * @param snapshot a DataSnapshot
     */
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        try {
            currUser = snapshot.child(currAuthUser.getUid()).getValue(User.class);
            setFragments();
        } catch (NullPointerException e) {
            startActivity(new Intent(MainActivity.this, LandingActivity.class));
        }
    }

    /**
     * Triggered if this listener either failed at the server, or is removed. Logs the error message
     * to the Logcat.
     * @param error the error description that we get from Firebase.
     */
    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.d("error", error.getMessage());
    }
}
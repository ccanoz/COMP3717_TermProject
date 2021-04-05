package com.bcit.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    FirebaseUser currAuthUser;
    User currUser;
    TextView currUserName;

    DatabaseReference databaseSchol;
    DatabaseReference dbUserInfo;
    ArrayList<Listing> listings;
    RecyclerView rvListings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getSupportActionBar().hide();

        currUserName = findViewById(R.id.textView_feed_user);
        currAuthUser = FirebaseAuth.getInstance().getCurrentUser();

        rvListings = findViewById(R.id.rvListings);
        listings = new ArrayList<Listing>();


        databaseSchol = FirebaseDatabase.getInstance().getReference("scholarship");
        dbUserInfo = FirebaseDatabase.getInstance().getReference("user");
    }

    @Override
    protected void onStart() {
        super.onStart();

    // TODO: Fix lines below - Commenting this out because its crashing the app

//        databaseSchol.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot scholSnapshot : snapshot.getChildren()) {
//                    String name = scholSnapshot.child("name").getValue(String.class);
//                    String desc = scholSnapshot.child("about").getValue(String.class);
//                    String tag1 = scholSnapshot.child("tags").child("0").getValue(String.class);
//                    String tag2 = scholSnapshot.child("tags").child("1").getValue(String.class);
//
//                    listings.add(new Listing("test", desc, tag1, tag2));
//                }
//                ListingAdapter adapter = new ListingAdapter(listings);
//                rvListings.setAdapter(adapter);
//                rvListings.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        dbUserInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currUser = snapshot.child(currAuthUser.getUid()).getValue(User.class);
                String userNameString = getString(R.string.welcome, currUser.getName());
                currUserName.setText(userNameString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private ArrayList<Listing> testListingList() {
//        ArrayList<Listing> listings = new ArrayList<Listing>();
//
//        listings.add(new Listing("First Scholarship", "A cool scholarship",
//                "Under 20,000", "Recently graduated"));
//        listings.add(new Listing("Second Scholarship", "A cool scholarship",
//                "Under 20,000", "Recently graduated"));
//        listings.add(new Listing("Third Scholarship", "A cool scholarship",
//                "Under 20,000", "Recently graduated"));
//        listings.add(new Listing("Fourth Scholarship", "A cool scholarship",
//                "Under 20,000", "Recently graduated"));
//
//        return listings;
//    }
}
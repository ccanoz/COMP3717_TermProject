package com.bcit.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemsListingActivity extends AppCompatActivity {

    DatabaseReference databaseSchol;
    ArrayList<Listing> listings;
    RecyclerView rvListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_listing);
        getSupportActionBar().hide();

        rvListings = findViewById(R.id.rvListings);

        listings = new ArrayList<Listing>();

        databaseSchol = FirebaseDatabase.getInstance().getReference("scholarship");

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseSchol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot scholSnapshot : snapshot.getChildren()) {
                    String name = scholSnapshot.child("name").getValue(String.class);
                    String desc = scholSnapshot.child("about").getValue(String.class);
                    String tag1 = scholSnapshot.child("tags").child("0").getValue(String.class);
                    String tag2 = scholSnapshot.child("tags").child("1").getValue(String.class);

                    listings.add(new Listing(name, desc, tag1, tag2));
                }
                ListingAdapter adapter = new ListingAdapter(listings);
                rvListings.setAdapter(adapter);
                rvListings.setLayoutManager(new LinearLayoutManager(ItemsListingActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//
//        databaseSchol.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listings.clear();
//                for (DataSnapshot scholSnapshot : snapshot.getChildren()) {
//                    Listing list = scholSnapshot.getValue(Listing.class);
//                    listings.add(list);
////                    Listing thisListing = scholSnapshot.getValue(Listing.class);
////                    listings.add(new Listing(thisListing.getScholarshipName(),
////                            thisListing.getDescription(), "tag1", "tag2"));
//                }
//
//                ListingAdapter adapter = new ListingAdapter(listings);
//                rvListings.setAdapter(adapter);
//                rvListings.setLayoutManager(new LinearLayoutManager(ItemsListingActivity.this));
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        databaseSchol.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listings.clear();
//                for (DataSnapshot listSnap : snapshot.getChildren()) {
//                    Listing list = listSnap.getValue(Listing.class);
//                    listings.add(list);
//                }
//
//                ListingAdapter adapter = new ListingAdapter(listings);
//
//                rvListings.setAdapter(adapter);
//                rvListings.setLayoutManager(new LinearLayoutManager(ItemsListingActivity.this));
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
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

//    private ArrayList<Listing> createListingList() {
//        ArrayList<Listing> listings = new ArrayList<Listing>();
//
//        listings.add(new Listing("thisListing.getScholarshipName()",
//                "thisListing.getDescription()", "tag1", "tag2"));
//        Log.v("LOG", "1");
//
//
//
//        databaseSchol.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                listings.add(new Listing("thisListing.getScholarshipName()",
//                        "thisListing.getDescription()", "tag1", "tag2"));
////                for (DataSnapshot scholSnapshot : dataSnapshot.getChildren()) {
////                    listings.add(new Listing("thisListing.getScholarshipName()",
////                            "thisListing.getDescription()", "tag1", "tag2"));
//////                    Listing thisListing = scholSnapshot.getValue(Listing.class);
////////                    listings.add(new Listing(thisListing.getScholarshipName(),
////////                            thisListing.getDescription(), "tag1", "tag2"));
//////                    String name = thisListing.getScholarshipName();
//////                    String about = thisListing.getDescription();
//////                    String tag1 = "tag1";
//////                    String tag2 = "tag2";
//////                    listings.add(new Listing(name, about, tag1, tag2));
//////                    return;
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        return listings;
//    }
}
package com.bcit.termproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ItemsListingActivity extends AppCompatActivity {

    ArrayList<Listing> listings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_listing);
        getSupportActionBar().hide();

        RecyclerView rvListings = findViewById(R.id.rvListings);

        listings = testListingList();

        ListingAdapter adapter = new ListingAdapter(listings);
        rvListings.setAdapter(adapter);
        rvListings.setLayoutManager(new LinearLayoutManager(this));

    }

    private ArrayList<Listing> testListingList() {
        ArrayList<Listing> listings = new ArrayList<Listing>();

        listings.add(new Listing("First Scholarship", "A cool scholarship",
                "Under 20,000", "Recently graduated"));
        listings.add(new Listing("Second Scholarship", "A cool scholarship",
                "Under 20,000", "Recently graduated"));
        listings.add(new Listing("Third Scholarship", "A cool scholarship",
                "Under 20,000", "Recently graduated"));
        listings.add(new Listing("Fourth Scholarship", "A cool scholarship",
                "Under 20,000", "Recently graduated"));

        return listings;
    }
}
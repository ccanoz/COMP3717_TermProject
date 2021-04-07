package com.bcit.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
                listings.clear();

                for (DataSnapshot scholSnapshot : snapshot.getChildren()) {
                    ArrayList<String> tags = new ArrayList<>();
                    String key = scholSnapshot.getKey();
                    String name = scholSnapshot.child("name").getValue(String.class);
                    String desc = scholSnapshot.child("about").getValue(String.class);

                    for (DataSnapshot tagSnap : scholSnapshot.child("tags").getChildren()) {
                        tags.add(tagSnap.getValue(String.class));
                    }

                    listings.add(new Listing(name, desc, key, tags));
                }
                ListingAdapter adapter = new ListingAdapter(listings);

                adapter.setOnAdapterItemListener(new OnAdapterItemListener() {
                    @Override
                    public void OnLongClick(Listing listing) {
                        Log.v("key", listing.getKey());
                        Intent intent = new Intent(ItemsListingActivity.this, ScholarshipInfoActivity.class);
                        intent.putExtra("SCHOLARSHIP_ITEM", listing.getKey());
                        startActivity(intent);
                    }
                    @Override
                    public void OnMarkClick(Listing listing) {
                        Log.v("mark", "Bookmark clicked");
                    }
                });

                rvListings.setAdapter(adapter);
                rvListings.setLayoutManager(new LinearLayoutManager(ItemsListingActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
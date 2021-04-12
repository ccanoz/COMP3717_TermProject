package com.bcit.termproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Fragment that displays the feed seen on the main page after log-in.
 */
public class FeedFragment extends Fragment {

    FirebaseUser currAuthUser;
    User currUser;
    TextView currUserName;
    Boolean isBookmarked;
    String scholId;

    DatabaseReference dbRefSchol;
    DatabaseReference dbUserInfo;
    ArrayList<Listing> listings;
    RecyclerView rvListings;

    TextView emptyText;
    ListingAdapter adapter;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Factory that returns a new FeedFragment.
     *
     * @return A new instance of fragment FeedFragment.
     */
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        currUserName = view.findViewById(R.id.textView_feed_user);
        currAuthUser = MainActivity.currAuthUser;

        rvListings = view.findViewById(R.id.rv_bookmarks);
        listings = new ArrayList<Listing>();
        emptyText = view.findViewById(R.id.tv_no_data);

        dbRefSchol = FirebaseDatabase.getInstance().getReference("scholarship");
        dbUserInfo = MainActivity.dbUserInfo;

        // Listen to data in the database for the current user
        setDbUserInfoListener();

        return view;
    }

    /**
     * Adds a value event listener to the database reference for the current user.
     * Sets the welcome message for the current user and sets up the user's
     * Bookmark list.
     */
    public void setDbUserInfoListener() {
        dbUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currUser = MainActivity.currUser;
                if (currUser != null) {
                    String userNameString = getString(R.string.welcome, currUser.getName());
                    currUserName.setText(userNameString);
                }

                // Listen to the data for the scholarships
                setDbRefScholListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Get scholarship listing details from the database, and use to create Listing objects.
     * Add the Listing objects created to an ArrayList.
     * @param snapshot DataSnapshot of scholarship details
     */
    public void setListings(DataSnapshot snapshot) {
        listings.clear();
        for (DataSnapshot scholSnapshot : snapshot.getChildren()) {
            ArrayList<String> tags = new ArrayList<>();

            // Check if scholarship id exists in the user's bookmark list before creating a Listing
            if (currUser != null && currUser.checkScholBookmarked(scholSnapshot.getKey())) {
                String key = scholSnapshot.getKey();
                String name = scholSnapshot.child("name").getValue(String.class);
                String desc = scholSnapshot.child("about").getValue(String.class);

                for (DataSnapshot tagSnap : scholSnapshot.child("tags").getChildren()) {
                    tags.add(tagSnap.getValue(String.class));
                }
                String img_url = scholSnapshot.child("logo").getValue(String.class);

                Listing newListing = new Listing(name, desc, key, tags, img_url);
                newListing.setIsBookmarked(true);
                listings.add(newListing);
            }
        }

    }


    /**
     * Populate the RecyclerView with the details for each Listing.
     */
    public void setRvListings() {

        // If no Bookmarked Listings, display a message
        checkBookmarkedEmpty();

        adapter = new ListingAdapter(listings);

        adapter.setOnAdapterItemListener(new OnAdapterItemListener() {

            // Launches the scholarship clicked
            @Override
            public void OnLongClick(Listing listing) {
                Intent intent = new Intent(getContext(), ScholarshipInfoActivity.class);
                intent.putExtra("SCHOLARSHIP_ITEM", listing.getKey());
                startActivity(intent);
            }

            // Toggles the bookmark icon
            @Override
            public void OnMarkClick(Listing listing) {
                scholId = listing.getKey();
                isBookmarked = currUser.getBookmarked() != null && (currUser.checkScholBookmarked(scholId));
                listing.setIsBookmarked(isBookmarked);
                bookmarkScholarship(listing);
            }
        });

        rvListings.setAdapter(adapter);
        rvListings.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
    }

    /**
     * Check if the user has no bookmarked listings and display a message
     * that there are no bookmarked listings.
     */
    public void checkBookmarkedEmpty() {
        if (!listings.isEmpty()) {
            //if data is available, don't show the empty text
            emptyText.setVisibility(View.GONE);
        } else
            emptyText.setVisibility(View.VISIBLE);
    }

    /**
     * Add a listener to values from the database and use the data to populate
     *
     */
    public void setDbRefScholListener() {
        dbRefSchol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setListings(snapshot);
                setRvListings();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * Update the current user's bookmark list to reflect whether the current scholarship is bookmarked or not.
     */
    public void updateBookmarks(Listing listing) {
        Task<Void> setValueTask = dbUserInfo.child(currAuthUser.getUid()).child("bookmarked").setValue(currUser.getBookmarked());
        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                if (listing.getIsBookmarked()) {
                    Toast.makeText(getContext(), "Scholarship bookmarked.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Scholarship removed from bookmarks.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Toggles between bookmarking/un-bookmarking a scholarship.
     */
    public void bookmarkScholarship(Listing listing) {
        if (listing.getIsBookmarked()) {
            currUser.unBookmark(scholId);
            listing.setIsBookmarked(false);
        } else {
            currUser.addToBookmarked(scholId);
            listing.setIsBookmarked(true);
        }
        updateBookmarks(listing);
    }


}
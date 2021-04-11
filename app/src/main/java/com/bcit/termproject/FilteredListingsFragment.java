package com.bcit.termproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Fragment that displays a filtered view of the scholarship Listings. The listings displayed
 * are based on the filter tag name that is passed in as a parameter on initialization of
 * this fragment.
 */
public class FilteredListingsFragment extends Fragment {

    DatabaseReference dbRefTags;
    DatabaseReference dbRefScholarship;
    DatabaseReference dbUserInfo;

    FirebaseDatabase dbRef;
    FirebaseUser currAuthUser;

    User currUser;

    ArrayList<Listing> listings;
    ArrayList<String> scholIds;
    String scholId;
    RecyclerView rvListings;
    Boolean isBookmarked = false;
    Chip chipTag;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String tagName;

    public FilteredListingsFragment() {
        // Required empty public constructor
    }

    /**
     * Factory that returns a new FilteredListingsFragment.
     * Parameter passed in represents the filter tag used to toggled
     * the scholarship Listings displayed in this fragment.
     *
     * @param tagName, String representing what to filter by
     * @return A new instance of fragment FilteredListingsFragment.
     */
    public static FilteredListingsFragment newInstance(String tagName) {
        FilteredListingsFragment fragment = new FilteredListingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, tagName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tagName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filtered_listings, container, false);

        currUser = MainActivity.currUser;

        rvListings = view.findViewById(R.id.rvListings);
        chipTag = view.findViewById(R.id.chip_tag);
        chipTag.setText(tagName);
        chipTag.setOnClickListener(v -> openFragment(ItemsListingFragment.newInstance("", "")));

        listings = new ArrayList<>();
        scholIds = new ArrayList<>();

        dbUserInfo = MainActivity.dbUserInfo;
        dbRefTags = FirebaseDatabase.getInstance().getReference("tags").child(tagName);
        dbRefScholarship = FirebaseDatabase.getInstance().getReference("scholarship");
        dbRef = FirebaseDatabase.getInstance();

        currAuthUser = MainActivity.currAuthUser;
        currUser = MainActivity.currUser;

        setDbRefTagListener();
        return view;
    }

    /**
     * Listen to values for the tags in the database.
     */
    public void setDbRefTagListener() {
        dbRefTags.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scholIds.clear();

                // Get the scholarship id's that exist for this tag
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String scholId = dataSnapshot.getValue(String.class);
                    scholIds.add(scholId);
                }

                // Get data for scholarships associated with this tag
                setDbRefScholarshipListener();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    /**
     * Listen to values for scholarships in the database.
     */
    public void setDbRefScholarshipListener(){
        dbRefScholarship.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        setListings(snapshot);
                        setRvListings();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    /**
     * Get scholarship listing details from the database, and use to create Listing objects.
     * Add the Listing objects created to an ArrayList.
     * @param snapshot DataSnapshot of scholarship details
     */
    public void setListings(DataSnapshot snapshot) {

        listings.clear();

        // Loop through each scholarship in the database
        for (DataSnapshot scholSnapshot : snapshot.getChildren()) {

            ArrayList<String> tags = new ArrayList<>();

            // Only create Listings for the scholarship id's that exist for the specific filter tag
            if (scholIds.contains(scholSnapshot.getKey())) {
                String key = scholSnapshot.getKey();
                String name = scholSnapshot.child("name").getValue(String.class);
                String desc = scholSnapshot.child("about").getValue(String.class);

                for (DataSnapshot tagSnap : scholSnapshot.child("tags").getChildren()) {
                    tags.add(tagSnap.getValue(String.class));
                }
                String img_url = scholSnapshot.child("logo").getValue(String.class);

                // Create a new Listing object using the database info and set it's bookmarked status
                Listing newListing = new Listing(name, desc, key, tags, img_url);

                if (currUser.checkScholBookmarked(newListing.getKey())){
                    newListing.setIsBookmarked(true);
                }

                listings.add(newListing);

            }
        }
    }

    /**
     * Populate the RecyclerView with the details for each Listing.
     */
    public void setRvListings() {
        ListingAdapter adapter = new ListingAdapter(listings);

        adapter.setOnAdapterItemListener(new OnAdapterItemListener() {
            @Override
            public void OnLongClick(Listing listing) {
                Intent intent = new Intent(getContext(), ScholarshipInfoActivity.class);
                intent.putExtra("SCHOLARSHIP_ITEM", listing.getKey());
                startActivity(intent);
            }

            @Override
            public void OnMarkClick(Listing listing) {
                scholId = listing.getKey();
                currUser = MainActivity.currUser;
                isBookmarked = currUser.getBookmarked() != null && (currUser.checkScholBookmarked(scholId));
                listing.setIsBookmarked(isBookmarked);
                bookmarkScholarship(listing);
            }
        });
        rvListings.setAdapter(adapter);
        rvListings.setLayoutManager(new LinearLayoutManager(getContext()));
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

    /**
     * Opens the specified fragment passed in.
     * @param fragment a Fragment.
     */
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
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
import android.widget.TextView;
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
 * Fragment that displays all of the Scholarship listings. Includes methods
 * to filter the listings, bookmark the listing, and view more details
 * about the scholarship.
 */
public class ItemsListingFragment extends Fragment {

    FirebaseUser currAuthUser;
    User currUser;
    TextView currUserName;
    Boolean isBookmarked;
    String scholId;
    Chip chipWomen;
    Chip chipCanadians;
    Chip chipLowIncome;

    DatabaseReference databaseSchol;
    DatabaseReference dbUserInfo;
    ArrayList<Listing> listings;
    RecyclerView rvListings;

    public ItemsListingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ItemsListingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsListingFragment newInstance() {
        ItemsListingFragment fragment = new ItemsListingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Sets up the current user, layout for this fragment, database references, and
     * Views.
     * @param inflater a LayoutInflater
     * @param container a ViewGroup
     * @param savedInstanceState a Bundle
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_items_listing, container, false);
        currUserName = view.findViewById(R.id.textView_feed_user);
        super.onCreate(savedInstanceState);

        setFilterChips(view);

        currAuthUser = MainActivity.currAuthUser;
        currUser = MainActivity.currUser;

        rvListings = view.findViewById(R.id.rvListings);
        listings = new ArrayList<Listing>();

        databaseSchol = FirebaseDatabase.getInstance().getReference("scholarship");
        dbUserInfo = MainActivity.dbUserInfo;
        return view;
    }

    /**
     * Set up the filter chips and onClick handlers for the different tag types.
     * @param view View for this fragment
     */
    public void setFilterChips(View view){
        chipWomen = view.findViewById(R.id.chip_women);
        chipCanadians = view.findViewById(R.id.chip_canadians);
        chipLowIncome = view.findViewById(R.id.chip_lowIncome);

        chipWomen.setOnClickListener(this::filterScholarships);
        chipCanadians.setOnClickListener(this::filterScholarships);
        chipLowIncome.setOnClickListener(this::filterScholarships);
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
            String key = scholSnapshot.getKey();
            String name = scholSnapshot.child("name").getValue(String.class);
            String desc = scholSnapshot.child("about").getValue(String.class);

            for (DataSnapshot tagSnap : scholSnapshot.child("tags").getChildren()) {
                tags.add(tagSnap.getValue(String.class));
            }

            String img_url = scholSnapshot.child("logo").getValue(String.class);

            Listing newListing = new Listing(name, desc, key, tags, img_url);

            if (currUser.checkScholBookmarked(newListing.getKey())){
                newListing.setIsBookmarked(true);
            }

            listings.add(newListing);
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
                isBookmarked = currUser.getBookmarked() != null && (currUser.checkScholBookmarked(scholId));
                listing.setIsBookmarked(isBookmarked);
                bookmarkScholarship(listing);
            }
        });

        rvListings.setAdapter(adapter);
        rvListings.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * Add a listener to the scholarship database reference. Set up the
     * recyclerview of Listings.
     */
    @Override
    public void onStart() {
        super.onStart();

        databaseSchol.addValueEventListener(new ValueEventListener() {
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

    /**
     * Launch a new instance of the fragment passed in.
     * @param fragment a Fragment
     */
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Filter the scholarships by the chip selected. Opens a new
     * FilteredListingsFragment.
     * @param v a View, the chip selected
     */
    public void filterScholarships(View v) {
        switch (v.getId()) {
            case R.id.chip_women:
                openFragment(FilteredListingsFragment.newInstance("Women"));
                break;
            case R.id.chip_canadians:
                openFragment(FilteredListingsFragment.newInstance("Canadians"));
                break;
            default:
                openFragment(FilteredListingsFragment.newInstance("Low Income"));
                break;
        }
    }

}
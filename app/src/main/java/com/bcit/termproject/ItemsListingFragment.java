package com.bcit.termproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsListingFragment#newInstance} factory method to
 * create an instance of this fragment.
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemsListingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemsListingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsListingFragment newInstance(String param1, String param2) {
        ItemsListingFragment fragment = new ItemsListingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

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


    @Override
    public void onStart() {
        super.onStart();

        databaseSchol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listings.clear();
                for (DataSnapshot scholSnapshot : snapshot.getChildren()) {
                    Log.v("snapshot", scholSnapshot.getKey());
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

                ListingAdapter adapter = new ListingAdapter(listings);

                adapter.setOnAdapterItemListener(new OnAdapterItemListener() {
                    @Override
                    public void OnLongClick(Listing listing) {
                        Log.v("key", listing.getKey());
                        Intent intent = new Intent(getContext(), ScholarshipInfoActivity.class);
                        intent.putExtra("SCHOLARSHIP_ITEM", listing.getKey());
                        startActivity(intent);
                    }

                    @Override
                    public void OnMarkClick(Listing listing) {
                        Log.v("mark", "Bookmark clicked");
                        scholId = listing.getKey();
                        isBookmarked = currUser.getBookmarked() != null && (currUser.checkScholBookmarked(scholId));
                        listing.setIsBookmarked(isBookmarked);
                        bookmarkScholarship(listing);
                    }
                });

                rvListings.setAdapter(adapter);
                rvListings.setLayoutManager(new LinearLayoutManager(getContext()));
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

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

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
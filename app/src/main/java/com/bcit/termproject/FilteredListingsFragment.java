package com.bcit.termproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilteredListingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilteredListingsFragment extends Fragment {

    DatabaseReference dbRefTags;
    DatabaseReference dbRefScholarship;
    ArrayList<Listing> listings;
    ArrayList<String> scholIds;
    RecyclerView rvListings;
    String tag;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FilteredListingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilteredListingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilteredListingsFragment newInstance(String param1, String param2) {
        FilteredListingsFragment fragment = new FilteredListingsFragment();
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
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_filtered_listings, container, false);

        rvListings = view.findViewById(R.id.rvListings);

        listings = new ArrayList<Listing>();
        scholIds = new ArrayList<>();

        dbRefTags = FirebaseDatabase.getInstance().getReference("tags").child("women");
        dbRefScholarship = FirebaseDatabase.getInstance().getReference("scholarship");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        dbRefTags.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String scholId = dataSnapshot.getValue(String.class);
                    scholIds.add(scholId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbRefScholarship.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot scholSnapshot : snapshot.getChildren()) {
                    if (scholIds.contains(scholSnapshot.getKey())) {
                        String key = scholSnapshot.getKey();
                        String name = scholSnapshot.child("name").getValue(String.class);
                        String desc = scholSnapshot.child("about").getValue(String.class);
                        String tag1 = scholSnapshot.child("tags").child("0").getValue(String.class);
                        String tag2 = scholSnapshot.child("tags").child("1").getValue(String.class);

                        listings.add(new Listing(name, desc, tag1, tag2, key));
                    }
                }
                ListingAdapter adapter = new ListingAdapter(listings);

                adapter.setOnAdapterItemListener(new OnAdapterItemListener() {
                    @Override
                    public void OnLongClick(Listing listing) {
                        Log.v("key", listing.getKey());
                        //TODO - commenting out for now because this is referencing an activity but we want a fragment
//                        Intent intent = new Intent(FilteredListingsFragment.this, ScholarshipInfoActivity.class);
//                        intent.putExtra("SCHOLARSHIP_ITEM", listing.getKey());
//                        startActivity(intent);
                    }
                    @Override
                    public void OnMarkClick(Listing listing) {
                        Log.v("mark", "Bookmark clicked");
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
}
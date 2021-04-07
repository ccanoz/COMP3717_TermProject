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
import android.widget.TextView;

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

        currAuthUser = MainActivity.currAuthUser;

        rvListings = view.findViewById(R.id.rvListings);
        listings = new ArrayList<Listing>();

        databaseSchol = FirebaseDatabase.getInstance().getReference("scholarship");
        dbUserInfo = MainActivity.dbUserInfo;
        return view;
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

                    listings.add(new Listing(name, desc, key, tags));
                }
//                rvListings = view.findViewById(R.id.rvListings);
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

//    private ArrayList<Listing> testListingList() {
//        ArrayList<Listing> listings = new ArrayList<Listing>();
//
//        listings.add(new Listing("First Scholarship", "A cool scholarship",
//                "Under 20,000", "Recently graduated", "hi"));
//        listings.add(new Listing("Second Scholarship", "A cool scholarship",
//                "Under 20,000", "Recently graduated", "hi"));
//        listings.add(new Listing("Third Scholarship", "A cool scholarship",
//                "Under 20,000", "Recently graduated", "hi"));
//        listings.add(new Listing("Fourth Scholarship", "A cool scholarship",
//                "Under 20,000", "Recently graduated", "hi"));
//
//        return listings;
//    }

}
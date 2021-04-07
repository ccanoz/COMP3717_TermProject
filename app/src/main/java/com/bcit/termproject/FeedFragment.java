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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

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

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        currUserName = view.findViewById(R.id.textView_feed_user);
        currAuthUser = MainActivity.currAuthUser;

        rvListings = view.findViewById(R.id.rv_bookmarks);
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
                for (DataSnapshot scholSnapshot : snapshot.getChildren()) {
                    Log.v("snapshot", scholSnapshot.getKey());
                    String key = scholSnapshot.getKey();
                    String name = scholSnapshot.child("name").getValue(String.class);
                    String desc = scholSnapshot.child("about").getValue(String.class);
                    String tag1 = scholSnapshot.child("tags").child("0").getValue(String.class);
                    String tag2 = scholSnapshot.child("tags").child("1").getValue(String.class);

                    listings.add(new Listing(name, desc, tag1, tag2, key));
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

        dbUserInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currUser = MainActivity.currUser;
                String userNameString = getString(R.string.welcome, currUser.getName());
                currUserName.setText(userNameString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
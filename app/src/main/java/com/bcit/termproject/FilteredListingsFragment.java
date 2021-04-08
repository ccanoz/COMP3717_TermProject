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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.chip.Chip;
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
    FirebaseDatabase dbRef;

    ArrayList<Listing> listings;
    ArrayList<String> scholIds;
    String scholId;
    RecyclerView rvListings;
    Boolean isBookmarked = false;
    Chip chipTag;

    User currUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String tagName;
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
            tagName = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        chipTag.setOnClickListener(v -> {
            openFragment(ItemsListingFragment.newInstance("", ""));
        });

        listings = new ArrayList<Listing>();

        scholIds = new ArrayList<>();

        dbRefTags = FirebaseDatabase.getInstance().getReference("tags").child(tagName);
        dbRefScholarship = FirebaseDatabase.getInstance().getReference("scholarship");
        dbRef = FirebaseDatabase.getInstance();

        Log.d("tag", tagName);



        dbRefTags.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FILTERED", "in onDataChange of tags");
                scholIds.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String scholId = dataSnapshot.getValue(String.class);
                    scholIds.add(scholId);
                }
                dbRefScholarship.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Log.d("FILTERED", "in onDataChange of scholarship");
                                listings.clear();

                                Log.d("FILTERED", snapshot.getChildren().toString());
                                for (DataSnapshot scholSnapshot : snapshot.getChildren()) {
                                    ArrayList<String> tags = new ArrayList<>();
                                    if (scholIds.contains(scholSnapshot.getKey())) {
                                        String key = scholSnapshot.getKey();
                                        String name = scholSnapshot.child("name").getValue(String.class);
                                        String desc = scholSnapshot.child("about").getValue(String.class);

                                        for (DataSnapshot tagSnap : scholSnapshot.child("tags").getChildren()) {
                                            tags.add(tagSnap.getValue(String.class));
                                        }
                                        String img_url = scholSnapshot.child("logo").getValue(String.class);

                                        listings.add(new Listing(name, desc, key, tags, img_url));
                                        Log.d("FILTERED", listings.toString());
                                    }
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
                                        currUser = MainActivity.currUser;
                                        isBookmarked = currUser.getBookmarked() != null && (currUser.checkScholBookmarked(scholId));
                                        bookmarkScholarship();
                                    }
                                });

                                rvListings.setAdapter(adapter);
                                rvListings.setLayoutManager(new LinearLayoutManager(getContext()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }



        });

//        dbRefScholarship.addValueEventListener();


        return view;
    }

    @Override
    public void onStart() {
        Log.d("FILTERED", "in onStart");
        super.onStart();




    }


    /**
     * Update the current user's bookmark list to reflect whether the current scholarship is bookmarked or not.
     */
    public void updateBookmarks() {
        Task<Void> setValueTask = MainActivity.dbUserInfo.child(MainActivity.currAuthUser.getUid()).child("bookmarked").setValue(currUser.getBookmarked());
        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                if (isBookmarked) {
                    Toast.makeText(getContext(), "Scholarship bookmarked.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Scholarship removed from bookmarks.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Sets the bookmark icon depending on if the current scholarship
     * is bookmarked or not.
     */
    public void setBookmarkIcon() {
        int iconId = isBookmarked? R.drawable.ic_bookmark_added: R.drawable.ic_bookmark_add;
//        scholBookmark.setImageResource(iconId);
    }

    /**
     * Toggles between bookmarking/un-bookmarking a scholarship.
     */
    public void bookmarkScholarship() {
        if (isBookmarked) {
            currUser.unBookmark(scholId);
            isBookmarked = false;
        } else {
            currUser.addToBookmarked(scholId);
            isBookmarked = true;
        }
        setBookmarkIcon();
        updateBookmarks();
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
package com.bcit.termproject;

import android.content.Intent;
import android.icu.util.TimeUnit;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    Boolean isBookmarked;
    String scholId;

    DatabaseReference databaseSchol;
    DatabaseReference dbUserInfo;
    ArrayList<Listing> listings;
    RecyclerView rvListings;

    Button btnWomen;
    Button btnLowIncome;
    Button btnCanadians;


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

        btnWomen = view.findViewById(R.id.button_women);
        btnWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterScholarships(v);
            }
        });
        btnLowIncome = view.findViewById(R.id.button_lowIncome);
        btnLowIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterScholarships(v);
            }
        });
        btnCanadians = view.findViewById(R.id.button_canadians);
        btnCanadians.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterScholarships(v);
            }
        });
        databaseSchol = FirebaseDatabase.getInstance().getReference("scholarship");
        dbUserInfo = MainActivity.dbUserInfo;
        return view;
    }

    public void filterScholarships(View v) {
        switch (v.getId()) {
            case R.id.button_women:
                openFragment(FilteredListingsFragment.newInstance("women", ""));
                return;
            case R.id.button_canadians:
                openFragment(FilteredListingsFragment.newInstance("canadians", ""));
                return;
            default:
                openFragment(FilteredListingsFragment.newInstance("lowIncome", ""));
                return;
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        //dbUserInfo.child("bookmarked")
////        Log.v("bookmark", dbUserInfo.child("bookmarked").getValue(String.class));
////        dbUserInfo.child(currAuthUser.getUid()).child("bookmarked").addValueEventListener(new ValueEventListener() {
        @Override
        public void onStart() {
            super.onStart();
//            try
//            {
//                Thread.sleep(2000);
//            }
//            catch(InterruptedException ex)
//            {
//                Thread.currentThread().interrupt();
//            }
            dbUserInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    databaseSchol.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listings.clear();
                            for (DataSnapshot scholSnapshot : snapshot.getChildren()) {
                                Log.v("snapshot", scholSnapshot.getKey());
                                ArrayList<String> tags = new ArrayList<>();
                                if (currUser.getBookmarked().contains(scholSnapshot.getKey())) {
                                    String key = scholSnapshot.getKey();
                                    String name = scholSnapshot.child("name").getValue(String.class);
                                    String desc = scholSnapshot.child("about").getValue(String.class);

                                    for (DataSnapshot tagSnap : scholSnapshot.child("tags").getChildren()) {
                                        tags.add(tagSnap.getValue(String.class));
                                    }
                                    String img_url = scholSnapshot.child("logo").getValue(String.class);

                                    listings.add(new Listing(name, desc, key, tags, img_url));
                                }
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

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    /**
     * Update the current user's bookmark list to reflect whether the current scholarship is bookmarked or not.
     */
    public void updateBookmarks() {
        Task<Void> setValueTask = dbUserInfo.child(currAuthUser.getUid()).child("bookmarked").setValue(currUser.getBookmarked());
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
//package com.bcit.termproject;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class FeedActivity extends AppCompatActivity {
//
//    FirebaseUser currAuthUser;
//    User currUser;
//    TextView currUserName;
//
//    DatabaseReference databaseSchol;
//    DatabaseReference dbUserInfo;
//    ArrayList<Listing> listings;
//    RecyclerView rvListings;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_feed);
//        getSupportActionBar().hide();
//
//        rvListings = findViewById(R.id.rvListings);
//        listings = new ArrayList<Listing>();
//        databaseSchol = FirebaseDatabase.getInstance().getReference("scholarship");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    // TODO: Fix lines below - Commenting this out because its crashing the app
//
////        databaseSchol.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                for (DataSnapshot scholSnapshot : snapshot.getChildren()) {
////        String key = scholSnapshot.getKey();
////                    String name = scholSnapshot.child("name").getValue(String.class);
////                    String desc = scholSnapshot.child("about").getValue(String.class);
////                    String tag1 = scholSnapshot.child("tags").child("0").getValue(String.class);
////                    String tag2 = scholSnapshot.child("tags").child("1").getValue(String.class);
////
////                    listings.add(new Listing("test", desc, tag1, tag2, key));
////                }
////                ListingAdapter adapter = new ListingAdapter(listings);
////                rvListings.setAdapter(adapter);
////                rvListings.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
//
//        dbUserInfo.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                currUser = snapshot.child(currAuthUser.getUid()).getValue(User.class);
//                Log.d("current user", currUser.getName());
//                String userNameString = getString(R.string.welcome, currUser.getName());
//                currUserName.setText(userNameString);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//}
package com.bcit.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Activity that holds information for a Scholarship Listing. Holds methods that construct the view
 * from Firebase data and toggle addition to the current user's bookmarks.
 */
public class ScholarshipInfoActivity extends AppCompatActivity {

    HashMap<String, String> requirementsDesc = new HashMap<>();
    List<String> requirements = new ArrayList<>();
    RecyclerView rvRequirements;
    TextView scholName;
    TextView scholAbout;
    TextView scholAmount;
    TextView scholOrg;
    FloatingActionButton scholBookmark;
    Boolean isBookmarked;
    Button applyButton;

    String scholId;

    FirebaseUser currAuthUser;
    User currUser;

    DatabaseReference dbUserInfo;
    DatabaseReference dbScholarships;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_info);

        Intent intent = getIntent();
        scholId = intent.getStringExtra("SCHOLARSHIP_ITEM");

        getViews();

        currAuthUser = MainActivity.currAuthUser;

        dbScholarships = FirebaseDatabase.getInstance().getReference("scholarship").child(scholId);
        dbUserInfo = MainActivity.dbUserInfo;
    }


    /**
     * Populate the activity layout with the scholarship details retrieved from
     * the database. Listen on changes to bookmark status for the scholarship being
     * viewed, for the current logged in user.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Listen to changes to the scholarship information
        dbScholarships.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requirementsDesc.clear();
                requirements.clear();

                setScholarshipInfo(snapshot);
                setScholarshipRequirements(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Listen to changes to the bookmark status
        dbUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currUser = MainActivity.currUser;
                isBookmarked = currUser.getBookmarked() != null && (currUser.checkScholBookmarked(scholId));
                setBookmarkIcon();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    /**
     * Store all of the views in ScholarshipInfoActivity into variables.
     */
    private void getViews() {
        scholBookmark = findViewById(R.id.fab_schol_bookmark);
        scholName = findViewById(R.id.textView_schol_name);
        scholAbout = findViewById(R.id.textView_schol_aboutTxt);
        scholAmount = findViewById(R.id.textView_schol_amount);
        scholOrg = findViewById(R.id.textView_schol_orgName);
        applyButton = findViewById(R.id.button_schol_apply);
        rvRequirements = findViewById(R.id.rv_schol_requirements);
    }

    /**
     * Retrieves a scholarship's name, description, amount, and organization name from a
     * DataSnapshot. Sets corresponding text fields in the layout to the values retrieved.
     *
     * @param snapshot of a Scholarship
     */
    private void setScholarshipInfo(@NonNull DataSnapshot snapshot){
        String name = snapshot.child("name").getValue(String.class);
        String description = snapshot.child("about").getValue(String.class);
        Long amount = snapshot.child("amount").getValue(Long.class);
        String organization = snapshot.child("organization").getValue(String.class);
        String logoUrl = snapshot.child("logo").getValue(String.class);
        String url = snapshot.child("apply").getValue(String.class);

        // Format with commas
        String amountString = "$ " + NumberFormat.getInstance().format(amount);

        scholName.setText(name);
        scholAbout.setText(description);
        scholAmount.setText(amountString);
        scholOrg.setText(organization);
        setApplyButton(url);

        ImageView logoImage = findViewById(R.id.imageView_schol_orgLogo);

        new ImageDownloaderTask(logoImage).execute(logoUrl);
    }

    /**
     * Sets the click listener to the apply button. The click listener redirects the user to the
     * scholarship url.
     *
     * @param url the url of the scholarship application page
     */
    private void setApplyButton(String url){
        applyButton.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }

    /**
     * Retrieves the requirements for a scholarship from a DataSnapshot and
     * populates the recyclerView with the requirement title and description.
     *
     * @param snapshot of a Scholarship
     */
    private void setScholarshipRequirements(@NonNull DataSnapshot snapshot) {
        DataSnapshot requirementsSnapshot = snapshot.child("Requirements");

        // Get each requirement and store in an array (titles) and HashMap (titles and description)
        for (DataSnapshot requirement : requirementsSnapshot.getChildren()) {
            String key = requirement.getKey();
            requirementsDesc.put(key, requirement.getValue(String.class));
            requirements.add(key);
        }

        RequirementsAdapter adapter = new RequirementsAdapter(requirementsDesc, requirements);
        rvRequirements.setAdapter(adapter);
        rvRequirements.setLayoutManager(new LinearLayoutManager(ScholarshipInfoActivity.this));
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
                    Toast.makeText(ScholarshipInfoActivity.this, "Scholarship bookmarked.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScholarshipInfoActivity.this, "Scholarship removed from bookmarks.", Toast.LENGTH_SHORT).show();
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
        scholBookmark.setImageResource(iconId);
    }

    /**
     * Toggles between bookmarking/un-bookmarking a scholarship.
     *
     * @param v View, the button bound to this function
     */
    public void bookmarkScholarship(View v) {
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

    /**
     * Set an alarm for this scholarship to notify the user of any news regarding it.
     *
     * @param v View, the button bound to this function
     */
    public void addAlarm(View v) {
        Toast.makeText(ScholarshipInfoActivity.this, "Alarm: Not Yet Implemented.", Toast.LENGTH_SHORT).show();
    }
}
package com.bcit.termproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;

public class ListingViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView;
    public TextView descTextView;
    public TextView tag1TextView;
    public TextView tag2TextView;
    public ImageView imageBookmark;
    public ChipGroup tagChipGroup;

    public ListingViewHolder(@NonNull View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.textView_listing_name);
        descTextView = itemView.findViewById(R.id.textView_listing_description);
        imageBookmark = itemView.findViewById(R.id.imageView_scholBookmark2);
        tagChipGroup = itemView.findViewById(R.id.chipGroup_tags);

    }
}

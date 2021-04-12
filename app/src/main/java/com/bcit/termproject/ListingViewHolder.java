package com.bcit.termproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;

/**
 * Holder class for listing class to recycler
 */
public class ListingViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView;
    public TextView descTextView;
    public ImageView imageBookmark;
    public ChipGroup tagChipGroup;
    public ImageView imgPicture;

    /**
     * Initialize a new ListingViewHolder object with the required layout views, according to an item view.
     * @param itemView a View
     */
    public ListingViewHolder(@NonNull View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.textView_listing_name);
        descTextView = itemView.findViewById(R.id.textView_listing_description);
        imageBookmark = itemView.findViewById(R.id.imageView_listing_bookmark);
        tagChipGroup = itemView.findViewById(R.id.chipGroup_listing_tags);
        imgPicture = itemView.findViewById(R.id.imageView_listing_scholarship);

    }
}

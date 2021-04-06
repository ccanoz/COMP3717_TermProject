package com.bcit.termproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListingViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView;
    public TextView descTextView;
    public TextView tag1TextView;
    public TextView tag2TextView;
    public ImageView imageBookmark;

    public ListingViewHolder(@NonNull View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.textView_listing_name);
        descTextView = itemView.findViewById(R.id.textView_listing_description);
        tag1TextView = itemView.findViewById(R.id.textView_listing_tag1);
        tag2TextView = itemView.findViewById(R.id.textView_listing_tag2);
        imageBookmark = itemView.findViewById(R.id.imageView_scholBookmark2);

    }
}

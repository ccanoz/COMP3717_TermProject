package com.bcit.termproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingViewHolder> {

    Context parentContext;

    private List<Listing> listings;

    private OnAdapterItemListener onAdapterItemListener;
    public void setOnAdapterItemListener(OnAdapterItemListener onAdapterItemListener) {
        this.onAdapterItemListener = onAdapterItemListener;
    }


    public ListingAdapter(List<Listing> listings) {
        this.listings = listings;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        View listingView = inflater.inflate(R.layout.item_listing, parent, false);
        ListingViewHolder viewHolder = new ListingViewHolder(listingView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Listing listing = listings.get(position);

        ImageView imgPicture = holder.imgPicture;
        if (listing.getPictureUrl() != null) {
            new ImageDownloaderTask(imgPicture).execute(listing.getPictureUrl());
        }

        TextView textViewScholarshipName = holder.nameTextView;
        textViewScholarshipName.setText(listing.getScholarshipName());

        TextView textViewListingDesc = holder.descTextView;
        textViewListingDesc.setText(listing.getDescription());

        ChipGroup tagChipGroup = holder.tagChipGroup;

        ImageView imageBookmarkView = holder.imageBookmark;
        imageBookmarkView.setImageResource(R.drawable.ic_bookmark_add);

        for (String tag: listing.getTags()) {
            Chip tagChip = new Chip(parentContext);
            tagChip.setText(tag);
            tagChipGroup.addView(tagChip);
        }

        textViewScholarshipName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdapterItemListener.OnLongClick(listing);
            }
        });
        textViewListingDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdapterItemListener.OnLongClick(listing);
            }
        });
        imageBookmarkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdapterItemListener.OnMarkClick(listing);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

}

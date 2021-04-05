package com.bcit.termproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingViewHolder> {
    private List<Listing> listings;

    public ListingAdapter(List<Listing> listings) {
        this.listings = listings;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listingView = inflater.inflate(R.layout.item_listing, parent, false);

        ListingViewHolder viewHolder = new ListingViewHolder(listingView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Listing listing = listings.get(position);

        TextView textViewScholarshipName = holder.nameTextView;
        textViewScholarshipName.setText(listing.getScholarshipName());

        TextView textViewListingDesc = holder.descTextView;
        textViewListingDesc.setText(listing.getDescription());

        TextView textViewListingTag1 = holder.tag1TextView;
        textViewListingTag1.setText(listing.getTag1());

        TextView textViewListingTag2 = holder.tag2TextView;
        textViewListingTag2.setText(listing.getTag2());

//        ImageView bookmark = holder.image;
//        bookmark.setImageResource(R.drawable.ic_bookmark_add);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }
}

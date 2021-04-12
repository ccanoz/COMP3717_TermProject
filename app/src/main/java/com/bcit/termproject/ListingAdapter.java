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

    /**
     * Setter for the onAdapterItemListener field.
     *
     * @param onAdapterItemListener an object that implements the OnAdapterItemListener interface.
     */
    public void setOnAdapterItemListener(OnAdapterItemListener onAdapterItemListener) {
        this.onAdapterItemListener = onAdapterItemListener;
    }


    /**
     * Initialize a new OnAdapterItemListener.
     *
     * @param listings a list of Listing objects
     */
    public ListingAdapter(List<Listing> listings) {
        this.listings = listings;
    }

    /**
     * Inflates the layout file to be used in the RecyclerView.
     *
     * @param parent   a ViewGroup
     * @param viewType an int
     * @return a AccountViewHolder
     */
    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        View listingView = inflater.inflate(R.layout.item_listing, parent, false);
        ListingViewHolder viewHolder = new ListingViewHolder(listingView);
        return viewHolder;
    }

    /**
     * Sets up the views for each item in the listings list.
     *
     * @param holder   the AccountViewHolder
     * @param position int, index of item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Listing listing = listings.get(position);

        ImageView imgPicture = holder.imgPicture;
        if (listing.getPictureUrl() != null) {
            new ImageDownloaderTask(imgPicture).execute(listing.getPictureUrl());
        }

        setViews(listing, holder);


    }

    /**
     * Helper method to set the Views according to a listing and view holder.
     * @param listing a Listing
     * @param holder a ListingViewHolder
     */
    private void setViews(Listing listing, ListingViewHolder holder){
        TextView textViewScholarshipName = holder.nameTextView;
        textViewScholarshipName.setText(listing.getScholarshipName());

        TextView textViewListingDesc = holder.descTextView;
        textViewListingDesc.setText(listing.getDescription());

        ChipGroup tagChipGroup = holder.tagChipGroup;

        ImageView imageBookmarkView = holder.imageBookmark;
        imageBookmarkView.setImageResource(R.drawable.ic_bookmark_add);

        setBookmarkIcon(imageBookmarkView, listing);

        tagChipGroup.removeAllViews();
        for (String tag : listing.getTags()) {
            Chip tagChip = new Chip(parentContext);
            tagChip.setText(tag);
            tagChipGroup.addView(tagChip);
        }

        textViewScholarshipName.setOnClickListener(v -> onAdapterItemListener.OnLongClick(listing));

        textViewListingDesc.setOnClickListener(v -> onAdapterItemListener.OnLongClick(listing));

        imageBookmarkView.setOnClickListener(v -> {
            onAdapterItemListener.OnMarkClick(listing);
            setBookmarkIcon(imageBookmarkView, listing);
        });
    }

    /**
     * Sets the bookmark icon depending on the state of each listing (bookmarked or not by the user)
     *
     * @param bookmarkIcon an ImageView, the bookmark image in the listing
     * @param listing      a Listing object
     */
    public void setBookmarkIcon(ImageView bookmarkIcon, Listing listing) {
        int iconId = listing.getIsBookmarked() ? R.drawable.ic_bookmark_added : R.drawable.ic_bookmark_add;
        bookmarkIcon.setImageResource(iconId);
    }

    /**
     * Returns the size of the listings list.
     *
     * @return an int
     */
    @Override
    public int getItemCount() {
        return listings.size();
    }

}

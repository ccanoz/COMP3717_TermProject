package com.bcit.termproject;

public interface OnAdapterItemListener {
    //On click for listing title and description
    void OnLongClick(Listing listing);
    //On click for listing bookmark
    void OnMarkClick(Listing listing);
}

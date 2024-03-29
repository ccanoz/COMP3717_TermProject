package com.bcit.termproject;

import java.util.ArrayList;

/**
 * Listing class for all the information in a scholarship listing
 */
public class Listing {
    private String name;
    private String about;
    private String key;
    private ArrayList<String> tags = new ArrayList<>();
    private String pictureUrl;
    private Boolean isBookmarked;

    public Listing(String name, String about, String key, ArrayList<String> tags, String pictureUrl) {
        this.name = name;
        this.about = about;
        this.key = key;
        this.tags = tags;
        this.pictureUrl = pictureUrl;
        this.isBookmarked = false;
    }

    public boolean getIsBookmarked() {return isBookmarked;}

    public void setIsBookmarked(boolean bookmarked) {
        this.isBookmarked = bookmarked;
    }

    public String getScholarshipName() {return name;}

    public String getDescription() {return about;}

    public String getKey() {return key;}

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getPictureUrl() { return pictureUrl; }

}

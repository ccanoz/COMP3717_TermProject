package com.bcit.termproject;

import java.util.ArrayList;

public class Listing {
    private String name;
    private String about;
    private String tag1;
    private String tag2;
    private String key;
    private ArrayList<String> tags = new ArrayList<>();

    public Listing(String name, String about, String key, ArrayList<String> tags) {
        this.name = name;
        this.about = about;
        this.key = key;
        this.tags = tags;
    }

    public String getScholarshipName() {return name;}

    public String getDescription() {return about;}

    public String getKey() {return key;}

    public ArrayList<String> getTags() {
        return tags;
    }

}

package com.bcit.termproject;

public class Listing {
    private String scholName;
    private String description;
    private String tag1;
    private String tag2;

    public Listing(String scholName, String description, String tag1, String tag2) {
        this.scholName = scholName;
        this.description = description;
        this.tag1 = tag1;
        this.tag2 = tag2;
    }

    public String getScholarshipName() {return scholName;}

    public String getDescription() {return description;}

    public String getTag1() {return tag1;}

    public String getTag2() {return tag2;}

}

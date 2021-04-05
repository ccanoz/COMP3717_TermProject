package com.bcit.termproject;

public class Listing {
    private String name;
    private String about;
    private String tag1;
    private String tag2;

    public Listing(String name, String about, String tag1, String tag2) {
        this.name = name;
        this.about = about;
        this.tag1 = tag1;
        this.tag2 = tag2;
    }

    public String getScholarshipName() {return name;}

    public String getDescription() {return about;}

    public String getTag1() {return tag1;}

    public String getTag2() {return tag2;}

}

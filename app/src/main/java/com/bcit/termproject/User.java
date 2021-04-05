package com.bcit.termproject;

import java.util.ArrayList;

public class User {
    ArrayList<String> bookmarked = new ArrayList<>();
    String name;
    String DOB; //change to Date?
    String gender;
    String yearlyIncome;
    String nationality;
    boolean employed;

    public User(){
        // Default constructor required
    }

    public User(ArrayList<String> bookmarked, String name, String DOB, String gender, String yearlyIncome,
                String nationality, boolean employed){
        this.bookmarked = bookmarked;
        this.name = name;
        this.DOB = DOB;
        this.gender = gender;
        this.yearlyIncome = yearlyIncome;
        this.nationality = nationality;
        this.employed = employed;
    }

    public void setBookmarked(ArrayList<String> bookmarked) {this.bookmarked = bookmarked;}
    public void setName(String name){this.name = name;}
    public void setDOB(String DOB){this.DOB = DOB;}
    public void setGender(String gender){this.gender = gender;}
    public void setYearlyIncome(String yearlyIncome){this.yearlyIncome = yearlyIncome;}
    public void setNationality(String nationality){this.nationality = nationality;}
    public void setEmployed(boolean employed){this.employed = employed;}

    public ArrayList<String> getBookmarked() { return bookmarked; }
    public String getName(){ return name; }
    public String getDOB(){ return DOB; }
    public String getGender(){ return gender; }
    public String getYearlyIncome(){ return yearlyIncome; }
    public String getNationality(){ return nationality; }
    public boolean getEmployed(){ return employed; }

    /**
     * Checks if the scholarship passed in exists in the user's bookmarks.
     * @param scholId
     */
    public boolean checkScholBookmarked(String scholId) {
        return this.bookmarked.contains(scholId);
    }

    /**
     * Add a scholarship to a User's bookmarked list.
     * @param scholId
     */
    public void addToBookmarked(String scholId) {
        if(!this.bookmarked.contains(scholId))
            this.bookmarked.add(scholId);
    }

    /**
     * Remove a scholarship from a User's bookmarked list.
     * @param scholId
     */
    public void unBookmark(String scholId) {
        if(this.bookmarked.contains(scholId))
            this.bookmarked.remove(scholId);
    }

    public String toString(){
        return "name: " + name + ", DOB: " + DOB + ", gender: " + gender + ", yearlyIncome: " +
                "" + yearlyIncome + ", nationality: " + nationality + ", employed: " + employed;
    }
}
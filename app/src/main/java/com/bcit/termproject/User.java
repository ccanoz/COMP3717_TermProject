package com.bcit.termproject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class User {
    ArrayList<String> bookmarked = new ArrayList<>();
    String name;
    String DOB;
    String gender;
    String income;
    String GPA;
    String nationality;
    boolean employed;

    /**
     * Default constructor for the User class. Required to initialize a User through a Firebase User
     */
    public User(){
        // Default constructor required
    }

    /**
     * Initialize a with member variables.
     * @param bookmarked a Array of Strings, the list of the IDs of the Listings that the User has bookmarked
     * @param name a String
     * @param DOB a String
     * @param gender a String
     * @param yearlyIncome a String
     * @param GPA a String
     * @param nationality a String
     * @param employed a boolean
     */
    public User(ArrayList<String> bookmarked, String name, String DOB, String gender, String yearlyIncome, String GPA,
                String nationality, boolean employed){
        this.bookmarked = bookmarked;
        this.name = name;
        this.DOB = DOB;
        this.gender = gender;
        this.income = yearlyIncome;
        this.GPA = GPA;
        this.nationality = nationality;
        this.employed = employed;
    }

    // setters
    public void setBookmarked(ArrayList<String> bookmarked) {this.bookmarked = bookmarked;}
    public void setName(String name){this.name = name;}
    public void setDOB(String DOB){this.DOB = DOB;}
    public void setGender(String gender){this.gender = gender;}
    public void setIncome(String income){this.income = income;}
    public void setGPA(String GPA){this.GPA = GPA;}
    public void setNationality(String nationality){this.nationality = nationality;}
    public void setEmployed(boolean employed){this.employed = employed;}

    // getters
    public ArrayList<String> getBookmarked() { return bookmarked; }
    public String getName(){ return name; }
    public String getDOB(){ return DOB; }
    public String getGender(){ return gender; }
    public String getIncome(){ return income; }
    public String getGPA(){ return GPA; }
    public String getNationality(){ return nationality; }
    public boolean getEmployed(){ return employed; }

    /**
     * Checks if the scholarship passed in exists in the user's bookmarks.
     * @param scholId the scholarship
     */
    public boolean checkScholBookmarked(String scholId) {
        return this.bookmarked.contains(scholId);
    }

    /**
     * Add a scholarship to a User's bookmarked list.
     * @param scholId the scholarship id
     */
    public void addToBookmarked(String scholId) {
        if(!this.bookmarked.contains(scholId))
            this.bookmarked.add(scholId);
    }

    /**
     * Remove a scholarship from a User's bookmarked list.
     * @param scholId the scholarship id
     */
    public void unBookmark(String scholId) {
        if(this.bookmarked.contains(scholId))
            this.bookmarked.remove(scholId);
    }

    /**
     * Returns a nicely formatted String that contains information about the User.
     * For debugging purposes.
     * @return a String
     */
    @NotNull
    public String toString(){
        return "name: " + name + ", DOB: " + DOB + ", gender: " + gender + ", yearlyIncome: " +
                "" + income + ", nationality: " + nationality + ", employed: " + employed;
    }
}
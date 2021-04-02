package com.bcit.termproject;

public class User {
    String name;
    String DOB; //change to Date?
    String gender;
    String yearlyIncome;
    String nationality;
    boolean employed;

    public User(){
        // Default constructor required
    }

    public void setName(String name){this.name = name;}
    public void setDOB(String DOB){this.DOB = DOB;}
    public void setGender(String gender){this.gender = gender;}
    public void setYearlyIncome(String yearlyIncome){this.yearlyIncome = yearlyIncome;}
    public void setNationality(String nationality){this.nationality = nationality;}
    public void setEmployed(boolean employed){this.employed = employed;}

    public String getName(){ return name; }
    public String getDOB(){ return DOB; }
    public String getGender(){ return gender; }
    public String getYearlyIncome(){ return yearlyIncome; }
    public String getNationality(){ return nationality; }
    public boolean isEmployed(){ return employed; }

    public String toString(){
        return "name: " + name + ", DOB: " + DOB + ", gender: " + gender + ", yearlyIncome: " +
                "" + yearlyIncome + ", nationality: " + nationality + ", employed: " + employed;
    }
}
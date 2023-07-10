package com.example.home;

public class ModelUsers {

    //getting users information from the database firebase
    private String name, userType, profileImage;

    public ModelUsers() {
    }

    public ModelUsers(String name, String userType, String profileImage) {
        this.name = name;
        this.userType = userType;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

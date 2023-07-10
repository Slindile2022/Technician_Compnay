package com.example.home;

public class Users {




    private String email;
    private String phone;
    private String name;
    private  String Uid;
    private String password;
    private String userType;
    private String address;
    private String profileImage;



    public Users() {

    }

    public Users(String uid, String phone, String email, String name,String password, String userType, String address, String profileImage) {
        this.Uid = uid;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.userType = userType;
        this.address = address;
        this.profileImage = profileImage;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }







}

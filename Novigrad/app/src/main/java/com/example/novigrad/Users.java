package com.example.novigrad;

import com.google.firebase.firestore.Exclude;

public class Users {
    private String userId;
    private String email;
    private String firstName;
    private String lastname;
    private String password;
    private String phone;
    private String type;
    private String BranchEmail;
    public Users() {
        //public no-arg constructor needed
    }
    @Exclude
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Users(String email, String firstName, String lastName, String password, String phone, String type) {
        this.email = email;
        this.firstName = firstName;
        this.lastname = lastName;
        this.password = password;
        this.phone = phone;
        this.type = type;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastname;
    }
    public String getPassword() {
        return password;
    }
    public String getPhone() {
        return phone;
    }
    public String getType() {
        return type;
    }
    public String getBranchEmail() {
        return BranchEmail;
    }

}

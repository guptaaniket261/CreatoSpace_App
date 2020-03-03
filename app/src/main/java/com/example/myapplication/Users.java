package com.example.myapplication;

import android.net.Uri;

public class Users {
    private String id;
    private String name;
    private String mail;
    private String phone;
    private String dob;
    private String qualification;
    private String experience;
    private String skills;
    private String profileImage;
    private String gender;

    public Users() {
    }

    Users(String id, String name, String email, String phone, String dob, String profileImage, String qualification, String experience, String skills, String gender) {
        this.id = id;
        this.name = name;
        this.mail = email;
        this.phone = phone;
        this.dob = dob;
        this.profileImage = profileImage;
        this.qualification = qualification;
        this.experience = experience;
        this.skills = skills;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public String getDob() {
        return dob;
    }

    public String getQualification() {
        return qualification;
    }

    public String getExperience() {
        return experience;
    }

    public String getSkills() {
        return skills;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getGender() {
        return gender;
    }
}
package com.example.myapplication.Models;

public class cards {
    private String userId;
    private String name;
    private String age;
    private String qualification;
    private String skills;
    private String experience;

    private String profileImageUrl;

    public String getAge() {
        return age;
    }

    public String getQualification() {
        return qualification;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getSkills() {
        return skills;
    }

    public String getExperience() {
        return experience;
    }

    public cards(String userid, String name, String profileImageUrl, String age, String qualification, String skills, String experience)
    {
        this.userId=userid;
        this.name=name;
        this.profileImageUrl=profileImageUrl;
        this.age = age;
        this.qualification = qualification;
        this.skills = skills;
        this.experience = experience;
    }
    public String getUserId()
    {
        return userId;
    }
    public void setUserId(String userid)
    {
        this.userId=userid;
    }
    public String getName()
    {
        return name;
    }
    public void setName()
    {
        this.name=name;
    }
    public String getProfileImageUrl()
    {
        return profileImageUrl;
    }
    public void setprofileImageUrl()
    {
        this.profileImageUrl=profileImageUrl;
    }
}

package com.example.myapplication;

public class MatchesObject {
    private String userId;
    private String name;
    private String profileImageUrl;


    public MatchesObject(String userId, String name, String profileImageUrl)
    {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
    public String getUserId()
    {
        return this.userId;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

}

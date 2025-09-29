package com.example.Social.profile.dto;
import lombok.*;


@Data

public class fetchProfile {

    private String UserId;

    private String Username;

    private String Bio;

    private String Email;

    private String ProfilePicUrl;

    private Boolean PrivateAcc;

    private  int FollowerCount;

    private  int FollowingCount;

    private  int FriendsCount;



}


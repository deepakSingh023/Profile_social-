package com.example.Social.profile.dto;
import lombok.*;


@Data
@AllArgsConstructor
public class fetchProfile {

    private String userId;

    private String username;

    private String bio;

    private String email;

    private String profilePicUrl;

    private Boolean privateAcc;

    private  int followerCount;

    private  int followingCount;

    private  int friendsCount;



}


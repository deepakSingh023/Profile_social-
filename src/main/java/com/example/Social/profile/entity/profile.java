package com.example.Social.profile.entity;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;





@Document(collection = "profile")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class profile {

    @Id
    private String Id;

    private String UserId;

    private String Username;

    private String Email;

    @Builder.Default
    private String ProfilePicUrl="";

    @Builder.Default
    private String Bio="";

    @Builder.Default
    private Boolean PrivateAcc=false;


    @Builder.Default
    private int FollowerCount=0;


    @Builder.Default
    private int FollowingCount=0;

    @Builder.Default
    private int FriendsCount=0;

}

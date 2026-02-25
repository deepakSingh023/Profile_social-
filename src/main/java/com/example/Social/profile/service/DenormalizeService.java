package com.example.Social.profile.service;


import com.example.Social.profile.dto.DenormalizeDto;
import com.example.Social.profile.tasks.PostClient;
import com.example.Social.profile.tasks.ReelClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class DenormalizeService {

    private final PostClient postClient;
    private final ReelClient reelClient;

    @Value("${secret.service}")
    private String secret;

    public void denormalize(DenormalizeDto data){

        postClient.denormalizePost(data,secret);

        reelClient.denormalize(data,secret);

    }
}

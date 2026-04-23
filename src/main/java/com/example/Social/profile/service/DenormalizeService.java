package com.example.Social.profile.service;


import com.example.Social.profile.dto.DenormalizeDto;
import com.example.Social.profile.tasks.CommentsClient;
import com.example.Social.profile.tasks.InteractionClient;
import com.example.Social.profile.tasks.PostClient;
import com.example.Social.profile.tasks.ReelClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class DenormalizeService {

    private final PostClient postClient;
    private final ReelClient reelClient;
    private final InteractionClient interactionClient;
    private final R2ImageService r2ImageService;
    private final CommentsClient commentsClient;

    private static final Logger log = LoggerFactory.getLogger(DenormalizeService.class);

    @Value("${secret.service}")
    private String secret;


    @Async("denormalize")
    public void denormalize(DenormalizeDto data){
        try {
            postClient.denormalizePost(data, secret);
            reelClient.denormalize(data, secret);
            interactionClient.denormalize(data, secret);
            commentsClient.denormalizePost(data,secret);
        } catch (Exception ex) {
            log.error("denormalize failed userId={}",data.userId(), ex);
        }
    }

    @Async("denormalize")
    public void deleteOldImageAsync(String oldUrl) {
        try {
            r2ImageService.deleteImage(oldUrl);
        } catch (Exception e) {
            log.warn("delete old image failed oldUrl={}", oldUrl, e);
        }
    }
}

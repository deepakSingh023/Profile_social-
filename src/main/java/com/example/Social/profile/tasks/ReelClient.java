package com.example.Social.profile.tasks;


import com.example.Social.profile.dto.DenormalizeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="denormalize", url="${reel.uri}")
public interface ReelClient {

    @PutMapping("/api/reel/denormalize")
    void denormalize(
            @RequestBody DenormalizeDto data,
            @RequestHeader("X-SECRET-TOKEN") String token
    );
}

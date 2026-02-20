package com.example.Social.profile.tasks;


import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@org.springframework.cloud.openfeign.FeignClient(name="increment", url="")
public interface FeignClient {

    @PutMapping("/api/denormalize/image")
    void denormalize(
            @RequestBody String profileImg,
            @RequestHeader("Authorization") String token
    );
}

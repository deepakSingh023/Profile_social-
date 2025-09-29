package com.example.Social.profile.repository;

import com.example.Social.profile.entity.profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ProfileRepository extends MongoRepository<profile, String> {

    Optional<profile> findByUserId(String userId);

}

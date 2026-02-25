package com.example.Social.profile.service;
import com.example.Social.profile.entity.profile;
import com.example.Social.profile.enums.CounterType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CounterService {

    private final MongoTemplate mongoTemplate;


    public void updateCounter(String userId, CounterType type, int delta) {

        if (delta != 1 && delta != -1) {
            throw new IllegalArgumentException("Delta must be 1 or -1");
        }

        Query query = Query.query(Criteria.where("userId").is(userId));

        // Only protect decrement
        if (delta == -1) {
            query.addCriteria(
                    Criteria.where(type.getField()).gt(0)
            );
        }

        Update update = new Update().inc(type.getField(), delta);

        var result = mongoTemplate.updateFirst(query, update, profile.class);

        if (result.getMatchedCount() == 0) {
            throw new IllegalStateException("Profile not found or invalid decrement");
        }
    }

}

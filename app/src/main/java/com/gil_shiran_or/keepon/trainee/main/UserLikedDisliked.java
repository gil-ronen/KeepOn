package com.gil_shiran_or.keepon.trainee.main;

import java.util.HashMap;
import java.util.Map;

public class UserLikedDisliked {

    private String userId;

    public UserLikedDisliked() {}

    public UserLikedDisliked(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("userId", userId);

        return result;
    }
}

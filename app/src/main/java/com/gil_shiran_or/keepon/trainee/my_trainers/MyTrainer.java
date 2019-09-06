package com.gil_shiran_or.keepon.trainee.my_trainers;

import java.util.HashMap;
import java.util.Map;

public class MyTrainer {

    private String userId;
    private boolean isRated = false;

    public MyTrainer() {}

    public MyTrainer(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getIsRated() {
        return isRated;
    }

    public void setIsRated(boolean rated) {
        isRated = rated;
    }

    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("userId", userId);
        result.put("isRated", isRated);

        return result;
    }
}

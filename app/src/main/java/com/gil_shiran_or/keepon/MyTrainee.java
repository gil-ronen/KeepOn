package com.gil_shiran_or.keepon;

import java.util.HashMap;
import java.util.Map;

public class MyTrainee {

    private String userId;

    public MyTrainee() {}

    public MyTrainee(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("userId", userId);

        return result;
    }
}

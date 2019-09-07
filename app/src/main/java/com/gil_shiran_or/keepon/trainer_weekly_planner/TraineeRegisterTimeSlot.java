package com.gil_shiran_or.keepon.trainer_weekly_planner;

import java.util.HashMap;
import java.util.Map;

public class TraineeRegisterTimeSlot
{
    private String userId;

    public TraineeRegisterTimeSlot() {}

    public TraineeRegisterTimeSlot(String userId) {
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


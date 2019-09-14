package com.gil_shiran_or.keepon.trainings_weekly_schedule;

import java.util.HashMap;
import java.util.Map;

public class TraineeRegisterTimeSlot
{
    private String userId;
    private boolean isGotScore = false;

    public TraineeRegisterTimeSlot() {}

    public TraineeRegisterTimeSlot(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getIsGotScore() {
        return isGotScore;
    }

    public void setIsGotScore(boolean gotScore) {
        isGotScore = gotScore;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("userId", userId);
        result.put("isGotScore", isGotScore);

        return result;
    }
}


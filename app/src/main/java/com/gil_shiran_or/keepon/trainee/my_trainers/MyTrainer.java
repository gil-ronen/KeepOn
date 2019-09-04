package com.gil_shiran_or.keepon.trainee.my_trainers;

public class MyTrainer {

    private String userId;
    private boolean isRated = false;

    public MyTrainer() {}

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
}

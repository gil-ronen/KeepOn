package com.gil_shiran_or.keepon.trainee.my_friends;

import java.util.HashMap;
import java.util.Map;

public class MyFriend {

    private String userId;
    private boolean isAccepted = false;
    private boolean isSendRequest = false;

    public MyFriend() {}

    public MyFriend(String userId, boolean isSendRequest) {
        this.userId = userId;
        this.isSendRequest = isSendRequest;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public boolean getIsSendRequest() {
        return isSendRequest;
    }

    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("userId", userId);
        result.put("isAccepted", isAccepted);
        result.put("isSendRequest", isSendRequest);

        return result;
    }
}

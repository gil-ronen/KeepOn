package com.gil_shiran_or.keepon.trainee.main;

import java.util.HashMap;
import java.util.Map;

public class Reply {

    private String replyId;
    private String userId;
    private String date;
    private String body;
    private int likes = 0;
    private int dislikes = 0;
    private boolean isLiked = false;
    private boolean isDisliked = false;

    public Reply() {}

    public Reply(String userId, String date, String body) {
        this.userId = userId;
        this.date = date;
        this.body = body;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean getIsDisliked() {
        return isDisliked;
    }

    public void setIsDisliked(boolean isDisliked) {
        this.isDisliked = isDisliked;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("userId", userId);
        result.put("date", date);
        result.put("body", body);
        result.put("likes", likes);
        result.put("dislikes", dislikes);

        return result;
    }
}

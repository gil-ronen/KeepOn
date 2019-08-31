package com.gil_shiran_or.keepon.trainee.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reply {

    private String replyId;
    private String userId;
    private String date;
    private String body;
    private int likes = 0;
    private int dislikes = 0;
    private List<UserLikedDisliked> usersLiked = new ArrayList<>();
    private List<UserLikedDisliked> usersDisliked = new ArrayList<>();

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

    public void addUserToUsersLiked(String userId) {
        usersLiked.add(new UserLikedDisliked(userId));
    }

    public void addUserToUsersDisliked(String userId) {
        usersDisliked.add(new UserLikedDisliked(userId));
    }

    public boolean isUsersLikedContainsUserId(String userId) {
        for (UserLikedDisliked userLiked : usersLiked) {
            if (userLiked.getUserId().equals(userId)) {
                return true;
            }
        }

        return false;
    }

    public boolean isUsersDislikedContainsUserId(String userId) {
        for (UserLikedDisliked userDisliked : usersDisliked) {
            if (userDisliked.getUserId().equals(userId)) {
                return true;
            }
        }

        return false;
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

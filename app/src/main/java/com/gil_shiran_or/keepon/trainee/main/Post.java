package com.gil_shiran_or.keepon.trainee.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {

    private String postId;
    private String userId;
    private String date;
    private String title;
    private String body;
    private int likes = 0;
    private int dislikes = 0;
    private List<UserLikedDisliked> usersLiked = new ArrayList<>();
    private List<UserLikedDisliked> usersDisliked = new ArrayList<>();

    public Post() {}

    public Post(String userId, String date, String title, String body) {
        this.userId = userId;
        this.date = date;
        this.title = title;
        this.body = body;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
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
        result.put("title", title);
        result.put("body", body);
        result.put("likes", likes);
        result.put("dislikes", dislikes);

        return result;
    }
}

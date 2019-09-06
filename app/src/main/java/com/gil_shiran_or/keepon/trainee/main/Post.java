package com.gil_shiran_or.keepon.trainee.main;

import java.util.HashMap;
import java.util.Map;

public class Post {

    private String postId;
    private String userId;
    private String date;
    private String title;
    private String body;
    private int likes = 0;
    private int dislikes = 0;
    private boolean isLiked = false;
    private boolean isDisliked = false;

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
        result.put("title", title);
        result.put("body", body);
        result.put("likes", likes);
        result.put("dislikes", dislikes);

        return result;
    }
}

package com.gil_shiran_or.keepon.trainee.my_trainers;

import java.util.HashMap;
import java.util.Map;

public class Review {

    private float rating;
    private String review;

    public Review() {

    }

    public Review(float rating, String review) {
        this.rating = rating;
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("rating", rating);
        result.put("review", review);

        return result;
    }
}

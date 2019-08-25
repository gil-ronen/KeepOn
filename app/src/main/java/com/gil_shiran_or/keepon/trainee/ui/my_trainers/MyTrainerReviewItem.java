package com.gil_shiran_or.keepon.trainee.ui.my_trainers;

public class MyTrainerReviewItem {

    private float rating;
    private String review;

    public MyTrainerReviewItem(float rating, String review) {
        this.rating = rating;
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}

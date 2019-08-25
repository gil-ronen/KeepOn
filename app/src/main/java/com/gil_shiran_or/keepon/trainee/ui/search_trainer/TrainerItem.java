package com.gil_shiran_or.keepon.trainee.ui.search_trainer;

import java.util.Date;

public class TrainerItem {

    private String trainerName;
    private String trainerCity;
    private String trainerGym;
    private int trainerPrice;
    private float trainerRating;
    private int trainerNumOfReviewers;
    private Date signupDate;
    private String id;

    public TrainerItem(String trainerName, String trainerCity, String trainerGym, int trainerPrice, float trainerRating, int trainerNumOfReviewers) {
        this.trainerName = trainerName;
        this.trainerCity = trainerCity;
        this.trainerGym = trainerGym;
        this.trainerPrice = trainerPrice;
        this.trainerRating = trainerRating;
        this.trainerNumOfReviewers = trainerNumOfReviewers;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public String getTrainerCity() {
        return trainerCity;
    }

    public String getTrainerGym() {
        return trainerGym;
    }

    public int getTrainerPrice() {
        return trainerPrice;
    }

    public float getTrainerRating() {
        return trainerRating;
    }

    public int getTrainerNumOfReviewers() {
        return trainerNumOfReviewers;
    }
}

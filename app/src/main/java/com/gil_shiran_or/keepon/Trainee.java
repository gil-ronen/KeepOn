package com.gil_shiran_or.keepon;

import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Trainee extends User implements Serializable
{
    private String weight;
    private String height;
    private String residence;
    private int level;
    private int total_score;
    private int score_to_next_level;

    public Trainee() {
    }

    public Trainee(String userId, String userType, String fullname, String username, String email, String password, String phoneNumber, String birthDate, String gender, String aboutMe, String profilePhotoUri, String weight, String height, String residence, int level, int total_score, int score_to_next_level) {
        super(userId, userType, fullname, username, email, password, phoneNumber, birthDate, gender, aboutMe, profilePhotoUri);
        this.weight = weight;
        this.height = height;
        this.residence = residence;
        this.level = level;
        this.total_score = total_score;
        this.score_to_next_level = score_to_next_level;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public int getScore_to_next_level() {
        return score_to_next_level;
    }

    public void setScore_to_next_level(int score_to_next_level) {
        this.score_to_next_level = score_to_next_level;
    }
}

package com.gil_shiran_or.keepon;

import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Trainee extends User implements Serializable
{
    private String weight;
    private String height;
    private String residence;

    public Trainee() {
    }

    public Trainee(String userId, String userType, String fullname, String username, String email, String password, String phoneNumber, String birthDate, String gender, String aboutMe, String profilePhotoUri, String weight, String height, String residence) {
        super(userId, userType, fullname, username, email, password, phoneNumber, birthDate, gender, aboutMe, profilePhotoUri);
        this.weight = weight;
        this.height = height;
        this.residence = residence;
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
}

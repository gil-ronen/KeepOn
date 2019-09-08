package com.gil_shiran_or.keepon.db;

import java.util.HashMap;
import java.util.Map;

public class Trainer extends User
{
    private String companyName;
    private String aboutMe;
    private String trainingCity;
    private String trainingStreet;
    private int price;
    private Rating rating;

    public Trainer() {}

    public Trainer(String name, String email, String password, String phoneNumber, String birthDate, String gender, String profilePhotoUrl,
                   String companyName, String aboutMe, String trainingCity, String trainingStreet, int price, Rating rating) {
        super(name, email, password, phoneNumber, birthDate, gender, profilePhotoUrl);
        this.companyName = companyName;
        this.aboutMe = aboutMe;
        this.trainingCity = trainingCity;
        this.trainingStreet = trainingStreet;
        this.price = price;
        this.rating = rating;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getTrainingCity() {
        return trainingCity;
    }

    public void setTrainingCity(String trainingCity) {
        this.trainingCity = trainingCity;
    }

    public String getTrainingStreet() {
        return trainingStreet;
    }

    public void setTrainingStreet(String trainingStreet) {
        this.trainingStreet = trainingStreet;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", getName());
        result.put("email", getEmail());
        result.put("phoneNumber", getPhoneNumber());
        result.put("birthDate", getBirthDate());
        result.put("gender", getGender());
        result.put("profilePhotoUrl", getProfilePhotoUrl());
        result.put("companyName", companyName);
        result.put("aboutMe", aboutMe);
        result.put("trainingCity", trainingCity);
        result.put("trainingStreet", trainingStreet);
        result.put("price", price);

        return result;
    }

}

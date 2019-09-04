package com.gil_shiran_or.keepon;

public class Trainer extends User
{
    private String companyName;
    private String aboutMe;
    private String trainingCity;
    private String trainingStreet;
    private int price;


    public Trainer() {}

    public Trainer(String name, String email, String password, String phoneNumber, String birthDate, String gender, String profilePhotoUrl, String companyName, String aboutMe, String trainingCity, String trainingStreet, int price) {
        super(name, email, password, phoneNumber, birthDate, gender, profilePhotoUrl);
        this.companyName = companyName;
        this.aboutMe = aboutMe;
        this.trainingCity = trainingCity;
        this.trainingStreet = trainingStreet;
        this.price = price;
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

}

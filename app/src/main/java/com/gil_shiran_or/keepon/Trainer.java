package com.gil_shiran_or.keepon;

public class Trainer extends User
{
    private String companyName;
    private String price;
    private String trainingPlaceAddress;

    public Trainer() {}

    public Trainer(String userId, String userType, String fullname, String username, String email, String password, String phoneNumber, String birthDate, String gender, String aboutMe, String profilePhotoUri, String companyName, String price, String trainingPlaceAddress) {
        super(userId, userType, fullname, username, email, password, phoneNumber, birthDate, gender, aboutMe, profilePhotoUri);
        this.companyName = companyName;
        this.price = price;
        this.trainingPlaceAddress = trainingPlaceAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTrainingPlaceAddress() {
        return trainingPlaceAddress;
    }

    public void setTrainingPlaceAddress(String trainingPlaceAddress) {
        this.trainingPlaceAddress = trainingPlaceAddress;
    }
}

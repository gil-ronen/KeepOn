package com.gil_shiran_or.keepon;


public class Trainee extends User
{
    private String city;
    private String street;

    public Trainee() {
    }

    public Trainee(String name, String email, String password, String phoneNumber, String birthDate, String gender, String profilePhotoUrl, String city, String street) {
        super(name, email, password, phoneNumber, birthDate, gender, profilePhotoUrl);
        this.city = city;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}

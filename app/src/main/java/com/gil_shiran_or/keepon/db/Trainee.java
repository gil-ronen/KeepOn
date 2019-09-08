package com.gil_shiran_or.keepon.db;


import java.util.HashMap;
import java.util.Map;

public class Trainee extends User
{
    private String city;
    private String street;
    private Status status;

    public Trainee() {
    }

    public Trainee(String name, String email, String password, String phoneNumber, String birthDate, String gender, String profilePhotoUrl, String city, String street, Status status) {
        super(name, email, password, phoneNumber, birthDate, gender, profilePhotoUrl);
        this.city = city;
        this.street = street;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", getName());
        result.put("email", getEmail());
        result.put("phoneNumber", getPhoneNumber());
        result.put("birthDate", getBirthDate());
        result.put("gender", getGender());
        result.put("profilePhotoUrl", getProfilePhotoUrl());
        result.put("city", city);
        result.put("street", street);

        return result;
    }
}

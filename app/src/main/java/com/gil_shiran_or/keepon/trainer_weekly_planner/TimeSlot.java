package com.gil_shiran_or.keepon.trainer_weekly_planner;

public class TimeSlot {

    private String title;
    private String dateAndTime;
    private String description;
    private String key;

    public TimeSlot() {
    }

    public TimeSlot(String title, String dateAndTime, String description, String key) {
        this.title = title;
        this.dateAndTime = dateAndTime;
        this.description = description;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

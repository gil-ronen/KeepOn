package com.gil_shiran_or.keepon.trainer_weekly_planner;

public class TimeSlot {

    private String timeSlotId;
    private String title;
    private String description;
    private String timeFrom;
    private String timeUntil;
    private String day;
    private String traineeId;
    private String trainerId;
    private int groupLimit;
    private int currentSumPeopleInGroup;


    private boolean isOccupied;
    private boolean isGroupSession;


    public TimeSlot() {
    }


    public TimeSlot(String title, String description, String timeFrom, String timeUntil, String day, String traineeId, String trainerId, int groupLimit, int currentSumPeopleInGroup, boolean isOccupied, boolean isGroupSession) {
        this.title = title;
        this.description = description;
        this.timeFrom = timeFrom;
        this.timeUntil = timeUntil;
        this.day = day;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.groupLimit = groupLimit;
        this.currentSumPeopleInGroup = currentSumPeopleInGroup;
        this.isOccupied = isOccupied;
        this.isGroupSession = isGroupSession;
    }

    public String getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeUntil() {
        return timeUntil;
    }

    public void setTimeUntil(String timeUntil) {
        this.timeUntil = timeUntil;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isGroupSession() {
        return isGroupSession;
    }

    public void setGroupSession(boolean groupSession) {
        isGroupSession = groupSession;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }


    public int getGroupLimit() {
        return groupLimit;
    }

    public void setGroupLimit(int groupLimit) {
        this.groupLimit = groupLimit;
    }

    public int getCurrentSumPeopleInGroup() {
        return currentSumPeopleInGroup;
    }

    public void setCurrentSumPeopleInGroup(int currentSumPeopleInGroup) {
        this.currentSumPeopleInGroup = currentSumPeopleInGroup;
    }
}

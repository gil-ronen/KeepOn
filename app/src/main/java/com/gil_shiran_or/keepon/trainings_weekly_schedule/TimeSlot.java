package com.gil_shiran_or.keepon.trainings_weekly_schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSlot {

    private String timeSlotId;
    private String title;
    private String description;
    private String timeFrom;
    private String timeUntil;
    private String day;
    private String trainerId;
    private int groupLimit;
    private int currentSumPeopleInGroup;
    private boolean groupSession;
    private boolean occupied;
    private List<TraineeRegisterTimeSlot> traineesId = new ArrayList<>();


    public TimeSlot() {
    }

    public TimeSlot(String title, String description, String timeFrom, String timeUntil, String day, String trainerId, int groupLimit, int currentSumPeopleInGroup, boolean groupSession, boolean occupied) {
        this.title = title;
        this.description = description;
        this.timeFrom = timeFrom;
        this.timeUntil = timeUntil;
        this.day = day;
        this.trainerId = trainerId;
        this.groupLimit = groupLimit;
        this.currentSumPeopleInGroup = currentSumPeopleInGroup;
        this.groupSession = groupSession;
        this.occupied = occupied;
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

    public boolean isGroupSession() {
        return groupSession;
    }

    public void setGroupSession(boolean groupSession) {
        this.groupSession = groupSession;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }


    public void addTraineeToTrainerTimeSlots(String traineeId) {
        traineesId.add(new TraineeRegisterTimeSlot(traineeId));
    }


    public void removeTraineeFromTrainerTimeSlots(String traineeId) {
        for (TraineeRegisterTimeSlot traineeRegisterTimeSlot : traineesId) {
            if (traineeRegisterTimeSlot.getUserId().equals(traineeId)) {
                traineesId.remove(traineeRegisterTimeSlot);
            }
        }

        traineesId.remove(traineeId);
    }

    public void clearTraineesId() {
        traineesId.clear();
    }



    public boolean isTraineesIdListContainsTraineeId(String traineeId) {
        for (TraineeRegisterTimeSlot traineeRegisterTimeSlot : traineesId) {
            if (traineeRegisterTimeSlot.getUserId().equals(traineeId)) {
                return true;
            }
        }

        return false;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("title", title);
        result.put("description", description);
        result.put("timeFrom", timeFrom);
        result.put("timeUntil", timeUntil);
        result.put("day", day);
        result.put("trainerId", trainerId);
        result.put("groupLimit", groupLimit);
        result.put("currentSumPeopleInGroup", currentSumPeopleInGroup);
        result.put("groupSession", groupSession);
        result.put("occupied", occupied);

        return result;
    }

}

package com.gil_shiran_or.keepon.trainee.status;

public class WeeklyTask {

    private String description;
    private int score;

    public WeeklyTask() {

    }

    public WeeklyTask(String description, int score) {
        this.description = description;
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public int getScore() {
        return score;
    }
}

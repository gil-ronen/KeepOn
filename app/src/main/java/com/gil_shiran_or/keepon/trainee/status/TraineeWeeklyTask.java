package com.gil_shiran_or.keepon.trainee.status;

import java.util.HashMap;
import java.util.Map;

public class TraineeWeeklyTask {

    private String taskId;
    private boolean isCompleted = false;
    private int times = 0;
    private int totalTimes;
    private int score;

    public TraineeWeeklyTask() {

    }

    public TraineeWeeklyTask(String taskId, int totalTimes, int score) {
        this.taskId = taskId;
        this.totalTimes = totalTimes;
        this.score = score;
    }

    public String getTaskId() {
        return taskId;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public int getTimes() {
        return times;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public int getScore() {
        return score;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("taskId", taskId);
        result.put("isCompleted", isCompleted);
        result.put("times", times);
        result.put("totalTimes", totalTimes);
        result.put("score", score);

        return result;
    }
}

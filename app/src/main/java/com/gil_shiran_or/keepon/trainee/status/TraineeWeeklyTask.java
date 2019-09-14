package com.gil_shiran_or.keepon.trainee.status;

import java.util.HashMap;
import java.util.Map;

public class TraineeWeeklyTask {

    private String taskId;
    private boolean isCompleted = false;
    private int times = 0;

    public TraineeWeeklyTask() {

    }

    public TraineeWeeklyTask(String taskId) {
        this.taskId = taskId;
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

    public void setTimes(int times) {
        this.times = times;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("taskId", taskId);
        result.put("isCompleted", isCompleted);
        result.put("times", times);

        return result;
    }
}

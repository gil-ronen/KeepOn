package com.gil_shiran_or.keepon.trainee.status;

import java.util.HashMap;
import java.util.Map;

public class TraineeWeeklyTask {

    private String taskId;
    private boolean isCompleted = false;

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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("taskId", taskId);
        result.put("isCompleted", isCompleted);

        return result;
    }
}

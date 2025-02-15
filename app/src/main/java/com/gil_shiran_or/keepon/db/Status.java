package com.gil_shiran_or.keepon.db;

import java.util.HashMap;
import java.util.Map;

public class Status
{
    private int level;
    private int totalScore;
    private int scoreToNextLevel;
    private boolean isRefreshedTasks = false;

    public Status() {
    }

    public Status(int level, int totalScore, int scoreToNextLevel) {
        this.level = level;
        this.totalScore = totalScore;
        this.scoreToNextLevel = scoreToNextLevel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getScoreToNextLevel() {
        return scoreToNextLevel;
    }

    public void setScoreToNextLevel(int scoreToNextLevel) {
        this.scoreToNextLevel = scoreToNextLevel;
    }

    public boolean getIsRefreshedTasks() {
        return isRefreshedTasks;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("level", level);
        result.put("totalScore", totalScore);
        result.put("scoreToNextLevel", scoreToNextLevel);
        result.put("isRefreshedTasks", isRefreshedTasks);

        return result;
    }
}

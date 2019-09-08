package com.gil_shiran_or.keepon.db;

import java.util.HashMap;
import java.util.Map;

public class Status
{
    private String level;
    private int totalScore;
    private int scoreToNextLevel;

    public Status() {
    }

    public Status(String level, int totalScore, int scoreToNextLevel) {
        this.level = level;
        this.totalScore = totalScore;
        this.scoreToNextLevel = scoreToNextLevel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("level", level);
        result.put("totalScore", totalScore);
        result.put("scoreToNextLevel", scoreToNextLevel);

        return result;
    }
}

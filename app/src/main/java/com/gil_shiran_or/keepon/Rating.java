package com.gil_shiran_or.keepon;

public class Rating
{
    private float rating;
    private int sumRatings;
    private int totalRaters;
    private int oneStarRaters;
    private int twoStarsRaters;
    private int threeStarsRaters;
    private int fourStarsRaters;
    private int fiveStarsRaters;

    public Rating() {
    }

    public Rating(float rating, int sumRatings, int totalRaters, int oneStarRaters, int twoStarsRaters, int threeStarsRaters, int fourStarsRaters, int fiveStarsRaters) {
        this.rating = rating;
        this.sumRatings = sumRatings;
        this.totalRaters = totalRaters;
        this.oneStarRaters = oneStarRaters;
        this.twoStarsRaters = twoStarsRaters;
        this.threeStarsRaters = threeStarsRaters;
        this.fourStarsRaters = fourStarsRaters;
        this.fiveStarsRaters = fiveStarsRaters;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getSumRatings() {
        return sumRatings;
    }

    public void setSumRatings(int sumRatings) {
        this.sumRatings = sumRatings;
    }

    public int getTotalRaters() {
        return totalRaters;
    }

    public void setTotalRaters(int totalRaters) {
        this.totalRaters = totalRaters;
    }

    public int getOneStarRaters() {
        return oneStarRaters;
    }

    public void setOneStarRaters(int oneStarRaters) {
        this.oneStarRaters = oneStarRaters;
    }

    public int getTwoStarsRaters() {
        return twoStarsRaters;
    }

    public void setTwoStarsRaters(int twoStarsRaters) {
        this.twoStarsRaters = twoStarsRaters;
    }

    public int getThreeStarsRaters() {
        return threeStarsRaters;
    }

    public void setThreeStarsRaters(int threeStarsRaters) {
        this.threeStarsRaters = threeStarsRaters;
    }

    public int getFourStarsRaters() {
        return fourStarsRaters;
    }

    public void setFourStarsRaters(int fourStarsRaters) {
        this.fourStarsRaters = fourStarsRaters;
    }

    public int getFiveStarsRaters() {
        return fiveStarsRaters;
    }

    public void setFiveStarsRaters(int fiveStarsRaters) {
        this.fiveStarsRaters = fiveStarsRaters;
    }
}

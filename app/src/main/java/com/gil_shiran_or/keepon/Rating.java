package com.gil_shiran_or.keepon;

public class Rating
{
    private float sum_rates;
    private int one_star_reviewers_num;
    private int two_stars_reviewers_num;
    private int three_stars_reviewers_num;
    private int four_stars_reviewers_num;
    private int five_stars_reviewers_num;

    public Rating() {
    }

    public Rating(float sum_rates, int one_star_reviewers_num, int two_stars_reviewers_num, int three_stars_reviewers_num, int four_stars_reviewers_num, int five_stars_reviewers_num) {
        this.sum_rates = sum_rates;
        this.one_star_reviewers_num = one_star_reviewers_num;
        this.two_stars_reviewers_num = two_stars_reviewers_num;
        this.three_stars_reviewers_num = three_stars_reviewers_num;
        this.four_stars_reviewers_num = four_stars_reviewers_num;
        this.five_stars_reviewers_num = five_stars_reviewers_num;
    }

    public float getSum_rates() {
        return sum_rates;
    }

    public void setSum_rates(float sum_rates) {
        this.sum_rates = sum_rates;
    }

    public int getOne_star_reviewers_num() {
        return one_star_reviewers_num;
    }

    public void setOne_star_reviewers_num(int one_star_reviewers_num) {
        this.one_star_reviewers_num = one_star_reviewers_num;
    }

    public int getTwo_stars_reviewers_num() {
        return two_stars_reviewers_num;
    }

    public void setTwo_stars_reviewers_num(int two_stars_reviewers_num) {
        this.two_stars_reviewers_num = two_stars_reviewers_num;
    }

    public int getThree_stars_reviewers_num() {
        return three_stars_reviewers_num;
    }

    public void setThree_stars_reviewers_num(int three_stars_reviewers_num) {
        this.three_stars_reviewers_num = three_stars_reviewers_num;
    }

    public int getFour_stars_reviewers_num() {
        return four_stars_reviewers_num;
    }

    public void setFour_stars_reviewers_num(int four_stars_reviewers_num) {
        this.four_stars_reviewers_num = four_stars_reviewers_num;
    }

    public int getFive_stars_reviewers_num() {
        return five_stars_reviewers_num;
    }

    public void setFive_stars_reviewers_num(int five_stars_reviewers_num) {
        this.five_stars_reviewers_num = five_stars_reviewers_num;
    }
}

package com.gil_shiran_or.keepon.trainer_weekly_planner;

import java.util.Comparator;

public class TimeComparator implements Comparator<TimeSlot>
{
    public int compare(TimeSlot left, TimeSlot right) {
        return left.getTimeFrom().compareTo(right.getTimeFrom());
    }
}

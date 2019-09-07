package com.gil_shiran_or.keepon.trainings_weekly_schedule;

import java.util.Comparator;

public class TimeComparator implements Comparator<TimeSlot>
{
    public int compare(TimeSlot left, TimeSlot right) {
        return left.getTimeFrom().compareTo(right.getTimeFrom());
    }
}

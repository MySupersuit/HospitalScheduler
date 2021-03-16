package com.example.hospitalscheduler;

import java.util.ArrayList;

public class OperatingTheatreV2 {

    private int number;
    private int isNotified;
    private ArrayList<OperationV2> schedule;

    public OperatingTheatreV2(int number, int isNotified, ArrayList<OperationV2> schedule) {
        this.number = number;
        this.isNotified = isNotified;
        this.schedule = schedule;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getIsNotified() {
        return isNotified;
    }

    public void setIsNotified(int isNotified) {
        this.isNotified = isNotified;
    }

    public ArrayList<OperationV2> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<OperationV2> schedule) {
        this.schedule = schedule;
    }
}

package com.example.hospitalscheduler;

import java.util.ArrayList;
import java.util.List;

public class OperatingTheatre {

    //    private List<Operation> schedule;

    // Put stages somewhere

    // Operation class
    // Procedure
    // Category
    // Stage
    // Surgeon
    // Anaesthetist
    // Scrub Nurse
    // Registrar
    // Previous Operation + Next Operation
    // bool isDelayed
    // bool isPatientIcky / isCovid (bordered yellow)

    private int number;
    private int isNotified; // boolean 1/0 = true/false
    private ArrayList<Operation> schedule;

    public OperatingTheatre(int number, int isNotified, ArrayList<Operation> schedule) {
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

    public List<Operation> getSchedule() {
        return schedule;
    }
}

package com.example.hospitalscheduler;

public class OperatingTheatre {

    private int number;
    private boolean notified;
    // get a schedule object for
    private String currStartTime;
    private String headSurgeon;
    private int currOpThumb;
    private String currOp;
    private int stage;

    public OperatingTheatre(int number, boolean notified, String currStartTime, String headSurgeon, int currOpThumb, String currOp, int stage) {
        this.number = number;
        this.notified = notified;
        this.currStartTime = currStartTime;
        this.headSurgeon = headSurgeon;
        this.currOpThumb = currOpThumb;
        this.currOp = currOp;
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public void setCurrOp(String currOp) {
        this.currOp = currOp;
    }

    public String getCurrOp() {
        return currOp;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public void setCurrStartTime(String currStartTime) {
        this.currStartTime = currStartTime;
    }

    public void setHeadSurgeon(String headSurgeon) {
        this.headSurgeon = headSurgeon;
    }

    public void setCurrOpThumb(int currOpThumb) {
        this.currOpThumb = currOpThumb;
    }

    public int getNumber() {
        return number;
    }

    public boolean isNotified() {
        return notified;
    }

    public String getCurrStartTime() {
        return currStartTime;
    }

    public String getHeadSurgeon() {
        return headSurgeon;
    }

    public int getCurrOpThumb() {
        return currOpThumb;
    }
}

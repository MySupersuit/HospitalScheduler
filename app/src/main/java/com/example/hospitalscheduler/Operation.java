package com.example.hospitalscheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class Operation implements Parcelable {

    private String procedure;
    private String category;
    private int stage;
    private String surgeon;
    private String anaesthetist;
    private String scrubNurse;
    private String registrar;
    private Operation prevOp;
    private Operation nextOp;
    private int isDelayed; // boolean 1/0 = true/false
    private int isCovid;   // boolean 1/0 = true/false
    private String startTime;  // set when patient goes into first stage

    public Operation(String procedure, String category,
                     int stage, String surgeon,
                     String anaesthetist, String scrubNurse,
                     String registrar, Operation prevOp,
                     Operation nextOp, int isDelayed,
                     int isCovid, String startTime) {
        this.procedure = procedure;
        this.category = category;
        this.stage = stage;
        this.surgeon = surgeon;
        this.anaesthetist = anaesthetist;
        this.scrubNurse = scrubNurse;
        this.registrar = registrar;
        this.prevOp = prevOp;
        this.nextOp = nextOp;
        this.isDelayed = isDelayed;
        this.isCovid = isCovid;
        this.startTime = startTime;
    }

    public static final Creator<Operation> CREATOR = new Creator<Operation>() {
        @Override
        public Operation createFromParcel(Parcel in) {
            return new Operation(in);
        }

        @Override
        public Operation[] newArray(int size) {
            return new Operation[size];
        }
    };

    public String[] getStaff() {
        return new String[]{getSurgeon(), getAnaesthetist(), getScrubNurse(), getRegistrar()};
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = surgeon;
    }

    public String getAnaesthetist() {
        return anaesthetist;
    }

    public void setAnaesthetist(String anaesthetist) {
        this.anaesthetist = anaesthetist;
    }

    public String getScrubNurse() {
        return scrubNurse;
    }

    public void setScrubNurse(String scrubNurse) {
        this.scrubNurse = scrubNurse;
    }

    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    public Operation getPrevOp() {
        return prevOp;
    }

    public void setPrevOp(Operation prevOp) {
        this.prevOp = prevOp;
    }

    public Operation getNextOp() {
        return nextOp;
    }

    public void setNextOp(Operation nextOp) {
        this.nextOp = nextOp;
    }

    public int getIsDelayed() {
        return isDelayed;
    }

    public void setIsDelayed(int isDelayed) {
        this.isDelayed = isDelayed;
    }

    public int getIsCovid() {
        return isCovid;
    }

    public void setIsCovid(int isCovid) {
        this.isCovid = isCovid;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.procedure);
        dest.writeString(this.category);
        dest.writeInt(this.stage);
        dest.writeString(this.surgeon);
        dest.writeString(this.anaesthetist);
        dest.writeString(this.scrubNurse);
        dest.writeString(this.registrar);
        dest.writeParcelable(this.prevOp, flags);
        dest.writeParcelable(this.nextOp, flags);
        dest.writeInt(this.isDelayed);
        dest.writeInt(this.isCovid);
    }

    protected Operation(Parcel in) {
        this.procedure = in.readString();
        this.category = in.readString();
        this.stage = in.readInt();
        this.surgeon = in.readString();
        this.anaesthetist = in.readString();
        this.scrubNurse = in.readString();
        this.registrar = in.readString();
        this.prevOp = in.readParcelable(Operation.class.getClassLoader());
        this.nextOp = in.readParcelable(Operation.class.getClassLoader());
        this.isDelayed = in.readInt();
        this.isCovid = in.readInt();
    }
}

//    private String procedure;
//    private String category;
//    private int stage;
//    private String surgeon;
//    private String anaesthetist;
//    private String scrubNurse;
//    private String registrar;
//    private Operation prevOp;
//    private Operation nextOp;
//    private boolean isDelayed;
//    private boolean isCovid;
//    private String startTime;  // set when patient goes into first stage

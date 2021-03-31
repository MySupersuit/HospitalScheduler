package com.example.hospitalscheduler.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.hospitalscheduler.utilities.Constants.*;

public class OperationV2 implements Parcelable {
    public String anaesthetist;
    public String category;
    public int current_stage;
    public long curr_stage_start_time;
    public int isCovid; // boolean 1/0 => true/false
    public int isDelayed; // boolean 1/0 => true/false
    public String id;
    public String patient_name;
    public String previous_op_id;   // "" == first op
    public String procedure;
    public String registrar;
    public String scrubNurse;
    public String surgeon;
    public int theatre_number;
    public List<Comment> comments;

    public OperationV2() {
        // necessary empty constructor
    }

    // full constructor
    public OperationV2(String anaesthetist, String category,
                       int current_stage, long curr_stage_start_time,
                       int isCovid, int isDelayed, String id,
                       String patient_name, String previous_op_id,
                       String procedure, String registrar,
                       String scrubNurse, String surgeon,
                       int theatre_number, List<Comment> comments) {
        this.anaesthetist = anaesthetist;
        this.category = category;
        this.current_stage = current_stage;
        this.curr_stage_start_time = curr_stage_start_time;
        this.id = id;
        this.isCovid = isCovid;
        this.isDelayed = isDelayed;
        this.patient_name = patient_name;
        this.previous_op_id = previous_op_id;
        this.procedure = procedure;
        this.registrar = registrar;
        this.scrubNurse = scrubNurse;
        this.surgeon = surgeon;
        this.theatre_number = theatre_number;
        this.comments = comments;
    }

    // constructor without stage info
    public OperationV2(String anaesthetist, String category,
                       String id,
                       int isCovid, int isDelayed,
                       String patient_name, String previous_op_id,
                       String procedure, String registrar,
                       String scrubNurse, String surgeon,
                       int theatre_number, List<Comment> comments) {
        this.anaesthetist = anaesthetist;
        this.category = category;
        this.isCovid = isCovid;
        this.isDelayed = isDelayed;
        this.patient_name = patient_name;
        this.previous_op_id = previous_op_id;
        this.procedure = procedure;
        this.registrar = registrar;
        this.scrubNurse = scrubNurse;
        this.surgeon = surgeon;
        this.theatre_number = theatre_number;
        this.comments = comments;

        this.id = id;
        this.current_stage = 0;
        this.curr_stage_start_time = -1;
    }

    // null constructor
    public OperationV2(int theatre_number) {
        this.anaesthetist = "";
        this.category = NO_OP;
        this.isCovid = 0;
        this.isDelayed = 0;
        this.patient_name = "";
        this.previous_op_id = "";
        this.procedure = "";
        this.registrar = "";
        this.scrubNurse = "";
        this.surgeon = "No Operation";
        this.theatre_number = theatre_number;
        this.comments = new ArrayList<>();
        this.id = "";
        this.current_stage = 0;
        this.curr_stage_start_time = -1;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public String getAnaesthetist() {
        return anaesthetist;
    }

    public void setAnaesthetist(String anaesthetist) {
        this.anaesthetist = anaesthetist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCurrent_stage() {
        return current_stage;
    }

    public void setCurrent_stage(int current_stage) {
        this.current_stage = current_stage;
    }

    public long getCurr_stage_start_time() {
        return curr_stage_start_time;
    }

    public void setCurr_stage_start_time(long curr_stage_start_time) {
        this.curr_stage_start_time = curr_stage_start_time;
    }

    public int getIsCovid() {
        return isCovid;
    }

    public void setIsCovid(int isCovid) {
        this.isCovid = isCovid;
    }

    public int getIsDelayed() {
        return isDelayed;
    }

    public void setIsDelayed(int isDelayed) {
        this.isDelayed = isDelayed;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPrevious_op_id() {
        return previous_op_id;
    }

    public void setPrevious_op_id(String previous_op_id) {
        this.previous_op_id = previous_op_id;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    public String getScrubNurse() {
        return scrubNurse;
    }

    public void setScrubNurse(String scrubNurse) {
        this.scrubNurse = scrubNurse;
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = surgeon;
    }

    public int getTheatre_number() {
        return theatre_number;
    }

    public void setTheatre_number(int theatre_number) {
        this.theatre_number = theatre_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Parcelable Functions
    public static final Creator<OperationV2> CREATOR = new Creator<OperationV2>() {
        @Override
        public OperationV2 createFromParcel(Parcel in) {
            return new OperationV2(in);
        }

        @Override
        public OperationV2[] newArray(int size) {
            return new OperationV2[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.anaesthetist);
        dest.writeString(this.category);
        dest.writeInt(this.current_stage);
        dest.writeLong(this.curr_stage_start_time);
        dest.writeInt(this.isCovid);
        dest.writeInt(this.isDelayed);
        dest.writeString(this.id);
        dest.writeString(this.patient_name);
        dest.writeString(this.previous_op_id);
        dest.writeString(this.procedure);
        dest.writeString(this.registrar);
        dest.writeString(this.scrubNurse);
        dest.writeString(this.surgeon);
        dest.writeInt(this.theatre_number);
        dest.writeTypedList(this.comments);
    }

    protected OperationV2(Parcel in) {
        this.anaesthetist = in.readString();
        this.category = in.readString();
        this.current_stage = in.readInt();
        this.curr_stage_start_time = in.readLong();
        this.isCovid = in.readInt();
        this.isDelayed = in.readInt();
        this.id = in.readString();
        this.patient_name = in.readString();
        this.previous_op_id = in.readString();
        this.procedure = in.readString();
        this.registrar = in.readString();
        this.scrubNurse = in.readString();
        this.surgeon = in.readString();
        this.theatre_number = in.readInt();
        this.comments = new ArrayList<Comment>();
        in.readTypedList(this.comments, Comment.CREATOR);
    }

}

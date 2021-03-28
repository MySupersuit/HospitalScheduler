package com.example.hospitalscheduler.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable, Comparable<Comment> {
    private String content;
    private long time;

    public Comment(String content, int time) {
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeLong(this.time);
    }

    protected Comment(Parcel in) {
        this.content = in.readString();
        this.time = in.readLong();
    }

    @Override
    public int compareTo(Comment o) {
        return (int) (o.getTime() - this.getTime());
    }
}

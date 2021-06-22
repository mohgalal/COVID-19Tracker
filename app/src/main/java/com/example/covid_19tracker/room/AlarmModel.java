package com.example.covid_19tracker.room;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "alarm")
public class AlarmModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "timeRepeat")
    private String timeRepeat;
    @ColumnInfo(name = "repeat")
    private String repeat;

    public AlarmModel(String title, String time, String repeat, String timeRepeat) {
        this.title = title;
        this.time = time;
        this.repeat = repeat;
        this.timeRepeat = timeRepeat;
    }
//    public AlarmModel(String title, String time, String repeat) {
//        this.title = title;
//        this.time = time;
//        this.repeat = repeat;
//    }
//
//    public AlarmModel(String title, String time) {
//        this.title = title;
//        this.time = time;
//    }
    public AlarmModel() {

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeRepeat() {
        return timeRepeat;
    }

    public void setTimeRepeat(String timeRepeat) {
        this.timeRepeat = timeRepeat;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }


}


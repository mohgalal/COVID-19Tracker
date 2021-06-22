package com.example.covid_19tracker.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDAO {

    @Insert
    void insertAlarm(AlarmModel alarmModel);
    @Delete
    void deleteAlarm(AlarmModel alarmModel);
    @Update
    void  updateAlarm(AlarmModel alarmModel);

    @Query("SELECT * FROM alarm")
    List<AlarmModel> getAllAlarms();


}

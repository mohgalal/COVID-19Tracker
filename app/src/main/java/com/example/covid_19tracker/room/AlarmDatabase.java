package com.example.covid_19tracker.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = AlarmModel.class,version = 1,exportSchema = false)
public abstract class AlarmDatabase extends RoomDatabase {

    public abstract AlarmDAO alarmDAO();

    private static AlarmDatabase instance;

    public static synchronized AlarmDatabase getInstance(Context context){

        if (instance == null){

            instance = Room.databaseBuilder(context, AlarmDatabase.class,"alarm_db").build();
        }
        return instance;
    }
}

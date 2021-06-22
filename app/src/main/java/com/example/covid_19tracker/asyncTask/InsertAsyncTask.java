package com.example.covid_19tracker.asyncTask;

import android.os.AsyncTask;

import com.example.covid_19tracker.room.AlarmDAO;
import com.example.covid_19tracker.room.AlarmModel;

public class InsertAsyncTask extends AsyncTask<AlarmModel,Void,Void> {

    private AlarmDAO alarmDAO;

    public InsertAsyncTask(AlarmDAO alarmDAO) {
        this.alarmDAO = alarmDAO;
    }

    @Override
    protected Void doInBackground(AlarmModel... alarmModels) {
        alarmDAO.insertAlarm(alarmModels[0]);
        return null;
    }
}

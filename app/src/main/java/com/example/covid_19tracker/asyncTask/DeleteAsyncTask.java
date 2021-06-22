package com.example.covid_19tracker.asyncTask;

import android.os.AsyncTask;

import com.example.covid_19tracker.room.AlarmDAO;
import com.example.covid_19tracker.room.AlarmModel;

public class DeleteAsyncTask extends AsyncTask<AlarmModel,Void,Void> {
    private AlarmDAO alarmDAO;

    public DeleteAsyncTask(AlarmDAO alarmDAO) {
        this.alarmDAO = alarmDAO;
    }

    @Override
    protected Void doInBackground(AlarmModel... alarmModels) {
        alarmDAO.deleteAlarm(alarmModels[0]);
        return null;
    }
}

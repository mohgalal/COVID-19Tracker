package com.example.covid_19tracker.asyncTask;

import android.os.AsyncTask;

import com.example.covid_19tracker.room.AlarmDAO;
import com.example.covid_19tracker.room.AlarmModel;

public class UpdateAsyncTask extends AsyncTask<AlarmModel,Void,Void> {
    private AlarmDAO alarmDAO;

    public UpdateAsyncTask(AlarmDAO alarmDAO) {
        this.alarmDAO = alarmDAO;
    }

    @Override
    protected Void doInBackground(AlarmModel... alarmModels) {
        alarmDAO.updateAlarm(alarmModels[0]);
        return null;

    }
}

package com.example.covid_19tracker.asyncTask;

import android.os.AsyncTask;

import com.example.covid_19tracker.room.AlarmDAO;
import com.example.covid_19tracker.room.AlarmModel;

import java.util.List;

public class GetAsyncTask extends AsyncTask<Void,Void, List<AlarmModel>> {
    private AlarmDAO alarmDAO;

    public GetAsyncTask(AlarmDAO alarmDAO) {
        this.alarmDAO = alarmDAO;
    }

    @Override
    protected List<AlarmModel> doInBackground(Void... voids) {

        return alarmDAO.getAllAlarms();
    }
}

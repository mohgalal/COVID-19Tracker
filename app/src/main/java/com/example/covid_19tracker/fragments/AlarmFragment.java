package com.example.covid_19tracker.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.R;
import com.example.covid_19tracker.adapter.AlarmAdapterRv;
import com.example.covid_19tracker.asyncTask.DeleteAsyncTask;
import com.example.covid_19tracker.asyncTask.GetAsyncTask;
import com.example.covid_19tracker.room.AlarmDatabase;
import com.example.covid_19tracker.room.AlarmModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlarmFragment extends Fragment {
    RecyclerView alarmRv;
    AlarmAdapterRv adapterRv;
    List<AlarmModel> alarmList = new ArrayList<>();
    TextView noAlarmTv;
    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);
        alarmRv = v.findViewById(R.id.alarm_rv);
        fab = v.findViewById(R.id.fab_btn);
        noAlarmTv = v.findViewById(R.id.no_alarm_tv);
        noAlarmTv.setVisibility(View.INVISIBLE);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllAlarms();
        setUpRecyclerView();
        setClickListner();
    }

    private void getAllAlarms() {
        try {
            alarmList.clear();
            alarmList.addAll(new GetAsyncTask(AlarmDatabase.getInstance(requireContext())
                    .alarmDAO()).execute().get());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setUpRecyclerView() {

        adapterRv = new AlarmAdapterRv(alarmList, new AlarmAdapterRv.OnRemoveProductClick() {
            @Override
            public void onRemoveClick(View view, int postion) {
                AlarmModel selectedAlarm = alarmList.get(postion);
                new DeleteAsyncTask(AlarmDatabase.getInstance(getContext()).alarmDAO()).execute(selectedAlarm);
                adapterRv.notifyDataSetChanged();
                getAllAlarms();
            }
        }, new AlarmAdapterRv.OnAlarmClickListener() {
            @Override
            public void onAlarmClick(View view, int position) {

                AlarmModel alarmModel = alarmList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("abc", alarmModel);

                Navigation.findNavController(view).navigate(R.id.action_alarmFragment_to_alarmDetailsFragment, bundle);

            }
        });
        alarmRv.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        alarmRv.addItemDecoration(new DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL));
        alarmRv.setAdapter(adapterRv);
    }

    private void setClickListner() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_alarmFragment_to_addReminderFragment2);
            }
        });
    }
}
package com.example.covid_19tracker.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.covid_19tracker.AlarmReceiver;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.asyncTask.UpdateAsyncTask;
import com.example.covid_19tracker.room.AlarmDatabase;
import com.example.covid_19tracker.room.AlarmModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmDetailsFragment extends Fragment {

    CheckBox cbSt, cbSn, cbMon, cbWed, cbThu, cbTus, cbFri;
    TextView tvTime,repeatTv;
    String timeTonotify,selectedNum;
    String title,time;
    TextInputEditText titleEt;
    MaterialButton updateBtn;
    Spinner timeRepeatSp;
    LinearLayout repeatWeeklyLt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_alarm_details, container, false);
        tvTime = view.findViewById(R.id.add_reminder_tv_time_deta);
        titleEt = view.findViewById(R.id.add_reminder_et_enter_medicine_name_deta);
        updateBtn = view.findViewById(R.id.update_btn);
        timeRepeatSp = view.findViewById(R.id.time_repeat_sp_deta);
        repeatWeeklyLt = view.findViewById(R.id.repeat_weekly_layout_deta);
        repeatTv = view.findViewById(R.id.add_reminder_tv_type_of_repetition_deta);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //this for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.number_of_repetition, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeRepeatSp.setAdapter(adapter);
        if (timeRepeatSp != null) {
            timeRepeatSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String  selectedNumber = parent.getItemAtPosition(position).toString();
                    selectedNum = selectedNumber;

                    //Toast.makeText(getContext(), selectedNum+"", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        timeTonotify = i + ":" + i1;
                        tvTime.setText(FormatTime(i, i1));
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        repeatWeeklyLt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater lf = LayoutInflater.from(getContext());
                View prompet = lf.inflate(R.layout.repeat_weekly,null);
                final AlertDialog.Builder alart = new AlertDialog.Builder(getContext());
                //TODO: Here I'll carry the checkbox.
                cbSt = prompet.findViewById(R.id.saturday_cb);
                cbSn = prompet.findViewById(R.id.sunday_cb);
                cbMon = prompet.findViewById(R.id.monday_cb);
                cbTus = prompet.findViewById(R.id.tuesday_cb);
                cbWed = prompet.findViewById(R.id.wednesday_cb);
                cbThu = prompet.findViewById(R.id.thursday_cb);
                cbFri = prompet.findViewById(R.id.friday_cb);
                alart.setView(prompet);
                alart.setCancelable(false);
                alart.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        onCheckboxClickListener();
                    }
                });
                alart.create();
                alart.show();
            }
        });

        AlarmModel alarmModel = getTheAlarmFromAlarmFragment();
        setUpClickListener(alarmModel);


    }

    private void onCheckboxClickListener() {
        StringBuffer stringBuffer = new StringBuffer();
        // stringBuffer.append("the days is:");
        if (cbSt.isChecked() && cbSn.isChecked() && cbMon.isChecked() && cbTus.isChecked()
                && cbWed.isChecked() && cbThu.isChecked() && cbFri.isChecked()){
            stringBuffer.append(getString(R.string.everyday));
            repeatTv.setText(stringBuffer.toString());
        }else {
            if (cbSt.isChecked()){
//
                stringBuffer.append(getString(R.string.sat)+",");
                //repeatTv.setText(cbSt.getText().toString());
            }
            if (cbSn.isChecked()){
                stringBuffer.append(getString(R.string.sun)+",");
                //repeatTv.setText(cbSn.getText().toString());

            }
            if (cbMon.isChecked()){
                stringBuffer.append(getString(R.string.mon)+",");
                // repeatTv.setText(cbMon.getText().toString());

            }
            if (cbWed.isChecked()){
                stringBuffer.append(getString(R.string.wed)+",");
                // repeatTv.setText(cbWed.getText().toString());
            }
            if (cbThu.isChecked()){
                stringBuffer.append(getString(R.string.thu)+",");
                // repeatTv.setText(cbThu.getText().toString());

            }
            if (cbTus.isChecked()){
                stringBuffer.append(getString(R.string.tue)+",");
                // repeatTv.setText(cbTus.getText().toString());

            }
            if (cbFri.isChecked()){
                stringBuffer.append(getString(R.string.fri)+",");
                //repeatTv.setText(cbFri.getText().toString());
            }

            if (!cbSt.isChecked() && !cbSn.isChecked() && !cbMon.isChecked() && !cbTus.isChecked()
                    && !cbWed.isChecked() && !cbThu.isChecked() && !cbFri.isChecked()){
                stringBuffer.append(getString(R.string.everyday));
            }
            repeatTv.setText(stringBuffer.toString());
        }
    }

    private void setUpClickListener(AlarmModel updatedAlarm) {

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 title = titleEt.getText().toString();
                 time = tvTime.getText().toString();
                String repeat = repeatTv.getText().toString();
                String timeRepeat = selectedNum;
                if(title.isEmpty() || time.isEmpty()){

                    Toast.makeText(requireContext(), getActivity().getResources().getString(R.string.enter_medicine_name), Toast.LENGTH_SHORT).show();

                } else {

                    updatedAlarm.setTitle(title);
                    updatedAlarm.setTime(time);
                    updatedAlarm.setRepeat(repeat);
                    updatedAlarm.setTimeRepeat(timeRepeat);
                    new UpdateAsyncTask(AlarmDatabase.getInstance(requireContext()).alarmDAO()).execute(updatedAlarm);
                    setAlarm(title,time);
                    Toast.makeText(requireContext(),getActivity().getResources().getString( R.string.alarm_has_been_updated_successfully), Toast.LENGTH_LONG).show();

                    Navigation.findNavController(v).navigate(R.id.action_alarmDetailsFragment_to_alarmFragment);
                }
                }
        });

    }

    private AlarmModel getTheAlarmFromAlarmFragment() {

        AlarmModel selectedAlarm = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedAlarm = (AlarmModel) bundle.getSerializable("abc");
            titleEt.setText(selectedAlarm.getTitle());
            tvTime.setText(selectedAlarm.getTime());
            repeatTv.setText(selectedAlarm.getRepeat());
        }
        return selectedAlarm;

    }

    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }

    private void setAlarm(String text, String time) {
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("text", text);
        intent.putExtra("time", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = timeTonotify;
        DateFormat formatter = new SimpleDateFormat("hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
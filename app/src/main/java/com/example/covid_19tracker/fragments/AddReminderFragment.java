package com.example.covid_19tracker.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.example.covid_19tracker.asyncTask.InsertAsyncTask;
import com.example.covid_19tracker.room.AlarmDatabase;
import com.example.covid_19tracker.room.AlarmModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;


public class AddReminderFragment extends Fragment {

    CheckBox cbSt, cbSn, cbMon, cbWed, cbThu, cbTus, cbFri;
    TextView tvTime, repeatTv;
    String timeTonotify,selectedNum;
    TextInputEditText titleEt;
    MaterialButton saveBtn;
    Spinner timeRepeatSp;
    LinearLayout repeatWeeklyLt;
    String title;
    Calendar calendar = Calendar.getInstance();
    String time;
    int hour,minute;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        tvTime = view.findViewById(R.id.add_reminder_tv_time);
        titleEt = view.findViewById(R.id.add_reminder_et_enter_medicine_name);
        saveBtn = view.findViewById(R.id.update_btn);
        timeRepeatSp = view.findViewById(R.id.time_repeat_sp);
        repeatWeeklyLt = view.findViewById(R.id.repeat_weekly_layout);
        repeatTv = view.findViewById(R.id.add_reminder_tv_type_of_repetition);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.number_of_repetition, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeRepeatSp.setAdapter(adapter);
        if (timeRepeatSp != null) {
            timeRepeatSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   String  selectedNumber = parent.getItemAtPosition(position).toString();
                    selectedNum = selectedNumber;

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //day = calendar.get(Calendar.DAY_OF_MONTH);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        timePicker.clearFocus();
                        hour = timePicker.getCurrentHour();
                        minute = timePicker.getCurrentMinute();
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
                alart.setPositiveButton( getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        onCheckboxClickListener();
                    }
                });

                alart.create();
                alart.show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 title = titleEt.getText().toString();
                 time = tvTime.getText().toString();
                String repeat = repeatTv.getText().toString();
                String timeRepeat = selectedNum;

                if (title.isEmpty() || time.equals("Time") || repeat.equals("Repeat Weekly")){
                    Toast.makeText(requireContext(), getActivity().getResources().getString(R.string.enter_medicine_name), Toast.LENGTH_SHORT).show();
                }else {

                    new InsertAsyncTask(AlarmDatabase.getInstance(requireContext())
                            .alarmDAO()).execute(new AlarmModel(title, time, repeat, timeRepeat));
                    //setAlarm(title,time);
                    newAlarm(title,timeTonotify);
                    Toast.makeText(getContext(), hour+" : "+minute, Toast.LENGTH_SHORT).show();
                   Navigation.findNavController(v).navigate(R.id.action_addReminderFragment2_to_alarmFragment);

                }

                }
        });

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
            if (cbTus.isChecked()){
                stringBuffer.append(getString(R.string.tue)+",");
                // repeatTv.setText(cbTus.getText().toString());

            }

        if (cbWed.isChecked()){
            stringBuffer.append(getString(R.string.wed)+",");
          // repeatTv.setText(cbWed.getText().toString());
        }
        if (cbThu.isChecked()){
            stringBuffer.append(getString(R.string.thu)+",");
           // repeatTv.setText(cbThu.getText().toString());

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

    private void newAlarm(String text,String time){

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("text", text);
        intent.putExtra("time", time);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);


        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

    }
}
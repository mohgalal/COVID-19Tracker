package com.example.covid_19tracker.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid_19tracker.NavigationBottom;
import com.example.covid_19tracker.Question;
import com.example.covid_19tracker.R;
import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.covid_19tracker.Constant.PROFILE_URL;
import static com.example.covid_19tracker.Constant.QUESTION_URL;
import static com.example.covid_19tracker.Constant.SSN_FILE_NAME;
import static com.example.covid_19tracker.Constant.SSN_SP_KEY;
import static com.example.covid_19tracker.Question.infected;

public class ProfileFragment extends Fragment {

    ProgressBar progressBar;
    TextView  progeressDayTv, profileNameTv, dateTv, isolationTv,isolation2Tv;
    MaterialButton statusBtn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String ssn, currentDate, infected;
    ConstraintLayout infectedLout, nonInfectedLout;
    Calendar calendar;
    int progressStatus = 0;
    int startDay;
    Handler handler = new Handler();
    private static final String TAG = "ProfileFragment";
    Calendar calendar1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        statusBtn = view.findViewById(R.id.profile_status_btn);
        progressBar = view.findViewById(R.id.progress_bar);
        dateTv = view.findViewById(R.id.the_date_tv);
        isolationTv = view.findViewById(R.id.isolation_period_tv);
        isolation2Tv = view.findViewById(R.id.isolation_period2_tv);
        profileNameTv = view.findViewById(R.id.profile_name_tv);
        progeressDayTv = view.findViewById(R.id.progress_days_tv);
        infectedLout = view.findViewById(R.id.infected_container);
        nonInfectedLout = view.findViewById(R.id.non_infected_container);
        calendar1 = Calendar.getInstance(TimeZone.getDefault());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = this.getActivity().getSharedPreferences(SSN_FILE_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(SSN_SP_KEY)) {
            ssn = sharedPreferences.getString(SSN_SP_KEY, "No SSN");
            infected=  sharedPreferences.getString("infect","No SSN");
            startDay=sharedPreferences.getInt("startDay",555);
        }

        calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        dateTv.setText(currentDate);
      //  Toast.makeText(getContext(), currentDate+" ", Toast.LENGTH_SHORT).show();

        infectedLout.setVisibility(View.INVISIBLE);
        nonInfectedLout.setVisibility(View.INVISIBLE);

        if (infected.equals("1")){
            infectedLout.setVisibility(View.VISIBLE);
            statusBtn.setText("Infected");
            statusBtn.setTextColor(Color.RED);
            statusBtn.setEnabled(false);
            int day = calendar.get(Calendar.DAY_OF_YEAR);
            int currentDay=Math.abs(day-startDay+1);
            progressBar.setProgress(currentDay);
            progeressDayTv.setText(currentDay + " days");
        }else if (infected.equals("0")){
            nonInfectedLout.setVisibility(View.VISIBLE);
            statusBtn.setText("Non-Infected");
            statusBtn.setTextColor(Color.GREEN);
        }


       httpRetrieve();



        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Question.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });


    }

    private void httpRetrieve() {
        RequestParams params = new RequestParams();
        //params.put("infected",infected);
        params.put("SSN",ssn);
        AsyncHttpClient async =new AsyncHttpClient();
        async.setTimeout(6000000);
        async.post(PROFILE_URL,params,new AsyncHttpResponseHandler(){

            public void onSuccess(String response) {
                super.onSuccess(response);
               // Log.d(TAG, "response: ");
                //Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                try {
//                   progressDialog.dismiss();
                    Log.d(TAG, response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject JO = jsonArray.getJSONObject(0);
                    Log.d(TAG, JO.toString());
                    String fullName = JO.getString("full_name");
                    String infected = JO.getString("infected");
                    if (!fullName.equals("null") && !fullName.isEmpty() && fullName != null){ profileNameTv.setText(fullName);}
                    if (!infected.equals("null") && !infected.isEmpty() && infected != null){
                        if (infected.equals("0")) {
                            statusBtn.setText("Non-Infected");
                            statusBtn.setTextColor(Color.GREEN);

                        }else{
                            statusBtn.setText("Infected");
                        statusBtn.setTextColor(Color.RED);}

                    }
                        //Toast.makeText(getContext(), "sorry, try again", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                super.onFailure(statusCode, error, content);
                Log.d(TAG, "onFailure: error");
                Toast.makeText(getContext(), error.getMessage()+content, Toast.LENGTH_SHORT).show();
                if (statusCode == 404){
                    Toast.makeText(getContext(), "not found", Toast.LENGTH_SHORT).show();
                }
                else if (statusCode >=500 && statusCode <= 600){
                    Toast.makeText(getContext(), "server error", Toast.LENGTH_SHORT).show();
                }
                else if (statusCode == 403){
                    Toast.makeText(getContext(), "forbidden error", Toast.LENGTH_SHORT).show();
                }

                else
                    Toast.makeText(getContext(), "Unexpected error ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testRunningProgressbar() {
        //        if (infected.equals("1")){
//            infectedLout.setVisibility(View.VISIBLE);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (progressStatus<10){
//                        progressStatus+=1;
//                        handler.post(new Runnable() {
//                            @SuppressLint("SetTextI18n")
//                            @Override
//                            public void run() {
//                                progressBar.setProgress(progressStatus);
//                                progeressDayTv.setText(progressStatus + "days");
//
//                                if (progressStatus == 10 && infected.equals("1")){
//                                    isolationTv.setText(" The isolation period has Completed");
//                                    isolation2Tv.setText(" Please Re-answer on the question ");
//                                    isolationTv.setTextColor(Color.GREEN);
//                                    isolation2Tv.setTextColor(Color.RED);
//                                    progressBar.setVisibility(View.INVISIBLE);
//                                    progeressDayTv.setVisibility(View.INVISIBLE);
//                                }
//                            }
//                        });
//
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        //calendar.add(Calendar.DATE,10);
//
//                    }
//                }
//            }).start(); }else if (infected.equals("0")){
//            nonInfectedLout.setVisibility(View.VISIBLE);
//        }
    }
}
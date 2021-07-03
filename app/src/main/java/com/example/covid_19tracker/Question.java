package com.example.covid_19tracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;

import static com.example.covid_19tracker.Constant.ERROR_DIALOG_REQUEST;
import static com.example.covid_19tracker.Constant.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.covid_19tracker.Constant.PERMISSIONS_REQUEST_ENABLE_GPS;
import static com.example.covid_19tracker.Constant.QUESTION_URL;
import static com.example.covid_19tracker.Constant.SSN_FILE_NAME;
import static com.example.covid_19tracker.Constant.SSN_SP_KEY;

public class Question extends AppCompatActivity {

    Button yesBtn, noBtn;
    String ssn;
    private static final String TAG = "Question";
    int endDay, endYear, startYear, startDay;
    Intent intent;
    Calendar calendar;
    public static String infected = "2";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        sharedPreferences = getSharedPreferences(SSN_FILE_NAME, Context.MODE_PRIVATE);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        if (sharedPreferences.contains(SSN_SP_KEY)) {
            ssn = sharedPreferences.getString(SSN_SP_KEY, "No SSN");

        }

        yesBtn = findViewById(R.id.syndrome_btn_yes);
        noBtn = findViewById(R.id.syndrome_btn_no);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                infected = "1";
                editor = sharedPreferences.edit();
                editor.putString("infect", infected);
                editor.apply();

                startYear = calendar.get(Calendar.YEAR);
                startDay = calendar.get(Calendar.DAY_OF_YEAR);
                if (365 - startDay >= 14) {
                    endDay = startDay + 14;
                    endYear = startYear;
                } else {
                    endDay = Math.abs(365 - startDay);
                    endYear = startYear + 1;
                }

                editor = sharedPreferences.edit();
                editor.putString("infect", infected);
                editor.putInt("startDay", startDay);
                editor.putInt("startYear", startYear);
                editor.putInt("endDay", endDay);
                editor.putInt("endYear", endYear);
                editor.apply();

                 asyncHttp(infected, ssn);

//                Intent intent = new Intent(Question.this, NavigationBottom.class);
//                startActivity(intent);
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                infected = "0";
                editor = sharedPreferences.edit();
                editor.putString("infect", infected);
                editor.apply();

                asyncHttp(infected, ssn);


//                Intent intent = new Intent(Question.this, NavigationBottom.class);
//                startActivity(intent);

            }
        });
    }


    private void asyncHttp(String infected, final String ssn) {

        RequestParams params = new RequestParams();
        params.put("infected", infected);
        params.put("SSN", ssn);

        AsyncHttpClient async = new AsyncHttpClient();
        async.setTimeout(6000000);
        async.post(QUESTION_URL, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) {
                super.onSuccess(response);
                Log.d(TAG, "onSuccess: ");

                try {
//                   progressDialog.dismiss();
                    Log.d(TAG, response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                    JSONObject JO = jsonArray.getJSONObject(0);
                    String code = JO.getString("code");

                    if (code.equals("Done")) {

                        Intent intent = new Intent(Question.this, NavigationBottom.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(Question.this, "sorry, try again", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                super.onFailure(statusCode, error, content);
                Log.d(TAG, "onFailure: error");
                Toast.makeText(Question.this, error.getMessage() + content, Toast.LENGTH_SHORT).show();
                if (statusCode == 404) {
                    Toast.makeText(Question.this, "not found", Toast.LENGTH_SHORT).show();
                } else if (statusCode >= 500 && statusCode <= 600) {
                    Toast.makeText(Question.this, "server error", Toast.LENGTH_SHORT).show();
                } else if (statusCode == 403) {
                    Toast.makeText(Question.this, "forbidden error", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(Question.this, "Unexpected error ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
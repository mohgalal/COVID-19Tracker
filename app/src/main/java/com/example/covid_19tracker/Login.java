package com.example.covid_19tracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.covid_19tracker.Constant.ERROR_DIALOG_REQUEST;
import static com.example.covid_19tracker.Constant.LOGIN_URL;
import static com.example.covid_19tracker.Constant.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.covid_19tracker.Constant.PERMISSIONS_REQUEST_ENABLE_GPS;
import static com.example.covid_19tracker.Constant.SSN_FILE_NAME;
import static com.example.covid_19tracker.Constant.SSN_SP_KEY;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    TextInputEditText et_SSN,et_pass;
    MaterialButton btn_login,btn_signup;
    CheckBox chb_stay_signed_in;
    TextView tv_forget_pass;
    Animation anim1,anim2,anim3,anim4,anim5;
    TextInputLayout textInputLayout1,textInputLayout2;
    int doubleBackToExitPressed = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public boolean mLocationPermissionGranted = false;

    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_SSN =findViewById(R.id.login_et_email);
//        et_pass=findViewById(R.id.login_et_pass);
        btn_login=findViewById(R.id.login_btn_login);
        btn_signup=findViewById(R.id.login_btn_sign_up);
//        tv_forget_pass=findViewById(R.id.login_forget_pass);
        chb_stay_signed_in = findViewById(R.id.login_chb_stay_signed_in);
        textInputLayout1=findViewById(R.id.login_text_input1);
//        textInputLayout2=findViewById(R.id.login_text_input2);
        progressBar= findViewById(R.id.progress_login);
        Sprite doubleBounce = new FoldingCube();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.INVISIBLE);

        anim1= AnimationUtils.loadAnimation(Login.this, R.anim.anim1);
        anim2= AnimationUtils.loadAnimation(Login.this, R.anim.anim2);
        anim3= AnimationUtils.loadAnimation(Login.this, R.anim.anim3);
        anim4= AnimationUtils.loadAnimation(Login.this, R.anim.anim4);
       // anim5= AnimationUtils.loadAnimation(Login.this,R.anim.anim5);

        textInputLayout1.setAnimation(anim1);
//        textInputLayout2.setAnimation(anim2);
//        tv_forget_pass.setAnimation(anim3);
        chb_stay_signed_in.setAnimation(anim3);
        btn_login.setAnimation(anim3);
        btn_signup.setAnimation(anim4);

        // hide old design
//        tv_forget_pass.setVisibility(View.GONE);
//        textInputLayout2.setVisibility(View.GONE);
        if(checkMapServices()){
            if(mLocationPermissionGranted){
                //  getChatrooms();
                //     getUserLocation();
//                goToNavigationBottomForDisplayMap();
            }
            else{
                getLocationPermission();

            }
        }



        // here to save the user state
        sharedPreferences = getSharedPreferences(SSN_FILE_NAME, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(SSN_SP_KEY)) {
                //sharedPreferences.getString(SSN_SP, "No SSN");
                Intent intent = new Intent(Login.this, NavigationBottom.class);
                startActivity(intent);
        } else {
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Intent intent = new Intent(Login.this,NavigationBottom.class);
//                startActivity(intent);
                    login(v);

                }
            });

            btn_signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this, Signup.class);
                    startActivity(intent);
                }
            });
        }
    }
    private void login(View view) {
        String SSN = et_SSN.getText().toString();

        if (SSN.length() != 14) {
            et_SSN.setError("The SSN should be 14 number");
        } else {

            RequestParams params = new RequestParams("SSN", SSN);

        progressBar.setVisibility(View.VISIBLE);

            AsyncHttpClient async = new AsyncHttpClient();
            async.setTimeout(6000000);
            async.post(LOGIN_URL, params, new AsyncHttpResponseHandler() {


                @Override
                public void onSuccess(String response) {
                    super.onSuccess(response);
                    Log.d(TAG, "onSuccess: ");
                    //Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                    try {
                   //progressBar.setVisibility(View.INVISIBLE);
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                        JSONObject JO = jsonArray.getJSONObject(0);
                        String code = JO.getString("code");


                        if (code.equals("login_true")) {
                            progressBar.setVisibility(View.INVISIBLE);
                            String ssn = JO.getString("SSN");
                            editor = sharedPreferences.edit();
                            editor.putString(SSN_SP_KEY,ssn);
                            editor.putString("infect","2");
                            editor.apply();
                            Toast.makeText(Login.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, NavigationBottom.class);
                            startActivity(intent);


                        } else
                            Toast.makeText(Login.this, "This SSN doesn't exist, sign up please", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    super.onFailure(statusCode, error, content);
                    Log.d(TAG, "onFailure: error");
                    Toast.makeText(Login.this, error.getMessage() + content, Toast.LENGTH_SHORT).show();
                    if (statusCode == 404) {
                        Toast.makeText(Login.this, "not found", Toast.LENGTH_SHORT).show();
                    } else if (statusCode >= 500 && statusCode <= 600) {
                        Toast.makeText(Login.this, "server error", Toast.LENGTH_SHORT).show();
                    } else if (statusCode == 403) {
                        Toast.makeText(Login.this, "forbidden error", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(Login.this, "Unexpected error ", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            System.exit(0);
        }
        else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Please press Back again to exit", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
    }
    //1
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Login.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Login.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    //3
    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly,please enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        //if user press yes go to setting activity for enable gps
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        //i use  startActivityForResult to know whether or not user accepted the permission or denied the permission
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                        // after that look at onActivityresult method
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    //6
    public void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */

        //  if permission accepted make  mLocationPermissionGranted = true;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            //  getChatrooms();
            // getUserLocation();
//            goToNavigationBottomForDisplayMap();
        }
        // else display default dialog for ask user to give his permission ,and i know if giving me permission or not from method onRequestPermissionsResult

        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    //7
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty ,and if ok permission the result arrays are not empty .
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }
    //4
    // i use this method after the user accepted or denied the permission
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                //if the user done accepted the permission use app
                if(mLocationPermissionGranted){
                    //5
                    // getUserLocation();
//                    goToNavigationBottomForDisplayMap();
                }
                //else go Location Permission for taking the permission
                else{

                    getLocationPermission();
                }
            }
        }

    }


    // i use this method to sure if GPS is enabled on the device or not  //2
    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

//if location or gps doesn't enable display alert dialog for enable
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    //8
    public boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }


}
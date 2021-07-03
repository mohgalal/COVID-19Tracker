package com.example.covid_19tracker;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.covid_19tracker.fragments.LanguagesFragment;
import com.example.covid_19tracker.services.LocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import static com.example.covid_19tracker.Constant.ERROR_DIALOG_REQUEST;
import static com.example.covid_19tracker.Constant.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.covid_19tracker.Constant.PERMISSIONS_REQUEST_ENABLE_GPS;
import static com.example.covid_19tracker.Constant.SSN_FILE_NAME;
import static com.example.covid_19tracker.Constant.SSN_SP_KEY;

public class NavigationBottom extends AppCompatActivity {
    
    private static final String TAG = "NavigationBottom";
    private FusedLocationProviderClient fusedLocationProviderClient;
    double lat, lng,lat2,lng2;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String infected;
    public static User user1;
    int doubleBackToExitPressed = 1;
    String ssn;
    Switch aSwitch;
    SharedPreferences sharedPreferences;
    public boolean mLocationPermissionGranted = false;
    String slanguage ;
    private Locale mCurrentLocale;

    Intent refresh;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(SSN_FILE_NAME, Context.MODE_PRIVATE);
        slanguage = sharedPreferences.getString("language","en");
       Configuration config = getBaseContext().getResources().getConfiguration();
//
//      //  if(!"".equals(slanguage)&&!config.locale.getLanguage().equals(slanguage)){
//        //    recreate();
//            Locale locale = new Locale("en");
//            Locale.setDefault(locale);
//            getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
      //  }

          changeLanguage();


           // settingLocale(getBaseContext(),slanguage);


        setContentView(R.layout.bottom_navigation);


//        if(slanguage.equals("ar")){
//            finish();
//            startActivity(getIntent());
//        }

//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
//                }
//            }
//        });
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNavigation = findViewById(R.id.bar_button_nbtn);
        bottomNavigation.setItemIconTintList(null);
        NavigationUI.setupWithNavController(bottomNavigation, navController);


        if (sharedPreferences.contains(SSN_SP_KEY)&&sharedPreferences.contains("infect")){
            ssn=  sharedPreferences.getString(SSN_SP_KEY,"No SSN");
            infected=  sharedPreferences.getString("infect","No SSN");
            // Toast.makeText(this, ssn+"", Toast.LENGTH_SHORT).show();
        }

        // Toast.makeText(this, slanguage, Toast.LENGTH_SHORT).show();



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        switch (infected) {
            case "1": {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Location clocation = (Location) task.getResult();
                        if(clocation!= null) {
                            lat = clocation.getLatitude();
                            lng = clocation.getLongitude();
                            // Log.d(TAG, "lng "+lat+"   "+ lng);
                        }
                        User user = new User(ssn, lat, lng);
                        myRef.child(ssn).setValue(user);
                        startLocationService();
                    }
                });
            }
            break;
            case "0": {
                myRef.child(ssn).removeValue();
                break;
            }

        }




//        Intent intent = getIntent();
//        String ssn = intent.getStringExtra(EXTRA_SSN);
        //Toast.makeText(this, ssn+"", Toast.LENGTH_SHORT).show();


//        Bundle bundle = new Bundle();
//        bundle.putString(EXTRA_SSN, ssn);
//        ProfileFragment fragment = new ProfileFragment();
//        fragment.setArguments(bundle);

//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.profileFragment, fragment);
//        ft.commit();



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

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doubleBackToExitPressed=1;
//            }
//        }, 2000);
    }
//    public void ToggleTheme(boolean isChecked){
//        if (isChecked) {
//            this.getTheme().applyStyle(R.style.AppTheme, false);
//        }
//        else{
//            this.getTheme().applyStyle(R.style.AppTheme, true);
//        }
//    }
private void startLocationService(){
    if(!isLocationServiceRunning()){
        Intent serviceIntent = new Intent(this, LocationService.class);
//        this.startService(serviceIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            NavigationBottom.this.startForegroundService(serviceIntent);
        }else{
            startService(serviceIntent);
        }
    }
}
private boolean isLocationServiceRunning() {
    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
        if("com.codingwithmitch.googledirectionstest.services.LocationService".equals(service.service.getClassName())) {
            Log.d(TAG, "isLocationServiceRunning: location service is already running.");
            return true;
        }
    }
    Log.d(TAG, "isLocationServiceRunning: location service is not running.");
    return false;
}
    //1
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(NavigationBottom.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(NavigationBottom.this, available, ERROR_DIALOG_REQUEST);
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(checkMapServices()){
//            if(mLocationPermissionGranted){
//                //  getChatrooms();
//                //     getUserLocation();
////                goToNavigationBottomForDisplayMap();
//            }
//            else{
//                getLocationPermission();
//
//            }
//        }
//    }



    public void changeLanguage(){

// i use this for solving language problem of item in navigation bottom and for saving state of language
        Locale locale;
        locale = new Locale(slanguage);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        config.setLayoutDirection(config.locale);
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        setContentView(R.layout.bottom_navigation);
//    }
    public void settingLocale(Context context,String language){
        Locale locale;
        Configuration config  = new Configuration();
        if(language.equals("en")) {
            locale = new Locale("en");
            Locale.setDefault(locale);
            config.locale = locale;
        }
            else if(language.equals("ar")){
            locale = new Locale("ar");
            Locale.setDefault(locale);
            config.locale = locale;
            }
            context.getResources().updateConfiguration(config,null);
        }


}

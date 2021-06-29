package com.example.covid_19tracker.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.covid_19tracker.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.covid_19tracker.Constant.SSN_FILE_NAME;
import static com.example.covid_19tracker.Constant.SSN_SP_KEY;

public class LocationService extends Service {

    private static final String TAG = "LocationService";

    private FusedLocationProviderClient mFusedLocationClient;
    private final static long UPDATE_INTERVAL = 4 * 1000;  /* 4 secs */
    private final static long FASTEST_INTERVAL = 2000; /* 2 sec */
    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences sharedPreferences;
    String ssn;
    String infected;
    String CHANNEL_ID = "my_channel_01";



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        sharedPreferences = getSharedPreferences(SSN_FILE_NAME, Context.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "My Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        }
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: called.");
        getLocation();
        return START_NOT_STICKY ;
    }


    private void getLocation() {

        // ---------------------------------- LocationRequest ------------------------------------
        // Create the location request to start receiving updates
        LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequestHighAccuracy.setInterval(UPDATE_INTERVAL);
        mLocationRequestHighAccuracy.setFastestInterval(FASTEST_INTERVAL);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: stopping the location service.");
            stopSelf();
            return;
        }

        Log.d(TAG, "getLocation: getting location information.");
        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {

                        Log.d(TAG, "onLocationResult: got location result.");
                        Location location = locationResult.getLastLocation();
                        if (sharedPreferences.contains(SSN_SP_KEY)&&sharedPreferences.contains("infect")){
                            ssn=  sharedPreferences.getString(SSN_SP_KEY,"No SSN");
                            infected=  sharedPreferences.getString("infect","No SSN");
                            // Toast.makeText(this, ssn+"", Toast.LENGTH_SHORT).show();
                        }
                        switch (infected)
                        {
                        case "1":
                            {
                                if (location != null) {
                                    User user = new User(ssn, location.getLatitude(), location.getLongitude());
                                    myRef.child(ssn).setValue(user);

                            }
                                break;
                            }
                                case "0": stopSelf();
                                break;
                        }

                        }
                },
                Looper.myLooper()); // Looper.myLooper tells this to repeat forever until thread is destroyed
    }



    //    private void saveUserLocation(final UserLocation userLocation){
//
//        try{
//            DocumentReference locationRef = FirebaseFirestore.getInstance()
//                    .collection(getString(R.string.collection_user_locations))
//                    .document(FirebaseAuth.getInstance().getUid());
//
//            locationRef.set(userLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        Log.d(TAG, "onComplete: \ninserted user location into database." +
//                                "\n latitude: " + userLocation.getGeo_point().getLatitude() +
//                                "\n longitude: " + userLocation.getGeo_point().getLongitude());
//                    }
//                }
//            });
//        }catch (NullPointerException e){
//            Log.e(TAG, "saveUserLocation: User instance is null, stopping location service.");
//            Log.e(TAG, "saveUserLocation: NullPointerException: "  + e.getMessage() );
//            stopSelf();
//        }
//
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "on destroyed", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent("com.android.LocationService");
//        sendBroadcast(intent);


    }
}

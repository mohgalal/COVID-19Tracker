package com.example.covid_19tracker.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.covid_19tracker.Login;
import com.example.covid_19tracker.Question;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static com.example.covid_19tracker.Constant.ERROR_DIALOG_REQUEST;
import static com.example.covid_19tracker.Constant.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.covid_19tracker.Constant.PERMISSIONS_REQUEST_ENABLE_GPS;
import static com.example.covid_19tracker.Constant.SSN_FILE_NAME;
import static com.example.covid_19tracker.Constant.SSN_SP_KEY;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String infected;
    Calendar calendar;
    ArrayList<User> users;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double lat2, lng2;
    double defaultLat2,defaultLng2;
    double currentLat, currentLng;
    SharedPreferences sharedPreferences;
    private static final int LOCATION_UPDATE_INTERVAL = 4000;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    String ssnSharedPrefrences;
    private static final String TAG = "MapFragment";
    public boolean mLocationPermissionGranted = false;
    SupportMapFragment mapFragment;
    public boolean isPermissionChecked=false;
    int startDay,startYear,endDay,endYear,currentDay,currentYear;
    ImageView ivStatisticsInfo;
    TextView totalInfected,recoveryCases;

    SharedPreferences.Editor editor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        users = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                //  getChatrooms();
                //     getUserLocation();
//                goToNavigationBottomForDisplayMap();
                getUserLocation();

            } else {
                getLocationPermission();
            }
        }

        isOnline();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ivStatisticsInfo = view.findViewById(R.id.iv_statistics_info);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = this.getActivity().getSharedPreferences(SSN_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        currentDay=calendar.get(Calendar.DAY_OF_YEAR);
        currentYear=calendar.get(Calendar.YEAR);
        if (sharedPreferences.contains(SSN_SP_KEY)) {
            ssnSharedPrefrences = sharedPreferences.getString(SSN_SP_KEY, "No SSN");
            startDay=sharedPreferences.getInt("startDay",555);
            startYear=sharedPreferences.getInt("startYear",2050);
            endDay=sharedPreferences.getInt("endDay",666);
            endYear=sharedPreferences.getInt("endYear",2080);
            infected=  sharedPreferences.getString("infect","No SSN");
           // Toast.makeText(getActivity(), "ssn : " + ssnSharedPrefrences, Toast.LENGTH_SHORT).show();
        }

        if (sharedPreferences.contains("lat2")&&sharedPreferences.contains("lng2")) {
            defaultLat2 = sharedPreferences.getFloat("lat2", (float) 30.586737);
            defaultLng2 = sharedPreferences.getFloat("lng2", (float) 31.524736);

           // Toast.makeText(getActivity(), "default lat,lng : " + defaultLat2+", "+defaultLng2, Toast.LENGTH_SHORT).show();
        }

        //for getting Statistics Info
        ivStatisticsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "sta info", Toast.LENGTH_SHORT).show();
                //Create Bottom Sheet dialog
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_for_statistics);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                //write code here
                totalInfected = bottomSheetDialog.findViewById(R.id.total_infected);
                recoveryCases = bottomSheetDialog.findViewById(R.id.recovery_cases);

                totalInfected.setText(40000+"");
                bottomSheetDialog.show();

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if(infected.equals("1")) {
            if (currentYear == endYear) {
                if (currentDay >= endDay) {
                    Intent intent = new Intent(getActivity(), Question.class);
                    startActivity(intent);
                }
            }
        }
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(30.125478, 31.325488);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//        LatLng latLng = new LatLng(lat1, lng1);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 5);
//        mMap.moveCamera(cameraUpdate);

        //taking data from firebase and initialize infected location on map
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    User user1 = d.getValue(User.class);
                    users.add(user1);
                }
                if (!users.isEmpty()) {
                    for (int i = 0; i < users.size(); i++) {
                        User user2 = users.get(i);
                        double lat = user2.getLat();
                        double lng = user2.getLng();
                        String ssn = user2.getssn();
                        if (lat != 0 && lng != 0) {
                            LatLng latLng = new LatLng(lat, lng);
                            mMap.addMarker(new MarkerOptions().position(latLng));
                            //  Marker mMarker =  mMap.addMarker(new MarkerOptions().position(latLng));

//                            Toast.makeText(getActivity(), "Initialize Infected  "+lat2+" , "+lng2, Toast.LENGTH_LONG).show();

//                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f);
//                                mMap.animateCamera(cameraUpdate);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ////////////////////////////////////////////////////////////////////////

        // for tracking device location only
        getUserLocation();
        // for start services and updated every 4 second and display updated location on map
        startUserLocationRunnable();

    }


    public void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                if (task.isSuccessful()) {
                    Location location = (Location) task.getResult();
                    if (location != null) {
                        lat2 = location.getLatitude();
                        lng2 = location.getLongitude();
                        editor.putFloat("lat2",(float)lat2);
                        editor.putFloat("lng2",(float)lng2);
                        editor.apply();
                    } else {
//                        lat2 = 30.586737;
//                        lng2 = 31.524736;
                        lat2 = defaultLat2;
                        lng2 = defaultLng2;

                    }
                   // Toast.makeText(getActivity(), "Device Location : " + lat2 + "," + lng2, Toast.LENGTH_LONG).show();
                    LatLng latLng2 = new LatLng(lat2, lng2);
                    //create marker
                    MarkerOptions marker = new MarkerOptions().position(latLng2).title("My Location");
                    //changing marker icon
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.newgooglemapicon));
                    mMap.addMarker(marker);
                    //move camera
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng2, 15f);
                    // mMap.moveCamera(cameraUpdate);
                    mMap.animateCamera(cameraUpdate);
                    //for making user to use zoom
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                } else {
                    Toast.makeText(getActivity(), " my location fail", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    //calling  retreiveUserLocation() method every 4 second
    private void startUserLocationRunnable() {
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    retreiveUserLocation();
                    mHandler.postDelayed(mRunnable, LOCATION_UPDATE_INTERVAL);
                } catch (NullPointerException e) {
                    Toast.makeText(getActivity(),e.getMessage()+"", Toast.LENGTH_LONG).show();
                }
            }
        }, LOCATION_UPDATE_INTERVAL);

    }

    //for taking updated location from firebase
    private void retreiveUserLocation() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    User user1 = d.getValue(User.class);
                    users.add(user1);
                }
                mMap.clear();
                if (!users.isEmpty()) {
                    for (int i = 0; i < users.size(); i++) {
                        User user2 = users.get(i);
                        double lat = user2.getLat();
                        double lng = user2.getLng();
                        String ssn = user2.getssn();
                        if (lat != 0 && lng != 0) {
                            LatLng latLng = new LatLng(lat, lng);
//                            if(ssn==ssnSharedPrefrences){
//                                //create marker
//                                MarkerOptions marker = new MarkerOptions().position(latLng).title("My Location");
//                                //changing marker icon
//                                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.newgooglemapicon));
//                                mMap.addMarker(marker);
//                            }

                            mMap.addMarker(new MarkerOptions().position(latLng)).setTitle(ssn + "");
                            //        Toast.makeText(getActivity(), "updated "+i+" : "+lat+" , "+lng, Toast.LENGTH_LONG).show();

                        }
                    }
                }
                getUserLocationWithoutZoom();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getUserLocationWithoutZoom() {

        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLat = location.getLatitude();
                        currentLng = location.getLongitude();
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                LatLng latLng = new LatLng(currentLat, currentLng);
                                //create marker
                                MarkerOptions marker = new MarkerOptions().position(latLng).title("My Location");
                                //changing marker icon
                                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.newgooglemapicon));
                                mMap.addMarker(marker);
                               // Toast.makeText(getActivity(), "Updated Device Location : " + currentLat + "," + currentLng, Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                }
            });
        } catch (NullPointerException e) {
//            e.printStackTrace();
        }
    }


    public boolean isOnline(){
        ConnectivityManager conMgr = (ConnectivityManager)this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if(netInfo==null ||!netInfo.isConnected() || !netInfo.isAvailable()){
            // Toast.makeText(getActivity(), "No internet connection !", Toast.LENGTH_LONG).show();
            dialogForInternet();
            return false;
        }
        return true;
    }
    public void dialogForInternet(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Connection Failed");
        alertDialog.setMessage("please turn on mobile data or connect to Wi-Fi.");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }




    //1
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getActivity(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    //3
    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            //  getChatrooms();
            // getUserLocation();
//            goToNavigationBottomForDisplayMap();
        }
        // else display default dialog for ask user to give his permission ,and i know if giving me permission or not from method onRequestPermissionsResult

        else {
            ActivityCompat.requestPermissions(getActivity(),
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

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

    @Override
    public void onResume() {
        super.onResume();
        if(isPermissionChecked) {
            if (checkMapServices()) {
                if (mLocationPermissionGranted) {

                } else {
                    getLocationPermission();

                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPermissionChecked=true;
    }
}
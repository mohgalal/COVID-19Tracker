package com.example.covid_19tracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.covid_19tracker.Login;
import com.example.covid_19tracker.NavigationBottom;
import com.example.covid_19tracker.R;

import static com.example.covid_19tracker.Constant.SSN_FILE_NAME;
import static com.example.covid_19tracker.Constant.SSN_SP_KEY;

public class SettingsFragment extends Fragment {

    TextView emergencyTv , languagesTv , contactusTv, logOutTv;
    ImageView gearIv;
    Switch darkModeSw;
    private SharedPreferences sharedPreferences;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_settings, container, false);
        gearIv = view.findViewById(R.id.gear_iv);
        emergencyTv = view.findViewById(R.id.emergency_tv);
        languagesTv = view.findViewById(R.id.change_language_tv);
        contactusTv = view.findViewById(R.id.contact_us_tv);
        logOutTv = view.findViewById(R.id.logout_tv);
        darkModeSw = view.findViewById(R.id.dark_mode_sw);
        return view ;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  the Animation for gear
        Drawable drawable = gearIv.getDrawable();
        if (drawable instanceof AnimatedVectorDrawableCompat){
            avd =(AnimatedVectorDrawableCompat) drawable;
            avd.start();
        }else if (drawable instanceof AnimatedVectorDrawable){
            avd2 = (AnimatedVectorDrawable) drawable;
            avd2.start();
        }

        //darkModeSw.setChecked(SaveState.getDarkModeState());
//        darkModeSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    getActivity().setTheme(R.style.DarkTheme);
//                    ((NavigationBottom)getActivity()).recreate();
//                }
//            }
//        });

        emergencyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_emergencyFragment);
            }
        });
        languagesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_languagesFragment);
            }
        });
        contactusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_contactUsFragment);
            }
        });

        logOutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences(SSN_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(SSN_SP_KEY);
                editor.apply();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });
    }


}
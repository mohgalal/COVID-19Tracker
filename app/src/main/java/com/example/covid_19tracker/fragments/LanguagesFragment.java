package com.example.covid_19tracker.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.covid_19tracker.R;

import java.util.Locale;


public class LanguagesFragment extends Fragment {


    ImageView backLanguageIv;
    TextView tv_ar,tv_En;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_languages, container, false);
        backLanguageIv = view.findViewById(R.id.back_language_iv);
        tv_ar=view.findViewById(R.id.arabic_tv);
        tv_En=view.findViewById(R.id.english_tv);
        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backLanguageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_languagesFragment_to_settingsFragment);
            }
        });
        tv_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ar",v);
            }
        });
        tv_En.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en",v);
            }
        });
    }
    public void setLocale(String lang,View v){
        Locale myLocale = new Locale(lang);
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        Configuration conf = getActivity().getResources().getConfiguration();
        conf.locale = myLocale;
        getActivity().getResources().updateConfiguration(conf,dm);
        Navigation.findNavController(v).navigate(R.id.languagesFragment);

    }
}
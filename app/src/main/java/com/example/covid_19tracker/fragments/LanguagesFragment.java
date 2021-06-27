package com.example.covid_19tracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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

import com.example.covid_19tracker.NavigationBottom;
import com.example.covid_19tracker.R;

import java.util.Locale;

import static com.example.covid_19tracker.Constant.SSN_FILE_NAME;
import static com.example.covid_19tracker.Constant.SSN_SP_KEY;


public class LanguagesFragment extends Fragment {


    ImageView backLanguageIv;
    TextView tv_ar,tv_En;
    String language;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


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
        sharedPreferences = this.getActivity().getSharedPreferences(SSN_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();



        backLanguageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_languagesFragment_to_settingsFragment);
            }
        });
        tv_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                language = "ar";
//                editor.putString("language", language);
//                editor.apply();
//                setLocale(language,v);
////                getActivity().finish();
////                Intent refresh = new Intent(getActivity(),NavigationBottom.class);
////                startActivity(refresh);
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }
        });
        tv_En.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                language = "en";
//                editor.putString("language", language);
//                editor.apply();
//                setLocale(language,v);
////                getActivity().finish();
////                Intent refresh = new Intent(getActivity(),NavigationBottom.class);
////                startActivity(refresh);
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));

            }
        });
    }
    public void setLocale(String lang,View v){
        Locale myLocale = new Locale(lang);
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        Configuration conf = getActivity().getResources().getConfiguration();
        conf.locale = myLocale;
        getActivity().getResources().updateConfiguration(conf,dm);
        //Navigation.findNavController(v).navigate(R.id.languagesFragment);
        getActivity().recreate();

//            new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//           Intent intent = getActivity().getIntent();
//           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
//           getActivity().overridePendingTransition(0,0);
//           getActivity().finish();
//           getActivity().overridePendingTransition(0,0);
//           startActivity(intent);
//            }
//            });
            }


}
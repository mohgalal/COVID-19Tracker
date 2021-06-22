package com.example.covid_19tracker.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.covid_19tracker.R;

public class ContactUsFragment extends Fragment {

    TextView emailNameTv;
    ImageView backContactIv, whatsappIv, facebookIv, instgramIv, covid19TrackerIv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        emailNameTv = view.findViewById(R.id.contact_email_tv);
        backContactIv = view.findViewById(R.id.back_contact_iv);
        whatsappIv = view.findViewById(R.id.whatsapp_iv);
        facebookIv = view.findViewById(R.id.facebook_iv);
        instgramIv = view.findViewById(R.id.instagram_iv);
        covid19TrackerIv = view.findViewById(R.id.covid19logo_iv);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"covid19trackerteam1@gmail.com"});
                intent.putExtra(Intent.EXTRA_TEXT,"May we help you?");
                startActivity(intent);
            }
        });

        whatsappIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ourNumber ="1022980592";
                boolean installed = appInstalledorNot("com.whatsapp");

                if (installed){

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+20"+ourNumber));
                    startActivity(intent);
                }else
                    Toast.makeText(getContext(), "Whatsapp not installed in your device", Toast.LENGTH_SHORT).show();
            }
        });

        facebookIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pageId = "375806339129228";


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("fb://page/"+pageId));
                startActivity(intent);

            }
        });

        covid19TrackerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://covid-19-tracker-software-company.business.site/?m=true"));
                    startActivity(intent);

            }
        });

        backContactIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_contactUsFragment_to_settingsFragment);
            }
        });
    }

    private boolean appInstalledorNot(String url){
        boolean appInstalled;
        PackageManager packageManager =getActivity().getPackageManager();

        try {
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            appInstalled =true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled =false;
        }
        return appInstalled;
    }
}
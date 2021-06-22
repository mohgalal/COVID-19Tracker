package com.example.covid_19tracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.covid_19tracker.adapter.AdviceAdapter;
import com.example.covid_19tracker.AdviceModel;
import com.example.covid_19tracker.R;

import java.util.ArrayList;

public class AdviceFragment extends Fragment {


ListView listAdvice;
ArrayList<AdviceModel> models;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    return  inflater.inflate(R.layout.fragment_advice, container, false);

}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listAdvice = view.findViewById(R.id.advice_fragment_lv);
        models = new ArrayList<>();

        models.add(new AdviceModel(R.drawable.ic_covid_protection_mask_healthcare_contagion_icon,"You must wear a full mask as a precaution in hospitals and closed crowded places "));
        models.add(new AdviceModel(R.drawable.ic_water_wash_hand_washing_healthcare_icon,"Wash hands thoroughly with soap and water or disinfectants, especially after coughing or sneezing or using toilets"));
        models.add(new AdviceModel(R.drawable.ic_avoid_public_places_icon,"Avoid contact with people with respiratory symptoms and maintain a distance of at least 1 meter between yourself and anyone with symptoms"));
        models.add(new AdviceModel(R.drawable.ic_raw_meat_no_avoid_eat_icon,"Do not eat any animal products uncooked, and guard not to mix them with other flavors"));
        models.add(new AdviceModel(R.drawable.ic_coffee_cup,"Not to drink coffee and any other liquids from a cup that others drink from it"));

        models.add(new AdviceModel(R.drawable.ic_button_avoid_touching_face_stop_preventive_measure_coronavirus_icon,"Avoid contact with the eyes, nose and mouth with unwashed hands"));
        models.add(new AdviceModel(R.drawable.ic_cleaning_door_knob_object_hygiene_icon,"Cleaning surfaces continuously and sterilizing them to maintain general cleanliness"));
        models.add(new AdviceModel(R.drawable.ic_cough_tissue_close_mouth_icon,"Cover the mouth when coughing and sneezing with a tissue or bending the elbow and dispose of the tissue in the trash and wash hands immediately"));
        models.add(new AdviceModel(R.drawable.ic_avoid_animal_dog_touching_virus_outbreak_icon,"Avoid contact with animals, whether alive or dead, and avoid contact with the brightest near these animals"));
        models.add(new AdviceModel(R.drawable.ic_summer_cold_water_drink_ice_cube_hydrate_icon,"Maintaining that the throat is moist by drinking more water constantly"));
        models.add(new AdviceModel(R.drawable.ic_nosmoking,"Smoking is harmful to your health, and hookah is a focus of viruses that you reap, especially in public places"));
        models.add(new AdviceModel(R.drawable.ic_handshake,"Refrain from kissing and just shake hands"));
        models.add(new AdviceModel(R.drawable.ic_sneezing_spread_germs_infection_illness_coronavirus_icon,"Avoid spitting on the floor "));
        models.add(new AdviceModel(R.drawable.ic_high_temperature_avatar_hot_fever_coronavirus_icon,"Stay at home when you have any symptoms such as high temperature, coughing or sneezing "));
        models.add(new AdviceModel(R.drawable.ic_thermometer_gun,"You must measure the temperature if you can be in crowded places "));













        AdviceAdapter adapter = new AdviceAdapter(getActivity(),models);
        listAdvice.setAdapter(adapter);


    }
}
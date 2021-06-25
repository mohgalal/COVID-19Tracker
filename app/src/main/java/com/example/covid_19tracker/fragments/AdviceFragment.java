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

        models.add(new AdviceModel(R.drawable.ic_covid_protection_mask_healthcare_contagion_icon,getActivity().getResources().getString(R.string.advice1)));
        models.add(new AdviceModel(R.drawable.ic_water_wash_hand_washing_healthcare_icon,getActivity().getResources().getString(R.string.advice2)));
        models.add(new AdviceModel(R.drawable.ic_avoid_public_places_icon,getActivity().getResources().getString(R.string.advice3)));
        models.add(new AdviceModel(R.drawable.ic_raw_meat_no_avoid_eat_icon,getActivity().getResources().getString(R.string.advice4)));
        models.add(new AdviceModel(R.drawable.ic_coffee_cup,getActivity().getResources().getString(R.string.advice5)));

        models.add(new AdviceModel(R.drawable.ic_button_avoid_touching_face_stop_preventive_measure_coronavirus_icon,getActivity().getResources().getString(R.string.advice6)));
        models.add(new AdviceModel(R.drawable.ic_cleaning_door_knob_object_hygiene_icon,getActivity().getResources().getString(R.string.advice7)));
        models.add(new AdviceModel(R.drawable.ic_cough_tissue_close_mouth_icon,getActivity().getResources().getString(R.string.advice8)));
        models.add(new AdviceModel(R.drawable.ic_avoid_animal_dog_touching_virus_outbreak_icon,getActivity().getResources().getString(R.string.advice9)));
        models.add(new AdviceModel(R.drawable.ic_summer_cold_water_drink_ice_cube_hydrate_icon,getActivity().getResources().getString(R.string.advice10)));
        models.add(new AdviceModel(R.drawable.ic_nosmoking,getActivity().getResources().getString(R.string.advice11)));
        models.add(new AdviceModel(R.drawable.ic_handshake,getActivity().getResources().getString(R.string.advice12)));
        models.add(new AdviceModel(R.drawable.ic_sneezing_spread_germs_infection_illness_coronavirus_icon,getActivity().getResources().getString(R.string.advice13)));
        models.add(new AdviceModel(R.drawable.ic_high_temperature_avatar_hot_fever_coronavirus_icon,getActivity().getResources().getString(R.string.advice14)));
        models.add(new AdviceModel(R.drawable.ic_thermometer_gun,getActivity().getResources().getString(R.string.advice15)));








        AdviceAdapter adapter = new AdviceAdapter(getActivity(),models);
        listAdvice.setAdapter(adapter);


    }
}
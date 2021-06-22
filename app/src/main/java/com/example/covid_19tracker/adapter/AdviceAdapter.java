package com.example.covid_19tracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.covid_19tracker.AdviceModel;
import com.example.covid_19tracker.R;

import java.util.ArrayList;

public class AdviceAdapter extends BaseAdapter {
    Context context;
    private ArrayList<AdviceModel> data;
    ImageView adviceImage;
    TextView adviceDescription;

    public AdviceAdapter(Context context, ArrayList<AdviceModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return  data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_advice_layout,parent,false);
        }

        adviceImage = (ImageView) convertView.findViewById(R.id.custom_advice_img_image);
        adviceDescription = (TextView) convertView.findViewById(R.id.custom_advice_tv_description);


        AdviceModel model = (AdviceModel) getItem(position); //return or get one object from array List of object


        adviceImage.setImageResource(model.getAdviceImage());
        adviceDescription.setText(model.getAdviceDescription());


        return convertView;
    }
}

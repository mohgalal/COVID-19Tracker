package com.example.covid_19tracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.R;
import com.example.covid_19tracker.room.AlarmModel;

import java.util.List;

public class AlarmAdapterRv extends RecyclerView.Adapter<AlarmAdapterRv.AlarmViewHolder> {

    List<AlarmModel> alarmList;

    OnRemoveProductClick onRemoveProductClick;
    OnAlarmClickListener onAlarmClickListener;


    public interface OnRemoveProductClick {

        void onRemoveClick(View view , int postion);

    }

    public interface OnAlarmClickListener{
        void onAlarmClick(View view, int position);
    }

    public AlarmAdapterRv(List<AlarmModel> alarmList, OnRemoveProductClick onRemoveProductClick, OnAlarmClickListener onAlarmClickListener) {
        this.alarmList = alarmList;
        this.onRemoveProductClick = onRemoveProductClick;
        this.onAlarmClickListener = onAlarmClickListener;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_items,parent,false);

        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmModel alarmModel = alarmList.get(position);
        holder.titleTv.setText(alarmModel.getTitle());
        holder.timeTv.setText(alarmModel.getTime());
        holder.repeatTv.setText(alarmModel.getRepeat());
        holder.timeRepeat.setText(alarmModel.getTimeRepeat());

        holder.removeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemoveProductClick.onRemoveClick(v,position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlarmClickListener.onAlarmClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder{

        TextView titleTv, timeTv, dateTv, repeatTv, timeRepeat;
        ImageView removeIv;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.recycle_title);
            timeTv = itemView.findViewById(R.id.time_rv);
            repeatTv = itemView.findViewById(R.id.recycle_repeat_info);
            removeIv = itemView.findViewById(R.id.remove_image);
            timeRepeat = itemView.findViewById(R.id.recycle_repeat_numbers);
        }
    }
}

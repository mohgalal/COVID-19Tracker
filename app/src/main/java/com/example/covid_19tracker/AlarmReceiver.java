package com.example.covid_19tracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       // Toast.makeText(context, "Please open the GPS for COVID-19Tracker", Toast.LENGTH_LONG).show();
        Bundle bundle = intent.getExtras();
        String text = bundle.getString("text");
        String time = bundle.getString("time");

        //Click on Notification

        Intent intent1 = new Intent(context, NavigationBottom.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Notification Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");
        mBuilder.setContentTitle("Take Your Pill")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .addAction(R.drawable.logo,"Replay",pendingIntent);
        //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

//        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
//        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
//        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);
//        contentView.setTextViewText(R.id.message, text);
//        contentView.setTextViewText(R.id.date, date);
//        mBuilder.setSmallIcon(R.drawable.logo);
//        mBuilder.setAutoCancel(true);
//        mBuilder.setOngoing(true);
//        mBuilder.setPriority(Notification.PRIORITY_HIGH);
//        mBuilder.setOnlyAlertOnce(true);
//        mBuilder.setSound(alarmSound);
//        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
//        mBuilder.setContent(contentView);
//        mBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);


    }

    }
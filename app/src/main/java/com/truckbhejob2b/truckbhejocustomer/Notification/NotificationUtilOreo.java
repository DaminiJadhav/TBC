package com.truckbhejob2b.truckbhejocustomer.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.truckbhejob2b.truckbhejocustomer.R;
import com.truckbhejob2b.truckbhejocustomer.*;

public class NotificationUtilOreo extends ContextWrapper {

    private NotificationManager mManager;
    private Context mContext;
    private Notification.Builder smallNotificationBuilder;
    Uri defaultSoundUri = Settings.System.DEFAULT_NOTIFICATION_URI;
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


    public NotificationUtilOreo(Context base) {
        super(base);
        mContext = base;
        createChannels();

    }

    public void createChannels() {
        NotificationChannel androidChannelEvent,androidChannelNotice,androidChannelConversation,androidChannelComment,androidChannelJob,androidChannelVisitor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();



            androidChannelConversation = new NotificationChannel(Config.ANDROID_CHANNEL_ID,
                    Config.ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

            androidChannelConversation.enableLights(true);
            androidChannelConversation.enableVibration(true);
            androidChannelConversation.setSound(alarmSound,att);
            androidChannelConversation.setLightColor(Color.RED);
            androidChannelConversation.setVibrationPattern(new long[] { 1000, 1000, 1000, 1000, 1000 });
            androidChannelConversation.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(androidChannelConversation);


        }
    }


    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public Notification.Builder getAndroidChannelNotification(String type, String title, String message, String timeStamp, PendingIntent resultPendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            smallNotificationBuilder = new Notification.Builder(
                    mContext, Config.ANDROID_CHANNEL_ID);


            return smallNotificationBuilder
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setWhen(Long.parseLong(timeStamp))
                    .setShowWhen(true)
                    .setOnlyAlertOnce(true)
                    .setSound(alarmSound)
                    .setStyle(new Notification.BigTextStyle().bigText(message))
                    .setSmallIcon(R.mipmap.ic_tbc_app_icon)
                    .setColor(mContext.getResources().getColor(R.color.colorPrimaryDark, mContext.getTheme()))
                    .setContentText(message);
        }
        return null;
    }

    public Notification.Builder getCustomAndroidChannelNotification(String title, String message, PendingIntent resultPendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), Config.ANDROID_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setWhen(0)
                    .setShowWhen(true)
                    .setOnlyAlertOnce(true)
                    .setSound(alarmSound)
                    .setStyle(new Notification.InboxStyle())
                    .setSmallIcon(R.mipmap.ic_tbc_app_icon)
                    .setColor(mContext.getResources().getColor(R.color.colorPrimaryDark, mContext.getTheme()))
                    .setContentText(message);
        }
        return null;
    }


}

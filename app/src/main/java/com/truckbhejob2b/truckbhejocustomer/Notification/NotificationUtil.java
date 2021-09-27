package com.truckbhejob2b.truckbhejocustomer.Notification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.truckbhejob2b.truckbhejocustomer.*;
import com.truckbhejob2b.truckbhejocustomer.DashboardActivity;

import java.util.List;
import java.util.Random;

public class NotificationUtil {

    private Random r = new Random();
    private int Low = 10;
    private int High = 1000;

    private int requestCode = r.nextInt(High - Low) + Low;
    private int notificationId = requestCode;

    private Context mContext;
    private  NotificationCompat.Builder smallNotificationBuilder ;
    public NotificationUtil(Context mContext) {
        this.mContext = mContext;
    }





    public void showNotificationMessage(final String pushAction, final String title, final String message,
                                        final String timeStamp, final String type,
                                        final String id, final String assignedToId,final String orderId,final String companyId) {



                notificationId = Integer.parseInt(id);


        Intent intent = new Intent(mContext, DashboardActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("orderId", orderId);
        intent.putExtra("companyId", companyId);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                NotificationUtilOreo notificationUtilsOreo = new NotificationUtilOreo(mContext);
                Notification.Builder notificationBuilder = notificationUtilsOreo.
                        getAndroidChannelNotification(type,title, message, timeStamp, resultPendingIntent);
                notificationUtilsOreo.getManager().notify(notificationId, notificationBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showSmallNotification(type,title, message, timeStamp, resultPendingIntent);

        }

    }

    private void showSmallNotification(String type,String title, String message, String timeStamp, PendingIntent resultPendingIntent) {

       // NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle().bigText(message);

                smallNotificationBuilder = new NotificationCompat.Builder(
                        mContext, Config.ANDROID_CHANNEL_ID);


        Notification notification;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notification = smallNotificationBuilder
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setStyle(inboxStyle)
                .setWhen(Long.parseLong(timeStamp))
                .setShowWhen(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
//                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_tbc_app_icon)
                .setColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (notificationManager != null) {
            notificationManager.notify(notificationId, notification);
        }
    }

    void showCustomNotificationMessage(String title, String message, String url) {

        Intent customIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        customIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent customResultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        requestCode,
                        customIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                NotificationUtilOreo notificationUtils = new NotificationUtilOreo(mContext);
                Notification.Builder notificationBuilder = notificationUtils.
                        getCustomAndroidChannelNotification(title, message, customResultPendingIntent);
                notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
                notificationUtils.getManager().notify(notificationId, notificationBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showCustomNotification(title, message, customResultPendingIntent);
        }

    }
    private void showCustomNotification(String title, String message, PendingIntent customResultPendingIntent) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(
                mContext, Config.ANDROID_CHANNEL_ID);
        Uri defaultSoundUri = Settings.System.DEFAULT_NOTIFICATION_URI;;

        Notification notification = builder
                .setAutoCancel(true)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(customResultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setWhen(0)
                .setShowWhen(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setLights(Color.RED, 3000, 3000)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.mipmap.ic_tbc_app_icon)
                .setColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .setContentText(message)
                .build();



        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(notificationId, notification);
        }
    }



    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses;
            if (activityManager != null) {
                runningProcesses = activityManager.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInBackground;
    }


}

package com.truckbhejob2b.truckbhejocustomer.FCM;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.truckbhejob2b.truckbhejocustomer.Notification.Config;
import com.truckbhejob2b.truckbhejocustomer.Notification.NotificationUtil;
import com.truckbhejob2b.truckbhejocustomer.SharedPref.*;
import com.truckbhejob2b.truckbhejocustomer.Utils.WebUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class FcmMessageService extends FirebaseMessagingService {
    private static final String TAG = "Vendor";
    private sharedPreference sharedPreference;


    String imageUrl = "";
    String title;
    String action;
    String type;
    String orderId;
    String companyId;
    Bitmap image ;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Log.d(TAG, "onMessageReceived: "+remoteMessage.getNotification().getBody());
        JSONObject jsonMessage;
        jsonMessage = new JSONObject(remoteMessage.getData());
        try {
            title = jsonMessage.getString(Config.PUSH_TITLE);
            action = jsonMessage.getString(Config.PUSH_ACTION);
            type = jsonMessage.getString(Config.PUSH_TYPE);

            orderId = jsonMessage.getString(Config.PUSH_ORDER_ID);
            companyId = jsonMessage.getString(Config.PUSH_COMPANY_ID);
            try {
                URL url = new URL(imageUrl);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                System.out.println(e);
            }
            sharedPreference = new sharedPreference(getApplicationContext());
            boolean isNotification = sharedPreference.getCustomerNotification();
//            Log.d(TAG, "onMessageReceived: "+isNotification);
            if (isNotification){
                sendNotification(title,action,type,orderId,companyId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String title,String action,String type,String orderId,String companyId) {
        Random r = new Random();
        int i1 = r.nextInt(100 - 1) + 1;
        Long timestampLong = System.currentTimeMillis();
        String timeStamp = timestampLong.toString();

        NotificationUtil notificationUtils = new NotificationUtil (getApplicationContext());
        notificationUtils.showNotificationMessage(type,title,action,timeStamp,type, String.valueOf(i1),"123",orderId,companyId);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        sharedPreference = new sharedPreference(getApplicationContext());
        sharedPreference.setCustomerFcm(s);
    }

}

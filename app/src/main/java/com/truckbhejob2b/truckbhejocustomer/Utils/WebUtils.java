package com.truckbhejob2b.truckbhejocustomer.Utils;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.truckbhejob2b.truckbhejocustomer.SharedPref.sharedPreference;
import com.truckbhejob2b.truckbhejocustomer.Volley.VolleySingleTone;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebUtils {

    public static void registeredFcmToServer(final Context mContext) {
        final sharedPreference preference = new sharedPreference(mContext);

        String UriUpdate = Uri.parse(RestUrl.UPDATE_FCM)
        .buildUpon()
        .appendQueryParameter(RestKeys.UPDATE_FCM_CUSTOMER_ID,preference.getCustomerId())
        .appendQueryParameter(RestKeys.UPDATE_FCM_FCM_TOKEN,preference.getCustomerFcm())
        .appendQueryParameter(RestKeys.UPDATE_FCM_API_TOKEN,preference.getCustomerApi())
        .build().toString();

        StringRequest fcm = new StringRequest(Request.Method.GET,
               UriUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                        // ((DashboardActivity) mContext).setSnackBar("Notification Enabled..");
                    } else {
                        //  ((DashboardActivity) mContext).setSnackBar("Notification not Enabled..");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleTone.getInstance(mContext).getRequestQueue().add(fcm);
    }

}

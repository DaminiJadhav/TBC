package com.truckbhejob2b.truckbhejocustomer.Volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleTone {
    private static VolleySingleTone mInstance;
    private RequestQueue mRequestQueue;


    public VolleySingleTone(Context context){
        mRequestQueue= Volley.newRequestQueue(context.getApplicationContext());
    }
    public static synchronized VolleySingleTone getInstance(Context context){
        if (mInstance == null){
            mInstance=new VolleySingleTone(context);

        }
        return mInstance;
    }
    public RequestQueue getRequestQueue (){
        return mRequestQueue;

    }

}

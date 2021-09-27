package com.truckbhejob2b.truckbhejocustomer.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class getCityName {

    public static String getCityName(Context mContext, String lat, String longi) {

        String cityName = "", stateName = "", fullAddress;
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(longi), 1);
            if (addresses != null) {
                Thread.sleep(100);
                Address returnedAddress = addresses.get(0);
               cityName = addresses.get(0).getLocality();
               stateName = addresses.get(0).getAdminArea();
                //String Country = addresses.get(0).getCountryName();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
      fullAddress = cityName +"\n"+ stateName;

        return fullAddress;
    }
    public static  String fullAddress(Context mContext,String lat,String longi){
        String city="NA",state="",country,postalCode,knowName,address="";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            Thread.sleep(100);
            addresses = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(longi), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
             address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
             city = addresses.get(0).getLocality();
             state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return address;
    }
}

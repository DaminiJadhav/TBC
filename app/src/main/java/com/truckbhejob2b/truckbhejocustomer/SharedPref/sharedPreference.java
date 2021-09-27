package com.truckbhejob2b.truckbhejocustomer.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class sharedPreference {
    private static final String PREF_NAME = "truckbhejo_customer";
    private SharedPreferences.Editor editor;
    private static Preference mInstancePreference;
    private SharedPreferences preferences;

    private static final String IS_ALREADY_LOGIN = "is login";
    private static final String CUSTOMER_ID = "customerId";
    private static final String COMPANY_NAME = "CompanyName";
    private static final String COMPANY_ID = "companyId";
    private static final String CUSTOMER_NAME = "customerName";
    private static final String CUSTOMER_MOBILE = "customerMobile";
    private static final String CUSTOMER_EMAIL = "customerEmail";
    private static final String CUSTOMER_FCM = "fcmToken";
    private static final String CUSTOMER_API = "apiToken";
    private static final String CUSTOMER_NOTIFICATION = "notificationStatus";
    private static final String IS_FIRST_TIME="firstTime";



    public sharedPreference(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putInt(PREF_NAME, 0);
        editor.apply();
    }

    public static Preference getInstance(Context context) {
        if (mInstancePreference == null) {
            mInstancePreference = new Preference(context);
        }
        return mInstancePreference;
    }

    public void setCustomerNotification(boolean isOk){
        editor.putBoolean(CUSTOMER_NOTIFICATION,isOk).commit();
    }
    public boolean getCustomerNotification(){
        return preferences.getBoolean(CUSTOMER_NOTIFICATION,true);
    }

    public void setIsAlreadyLogin(boolean login) {
        editor.putBoolean(IS_ALREADY_LOGIN, login).commit();
    }

    public boolean getIsAlreadyLogin() {
        return preferences.getBoolean(IS_ALREADY_LOGIN, false);
    }

    public void setIsFirstTime(boolean firstTime){
        editor.putBoolean(IS_FIRST_TIME,firstTime).commit();
    }
    public boolean getIsFirstTime(){
        return preferences.getBoolean(IS_FIRST_TIME,true);
    }

    public void setCustomerId(String vendorId) {
        editor.putString(CUSTOMER_ID, vendorId).commit();
    }

    public String getCustomerId() {
        return preferences.getString(CUSTOMER_ID, "");
    }

    public void setCompanyId(String companyId){
        editor.putString(COMPANY_ID,companyId).commit();
    }
    public String getCompanyId(){
        return preferences.getString(COMPANY_ID,"");
    }
    public void setCompanyName(String companyName) {
        editor.putString(COMPANY_NAME, companyName).commit();
    }

    public String getCompanyName() {
        return preferences.getString(COMPANY_NAME, "");
    }


    public void setCustomerName(String contactPersonName) {
        editor.putString(CUSTOMER_NAME, contactPersonName).commit();
    }

    public String getCustomerName() {
        return preferences.getString(CUSTOMER_NAME, "");
    }

    public void setCustomerMobile(String contactPersonEmail) {
        editor.putString(CUSTOMER_MOBILE, contactPersonEmail).commit();
    }

    public String getCustomerMobile() {
        return preferences.getString(CUSTOMER_MOBILE, "");
    }

    public void setCustomerEmail(String contactPersonNumber) {
        editor.putString(CUSTOMER_EMAIL, contactPersonNumber).commit();
    }

    public String getCustomerEmail() {
        return preferences.getString(CUSTOMER_EMAIL, "");
    }

    public void setCustomerFcm(String fcmToken) {
        editor.putString(CUSTOMER_FCM, fcmToken).commit();
    }

    public String getCustomerFcm() {
        return preferences.getString(CUSTOMER_FCM, "");
    }

    public void setCustomerApi(String apiToken){
        editor.putString(CUSTOMER_API,apiToken);
    }
    public String getCustomerApi(){
        return preferences.getString(CUSTOMER_API,"");
    }


    public void clearAllPreferenceValues() {
        //editor.clear().commit();
        editor.remove(CUSTOMER_ID).commit();
        editor.remove(COMPANY_NAME).commit();
        editor.remove(CUSTOMER_NAME).commit();
        editor.remove(CUSTOMER_MOBILE).commit();
        editor.remove(CUSTOMER_EMAIL).commit();
        editor.remove(IS_ALREADY_LOGIN).commit();
        editor.remove(CUSTOMER_API).commit();
        editor.remove(CUSTOMER_ID).commit();
    }

}

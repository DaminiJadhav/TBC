package com.truckbhejob2b.truckbhejocustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;
import com.truckbhejob2b.truckbhejocustomer.SharedPref.sharedPreference;
import com.truckbhejob2b.truckbhejocustomer.Utils.WebUtils;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private sharedPreference sharedPreference;
    private static final String TAG = "Splash";
    private int SPLASH_SCREEN_TIME = 3000;
    private Toolbar toolbar;
    private Context mContext;
    private int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreference = new sharedPreference(this);
            checkRunTimePermission();

    }
    private void getFcmToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                           // Log.d(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        sharedPreference.setCustomerFcm(token);
                        WebUtils.registeredFcmToServer(SplashActivity.this);

//                        // Log and toast
//                        String msg = "Your fcm Token Is"+ token;
//                        Log.d(TAG, msg);
//                        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{ Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, PERMISSION_CODE);
        } else {
            splashScreen();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    splashScreen();
                } else {
                    // Log.d(TAG, "onRequestPermissionsResult: Dennied");
                    checkRunTimePermission();
                }
            }
        }
    }
    private void splashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreference.getIsAlreadyLogin()) {
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                    finish();
                    getFcmToken();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }

            }
        }, SPLASH_SCREEN_TIME);
    }

}
package com.truckbhejob2b.truckbhejocustomer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.truckbhejob2b.truckbhejocustomer.Fragments.DashboardFragment;
import com.truckbhejob2b.truckbhejocustomer.Utils.CustomProgressDialog;
import com.truckbhejob2b.truckbhejocustomer.Utils.NetworkUtil;
import com.truckbhejob2b.truckbhejocustomer.Utils.ResponseModel;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestUrl;
import com.truckbhejob2b.truckbhejocustomer.Utils.RetrofitAPI;
import com.truckbhejob2b.truckbhejocustomer.Volley.VolleySingleTone;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private Boolean exit = false;
    private Toolbar toolbar;
    private Context mContext;
    private String TAG = "ActivityLogin";
    private CustomProgressDialog cpd;
    private CardView mCardLogin;
    private EditText mEdtMobileNumber;
    private String strMobileNumber="";
    private android.app.AlertDialog Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        cpd = new CustomProgressDialog(mContext);
        mEdtMobileNumber = findViewById(R.id.edtOtp);
        mCardLogin = findViewById(R.id.cardViewLogin);
        mCardLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() ==R.id.cardViewLogin){
            strMobileNumber= mEdtMobileNumber.getText().toString().trim();
            hideSoftKeyboard(mEdtMobileNumber);
            if (!strMobileNumber.equals("")){
                if (strMobileNumber.length() < 10 ){
                    mEdtMobileNumber.setError(getString(R.string.valid_mobile_number));
                    toastMessage(getString(R.string.valid_mobile_number));
                }else {
                    if (NetworkUtil.getConnectivityStatus(this) !=NetworkUtil.TYPE_NOT_CONNECTED){
                        loginCustomer();
                    }else {
                        noInternetConnectionDialog();
                    }
                }
            }else {
                mEdtMobileNumber.setError(getString(R.string.valid_mobile_number));
                toastMessage(getString(R.string.valid_mobile_number));
            }
        }
    }
    private void loginCustomer(){
       final CustomProgressDialog customProgressDialog = new CustomProgressDialog(this);
       customProgressDialog.show();

        RequestBody mobileNumber = RequestBody.create(MultipartBody.FORM, strMobileNumber);
        OkHttpClient client1 = new OkHttpClient().newBuilder()
                .connectTimeout(80, TimeUnit.SECONDS)
                .readTimeout(80, TimeUnit.SECONDS)
                .writeTimeout(80, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RestUrl.PLACE_ORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client1)
                .build();
        RetrofitAPI client = retrofit.create(RetrofitAPI.class);
        Call<ResponseModel> call = client.getCustomerLogin(mobileNumber);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {

                if (response.isSuccessful()) {
                    customProgressDialog.hide();
                    if (response.body().getStatus().equals("1")) {
                        toastMessage(getString(R.string.otp_send));
                        Intent intent = new Intent(LoginActivity.this,OTPActivity.class);
                        intent.putExtra(RestKeys.MOBILE_NUMBER,strMobileNumber);
                        startActivity(intent);
                    } else if (response.body().getStatus().equals("2")){
                        showDialogNumberNotActive();
                    }else {
                        showDialog();
                    }
                } else {
                    customProgressDialog.hide();
                   toastMessage(getString(R.string.somethingWrong));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                customProgressDialog.hide();
               toastMessage(getString(R.string.somethingWrong));
            }
        });
    }
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    public void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    private void noInternetConnectionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name) + " "+getString(R.string.says));
        builder.setMessage(getString(R.string.msg_no_internet_connection));
        builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginCustomer();
                dialog.cancel();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }
    private void showDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View DialogView = factory.inflate(R.layout.custom_login_dialog, null);
        Dialog = new android.app.AlertDialog.Builder(this).create();
        Dialog.setCancelable(false);
        Dialog.setView(DialogView);
        TextView btnYes,btnNo;
        btnYes = DialogView.findViewById(R.id.txtViewYes);
        btnNo = DialogView.findViewById(R.id.txtViewNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,CustomerRegistration.class);
                intent.putExtra("MobileNumber",strMobileNumber);
                startActivity(intent);
            }
        });
       btnNo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Dialog.dismiss();
           }
       });
        Dialog.show();
    }
    private void showDialogNumberNotActive() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View DialogView = factory.inflate(R.layout.custom_number_not_active, null);
        Dialog = new android.app.AlertDialog.Builder(this).create();
        Dialog.setCancelable(false);
        Dialog.setView(DialogView);
        CardView btnYes;
        btnYes = DialogView.findViewById(R.id.cardViewOk);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Dialog.dismiss();
            }
        });
        Dialog.show();
    }
    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);
            finish();
            System.exit(0);
        } else {
            Toast.makeText(this, getString(R.string.back_button_press),
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
}
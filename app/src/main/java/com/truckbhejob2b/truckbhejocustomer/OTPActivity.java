package com.truckbhejob2b.truckbhejocustomer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.truckbhejob2b.truckbhejocustomer.SharedPref.sharedPreference;
import com.truckbhejob2b.truckbhejocustomer.Utils.CustomProgressDialog;
import com.truckbhejob2b.truckbhejocustomer.Utils.NetworkUtil;
import com.truckbhejob2b.truckbhejocustomer.Utils.ResponseModel;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestUrl;
import com.truckbhejob2b.truckbhejocustomer.Utils.RetrofitAPI;
import com.truckbhejob2b.truckbhejocustomer.Volley.VolleySingleTone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPActivity extends AppCompatActivity  {

    private EditText mEdtOtp;
    private CardView mCardViewLogin;
    private CustomProgressDialog customProgressDialog;
    private String TAG = "Customer";
    private String mobileNumber;
    private TextView mTxtViewMobile, mTxtViewTimer, mTxtCallUs, mTxtResendOtp;
    private int runTime;
    private Runnable runnable;
    private sharedPreference sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        sharedPreferences = new sharedPreference(this);

        mobileNumber = getIntent().getStringExtra(RestKeys.MOBILE_NUMBER);
        customProgressDialog = new CustomProgressDialog(OTPActivity.this);
        customProgressDialog.hide();
        mTxtViewMobile = findViewById(R.id.txtViewMobile);
        mTxtViewTimer = findViewById(R.id.txtTimer);
        mTxtCallUs = findViewById(R.id.txtViewCallus);
        mTxtResendOtp = findViewById(R.id.txtViewResend);
        mTxtResendOtp.setVisibility(View.GONE);

        mTxtViewMobile.setText(getString(R.string.sen_to) + " " + mobileNumber);
        timerThread(60);
        mEdtOtp = findViewById(R.id.edtOtp);
        mCardViewLogin = findViewById(R.id.cardViewLogin);

        // new OtpReceiver().setEditText(mEdtOtp,OTPActivity.this);
        mEdtOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String otp = mEdtOtp.getText().toString().trim();
                if (!otp.equals("")) {
                    if (otp.length()==6){
                        customProgressDialog.show();
                        verifyOtp(mobileNumber, otp);
                    }

                }
            }
        });
        mCardViewLogin.setOnClickListener(new loginClickListener());
        mTxtResendOtp.setOnClickListener(new resentOtpClickListener());
        mTxtCallUs.setOnClickListener(new needHelpListener());
    }

    class loginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (NetworkUtil.getConnectivityStatus(OTPActivity.this) != NetworkUtil.TYPE_NOT_CONNECTED) {
                if (mobileNumber != null) {
                    mEdtOtp.getText().toString().trim();
                    verifyOtp(mobileNumber, mEdtOtp.getText().toString().trim());
                } else {
                    Toast.makeText(OTPActivity.this, getString(R.string.somethingWrong), Toast.LENGTH_LONG).show();
                }
            } else {
                noInternetConnectionDialog();
            }
        }
    }

    private void noInternetConnectionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name) + " "+getString(R.string.says));
        builder.setMessage(getString(R.string.msg_no_internet_connection));
        builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customProgressDialog.show();
                verifyOtp(mobileNumber, mEdtOtp.getText().toString().trim());
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

    class resentOtpClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mTxtResendOtp.setVisibility(View.GONE);
            resentOTP();
           // timerThread(60);
        }
    }

    class needHelpListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //9021329329
            callToManager("9021329329");
        }
    }
    private void callToManager(String number){
        if (!number.equals("") && !number.equals("null")){
            if (!number.startsWith("+91") && !number.startsWith("0"))
                number = "+91" + number;
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }

    }
    private void resentOTP() {
        getOTP(mobileNumber);
        timerThread(120);
    }
    private void timerThread(int time) {
        runTime = time;
        final Handler handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (runTime == 0) {
                    mTxtResendOtp.setVisibility(View.VISIBLE);
                    handler.removeCallbacks(runnable);
                } else {
                    runTime--;
                    mTxtViewTimer.setText(String.valueOf(runTime));
                }

                handler.postDelayed(this, 1000);
            }
        };
        runnable.run();
    }

    private void verifyOtp(final String mobileNumber, final String otp) {
        customProgressDialog.show();
        String url = Uri.parse(RestUrl.VERIFY_OTP)
                .buildUpon()
                .appendQueryParameter(RestKeys.LOGIN_MOBILE_NUMBER, mobileNumber)
                .appendQueryParameter(RestKeys.VERIFY_OTP, otp)
                .appendQueryParameter(RestKeys.VERIFY_DEVICE_TOKEN,sharedPreferences.getCustomerFcm())
                .build().toString();
        StringRequest verifyOtp = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                customProgressDialog.hide();
              //  Log.d(TAG, "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                        JSONArray vendorDetails = jsonObject.getJSONArray(RestKeys.DATA);
                        JSONObject customer = vendorDetails.getJSONObject(0);
                        if (customer.has(RestKeys.RES_VERIFY_CUSTOMER_ID)) {
                            sharedPreferences.setCustomerId(customer.getString(RestKeys.RES_VERIFY_CUSTOMER_ID));
                        }
                        if (customer.has(RestKeys.RES_VERIFY_CUSTOMER_NAME)) {
                            sharedPreferences.setCustomerName(customer.getString(RestKeys.RES_VERIFY_CUSTOMER_NAME));
                        }
                        if (customer.has(RestKeys.RES_VERIFY_CUSTOMER_MOBILE)) {
                            sharedPreferences.setCustomerMobile(customer.getString(RestKeys.RES_VERIFY_CUSTOMER_MOBILE));
                        }
                        if (customer.has(RestKeys.RES_VERIFY_CUSTOMER_EMAIL)) {
                            sharedPreferences.setCustomerEmail(customer.getString(RestKeys.RES_VERIFY_CUSTOMER_EMAIL));
                        }
                        if (customer.has(RestKeys.RES_VERIFY_COMPANY_NAME)) {
                            sharedPreferences.setCompanyName(customer.getString(RestKeys.RES_VERIFY_COMPANY_NAME));
                        }
                        if (customer.has(RestKeys.RES_VERIFY_COMPANY_ID)) {
                            sharedPreferences.setCompanyId(customer.getString(RestKeys.RES_VERIFY_COMPANY_ID));
                        }
                        if (customer.has(RestKeys.RES_VERIFY_API_TOKEN)) {
                            sharedPreferences.setCustomerApi(customer.getString(RestKeys.RES_VERIFY_API_TOKEN));
                        }
                       sharedPreferences.setIsAlreadyLogin(true);

                        startActivity(new Intent(OTPActivity.this, DashboardActivity.class));

                    } else {
                        Toast.makeText(OTPActivity.this,jsonObject.getString(RestKeys.MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hide();
                Toast.makeText(OTPActivity.this, getString(R.string.somethingWrong), Toast.LENGTH_LONG).show();
                //Log.d(TAG, "onErrorResponse: " + error);
            }
        });

         VolleySingleTone.getInstance(this).getRequestQueue().add(verifyOtp);

    }
    private void getOTP(String mobileNumber) {

        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.show();
        RequestBody strMobileNumber = RequestBody.create(MultipartBody.FORM, mobileNumber);
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
        Call<ResponseModel> call = client.getCustomerLogin(strMobileNumber);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {

                if (response.isSuccessful()) {
                    customProgressDialog.hide();
                    if (response.body().getStatus().equals("1")) {
                        Toast.makeText(OTPActivity.this,getString(R.string.otp_send),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OTPActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        startActivity(new Intent(OTPActivity.this,LoginActivity.class));
                    }
                } else {
                    customProgressDialog.hide();
                    Toast.makeText(OTPActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                customProgressDialog.hide();
                Toast.makeText(OTPActivity.this,getString(R.string.somethingWrong),Toast.LENGTH_LONG).show();

            }
        });
    }

}
package com.truckbhejob2b.truckbhejocustomer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerRegistration extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText mEdtTextCompanyName, mEdtTextCustomerName, mEdtTextCustomerNumber, mEdtTextCustomerEmail, mEdtTextCustomerAddress;
    private String strCustomerName = "", strCustomerNumber = "", strCustomerEmail = "", strCustomerAddress = "", strCompanyName = "";
    private CardView cardViewSubmit;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String TAG = "ActivityLogin";
    private android.app.AlertDialog Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registartion);
        strCustomerNumber = getIntent().getStringExtra("MobileNumber");

        mEdtTextCompanyName = findViewById(R.id.TextInputEditTextCompanyName);
        mEdtTextCustomerName = findViewById(R.id.TextInputEditTextCustomerName);
        mEdtTextCustomerNumber = findViewById(R.id.TextInputEditTextCustomerNumber);
        mEdtTextCustomerEmail = findViewById(R.id.TextInputEditTextCustomerEmail);
        mEdtTextCustomerAddress = findViewById(R.id.TextInputEditTextCustomerAddress);


        cardViewSubmit = findViewById(R.id.btnRegister);
        cardViewSubmit.setOnClickListener(this);
        mEdtTextCustomerNumber.setText(strCustomerNumber);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            hideSoftKeyboard(cardViewSubmit);
            strCompanyName = mEdtTextCompanyName.getText().toString().trim();
            strCustomerName = mEdtTextCustomerName.getText().toString().trim();
            strCustomerNumber = mEdtTextCustomerNumber.getText().toString().trim();
            strCustomerEmail = mEdtTextCustomerEmail.getText().toString().trim();
            strCustomerAddress = mEdtTextCustomerAddress.getText().toString().trim();
            validateData();
        }
    }

    //    private void getCompanyList(){
//        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(this);
//        customProgressDialog.show();
//        StringRequest getCompany = new StringRequest(Request.Method.GET, RestUrl.COMPANY_LIST, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                customProgressDialog.hide();
//                try {
//                    JSONObject res = new JSONObject(response);
//                    if(res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)){
//                        JSONArray data = res.getJSONArray(RestKeys.DATA);
//                        for (int i = 0;i<data.length();i++){
//                            JSONObject company = data.getJSONObject(i);
//                            companyName.add(company.getString("companyName"));
//                        }
//                        mCompanySpinner.setAdapter(dataAdapter);
//                    }else {
//                        toastMessage("Our Server is busy now, please try again later..");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                customProgressDialog.show();
//                toastMessage("Our Server is busy now, please try again later..");
//            }
//        });
//        VolleySingleTone.getInstance(this).getRequestQueue().add(getCompany);
//
//    }
    private void validateData() {
        if (!strCompanyName.equals("") && !strCustomerName.equals("") && !strCustomerNumber.equals("") && !strCustomerEmail.equals("") && !strCustomerAddress.equals("") && !strCompanyName.equals("Company Name") && strCustomerNumber.length() == 10 && strCustomerEmail.matches(emailPattern)) {
            if (NetworkUtil.getConnectivityStatus(this) != NetworkUtil.TYPE_NOT_CONNECTED) {
                sendRequestToAdmin();
            } else {
                //  networkState = 2;
                noInternetConnectionDialog();
            }
        } else if (strCompanyName.equals("")) {
            toastMessage(getString(R.string.error_company));

        } else if (strCompanyName.equals("Company Name")) {
            toastMessage(getString(R.string.error_company));

        } else if (strCustomerName.equals("")) {
            toastMessage(getString(R.string.error_customer_name));
            mEdtTextCustomerName.setError(getString(R.string.error_customer_name));
        } else if (strCustomerNumber.equals("")) {
            toastMessage(getString(R.string.error_customer_number));
            mEdtTextCustomerNumber.setError(getString(R.string.error_customer_number));
        } else if (strCustomerNumber.length() < 10 || strCustomerNumber.length() > 10) {
            toastMessage(getString(R.string.error_validate));
            mEdtTextCustomerNumber.setError(getString(R.string.error_validate));
        } else if (strCustomerEmail.equals("")) {
            toastMessage(getString(R.string.error_customer_email));
            mEdtTextCustomerEmail.setError(getString(R.string.error_customer_email));
        } else if (strCustomerAddress.equals("")) {
            toastMessage(getString(R.string.error_customer_address));
            mEdtTextCustomerAddress.setError(getString(R.string.error_customer_address));
        } else if (!strCustomerEmail.matches(emailPattern)) {
            toastMessage(getString(R.string.error_validate_email));
            mEdtTextCustomerEmail.setError(getString(R.string.error_validate_email));
        } else {
            toastMessage(getString(R.string.error_all));
            mEdtTextCustomerName.setError(getString(R.string.error_customer_name));
            mEdtTextCustomerNumber.setError(getString(R.string.error_customer_number));
            mEdtTextCustomerEmail.setError(getString(R.string.error_customer_email));
            mEdtTextCustomerAddress.setError(getString(R.string.error_customer_address));
        }
    }

//    private void sendRequestToAdmin() {
////        Log.d(TAG, "sendRequestToAdmin: ");
//        cardViewSubmit.setVisibility(View.GONE);
//        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(this);
//        customProgressDialog.show();
//        RequestBody companyName = RequestBody.create(MultipartBody.FORM, strCompanyName);
//        RequestBody customerName = RequestBody.create(MultipartBody.FORM, strCustomerName);
//        RequestBody customerNumber = RequestBody.create(MultipartBody.FORM, strCustomerNumber);
//        RequestBody customerEmail = RequestBody.create(MultipartBody.FORM, strCustomerEmail);
//        RequestBody customerAddress = RequestBody.create(MultipartBody.FORM, strCustomerAddress);
//
//        OkHttpClient client1 = new OkHttpClient().newBuilder()
//                .connectTimeout(80, TimeUnit.SECONDS)
//                .readTimeout(80, TimeUnit.SECONDS)
//                .writeTimeout(80, TimeUnit.SECONDS)
//                .build();
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(RestUrl.PLACE_ORDER)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                //.addConverterFactory(GsonConverterFactory.create(gson))
//                .client(client1)
//                .build();
//        RetrofitAPI client = retrofit.create(RetrofitAPI.class);
//        Call<ResponseModel> call = client.customerRegistration(companyName, customerName, customerNumber, customerEmail, customerAddress);
//        call.enqueue(new Callback<ResponseModel>() {
//            @Override
//            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
//                customProgressDialog.hide();
//                //Log.d(TAG, "onResponse: "+response.body());
//                if (response.isSuccessful()) {
//                    if (response.body().getSuccess().equals("1")) {
//                        toastMessage(response.body().getMessage());
//                        startActivity(new Intent(CustomerRegistration.this, LoginActivity.class));
//                    } else {
//                        cardViewSubmit.setVisibility(View.VISIBLE);
//                        toastMessage(response.body().getMessage());
//                    }
//                } else {
//                    cardViewSubmit.setVisibility(View.VISIBLE);
//                    toastMessage(response.body().getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                customProgressDialog.hide();
//                cardViewSubmit.setVisibility(View.VISIBLE);
//                toastMessage(getString(R.string.somethingWrong));
//             //   toastMessage(""+call);
////                Log.d(TAG, "onFailure: "+t.getMessage());
//            }
//        });
//    }
    private void sendRequestToAdmin(){
        final CustomProgressDialog cpd = new CustomProgressDialog(this);
        cpd.show();
        String Uri = android.net.Uri.parse(RestUrl.CUSTOMER_REGISTRATION)
                .buildUpon()
                .build().toString();
//       Log.d(TAG, "loginCustomer: "+Uri);
        final StringRequest login = new StringRequest(Request.Method.POST, Uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cpd.hide();
               // Log.d(TAG, "onResponse: "+response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString("success").equals("1")){
                        showDialog();
//                        toastMessage(res.getString(RestKeys.MESSAGE));
//                        Intent intent = new Intent(CustomerRegistration.this,LoginActivity.class);
//                        startActivity(intent);
                    }else {
                        toastMessage(res.getString(RestKeys.MESSAGE));
                        Intent intent = new Intent(CustomerRegistration.this,LoginActivity.class);
                        startActivity(intent);
//                            startActivity(new Intent(LoginActivity.this,CustomerRegistration.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.hide();
                toastMessage(getString(R.string.somethingWrong));
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(RestKeys.REG_COMPANY_NAME,strCompanyName);
                params.put(RestKeys.REG_CUSTOMER_NAME,strCustomerName);
                params.put(RestKeys.REG_CUSTOMER_NUMBER,strCustomerNumber);
                params.put(RestKeys.REG_CUSTOMER_EMAIL,strCustomerEmail);
                params.put(RestKeys.REG_CUSTOMER_ADDRESS,strCustomerAddress);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        new VolleySingleTone(this).getRequestQueue().add(login);
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void noInternetConnectionDialog() {

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name) + " " + getString(R.string.says));
        builder.setMessage(getString(R.string.msg_no_internet_connection));
        builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                validateData();
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
        final View DialogView = factory.inflate(R.layout.custom_register_dialog, null);
        Dialog = new android.app.AlertDialog.Builder(this).create();
        Dialog.setCancelable(false);
        Dialog.setView(DialogView);
        CardView btnOk;
        btnOk = DialogView.findViewById(R.id.cardViewOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRegistration.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        Dialog.show();
    }
}
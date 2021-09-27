package com.truckbhejob2b.truckbhejocustomer.Fragments;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.truckbhejob2b.truckbhejocustomer.DashboardActivity;
import com.truckbhejob2b.truckbhejocustomer.R;
import com.truckbhejob2b.truckbhejocustomer.SharedPref.sharedPreference;
import com.truckbhejob2b.truckbhejocustomer.Utils.CustomProgressDialog;
import com.truckbhejob2b.truckbhejocustomer.Utils.NetworkUtil;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestUrl;
import com.truckbhejob2b.truckbhejocustomer.Volley.VolleySingleTone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SettingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //User Define
    private View rootView;
    private CustomProgressDialog customProgressDialog;
    private sharedPreference sharedPreference;

    private TextView txtViewMobileNumber,txtViewEmail,txtViewCustomerName,txtViewCompanyName,txtViewAddress;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_setting, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setUpUi();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).enableBackButton(true, getString(R.string.fragment_setting));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DashboardActivity) getActivity()).enableBackButton(false, getString(R.string.fragment_dashboard));
    }
    private void setUpUi(){
        customProgressDialog = new CustomProgressDialog(getActivity());
        sharedPreference = new sharedPreference(getActivity());

        txtViewMobileNumber = rootView.findViewById(R.id.txtViewMobile);
        txtViewEmail = rootView.findViewById(R.id.txtViewEmail);
        txtViewCustomerName = rootView.findViewById(R.id.txtViewCustomerName);
        txtViewCompanyName = rootView.findViewById(R.id.txtViewCompanyName);
        txtViewAddress = rootView.findViewById(R.id.txtViewAddress);

        if (NetworkUtil.getConnectivityStatus(getActivity()) != NetworkUtil.TYPE_NOT_CONNECTED){
            customProgressDialog.show();
            getUserDetails();
        }else {
            noInternetConnectionDialog();
        }
    }
    private void noInternetConnectionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.app_name) + " " + getString(R.string.says));
        builder.setMessage(getString(R.string.msg_no_internet_connection));
        builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customProgressDialog.show();
                getUserDetails();
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

    private void getUserDetails(){
        String url = Uri.parse(RestUrl.USER_SETTING)
                .buildUpon()
                .appendQueryParameter(RestKeys.SETTING_CUSTOMER_ID,sharedPreference.getCustomerId())
                .build().toString();
        StringRequest getUser = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            customProgressDialog.hide();
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)){
                        JSONArray data = res.getJSONArray(RestKeys.DATA);
                        JSONObject user = data.getJSONObject(0);
                        if (user.has(RestKeys.RES_SETTING_CUSTOMER_NAME)){
                            txtViewCustomerName.setText(user.getString(RestKeys.RES_SETTING_CUSTOMER_NAME));
                        }
                        if (user.has(RestKeys.RES_SETTING_COMPANY_NAME)){
                            txtViewCompanyName.setText(user.getString(RestKeys.RES_SETTING_COMPANY_NAME));
                        }
                        if (user.has(RestKeys.RES_SETTING_CUSTOMER_EMAIL)){
                            txtViewEmail.setText(user.getString(RestKeys.RES_SETTING_CUSTOMER_EMAIL));
                        }
                        if (user.has(RestKeys.RES_SETTING_CUSTOMER_CONTACT)){
                            txtViewMobileNumber.setText(user.getString(RestKeys.RES_SETTING_CUSTOMER_CONTACT));
                        }
                        if (user.has(RestKeys.RES_SETTING_CUSTOMER_ADDRESS)){
                            txtViewAddress.setText(user.getString(RestKeys.RES_SETTING_CUSTOMER_ADDRESS));
                        }
                    }else {
                        ((DashboardActivity)getActivity()).setSnackBar(getString(R.string.somethingWrong));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hide();
                ((DashboardActivity)getActivity()).setSnackBar(getString(R.string.somethingWrong));

            }
        });
        new VolleySingleTone(getActivity()).getRequestQueue().add(getUser);
    }
}
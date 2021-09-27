package com.truckbhejob2b.truckbhejocustomer.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.truckbhejob2b.truckbhejocustomer.Adapter.AdapterNotification;
import com.truckbhejob2b.truckbhejocustomer.DashboardActivity;
import com.truckbhejob2b.truckbhejocustomer.Model.NotificationModel;
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

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private RecyclerView recyclerView;
    private AdapterNotification adapterNotification;
    private List<NotificationModel> notificationModelsList;
    private Context mContext;
    private LinearLayoutManager mLayoutManager;
    private int currentItem,scrolledItem,totalItem;
    private Boolean isScroll = false;
    private int offSet = 0;
    private int pageSize = 10;
    private CustomProgressDialog cpd;
    private sharedPreference sharedPreference;
    private TextView txtViewNo;
    private boolean isLoading = false;

    public NotificationFragment() {
        // Required empty public constructor
    }


    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        rootView= inflater.inflate(R.layout.fragment_notification, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setUpUi();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((DashboardActivity)getActivity()).enableBackButton(true,getString(R.string.menu_notification));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DashboardActivity) getActivity()).enableBackButton(false, getString(R.string.fragment_dashboard));
    }

    private void setUpUi(){
        cpd = new CustomProgressDialog(getActivity());
        sharedPreference = new sharedPreference(getActivity());
        recyclerView = rootView.findViewById(R.id.recyclerViewNotification);
        notificationModelsList = new ArrayList<>();
        txtViewNo = rootView.findViewById(R.id.txtViewNoNotification);
        setRecyclerView();
        if (NetworkUtil.getConnectivityStatus(getActivity())!=NetworkUtil.TYPE_NOT_CONNECTED){
            getNotification(offSet*pageSize);
            cpd.show();
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
                cpd.show();
                getNotification(offSet*pageSize);
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

    private void setRecyclerView(){
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapterNotification = new AdapterNotification(getActivity(),notificationModelsList);
        recyclerView.setAdapter(adapterNotification);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScroll = true;
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = mLayoutManager.getChildCount();
                totalItem = mLayoutManager.getItemCount();
                scrolledItem = mLayoutManager.findFirstVisibleItemPosition();
                if (isScroll && !isLoading){
                    if (currentItem +scrolledItem == totalItem){
                        isLoading = true;
                        offSet ++;
                        getNotification(offSet*pageSize);
                        cpd.show();
                    }
                }
            }
        });
    }

    private void getNotification(final int offSet){
        String url = Uri.parse(RestUrl.NOTIFICATION)
                .buildUpon()
                .appendQueryParameter(RestKeys.SETTING_CUSTOMER_ID,sharedPreference.getCustomerId())
                .appendQueryParameter(RestKeys.TRIPS_OFFSET, String.valueOf(offSet))
                .build().toString();
        StringRequest getNotifications =  new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cpd.hide();
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)){
                        JSONArray jsonArray = res.getJSONArray(RestKeys.DATA);
                        isLoading=false;
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject notification = jsonArray.getJSONObject(i);
                            NotificationModel notificationModel = new NotificationModel();
                            if (notification.has(RestKeys.NOTIFICATION_TITLE)){
                                notificationModel.setnTitle(notification.getString(RestKeys.NOTIFICATION_TITLE));
                            }
                            if (notification.has(RestKeys.NOTIFICATION_DESC)){
                                notificationModel.setnDesc(notification.getString(RestKeys.NOTIFICATION_DESC));
                            }
                            if (notification.has(RestKeys.NOTIFICATION_ACTION)){
                                notificationModel.setnAction(notification.getString(RestKeys.NOTIFICATION_ACTION));
                            }
                            if (notification.has(RestKeys.NOTIFICATION_CREATED_ON)){
                                notificationModel.setnDate(notification.getString(RestKeys.NOTIFICATION_CREATED_ON));
                            }
                            notificationModelsList.add(notificationModel);
                            adapterNotification.notifyDataSetChanged();

                        }

                    }else {
                        if (offSet == 0){
                            txtViewNo.setVisibility(View.VISIBLE);
                        }else {
                            ((DashboardActivity)getActivity()).setSnackBar(getString(R.string.no_mor_notifications));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.hide();
                ((DashboardActivity)getActivity()).setSnackBar(getString(R.string.somethingWrong));
            }
        });
        new VolleySingleTone(getActivity()).getRequestQueue().add(getNotifications);
    }
}
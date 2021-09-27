package com.truckbhejob2b.truckbhejocustomer.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.Dash;
import com.truckbhejob2b.truckbhejocustomer.Adapter.AdapterTrips;
import com.truckbhejob2b.truckbhejocustomer.DashboardActivity;
import com.truckbhejob2b.truckbhejocustomer.Model.Trips;
import com.truckbhejob2b.truckbhejocustomer.R;
import com.truckbhejob2b.truckbhejocustomer.SharedPref.sharedPreference;
import com.truckbhejob2b.truckbhejocustomer.Utils.CustomProgressDialog;
import com.truckbhejob2b.truckbhejocustomer.Utils.NetworkUtil;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestUrl;
import com.truckbhejob2b.truckbhejocustomer.Utils.getCityName;
import com.truckbhejob2b.truckbhejocustomer.Volley.VolleySingleTone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripsFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private sharedPreference sharedPreference;
    private CustomProgressDialog customProgressDialog;
    private String TAG = "Dashboard";
    private View rootView;
    private LinearLayout linearLayoutAll, linearLayoutRunning, linearLayoutDelivered;
    private ImageView allImageView, runningImageView, deliveredImageView;
    private TextView lLAll, lLRunning, lLDelivered;
    private RecyclerView mRecyclerView;
    private AdapterTrips adapterTrips;
    private List<Trips> tripsList;
    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling = false;
    private boolean isLoading = false;
    private int currentItem, scrolledItem, totalItem;
    private int offSet = 0;
    private int pageSize = 5;
    private Context mContext;
    private boolean isSearching = false;
    private String tripsStatus = "6";
    private int layoutStatus = 0;

    private int layoutId = 1;

    private String searchText = "";

    public TripsFragment() {
        // Required empty public constructor
    }
    public static TripsFragment newInstance(String param1, String param2) {
        TripsFragment fragment = new TripsFragment();
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
            searchText =getArguments().getString("SearchText");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_trips, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setUpUi();
        mContext = getActivity();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).enableBackButton(true,getString(R.string.trips_fragment));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DashboardActivity)getActivity()).enableBackButton(false,getString(R.string.fragment_dashboard));
    }

    private void setUpUi(){
        setHasOptionsMenu(true);
        sharedPreference = new sharedPreference(getActivity());
        customProgressDialog = new CustomProgressDialog(getActivity());
        customProgressDialog.onWindowFocusChanged(true);
        tripsList = new ArrayList<>();
        linearLayoutAll = rootView.findViewById(R.id.linLay_All);
        linearLayoutRunning = rootView.findViewById(R.id.linLay_Running);
        linearLayoutDelivered = rootView.findViewById(R.id.linLay_Delivery);
        allImageView = rootView.findViewById(R.id.img_All);
        runningImageView = rootView.findViewById(R.id.img_Running);
        deliveredImageView = rootView.findViewById(R.id.img_Delivery);
        lLAll = rootView.findViewById(R.id.tv_All);
        lLRunning = rootView.findViewById(R.id.tv_Running);
        lLDelivered = rootView.findViewById(R.id.tv_Delivery);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewDash);
        linearLayoutAll.setOnClickListener(this);
        linearLayoutRunning.setOnClickListener(this);
        linearLayoutDelivered.setOnClickListener(this);
        setRecyclerViewProperties();
        if (NetworkUtil.getConnectivityStatus(getActivity()) != NetworkUtil.TYPE_NOT_CONNECTED) {
            customProgressDialog.show();
            if (searchText.equals("")){
                getTrips(tripsStatus,offSet*pageSize);
            }else {
                searchTrips(searchText,offSet*pageSize);
                isSearching = true;
            }
        } else {
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        item.setVisible(true);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  Log.d(TAG, "onQueryTextSubmit: " + query);
                if (query != null) {
                    if (NetworkUtil.getConnectivityStatus(getContext()) != NetworkUtil.TYPE_NOT_CONNECTED) {
                        offSet = 0;
                        isSearching = true;
                        searchText = query.toUpperCase();
                        tripsList.clear();
                        adapterTrips.notifyDataSetChanged();
                        searchView.clearFocus();
                        searchTrips(searchText,pageSize*offSet);
                    }
                } else {
                    ((DashboardActivity) getContext()).setSnackBar("Please Enter some text to search");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Log.d(TAG, "onQueryTextChange: "+newText);

                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.search));

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                    switch (layoutStatus){
                        case 0:
                            allLinearClick();
                            break;
                        case 1:
                            runningClick();
                            break;
                        case 2:
                            deliveredClick();
                            break;
                    }

                return false;
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linLay_All:
                allLinearClick();
                break;
            case R.id.linLay_Running:
                runningClick();
                break;
            case R.id.linLay_Delivery:
                deliveredClick();
                break;

        }
    }
    private void getTrips(String tripsStatus, final int finalOffSet) {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(getActivity());
        customProgressDialog.show();
        String getUri = Uri.parse(RestUrl.GET_DASH_TRIPS)
                .buildUpon()
                .appendQueryParameter(RestKeys.TRIPS_COMPANY_ID, sharedPreference.getCompanyId())
                .appendQueryParameter(RestKeys.TRIPS_STATUS, tripsStatus)
                .appendQueryParameter(RestKeys.TRIPS_OFFSET, String.valueOf(finalOffSet))
                .build().toString();
        //   Log.d(TAG, "getTrips: "+getUri);
        StringRequest getTrips = new StringRequest(Request.Method.GET, getUri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                        JSONArray getTripsArray = res.getJSONArray(RestKeys.DATA);
                        for (int i = 0; i < getTripsArray.length(); i++) {
                            JSONObject trip = getTripsArray.getJSONObject(i);
                            Trips trips = new Trips();
                            isLoading = false;
                            if (trip.has(RestKeys.RES_TRIPS_ORDER_ID)) {
                                trips.setOrderId(trip.getString(RestKeys.RES_TRIPS_ORDER_ID));
                            }
                            if (trip.has(RestKeys.RES_TRIPS_ORDER_DATE)) {
                                trips.setOrderDate(trip.getString(RestKeys.RES_TRIPS_ORDER_DATE));
                            }
                            if (trip.has(RestKeys.RES_TRIPS_PRODUCT_TYPE)) {
                                trips.setProductType(trip.getString(RestKeys.RES_TRIPS_PRODUCT_TYPE));
                            }
                            if (trip.has(RestKeys.RES_TRIPS_FROM_LAT) && trip.has(RestKeys.RES_TRIPS_FROM_LONG)) {
                                String fromLat = trip.getString(RestKeys.RES_TRIPS_FROM_LAT);
                                String fromLong = trip.getString(RestKeys.RES_TRIPS_FROM_LONG);
                                trips.setFromLocation(getCityName.getCityName(getActivity(), fromLat, fromLong));
                            }
                            if (trip.has(RestKeys.RES_TRIPS_TO3_LAT) && trip.has(RestKeys.RES_TRIPS_TO3_LONG)) {
                                String to3Lat = trip.getString(RestKeys.RES_TRIPS_TO3_LAT);
                                String to3Long = trip.getString(RestKeys.RES_TRIPS_TO3_LONG);
                                if (!to3Lat.equals("") && !to3Long.equals("")) {
                                    trips.setToLocation(getCityName.getCityName(getActivity(), to3Lat, to3Long));
                                } else {
                                    String to2Lat = trip.getString(RestKeys.RES_TRIPS_TO2_LAT);
                                    String to2Long = trip.getString(RestKeys.RES_TRIPS_TO2_LONG);
                                    if (!to2Lat.equals("") && !to2Long.equals("")) {
                                        trips.setToLocation(getCityName.getCityName(getActivity(), to2Lat, to2Long));
                                    } else {
                                        String toLat = trip.getString(RestKeys.RES_TRIPS_TO_LAT);
                                        String toLong = trip.getString(RestKeys.RES_TRIPS_TO_LONG);
                                        trips.setToLocation(getCityName.getCityName(getActivity(), toLat, toLong));
                                    }

                                }
                            }
                            if (trip.has(RestKeys.RES_TRIPS_VEHICLE_TYPE)) {
                                trips.setVehicleType(trip.getString(RestKeys.RES_TRIPS_VEHICLE_TYPE));
                            }
                            if (trip.has(RestKeys.RES_TRIPS_DRIVER_NUMBER)) {
                                String driverNumber = trip.getString(RestKeys.RES_TRIPS_DRIVER_NUMBER);
                                if (driverNumber.equals("null") || driverNumber.equals("")) {
                                    trips.setDriverNumber("");
                                } else {
                                    trips.setDriverNumber(driverNumber);
                                }
                            }
                            if (trip.has(RestKeys.RES_TRIPS_VEHICLE_NUMBER)) {
                                String vehicleNumber = trip.getString(RestKeys.RES_TRIPS_VEHICLE_NUMBER).toUpperCase();
                                if (vehicleNumber.equals("null") || vehicleNumber.equals("")) {
                                    trips.setVehicleNumber("");
                                } else {
                                    trips.setVehicleNumber(vehicleNumber);
                                }
                            }
                            if (trip.has(RestKeys.RES_TRIPS_TRACK_STATUS)) {
                                String trackStatus = trip.getString(RestKeys.RES_TRIPS_TRACK_STATUS);
                                if (trackStatus.equals("null") || trackStatus.equals("")) {
                                    trips.setTrackStatus("NO");
                                } else {
                                    trips.setTrackStatus(trackStatus);
                                }
                            }
                            if (trip.has(RestKeys.RES_TRIPS_ORDER_STATUS)) {
                                String orderStatus = trip.getString(RestKeys.RES_TRIPS_ORDER_STATUS);
                                switch (orderStatus) {
                                    case "0":
                                        String podStatus = trip.getString(RestKeys.RES_TRIPS_POD_STATUS);
                                        if (podStatus.equals("1")) {
                                            trips.setPodStatus("8");
                                        } else {
                                            trips.setPodStatus("9");
                                        }
                                        trips.setOrderStatus("0");
                                        break;
                                    case "1":
                                        trips.setOrderStatus("1");
                                        break;
                                    case "2":
                                        trips.setOrderStatus("2");
                                        break;
                                    case "3":
                                        trips.setOrderStatus("3");
                                        break;
                                    case "4":
                                        trips.setOrderStatus("4");
                                        break;
                                }
                            }

                            tripsList.add(trips);
                            adapterTrips.notifyDataSetChanged();
                            customProgressDialog.hide();
                            customProgressDialog.onWindowFocusChanged(false);
                        }
                    } else {
                        if (finalOffSet == 0){
                            ((DashboardActivity) getActivity()).setSnackBar(res.getString(RestKeys.MESSAGE));
                        }else {
                            ((DashboardActivity) getActivity()).setSnackBar(getString(R.string.no_more_orders));
                        }
                        customProgressDialog.hide();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((DashboardActivity) getActivity()).setSnackBar(getString(R.string.somethingWrong));
                customProgressDialog.hide();
            }
        });
        new VolleySingleTone(getActivity()).getRequestQueue().add(getTrips);
    }
    private void setRecyclerViewProperties() {
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapterTrips = new AdapterTrips(getActivity(), tripsList);
        mRecyclerView.setAdapter(adapterTrips);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = linearLayoutManager.getChildCount();
                totalItem = linearLayoutManager.getItemCount();
                scrolledItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && !isLoading) {
                    if (currentItem + scrolledItem == totalItem) {
                        isLoading = true;
                        offSet++;
                        if (isSearching) {
                            searchTrips(searchText, offSet * pageSize);
                        } else {
                            getTrips(tripsStatus, offSet * pageSize);
                            //customProgressDialog.show();
                        }
                    }
                }
            }
        });
    }
    private void allLinearClick() {
        isSearching = false;
        layoutStatus = 0;
//        lLAll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        allImageView.setVisibility(View.VISIBLE);
        runningImageView.setVisibility(View.GONE);
        deliveredImageView.setVisibility(View.GONE);
//        lLRunning.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//        lLDelivered.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        offSet = 0;
        tripsStatus = "6";
        tripsList.clear();
        adapterTrips.notifyDataSetChanged();
        getTrips(tripsStatus, offSet * pageSize);
        //  customProgressDialog.show();
    }

    private void runningClick() {
        isSearching = false;
        layoutStatus = 1;
//        lLRunning.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        allImageView.setVisibility(View.GONE);
        runningImageView.setVisibility(View.VISIBLE);
        deliveredImageView.setVisibility(View.GONE);
//        lLAll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//        lLDelivered.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        offSet = 0;
        tripsStatus = "4";
        tripsList.clear();
        adapterTrips.notifyDataSetChanged();
        getTrips(tripsStatus, offSet * pageSize);
        //customProgressDialog.show();

    }

    private void deliveredClick() {
        isSearching = false;
        layoutStatus = 2;
//        lLDelivered.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        allImageView.setVisibility(View.GONE);
        runningImageView.setVisibility(View.GONE);
        deliveredImageView.setVisibility(View.VISIBLE);
//        lLAll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//        lLRunning.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        offSet = 0;
        tripsStatus = "0";
        tripsList.clear();
        adapterTrips.notifyDataSetChanged();
        getTrips(tripsStatus, offSet * pageSize);
        //    customProgressDialog.show();
    }
    private void searchTrips(String searchText, final int offset) {
        isSearching = true;
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(getActivity());
        customProgressDialog.show();
        String searchUri = Uri.parse(RestUrl.SEARCH_TRIP)
                .buildUpon()
                .appendQueryParameter(RestKeys.SEARCH_TEXT, searchText)
                .appendQueryParameter(RestKeys.TRIPS_COMPANY_ID, sharedPreference.getCompanyId())
                .appendQueryParameter(RestKeys.TRIPS_OFFSET, String.valueOf(offset))
                .build().toString();
        //  Log.d(TAG, "searchTrips: "+searchUri);
        StringRequest searchTrip = new StringRequest(Request.Method.GET, searchUri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                        JSONArray getTripsArray = res.getJSONArray(RestKeys.DATA);
                        for (int i = 0; i < getTripsArray.length(); i++) {
                            JSONObject trip = getTripsArray.getJSONObject(i);
                            Trips trips = new Trips();
                            isLoading = false;
                            if (trip.has(RestKeys.RES_TRIPS_ORDER_ID)) {
                                trips.setOrderId(trip.getString(RestKeys.RES_TRIPS_ORDER_ID));
                            }
                            if (trip.has(RestKeys.RES_TRIPS_ORDER_DATE)) {
                                trips.setOrderDate(trip.getString(RestKeys.RES_TRIPS_ORDER_DATE));
                            }
                            if (trip.has(RestKeys.RES_TRIPS_PRODUCT_TYPE)) {
                                trips.setProductType(trip.getString(RestKeys.RES_TRIPS_PRODUCT_TYPE));
                            }
                            if (trip.has(RestKeys.RES_TRIPS_FROM_LAT) && trip.has(RestKeys.RES_TRIPS_FROM_LONG)) {
                                String fromLat = trip.getString(RestKeys.RES_TRIPS_FROM_LAT);
                                String fromLong = trip.getString(RestKeys.RES_TRIPS_FROM_LONG);
                                trips.setFromLocation(getCityName.getCityName(getActivity(), fromLat, fromLong));
                            }
                            if (trip.has(RestKeys.RES_TRIPS_TO3_LAT) && trip.has(RestKeys.RES_TRIPS_TO3_LONG)) {
                                String to3Lat = trip.getString(RestKeys.RES_TRIPS_TO3_LAT);
                                String to3Long = trip.getString(RestKeys.RES_TRIPS_TO3_LONG);
                                if (!to3Lat.equals("") && !to3Long.equals("")) {
                                    trips.setToLocation(getCityName.getCityName(getActivity(), to3Lat, to3Long));
                                } else {
                                    String to2Lat = trip.getString(RestKeys.RES_TRIPS_TO2_LAT);
                                    String to2Long = trip.getString(RestKeys.RES_TRIPS_TO2_LONG);
                                    if (!to2Lat.equals("") && !to2Long.equals("")) {
                                        trips.setToLocation(getCityName.getCityName(getActivity(), to2Lat, to2Long));
                                    } else {
                                        String toLat = trip.getString(RestKeys.RES_TRIPS_TO_LAT);
                                        String toLong = trip.getString(RestKeys.RES_TRIPS_TO_LONG);
                                        trips.setToLocation(getCityName.getCityName(getActivity(), toLat, toLong));
                                    }

                                }
                            }
                            if (trip.has(RestKeys.RES_TRIPS_VEHICLE_TYPE)) {
                                trips.setVehicleType(trip.getString(RestKeys.RES_TRIPS_VEHICLE_TYPE));
                            }
                            if (trip.has(RestKeys.RES_TRIPS_DRIVER_NUMBER)) {
                                String driverNumber = trip.getString(RestKeys.RES_TRIPS_DRIVER_NUMBER);
                                if (driverNumber.equals("null") || driverNumber.equals("")) {
                                    trips.setDriverNumber("");
                                } else {
                                    trips.setDriverNumber(driverNumber);
                                }
                            }
                            if (trip.has(RestKeys.RES_TRIPS_VEHICLE_NUMBER)) {
                                String vehicleNumber = trip.getString(RestKeys.RES_TRIPS_VEHICLE_NUMBER).toUpperCase();
                                if (vehicleNumber.equals("null") || vehicleNumber.equals("")) {
                                    trips.setVehicleNumber("");
                                } else {
                                    trips.setVehicleNumber(vehicleNumber);
                                }
                            }
                            if (trip.has(RestKeys.RES_TRIPS_TRACK_STATUS)) {
                                String trackStatus = trip.getString(RestKeys.RES_TRIPS_TRACK_STATUS);
                                if (trackStatus.equals("null") || trackStatus.equals("")) {
                                    trips.setTrackStatus("NO");
                                } else {
                                    trips.setTrackStatus(trackStatus);
                                }
                            }
                            if (trip.has(RestKeys.RES_TRIPS_ORDER_STATUS)) {
                                String orderStatus = trip.getString(RestKeys.RES_TRIPS_ORDER_STATUS);
                                switch (orderStatus) {
                                    case "0":
                                        String podStatus = trip.getString(RestKeys.RES_TRIPS_POD_STATUS);
                                        if (podStatus.equals("1")) {
                                            trips.setPodStatus("8");
                                        } else {
                                            trips.setPodStatus("9");
                                        }
                                        trips.setOrderStatus("0");
                                        break;
                                    case "1":
                                        trips.setOrderStatus("1");
                                        break;
                                    case "2":
                                        trips.setOrderStatus("2");
                                        break;
                                    case "3":
                                        trips.setOrderStatus("3");
                                        break;
                                    case "4":
                                        trips.setOrderStatus("4");
                                        break;
                                }
                            }

                            tripsList.add(trips);
                            adapterTrips.notifyDataSetChanged();
                            customProgressDialog.hide();
                            customProgressDialog.onWindowFocusChanged(false);
                        }
                    } else {
                        customProgressDialog.hide();
                        if(offset == 0){
                            ((DashboardActivity) getActivity()).setSnackBar(res.getString(RestKeys.MESSAGE));
                        }else {
                            ((DashboardActivity) getActivity()).setSnackBar(getString(R.string.no_more_orders));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hide();
                ((DashboardActivity) getActivity()).setSnackBar(getString(R.string.somethingWrong));

            }
        });
        searchTrip.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        new VolleySingleTone(getActivity()).getRequestQueue().add(searchTrip);
    }

}
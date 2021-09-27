package com.truckbhejob2b.truckbhejocustomer.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.truckbhejob2b.truckbhejocustomer.DashboardActivity;
import com.truckbhejob2b.truckbhejocustomer.LoginActivity;
import com.truckbhejob2b.truckbhejocustomer.R;
import com.truckbhejob2b.truckbhejocustomer.Utils.CustomProgressDialog;
import com.truckbhejob2b.truckbhejocustomer.Utils.NetworkUtil;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestUrl;
import com.truckbhejob2b.truckbhejocustomer.Utils.getCityName;
import com.truckbhejob2b.truckbhejocustomer.Volley.VolleySingleTone;
import com.truckbhejob2b.truckbhejocustomer.SharedPref.sharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    // User Define
    private sharedPreference sharedPreference;
    private CustomProgressDialog customProgressDialog;
    private String TAG = "Dashboard";
    private View rootView;
    private Context mContext;
    private ImageView imageBtnSearch, imageViewOptionMenu;
    private TextView txtViewRunningCount, txtViewDeliveredCount;
    private EditText edtTextSearch;
    private CardView cardViewPlaceOrder, cardViewShowAllTrips, cardViewTrip;
    private TextView txtViewOrderId, txtViewVehicleNumber, txtViewOrderStatus, txtViewFromLocation, txtViewToLocation, txtViewVehicleType, txtViewProductType,
            txtViewOrderDate;
    private String strOrderId = "", strDriverNumber = "";
    private int yourAppVersion;
    private android.app.AlertDialog Dialog;

    private TextView lastTripText;
    private boolean isTripAvailable = false, isMoreOptionAvailable = false;
    private ImageView mImageViewVehicleType, mImageViewProductType, mImageViewDate, mImageViewArrow, mImageViewMoreEvent;

    private String searchText = "";
    private String number = "";
    private ImageButton notificationMenu, callMenu;

    public DashboardFragment() {

    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mContext = getActivity();
        setUpUi();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //((DashboardActivity) getContext()).setToolbarTitle(getString(R.string.fragment_dashboard));
        ((DashboardActivity) getActivity()).enableBackButton(false, "");
    }

    private void setUpUi() {

        setHasOptionsMenu(true);
        sharedPreference = new sharedPreference(getActivity());
        customProgressDialog = new CustomProgressDialog(getActivity());
        customProgressDialog.onWindowFocusChanged(true);

        cardViewPlaceOrder = rootView.findViewById(R.id.cardViewPlaceOrder);
        cardViewShowAllTrips = rootView.findViewById(R.id.cardView3);
        cardViewTrip = rootView.findViewById(R.id.cardViewRunningOrder);
        imageViewOptionMenu = rootView.findViewById(R.id.imageViewMoreEvent);


        edtTextSearch = rootView.findViewById(R.id.edtSearchBox);
        imageBtnSearch = rootView.findViewById(R.id.btnSearch);

        txtViewOrderId = rootView.findViewById(R.id.txtViewOrderId);
        txtViewVehicleNumber = rootView.findViewById(R.id.txtViewVehicleNumber);
        txtViewOrderStatus = rootView.findViewById(R.id.txtViewStatus);
        txtViewFromLocation = rootView.findViewById(R.id.txtViewFromLocation);
        txtViewToLocation = rootView.findViewById(R.id.txtViewToLocation);
        txtViewVehicleType = rootView.findViewById(R.id.txtViewVehicleType);
        txtViewProductType = rootView.findViewById(R.id.txtViewProduct);
        txtViewOrderDate = rootView.findViewById(R.id.txtViewOrderDate);

        lastTripText = rootView.findViewById(R.id.txtViewLastTrip);

        mImageViewVehicleType = rootView.findViewById(R.id.imageView11);
        mImageViewProductType = rootView.findViewById(R.id.imageView12);
        mImageViewDate = rootView.findViewById(R.id.imageView13);
        mImageViewArrow = rootView.findViewById(R.id.imageView8);
        mImageViewMoreEvent = rootView.findViewById(R.id.imageViewMoreEvent);


        txtViewRunningCount = rootView.findViewById(R.id.txtViewRunning);
        txtViewDeliveredCount = rootView.findViewById(R.id.txtViewDelivered);
        //mRecyclerView = rootView.findViewById(R.id.recyclerViewDash);
        //setRecyclerViewProperties();

        // OnClick Listener

        imageBtnSearch.setOnClickListener(this);
        cardViewShowAllTrips.setOnClickListener(this);
        cardViewPlaceOrder.setOnClickListener(this);
        cardViewTrip.setOnClickListener(this);
        imageViewOptionMenu.setOnClickListener(this);
//        cardViewTrip.setVisibility(View.GONE);
        getAppVersion();

//        if (sharedPreference.getIsFirstTime()){
//            showTapCallMenu();
//        }else{
        getUserDetails();
//        }

    }

    private void getUserDetails() {
        if (NetworkUtil.getConnectivityStatus(getActivity()) != NetworkUtil.TYPE_NOT_CONNECTED) {
            customProgressDialog.show();
            validateLogin();

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
                validateLogin();
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

    private void validateLogin() {
        String Uri = android.net.Uri.parse(RestUrl.VALIDATE_LOGIN)
                .buildUpon()
                .appendQueryParameter(RestKeys.VALIDATE_CUSTOMER_ID, sharedPreference.getCustomerId())
                .appendQueryParameter(RestKeys.VALIDATE_API_TOKEN, sharedPreference.getCustomerApi())
                .build().toString();
        StringRequest validateLogin = new StringRequest(Request.Method.GET, Uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                customProgressDialog.hide();
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                        JSONArray verifyData = res.getJSONArray(RestKeys.DATA);
                        JSONObject ver = verifyData.getJSONObject(0);
                        int appVersion = Integer.parseInt(ver.getString("versionCode"));
                        if (yourAppVersion < appVersion) {
                            updateApp();
                        } else {
                            customProgressDialog.show();
                            getDashCount();
                        }
                    } else {
                        displayDialog();
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
        new VolleySingleTone(getActivity()).getRequestQueue().add(validateLogin);
    }

    private void getAppVersion() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            yourAppVersion = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateApp() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View DialogView = factory.inflate(R.layout.custom_new_version, null);
        Dialog = new android.app.AlertDialog.Builder(getActivity()).create();
        Dialog.setCancelable(false);
        Dialog.setView(DialogView);
        final String appName = "com.truckbhejob2b.truckbhejocustomer";
        CardView updateApp = DialogView.findViewById(R.id.cardViewUpdateApp);
        updateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));

            }
        });
        Dialog.show();
    }

    private void displayDialog() {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.notice))
                .setMessage(getString(R.string.notice_already_login))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPreference.clearAllPreferenceValues();
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                })
                .show();
    }

    public void getDashCount() {
        String UriCount = Uri.parse(RestUrl.GET_DASH_COUNT)
                .buildUpon()
                .appendQueryParameter(RestKeys.COUNT_CUSTOMER_ID, sharedPreference.getCompanyId())
                .build().toString();
        StringRequest count = new StringRequest(Request.Method.GET, UriCount, new Response.Listener<String>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                        JSONArray dash = res.getJSONArray(RestKeys.DATA);
                        if (dash.length() > 0) {
                            JSONObject count = dash.getJSONObject(0);
                            if (count.has(RestKeys.RUNNING_COUNT)) {
                                String running = count.getString(RestKeys.RUNNING_COUNT);
                                if (running.equals("null") || running.equals("")) {
                                    txtViewRunningCount.setText("0");
                                } else {
                                    txtViewRunningCount.setText(running);
                                }
                                if (count.has(RestKeys.DELIVERED_COUNT)) {
                                    String delivered = count.getString(RestKeys.DELIVERED_COUNT);
                                    if (delivered.equals("null") || delivered.equals("")) {
                                        txtViewDeliveredCount.setText("0");
                                    } else {
                                        txtViewDeliveredCount.setText(delivered);
                                    }
                                }
                                if (count.has(RestKeys.DASH_ORDER_ORDER_ID)) {
                                    strOrderId = count.getString(RestKeys.DASH_ORDER_ORDER_ID);
                                    txtViewOrderId.setText(strOrderId);
                                    if (!strOrderId.equals("")) {
                                        isTripAvailable = true;
                                        isMoreOptionAvailable = true;
                                        if (count.has(RestKeys.DASH_ORDER_VEHICLE_NUMBER)) {
                                            String vehicleNumber = count.getString(RestKeys.DASH_ORDER_VEHICLE_NUMBER);
                                            if (vehicleNumber.equals("") || vehicleNumber.equals("null")) {
                                                txtViewVehicleNumber.setText("");
                                            } else {
                                                txtViewVehicleNumber.setText(vehicleNumber);
                                            }
                                        }
                                        if (count.has(RestKeys.DASH_ORDER_STATUS)) {
                                            String orderStatus = count.getString(RestKeys.DASH_ORDER_STATUS);
                                            switch (orderStatus) {
                                                case "0":
                                                    txtViewOrderStatus.setText(getString(R.string.trips_delivered));
                                                    txtViewOrderStatus.setBackground(getActivity().getDrawable(R.drawable.rectgreenbox));
                                                    break;
                                                case "1":
                                                    txtViewOrderStatus.setText(getString(R.string.trips_Pending));
                                                    txtViewOrderStatus.setBackground(getActivity().getDrawable(R.drawable.rectyellowbox));
                                                    break;
                                                case "2":
                                                    txtViewOrderStatus.setText(getString(R.string.trips_accepted));
                                                    break;
                                                case "3":
                                                    txtViewOrderStatus.setText(getString(R.string.trips_canceled));
                                                    break;
                                                default:
                                                    txtViewOrderStatus.setText(getString(R.string.trips_running));
                                                    txtViewOrderStatus.setBackground(getActivity().getDrawable(R.drawable.rectgreenbox));
                                                    break;
                                            }
                                        }
                                        if (count.has(RestKeys.DASH_ORDER_FROM_LAT) && count.has(RestKeys.DASH_ORDER_FROM_LONG)) {
                                            String fromLat = count.getString(RestKeys.DASH_ORDER_FROM_LAT);
                                            String fromLong = count.getString(RestKeys.DASH_ORDER_FROM_LONG);
                                            txtViewFromLocation.setText(getCityName.getCityName(getActivity(), fromLat, fromLong));
                                        }
                                        if (count.has(RestKeys.DASH_ORDER_VEHICLE_TYPE)) {
                                            txtViewVehicleType.setText(count.getString(RestKeys.DASH_ORDER_VEHICLE_TYPE));
                                        }
                                        if (count.has(RestKeys.DASH_ORDER_PRODUCT_TYPE)) {
                                            txtViewProductType.setText(count.getString(RestKeys.DASH_ORDER_PRODUCT_TYPE));
                                        }
                                        if (count.has(RestKeys.DASH_ORDER_DATE)) {
                                            txtViewOrderDate.setText(count.getString(RestKeys.DASH_ORDER_DATE));
                                        }
                                        if (count.has(RestKeys.DASH_ORDER_DRIVER_NUMBER)) {
                                            strDriverNumber = count.getString(RestKeys.DASH_ORDER_DRIVER_NUMBER);
                                        }
                                        if (count.has(RestKeys.DASH_ORDER_TO_LAT3) && count.has(RestKeys.DASH_ORDER_TO_LAT3)) {
                                            String strToLat1 = count.getString(RestKeys.DASH_ORDER_TO_LAT3);
                                            String strToLong1 = count.getString(RestKeys.DASH_ORDER_TO_LONG3);
                                            if (!strToLat1.equals("") && !strToLong1.equals("")) {
                                                txtViewToLocation.setText(getCityName.getCityName(getActivity(), strToLat1, strToLong1));
                                            } else {
                                                String strToLat2 = count.getString(RestKeys.DASH_ORDER_TO_LAT2);
                                                String strToLong2 = count.getString(RestKeys.DASH_ORDER_TO_LONG2);
                                                if (!strToLong1.equals("") && !strToLong2.equals("")) {
                                                    txtViewToLocation.setText(getCityName.getCityName(getActivity(), strToLat2, strToLong2));
                                                } else {
                                                    txtViewToLocation.setText(getCityName.getCityName(getActivity(), count.getString(RestKeys.DASH_ORDER_TO_LAT1), count.getString(RestKeys.DASH_ORDER_TO_LONG1)));
                                                }
                                            }
                                        }
                                        if (count.has("manager_contact_no")) {
                                            number = count.getString("manager_contact_no");
                                        }

                                    } else {
                                        lastTripText.setVisibility(View.VISIBLE);
                                        hideCardWidgets();
                                    }
                                }

                            }
                            customProgressDialog.hide();
                            showTapView();

                        } else {
                            txtViewRunningCount.setText("0");
                            txtViewDeliveredCount.setText("0");
                            customProgressDialog.hide();
                            lastTripText.setVisibility(View.VISIBLE);
                            hideCardWidgets();
                            showTapView();
                        }

                    } else {
                        customProgressDialog.hide();
                        ((DashboardActivity) getActivity()).setSnackBar(res.getString(RestKeys.MESSAGE));
                        showTapView();
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
                showTapView();

            }
        });
        new VolleySingleTone(getActivity()).getRequestQueue().add(count);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.notification:
                ((DashboardActivity) mContext).changeFragment(new NotificationFragment(), "FragmentNotification");
                break;
            case R.id.call:
                callToManager(number);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    private void callToManager(String number) {
        if (!number.equals("") && !number.equals("null")) {
            if (!number.startsWith("+91") && !number.startsWith("0"))
                number = "+91" + number;
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            getActivity().startActivity(intent);
        } else {
            ((DashboardActivity) getActivity()).setSnackBar("Manager number not available..");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                searchText = edtTextSearch.getText().toString().trim();
                if (searchText.equals("")) {
                    ((DashboardActivity) getActivity()).setSnackBar(getString(R.string.please_enter_search_string));
                } else {
                    if (NetworkUtil.getConnectivityStatus(getActivity()) != NetworkUtil.TYPE_NOT_CONNECTED) {
                        edtTextSearch.setText("");
                        TripsFragment tripsFragment = new TripsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("SearchText", searchText);
                        tripsFragment.setArguments(bundle);
                        ((DashboardActivity) mContext).changeFragment(tripsFragment, "FragmentTrips");
                        ((DashboardActivity) getActivity()).hideSoftKeyboard(imageBtnSearch);
                    } else {
                        ((DashboardActivity) getActivity()).setSnackBar(getString(R.string.msg_no_internet_connection));
                    }
                }
                break;
            case R.id.cardViewPlaceOrder:
                FragmentPlaceOrder fragmentPlaceOrder = new FragmentPlaceOrder();
                ((DashboardActivity) getActivity()).changeFragment(fragmentPlaceOrder, "FragmentPlaceOrder");
                break;
            case R.id.cardView3:

                TripsFragment tripsFragment = new TripsFragment();
                ((DashboardActivity) getActivity()).changeFragment(tripsFragment, "FragmentPlaceOrder");
                break;
            case R.id.cardViewRunningOrder:
                if (isTripAvailable) {
                    OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(RestKeys.RES_TRIPS_ORDER_ID, strOrderId);
                    orderDetailsFragment.setArguments(bundle);
                    ((DashboardActivity) mContext).changeFragment(orderDetailsFragment, "FragmentOrderDetails");
                }
                break;


        }
    }

    private void hideCardWidgets() {
        mImageViewVehicleType.setVisibility(View.GONE);
        mImageViewProductType.setVisibility(View.GONE);
        mImageViewDate.setVisibility(View.GONE);
        mImageViewArrow.setVisibility(View.GONE);
        mImageViewMoreEvent.setVisibility(View.GONE);
    }

    private void showTapView() {
        if (sharedPreference.getIsFirstTime()) {
            showTapCallMenu();
            // showFirstTap();
        }
    }

    private void showTapCallMenu() {
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forToolbarMenuItem(((DashboardActivity) getActivity()).getToolbarObject(), R.id.call, "Contact Truckbhejo Manager")
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(20),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        showNotificationTap();
                    }
                });
    }

    private void showNotificationTap() {
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forToolbarMenuItem(((DashboardActivity) getActivity()).getToolbarObject(), R.id.notification, "Get Notified.")

                        .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(20),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        showFirstTap();
                    }
                });
    }

    private void showFirstTap() {
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(cardViewPlaceOrder, "Place New Order", "Tap to place new order.")
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        showSecondTap();
                    }
                });

    }

    private void showSecondTap() {
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(edtTextSearch, "Search For Orders", "")
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        showThirdTap();
                    }
                });
    }

    private void showThirdTap() {
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(txtViewRunningCount, "Track All Running Orders", "")
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        showFourthTap();
                    }
                });
    }

    private void showFourthTap() {
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(txtViewDeliveredCount, "Track All Delivered Orders", "")
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        showFifthTap();
                    }
                });
    }

    private void showFifthTap() {
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(cardViewTrip, "Your Last Trip", "")
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        showSixthTap();
                    }
                });
    }

    private void showSixthTap() {
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(cardViewShowAllTrips, "Tap To View All Trips", "")
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        sharedPreference.setIsFirstTime(false);
                        getUserDetails();
                    }
                });
    }

}
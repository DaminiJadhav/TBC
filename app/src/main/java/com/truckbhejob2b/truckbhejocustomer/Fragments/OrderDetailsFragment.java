package com.truckbhejob2b.truckbhejocustomer.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.truckbhejob2b.truckbhejocustomer.DashboardActivity;
import com.truckbhejob2b.truckbhejocustomer.R;
import com.truckbhejob2b.truckbhejocustomer.Utils.CustomProgressDialog;
import com.truckbhejob2b.truckbhejocustomer.Utils.NetworkUtil;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestUrl;
import com.truckbhejob2b.truckbhejocustomer.Volley.VolleySingleTone;
import com.truckbhejob2b.truckbhejocustomer.WebViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class OrderDetailsFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String TAG = "OrderDetails";

    private View rootView;
    private String orderId;

    private TextView txtViewVehicleNumber, txtViewDriverName, txtViewDriverNumber, txtViewLr, txtViewEway, txtViewPodFront, txtViewPodBack;
    private ImageView imageViewLr, imageViewEway, imageViewPodFront, imageViewPodBack;
    private TextView txtViewNoDoc, txtViewNoPod;
    private TextInputEditText txtViewLrNumber, txtViewEwayNumber;
    private TextInputLayout txtViewLayoutLr, txtViewLayoutEway;
    private Boolean isLrImageAvailable = false, isEwayImageAvailable = false, isPodFrontImageAvailable = false, isPodBackImageAvailable = false;
    private String lrImageUri, eWayImageUri, podFrontImageUri, podBackImageUri;

    private CustomProgressDialog customProgressDialog;

    private ImageView mImageViewCallButton, mImageViewTrackButton;
    private String driverNumber = "";
    private TextView mTxtViewDateOfDelivery, mTxtViewFeedback, txtViewDateOfDelivery, txtViewFeedback;
    private LinearLayout mLinearLayoutDateOfDelivery, mLinearLayoutFeedback;

    private TextView mTxtViewOrderStatus, mTxtViewColon, txtViewOrderStatus;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    public static OrderDetailsFragment newInstance(String param1, String param2) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
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
            orderId = getArguments().getString(RestKeys.RES_TRIPS_ORDER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_details, container, false);
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
        ((DashboardActivity) getActivity()).enableBackButton(true, getString(R.string.order_details));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DashboardActivity) getActivity()).enableBackButton(false, getString(R.string.fragment_dashboard));
    }

    private void setUpUi() {
        customProgressDialog = new CustomProgressDialog(getActivity());
        txtViewVehicleNumber = rootView.findViewById(R.id.txtViewVehicleNumber);
        txtViewDriverName = rootView.findViewById(R.id.txtViewDriverName);
        txtViewDriverNumber = rootView.findViewById(R.id.txtViewMobileNumber);

        txtViewLr = rootView.findViewById(R.id.textView8);
        txtViewEway = rootView.findViewById(R.id.textView10);
        txtViewPodFront = rootView.findViewById(R.id.textView11);
        txtViewPodBack = rootView.findViewById(R.id.textView12);

        imageViewLr = rootView.findViewById(R.id.imageViewLrNumber);
        imageViewEway = rootView.findViewById(R.id.imageViewEwayBill);
        imageViewPodFront = rootView.findViewById(R.id.imageViewFrontPod);
        imageViewPodBack = rootView.findViewById(R.id.imageViewBackPod);

        mImageViewCallButton = rootView.findViewById(R.id.imageCall);
        mImageViewTrackButton = rootView.findViewById(R.id.imageLocation);

        mTxtViewOrderStatus = rootView.findViewById(R.id.textView20);
        mTxtViewColon = rootView.findViewById(R.id.textView82);
        txtViewOrderStatus = rootView.findViewById(R.id.txtViewOrderStatus);

        //pod Card
        mLinearLayoutDateOfDelivery = rootView.findViewById(R.id.linearLayout7);
        mLinearLayoutFeedback = rootView.findViewById(R.id.linearLayout8);
        mTxtViewDateOfDelivery = rootView.findViewById(R.id.mTxtViewDeliveryDate);
        mTxtViewFeedback = rootView.findViewById(R.id.mTxtViewFeedback);
        txtViewDateOfDelivery = rootView.findViewById(R.id.txtViewDeliveryDate);
        txtViewFeedback = rootView.findViewById(R.id.txtViewFeedback);


        imageViewLr.setOnClickListener(this);
        imageViewEway.setOnClickListener(this);
        imageViewPodFront.setOnClickListener(this);
        imageViewPodBack.setOnClickListener(this);

        mImageViewCallButton.setOnClickListener(this);
        mImageViewTrackButton.setOnClickListener(this);

        txtViewNoDoc = rootView.findViewById(R.id.txtViewNoOrderDetails);
        txtViewNoPod = rootView.findViewById(R.id.txtViewNoPodDetails);

        txtViewLrNumber = rootView.findViewById(R.id.txtViewLrNumber);
        txtViewEwayNumber = rootView.findViewById(R.id.txtViewEwayBill);
        txtViewLayoutLr = rootView.findViewById(R.id.textInputLrNumber);
        txtViewLayoutEway = rootView.findViewById(R.id.textInputEwayBill);

        if (NetworkUtil.getConnectivityStatus(getActivity()) != NetworkUtil.TYPE_NOT_CONNECTED) {
            getOrderDetails();
            customProgressDialog.show();
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
                getOrderDetails();
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

    private void getOrderDetails() {
        String url = Uri.parse(RestUrl.ORDER_DETAILS)
                .buildUpon()
                .appendQueryParameter(RestKeys.ORDER_DETAILS_ORDER_ID, orderId)
                .build().toString();
        StringRequest getDtl = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                customProgressDialog.hide();
               //  Log.d(TAG, "onResponse: "+response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                        JSONArray data = res.getJSONArray(RestKeys.DATA);
                        JSONObject orderDtl = data.getJSONObject(0);

                        if (orderDtl.has("status")) {
                            String orderStatus = orderDtl.getString("status");
//                                    if (orderStatus.equals("4") && !orderStatus.equals("null")){
//                                        txtViewOrderStatus.setText("Delivered");
//                                    }else if (orderStatus.equals("4") && !orderStatus.equals("null")){
//                                        txtViewOrderStatus.setText("Running");
//                                    } else{
//                                        txtViewOrderStatus.setText("Pending");
//                                    }
                            //                           Log.d(TAG, "onResponse: "+orderStatus);
                            if (orderStatus.contains("4")) {
                                txtViewOrderStatus.setText("Running");
                            } else if (orderStatus.contains("0")) {
                                txtViewOrderStatus.setText("Delivered");
                            } else {
                                txtViewOrderStatus.setText("Pending");
                            }
                        }
                        if (orderDtl.has(RestKeys.RES_ORDER_DTL_DRIVER_NAME)) {
                            txtViewDriverName.setText(orderDtl.getString(RestKeys.RES_ORDER_DTL_DRIVER_NAME));
                        }
                        if (orderDtl.has(RestKeys.RES_ORDER_DTL_DRIVER_NUMBER)) {
                            txtViewDriverNumber.setText(orderDtl.getString(RestKeys.RES_ORDER_DTL_DRIVER_NUMBER));
                            driverNumber = orderDtl.getString(RestKeys.RES_ORDER_DTL_DRIVER_NUMBER);
                        }
                        if (orderDtl.has(RestKeys.RES_ORDER_DTL_VEHICLE_NUMBER)) {
                            txtViewVehicleNumber.setText(orderDtl.getString(RestKeys.RES_ORDER_DTL_VEHICLE_NUMBER));
                        }
                        if (orderDtl.has(RestKeys.RES_ORDER_DTL_LR_NUMBER)) {
                            String lrNumber = orderDtl.getString(RestKeys.RES_ORDER_DTL_LR_NUMBER);
                            if (lrNumber.equals("null") || lrNumber.equals("NULL")) {
                                hideDocCard();
                                hidePodCard();
                            } else {
                                txtViewLrNumber.setText(lrNumber);
                                txtViewEwayNumber.setText(orderDtl.getString(RestKeys.RES_ORDER_DTL_EWAY_NUMBER));
                                if (orderDtl.has(RestKeys.RES_ORDER_DTL_LR_IMAGE)) {
                                    isLrImageAvailable = true;
                                    String lrImage = orderDtl.getString(RestKeys.RES_ORDER_DTL_LR_IMAGE);
                                    if (!lrImage.equals("")) {
                                        lrImageUri = lrImage;
                                        String extension = lrImage.substring(lrImage.lastIndexOf("."));
                                        if (extension.equals(".pdf")) {
                                            imageViewLr.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.pdf_96));
                                        } else {
                                            Picasso.get().load(RestUrl.NEW_IMAGE_URL + orderDtl.getString(RestKeys.RES_ORDER_DTL_LR_IMAGE)).placeholder(R.mipmap.app_ic).error(R.mipmap.app_ic).into(imageViewLr);
                                        }
                                    }
                                }
                                if (orderDtl.has(RestKeys.RES_ORDER_DTL_EWAY_IMAGE)) {
                                    isEwayImageAvailable = true;
                                    String eWayImage = orderDtl.getString(RestKeys.RES_ORDER_DTL_EWAY_IMAGE);
                                    if (!eWayImage.equals("")) {
                                        eWayImageUri = eWayImage;
                                        String extension = eWayImage.substring(eWayImage.lastIndexOf("."));
                                        if (extension.equals(".pdf")) {
                                            imageViewEway.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.pdf_96));
                                        } else {
                                            Picasso.get().load(RestUrl.NEW_IMAGE_URL + orderDtl.getString(RestKeys.RES_ORDER_DTL_EWAY_IMAGE)).placeholder(R.mipmap.app_ic).error(R.mipmap.app_ic).into(imageViewEway);
                                        }
                                    }
                                }

                                if (orderDtl.has(RestKeys.RES_ORDER_DTL_POD_FRONT) && !orderDtl.isNull(RestKeys.RES_ORDER_DTL_POD_FRONT)) {
                                    String podImage = orderDtl.getString(RestKeys.RES_ORDER_DTL_POD_FRONT);
                                    String DateOfDelivery = orderDtl.getString(RestKeys.RES_ORDER_DTL_DATE);
                                    if (DateOfDelivery.equals("")){
                                        hidePodCard();
                                    }else {
                                        txtViewDateOfDelivery.setText(orderDtl.getString(RestKeys.RES_ORDER_DTL_DATE));
                                        txtViewFeedback.setText(orderDtl.getString(RestKeys.RES_ORDER_DTL_FEEDBACK));
                                        if(podImage.equals("null") || podImage.equals("NULL") || podImage.equals("")){
//                                            hidePodCard();
                                            imageViewPodFront.setVisibility(View.GONE);
                                            imageViewPodBack.setVisibility(View.GONE);
                                            txtViewPodBack.setVisibility(View.GONE);
                                            txtViewPodFront.setVisibility(View.GONE);
                                        }else {
                                            podFrontImageUri = podImage;
                                            String extension = podImage.substring(podImage.lastIndexOf("."));

                                            if (extension.equals(".pdf")) {
                                                isPodFrontImageAvailable = true;
                                                imageViewPodFront.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.pdf_96));
                                                imageViewPodBack.setVisibility(View.GONE);
                                                txtViewPodBack.setVisibility(View.GONE);
                                            } else {
                                                isPodFrontImageAvailable = true;
                                                isPodBackImageAvailable = true;
                                                Picasso.get().load(RestUrl.NEW_IMAGE_URL + orderDtl.getString(RestKeys.RES_ORDER_DTL_POD_FRONT)).placeholder(R.mipmap.app_ic).error(R.mipmap.app_ic).into(imageViewPodFront);
                                                Picasso.get().load(RestUrl.NEW_IMAGE_URL + orderDtl.getString(RestKeys.RES_ORDER_DTL_POD_BACK)).placeholder(R.mipmap.app_ic).error(R.mipmap.app_ic).into(imageViewPodBack);
                                                podBackImageUri = orderDtl.getString(RestKeys.RES_ORDER_DTL_POD_BACK);
                                            }
                                        }
                                    }

                                } else {
                                    hidePodCard();
                                }
                            }
                        }
                    } else {
                        hidePodCard();
                        hideDocCard();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hide();
            }
        });
        new VolleySingleTone(getActivity()).getRequestQueue().add(getDtl);
    }

    private void hideDocCard() {
        txtViewLayoutLr.setVisibility(View.GONE);
        txtViewLayoutEway.setVisibility(View.GONE);
        imageViewLr.setVisibility(View.GONE);
        imageViewEway.setVisibility(View.GONE);
        txtViewLr.setVisibility(View.GONE);
        txtViewEway.setVisibility(View.GONE);
        txtViewNoDoc.setVisibility(View.VISIBLE);
    }

    private void hidePodCard() {
        imageViewPodBack.setVisibility(View.GONE);
        imageViewPodFront.setVisibility(View.GONE);
        txtViewPodBack.setVisibility(View.GONE);
        txtViewPodFront.setVisibility(View.GONE);
        txtViewNoPod.setVisibility(View.VISIBLE);
        mLinearLayoutFeedback.setVisibility(View.GONE);
        mLinearLayoutDateOfDelivery.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewLrNumber:
                if (isLrImageAvailable) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(RestKeys.URI, RestUrl.NEW_IMAGE_URL + lrImageUri);
                    intent.putExtra(RestKeys.ORDER_ID, orderId);
                    startActivity(intent);
                }
                break;
            case R.id.imageViewEwayBill:
                if (isEwayImageAvailable) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(RestKeys.URI, RestUrl.NEW_IMAGE_URL + eWayImageUri);
                    intent.putExtra(RestKeys.ORDER_ID, orderId);
                    startActivity(intent);
                }
                break;
            case R.id.imageViewFrontPod:
                if (isPodFrontImageAvailable) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(RestKeys.URI, RestUrl.NEW_IMAGE_URL + podFrontImageUri);
                    intent.putExtra(RestKeys.ORDER_ID, orderId);
                    startActivity(intent);
                }
                break;
            case R.id.imageViewBackPod:
                if (isPodBackImageAvailable) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(RestKeys.URI, RestUrl.NEW_IMAGE_URL + podBackImageUri);
                    intent.putExtra(RestKeys.ORDER_ID, orderId);
                    startActivity(intent);
                }
                break;
            case R.id.imageCall:
                if (!driverNumber.equals("")) {
                    callToDriver(driverNumber);
                }
                break;
            case R.id.imageLocation:
                trackOrder(orderId);
                break;
        }

    }

    @SuppressLint("MissingPermission")
    private void callToDriver(String number) {
        if (!number.startsWith("+91") && !number.startsWith("0"))
            number = "+91" + number;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        getActivity().startActivity(intent);
    }

    private void trackOrder(String orderId) {

        TrackOrderFragment trackOrderFragment = new TrackOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RestKeys.RES_TRIPS_ORDER_ID, orderId);
        trackOrderFragment.setArguments(bundle);
        ((DashboardActivity) getActivity()).changeFragment(trackOrderFragment, "FragmentTrackOrder");

    }
}
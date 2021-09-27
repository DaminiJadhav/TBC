package com.truckbhejob2b.truckbhejocustomer.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.truckbhejob2b.truckbhejocustomer.DashboardActivity;
import com.truckbhejob2b.truckbhejocustomer.R;
import com.truckbhejob2b.truckbhejocustomer.Utils.CustomProgressDialog;
import com.truckbhejob2b.truckbhejocustomer.Utils.NetworkUtil;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestUrl;
import com.truckbhejob2b.truckbhejocustomer.Utils.getCityName;
import com.truckbhejob2b.truckbhejocustomer.Volley.VolleySingleTone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackOrderFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String TAG ="Track";
    private View rootView;
    private String orderId;
    private CustomProgressDialog customProgressDialog;

    private GoogleMap mGoogleMap;
    private String fromLat, fromLong, toLat, toLong, to2Lat, to2Long, to3Lat, to3Long = "";
    private TextView mTxtViewEtaTime, mTxtViewOnTime, mTxtViewLastLocation;
    private MarkerOptions sourceMarker, finalDestination,destination1,destination2,currentLocationMarker;
    private String finalDestinationLat ,finalDestinationLong ,Destination2Lat,Destination2Long ,Destination1Lat,Destination1Long;


    public TrackOrderFragment() {
        // Required empty public constructor
    }

    public static TrackOrderFragment newInstance(String param1, String param2) {
        TrackOrderFragment fragment = new TrackOrderFragment();
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_track_order, container, false);
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
        ((DashboardActivity)getActivity()).enableBackButton(true,getString(R.string.track_order));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DashboardActivity) getActivity()).enableBackButton(false, getString(R.string.fragment_dashboard));
    }
    private void setUpUi(){
        mTxtViewEtaTime = rootView.findViewById(R.id.txtViewEtaTime);
        mTxtViewOnTime = rootView.findViewById(R.id.txtViewOnTime);
        mTxtViewLastLocation = rootView.findViewById(R.id.txtViewLastLocation);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        supportMapFragment.getMapAsync(this);
        customProgressDialog = new CustomProgressDialog(getActivity());
        if (NetworkUtil.getConnectivityStatus(getActivity())!=NetworkUtil.TYPE_NOT_CONNECTED){
            if (!orderId.equals("")){
                loadLocationDetails(orderId);
                getTrackingDetails();
                customProgressDialog.show();
            }
        }else {
            ((DashboardActivity)getActivity()).setSnackBar(getString(R.string.msg_no_internet_connection));
        }
    }
    private void loadLocationDetails(String orderId){
        String Url = Uri.parse(RestUrl.TRACKING_POINTS)
                .buildUpon()
                .appendQueryParameter(RestKeys.TRACKING_ORDER_ID, orderId)
                .build().toString();
        StringRequest track = new StringRequest(Request.Method.GET,
                Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        customProgressDialog.hide();
                        try {

                            JSONObject res = new JSONObject(response);
                            if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                                JSONArray dataArray = res.getJSONArray(RestKeys.DATA);
                                JSONObject data = dataArray.getJSONObject(0);
                                if (data.has(RestKeys.TRACK_FROM_LOC_LAT) && data.has(RestKeys.TRACK_FROM_LOC_LONG)) {
                                    fromLat = data.getString(RestKeys.TRACK_FROM_LOC_LAT);
                                    fromLong = data.getString(RestKeys.TRACK_FROM_LOC_LONG);

                                }
                                if (data.has(RestKeys.TRACK_TO_LAT) && data.has(RestKeys.TRACK_TO_LONG)) {
                                    toLat = data.getString(RestKeys.TRACK_TO_LAT);
                                    toLong = data.getString(RestKeys.TRACK_TO_LONG);
                                }
                                if (data.has(RestKeys.TRACK_TO2_LAT) && data.has(RestKeys.TRACK_TO2_LONG)){
                                    to2Lat =data.getString(RestKeys.TRACK_TO2_LAT);
                                    to2Long = data.getString(RestKeys.TRACK_TO2_LONG);

                                }
                                if (data.has(RestKeys.TRACK_TO3_LAT) && data.has(RestKeys.TRACK_TO3_LONG)){
                                    to3Lat = data.getString(RestKeys.TRACK_TO3_LAT);
                                    to3Long = data.getString(RestKeys.TRACK_TO3_LONG);

                                }
                                drawRoute();
                            } else {
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
      new  VolleySingleTone(getActivity()).getInstance(getContext()).getRequestQueue().add(track);

    }
    private void getTrackingDetails(){
        String url = Uri.parse(RestUrl.TRACK_ORDER)
                .buildUpon()
                .appendQueryParameter(RestKeys.TRACK_ORDER_ID, orderId)
                .build().toString();
        StringRequest getTrack = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(String response) {
               // Log.d(TAG, "onResponse: "+response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)){
                        JSONArray getTrack = res.getJSONArray(RestKeys.DATA);
                        JSONObject tracking = getTrack.getJSONObject(0);
                        if (tracking.has(RestKeys.GET_TRACK_LAT_LONG)){
                            String currentLatLong = tracking.getString(RestKeys.GET_TRACK_LAT_LONG);
                            String [] calculated = currentLatLong.split(",");

                            double currentLat = Double.parseDouble(calculated[0]);
                            double currentLong = Double.parseDouble(calculated[1]);
                            currentLocationMarker = new MarkerOptions().position(new LatLng(currentLat,currentLong));
                            Bitmap bitmap = getBitmap(R.drawable.ic_3d_truck_icon_2);
                            currentLocationMarker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationMarker.getPosition(), 10));
                            LatLng coordinate = new LatLng(currentLocationMarker.getPosition().latitude, currentLocationMarker.getPosition().longitude);
                            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                    coordinate, 10);
                            mGoogleMap.moveCamera(location);
                            mGoogleMap.addMarker(currentLocationMarker);
                        }
                        if (tracking.has(RestKeys.GET_TRACK_ADDRESS)){
                            mTxtViewLastLocation.setText(tracking.getString(RestKeys.GET_TRACK_ADDRESS));
                            currentLocationMarker.title(tracking.getString(RestKeys.GET_TRACK_ADDRESS));
                        }
                        if (tracking.has(RestKeys.GET_TRACK_ON_TIME)){
                         //   String onTime = tracking.getString(RestKeys.GET_TRACK_ON_TIME);
//                            if (onTime.equals("0")){
//                                mTxtViewOnTime.setBackground(getActivity().getDrawable(R.drawable.custom_bg_red));
//                                mTxtViewEtaTime.setBackground(getActivity().getDrawable(R.drawable.custom_bg_red));
//                                mTxtViewOnTime.setText("Delayed");
                          //  }else {
                                mTxtViewOnTime.setText("On Time");
                            //}
                        }
                        if (tracking.has(RestKeys.GET_TRACK_ETA)){
                            String etaTime = tracking.getString(RestKeys.GET_TRACK_ETA);
//                            Log.d(TAG, "onResponse: "+etaTime);
                            if (etaTime.equals("")){
                                mTxtViewEtaTime.setText("NA");
                            }else {
                                String[] standard = etaTime.split("T");
                                mTxtViewEtaTime.setText(standard[0]);
                            }

                        }

                        if (tracking.has(RestKeys.GET_TRACK_LAST_TRACK)){
                            String lastTrackTime = tracking.getString(RestKeys.GET_TRACK_LAST_TRACK);
                            currentLocationMarker.snippet(lastTrackTime);
                        }

                        for (int i=1;i<getTrack.length();i++){
                            JSONObject trackingNew = getTrack.getJSONObject(i);
                            if (trackingNew.has(RestKeys.GET_TRACK_LAT_LONG)){
                                String currentLatLong = trackingNew.getString(RestKeys.GET_TRACK_LAT_LONG);
                                String [] calculated = currentLatLong.split(",");
                                double currentLat = Double.parseDouble(calculated[0]);
                                double currentLong = Double.parseDouble(calculated[1]);
                                currentLocationMarker = new MarkerOptions().position(new LatLng(currentLat,currentLong));
                                Bitmap bitmap = circleGetBitmap(R.drawable.circle);
                                currentLocationMarker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationMarker.getPosition(), 10));
//                                LatLng coordinate = new LatLng(currentLocationMarker.getPosition().latitude, currentLocationMarker.getPosition().longitude);
////                                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
////                                        coordinate, 10);
////                                mGoogleMap.moveCamera(location);
                                mGoogleMap.addMarker(currentLocationMarker);
                            }
                        }
                    }else{
                        mTxtViewLastLocation.setText("Tracking not Started yet.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleTone.getInstance(getActivity()).getRequestQueue().add(getTrack);
    }
    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, 150, 140);
        drawable.draw(canvas);
        return bitmap;
    }
    private Bitmap circleGetBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(40, 30, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, 40, 30);
        drawable.draw(canvas);

        return bitmap;
    }

    private void drawRoute(){
        sourceMarker = new MarkerOptions().position(new LatLng(Double.parseDouble(fromLat), Double.parseDouble(fromLong))).title(getCityName.fullAddress(getContext(),fromLat,fromLong));
        sourceMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        Bitmap bitmap = getBitmap(R.drawable.ic_truck_icon_map);
//        sourceMarker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sourceMarker.getPosition(), 5));
        mGoogleMap.addMarker(sourceMarker);
        drawCircle(fromLat,fromLong);

        if (!to3Lat.equals("") && !to3Long.equals("")){
            finalDestinationLat = to3Lat;
            finalDestinationLong = to3Long;
            Destination2Lat = to2Lat;
            Destination2Long = to2Long;
            Destination1Lat = toLat;
            Destination1Long = toLong;
            finalDestination = new MarkerOptions().position(new LatLng(Double.parseDouble(to3Lat), Double.parseDouble(to3Long))).title(getCityName.fullAddress(getContext(),to3Lat,to3Long));
            mGoogleMap.addMarker(finalDestination);
            destination2 = new MarkerOptions().position(new LatLng(Double.parseDouble(to2Lat), Double.parseDouble(to2Long))).title(getCityName.fullAddress(getContext(),to2Lat,to2Long));
            destination1 = new MarkerOptions().position(new LatLng(Double.parseDouble(toLat), Double.parseDouble(toLong))).title(getCityName.fullAddress(getContext(),toLat,toLong));
            mGoogleMap.addMarker(destination2);
            mGoogleMap.addMarker(destination1);
            drawCircle(finalDestinationLat,finalDestinationLong);
            drawCircle(Destination2Lat,Destination2Long);
            drawCircle(Destination1Lat,Destination1Long);
        }else {
            if (!to2Lat.equals("") && !to2Long.equals("")){
                finalDestinationLat = to2Lat;
                finalDestinationLong = to2Long;
                Destination1Lat = toLat;
                Destination1Long = toLong;
                finalDestination = new MarkerOptions().position(new LatLng(Double.parseDouble(to2Lat), Double.parseDouble(to2Long))).title(getCityName.fullAddress(getContext(),to2Lat,to2Long));
                destination1 = new MarkerOptions().position(new LatLng(Double.parseDouble(toLat), Double.parseDouble(toLong))).title(getCityName.fullAddress(getContext(),toLat,toLong));
                mGoogleMap.addMarker(finalDestination);
                mGoogleMap.addMarker(destination1);
                drawCircle(finalDestinationLat,finalDestinationLong);
                drawCircle(Destination1Lat,Destination1Long);

            }else {
                finalDestinationLat = toLat;
                finalDestinationLong = toLong;
                finalDestination = new MarkerOptions().position(new LatLng(Double.parseDouble(toLat), Double.parseDouble(toLong))).title(getCityName.fullAddress(getContext(),toLat,toLong));
                mGoogleMap.addMarker(finalDestination);
                drawCircle(finalDestinationLat,finalDestinationLong);
            }
        }

    }
    private void drawCircle(final String lat,final String longi){
        Circle circle = mGoogleMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.parseDouble(lat), Double.parseDouble(longi)))
                .strokeColor(Color.BLUE)
                .strokeWidth(1)
                .radius(5000)
                .fillColor(getResources().getColor(R.color.circle)));
        //.fillColor(0x40ff0000));


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }
}
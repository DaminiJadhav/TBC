package com.truckbhejob2b.truckbhejocustomer.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.truckbhejob2b.truckbhejocustomer.DashboardActivity;
import com.truckbhejob2b.truckbhejocustomer.Model.CustomerNames;
import com.truckbhejob2b.truckbhejocustomer.Model.Dashboard;
import com.truckbhejob2b.truckbhejocustomer.Model.VehicleType;
import com.truckbhejob2b.truckbhejocustomer.Model.productType;
import com.truckbhejob2b.truckbhejocustomer.R;
import com.truckbhejob2b.truckbhejocustomer.SharedPref.sharedPreference;
import com.truckbhejob2b.truckbhejocustomer.Utils.CustomProgressDialog;
import com.truckbhejob2b.truckbhejocustomer.Utils.DateTimeUtils;
import com.truckbhejob2b.truckbhejocustomer.Utils.NetworkUtil;
import com.truckbhejob2b.truckbhejocustomer.Utils.ResponseModel;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestUrl;
import com.truckbhejob2b.truckbhejocustomer.Utils.RetrofitAPI;
import com.truckbhejob2b.truckbhejocustomer.Volley.VolleySingleTone;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import static android.content.ContentValues.TAG;

public class FragmentPlaceOrder extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PERMISSION_REQUEST_CALL_PHONE = 11;
    private String PhNo = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private View rootView;

    private CustomProgressDialog cpd;
    private StringRequest sr;
    private ArrayList<HashMap<String, String>> companyTypesList;
    private ArrayList<HashMap<String, String>> customerNameList;
    private ArrayList<HashMap<String, String>> vehicleTypeList;
    private ArrayList<HashMap<String, String>> productTypeList;
    private Spinner spnVehicleType;
    private TextView tv_PlaceOrder;
    private TextView tv_PlacePickupPoint, tv_PlaceDropPoint, tv_PlaceDropPoint2, tv_PlaceDropPoint3;
    private String strCompanyId = "";
    private String strCustomerNameId = "";

    private String strVehicleTypeId = "";

    private int pos = 0;
    private ArrayAdapter<String> orderAdapter;
    private DatePickerDialog dpd;
    private Calendar nextDate;
    private int PLACE_PICKUP_POINT = 1;
    private int PLACE_DROP_POINT = 11;
    private int PLACE_DROP_POINT2 = 12;
    private int PLACE_DROP_POINT3 = 13;

    private CardView cardViewDateAndTime;
    private TextView mEdtDateAndTime;
    private String Date = "", Time = "", DateAndTime = "";

    private TextInputEditText mEdtProductWeight, mEdtQuantityProduct;
    private String productWeight = "", quantityProduct = "", vehicleCapacity;
    private CardView mCardViewFromLocation, mCardViewToLocation, mCardViewSave, mCardViewSecondLocation, mCardViewThirdLocation;


    private String strFromLocationLat = "", strFromLocationLong = "", strToLocationLat = "", strToLocationLong = "", str2ToLocationLat = "",
            str2ToLocationLong = "", str3ToLocationLat = "", str3ToLocationLong = "", str1ToLocationAddress = "", str2ToLocationAddress = "",
            str3ToLocationAddress = "", strFromLocationAddress = "", finalDropAddress = "";
    private int toLocationCount = 0;
    private int imageButtonClick = 0;
    private ImageView imageViewAddToLocation;
    private Context mContext;
    private Spinner mSpinnerProduct;

    PlacesClient placesClient;
    private String productTypes = "";
    private int productTypeId;

    private com.truckbhejob2b.truckbhejocustomer.SharedPref.sharedPreference sharedPreference;

    private String TAG = "PlaceOrder";

    public FragmentPlaceOrder() {
        // Required empty public constructor
    }


    public static FragmentPlaceOrder newInstance(String param1, String param2) {
        FragmentPlaceOrder fragment = new FragmentPlaceOrder();
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
        rootView = inflater.inflate(R.layout.fragment_place_order, container, false);
        setUI();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mContext = getActivity();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).enableBackButton(true,getString(R.string.place_order));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DashboardActivity) getActivity()).enableBackButton(false, getString(R.string.fragment_dashboard));
    }

    private void setUI() {
        loadPlaces();

        sharedPreference = new sharedPreference(getActivity());
        spnVehicleType = rootView.findViewById(R.id.spn_Vehicle_Type);
        tv_PlaceOrder = rootView.findViewById(R.id.tv_PlaceOrder);
        tv_PlacePickupPoint = rootView.findViewById(R.id.tv_PlacePickupPoint);
        tv_PlaceDropPoint = rootView.findViewById(R.id.tv_PlaceDropPoint);
        tv_PlaceDropPoint2 = rootView.findViewById(R.id.txtViewSecondLocation);
        tv_PlaceDropPoint3 = rootView.findViewById(R.id.txtViewThirdLocation);
        cardViewDateAndTime = rootView.findViewById(R.id.cardViewSpinner7);
        mEdtDateAndTime = rootView.findViewById(R.id.txtPickUpDateTime);

        mEdtProductWeight = rootView.findViewById(R.id.edtProductWeight);
        mEdtQuantityProduct = rootView.findViewById(R.id.edtProductQuantity);
        // mEdtProductType = rootView.findViewById(R.id.edtProductType);
        mSpinnerProduct = rootView.findViewById(R.id.productTypeSpinner);
        mCardViewFromLocation = rootView.findViewById(R.id.cardViewSpinner4);
        mCardViewToLocation = rootView.findViewById(R.id.cardViewSpinner5);
        mCardViewSave = rootView.findViewById(R.id.btnSave);
        imageViewAddToLocation = rootView.findViewById(R.id.imageViewToAddLocation);
        mCardViewSecondLocation = rootView.findViewById(R.id.cardViewSpinner10);
        mCardViewThirdLocation = rootView.findViewById(R.id.cardViewSpinner11);


        imageViewAddToLocation.setOnClickListener(this);
        tv_PlacePickupPoint.setOnClickListener(this);
        mCardViewSave.setOnClickListener(this);
        tv_PlaceOrder.setOnClickListener(this);
        tv_PlaceDropPoint.setOnClickListener(this);
        mCardViewSecondLocation.setOnClickListener(this);
        mCardViewThirdLocation.setOnClickListener(this);


        mCardViewFromLocation.setOnClickListener(this);
        mCardViewToLocation.setOnClickListener(this);
        cardViewDateAndTime.setOnClickListener(this);

        if (NetworkUtil.getConnectivityStatus(getActivity()) != NetworkUtil.TYPE_NOT_CONNECTED) {
            loadVehicleTypeData();
            loadProductType();
        } else {
            noInternetDialog(getResources().getString(R.string.msg_no_internet_connection));
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void loadPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), getString(R.string.google_maps_key));
        }
        placesClient = Places.createClient(getActivity());
    }

    @Override
    public void onClick(View view) {
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        switch (view.getId()) {

            case R.id.cardViewSpinner7:
                ((DashboardActivity) getActivity()).hideSoftKeyboard(rootView);
                startDatePickerDialogNext();
                break;
            case R.id.tv_PlacePickupPoint:
                ((DashboardActivity) getActivity()).hideSoftKeyboard(rootView);
                pickUpLocationPicker();
                break;
            case R.id.imageViewToAddLocation:
                ((DashboardActivity) getActivity()).hideSoftKeyboard(rootView);
                imageButtonClick++;
                if (imageButtonClick == 1) {
                    mCardViewSecondLocation.setVisibility(View.VISIBLE);
                } else if (imageButtonClick == 2) {
                    mCardViewThirdLocation.setVisibility(View.VISIBLE);
                    imageViewAddToLocation.setVisibility(View.GONE);
                }
                break;

            case R.id.tv_PlaceDropPoint:

                ((DashboardActivity) getActivity()).hideSoftKeyboard(rootView);
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                try {
                    gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }

                try {
                    networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }

                if (!gpsEnabled && !networkEnabled) {
                    AlertDialog.Builder aletDialog = new AlertDialog.Builder(getActivity());
                    aletDialog.setTitle(R.string.gps_setting);
                    aletDialog.setMessage(R.string.gps_enable);
                    aletDialog.setPositiveButton(R.string.settings,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    getActivity().startActivity(myIntent);
                                    toLocationCount = 0;
                                    //get gps
                                }
                            });
                    aletDialog.setNegativeButton(getActivity().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    // TODO Auto-generated method stub
                                    onResume();
                                }
                            });
                    aletDialog.show();
                } else {
                    List<com.google.android.libraries.places.api.model.Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.ADDRESS);
                    Intent autoCompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(mContext);
                    startActivityForResult(autoCompleteIntent, PLACE_DROP_POINT);
                }
                break;
            case R.id.cardViewSpinner10:

                ((DashboardActivity) getActivity()).hideSoftKeyboard(rootView);
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                try {
                    gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }

                try {
                    networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }

                if (!gpsEnabled && !networkEnabled) {
                    AlertDialog.Builder aletDialog = new AlertDialog.Builder(getActivity());
                    aletDialog.setTitle(R.string.gps_setting);
                    aletDialog.setMessage(R.string.gps_enable);
                    aletDialog.setPositiveButton(R.string.settings,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    getActivity().startActivity(myIntent);
                                    toLocationCount = 0;
                                    //get gps
                                }
                            });
                    aletDialog.setNegativeButton(getActivity().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    // TODO Auto-generated method stub
                                    onResume();
                                }
                            });
                    aletDialog.show();
                } else {
                    List<com.google.android.libraries.places.api.model.Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.ADDRESS);
                    Intent autoCompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(mContext);
                    startActivityForResult(autoCompleteIntent, PLACE_DROP_POINT2);
                }
                break;
            case R.id.cardViewSpinner11:

                ((DashboardActivity) getActivity()).hideSoftKeyboard(rootView);
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                try {
                    gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }

                try {
                    networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }

                if (!gpsEnabled && !networkEnabled) {
                    AlertDialog.Builder aletDialog = new AlertDialog.Builder(getActivity());
                    aletDialog.setTitle(R.string.gps_setting);
                    aletDialog.setMessage(R.string.gps_enable);
                    aletDialog.setPositiveButton(R.string.settings,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    getActivity().startActivity(myIntent);
                                    toLocationCount = 0;
                                    //get gps
                                }
                            });
                    aletDialog.setNegativeButton(getActivity().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    // TODO Auto-generated method stub
                                    onResume();
                                }
                            });
                    aletDialog.show();
                } else {
                    List<com.google.android.libraries.places.api.model.Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.ADDRESS);
                    Intent autoCompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(mContext);
                    startActivityForResult(autoCompleteIntent, PLACE_DROP_POINT3);
                }
                break;
            case R.id.btnSave:

                productWeight = mEdtProductWeight.getText().toString().trim();
                quantityProduct = mEdtQuantityProduct.getText().toString().trim();
                ((DashboardActivity) getActivity()).hideSoftKeyboard(rootView);
                if (productTypes.equals("0")) {
                    Toast.makeText(getActivity(), "please select Product Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (strVehicleTypeId.equals("")) {
                    Toast.makeText(getActivity(), "please select Vehicle type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mEdtDateAndTime.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "please select order date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (strFromLocationLat.equals("") && strFromLocationLong.equals("")) {
                    Toast.makeText(getActivity(), "please select pickup point", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (strToLocationLat.equals("") && strToLocationLong.equals("")) {
                    Toast.makeText(getActivity(), "please select drop point", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEdtProductWeight.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "please Enter Product Weight", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEdtQuantityProduct.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "please Enter Product Quantity ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.valueOf(productWeight) > Integer.parseInt(vehicleCapacity)) {
                    Toast.makeText(getActivity(), "Selected Vehicle Capacity is Less than you entered..", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!str3ToLocationAddress.equals("")) {
                    finalDropAddress = str3ToLocationAddress;
                } else if (!str2ToLocationAddress.equals("")) {
                    finalDropAddress = str2ToLocationAddress;
                } else {
                    finalDropAddress = str1ToLocationAddress;
                }
                if (NetworkUtil.getConnectivityStatus(mContext) != NetworkUtil.TYPE_NOT_CONNECTED) {
                    validateData();
                } else {
                    noInternetDialog(getResources().getString(R.string.msg_no_internet_connection));
                    mCardViewSave.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void validateData() {
        mCardViewSave.setVisibility(View.GONE);
        placeOrder(strVehicleTypeId, strFromLocationLat, strFromLocationLong, strToLocationLat, strToLocationLong, str2ToLocationLat, str2ToLocationLong, str3ToLocationLat, str3ToLocationLong,
                mEdtDateAndTime.getText().toString().trim(), mEdtProductWeight.getText().toString().trim(), mEdtQuantityProduct.getText().toString().trim());
    }

    private void noInternetDialog(String message) {
        if (!getActivity().isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.app_name) + " " + getString(R.string.says));
            builder.setMessage(message);
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
    }

    private void callService(StringRequest sr) {
        sr.setRetryPolicy(new DefaultRetryPolicy(
                RestUrl.REQUEST_TIMEOUT,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).cancelAll(sr);
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void displayDialog(String message) {
        if (!getActivity().isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.app_name) + " " + getString(R.string.says));
            builder.setMessage(message);
            builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    cpd.show();
                    callService(sr);
                    loadVehicleTypeData();
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
    }

    private void startDatePickerDialogNext() {
        Calendar c = Calendar.getInstance();
        if (!mEdtDateAndTime.getText().toString().trim().isEmpty()) {
            try {
                c.setTime(DateTimeUtils.appDateFormat.parse(mEdtDateAndTime.getText().toString().trim()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int start_mYear = c.get(Calendar.YEAR);
        int start_mMonth = c.get(Calendar.MONTH);
        int start_mDay = c.get(Calendar.DAY_OF_MONTH);
        // Launch Date Picker Dialog
        dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (nextDate == null)
                            nextDate = Calendar.getInstance();
                        try {
                            Date d2 = DateTimeUtils.appDateFormat.parse(year + "-"
                                    + (monthOfYear + 1) + "-" + dayOfMonth);
                            nextDate.setTime(d2);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date = year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth;
                        timePick(Date);
                        if (!mEdtDateAndTime.getText().toString().equals("")) {
                            // getDriverName(strOrderTypeId);

                        }
                    }
                }, start_mYear, start_mMonth, start_mDay);
        dpd.getDatePicker().setMinDate(new Date().getTime());
        dpd.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKUP_POINT) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = (Place) Autocomplete.getPlaceFromIntent(data);
                strFromLocationAddress = (String) place.getAddress();
                tv_PlacePickupPoint.setText(place.getAddress());
                LatLng location = place.getLatLng();
                strFromLocationLat = String.valueOf(location.latitude);
                strFromLocationLong = String.valueOf(location.longitude);
                //Log.d(TAG, "Selected Place: "+strFromLocationLat+","+strFromLocationLong);
            }
        }

        if (requestCode == PLACE_DROP_POINT) {
            if (resultCode == getActivity().RESULT_OK) {
                //Place place = PlacePicker.getPlace(data, getActivity());
                Place place = (Place) Autocomplete.getPlaceFromIntent(data);
                str1ToLocationAddress = (String) place.getAddress();
                // finalDropAddress = str1ToLocationAddress;
                tv_PlaceDropPoint.setText(str1ToLocationAddress);
                LatLng location = place.getLatLng();
                strToLocationLat = String.valueOf(location.latitude);
                strToLocationLong = String.valueOf(location.longitude);

            }
        }
        if (requestCode == PLACE_DROP_POINT2) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = (Place) Autocomplete.getPlaceFromIntent(data);
                str2ToLocationAddress = (String) place.getAddress();
                //finalDropAddress = str2ToLocationAddress;
                tv_PlaceDropPoint2.setVisibility(View.VISIBLE);
                tv_PlaceDropPoint2.setText(str2ToLocationAddress);
                LatLng to2location = place.getLatLng();
                str2ToLocationLat = String.valueOf(to2location.latitude);
                str2ToLocationLong = String.valueOf(to2location.longitude);

            }
        }
        if (requestCode == PLACE_DROP_POINT3) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = (Place) Autocomplete.getPlaceFromIntent(data);
                str3ToLocationAddress = (String) place.getAddress();
                //finalDropAddress = str3ToLocationAddress;
                tv_PlaceDropPoint3.setVisibility(View.VISIBLE);
                tv_PlaceDropPoint3.setText(str3ToLocationAddress);
                LatLng to3Location = place.getLatLng();
                str3ToLocationLat = String.valueOf(to3Location.latitude);
                str3ToLocationLong = String.valueOf(to3Location.longitude);


            }
        }
    }


    private void timePick(final String Date) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String hr = "";
                String min = "";
                if (selectedHour < 10) {
                    hr = "0" + selectedHour;
                } else {
                    hr = "" + selectedHour;
                }
                if (selectedMinute < 10) {
                    min = "0" + selectedMinute;
                } else {
                    min = "" + selectedMinute;
                }
                String time = hr + ":" + min;

                DateAndTime = Date + " " + time;
                mEdtDateAndTime.setText(DateAndTime);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void placeOrder(
            final String vehicleId, final String fromLocationLat, final String fromLocationLong, final String to1LocationLat, final String to1LocationLong, final String to2LocationLat, final String to2LocationLong, final String to3LocationLat, final String to3LocationLong,
            final String pickUpDateAndTime, final String weightOfProduct, final String quantityProduct) {
final CustomProgressDialog customProgressDialog =new CustomProgressDialog(getActivity());
customProgressDialog.show();

        RequestBody companyId = RequestBody.create(MultipartBody.FORM, sharedPreference.getCompanyId());
        RequestBody customerID = RequestBody.create(MultipartBody.FORM, sharedPreference.getCustomerId());
        RequestBody vehicleIds = RequestBody.create(MultipartBody.FORM, vehicleId);
        RequestBody pickPoint = RequestBody.create(MultipartBody.FORM, strFromLocationAddress);
        RequestBody dropPoint = RequestBody.create(MultipartBody.FORM, finalDropAddress);
        RequestBody fromLat = RequestBody.create(MultipartBody.FORM, fromLocationLat);
        RequestBody fromLong = RequestBody.create(MultipartBody.FORM, fromLocationLong);
        RequestBody toLat = RequestBody.create(MultipartBody.FORM, to1LocationLat);
        RequestBody toLong = RequestBody.create(MultipartBody.FORM, to1LocationLong);
        RequestBody to2Lat = RequestBody.create(MultipartBody.FORM, to2LocationLat);
        RequestBody to2Long = RequestBody.create(MultipartBody.FORM, to2LocationLong);
        RequestBody to3Lat = RequestBody.create(MultipartBody.FORM, to3LocationLat);
        RequestBody to3Long = RequestBody.create(MultipartBody.FORM, to3LocationLong);
        RequestBody orderDate = RequestBody.create(MultipartBody.FORM, pickUpDateAndTime);
        RequestBody productType = RequestBody.create(MultipartBody.FORM, productTypes);
        RequestBody productWeight = RequestBody.create(MultipartBody.FORM, weightOfProduct);
        RequestBody productQuantity = RequestBody.create(MultipartBody.FORM, quantityProduct);

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
                //.addConverterFactory(GsonConverterFactory.create(gson))
                .client(client1)
                .build();
        RetrofitAPI client = retrofit.create(RetrofitAPI.class);
        Call<ResponseModel> call = client.customerPlaceOrder(companyId, customerID, vehicleIds, pickPoint, dropPoint, fromLat, fromLong, toLat, toLong, to2Lat, to2Long, to3Lat, to3Long, orderDate, productType, productWeight, productQuantity);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                customProgressDialog.hide();
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("1")) {
                        ((DashboardActivity) getActivity()).setSnackBar(response.body().getMessage());
                        ((DashboardActivity) getActivity()).clearBackStack();
                        ((DashboardActivity)getActivity()).changeFragment(new DashboardFragment(),"FragmentDashboard");

                    } else {
                        ((DashboardActivity) getActivity()).setSnackBar(response.body().getMessage());
                    }
                } else {
                    customProgressDialog.hide();
                    ((DashboardActivity) getActivity()).setSnackBar(getString(R.string.somethingWrong));
                    mCardViewSave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                customProgressDialog.hide();
                ((DashboardActivity) getActivity()).setSnackBar(getString(R.string.somethingWrong));
                mCardViewSave.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadVehicleTypeData() {
       final CustomProgressDialog customProgressDialog= new CustomProgressDialog(getActivity());
       customProgressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestUrl.VehicleType,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        customProgressDialog.hide();
                        try {
                            ArrayList<String> stringVehicleTypeList = new ArrayList<>();
                            stringVehicleTypeList.add("Vehicle Type");
                            JSONObject Response = new JSONObject(response);
                            if (Response.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                                JSONArray vehicleDate = Response.getJSONArray(RestKeys.DATA);

                                vehicleTypeList = new ArrayList<HashMap<String, String>>();
                                HashMap map = new HashMap();
                                map.put(RestKeys.vehicleId, "0");
                                map.put(RestKeys.vehicleCapacity, "0");
                                vehicleTypeList.add(map);

                                for (int j = 0; j < vehicleDate.length(); j++) {
                                    VehicleType vehicleType = new VehicleType();
                                    JSONObject vehicle = vehicleDate.getJSONObject(j);

                                    if (vehicle.has(RestKeys.vehicleId) && !vehicle.equals("null")) {
                                        vehicleType.setVehicleTypeId(vehicle.getString(RestKeys.vehicleId));
                                    }
                                    if (vehicle.has(RestKeys.vehicleName) && !vehicle.equals("null")) {
                                        vehicleType.setVehicleTypeName(vehicle.getString(RestKeys.vehicleName));
                                    }
                                    if (vehicle.has(RestKeys.vehicleCapacity)) {
                                        vehicleType.setVehicleCapacity(vehicle.getString(RestKeys.vehicleCapacity));
                                    }
                                    map = new HashMap();
                                    map.put(RestKeys.vehicleId, vehicleType.getVehicleTypeId());
                                    map.put(RestKeys.vehicleCapacity, vehicleType.getVehicleCapacity());
                                    vehicleTypeList.add(map);

                                    stringVehicleTypeList.add(vehicleType.getVehicleTypeName());


                                }
                                spnVehicleType.setAdapter(new ArrayAdapter(getActivity(),
                                        android.R.layout.simple_spinner_dropdown_item,
                                        stringVehicleTypeList));

                                spnVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (position != 0) {
                                            strVehicleTypeId = vehicleTypeList.get(position).get(RestKeys.vehicleId);
                                            //Log.d(TAG, "onItemSelected: " + strVehicleTypeId + ","+ vehicleCapacity);

                                            getVehicleCapacity(strVehicleTypeId);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                            } else {
                                ((DashboardActivity) getActivity()).setSnackBar(getString(R.string.somethingWrong));
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

//         VolleySingleTone(mContext).getRequestQueue().add(stringRequest);
        VolleySingleTone.getInstance(getActivity()).getRequestQueue().add(stringRequest);

    }

    private void loadProductType() {
        StringRequest product = new StringRequest(Request.Method.GET, RestUrl.categoryList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final ArrayList<String> stringProductList = new ArrayList<>();
                    stringProductList.add("Product Type");
                    if (jsonObject.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                        JSONArray ja_ProductTypes = jsonObject.getJSONArray(RestKeys.DATA);
                        productTypeList = new ArrayList<HashMap<String, String>>();
                        HashMap map = new HashMap();
                        map.put(RestKeys.CATEGORY_ID, "0");
                        productTypeList.add(map);
                        for (int i = 0; i < ja_ProductTypes.length(); i++) {
                            JSONObject jo_CompanyTypes = ja_ProductTypes.getJSONObject(i);
                            productType productType = new productType();
                            if (jo_CompanyTypes.has(RestKeys.CATEGORY_ID) && !jo_CompanyTypes.isNull(RestKeys.CATEGORY_ID))
                                productType.setProductId(jo_CompanyTypes.getString(RestKeys.CATEGORY_ID));

                            if (jo_CompanyTypes.has(RestKeys.CATEGORY_NAME) && !jo_CompanyTypes.isNull(RestKeys.CATEGORY_NAME))
                                productType.setProductType(jo_CompanyTypes.getString(RestKeys.CATEGORY_NAME));

                            map = new HashMap();
                            map.put(RestKeys.CATEGORY_ID, productType.getProductType());
                            productTypeList.add(map);
                            stringProductList.add(productType.getProductId());
                        }
                        mSpinnerProduct.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringProductList));
                        mSpinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0) {
                                    productTypes = productTypeList.get(position).get(RestKeys.CATEGORY_ID);
                                    // Log.d(TAG, "onItemSelected: "+productTypes);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
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
        VolleySingleTone.getInstance(getActivity()).getRequestQueue().add(product);
    }

    private void getVehicleCapacity(String vehicleId) {
        final CustomProgressDialog customProgressDialog= new CustomProgressDialog(getActivity());
        customProgressDialog.show();
        String Url = Uri.parse(RestUrl.vehicleCapacity)
                .buildUpon()
                .appendQueryParameter(RestKeys.V_ID, vehicleId)
                .build().toString();
        StringRequest loadCapacity = new StringRequest(Request.Method.GET,
                Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        customProgressDialog.hide();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                                JSONArray capacity = res.getJSONArray(RestKeys.DATA);
                                JSONObject vehiclecapacity = capacity.getJSONObject(0);

                                if (vehiclecapacity.has(RestKeys.vehicleCapacity)) {
                                    vehicleCapacity = vehiclecapacity.getString(RestKeys.vehicleCapacity);
                                    //  Log.d(TAG, "Capacity: "+vehicleCapacity);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//            cpd.hide();
            }
        });
        VolleySingleTone.getInstance(getContext()).getRequestQueue().add(loadCapacity);
    }

    private void pickUpLocationPicker() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder aletDialog = new AlertDialog.Builder(getActivity());
            aletDialog.setTitle(R.string.gps_setting);
            aletDialog.setMessage(R.string.gps_enable);
            aletDialog.setPositiveButton(R.string.settings,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            getActivity().startActivity(myIntent);
                            //get gps
                        }
                    });
            aletDialog.setNegativeButton(getActivity().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub
                            onResume();
                        }
                    });
            aletDialog.show();
        } else {

            List<com.google.android.libraries.places.api.model.Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.ADDRESS);
            Intent autoCompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(mContext);
            startActivityForResult(autoCompleteIntent, PLACE_PICKUP_POINT);
//            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

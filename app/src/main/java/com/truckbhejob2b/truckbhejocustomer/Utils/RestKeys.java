package com.truckbhejob2b.truckbhejocustomer.Utils;

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.Dash;

import java.security.PublicKey;

public class RestKeys {

    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String SUCCESS = "success";
    public static final String SUCCESS_VALUE = "1";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";
    public static final String API_KEY = "API-KEY";
    public static final String API_KEY_VALUE = "dHJ1Y2tiaGVqb0IyQg==";

    public static final String OTP_SUCCESS = "ErrorCode";
    public static final String OTP_SUCCESS_VALUE = "000";

    // Place Order Fragment
    public static final String dashCompId = "compi_id";
    public static final String dashCompName = "company_name";

    public static final String customerId = "id";
    public static final String customerName = "customer_name";

    public static final String vehicleId = "id";
    public static final String vehicleName = "name";
    public static final String vehicleCapacity = "vehicle_capacity";
    public static final String CATEGORY_NAME = "id";
    public static final String CATEGORY_ID ="categoryName";

    public static final String V_ID = "v_id";


    // Login Activity
    public static final String LOGIN_MOBILE_NUMBER="mobileNumber";

    //OTP Activity
    public static final String VERIFY_OTP = "OTP";
    public static final String VERIFY_DEVICE_TOKEN = "deviceToken";

    public static final String RES_VERIFY_CUSTOMER_ID ="cutomerId";
    public static final String RES_VERIFY_CUSTOMER_NAME = "customerName";
    public static final String RES_VERIFY_CUSTOMER_EMAIL ="customerEmail";
    public static final String RES_VERIFY_CUSTOMER_MOBILE="customerContact";
    public static final String RES_VERIFY_COMPANY_NAME = "companyName";
    public static final String RES_VERIFY_COMPANY_ID = "companyId";
    public static final String RES_VERIFY_API_TOKEN = "apiToken";


    //Dashboard Fragment

    //Validate Login
    public static final String VALIDATE_CUSTOMER_ID = "customerId";
    public static final String VALIDATE_API_TOKEN = "apiToken";

    // Get Count
    public static final String COUNT_CUSTOMER_ID = "customer_id";
    public static final String RUNNING_COUNT = "running_order";
    public static final String DELIVERED_COUNT = "deliver_order";

    public static final String DASH_ORDER_ORDER_ID="order_id";
    public static final String DASH_ORDER_VEHICLE_NUMBER="vehicle_reg_no";
    public static final String DASH_ORDER_STATUS="status";
    public static final String DASH_ORDER_FROM_LAT="from_location_latitude";
    public static final String DASH_ORDER_FROM_LONG="from_location_longitude";
    public static final String DASH_ORDER_TO_LAT1="to_location_latitude";
    public static final String DASH_ORDER_TO_LONG1="to_location_longitude";
    public static final String DASH_ORDER_TO_LAT2="to2_location_latitude";
    public static final String DASH_ORDER_TO_LONG2="to2_location_longitude";
    public static final String DASH_ORDER_TO_LAT3="to3_location_latitude";
    public static final String DASH_ORDER_TO_LONG3="to3_location_longitude";
    public static final String DASH_ORDER_VEHICLE_TYPE="name";
    public static final String DASH_ORDER_PRODUCT_TYPE="category_name";
    public static final String DASH_ORDER_DATE ="order_date";
    public static final String DASH_ORDER_DRIVER_NUMBER = "mobile_no";






    //Get Trips
    public static final String TRIPS_COMPANY_ID = "company_id";
    public static final String TRIPS_STATUS = "status";
    public static final String TRIPS_OFFSET = "offSet";

    public static final String RES_TRIPS_ORDER_ID ="order_id";
    public static final String RES_TRIPS_ORDER_DATE ="order_date";
    public static final String RES_TRIPS_FROM_LAT ="from_location_latitude";
    public static final String RES_TRIPS_FROM_LONG ="from_location_longitude";
    public static final String RES_TRIPS_TO_LAT ="to_location_latitude";
    public static final String RES_TRIPS_TO_LONG ="to_location_longitude";
    public static final String RES_TRIPS_TO2_LAT ="to2_location_latitude";
    public static final String RES_TRIPS_TO2_LONG ="to2_location_longitude";
    public static final String RES_TRIPS_TO3_LAT ="to3_location_latitude";
    public static final String RES_TRIPS_TO3_LONG ="to3_location_longitude";
    public static final String RES_TRIPS_VEHICLE_TYPE ="vehicle_type";
    public static final String RES_TRIPS_ORDER_STATUS ="status";
    public static final String RES_TRIPS_DRIVER_NUMBER ="mobile_no";
    public static final String RES_TRIPS_VEHICLE_NUMBER ="vehicleNumber";
    public static final String RES_TRIPS_POD_STATUS = "pod_status";
    public static final String RES_TRIPS_TRACK_STATUS = "onTime";
    public static final String RES_TRIPS_PRODUCT_TYPE = "product_type";

    // Search
    public static final String SEARCH_TEXT="search_dtl";

// Fragment Place Order
    public static final String PLACE_COMPANY_ID="company_id";
    public static final String PLACE_CUSTOMER_ID="customer_id";
    public static final String PLACE_VEHICLE_TYPE_ID="vehicle_id";
    public static final String PLACE_PICK_POINT="pickup_point";
    public static final String PLACE_DROP_POINT="drop_point";
    public static final String PLACE_FROM_LAT="from_location_latitude";
    public static final String PLACE_FROM_LONG="from_location_longitude";
    public static final String PLACE_TO_LAT="to_location_latitude";
    public static final String PLACE_TO_LONG="to_location_longitude";
    public static final String PLACE_TO2_LAT="to2_location_latitude";
    public static final String PLACE_TO2_LONG="to2_location_longitude";
    public static final String PLACE_TO3_LAT="to3_location_latitude";
    public static final String PLACE_TO3_LONG="to3_location_longitude";
    public static final String PLACE_PRODUCT_TYPE="product_type";
    public static final String PLACE_ORDER_DATE="order_date";
    public static final String PLACE_PRODUCT_WEIGHT="product_weight";
    public static final String PLACE_PRODUCT_QUANTITY="product_quantity";


    // Tracking Fragment

    public static final String TRACKING_ORDER_ID = "order_id";
    public static final String TRACK_PICK_POINT ="pickup_point";
    public static final String TRACK_DROP_POINT ="drop_point";
    public static final String TRACK_FROM_LOC_LAT ="fromlat";
    public static final String TRACK_FROM_LOC_LONG ="fromLong";
    public static final String TRACK_TO_LAT = "toLat1";
    public static final String TRACK_TO_LONG = "toLong1";
    public static final String TRACK_TO2_LAT = "toLat2";
    public static final String TRACK_TO2_LONG = "toLong2";
    public static final String TRACK_TO3_LAT = "toLat3";
    public static final String TRACK_TO3_LONG = "toLong3";

    public static final String TRACK_ORDER_ID= "orderId";
    public static final String GET_TRACK_LAT_LONG = "latLong";
    public static final String GET_TRACK_ADDRESS = "lastAddress";
    public static final String GET_TRACK_ON_TIME = "onTime";
    public static final String GET_TRACK_ETA = "etaTime";
    public static final String GET_TRACK_NEAR_BY_DEST = "nearByDest";
    public static final String GET_TRACK_NEAR_BY_SRC = "nearBySrc";
    public static final String GET_TRACK_HALT_MSG = "haltMsg";
    public static final String GET_TRACK_LAST_TRACK = "lastTrack";


    // Fragment Order Details
    public static final String ORDER_DETAILS_ORDER_ID = "orderId";
    public static final String RES_ORDER_DTL_VEHICLE_NUMBER = "vehicleNumber";
    public static final String RES_ORDER_DTL_DRIVER_NAME = "driverName";
    public static final String RES_ORDER_DTL_DRIVER_NUMBER = "driverMobileNumber";
    public static final String RES_ORDER_DTL_LR_NUMBER = "lrNumber";
    public static final String RES_ORDER_DTL_LR_IMAGE = "lrImage";
    public static final String RES_ORDER_DTL_EWAY_NUMBER = "ewayNumber";
    public static final String RES_ORDER_DTL_EWAY_IMAGE = "ewayImage";
    public static final String RES_ORDER_DTL_POD_FRONT = "podFront";
    public static final String RES_ORDER_DTL_POD_BACK = "podBack";
    public static final String RES_ORDER_DTL_DATE = "date";
    public static final String RES_ORDER_DTL_FEEDBACK = "feedback";


    //Fragment Setting

    public static final String SETTING_CUSTOMER_ID = "customerId";

    public static final String RES_SETTING_COMPANY_NAME = "companyName";
    public static final String RES_SETTING_CUSTOMER_NAME = "customerName";
    public static final String RES_SETTING_CUSTOMER_CONTACT = "customerContact";
    public static final String RES_SETTING_CUSTOMER_EMAIL = "customerEmail";
    public static final String RES_SETTING_CUSTOMER_ADDRESS = "customerAddress";


    // Fragment Notification
    public static final String NOTIFICATION_TITLE ="ntitle";
    public static final String NOTIFICATION_DESC ="nDescription";
    public static final String NOTIFICATION_ACTION ="nAction";
    public static final String NOTIFICATION_CREATED_ON ="nDate";


    // Update FCM
    public static final String UPDATE_FCM_CUSTOMER_ID = "customerId";
    public static final String UPDATE_FCM_FCM_TOKEN = "fcmToken";
    public static final String UPDATE_FCM_API_TOKEN = "apiToken";


    // Download File
    public static final String URI ="uri";
    public static final String ORDER_ID="orderId";

    // Activity Registration
//    public static final String REG_COMPANY_NAME = "company_name";
//    public static final String REG_CUSTOMER_NAME ="customer_name";
//    public static final String REG_CUSTOMER_NUMBER = "customer_number";
//    public static final String REG_CUSTOMER_EMAIL = "customer_email";
//    public static final String REG_CUSTOMER_ADDRESS = "customer_address";

    //
    public static final String REG_COMPANY_NAME = "company_id";
    public static final String REG_CUSTOMER_NAME ="customer_name";
    public static final String REG_CUSTOMER_NUMBER = "customer_number";
    public static final String REG_CUSTOMER_EMAIL = "customer_email";
    public static final String REG_CUSTOMER_ADDRESS = "customer_address";





}

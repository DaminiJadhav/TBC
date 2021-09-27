package com.truckbhejob2b.truckbhejocustomer.Utils;

public class RestUrl {
    public static final int REQUEST_TIMEOUT = 10000;


    //Testing Server
//    public static final String NEW_BASE_URL = "https://truckbhejo.sdaemon.com/";
//    public static final String NEW_IMAGE_URL = "https://truckbhejo.sdaemon.com/truckbhejo_api/";
//    public static final String NEW_API_PATH = "truckbhejo_api/index.php/web/";


    // Live Server
    public static final String NEW_BASE_URL = "https://truckbhejo.com/";
    public static final String NEW_IMAGE_URL = "https://truckbhejo.com/truckbhejo_api/";
    public static final String NEW_API_PATH = "truckbhejo_api/index.php/web/";


    public static final String CUSTOMER_LOGIN = NEW_BASE_URL + NEW_API_PATH + "getCustomerLogin";
    public static final String VERIFY_OTP = NEW_BASE_URL+NEW_API_PATH+"verifyCustomerOTP";

    public static final String VALIDATE_LOGIN = NEW_BASE_URL+NEW_API_PATH+"verifyCustomerApiToken";

    public static final String GET_DASH_COUNT = NEW_BASE_URL + NEW_API_PATH + "Customer_dashboard";

    public static final String GET_DASH_TRIPS = NEW_BASE_URL + NEW_API_PATH + "getCustomerOrders";

    public static final String SEARCH_TRIP = NEW_BASE_URL+NEW_API_PATH+"getCustomerSearch";

    public static final String ORDER_DETAILS = NEW_BASE_URL+NEW_API_PATH+"getCustomerOrderDtl";

    public static final String PLACE_ORDER = NEW_BASE_URL+NEW_API_PATH;

    public static final String VehicleType =  NEW_BASE_URL + NEW_API_PATH + "getVehicleType";
    public static final String categoryList = NEW_BASE_URL+NEW_API_PATH+"categoryList";
    public static final String vehicleCapacity = NEW_BASE_URL + NEW_API_PATH + "getIDVehicleType";

    public static final String TRACK_ORDER = NEW_BASE_URL+NEW_API_PATH+"getTrackingDetails";
    public static final String TRACKING_POINTS = NEW_BASE_URL+NEW_API_PATH+"getTrackVehicle";

    public static final String UPDATE_FCM = NEW_BASE_URL+NEW_API_PATH+"updateCustomerFCM";

    public static final String LOGOUT_CUSTOMER = NEW_BASE_URL+NEW_API_PATH+"logOutCustomer";

    public static final String USER_SETTING = NEW_BASE_URL+NEW_API_PATH+"getCustomerDtl";

    public static final String NOTIFICATION = NEW_BASE_URL+NEW_API_PATH+"getCustomerNotifications";

    public static final String COMPANY_LIST = NEW_BASE_URL+NEW_API_PATH+"getCompanyNameList";

    public static final String CUSTOMER_REGISTRATION = NEW_BASE_URL+NEW_API_PATH+"CustomerEmailSMS";

  //  public static final String CUSTOMER_REGISTRATION = NEW_BASE_URL+NEW_API_PATH+"CustomerRegistration";





}

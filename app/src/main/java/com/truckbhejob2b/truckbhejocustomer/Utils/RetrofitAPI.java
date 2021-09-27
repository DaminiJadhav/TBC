package com.truckbhejob2b.truckbhejocustomer.Utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitAPI {
    @Multipart
    @POST("getCustomerLogin")
    Call<ResponseModel>getCustomerLogin(
            @Part(RestKeys.LOGIN_MOBILE_NUMBER) RequestBody mobileNumber

    );
     @Multipart
    @POST("CustomerOrderPlace")
    Call<ResponseModel>customerPlaceOrder(
            @Part(RestKeys.PLACE_COMPANY_ID) RequestBody companyId,
            @Part(RestKeys.PLACE_CUSTOMER_ID) RequestBody customerId,
            @Part(RestKeys.PLACE_VEHICLE_TYPE_ID) RequestBody vehicleIds,
            @Part(RestKeys.PLACE_PICK_POINT) RequestBody pickPoint,
            @Part(RestKeys.PLACE_DROP_POINT) RequestBody dropPoint,
            @Part(RestKeys.PLACE_FROM_LAT) RequestBody fromLat,
            @Part(RestKeys.PLACE_FROM_LONG) RequestBody fromLong,
            @Part(RestKeys.PLACE_TO_LAT) RequestBody toLat,
            @Part(RestKeys.PLACE_TO_LONG) RequestBody toLong,
            @Part(RestKeys.PLACE_TO2_LAT) RequestBody to2Lat,
            @Part(RestKeys.PLACE_TO2_LONG) RequestBody to2Long,
            @Part(RestKeys.PLACE_TO3_LAT) RequestBody to3Lat,
            @Part(RestKeys.PLACE_TO3_LONG) RequestBody to3Long,
            @Part(RestKeys.PLACE_ORDER_DATE) RequestBody orderDate,
            @Part(RestKeys.PLACE_PRODUCT_TYPE) RequestBody productType,
            @Part(RestKeys.PLACE_PRODUCT_WEIGHT) RequestBody productWeight,
            @Part(RestKeys.PLACE_PRODUCT_QUANTITY) RequestBody productQuantity
            );
    @Multipart
    @POST("CustomerEmailSMS")
    Call<ResponseModel>customerRegistration(
            @Part(RestKeys.REG_COMPANY_NAME) RequestBody companyName,
            @Part(RestKeys.REG_CUSTOMER_NAME) RequestBody customerName,
            @Part(RestKeys.REG_CUSTOMER_NUMBER) RequestBody customerNumber,
            @Part(RestKeys.REG_CUSTOMER_EMAIL) RequestBody customerEmail,
            @Part(RestKeys.REG_CUSTOMER_ADDRESS) RequestBody customerAddress
    );
}

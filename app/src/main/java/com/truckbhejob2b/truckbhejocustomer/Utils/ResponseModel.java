package com.truckbhejob2b.truckbhejocustomer.Utils;

import com.google.gson.annotations.SerializedName;

public class ResponseModel {

    @SerializedName("success")
    String success;
    @SerializedName("message")
    String message;
    @SerializedName("status")
    String status;


    public String getSuccess() {
        return success;
    }


    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}

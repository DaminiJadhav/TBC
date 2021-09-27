package com.truckbhejob2b.truckbhejocustomer.Model;

import java.io.Serializable;

/**
 * Created by Tech Indiana on 08-Feb-17.
 */

public class Dashboard implements Serializable {
    private String dashCompId;
    private String dashCompName;
    private String dashRunningOrders;
    private String dashCompleteOrders;
    private String dashPendingOrders;
    private String dashCompanyLogo;
    private String dashCompanyContact;

    public String getDashCompleteOrders() {
        return dashCompleteOrders;
    }

    public void setDashCompleteOrders(String dashCompleteOrders) {
        this.dashCompleteOrders = dashCompleteOrders;
    }

    public String getDashPendingOrders() {
        return dashPendingOrders;
    }

    public void setDashPendingOrders(String dashPendingOrders) {
        this.dashPendingOrders = dashPendingOrders;
    }

    public String getDashCompanyContact() {
        return dashCompanyContact;
    }

    public void setDashCompanyContact(String dashCompanyContact) {
        this.dashCompanyContact = dashCompanyContact;
    }

    public String getDashCompanyLogo() {
        return dashCompanyLogo;
    }

    public void setDashCompanyLogo(String dashCompanyLogo) {
        this.dashCompanyLogo = dashCompanyLogo;
    }


    public String getDashCompId() {
        return dashCompId;
    }

    public void setDashCompId(String dashCompId) {
        this.dashCompId = dashCompId;
    }

    public String getDashCompName() {
        return dashCompName;
    }

    public void setDashCompName(String dashCompName) {
        this.dashCompName = dashCompName;
    }

    public String getDashRunningOrders() {
        return dashRunningOrders;
    }

    public void setDashRunningOrders(String dashRunningOrders) {
        this.dashRunningOrders = dashRunningOrders;
    }


}

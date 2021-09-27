package com.truckbhejob2b.truckbhejocustomer.Model;

public class NotificationModel {
    private String orderId;
    private String nTitle;
    private String nDesc;
    private String nAction;
    private String nDate;

    public NotificationModel() {
        this.orderId = orderId;
        this.nTitle = nTitle;
        this.nDesc = nDesc;
        this.nAction = nAction;
        this.nDate = nDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getnTitle() {
        return nTitle;
    }

    public void setnTitle(String nTitle) {
        this.nTitle = nTitle;
    }

    public String getnDesc() {
        return nDesc;
    }

    public void setnDesc(String nDesc) {
        this.nDesc = nDesc;
    }

    public String getnAction() {
        return nAction;
    }

    public void setnAction(String nAction) {
        this.nAction = nAction;
    }

    public String getnDate() {
        return nDate;
    }

    public void setnDate(String nDate) {
        this.nDate = nDate;
    }
}

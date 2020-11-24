package com.cms.callmanager.fcm_notification;

class Notification_Model {
    String DocketNo;
    String ATMId;
    String Address;
    String CalllogDateTime;
    String TargetResponseTime;
    String TargetResolutionTime;
    String RequestType;



    public String getDocketNo() {
        return DocketNo;
    }

    public void setDocketNo(String docketNo) {
        DocketNo = docketNo;
    }

    public String getATMId() {
        return ATMId;
    }

    public void setATMId(String ATMId) {
        this.ATMId = ATMId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCalllogDateTime() {
        return CalllogDateTime;
    }

    public void setCalllogDateTime(String calllogDateTime) {
        CalllogDateTime = calllogDateTime;
    }

    public String getTargetResponseTime() {
        return TargetResponseTime;
    }

    public void setTargetResponseTime(String targetResponseTime) {
        TargetResponseTime = targetResponseTime;
    }

    public String getTargetResolutionTime() {
        return TargetResolutionTime;
    }

    public void setTargetResolutionTime(String targetResolutionTime) {
        TargetResolutionTime = targetResolutionTime;
    }

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }
}

package com.cms.callmanager.model;

public class Open_Call_Details_model {

    private String NoofCallsfortheATM;
    private String ATMId;
    private String DocketNo;
    private String CallType;
    private String SubStatus;
  //  private  Boolean selectedYesNo;
      private  String selectedYesNo;


    public String getNoofCallsfortheATM() {
        return NoofCallsfortheATM;
    }

    public void setNoofCallsfortheATM(String noofCallsfortheATM) {
        NoofCallsfortheATM = noofCallsfortheATM;
    }

    public String getATMId() {
        return ATMId;
    }

    public void setATMId(String ATMId) {
        this.ATMId = ATMId;
    }

    public String getDocketNo() {
        return DocketNo;
    }

    public void setDocketNo(String docketNo) {
        DocketNo = docketNo;
    }

    public String getCallType() {
        return CallType;
    }

    public void setCallType(String callType) {
        CallType = callType;
    }

    public String getSubStatus() {
        return SubStatus;
    }

    public void setSubStatus(String subStatus) {
        SubStatus = subStatus;
    }

   /* public Boolean getSelectedYesNo() {
        return selectedYesNo;
    }

    public void setSelectedYesNo(Boolean selectedYesNo) {
        this.selectedYesNo = selectedYesNo;
    }*/

    public String getSelectedYesNo() {
        return selectedYesNo;
    }

    public void setSelectedYesNo(String selectedYesNo) {
        this.selectedYesNo = selectedYesNo;
    }
}

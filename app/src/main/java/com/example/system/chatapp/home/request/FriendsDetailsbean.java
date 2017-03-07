package com.example.system.chatapp.home.request;

/**
 * Created by SYSTEM on 02-12-2016.
 */

public class FriendsDetailsbean {

    private String requestSendername;
    private String requestSenderage;
    private String requestSendermobilenumber;
    private String requestSenderEmail;
    private String requestSenderProfilepic;
    private String requestReceivername;
    private String requestReceiverage;
    private String requestReceivermobilenumber;
    private String requestReceiverEmail;
    private String requestReceiverProfilepic;
    private String requestStatus;
    private String mREquest_key;

    public FriendsDetailsbean(){

    }

    public FriendsDetailsbean(String requestReceiverEmail,String requestReceiverage, String requestReceivermobilenumber,
                              String requestReceivername,String requestReceiverProfilepic,String requestSenderEmail, String requestSenderage,
                              String requestSendermobilenumber,String requestSendername,String requestStatus,
                              String requestSenderProfilepic){
        this.requestSendername=requestSendername;
        this.requestSenderage=requestSenderage;
        this.requestSendermobilenumber=requestSendermobilenumber;
        this.requestSenderEmail=requestSenderEmail;
        this.requestSenderProfilepic=requestSenderProfilepic;
        this.requestReceivername=requestReceivername;
        this.requestReceiverage=requestReceiverage;
        this.requestReceivermobilenumber=requestReceivermobilenumber;
        this.requestReceiverEmail=requestReceiverEmail;
        this.requestReceiverProfilepic=requestReceiverProfilepic;
        this.requestStatus=requestStatus;

    }

    public String getRequestSenderProfilepic() {
        return requestSenderProfilepic;
    }

    public void setRequestSenderProfilepic(String requestSenderProfilepic) {
        this.requestSenderProfilepic = requestSenderProfilepic;
    }

    public String getRequestReceiverProfilepic() {
        return requestReceiverProfilepic;
    }

    public void setRequestReceiverProfilepic(String requestReceiverProfilepic) {
        this.requestReceiverProfilepic = requestReceiverProfilepic;
    }

    public String getmREquest_key() {
        return mREquest_key;
    }

    public void setmREquest_key(String mREquest_key) {
        this.mREquest_key = mREquest_key;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestSendername() {
        return requestSendername;
    }

    public void setRequestSendername(String requestSendername) {
        this.requestSendername = requestSendername;
    }

    public String getRequestSenderage() {
        return requestSenderage;
    }

    public void setRequestSenderage(String requestSenderage) {
        this.requestSenderage = requestSenderage;
    }

    public String getRequestSendermobilenumber() {
        return requestSendermobilenumber;
    }

    public void setRequestSendermobilenumber(String requestSendermobilenumber) {
        this.requestSendermobilenumber = requestSendermobilenumber;
    }

    public String getRequestSenderEmail() {
        return requestSenderEmail;
    }

    public void setRequestSenderEmail(String requestSenderEmail) {
        this.requestSenderEmail = requestSenderEmail;
    }

    public String getRequestReceivername() {
        return requestReceivername;
    }

    public void setRequestReceivername(String requestReceivername) {
        this.requestReceivername = requestReceivername;
    }

    public String getRequestReceiverage() {
        return requestReceiverage;
    }

    public void setRequestReceiverage(String requestReceiverage) {
        this.requestReceiverage = requestReceiverage;
    }

    public String getRequestReceivermobilenumber() {
        return requestReceivermobilenumber;
    }

    public void setRequestReceivermobilenumber(String requestReceivermobilenumber) {
        this.requestReceivermobilenumber = requestReceivermobilenumber;
    }

    public String getRequestReceiverEmail() {
        return requestReceiverEmail;
    }

    public void setRequestReceiverEmail(String requestReceiverEmail) {
        this.requestReceiverEmail = requestReceiverEmail;
    }
}

package com.example.system.chatapp.beans;

/**
 * Created by SYSTEM on 30-11-2016.
 */

public class users {


    private String age;
    private String mobilenumber;
    private String name;
    private String emailid;
    private String profileImage;
    private String key;

    public users(){
    }
    public users(String age,String mobilenumber,String name,String emailid,String profileImage){
        this.age=age;
        this.mobilenumber=mobilenumber;
        this.name=name;
        this.emailid=emailid;
        this.profileImage=profileImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getage() {
        return age;
    }

    public void setage(String age) {
        this.age = age;
    }

    public String getmobilenumber() {
        return mobilenumber;
    }

    public void setmobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
}

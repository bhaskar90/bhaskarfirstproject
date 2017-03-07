package com.example.system.chatapp.beans;

/**
 * Created by SYSTEM on 06-12-2016.
 */

public class ImageUpload {


    private String profilePic;

    public ImageUpload(String profilePic){
        this.profilePic=profilePic;
    }

    public ImageUpload(){

    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}

package com.example.system.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;

import com.example.system.chatapp.home.HomeActivity;
import com.example.system.chatapp.login.SignupActivity;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 1500;

    //handler for splashscreen
    Handler mHandlerTime;
    Runnable mRunnableTimeOut;
    SharedPreferences login_infoprre;
    String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        login_infoprre=getSharedPreferences("LoginDetails",0);
        mUsername=login_infoprre.getString("loginname",null);


        mHandlerTime = new Handler();
        mRunnableTimeOut = new Runnable() {
            @Override
            public void run() {

                if(mUsername==null) {
                    Intent iRedirectScreen = new Intent(SplashScreen.this, SignupActivity.class);
                    startActivity(iRedirectScreen);
                    finish();
                }else{
                    Intent iRedirectScreen = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(iRedirectScreen);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onPause() {
        mHandlerTime.removeCallbacks(mRunnableTimeOut);
        super.onPause();
    }

    @Override
    protected void onResume() {
        mHandlerTime.postDelayed(mRunnableTimeOut, SPLASH_TIME_OUT);
        super.onResume();
    }
}

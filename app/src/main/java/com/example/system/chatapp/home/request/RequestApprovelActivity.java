package com.example.system.chatapp.home.request;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.chatapp.R;
import com.example.system.chatapp.beans.Message;
import com.example.system.chatapp.home.UserListActivity;
import com.example.system.chatapp.utils.CircularImageView;
import com.example.system.chatapp.utils.ConnectionDetector;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RequestApprovelActivity extends Activity {

    ImageView vI_tl_back;
    TextView vT_tb_headtext,vT_arp_name,vT_arp_mobile,vT_arp_emailid,
            vT_arp_request,vT_arp_approverequest,vT_arp_rejectrequest;
    String mSendername,mSenderEmail,mSendermobilenumber,mStatus,mKey,mSenderProfilepic;
    CircularImageView vI_arp_profilepic;
    SharedPreferences callServiceprf;
    ConnectionDetector detector;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_approvel);
        initializeViews();
    }

    private void initializeViews() {
        vI_tl_back= (ImageView) findViewById(R.id.vI_tl_back);
        vI_arp_profilepic= (CircularImageView) findViewById(R.id.vI_arp_profilepic);
        vT_tb_headtext= (TextView) findViewById(R.id.vT_tb_headtext);
        vT_arp_name= (TextView) findViewById(R.id.vT_arp_name);
        vT_arp_mobile= (TextView) findViewById(R.id.vT_arp_mobile);
        vT_arp_emailid= (TextView) findViewById(R.id.vT_arp_emailid);
        vT_arp_request= (TextView) findViewById(R.id.vT_arp_request);
        vT_arp_approverequest= (TextView) findViewById(R.id.vT_arp_approverequest);
        vT_arp_rejectrequest= (TextView) findViewById(R.id.vT_arp_rejectrequest);

        setValues();
    }

    private void setValues() {
        vT_tb_headtext.setText("REQUESTS");
        callServiceprf=getSharedPreferences("Callstatus",0);
        dialog=new ProgressDialog(RequestApprovelActivity.this);
        detector=new ConnectionDetector(RequestApprovelActivity.this);

        vI_tl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent appr_intent=getIntent();
        mSendername=appr_intent.getStringExtra("senderName");
        mSenderEmail=appr_intent.getStringExtra("senderEmail");
        mSendermobilenumber=appr_intent.getStringExtra("senderMobile");
        mStatus=appr_intent.getStringExtra("reqStatus");
        mKey=appr_intent.getStringExtra("Key");
        mSenderProfilepic=appr_intent.getStringExtra("profilePic");

       /* byte[] imageAsBytes = Base64.decode(mSenderProfilepic.getBytes(), Base64.DEFAULT);
        vI_arp_profilepic.setImageBitmap(
                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
        );*/

        Picasso.with(RequestApprovelActivity.this)
                .load(mSenderProfilepic)
                .placeholder(R.drawable.profiles)        // optional
                .into(vI_arp_profilepic);


        vT_arp_name.setText(mSendername);
        vT_arp_mobile.setText(mSendermobilenumber);
        vT_arp_emailid.setText(mSenderEmail);
        vT_arp_request.setText(mStatus);

        if(mStatus.equalsIgnoreCase("Pending")){
            vT_arp_approverequest.setVisibility(View.VISIBLE);
        }else{
            vT_arp_rejectrequest.setVisibility(View.VISIBLE);
        }
        vT_arp_approverequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(detector.isConnectingToInternet()) {

                    dialog.setMessage("Proccessing...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    FirebaseDatabase.getInstance()
                            .getReference("requests")
                            .child(mKey)
                            .child("requestStatus")
                            .setValue("Accepted");
                    
                    if(dialog.isShowing())
                        dialog.dismiss();
                    finish();
                }else{
                    Toast.makeText(RequestApprovelActivity.this,"Please check internet",Toast.LENGTH_SHORT).show();
                }
               /* FirebaseDatabase.getInstance()
                        .getReference("requests")
                        .child(mKey)
                .removeValue();*/
            }
        });

        vT_arp_rejectrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detector.isConnectingToInternet()) {

                    dialog.setMessage("Proccessing...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    FirebaseDatabase.getInstance()
                            .getReference("requests")
                            .child(mKey)
                            .child("requestStatus")
                            .setValue("Terminated");

                    if(dialog.isShowing())
                        dialog.dismiss();
                    finish();
                }else {
                    Toast.makeText(RequestApprovelActivity.this,"Please check internet",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor=callServiceprf.edit();
        editor.putString("callnow","no");
        editor.apply();
    }
}

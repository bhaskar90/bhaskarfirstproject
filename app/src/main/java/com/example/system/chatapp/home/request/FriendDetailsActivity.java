package com.example.system.chatapp.home.request;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.chatapp.R;
import com.example.system.chatapp.beans.Message;
import com.example.system.chatapp.home.HomeActivity;
import com.example.system.chatapp.utils.CircularImageView;
import com.example.system.chatapp.utils.ConnectionDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FriendDetailsActivity extends Activity {


    ImageView vI_tl_back;
    TextView vT_tb_headtext,vT_afd_name,vT_afd_mobile,vT_afd_emailid,vT_afd_request,vT_afd_sendrequest;
    String mReceivername,mReceivermobilenumber,mReceiverEmailid,mRequeststatus,mRequestReceiverProfilepic;
    String mSendername,mSendermobilenumber,mSenderEmail,mSenderProfilepic;
    SharedPreferences login_pref;
    DatabaseReference reference;
    CircularImageView vI_afd_profilepic;
    ConnectionDetector detector;
    ProgressDialog dialog;
    LinearLayout activity_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);
        initializeViews();
    }

    private void initializeViews() {

        vT_tb_headtext= (TextView) findViewById(R.id.vT_tb_headtext);
        vI_tl_back= (ImageView) findViewById(R.id.vI_tl_back);
        vI_afd_profilepic= (CircularImageView) findViewById(R.id.vI_afd_profilepic);
        vT_afd_name= (TextView) findViewById(R.id.vT_afd_name);
        vT_afd_mobile= (TextView) findViewById(R.id.vT_afd_mobile);
        vT_afd_emailid= (TextView) findViewById(R.id.vT_afd_emailid);
        vT_afd_request= (TextView) findViewById(R.id.vT_afd_request);
        vT_afd_sendrequest= (TextView) findViewById(R.id.vT_afd_sendrequest);

        activity_profile= (LinearLayout) findViewById(R.id.activity_profile);

        login_pref=getSharedPreferences("LoginDetails",0);
        mSendername=login_pref.getString("loginname",null);
        mSendermobilenumber=login_pref.getString("mobilenumber",null);
        mSenderEmail=login_pref.getString("email",null);
        mSenderProfilepic=login_pref.getString("profilePic",null);

        Intent friends_details_intent=getIntent();
        mReceivername=friends_details_intent.getStringExtra("receiverName");
        mReceivermobilenumber=friends_details_intent.getStringExtra("receiverMobilenumber");
        mReceiverEmailid=friends_details_intent.getStringExtra("receiverEmail");
        mRequestReceiverProfilepic=friends_details_intent.getStringExtra("receiverProfilepic");

        mRequeststatus=getRequestStatus();

        setValues();
    }

    private void setValues() {

        vT_tb_headtext.setText("REQUESTS");
        vT_afd_name.setText(mReceivername);
        vT_afd_mobile.setText(mReceivermobilenumber);
        vT_afd_emailid.setText(mReceiverEmailid);
        detector=new ConnectionDetector(FriendDetailsActivity.this);
        dialog=new ProgressDialog(FriendDetailsActivity.this);

      /*  byte[] imageAsBytes = Base64.decode(mRequestReceiverProfilepic.getBytes(), Base64.DEFAULT);
        vI_afd_profilepic.setImageBitmap(
                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
        );*/

        Picasso.with(FriendDetailsActivity.this)
                .load(mRequestReceiverProfilepic)
                .placeholder(R.drawable.profiles)        // optional
                .into(vI_afd_profilepic);


        vI_tl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        vT_afd_sendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(detector.isConnectingToInternet()) {

                   /* dialog.setMessage("Proccessing...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();*/

                    FirebaseDatabase.getInstance()
                            .getReference("requests")
                            .push()
                            .setValue(new FriendsDetailsbean(mReceiverEmailid, "00", mReceivermobilenumber, mReceivername, mRequestReceiverProfilepic, mSenderEmail
                                    , "00", mSendermobilenumber, mSendername, "Request Sent", mSenderProfilepic)
                            );
                    Snackbar snackbar=Snackbar.make(activity_profile,"Request sent Successfully",Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.material));
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snackbar.setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            finish();
                        }

                        @Override
                        public void onShown(Snackbar snackbar) {
                            super.onShown(snackbar);
                        }
                    }).show();
                  /*  Snackbar.make(activity_profile,"Request sent Successfully",Snackbar.LENGTH_SHORT)
                            .setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    super.onDismissed(snackbar, event);
                                    *//*if(dialog.isShowing())
                                        dialog.dismiss();*//*

                                }

                                @Override
                                public void onShown(Snackbar snackbar) {
                                    super.onShown(snackbar);
                                }
                            })
                            .show();
*/
                }else{
                    Toast.makeText(FriendDetailsActivity.this,"Please check internet",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }




    public String getRequestStatus(){

        reference=FirebaseDatabase.getInstance().getReference("requests");
        Query query_ref=reference.orderByChild("requestStatus");
        query_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){
                    FriendsDetailsbean frds_obj=dsp.getValue(FriendsDetailsbean.class);
                    if(frds_obj.getRequestReceiverEmail().equalsIgnoreCase(mSenderEmail) && frds_obj.getRequestSenderEmail().equalsIgnoreCase(mReceiverEmailid)
                            || frds_obj.getRequestReceiverEmail().equalsIgnoreCase(mReceiverEmailid) && frds_obj.getRequestSenderEmail().equalsIgnoreCase(mSenderEmail)){
                        mRequeststatus=frds_obj.getRequestStatus();
                        vT_afd_request.setText(mRequeststatus);
                        vT_afd_sendrequest.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mRequeststatus="Send Request";
            }
        });



        return mRequeststatus;
    }

}

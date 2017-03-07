package com.example.system.chatapp.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.system.chatapp.R;
import com.example.system.chatapp.beans.Message;
import com.example.system.chatapp.beans.users;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendChatActivity extends Activity {

    ListView vL_afc_friendchatlist;
    TextView vT_tb_headtext;
    ImageView vI_tl_back,vI_afc_send;
    DatabaseReference reference;
    ArrayList<Message> messageList;
    String mFrdemailId,mFrdname,sending_data,mSenderEmailid;
    EditText vE_afc_sendtext;
    SharedPreferences login_pref;
    RelativeLayout vR_msg_receiver,vR_msg_sender;
    private FirebaseListAdapter<Message> adapter;
    //chatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);
        initializeViews();
    }

    private void initializeViews() {
        vL_afc_friendchatlist= (ListView) findViewById(R.id.vL_afc_friendchatlist);
        vT_tb_headtext= (TextView) findViewById(R.id.vT_tb_headtext);
        vI_tl_back= (ImageView) findViewById(R.id.vI_tl_back);
        vI_afc_send= (ImageView) findViewById(R.id.vI_afc_send);
        vE_afc_sendtext= (EditText) findViewById(R.id.vE_afc_sendtext);

        login_pref=getSharedPreferences("LoginDetails",0);
        mSenderEmailid=login_pref.getString("email",null);

        Intent chat_intent=getIntent();
        mFrdemailId=chat_intent.getStringExtra("friendemailid");
        mFrdname=chat_intent.getStringExtra("friendname");
        setValues();
    }

    private void setValues() {
        vT_tb_headtext.setText("CHAT");
        messageList=new ArrayList<>();


        vI_tl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        vI_afc_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendText();
            }
        });
        displayChatMessages();

    }


    public void sendText(){
        sending_data=vE_afc_sendtext.getText().toString().trim();
        FirebaseDatabase.getInstance()
                .getReference("friendschat")
                .push()
                .setValue(new Message(sending_data,mSenderEmailid,mFrdemailId)
                );

        // Clear the input
        vE_afc_sendtext.setText("");
        //getFriendchat();
    }

    private void displayChatMessages() {

        reference= FirebaseDatabase.getInstance().getReference("friendschat");

        adapter = new FirebaseListAdapter<Message>(FriendChatActivity.this,Message.class ,
                R.layout.message,reference) {
            @Override
            protected void populateView(View v, Message model, int position) {
                // Get references to the views of message.xml

                vR_msg_receiver= (RelativeLayout) v.findViewById(R.id.vR_msg_receiver);
                vR_msg_sender= (RelativeLayout) v.findViewById(R.id.vR_msg_sender);

                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);

                TextView messageText1 = (TextView)v.findViewById(R.id.message_text1);
                TextView messageUser1 = (TextView)v.findViewById(R.id.message_user1);

                // TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                // Set their text
                String time= DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getTimestamp()).toString();

                if(mSenderEmailid.equalsIgnoreCase(model.getSender()) &&
                    mFrdemailId.equalsIgnoreCase(model.getReceiver())){
                    vR_msg_sender.setVisibility(View.VISIBLE);
                    vR_msg_receiver.setVisibility(View.GONE);
                    messageUser1.setText(model.getSender()+" "+time);
                    messageText1.setText(model.getContent());
                }else if(mFrdemailId.equalsIgnoreCase(model.getSender()) &&
                    mSenderEmailid.equalsIgnoreCase(model.getReceiver())){
                    vR_msg_sender.setVisibility(View.GONE);
                    vR_msg_receiver.setVisibility(View.VISIBLE);
                    messageUser.setText(model.getSender()+" "+time);
                    messageText.setText(model.getContent());
                }

            }
        };
        vL_afc_friendchatlist.setAdapter(adapter);
    }
}

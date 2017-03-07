package com.example.system.chatapp.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.chatapp.R;
import com.example.system.chatapp.beans.Message;
import com.example.system.chatapp.other.MainActivity;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends Activity {

    ListView data_list;
    EditText send_content;
    ImageView send_img;
    String sending_data,mLoginname,mUsername;
    String[] username;
    TextView vT_tb_headtext;
    ImageView vI_tl_back;
    RelativeLayout vR_msg_receiver,vR_msg_sender;
    SharedPreferences log_info_pref;

    private FirebaseListAdapter<Message> adapter;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initializeViews();
    }

    private void initializeViews() {
        data_list= (ListView) findViewById(R.id.data_list);
        send_content= (EditText) findViewById(R.id.send_content);
        send_img= (ImageView) findViewById(R.id.send_img);
        vT_tb_headtext= (TextView) findViewById(R.id.vT_tb_headtext);
        vI_tl_back= (ImageView) findViewById(R.id.vI_tl_back);

        log_info_pref=getSharedPreferences("LoginDetails",0);
        setValues();
    }

    private void setValues() {

        vT_tb_headtext.setText("CHAT");

        vI_tl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mLoginname=log_info_pref.getString("email",null);
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent=new Intent(ChatActivity.this,MainActivity.class);
            startActivity(intent);
        } else {
            // Load chat room contents
            displayChatMessages();
        }

        send_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sending_data=send_content.getText().toString().trim();
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
               /* FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(sending_data, FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                        );*/
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null) {
                    String uname = user.getEmail();
                    String email = user.getDisplayName();
                    String nn = user.getUid();
                    username = uname.split("@");
                    mUsername=uname;
                    //    Toast.makeText(ChatActivity.this, username[0], Toast.LENGTH_LONG).show();
                }else {
                    username[0]="Anonymus";
                }
                FirebaseDatabase.getInstance()
                        .getReference("messages")
                        .push()
                        .setValue(new Message(sending_data,mUsername,"Anonymus")
                        );

                // Clear the input
                send_content.setText("");
            }
        });
    }

    /*private void displayChatMessages() {

        reference= FirebaseDatabase.getInstance().getReference();

        adapter = new FirebaseListAdapter<ChatMessage>(ChatActivity.this,ChatMessage.class ,
                R.layout.message,reference) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml

                vR_msg_receiver= (RelativeLayout) v.findViewById(R.id.vR_msg_receiver);
                vR_msg_sender= (RelativeLayout) v.findViewById(R.id.vR_msg_sender);

                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);

                TextView messageText1 = (TextView)v.findViewById(R.id.message_text1);
                TextView messageUser1 = (TextView)v.findViewById(R.id.message_user1);


               // TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                // Set their text
                String time=DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()).toString();

                if(mLoginname.equalsIgnoreCase(model.getMessageUser())){
                    vR_msg_sender.setVisibility(View.VISIBLE);
                    vR_msg_receiver.setVisibility(View.GONE);
                    messageUser1.setText(model.getMessageUser()+" "+time);
                    messageText1.setText(model.getMessageText());
                }else{
                    vR_msg_sender.setVisibility(View.GONE);
                    vR_msg_receiver.setVisibility(View.VISIBLE);
                    messageUser.setText(model.getMessageUser()+" "+time);
                    messageText.setText(model.getMessageText());
                }
                // Format the date before showing it
               // messageTime.setText();

            }
        };
        data_list.setAdapter(adapter);
    }*/


    private void displayChatMessages() {

        reference= FirebaseDatabase.getInstance().getReference("messages");

        adapter = new FirebaseListAdapter<Message>(ChatActivity.this,Message.class ,
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
                String time=DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getTimestamp()).toString();

                if(mLoginname.equalsIgnoreCase(model.getSender())){
                    vR_msg_sender.setVisibility(View.VISIBLE);
                    vR_msg_receiver.setVisibility(View.GONE);
                    messageUser1.setText(model.getSender()+" "+time);
                    messageText1.setText(model.getContent());
                }else{
                    vR_msg_sender.setVisibility(View.GONE);
                    vR_msg_receiver.setVisibility(View.VISIBLE);
                    messageUser.setText(model.getSender()+" "+time);
                    messageText.setText(model.getContent());
                }

                if(model.getSender().equalsIgnoreCase("bhaskar")){
                    vR_msg_sender.setVisibility(View.VISIBLE);
                    vR_msg_receiver.setVisibility(View.GONE);
                    messageUser1.setText(model.getSender()+" "+time);
                    messageText1.setText(model.getContent());
                }




                // Format the date before showing it
                // messageTime.setText();



            }
        };
        data_list.setAdapter(adapter);
    }




}

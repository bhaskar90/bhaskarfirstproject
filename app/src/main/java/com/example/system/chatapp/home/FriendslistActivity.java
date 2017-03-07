package com.example.system.chatapp.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.system.chatapp.R;
import com.example.system.chatapp.beans.users;
import com.example.system.chatapp.home.request.FriendDetailsActivity;
import com.example.system.chatapp.home.request.FriendsDetailsbean;
import com.example.system.chatapp.home.request.RequestApprovelActivity;
import com.example.system.chatapp.home.request.RequestReceivedActivity;
//import com.firebase.ui.database.FirebaseListAdapter;
import com.example.system.chatapp.utils.CircularImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendslistActivity extends Activity {

    TextView vT_tb_headtext,vT_ul_mobile,vT_ul_name;
    ImageView vI_tl_back;
    CircularImageView vI_ul_userprofile;
    ListView vL_au_userlist;
    LinearLayout vL_ul_userlayout;
    SharedPreferences login_pref;
    String email;
    String email_id, name;
    ArrayList<FriendsDetailsbean> friendList;
    friendsAdapter adapter;
    ProgressDialog dialog;
    //ArrayList<users> userList;

   // private FirebaseListAdapter<users> usersadapter;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendslist);
        initializeViews();
    }

    private void initializeViews() {
        vT_tb_headtext= (TextView) findViewById(R.id.vT_tb_headtext);
        vI_tl_back= (ImageView) findViewById(R.id.vI_tl_back);
        vL_au_userlist= (ListView) findViewById(R.id.vL_au_userlist);
        friendList = new ArrayList<>();
      //  userList=new ArrayList<>();
        login_pref=getSharedPreferences("LoginDetails",0);
        email=login_pref.getString("email",null);
        setValues();
    }

    private void setValues() {
        vT_tb_headtext.setText("Friends");
        vI_tl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter=new friendsAdapter(FriendslistActivity.this);
        dialog=new ProgressDialog(FriendslistActivity.this);
        vL_au_userlist.setAdapter(adapter);

        displayUserslist();
    }



    private void displayUserslist() {


        dialog.setMessage("Proccessing...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if(friendList!=null){
            if(friendList.size()!=0){
                friendList.clear();
            }
        }

       /* reference= FirebaseDatabase.getInstance().getReference("requests");
        Query quesry_ref=reference.orderByChild("requestReceiverEmail").equalTo(email);

        quesry_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String key=dsp.getKey();
                    FriendsDetailsbean user_profile = dsp.getValue(FriendsDetailsbean.class);
                    if(user_profile.getRequestStatus().equalsIgnoreCase("Accepted")) {
                        user_profile.setmREquest_key(key);
                        friendList.add(user_profile);
                        Log.d("bhaskar", user_profile.getRequestStatus() + " " + user_profile.getRequestSenderEmail());
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/


        reference= FirebaseDatabase.getInstance().getReference("requests");
        Query quesry_ref=reference.orderByChild("requestReceiverEmail");

        quesry_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String key=dsp.getKey();
                    FriendsDetailsbean user_profile = dsp.getValue(FriendsDetailsbean.class);
                    if((user_profile.getRequestReceiverEmail().equalsIgnoreCase(email)&&user_profile.getRequestStatus().equalsIgnoreCase("Accepted"))
                           || user_profile.getRequestSenderEmail().equalsIgnoreCase(email) && user_profile.getRequestStatus().equalsIgnoreCase("Accepted") ) {
                        user_profile.setmREquest_key(key);
                        friendList.add(user_profile);
                        Log.d("bhaskar", user_profile.getRequestStatus() + " " + user_profile.getRequestSenderEmail());
                    }
                }
                adapter.notifyDataSetChanged();
                if(dialog.isShowing())
                    dialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });







    }

    public  class friendsAdapter extends BaseAdapter {

        LayoutInflater inflater;
        public friendsAdapter(Context context){
            inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return friendList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            if(view==null){
                view=inflater.inflate(R.layout.user_layout,viewGroup,false);
            }
            vT_ul_mobile = (TextView) view.findViewById(R.id.vT_ul_mobile);
            vT_ul_name = (TextView) view.findViewById(R.id.vT_ul_name);
            vI_ul_userprofile= (CircularImageView) view.findViewById(R.id.vI_ul_userprofile);
            vL_ul_userlayout = (LinearLayout) view.findViewById(R.id.vL_ul_userlayout);


            if(friendList.get(position).getRequestReceiverEmail().equalsIgnoreCase(email) &&
                    friendList.get(position).getRequestStatus().equalsIgnoreCase("Accepted")){

               /*   byte[] imageAsBytes = Base64.decode(friendList.get(position).getRequestSenderProfilepic().getBytes(),
                          Base64.DEFAULT);
                vI_ul_userprofile.setImageBitmap(
                        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                );*/

                Picasso.with(FriendslistActivity.this)
                        .load(friendList.get(position).getRequestSenderProfilepic())
                        .placeholder(R.drawable.profiles)        // optional
                        .into(vI_ul_userprofile);




                vT_ul_mobile.setText(friendList.get(position).getRequestSendermobilenumber());
                vT_ul_name.setText(friendList.get(position).getRequestSendername());


            }else if(friendList.get(position).getRequestSenderEmail().equalsIgnoreCase(email) &&
                    friendList.get(position).getRequestStatus().equalsIgnoreCase("Accepted")){
                vT_ul_mobile.setText(friendList.get(position).getRequestReceivermobilenumber());
                vT_ul_name.setText(friendList.get(position).getRequestReceivername());

               /* byte[] imageAsBytes = Base64.decode(friendList.get(position).getRequestSenderProfilepic().getBytes(),
                        Base64.DEFAULT);
                vI_ul_userprofile.setImageBitmap(
                        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                );*/

                Picasso.with(FriendslistActivity.this)
                        .load(friendList.get(position).getRequestSenderProfilepic())
                        .placeholder(R.drawable.profiles)        // optional
                        .into(vI_ul_userprofile);

            }


            vL_ul_userlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(friendList.get(position).getRequestReceiverEmail().equalsIgnoreCase(email) &&
                            friendList.get(position).getRequestStatus().equalsIgnoreCase("Accepted")){
                        email_id=friendList.get(position).getRequestSenderEmail();
                        name=friendList.get(position).getRequestSendername();

                    }else if(friendList.get(position).getRequestSenderEmail().equalsIgnoreCase(email) &&
                            friendList.get(position).getRequestStatus().equalsIgnoreCase("Accepted")){
                        email_id=friendList.get(position).getRequestReceiverEmail();
                        name=friendList.get(position).getRequestReceivername();
                    }

                    Intent friendchat_intent=new Intent(FriendslistActivity.this,FriendChatActivity.class);
                    friendchat_intent.putExtra("friendemailid",email_id);
                    friendchat_intent.putExtra("friendname",name);
                    startActivity(friendchat_intent);
                }
            });

            return view;
        }
    }

}

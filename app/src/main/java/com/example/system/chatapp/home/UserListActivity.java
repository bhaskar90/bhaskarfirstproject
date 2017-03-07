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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.chatapp.R;
import com.example.system.chatapp.beans.users;
import com.example.system.chatapp.home.request.FriendDetailsActivity;
import com.example.system.chatapp.login.LoginActivity;
import com.example.system.chatapp.utils.CircularImageView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListActivity extends Activity {

    TextView vT_tb_headtext,vT_ul_mobile,vT_ul_name;
    ImageView vI_tl_back,vI_aul_serachuser,vI_aul_cancelserach;
    CircularImageView vI_ul_userprofile;
    ListView vL_au_userlist;
    LinearLayout vL_ul_userlayout;
    SharedPreferences login_pref;
    String email;
    ArrayList<users> userList;
    ProgressDialog dialog;
    String text;
    // need to change the values
    EditText entertext;

    private FirebaseListAdapter<users> usersadapter;
    DatabaseReference reference;
    usersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        initializeViews();
    }

    private void initializeViews() {
        vT_tb_headtext= (TextView) findViewById(R.id.vT_tb_headtext);
        vI_tl_back= (ImageView) findViewById(R.id.vI_tl_back);
        vL_au_userlist= (ListView) findViewById(R.id.vL_aul_userlist);

        // need to change the values
        entertext= (EditText) findViewById(R.id.entertext);
        vI_aul_serachuser= (ImageView) findViewById(R.id.vI_aul_serachuser);
        vI_aul_cancelserach= (ImageView) findViewById(R.id.vI_aul_cancelserach);


        login_pref=getSharedPreferences("LoginDetails",0);
        email=login_pref.getString("email",null);
        setValues();
    }

    private void setValues() {
        vT_tb_headtext.setText("Users");
        vI_tl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        userList=new ArrayList<>();
        dialog=new ProgressDialog(UserListActivity.this);
        adapter=new usersAdapter(UserListActivity.this);
        vL_au_userlist.setAdapter(adapter);

        vI_aul_serachuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text=entertext.getText().toString().trim();
                if(!text.equalsIgnoreCase("")){
                    displayUsersbysearch();
                }else{
                    Toast.makeText(UserListActivity.this,"Please enter email id to Search",Toast.LENGTH_SHORT).show();
                }

            }
        });

        vI_aul_cancelserach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entertext.setText("");
                displayUserslist();
            }
        });


        displayUserslist();
    }

    private void displayUserslist() {

        dialog.setMessage("Proccessing...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if(userList!=null){
            if(userList.size()!=0){
                userList.clear();
            }
        }


        reference= FirebaseDatabase.getInstance().getReference("users");
        Query quesry_ref=reference.orderByChild("name");

        quesry_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String key=dsp.getKey();
                    users users_object=dsp.getValue(users.class);
                    if(!users_object.getEmailid().equalsIgnoreCase(email)) {
                        users_object.setKey(key);
                        userList.add(users_object);
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


    private void displayUsersbysearch() {

        dialog.setMessage("Proccessing...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if(userList!=null){
            if(userList.size()!=0){
                userList.clear();
            }
        }
        text=entertext.getText().toString().trim();

        reference= FirebaseDatabase.getInstance().getReference("users");
        Query quesry_ref=reference.orderByChild("name");

        quesry_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String key=dsp.getKey();
                    users users_object=dsp.getValue(users.class);
                    if(users_object.getEmailid().equalsIgnoreCase(text)) {
                        users_object.setKey(key);
                        userList.add(users_object);
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


   /* private void displayUsersbysearch() {

        if(userList!=null){
            if(userList.size()!=0){
                userList.clear();
            }
        }

        String text=entertext.getText().toString().trim();

        reference= FirebaseDatabase.getInstance().getReference("users");
        usersadapter = new FirebaseListAdapter<users>(UserListActivity.this,users.class ,
                R.layout.user_layout,reference.orderByChild("emailid").equalTo(text)) {
            @Override
            protected void populateView(View v, users model, final int position) {
                vT_ul_mobile= (TextView) v.findViewById(R.id.vT_ul_mobile);
                vT_ul_name= (TextView) v.findViewById(R.id.vT_ul_name);
                vL_ul_userlayout= (LinearLayout) v.findViewById(R.id.vL_ul_userlayout);
                vI_ul_userprofile= (CircularImageView) v.findViewById(R.id.vI_ul_userprofile);


               *//* byte[] imageAsBytes = Base64.decode(model.getProfileImage().getBytes(), Base64.DEFAULT);
                vI_ul_userprofile.setImageBitmap(
                        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                );*//*

                Picasso.with(UserListActivity.this)
                        .load(model.getProfileImage())
                        .placeholder(R.drawable.profiles)        // optional
                        .into(vI_ul_userprofile);

                vT_ul_name.setText(model.getname());
                vT_ul_mobile.setText(model.getmobilenumber());
                userList.add(model);

                vL_ul_userlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent userchat_intent=new Intent(UserListActivity.this,FriendDetailsActivity.class);
                        userchat_intent.putExtra("receiverName",userList.get(position).getname());
                        userchat_intent.putExtra("receiverAge",userList.get(position).getEmailid());
                        userchat_intent.putExtra("receiverMobilenumber",userList.get(position).getmobilenumber());
                        userchat_intent.putExtra("receiverEmail",userList.get(position).getEmailid());
                        userchat_intent.putExtra("receiverProfilepic",userList.get(position).getProfileImage());
                        userchat_intent.putExtra("requestStatus","false");
                        startActivity(userchat_intent);
                    }
                });

                if(model.getEmailid().equalsIgnoreCase(email)){
                    Log.d("bhaskar",model.getage()+" "+model.getname()+" "+model.getmobilenumber());
                }
            }
        };
       // adapter.notifyDataSetChanged();
        vL_au_userlist.setAdapter(usersadapter);
    }*/




    public  class usersAdapter extends BaseAdapter {

        LayoutInflater inflater;
        public usersAdapter(Context context){
            inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return userList.size();
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
            vL_ul_userlayout = (LinearLayout) view.findViewById(R.id.vL_ul_userlayout);
            vI_ul_userprofile= (CircularImageView) view.findViewById(R.id.vI_ul_userprofile);


           /* byte[] imageAsBytes = Base64.decode(userList.get(position).getProfileImage().getBytes(), Base64.DEFAULT);
            vI_ul_userprofile.setImageBitmap(
                    BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
            );*/

            Picasso.with(UserListActivity.this)
                    .load(userList.get(position).getProfileImage())
                    .placeholder(R.drawable.profiles)        // optional
                    .into(vI_ul_userprofile);


            vT_ul_name.setText(userList.get(position).getname());
            vT_ul_mobile.setText(userList.get(position).getmobilenumber());


            vL_ul_userlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent userchat_intent=new Intent(UserListActivity.this,FriendDetailsActivity.class);
                    userchat_intent.putExtra("receiverName",userList.get(position).getname());
                    userchat_intent.putExtra("receiverAge",userList.get(position).getEmailid());
                    userchat_intent.putExtra("receiverMobilenumber",userList.get(position).getmobilenumber());
                    userchat_intent.putExtra("receiverEmail",userList.get(position).getEmailid());
                    userchat_intent.putExtra("receiverProfilepic",userList.get(position).getProfileImage());
                    userchat_intent.putExtra("requestStatus","false");
                    startActivity(userchat_intent);
                }
            });

            return view;
        }
    }


}

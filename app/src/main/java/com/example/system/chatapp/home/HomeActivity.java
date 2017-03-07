package com.example.system.chatapp.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.chatapp.R;
import com.example.system.chatapp.home.request.RequestReceivedActivity;
import com.example.system.chatapp.login.ProfileActivity;
import com.example.system.chatapp.login.SignupActivity;
import com.example.system.chatapp.other.ImageUploadingActivity;
import com.example.system.chatapp.utils.CircularImageView;
import com.squareup.picasso.Picasso;

public class HomeActivity extends Activity {

    TextView vT_tb_headtext,vT_nhl_username,vT_nhl_useremail,vT_nhl_mobilenumber;
    ImageView vI_tl_back,vI_tl_overflow;
    CardView vC_nhl_forumfriends,vC_nhl_forumchat,vC_nhl_friends,vC_nhl_friendsrequest;
    CircularImageView vC_nhl_profilepic;
    String mUsername, mMobilenumber, mEmailId, mKey, mProfilepic;
    SharedPreferences loginfo_pref;
    AlertDialog dialog;
    PopupWindow logoutPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newhome_layout);
        initializeViews();
    }

    private void initializeViews() {
        vT_tb_headtext= (TextView) findViewById(R.id.vT_tb_headtext);
        vI_tl_back= (ImageView) findViewById(R.id.vI_tl_back);
        vI_tl_overflow= (ImageView) findViewById(R.id.vI_tl_overflow);

        vC_nhl_forumfriends= (CardView) findViewById(R.id.vC_nhl_forumfriends);
        vC_nhl_forumchat= (CardView) findViewById(R.id.vC_nhl_forumchat);
        vC_nhl_friends= (CardView) findViewById(R.id.vC_nhl_friends);
        vC_nhl_friendsrequest= (CardView) findViewById(R.id.vC_nhl_friendsrequest);
        vC_nhl_profilepic= (CircularImageView) findViewById(R.id.vC_nhl_profilepic);
        vT_nhl_username= (TextView) findViewById(R.id.vT_nhl_username);
        vT_nhl_useremail= (TextView) findViewById(R.id.vT_nhl_useremail);
        vT_nhl_mobilenumber= (TextView) findViewById(R.id.vT_nhl_mobilenumber);



        setValues();
    }

    private void setValues() {
        vT_tb_headtext.setText("HOME");
        vI_tl_back.setImageResource(R.drawable.drawer_icon);
        vI_tl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

      /*  byte[] imageAsBytes = Base64.decode(mProfilepic.getBytes(), Base64.DEFAULT);
        vC_nhl_profilepic.setImageBitmap(
                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
        );*/
        vI_tl_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupmenu();
            }
        });

        vC_nhl_forumfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UserListActivity.class);
                startActivity(intent);
            }
        });

        vC_nhl_forumchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        vC_nhl_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FriendslistActivity.class);
                startActivity(intent);
            }
        });

        vC_nhl_friendsrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RequestReceivedActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        loginfo_pref = getSharedPreferences("LoginDetails", 0);
        mUsername = loginfo_pref.getString("uname", null);
        mMobilenumber = loginfo_pref.getString("mobilenumber", null);
        mEmailId = loginfo_pref.getString("email", null);
        mKey = loginfo_pref.getString("loginKey", null);
        mProfilepic = loginfo_pref.getString("profilePic", null);

        Picasso.with(HomeActivity.this)
                .load(mProfilepic)
                .placeholder(R.drawable.profiles)        // optional
                .into(vC_nhl_profilepic);

        vT_nhl_username.setText(mUsername);
        vT_nhl_mobilenumber.setText(mMobilenumber);
        vT_nhl_useremail.setText(mEmailId);
    }

    public void popupmenu(){

        final android.widget.PopupMenu popup = new android.widget.PopupMenu(HomeActivity.this, vI_tl_overflow);
        popup.getMenuInflater().inflate(R.menu.popmenu, popup.getMenu());

        Menu menu = popup.getMenu();
        for (int index = 0; index < menu.size(); index++) {
            MenuItem menuItem = menu.getItem(index);
           // applyFontToMenuItem(menuItem);
        }

        popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.logout:
                        callAlertDialog();
                        break;
                    case R.id.user_guide:
                        Toast.makeText(HomeActivity.this,"User guide will come soon here",Toast.LENGTH_SHORT).show();
                       /* Intent intent=new Intent(HomeActivity.this, ImageUploadingActivity.class);
                        startActivity(intent);*/
                        break;
                    case R.id.faq:
                        Toast.makeText(HomeActivity.this,"FAQ will come soon here",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        Intent proIntent=new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(proIntent);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }



    public void callAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        dialog = builder.create();
        builder.setTitle("Logout");
        builder.setMessage("Are you sure want to Logout ?");
        builder.setView(R.layout.alert_layout);

       /* String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor=loginfo_pref.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(HomeActivity.this, SignupActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.dismiss();
                    }
                });*/
        LinearLayout viewGroup= (LinearLayout) findViewById(R.id.layout);
        LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);
        View view = inflater.inflate(R.layout.alert_layout,viewGroup);
        TextView vT_al_cancel= (TextView) view.findViewById(R.id.vT_al_cancel);
        TextView vT_al_ok= (TextView) view.findViewById(R.id.vT_al_ok);

        vT_al_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        vT_al_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=loginfo_pref.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(HomeActivity.this, SignupActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }


}

package com.example.system.chatapp.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.chatapp.beans.users;
import com.example.system.chatapp.home.HomeActivity;
import com.example.system.chatapp.R;
import com.example.system.chatapp.utils.ConnectionDetector;
//import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends Activity {

    TextView vT_tb_headtext,vT_al_email,vT_al_pswd,vT_al_login,vT_ul_mobile,vT_ul_name;
    String mEmail_id,mPswd,result_input,uname,email,userLogind="";
    ImageView vI_tl_back;
    ConnectionDetector detector;
    ProgressDialog dialog;
    String[] username;
    SharedPreferences log_pref;
    DatabaseReference reference;
    ListView vL_au_userlist;
    //private FirebaseListAdapter<users> usersadapter;
    ArrayList<users> userList;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
    }

    private void initializeViews() {
       // vT_tb_headtext= (TextView) findViewById(R.id.vT_tb_headtext);
        vT_al_email= (TextView) findViewById(R.id.vT_al_email);
        vT_al_pswd= (TextView) findViewById(R.id.vT_al_pswd);
        vT_al_login= (TextView) findViewById(R.id.vT_al_login);
       // vI_tl_back= (ImageView) findViewById(R.id.vI_tl_back);
        log_pref=getSharedPreferences("LoginDetails",MODE_PRIVATE);
        userList=new ArrayList<>();

        setValues();
    }

    private void setValues() {
       // vT_tb_headtext.setText("LOGIN");
        detector=new ConnectionDetector(LoginActivity.this);
        dialog=new ProgressDialog(LoginActivity.this);

        Intent intent=getIntent();
        userLogind=intent.getStringExtra("LoginId");
        if(userLogind!=null){
            vT_al_email.setText(userLogind);
        }

       /* vI_tl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/


        vT_al_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(detector.isConnectingToInternet()) {
                    String validation_result = validateInputs();
                    if (validation_result.equalsIgnoreCase("")) {
                        dialog.setMessage("Proccessing...");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        mEmail_id = vT_al_email.getText().toString().trim();
                        mPswd = vT_al_pswd.getText().toString().trim();

                        auth = FirebaseAuth.getInstance();
                        auth.signInWithEmailAndPassword(mEmail_id, mPswd)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            // User signed in successfully
                                            FirebaseUser user = auth.getCurrentUser();
                                            if(user!=null){
                                                uname=user.getDisplayName();
                                                email=user.getEmail();
                                                String nn=user.getUid();
                                                username=email.split("@");
                                               // Toast.makeText(LoginActivity.this, username[0], Toast.LENGTH_LONG).show();
                                            }else {
                                                username[0]="Anonymus";
                                            }

                                            getUserprofileDetails(email);

                                                      //  Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_LONG).show();

                                        } else {
                                            if(dialog.isShowing())
                                                dialog.dismiss();
                                            Toast.makeText(LoginActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
               /* if (auth.getCurrentUser() != null) {
                    // Already signed in
                    // Do nothing
                } else {

                }*/


                    }else{
                        Toast.makeText(LoginActivity.this, validation_result, Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Please check internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getUserprofileDetails(String email_id) {

        reference= FirebaseDatabase.getInstance().getReference("users");
        Query quesry_ref=reference.orderByChild("emailid").equalTo(email_id);

        quesry_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                    users user_profile = dsp.getValue(users.class);
                    String lKey=dsp.getKey();
                    SharedPreferences.Editor editor=log_pref.edit();
                    editor.putString("loginname",username[0]);
                    editor.putString("email",email);
                    editor.putString("mobilenumber",user_profile.getmobilenumber());
                    editor.putString("uname",user_profile.getname());
                    editor.putString("loginKey",lKey);
                    editor.putString("profilePic",user_profile.getProfileImage());
                    editor.apply();
                    userList.add(user_profile);
                    if(dialog.isShowing())
                        dialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Log.d("bhaskar", user_profile.getmobilenumber() + " " + user_profile.getname());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public String validateInputs(){

        if(vT_al_email.getText().toString().trim().equalsIgnoreCase("")){
            result_input="Please enter email id";
        }else if(vT_al_pswd.getText().toString().trim().equalsIgnoreCase("")){
            result_input="Please enter password";
        } else{
            result_input="";
        }

        return result_input;
    }







}

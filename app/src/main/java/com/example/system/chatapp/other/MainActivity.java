package com.example.system.chatapp.other;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.system.chatapp.home.ChatActivity;
import com.example.system.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends Activity {


    EditText email_id,password,email_sign,pswd_sign;
    Button create_account,Sign_in;
    String email,pswd,email_sn,pswd_sn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email_id= (EditText) findViewById(R.id.email_id);
        password= (EditText) findViewById(R.id.password);
        email_sign= (EditText) findViewById(R.id.email_sign);
        pswd_sign= (EditText) findViewById(R.id.pswd_sign);
        Sign_in= (Button) findViewById(R.id.Sign_in);
        create_account= (Button) findViewById(R.id.create_account);


        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=email_id.getText().toString().trim();
                pswd=password.getText().toString().trim();
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email,pswd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    // User registered successfully
                                    Toast.makeText(MainActivity.this,""+task.getResult().getUser(),Toast.LENGTH_LONG).show();
                                   // Toast.makeText(MainActivity.this,"Created Successfully",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(MainActivity.this,""+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_sn=email_sign.getText().toString().trim();
                pswd_sn=pswd_sign.getText().toString().trim();

                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.signInWithEmailAndPassword(email_sn, pswd_sn)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User signed in successfully
                                    Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this,"Successfully logged in",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(MainActivity.this,"Please check username and password",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
               /* if (auth.getCurrentUser() != null) {
                    // Already signed in
                    // Do nothing
                } else {

                }*/
            }
        });





    }






}

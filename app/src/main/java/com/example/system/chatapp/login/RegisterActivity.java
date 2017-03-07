package com.example.system.chatapp.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.chatapp.R;
import com.example.system.chatapp.beans.users;
import com.example.system.chatapp.utils.CameraHandler;
import com.example.system.chatapp.utils.CircularImageView;
import com.example.system.chatapp.utils.ConnectionDetector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends Activity implements CameraHandler.OnImageResultListner{

    TextView vT_ar_signup;
    EditText vE_ar_emailid,vE_ar_pswd,vE_ar_cmpswd,vE_ar_username,vE_ar_mobilenumber;
    String mEmail_id,mPswd,result_input;
    CircularImageView vI_ar_profilepic;
    ConnectionDetector detector;
    ProgressDialog dialog;
    String mUsername,mMobilenumber;
    StorageReference storageRef;
    FirebaseStorage storage;
    CameraHandler handler;
    String url,picName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeViews();
    }

    private void initializeViews() {
        vE_ar_emailid= (EditText) findViewById(R.id.vE_ar_emailid);
        vE_ar_pswd= (EditText) findViewById(R.id.vE_ar_pswd);
        vE_ar_cmpswd= (EditText) findViewById(R.id.vE_ar_cmpswd);
        vT_ar_signup= (TextView) findViewById(R.id.vT_ar_signup);
        vI_ar_profilepic= (CircularImageView) findViewById(R.id.vI_ar_profilepic);
        vE_ar_username= (EditText) findViewById(R.id.vE_ar_username);
        vE_ar_mobilenumber= (EditText) findViewById(R.id.vE_ar_mobilenumber);

        storage=FirebaseStorage.getInstance();
        setValues();

    }
    private void setValues() {
        detector=new ConnectionDetector(RegisterActivity.this);
        dialog=new ProgressDialog(RegisterActivity.this);
        handler=new CameraHandler(this);

        storageRef = FirebaseStorage.getInstance().getReference().child("Chatapp/file.jpg");



        vI_ar_profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // selectImage();
               /// uploadImage();
                handler.showDialogToCaptureImage();

            }
        });


        vT_ar_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(detector.isConnectingToInternet()) {
                    String validation_result=validateInputs();
                    if (validation_result.equalsIgnoreCase("")) {
                        dialog.setMessage("Proccessing...");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        mEmail_id = vE_ar_emailid.getText().toString().trim();
                        mPswd = vE_ar_pswd.getText().toString().trim();
                        FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(mEmail_id, mPswd)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // User registered successfully
                                            if(dialog.isShowing())
                                                dialog.dismiss();
                                            postUserDetails();
                                           // Toast.makeText(RegisterActivity.this, "" + task.getResult().getUser().toString(), Toast.LENGTH_LONG).show();

                                            Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                                            loginIntent.putExtra("LoginId",mEmail_id);
                                            startActivity(loginIntent);


                                            // Toast.makeText(MainActivity.this,"Created Successfully",Toast.LENGTH_LONG).show();
                                        } else {
                                            if(dialog.isShowing())
                                                dialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }else {
                        Toast.makeText(RegisterActivity.this, validation_result, Toast.LENGTH_LONG).show();
                    }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Please check internet", Toast.LENGTH_LONG).show();
                    }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handler.onActivityResult(requestCode, resultCode, data);

    }


    public void  postUserDetails(){

      /*  BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
        vI_ar_profilepic.setDrawingCacheEnabled(true);
        vI_ar_profilepic.buildDrawingCache();
        Bitmap bitmap = vI_ar_profilepic.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);*/

        mUsername=vE_ar_username.getText().toString().trim();
        mMobilenumber=vE_ar_mobilenumber.getText().toString().trim();
        FirebaseDatabase.getInstance()
                .getReference("users")
                .push()
                .setValue(new users("26",mMobilenumber,mUsername,mEmail_id,url)
                );



    }



    public String validateInputs(){
        if(vE_ar_username.getText().toString().trim().equalsIgnoreCase("")){
            result_input="Please enter user name";
        } else if(vE_ar_mobilenumber.getText().toString().trim().equalsIgnoreCase("")){
            result_input="Please enter mobile number";
        } else if(vE_ar_emailid.getText().toString().trim().equalsIgnoreCase("")){
            result_input="Please enter email id";
        }else if(vE_ar_pswd.getText().toString().trim().equalsIgnoreCase("")){
            result_input="Please enter password";
        }else if(vE_ar_cmpswd.getText().toString().trim().equalsIgnoreCase("")){
            result_input="Please enter confirm password";
        }else if(!vE_ar_cmpswd.getText().toString().trim().equalsIgnoreCase(vE_ar_pswd.getText().toString().trim())){
            result_input="Passwprd and confirm password sholud be same";
        }else {
            result_input="";
        }
        return result_input;
    }


    @Override
    public void onImageResult(String imageName, String imagePath, Bitmap imageFile, int sourceType) {


        if(sourceType==CameraHandler.CAMERA_TYPE){
            /*Log.e("imageName",imageName);
            Log.e("imagePath",imagePath);
            Log.e("type","CameraType");*/
            picName=imageName;
            vI_ar_profilepic.setImageBitmap(imageFile);
            uploadProfilepic();
        }else if(sourceType==CameraHandler.GALLERY_TYPE){
           /* Log.e("imageName",imageName);
            Log.e("imagePath",imagePath);
            Log.e("type","GalleryType");*/
            picName=imageName;
            vI_ar_profilepic.setImageBitmap(imageFile);
            uploadProfilepic();
        }


    }



    public void uploadProfilepic(){

        StorageReference storageRef = storage.getReferenceFromUrl("gs://chatapp-24ab9.appspot.com");
        String chileName="images/"+picName;
        StorageReference spaceRef = storageRef.child(chileName);
        vI_ar_profilepic.setDrawingCacheEnabled(true);
        vI_ar_profilepic.buildDrawingCache();
        Bitmap bitmap = vI_ar_profilepic.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = spaceRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                try{
                    url=downloadUrl.toString();
                }catch (NullPointerException ex){
                    ex.printStackTrace();
                }
                // Log.d("bhaskar",downloadUrl.getLastPathSegment());
            }
        });


    }




}

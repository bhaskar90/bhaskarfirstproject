package com.example.system.chatapp.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.system.chatapp.R;
import com.example.system.chatapp.beans.ImageUpload;
import com.example.system.chatapp.beans.users;
import com.example.system.chatapp.home.HomeActivity;
import com.example.system.chatapp.utils.CameraHandler;
import com.example.system.chatapp.utils.CircularImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends Activity implements CameraHandler.OnImageResultListner {

    TextView vT_tb_headtext, vT_ap_update;
    ImageView vI_tl_back, vI_ap_edit;
    CircularImageView vI_ap_profile;
    EditText vE_ap_name, vE_ap_mobile, vE_ap_emailid;
    String mUsername, mMobilenumber, mEmailId, mKey, mProfilepic,picName;
    SharedPreferences loginfo_pref;
    DatabaseReference reference;
    CameraHandler handler;
    boolean isProfilepicChanged;
    FirebaseStorage storage;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeViews();
    }

    private void initializeViews() {
        vT_tb_headtext = (TextView) findViewById(R.id.vT_tb_headtext);
        vT_ap_update = (TextView) findViewById(R.id.vT_ap_update);
        vI_tl_back = (ImageView) findViewById(R.id.vI_tl_back);
        vI_ap_profile = (CircularImageView) findViewById(R.id.vI_ap_profile);
        vI_ap_edit = (ImageView) findViewById(R.id.vI_ap_edit);
        vE_ap_name = (EditText) findViewById(R.id.vE_ap_name);
        vE_ap_mobile = (EditText) findViewById(R.id.vE_ap_mobile);
        vE_ap_emailid = (EditText) findViewById(R.id.vE_ap_emailid);
        loginfo_pref = getSharedPreferences("LoginDetails", 0);
        setValues();
    }

    private void setValues() {
        vT_tb_headtext.setText("Profile");
        storage=FirebaseStorage.getInstance();
        mUsername = loginfo_pref.getString("uname", null);
        mMobilenumber = loginfo_pref.getString("mobilenumber", null);
        mEmailId = loginfo_pref.getString("email", null);
        mKey = loginfo_pref.getString("loginKey", null);
        mProfilepic = loginfo_pref.getString("profilePic", null);
        handler = new CameraHandler(this);
        dialog=new ProgressDialog(ProfileActivity.this);
        vI_ap_profile.setEnabled(false);
        vE_ap_name.setText(mUsername);
        vE_ap_mobile.setText(mMobilenumber);
        vE_ap_emailid.setText(mEmailId);

       /* byte[] imageAsBytes = Base64.decode(mProfilepic.getBytes(), Base64.DEFAULT);
        vI_ap_profile.setImageBitmap(
                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
        );*/

        Picasso.with(ProfileActivity.this)
                .load(mProfilepic)
                .placeholder(R.drawable.profiles)        // optional
                .into(vI_ap_profile);



        vI_tl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        vT_ap_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateProfile();
            }
        });


        vI_ap_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.showDialogToCaptureImage();
            }
        });


        vI_ap_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vT_ap_update.setVisibility(View.VISIBLE);
                vE_ap_name.setFocusableInTouchMode(true);
                vE_ap_mobile.setFocusableInTouchMode(true);
                vI_ap_profile.setEnabled(true);
            }
        });
    }

    private void getUserprofileDetails(String email_id) {

        reference = FirebaseDatabase.getInstance().getReference("users");
        Query quesry_ref = reference.orderByChild("emailid").equalTo(email_id);

        quesry_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    users user_profile = dsp.getValue(users.class);
                    String lKey = dsp.getKey();
                    SharedPreferences.Editor editor = loginfo_pref.edit();
                    editor.putString("loginname", user_profile.getname());
                    editor.putString("email", user_profile.getEmailid());
                    editor.putString("mobilenumber", user_profile.getmobilenumber());
                    editor.putString("uname", user_profile.getname());
                    editor.putString("loginKey", lKey);
                    editor.putString("profilePic", user_profile.getProfileImage());
                    editor.apply();
                    //userList.add(user_profile);
                    Log.d("bhaskar", user_profile.getmobilenumber() + " " + user_profile.getname());
                }
                if(dialog.isShowing())
                    dialog.dismiss();
                finish();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });


    }


    public void updateProfile() {

        dialog.setMessage("Proccessing...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String userEmail = vE_ap_emailid.getText().toString().trim();
        String userMobile = vE_ap_mobile.getText().toString().trim();
        String userName = vE_ap_name.getText().toString().trim();

      /*   String base64Image;
       if (isProfilepicChanged) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
            vI_ap_profile.setDrawingCacheEnabled(true);
            vI_ap_profile.buildDrawingCache();
            Bitmap bitmap = vI_ap_profile.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        } else {
            base64Image = mProfilepic;
        }*/

        reference = FirebaseDatabase.getInstance().getReference("users");
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("age", "26");
        userMap.put("emailid", userEmail);
        userMap.put("mobilenumber", userMobile);
        userMap.put("name", userName);
        userMap.put("profileImage", mProfilepic);
        reference.child(mKey)
                .updateChildren(userMap);

        getUserprofileDetails(userEmail);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handler.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onImageResult(String imageName, String imagePath, Bitmap imageFile, int sourceType) {
        if (sourceType == CameraHandler.CAMERA_TYPE) {
            isProfilepicChanged = true;
            picName=imageName;
            vI_ap_profile.setImageBitmap(imageFile);
            uploadProfilepic();
        } else if (sourceType == CameraHandler.GALLERY_TYPE) {
            isProfilepicChanged = true;
            picName=imageName;
            vI_ap_profile.setImageBitmap(imageFile);
            uploadProfilepic();
        }
    }


    public void uploadProfilepic(){

        dialog.setMessage("Proccessing...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://chatapp-24ab9.appspot.com");
        String chileName="images/"+picName;
        StorageReference spaceRef = storageRef.child(chileName);
        vI_ap_profile.setDrawingCacheEnabled(true);
        vI_ap_profile.buildDrawingCache();
        Bitmap bitmap = vI_ap_profile.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = spaceRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                try{
                    mProfilepic=downloadUrl.toString();
                }catch (NullPointerException ex){
                    ex.printStackTrace();
                }
                if(dialog.isShowing())
                    dialog.dismiss();

                // Log.d("bhaskar",downloadUrl.getLastPathSegment());
            }
        });


    }



}

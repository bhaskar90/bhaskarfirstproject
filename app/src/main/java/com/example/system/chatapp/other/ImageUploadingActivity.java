package com.example.system.chatapp.other;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.system.chatapp.R;
import com.example.system.chatapp.utils.CameraHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ImageUploadingActivity extends AppCompatActivity  implements CameraHandler.OnImageResultListner{

    ImageView imageupload;
    CameraHandler handler;
    TextView uploadImage;
    FirebaseStorage storage ;
    String picName;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_uploading);

        handler=new CameraHandler(this);
        imageupload= (ImageView) findViewById(R.id.imageupload);
        uploadImage= (TextView) findViewById(R.id.uploadImage);
        storage = FirebaseStorage.getInstance();

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.showDialogToCaptureImage();
            }
        });


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference storageRef = storage.getReferenceFromUrl("gs://chatapp-24ab9.appspot.com");
                String chileName="images/"+picName;
                StorageReference spaceRef = storageRef.child(chileName);
                imageupload.setDrawingCacheEnabled(true);
                imageupload.buildDrawingCache();
                Bitmap bitmap = imageupload.getDrawingCache();
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
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handler.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onImageResult(String imageName, String imagePath, Bitmap imageFile, int sourceType) {

        if(sourceType==CameraHandler.CAMERA_TYPE){
            Log.e("imageName",imageName);
            //Log.e("imagePath",imagePath);
            //Log.e("type","CameraType");
            picName=imageName;
            imageupload.setImageBitmap(imageFile);
        }else if(sourceType==CameraHandler.GALLERY_TYPE){
            Log.e("imageName",imageName);
            picName=imageName;
            //Log.e("imagePath",imagePath);
           // Log.e("type","GalleryType");
            imageupload.setImageBitmap(imageFile);
        }


    }
}

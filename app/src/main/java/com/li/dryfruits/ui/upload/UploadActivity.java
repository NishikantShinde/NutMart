package com.li.dryfruits.ui.upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.ui.login.LoginActivity;
import com.li.dryfruits.util.AppConstants;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

public class UploadActivity extends AppCompatActivity {

    ProductModel productModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Bundle b= getIntent().getExtras();
        if (b!=null){
            productModel=b.getParcelable(AppConstants.productData);
        }
        Button upload= findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndPermission.with(UploadActivity.this)
                        .runtime()
                        .permission(Permission.Group.STORAGE)
                        .onDenied(permission->{

                        }).onGranted(permission->{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),100);
                }).start();
            }
        });
    }

    private void saveProfileImage(Uri uriProfileImage) {

        if (uriProfileImage != null) {
            StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("productPics/"+productModel.getProductId()+".jpg");
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String profileUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                    Log.e("url",profileUrl);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri fileUri=data.getData();
            saveProfileImage(fileUri);
        }
    }
}
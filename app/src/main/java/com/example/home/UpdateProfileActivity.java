package com.example.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.home.databinding.ActivityAddProductBinding;
import com.example.home.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {


    //view binding
    private ActivityUpdateProfileBinding binding;


   FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    //firebase instances
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

    //firebase instances fire store
    StorageReference storageReference;


    //progress dialog

    private ProgressDialog progressDialog;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission arrays
    private String[] cameraPermissions;
    private String[] storagePermissions;

    //image picked url
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());





        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait");
        progressDialog.setCanceledOnTouchOutside(false);



        //init permission arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //handle click, go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //handle click, picture

        binding.productIconTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog to pick image

                showImagePickDialog();

            }
        });


        //handle add appliance click

        binding.updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Flow
                //input data


               inputData();




            }
        });


    }

    private  String name="", phone="", address="";
    private void inputData() {

        //input data


        name = binding.nameEt.getText().toString().trim();
        phone = binding.phoneEt.getText().toString().trim();
        address = binding.addressEt.getText().toString().trim();



        //validation of phone number

        String mobileRegex = "[0][6-8][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"; // validation of south africa phone number
        Matcher matcher;

        Pattern mobilePattern = Pattern.compile(mobileRegex);
        matcher = mobilePattern.matcher(phone);


        //validate data



        if (image_uri == null){
            Toast.makeText(this, "image is required", Toast.LENGTH_SHORT).show();
            return;

        }

        if(TextUtils.isEmpty(name)){

            Toast.makeText(this, "name is required", Toast.LENGTH_SHORT).show();
            return;

        }
        if (name.length()> 16){

            Toast.makeText(this, "name too long", Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(phone)){

            Toast.makeText(this, "phone number is required", Toast.LENGTH_SHORT).show();
            return;

        }
        else if (phone.length() > 10){

            Toast.makeText(this, "only 10 digits are allowed", Toast.LENGTH_SHORT).show();
            return;


        }
        else if (!matcher.find()){
            Toast.makeText(this, "invalid phone number", Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(address)){

            Toast.makeText(this, "enter your address", Toast.LENGTH_SHORT).show();
            return;

        }
        else {

            uploadImage();

            //data validated, then you can take it to realtime database


        }





    }


    private void uploadImage() {

        progressDialog.setMessage("updating profile");
        progressDialog.show();



       // final String timestamp = ""+System.currentTimeMillis();

         final String uid = firebaseAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference("profileImages/"+uid);

        storageReference.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //getting url of the image

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while(!uriTask.isSuccessful());

                        Uri downloadImageUri = uriTask.getResult();

                        if(uriTask.isSuccessful()){

/*


                            //url of the image, upload to realtime database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            databaseReference.child(uid)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            //get user type

                                            String userType =""+snapshot.child("userType").getValue();
                                            String email = ""+snapshot.child("email").getValue();
                                            String password = ""+snapshot.child("password").getValue();


                                            //trying to add the data to realtime database

                                            //ProductClass productClass =new ProductClass(timestamp, productTitle,productDescription,productCategory, ""+downloadImageUri);

                                            //EditProfile editProfile = new EditProfile(phone, name, uid, ""+downloadImageUri);

                                            Users users=new Users(uid,phone,email,name.toUpperCase(),password, userType,address,""+downloadImageUri);


                                            firebaseDatabase.getReference("Users").child(uid).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //if a product was added on the realtime database



                                                    //clear data after


                                                    startActivity(new Intent(UpdateProfileActivity.this, UpdateProfileActivity.class));
                                                    finish();
                                                }
                                            });





                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });





*/
                            //updating your information into realtime database



                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            ref.child("address").setValue(address);
                            ref.child("name").setValue(name.toUpperCase());
                            ref.child("profileImage").setValue(""+downloadImageUri);
                            ref.child("phone").setValue(phone);





                            //clear the values after adding
                            binding.phoneEt.setText("");
                            binding.addressEt.setText("");
                            binding.nameEt.setText("");

                           binding.productIconTv.setImageResource(R.drawable.ic_baseline_person_24);
                           image_uri = null;







                        }



                        //success
                        binding.productIconTv.setImageURI(null);

                        progressDialog.dismiss();

                        Toast.makeText(UpdateProfileActivity.this, "profile updated", Toast.LENGTH_SHORT).show();




                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UpdateProfileActivity.this, "failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void showImagePickDialog() {

        //options to display dialog
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item clicks

                        if(which==0){
                            //camera clicked

                            if(checkCameraPermission()){
                                //permission granted
                                pickFromCamera();
                            }
                            else{
                                //permission not  granted, request
                                requestCameraPermission();

                            }

                        }

                        else{
                            //gallery clicked

                            if(checkStoragePermission()){
                                //permission granted
                                pickFromGallery();

                            }
                            else{
                                //permission not  granted, request
                                requestStoragePermission();

                            }

                        }
                    }
                })
                .show();





    }

    //intent to pick image from gallery
    private void pickFromGallery(){

        //intent to pick image from gallery

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    //intent to pick image from camera

    private void pickFromCamera(){

        //intent to pick image from camera

        //using media store to pick high/original quality image

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "temp_Image");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "temp_Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);


    }

    private boolean checkStoragePermission(){

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        // returns true/false
        return result;

    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);


        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        // returns true/false
        return result && result1;

    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //handle permission results


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted){
                        //both permissions granted
                        pickFromCamera();
                    }
                    else{
                        //both or one of the permissions denied
                        Toast.makeText(this, "camera and storage permission are required ", Toast.LENGTH_SHORT).show();
                    }
                    break;

                }
            }

            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted){
                        //permission granted
                        pickFromGallery();
                    }
                    else{
                        //permission denied

                        Toast.makeText(this, "storage permission are required ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //handle image pick results


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == IMAGE_PICK_GALLERY_CODE){
            //image from gallery

            image_uri = data.getData();

            //set image

            binding.productIconTv.setImageURI(image_uri);

        }

        else if (requestCode == IMAGE_PICK_CAMERA_CODE){
            //image from camera

            binding.productIconTv.setImageURI(image_uri);

        }



    }



}
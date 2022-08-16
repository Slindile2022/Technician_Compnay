package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.home.databinding.ActivityRegisterBinding;
import com.example.home.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //view binding
    private ActivityRegisterBinding binding;


    private FirebaseAuth firebaseAuth;

    //firebase collection


   private FirebaseDatabase firebaseDatabase;

    //progress dialog

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //initialise firebase auth

        firebaseAuth = FirebaseAuth.getInstance();

        //initialise firebase storage

       //firestore = FirebaseFirestore.getInstance();

        //initialise firebase realtime database





        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait");
        progressDialog.setCanceledOnTouchOutside(false);




        //handle click, go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // handle register button
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();

            }
        });

    }

    private String name="", email="", password="",cPassword="", phone="";

    private void validateData() {
        //validating user information

        //get data

        name = binding.nameEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();
        phone = binding.phoneEt.getText().toString().trim();
        cPassword = binding.cPasswordEt.getText().toString().trim();



        //validation data




        //validate data

        if(TextUtils.isEmpty(name)){

            Toast.makeText(this, "enter your name", Toast.LENGTH_SHORT).show();
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "invalid email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "enter phone number", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(cPassword)){
            Toast.makeText(this, "confirm password", Toast.LENGTH_SHORT).show();
        }

        else if(!password.equals(cPassword)){
            Toast.makeText(this, "password does not match", Toast.LENGTH_SHORT).show();
        }

        else {
            createUserAccount();
        }
    }

    private void createUserAccount() {
        //show progress

        progressDialog.setMessage("Creating account..");
        progressDialog.show();

        //create user in firebase auth

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //account creation success

                        //data added to database
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Account created..", Toast.LENGTH_SHORT).show();

                        //direct to dashboard since account created
                        startActivity(new Intent(RegisterActivity.this, DashboardAdminActivity.class));
                        finish();


                       updateUserInfo();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //account creation failure
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void updateUserInfo() {
        progressDialog.setMessage("creating account...");


        // time stamp
        long timestamp = System.currentTimeMillis();


        //get current user
        String uid = firebaseAuth.getUid();

        //setup data to add in database
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("phone", phone);
        hashMap.put("profileImage", "");//will update later
        hashMap.put("address", "");//will update later
        hashMap.put("userType", "user");
        hashMap.put("timestamp", "timestamp");



        //set data to database

        DatabaseReference ref ;
        ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //data added to database

                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Account created..", Toast.LENGTH_SHORT).show();

                        //direct to dashboard since account created
                        startActivity(new Intent(RegisterActivity.this, DashboardAdminActivity.class));
                        finish();



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {

                        //data not added

                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });


















    }
}
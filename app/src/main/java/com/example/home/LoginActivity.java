package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.home.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {


    //view binding
    private ActivityLoginBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog

    private ProgressDialog progressDialog;

    String message = "you have been successfully logged in to your profile, regards : Home Appliance Application";
    String phone = "27729499847";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //int firebase auth
        firebaseAuth = FirebaseAuth.getInstance();



        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait");
        progressDialog.setCanceledOnTouchOutside(false);

     // handle click
        binding.noAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        //handle click forgot password

        binding.forgotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        //handle, begin login

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();


            }
        });

    }




    private String email="", password="";
    private void validateData() {

        //validate user data before login

        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();


        if (email.isEmpty()){
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "invalid email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
        }
        else{
            //begin logging in user
            loginUser();
        }
    }

    private void loginUser() {

        //show progress

        progressDialog.setMessage("logging in...");
        progressDialog.show();

        //login user

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //check if the email is verified
                        checkUser();



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //Login failure
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void checkUser() {


        //checking if the user is verified or not

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified()){

           //check in the realtime database the user access level

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //get user type

                            String userType =""+snapshot.child("userType").getValue();


                            //check user type

                            if(userType.equals("admin")){
                                //you are an admin

                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                                finish();
                                Toast.makeText(LoginActivity.this, "welcome back admin", Toast.LENGTH_SHORT).show();
                            }
                            else if(userType.equals("customer")) {

                                //you are an ordinary user , we take you to customer admin

                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, DashboardUserActivity.class));
                                finish();
                                Toast.makeText(LoginActivity.this, "welcome back customer", Toast.LENGTH_SHORT).show();
                            }

                            else if (userType.equals("technician")){

                                //you are an technician, it takes you to normal place
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, DashboardTechniciansActivity.class));
                                finish();
                                Toast.makeText(LoginActivity.this, "welcome back technician", Toast.LENGTH_SHORT).show();

                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




        }

        else {

            //show progress bar
            progressDialog.setMessage("sending account verification instructions");
            progressDialog.show();


            //send verification email
            FirebaseUser user = firebaseAuth.getCurrentUser();
            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    progressDialog.dismiss();

                    //send verification to the user's email
                    Toast.makeText(LoginActivity.this, "email verification sent", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();

                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });




        }
    }


}
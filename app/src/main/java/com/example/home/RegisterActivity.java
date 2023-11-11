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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {


    //view binding
    private ActivityRegisterBinding binding;

    //firebase auth

    private FirebaseAuth firebaseAuth;




    //progress dialog

    private ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //initialise firebase auth

        firebaseAuth = FirebaseAuth.getInstance();





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



        //validation of phone number

        String mobileRegex = "[0][6-8][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"; // validation of south africa phone number
        Matcher matcher;

        Pattern mobilePattern = Pattern.compile(mobileRegex);
        matcher = mobilePattern.matcher(phone);




        //validate data

        if(TextUtils.isEmpty(name)){

            Toast.makeText(this, "enter your name", Toast.LENGTH_SHORT).show();
        }
        if (name.length()> 16){

            Toast.makeText(this, "name too long", Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(email)){

            Toast.makeText(this, "enter your email", Toast.LENGTH_SHORT).show();
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "invalid email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "enter phone number", Toast.LENGTH_SHORT).show();
        }
        else if (phone.length() > 10){

            Toast.makeText(this, "only 10 digits are allowed", Toast.LENGTH_SHORT).show();

        }
        else if (!matcher.find()){
            Toast.makeText(this, "invalid phone number", Toast.LENGTH_SHORT).show();
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


                        String uid = authResult.getUser().getUid();

                        Users users=new Users(uid,phone,email,name.toUpperCase(),password, "customer","","");




                        firebaseDatabase.getReference("Users").child(uid).setValue(users);

                        //send verification email right after registering

                        //send verification email
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                progressDialog.dismiss();

                                //send verification to the user's email and take you to log in
                                Toast.makeText(RegisterActivity.this, "Account created, check your email for verification", Toast.LENGTH_SHORT).show();

                                //direct to dashboard since account created
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();





                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();

                                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });







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

}
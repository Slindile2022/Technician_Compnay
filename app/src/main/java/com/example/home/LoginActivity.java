package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.home.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    //view binding
    private ActivityLoginBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog

    private ProgressDialog progressDialog;



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
            //begin loging in user
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

                        //data added to database
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "welcome back..", Toast.LENGTH_SHORT).show();

                        //direct to dashboard since account created
                        startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                        finish();
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


}
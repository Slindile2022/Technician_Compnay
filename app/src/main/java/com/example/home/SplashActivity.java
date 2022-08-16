package com.example.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //int auth

        firebaseAuth = FirebaseAuth.getInstance();


        //start main activity after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();

            }
        },2000);

    }

    private void checkUser() {

        //get current use if is logged in
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //user not logged in
            //start main screen

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();

        }
        else
        {
            //user is logged in
            //direct to dashboard since account created
            startActivity(new Intent(SplashActivity.this, DashboardAdminActivity.class));
            finish();


        }
    }
}
package com.example.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.home.databinding.ActivityDashboardAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardAdminActivity extends AppCompatActivity {

    //view binding
    private ActivityDashboardAdminBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //int firebase auth

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        //handle log out

        //default layout should be products

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //log out the user

                firebaseAuth.signOut();
                checkUser();
            }
        });

        //handle , add product

        binding.addPdtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takes you to add product page

                startActivity(new Intent(DashboardAdminActivity.this, AddProductActivity.class));

            }
        });

        //handle , update user profile

        binding.updateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takes you to add product page

                startActivity(new Intent(DashboardAdminActivity.this, UpdateProfileActivity.class));

            }
        });

        //check, click on product

        binding.tabProductTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // load products
                showProductsUi();
                

            }
        });

        binding.tabOrderTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //view orders
                showOrdersUi();

            }
        });


    }


    private void showProductsUi() {

        //show products ui and hide orders

        binding.productR1.setVisibility(View.VISIBLE);
        binding.ordersR1.setVisibility(View.GONE);

        binding.tabProductTv.setTextColor(getResources().getColor(R.color.black));

        binding.tabProductTv.setBackgroundResource(R.drawable.shape_rect03);

        binding.tabOrderTv.setTextColor(getResources().getColor(R.color.white));

        binding.tabOrderTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void showOrdersUi() {

        //show orders ui and hide products

        binding.ordersR1.setVisibility(View.VISIBLE);
        binding.productR1.setVisibility(View.GONE);

        binding.tabOrderTv.setTextColor(getResources().getColor(R.color.black));

        binding.tabOrderTv.setBackgroundResource(R.drawable.shape_rect03);

        binding.tabProductTv.setTextColor(getResources().getColor(R.color.white));

        binding.tabProductTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }





    private void checkUser() {

        //get current user

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null){
            //not logged in, go to main screeen

            startActivity(new Intent(this, MainActivity.class));
            finish();

        }
        else{

            //logged in, get user info
            String email = firebaseUser.getEmail();

            //set in the text view of the toolabar
            binding.subTitleTv.setText(email);
        }
    }
}
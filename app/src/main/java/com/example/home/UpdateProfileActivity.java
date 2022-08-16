package com.example.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.home.databinding.ActivityAddProductBinding;
import com.example.home.databinding.ActivityUpdateProfileBinding;

public class UpdateProfileActivity extends AppCompatActivity {


    //view binding
    private ActivityUpdateProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        //handle click, go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
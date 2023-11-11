package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.home.databinding.ActivityDashboardUserBinding;
import com.example.home.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingsActivity extends AppCompatActivity {

    //view binding
    private ActivitySettingsBinding binding;

    static final String  enabledMessage = "Notifications are enabled";
    static final String  disabledMessage = "Notifications are disabled";

    private boolean isChecked = false;


    private FirebaseAuth firebaseAuth;

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        //int shared preferences

        sp = getSharedPreferences("SETTINGS_SP", MODE_PRIVATE);

        //check last selected option; true/false
        isChecked = sp.getBoolean("FCM_ENABLED", true);
        binding.ntfSwitch.setChecked(isChecked);

        if (isChecked){
            //was enabled
            binding.notificationStatusTv.setText(enabledMessage);
        }
        else {
            //was disabled
            binding.notificationStatusTv.setText(disabledMessage);


        }


        //handle click, go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //add switch check change listener to enable/disable notifications




        binding.ntfSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    //checked, allow notifications


                    subscribeToTopic();

                }
                else {
                    //unchecked, allow notifications
                    unsubscribeToTopic();

                }

            }
        });



        binding.ntfSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    //checked, allow notifications


                    subscribeToTopic();

                }
                else {
                    //unchecked, allow notifications
                    unsubscribeToTopic();

                }


            }
        });

    }

    private void subscribeToTopic(){

        FirebaseMessaging.getInstance().subscribeToTopic(Category.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        //subscribed successfully, save settings
                        spEditor = sp.edit();
                        spEditor.putBoolean("FCM_ENABLED", true);
                        spEditor.apply();

                        Toast.makeText(SettingsActivity.this, ""+enabledMessage, Toast.LENGTH_SHORT).show();
                        binding.notificationStatusTv.setText(enabledMessage);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to subscribe
                        Toast.makeText(SettingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();



                    }
                });
    }

    private void unsubscribeToTopic(){

        FirebaseMessaging.getInstance().unsubscribeFromTopic(Category.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //unsubscribed successfully, save settings
                        spEditor = sp.edit();
                        spEditor.putBoolean("FCM_ENABLED", false);
                        spEditor.apply();

                        Toast.makeText(SettingsActivity.this, ""+disabledMessage, Toast.LENGTH_SHORT).show();
                        binding.notificationStatusTv.setText(disabledMessage);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to unsubscribe
                        Toast.makeText(SettingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }


}
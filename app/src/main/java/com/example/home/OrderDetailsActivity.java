package com.example.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.home.databinding.ActivityDashboardAdminBinding;
import com.example.home.databinding.ActivityDashboardUserBinding;
import com.example.home.databinding.ActivityOrderDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    //view binding
    private ActivityOrderDetailsBinding binding;

    private String orderId, userId, orderStatus;

    private FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;

    Query databaseReference;

    OrderedItemsDetailsAdapter orderedItemsDetailsAdapter;


    List<ModelOrderedItemsDetails> modelOrderedItemsDetailsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getting the values
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        userId = intent.getStringExtra("userId");
        orderStatus = intent.getStringExtra("orderStatus");


        firebaseAuth = FirebaseAuth.getInstance();

        loadMyInfo();

        //convert timestamp to proper date format

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(orderId));
        String formatDate = DateFormat.format("dd/MM/yyyy", calendar).toString();

        //convert timestamp to proper date format

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(Long.parseLong(orderId));
        String formatDate1 = DateFormat.format("HH:mm", calendar).toString();


        //set in the text view of the toolbar
        binding.orderStatusTv.setText(orderStatus);
        binding.orderIdTv.setText(orderId);
        binding.dateTv.setText(formatDate + "(" + formatDate1 + ")");


        //handle back button
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        //load all the order details

        firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference().child("AdminOrderedItemsList");
        databaseReference = firebaseDatabase.getReference().child("Users").child(userId).child("ordersList").child("items").child(orderId);

        recyclerView = binding.itemsRv;
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));


        modelOrderedItemsDetailsList = new ArrayList<ModelOrderedItemsDetails>();
        orderedItemsDetailsAdapter = new OrderedItemsDetailsAdapter(OrderDetailsActivity.this, modelOrderedItemsDetailsList);
        recyclerView.setAdapter(orderedItemsDetailsAdapter);





        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ModelOrderedItemsDetails modelOrderedItemsDetails = snapshot.getValue(ModelOrderedItemsDetails.class);
                modelOrderedItemsDetailsList.add(modelOrderedItemsDetails);

                //counting the items
                int itemCount = modelOrderedItemsDetailsList.size();
                String items = Integer.toString(itemCount);
                binding.itemsQuantityTv.setText(items);



                orderedItemsDetailsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    private void loadMyInfo() {


        //getting your information from realtime database


        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get user type

                        //get user type

                        String address =""+snapshot.child("address").getValue();
                        String phone =""+snapshot.child("phone").getValue();


                        binding.addressTv.setText(address);
                        binding.phoneTv.setText(phone);






                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }






                });

    }

}


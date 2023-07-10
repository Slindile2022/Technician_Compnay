package com.example.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.home.databinding.ActivityOrderDetailsAdminBinding;
import com.example.home.databinding.ActivityOrderDetailsBinding;
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

public class OrderDetailsAdminActivity extends AppCompatActivity {

    //view binding
    private ActivityOrderDetailsAdminBinding binding;

    String orderId, userId, orderStatus, technician, amount, nameOfAttender,nameOfAttenderOriginalId, userType1;

    private FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;

    Query databaseReference;

    OrderedItemsDetailsAdapter orderedItemsDetailsAdapter;


    List<ModelOrderedItemsDetails> modelOrderedItemsDetailsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderDetailsAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getting the values
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        userId = intent.getStringExtra("userId");
        orderStatus = intent.getStringExtra("orderStatus");
        technician = intent.getStringExtra("technician");


        firebaseAuth = FirebaseAuth.getInstance();

        loadMyInfo();

        loadAttenderInfo();

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


        recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailsAdminActivity.this));


        modelOrderedItemsDetailsList = new ArrayList<ModelOrderedItemsDetails>();
        orderedItemsDetailsAdapter = new OrderedItemsDetailsAdapter(OrderDetailsAdminActivity.this, modelOrderedItemsDetailsList);
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

        binding.moneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //getting your information from realtime database


                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AdminOrders");
                databaseReference.child(orderId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //get user type

                                //get user name

                                String name = ""+snapshot.child("technician").getValue();
                                String price = ""+snapshot.child("price").getValue();



                                //get attendant information

                                //getting technician information



                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                databaseReference.child(firebaseAuth.getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                //get user type


                                                String attenderId =""+snapshot.child("uid").getValue();
                                                String userType = ""+snapshot.child("userType").getValue();

                                                if (userType.equals("admin")){
                                                    //must be able to play with all the settings

                                                    //money analog

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsAdminActivity.this);
                                                    builder.setTitle("Enter amount");

                                                    // Set up the input
                                                    final EditText input = new EditText(OrderDetailsAdminActivity.this);
                                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                                    builder.setView(input);



                                                    // Set up the buttons
                                                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            amount = input.getText().toString();


                                                            //checking if the amount is entered or not
                                                            if (amount.isEmpty()){
                                                                Toast.makeText(OrderDetailsAdminActivity.this, "amount is empty", Toast.LENGTH_SHORT).show();

                                                            }
                                                            else {

                                                                //update price under users

                                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders").child(orderId);
                                                                ref.child("price").setValue(amount);


                                                                //update price under admin orders

                                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AdminOrders").child(orderId);
                                                                reference.child("price").setValue(amount);
                                                            }


                                                        }
                                                    });
                                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            dialog.cancel();
                                                        }
                                                    });

                                                    builder.show();





                                                }

                                                else if (!price.isEmpty()){

                                                    Toast.makeText(OrderDetailsAdminActivity.this, "You can't change the price", Toast.LENGTH_SHORT).show();
                                                    return;

                                                }

                                                else if (!nameOfAttenderOriginalId.equals(attenderId)){

                                                    Toast.makeText(OrderDetailsAdminActivity.this, "The order is being attended", Toast.LENGTH_SHORT).show();
                                                    return;

                                                }
                                                else {




                                                    //money analog

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsAdminActivity.this);
                                                    builder.setTitle("Enter amount");

                                                    // Set up the input
                                                    final EditText input = new EditText(OrderDetailsAdminActivity.this);
                                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                                    builder.setView(input);



                                                    // Set up the buttons
                                                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            amount = input.getText().toString();


                                                            //checking if the amount is entered or not
                                                            if (amount.isEmpty()){
                                                                Toast.makeText(OrderDetailsAdminActivity.this, "amount is empty", Toast.LENGTH_SHORT).show();

                                                            }
                                                            else {

                                                                //update price under users

                                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders").child(orderId);
                                                                ref.child("price").setValue(amount);


                                                                //update price under admin orders

                                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AdminOrders").child(orderId);
                                                                reference.child("price").setValue(amount);
                                                            }


                                                        }
                                                    });
                                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            dialog.cancel();
                                                        }
                                                    });

                                                    builder.show();





                                                }






                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }






                                        });







                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



            }
        });

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //getting technician information



                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users");
                databaseReference1.child(firebaseAuth.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //get user type

                                String name = ""+snapshot.child("name").getValue();
                                String attenderId =""+snapshot.child("uid").getValue();
                                String userType =""+snapshot.child("userType").getValue();





                                //getting your information from realtime database, like your name




                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AdminOrders");
                                databaseReference.child(orderId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                //get user name

                                                String name = ""+snapshot.child("technician").getValue();
                                                String attenderId = ""+snapshot.child("technicianId").getValue();


                                                //check if the user is admin or not

                                                if (userType.equals("admin")){
                                                    //allow admin to make changes

                                                    //Dialog

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsAdminActivity.this);
                                                    builder.setTitle("order status")
                                                            .setItems(Category.orderFilter2, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    //get picked category



                                                                    String selected = Category.orderFilter2[which];


                                                                    //get attendant information

                                                                    //getting technician information



                                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                                                    databaseReference.child(firebaseAuth.getUid())
                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    //get user type

                                                                                    String name = ""+snapshot.child("name").getValue();
                                                                                    String attenderId =""+snapshot.child("uid").getValue();
                                                                                    String userType =""+snapshot.child("userType").getValue();



                                                                                    //update order status under users

                                                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders").child(orderId);
                                                                                    ref.child("orderStatus").setValue(selected);
                                                                                    ref.child("technician").setValue(name);
                                                                                    ref.child("technicianId").setValue(attenderId);


                                                                                    //update order status admin orders

                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AdminOrders").child(orderId);
                                                                                    reference.child("orderStatus").setValue(selected);
                                                                                    reference.child("technician").setValue(name);
                                                                                    reference.child("technicianId").setValue(attenderId);

                                                                                    binding.orderStatusTv.setText(selected);








                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }






                                                                            });






                                                                }



                                                            }).show();





                                                }


                                                else if(attenderId.isEmpty()){

                                                    //Dialog

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsAdminActivity.this);
                                                    builder.setTitle("order status")
                                                            .setItems(Category.orderFilter1, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    //get picked category



                                                                    String selected = Category.orderFilter1[which];


                                                                    //get attendant information

                                                                    //getting technician information



                                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                                                    databaseReference.child(firebaseAuth.getUid())
                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    //get user type

                                                                                    String name = ""+snapshot.child("name").getValue();
                                                                                    String attenderId =""+snapshot.child("uid").getValue();
                                                                                    String userType =""+snapshot.child("userType").getValue();



                                                                                    //update order status under users

                                                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders").child(orderId);
                                                                                    ref.child("orderStatus").setValue(selected);
                                                                                    ref.child("technician").setValue(name);
                                                                                    ref.child("technicianId").setValue(attenderId);


                                                                                    //update order status admin orders

                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AdminOrders").child(orderId);
                                                                                    reference.child("orderStatus").setValue(selected);
                                                                                    reference.child("technician").setValue(name);
                                                                                    reference.child("technicianId").setValue(attenderId);

                                                                                    binding.orderStatusTv.setText(selected);








                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }






                                                                            });






                                                                }



                                                            }).show();



                                                }
                                                else if (!nameOfAttenderOriginalId.equals(attenderId)){

                                                    Toast.makeText(OrderDetailsAdminActivity.this, "The order is being attended", Toast.LENGTH_SHORT).show();
                                                }
                                                else {

                                                    //Dialog

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsAdminActivity.this);
                                                    builder.setTitle("order status")
                                                            .setItems(Category.orderFilter1, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    //get picked category



                                                                    String selected = Category.orderFilter1[which];


                                                                    //get attendant information

                                                                    //getting technician information



                                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                                                    databaseReference.child(firebaseAuth.getUid())
                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    //get user type

                                                                                    String name = ""+snapshot.child("name").getValue();
                                                                                    String attenderId =""+snapshot.child("uid").getValue();
                                                                                    String userType =""+snapshot.child("userType").getValue();



                                                                                    //update order status under users

                                                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("orders").child(orderId);
                                                                                    ref.child("orderStatus").setValue(selected);
                                                                                    ref.child("technician").setValue(name);
                                                                                    ref.child("technicianId").setValue(attenderId);


                                                                                    //update order status admin orders

                                                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AdminOrders").child(orderId);
                                                                                    reference.child("orderStatus").setValue(selected);
                                                                                    reference.child("technician").setValue(name);
                                                                                    reference.child("technicianId").setValue(attenderId);

                                                                                    binding.orderStatusTv.setText(selected);


















                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }






                                                                            });






                                                                }



                                                            }).show();



                                                }





                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }






                        });





            }
        });



    }




    private void loadMyInfo() {

        //getting your information from realtime database



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get user type

                        //get user type

                        String address =""+snapshot.child("address").getValue();
                        String phone =""+snapshot.child("phone").getValue();
                        String name = ""+snapshot.child("name").getValue();



                        binding.addressTv.setText(address);
                        binding.phoneTv.setText(phone);
                        binding.orderByTv.setText(name);






                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }






                });

    }

    private void loadAttenderInfo() {

        //getting technician information



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get user type

                        String name = ""+snapshot.child("name").getValue();
                        String attenderId =""+snapshot.child("uid").getValue();


                        nameOfAttender = attenderId;
                        nameOfAttenderOriginalId = attenderId;








                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }






                });

    }
}
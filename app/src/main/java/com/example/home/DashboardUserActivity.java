package com.example.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.home.databinding.ActivityDashboardAdminBinding;
import com.example.home.databinding.ActivityDashboardTechniciansBinding;
import com.example.home.databinding.ActivityDashboardUserBinding;
import com.google.android.gms.common.internal.Constants;
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

import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class DashboardUserActivity extends AppCompatActivity {

    //view binding
    private ActivityDashboardUserBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog



    private ProgressDialog progressDialog;

    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;

    Query databaseReference;

    CustomerProductAdapter customerProductAdapter;
    OrderCustomerAdapter orderCustomerAdapter;


    List<ModelClass> modelClassList;
    List<ModelOrderCustomer> modelOrderCustomerList;


    //cart
    private ArrayList<ModelCartItems> cartItemsList;
    private CartItemsAdapter cartItemsAdapter;


    //string to check your physical address
    private String orderId;

    private  EasyDB easyDB;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //int firebase auth

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        //user id
        FirebaseUser user = firebaseAuth.getCurrentUser();


        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        //gonna help us for cart increment
        easyDB = EasyDB.init(this, "ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text", "unique"}))
                .addColumn(new Column("Item_PID", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Description", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                .doneTableColumn();

        //cart count

        cartCount();;
        //timestamp
        orderId = ""+System.currentTimeMillis();


        //show products by default
        showProductsUi();


        binding.filteredProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardUserActivity.this);
                builder.setTitle("appliance category")
                        .setItems(Category.applianceCategory1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get picked category



                                String selected = Category.applianceCategory1[which];


                                binding.filteredProductTv.setText("Showing "+selected);





                                if (selected.equals("All")){

                                    //load all the products since they are not filtered

                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                    databaseReference = firebaseDatabase.getReference().child("Products");

                                    recyclerView = binding.productsRv;
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(DashboardUserActivity.this));

                                    modelClassList = new ArrayList<ModelClass>();
                                    customerProductAdapter = new CustomerProductAdapter(DashboardUserActivity.this, modelClassList);
                                    recyclerView.setAdapter(customerProductAdapter);



                                    databaseReference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            ModelClass modelClass = snapshot.getValue(ModelClass.class);
                                            modelClassList.add(modelClass);

                                            customerProductAdapter.notifyDataSetChanged();
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

                                else  {


                                    //load only the selected products

                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                    databaseReference = firebaseDatabase.getReference().child("Products").orderByChild("productCategory").equalTo(selected);

                                    recyclerView = binding.productsRv;
                                    recyclerView.setHasFixedSize(true);


                                    recyclerView.setLayoutManager(new LinearLayoutManager(DashboardUserActivity.this));


                                    modelClassList = new ArrayList<ModelClass>();
                                    customerProductAdapter = new CustomerProductAdapter(DashboardUserActivity.this, modelClassList);
                                    recyclerView.setAdapter(customerProductAdapter);



                                    databaseReference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            ModelClass modelClass = snapshot.getValue(ModelClass.class);
                                            modelClassList.add(modelClass);

                                            customerProductAdapter.notifyDataSetChanged();
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




                            }



                        }).show();

            }
        });

        //if they are not filtered show them all

        //load all the products
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Products");
        // databaseReference.orderByChild("productCategory").equalTo("kettle");
        recyclerView = binding.productsRv;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardUserActivity.this));

        modelClassList = new ArrayList<ModelClass>();
        customerProductAdapter = new CustomerProductAdapter(DashboardUserActivity.this, modelClassList);
        recyclerView.setAdapter(customerProductAdapter);



        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ModelClass modelClass = snapshot.getValue(ModelClass.class);
                modelClassList.add(modelClass);

                customerProductAdapter.notifyDataSetChanged();
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





        //handle searching

        binding.searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    customerProductAdapter.getFilter().filter(s);
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //handle log out


        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the object of AlertDialog Builder class
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardUserActivity.this);

                // Set the message show for the Alert time
                builder.setMessage("Do you want to exit ?");

                // Set Alert Title
                builder.setTitle("Sign out");

                // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                builder.setCancelable(false);

                // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // When the user click yes button then app will close

                    //log out the user

                    firebaseAuth.signOut();
                    checkUser();

                });

                // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();

            }
        });

        //handle cart button
        binding.addPdtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show cart dialog

                showCartDialog();


            }
        });



        //handle , update user profile

        binding.updateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takes you to update your profile

                startActivity(new Intent(DashboardUserActivity.this, UpdateProfileActivity.class));

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

    private void showCartDialog() {

        //init list

        cartItemsList = new ArrayList<>();


        //inflate cart layout
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_cart, null);

        //TextView cartItemsRv = view.findViewById(R.id.cartItemsRv);
        Button checkOutBtn = view.findViewById(R.id.checkOutBtn);
        RecyclerView cartItemsRv = view.findViewById(R.id.cartItemsRv);

        //dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set view to dialog
        builder.setView(view);

        EasyDB easyDB = EasyDB.init(this, "ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text", "unique"}))
                .addColumn(new Column("Item_PID", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Description", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                .doneTableColumn();

        //get all records from database
        Cursor res = easyDB.getAllData();
        while (res.moveToNext()){
            String id = res.getString(1);
            String pId = res.getString(2);
            String name = res.getString(3);
            String description = res.getString(4);
            String quantity = res.getString(5);

            ModelCartItems modelCartItems = new ModelCartItems(
                    ""+id,
                    ""+pId,
                    ""+name,
                    ""+quantity,
                    ""+description
            );

            cartItemsList.add(modelCartItems);
        }
        //set up adapter

        cartItemsAdapter = new CartItemsAdapter(this, cartItemsList);
        cartItemsRv.setAdapter(cartItemsAdapter);

        //show dialog

        AlertDialog dialog = builder.create();
        dialog.show();

        //place order

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first validate if the address was updated or not

                //getting your information from realtime database


                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                databaseReference.child(firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                //check the address

                                String physicalAddress = ""+snapshot.child("address").getValue();
                                String name = ""+snapshot.child("name").getValue();

                                //address = physicalAddress;

                                if (physicalAddress.equals("")){
                                    Toast.makeText(DashboardUserActivity.this, "please update your address under your profile", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    return;


                                }

                                //checking if the cart size is empty or not

                                else if (cartItemsList.size() != 0){



                                    //add the information of the customer needing the service
                                    submitOrder();

                                    //if the cart is not empty add the list on the database



                                    for (int i=0; i<cartItemsList.size();i++){

                                        //add the items first
                                        String pId = cartItemsList.get(i).timeStamp;
                                        String title = cartItemsList.get(i).getName();
                                        String description = cartItemsList.get(i).getDescription();
                                        String quantity = cartItemsList.get(i).getQuantity();

                                        SubmitOrder submitOrder =new SubmitOrder(pId, title, description, quantity);



                                        //add the order in realtime database






                                        firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("ordersList").child("items").child(orderId).child(pId).setValue(submitOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                //dismiss the dialog on success

                                                dialog.dismiss();

                                                //if the order was successful, no toast since we we're gonna toast once the item list is added to the database



                                                //delete the cart items after submitting

                                                EasyDB easyDB = EasyDB.init(DashboardUserActivity.this, "ITEMS_DB")
                                                        .setTableName("ITEMS_TABLE")
                                                        .addColumn(new Column("Item_Id", new String[]{"text", "unique"}))
                                                        .addColumn(new Column("Item_PID", new String[]{"text", "not null"}))
                                                        .addColumn(new Column("Item_Name", new String[]{"text", "not null"}))
                                                        .addColumn(new Column("Item_Description", new String[]{"text", "not null"}))
                                                        .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                                                        .doneTableColumn();
                                                easyDB.deleteAllDataFromTable();


                                                cartCount();


                                            }
                                        });















                                    }



                                }
                                else {

                                    //dismiss the dialog since it is empty

                                    dialog.dismiss();

                                    //cart is empty
                                    Toast.makeText(DashboardUserActivity.this, "your cart is empty!", Toast.LENGTH_SHORT).show();
                                    return;



                                }






                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                //clear the string to submit a new order

                //timestamp
                orderId = ""+System.currentTimeMillis();


            }



        });








    }

    private void submitOrder() {

        //show progress

        progressDialog.setMessage("Sending your request");
        progressDialog.show();


        //getting your information from realtime database


        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get user type

                        //get user type

                        String userType =""+snapshot.child("userType").getValue();
                        String name = ""+snapshot.child("name").getValue();
                        String email = ""+snapshot.child("email").getValue();
                        String phone = ""+snapshot.child("phone").getValue();



                        //client id

                        String id = firebaseUser.getUid();


                        //trying to add the data to realtime database

                        SubmitOrderUserInfo submitOrderUserInfo =new SubmitOrderUserInfo(orderId, orderId, name, phone, id, "Not attended","","","");

                        //add the order in realtime database




                        firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("orders").child(orderId).setValue(submitOrderUserInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                //if the order was successful



                                progressDialog.dismiss();


                                Toast.makeText(DashboardUserActivity.this, "your request is submitted", Toast.LENGTH_LONG).show();
                            }
                        });

                        //also add to database so that admin can see the list of all orders



                        firebaseDatabase.getReference("AdminOrders").child(orderId).setValue(submitOrderUserInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                //if the order was successful




                                progressDialog.dismiss();

                                //after placing your order, open the order details
                                Intent intent = new Intent(DashboardUserActivity.this, OrderDetailsActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("userId", id);
                                startActivity(intent);


                                //update cart
                                cartCount();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
            //not logged in, go to main screen

            startActivity(new Intent(this, MainActivity.class));
            finish();

        }
        else{

            //logged in, get user info
            loadMyInfo();


            //handling searching by filter button
            binding.filteredProductBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Dialog

                    AlertDialog.Builder builder = new AlertDialog.Builder(DashboardUserActivity.this);
                    builder.setTitle("order status")
                            .setItems(Category.orderFilter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //get picked category



                                    String selected = Category.orderFilter[which];


                                    binding.filteredProductTv1.setText("Showing "+selected);





                                    if (selected.equals("All")){

                                        //load all the products
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("orders");



                                        recyclerView = binding.ordersRv;
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardUserActivity.this));

                                        modelOrderCustomerList = new ArrayList<ModelOrderCustomer>();
                                        orderCustomerAdapter = new OrderCustomerAdapter(DashboardUserActivity.this, modelOrderCustomerList);
                                        recyclerView.setAdapter(orderCustomerAdapter);



                                        databaseReference.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                ModelOrderCustomer modelOrderCustomer = snapshot.getValue(ModelOrderCustomer.class);
                                                modelOrderCustomerList.add(modelOrderCustomer);

                                                orderCustomerAdapter.notifyDataSetChanged();
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

                                    else  {


                                        //load only the filtered orders

                                        //load all the products
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("orders").orderByChild("orderStatus").equalTo(selected);



                                        recyclerView = binding.ordersRv;
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardUserActivity.this));

                                        modelOrderCustomerList = new ArrayList<ModelOrderCustomer>();
                                        orderCustomerAdapter = new OrderCustomerAdapter(DashboardUserActivity.this, modelOrderCustomerList);
                                        recyclerView.setAdapter(orderCustomerAdapter);



                                        databaseReference.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                ModelOrderCustomer modelOrderCustomer = snapshot.getValue(ModelOrderCustomer.class);
                                                modelOrderCustomerList.add(modelOrderCustomer);

                                                orderCustomerAdapter.notifyDataSetChanged();
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




                                }



                            }).show();

                }
            });



            //load all the products
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("orders");



            recyclerView = binding.ordersRv;
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(DashboardUserActivity.this));

            modelOrderCustomerList = new ArrayList<ModelOrderCustomer>();
            orderCustomerAdapter = new OrderCustomerAdapter(DashboardUserActivity.this, modelOrderCustomerList);
            recyclerView.setAdapter(orderCustomerAdapter);



            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    ModelOrderCustomer modelOrderCustomer = snapshot.getValue(ModelOrderCustomer.class);
                    modelOrderCustomerList.add(modelOrderCustomer);

                    orderCustomerAdapter.notifyDataSetChanged();
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
    }

    //cart count
    public void cartCount(){

        //cart items count
        int count = easyDB.getAllData().getCount();

        if (count <= 0){
            //no item in cart, hide it
            binding.cartCount.setVisibility(View.GONE);

        }
        else {
            //the cart is not empty
            binding.cartCount.setVisibility(View.VISIBLE);
            binding.cartCount.setText(""+count);
        }




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

                        String userType =""+snapshot.child("userType").getValue();
                        String name = ""+snapshot.child("name").getValue();
                        String email = ""+snapshot.child("email").getValue();
                        String profileImage = ""+snapshot.child("profileImage").getValue();



                        //set in the text view of the toolbar
                        binding.subTitleTv.setText(email.substring(0, 10) + "...");
                        binding.userType.setText(userType);
                        binding.emailEt.setText(name);

                        //set profile image

                        Glide.with(DashboardUserActivity.this)
                                .load(profileImage)
                                .placeholder(R.drawable.profile)
                                .into(binding.profileImage);

                        //load all your orders
                        //loadOrders();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void prepareNotificationMessage(String orderId){
        //when user places order, send notification to admin and technician

        //prepare data for notification
        String NOTIFICATION_TOPIC = "/topic/" + Category.FCM_TOPIC; //must be subscribed by user
        String NOTIFICATION_TILE = "New Order" +orderId;
        String NOTIFICATION_MESSAGE = "You have new order";
        String NOTIFICATION_TYPE = "New order";

        //prepare json(what you want to send and where)

        JSONObject notificationJo = new JSONObject();
        JSONObject notificationBodyJo = new JSONObject();

        try {
            //what to send
            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE);
            notificationBodyJo.put("customer", firebaseAuth.getUid()); //user signed up
            notificationBodyJo.put("orderId", orderId);
            notificationBodyJo.put("notificationTitle", NOTIFICATION_TILE);
            notificationBodyJo.put("notificationMessage", NOTIFICATION_MESSAGE);

            //where to send
            notificationJo.put("to", NOTIFICATION_TOPIC); //to all subscribed
            notificationJo.put("data", notificationBodyJo);

        }
        catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        sendFcmNotification(notificationJo, orderId);




    }

    private void sendFcmNotification(JSONObject notificationJo, String orderId) {

        //send volley request

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //after sending fcm start order details activity

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //if failed sending fcm, still start order activity


            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //put required headers
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key="+Category.FCM_KEY);


                return super.getParams();
            }
        };


    }



}
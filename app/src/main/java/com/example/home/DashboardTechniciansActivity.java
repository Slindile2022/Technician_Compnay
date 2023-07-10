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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.example.home.databinding.ActivityDashboardAdminBinding;
import com.example.home.databinding.ActivityDashboardTechniciansBinding;
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
import java.util.List;

public class DashboardTechniciansActivity extends AppCompatActivity {

    //view binding
    private ActivityDashboardTechniciansBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;

    Query databaseReference;

    AdminProductAdapter adminProductAdapter;

    List<ModelClass> modelClassList;




    //getting all the orders
    OrderAdminAdapter orderAdminAdapter;
    List<ModelOrderCustomer> modelOrderCustomerList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardTechniciansBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //int firebase auth

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();



        //default call is product

        showProductsUi();

        //handle filter button

        binding.filteredProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardTechniciansActivity.this);
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
                                    // databaseReference.orderByChild("productCategory").equalTo("kettle");
                                    recyclerView = binding.productsRv;
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(DashboardTechniciansActivity.this));

                                    modelClassList = new ArrayList<ModelClass>();
                                    adminProductAdapter = new AdminProductAdapter(DashboardTechniciansActivity.this, modelClassList);
                                    recyclerView.setAdapter(adminProductAdapter);



                                    databaseReference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            ModelClass modelClass = snapshot.getValue(ModelClass.class);
                                            modelClassList.add(modelClass);

                                            adminProductAdapter.notifyDataSetChanged();
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
                                    // databaseReference.orderByChild("productCategory").equalTo("kettle");
                                    recyclerView = binding.productsRv;
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(DashboardTechniciansActivity.this));

                                    modelClassList = new ArrayList<ModelClass>();
                                    adminProductAdapter = new AdminProductAdapter(DashboardTechniciansActivity.this, modelClassList);
                                    recyclerView.setAdapter(adminProductAdapter);



                                    databaseReference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            ModelClass modelClass = snapshot.getValue(ModelClass.class);
                                            modelClassList.add(modelClass);

                                            adminProductAdapter.notifyDataSetChanged();
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


        //loading all the products if they are not filtered

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Products");
        recyclerView = binding.productsRv;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelClassList = new ArrayList<ModelClass>();
        adminProductAdapter = new AdminProductAdapter(DashboardTechniciansActivity.this, modelClassList);
        recyclerView.setAdapter(adminProductAdapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ModelClass modelClass = snapshot.getValue(ModelClass.class);
                modelClassList.add(modelClass);

                adminProductAdapter.notifyDataSetChanged();
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
                    adminProductAdapter.getFilter().filter(s);
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //handle inbox icon


        //handle log out

        //default layout should be products

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the object of AlertDialog Builder class
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardTechniciansActivity.this);

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



        //handle , update user profile

        binding.updateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takes you to update your profile


                startActivity(new Intent(DashboardTechniciansActivity.this, UpdateProfileActivity.class));

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(DashboardTechniciansActivity.this);
                    builder.setTitle("order status")
                            .setItems(Category.orderFilter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //get picked category



                                    String selected = Category.orderFilter[which];


                                    binding.filteredProductTv1.setText("Showing "+selected);





                                    if (selected.equals("All")){

                                        //load all the orders
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        databaseReference = firebaseDatabase.getReference().child("AdminOrders");


                                        recyclerView = binding.ordersRv;
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardTechniciansActivity.this));

                                        modelOrderCustomerList = new ArrayList<ModelOrderCustomer>();
                                        orderAdminAdapter = new OrderAdminAdapter(DashboardTechniciansActivity.this, modelOrderCustomerList);
                                        recyclerView.setAdapter(orderAdminAdapter);


                                        databaseReference.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                ModelOrderCustomer modelOrderCustomer = snapshot.getValue(ModelOrderCustomer.class);
                                                modelOrderCustomerList.add(modelOrderCustomer);

                                                orderAdminAdapter.notifyDataSetChanged();
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

                                        //load all the orders
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        databaseReference = firebaseDatabase.getReference().child("AdminOrders").orderByChild("orderStatus").equalTo(selected);


                                        recyclerView = binding.ordersRv;
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardTechniciansActivity.this));

                                        modelOrderCustomerList = new ArrayList<ModelOrderCustomer>();
                                        orderAdminAdapter = new OrderAdminAdapter(DashboardTechniciansActivity.this, modelOrderCustomerList);
                                        recyclerView.setAdapter(orderAdminAdapter);


                                        databaseReference.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                ModelOrderCustomer modelOrderCustomer = snapshot.getValue(ModelOrderCustomer.class);
                                                modelOrderCustomerList.add(modelOrderCustomer);

                                                orderAdminAdapter.notifyDataSetChanged();
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

            //load all the orders if not filtered
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("AdminOrders");


            recyclerView = binding.ordersRv;
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(DashboardTechniciansActivity.this));

            modelOrderCustomerList = new ArrayList<ModelOrderCustomer>();
            orderAdminAdapter = new OrderAdminAdapter(DashboardTechniciansActivity.this, modelOrderCustomerList);
            recyclerView.setAdapter(orderAdminAdapter);


            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    ModelOrderCustomer modelOrderCustomer = snapshot.getValue(ModelOrderCustomer.class);
                    modelOrderCustomerList.add(modelOrderCustomer);

                    orderAdminAdapter.notifyDataSetChanged();
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

            //handle searching for orders

            binding.searchProductEt1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    try {
                        orderAdminAdapter.getFilter().filter(s);
                    }
                    catch (Exception exception){
                        exception.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


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

                        Glide.with(DashboardTechniciansActivity.this)
                                .load(profileImage)
                                .placeholder(R.drawable.profile)
                                .into(binding.profileImage);

                        //load orders


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}
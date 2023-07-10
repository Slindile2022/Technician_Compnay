package com.example.home;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.home.databinding.DialogQuantityBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;


public class CustomerProductAdapter extends RecyclerView.Adapter<CustomerProductAdapter.ViewHolder> implements Filterable {

    Context context;
    List<ModelClass> modelClassesList,filterLists;

    //gonna help us to decrement or increment the quantity value
    private static int counter = 1;
    private String stringVal;

    private String quantity;



    FilterProductsCustomer  filter;

    //getting constructor


    public CustomerProductAdapter(Context context, List<ModelClass> modelClassesList) {
        this.context = context;
        this.modelClassesList =modelClassesList;
        this.filterLists = modelClassesList;
    }

    @NonNull
    @Override
    public CustomerProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_user, parent, false);
        return new CustomerProductAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CustomerProductAdapter.ViewHolder holder, int position) {

        //here we are binding the data

        ModelClass modelClass = modelClassesList.get(position);

        holder.productTitle.setText(modelClass.getProductTitle());
        holder.productDescription.setText(modelClass.getProductDescription());
        
        //setting up image

        String imageUri = null;
        imageUri = modelClass.getProductIconTv();
        Picasso.get().load(imageUri).into(holder.imageView);



        //handling add to cart function
        
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add product to the cart

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_quantity, null);

                // declaring the items for our dialog

                ImageView productIv;
                TextView quantityTv;
                TextView titleTv;
                TextView productDescriptionTv;
                ImageButton incrementBtn, decrementBtn;
                Button continueBtn;

                String timeStamp = modelClass.getTimeStamp();
                String title = modelClass.getProductTitle();
                String productDescription = modelClass.getProductDescription();




                //int layout views
               // productIv = dialogView.findViewById(R.id.productIv);
                titleTv  = dialogView.findViewById(R.id.titleIv);
                incrementBtn  = dialogView.findViewById(R.id.incrementBtn);
                decrementBtn = dialogView.findViewById(R.id.decrementBtn);
                continueBtn = dialogView.findViewById(R.id.continueBtn);
                productDescriptionTv  = dialogView.findViewById(R.id.productDescriptionTv);
                quantityTv = dialogView.findViewById(R.id.quantityTv);

                //assigning values to the variables


                titleTv.setText(modelClass.getProductTitle());
                productDescriptionTv.setText(modelClass.getProductDescription());
                builder.setView(dialogView);
                builder.setCancelable(true);

                //making sure that every time you exit the dialog the counter value goes back to 1
                //counter = 1;

                builder.show();





                //setting up image








                //handle increment button
                incrementBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //increment the quantity value
                        counter++;
                        stringVal = Integer.toString(counter);
                        quantityTv.setText(stringVal);
                        quantity = stringVal;
                    }
                });

                //making sure that every time you exit the dialog the counter value goes back to 1
                counter = 1;
                stringVal = Integer.toString(counter);
                quantityTv.setText(stringVal);
                quantity = stringVal;


                //handle decrement button
                decrementBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //decrement the quantity value

                        if (counter == 1){
                            stringVal = Integer.toString(counter);
                            quantityTv.setText(stringVal);
                            quantity = stringVal;

                        }
                        else {
                            counter--;
                            stringVal = Integer.toString(counter);
                            quantityTv.setText(stringVal);
                            quantity = stringVal;
                        }
                    }
                });

                //making sure that every time you exit the dialog the counter value goes back to 1
                counter = 1;
                stringVal = Integer.toString(counter);
                quantityTv.setText(stringVal);
                quantity = stringVal;


                //handle Add to cart function

                continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //add data to database
                        addToCart(timeStamp, title, productDescription, quantity);

                        //dismiss the dialog










                    }
                });











            }
        });



    }

    private int itemId =1;
    private void addToCart(String timeStamp, String title, String productDescription, String quantity) {

        itemId++;

        EasyDB easyDB = EasyDB.init(context, "ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text", "unique"}))
                .addColumn(new Column("Item_PID", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Description", new String[]{"text", "not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                .doneTableColumn();

        Boolean b = easyDB.addData("Item_Id", itemId)
                .addData("Item_PID",timeStamp)
                .addData("Item_Name",title)
                .addData("Item_Description",productDescription)
                .addData("Item_Quantity",quantity)
                .doneDataAdding();

        Toast.makeText(context, "added to cart", Toast.LENGTH_SHORT).show();

        //updating the cart
        ((DashboardUserActivity)context).cartCount();

    }


    @Override
    public int getItemCount() {
        return modelClassesList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterProductsCustomer(CustomerProductAdapter.this, filterLists);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // declaring the items for our recycle view
        ImageView imageView;
        TextView productTitle, productDescription, addToCart;

        // declaring the items for our dialog
        ImageView productIv;
        TextView quantityTv, titleTv;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.productIconTv);
            productTitle = itemView.findViewById(R.id.titleTv);
            productDescription = itemView.findViewById(R.id.descriptionTv);
            addToCart = itemView.findViewById(R.id.addToCart);

            //int layout views
            productIv = itemView.findViewById(R.id.productIv);
            titleTv  = itemView.findViewById(R.id.titleIv);
            quantityTv  = itemView.findViewById(R.id.quantityTv);


        }
    }
}

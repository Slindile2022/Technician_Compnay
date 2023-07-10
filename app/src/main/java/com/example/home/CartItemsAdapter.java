package com.example.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.HolderCartItem> {

    private Context context;
    private ArrayList<ModelCartItems> cartItems;

    public CartItemsAdapter(Context context, ArrayList<ModelCartItems> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate layout row cart items
        View view = LayoutInflater.from(context).inflate(R.layout.row_cartitem, parent, false);


        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, int position) {

        //get data

        ModelCartItems modelCartItems = cartItems.get(position);

        String id = modelCartItems.getId();
        String title = modelCartItems.getName();
        String productId = modelCartItems.getTimeStamp();
        String productDescription = modelCartItems.getDescription();
        String productQuantity = modelCartItems.getQuantity();

        //set data

        holder.itemTitleTv.setText(""+title);
        holder.itemDescription.setText(""+productDescription);
        holder.itemQuantity.setText(""+productQuantity);
        holder.itemTitleTv.setText(""+title);



        //handle remove click listener, delete from cart

        holder.itemRemoveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //will create table if not exist, but in that case will must exit
                EasyDB easyDB = EasyDB.init(context, "ITEMS_DB")
                        .setTableName("ITEMS_TABLE")
                        .addColumn(new Column("Item_Id", new String[]{"text", "unique"}))
                        .addColumn(new Column("Item_PID", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Name", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Description", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                        .doneTableColumn();

                easyDB.deleteRow(1, id);
                Toast.makeText(context, "item removed...", Toast.LENGTH_SHORT).show();

                //refresh the list
                cartItems.remove(position);
                notifyItemChanged(position);
                notifyDataSetChanged();

                //after removing from cart, update cart count
                ((DashboardUserActivity)context).cartCount();


            }
        });


    }

    @Override
    public int getItemCount() {
        return cartItems.size();//return the number of records
    }

    //view holder class

    class  HolderCartItem extends RecyclerView.ViewHolder{

        //ui views of the row cart items

        private TextView itemTitleTv, itemDescription, itemQuantity, itemRemoveTv;


        public HolderCartItem(@NonNull View itemView) {
            super(itemView);

            //init views

            itemTitleTv = itemView.findViewById(R.id.itemTitleTv);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemRemoveTv = itemView.findViewById(R.id.itemRemoveTv);
        }

    }
}

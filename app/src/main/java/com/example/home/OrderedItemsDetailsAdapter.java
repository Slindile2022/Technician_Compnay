package com.example.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrderedItemsDetailsAdapter extends RecyclerView.Adapter<OrderedItemsDetailsAdapter.HolderOrderedItemsDetails>{

    private Context context;
    private List<ModelOrderedItemsDetails> modelOrderedItemsDetailsList;

    public OrderedItemsDetailsAdapter(Context context, List<ModelOrderedItemsDetails> modelOrderedItemsDetailsList) {
        this.context = context;
        this.modelOrderedItemsDetailsList = modelOrderedItemsDetailsList;
    }

    @NonNull
    @Override
    public HolderOrderedItemsDetails onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate layout

        View view = LayoutInflater.from(context).inflate(R.layout.row_order_details, parent,false);
        return new HolderOrderedItemsDetails(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderedItemsDetails holder, int position) {

        //get data at position

        ModelOrderedItemsDetails modelOrderedItemsDetails = modelOrderedItemsDetailsList.get(position);

        String productName = modelOrderedItemsDetails.getProductName();
        String productDescription = modelOrderedItemsDetails.getProductDescription();
        String quantity = modelOrderedItemsDetails.getProductQuantity();

        holder.productName.setText(productName);
        holder.productDescription.setText(productDescription);
        holder.quantity.setText(quantity);

    }

    @Override
    public int getItemCount() {
        return modelOrderedItemsDetailsList.size();
    }

    //view holder class
    class HolderOrderedItemsDetails extends RecyclerView.ViewHolder{

        //views or row ordered items details
        private TextView productName, productDescription, quantity;

        public HolderOrderedItemsDetails(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.itemTitleTv);
            productDescription = itemView.findViewById(R.id.itemDescription);
            quantity = itemView.findViewById(R.id.itemQuantity);
        }
    }
}

package com.example.home;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.type.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderCustomerAdapter extends RecyclerView.Adapter<OrderCustomerAdapter.HolderOderCustomer> implements Filterable {

     Context context;
     List<ModelOrderCustomer> orderCustomersList, filterList;

     FilterOrdersCustomer filter;

    public OrderCustomerAdapter(Context context, List<ModelOrderCustomer> orderCustomersList) {
        this.context = context;
        this.orderCustomersList = orderCustomersList;
        this.filterList = orderCustomersList;
    }

    @NonNull
    @Override
    public HolderOderCustomer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate view

        View view = LayoutInflater.from(context).inflate(R.layout.row_order_customer, parent, false);
        return new HolderOderCustomer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOderCustomer holder, int position) {
        //get data

        ModelOrderCustomer modelOrderCustomer = orderCustomersList.get(position);



        String orderId = modelOrderCustomer.getOrderId();
        String orderTime = modelOrderCustomer.getOrderTime();
        String orderBy = modelOrderCustomer.getOrderBy();
        String customerPhone = modelOrderCustomer.getCustomerPhone();
        String id = modelOrderCustomer.getId();
        String orderStatus = modelOrderCustomer.getOrderStatus();
        String price = modelOrderCustomer.getPrice();
        String technician = modelOrderCustomer.getTechnician();
        String technicianId = modelOrderCustomer.getTechnicianId();

        //set data

        holder.amountTv.setText("Price : R"+price);
        holder.orderIdTv.setText("Order Id:"+orderId);
        holder.technicianTv.setText("technician : "+technician);
        holder.statusTv.setText(orderStatus);

        //convert timestamp to proper date format

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(orderTime));
        String formatDate = DateFormat.format("dd/MM/yyyy", calendar).toString();

        //convert timestamp to proper date format

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(Long.parseLong(orderTime));
        String formatDate1 = DateFormat.format("HH:mm", calendar).toString();

        //holder for date

        holder.dateTv.setText(formatDate+"("+formatDate1+")");


        //must be able to see the whole details about the order
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open order details, we need  orderId and user id
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("userId", id);
                intent.putExtra("orderStatus", orderStatus);
                context.startActivity(intent);



            }
        });



    }

    @Override
    public int getItemCount() {
        return orderCustomersList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterOrdersCustomer(OrderCustomerAdapter.this,filterList);
        }
        return filter;
    }

    //view holder class

    class HolderOderCustomer extends RecyclerView.ViewHolder{

       // views of layout
        TextView orderIdTv, dateTv, amountTv, statusTv, technicianTv;

        public HolderOderCustomer(@NonNull View itemView) {
            super(itemView);

            orderIdTv = itemView.findViewById(R.id.orderIdTv);
            dateTv =  itemView.findViewById(R.id.dateTv);
            amountTv = itemView.findViewById(R.id.amountTv);
            statusTv = itemView.findViewById(R.id.statusTv);
            technicianTv =  itemView.findViewById(R.id.technicianTv);




        }
    }
}

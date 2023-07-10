package com.example.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> implements Filterable {

    Context context;
    List<ModelClass> modelClassesList, filterList;

    FilterProduct  filter;


    //getting constructor


    public AdminProductAdapter(Context context, List<ModelClass> modelClassesList) {
        this.context = context;
        this.modelClassesList =modelClassesList;
        this.filterList = modelClassesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_technician, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //here we are binding the data

        ModelClass modelClass = modelClassesList.get(position);

        holder.productTitle.setText(modelClass.getProductTitle());
        holder.productDescription.setText(modelClass.getProductDescription());

        //setting up image

        String imageUri = null;
        imageUri = modelClass.getProductIconTv();
        Picasso.get().load(imageUri).into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return modelClassesList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterProduct(this, filterList);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // declaring the items
        ImageView imageView;
        TextView productTitle, productDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.productIconTv);
            productTitle = itemView.findViewById(R.id.titleTv);
            productDescription = itemView.findViewById(R.id.descriptionTv);

        }
    }
}

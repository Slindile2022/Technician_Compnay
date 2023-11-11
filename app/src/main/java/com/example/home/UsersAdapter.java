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

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> implements Filterable {


    Context context;
    List<ModelUsers> modelClassesList;
    List<ModelUsers> filterList;

    FilterUsers filter;


    //getting constructor


    public UsersAdapter(Context context, List<ModelUsers> modelClassesList) {
        this.context = context;
        this.modelClassesList =modelClassesList;
        this.filterList = modelClassesList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //here we are binding the data

        ModelUsers modelUsers = modelClassesList.get(position);
        String profileImageUrl1 = modelUsers.getProfileImage();


        holder.userName.setText(modelUsers.getName());
        holder.userType.setText(modelUsers.getUserType());



        //setting up image

       // String imageUri = null;
      //  imageUri = modelUsers.getProfileImage();
      //  Picasso.get().load(imageUri).into(holder.imageView);

        //set user profile picture

        // Assuming you have a reference to the CircleImageView in your ViewHolder
        CircleImageView profileImage = (CircleImageView) holder.imageView;

        // Get the profile image URL from your PostData object
        String profileImageUrl = profileImageUrl1;

        // Load the profile image into the CircleImageView using Picasso

        try {
            Picasso.get().load(profileImageUrl)
                    .placeholder(R.drawable.profile) // Placeholder image while loading
                    .error(R.drawable.profile) // Error image if loading fails
                    .into(profileImage);
        }catch (Exception e){
            //do nothing
        }





    }

    @Override
    public int getItemCount() {
        return modelClassesList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterUsers(this, filterList);

        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // declaring the items
        ImageView imageView;
        TextView userName, userType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.userIconTv);
            userName = itemView.findViewById(R.id.userNameTv);
            userType = itemView.findViewById(R.id.userTypeTv);

        }
    }

}

package com.example.home;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterUsers extends Filter{

    private UsersAdapter userAdapter;
    private List<ModelUsers> filterList;

    public FilterUsers(UsersAdapter usersAdapter, List<ModelUsers> filterList) {
        this.userAdapter = usersAdapter ;
        this.filterList = filterList;
    }



    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        //validate search

        if (constraint != null && constraint.length() > 0){

            //search field not empty


            //change upper case, to make case insensitive

            constraint = constraint.toString().toUpperCase();

            //store our filtered results

            ArrayList<ModelUsers> filteredUsers = new ArrayList<>();
            for (int i=0;i<filterList.size();i++){

                //check

                if (filterList.get(i).getName().toUpperCase().contains(constraint) ){
                    //add filtered data list to list

                    filteredUsers.add(filterList.get(i));


                }

            }

            filterResults.count = filteredUsers.size();
            filterResults.values = filteredUsers;

        }

        else {

            //search field empty

            filterResults.count = filterList.size();
            filterResults.values = filterList;

        }


        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        userAdapter.modelClassesList = (ArrayList<ModelUsers>) results.values;

        //refresh adapter
        userAdapter.notifyDataSetChanged();


    }


}

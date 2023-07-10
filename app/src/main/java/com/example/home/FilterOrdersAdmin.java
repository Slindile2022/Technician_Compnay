package com.example.home;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterOrdersAdmin extends Filter {

    private OrderAdminAdapter orderAdminAdapter;
    private List<ModelOrderCustomer> filterList;

    public FilterOrdersAdmin(OrderAdminAdapter orderAdminAdapter, List<ModelOrderCustomer> filterList) {
        this.orderAdminAdapter = orderAdminAdapter;
        this.filterList = filterList;
    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults filterResults = new Filter.FilterResults();
        //validate search

        if (constraint != null && constraint.length() > 0){

            //search field not empty


            //change upper case, to make case insensitive

            constraint = constraint.toString().toUpperCase();

            //store our filtered results

            ArrayList<ModelOrderCustomer> filteredOrdersAdmin = new ArrayList<>();
            for (int i=0;i<filterList.size();i++){

                //check

                if (filterList.get(i).orderId.toUpperCase().contains(constraint) || filterList.get(i).getOrderBy().toUpperCase().contains(constraint)){
                    //add filtered data list to list

                    filteredOrdersAdmin.add(filterList.get(i));


                }

            }

            filterResults.count = filteredOrdersAdmin.size();
            filterResults.values = filteredOrdersAdmin;

        }

        else {

            //search field empty

            filterResults.count = filterList.size();
            filterResults.values = filterList;

        }


        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {

        orderAdminAdapter.orderCustomersList = (ArrayList<ModelOrderCustomer>) results.values;

        //refresh adapter
        orderAdminAdapter.notifyDataSetChanged();


    }
}

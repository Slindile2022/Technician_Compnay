package com.example.home;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterOrdersCustomer extends Filter {

    private OrderCustomerAdapter orderCustomerAdapter;
    private List<ModelOrderCustomer> filterList;

    public FilterOrdersCustomer(OrderCustomerAdapter orderCustomerAdapter, List<ModelOrderCustomer> filterList) {
        this.orderCustomerAdapter = orderCustomerAdapter;
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

            ArrayList<ModelOrderCustomer> filteredOrdersCustomer = new ArrayList<>();
            for (int i=0;i<filterList.size();i++){

                //check

                if (filterList.get(i).orderId.toUpperCase().contains(constraint) ){
                    //add filtered data list to list

                    filteredOrdersCustomer.add(filterList.get(i));


                }

            }

            filterResults.count = filteredOrdersCustomer.size();
            filterResults.values = filteredOrdersCustomer;

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

        orderCustomerAdapter.orderCustomersList = (ArrayList<ModelOrderCustomer>) results.values;

        //refresh adapter
        orderCustomerAdapter.notifyDataSetChanged();

    }
}

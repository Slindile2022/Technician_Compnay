package com.example.home;

import android.widget.Filter;

import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.List;

public class FilterProductsCustomer extends Filter {
    private CustomerProductAdapter  customerProductAdapter;
    private List<ModelClass> filterList;

    public FilterProductsCustomer(CustomerProductAdapter customerProductAdapter, List<ModelClass> filterList) {
        this.customerProductAdapter = customerProductAdapter ;
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

            ArrayList<ModelClass> filteredProductCustomer = new ArrayList<>();
            for (int i=0;i<filterList.size();i++){

                //check

                if (filterList.get(i).getProductTitle().toUpperCase().contains(constraint) || filterList.get(i).getProductDescription().toUpperCase().contains(constraint)){
                    //add filtered data list to list

                    filteredProductCustomer.add(filterList.get(i));


                }

            }

            filterResults.count = filteredProductCustomer.size();
            filterResults.values = filteredProductCustomer;

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

        customerProductAdapter.modelClassesList = (ArrayList<ModelClass>) results.values;

        //refresh adapter
        customerProductAdapter.notifyDataSetChanged();


    }
}

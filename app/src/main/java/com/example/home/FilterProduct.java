package com.example.home;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterProduct extends Filter {

    private AdminProductAdapter  adminProductAdapter;
    private List<ModelClass> filterList;

    public FilterProduct(AdminProductAdapter adminProductAdapter, List<ModelClass> filterList) {
        this.adminProductAdapter = adminProductAdapter;
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

            ArrayList<ModelClass> filteredProduct = new ArrayList<>();
            for (int i=0;i<filterList.size();i++){

                //check

                if (filterList.get(i).getProductTitle().toUpperCase().contains(constraint) || filterList.get(i).getProductDescription().toUpperCase().contains(constraint)){
                    //add filtered data list to list

                    filteredProduct.add(filterList.get(i));


                }

            }

            filterResults.count = filteredProduct.size();
            filterResults.values = filteredProduct;

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

        adminProductAdapter.modelClassesList = (ArrayList<ModelClass>) results.values;

        //refresh adapter
        adminProductAdapter.notifyDataSetChanged();


    }
}

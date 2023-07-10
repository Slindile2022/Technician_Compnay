package com.example.home;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterProductsTechnician extends Filter {
    private TechnicianProductAdapter technicianProductAdapter;
    private List<ModelClass> filterList;

    public FilterProductsTechnician(TechnicianProductAdapter technicianProductAdapter, List<ModelClass> filterList) {
        this.technicianProductAdapter = technicianProductAdapter ;
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

            ArrayList<ModelClass> filteredProductTechnician = new ArrayList<>();
            for (int i=0;i<filterList.size();i++){

                //check

                if (filterList.get(i).getProductTitle().toUpperCase().contains(constraint) || filterList.get(i).getProductDescription().toUpperCase().contains(constraint)){
                    //add filtered data list to list

                    filteredProductTechnician.add(filterList.get(i));


                }

            }

            filterResults.count = filteredProductTechnician.size();
            filterResults.values = filteredProductTechnician;

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

        technicianProductAdapter.modelClassesList = (ArrayList<ModelClass>) results.values;

        //refresh adapter
        technicianProductAdapter.notifyDataSetChanged();


    }
}

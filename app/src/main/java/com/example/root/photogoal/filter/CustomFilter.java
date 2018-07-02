package com.example.root.photogoal.filter;

import android.widget.Filter;

import com.example.root.photogoal.adapter.GetCountryAdapter;
import com.example.root.photogoal.response.GetCountryResponse;

import java.util.ArrayList;
import java.util.List;

public class CustomFilter extends Filter {

    GetCountryAdapter adapter;
    List<GetCountryResponse.Country> filterList;

    public CustomFilter(List<GetCountryResponse.Country> filterList, GetCountryAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            List<GetCountryResponse.Country> filteredCountries = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getFullname().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredCountries.add(filterList.get(i));
                }
            }

            results.count=filteredCountries.size();
            results.values=filteredCountries;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.countries = (List<GetCountryResponse.Country>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }

}

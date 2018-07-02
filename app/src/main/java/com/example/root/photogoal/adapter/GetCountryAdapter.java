package com.example.root.photogoal.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.photogoal.Application.PhotoGoalApplication;
import com.example.root.photogoal.R;
import com.example.root.photogoal.filter.CustomFilter;
import com.example.root.photogoal.response.GetCountryResponse;
import com.example.root.photogoal.ui.ChoseTemplateActivity;
import com.example.root.photogoal.ui.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GetCountryAdapter extends RecyclerView.Adapter<GetCountryAdapter.ViewHolder> implements Filterable{
    public List<GetCountryResponse.Country> countries, filterList;

    CustomFilter filter;

    public GetCountryAdapter(List<GetCountryResponse.Country> countries) {
        this.countries = countries;
        this.filterList = countries;

    }

    @NonNull
    @Override
    public GetCountryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_country, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GetCountryAdapter.ViewHolder viewHolder, int i) {
        viewHolder.country_name.setText(countries.get(i).getFullname());
        viewHolder.country_code.setText(countries.get(i).getName());
        viewHolder.flag.setText(countries.get(i).getPicture());
        Picasso.get().load(countries.get(i).getPicture()).resize(200, 200).into(viewHolder.country_flag);

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null)
        {
            filter = new CustomFilter(filterList, this);
        }

        return filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView country_name;
        private TextView country_code;
        private TextView flag;
        private ImageView country_flag;
        public ViewHolder(final View view) {
            super(view);
            country_name = view.findViewById(R.id.country_name);
            country_flag = view.findViewById(R.id.country_flag);
            country_code = view.findViewById(R.id.country_code);
            flag = view.findViewById(R.id.flag);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = ChoseTemplateActivity.GetCountry.newIntent(view.getContext(), country_code.getText()
                            .toString(), flag.getText().toString());
                    view.getContext().startActivity(intent);

                }
            });

        }
    }
}

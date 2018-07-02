package com.example.root.photogoal.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.example.root.photogoal.Application.PhotoGoalApplication;
import com.example.root.photogoal.R;
import com.example.root.photogoal.adapter.GetCountryAdapter;
import com.example.root.photogoal.response.GetCountryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCountryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<GetCountryResponse.Country> countries;
    private GetCountryAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getcountry);
        searchView = findViewById(R.id.Search);
        searchView.setQueryHint("Find your favorite team");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });

        recyclerView = findViewById(R.id.country_RecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ((PhotoGoalApplication) getApplication()).provideGetCountryService()
                .getCountry()
                .enqueue(new Callback<GetCountryResponse>() {
                    @Override
                    public void onResponse(Call<GetCountryResponse> call, Response<GetCountryResponse> response) {
                        if (response.isSuccessful()) {
                            GetCountryResponse body = response.body();
                            if (body!= null) {
                                countries = body.getCountries();
                                adapter = new GetCountryAdapter(countries);
                                recyclerView.setAdapter(adapter);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<GetCountryResponse> call, Throwable t) {

                    }
                });
    }

}

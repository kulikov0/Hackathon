package com.example.root.photogoal.api;

import com.example.root.photogoal.response.GetCountryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetCountryService {

    @GET("get_teams/")
    Call<GetCountryResponse> getCountry();

}

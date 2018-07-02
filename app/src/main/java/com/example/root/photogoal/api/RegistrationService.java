package com.example.root.photogoal.api;

import com.example.root.photogoal.response.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistrationService {

    @POST("register/")
    Call<RegistrationResponse> register(@Body RegistrationBody body);
}

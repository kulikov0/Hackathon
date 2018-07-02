package com.example.root.photogoal.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SendCountryResultService {

    @POST("set_team/")
    Call<ResponseBody> country(@Body SendCountryBody body);
}

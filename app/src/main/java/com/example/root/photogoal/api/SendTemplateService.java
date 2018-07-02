package com.example.root.photogoal.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SendTemplateService {

    @POST("set_mask/")
    Call<ResponseBody> template(@Body SendTemplateBody body);
}

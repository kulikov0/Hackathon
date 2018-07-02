package com.example.root.photogoal.api;

import com.example.root.photogoal.response.UploadImageResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UploadImageService {

    @POST("send_photo/")
    @Multipart
    Call<UploadImageResponse> sendPhoto(@Part MultipartBody.Part image,
                                        @Part("file")RequestBody name,
                                        @Query("user_id") String user_id,
                                        @Query("session") String session_id);
}

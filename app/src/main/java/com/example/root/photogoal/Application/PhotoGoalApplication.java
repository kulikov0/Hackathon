package com.example.root.photogoal.Application;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.root.photogoal.api.GetCountryService;
import com.example.root.photogoal.api.HttpLoggingInterceptor;
import com.example.root.photogoal.api.RegistrationService;
import com.example.root.photogoal.api.SendCountryResultService;
import com.example.root.photogoal.api.SendTemplateService;
import com.example.root.photogoal.api.UploadImageService;
import com.example.root.photogoal.response.RegistrationResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoGoalApplication extends Application {

    @Nullable private volatile Retrofit retrofit = null;

    @Nullable private volatile RegistrationService registrationService = null;

    @Nullable private volatile UploadImageService uploadImageService = null;

    @Nullable private volatile GetCountryService getCountryService = null;

    @Nullable private volatile SendCountryResultService sendCountryResultService = null;

    @Nullable private volatile SendTemplateService sendTemplateService = null;

    @NonNull private final Object retrofitLock = new Object();

    @NonNull private final Object registrationServiceLock = new Object();

    @NonNull private final Object uploadImageServiceLock = new Object();

    @NonNull private final Object getCountryServiceLock = new Object();

    @NonNull private final Object sendCountryResultLock = new Object();

    @NonNull private final Object sendTemplateServiceLock = new Object();



    @NonNull private Retrofit provideRetrofit() {
        Retrofit localInstance = retrofit;
        if (localInstance == null) {
            synchronized (retrofitLock) {
                localInstance = retrofit;
                if (localInstance == null) {
                    localInstance = retrofit = buildRetrofit();
                }
            }
        }

        assert (localInstance != null);

        return localInstance;
    }

    @NonNull private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://photogoal.cf/api/")
                .client(buildClient())
                .validateEagerly(true)
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .build();
    }

    @NonNull private OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cache(new Cache(getDir("okhttp-cache", Context.MODE_PRIVATE), Long.MAX_VALUE))
                .connectionPool(new ConnectionPool(1, 1, TimeUnit.SECONDS))
                .build();
    }



    @NonNull public RegistrationService provideRegistrationService() {
        RegistrationService localInstance = registrationService;
        if (localInstance == null) {
            synchronized (registrationServiceLock) {
                localInstance = registrationService;
                if (localInstance == null) {
                    localInstance = registrationService = provideRetrofit().create(RegistrationService.class);
                }
            }
        }

        assert (localInstance != null);

        return localInstance;
    }

    @NonNull public UploadImageService provideUploadImageService() {
        UploadImageService localInstance = uploadImageService;
        if (localInstance == null) {
            synchronized (uploadImageServiceLock) {
                localInstance = uploadImageService;
                if (localInstance == null) {
                    localInstance = uploadImageService = provideRetrofit().create(UploadImageService.class);
                }
            }
        }

        assert (localInstance != null);
        return localInstance;
    }

    @NonNull public SendCountryResultService provideSendCountryResult() {
        SendCountryResultService localInstance = sendCountryResultService;
        if (localInstance == null) {
            synchronized (sendCountryResultLock) {
                localInstance = sendCountryResultService;
                if (localInstance == null) {
                    localInstance = sendCountryResultService = provideRetrofit().create(SendCountryResultService.class);
                }
            }
        }

        assert (localInstance != null);

        return localInstance;
    }

    @NonNull public GetCountryService provideGetCountryService() {
        GetCountryService localInstance = getCountryService;
        if (localInstance == null) {
            synchronized (getCountryServiceLock) {
                localInstance = getCountryService;
                if (localInstance == null) {
                    localInstance = getCountryService = provideRetrofit().create(GetCountryService.class);
                }
            }
        }

        assert (localInstance != null);

        return localInstance;
    }

    @NonNull public SendTemplateService provideSendTemplateService() {
        SendTemplateService localInstance = sendTemplateService;
        if (localInstance == null) {
            synchronized (sendTemplateServiceLock) {
                localInstance = sendTemplateService;
                if (localInstance == null) {
                    localInstance = sendTemplateService = provideRetrofit().create(SendTemplateService.class);
                }
            }
        }

        assert  (localInstance != null);

        return localInstance;
    }

    @NonNull private Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();

        builder = RegistrationResponse.Status.apply(builder);

        return builder.create();
    }
}

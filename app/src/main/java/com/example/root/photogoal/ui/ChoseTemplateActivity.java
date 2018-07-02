package com.example.root.photogoal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.root.photogoal.Application.PhotoGoalApplication;
import com.example.root.photogoal.R;
import com.example.root.photogoal.api.SendCountryBody;
import com.example.root.photogoal.api.SendTemplateBody;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoseTemplateActivity extends AppCompatActivity {

    String USER_DATA = "user_data";

    ImageView flag1;
    ImageView flag2;
    ImageView flag3;

    CardView zero;
    CardView one;
    CardView two;


    public static class GetCountry {
        private static final String Country = "country";
        private static final String Flag = "flag";
        public static Intent newIntent(Context context, String country, String flag) {
            Intent intent = new Intent(context, ChoseTemplateActivity.class);
            intent.putExtra(Country, country);
            intent.putExtra(Flag, flag);
            return intent;
        }
    }

    private String country;
    private String flag;
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);
        Intent in = getIntent();
        if (in != null) {
            country = in.getStringExtra(GetCountry.Country);
            flag = in.getStringExtra(GetCountry.Flag);
        } else if (savedInstanceState != null) {
            country = savedInstanceState.getString(GetCountry.Country);
            flag = savedInstanceState.getString(GetCountry.Flag);
        }

        final String user_id = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).getString("id", "");
        final String session = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).getString("session", "");

        zero = findViewById(R.id.zero);
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer mask = 0;
                ((PhotoGoalApplication) getApplication()).provideSendTemplateService()
                        .template(new SendTemplateBody(user_id, session, mask))
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    startActivity(new Intent(ChoseTemplateActivity.this, MainActivity.class));

                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
            }
        });

        one = findViewById(R.id.one);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer mask = 1;
                ((PhotoGoalApplication) getApplication()).provideSendTemplateService()
                        .template(new SendTemplateBody(user_id, session, mask))
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    startActivity(new Intent(ChoseTemplateActivity.this, MainActivity.class));

                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
            }
        });

        two = findViewById(R.id.two);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer mask = 2;
                ((PhotoGoalApplication) getApplication()).provideSendTemplateService()
                        .template(new SendTemplateBody(user_id, session, mask))
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    startActivity(new Intent(ChoseTemplateActivity.this, MainActivity.class));

                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

            }
        });

        flag1 = findViewById(R.id.flag1);
        flag2 = findViewById(R.id.flag2);
        flag3 = findViewById(R.id.flag3);
        Picasso.get().load(flag).resize(23, 23).into(flag1);
        Picasso.get().load(flag).resize(23, 23).into(flag2);
        Picasso.get().load(flag).resize(250, 250).into(flag3);

        String team = country;
        ((PhotoGoalApplication)getApplication()).provideSendCountryResult()
                .country(new SendCountryBody(user_id, session, team))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }


}

package com.example.root.photogoal.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.root.photogoal.Application.PhotoGoalApplication;
import com.example.root.photogoal.R;
import com.example.root.photogoal.api.RegistrationBody;
import com.example.root.photogoal.response.RegistrationResponse;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;
    private final String MY_SETTINGS = "my_settings";
    private VideoView bgVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Twitter.initialize(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        bgVideo = findViewById(R.id.bgVideoView);
        bgVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("ayuXpMk9MFG2deV8iMTImP9mE", "ks3C2trq5uVMqXIAviiuFjlTTGRUUVnsdQI29cHrVjr24BWSmZ"))
                .debug(true)
                .build();
        Twitter.initialize(config);


        Twitter.initialize(config);
        loginButton = findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                Log.e("secret: ", secret);
                ((PhotoGoalApplication) getApplication()).provideRegistrationService()
                        .register(new RegistrationBody(new RegistrationBody.userRegistration(token, secret)))
                        .enqueue(new retrofit2.Callback<RegistrationResponse>() {
                            @Override
                            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                                if (response.isSuccessful()) {
                                    RegistrationResponse body = response.body();
                                    String USER_DATA = "user_data";
                                    getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                                            .putString("id", body.getUser())
                                            .putString("session", body.getSession())
                                            .commit();
                                    getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE).edit().putString("logged", "yes").commit();
                                    startActivity(new Intent(LoginActivity.this, GetCountryActivity.class));

                                }

                            }

                            @Override
                            public void onFailure(Call<RegistrationResponse> call, Throwable t) {

                            }
                        });

            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);
        bgVideo.setVideoURI(uri);
        bgVideo.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        bgVideo.stopPlayback();
    }
}

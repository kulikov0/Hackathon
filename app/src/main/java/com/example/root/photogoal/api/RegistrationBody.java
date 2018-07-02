package com.example.root.photogoal.api;

import com.google.gson.annotations.SerializedName;

public class RegistrationBody {

    @SerializedName("register") userRegistration registration;

    public RegistrationBody(userRegistration registration) {
        this.registration = registration;
    }

    public static class userRegistration {
        @SerializedName("token") String token;
        @SerializedName("secret") String secret;

        public userRegistration(String token, String secret) {
            this.token = token;
            this.secret = secret;
        }
     }
}

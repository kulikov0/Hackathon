package com.example.root.photogoal.api;

import com.google.gson.annotations.SerializedName;

public class SendCountryBody {

    @SerializedName("user_id") String user_id;
    @SerializedName("session") String session;
    @SerializedName("team") String team;

    public SendCountryBody(String user_id, String session, String team) {
        this.user_id = user_id;
        this.session = session;
        this.team = team;
    }
}

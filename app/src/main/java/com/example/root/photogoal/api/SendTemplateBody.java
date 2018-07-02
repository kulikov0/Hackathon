package com.example.root.photogoal.api;

import com.google.gson.annotations.SerializedName;

public class SendTemplateBody {

    @SerializedName("user_id") String user_id;
    @SerializedName("session") String session;
    @SerializedName("mask") Integer mask;

    public SendTemplateBody(String user_id, String session, Integer mask) {
        this.user_id = user_id;
        this.session = session;
        this.mask = mask;
    }
}

package com.example.root.photogoal.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class UploadImageResponse {

    @SerializedName("status") String status;
    @SerializedName("link") String link;

    public UploadImageResponse(String status, String link) {
        this.status = status;
        this.link = link;
    }

    @NonNull public String getLink() {
        return link;
    }
}

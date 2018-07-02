package com.example.root.photogoal.response;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCountryResponse {

    @SerializedName("countries") List<Country> countries;

    public GetCountryResponse(@Nullable List<Country> countries) {

        this.countries = countries;
    }

    public static class Country {
        @SerializedName("name") String name;
        @SerializedName("pic") String picture;
        @SerializedName("full") String fullname;

        public Country(String name, String picture, String fullname) {
            this.name = name;
            this.picture = picture;
            this.fullname = fullname;
        }

        @NonNull public String getName() {
            return name;
        }

        @NonNull public String getFullname() {
            return fullname;
        }

        @NonNull public String getPicture() {
            return picture;
        }
    }

    @NonNull public List<Country> getCountries() {
        return countries;
    }

}

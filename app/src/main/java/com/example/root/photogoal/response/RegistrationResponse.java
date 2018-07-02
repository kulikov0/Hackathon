package com.example.root.photogoal.response;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RegistrationResponse {
    public enum Status {
        SUCCESS("ok"),
        FAILURE("error");

        @NonNull
        private static final Map<String, Status> enumMap = new HashMap<>();

        static { for (Status status: Status.values()) { enumMap.put(status.value, status); } }

        @NonNull public static Status parse(String value) { return enumMap.get(value); }

        @NonNull public static GsonBuilder apply(GsonBuilder builder) { return builder.registerTypeAdapter(Status.class, new Serializer()).registerTypeAdapter(Status.class, new Deserializer()); }

        @NonNull private final String value;

        Status(@NonNull String value) {
            this.value = value;
        }

        private static class Serializer implements JsonSerializer<Status> {

            @Override
            public JsonElement serialize(Status src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.value);
            }
        }

        private static class Deserializer implements JsonDeserializer<Status> {

            @Override
            public Status deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return Status.parse(json.getAsString());
            }
        }
    }

    @SerializedName("status") private final Status status;
    @SerializedName("user_id") String user;
    @SerializedName("session_id") String session;

    public RegistrationResponse(@NonNull Status status, String user, String session) {
        this.status = status;
        this.user = user;
        this.session = session;

    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    public String getUser() {
        return user;
    }

    public String getSession() {
        return session;
    }
}



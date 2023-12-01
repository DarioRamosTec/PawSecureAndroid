package com.example.pawsecure.response;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class GeneralResponse {
    @SerializedName("msg")
    public String message;

    @SerializedName("data")
    public JSONObject data;

    @SerializedName("errors")
    public ValidationEmailResponse errors;
}

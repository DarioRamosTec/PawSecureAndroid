package com.example.pawsecure.response;

import com.example.pawsecure.implementation.PawSecureResponse;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class GeneralResponse extends PawSecureResponse {
    @SerializedName("msg")
    public String message;

    @SerializedName("data")
    public JSONObject data;

    @SerializedName("errors")
    public ValidationResponse errors;
}

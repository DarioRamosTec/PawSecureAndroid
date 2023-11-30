package com.example.pawsecure.response;

import com.example.pawsecure.model.ErrorValidation;
import com.example.pawsecure.model.User;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.Dictionary;
import java.util.List;
import java.util.Objects;

public class GeneralResponse {
    @SerializedName("msg")
    public String message;

    @SerializedName("data")
    public JSONObject data;

    @SerializedName("errors")
    public ErrorValidation errors;
}

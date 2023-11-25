package com.example.pawsecure.response;

import com.example.pawsecure.model.ErrorValidation;
import com.google.gson.annotations.SerializedName;

import java.util.Dictionary;
import java.util.List;

public class GeneralResponse {
    @SerializedName("msg")
    public String message;

    @SerializedName("data")
    public String[] data;

    @SerializedName("errors")
    public ErrorValidation errors;
}

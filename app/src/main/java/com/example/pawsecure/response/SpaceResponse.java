package com.example.pawsecure.response;

import com.example.pawsecure.implementation.PawSecureResponse;
import com.example.pawsecure.model.Space;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

public class SpaceResponse extends PawSecureResponse {
    @SerializedName("msg")
    public String message;

    @SerializedName("data")
    public List<Space> data;
}

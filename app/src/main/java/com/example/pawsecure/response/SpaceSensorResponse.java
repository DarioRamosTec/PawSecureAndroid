package com.example.pawsecure.response;

import com.example.pawsecure.implementation.PawSecureResponse;
import com.example.pawsecure.model.Sensor;
import com.example.pawsecure.model.Space;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class SpaceSensorResponse extends PawSecureResponse {
    @SerializedName("msg")
    public String message;

    @SerializedName("data")
    public List<Map<String, List<Sensor>>> data;
}

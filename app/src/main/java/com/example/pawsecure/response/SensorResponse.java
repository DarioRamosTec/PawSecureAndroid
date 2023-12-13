package com.example.pawsecure.response;

import com.example.pawsecure.implementation.PawSecureResponse;
import com.example.pawsecure.model.Sensor;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class SensorResponse extends PawSecureResponse {
    @SerializedName("msg")
    public String message;

    @SerializedName("data")
    public Sensor data;
}

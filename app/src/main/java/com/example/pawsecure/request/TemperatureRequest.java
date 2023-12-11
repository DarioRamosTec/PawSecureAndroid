package com.example.pawsecure.request;

import com.example.pawsecure.model.Temperature;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TemperatureRequest {
    @GET("endpoint")
    Call<Temperature> getData();
}

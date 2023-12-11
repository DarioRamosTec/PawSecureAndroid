package com.example.pawsecure.request;


import com.example.pawsecure.model.Humidity;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HumidityRequest {
    @GET("endpoint")
    Call<Humidity> getData();
}

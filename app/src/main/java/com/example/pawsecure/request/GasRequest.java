package com.example.pawsecure.request;

import com.example.pawsecure.model.Gas;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GasRequest {
    @GET("endpoint")
    Call<Gas> getData();
}

package com.example.pawsecure.request;


import com.example.pawsecure.model.Position;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PositionRequest {
    @GET("endpoint")
    Call<Position> getData();
}

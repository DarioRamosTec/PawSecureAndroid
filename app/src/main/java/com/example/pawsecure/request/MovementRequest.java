package com.example.pawsecure.request;
import com.example.pawsecure.model.Movement;

import retrofit2.Call;
import retrofit2.http.GET;
public interface MovementRequest {
    @GET("endpoint")
    Call<Movement> getData();

}

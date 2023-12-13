package com.example.pawsecure.request;

import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.SpaceSensorResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface SensorRequest {
    @GET("auth/space/{id}/sensors")
    Call<SpaceSensorResponse> index (@Header("Authorization") String authorization,
                                     @Path("id") int id);
}

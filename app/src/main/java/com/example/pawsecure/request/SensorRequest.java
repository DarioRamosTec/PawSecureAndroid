package com.example.pawsecure.request;

import com.example.pawsecure.response.SensorResponse;
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

    @GET("auth/space/{id}/'space/{id}/position'")
    Call<SensorResponse> position (@Header("Authorization") String authorization,
                                   @Path("id") int id);

    @GET("auth/space/{id}/'space/{id}/motion'")
    Call<SensorResponse> motion (@Header("Authorization") String authorization,
                                 @Path("id") int id);

    @GET("auth/space/{id}/sensor/{sensor}")
    Call<SensorResponse> sensor (@Header("Authorization") String authorization,
                                 @Path("id") int id,
                                 @Path("sensor") String sensor);
}

package com.example.pawsecure.request;

import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SpaceRequest {
    @FormUrlEncoded
    @POST("auth/space")
    Call<SpaceResponse> store (@Header("Authorization") String authorization,
                               @Field("name") String name,
                               @Field("description") String description);
}

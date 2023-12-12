package com.example.pawsecure.request;

import com.example.pawsecure.response.SpaceResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PetSpaceRequest {
    @FormUrlEncoded
    @POST("auth/spaces/{id}")
    Call<SpaceResponse> store (@Header("Authorization") String authorization,
                               @Path("id") int id,
                               @Field("pets") int[] pets);
}

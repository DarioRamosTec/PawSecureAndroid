package com.example.pawsecure.request;

import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SpaceRequest {
    @FormUrlEncoded
    @POST("auth/space")
    Call<SpaceResponse> store (@Header("Authorization") String authorization,
                               @Field("name") String name,
                               @Field("description") String description);

    @GET("auth/space/{id}")
    Call<SpaceResponse> index (@Header("Authorization") String authorization,
                               @Path("id") int id);

    @FormUrlEncoded
    @POST("auth/space/{id}/link")
    Call<SpaceResponse> link (@Header("Authorization") String authorization,
                              @Path("id") int id,
                              @Field("mac") String mac);
}

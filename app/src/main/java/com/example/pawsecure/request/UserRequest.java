package com.example.pawsecure.request;

import com.example.pawsecure.response.GeneralResponse;
import com.example.pawsecure.response.LangResponse;
import com.example.pawsecure.response.PetResponse;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserRequest {
    @FormUrlEncoded
    @POST("register")
    Call<GeneralResponse> register (@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("password") String password,
                                   @Field("password_again") String password_again,
                                   @Field("lang") String lang);
    @FormUrlEncoded
    @POST("auth/login")
    Call<TokenResponse> login (@Field("email") String email,
                               @Field("password") String password);

    @POST("auth/refresh")
    Call<TokenResponse> refresh(@Header("Authorization") String authorization);

    @GET("auth/spaces")
    Call<SpaceResponse> spaces(@Header("Authorization") String authorization);

    @GET("auth/pets")
    Call<PetResponse> pets(@Header("Authorization") String authorization);

    @GET("auth/lang")
    Call<LangResponse> lang(@Header("Authorization") String authorization);

    @GET("auth/me")
    Call<UserResponse> index(@Header("Authorization") String authorization);

    @POST("auth/logout")
    Call<UserResponse> logout(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("auth/me")
    Call<UserResponse> update(@Header("Authorization") String authorization,
                              @Field("name") String name,
                              @Field("middle_name") String middle_name,
                              @Field("last_name") String last_name);

}

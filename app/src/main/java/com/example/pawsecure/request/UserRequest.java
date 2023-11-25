package com.example.pawsecure.request;

import com.example.pawsecure.response.GeneralResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserRequest {
    @FormUrlEncoded
    @POST("register")
    Call<GeneralResponse> register(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("password") String password,
                                   @Field("password_again") String password_again,
                                   @Field("lang") String lang);
}

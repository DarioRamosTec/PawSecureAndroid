package com.example.pawsecure.request;

import com.example.pawsecure.response.GeneralResponse;
import com.example.pawsecure.response.PetResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PetRequest {
    @FormUrlEncoded
    @POST("auth/pet")
    Call<PetResponse> pet (@Header("Authorization") String authorization,
                           @Field("name") String name,
                           @Field("race") String race,
                           @Field("sex") String sex,
                           @Field("icon") int icon,
                           @Field("image") String image,
                           @Field("animal") int animal,
                           @Field("birthday") String birthday,
                           @Field("description") String description);
}

package com.example.pawsecure.request;

import com.example.pawsecure.model.Sound;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SoundRequest {
    @GET("endpoint")
    Call<Sound> getData();
}

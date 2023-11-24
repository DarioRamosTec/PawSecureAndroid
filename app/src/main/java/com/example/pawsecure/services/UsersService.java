package com.example.pawsecure.services;

import com.example.pawsecure.models.Message;

import retrofit2.Call;
import retrofit2.http.POST;

public interface UsersService {
    @POST("register")
    Call<Message> register();
}

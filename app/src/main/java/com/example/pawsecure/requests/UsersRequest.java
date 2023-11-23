package com.example.pawsecure.requests;

import com.example.pawsecure.models.Message;
import com.example.pawsecure.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersRequest {
    @POST("register")
    Call<Message> register();
}

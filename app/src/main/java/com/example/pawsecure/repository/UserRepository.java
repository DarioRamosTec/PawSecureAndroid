package com.example.pawsecure.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pawsecure.request.UserRequest;
import com.example.pawsecure.response.GeneralResponse;
import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.retrofit.RetrofitRequest;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserRequest userRequest;

    public UserRepository() {
        userRequest = RetrofitRequest.obtainRetrofit().create(UserRequest.class);
    }

    public LiveData<GeneralResponse> signUp(String name, String email, String password, String password_again, String lang) {
        MutableLiveData<GeneralResponse> mutableLiveData = new MutableLiveData<>();
        userRequest.register(name, email, password, password_again, lang).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (!response.isSuccessful()) {
                    try {
                        GeneralResponse generalResponse = new Gson().fromJson(response.errorBody().string(), GeneralResponse.class);
                        mutableLiveData.setValue(generalResponse);
                    } catch (IOException ignored) {

                    }
                } else {
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<TokenResponse> login (String email, String password) {
        MutableLiveData<TokenResponse> mutableLiveData = new MutableLiveData<>();
        userRequest.login(email, password).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        TokenResponse tokenResponse = new Gson().fromJson(response.errorBody().string(), TokenResponse.class);
                        mutableLiveData.setValue(tokenResponse);
                    } catch (Exception ignored) {

                    }
                } else {
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }
}

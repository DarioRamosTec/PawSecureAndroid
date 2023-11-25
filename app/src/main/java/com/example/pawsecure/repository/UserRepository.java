package com.example.pawsecure.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pawsecure.request.UserRequest;
import com.example.pawsecure.response.GeneralResponse;
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

    public LiveData<GeneralResponse> getDataToSignUp (String name, String email, String password, String password_again, String lang) {
        MutableLiveData<GeneralResponse> mutableLiveData = new MutableLiveData<>();
        userRequest.register(name, email, password, password_again, lang).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (!response.isSuccessful()) {
                    try {
                        GeneralResponse generalResponse = new Gson().fromJson(response.errorBody().string(), GeneralResponse.class);
                        mutableLiveData.setValue(generalResponse);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
}

package com.example.pawsecure.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pawsecure.request.UserRequest;
import com.example.pawsecure.response.GeneralResponse;
import com.example.pawsecure.response.SpaceResponse;
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
                switch (response.code()) {
                    case 400:
                        GeneralResponse generalResponse = null;
                        try {
                            generalResponse = new Gson().fromJson(response.errorBody().string(), GeneralResponse.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        mutableLiveData.setValue(generalResponse);
                        break;
                    case 202:
                        mutableLiveData.setValue(response.body());
                        break;
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
                switch (response.code()) {
                    case 403:
                    case 401:
                        TokenResponse tokenResponse = null;
                        try {
                            tokenResponse = new Gson().fromJson(response.errorBody().string(), TokenResponse.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        mutableLiveData.setValue(tokenResponse);
                        break;
                    case 200:
                        mutableLiveData.setValue(response.body());
                        break;
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<TokenResponse> refresh (String headerAuth) {
        MutableLiveData<TokenResponse> mutableLiveData = new MutableLiveData<>();
        userRequest.refresh(headerAuth).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                switch (response.code()) {
                    case 401:
                        TokenResponse tokenResponse = null;
                        try {
                            tokenResponse = new Gson().fromJson(response.errorBody().string(), TokenResponse.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        mutableLiveData.setValue(tokenResponse);
                        break;
                    case 200:
                        mutableLiveData.setValue(response.body());
                        break;
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<SpaceResponse> spaces (String headerAuth) {
        MutableLiveData<SpaceResponse> mutableLiveData = new MutableLiveData<>();
        userRequest.spaces(headerAuth).enqueue(new Callback<SpaceResponse>() {
            @Override
            public void onResponse(Call<SpaceResponse> call, Response<SpaceResponse> response) {
                SpaceResponse spaceResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        spaceResponse = new SpaceResponse();
                        spaceResponse.code = String.valueOf(response.code());
                        break;
                    case 400:
                        try {
                            spaceResponse = new Gson().fromJson(response.errorBody().string(), SpaceResponse.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        spaceResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(spaceResponse);
                        break;
                    case 200:
                        spaceResponse = response.body();
                        if (spaceResponse != null) {
                            spaceResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(spaceResponse);
                        break;
                }
            }

            @Override
            public void onFailure(Call<SpaceResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }
}

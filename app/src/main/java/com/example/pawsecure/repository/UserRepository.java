package com.example.pawsecure.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pawsecure.model.Pet;
import com.example.pawsecure.request.PetRequest;
import com.example.pawsecure.request.UserRequest;
import com.example.pawsecure.response.GeneralResponse;
import com.example.pawsecure.response.PetResponse;
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
    private PetRequest petRequest;

    public UserRepository() {

    }

    UserRequest getUserRequest() {
        if (userRequest == null) {
            userRequest = RetrofitRequest.obtainRetrofit().create(UserRequest.class);
        }
        return userRequest;
    }

    PetRequest getPetRequest() {
        if (petRequest == null) {
            petRequest = RetrofitRequest.obtainRetrofit().create(PetRequest.class);
        }
        return petRequest;
    }

    public LiveData<GeneralResponse> signUp(String name, String email, String password, String password_again, String lang) {
        MutableLiveData<GeneralResponse> mutableLiveData = new MutableLiveData<>();
        getUserRequest().register(name, email, password, password_again, lang).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                GeneralResponse generalResponse;
                switch (response.code()) {
                    case 400:
                        try {
                            generalResponse = new Gson().fromJson(response.errorBody().string(), GeneralResponse.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        generalResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(generalResponse);
                        break;
                    case 202:
                        generalResponse = response.body();
                        if (generalResponse != null) {
                            generalResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(generalResponse);
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
        getUserRequest().login(email, password).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                TokenResponse tokenResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        tokenResponse = new TokenResponse();
                        tokenResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(tokenResponse);
                        break;
                    case 200:
                        tokenResponse = response.body();
                        if (tokenResponse != null) {
                            tokenResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(tokenResponse);
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
        getUserRequest().refresh(headerAuth).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                TokenResponse tokenResponse;
                switch (response.code()) {
                    case 401:
                        tokenResponse = new TokenResponse();
                        tokenResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(tokenResponse);
                        break;
                    case 200:
                        tokenResponse = response.body();
                        if (tokenResponse != null) {
                            tokenResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(tokenResponse);
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
        getUserRequest().spaces(headerAuth).enqueue(new Callback<SpaceResponse>() {
            @Override
            public void onResponse(Call<SpaceResponse> call, Response<SpaceResponse> response) {
                SpaceResponse spaceResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        spaceResponse = new SpaceResponse();
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
            public void onFailure(Call<SpaceResponse> call, Throwable t) { }
        });
        return mutableLiveData;
    }

    public LiveData<PetResponse> pets (String headerAuth) {
        MutableLiveData<PetResponse> mutableLiveData = new MutableLiveData<>();
        getUserRequest().pets(headerAuth).enqueue(new Callback<PetResponse>() {
            @Override
            public void onResponse(Call<PetResponse> call, Response<PetResponse> response) {
                PetResponse petResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        petResponse = new PetResponse();
                        petResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(petResponse);
                        break;
                    case 200:
                        petResponse = response.body();
                        if (petResponse != null) {
                            petResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(petResponse);
                        break;
                }
            }

            @Override
            public void onFailure(Call<PetResponse> call, Throwable t) { }
        });
        return mutableLiveData;
    }

    public LiveData<PetResponse> storePet (String headerAuth, String name, String race,
                                           String sex, int icon, String image, int animal,
                                           String birthday, String description) {
        MutableLiveData<PetResponse> mutableLiveData = new MutableLiveData<>();
        getPetRequest().pet(headerAuth, name, race, sex, icon, image, animal, birthday, description)
                .enqueue(new Callback<PetResponse>() {
            @Override
            public void onResponse(Call<PetResponse> call, Response<PetResponse> response) {
                PetResponse petResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        petResponse = new PetResponse();
                        petResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(petResponse);
                        break;
                    case 400:
                        try {
                            petResponse = new Gson().fromJson(response.errorBody().string(), PetResponse.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        petResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(petResponse);
                        break;
                    case 200:
                        petResponse = response.body();
                        if (petResponse != null) {
                            petResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(petResponse);
                        break;
                }
            }

            @Override
            public void onFailure(Call<PetResponse> call, Throwable t) { }
        });
        return mutableLiveData;
    }

}

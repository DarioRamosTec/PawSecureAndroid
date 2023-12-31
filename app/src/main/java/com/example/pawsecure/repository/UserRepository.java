package com.example.pawsecure.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pawsecure.request.PetRequest;
import com.example.pawsecure.request.PetSpaceRequest;
import com.example.pawsecure.request.SensorRequest;
import com.example.pawsecure.request.SpaceRequest;
import com.example.pawsecure.request.UserRequest;
import com.example.pawsecure.response.GeneralResponse;
import com.example.pawsecure.response.LangResponse;
import com.example.pawsecure.response.PetResponse;
import com.example.pawsecure.response.SensorResponse;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.SpaceSensorResponse;
import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.response.UserResponse;
import com.example.pawsecure.retrofit.RetrofitRequest;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserRequest userRequest;
    private PetRequest petRequest;
    private SpaceRequest spaceRequest;
    private PetSpaceRequest petSpaceRequest;
    private SensorRequest sensorRequest;

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

    SpaceRequest getSpaceRequest() {
        if (spaceRequest == null) {
            spaceRequest = RetrofitRequest.obtainRetrofit().create(SpaceRequest.class);
        }
        return spaceRequest;
    }

    PetSpaceRequest getPetSpaceRequest() {
        if (petSpaceRequest == null) {
            petSpaceRequest = RetrofitRequest.obtainRetrofit().create(PetSpaceRequest.class);
        }
        return petSpaceRequest;
    }

    SensorRequest getSensorRequest() {
        if (sensorRequest == null) {
            sensorRequest = RetrofitRequest.obtainRetrofit().create(SensorRequest.class);
        }
        return sensorRequest;
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
            public void onFailure(Call<SpaceResponse> call, Throwable t) {

            }
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
        getPetRequest().store(headerAuth, name, race, sex, icon, image, animal, birthday, description)
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
                    case 201:
                        petResponse = response.body();
                        if (petResponse != null) {
                            petResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(petResponse);
                        break;
                }
            }

            @Override
            public void onFailure(Call<PetResponse> call, Throwable t) {
            }
        });
        return mutableLiveData;
    }

    public LiveData<SpaceResponse> storeSpace (String auth, String name, String description) {
        MutableLiveData<SpaceResponse> mutableLiveData = new MutableLiveData<>();
        getSpaceRequest().store(auth, name, description)
                .enqueue(new Callback<SpaceResponse>() {
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
                            case 400:
                                try {
                                    spaceResponse = new Gson().fromJson(response.errorBody().string(), SpaceResponse.class);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                spaceResponse.code = String.valueOf(response.code());
                                mutableLiveData.setValue(spaceResponse);
                                break;
                            case 201:
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

    public LiveData<SpaceResponse> pivotPetSpace (String auth, int id, ArrayList<Integer> pets) {
        MutableLiveData<SpaceResponse> mutableLiveData = new MutableLiveData<>();
        getPetSpaceRequest().store(auth, id, pets)
                .enqueue(new Callback<SpaceResponse>() {
                    @Override
                    public void onResponse(Call<SpaceResponse> call, Response<SpaceResponse> response) {
                        SpaceResponse spaceResponse;
                        switch (response.code()) {
                            case 404:
                            case 403:
                            case 401:
                                spaceResponse = new SpaceResponse();
                                spaceResponse.code = String.valueOf(response.code());
                                mutableLiveData.setValue(spaceResponse);
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
                            case 201:
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

    public LiveData<SpaceResponse> indexSpace (String auth, int id) {
        MutableLiveData<SpaceResponse> mutableLiveData = new MutableLiveData<>();
        getSpaceRequest().index(auth, id).enqueue(new Callback<SpaceResponse>() {
                    @Override
                    public void onResponse(Call<SpaceResponse> call, Response<SpaceResponse> response) {
                        SpaceResponse spaceResponse;
                        switch (response.code()) {
                            case 404:
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
                    public void onFailure(Call<SpaceResponse> call, Throwable t) {
                    }
                });
        return mutableLiveData;
    }

    public LiveData<SpaceResponse> linkSpace (String auth, int id, String mac) {
        MutableLiveData<SpaceResponse> mutableLiveData = new MutableLiveData<>();
        getSpaceRequest().link(auth, id, mac).enqueue(new Callback<SpaceResponse>() {
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

    public LiveData<SpaceSensorResponse> indexSensor (String auth, int id) {
        MutableLiveData<SpaceSensorResponse> mutableLiveData = new MutableLiveData<>();
        getSensorRequest().index(auth, id).enqueue(new Callback<SpaceSensorResponse>() {
            @Override
            public void onResponse(Call<SpaceSensorResponse> call, Response<SpaceSensorResponse> response) {
                SpaceSensorResponse spaceSensorResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        spaceSensorResponse = new SpaceSensorResponse();
                        spaceSensorResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(spaceSensorResponse);
                        break;
                    case 200:
                        spaceSensorResponse = response.body();
                        if (spaceSensorResponse != null) {
                            spaceSensorResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(spaceSensorResponse);
                        break;
                }
            }

            @Override
            public void onFailure(Call<SpaceSensorResponse> call, Throwable t) {
            }
        });
        return mutableLiveData;
    }

    public LiveData<SensorResponse> sensorPosition (String auth, int id) {
        MutableLiveData<SensorResponse> mutableLiveData = new MutableLiveData<>();
        getSensorRequest().position(auth, id).enqueue(getCallBackSensor(mutableLiveData));
        return mutableLiveData;
    }

    public LiveData<SensorResponse> sensorMotion (String auth, int id) {
        MutableLiveData<SensorResponse> mutableLiveData = new MutableLiveData<>();
        getSensorRequest().motion(auth, id).enqueue(getCallBackSensor(mutableLiveData));
        return mutableLiveData;
    }

    public LiveData<SensorResponse> findSensor (String auth, int id, String sensor) {
        MutableLiveData<SensorResponse> mutableLiveData = new MutableLiveData<>();
        getSensorRequest().sensor(auth, id, sensor).enqueue(getCallBackSensor(mutableLiveData));
        return mutableLiveData;
    }

    Callback<SensorResponse> getCallBackSensor(MutableLiveData<SensorResponse> mutableLiveData) {
        return new Callback<SensorResponse>() {
            @Override
            public void onResponse(Call<SensorResponse> call, Response<SensorResponse> response) {
                SensorResponse sensorResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        sensorResponse = new SensorResponse();
                        sensorResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(sensorResponse);
                        break;
                    case 202:
                    case 200:
                        sensorResponse = response.body();
                        if (sensorResponse != null) {
                            sensorResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(sensorResponse);
                        break;
                }
            }

            @Override
            public void onFailure(Call<SensorResponse> call, Throwable t) {
            }
        };
    }

    public LiveData<SpaceResponse> targetSensor (String auth, int id, Integer pet) {
        MutableLiveData<SpaceResponse> mutableLiveData = new MutableLiveData<>();
        getSpaceRequest().target(auth, id, pet).enqueue(new Callback<SpaceResponse>() {
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

    public LiveData<UserResponse> indexMe (String auth) {
        MutableLiveData<UserResponse> mutableLiveData = new MutableLiveData<>();
        getUserRequest().index(auth).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        userResponse = new UserResponse();
                        userResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(userResponse);
                        break;
                    case 200:
                        userResponse = response.body();
                        if (userResponse != null) {
                            userResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(userResponse);
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<UserResponse> updateMe (String auth, String name, String middle_name, String last_name) {
        MutableLiveData<UserResponse> mutableLiveData = new MutableLiveData<>();
        getUserRequest().update(auth, name, middle_name, last_name).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        userResponse = new UserResponse();
                        userResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(userResponse);
                        break;
                    case 400:
                        try {
                            userResponse = new Gson().fromJson(response.errorBody().string(), UserResponse.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        userResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(userResponse);
                        break;
                    case 200:
                        userResponse = response.body();
                        if (userResponse != null) {
                            userResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(userResponse);
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<UserResponse> logoutMe (String auth) {
        MutableLiveData<UserResponse> mutableLiveData = new MutableLiveData<>();
        getUserRequest().logout(auth).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        userResponse = new UserResponse();
                        userResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(userResponse);
                        break;
                    case 200:
                        userResponse = response.body();
                        if (userResponse != null) {
                            userResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(userResponse);
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<LangResponse> indexLang (String auth) {
        MutableLiveData<LangResponse> mutableLiveData = new MutableLiveData<>();
        getUserRequest().lang(auth).enqueue(new Callback<LangResponse>() {
            @Override
            public void onResponse(Call<LangResponse> call, Response<LangResponse> response) {
                LangResponse langResponse;
                switch (response.code()) {
                    case 403:
                    case 401:
                        langResponse = new LangResponse();
                        langResponse.code = String.valueOf(response.code());
                        mutableLiveData.setValue(langResponse);
                        break;
                    case 200:
                        langResponse = response.body();
                        if (langResponse != null) {
                            langResponse.code = String.valueOf(response.code());
                        }
                        mutableLiveData.setValue(langResponse);
                        break;
                }
            }

            @Override
            public void onFailure(Call<LangResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

}

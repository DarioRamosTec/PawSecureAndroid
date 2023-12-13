package com.example.pawsecure.view_model;

import androidx.lifecycle.LiveData;

import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.SensorResponse;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.SpaceSensorResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public class SpaceViewModel extends PawSecureViewModel {
    public LiveData<SpaceResponse> space (String auth, int id) {
        return userRepository.indexSpace(auth, id);
    }

    public LiveData<SpaceSensorResponse> sensors (String auth, int id) {
        return userRepository.indexSensor(auth, id);
    }

    public LiveData<SensorResponse> position (String auth, int id) {
        return userRepository.sensorPosition(auth, id);
    }

    public LiveData<SensorResponse> motion (String auth, int id) {
        return userRepository.sensorMotion(auth, id);
    }

    public LiveData<SensorResponse> sensor (String auth, int id, String sensor) {
        return userRepository.findSensor(auth, id, sensor);
    }

}

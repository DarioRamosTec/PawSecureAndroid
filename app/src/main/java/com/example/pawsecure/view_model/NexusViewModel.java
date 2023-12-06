package com.example.pawsecure.view_model;

import androidx.lifecycle.LiveData;

import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.GeneralResponse;
import com.example.pawsecure.response.SpaceResponse;

public class NexusViewModel extends PawSecureViewModel {
    public LiveData<SpaceResponse> spaces(String token) {
        return userRepository.spaces(token);
    }
}

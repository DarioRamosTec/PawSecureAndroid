package com.example.pawsecure.view_model;

import androidx.lifecycle.LiveData;

import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.PetResponse;
import com.example.pawsecure.response.SpaceResponse;

public class ChooseViewModel extends PawSecureViewModel {
    public LiveData<PetResponse> pets(String token) {
        return userRepository.pets(token);
    }
}

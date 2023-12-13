package com.example.pawsecure.view_model;

import androidx.lifecycle.LiveData;

import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.SpaceResponse;

import java.util.ArrayList;

public class SpaceViewModel extends PawSecureViewModel {
    public LiveData<SpaceResponse> space (String auth, int id) {
        return userRepository.indexSpace(auth, id);
    }
}

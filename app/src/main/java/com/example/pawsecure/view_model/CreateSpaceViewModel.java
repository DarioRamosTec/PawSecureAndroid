package com.example.pawsecure.view_model;

import androidx.lifecycle.LiveData;

import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.PetResponse;
import com.example.pawsecure.response.SpaceResponse;

import java.util.ArrayList;

public class CreateSpaceViewModel extends PawSecureViewModel {
    public LiveData<SpaceResponse> space(String auth, String name, String description) {
        return userRepository.storeSpace(auth, name, description);
    }

    public LiveData<SpaceResponse> petSpace(String auth, int id, ArrayList<Integer> pets) {
        return userRepository.pivotPetSpace(auth, id, pets);
    }
}

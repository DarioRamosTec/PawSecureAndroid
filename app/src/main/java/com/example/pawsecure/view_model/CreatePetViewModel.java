package com.example.pawsecure.view_model;

import androidx.lifecycle.LiveData;

import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.PetResponse;

public class CreatePetViewModel extends PawSecureViewModel  {
    public LiveData<PetResponse> pet(String headerAuth, String name, String race,
                                     String sex, int icon, String image, int animal,
                                     String birthday, String description) {
        return userRepository.storePet(headerAuth, name, race, sex, icon, image, animal, birthday, description);
    }

}

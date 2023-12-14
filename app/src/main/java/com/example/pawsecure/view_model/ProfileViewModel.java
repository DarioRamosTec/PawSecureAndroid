package com.example.pawsecure.view_model;

import androidx.lifecycle.LiveData;

import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.model.User;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.UserResponse;

public class ProfileViewModel extends PawSecureViewModel {
    public LiveData<UserResponse> index(String token) {
        return userRepository.indexMe(token);
    }
    public LiveData<UserResponse> logout(String token) {
        return userRepository.logoutMe(token);
    }
    public LiveData<UserResponse> update(String token, String name, String middle_name, String last_name) {

        return userRepository.updateMe(token, name, middle_name, last_name);
    }

}

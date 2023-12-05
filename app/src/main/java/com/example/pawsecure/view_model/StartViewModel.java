package com.example.pawsecure.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pawsecure.repository.UserRepository;
import com.example.pawsecure.response.TokenResponse;

public class StartViewModel extends ViewModel {
    private final UserRepository userRepository;

    public StartViewModel () {
        userRepository = new UserRepository();
    }

    public LiveData<TokenResponse> refresh () {
        return userRepository.refresh();
    }
}

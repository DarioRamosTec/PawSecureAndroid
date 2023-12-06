package com.example.pawsecure.implementation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pawsecure.repository.UserRepository;
import com.example.pawsecure.response.TokenResponse;

public class PawSecureViewModel extends ViewModel {
    protected final UserRepository userRepository;

    public PawSecureViewModel () {
        userRepository = new UserRepository();
    }

    public LiveData<TokenResponse> refresh (String headerAuth) {
        return userRepository.refresh(headerAuth);
    }

    public LiveData<TokenResponse> login(String email, String password) {
        return userRepository.login(email, password);
    }
}

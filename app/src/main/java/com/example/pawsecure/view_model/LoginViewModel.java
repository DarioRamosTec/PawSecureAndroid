package com.example.pawsecure.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pawsecure.repository.UserRepository;
import com.example.pawsecure.response.GeneralResponse;
import com.example.pawsecure.response.TokenResponse;

public class LoginViewModel extends ViewModel {
    private UserRepository userRepository;

    public LoginViewModel () {
        userRepository = new UserRepository();
    }

    public LiveData<TokenResponse> getLoginData (String email, String password) {
        return userRepository.login(email, password);
    }
}

package com.example.pawsecure.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pawsecure.repository.UserRepository;
import com.example.pawsecure.response.GeneralResponse;

public class RegisterViewModel extends ViewModel {
    private final UserRepository userRepository;

    public RegisterViewModel () {
        userRepository = new UserRepository();
    }

    public LiveData<GeneralResponse> getRegisterData(String name, String email, String password, String password_again, String lang) {
        return userRepository.signUp(name, email, password, password_again, lang);
    }
}

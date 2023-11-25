package com.example.pawsecure.view_model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pawsecure.repository.UserRepository;
import com.example.pawsecure.response.GeneralResponse;

public class RegisterViewModel extends ViewModel {
    private UserRepository userRepository;

    public RegisterViewModel () {
        userRepository = new UserRepository();
    }

    public LiveData<GeneralResponse> getRegisterData(String name, String email, String password, String password_again, String lang) {
        return userRepository.getDataToSignUp(name, email, password, password_again, lang);
    }
}

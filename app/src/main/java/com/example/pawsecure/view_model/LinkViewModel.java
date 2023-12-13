package com.example.pawsecure.view_model;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.TokenResponse;

public class LinkViewModel extends PawSecureViewModel {
    public LiveData<SpaceResponse> link(String token, int id, String mac) {
        return userRepository.linkSpace(token, id, mac);
    }
}
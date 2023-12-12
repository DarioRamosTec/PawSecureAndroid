package com.example.pawsecure.view_model;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.TokenResponse;

public class LinkViewModel extends PawSecureViewModel {
    public LiveData<Boolean> searchDevice(Context context, BluetoothAdapter bluetoothAdapter) {
        MutableLiveData<Boolean> mutableLiveData = null;
        try {
            mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(bluetoothAdapter.startDiscovery());
        } catch (SecurityException e) {

        }
        return mutableLiveData;
    }
}
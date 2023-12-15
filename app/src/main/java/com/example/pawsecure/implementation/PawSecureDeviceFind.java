package com.example.pawsecure.implementation;

import android.bluetooth.BluetoothDevice;

import androidx.fragment.app.Fragment;

public interface PawSecureDeviceFind {
    void boundToDevice(BluetoothDevice bluetoothDevice);
}

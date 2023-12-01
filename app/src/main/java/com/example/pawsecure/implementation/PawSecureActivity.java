package com.example.pawsecure.implementation;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pawsecure.view.LoginActivity;

public class PawSecureActivity extends AppCompatActivity {
    public void onNotAuth() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

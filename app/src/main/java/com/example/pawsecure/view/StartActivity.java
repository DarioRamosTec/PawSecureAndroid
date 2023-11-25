package com.example.pawsecure.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pawsecure.R;

import java.util.Locale;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.buttonLoginStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(LoginActivity.class);
            }
        });
        findViewById(R.id.buttonSignUpStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(RegisterActivity.class);
            }
        });
    }

    void startIntent(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }
}
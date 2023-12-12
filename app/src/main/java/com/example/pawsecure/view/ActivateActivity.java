package com.example.pawsecure.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;

public class ActivateActivity extends PawSecureActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);

        Button buttonActivate = findViewById(R.id.buttonActivate);
        buttonActivate.setOnClickListener(view -> startIntent(NexusActivity.class, true));
    }
}
package com.example.pawsecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.buttonLoginRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
        findViewById(R.id.buttonSignUpRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEmail();
            }
        });
    }

    void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    void goToEmail() {
        Intent intent = new Intent(this, EmailActivity.class);
        startActivity(intent);
    }
}
package com.example.pawsecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        findViewById(R.id.buttonResendEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEmail();
            }
        });
        findViewById(R.id.buttonVerifyRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivate();
            }
        });
    }

    void goToActivate() {
        if (true) {
            Intent intent = new Intent(this, ActivateActivity.class);
            startActivity(intent);
            finish();
        } else {
            goToEmail();
        }
    }

    void goToEmail () {
        Intent intent = new Intent(this, EmailActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.example.pawsecure.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.lifecycle.ViewModelProvider;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.StartViewModel;

import java.util.Locale;

public class StartActivity extends PawSecureActivity {

    StartViewModel startViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startViewModel = new ViewModelProvider(this).get(StartViewModel.class);
        if (!Token.getToken(this).equals("")) {
            startViewModel.refresh().observe(this, new PawSecureObserver<TokenResponse>(this, new ObserveRefresh()));
        }

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

    class ObserveRefresh implements PawSecureObserver.PawSecureOnChanged<TokenResponse> {
        @Override
        public void onChanged(TokenResponse tokenResponse) {
            if (tokenResponse.error == null) {
                startIntent(NexusActivity.class);
            } else {

            }
        }
    }

}
package com.example.pawsecure.view;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.view_model.LoginViewModel;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends PawSecureActivity implements View.OnClickListener {

    TextInputEditText editTextPasswordLogin;
    TextInputEditText editTextEmailLogin;
    TextInputLayout textInputEmailLogin;
    TextInputLayout textInputPasswordLogin;
    LoginViewModel loginViewModel;
    CircularProgressIndicator progressIndicatorLogin;
    View viewBlackLogin;
    Button buttonLoginLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        findViewById(R.id.buttonSignUpLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);
        editTextEmailLogin = findViewById(R.id.editTextEmailLogin);
        textInputEmailLogin = findViewById(R.id.textInputEmailLogin);
        textInputPasswordLogin = findViewById(R.id.textInputPasswordLogin);
        progressIndicatorLogin = findViewById(R.id.progressIndicatorLogin);
        viewBlackLogin = findViewById(R.id.viewBlackLogin);
        buttonLoginLogin = findViewById(R.id.buttonLoginLogin);
        buttonLoginLogin.setOnClickListener(this);
    }

    void goToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    void goToNexus() {
        startActivity(new Intent(this, NexusActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        ObjectAnimator animationStart = ObjectAnimator.ofFloat(viewBlackLogin, "alpha", 0.15f).setDuration(200);
        animationStart.setInterpolator(new LinearOutSlowInInterpolator());
        animationStart.start();
        progressIndicatorLogin.show();
        progressIndicatorLogin.setContentDescription(getText(R.string.wait));
        buttonLoginLogin.setEnabled(false);

        loginViewModel.getLoginData(editTextEmailLogin.getText().toString(),
                editTextPasswordLogin.getText().toString()).observe(this, new PawSecureObserver<TokenResponse>(this, new ObserveLogin()));
    }

    class ObserveLogin implements PawSecureObserver.PawSecureOnChanged<TokenResponse> {

        @Override
        public void onChanged(TokenResponse tokenResponse) {
            ObjectAnimator animationFinal = ObjectAnimator.ofFloat(viewBlackLogin, "alpha", 0f).setDuration(200);
            animationFinal.setInterpolator(new FastOutSlowInInterpolator());
            animationFinal.start();
            progressIndicatorLogin.hide();
            progressIndicatorLogin.setContentDescription(null);
            buttonLoginLogin.setEnabled(true);

            if (tokenResponse.error == null) {
                textInputEmailLogin.setError(null);
                textInputEmailLogin.setErrorContentDescription(null);
                textInputPasswordLogin.setError(null);
                textInputPasswordLogin.setErrorContentDescription(null);
                goToNexus();
            } else {
                textInputEmailLogin.setError(getText(R.string.login_validation_error));
                textInputEmailLogin.setErrorContentDescription(getText(R.string.login_validation_error));
                textInputPasswordLogin.setError(getText(R.string.login_validation_error));
                textInputPasswordLogin.setErrorContentDescription(getText(R.string.login_validation_error));

            }
        }
    }
}
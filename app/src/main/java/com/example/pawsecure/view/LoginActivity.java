package com.example.pawsecure.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends PawSecureActivity implements View.OnClickListener {

    TextInputEditText editTextPasswordLogin;
    TextInputEditText editTextEmailLogin;
    TextInputLayout textInputEmailLogin;
    TextInputLayout textInputPasswordLogin;
    LoginViewModel loginViewModel;
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
        buttonLoginLogin = findViewById(R.id.buttonLoginLogin);
        buttonLoginLogin.setOnClickListener(this);

        establishCurtain(findViewById(R.id.progressIndicatorLogin), findViewById(R.id.viewBlackLogin));
    }

    void goToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    void goToNexus() {
        startActivity(new Intent(this, NexusActivity.class));
        finish();
    }

    void goToEmail() {
        Intent intent = new Intent(this, EmailActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        String email = (editTextEmailLogin.getText() != null ? editTextEmailLogin.getText() : "").toString();
        String password = (editTextPasswordLogin.getText() != null ? editTextPasswordLogin.getText() : "").toString();
        showCurtain(new Button[]{buttonLoginLogin});
        loginViewModel.login(email, password).observe(this, new PawSecureObserver<TokenResponse>(this, new ObserveLogin(email, password)));
    }

    class ObserveLogin implements PawSecureObserver.PawSecureOnChanged<TokenResponse> {

        String email;
        String password;

        public ObserveLogin (String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        public void onChanged(TokenResponse tokenResponse) {
            hideCurtain(new Button[]{buttonLoginLogin});
            if (tokenResponse.error == null) {
                textInputEmailLogin.setError(null);
                textInputEmailLogin.setErrorContentDescription(null);
                textInputPasswordLogin.setError(null);
                textInputPasswordLogin.setErrorContentDescription(null);
                Token.setToken(getBaseContext(), tokenResponse);
                Token.setData(getBaseContext(), email, password);
                goToNexus();
            } else {
                if (tokenResponse.error.equals("403")) {
                    goToEmail();
                } else {
                    textInputEmailLogin.setError(getText(R.string.login_validation_error));
                    textInputEmailLogin.setErrorContentDescription(getText(R.string.login_validation_error));
                    textInputPasswordLogin.setError(getText(R.string.login_validation_error));
                    textInputPasswordLogin.setErrorContentDescription(getText(R.string.login_validation_error));
                }
            }
        }
    }
}
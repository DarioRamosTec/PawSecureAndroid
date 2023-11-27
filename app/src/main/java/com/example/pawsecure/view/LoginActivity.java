package com.example.pawsecure.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pawsecure.R;
import com.example.pawsecure.view_model.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText editTextPasswordLogin;
    TextInputEditText editTextEmailLogin;
    TextInputLayout textInputEmailLogin;
    TextInputLayout textInputPasswordLogin;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new LoginViewModel();

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

        findViewById(R.id.buttonLoginLogin).setOnClickListener(this);
    }

    void goToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        loginViewModel.getLoginData(editTextEmailLogin.getText().toString(),
                editTextPasswordLogin.getText().toString()).observe(this, loginData -> {
                    if (loginData.error == null) {
                        textInputEmailLogin.setError(null);
                        textInputEmailLogin.setErrorContentDescription(null);
                        textInputPasswordLogin.setError(null);
                        textInputPasswordLogin.setErrorContentDescription(null);
                        //
                    } else {
                        textInputEmailLogin.setError(getText(R.string.login_validation_error));
                        textInputEmailLogin.setErrorContentDescription(getText(R.string.login_validation_error));
                        textInputPasswordLogin.setError(getText(R.string.login_validation_error));
                        textInputPasswordLogin.setErrorContentDescription(getText(R.string.login_validation_error));
                    }
        });
    }
}
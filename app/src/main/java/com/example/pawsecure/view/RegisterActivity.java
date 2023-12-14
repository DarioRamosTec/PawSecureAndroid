package com.example.pawsecure.view;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.lifecycle.ViewModelProvider;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.transition.CircularPropagation;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.view_model.RegisterViewModel;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

import retrofit2.Call;

public class RegisterActivity extends PawSecureActivity implements View.OnClickListener {

    TextInputLayout textInputNameRegister;
    TextInputLayout textInputEmailRegister;
    TextInputLayout textInputPasswordRegister;
    TextInputLayout textInputPasswordAgainRegister;

    TextInputEditText editTextName;
    TextInputEditText editTextEmail;
    TextInputEditText editTextPassword;
    TextInputEditText editTextPasswordAgain;
    RegisterViewModel registerViewModel;
    CircularProgressIndicator progressIndicatorRegister;
    View viewBlackRegister;
    Button buttonLoginRegister;
    Button buttonSignUpRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        buttonLoginRegister = findViewById(R.id.buttonLoginRegister);
        buttonLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
        buttonSignUpRegister = findViewById(R.id.buttonSignUpRegister);
        buttonSignUpRegister.setOnClickListener(this);
        textInputNameRegister = findViewById(R.id.textInputNameRegister);
        textInputEmailRegister = findViewById(R.id.textInputEmailRegister);
        textInputPasswordRegister = findViewById(R.id.textInputPasswordRegister);
        textInputPasswordAgainRegister = findViewById(R.id.textInputPasswordAgainRegister);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordAgain = findViewById(R.id.editTextPasswordAgain);

        progressIndicatorRegister = findViewById(R.id.progressIndicatorRegister);
        viewBlackRegister = findViewById(R.id.viewBlackRegister);

        establishCurtain(progressIndicatorRegister, viewBlackRegister);

        this.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                finish();
            }
        });
        progressIndicatorRegister.setVisibilityAfterHide(View.INVISIBLE);
    }

    void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    void goToEmail(String email, String password) {
        Intent intent = new Intent(this, EmailActivity.class);
        intent.putExtra("MAIL", email);
        intent.putExtra("PASSWORD", password);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        showCurtain(new Button[]{buttonSignUpRegister});

        registerViewModel.getRegisterData(editTextName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString(),
                editTextPasswordAgain.getText().toString(),
                Locale.getDefault().getLanguage()).observe(this, registerData -> {
                    hideCurtain(new Button[]{buttonSignUpRegister});

                    if (!registerData.code.equals("202")) {
                        String errorName = registerData.errors.name != null && registerData.errors.name.size() > 0 ? registerData.errors.name.get(0) : null;
                        textInputNameRegister.setError(errorName);
                        textInputNameRegister.setErrorContentDescription(errorName);

                        String errorEmail = registerData.errors.email != null && registerData.errors.email.size() > 0 ? registerData.errors.email.get(0) : null;
                        textInputEmailRegister.setError(errorEmail);
                        textInputEmailRegister.setErrorContentDescription(errorEmail);

                        String errorPassword = registerData.errors.password != null && registerData.errors.password.size() > 0 ? registerData.errors.password.get(0) : null;
                        textInputPasswordRegister.setError(errorPassword);
                        textInputPasswordRegister.setErrorContentDescription(errorPassword);

                        String errorPasswordAgain = registerData.errors.password_again != null && registerData.errors.password_again.size() > 0 ? registerData.errors.password_again.get(0) : null;
                        textInputPasswordAgainRegister.setError(errorPasswordAgain);
                        textInputPasswordAgainRegister.setErrorContentDescription(errorPasswordAgain);
                    } else {
                        textInputNameRegister.setError(null);
                        textInputNameRegister.setErrorContentDescription(null);
                        textInputEmailRegister.setError(null);
                        textInputEmailRegister.setErrorContentDescription(null);
                        textInputPasswordRegister.setError(null);
                        textInputPasswordRegister.setErrorContentDescription(null);
                        textInputPasswordAgainRegister.setError(null);
                        textInputPasswordAgainRegister.setErrorContentDescription(null);
                        goToEmail(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                    }
        });
    }
}
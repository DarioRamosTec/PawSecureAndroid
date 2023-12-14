package com.example.pawsecure.view;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.pawsecure.view_model.EmailViewModel;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

public class EmailActivity extends PawSecureActivity {

    String mail;
    String password;
    EmailViewModel emailViewModel;
    ConstraintLayout constraintEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        constraintEmail = findViewById(R.id.constraintEmail);
        mail = getIntent().getStringExtra("MAIL");
        password = getIntent().getStringExtra("PASSWORD");
        emailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);

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

        this.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                finish();
            }
        });
    }

    void goToActivate() {
        emailViewModel.login(mail, password).observe(this, new PawSecureObserver<TokenResponse>(this, new ObserveLogin(mail, password)));
    }



    void goToEmail () {
        Intent intent = new Intent(this, EmailActivity.class);
        startActivity(intent);
        finish();
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
            if (tokenResponse.code.equals("200")) {
                startIntent(ActivateActivity.class, true);
                Token.setData(getParent(), email, password);
            } else {
                Snackbar snackbar = snackbar = Snackbar.make(getParent(), constraintEmail, getResources().getText(R.string.not_login), Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry, view -> {
                            goToActivate();
                        });
                snackbar.show();
            }
        }
    }
}
package com.example.pawsecure.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.StartViewModel;

public class StartActivity extends PawSecureActivity {

    StartViewModel startViewModel;
    Button buttonLogin;
    Button buttonSignUp;
    TextView textView;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startViewModel = new ViewModelProvider(this).get(StartViewModel.class);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        buttonLogin = findViewById(R.id.buttonLoginStart);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(LoginActivity.class);
            }
        });
        buttonSignUp = findViewById(R.id.buttonSignUpStart);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(RegisterActivity.class);
            }
        });
        establishCurtain(findViewById(R.id.progressCurtainStart), findViewById(R.id.viewCurtainStart));
        amountDarkCurtain = 0;
        becameVisible(false);
        if (Token.checkToken(this)) {
            showCurtain(new Button[]{buttonLogin, buttonSignUp});
            startViewModel.refresh(Token.getBearer()).observe(this, new PawSecureObserver<TokenResponse>(this, new ObserveReadyToUse(true, this)));
        } else if (Token.checkUser(this)) {
            showCurtain(new Button[]{buttonLogin, buttonSignUp});
            String email = Token.getData()[0];
            String password = Token.getData()[1];
            startViewModel.login(email, password).observe(this, new PawSecureObserver<TokenResponse>(this, new ObserveReadyToUse(false, this, email, password)));
        } else {
            becameVisible(true);
        }
    }

    void startIntent(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }

    public void becameVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        buttonLogin.setVisibility(visibility);
        buttonSignUp.setVisibility(visibility);
        textView.setVisibility(visibility);
        textView2.setVisibility(visibility);
    }

    class ObserveReadyToUse implements PawSecureObserver.PawSecureOnChanged<TokenResponse> {

        PawSecureActivity pawSecureActivity;
        String email;
        String password;
        boolean refresh;

        public ObserveReadyToUse (boolean refresh, PawSecureActivity pawSecureActivity) {
            this.refresh = refresh;
            this.pawSecureActivity = pawSecureActivity;
        }

        public ObserveReadyToUse (boolean refresh, PawSecureActivity pawSecureActivity, String email, String password) {
            this.refresh = refresh;
            this.pawSecureActivity = pawSecureActivity;
            this.email = email;
            this.password = password;
        }

        @Override
        public void onChanged(TokenResponse tokenResponse) {
            hideCurtain(new Button[]{buttonLogin, buttonSignUp});
            if (tokenResponse.error == null) {
                Token.setToken(getBaseContext(), tokenResponse);
                Token.setData(getBaseContext(), this.email, this.password);
                startIntent(NexusActivity.class);
            } else {
                if (refresh) {
                    startViewModel.login(Token.getData()[0], Token.getData()[1]).observe(pawSecureActivity, new PawSecureObserver<TokenResponse>(pawSecureActivity, new ObserveReadyToUse(false, pawSecureActivity)));
                } else {
                    ((StartActivity) pawSecureActivity).becameVisible(true);
                }
            }
        }
    }

}
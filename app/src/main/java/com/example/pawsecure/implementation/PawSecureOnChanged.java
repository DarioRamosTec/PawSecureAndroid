package com.example.pawsecure.implementation;

import android.content.Context;

import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.token.Token;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class PawSecureOnChanged {

    public Context context;
    public PawSecureActivity pawSecureActivity;
    public PawSecureViewModel pawSecureViewModel;
    public PawSecureOnChanged (Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
        this.context = context;
        this.pawSecureActivity = pawSecureActivity;
        this.pawSecureViewModel = pawSecureViewModel;
    }

    public void checkAuth(Context context, PawSecureViewModel viewModel, PawSecureActivity pawSecureActivity) {
        boolean checkedData = Token.checkUser(context);
        if (Token.checkToken(context)) {
            viewModel.refresh(Token.getBearer()).observe(pawSecureActivity, new PawSecureObserver<TokenResponse>(pawSecureActivity, new AuthOnChanged(context, true, viewModel, pawSecureActivity)));
        } else if (checkedData) {
            String email = Token.getData()[0];
            String password = Token.getData()[1];
            viewModel.login(email, password).observe(pawSecureActivity, new PawSecureObserver<TokenResponse>(pawSecureActivity, new AuthOnChanged(context, false, viewModel, pawSecureActivity)));
        }
    }

    public void checkValidationTextInput (List<String> field, TextInputLayout inputLayout) {
        String errorField = field != null && field.size() > 0 ? field.get(0) : null;
        inputLayout.setError(errorField);
        inputLayout.setErrorContentDescription(errorField);
    }

    public void setNullTextInput (TextInputLayout inputLayout) {
        inputLayout.setError(null);
        inputLayout.setErrorContentDescription(null);
    }

    class AuthOnChanged implements PawSecureObserver.PawSecureOnChanged<TokenResponse> {
        Context context;
        boolean refresh;
        PawSecureViewModel viewModel;
        PawSecureActivity pawSecureActivity;

        public AuthOnChanged(Context context, boolean refresh, PawSecureViewModel viewModel, PawSecureActivity pawSecureActivity) {
            this.context = context;
            this.refresh = refresh;
            this.viewModel = viewModel;
            this.pawSecureActivity = pawSecureActivity;
        }

        @Override
        public void onChanged(TokenResponse tokenResponse) {
            if (tokenResponse.code.equals("200")) {
                Token.setToken(this.context, tokenResponse);
                pawSecureActivity.onAuth();
            } else {
                if (refresh) {
                    viewModel.login(Token.getData()[0], Token.getData()[1]).observe(pawSecureActivity, new PawSecureObserver<TokenResponse>(pawSecureActivity, new AuthOnChanged(context, false, viewModel, pawSecureActivity)));
                } else {
                    pawSecureActivity.onNotAuth();
                }
            }
        }
    }
}

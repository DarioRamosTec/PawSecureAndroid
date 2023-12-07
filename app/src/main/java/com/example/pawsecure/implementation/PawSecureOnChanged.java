package com.example.pawsecure.implementation;

import android.content.Context;
import android.widget.Button;

import androidx.lifecycle.ViewModel;

import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view.NexusActivity;
import com.example.pawsecure.view.StartActivity;

public class PawSecureOnChanged {
    public void checkAuth(Context context, PawSecureViewModel viewModel, PawSecureActivity pawSecureActivity) {
        boolean checkedData = Token.checkUser(context);
        if (Token.checkToken(context)) {
            viewModel.refresh(Token.getBearer()).observe(pawSecureActivity, new PawSecureObserver<TokenResponse>(pawSecureActivity, new ObserveAuth(context, true, viewModel, pawSecureActivity)));
        } else if (checkedData) {
            String email = Token.getData()[0];
            String password = Token.getData()[1];
            viewModel.login(email, password).observe(pawSecureActivity, new PawSecureObserver<TokenResponse>(pawSecureActivity, new ObserveAuth(context, false, viewModel, pawSecureActivity)));
        }
    }

    class ObserveAuth implements PawSecureObserver.PawSecureOnChanged<TokenResponse> {
        Context context;
        boolean refresh;
        PawSecureViewModel viewModel;
        PawSecureActivity pawSecureActivity;

        public ObserveAuth (Context context, boolean refresh, PawSecureViewModel viewModel, PawSecureActivity pawSecureActivity) {
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
                    viewModel.login(Token.getData()[0], Token.getData()[1]).observe(pawSecureActivity, new PawSecureObserver<TokenResponse>(pawSecureActivity, new ObserveAuth(context, false, viewModel, pawSecureActivity)));
                } else {
                    pawSecureActivity.onNotAuth();
                }
            }
        }
    }
}

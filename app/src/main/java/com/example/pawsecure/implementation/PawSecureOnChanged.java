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
    public void checkAuth(Context context, PawSecureViewModel viewModel) {
        boolean checkedData = Token.checkUser(context);
        if (Token.checkToken(context)) {
            viewModel.refresh(Token.getBearer()).observe(this, new PawSecureObserver<TokenResponse>(this, new ObserveAuth(true, this)));
        } else if (checkedData) {
            String email = Token.getData()[0];
            String password = Token.getData()[1];
            viewModel.login(email, password).observe(this, new PawSecureObserver<TokenResponse>(this, new ObserveAuth(false, this, email, password)));
        }
    }

    class ObserveAuth implements PawSecureObserver.PawSecureOnChanged<TokenResponse> {

        @Override
        public void onChanged(TokenResponse tokenResponse) {
            if (tokenResponse.code == null) {
                Token.setToken(getBaseContext(), tokenResponse);
                if (refresh) {
                    Token.setData(getBaseContext(), this.email, this.password);
                }
                startIntent(NexusActivity.class, true);
            } else {
                if (refresh) {
                    startViewModel.login(Token.getData()[0], Token.getData()[1]).observe(pawSecureActivity, new PawSecureObserver<TokenResponse>(pawSecureActivity, new StartActivity.ObserveReadyToUse(false, pawSecureActivity)));
                } else {
                    ((StartActivity) pawSecureActivity).becameVisible(true);
                }
            }
        }
    }
}

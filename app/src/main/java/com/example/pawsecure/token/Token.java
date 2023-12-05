package com.example.pawsecure.token;

import static android.app.PendingIntent.getActivity;
import static android.provider.Settings.System.getString;

import static androidx.core.content.res.TypedArrayUtils.getText;
import static androidx.core.graphics.drawable.IconCompat.getResources;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.pawsecure.R;
import com.example.pawsecure.repository.UserRepository;
import com.example.pawsecure.response.TokenResponse;

import java.math.BigInteger;

public class Token {

    private static String token;
    private static long expires;
    private static String type;
    private static CountDownTimer countDownTimer;
    private static UserRepository userRepository;
    static boolean expired;

    public static String getToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.key_directory), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.key_token), "");
    }

    public static String getToken() {
        if (expired) {
            refreshToken();
            token = null;
        }
        return token;
    }

    public static void setToken(TokenResponse tokenResponse) {
        if (token == null) {
            new CountDownTimer(expires, 1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    expired = false;
                }
            }.start();
        }
        token = tokenResponse.access_token;
        expires = tokenResponse.expires_in;
        type = tokenResponse.token_type;
    }

    private static void refreshToken() {
        userRepository.refresh().observe(new LifecycleOwner() {
            @NonNull
            @Override
            public Lifecycle getLifecycle() {
                return null;
            }
        }, new Observer<TokenResponse>() {
            @Override
            public void onChanged(TokenResponse tokenResponse) {
                setToken(tokenResponse);
            }
        });
    }
}

package com.example.pawsecure.token;

import static android.app.PendingIntent.getActivity;
import static android.provider.Settings.System.getString;

import static androidx.core.content.res.TypedArrayUtils.getText;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;

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
    private static String type;
    private static long expires;
    private static boolean expired = false;
    private static String email;
    private static String password;
    private static CountDownTimer countDownTimer;

    public static boolean checkToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.key_directory), Context.MODE_PRIVATE);
        token = sharedPref.getString(context.getString(R.string.key_token), "");
        type = sharedPref.getString(context.getString(R.string.key_type), "bearer");
        return !token.equals("");
    }

    public static boolean checkUser(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.data_directory), Context.MODE_PRIVATE);
        email = sharedPref.getString(context.getString(R.string.data_email), "");
        password = sharedPref.getString(context.getString(R.string.data_password), "");
        return !email.equals("") && !password.equals("");
    }

    public static String getBearer() {
        return type + " " + token;
    }

    public static String getToken() {
        return token;
    }

    public static String[] getData() {
        return new String[] { email, password };
    }

    public static void setToken(Context context, TokenResponse tokenResponse) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        token = tokenResponse.access_token;
        expires = tokenResponse.expires_in;
        type = tokenResponse.token_type;
        expired = false;

        countDownTimer = new CountDownTimer(expires, expires/2) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                expired = true;
            }
        };
        countDownTimer.start();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.key_directory), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(context.getString(R.string.key_token), token);
        sharedPrefEditor.putString(context.getString(R.string.key_type), type);
        sharedPrefEditor.apply();
    }

    public static void setData(Context context, String email, String password) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.data_directory), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        Token.email = email;
        Token.password = password;
        sharedPrefEditor.putString(context.getString(R.string.data_email), Token.email);
        sharedPrefEditor.putString(context.getString(R.string.data_password), Token.password);
        sharedPrefEditor.apply();
    }

    public static void setGoodbye(Context context) {
        Token.token = null;
        Token.type = null;
        Token.expires = 0;
        Token.expired = false;
        Token.email = null;
        Token.password = null;
        Token.countDownTimer = null;

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.data_directory), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(context.getString(R.string.data_email), Token.email);
        sharedPrefEditor.putString(context.getString(R.string.data_password), Token.password);
        sharedPrefEditor.apply();

        SharedPreferences sharedKey = context.getSharedPreferences(context.getString(R.string.key_directory), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedKeyEditor = sharedKey.edit();
        sharedKeyEditor.putString(context.getString(R.string.key_token), "");
        sharedKeyEditor.putString(context.getString(R.string.key_type), "");
        sharedKeyEditor.apply();
    }
}

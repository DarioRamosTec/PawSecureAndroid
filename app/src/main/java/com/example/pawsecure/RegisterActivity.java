package com.example.pawsecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pawsecure.models.Message;
import com.example.pawsecure.services.UsersService;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.buttonLoginRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
        findViewById(R.id.buttonSignUpRegister).setOnClickListener(this);
    }

    void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    void goToEmail() {
        Intent intent = new Intent(this, EmailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://paw-secure.jafetguzman.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UsersService usersRequest = retrofit.create(UsersService.class);
        usersRequest.register().enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    Message message = response.body();
                } else {
                    try {
                        Message message = new Gson().fromJson(response.errorBody().string(), Message.class);
                        Log.d("UTT", message.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }
}
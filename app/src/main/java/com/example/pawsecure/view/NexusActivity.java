package com.example.pawsecure.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.NexusViewModel;

public class NexusActivity extends PawSecureActivity {

    NexusViewModel nexusViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nexus);
        nexusViewModel = new ViewModelProvider(this).get(NexusViewModel.class);
        nexusViewModel.spaces(Token.getBearer()).observe(this, new PawSecureObserver<SpaceResponse>(this, new NexusActivity.ObserveSpaces()));
        //hidecurtain
    }

    class ObserveSpaces extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<SpaceResponse> {

        @Override
        public void onChanged(SpaceResponse spaceResponse) {
            switch (spaceResponse.code) {
                case "403":
                case "401":
                    checkAuth();
                    break;
                case "200":

                    break;
            }
        }
    }
}
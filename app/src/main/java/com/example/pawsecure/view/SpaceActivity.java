package com.example.pawsecure.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.model.Space;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.SpaceViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SpaceActivity extends PawSecureActivity implements View.OnClickListener {

    LinearLayout linearNotLinked;
    LinearLayout linearLinked;
    Button linkButtonSpace;
    SpaceViewModel spaceViewModel;
    int spaceId;
    Space space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);

        Bundle bundle = getIntent().getExtras();
        spaceId = (int) bundle.get("SPACE_ID");

        linearNotLinked = findViewById(R.id.linearNotLinked);
        linearLinked = findViewById(R.id.linearLinked);
        linkButtonSpace = findViewById(R.id.linkButtonSpace);

        establishCurtain(findViewById(R.id.progressSpace), findViewById(R.id.curtainSpace));
        showCurtain(new Button[] {});
        spaceViewModel = new ViewModelProvider(this).get(SpaceViewModel.class);
        spaceViewModel.space(Token.getBearer(), spaceId).observe(this, new PawSecureObserver<SpaceResponse>(this, new SpaceOnChanged(this, this, spaceViewModel)));
        linkButtonSpace.setOnClickListener(this);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        startIntentForResult(LinkActivity.class);
    }

    class SpaceOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<SpaceResponse> {

        public SpaceOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
            super(context, pawSecureActivity, pawSecureViewModel);
        }

        @Override
        public void onChanged(SpaceResponse spaceResponse) {
            switch (spaceResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, pawSecureViewModel, pawSecureActivity);
                    break;
                case "200":
                    hideCurtain(new Button[] {});
                    space = spaceResponse.data.get(0);
                    if (space.linked == 1) {
                        linearLinked.setVisibility(View.VISIBLE);
                    } else {
                        linearNotLinked.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

}
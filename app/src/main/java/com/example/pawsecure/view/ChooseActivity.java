package com.example.pawsecure.view;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.PetAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureAnimator;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.PetResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.ChooseViewModel;

public class ChooseActivity extends PawSecureActivity {

    TextView textNothingChoose;
    RecyclerView recyclerGroupChoose;
    RecyclerView recyclerPetChoose;
    ChooseViewModel chooseViewModel;
    Toolbar topAppBarChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        establishCurtain(findViewById(R.id.progressIndicatorChoose), findViewById(R.id.viewBlackChoose));
        textNothingChoose = findViewById(R.id.textNothingChoose);
        recyclerGroupChoose = findViewById(R.id.recyclerGroupChoose);
        recyclerPetChoose = findViewById(R.id.recyclerPetChoose);
        textNothingChoose.setText("");

        chooseViewModel = new ViewModelProvider(this).get(ChooseViewModel.class);
        getPets();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getApplicationContext(), NexusActivity.class));
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        topAppBarChoose = findViewById(R.id.topAppBarChoose);
        topAppBarChoose.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NexusActivity.class));
                finish();
            }
        });
    }

    void getPets() {
        showCurtain(new Button[] {});
        chooseViewModel.pets(Token.getBearer()).observe(this, new PawSecureObserver<PetResponse>(this, new SpacesOnChanged(this, this, chooseViewModel)));
    }

    class SpacesOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<PetResponse> {

        public SpacesOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
            super(context, pawSecureActivity, pawSecureViewModel);
        }

        @Override
        public void onChanged(PetResponse petResponse) {
            switch (petResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, pawSecureViewModel, pawSecureActivity);
                    break;
                case "200":
                    hideCurtain(new Button[] {});
                    if (petResponse.data.size() == 0) {
                        textNothingChoose.setText(petResponse.message);
                        PawSecureAnimator.alpha(textNothingChoose, 1, 1200, 0).start();
                        PawSecureAnimator.translateY(textNothingChoose, 0, 1200, -150).start();
                    } else {
                        recyclerPetChoose.setAdapter(new PetAdapter(petResponse.data, pawSecureActivity));
                        recyclerPetChoose.setLayoutManager(new GridLayoutManager(pawSecureActivity, 2, RecyclerView.VERTICAL, false));
                        recyclerPetChoose.setHasFixedSize(true);
                    }
                    break;
            }
        }
    }

    @Override
    public void onAuth() {
        getPets();
        super.onAuth();
    }

}
package com.example.pawsecure.view;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.PetAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.model.Pet;
import com.example.pawsecure.response.PetResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.ChooseViewModel;

import java.util.ArrayList;
import java.util.List;

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

        amountDarkCurtain = 0f;
        chooseViewModel = new ViewModelProvider(this).get(ChooseViewModel.class);
        getPets();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        topAppBarChoose = findViewById(R.id.topAppBarChoose);
        topAppBarChoose.setNavigationOnClickListener(view -> finish());

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                if (intent != null) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        if (bundle.getBoolean("CHECK_PETS")) {
                            getPets();
                        }
                    }
                }
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
                        List<Pet> petListCreate = new ArrayList<Pet>();
                        petListCreate.add(new Pet.PetCreate());
                        recyclerPetChoose.setAdapter(new PetAdapter(petListCreate, pawSecureActivity));
                    } else {
                        petResponse.data.add(0, new Pet.PetCreate());
                        recyclerPetChoose.setAdapter(new PetAdapter(petResponse.data, pawSecureActivity));
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(pawSecureActivity, 2, RecyclerView.VERTICAL, false);
                    recyclerPetChoose.setLayoutManager(gridLayoutManager);
                    recyclerPetChoose.setHasFixedSize(true);
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
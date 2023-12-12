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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.PetAdapter;
import com.example.pawsecure.adapter.PetIconAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.model.Pet;
import com.example.pawsecure.response.PetResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.ChooseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ChooseActivity extends PawSecureActivity {

    TextView textNothingChoose;
    RecyclerView recyclerGroupChoose;
    RecyclerView recyclerPetChoose;
    ChooseViewModel chooseViewModel;
    Toolbar topAppBarChoose;
    LinearLayout linearChoose;
    List<Pet> petListGroup;
    FloatingActionButton fabChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        establishCurtain(findViewById(R.id.progressIndicatorChoose), findViewById(R.id.viewBlackChoose));
        textNothingChoose = findViewById(R.id.textNothingChoose);
        recyclerGroupChoose = findViewById(R.id.recyclerGroupChoose);
        recyclerPetChoose = findViewById(R.id.recyclerPetChoose);
        linearChoose = findViewById(R.id.linearChoose);
        fabChoose = findViewById(R.id.fabChoose);
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

        recyclerGroupChoose.setAdapter(new PetIconAdapter(new ArrayList<>(), this));
        recyclerGroupChoose.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerGroupChoose.setHasFixedSize(true);

        fabChoose.setEnabled(false);
        fabChoose.setOnClickListener(view -> {
            List<Pet> petListGroup = ((PetIconAdapter)recyclerGroupChoose.getAdapter()).getPetList();
            Intent intent = new Intent(this, LinkActivity.class);
            int[] ints = new int[petListGroup.size()];
            for (int i = 0; i < petListGroup.size() ; i++) {
                ints[i] = petListGroup.get(i).id;
            }
            intent.putExtra("IDS_SPACE", ints);
            startActivity(intent);
        });

    }

    void getPets() {
        showCurtain(new Button[] {});
        chooseViewModel.pets(Token.getBearer()).observe(this, new PawSecureObserver<PetResponse>(this, new SpacesOnChanged(this, this, chooseViewModel)));
    }

    public void selectPet(Pet pet) {
        if (linearChoose.getVisibility() == View.GONE) {
            linearChoose.setVisibility(View.VISIBLE);
        }
        List<Pet> pets = ((PetIconAdapter)recyclerGroupChoose.getAdapter()).getPetList();
        if (pets.contains(pet)) {
            int index = pets.indexOf(pet);
            pets.remove(index);
            recyclerGroupChoose.getAdapter().notifyItemRemoved(index);
        } else {
            ((PetIconAdapter)recyclerGroupChoose.getAdapter()).getPetList().add(pet);
            recyclerGroupChoose.getAdapter().notifyItemInserted(recyclerGroupChoose.getAdapter().getItemCount() - 1);
        }
        if (pets.size() == 0) {
            linearChoose.setVisibility(View.GONE);
            fabChoose.setEnabled(false);
        } else {
            fabChoose.setEnabled(true);
        }
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
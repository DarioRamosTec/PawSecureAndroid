package com.example.pawsecure.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.pawsecure.R;
import com.example.pawsecure.fragment.ImagePetBottomSheet;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.CreatePetViewModel;
import com.example.pawsecure.view_model.CreateSpaceViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateSpaceActivity extends PawSecureActivity implements View.OnClickListener {

    TextInputLayout inputNameCreatePet;
    TextInputLayout inputDescriptionCreatePet;
    List<TextInputLayout> inputLayoutList;
    TextInputEditText editNameCreatePet;
    TextInputEditText editDescriptionCreatePet;
    CreateSpaceViewModel createSpaceViewModel;
    MaterialToolbar topBarCreateSpace;
    Button buttonCreateSpace;
    RecyclerView recyclerCreateSpace;
    ArrayList<Integer> ids;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_space);

        ids = new ArrayList<>();
        if (getIntent().getExtras() != null) {
            int[] idsSpaces = (int[]) getIntent().getExtras().get("IDS_SPACE");
            if (idsSpaces != null) {
                for (int i = 0; i < idsSpaces.length; i++) {
                    ids.add(idsSpaces[i]);
                }
            }
        }

        inputNameCreatePet = findViewById(R.id.inputNameCreatePet);
        inputDescriptionCreatePet = findViewById(R.id.inputDescriptionCreatePet);
        editNameCreatePet = findViewById(R.id.editNameCreatePet);
        editDescriptionCreatePet = findViewById(R.id.editDescriptionCreatePet);
        topBarCreateSpace = findViewById(R.id.topBarCreateSpace);
        buttonCreateSpace = findViewById(R.id.buttonCreateSpace);
        recyclerCreateSpace = findViewById(R.id.recyclerCreateSpace);

        inputLayoutList = new ArrayList<TextInputLayout>() {{
            add(inputNameCreatePet);
            add(inputDescriptionCreatePet);
        }};

        createSpaceViewModel = new ViewModelProvider(this).get(CreateSpaceViewModel.class);
        establishCurtain(findViewById(R.id.progressCreateSpace), findViewById(R.id.curtainCreateSpace));
        topBarCreateSpace.setNavigationOnClickListener(view -> finish());

        buttonCreateSpace.setOnClickListener(this);

        //recycler
    }

    @Override
    public void onClick(View view) {
        showCurtain(new Button[] { buttonCreateSpace });
        store();
    }

    void store() {
        String name = editNameCreatePet.getText().toString();
        String description = editDescriptionCreatePet.getText().toString();
        createSpaceViewModel.space(Token.getBearer(), name, description).observe(this, new PawSecureObserver<SpaceResponse>(this, new SpaceOnChanged(this, this, createSpaceViewModel)));
    }

    void pivot() {
        createSpaceViewModel.petSpace(Token.getBearer(), id, ids).observe(this, new PawSecureObserver<SpaceResponse>(this, new PetSpaceOnChanged(this, this, createSpaceViewModel)));
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
                case "400":
                    hideCurtain(new Button[] { buttonCreateSpace });
                    checkValidationTextInput(spaceResponse.errors.name, inputNameCreatePet);
                    checkValidationTextInput(spaceResponse.errors.description, inputDescriptionCreatePet);
                    break;
                case "201":
                    for (TextInputLayout input : inputLayoutList ) {
                        setNullTextInput(input);
                    }
                    id = spaceResponse.data.get(0).id;
                    pivot();
                    break;
            }
        }
    }

    static class PetSpaceOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<SpaceResponse> {
        public PetSpaceOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
            super(context, pawSecureActivity, pawSecureViewModel);
        }

        @Override
        public void onChanged(SpaceResponse spaceResponse) {
            switch (spaceResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, pawSecureViewModel, pawSecureActivity);
                    break;
                case "201":
                    pawSecureActivity.restartActivities(NexusActivity.class);
                    break;
            }
        }
    }

    @Override
    public void onAuth() {
        if (id == 0) {
            store();
        } else {
            pivot();
        }
        super.onAuth();
    }
}
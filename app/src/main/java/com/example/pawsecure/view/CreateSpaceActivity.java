package com.example.pawsecure.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import com.example.pawsecure.response.PetResponse;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_space);

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

        //recycler

        //buttonCreateSpace.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        showCurtain(new Button[] { buttonCreateSpace });
        //createSpaceViewModel.pet(Token.getBearer(), editNameMakePet.getText().toString(), null,
        //        null, iconPet, imageEncoded, animalPet, editDateMakePet.getText().toString(),
        //        editDescriptionMakePet.getText().toString()).observe(this, new PawSecureObserver<PetResponse>(this, new CreatePetActivity.PetOnChanged(this, this, createPetViewModel)));
    }
}
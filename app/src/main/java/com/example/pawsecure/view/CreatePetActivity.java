package com.example.pawsecure.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.PetAdapter;
import com.example.pawsecure.fragment.ImagePetBottomSheet;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.model.Pet;
import com.example.pawsecure.response.PetResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.CreatePetViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreatePetActivity extends PawSecureActivity implements View.OnClickListener {

    TextInputLayout inputNameMakePet;
    TextInputLayout inputDateMakePet;
    TextInputLayout inputDescriptionMakePet;
    List<TextInputLayout> inputLayoutList;
    TextInputEditText editNameMakePet;
    TextInputEditText editDescriptionMakePet;
    TextInputEditText editDateMakePet;
    CreatePetViewModel createPetViewModel;
    MaterialToolbar topBarMakePet;
    Button buttonMakePet;
    Button buttonImageCreatePet;
    FloatingActionButton fabMakePet;
    ImageView imageMakePet;
    ImageView iconMakePet;
    MaterialDatePicker<Long> materialDatePicker;
    FrameLayout bottomSheetMakePet;
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    ImagePetBottomSheet imagePetBottomSheet;
    public boolean notUseFab = true;
    int iconPet;
    int animalPet = 1;
    ConstraintLayout constraintCreatePet;
    Snackbar snackbar;
    String imageEncoded = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pet);

        fabMakePet = findViewById(R.id.fabMakePet);
        topBarMakePet = findViewById(R.id.topBarMakePet);
        buttonMakePet = findViewById(R.id.buttonMakePet);
        imageMakePet = findViewById(R.id.imageMakePet);
        iconMakePet = findViewById(R.id.iconMakePet);
        editDateMakePet = findViewById(R.id.editDateMakePet);
        editDescriptionMakePet = findViewById(R.id.editDescriptionMakePet);
        editNameMakePet = findViewById(R.id.editNameMakePet);
        inputNameMakePet = findViewById(R.id.inputNameMakePet);
        inputDateMakePet = findViewById(R.id.inputDateMakePet);
        inputDescriptionMakePet = findViewById(R.id.inputDescriptionMakePet);
        constraintCreatePet = findViewById(R.id.constraintCreatePet);
        buttonImageCreatePet = findViewById(R.id.buttonImageCreatePet);

        inputLayoutList = new ArrayList<TextInputLayout>() {{
            add(inputNameMakePet);
            add(inputDateMakePet);
            add(inputDescriptionMakePet);
        }};

        createPetViewModel = new ViewModelProvider(this).get(CreatePetViewModel.class);

        establishCurtain(findViewById(R.id.progressMakePet), findViewById(R.id.curtainMakePet));
        topBarMakePet.setNavigationOnClickListener(view -> finish());

        materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker_title)
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build())
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String text = localDateTime.format(formatter);
            editDateMakePet.setText(text);
        });
        editDateMakePet.setOnClickListener(view -> {
            if (!materialDatePicker.isAdded()) {
                materialDatePicker.show(getSupportFragmentManager(), null);
            }
        });

        imagePetBottomSheet = new ImagePetBottomSheet(this);
        fabMakePet.setOnClickListener(view -> {
            if (notUseFab) {
                imagePetBottomSheet.show(getSupportFragmentManager(), null);
                notUseFab = false;
            }
            });

        buttonMakePet.setOnClickListener(this);
        snackbar = Snackbar.make(this, constraintCreatePet, getResources().getText(R.string.create_pet_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, this);


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()  == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        } catch (IOException e) {

                        }
                        if (selectedImageBitmap != null) {
                            imageMakePet.setImageBitmap(selectedImageBitmap);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                            imageEncoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                        }                        
                    }
                }
            }
        });

        buttonImageCreatePet.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });

    }

    public void changeIcon (int position, int icon, int animal) {
        iconMakePet.setImageDrawable(AppCompatResources.getDrawable(this, icon));
        iconPet = position;
        animalPet = animal;
        imagePetBottomSheet.dismiss();
    }

    @Override
    public void onClick(View view) {
        showCurtain(new Button[] { buttonMakePet });
        createPetViewModel.pet(Token.getBearer(), editNameMakePet.getText().toString(), null,
                null, iconPet, imageEncoded, animalPet, editDateMakePet.getText().toString(),
                editDescriptionMakePet.getText().toString()).observe(this, new PawSecureObserver<PetResponse>(this, new PetOnChanged(this, this, createPetViewModel)));
    }

    void sendToastError() {
        snackbar.show();
    }

    void sendToastErrorImage(String error) {
        Snackbar snackbar = Snackbar.make(this, constraintCreatePet, error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    class PetOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<PetResponse> {

        public PetOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
            super(context, pawSecureActivity, pawSecureViewModel);
        }

        @Override
        public void onChanged(PetResponse petResponse) {
            switch (petResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, pawSecureViewModel, pawSecureActivity);
                    break;
                case "400":
                    hideCurtain(new Button[] { buttonMakePet });
                    checkValidationTextInput(petResponse.errors.name, inputNameMakePet);
                    checkValidationTextInput(petResponse.errors.description, inputDescriptionMakePet);
                    checkValidationTextInput(petResponse.errors.birthday, inputDateMakePet);
                    String errorField = petResponse.errors.image != null && petResponse.errors.image.size() > 0 ? petResponse.errors.image.get(0) : null;
                    if (errorField != null) {
                        sendToastErrorImage(errorField);
                    }
                    break;
                case "201":
                    hideCurtain(new Button[] { buttonMakePet });
                    for (TextInputLayout input : inputLayoutList ) {
                        setNullTextInput(input);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("CHECK_PETS", true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onAuth() {
        sendToastError();
        super.onAuth();
    }
}
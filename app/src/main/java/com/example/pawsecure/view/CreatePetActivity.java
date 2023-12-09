package com.example.pawsecure.view;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    FloatingActionButton fabMakePet;
    ImageView imageMakePet;
    ImageView iconMakePet;
    MaterialDatePicker<Long> materialDatePicker;
    FrameLayout bottomSheetMakePet;
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    ImagePetBottomSheet imagePetBottomSheet;
    public boolean notUseFab = true;
    int iconPet;
    int animalPet;
    ConstraintLayout constraintCreatePet;
    Snackbar snackbar;

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
                null, iconPet, null, animalPet, editDateMakePet.getText().toString(),
                editDescriptionMakePet.getText().toString()).observe(this, new PawSecureObserver<PetResponse>(this, new PetOnChanged(this, this, createPetViewModel)));
    }

    void sendToastError() {
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
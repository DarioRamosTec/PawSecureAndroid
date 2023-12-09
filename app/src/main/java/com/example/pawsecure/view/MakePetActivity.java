package com.example.pawsecure.view;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.pawsecure.R;
import com.example.pawsecure.fragment.ImagePetBottomSheet;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.view_model.MakePetViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;

public class MakePetActivity extends PawSecureActivity {

    TextInputLayout inputNameMakePet;
    TextInputLayout inputDateMakePet;
    TextInputLayout inputDescriptionMakePet;
    TextInputEditText editNameMakePet;
    TextInputEditText editDescriptionMakePet;
    TextInputEditText editDateMakePet;
    MakePetViewModel makePetViewModel;
    MaterialToolbar topBarMakePet;
    Button buttonMakePet;
    FloatingActionButton fabMakePet;
    ImageView imageMakePet;
    ImageView iconMakePet;
    MaterialDatePicker<Long> materialDatePicker;
    FrameLayout bottomSheetMakePet;
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    ImagePetBottomSheet imagePetBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_pet);

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
        //bottomSheetMakePet = findViewById(R.id.bottomSheetMakePet);

        makePetViewModel = new ViewModelProvider(this).get(MakePetViewModel.class);

        establishCurtain(findViewById(R.id.progressMakePet), findViewById(R.id.curtainMakePet));
        topBarMakePet.setNavigationOnClickListener(view -> finish());

        materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker_title)
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build())
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            editDateMakePet.setText(simpleDateFormat.format(selection));
        });
        editDateMakePet.setOnClickListener(view -> materialDatePicker.show(getSupportFragmentManager(), null));

        //bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetMakePet);
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        imagePetBottomSheet = new ImagePetBottomSheet(this);
        fabMakePet.setOnClickListener(view -> imagePetBottomSheet.show(getSupportFragmentManager(), null));

    }
}
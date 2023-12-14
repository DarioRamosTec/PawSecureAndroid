package com.example.pawsecure.view;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.SpaceAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureAnimator;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.model.User;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.UserResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.ProfileViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileActivity extends PawSecureActivity {

    MaterialToolbar topBarProfile;
    Button changePasswordProfile;
    Button textSaveProfile;
    Button logoutProfile;
    ProfileViewModel profileViewModel;
    User user;
    TextView desc;
    TextView mail;
    TextView name;
    EditText editLastNameProfile;
    EditText editMiddleNameProfile;
    EditText editNameProfile;
    TextInputLayout inputLastNameProfile;
    TextInputLayout inputMiddleNameProfile;
    TextInputLayout inputNameProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        topBarProfile = findViewById(R.id.topBarProfile);
        topBarProfile.setNavigationOnClickListener(view -> finish());

        establishCurtain(findViewById(R.id.progressProfile), findViewById(R.id.curtainProfile));
        me();

        desc = findViewById(R.id.nose);
        mail = findViewById(R.id.correo);
        name = findViewById(R.id.user);
        inputLastNameProfile = findViewById(R.id.inputLastNameProfile);
        inputMiddleNameProfile = findViewById(R.id.inputMiddleNameProfile);
        inputNameProfile = findViewById(R.id.inputNameProfile);
        editLastNameProfile = findViewById(R.id.editLastNameProfile);
        editMiddleNameProfile = findViewById(R.id.editMiddleNameProfile);
        editNameProfile = findViewById(R.id.editNameProfile);
        logoutProfile = findViewById(R.id.logoutProfile);
        changePasswordProfile = findViewById(R.id.changePasswordProfile);

        logoutProfile.setOnClickListener(view -> bye());
        changePasswordProfile.setOnClickListener(view -> save());
    }

    void me() {
        showCurtain(new Button[]{ findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile) });
        profileViewModel.index(Token.getBearer()).observe(this, new PawSecureObserver<UserResponse>(this, new ProfileActivity.MeOnChanged(this, this, profileViewModel)));
    }
    
    void save() {
        showCurtain(new Button[]{ findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile) });
        profileViewModel.update(Token.getBearer(), editNameProfile.getText().toString(), editMiddleNameProfile.getText().toString(), editLastNameProfile.getText().toString()).observe(this, new PawSecureObserver<UserResponse>(this, new ProfileActivity.MeOnChanged(this, this, profileViewModel)));
    }

    void bye() {
        showCurtain(new Button[]{ findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile) });
        profileViewModel.logout(Token.getBearer()).observe(this, new PawSecureObserver<UserResponse>(this, new ProfileActivity.LogoutOnChanged(this, this, profileViewModel)));
    }

    void changeData() {
        desc.setText(user.birthday);
        mail.setText(user.email);
        String stt = (user.name == null ? "" : user.name ) + " " + (user.middle_name == null ? "" : user.middle_name ) + " " + (user.last_name == null ? "" : user.last_name );
        name.setText(stt);
        editLastNameProfile.setText(user.last_name);
        editMiddleNameProfile.setText(user.middle_name);
        editNameProfile.setText(user.name);

    }

    class MeOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<UserResponse> {

        public MeOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
            super(context, pawSecureActivity, pawSecureViewModel);
        }

        @Override
        public void onChanged(UserResponse userResponse) {
            switch (userResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, pawSecureViewModel, pawSecureActivity);
                    break;
                case "404":
                case "400":
                    hideCurtain(new Button[]{ findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile) });
                    checkValidationTextInput(userResponse.errors.name, inputNameProfile);
                    checkValidationTextInput(userResponse.errors.middle_name, inputMiddleNameProfile);
                    checkValidationTextInput(userResponse.errors.last_name, inputLastNameProfile);
                    break;
                case "200":
                    hideCurtain(new Button[]{ findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile) });
                    checkValidationTextInput(null, inputNameProfile);
                    checkValidationTextInput(null, inputLastNameProfile);
                    checkValidationTextInput(null, inputMiddleNameProfile);
                    user = userResponse.data;
                    changeData();
                    break;
            }
        }
    }

    class LogoutOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<UserResponse> {

        public LogoutOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
            super(context, pawSecureActivity, pawSecureViewModel);
        }

        @Override
        public void onChanged(UserResponse userResponse) {
            switch (userResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, pawSecureViewModel, pawSecureActivity);
                    break;
                case "200":
                    hideCurtain(new Button[]{ findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile), findViewById(R.id.changePasswordProfile) });
                    Token.setGoodbye(pawSecureActivity);
                    restartActivities(StartActivity.class);
                    break;
            }
        }
    }

}
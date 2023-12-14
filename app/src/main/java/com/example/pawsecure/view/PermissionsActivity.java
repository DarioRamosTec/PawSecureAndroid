package com.example.pawsecure.view;

import static androidx.core.app.ActivityCompat.requestPermissions;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.PermissionAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecurePermission;
import com.google.android.material.materialswitch.MaterialSwitch;

public class PermissionsActivity extends PawSecureActivity {

    RecyclerView recyclerViewPermissions;
    ActivityResultLauncher<String> requestPermissionLauncher;
    MaterialSwitch materialSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        Toolbar topAppBar = findViewById(R.id.topAppBarPermission);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        recyclerViewPermissions = findViewById(R.id.recyclerViewPermissions);
        recyclerViewPermissions.setAdapter(new PermissionAdapter(this));
        recyclerViewPermissions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewPermissions.setHasFixedSize(true);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            materialSwitch.setChecked(isGranted);
        });
    }

    public void checkOutPermission (PawSecurePermission permission, MaterialSwitch switchObjective) {
        materialSwitch = switchObjective;
        requestPermissionLauncher.launch(permission.permission);
    }

}
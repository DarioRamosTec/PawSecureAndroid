package com.example.pawsecure.view;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.widget.Button;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.PetAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.model.Pet;
import com.example.pawsecure.response.PetResponse;
import com.example.pawsecure.view_model.LinkViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LinkActivity extends PawSecureActivity {

    MaterialToolbar topAppLink;
    Button buttonLink;
    int[] ids;
    Snackbar snackbar;
    ConstraintLayout constraintLink;
    BroadcastReceiver receiver;
    LinkViewModel linkViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        if (getIntent().getExtras() != null) {
            ids = (int[]) getIntent().getExtras().get("IDS_SPACE");
            //Intent intent = new Intent(this, CreateSpaceActivity.class);
            //intent.putExtra("IDS_SPACE", ids);
            //startActivity(intent);
        }

        linkViewModel = new ViewModelProvider(this).get(LinkViewModel.class);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
        topAppLink = findViewById(R.id.topAppLink);
        constraintLink = findViewById(R.id.constraintLink);
        buttonLink = findViewById(R.id.buttonLink);
        topAppLink.setNavigationOnClickListener(view -> finish());

        snackbar = Snackbar.make(this, constraintLink, getResources().getText(R.string.link_error), Snackbar.LENGTH_INDEFINITE).setAction(R.string.go_settings, view -> toSettings());

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    link();
                }
            }
        });

        buttonLink.setOnClickListener(view -> {
            String[] permissions;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions = new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION};
            } else {
                permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION};
            }
            boolean request = true;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                    request = false;
                    break;
                }
            }

            if (request) {
                link();
            } else {
                requestPermissions(permissions, 111223);
            }
        });

        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    try {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress();
                    } catch (SecurityException e) {

                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111223) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    continue;
                }
                granted = false;
                break;
            }
            if (granted) {
                link();
                return;
            }
            snackbar.show();
        }
    }

    void link() {
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activityResultLauncher.launch(intent);
            } else {
                try {
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    bluetoothAdapter.startDiscovery();
                    //linkViewModel.searchDevice(this, bluetoothAdapter).observe(this, new PawSecureObserver<Boolean>(this, new SearchOnChanged(this, this, linkViewModel, bluetoothAdapter)));
                } catch (SecurityException e) {

                }
            }
        }
    }

    class SearchOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<Boolean> {

        BluetoothAdapter bluetoothAdapter;

        public SearchOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel, BluetoothAdapter bluetoothAdapter) {
            super(context, pawSecureActivity, pawSecureViewModel);
            this.bluetoothAdapter = bluetoothAdapter;
        }


        @Override
        public void onChanged(Boolean aBoolean) {
            try {
                bluetoothAdapter.cancelDiscovery();
            } catch (SecurityException e) {

            }

        }
    }
}
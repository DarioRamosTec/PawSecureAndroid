package com.example.pawsecure.view;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Button;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.DevicesAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.view_model.LinkViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LinkActivity extends PawSecureActivity {

    MaterialToolbar topAppLink;
    Button searchButtonLink;
    Snackbar snackbar;
    ConstraintLayout constraintLink;
    BroadcastReceiver receiver;
    LinkViewModel linkViewModel;
    RecyclerView recyclerLink;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

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
        searchButtonLink = findViewById(R.id.searchButtonLink);
        recyclerLink = findViewById(R.id.recyclerLink);
        recyclerLink.setAdapter(new DevicesAdapter(new ArrayList<>(), this));
        recyclerLink.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerLink.setHasFixedSize(true);

        topAppLink.setNavigationOnClickListener(view -> finish());

        snackbar = Snackbar.make(this, constraintLink, getResources().getText(R.string.link_search_error), Snackbar.LENGTH_INDEFINITE).setAction(R.string.go_settings, view -> toSettings());

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    link();
                }
            }
        });

        searchButtonLink.setOnClickListener(view -> {
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
                        DevicesAdapter adapter = (DevicesAdapter) recyclerLink.getAdapter();
                        List<BluetoothDevice> bluetoothDevices = adapter.getList();
                        if (bluetoothDevices != null) {
                            bluetoothDevices.add(device);
                            adapter.notifyItemInserted(adapter.getItemCount() - 1);
                        }
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
                } catch (SecurityException e) {

                }
            }
        }
    }

    public void connectToDevice(BluetoothDevice bluetoothDevice) {

        try {
            if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_NONE) {
                Set<BluetoothDevice> bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice bt : bluetoothDeviceSet) {
                    //Method m = bt.getClass().getMethod("removeBond", (Class[]) null);
                    //m.invoke(bt, (Object[]) null);
                    bt.getUuids();
                }

            }
        } catch (SecurityException e) {
        }

        ((Runnable) () -> {
            try {
                connected(bluetoothDevice.createBond());
                ParcelUuid[] parcelUuids = bluetoothDevice.getUuids();
                for (ParcelUuid p : parcelUuids) {
                    BluetoothSocket bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(p.getUuid());
                    bluetoothSocket.connect();
                    try {
                        //
                        if (bluetoothSocket.isConnected()) {
                            OutputStream outputStream = bluetoothSocket.getOutputStream();
                            outputStream.write("Casanostra:8712832781:H1}".getBytes());
                            Log.d("UTT", "aa");
                        }
                    } catch (Exception e)  {

                    }
                }
            } catch (SecurityException | IOException e) {

            }
        }).run();

        /*
        BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        try {
            bluetoothDevice.createBond();
            bluetoothLeScanner.startScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if (Objects.equals(result.getDevice().getAddress(), bluetoothDevice.getAddress())) {
                        List<ParcelUuid> uuids = result.getScanRecord().getServiceUuids();
                    }
                }
            });
        } catch (SecurityException e) {

        }


         */
    }

    public void connected(boolean success) {
        if (success) {
            //startActivity(new Intent(this, ProfileActivity.class));
            //finish();
        } else {
            snackbar = Snackbar.make(this, constraintLink, getResources().getText(R.string.link_linking_error), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
}
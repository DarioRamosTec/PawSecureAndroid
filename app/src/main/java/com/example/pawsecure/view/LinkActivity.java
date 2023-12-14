package com.example.pawsecure.view;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.widget.Button;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.DevicesAdapter;
import com.example.pawsecure.fragment.WifiLinkDialog;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.LinkViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LinkActivity extends PawSecureActivity  {

    MaterialToolbar topAppLink;
    Button searchButtonLink;
    Button configureButtonLink;
    Snackbar snackbar;
    ConstraintLayout constraintLink;
    BroadcastReceiver receiver;
    LinkViewModel linkViewModel;
    RecyclerView recyclerLink;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    WifiLinkDialog wifiLinkDialog;
    public String password;
    public String ssid;
    int spaceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        Bundle bundle = getIntent().getExtras();
        spaceId = (int) bundle.get("SPACE_ID");

        establishCurtain(findViewById(R.id.progressLink), findViewById(R.id.curtainLink), findViewById(R.id.textCurtainLink));
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
        configureButtonLink = findViewById(R.id.configureButtonLink);
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
            search();
        });
        configureButtonLink.setOnClickListener(view -> wifi());

        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    try {
                        DevicesAdapter adapter = (DevicesAdapter) recyclerLink.getAdapter();
                        List<BluetoothDevice> bluetoothDevices = adapter.getList();
                        if (bluetoothDevices != null) {
                            if (!bluetoothDevices.contains(device)) {
                                bluetoothDevices.add(device);
                                adapter.notifyItemInserted(adapter.getItemCount() - 1);
                            }
                        }
                    } catch (SecurityException e) {

                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        this.ssid = ((WifiManager)getSystemService(WIFI_SERVICE)).getConnectionInfo().getSSID().replaceAll("\"", "");
        search();
        wifi();
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
                search();
                return;
            }
            snackbar.show();
        }
    }

    void search() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions = new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE};
        } else {
            permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE};
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

    public void boundToDevice(BluetoothDevice bluetoothDevice) {
        showCurtain(new Button[] { searchButtonLink, configureButtonLink });
        curtainText(getString(R.string.link_bond));
        try {
            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                connectToDevice(bluetoothDevice.createBond(), bluetoothDevice);
            } else {
                connectToDevice(true, bluetoothDevice);
            }
        } catch (SecurityException e) {}
    }

    public void connectToDevice(boolean success, BluetoothDevice bluetoothDevice) {
        AtomicBoolean finish = new AtomicBoolean(false);
        if (success) {
            ((Runnable) () -> {
                try {
                    if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        curtainText(getString(R.string.link_uuid));
                        ParcelUuid[] parcelUuids = bluetoothDevice.getUuids();
                        if (parcelUuids != null) {
                            for (ParcelUuid p : parcelUuids) {
                                BluetoothSocket bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(p.getUuid());
                                curtainText(getString(R.string.link_connect));
                                bluetoothSocket.connect();
                                if (bluetoothSocket.isConnected()) {
                                    curtainText(getString(R.string.link_reading));
                                    OutputStream outputStream = bluetoothSocket.getOutputStream();
                                    String stt = this.ssid+":"+this.password+":"+"H1}";
                                    curtainText(getString(R.string.link_writing));
                                    outputStream.write(stt.getBytes());
                                    curtainText(getString(R.string.link_saving));
                                    linkViewModel.link(Token.getBearer(), spaceId, bluetoothDevice.getAddress())
                                                .observe(this, new PawSecureObserver<SpaceResponse>(this, new LinkActivity.LinkOnChanged(this, this, linkViewModel)));
                                    finish.set(true);
                                }
                            }
                        }
                    }
                } catch (SecurityException | IOException e) {}
            }).run();
        }

        if (!finish.get()) {
            hideCurtain(new Button[] { searchButtonLink, configureButtonLink });
            snackbar = Snackbar.make(this, constraintLink, getResources().getText(R.string.link_linking_error), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    void wifi() {
        if (wifiLinkDialog == null || !wifiLinkDialog.isAdded()) {
            wifiLinkDialog = new WifiLinkDialog(this, this.ssid, this.password);
            wifiLinkDialog.show(getSupportFragmentManager(), null);
        }
    }

    public void defineWifi(String ssid, String password) {
        this.ssid = ssid;
        this.password = password;
    }

    class LinkOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<SpaceResponse> {

        public LinkOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
            super(context, pawSecureActivity, pawSecureViewModel);
        }

        @Override
        public void onChanged(SpaceResponse spaceResponse) {
            switch (spaceResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, pawSecureViewModel, pawSecureActivity);
                    break;
                default:
                    hideCurtain(new Button[] { searchButtonLink, configureButtonLink });
                    break;
                case "200":
                    //Intent intent = new Intent();
                    //intent.putExtra("CHECK_SPACE", true);
                    //setResult(Activity.RESULT_OK, intent);
                    //finish();
                    snackbar = Snackbar.make(pawSecureActivity, constraintLink, getResources().getText(R.string.link_linking_sucess), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    break;
            }
        }
    }
}
package com.example.pawsecure.view;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.ParcelUuid;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.DevicesAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureDeviceFind;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.model.Pet;
import com.example.pawsecure.model.Sensor;
import com.example.pawsecure.model.Space;
import com.example.pawsecure.response.SensorResponse;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.SpaceSensorResponse;
import com.example.pawsecure.singletone.ImagePetManager;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.SpaceViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.sidesheet.SideSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpaceActivity extends PawSecureActivity implements View.OnClickListener, OnMapReadyCallback, PawSecureDeviceFind {

    LinearLayout linearNotLinked;
    LinearLayout linearLinkedSpace;
    LinearLayout linearSensors;
    Button linkButtonSpace;
    SpaceViewModel spaceViewModel;
    int spaceId;
    Space space;
    MaterialToolbar topBarCreateSpace;
    TabLayout tabLayoutSpace;
    SupportMapFragment mapSpace;
    GoogleMap googleMap;
    SwipeRefreshLayout refreshLayout;
    FrameLayout bottomSheetSpace;
    Button reloadButtonSensor;
    Map<String, List<Sensor>> listMap;
    BluetoothDevice bluetoothDeviceTarget;
    TextView temperatureUpdate;
    TextView temperatureValue;
    TextView humidityValue;
    TextView humidityUpdate;
    TextView soundValue;
    TextView soundUpdate;
    TextView gasUpdate;
    TextView gasValue;
    TextView motionUpdate;
    TextView motionValue;
    TextView presenceUpdate;
    TextView presenceValue;
    TextView positionUpdate;
    TextView positionValue;
    List<Sensor> sensorList;
    Chip chipBuzzerSpace;
    Chip chipFanSpace;
    Chip chipLightSpace;
    ConstraintLayout constraintSpace;
    Chip chipTargetSpace;
    int tabId = 0;
    Boolean chipTargetIsInSite;
    View sideSheetSpace;
    SideSheetBehavior<View> sideSheetBehavior;
    RecyclerView recyclerSheetSpace;
    BroadcastReceiver receiver;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);

        Bundle bundle = getIntent().getExtras();
        spaceId = (int) bundle.get("SPACE_ID");

        chipBuzzerSpace = findViewById(R.id.chipBuzzerSpace);
        chipLightSpace = findViewById(R.id.chipLightSpace);
        chipFanSpace = findViewById(R.id.chipFanSpace);
        linearNotLinked = findViewById(R.id.linearNotLinked);
        linearLinkedSpace = findViewById(R.id.linearLinkedSpace);
        linkButtonSpace = findViewById(R.id.linkButtonSpace);
        topBarCreateSpace = findViewById(R.id.topBarCreateSpace);
        tabLayoutSpace = findViewById(R.id.tabLayoutSpace);
        refreshLayout = findViewById(R.id.refreshLayout);
        bottomSheetSpace = findViewById(R.id.bottomSheetSpace);
        linearSensors = findViewById(R.id.linearSensors);
        chipTargetSpace = findViewById(R.id.chipTargetSpace);
        mapSpace = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapSpace);

        establishCurtain(findViewById(R.id.progressSpace), findViewById(R.id.curtainSpace));
        spaceViewModel = new ViewModelProvider(this).get(SpaceViewModel.class);
        spaceViewModel.space(Token.getBearer(), spaceId).observe(this, new PawSecureObserver<SpaceResponse>(this, new SpaceOnChanged(this, this, spaceViewModel)));
        linkButtonSpace.setOnClickListener(this);
        topBarCreateSpace.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuSpaceLink) {
                    goToLink();
                    return true;
                } else if (item.getItemId() == R.id.menuSpaceDevice) {
                    goToDevice();
                    return true;
                }
                return false;
            }
        });
        topBarCreateSpace.setNavigationOnClickListener(view -> finish());

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Bundle bundle = intent.getExtras();
                        if (bundle != null) {
                            if (bundle.getBoolean("CHECK_SPACE")) {
                                linearLinkedSpace.setVisibility(View.VISIBLE);
                                linearNotLinked.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
            }
        });

        mapSpace.getMapAsync(this);
        refreshLayout.setEnabled(false);

        BottomSheetBehavior<FrameLayout> standardBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetSpace);
        standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        standardBottomSheetBehavior.setPeekHeight(140);
        standardBottomSheetBehavior.setHideable(false);

        View child = getLayoutInflater().inflate(R.layout.layout_sensors, null);
        linearSensors.addView(child);
        reloadButtonSensor = linearSensors.findViewById(R.id.reloadButtonSensor);
        showCurtain(new Button[] { reloadButtonSensor });

        temperatureUpdate = linearSensors.findViewById(R.id.temperatureUpdate);
        temperatureValue = linearSensors.findViewById(R.id.temperatureValue);
        humidityValue = linearSensors.findViewById(R.id.humidityValue);
        humidityUpdate = linearSensors.findViewById(R.id.humidityUpdate);
        soundValue = linearSensors.findViewById(R.id.soundValue);
        soundUpdate = linearSensors.findViewById(R.id.soundUpdate);
        gasUpdate = linearSensors.findViewById(R.id.gasUpdate);
        gasValue = linearSensors.findViewById(R.id.gasValue);
        motionUpdate = linearSensors.findViewById(R.id.motionUpdate);
        motionValue = linearSensors.findViewById(R.id.motionValue);
        presenceUpdate = linearSensors.findViewById(R.id.presenceUpdate);
        presenceValue = linearSensors.findViewById(R.id.presenceValue);
        positionUpdate = linearSensors.findViewById(R.id.positionUpdate);
        positionValue = linearSensors.findViewById(R.id.positionValue);

        reloadButtonSensor.setOnClickListener(view -> {
            reloadAllData();
        });

        tabLayoutSpace.setEnabled(false);
        reloadButtonSensor.setEnabled(false);
        chipTargetSpace.setEnabled(false);

        chipTargetSpace.setOnClickListener(view -> {
            if (!chipTargetIsInSite) {
                setTarget();
            }
            chipTargetSpace.setChecked(true);
            chipTargetIsInSite = true;
        });

        chipBuzzerSpace.setOnClickListener(view -> {
            connectToDevice("B");
        });

        chipFanSpace.setOnClickListener(view -> {
            connectToDevice("V");
        });

        chipLightSpace.setOnClickListener(view -> {
            connectToDevice("L");
        });

        bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

        sideSheetSpace = findViewById(R.id.sideSheetSpace);
        sideSheetBehavior = SideSheetBehavior.from(sideSheetSpace);
        sideSheetBehavior.setState(SideSheetBehavior.STATE_HIDDEN);
        sideSheetBehavior.setDraggable(true);

        recyclerSheetSpace = findViewById(R.id.recyclerSheetSpace);
        recyclerSheetSpace.setAdapter(new DevicesAdapter(new ArrayList<>(), this));
        recyclerSheetSpace.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerSheetSpace.setHasFixedSize(true);

        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    try {
                        DevicesAdapter adapter = (DevicesAdapter) recyclerSheetSpace.getAdapter();
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
        search();
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
            Snackbar.make(this, constraintSpace, getResources().getText(R.string.link_search_error), Snackbar.LENGTH_INDEFINITE).setAction(R.string.go_settings, view -> toSettings());
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
        try {
            bluetoothDeviceTarget = bluetoothDevice;
            bluetoothDeviceTarget.createBond();
        } catch (SecurityException e) {}
    }

    public void connectToDevice(String value) {
        ((Runnable) () -> {
            try {
                if (bluetoothDeviceTarget != null) {
                    ParcelUuid[] parcelUuids = bluetoothDeviceTarget.getUuids();
                    if (parcelUuids != null) {
                        for (ParcelUuid p : parcelUuids) {
                            BluetoothSocket bluetoothSocket = bluetoothDeviceTarget.createRfcommSocketToServiceRecord(p.getUuid());
                            bluetoothSocket.connect();
                            if (bluetoothSocket.isConnected()) {
                                OutputStream outputStream = bluetoothSocket.getOutputStream();
                                String stt = value;
                                outputStream.write(stt.getBytes());
                            }
                        }
                    }
                }
            } catch (SecurityException | IOException e) {
            }
        }).run();
    }

    void goToDevice() {
        sideSheetBehavior.expand();
    }

    @Override
    public void onClick(View view) {
        goToLink();
    }

    void goToLink() {
        Intent intent = new Intent(this, LinkActivity.class);
        intent.putExtra("SPACE_ID", spaceId);
        activityResultLauncher.launch(intent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    void setMap(double latitude, double longitude, String hour) {
        if (this.googleMap != null) {
            this.googleMap.clear();
            LatLng marker = new LatLng(latitude, longitude);
            this.googleMap.addMarker(new MarkerOptions().position(marker).title(hour));
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
        }
    }

    void clearMap() {
        if (this.googleMap != null) {
            this.googleMap.clear();
        }
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
                case "200":
                    space = spaceResponse.data.get(0);
                    topBarCreateSpace.setTitle(space.name);
                    decideLink();
                    tabPets();
                    chipTargetSpace.setEnabled(true);
                    establishChip();
                    break;
            }
        }
    }
    
    void establishChip() {
        if ((space.target == null && tabId == 0) || (space.target != null && space.target == tabId) ) {
            chipTargetIsInSite = true;
            chipTargetSpace.setChecked(true);
        } else {
            chipTargetIsInSite = false;
            chipTargetSpace.setChecked(false);
        }
    }

    void decideLink() {
        hideCurtain(new Button[] { reloadButtonSensor });
        //space.linked == 1
        if (true) {
            linearLinkedSpace.setVisibility(View.VISIBLE);
            topBarCreateSpace.inflateMenu(R.menu.menu_space);
            establishAllSensors();
        } else {
            linearNotLinked.setVisibility(View.VISIBLE);
        }
    }
    void tabPets() {
        tabLayoutSpace.addTab(tabLayoutSpace.newTab().setText(getString(R.string.pet_none)).setId(0));
        for (Pet pet : space.pets) {
            tabLayoutSpace.addTab(tabLayoutSpace.newTab().setText(pet.name).setIcon(ImagePetManager.getIdIconPet(pet.icon)).setId(pet.id));
        }
        tabLayoutSpace.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabId = tab.getId();
                searchForData(tabId);
                establishChip();
                sensorList = listMap.get(String.valueOf(tabId));
                Sensor position = sensorList.get(6);
                if (position != null) {
                    String[] dataLL = position.data.split(",");
                    setMap(Double.parseDouble(dataLL[0]), Double.parseDouble(dataLL[1]), position.time);
                } else {
                    clearMap();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onAuth() {
        super.onAuth();
    }

    void establishAllSensors() {
        showCurtain(new Button[] { reloadButtonSensor });
        spaceViewModel.sensors(Token.getBearer(), space.id).observe(this, new PawSecureObserver<SpaceSensorResponse>(this, new SpaceSensorOnChanged(this, this, spaceViewModel)));
    }

    class SpaceSensorOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<SpaceSensorResponse> {

        public SpaceSensorOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
            super(context, pawSecureActivity, pawSecureViewModel);
        }

        @Override
        public void onChanged(SpaceSensorResponse spaceResponse) {
            switch (spaceResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, pawSecureViewModel, pawSecureActivity);
                    break;
                case "202":
                case "200":
                    hideCurtain(new Button[] { reloadButtonSensor });
                    listMap = spaceResponse.data.get(0);
                    searchForData(tabId);
                    tabLayoutSpace.setEnabled(true);
                    reloadButtonSensor.setEnabled(true);
                    break;
            }
        }
    }

    void searchForData(int id) {
        sensorList = listMap.get(String.valueOf(id));
        Sensor presence = sensorList.get(0);
        Sensor humidity = sensorList.get(1);
        Sensor sound = sensorList.get(2);
        Sensor temperature = sensorList.get(3);
        Sensor gas = sensorList.get(4);
        Sensor motion = sensorList.get(5);
        Sensor position = sensorList.get(6);

        Resources res = getResources();
        if (temperature != null) {
            temperatureUpdate.setText(String.format(getString(R.string.space_last), temperature.time));
            temperatureValue.setText(String.format(getString(R.string.sensor_value), String.valueOf(temperature.measure), temperature.sensor_type.unity));
        } else {
            temperatureUpdate.setText(String.format(getString(R.string.space_last), ""));
            temperatureValue.setText("");
        }

        if (humidity != null) {
            humidityUpdate.setText(String.format(getString(R.string.space_last), humidity.time));
            humidityValue.setText(String.format(getString(R.string.sensor_value), String.valueOf(humidity.measure), humidity.sensor_type.unity));
        } else {
            humidityUpdate.setText(String.format(getString(R.string.space_last), ""));
            humidityValue.setText("");
        }

        if (sound != null) {
            soundUpdate.setText(String.format(getString(R.string.space_last), sound.time));
            soundValue.setText(String.format(getString(R.string.sensor_string), (sound.measure == 1 ? getString(R.string.strong) : getString(R.string.weak))));
        } else {
            soundUpdate.setText(String.format(getString(R.string.space_last), ""));
            soundValue.setText("");
        }

        if (gas != null) {
            gasUpdate.setText(String.format(getString(R.string.space_last), gas.time));
            gasValue.setText(String.format(getString(R.string.sensor_string), (gas.measure == 1 ? getString(R.string.yes) : getString(R.string.no))));
        } else {
            gasUpdate.setText(String.format(getString(R.string.space_last), ""));
            gasValue.setText("");
        }

        if (motion != null) {
            motionUpdate.setText(String.format(getString(R.string.space_last), motion.time));
            motionValue.setText(String.format(getString(R.string.sensor_string), (motion.measure == 1 ? getString(R.string.strong) : getString(R.string.weak))));
        } else {
            motionUpdate.setText(String.format(getString(R.string.space_last), ""));
            motionValue.setText("");
        }

        if (presence != null) {
            presenceUpdate.setText(String.format(getString(R.string.space_last), presence.time));
            presenceValue.setText(String.format(getString(R.string.sensor_string), (presence.measure == 1 ? getString(R.string.on) : getString(R.string.off))));
        } else {
            presenceUpdate.setText(String.format(getString(R.string.space_last), ""));
            presenceValue.setText("");
        }

        if (position != null) {
            positionUpdate.setText(String.format(getString(R.string.space_last), position.time));
            String[] dataLL = position.data.split(",");
            setMap(Double.parseDouble(dataLL[0]), Double.parseDouble(dataLL[1]), position.time);
        } else {
            positionUpdate.setText(String.format(getString(R.string.space_last), ""));
            //positionValue.setText("");
        }
    }

    void setTarget() {
        showCurtain(new Button[] { reloadButtonSensor });
        tabLayoutSpace.setEnabled(false);
        chipTargetSpace.setEnabled(false);
        spaceViewModel.target(Token.getBearer(), space.id, (tabId == 0 ? null : tabId )).observe(this, new PawSecureObserver<SpaceResponse>(this, new TargetOnChanged(this, this, spaceViewModel)));
    }

    void reloadAllData() {
        reloadButtonSensor.setEnabled(false);
        for (int i = 0; i <= 6; i++) {
            reloadData(i);
        }
        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                reloadButtonSensor.setEnabled(true);
            }
        }.start();
    }

    class SensorOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<SensorResponse> {

        public SensorOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
            super(context, pawSecureActivity, pawSecureViewModel);
        }

        @Override
        public void onChanged(SensorResponse sensorResponse) {
            switch (sensorResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, pawSecureViewModel, pawSecureActivity);
                    break;
                case "202":
                case "200":
                    int sensorTab = (sensorResponse.data.pet == null ? 0 : sensorResponse.data.pet);
                    listMap.get(String.valueOf(sensorTab)).set(sensorResponse.data.sensor_type.id-1, sensorResponse.data);
                    if (tabId == sensorTab) {
                        searchForData(tabId);
                    }
                    break;
            }
        }
    }

    class TargetOnChanged extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<SpaceResponse> {

        public TargetOnChanged(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
            super(context, pawSecureActivity, pawSecureViewModel);
        }

        @Override
        public void onChanged(SpaceResponse spaceResponse) {
            switch (spaceResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, pawSecureViewModel, pawSecureActivity);
                    break;
                case "200":
                    tabLayoutSpace.setEnabled(true);
                    chipTargetSpace.setEnabled(true);
                    hideCurtain(new Button[] { reloadButtonSensor });
                    space = spaceResponse.data.get(0);
                    break;
            }
        }
    }

    void reloadData(int index) {
        switch (index) {
            case 0:
                spaceViewModel.sensor(Token.getBearer(), space.id, "presence")
                        .observe(this, new PawSecureObserver<SensorResponse>(this, new SensorOnChanged(this, this, spaceViewModel)));
                break;
            case 1:
                spaceViewModel.sensor(Token.getBearer(), space.id, "humidity")
                        .observe(this, new PawSecureObserver<SensorResponse>(this, new SensorOnChanged(this, this, spaceViewModel)));
                break;
            case 2:
                spaceViewModel.sensor(Token.getBearer(), space.id, "sound")
                        .observe(this, new PawSecureObserver<SensorResponse>(this, new SensorOnChanged(this, this, spaceViewModel)));
                break;
            case 3:
                spaceViewModel.sensor(Token.getBearer(), space.id, "temperature")
                        .observe(this, new PawSecureObserver<SensorResponse>(this, new SensorOnChanged(this, this, spaceViewModel)));
                break;
            case 4:
                spaceViewModel.sensor(Token.getBearer(), space.id, "gas")
                        .observe(this, new PawSecureObserver<SensorResponse>(this, new SensorOnChanged(this, this, spaceViewModel)));
                break;
            case 5:
                spaceViewModel.motion(Token.getBearer(), space.id)
                        .observe(this, new PawSecureObserver<SensorResponse>(this, new SensorOnChanged(this, this, spaceViewModel)));
                break;
            case 6:
                spaceViewModel.position(Token.getBearer(), space.id)
                        .observe(this, new PawSecureObserver<SensorResponse>(this, new SensorOnChanged(this, this, spaceViewModel)));
                break;
        }
    }
}
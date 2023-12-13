package com.example.pawsecure.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
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
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Map;

public class SpaceActivity extends PawSecureActivity implements View.OnClickListener, OnMapReadyCallback {

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
    int tabId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);

        Bundle bundle = getIntent().getExtras();
        spaceId = (int) bundle.get("SPACE_ID");

        linearNotLinked = findViewById(R.id.linearNotLinked);
        linearLinkedSpace = findViewById(R.id.linearLinkedSpace);
        linkButtonSpace = findViewById(R.id.linkButtonSpace);
        topBarCreateSpace = findViewById(R.id.topBarCreateSpace);
        tabLayoutSpace = findViewById(R.id.tabLayoutSpace);
        refreshLayout = findViewById(R.id.refreshLayout);
        bottomSheetSpace = findViewById(R.id.bottomSheetSpace);
        linearSensors = findViewById(R.id.linearSensors);
        mapSpace = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapSpace);

        establishCurtain(findViewById(R.id.progressSpace), findViewById(R.id.curtainSpace));
        showCurtain(new Button[] {});
        spaceViewModel = new ViewModelProvider(this).get(SpaceViewModel.class);
        spaceViewModel.space(Token.getBearer(), spaceId).observe(this, new PawSecureObserver<SpaceResponse>(this, new SpaceOnChanged(this, this, spaceViewModel)));
        linkButtonSpace.setOnClickListener(this);
        topBarCreateSpace.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuSpaceLink) {
                    goToLink();
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
        setMap(25.5345162, -103.3274717);
    }

    void setMap(double latitude, double longitude) {
        if (this.googleMap != null) {
            LatLng marker = new LatLng(latitude, longitude);
            this.googleMap.addMarker(new MarkerOptions().position(marker).title(getString(R.string.space_maps_title)));
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
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
                    break;
            }
        }
    }

    void decideLink() {
        hideCurtain(new Button[] {});
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
        reloadAllData();
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
                case "200":
                    listMap = spaceResponse.data.get(0);
                    searchForData(tabId);
                    tabLayoutSpace.setEnabled(true);
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
            temperatureValue.setText(String.format(getString(R.string.sensor_value), temperature.measure, temperature.sensor_type.unity));
        } else {
            temperatureUpdate.setText(String.format(getString(R.string.space_last), ""));
        }

        if (humidity != null) {
            humidityUpdate.setText(String.format(getString(R.string.space_last), humidity.time));
            humidityValue.setText(String.format(getString(R.string.sensor_value), humidity.measure, humidity.sensor_type.unity));
        } else {
            humidityUpdate.setText(String.format(getString(R.string.space_last), ""));
        }

        if (sound != null) {
            soundUpdate.setText(String.format(getString(R.string.space_last), sound.time));
            soundValue.setText(String.format(getString(R.string.sensor_string), (sound.measure == 1 ? getString(R.string.yes) : getString(R.string.no))));
        } else {
            soundUpdate.setText(String.format(getString(R.string.space_last), ""));
        }

        if (gas != null) {
            gasUpdate.setText(String.format(getString(R.string.space_last), gas.time));
            gasValue.setText(String.format(getString(R.string.sensor_string), (gas.measure == 1 ? getString(R.string.yes) : getString(R.string.no))));
        } else {
            gasUpdate.setText(String.format(getString(R.string.space_last), ""));
        }

        if (motion != null) {
            motionUpdate.setText(String.format(getString(R.string.space_last), motion.time));
            motionValue.setText(String.format(getString(R.string.sensor_string), (motion.measure == 1 ? getString(R.string.yes) : getString(R.string.no))));
        } else {
            motionUpdate.setText(String.format(getString(R.string.space_last), ""));
        }

        if (presence != null) {
            presenceUpdate.setText(String.format(getString(R.string.space_last), presence.time));
            presenceValue.setText(String.format(getString(R.string.sensor_string), (presence.measure == 1 ? getString(R.string.yes) : getString(R.string.no))));
        } else {
            presenceUpdate.setText(String.format(getString(R.string.space_last), ""));
        }

        if (position != null) {
            positionUpdate.setText(String.format(getString(R.string.space_last), position.time));
            positionValue.setText(String.format(getString(R.string.sensor_string), (position.measure == 1 ? getString(R.string.yes) : getString(R.string.no))));
            String[] dataLL = position.data.split("j");
            setMap(Double.parseDouble(dataLL[0]), Double.parseDouble(dataLL[1]));
        } else {
            positionUpdate.setText(String.format(getString(R.string.space_last), ""));
        }
    }

    void setTarget() {
        //introducir set target  con chip
    }

    void reloadAllData() {
        for (int i = 0; i <= 6; i++) {
            reloadData(i);
        }
        //con snackbar diciendo donde
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
                    listMap.get(String.valueOf(sensorTab)).set(sensorResponse.data.sensor_type.id, sensorResponse.data);
                    if (tabId == sensorTab) {
                        searchForData(tabId);
                    }
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
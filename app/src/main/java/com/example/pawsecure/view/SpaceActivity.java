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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.model.Pet;
import com.example.pawsecure.model.Space;
import com.example.pawsecure.response.SpaceResponse;
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

public class SpaceActivity extends PawSecureActivity implements View.OnClickListener, OnMapReadyCallback {

    LinearLayout linearNotLinked;
    LinearLayout linearLinkedSpace;
    Button linkButtonSpace;
    SpaceViewModel spaceViewModel;
    int spaceId;
    Space space;
    MaterialToolbar topBarCreateSpace;
    TabLayout tabLayoutSpace;
    MapsActivity mapsActivity;
    SupportMapFragment mapSpace;
    GoogleMap googleMap;
    SwipeRefreshLayout refreshLayout;
    FrameLayout bottomSheetSpace;

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
        //standardBottomSheetBehavior.setPeekHeight(150);
        standardBottomSheetBehavior.setHideable(false);
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
        LatLng marker = new LatLng(-34, 151);
        this.googleMap.addMarker(new MarkerOptions().position(marker).title(getString(R.string.space_maps_title)));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
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
                    hideCurtain(new Button[] {});
                    space = spaceResponse.data.get(0);
                    topBarCreateSpace.setTitle(space.name);
                    decideLink();
                    tabPets();
                    break;
            }
        }
    }

    void decideLink() {
        //space.linked == 1
        if (true) {
            linearLinkedSpace.setVisibility(View.VISIBLE);
            topBarCreateSpace.inflateMenu(R.menu.menu_space);
        } else {
            linearNotLinked.setVisibility(View.VISIBLE);
        }
    }
    void tabPets() {
        tabLayoutSpace.addTab(tabLayoutSpace.newTab().setText(getString(R.string.pet_none)));
        for (Pet pet : space.pets) {
            tabLayoutSpace.addTab(tabLayoutSpace.newTab().setText(pet.name).setIcon(ImagePetManager.getIdIconPet(pet.icon)).setId(pet.id));
        }
        tabLayoutSpace.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchForData(tab.getId());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    void searchForData(int id) {

    }

    void reloadData() {

    }

}
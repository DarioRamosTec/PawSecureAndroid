package com.example.pawsecure.view;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.SpaceAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureAnimator;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.implementation.PawSecureViewModel;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.NexusViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NexusActivity extends PawSecureActivity {

    NexusViewModel nexusViewModel;
    TextView textNothingNexus;
    RecyclerView recyclerNexus;
    FloatingActionButton fabNexus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nexus);

        amountDarkCurtain = 0f;
        establishCurtain(findViewById(R.id.progressCurtainNexus), findViewById(R.id.viewCurtainNexus));
        textNothingNexus = findViewById(R.id.textNothingNexus);
        recyclerNexus = findViewById(R.id.recyclerNexus);
        textNothingNexus.setText("");

        nexusViewModel = new ViewModelProvider(this).get(NexusViewModel.class);
        getSpaces();

        fabNexus = findViewById(R.id.fabNexus);
        fabNexus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(ChooseActivity.class, false);
            }
        });
    }

    void getSpaces() {
        showCurtain(new Button[] {});
        nexusViewModel.spaces(Token.getBearer()).observe(this, new PawSecureObserver<SpaceResponse>(this, new NexusActivity.ObserveSpaces(this, this, nexusViewModel)));
    }

    class ObserveSpaces extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<SpaceResponse> {

        public ObserveSpaces(Context context, PawSecureActivity pawSecureActivity, PawSecureViewModel pawSecureViewModel) {
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
                    if (spaceResponse.data.size() == 0) {
                        textNothingNexus.setText(spaceResponse.message);
                        PawSecureAnimator.alpha(textNothingNexus, 1, 1200, 0).start();
                        PawSecureAnimator.translateY(textNothingNexus, 0, 1200, -150).start();
                    } else {
                        recyclerNexus.setAdapter(new SpaceAdapter(spaceResponse.data));
                        recyclerNexus.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        recyclerNexus.setHasFixedSize(true);
                    }
                    break;
            }
        }
    }

    @Override
    public void onAuth() {
        getSpaces();
        super.onAuth();
    }
}
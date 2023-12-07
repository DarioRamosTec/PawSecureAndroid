package com.example.pawsecure.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.SpaceAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureObserver;
import com.example.pawsecure.implementation.PawSecureOnChanged;
import com.example.pawsecure.response.SpaceResponse;
import com.example.pawsecure.response.TokenResponse;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view_model.NexusViewModel;

public class NexusActivity extends PawSecureActivity {

    NexusViewModel nexusViewModel;
    TextView textNothingNexus;
    RecyclerView recyclerNexus;

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
    }

    void getSpaces() {
        showCurtain(new Button[] {});
        nexusViewModel.spaces(Token.getBearer()).observe(this, new PawSecureObserver<SpaceResponse>(this, new NexusActivity.ObserveSpaces(this, this)));
    }

    class ObserveSpaces extends PawSecureOnChanged implements PawSecureObserver.PawSecureOnChanged<SpaceResponse> {
        Context context;
        NexusActivity nexusActivity;
        public ObserveSpaces (Context context, NexusActivity nexusActivity) {
            this.context = context;
            this.nexusActivity = nexusActivity;
        }

        @Override
        public void onChanged(SpaceResponse spaceResponse) {
            switch (spaceResponse.code) {
                case "403":
                case "401":
                    checkAuth(context, nexusViewModel, nexusActivity);
                    break;
                case "200":
                    hideCurtain(new Button[] {});
                    if (spaceResponse.data.size() == 0) {
                        textNothingNexus.setText(spaceResponse.message);
                        //ANIMACION DE APAREACION DE TEXTO !!
                    } else {
                        //CARROUSEL CON IMAGEN SUBIDA E ICONO (CARDS)
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
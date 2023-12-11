package com.example.pawsecure.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pawsecure.R;

public class LinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        if (getIntent().getExtras() != null) {
            int[] ids = (int[]) getIntent().getExtras().get("IDS_SPACE");
            Intent intent = new Intent(this, CreateSpaceActivity.class);
            intent.putExtra("IDS_SPACE", ids);
            startActivity(intent);
        }

    }
}
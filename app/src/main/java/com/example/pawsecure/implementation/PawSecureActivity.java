package com.example.pawsecure.implementation;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.example.pawsecure.R;
import com.example.pawsecure.view.LoginActivity;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class PawSecureActivity extends AppCompatActivity {

    protected CircularProgressIndicator circularProgressIndicatorCurtain;
    protected View viewCurtain;

    public void onNotAuth() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    void hideCurtain() {

    }

    public void showCurtain(Button btnToDisable) {
        ObjectAnimator animationStart = ObjectAnimator.ofFloat(viewCurtain, "alpha", 0.15f).setDuration(200);
        animationStart.setInterpolator(new LinearOutSlowInInterpolator());
        animationStart.start();
        circularProgressIndicatorCurtain.show();
        circularProgressIndicatorCurtain.setContentDescription(getText(R.string.wait));
        btnToDisable.setEnabled(false);
    }
}

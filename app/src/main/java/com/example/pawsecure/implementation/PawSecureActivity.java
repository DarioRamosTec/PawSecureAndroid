package com.example.pawsecure.implementation;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.example.pawsecure.R;
import com.example.pawsecure.view.LoginActivity;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class PawSecureActivity extends AppCompatActivity implements PawSecureInterface {

    protected CircularProgressIndicator circularProgressIndicatorCurtain;
    protected View viewCurtain;
    protected float amountDarkCurtain = 0.15f;
    public ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public void onNotAuth() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void hideCurtain(Button[] btnToDisable) {
        ObjectAnimator animationFinal = ObjectAnimator.ofFloat(viewCurtain, "alpha", 0f).setDuration(200);
        animationFinal.setInterpolator(new FastOutSlowInInterpolator());
        animationFinal.start();
        circularProgressIndicatorCurtain.hide();
        circularProgressIndicatorCurtain.setContentDescription(null);
        for (Button button : btnToDisable) {
            button.setEnabled(true);
        }
    }

    public void showCurtain(Button[] btnToDisable) {
        ObjectAnimator animationStart = ObjectAnimator.ofFloat(viewCurtain, "alpha", amountDarkCurtain).setDuration(200);
        animationStart.setInterpolator(new LinearOutSlowInInterpolator());
        animationStart.start();
        circularProgressIndicatorCurtain.show();
        circularProgressIndicatorCurtain.setContentDescription(getText(R.string.wait));
        for (Button button : btnToDisable) {
            button.setEnabled(false);
        }
    }

    protected void establishCurtain(CircularProgressIndicator circularProgressIndicatorCurtain, View viewCurtain) {
        this.circularProgressIndicatorCurtain = circularProgressIndicatorCurtain;
        this.viewCurtain = viewCurtain;
    }

    @Override
    public void onAuth() {

    }

    public void startIntent(Class<?> cls, boolean close) {
        startActivity(new Intent(this, cls));
        if (close) {
            finish();
        }
    }

    public void restartActivities(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void startIntentForResult(Class<?> cls) {
        activityResultLauncher.launch(new Intent(this, cls));
    }

    protected void toSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}

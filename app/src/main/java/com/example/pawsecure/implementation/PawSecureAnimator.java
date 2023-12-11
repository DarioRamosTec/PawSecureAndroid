package com.example.pawsecure.implementation;

import android.animation.ObjectAnimator;
import android.view.View;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class PawSecureAnimator {
    public static ObjectAnimator alpha(View view, float value, int time, float startAlpha) {
        view.setAlpha(startAlpha);
        ObjectAnimator animationStart = ObjectAnimator.ofFloat(view, "alpha", value).setDuration(time);
        animationStart.setInterpolator(new LinearOutSlowInInterpolator());
        return animationStart;
    }

    public static ObjectAnimator translateY (View view, float value, int time, float startPosition) {
        view.setTranslationY(startPosition);
        ObjectAnimator animationStart = ObjectAnimator.ofFloat(view, "translationY", value).setDuration(time);
        animationStart.setInterpolator(new LinearOutSlowInInterpolator());
        return animationStart;
    }

    public static ObjectAnimator[] scale (View view, float value, int time, float startPosition) {
        view.setScaleX(startPosition);
        view.setScaleY(startPosition);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view, "scaleX", value)
                .setDuration(time);
        objectAnimator1.setInterpolator(new LinearOutSlowInInterpolator());
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view, "scaleY", value)
                .setDuration(time);
        objectAnimator2.setInterpolator(new LinearOutSlowInInterpolator());
        return new ObjectAnimator[] { objectAnimator1, objectAnimator2 };
    }

}

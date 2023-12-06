package com.example.pawsecure.implementation;

import androidx.lifecycle.Observer;

public class PawSecureObserver<T> implements Observer<T> {

    PawSecureActivity pawSecureActivity;
    PawSecureOnChanged<T> pawSecureOnChanged;

    public PawSecureObserver(PawSecureActivity pawSecureActivity, PawSecureOnChanged<T> pawSecureOnChanged) {
        this.pawSecureActivity = pawSecureActivity;
        this.pawSecureOnChanged = pawSecureOnChanged;
    }

    @Override
    public void onChanged(T t) {
        pawSecureOnChanged.onChanged(t);
    }

    public interface PawSecureOnChanged<T> {
        void onChanged(T t);
    }
}

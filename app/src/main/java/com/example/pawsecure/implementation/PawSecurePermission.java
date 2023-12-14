package com.example.pawsecure.implementation;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;

public class PawSecurePermission {
    public String permission;
    public Integer image;
    public String name;
    public String description;

    public PawSecurePermission (String permission, String name, Integer resource, String description) {
        this.permission = permission;
        this.name = name;
        this.image = resource;
        this.description = description;
    }

    public boolean check(Context context) {
        return checkSelfPermission(context, this.permission) == PermissionChecker.PERMISSION_GRANTED;
    }
}

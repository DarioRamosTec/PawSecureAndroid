package com.example.pawsecure.singletone;

import android.Manifest;
import android.content.Context;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecurePermission;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PermissionManager {

    public static Map<String, PawSecurePermission> permissionDictionary;

    public static Map<String, PawSecurePermission> getMap() {
        if (permissionDictionary == null) {
            permissionDictionary = new Hashtable<String, PawSecurePermission>() {{
                put("CAMERA", new PawSecurePermission(Manifest.permission.CAMERA, "Camera", R.drawable.outline_camera_24, ""));
                put("CALL_PHONE", new PawSecurePermission(Manifest.permission.CALL_PHONE, "Phone", R.drawable.round_local_phone_24, ""));
            }};
        }
        return permissionDictionary;
    }

    public boolean check(Context context) {
        for (PawSecurePermission p : getMap().values()) {
            if (!p.check(context)) {
                return false;
            }
        }
        return true;
    }

    public Boolean check(Context context, String permission) {
        if (getMap().containsKey(permission)) {
            PawSecurePermission p = getMap().get(permission);
            if (p != null) {
                return p.check(context);    
            }
        }
        return false;
    }
}

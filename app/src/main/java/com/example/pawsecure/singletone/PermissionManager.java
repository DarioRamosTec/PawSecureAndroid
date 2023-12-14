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
                put("READ_MEDIA_IMAGES", new PawSecurePermission(Manifest.permission.READ_MEDIA_IMAGES, "IMAGES", R.drawable.outline_camera_24, "Habilidad para ver tus fotos."));
                put("BLUETOOTH", new PawSecurePermission(Manifest.permission.BLUETOOTH, "BLUETOOTH", R.drawable.round_local_phone_24, "Forma de conectarse mediante bluetooth."));
                put("ACCESS_NETWORK_STATE", new PawSecurePermission(Manifest.permission.ACCESS_NETWORK_STATE, "WIFI", R.drawable.outline_camera_24, "Conocer información de la red WiFi"));
                put("INTERNET", new PawSecurePermission(Manifest.permission.INTERNET, "INTERNET", R.drawable.round_local_phone_24, "Conectarse a Internet"));
                put("ACCESS_FINE_LOCATION", new PawSecurePermission(Manifest.permission.ACCESS_FINE_LOCATION, "LOCATION", R.drawable.outline_camera_24, "Accesar a la ubicación."));
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

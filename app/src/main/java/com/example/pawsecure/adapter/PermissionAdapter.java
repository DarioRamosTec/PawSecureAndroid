package com.example.pawsecure.adapter;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecurePermission;
import com.example.pawsecure.singletone.PermissionManager;
import com.example.pawsecure.view.PermissionsActivity;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.ViewHolder> {

    PermissionsActivity permissionsActivity;
    Map<String, PawSecurePermission> permissionMap;
    List<PawSecurePermission> permissionList;

    public PermissionAdapter (PermissionsActivity permissionsActivity) {
        this.permissionsActivity = permissionsActivity;
        this.permissionList = new ArrayList<>();
        permissionList.addAll(PermissionManager.getMap().values());
    }

    @NonNull
    @Override
    public PermissionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater ly = LayoutInflater.from(parent.getContext());
        View v = ly.inflate(R.layout.item_permission, parent, false);
        return new PermissionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionAdapter.ViewHolder holder, int position) {
        holder.setData(permissionList.get(position), permissionsActivity);
    }

    @Override
    public int getItemCount() {
        return permissionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDescriptionItemPermission;
        TextView textNameItemPermission;
        MaterialSwitch switchItemPermission;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNameItemPermission = itemView.findViewById(R.id.textNameItemPermission);
            textDescriptionItemPermission = itemView.findViewById(R.id.textDescriptionItemPermission);
            switchItemPermission = itemView.findViewById(R.id.switchItemPermission);
        }
        public void setData(PawSecurePermission permission, PermissionsActivity permissionsActivity) {
            textNameItemPermission.setText(permission.name);
            textDescriptionItemPermission.setText(permission.description.toString());
            switchItemPermission.setChecked(permission.check(itemView.getContext()));
            switchItemPermission.setOnCheckedChangeListener(new PermissionListener(permissionsActivity, permission, switchItemPermission));
        }
    }

    static class PermissionListener implements CompoundButton.OnCheckedChangeListener {
        PermissionsActivity activity;
        PawSecurePermission permission;
        MaterialSwitch aSwitch;
        public PermissionListener (PermissionsActivity acc, PawSecurePermission permission, MaterialSwitch aSwitch) {
            this.activity = acc;
            this.permission = permission;
            this.aSwitch = aSwitch;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            activity.checkOutPermission(permission, aSwitch);
        }
    }
}

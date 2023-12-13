package com.example.pawsecure.adapter;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureAdapter;
import com.example.pawsecure.singletone.ImagePetManager;
import com.example.pawsecure.view.CreatePetActivity;
import com.example.pawsecure.view.LinkActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {

    List<BluetoothDevice> list;
    LinkActivity linkActivity;

    public DevicesAdapter (List<BluetoothDevice> list, LinkActivity linkActivity) {
        this.list = list;
        this.linkActivity = linkActivity;
    }

    public List<BluetoothDevice> getList() {
        return list;
    }

    @NonNull
    @Override
    public DevicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_device, parent, false);
        return new DevicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesAdapter.ViewHolder holder, int position) {
        holder.setData(list.get(position), this.linkActivity);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        TextView nameDevice;
        TextView addressDevice;
        LinearLayout linearDevice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addressDevice = itemView.findViewById(R.id.addressDevice);
            nameDevice = itemView.findViewById(R.id.nameDevice);
            linearDevice = itemView.findViewById(R.id.linearDevice);
        }

        void setData(BluetoothDevice bluetoothDevice, LinkActivity linkActivity) {
            try {
                addressDevice.setText(bluetoothDevice.getAddress());
                nameDevice.setText(bluetoothDevice.getName());
                linearDevice.setOnClickListener(view -> linkActivity.boundToDevice(bluetoothDevice));
            } catch (SecurityException e) {

            }
        }
    }
}
package com.example.pawsecure.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pawsecure.R;
import com.example.pawsecure.view.LinkActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WifiLinkDialog extends DialogFragment {

    LinkActivity linkActivity;

    public WifiLinkDialog(LinkActivity linkActivity) {
        this.linkActivity = linkActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity(), com.google.android.material.R.style.MaterialAlertDialog_Material3);
        View theDialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState);
        builder.setView(theDialogView).setTitle(R.string.wifi_link_hello).setCancelable(true);

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi_link, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((EditText) view.findViewById(R.id.editTextPasswordWifiLink)).setText(linkActivity.password);
        ((EditText) view.findViewById(R.id.editTextSSIDWifiLink)).setText(linkActivity.ssid);
        view.findViewById(R.id.buttonDoneWifiLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_) {
                Editable password = ((EditText) view.findViewById(R.id.editTextPasswordWifiLink)).getText();
                Editable ssid = ((EditText) view.findViewById(R.id.editTextSSIDWifiLink)).getText();
                linkActivity.defineWifi(ssid.toString(), password.toString());
            }
        });
    }
}

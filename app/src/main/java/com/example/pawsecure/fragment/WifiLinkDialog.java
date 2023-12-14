package com.example.pawsecure.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pawsecure.R;
import com.example.pawsecure.view.LinkActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WifiLinkDialog extends DialogFragment implements DialogInterface.OnClickListener {

    LinkActivity linkActivity;
    EditText editTextPasswordWifiLink;
    EditText editTextSSIDWifiLink;
    View theDialogView;
    String ssid;
    String password;

    public WifiLinkDialog(LinkActivity linkActivity, String ssid, String password) {
        this.linkActivity = linkActivity;
        this.ssid = ssid;
        this.password = password;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
        theDialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState);
        builder.setView(theDialogView);
        builder.setTitle(R.string.wifi_link_hello);
        builder.setPositiveButton(getString(R.string.done), this);
        return builder.create();
    }

    @Override
    public View getView() {
        return theDialogView;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi_link, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((EditText)getView().findViewById(R.id.editTextPasswordWifiLink)).setText(this.password);
        ((EditText)getView().findViewById(R.id.editTextSSIDWifiLink)).setText(this.ssid);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        linkActivity.defineWifi(((EditText) getView().findViewById(R.id.editTextSSIDWifiLink)).getText().toString(), ((EditText) getView().findViewById(R.id.editTextPasswordWifiLink)).getText().toString());
    }
}
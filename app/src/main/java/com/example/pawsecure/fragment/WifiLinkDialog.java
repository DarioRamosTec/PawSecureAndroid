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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.pawsecure.R;
import com.example.pawsecure.view.LinkActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WifiLinkDialog extends DialogFragment implements View.OnClickListener {

    EditText editTextPasswordWifiLink;
    EditText editTextSSIDWifiLink;
    LinkActivity linkActivity;

    public WifiLinkDialog(LinkActivity linkActivity) {
        this.linkActivity = linkActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext(), com.google.android.material.R.style.MaterialAlertDialog_Material3);
        builder.setTitle(R.string.wifi_link_hello);
        builder.setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editable password = editTextPasswordWifiLink.getText();
                Editable ssid = editTextSSIDWifiLink.getText();
                linkActivity.defineWifi(ssid.toString(), password.toString());
                dismiss();
            }
        });
        builder.setView(R.layout.fragment_wifi_link);

        return builder.create();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi_link, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextPasswordWifiLink = view.findViewById(R.id.editTextPasswordWifiLink);
        editTextPasswordWifiLink.setText(linkActivity.password);
        editTextSSIDWifiLink = view.findViewById(R.id.editTextSSIDWifiLink);
        editTextSSIDWifiLink.setText(linkActivity.ssid);

        Button buttonDoneWifiLink = view.findViewById(R.id.buttonDoneWifiLink);
        buttonDoneWifiLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Editable password = editTextPasswordWifiLink.getText();
        Editable ssid = editTextSSIDWifiLink.getText();
        linkActivity.defineWifi(ssid.toString(), password.toString());
        dismiss();
    }
}
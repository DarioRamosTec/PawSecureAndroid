package com.example.pawsecure.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pawsecure.R;
import com.example.pawsecure.view.LinkActivity;

public class WifiLinkDialog extends DialogFragment {

    LinkActivity linkActivity;

    public WifiLinkDialog(LinkActivity linkActivity) {
        this.linkActivity = linkActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi_link, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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

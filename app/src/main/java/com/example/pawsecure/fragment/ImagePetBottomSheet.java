package com.example.pawsecure.fragment;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsecure.R;
import com.example.pawsecure.adapter.ImagePetAdapter;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ImagePetBottomSheet extends BottomSheetDialogFragment {

    RecyclerView recyclerView;
    PawSecureActivity pawSecureActivity;

    public ImagePetBottomSheet (PawSecureActivity pawSecureActivity) {
        this.pawSecureActivity = pawSecureActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerFragmentMakePet);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new ImagePetAdapter(pawSecureActivity));
    }
}

package com.example.pawsecure.adapter;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureAnimator;
import com.example.pawsecure.model.Pet;
import com.example.pawsecure.retrofit.RetrofitRequest;
import com.example.pawsecure.singletone.ImagePetManager;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view.ChooseActivity;
import com.example.pawsecure.view.CreatePetActivity;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PetIconAdapter extends RecyclerView.Adapter<PetIconAdapter.ViewHolder> {

    List<Pet> petList;
    PawSecureActivity pawSecureActivity;

    public PetIconAdapter (List<Pet> petList, PawSecureActivity pawSecureActivity) {
        this.petList = petList;
        this.pawSecureActivity = pawSecureActivity;
    }

    public List<Pet> getPetList () {
        return petList;
    }

    @NonNull
    @Override
    public PetIconAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater ly = LayoutInflater.from(parent.getContext());
        View view = ly.inflate(R.layout.item_pet_icon, parent, false);
        return new PetIconAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetIconAdapter.ViewHolder holder, int position) {
        holder.getData(petList.get(position), pawSecureActivity);
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePetIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePetIcon = itemView.findViewById(R.id.imagePetIcon);
        }

        void getData(Pet pet, PawSecureActivity pawSecureActivity) {
            imagePetIcon.setImageDrawable(AppCompatResources.getDrawable(itemView.getContext(), ImagePetManager.getIdIconPet(pet.icon)));
        }
    }
}

package com.example.pawsecure.adapter;

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
import com.example.pawsecure.retrofit.RetrofitRequest;
import com.example.pawsecure.singletone.ImagePetManager;
import com.example.pawsecure.model.Pet;
import com.example.pawsecure.token.Token;
import com.example.pawsecure.view.ChooseActivity;
import com.example.pawsecure.view.CreatePetActivity;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    List<Pet> petList;
    PawSecureActivity pawSecureActivity;

    public PetAdapter (List<Pet> petList, PawSecureActivity pawSecureActivity) {
        this.petList = petList;
        this.pawSecureActivity = pawSecureActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater ly = LayoutInflater.from(parent.getContext());
        View view = ly.inflate(R.layout.item_pet, parent, false);
        return new PetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getData(petList.get(position), pawSecureActivity);
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageBackgroundPet;
        ImageView imageIconPet;
        TextView textNamePet;
        TextView textDescriptionPet;
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBackgroundPet = itemView.findViewById(R.id.imageBackgroundPet);
            imageIconPet = itemView.findViewById(R.id.imageIconPet);
            textNamePet = itemView.findViewById(R.id.textNamePet);
            textDescriptionPet = itemView.findViewById(R.id.textDescriptionPet);
            card = itemView.findViewById(R.id.card);
        }

        void getData(Pet pet, PawSecureActivity pawSecureActivity) {
            if (pet instanceof Pet.PetCreate) {
                imageBackgroundPet.setImageDrawable(null);
                imageIconPet.setImageDrawable(AppCompatResources.getDrawable(pawSecureActivity, ImagePetManager.getIdIconPet(-1)));
                textNamePet.setText(pawSecureActivity.getString(R.string.choose_create_pet));
                textDescriptionPet.setText(pawSecureActivity.getString(R.string.choose_create_msg));
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pawSecureActivity.startIntentForResult(CreatePetActivity.class);
                    }
                });
            } else {
                Picasso.get().load(RetrofitRequest.site+RetrofitRequest.mePets+pet.id+"?token="+Token.getToken()).into(imageBackgroundPet);
                imageIconPet.setImageDrawable(AppCompatResources.getDrawable(pawSecureActivity, ImagePetManager.getIdIconPet(pet.icon)));
                textNamePet.setText(pet.name);
                textDescriptionPet.setText(pet.description);
                card.setOnClickListener(null);
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}

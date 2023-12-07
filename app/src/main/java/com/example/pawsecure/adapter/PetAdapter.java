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
import com.example.pawsecure.implementation.PawSecureResource;
import com.example.pawsecure.model.Pet;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageBackgroundPet;
        ImageView imageIconPet;
        TextView textNamePet;
        TextView textDescriptionPet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBackgroundPet = itemView.findViewById(R.id.imageBackgroundPet);
            imageIconPet = itemView.findViewById(R.id.imageIconPet);
            textNamePet = itemView.findViewById(R.id.textNamePet);
            textDescriptionPet = itemView.findViewById(R.id.textDescriptionPet);
        }

        void getData(Pet pet, PawSecureActivity pawSecureActivity) {
            Picasso.get().load(pet.image).into(imageBackgroundPet);
            imageIconPet.setImageDrawable(AppCompatResources.getDrawable(pawSecureActivity, PawSecureResource.getIdIconPet(pawSecureActivity, pet.icon)));
            textNamePet.setText(pet.name);
            textDescriptionPet.setText(pet.description);
        }
    }
}

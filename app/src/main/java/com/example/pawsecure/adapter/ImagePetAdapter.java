package com.example.pawsecure.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsecure.R;
import com.example.pawsecure.singletone.ImagePetManager;
import com.example.pawsecure.view.CreatePetActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ImagePetAdapter extends RecyclerView.Adapter<ImagePetAdapter.ViewHolder> {

    List<ImagePetManager.ImagePet> list;
    CreatePetActivity createPetActivity;

    public ImagePetAdapter (CreatePetActivity createPetActivity) {
        this.list = ImagePetManager.getList();
        this.createPetActivity = createPetActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_pet_image, parent, false);
        return new ImagePetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position, list.get(position), this.createPetActivity);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView imagePetImage;
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePetImage = itemView.findViewById(R.id.imagePetImage);
            card = itemView.findViewById(R.id.card);
        }

        void setData(Integer position, ImagePetManager.ImagePet imagePet, CreatePetActivity createPetActivity) {
            imagePetImage.setImageDrawable(AppCompatResources.getDrawable(createPetActivity, imagePet.icon));
            card.setOnClickListener(view -> {
                createPetActivity.changeIcon(position, imagePet.icon, imagePet.animal);
            });
        }
    }
}

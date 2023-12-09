package com.example.pawsecure.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsecure.R;
import com.example.pawsecure.fragment.ImagePetBottomSheet;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.singletone.ImagePet;

import java.util.ArrayList;
import java.util.List;

public class ImagePetAdapter extends RecyclerView.Adapter<ImagePetAdapter.ViewHolder> {

    List<Integer> list;
    PawSecureActivity pawSecureActivity;

    public ImagePetAdapter (PawSecureActivity pawSecureActivity) {
        this.list = ImagePet.getList();
        this.pawSecureActivity = pawSecureActivity;
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
        holder.setData(position, list.get(position), this.pawSecureActivity);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePetImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePetImage = itemView.findViewById(R.id.imagePetImage);
        }

        void setData(Integer position, Integer icon, PawSecureActivity pawSecureActivity) {
            Log.d("UTT", "A");
            imagePetImage.setImageDrawable(AppCompatResources.getDrawable(pawSecureActivity, icon));
        }
    }
}

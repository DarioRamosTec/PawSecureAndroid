package com.example.pawsecure.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsecure.R;
import com.example.pawsecure.model.Pet;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class SpaceCarouselAdapter extends RecyclerView.Adapter<SpaceCarouselAdapter.ViewHolder> {

    List<Pet> listPet;

    public SpaceCarouselAdapter (List<Pet> listPet) {
        this.listPet = listPet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_space_carousel, parent, false);
        return new SpaceCarouselAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getData(listPet.get(position));
    }

    @Override
    public int getItemCount() {
        return listPet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageCarouselSpace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCarouselSpace = itemView.findViewById(R.id.imageCarouselSpace);

        }

        public void getData(Pet pet) {
            //pet.image;

        }
    }
}

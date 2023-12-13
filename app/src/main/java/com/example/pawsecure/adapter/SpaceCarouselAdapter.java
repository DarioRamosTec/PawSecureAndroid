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
import com.example.pawsecure.model.Pet;
import com.example.pawsecure.retrofit.RetrofitRequest;
import com.example.pawsecure.singletone.ImagePetManager;
import com.example.pawsecure.token.Token;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SpaceCarouselAdapter extends RecyclerView.Adapter<SpaceCarouselAdapter.ViewHolder> {

    List<Pet> listPet = new ArrayList<>();

    public SpaceCarouselAdapter (List<Pet> listPet) {
        for (Pet pet : listPet) {
            if (!(pet.image == null || pet.image.equals(""))) {
                this.listPet.add(pet);
            }
        }
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
            if (pet.image == null || pet.image.equals("")) {
                imageCarouselSpace.setImageDrawable(null);
            } else {
                Picasso.get().load(RetrofitRequest.site+RetrofitRequest.mePets+pet.id+"?token="+ Token.getToken())
                        .placeholder(R.drawable.icon_placeholder)
                        .error(R.drawable.icon_connect)
                        .into(imageCarouselSpace);
            }
        }
    }
}

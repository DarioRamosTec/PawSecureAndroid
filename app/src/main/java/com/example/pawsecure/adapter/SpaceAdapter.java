package com.example.pawsecure.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsecure.R;
import com.example.pawsecure.model.Space;
import com.google.android.material.carousel.CarouselLayoutManager;

import java.util.List;

public class SpaceAdapter extends RecyclerView.Adapter<SpaceAdapter.ViewHolder> {
    List<Space> spaceList;

    public SpaceAdapter(List<Space> spaceList) {
        this.spaceList = spaceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_space, parent, false);
        return new SpaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(spaceList.get(position));
    }

    @Override
    public int getItemCount() {
        return spaceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textItemSpace;
        ImageView imageItemSpace;
        RecyclerView recyclerViewCarouselSpace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemSpace = itemView.findViewById(R.id.textItemSpace);
            imageItemSpace = itemView.findViewById(R.id.imageItemSpace);
            recyclerViewCarouselSpace = itemView.findViewById(R.id.recyclerViewCarouselSpace);
        }

        public void setData(Space space) {
            textItemSpace.setText(space.name);
            recyclerViewCarouselSpace.setAdapter(new SpaceCarouselAdapter(space.pets));
            recyclerViewCarouselSpace.setLayoutManager(new CarouselLayoutManager());
        }
    }
}

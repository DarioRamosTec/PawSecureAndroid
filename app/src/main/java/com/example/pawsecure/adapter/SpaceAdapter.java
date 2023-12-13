package com.example.pawsecure.adapter;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.PawSecureActivity;
import com.example.pawsecure.implementation.PawSecureLinearLayoutManager;
import com.example.pawsecure.model.Space;
import com.example.pawsecure.view.NexusActivity;
import com.example.pawsecure.view.SpaceActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.carousel.CarouselLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class SpaceAdapter extends RecyclerView.Adapter<SpaceAdapter.ViewHolder> {
    List<Space> spaceList;
    PawSecureActivity pawSecureActivity;

    public SpaceAdapter(List<Space> spaceList, PawSecureActivity pawSecureActivity) {
        this.spaceList = spaceList;
        this.pawSecureActivity = pawSecureActivity;
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
        holder.setData(spaceList.get(position), pawSecureActivity);
    }

    @Override
    public int getItemCount() {
        return spaceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textItemSpace;
        TextView textSecondaryItemSpace;
        RecyclerView recyclerViewCarouselSpace;
        RecyclerView recyclerItemSpace;
        MaterialCardView cardItemSpace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemSpace = itemView.findViewById(R.id.textItemSpace);
            recyclerItemSpace = itemView.findViewById(R.id.recyclerItemSpace);
            recyclerViewCarouselSpace = itemView.findViewById(R.id.recyclerViewCarouselSpace);
            textSecondaryItemSpace = itemView.findViewById(R.id.textSecondaryItemSpace);
            cardItemSpace = itemView.findViewById(R.id.cardItemSpace);
        }

        public void setData(Space space, PawSecureActivity pawSecureActivity) {
            textItemSpace.setText(space.name);
            textSecondaryItemSpace.setText(space.description);
            recyclerViewCarouselSpace.setAdapter(new SpaceCarouselAdapter(space.pets));
            recyclerViewCarouselSpace.setLayoutManager(new CarouselLayoutManager());
            recyclerItemSpace.setAdapter(new PetIconAdapter(space.pets, null));
            PawSecureLinearLayoutManager pawSecureLinearLayoutManager = new PawSecureLinearLayoutManager(textItemSpace.getContext(), LinearLayoutManager.VERTICAL, false);
            pawSecureLinearLayoutManager.setSmoothScrollDuration(1000);
            recyclerItemSpace.setLayoutManager(pawSecureLinearLayoutManager);
            recyclerItemSpace.setHasFixedSize(true);
            if (space.pets.size() > 1) {
                scroll(0, space.pets.size());
            }
            cardItemSpace.setOnClickListener(view -> {
                Intent intent = new Intent(pawSecureActivity, SpaceActivity.class);
                intent.putExtra("SPACE_ID", space.id);
                pawSecureActivity.startActivity(intent);
            });
        }

        void scroll(int i, int size) {
            recyclerItemSpace.smoothScrollToPosition(i);
            CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    scroll((i+1)%size, size);
                }
            };
            countDownTimer.start();
        }
    }
}

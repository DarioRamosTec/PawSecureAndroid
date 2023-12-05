package com.example.pawsecure.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsecure.R;
import com.example.pawsecure.implementation.Listener;
import com.example.pawsecure.model.Permisos;

import java.util.List;

public class PermisosAdapter extends RecyclerView.Adapter<PermisosAdapter.ViewHolder> {

    private List<Permisos> pmLista;
    private Listener listener;

    public PermisosAdapter(List<Permisos> permisosList, Listener listener)
    {
        this.pmLista=permisosList;
        this.listener=listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater ly=LayoutInflater.from(parent.getContext());
        View v=ly.inflate(R.layout.itempermisi, parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Permisos p = pmLista.get(holder.getAdapterPosition());
        holder.setdata(p);

        holder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onCheckedChange(pmLista.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pmLista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView namePM;
        Switch sw;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namePM=itemView.findViewById(R.id.namePM);
            sw=itemView.findViewById(R.id.sw);
        }

        public void setdata(Permisos p) {
            Permisos permisos=p;
            namePM.setText(p.getNombre());

        }
    }
}
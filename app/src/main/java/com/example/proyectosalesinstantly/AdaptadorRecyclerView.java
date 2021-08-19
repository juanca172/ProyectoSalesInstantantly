package com.example.proyectosalesinstantly;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.core.net.UriCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdaptadorRecyclerView extends RecyclerView.Adapter<AdaptadorRecyclerView.RecyclerHolder> {
    private List<CardViewAtributos> items;
    HomeView Home;

    public AdaptadorRecyclerView(List<CardViewAtributos> items, HomeView Home) {
        this.items = items;
        this.Home = Home;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_card_view, parent,false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorRecyclerView.RecyclerHolder holder, int position) {
        CardViewAtributos atributosDelCardView = items.get(position);
        holder.tvTitulo.setText(atributosDelCardView.getNombre());
        holder.tvDescripcion.setText(atributosDelCardView.getDescripcion());
        holder.tvPrecio.setText(String.valueOf(atributosDelCardView.getPrecioProducto()));
        Glide.with(Home).load(atributosDelCardView.getImagenDeTienda()).into(holder.imgItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), VisualizacionTiendasView.class);
                i.putExtra("itemDetail", atributosDelCardView);
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        //colocamos la cantidad de elementos de la lista
        return items.size();
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        private TextView tvTitulo;
        private TextView tvDescripcion;
        private TextView tvPrecio;


        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imageViewTiendas);
            tvTitulo = itemView.findViewById(R.id.tvNombreDelProducto);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProducto);
        }
    }

}

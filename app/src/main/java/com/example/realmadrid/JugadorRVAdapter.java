package com.example.realmadrid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JugadorRVAdapter extends RecyclerView.Adapter<JugadorRVAdapter.ViewHolder> {
    //creando variables para nuestra lista, contexto, interfaz y posición.
    private ArrayList<JugadorRVModal> jugadorRVModalArrayList;
    private Context context;
    private JugadorClickInterface jugadorClickInterface;
    int lastPos = -1;

    //creando los  constructores.
    public JugadorRVAdapter(ArrayList<JugadorRVModal> jugadorRVModalArrayList, Context context, JugadorClickInterface jugadorClickInterface) {
        this.jugadorRVModalArrayList = jugadorRVModalArrayList;
        this.context = context;
        this.jugadorClickInterface = jugadorClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflando un archivo de diseño en la línea de abajo.
        View view = LayoutInflater.from(context).inflate(R.layout.jugador_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //configurando datos para nuestro elemento de RecyclerView en la línea de abajo.
        JugadorRVModal jugadorRVModal = jugadorRVModalArrayList.get(position);
        holder.jugadorTV.setText(jugadorRVModal.getJugadorName());
        holder.jugadorPriceTV.setText("Precio. " + jugadorRVModal.getJugadorPrice());
        Picasso.get().load(jugadorRVModal.getJugadorImg()).into(holder.jugadorIV);
        // agregando animación al los elementos de recycleView en la línea de abajo.
        setAnimation(holder.itemView, position);
        holder.jugadorIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jugadorClickInterface.onJugadorClick(position);
            }
        });
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            // en la línea de abajo estamos configurando la animación.
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return jugadorRVModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //creando variable para nuestra vista de imagen y vista de texto en la línea de abajo.
        private ImageView jugadorIV;
        private TextView jugadorTV, jugadorPriceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing all our variables on below line.
            jugadorIV = itemView.findViewById(R.id.idIVJugador);
            jugadorTV = itemView.findViewById(R.id.idTVJUgadorName);
            jugadorPriceTV = itemView.findViewById(R.id.idTVCousePrice);
        }
    }

    //creando una interfaz para hacer clic
    public interface JugadorClickInterface {
        void onJugadorClick(int position);
    }
}

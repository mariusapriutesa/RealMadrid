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
    //creating variables for our list, context, interface and position.
    private ArrayList<JugadorRVModal> jugadorRVModalArrayList;
    private Context context;
    private JugadorClickInterface jugadorClickInterface;
    int lastPos = -1;

    //creating a constructor.
    public JugadorRVAdapter(ArrayList<JugadorRVModal> jugadorRVModalArrayList, Context context, JugadorClickInterface jugadorClickInterface) {
        this.jugadorRVModalArrayList = jugadorRVModalArrayList;
        this.context = context;
        this.jugadorClickInterface = jugadorClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.jugador_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //setting data to our recycler view item on below line.
        JugadorRVModal jugadorRVModal = jugadorRVModalArrayList.get(position);
        holder.jugadorTV.setText(jugadorRVModal.getJugadorName());
        holder.jugadorPriceTV.setText("Precio. " + jugadorRVModal.getJugadorPrice());
        Picasso.get().load(jugadorRVModal.getJugadorImg()).into(holder.jugadorIV);
        //adding animation to recycler view item on below line.
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
            //on below line we are setting animation.
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
        //creating variable for our image view and text view on below line.
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

    //creating a interface for on click
    public interface JugadorClickInterface {
        void onJugadorClick(int position);
    }
}

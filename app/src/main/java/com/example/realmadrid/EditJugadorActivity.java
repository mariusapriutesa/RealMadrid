package com.example.realmadrid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditJugadorActivity extends AppCompatActivity {

    //creating variables for our edit text, firebase database, database reference, jugador rv modal,progress bar.
    private TextInputEditText jugadorNameEdt, jugadorDescEdt, jugadorPriceEdt, bestSuitedEdt, jugadorImgEdt, jugadorLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    JugadorRVModal jugadorRVModal;
    private ProgressBar loadingPB;
    //creating a string for our jugador id.
    private String jugadorID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jugador);
        //initializing all our variables on below line.
        Button addJugadorBtn = findViewById(R.id.idBtnAddJugador);
        jugadorNameEdt = findViewById(R.id.idEdtJugadorName);
        jugadorDescEdt = findViewById(R.id.idEdtJugadorDescription);
        jugadorPriceEdt = findViewById(R.id.idEdtJugadorPrice);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        jugadorImgEdt = findViewById(R.id.idEdtJugadorImageLink);
        jugadorLinkEdt = findViewById(R.id.idEdtJugadorLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //on below line we are getting our modal class on which we have passed.
        jugadorRVModal = getIntent().getParcelableExtra("jugador");
        Button deleteJugadorBtn = findViewById(R.id.idBtnDeleteJugador);

        if (jugadorRVModal != null) {
            //on below line we are setting data to our edit text from our modal class.
            jugadorNameEdt.setText(jugadorRVModal.getJugadorName());
            jugadorPriceEdt.setText(jugadorRVModal.getJugadorPrice());
            bestSuitedEdt.setText(jugadorRVModal.getBestSuitedFor());
            jugadorImgEdt.setText(jugadorRVModal.getJugadorImg());
            jugadorLinkEdt.setText(jugadorRVModal.getJugadorLink());
            jugadorDescEdt.setText(jugadorRVModal.getJugadorDescription());
            jugadorID = jugadorRVModal.getJugadorId();
        }

        //on below line we are initialing our database reference and we are adding a child as our jugador id.
        databaseReference = firebaseDatabase.getReference("Jugadores").child(jugadorID);
        //on below line we are adding click listener for our add jugador button.
        addJugadorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on below line we are making our progress bar as visible.
                loadingPB.setVisibility(View.VISIBLE);
                //on below line we are getting data from our edit text.
                String jugadorName = jugadorNameEdt.getText().toString();
                String jugadorDesc = jugadorDescEdt.getText().toString();
                String jugadorPrice = jugadorPriceEdt.getText().toString();
                String bestSuited = bestSuitedEdt.getText().toString();
                String jugadorImg = jugadorImgEdt.getText().toString();
                String jugadorLink = jugadorLinkEdt.getText().toString();
                //on below line we are creating a map for passing a data using key and value pair.
                Map<String, Object> map = new HashMap<>();
                map.put("jugadorName", jugadorName);
                map.put("jugadorDescription", jugadorDesc);
                map.put("jugadorPrice", jugadorPrice);
                map.put("bestSuitedFor", bestSuited);
                map.put("jugadorImg", jugadorImg);
                map.put("jugadorLink", jugadorLink);
                map.put("jugadorId", jugadorID);

                //on below line we are calling a database reference on add value event listener and on data change method
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //making progress bar visibility as gone.
                        loadingPB.setVisibility(View.GONE);
                        //adding a map to our database.
                        databaseReference.updateChildren(map);
                        //on below line we are displaying a toast message.
                        Toast.makeText(EditJugadorActivity.this, "Jugador Updated..", Toast.LENGTH_SHORT).show();
                        //opening a new activity after updating our coarse.
                        startActivity(new Intent(EditJugadorActivity.this, com.example.realmadrid.MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //displaying a failure message on toast.
                        Toast.makeText(EditJugadorActivity.this, "Fail to update jugador..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //adding a click listener for our delete jugador button.
        deleteJugadorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling a method to delete a jugador.
                deleteJugador();
            }
        });

    }

    private void deleteJugador() {
        //on below line calling a method to delete the jugador.
        databaseReference.removeValue();
        //displaying a toast message on below line.
        Toast.makeText(this, "Jugador Deleted..", Toast.LENGTH_SHORT).show();
        //opening a main activity on below line.
        startActivity(new Intent(EditJugadorActivity.this, com.example.realmadrid.MainActivity.class));
    }
}
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

public class AddJugadorActivity extends AppCompatActivity {

    //creating variables for our button, edit text,firebase database, database refrence, progress bar.
    private Button addJugadorBtn;
    private TextInputEditText jugadorNameEdt, jugadorDescEdt, jugadorPriceEdt, bestSuitedEdt, jugadorImgEdt, jugadorLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String jugadorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jugador);
        //initializing all our variables.
        addJugadorBtn = findViewById(R.id.idBtnAddJugador);
        jugadorNameEdt = findViewById(R.id.idEdtJugadorName);
        jugadorDescEdt = findViewById(R.id.idEdtJugadorDescription);
        jugadorPriceEdt = findViewById(R.id.idEdtJugadorPrice);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        jugadorImgEdt = findViewById(R.id.idEdtJugadorImageLink);
        jugadorLinkEdt = findViewById(R.id.idEdtJugadorLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("Jugadores");
        //adding click listener for our add jugador button.
        addJugadorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                //getting data from our edit text.
                String jugadorName = jugadorNameEdt.getText().toString();
                String jugadorDesc = jugadorDescEdt.getText().toString();
                String jugadorPrice = jugadorPriceEdt.getText().toString();
                String bestSuited = bestSuitedEdt.getText().toString();
                String jugadorImg = jugadorImgEdt.getText().toString();
                String jugadorLink = jugadorLinkEdt.getText().toString();
                jugadorID = jugadorName;
                //on below line we are passing all data to our modal class.
                JugadorRVModal jugadorRVModal = new JugadorRVModal(jugadorID, jugadorName, jugadorDesc, jugadorPrice, bestSuited, jugadorImg, jugadorLink);
                //on below line we are calling a add value event to pass data to firebase database.
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //on below line we are setting data in our firebase database.
                        databaseReference.child(jugadorID).setValue(jugadorRVModal);
                        //displaying a toast message.
                        Toast.makeText(AddJugadorActivity.this, "Jugador Added..", Toast.LENGTH_SHORT).show();
                        //starting a main activity.
                        startActivity(new Intent(AddJugadorActivity.this, com.example.realmadrid.MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //displaying a failure message on below line.
                        Toast.makeText(AddJugadorActivity.this, "Fail to add Jugador..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
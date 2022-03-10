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

    //creando variables para nuestro texto de edición, base de datos firebase, referencia de base de datos, jugador rv modal, barra de progreso.
    private TextInputEditText jugadorNameEdt, jugadorDescEdt, jugadorPriceEdt, bestSuitedEdt, jugadorImgEdt, jugadorLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    JugadorRVModal jugadorRVModal;
    private ProgressBar loadingPB;
    //creating a string para nuestro jugador id.
    private String jugadorID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jugador);
        //inicializando todas nuestras variables en la línea de abajo.
        Button addJugadorBtn = findViewById(R.id.idBtnAddJugador);
        jugadorNameEdt = findViewById(R.id.idEdtJugadorName);
        jugadorDescEdt = findViewById(R.id.idEdtJugadorDescription);
        jugadorPriceEdt = findViewById(R.id.idEdtJugadorPrice);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        jugadorImgEdt = findViewById(R.id.idEdtJugadorImageLink);
        jugadorLinkEdt = findViewById(R.id.idEdtJugadorLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //en la línea de abajo estamos obteniendo nuestra clase modal en la que hemos pasado.
        jugadorRVModal = getIntent().getParcelableExtra("jugador");
        Button deleteJugadorBtn = findViewById(R.id.idBtnDeleteJugador);

        if (jugadorRVModal != null) {
            //en la línea de abajo estamos configurando datos para nuestro texto de edición de nuestra clase modal.
            jugadorNameEdt.setText(jugadorRVModal.getJugadorName());
            jugadorPriceEdt.setText(jugadorRVModal.getJugadorPrice());
            bestSuitedEdt.setText(jugadorRVModal.getBestSuitedFor());
            jugadorImgEdt.setText(jugadorRVModal.getJugadorImg());
            jugadorLinkEdt.setText(jugadorRVModal.getJugadorLink());
            jugadorDescEdt.setText(jugadorRVModal.getJugadorDescription());
            jugadorID = jugadorRVModal.getJugadorId();
        }

        //en la línea de abajo estamos inicializando nuestra referencia de base de datos y estamos agregando un hijo como nuestra identificación de jugador.
        databaseReference = firebaseDatabase.getReference("Jugadores").child(jugadorID);
        //en la línea de abajo estamos agregando un oyente de clics para nuestro botón de agregar jugador.
        addJugadorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //en la línea de abajo estamos haciendo que nuestra barra de progreso sea visible.
                loadingPB.setVisibility(View.VISIBLE);
                //en la línea de abajo estamos obteniendo datos de nuestro texto de edición.
                String jugadorName = jugadorNameEdt.getText().toString();
                String jugadorDesc = jugadorDescEdt.getText().toString();
                String jugadorPrice = jugadorPriceEdt.getText().toString();
                String bestSuited = bestSuitedEdt.getText().toString();
                String jugadorImg = jugadorImgEdt.getText().toString();
                String jugadorLink = jugadorLinkEdt.getText().toString();
                //en la línea de abajo estamos creando un mapa para pasar datos usando un par de clave y valor.
                Map<String, Object> map = new HashMap<>();
                map.put("jugadorName", jugadorName);
                map.put("jugadorDescription", jugadorDesc);
                map.put("jugadorPrice", jugadorPrice);
                map.put("bestSuitedFor", bestSuited);
                map.put("jugadorImg", jugadorImg);
                map.put("jugadorLink", jugadorLink);
                map.put("jugadorId", jugadorID);

                //en la línea de abajo estamos llamando a una referencia de base de datos en el detector de eventos de valor agregado y en el método de cambio de datos
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // haciendo que la visibilidad de la barra de progreso desaparezca.
                        loadingPB.setVisibility(View.GONE);
                        //añadiendo un mapa a nuestra base de datos.
                        databaseReference.updateChildren(map);
                        //en la línea de abajo estamos mostrando un mensaje de brindis.
                        Toast.makeText(EditJugadorActivity.this, "Actualizando Jugador...", Toast.LENGTH_SHORT).show();
                        //abrir una nueva actividad después de actualizar nuestro Jugador.
                        startActivity(new Intent(EditJugadorActivity.this, com.example.realmadrid.MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //mostrando un mensaje de falla en el brindis.
                        Toast.makeText(EditJugadorActivity.this, "Ha fracasado la actualizacion del jugador..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //agregando un detector de clics para nuestro botón de eliminar jugador.
        deleteJugadorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamar a un método para eliminar una jugadora
                deleteJugador();
            }
        });

    }

    private void deleteJugador() {
        // en la línea de abajo llamando a un método para borrar el jugador.
        databaseReference.removeValue();
        //mostrando un mensaje de brindis en la línea de abajo.
        Toast.makeText(this, "Jugador Deleted..", Toast.LENGTH_SHORT).show();
        //abrir una actividad principal en la línea de abajo.
        startActivity(new Intent(EditJugadorActivity.this, com.example.realmadrid.MainActivity.class));
    }
}
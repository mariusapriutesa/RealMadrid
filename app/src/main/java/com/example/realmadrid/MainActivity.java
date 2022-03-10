package com.example.realmadrid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements JugadorRVAdapter.JugadorClickInterface {

    //creando variables para fab, firebase database, progress bar, list, adapter,firebase auth, recycler view and relative layout.
    private FloatingActionButton addJugadorFAB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView jugadorRV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<JugadorRVModal> jugadorRVModalArrayList;
    private JugadorRVAdapter jugadorRVAdapter;
    private RelativeLayout homeRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing all our variables.
        jugadorRV = findViewById(R.id.idRVJugadores);
        homeRL = findViewById(R.id.idRLBSheet);
        loadingPB = findViewById(R.id.idPBLoading);
        addJugadorFAB = findViewById(R.id.idFABAddJugador);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        jugadorRVModalArrayList = new ArrayList<>();
        // en la línea de abajo estamos obteniendo la referencia de la base de datos.
        databaseReference = firebaseDatabase.getReference("Jugadores");
        // En la línea de abajo agregando un click listener para nuestro floating action button.
        addJugadorFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrir una nueva actividad para agregar un jugador.
                Intent i = new Intent(MainActivity.this, AddJugadorActivity.class);
                startActivity(i);
            }
        });
        // en la línea de abajo inicializando nuestra clase de adaptador.
        jugadorRVAdapter = new JugadorRVAdapter(jugadorRVModalArrayList, this, this::onJugadorClick);
        // configurando el simulador de diseño para la vista del reciclador en la línea de abajo.
        jugadorRV.setLayoutManager(new LinearLayoutManager(this));
        //configurando el adaptador a recycler view abajo.
        jugadorRV.setAdapter(jugadorRVAdapter);
        // en la línea de abajo llamando a un método para obtener jugadores de la base de datos.
        getJugadores();
    }

    private void getJugadores() {
        // en la línea de abajo limpiando nuestra lista.
        jugadorRVModalArrayList.clear();
        // en la línea de abajo estamos llamando al método add child event listener para leer los datos.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //en la línea de abajo estamos ocultando nuestra barra de progreso.
                loadingPB.setVisibility(View.GONE);
                // agregando una instantánea a nuestra lista de matrices en la línea de abajo.
                jugadorRVModalArrayList.add(snapshot.getValue(JugadorRVModal.class));
                //notificando a nuestro adaptador que los datos han cambiado.
                jugadorRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //este método se llama cuando se agrega un nuevo hijo, estamos notificando a nuestro adaptador y haciendo que la visibilidad de la barra de progreso desaparezca.
                loadingPB.setVisibility(View.GONE);
                jugadorRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notificando a nuestro adaptador cuando se elimine el niño.
                jugadorRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notificando a nuestro adaptador cuando se mueve al niño.
                jugadorRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onJugadorClick(int position) {
        // llamando a un método para mostrar una hoja inferior en la línea de abajo.
        displayBottomSheet(jugadorRVModalArrayList.get(position));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // agregando un clik list para la opción seleccionada en la línea de abajo.
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                //mostrando a toast message en el usuario cerró la sesión en el interior al hacer clic.
                Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
                // En la línea inferior estamos cerrando la sesión de nuestra usuaria.
                mAuth.signOut();
                // en la línea de abajo estamos abriendo nuestra actividad de inicio de sesión.
                Intent i = new Intent(MainActivity.this, com.example.realmadrid.LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //en la línea de abajo estamos inflando nuestro archivo de menú para mostrar nuestras opciones de menú.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void displayBottomSheet(JugadorRVModal modal) {
        // en la línea de abajo estamos creando nuestro cuadro de diálogo de hoja inferior.
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        //en la línea de abajo estamos inflando un archivo de diseño para su hoja inferior.
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, homeRL);
        // configurando la vista de contenido para la hoja inferior en la línea de abajo.
        bottomSheetTeachersDialog.setContentView(layout);
        // en la línea de abajo estamos configurando un cancelable
        bottomSheetTeachersDialog.setCancelable(false);
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);
        //llamando a un método para mostrar nuestra última hoja.
        bottomSheetTeachersDialog.show();
        // en la línea de abajo estamos creando variables para nuestra vista de texto y vista de imagen dentro de la hoja inferior
        //e inicializarlos con sus identificaciones.
        TextView jugadorNameTV = layout.findViewById(R.id.idTVJugadorName);
        TextView jugadorDescTV = layout.findViewById(R.id.idTVJugadorDesc);
        TextView suitedForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView priceTV = layout.findViewById(R.id.idTVJugadorPrice);
        ImageView jugadorIV = layout.findViewById(R.id.idIVJugador);
        // en la línea de abajo estamos configurando datos para diferentes vistas en la línea de abajo.
        jugadorNameTV.setText(modal.getJugadorName());
        jugadorDescTV.setText(modal.getJugadorDescription());
        suitedForTV.setText("Suited for " + modal.getBestSuitedFor());
        priceTV.setText("Precio:" + modal.getJugadorPrice()+"€");
        Picasso.get().load(modal.getJugadorImg()).into(jugadorIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);
        Button editBtn = layout.findViewById(R.id.idBtnEditJugador);

        //agregando el click listener para nuestro edit button.
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //en la línea de abajo estamos abriendo nuestro EditJugadorActivity en la línea de abajo.
                Intent i = new Intent(MainActivity.this, EditJugadorActivity.class);
                //en la línea de abajo estamos pasando nuestro jugador modal
                i.putExtra("jugador", modal);
                startActivity(i);
            }
        });
        //adding click listener para nuestro botón de vista en la línea de abajo.
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //en la línea de abajo estamos navegando al navegador para mostrar los detalles del jugador desde su url
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(modal.getJugadorLink()));
                startActivity(i);
            }
        });

    }


}
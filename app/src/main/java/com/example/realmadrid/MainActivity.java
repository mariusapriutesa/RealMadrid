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

    //creating variables for fab, firebase database, progress bar, list, adapter,firebase auth, recycler view and relative layout.
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
        //on below line we are getting database reference.
        databaseReference = firebaseDatabase.getReference("Jugadores");
        //on below line adding a click listener for our floating action button.
        addJugadorFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening a new activity for adding a jugador.
                Intent i = new Intent(MainActivity.this, AddJugadorActivity.class);
                startActivity(i);
            }
        });
        //on below line initializing our adapter class.
        jugadorRVAdapter = new JugadorRVAdapter(jugadorRVModalArrayList, this, this::onJugadorClick);
        //setting layout malinger to recycler view on below line.
        jugadorRV.setLayoutManager(new LinearLayoutManager(this));
        //setting adapter to recycler view on below line.
        jugadorRV.setAdapter(jugadorRVAdapter);
        //on below line calling a method to fetch jugadores from database.
        getJugadores();
    }

    private void getJugadores() {
        //on below line clearing our list.
        jugadorRVModalArrayList.clear();
        //on below line we are calling add child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //on below line we are hiding our progress bar.
                loadingPB.setVisibility(View.GONE);
                //adding snapshot to our array list on below line.
                jugadorRVModalArrayList.add(snapshot.getValue(JugadorRVModal.class));
                //notifying our adapter that data has changed.
                jugadorRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //this method is called when new child is added we are notifying our adapter and making progress bar visibility as gone.
                loadingPB.setVisibility(View.GONE);
                jugadorRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //notifying our adapter when child is removed.
                jugadorRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //notifying our adapter when child is moved.
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
        //calling a method to display a bottom sheet on below line.
        displayBottomSheet(jugadorRVModalArrayList.get(position));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //adding a click listner for option selected on below line.
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                //displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
                //on below line we are signing out our user.
                mAuth.signOut();
                //on below line we are opening our login activity.
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
        //on below line we are inflating our menu file for displaying our menu options.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void displayBottomSheet(JugadorRVModal modal) {
        //on below line we are creating our bottom sheet dialog.
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        //on below line we are inflating our layout file for our bottom sheet.
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, homeRL);
        //setting content view for bottom sheet on below line.
        bottomSheetTeachersDialog.setContentView(layout);
        //on below line we are setting a cancelable
        bottomSheetTeachersDialog.setCancelable(false);
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);
        //calling a method to display our bottom sheet.
        bottomSheetTeachersDialog.show();
        //on below line we are creating variables for our text view and image view inside bottom sheet
        //and initialing them with their ids.
        TextView jugadorNameTV = layout.findViewById(R.id.idTVJugadorName);
        TextView jugadorDescTV = layout.findViewById(R.id.idTVJugadorDesc);
        TextView suitedForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView priceTV = layout.findViewById(R.id.idTVJugadorPrice);
        ImageView jugadorIV = layout.findViewById(R.id.idIVJugador);
        //on below line we are setting data to different views on below line.
        jugadorNameTV.setText(modal.getJugadorName());
        jugadorDescTV.setText(modal.getJugadorDescription());
        suitedForTV.setText("Suited for " + modal.getBestSuitedFor());
        priceTV.setText("Precio:" + modal.getJugadorPrice()+"€");
        Picasso.get().load(modal.getJugadorImg()).into(jugadorIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);
        Button editBtn = layout.findViewById(R.id.idBtnEditJugador);

        //adding on click listener for our edit button.
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on below line we are opening our EditJugadorActivity on below line.
                Intent i = new Intent(MainActivity.this, EditJugadorActivity.class);
                //on below line we are passing our jugador modal
                i.putExtra("jugador", modal);
                startActivity(i);
            }
        });
        //adding click listener for our view button on below line.
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on below line we are navigating to browser for displaying jugador details from its url
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(modal.getJugadorLink()));
                startActivity(i);
            }
        });

    }


}
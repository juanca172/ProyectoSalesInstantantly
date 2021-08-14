package com.example.proyectosalesinstantly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeView extends AppCompatActivity {
    //encapsulamiento del spinner
    private Spinner spinner;
    private AutoCompleteTextView aCText;
    //encapsulamiento del listView
    private RecyclerView rvtiendas;
    private AdaptadorRecyclerView adapter;
    private List<CardViewAtributos> items;
    FirebaseDatabase database;
    FirebaseAuth auth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child("users");
        //Relacion entre la vista y la parte logica
        spinner = (Spinner)findViewById(R.id.PruebaSpinner);
        //Creacion de un array el cual tiene las categorias
        String [] categorias = {"Tecnologia", "Belleza y cuidado personal", "Articulos del hogar","televisores", "moda", "juegos y juguetes", "animales y mascotas"};
        //creacion de un adaptador para enviar datos al spinner
        ArrayAdapter<String> arrayAdaptador = new ArrayAdapter<String>(this, R.layout.recurso_spinner, categorias);
        spinner.setAdapter(arrayAdaptador);
        //creacion de un adaptador para enviar datos al listView
        initViews();
        initValues();
    }
    public void initViews() {
        rvtiendas = findViewById(R.id.rvLista);
    }
    public void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvtiendas.setLayoutManager(manager);

        items = getItems();
        adapter = new AdaptadorRecyclerView(items);
        rvtiendas.setAdapter(adapter);
    }

    public List<CardViewAtributos> getItems() {
        List<CardViewAtributos> itemsList = new ArrayList<>();
        itemsList.add(new CardViewAtributos("Tomate", "salsa de tomate", R.drawable.tomate));
        itemsList.add(new CardViewAtributos("axe", "desodorante",R.drawable.axe));
        itemsList.add(new CardViewAtributos("ponds", "desodorante", R.drawable.ponds));
        itemsList.add(new CardViewAtributos("axe", "desodorante",R.drawable.axe));
        itemsList.add(new CardViewAtributos("ego", "salsa de tomate", R.drawable.ego));


        return itemsList;
    }
    public void goToVistaIniciarSesionDesdeLasTiendas(View view) {
        Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_LONG).show();
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
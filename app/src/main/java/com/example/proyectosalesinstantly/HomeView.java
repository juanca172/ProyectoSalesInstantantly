package com.example.proyectosalesinstantly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeView extends AppCompatActivity {
    //encapsulamiento del spinner
    private Spinner spinner;
    private AutoCompleteTextView aCText;
    //encapsulamiento del listView
    private RecyclerView rvtiendas;
    private AdaptadorRecyclerView adapter;
    //private List<CardViewAtributos> items;
    FirebaseFirestore db;
    FirebaseDatabase database;
    FirebaseAuth auth;
    private DatabaseReference mDatabase;
    public String url;


    List<CardViewAtributos> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);
        //fireStore
        db = FirebaseFirestore.getInstance();
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
        ObtenerValores();
    }
    public void initViews() {
        rvtiendas = findViewById(R.id.rvLista);
    }
    public void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvtiendas.setLayoutManager(manager);
        //items = getItems();
        adapter = new AdaptadorRecyclerView(items,this);
        rvtiendas.setAdapter(adapter);
    }
    public void goToVistaIniciarSesionDesdeLasTiendas(View view) {
        Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_LONG).show();
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void goToPerfil(View view){
        Intent intent = new Intent(this, PerfilUsuarioView.class);
        startActivity(intent);
    }
    //metodo para obtener valores del fireStore
    public void ObtenerValores() {
        db.collection("Productos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Integer size  = task.getResult().size();
                    int count = 1;
                    for(QueryDocumentSnapshot document: task.getResult()){
                        String uri = document.getString("Imagen");
                        String nombre = document.getString("Nombre");
                        String descripcion = document.getString("Descripcion");
                        items.add(new CardViewAtributos(nombre, descripcion,uri));
                        if(count <size){
                            count++;
                        }
                        else{
                            initValues();
                        }
                    }
                }
                else {
                    Toast.makeText(HomeView.this, "No se encontraron los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
package com.example.proyectosalesinstantly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class HomeView extends AppCompatActivity {
    //encapsulamiento del spinner
    private Spinner spinner;
    //encapsulamiento del listView
    private RecyclerView rvtiendas;
    private AdaptadorRecyclerView adapter;
    //private List<CardViewAtributos> items;
    FirebaseFirestore db;
    FirebaseDatabase database;
    FirebaseAuth auth;
    private DatabaseReference mDatabase;

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
        String [] categorias = {"Tecnologia", "Articulos del hogar"};
        //creacion de un adaptador para enviar datos al spinner
        ArrayAdapter<String> arrayAdaptador = new ArrayAdapter<String>(this, R.layout.recurso_spinner, categorias);
        spinner.setAdapter(arrayAdaptador);
        //creacion de un adaptador para enviar datos al listView
        initViews();

        //Enlace del spiner a la seleccion de las categorias
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getItemAtPosition(position).equals("Articulos del hogar")){
                    ValoresArticulosDelHogar("Productos");

                }
                if (spinner.getItemAtPosition(position).equals("Tecnologia")){
                    ValoresTecnologia("Tecnologia");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
    //metodo para obtener valores de los Articulos Del Hogar del fireStore
    public void ValoresArticulosDelHogar(String productos) {
        db.collection("Productos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                items.clear();
                if(task.isSuccessful()){
                    Integer size  = task.getResult().size();
                    int count = 1;
                    for(QueryDocumentSnapshot document: task.getResult()){
                        String uri = document.getString("Imagen");
                        String nombre = document.getString("Nombre");
                        String descripcion = document.getString("Descripcion");
                        Double precio = document.getDouble("Precio");
                        items.add(new CardViewAtributos(nombre, descripcion,uri,precio));
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

    //metodo para obtener valores de Tecnologia del fireStore
    private void ValoresTecnologia(String tecnologia) {
        db.collection("Tecnologia").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                items.clear();
                if(task.isSuccessful()){
                    Integer size  = task.getResult().size();
                    int count = 1;
                    for(QueryDocumentSnapshot document: task.getResult()){
                        String uri = document.getString("Imagen");
                        String nombre = document.getString("Nombre");
                        String descripcion = document.getString("Descripcion");
                        Double precio = document.getDouble("Precio");
                        items.add(new CardViewAtributos(nombre, descripcion,uri,precio));
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
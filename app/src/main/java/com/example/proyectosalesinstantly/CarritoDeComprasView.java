package com.example.proyectosalesinstantly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CarritoDeComprasView extends AppCompatActivity {
    private RecyclerView rvCarrito;
    private AdaptadorRecyclerView adaptador;
    FirebaseFirestore db;
    List<CardViewAtributos> itemsCard = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_de_compras_view);

        db = FirebaseFirestore.getInstance();
        rvCarrito = findViewById(R.id.recyclerViewCarrito);
        initValues();
        ValoresArticulosDelHogar();
    }
    public void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvCarrito.setLayoutManager(manager);
        //items = getItems();
        adaptador = new AdaptadorRecyclerView(itemsCard,this);
        rvCarrito.setAdapter(adaptador);
    }
    //metodo para obtener valores de los Articulos Del Hogar del fireStore
    public void ValoresArticulosDelHogar() {
        db.collection("Carrito").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Integer size  = task.getResult().size();
                    int count = 1;
                    for(QueryDocumentSnapshot document: task.getResult()){
                        String uri = document.getString("Imagen");
                        String nombre = document.getString("Nombre");
                        String descripcion = document.getString("Descripcion");
                        Double precio = document.getDouble("Price");
                        itemsCard.add(new CardViewAtributos(nombre, descripcion,uri,precio));
                        if(count <size){
                            count++;
                        }
                        else{
                            initValues();
                        }
                    }
                }
                else {
                    Toast.makeText(CarritoDeComprasView.this, "No se encontraron los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
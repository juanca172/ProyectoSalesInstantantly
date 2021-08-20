package com.example.proyectosalesinstantly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    Integer size;
    double total;
    TextView preciosTotales;
    double TotalAMostrar = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_de_compras_view);

        db = FirebaseFirestore.getInstance();
        rvCarrito = findViewById(R.id.recyclerViewCarrito);
        initValues();
        ValoresArticulosDelHogar();
        preciosTotales = findViewById(R.id.tvTotal);
        TotalAComprar();
    }
    public void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvCarrito.setLayoutManager(manager);
        //items = getItems();
        adaptador = new AdaptadorRecyclerView(itemsCard,this);
        rvCarrito.setAdapter(adaptador);
    }

    public void SeguirComprando(View view){
        Intent intent = new Intent(CarritoDeComprasView.this, HomeView.class );
        startActivity(intent);
    }

    public void limpiarCarrito(View view){
        itemsCard.clear(); // clear list
        adaptador.notifyDataSetChanged();

        TotalAMostrar = 0.0;
        preciosTotales.setText("$" +String.valueOf(TotalAMostrar));

        for(int i= 0; i <= size; i++) {

            db.collection("Carrito").document(Integer.toString(i))
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CarritoDeComprasView.this, "carrito elliminado", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CarritoDeComprasView.this, "error", Toast.LENGTH_LONG).show();

                        }
                    });
        }
    }

    //metodo para obtener valores de los Articulos Del Hogar del fireStore
    public void ValoresArticulosDelHogar() {
        db.collection("Carrito").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                     size  = task.getResult().size();
                    int count = 1;
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Toast.makeText(CarritoDeComprasView.this, Integer.toString(count), Toast.LENGTH_SHORT).show();

                        String uri = document.getString("Imagen");
                        String nombre = document.getString("Nombre");
                        String descripcion = document.getString("Descripcion");
                        Double precio = document.getDouble("Price");
                        total = precio;
                        TotalAComprar();
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


    public void CompraRealizada(View view) {
        Toast.makeText(this, "Compra Realizada",Toast.LENGTH_SHORT).show();
    }

    public void TotalAComprar(){
        TotalAMostrar = total + TotalAMostrar;
        preciosTotales.setText( "$"+ String.valueOf(TotalAMostrar));
    }
}
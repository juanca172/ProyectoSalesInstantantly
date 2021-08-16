package com.example.proyectosalesinstantly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeView extends AppCompatActivity {

    private static final String TAG = "DocSnippets";
    //encapsulamiento del spinner
    private Spinner spinner;
    private AutoCompleteTextView aCText;
    //encapsulamiento del listView
    private RecyclerView rvtiendas;
    private AdaptadorRecyclerView adapter;
    private List<CardViewAtributos> items;
    FirebaseDatabase database;
    FirebaseAuth auth;

    int count;



    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);

        mStorageRef = FirebaseStorage.getInstance().getReference();
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
        getItems();

    }

    public List<CardViewAtributos> getItems() {
        List<CardViewAtributos> itemsList = new ArrayList<>();
        StorageReference ref  = mStorageRef.child("productos");
        ref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                Log.e("productos","productos obtenidos");
                int lengthItems = listResult.getItems().size();
                count = 1;
                for(StorageReference item : listResult.getItems()){
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            Toast.makeText(HomeView.this, uri.toString(), Toast.LENGTH_LONG ).show();
                            itemsList.add(new CardViewAtributos(    item.getName().toString(), "salsa de tomate", R.drawable.tomate, uri.toString()));
                            if(count < lengthItems){
                                count++;
                            }else{
                                items =itemsList;
                                adapter = new AdaptadorRecyclerView(items, HomeView.this);
                                rvtiendas.setAdapter(adapter);
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                }

            }
        });



        return itemsList;
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
}
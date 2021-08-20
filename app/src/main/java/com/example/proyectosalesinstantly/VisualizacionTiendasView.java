package com.example.proyectosalesinstantly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class VisualizacionTiendasView extends AppCompatActivity {
    private LocationManager ubicacion;
    private TextView descripcion;
    Double latitud;
    Double longitud;
    private ImageView imagen;

    private ImageView imageTienda;
    private TextView nombreTienda;
    private  TextView descTienda;

    private TextView textoNombre;
    private TextView TextoDescripcion;
    private TextView precioP;
    private CardViewAtributos cardViewAtributos;

    public String imgenParaOtraVista;
    public String nombreParaOtraVista;
    public String descripcionParaOtraVista;
    public Double precioParaOtraVista;
    private Button carrito;
    FirebaseFirestore db;
    Map<String, Object> map = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_visualizacion_tiendas_view);
        descripcion = findViewById(R.id.tvDescripcionTienda);
        imagen = findViewById(R.id.imgvImagenDelCardView);
        textoNombre = findViewById(R.id.tvNombreProductoCardVIEW);
        TextoDescripcion = findViewById(R.id.tvDescripcionProductoCardVIEW);
        precioP = findViewById(R.id.precioProductoView);
        carrito = findViewById(R.id.btnCarritoCompras);
        imageTienda = findViewById(R.id.imageTienda);
        nombreTienda = findViewById(R.id.nombreTienda);
        descTienda = findViewById(R.id.descTienda);



        getTienda();
        localizacion();
        localizarMovimiento();
        initValues();
    }

    private void getTienda() {
        DocumentReference docRef = db.collection("Tiendas").document("1");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        nombreTienda.setText(document.getString("nombre"));
                        descTienda.setText(document.getString("descripcion"));
                        // latitud = document.getDouble("lat");
                        // longitud =document.getDouble("long");
                        Glide.with(VisualizacionTiendasView.this).load(document.getString("url")).into(imageTienda);

                    }
                }
            }
        });

    }



    private void initValues() {
        cardViewAtributos= (CardViewAtributos) getIntent().getExtras().getSerializable("itemDetail");
        Glide.with(this).load(cardViewAtributos.getImagenDeTienda()).into(imagen);
        textoNombre.setText(cardViewAtributos.getNombre());
        TextoDescripcion.setText(cardViewAtributos.getDescripcion());
        precioP.setText(String.valueOf(cardViewAtributos.getPrecioProducto()));
        imgenParaOtraVista = cardViewAtributos.getImagenDeTienda();
        nombreParaOtraVista= cardViewAtributos.getNombre();
        descripcionParaOtraVista= cardViewAtributos.getDescripcion();
        precioParaOtraVista = cardViewAtributos.getPrecioProducto();

        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuentaDeCantidadDeDocumentos();

            }
        });
    }

    private void CrearDatos(int cuenta) {
        String titulo = nombreParaOtraVista;
        String descripcion = descripcionParaOtraVista;
        String imagen = imgenParaOtraVista;
        Double price = precioParaOtraVista;
        map.put("Nombre", titulo);
        map.put("Descripcion", descripcion);
        map.put("Imagen", imagen);
        map.put("Price", price);
        String valorCuenta = String.valueOf(cuenta);
        db.collection("Carrito").document(valorCuenta).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(VisualizacionTiendasView.this, "Se agrego Producto",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(VisualizacionTiendasView.this, CarritoDeComprasView.class);
                startActivity(i);
            }
        });

    }
    private void cuentaDeCantidadDeDocumentos(){
        db.collection("Carrito").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Integer count = task.getResult().size();
                CrearDatos(count);
                count++;
            }
        });
    }

    private void localizacion() {
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1000);
        }
        Location location = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null) {
            descripcion.setText("latitud:" + location.getLatitude() + "\n longitud: "+ location.getLongitude());

        }
    }
    public void goToMap(View view) {
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("latitud",latitud);
        i.putExtra("longitud", longitud);
        startActivity(i);
    }

    private void localizarMovimiento() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> direccion = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    //String miDireccion = "Direccion: " + direccion.get(0).getAddressLine(0);
                    //Toast.makeText(VisualizacionTiendasView.this, miDireccion,Toast.LENGTH_SHORT).show();
                    latitud = location.getLatitude();
                    longitud= location.getLongitude();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(VisualizacionTiendasView.this, "latitud:" + location.getLatitude() + "\n longitud_ "+ location.getLongitude(), Toast.LENGTH_SHORT ).show();
            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }
            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

        };
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
    }
}
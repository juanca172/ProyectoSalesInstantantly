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
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class VisualizacionTiendasView extends AppCompatActivity {
    private LocationManager ubicacion;
    private TextView descripcion;
    Double latitud;
    Double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizacion_tiendas_view);
        descripcion = findViewById(R.id.tvDescripcionTienda);
        localizacion();
        localizarMovimiento();
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
                    String miDireccion = "Direccion: " + direccion.get(0).getAddressLine(0);
                    Toast.makeText(VisualizacionTiendasView.this, miDireccion,Toast.LENGTH_SHORT).show();
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
package com.example.proyectosalesinstantly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText etCampoEmail, etCampoContraseña;
    private Button btnIniciarSesion, btnNotCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCampoEmail = findViewById(R.id.etCorreoElectronico);
        etCampoContraseña = findViewById(R.id.etContraseña);

    }

    public void GoToRegistroUsuario(View view) {
        Intent i = new Intent(MainActivity.this, RegistroActivityView.class);
        startActivity(i);
    }
}
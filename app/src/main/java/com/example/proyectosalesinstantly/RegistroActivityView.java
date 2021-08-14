package com.example.proyectosalesinstantly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivityView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etEmail, etContrasenia;
    private Button btnRegistro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_view);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etCorreoElectronicoRegistro);
        etContrasenia = findViewById(R.id.etContraseñaRegistro);

        btnRegistro = findViewById(R.id.btnRegistroView);


        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campoEmail = etEmail.getText().toString();
                String campoContraseña = etContrasenia.getText().toString();

                mAuth.createUserWithEmailAndPassword(campoEmail,campoContraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistroActivityView.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(RegistroActivityView.this, "Error al Registrar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
package com.example.proyectosalesinstantly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText usuario;
    private EditText contraseña;
    private Button BtnIngresar;
    private Boolean variableSharedPreference;
    FirebaseAuth auth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);


        preferences = getSharedPreferences("usuario", 0);
        editor = preferences.edit();
        usuario = (EditText) findViewById(R.id.txe_correo_usuario);
        contraseña = (EditText) findViewById(R.id.txe_contraseña);
        BtnIngresar =findViewById(R.id.BtnIngresar);
        auth = FirebaseAuth.getInstance();
        BtnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCamposVacios()) {
                    String Usuario = usuario.getText().toString();
                    String Contraseña = contraseña.getText().toString();

                    auth.signInWithEmailAndPassword(Usuario, Contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"inicio sesion",Toast.LENGTH_SHORT).show();
                                editor.putString("correo", Usuario);
                                editor.commit();
                            }else {
                                Toast.makeText(MainActivity.this,"Error al iniciar sesion",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this,"Revisa Los Campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public Boolean validarCamposVacios() {
        String usuario1 = usuario.getText().toString();
        String contraseña1 = contraseña.getText().toString();
        boolean retorno = true;
        if (usuario1.isEmpty()) {
            usuario.setError("campo vacío");
            retorno = false;
            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
        if (contraseña1.isEmpty()) {
            contraseña.setError("campo vacío");
            retorno = false;
            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
        return retorno;
    }

    //Metodo para pasar a Crear Cuenta
    public void GoToRegistroUsuario(View view) {
        Intent i = new Intent(MainActivity.this, RegistroActivityView.class);
        startActivity(i);
    }
}
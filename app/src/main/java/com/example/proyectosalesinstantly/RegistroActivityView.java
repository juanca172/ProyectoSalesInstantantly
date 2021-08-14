package com.example.proyectosalesinstantly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class RegistroActivityView extends AppCompatActivity {
    private EditText NombreApellido;
    private Button campoFecha;
    private Button botonRegister;
    private EditText campoCorreo;
    private EditText campoContraseña;
    FirebaseAuth mAuth;
    private EditText Ccontraseña;
    private EditText direccion;
    private EditText ciudad;
    private EditText Ntelefono;
    private DatabaseReference mDatabase;
    private DatePickerDialog datePickerDialog;
    long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_view);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");
        //metodo para obtener el numero de hijos
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId =(snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        NombreApellido = findViewById(R.id.editTextNombreyApellido);
        campoFecha = findViewById(R.id.eFecha);
        botonRegister = findViewById(R.id.btnRegistrarse);
        campoCorreo = findViewById(R.id.editTextCorreoElectronico);
        campoContraseña = findViewById(R.id.editTextContraseña);
        Ccontraseña = (EditText) findViewById(R.id.ediTextContraseñaConfirmacion);
        direccion = (EditText) findViewById(R.id.editTextDireccion);
        ciudad = (EditText) findViewById(R.id.editTextCiudad);
        Ntelefono = (EditText) findViewById(R.id.editTextNumeroTelefonico);
        initDatePicker();
        campoFecha.setText(getTodayDate());
        botonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCamposVacios()) {
                    mAuth = FirebaseAuth.getInstance();
                    String usuario2 = campoCorreo.getText().toString();
                    String contraseña2 = campoContraseña.getText().toString();

                    mAuth.createUserWithEmailAndPassword(usuario2,contraseña2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                writeNewUser("", NombreApellido.getText().toString(), campoCorreo.getText().toString(), contraseña2, direccion.getText().toString(), ciudad.getText().toString(), Ntelefono.getText().toString(), campoFecha.getText().toString());
                            }else {
                                Toast.makeText(RegistroActivityView.this,"Error al registrar",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else{
                    Toast.makeText(RegistroActivityView.this,"Revisa Los Campos", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        month = month +1;
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1 ;
                String date = makeDateString(day, month, year);
                campoFecha.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);


    }

    private String makeDateString(int day, int month, int year) {
        return day + " / " + month +" / "+ year;

    }

    public void onDataChange(){

    }

    public void writeNewUser(String userId, String nombre, String email, String contrasena, String direccion, String ciudad, String telefono, String fecha) {
        ModeloUsuario user = new ModeloUsuario( nombre,  email,  contrasena,  direccion,  ciudad,  telefono,  fecha);

        mDatabase.child(String.valueOf((maxId +1))).setValue(user);
        //mDatabase.setValue(user);
        //mDatabase.child("users").child(userId).setValue(user);
        Toast.makeText( RegistroActivityView.this,"Usuario Registrado",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistroActivityView.this, MainActivity.class );
        startActivity(intent);
    }

    public boolean validarCamposVacios () {
        boolean retorno = true;

        String usu = campoCorreo.getText().toString();
        String Icon = campoContraseña.getText().toString();
        String Ccon = Ccontraseña.getText().toString();
        String dir = direccion.getText().toString();
        String ciu = ciudad.getText().toString();
        String Ntel = Ntelefono.getText().toString();
        String Fnac = campoFecha.getText().toString();
        String NoAp = NombreApellido.getText().toString();

        if (NoAp.isEmpty()) {
            NombreApellido.setError("campo vacío");
            retorno = false;
            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
        if (usu.isEmpty()) {
            campoCorreo.setError("campo vacío");
            retorno = false;
            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
        if (Icon.isEmpty()) {
            campoContraseña.setError("campo vacío");
            retorno = false;
            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
        if (Ccon.isEmpty()) {
            Ccontraseña.setError("campo vacío");
            retorno = false;
            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
        if (dir.isEmpty()) {
            direccion.setError("campo vacío");
            retorno = false;
            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
        if (ciu.isEmpty()) {
            ciudad.setError("campo vacío");
            retorno = false;
            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
        if (Ntel.isEmpty()) {
            Ntelefono.setError("campo vacío");
            retorno = false;
            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
        if (Fnac.isEmpty()) {
            campoFecha.setError("campo vacío");
            retorno = false;
            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
        return retorno;

    }

    public void openDatePicker(View view) {
        datePickerDialog.show();

    }
}
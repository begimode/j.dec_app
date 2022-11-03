package com.jorge.sprint0_jorgelarrosaquesada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    String ip = "192.168.0.14";
    EditText correo;
    EditText contrasenya;
    EditText nombre;
    EditText apellidos;
    EditText telefono;
    Button registrar;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        correo = findViewById(R.id.editTextEmail);
        contrasenya = findViewById(R.id.editTextPassword);
        nombre = findViewById(R.id.editTextCorreo);
        apellidos = findViewById(R.id.editTextPass);
        telefono = findViewById(R.id.telefono);
        registrar = findViewById(R.id.buttonRegistrar);
        loginText = findViewById(R.id.textView6);

        //Librería encargada ser cocentarse con el Servidor
        AndroidNetworking.initialize(getApplicationContext());

        TextView leer = findViewById(R.id.leer);
        leer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarTerminos(view);
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarUsuario(view);
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarVolverLogin(view);
            }
        });
    }

    private void pulsarTerminos(View view){
        Intent intent = new Intent(this, Politica_Privacidad.class);
        startActivity(intent);
    }

    private void pulsarVolverLogin(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private Usuario guardarUsuario(String correo, String contrasenya, int telefono, String nombre, String apellidos){
        Usuario usuario = new Usuario();

        usuario.setCorreo(correo);
        usuario.setContrasenya(contrasenya);
        usuario.setTelefono(telefono);
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setEstado("No Verificado");

        return usuario;
    }


    private void insertarUsuario(View view){
        Usuario usuario = guardarUsuario(correo.getText().toString(),contrasenya.getText().toString(),Integer.parseInt(telefono.getText().toString()),nombre.getText().toString(),apellidos.getText().toString());

        //Envíar datos POST
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("correo", usuario.getCorreo());
            jsonObject.put("contrasenya", usuario.getContrasenya());
            jsonObject.put("telefono", usuario.getTelefono());
            jsonObject.put("nombre", usuario.getNombre());
            jsonObject.put("apellidos", usuario.getApellidos());
            jsonObject.put("estado", usuario.getEstado());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://" + ip + ":8080/insertarUsuario")//Recordar cambiar ip cada vez que cambies de red
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

        Toast.makeText(getApplicationContext(),"Usuario Creado",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
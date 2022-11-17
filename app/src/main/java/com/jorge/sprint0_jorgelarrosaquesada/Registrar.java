package com.jorge.sprint0_jorgelarrosaquesada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

//----------------------------------------------------
// Archivo: Registrar.java
// J.Dec
//----------------------------------------------------

public class Registrar extends AppCompatActivity {

    //Se declran las variables
    String ip = "192.168.1.98";
    EditText correo;
    EditText contrasenya;
    EditText contrasenyaRepetir;
    EditText nombre;
    EditText apellidos;
    EditText telefono;
    Button registrar;
    TextView loginText;
    CheckBox terminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //Relaciono las variables con los id del layout
        correo = findViewById(R.id.editTextEmail);
        contrasenya = findViewById(R.id.editTextPassword);
        contrasenyaRepetir = findViewById(R.id.editTextPassword2);
        nombre = findViewById(R.id.editTextCorreo);
        apellidos = findViewById(R.id.editTextPass);
        telefono = findViewById(R.id.telefono);
        registrar = findViewById(R.id.buttonRegistrar);
        loginText = findViewById(R.id.textView6);
        terminos = findViewById(R.id.checkBox);
        TextView leer = findViewById(R.id.leer);

        //Librería encargada de conectarte con el Servidor
        AndroidNetworking.initialize(getApplicationContext());

        //Botón para llamar a la función pulsarTerminos()
        leer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarTerminos(view);
            }
        });

        //Botón para llamar a la función insertarUsuario()
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarUsuario(view);
            }
        });

        //Botón para llamar a la función pulsarVolverLogin()
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarVolverLogin(view);
            }
        });
    }

    // .................................................................
    // view: View
    // -->
    // pulsarTerminos() <--
    //
    // Esta función te lleva a la pestaña de Politica y Privacidad.
    // .................................................................

    private void pulsarTerminos(View view){
        Intent intent = new Intent(this, Politica_Privacidad.class);
        startActivity(intent);
    }

    // .................................................................
    // view: View
    // -->
    // pulsarVolverLogin() <--
    //
    // Esta función te lleva a la pestaña de Login.
    // .................................................................

    private void pulsarVolverLogin(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    // .................................................................
    // correo: Texto, contrasenya: Texto, telefono: R, nombre: Texto, apellidos: Texto
    // -->
    // guardarUsuario() <--
    // <--
    // Usuario:
    // {correo: Texto, contrasenya: Texto, telefono: int, nombre: Texto, apellidos: Texto, estado: String}
    //
    // Esta función crea un objeto usuario mediante un constructor al cual se le pasan los valores mencionados arriba y lo devuelve.
    // .................................................................

    private Usuario guardarUsuario(String correo, String contrasenya, int telefono, String nombre, String apellidos){
        Usuario usuario = new Usuario();

        usuario.setID_user(0);
        usuario.setCorreo(correo);
        usuario.setContrasenya(contrasenya);
        usuario.setTelefono(telefono);
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setEstado("No Verificado");

        return usuario;
    }

    // .................................................................
    // view: View
    // -->
    // insertarUsuario() <--
    //
    // Esta función crea un usuario con los valores que obtiene por los imputs, después hace un post al servidor node y por último te devuelve a la pestaña de login
    // .................................................................

    private void insertarUsuario(View view){

        if(contrasenya.getText().toString().equals(contrasenyaRepetir.getText().toString()) && (terminos.isChecked())){
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
                jsonObject.put("ID_user", null);

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
        }else {
            Toast.makeText(getApplicationContext(),"Vuelva a revisar los datos",Toast.LENGTH_SHORT).show();
        }
    }
}
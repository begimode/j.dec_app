package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

//----------------------------------------------------
// Archivo: EditarPerfil.java
// J.Dec
//----------------------------------------------------

public class EditarPerfil extends AppCompatActivity {

    //Se declran las variables
    String ip = "192.168.96.243";


    ImageView atras;
    TextView usuario;
    EditText nombre;
    EditText apellidos;
    EditText correo;
    EditText telefono;
    EditText contrasenya;
    ImageView cerrarSesion;
    Button aplicarCambios;
    Boolean sesion;

    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //Librería encargada ser cocentarse con el Servidor
        AndroidNetworking.initialize(getApplicationContext());

        //Sirve para guardar valores de en la memoria interna del la app
        myPreferences = PreferenceManager.getDefaultSharedPreferences(EditarPerfil.this);
        myEditor = myPreferences.edit();

        //Obtengo de la memoria interna el valor de la sesion
        sesion = myPreferences.getBoolean("sesion", false);

        //Compruebo si la sesión tiene como valor false, y si es así te lleva a la pestaña de Login
        if(sesion == false){
            Intent intent = new Intent(EditarPerfil.this, Login.class);
            startActivity(intent);
        }

        Log.d("pepe", ": edit " + myPreferences.getInt("ID_user", 0));
         /*
        Log.d("datos", ": login " + myPreferences.getString("correo", "unknown"));
        Log.d("datos", ": login " + myPreferences.getString("contrasenya", "unknown"));
        Log.d("datos", ": login " + myPreferences.getInt("telefono", 0));
        Log.d("datos", ": login " + myPreferences.getString("nombre", "unknown"));
        Log.d("datos", ": login " + myPreferences.getString("apellidos", "unknown"));
        Log.d("datos", ": login " + myPreferences.getString("estado", "unknown"));
        Log.d("datos", "Estado de la sesion: " + sesion);
        */

        //Relaciono las variables con los id del layout
        cerrarSesion = findViewById(R.id.imageView6);
        atras = findViewById(R.id.atras);
        usuario = findViewById(R.id.textView4);
        nombre = findViewById(R.id.editNombre);
        apellidos = findViewById(R.id.editApellidos);
        correo = findViewById(R.id.editCorreo);
        telefono = findViewById(R.id.editTelefono);
        aplicarCambios = findViewById(R.id.cambios);
        contrasenya = findViewById(R.id.editContrasenya);
        contrasenya.setText(myPreferences.getString("contrasenya2", "unknown"));

        //Botón para llamar a la función pulsarCerrarSesion()
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarCerrarSesion(view);
            }
        });

        //Botón que te lleva a la pestaña MainActivity
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarPerfil.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Escribo en los respectivos EditText los valores que he guardado anteriormente en la memoria interna sobre los datos del usuario al loguear
        usuario.setText(myPreferences.getString("nombre", "unknown") + " " + myPreferences.getString("apellidos", "unknown"));
        nombre.setText(myPreferences.getString("nombre", "unknown"));
        apellidos.setText(myPreferences.getString("apellidos", "unknown"));
        correo.setText(myPreferences.getString("correo", "unknown"));
        int numero = myPreferences.getInt("telefono", 0);
        telefono.setText(String.valueOf(numero));

        //Botón para llamar a la función pulsarAplicarCambios()
        aplicarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {pulsarAplicarCambios(view);}
        });

    }

    // .................................................................
    // view: View
    // -->
    // pulsarAplicarCambios() <--
    //
    // Esta función crea un objeto JSON con los datos que obtiene de los imputs y de la memoria interna de la sesión, después hace un post al servidor node para actualizar los datos del usuario y por último llama a la función cerrarSesion
    // .................................................................

    private void pulsarAplicarCambios(View view){
        //Envíar datos POST
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID_user", myPreferences.getInt("ID_user", 0));
            jsonObject.put("correo", correo.getText().toString());
            jsonObject.put("telefono", Integer.parseInt(telefono.getText().toString()));
            jsonObject.put("nombre", nombre.getText().toString());
            jsonObject.put("apellidos", apellidos.getText().toString());
            jsonObject.put("contrasenya", contrasenya.getText().toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://" + ip + ":8080/actualizar")//Recordar cambiar ip cada vez que cambies de red
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //pulsarCerrarSesion();
                        //Se detiene el timer para subir medidas
                        new MainActivity().pararTimer();
                        new MainActivity().detenerBusquedaDispositivosBTLE();
                    }

                    @Override
                    public void onError(ANError error) {
                        //Log.d("hola", "onError: " + error);
                        pulsarCerrarSesion2();
                        new MainActivity().pararTimer();
                        new MainActivity().detenerBusquedaDispositivosBTLE();
                    }
                });


    }

    // .................................................................
    // view: View
    // -->
    // pulsarCerrarSesion() <--
    //
    // Esta función modifica los datos del usuario que están en la memoria interna, vaciando los datos y poniendo el valor de sesion como false. Te devuelve al login
    // .................................................................

    private void pulsarCerrarSesion(View view){

        //Al cerrar la sesión modifico el estado de la sesión
        myEditor.putBoolean("sesion", false);
        myEditor.putString("correo", null);
        myEditor.putString("contrasenya", null);
        myEditor.putInt("telefono", 0);
        myEditor.putString("nombre", null);
        myEditor.putString("apellidos", null);
        myEditor.putString("estado", null);
        myEditor.putInt("id_user", 0);

        myEditor.putInt("ID_placa", 0);
        myEditor.putString("UUID_placa", null);


        sesion = myPreferences.getBoolean("sesion", false);
        myEditor.commit();

        //Log.d("test", "onCreate: " + sesion);


        Intent intent = new Intent(EditarPerfil.this, Login.class);
        startActivity(intent);

        //Se detiene el timer para subir medidas
        new MainActivity().pararTimer();
        new MainActivity().detenerBusquedaDispositivosBTLE();

    }

    // .................................................................
    //
    // pulsarCerrarSesion2() <--
    //
    // Esta función modifica los datos del usuario que están en la memoria interna, vaciando los datos y poniendo el valor de sesion como false. Te devuelve al login
    // .................................................................

    private void pulsarCerrarSesion2(){
        //Al cerrar la sesión modifico el estado de la sesión
        myEditor.putBoolean("sesion", false);
        myEditor.putString("correo", null);
        myEditor.putString("contrasenya", null);
        myEditor.putInt("telefono", 0);
        myEditor.putString("nombre", null);
        myEditor.putString("apellidos", null);
        myEditor.putString("estado", null);
        myEditor.putInt("id_user", 0);
        sesion = myPreferences.getBoolean("sesion", false);
        //Log.d("test", "onCreate: " + sesion);

        myEditor.putInt("ID_placa", 0);
        myEditor.putString("UUID_placa", null);
        myEditor.commit();

        new MainActivity().pararTimer();
        new MainActivity().detenerBusquedaDispositivosBTLE();

        Intent intent = new Intent(EditarPerfil.this, Login.class);
        startActivity(intent);
    }

}
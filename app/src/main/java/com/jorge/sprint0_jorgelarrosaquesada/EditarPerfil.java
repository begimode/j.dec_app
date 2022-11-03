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
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditarPerfil extends AppCompatActivity {

    String ip = "192.168.0.14";

    ImageView atras;
    TextView usuario;
    EditText nombre;
    EditText apellidos;
    EditText correo;
    EditText telefono;
    ImageView cerrarSesion;
    Button aplicarCambios;

    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;

    Boolean sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        AndroidNetworking.initialize(getApplicationContext());

        myPreferences = PreferenceManager.getDefaultSharedPreferences(EditarPerfil.this);
        myEditor = myPreferences.edit();

        sesion = myPreferences.getBoolean("sesion", false);

        if(sesion == false){
            Intent intent = new Intent(EditarPerfil.this, Login.class);
            startActivity(intent);
        }

         /*
        Log.d("datos", ": login " + myPreferences.getString("correo", "unknown"));
        Log.d("datos", ": login " + myPreferences.getString("contrasenya", "unknown"));
        Log.d("datos", ": login " + myPreferences.getInt("telefono", 0));
        Log.d("datos", ": login " + myPreferences.getString("nombre", "unknown"));
        Log.d("datos", ": login " + myPreferences.getString("apellidos", "unknown"));
        Log.d("datos", ": login " + myPreferences.getString("estado", "unknown"));
        Log.d("datos", "Estado de la sesion: " + sesion);
        */

        cerrarSesion = findViewById(R.id.imageView6);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarCerrarSesion(view);
            }
        });

        atras = findViewById(R.id.imageView5);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarPerfil.this, MainActivity.class);
                startActivity(intent);
            }
        });

        usuario = findViewById(R.id.textView4);
        usuario.setText(myPreferences.getString("nombre", "unknown") + " " + myPreferences.getString("apellidos", "unknown"));

        nombre = findViewById(R.id.editNombre);
        nombre.setText(myPreferences.getString("nombre", "unknown"));

        apellidos = findViewById(R.id.editApellidos);
        apellidos.setText(myPreferences.getString("apellidos", "unknown"));

        correo = findViewById(R.id.editCorreo);
        correo.setText(myPreferences.getString("correo", "unknown"));

        telefono = findViewById(R.id.editTelefono);
        int numero = myPreferences.getInt("telefono", 0);
        telefono.setText(String.valueOf(numero));

        aplicarCambios = findViewById(R.id.cambios);
        aplicarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarAplicarCambios(view);
            }
        });

    }

    private void pulsarAplicarCambios(View view){
        //Envíar datos POST
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("correoActual", myPreferences.getString("correo", "unknown"));
            jsonObject.put("correo", correo.getText().toString());
            jsonObject.put("telefono", Integer.parseInt(telefono.getText().toString()));
            jsonObject.put("nombre", nombre.getText().toString());
            jsonObject.put("apellidos", apellidos.getText().toString());

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
                    }

                    @Override
                    public void onError(ANError error) {
                        //Log.d("hola", "onError: " + error);
                        pulsarCerrarSesion2();
                    }
                });


    }

    private void pulsarCerrarSesion(View view){
        //Al cerrar la sesión modifico el estado de la sesión
        myEditor.putBoolean("sesion", false);
        myEditor.putString("correo", null);
        myEditor.putString("contrasenya", null);
        myEditor.putInt("telefono", 0);
        myEditor.putString("nombre", null);
        myEditor.putString("apellidos", null);
        myEditor.putString("estado", null);
        myEditor.commit();
        sesion = myPreferences.getBoolean("sesion", false);
        //Log.d("test", "onCreate: " + sesion);

        Intent intent = new Intent(EditarPerfil.this, Login.class);
        startActivity(intent);
    }

    private void pulsarCerrarSesion2(){
        //Al cerrar la sesión modifico el estado de la sesión
        myEditor.putBoolean("sesion", false);
        myEditor.putString("correo", null);
        myEditor.putString("contrasenya", null);
        myEditor.putInt("telefono", 0);
        myEditor.putString("nombre", null);
        myEditor.putString("apellidos", null);
        myEditor.putString("estado", null);
        myEditor.commit();
        sesion = myPreferences.getBoolean("sesion", false);
        //Log.d("test", "onCreate: " + sesion);

        Intent intent = new Intent(EditarPerfil.this, Login.class);
        startActivity(intent);
    }
}
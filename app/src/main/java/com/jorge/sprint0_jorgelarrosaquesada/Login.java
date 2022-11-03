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
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Login extends AppCompatActivity {

    String ip = "192.168.0.14";

    //Sirve para guardar datos permanentes
    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;
    boolean sesion;

    EditText correo;
    EditText contrasenya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Guardo los datos permanentes
        myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
        myEditor = myPreferences.edit();
        sesion = myPreferences.getBoolean("sesion", false);
        /*
        Log.d("datos", ": login " + myPreferences.getString("correo", "unknown"));
        Log.d("datos", ": login " + myPreferences.getString("contrasenya", "unknown"));
        Log.d("datos", ": login " + myPreferences.getInt("telefono", 0));
        Log.d("datos", ": login " + myPreferences.getString("nombre", "unknown"));
        Log.d("datos", ": login " + myPreferences.getString("apellidos", "unknown"));
        Log.d("datos", ": login " + myPreferences.getString("estado", "unknown"));
        Log.d("datos", "Estado de la sesion: " + sesion);
        */

        //Compruebo si la sesion este iniciada
        if(sesion){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

        AndroidNetworking.initialize(getApplicationContext());

        correo = findViewById(R.id.editTextCorreo);
        contrasenya = findViewById(R.id.editTextPass);

        Button login = findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarLogin(view);
            }
        });
        TextView registrar = findViewById(R.id.textView4);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarRegistrar(view);
            }
        });
    }

    private void pulsarLogin(View view){

        AndroidNetworking.get("http://" + ip + ":8080/usuario/" + correo.getText().toString())
                .addPathParameter("userId", "1")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Usuario.class, new ParsedRequestListener<Usuario>() {
                    @Override
                    public void onResponse(Usuario user) {

                        //Guardo los valores de la sesion
                        myEditor.putString("correo", user.getCorreo());
                        myEditor.putString("contrasenya", user.getContrasenya());
                        myEditor.putInt("telefono", user.getTelefono());
                        myEditor.putString("nombre", user.getNombre());
                        myEditor.putString("apellidos", user.getApellidos());
                        myEditor.putString("estado", user.getEstado());

                        Log.d("pepe", "correo : " + user.getCorreo());
                        Log.d("pepe", "contrasenya : " + user.getContrasenya());
                        Log.d("pepe", "telefono : " + user.getTelefono());
                        Log.d("pepe", "nombre : " + user.getNombre());
                        Log.d("pepe", "apellidos : " + user.getApellidos());
                        Log.d("pepe", "estado : " + user.getEstado());

                        AndroidNetworking.get("http://" + ip + ":8080/desencriptar3?hash="+user.getContrasenya()+"&cont="+contrasenya.getText().toString())
                                .addPathParameter("userId", "1")
                                .setTag(this)
                                .setPriority(Priority.LOW)
                                .build()
                                .getAsObject(Verdad.class, new ParsedRequestListener<Verdad>() {
                                    @Override
                                    public void onResponse(Verdad verdad) {
                                        //Log.d("pepe", "verdad : " + verdad.getEstado());
                                        if(verdad.getEstado()){

                                            //Si todo es correcto guardo la sesion

                                            myEditor.putBoolean("sesion", verdad.getEstado());
                                            myEditor.commit();
                                            sesion = myPreferences.getBoolean("sesion", false);
                                            //Log.d("test", "onCreate: " + sesion);

                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(getApplicationContext(),"Intentelo de nuevo",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    @Override
                                    public void onError(ANError anError) {
                                        Log.d("pepe", "error : " + anError);
                                    }
                                });
                            //
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("pepe", "error : " + anError);
                    }
                });


    }

    private void pulsarRegistrar(View view){
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }
}
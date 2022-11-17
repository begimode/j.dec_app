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
import org.w3c.dom.Text;

import java.util.List;

//----------------------------------------------------
// Archivo: Login.java
// J.Dec
//----------------------------------------------------

public class Login extends AppCompatActivity {

    //Se declaran las variables
    String ip = "192.168.1.98";


    //Sirve para guardar datos permanentes
    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;
    boolean sesion;
    EditText correo;
    EditText contrasenya;
    TextView olvidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Sirve para guardar valores de en la memoria interna del la app
        myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
        myEditor = myPreferences.edit();

        //Obtengo de la memoria interna el valor de la sesion
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

        //Compruebo si la sesion este iniciada y si es así pasa al MainActivity
        if(sesion){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

        //Librería encargada ser cocentarse con el Servidor
        AndroidNetworking.initialize(getApplicationContext());

        //Relaciono las variables con los id del layout
        correo = findViewById(R.id.editTextCorreo);
        contrasenya = findViewById(R.id.editTextPass);
        Button login = findViewById(R.id.button_login);
        TextView registrar = findViewById(R.id.textView4);
        olvidar= findViewById(R.id.olvidar);

        //Botón para llamar a la función pulsarLogin()
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarLogin(view);
            }
        });

        //Botón para llamar a la función pulsarRegistrar()
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarRegistrar(view);
            }
        });

        // Para abrir la pestaña de he olvidado mi contraseña

        olvidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RecuperarContrasenya.class);
                startActivity(intent);
            }
        });
    }

    // .................................................................
    // view: View
    // -->
    // pulsarLogin() <--
    //
    // Esta función hace un get al servidor pasandole el correo para comprobar que exista el usuario, si existe guarda los datos del usuario en la memoria interna, hace otro get para comprobar las contraseñas y si es correcto guarda una sesión y pasa a la pestaña de MainActivity
    // .................................................................

    private void pulsarLogin(View view){

        AndroidNetworking.get("http://" + ip + ":8080/usuario/" + correo.getText().toString())
                .addPathParameter("userId", "1")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Usuario.class, new ParsedRequestListener<Usuario>() {
                    @Override
                    public void onResponse(Usuario user) {

                        String id = Integer.toString(user.getID_user());
                        int id_ = Integer.parseInt(id);
                        //Guardo los valores de la sesion
                        myEditor.putString("correo", user.getCorreo());
                        myEditor.putString("contrasenya", user.getContrasenya());
                        myEditor.putInt("telefono", user.getTelefono());
                        myEditor.putString("nombre", user.getNombre());
                        myEditor.putString("apellidos", user.getApellidos());
                        myEditor.putString("estado", user.getEstado());
                        myEditor.putInt("ID_user",  id_);


                        Log.d("pepe", "correo : " + user.getCorreo());
                        Log.d("pepe", "contrasenya : " + user.getContrasenya());
                        Log.d("pepe", "telefono : " + user.getTelefono());
                        Log.d("pepe", "nombre : " + user.getNombre());
                        Log.d("pepe", "apellidos : " + user.getApellidos());
                        Log.d("pepe", "estado : " + user.getEstado());
                        Log.d("pepe", "ID_user : " +  myPreferences.getInt("ID_user", 0));

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
                                            myEditor.putString("contrasenya2", contrasenya.getText().toString());
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

    // .................................................................
    // view: View
    // -->
    // pulsarRegistrar() <--
    //
    // Esta función te lleva a la pestaña de Registrar.
    // .................................................................

    private void pulsarRegistrar(View view){
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }
}
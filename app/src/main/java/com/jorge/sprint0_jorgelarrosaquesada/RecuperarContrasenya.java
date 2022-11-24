package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class RecuperarContrasenya extends AppCompatActivity {

    String ip = "192.168.96.243";
    EditText correo;
    Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasenya);

        //Relaciono las variables con los id del layout
        ImageView atras = findViewById(R.id.atras);
        correo = findViewById(R.id.correo_recuperar);
        enviar = findViewById(R.id.enviar_3);

        //Botón que te lleva a la pestaña de Registrar.
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecuperarContrasenya.this, Login.class);
                startActivity(intent);
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperarContrasenya(view);
            }
        });

        //Librería encargada ser cocentarse con el Servidor
        AndroidNetworking.initialize(getApplicationContext());

    }

    private String randomContrasenya(int num){
        String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(num);
        for(int i=0;i<num;++i) {
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return sb.toString();
    }

    private void recuperarContrasenya(View view)
    {
        Log.d("CAMBIAR CONTRASEÑA", "BOTON PULSADO");
        String password = randomContrasenya(6);

        //Envíar datos POST
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("correo", correo.getText().toString());
            jsonObject.put("text", password);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://" + ip + ":8080/cambiarContrasenya")//Recordar cambiar ip cada vez que cambies de red
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }

                    @Override
                    public void onError(ANError error) {
                        //Log.d("hola", "onError: " + error);
                    }
                });

    }
}
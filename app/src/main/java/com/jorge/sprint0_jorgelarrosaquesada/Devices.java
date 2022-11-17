package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//----------------------------------------------------
// Archivo: Devices.java
// J.Dec
//----------------------------------------------------

public class Devices extends AppCompatActivity {

    //Se declaran las variables
    ImageView atras;
    String ip = "192.168.1.98";


    //Datos usuario
    public int ID_user;
    public int ID_placa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);


        //Obtenemos del Intent los datos que se han pasado de la pestaña MainActivity
        ID_user = getIntent().getExtras().getInt("ID_user");
        ID_placa = getIntent().getExtras().getInt("ID_placa");


        //Relaciono las variables con los id del layout
        TextView ultimaMedida = (TextView) findViewById(R.id.textView17);

        atras = (ImageView)findViewById(R.id.atras);


        //Botón que te lleva a la pestaña de Login.
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Devices.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Log.d("ID USUARIO DEVICES", String.valueOf(ID_user));

        getDeviceInfo();
    }


    public void getDeviceInfo(){

        AndroidNetworking.get("http://" + ip + ":8080/buscarPlacaConId/" + ID_user)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String uuid = response.getString("uuid");
                            int id = response.getInt("ID_placa");

                            Log.d("ID DEL DISPOSITIVO", String.valueOf(id));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });


        AndroidNetworking.get("http://" + ip + ":8080/buscarMedicionConID/" + ID_placa)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Medida.class, new ParsedRequestListener<List<Medida>>() {
                    @Override
                    public void onResponse(List<Medida> medidas) {
                        List<Integer> listaValores = new ArrayList<Integer>();
                        List<String> listaFechas = new ArrayList<String>();
                        List<Coordenada> listaUbicacion = new ArrayList<Coordenada>();
                        // do anything with response
                        for (Medida medida : medidas) {
                            listaValores.add((int) medida.getValor());
                            listaFechas.add(medida.getTiempo());
                            listaUbicacion.add(medida.getCoordenada());


                            Log.d("LISTA MEDIDADAS", String.valueOf(listaValores));
                            Integer maxMedida = Collections.max(listaValores);
                            Integer minMedida = Collections.min(listaValores);

                            Log.d("VALOR MAX LSITA VALORES", String.valueOf(maxMedida));
                            Log.d("VALOR MIN LSITA VALORES", String.valueOf(minMedida));


                            Log.d("LISTA FECHAS", String.valueOf(listaFechas));
                            Log.d("LISTA UBICACIONES", String.valueOf(listaUbicacion));

                        }

                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                    }
                });
    }

}

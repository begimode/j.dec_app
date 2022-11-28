package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//----------------------------------------------------
// Archivo: Devices.java
// J.Dec
//----------------------------------------------------

public class Devices extends AppCompatActivity {

    //Se declaran las variables
    String ip = "192.168.1.103";

    //Textviews
    TextView nombreDispositivo;
    TextView LastMedicion;
    TextView MinMedicion;
    TextView MaxMedicion;
    TextView LastFecha;

    //ImageViews
    ImageView atras;
    ImageView refresh;
    ImageView deleteButton;

    //Datos usuario
    public int ID_user;
    public int ID_placa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);


        //Obtenemos del Intent los datos que se han pasado de la pestaña MainActivity
        ID_user = getIntent().getExtras().getInt("ID_user");
        Log.d("ID USUARIO", String.valueOf(ID_user));
        ID_placa = getIntent().getExtras().getInt("ID_placa");


        //TextViews
        nombreDispositivo = findViewById(R.id.nombreDispositivo);
        LastMedicion = findViewById(R.id.LastMedicion);
        MinMedicion = findViewById(R.id.MinMedicion);
        MaxMedicion = findViewById(R.id.MaxMedicion);
        //LastFecha = findViewById(R.id.LastFecha);

        //ImageViews
        atras = (ImageView)findViewById(R.id.atras);
        refresh = (ImageView)findViewById(R.id.refreshButton);
        deleteButton = (ImageView) findViewById(R.id.deleteDevice);



        //Botón que te lleva a la pestaña de Login.
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Devices.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Botón que refresca la información
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceInfo();
                Log.d("REFRESH BUTTON", "Se ha pulsado el boton de refrescar");
            }
        });

        //Botón que borra la placa
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // deleteDevice();
            }
        });

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
                        List<Integer> listaFechas = new ArrayList<Integer>();
                        List<Coordenada> listaUbicacion = new ArrayList<Coordenada>();

                        // do anything with response
                        int maxMedida = 0;
                        int minMedida = 0;
                        int lastMedida = 0;
                        int lastFecha = 0;

                        for (Medida medida : medidas) {
                            listaValores.add((int) medida.getValor());
                            listaFechas.add(medida.getTiempo());
                            listaUbicacion.add(medida.getCoordenada());

                            Log.d("LISTA FECHAS", String.valueOf(listaFechas));


                            maxMedida = Collections.max(listaValores);
                            minMedida = Collections.min(listaValores);
                            lastMedida = listaValores.get(listaValores.size()-1);
                            lastFecha = listaFechas.get(listaFechas.size()-1);

                        }
                        mostrarMedidas(maxMedida, minMedida, lastMedida, lastFecha);

                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                    }
                });
    }

    private void mostrarMedidas(Integer maxMedida, Integer minMedida, Integer lastMedida, Integer lastFecha) {
        MaxMedicion.setText(maxMedida.toString());
        MinMedicion.setText(minMedida.toString());
        LastMedicion.setText(lastMedida.toString());
    }

    private void deleteDevice() {
        AndroidNetworking.delete("")
                .addQueryParameter("limit", "3")
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
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
    }


}

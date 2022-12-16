package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.jorge.sprint0_jorgelarrosaquesada.contaminantes.azufre;
import com.jorge.sprint0_jorgelarrosaquesada.contaminantes.carbono2;
import com.jorge.sprint0_jorgelarrosaquesada.contaminantes.monoxido;
import com.jorge.sprint0_jorgelarrosaquesada.contaminantes.nitrogeno2;
import com.jorge.sprint0_jorgelarrosaquesada.contaminantes.ozono;

import java.util.ArrayList;
import java.util.List;

//----------------------------------------------------
// Archivo: InformacionAdicional.java
// J.Dec
//----------------------------------------------------

public class InformacionAdicional extends AppCompatActivity {

    //Se declran las variables
    String ip = new LogicaFake().getIp();

    //Datos usuario
    public int ID_placa;

    ImageView perfil;
    ImageView information;
    ImageView buttonScan;
    ImageView devices;
    CardView NO2;
    CardView CO2;
    CardView CO;
    CardView SO2;
    CardView O3;
    CardView calidadCuadro;

    TextView calidadValor;
    ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacionadicional);

        ID_placa = getIntent().getExtras().getInt("ID_placa");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Relaciono las variables con los id del layout
        perfil = findViewById(R.id.perfilImage);
        information = findViewById(R.id.imageInformation);
        buttonScan = findViewById(R.id.imageView8);
        devices = findViewById(R.id.profileImage);
        NO2 = findViewById(R.id.no2_card);
        CO2 = findViewById(R.id.co2_card);
        CO = findViewById(R.id.co_card);
        SO2 = findViewById(R.id.so2_card);
        O3 = findViewById(R.id.o3_card);

        //calidadCuadro = findViewById(R.id.calidadCard);
        calidadValor = findViewById(R.id.calidadValor);
        refresh = (ImageView)findViewById(R.id.actualizarBoton);


        //Botón que te lleva a la pestaña de EditPerfil.
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformacionAdicional.this, EditarPerfil.class);
                startActivity(intent);
            }
        });

        //Botón que te lleva a la pestaña de InformacionAdicional.
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformacionAdicional.this, InformacionAdicional.class);
                startActivity(intent);
            }
        });


        //Scan
        //Botón para el escaneao del qr, Botón para llamar a la función botonLeerCodigoQR()
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Botón que te lleva a la pestaña de Devices.
        devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformacionAdicional.this, Devices.class);
                startActivity(intent);
            }
        });

        NO2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformacionAdicional.this, nitrogeno2.class);
                startActivity(intent);
            }
        });

        SO2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformacionAdicional.this, azufre.class);
                startActivity(intent);
            }
        });

        O3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformacionAdicional.this, ozono.class);
                startActivity(intent);
            }
        });

        CO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformacionAdicional.this, monoxido.class);
                startActivity(intent);
            }
        });

        CO2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformacionAdicional.this, carbono2.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarMedidasHoy();
            }
        });

        buscarMedidasHoy();
    }


    public void buscarMedidasHoy() {
        Log.d("MEDIDA LOG", "HA ENTRADO");
        AndroidNetworking.get("http://" + ip + ":8080/buscarMedicionesHoy/" + ID_placa)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Medida.class, new ParsedRequestListener<List<Medida>>() {
                    @Override
                    public void onResponse(List<Medida> medidas) {

                        List<Integer> listaValores = new ArrayList<Integer>();
                        float sumatorio = 0;

                        for (Medida medida : medidas) {
                            listaValores.add((int) medida.getValor());
                            Log.d("MEDIDA LOG", String.valueOf(medida.getValor()));
                            sumatorio = sumatorio + medida.getValor();
                        }
                        float calidad = sumatorio/30;
                        mostrarCalidadAire(calidad);

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("MEDIDA LOG", String.valueOf(anError));
                    }
                });
    }

    private void mostrarCalidadAire(float calidad) {
        Log.d("CALIDAD AIRE", "Calidad aire" + String.valueOf(calidad));
        String calidadString = String.valueOf(calidad);

        calidadValor.setText(calidadString);

        if(calidad < 20){
            //calidadCuadro.setCardBackgroundColor(255);
            calidadValor.setText(calidadString);
        } else if(calidad > 20 && calidad < 40){
            calidadValor.setText(calidadString);
        } else if(calidad > 40){
            calidadValor.setText(calidadString);

        }
    }
}
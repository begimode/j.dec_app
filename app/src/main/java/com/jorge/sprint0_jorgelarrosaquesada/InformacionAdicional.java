package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jorge.sprint0_jorgelarrosaquesada.contaminantes.azufre;
import com.jorge.sprint0_jorgelarrosaquesada.contaminantes.carbono2;
import com.jorge.sprint0_jorgelarrosaquesada.contaminantes.monoxido;
import com.jorge.sprint0_jorgelarrosaquesada.contaminantes.nitrogeno2;
import com.jorge.sprint0_jorgelarrosaquesada.contaminantes.ozono;

//----------------------------------------------------
// Archivo: InformacionAdicional.java
// J.Dec
//----------------------------------------------------

public class InformacionAdicional extends AppCompatActivity {

    //Se declran las variables
    ImageView perfil;
    ImageView information;
    ImageView buttonScan;
    ImageView devices;
    CardView NO2;
    CardView CO2;
    CardView CO;
    CardView SO2;
    CardView O3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacionadicional);

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

    }
}
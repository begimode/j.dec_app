package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//----------------------------------------------------
// Archivo: Devices.java
// J.Dec
//----------------------------------------------------

public class Devices extends AppCompatActivity {

    //Se declaran las variables
    TextView ultimaMedida;
    ImageView atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        //Obtenemos del Intent los datos que se han pasado de la pestaña MainActivity
        int valor = getIntent().getExtras().getInt("medicion");

        //Relaciono las variables con los id del layout
        ultimaMedida = (TextView)findViewById(R.id.textView17);
        atras = (ImageView)findViewById(R.id.atras);

        //Se añade el texto con los datos obtenidos antes
        ultimaMedida.setText("Última medida: " + valor);

        //Botón que te lleva a la pestaña de Login.
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Devices.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
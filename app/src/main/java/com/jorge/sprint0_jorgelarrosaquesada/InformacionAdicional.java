package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

//----------------------------------------------------
// Archivo: InformacionAdicional.java
// J.Dec
//----------------------------------------------------

public class InformacionAdicional extends AppCompatActivity {

    //Se declran las variables
    ImageView atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacionadicional);

        //Relaciono las variables con los id del layout
        atras = findViewById(R.id.imageView);
        //Botón que te lleva a la pestaña MainActivity
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformacionAdicional.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

//----------------------------------------------------
// Archivo: Politica_Privacidad.java
// J.Dec
//----------------------------------------------------

public class Politica_Privacidad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_de_datos);

        //Relaciono las variables con los id del layout
        ImageView atras = findViewById(R.id.imageView);

        //Botón que te lleva a la pestaña de Registrar.
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Politica_Privacidad.this, Registrar.class);
                startActivity(intent);
            }
        });
    }
}
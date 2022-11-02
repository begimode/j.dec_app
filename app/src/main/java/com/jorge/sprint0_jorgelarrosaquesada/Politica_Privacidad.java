package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Politica_Privacidad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_de_datos);

        ImageView atras = findViewById(R.id.imageView);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarAtras(view);
            }
        });
    }

    private void pulsarAtras(View view){
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }
}
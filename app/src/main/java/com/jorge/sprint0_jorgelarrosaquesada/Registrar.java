package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Registrar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        TextView leer = findViewById(R.id.leer);
        leer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarTerminos(view);
            }
        });
    }

    private void pulsarTerminos(View view){
        Intent intent = new Intent(this, Politica_Privacidad.class);
        startActivity(intent);
    }
}
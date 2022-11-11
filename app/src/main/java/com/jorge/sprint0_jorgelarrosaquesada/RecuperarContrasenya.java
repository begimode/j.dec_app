package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RecuperarContrasenya extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasenya);

        //Relaciono las variables con los id del layout
        ImageView atras = findViewById(R.id.atras);

        //Botón que te lleva a la pestaña de Registrar.
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecuperarContrasenya.this, Login.class);
                startActivity(intent);
            }
        });

    }
}
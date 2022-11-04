package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//----------------------------------------------------
// Archivo: InitTutorial.java
// J.Dec
//----------------------------------------------------

public class InitTutorial extends AppCompatActivity {

    //Se declran las variables
    Button empezar;
    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;
    boolean sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        //Relaciono las variables con los id del layout
        empezar = findViewById(R.id.button);

        //Sirve para guardar valores de en la memoria interna del la app
        myPreferences = PreferenceManager.getDefaultSharedPreferences(InitTutorial.this);
        myEditor = myPreferences.edit();

        //Obtengo de la memoria interna el valor de la sesion
        sesion = myPreferences.getBoolean("sesion", false);

        //Compruebo si la sesion este iniciada y si es así paso al login
        if(sesion){
            Intent intent = new Intent(InitTutorial.this, Login.class);
            startActivity(intent);
        }

        //Botón que te lleva a la pestaña Login
        empezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InitTutorial.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
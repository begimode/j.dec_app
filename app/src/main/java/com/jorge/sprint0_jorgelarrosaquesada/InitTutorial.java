package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class InitTutorial extends AppCompatActivity {

    Button empezar;
    //Sirve para guardar datos permanentes
    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;
    boolean sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        empezar = findViewById(R.id.button);

        myPreferences = PreferenceManager.getDefaultSharedPreferences(InitTutorial.this);
        myEditor = myPreferences.edit();
        sesion = myPreferences.getBoolean("sesion", false);

        //Compruebo si la sesion este iniciada
        if(sesion){
            Intent intent = new Intent(InitTutorial.this, Login.class);
            startActivity(intent);
        }

        empezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarComenzar(view);
            }
        });
    }

    private void pulsarComenzar(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
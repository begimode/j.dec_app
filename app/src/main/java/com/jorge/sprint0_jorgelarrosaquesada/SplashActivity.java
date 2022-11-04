package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

//----------------------------------------------------
// Archivo: SplashActivity.java
// J.Dec
//----------------------------------------------------

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Se agregan las animaciones
        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        //Relaciono las variables con los id del layout
        TextView text1 = this.findViewById(R.id.textoEntrada);
        ImageView image1 = this.findViewById(R.id.imageView3);

        //Inicio las animaciones
        image1.setAnimation(animacion1);
        text1.setAnimation(animacion2);

        //Sirve para pasar a la pestaña InitTutorial al pasar 4 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, InitTutorial.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}
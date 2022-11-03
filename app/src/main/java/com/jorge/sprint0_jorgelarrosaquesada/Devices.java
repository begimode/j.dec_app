package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Devices extends AppCompatActivity {

    TextView ultimaMedida;
    ImageView atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        int valor = getIntent().getExtras().getInt("medicion");

        ultimaMedida = (TextView)findViewById(R.id.textView17);
        ultimaMedida.setText("Última medida: " + valor);

        atras = (ImageView)findViewById(R.id.imageView5);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Devices.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
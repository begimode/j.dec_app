package com.jorge.sprint0_jorgelarrosaquesada.contaminantes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.jorge.sprint0_jorgelarrosaquesada.InformacionAdicional;
import com.jorge.sprint0_jorgelarrosaquesada.R;

public class ozono extends AppCompatActivity {

    ImageView atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ozono);
        atras = findViewById(R.id.No2_atras);

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ozono.this, InformacionAdicional.class);
                startActivity(intent);
            }
        });
    }
}
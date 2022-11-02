package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarLogin(view);
            }
        });
        TextView registrar = findViewById(R.id.textView4);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsarRegistrar(view);
            }
        });
    }

    private void pulsarLogin(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void pulsarRegistrar(View view){
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }
}
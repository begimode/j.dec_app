package com.jorge.sprint0_jorgelarrosaquesada;

//----------------------------------------------------
// Archivo: Coordenada.java
// J.Dec
// Descripción: Esta clase sirve para crear objetos de tipo Coordenada que se utilizan en la clase Medida. Guardan las coordenadas que se obtienen en la APP
//----------------------------------------------------

public class Coordenada {

    //Atributos
    private float x;
    private float y;

    //Constructor que crea un objeto coordenada pasando los datos
    public Coordenada(float x, float y) {
        this.x = x;
        this.y = y;
    }

    //Constructor que crea un objeto coordenada pero vacío
    public Coordenada() {
    }

    //Getter Setter
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

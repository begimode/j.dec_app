package com.jorge.sprint0_jorgelarrosaquesada;

//----------------------------------------------------
// Archivo: Coordenada.java
// Jorge Larrosa Quesada
// Sprint 0
//----------------------------------------------------

public class Coordenada {

    //Atributos
    private float x;
    private float y;

    //Constructor
    public Coordenada(float x, float y) {
        this.x = x;
        this.y = y;
    }

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

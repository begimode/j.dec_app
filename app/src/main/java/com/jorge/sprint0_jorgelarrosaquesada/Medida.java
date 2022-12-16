package com.jorge.sprint0_jorgelarrosaquesada;

import android.util.Log;

import java.util.Date;

//----------------------------------------------------
// Archivo: Medida.java
// J.Dec
// Descripción: Esta clase crea un objeto Medida la cual sirve para enviarlo al servidor
//----------------------------------------------------

public class Medida {

    //Atributos
    private float valor;
    private String tiempo;
    private String nombre_sensor;
    private Coordenada coordenada;


    private int ID_medida, ID_placa;
    private String tipo_medida;

    //Constructor que crea un objeto coordenada pasando los datos
    public Medida(float valor, String tiempo, String nombre_sensor, Coordenada coordenada) {
        this.valor = valor;
        this.tiempo = tiempo;
        this.nombre_sensor = nombre_sensor;
        this.coordenada = coordenada;
    }

    //Constructor que crea un objeto coordenada pero vacío
    public Medida() {
    }

    //Getter Setter
    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getNombre_sensor() {
        return nombre_sensor;
    }

    public void setNombre_sensor(String nombre_sensor) {
        this.nombre_sensor = nombre_sensor;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }



    public int getID_medida() {
        return ID_medida;
    }

    public void setID_medida(int ID_medida) {
        this.ID_medida = ID_medida;
    }

    public int getID_placa() {
        return ID_placa;
    }

    public void setID_placa(int ID_placa) {
        this.ID_placa = ID_placa;
    }

    public String getTipo_medida() {
        return tipo_medida;
    }

    public void setTipo_medida(String tipo_medida) {
        this.tipo_medida = tipo_medida;
    }
}

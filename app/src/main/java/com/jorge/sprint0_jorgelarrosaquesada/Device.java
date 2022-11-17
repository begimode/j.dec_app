package com.jorge.sprint0_jorgelarrosaquesada;

import java.util.UUID;

public class Device {
    private String fecha, uuid;
    private float ultima_medicion, max_medicion, min_medicion;
    private Coordenada ubicacion;


    //Getters and Setters
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getUltima_medicion() {
        return ultima_medicion;
    }

    public void setUltima_medicion(float ultima_medicion) {
        this.ultima_medicion = ultima_medicion;
    }

    public float getMax_medicion() {
        return max_medicion;
    }

    public void setMax_medicion(float max_medicion) {
        this.max_medicion = max_medicion;
    }

    public float getMin_medicion() {
        return min_medicion;
    }

    public void setMin_medicion(float min_medicion) {
        this.min_medicion = min_medicion;
    }

    public Coordenada getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Coordenada ubicacion) {
        this.ubicacion = ubicacion;
    }
}

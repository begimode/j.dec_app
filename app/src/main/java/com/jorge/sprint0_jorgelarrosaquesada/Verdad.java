package com.jorge.sprint0_jorgelarrosaquesada;

//----------------------------------------------------
// Archivo: Verdad.java
// J.Dec
// Descripción: Esta clase crea un objeto Verdad que se usa para obtener el boolean al comprobar las contraseñas
//----------------------------------------------------

public class Verdad {

    //Atributos
    private Boolean estado;

    //Constructor que crea un objeto coordenada pero vacío
    public Verdad() {
    }

    //Constructor que crea un objeto coordenada pasando los datos
    public Verdad(Boolean estado) {
        this.estado = estado;
    }

    //Getter Setter
    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    //ToString
    @Override
    public String toString() {
        return "Verdad{" +
                "estado=" + estado +
                '}';
    }
}

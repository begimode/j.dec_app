package com.jorge.sprint0_jorgelarrosaquesada;

public class Verdad {
    private Boolean estado;

    public Verdad() {
    }

    public Verdad(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Verdad{" +
                "estado=" + estado +
                '}';
    }
}

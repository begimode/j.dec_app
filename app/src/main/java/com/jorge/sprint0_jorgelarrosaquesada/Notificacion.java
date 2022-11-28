package com.jorge.sprint0_jorgelarrosaquesada;

public class Notificacion {


    String motivo;
    String mensaje;
    String fecha;
    int ID_user;
    int ID_placa;

    //Constructor que crea un objeto coordenada pero vacÃ­o
    public Notificacion() {
    }

    public Notificacion(String motivo, String mensaje, String fecha, int ID_user, int ID_placa) {
        this.motivo = motivo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.ID_user = ID_user;
        this.ID_placa = ID_placa;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getID_user() {
        return ID_user;
    }

    public void setID_user(int ID_user) {
        this.ID_user = ID_user;
    }

    public int getID_placa() {
        return ID_placa;
    }

    public void setID_placa(int ID_placa) {
        this.ID_placa = ID_placa;
    }
}
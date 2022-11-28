package com.jorge.sprint0_jorgelarrosaquesada;

public class Placa {
    //Atributos
    private int ID_placa;
    private int ID_user;
    private String uuid;
    private int estadoPlaca;

    public Placa() {
    }

    public Placa(int ID_placa, int ID_user, String uuid, int estadoPlaca) {
        this.ID_placa = ID_placa;
        this.ID_user = ID_user;
        this.uuid = uuid;
        this.estadoPlaca = estadoPlaca;
    }

    public int getID_placa() {
        return ID_placa;
    }

    public void setID_placa(int ID_placa) {
        this.ID_placa = ID_placa;
    }

    public int getID_user() {
        return ID_user;
    }

    public void setID_user(int ID_user) {
        this.ID_user = ID_user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getEstadoPlaca() {
        return estadoPlaca;
    }

    public void setEstadoPlaca(int estadoPlaca) {
        this.estadoPlaca = estadoPlaca;
    }

    @Override
    public String toString() {
        return "Placa{" +
                "ID_placa=" + ID_placa +
                ", ID_user=" + ID_user +
                ", uuid='" + uuid + '\'' +
                ", estadoPlaca=" + estadoPlaca +
                '}';
    }
}

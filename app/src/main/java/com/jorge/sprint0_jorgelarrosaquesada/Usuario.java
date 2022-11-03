package com.jorge.sprint0_jorgelarrosaquesada;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String correo;
    private String contrasenya;
    private int telefono;
    private String nombre;
    private String apellidos;
    private String estado;

    public Usuario() {
    }

    public Usuario(String correo, String contrasenya, int telefono, String nombre, String apellidos, String estado) {
        this.correo = correo;
        this.contrasenya = contrasenya;
        this.telefono = telefono;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.estado = estado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "correo='" + correo + '\'' +
                ", contrasenya='" + contrasenya + '\'' +
                ", telefono=" + telefono +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}

package com.jorge.sprint0_jorgelarrosaquesada;

import java.io.Serializable;

//----------------------------------------------------
// Archivo: Usuario.java
// J.Dec
// Descripción: Esta clase crea un objeto Usuario para guardar los datos al loguear y para registrar al usuario
//----------------------------------------------------

public class Usuario implements Serializable {

    //Atributos
    private String correo;
    private String contrasenya;
    private int telefono;
    private String nombre;
    private String apellidos;
    private String estado;

    //Constructor que crea un objeto coordenada pero vacío
    public Usuario() {
    }

    //Constructor que crea un objeto coordenada pasando los datos
    public Usuario(String correo, String contrasenya, int telefono, String nombre, String apellidos, String estado) {
        this.correo = correo;
        this.contrasenya = contrasenya;
        this.telefono = telefono;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.estado = estado;
    }

    //Getters Setters
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

    //ToString
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

package com.example.pawsecure.model;

public class Permisos {
    public String nombre;
    public String color;
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Permisos(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
    }
}

package com.Telcel;

public class Celular {
    private String nombre;
    private int capacidad;
    private double precio;

    public Celular(String nombre, int capacidad, double precio) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precio = precio;
    }

    public String getNombre() {
        return this.nombre;
    }


    public int getCapacidad() {
        return this.capacidad;
    }


    public double getPrecio() {
        return this.precio;
    }
}

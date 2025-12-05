package com.code.tienda_supertech.model;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private double precio;
    private int cantidad;

    public Producto() {
    }

    public Producto(int cantidad, double precio, String imagen, String descripcion, String nombre, int id) {
        this.cantidad = cantidad;
        this.precio = precio;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                '}';
    }
}

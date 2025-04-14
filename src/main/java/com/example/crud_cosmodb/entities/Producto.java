package com.example.crud_cosmodb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Productos")
public class Producto {

    @Id
    private String codigoBarras;

    private String nombre;
    private int precio;

    public Producto() {
    }

    public Producto(String codigoBarras, String nombre, int precio) {
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

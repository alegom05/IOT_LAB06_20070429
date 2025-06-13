package com.example.lab6;

public class Movimiento {
    public String id;
    public String titulo;
    public double monto;
    public String descripcion;
    public String fecha;

    public Movimiento() {}

    public Movimiento(String titulo, double monto, String descripcion, String fecha) {
        this.titulo = titulo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public void setId(String id) {

    }
}

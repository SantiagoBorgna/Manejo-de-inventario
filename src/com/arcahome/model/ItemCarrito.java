package com.arcahome.model;

public class ItemCarrito {
    private int idArticulo;
    private String nombre;
    private int cantidad;
    private double precioVenta;
    private double precioCompra; // Necesario para calcular ganancia real

    public ItemCarrito(int id, String nom, int cant, double pVenta, double pCompra) {
        this.idArticulo = id; this.nombre = nom; this.cantidad = cant;
        this.precioVenta = pVenta; this.precioCompra = pCompra;
    }

    public double getSubtotalVenta() { return cantidad * precioVenta; }
    public double getSubtotalCosto() { return cantidad * precioCompra; }
    public int getIdArticulo() { return idArticulo; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public double getPrecioCompra(){
        return precioCompra;
    }
}
package com.arcahome.model;

public class VentaResumen {
    private String cliente;
    private String articulos;
    private String medioPago;
    private double importe;
    private double ganancia;

    public VentaResumen(String cliente, String articulos, String medioPago, double importe, double ganancia) {
        this.cliente = cliente;
        this.articulos = articulos;
        this.medioPago = medioPago;
        this.importe = importe;
        this.ganancia = ganancia;
    }

    // Getters
    public String getCliente() { return cliente; }
    public String getArticulos() { return articulos; }
    public String getMedioPago() { return medioPago; }
    public double getImporte() { return importe; }
    public double getGanancia() { return ganancia; }
}
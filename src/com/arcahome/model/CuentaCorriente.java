package com.arcahome.model;

public class CuentaCorriente {
    private int id;
    private String cliente;
    private String descripcion;
    private String fecha;
    private double monto;
    private double saldoDeudor;
    private double ganancia;

    public CuentaCorriente() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public double getSaldoDeudor() { return saldoDeudor; }
    public void setSaldoDeudor(double saldoDeudor) { this.saldoDeudor = saldoDeudor; }
    public double getGanancia() { return ganancia; }
    public void setGanancia(double ganancia) { this.ganancia = ganancia; }
}
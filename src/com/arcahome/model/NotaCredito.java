package com.arcahome.model;

public class NotaCredito {
    private String cliente;
    private String fecha;
    private double monto;
    private String motivo;
    private String ciudad;

    public NotaCredito() {}

    public NotaCredito(String cliente, String fecha, double monto, String motivo, String ciudad) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.monto = monto;
        this.motivo = motivo;
        this.ciudad = ciudad;
    }

    // Getters y Setters
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
}
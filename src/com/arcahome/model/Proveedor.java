package com.arcahome.model;

public class Proveedor {
    private String nombre;
    private String firma;
    private String localidad;
    private String contacto;
    private double compraMinima;

    public Proveedor() {}

    public Proveedor(String nombre, String firma, String localidad, String contacto, double compraMinima) {
        this.nombre = nombre;
        this.firma = firma;
        this.localidad = localidad;
        this.contacto = contacto;
        this.compraMinima = compraMinima;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getFirma() { return firma; }
    public void setFirma(String firma) { this.firma = firma; }
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public double getCompraMinima() { return compraMinima; }
    public void setCompraMinima(double compraMinima) { this.compraMinima = compraMinima; }
}
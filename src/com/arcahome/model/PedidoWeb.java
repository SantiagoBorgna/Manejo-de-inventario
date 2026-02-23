package com.arcahome.model;

public class PedidoWeb {
    private int id;
    private String fecha;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
    // Dirección
    private String calle;
    private String numero;
    private String pisoDepto;
    private String ciudad;
    private String provincia;
    private String cp;
    // Datos venta
    private String metodoEnvio;
    private String costoEnvio;
    private double totalFinal;
    private String resumenArticulos;
    private String estado; // "Pendiente", "Entregado", etc.
    private String medioPago;

    private boolean despachado = false;

    // Constructor vacío
    public PedidoWeb() {}

    // Constructor completo
    public PedidoWeb(int id, String fecha, String nombre, String apellido, String dni, String telefono, String email, String calle, String numero, String pisoDepto, String ciudad, String provincia, String cp, String metodoEnvio, String costoEnvio, double totalFinal, String resumenArticulos, String estado, String medioPago) {
        this.id = id;
        this.fecha = fecha;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.calle = calle;
        this.numero = numero;
        this.pisoDepto = pisoDepto;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.cp = cp;
        this.metodoEnvio = metodoEnvio;
        this.costoEnvio = costoEnvio;
        this.totalFinal = totalFinal;
        this.resumenArticulos = resumenArticulos;
        this.estado = estado;
        this.medioPago = medioPago;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPisoDepto() {
        return pisoDepto;
    }

    public void setPisoDepto(String pisoDepto) {
        this.pisoDepto = pisoDepto;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getMetodoEnvio() {
        return metodoEnvio;
    }

    public void setMetodoEnvio(String metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    public String getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(String costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public double getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(double totalFinal) {
        this.totalFinal = totalFinal;
    }

    public String getResumenArticulos() {
        return resumenArticulos;
    }

    public void setResumenArticulos(String resumenArticulos) {
        this.resumenArticulos = resumenArticulos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public boolean isDespachado() {
        return despachado;
    }

    public void setDespachado(boolean despachado) {
        this.despachado = despachado;
    }

    public String getNombreCompleto() { return nombre + " " + apellido; }
    public String getDireccionCompleta() { return calle + " " + numero + " " + pisoDepto + ", " + ciudad + ", " + provincia + " (" + cp + ")"; }
    public String getContacto() { return telefono + " | " + email; }
}
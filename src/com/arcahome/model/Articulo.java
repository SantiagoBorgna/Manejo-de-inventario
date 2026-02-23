package com.arcahome.model;

public class Articulo {
    // Identificadores y básicos
    private int id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String proveedor;
    private String negocio = "Arca home";
    private String ciudad;

    private int stockOncativo; // cant1articulo
    private int stockOliva;    // cant2articulo
    private int stockDeposito3; // cant3articulo

    private double precioCompra;
    private double precioVenta;

    private double peso;
    private double alto;
    private double ancho;
    private double profundidad;

    private String imagen1;
    private String imagen2;
    private String imagen3;
    private String imagen4;

    public Articulo() {}

    // Constructor completo para inserciones rápidas
    public Articulo(String nombre, String descripcion, int stockOncativo, int stockOliva,
                    double precioCompra, double precioVenta, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stockOncativo = stockOncativo;
        this.stockOliva = stockOliva;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.categoria = categoria;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    public String getNegocio() { return negocio; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public int getStockOncativo() { return stockOncativo; }
    public void setStockOncativo(int stockOncativo) { this.stockOncativo = stockOncativo; }

    public int getStockOliva() { return stockOliva; }
    public void setStockOliva(int stockOliva) { this.stockOliva = stockOliva; }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getAlto() { return alto; }
    public void setAlto(double alto) { this.alto = alto; }

    public double getAncho() { return ancho; }
    public void setAncho(double ancho) { this.ancho = ancho; }

    public double getProfundidad() { return profundidad; }
    public void setProfundidad(double profundidad) { this.profundidad = profundidad; }

    public String getImagen1() { return imagen1; }
    public void setImagen1(String imagen1) { this.imagen1 = imagen1; }

    public String getImagen2() { return imagen2; }
    public void setImagen2(String imagen2) { this.imagen2 = imagen2; }

    public String getImagen3() { return imagen3; }
    public void setImagen3(String imagen3) { this.imagen3 = imagen3; }

    public String getImagen4() { return imagen4; }
    public void setImagen4(String imagen4) { this.imagen4 = imagen4; }

    public int getStockTotal() {
        return stockOncativo + stockOliva + stockDeposito3;
    }

    public double getGanancia() {
        return precioVenta - precioCompra;
    }

    public void setNegocio(String negocio) { this.negocio = negocio; }
}
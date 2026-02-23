package com.arcahome.dao;

import com.arcahome.db.DatabaseConnection;
import com.arcahome.model.Articulo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAO {

    public List<Articulo> listar(String ciudad) throws SQLException {
        List<Articulo> articulos = new ArrayList<>();
        String query = "SELECT * FROM articulos WHERE ciudad_articulo LIKE ? ORDER BY nombre_articulo ASC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, ciudad);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Articulo art = mapResultSetToArticulo(rs);
                articulos.add(art);
            }
        }
        return articulos;
    }

    public Articulo obtenerPorId(int id) throws SQLException {
        String query = "SELECT * FROM articulos WHERE id_articulo = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToArticulo(rs);
            }
        }
        return null;
    }

    public boolean insertar(Articulo art) throws SQLException {
        String query = "INSERT INTO articulos (nombre_articulo, descripcion_articulo, cant1articulo, cant2articulo, " +
                "negocio_articulo, categoria_articulo, precio_compra_articulo, precio_venta_articulo, " +
                "proveedor_articulo, imagen1, imagen2, imagen3, imagen4, ciudad_articulo, " +
                "peso_articulo, alto_articulo, ancho_articulo, profundidad_articulo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            setParams(ps, art);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Articulo art) throws SQLException {
        String query = "UPDATE articulos SET nombre_articulo=?, descripcion_articulo=?, cant1articulo=?, cant2articulo=?, " +
                "negocio_articulo=?, categoria_articulo=?, precio_compra_articulo=?, precio_venta_articulo=?, " +
                "proveedor_articulo=?, imagen1=?, imagen2=?, imagen3=?, imagen4=?, ciudad_articulo=?, " +
                "peso_articulo=?, alto_articulo=?, ancho_articulo=?, profundidad_articulo=? WHERE id_articulo=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             setParams(ps, art);
             ps.setInt(19, art.getId());
             return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarStock(int id, int cantidad, String sucursal) throws SQLException {
        String columna = sucursal.equals("Oncativo") ? "cant1articulo" : "cant2articulo";

        String query = "UPDATE articulos SET " + columna + " = " + columna + " + ? WHERE id_articulo = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Articulo> buscar(String busqueda, String sucursal) throws SQLException {
        List<Articulo> articulos = new ArrayList<>();
        String query = "SELECT * FROM articulos WHERE nombre_articulo LIKE ? AND ciudad_articulo LIKE ? ORDER BY nombre_articulo ASC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, "%" + busqueda + "%");
            ps.setString(2, sucursal);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                articulos.add(mapResultSetToArticulo(rs));
            }
        }
        return articulos;
    }

    private void setParams(PreparedStatement ps, Articulo art) throws SQLException {
        ps.setString(1, art.getNombre());
        ps.setString(2, art.getDescripcion());
        ps.setInt(3, art.getStockOncativo());
        ps.setInt(4, art.getStockOliva());
        ps.setString(5, art.getNegocio());
        ps.setString(6, art.getCategoria());
        ps.setDouble(7, art.getPrecioCompra());
        ps.setDouble(8, art.getPrecioVenta());
        ps.setString(9, art.getProveedor());
        ps.setString(10, art.getImagen1());
        ps.setString(11, art.getImagen2());
        ps.setString(12, art.getImagen3());
        ps.setString(13, art.getImagen4());
        ps.setString(14, art.getCiudad());
        ps.setDouble(15, art.getPeso());
        ps.setDouble(16, art.getAlto());
        ps.setDouble(17, art.getAncho());
        ps.setDouble(18, art.getProfundidad());
    }

    private Articulo mapResultSetToArticulo(ResultSet rs) throws SQLException {
        Articulo art = new Articulo();
        art.setId(rs.getInt("id_articulo"));
        art.setNombre(rs.getString("nombre_articulo"));
        art.setDescripcion(rs.getString("descripcion_articulo"));
        art.setStockOncativo(rs.getInt("cant1articulo"));
        art.setStockOliva(rs.getInt("cant2articulo"));
        art.setCategoria(rs.getString("categoria_articulo"));
        art.setPrecioCompra(rs.getDouble("precio_compra_articulo"));
        art.setPrecioVenta(rs.getDouble("precio_venta_articulo"));
        art.setProveedor(rs.getString("proveedor_articulo"));
        art.setImagen1(rs.getString("imagen1"));
        art.setImagen2(rs.getString("imagen2"));
        art.setImagen3(rs.getString("imagen3"));
        art.setImagen4(rs.getString("imagen4"));
        art.setCiudad(rs.getString("ciudad_articulo"));
        art.setPeso(rs.getDouble("peso_articulo"));
        art.setAlto(rs.getDouble("alto_articulo"));
        art.setAncho(rs.getDouble("ancho_articulo"));
        art.setProfundidad(rs.getDouble("profundidad_articulo"));
        return art;
    }
}
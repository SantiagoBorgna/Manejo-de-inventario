package com.arcahome.dao;

import com.arcahome.db.DatabaseConnection;
import com.arcahome.model.Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public List<Proveedor> listar(String busqueda) throws SQLException {
        List<Proveedor> lista = new ArrayList<>();
        String query = "SELECT * FROM proveedores WHERE nombreProveedor LIKE ? ORDER BY nombreProveedor ASC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, "%" + busqueda + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Proveedor(
                        rs.getString("nombreProveedor"),
                        rs.getString("firmaProveedor"),
                        rs.getString("localidadProveedor"),
                        rs.getString("contactoProveedor"),
                        rs.getDouble("compraMinimaProveedor")
                ));
            }
        }
        return lista;
    }

    public boolean insertar(Proveedor p) throws SQLException {
        String query = "INSERT INTO proveedores (nombreProveedor, firmaProveedor, localidadProveedor, contactoProveedor, compraMinimaProveedor) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getFirma());
            ps.setString(3, p.getLocalidad());
            ps.setString(4, p.getContacto());
            ps.setDouble(5, p.getCompraMinima());
            return ps.executeUpdate() > 0;
        }
    }
}
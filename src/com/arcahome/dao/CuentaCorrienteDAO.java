package com.arcahome.dao;

import com.arcahome.db.DatabaseConnection;
import com.arcahome.model.CuentaCorriente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaCorrienteDAO {

    public List<CuentaCorriente> listar(String busqueda, String sucursal) throws SQLException {
        List<CuentaCorriente> lista = new ArrayList<>();
        String query = "SELECT * FROM cuentas_corrientes WHERE nombre_cliente LIKE ? AND ciudad_venta = ? ORDER BY id_cuenta DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, "%" + busqueda + "%");
            ps.setString(2, sucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CuentaCorriente cc = new CuentaCorriente();
                cc.setId(rs.getInt("id_cuenta"));
                cc.setCliente(rs.getString("nombre_cliente"));
                cc.setDescripcion(rs.getString("descripcion_venta"));
                cc.setFecha(rs.getString("fecha_venta"));
                cc.setMonto(rs.getDouble("monto_venta"));
                cc.setSaldoDeudor(rs.getDouble("saldo_deudor"));
                cc.setGanancia(rs.getDouble("ganancia_venta"));
                lista.add(cc);
            }
        }
        return lista;
    }

    public boolean registrarPago(CuentaCorriente cc, double pago, String medio, String fechaActual, String sucursal) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        try {
            con.setAutoCommit(false);

            if (pago >= cc.getMonto()) {
                String delete = "DELETE FROM cuentas_corrientes WHERE id_cuenta = ?";
                try (PreparedStatement ps = con.prepareStatement(delete)) {
                    ps.setInt(1, cc.getId());
                    ps.executeUpdate();
                }
            } else {
                String update = "UPDATE cuentas_corrientes SET monto_venta = monto_venta - ? WHERE id_cuenta = ?";
                try (PreparedStatement ps = con.prepareStatement(update)) {
                    ps.setDouble(1, pago);
                    ps.setInt(2, cc.getId());
                    ps.executeUpdate();
                }
            }

            String updSaldo = "UPDATE cuentas_corrientes SET saldo_deudor = saldo_deudor - ? WHERE nombre_cliente = ?";
            try (PreparedStatement ps = con.prepareStatement(updSaldo)) {
                ps.setDouble(1, pago);
                ps.setString(2, cc.getCliente());
                ps.executeUpdate();
            }

            // Insertar en Libro Diario
            String libro = "INSERT INTO libro_diario (fecha_venta, sucursal_venta, cliente_venta, articulos_venta, monto_venta, medio_de_pago_venta, ganancia_venta) VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(libro)) {
                ps.setString(1, fechaActual);
                ps.setString(2, sucursal);
                ps.setString(3, cc.getCliente());
                ps.setString(4, "Pago de cuenta corriente: " + cc.getFecha());
                ps.setDouble(5, pago);
                ps.setString(6, medio);
                ps.setDouble(7, 0);
                ps.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }
}
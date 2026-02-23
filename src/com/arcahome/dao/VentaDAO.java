package com.arcahome.dao;

import com.arcahome.db.DatabaseConnection;
import com.arcahome.model.ItemCarrito;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.List;

public class VentaDAO {

    public boolean procesarVenta(String cliente, List<ItemCarrito> carrito, double monto1, String medio1, double monto2, String medio2, String sucursal, String fecha) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        try {
            con.setAutoCommit(false);

            // VALIDACIÓN PREVIA DE NOTA DE CRÉDITO
            if (medio2 != null && medio2.equals("Nota de crédito")) {
                String sqlCheck = "SELECT monto_nota FROM notas_credito WHERE nombre_cliente = ? AND ciudad_nota = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlCheck)) {
                    ps.setString(1, cliente);
                    ps.setString(2, sucursal);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new SQLException("El cliente no tiene notas de crédito a su nombre.");
                    }
                    double saldoDisponible = rs.getDouble("monto_nota");
                    if (monto2 > saldoDisponible) {
                        throw new SQLException("Saldo insuficiente en la nota de crédito. El saldo es $" + saldoDisponible);
                    }
                }
            }

            // DESCUENTO DE STOCK Y CÁLCULO DE COSTOS
            double costoTotalMercaderia = 0;
            StringBuilder descArticulos = new StringBuilder();
            for (ItemCarrito item : carrito) {
                String col = sucursal.equals("Oncativo") ? "cant1articulo" : "cant2articulo";
                String sqlStock = "UPDATE articulos SET " + col + " = " + col + " - ? WHERE id_articulo = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlStock)) {
                    ps.setInt(1, item.getCantidad());
                    ps.setInt(2, item.getIdArticulo());
                    ps.executeUpdate();
                }
                costoTotalMercaderia += item.getSubtotalCosto();
                descArticulos.append(item.getNombre()).append(" x").append(item.getCantidad()).append(" / ");
            }
            String descFinal = descArticulos.toString().substring(0, descArticulos.length() - 3);
            double montoVentaTotal = monto1 + monto2;
            double gananciaTotal = montoVentaTotal - costoTotalMercaderia;

            // REGISTRO DE VENTA 1
            registrarMovimiento(con, cliente, descFinal, monto1, medio1, gananciaTotal, sucursal, fecha);

            // REGISTRO DE VENTA 2
            if (monto2 > 0 && medio2 != null) {
                registrarMovimiento(con, cliente, "Segundo medio de pago", monto2, medio2, 0, sucursal, fecha);

                // Si el segundo medio fue Nota de Crédito, la restamos de la DB
                if (medio2.equals("Nota de crédito")) {
                    String sqlNC = "UPDATE notas_credito SET monto_nota = monto_nota - ? WHERE nombre_cliente = ? AND ciudad_nota = ?";
                    try (PreparedStatement ps = con.prepareStatement(sqlNC)) {
                        ps.setDouble(1, monto2);
                        ps.setString(2, cliente);
                        ps.setString(3, sucursal);
                        ps.executeUpdate();

                        con.prepareStatement("DELETE FROM notas_credito WHERE monto_nota <= 0").executeUpdate();
                    }
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            con.rollback();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    private void registrarMovimiento(Connection con, String cliente, String desc, double monto, String medio, double ganancia, String sucursal, String fecha) throws SQLException {
        if (medio == null || medio.equalsIgnoreCase("Nota de crédito")) {
            return;
        }

        if (medio.equalsIgnoreCase("Cuenta Corriente")) {
            // Lógica de Saldo Acumulado
            double saldoPrevio = 0;
            String sqlSaldo = "SELECT saldo_deudor FROM cuentas_corrientes WHERE nombre_cliente = ? LIMIT 1";
            try (PreparedStatement ps = con.prepareStatement(sqlSaldo)) {
                ps.setString(1, cliente);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) saldoPrevio = rs.getDouble("saldo_deudor");
            }

            double nuevoSaldo = saldoPrevio + monto;
            String sqlIns = "INSERT INTO cuentas_corrientes (fecha_venta, nombre_cliente, descripcion_venta, monto_venta, saldo_deudor, ganancia_venta, ciudad_venta) VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sqlIns)) {
                ps.setString(1, fecha);
                ps.setString(2, cliente);
                ps.setString(3, desc);
                ps.setDouble(4, monto);
                ps.setDouble(5, nuevoSaldo);
                ps.setDouble(6, ganancia);
                ps.setString(7, sucursal);
                ps.executeUpdate();
            }
            // Actualiza todas las entradas del cliente para que vean el mismo saldo final
            String sqlUpd = "UPDATE cuentas_corrientes SET saldo_deudor = ? WHERE nombre_cliente = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlUpd)) {
                ps.setDouble(1, nuevoSaldo);
                ps.setString(2, cliente);
                ps.executeUpdate();
            }
        } else {
            // Registro normal en Libro Diario
            String sqlLibro = "INSERT INTO libro_diario (fecha_venta, sucursal_venta, cliente_venta, articulos_venta, monto_venta, medio_de_pago_venta, ganancia_venta) VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sqlLibro)) {
                ps.setString(1, fecha);
                ps.setString(2, sucursal);
                ps.setString(3, cliente);
                ps.setString(4, desc);
                ps.setDouble(5, monto);
                ps.setString(6, medio);
                ps.setDouble(7, ganancia);
                ps.executeUpdate();
            }
        }
    }
}
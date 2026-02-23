package com.arcahome.dao;

import com.arcahome.db.DatabaseConnection;
import com.arcahome.model.VentaResumen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDiarioDAO {

    public List<VentaResumen> listarVentas(String sucursal, String fecha, String cliente, String medioPago) throws SQLException {
        List<VentaResumen> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT cliente_venta, articulos_venta, medio_de_pago_venta, monto_venta, ganancia_venta " +
                "FROM libro_diario WHERE sucursal_venta = ? AND fecha_venta = ?");

        if (!cliente.isEmpty()) sql.append(" AND cliente_venta LIKE ?");
        if (!medioPago.equals("Todo")) sql.append(" AND medio_de_pago_venta = ?");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int i = 1;
            ps.setString(i++, sucursal);
            ps.setString(i++, fecha);
            if (!cliente.isEmpty()) ps.setString(i++, "%" + cliente + "%");
            if (!medioPago.equals("Todo")) ps.setString(i++, medioPago);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new VentaResumen(
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5)
                ));
            }
        }
        return lista;
    }

    public double[] calcularTotalesMensuales(String sucursal, String mesAnio) throws SQLException {
        double[] totales = {0, 0}; // [ventas, ganancias]
        String sql = "SELECT SUM(monto_venta), SUM(ganancia_venta) FROM libro_diario WHERE sucursal_venta = ? AND fecha_venta LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sucursal);
            ps.setString(2, "%-" + mesAnio);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totales[0] = rs.getDouble(1);
                totales[1] = rs.getDouble(2);
            }
        }
        return totales;
    }
}
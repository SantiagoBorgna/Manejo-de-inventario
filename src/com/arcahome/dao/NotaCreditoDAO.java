package com.arcahome.dao;

import com.arcahome.db.DatabaseConnection;
import com.arcahome.model.NotaCredito;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotaCreditoDAO {

    public List<NotaCredito> listar(String busqueda, String sucursal) throws SQLException {
        List<NotaCredito> lista = new ArrayList<>();
        String query = "SELECT * FROM notas_credito WHERE ciudad_nota = ? AND nombre_cliente LIKE ? ORDER BY nombre_cliente ASC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, sucursal);
            ps.setString(2, "%" + busqueda + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new NotaCredito(
                        rs.getString("nombre_cliente"),
                        rs.getString("fecha_nota"),
                        rs.getDouble("monto_nota"),
                        rs.getString("motivo_nota"),
                        rs.getString("ciudad_nota")
                ));
            }
        }
        return lista;
    }

    public boolean guardarOActualizar(NotaCredito nota) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            // Verificar si existe
            String check = "SELECT monto_nota FROM notas_credito WHERE nombre_cliente = ? AND ciudad_nota = ?";
            PreparedStatement psCheck = con.prepareStatement(check);
            psCheck.setString(1, nota.getCliente());
            psCheck.setString(2, nota.getCiudad());
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                // Actualizar acumulando
                double nuevoMonto = rs.getDouble("monto_nota") + nota.getMonto();
                String update = "UPDATE notas_credito SET monto_nota = ?, fecha_nota = ?, motivo_nota = ? WHERE nombre_cliente = ? AND ciudad_nota = ?";
                PreparedStatement psUpd = con.prepareStatement(update);
                psUpd.setDouble(1, nuevoMonto);
                psUpd.setString(2, nota.getFecha());
                psUpd.setString(3, nota.getMotivo());
                psUpd.setString(4, nota.getCliente());
                psUpd.setString(5, nota.getCiudad());
                return psUpd.executeUpdate() > 0;
            } else {
                // Insertar nueva
                String insert = "INSERT INTO notas_credito (nombre_cliente, monto_nota, fecha_nota, motivo_nota, ciudad_nota) VALUES (?,?,?,?,?)";
                PreparedStatement psIns = con.prepareStatement(insert);
                psIns.setString(1, nota.getCliente());
                psIns.setDouble(2, nota.getMonto());
                psIns.setString(3, nota.getFecha());
                psIns.setString(4, nota.getMotivo());
                psIns.setString(5, nota.getCiudad());
                return psIns.executeUpdate() > 0;
            }
        }
    }
}
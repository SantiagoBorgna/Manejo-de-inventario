package com.arcahome.dao;

import com.arcahome.db.DatabaseConnection;
import com.arcahome.model.PedidoWeb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoWebDAO {

    public List<PedidoWeb> listar(String tipoEnvio, String busqueda, String filtroEstado) {
        List<PedidoWeb> lista = new ArrayList<>();
        String sql;

        // Filtro de Tipo
        String whereClause;
        if (tipoEnvio.equals("RETIRO")) {
            whereClause = "(metodo_envio LIKE '%Retirar%' OR metodo_envio LIKE '%Local%')";
        } else {
            whereClause = "(metodo_envio NOT LIKE '%Retirar%' AND metodo_envio NOT LIKE '%Local%')";
        }

        // Filtro de Estado
        if (filtroEstado.equals("Completados")) { // Usaremos un nombre genérico interno o el del combo
            whereClause += " AND despachado = 1";
        } else if (filtroEstado.equals("Pendientes")) {
            whereClause += " AND despachado = 0";
        }

        // 3. Consulta final con ORDEN ASCENDENTE
        sql = "SELECT * FROM pedidos_web WHERE " + whereClause +
                " AND (nombre_cliente LIKE ? OR apellido_cliente LIKE ? OR id LIKE ?) " +
                " ORDER BY id ASC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String searchPattern = "%" + busqueda + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PedidoWeb p = new PedidoWeb();
                p.setId(rs.getInt("id"));
                p.setFecha(rs.getString("fecha_pedido"));
                p.setNombre(rs.getString("nombre_cliente"));
                p.setApellido(rs.getString("apellido_cliente"));
                p.setDni(rs.getString("dni"));
                p.setTelefono(rs.getString("telefono"));
                p.setEmail(rs.getString("email"));
                p.setCalle(rs.getString("calle"));
                p.setNumero(rs.getString("numero"));
                p.setPisoDepto(rs.getString("piso_depto"));
                p.setCiudad(rs.getString("ciudad"));
                p.setProvincia(rs.getString("provincia"));
                p.setCp(rs.getString("codigo_postal"));
                p.setMetodoEnvio(rs.getString("metodo_envio"));
                p.setCostoEnvio(rs.getString("costo_envio"));
                p.setMedioPago(rs.getString("medio_pago"));

                try {
                    String totalStr = rs.getString("total_final");

                    if (totalStr != null) {
                        totalStr = totalStr.replace("$", "").trim();

                        if (totalStr.contains(",")) {
                            totalStr = totalStr.replace(".", "");
                            totalStr = totalStr.replace(",", ".");
                        }

                        p.setTotalFinal(Double.parseDouble(totalStr));
                    }
                } catch (Exception e) { p.setTotalFinal(0.0); }

                p.setResumenArticulos(rs.getString("resumen_articulos"));

                p.setCalle(rs.getString("calle"));
                p.setNumero(rs.getString("numero"));
                p.setCiudad(rs.getString("ciudad"));

                p.setDespachado(rs.getBoolean("despachado"));

                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean actualizarDespacho(int id, boolean estado) {
        String sql = "UPDATE pedidos_web SET despachado = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE pedidos_web SET estado = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
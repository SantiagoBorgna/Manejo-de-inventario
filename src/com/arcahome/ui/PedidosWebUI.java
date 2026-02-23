package com.arcahome.ui;

import com.arcahome.dao.PedidoWebDAO;
import com.arcahome.model.PedidoWeb;
import com.arcahome.main.App;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class PedidosWebUI {
    private final PedidoWebDAO dao = new PedidoWebDAO();
    private JTable table;
    private boolean esModoEnvio;
    private JComboBox<String> cbFiltroEstado;
    private TableRowSorter<DefaultTableModel> sorter;

    public PedidosWebUI(boolean esModoEnvio) {
        this.esModoEnvio = esModoEnvio;
    }

    public void abrirInterfaz() {
        String titulo = esModoEnvio ? "Gestión de Envíos Web" : "Pedidos Web - Retiro en Local";
        String labelCheck = esModoEnvio ? "Despachado" : "Entregado";
        String labelPendiente = esModoEnvio ? "No despachados" : "No entregados";
        String labelListo = esModoEnvio ? "Despachados" : "Entregados";

        JFrame frame = new JFrame(titulo);
        frame.setIconImage(App.logo);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        // --- PANEL SUPERIOR ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        topPanel.setBackground(new Color(220, 204, 181));

        JTextField txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(250, 45));
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 20));

        cbFiltroEstado = new JComboBox<>(new String[]{"Todos", labelPendiente, labelListo});
        cbFiltroEstado.setPreferredSize(new Dimension(200, 45));
        cbFiltroEstado.setFont(new Font("Roboto", Font.PLAIN, 18));
        cbFiltroEstado.setBackground(new Color(240, 233, 225));

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setPreferredSize(new Dimension(130, 60));
        btnSearch.setBackground(new Color(240, 233, 225));
        btnSearch.setFont(new Font("Roboto", Font.PLAIN, 24));

        topPanel.add(txtSearch);
        topPanel.add(cbFiltroEstado);
        topPanel.add(btnSearch);

        // --- DEFINICIÓN DE COLUMNAS SEGÚN MODO ---
        String[] cols;
        if (esModoEnvio) {
            // Tabla Completa para Envíos
            cols = new String[]{"ID", "Fecha", "Cliente", "DNI", "Email", "Teléfono", "CP", "Localidad", "Provincia", "Dirección", "Método", "Costo", "Detalle", labelCheck};
        } else {
            // Tabla Simple para Retiro
            cols = new String[]{"ID", "Fecha", "Cliente", "Total", "Detalle Compra", labelCheck};
        }

        // El índice del checkbox siempre es la última columna
        int checkIndex = cols.length - 1;

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == checkIndex) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == checkIndex;
            }
        };

        table = new JTable(model);

        // Configuración visual y Sorter
        configurarTabla(checkIndex);

        // Evento Checkbox
        model.addTableModelListener(e -> {
            if (e.getColumn() == checkIndex && e.getFirstRow() >= 0) {
                int row = e.getFirstRow();
                try {
                    int idPedido = (int) model.getValueAt(row, 0);
                    boolean nuevoEstado = (boolean) model.getValueAt(row, checkIndex);
                    dao.actualizarDespacho(idPedido, nuevoEstado);
                } catch (Exception ex) {}
            }
        });

        // Eventos Filtros
        btnSearch.addActionListener(e -> refrescar(txtSearch.getText().trim()));
        cbFiltroEstado.addActionListener(e -> refrescar(txtSearch.getText().trim()));

        refrescar("");

        frame.add(topPanel, BorderLayout.NORTH);
        // Usamos JScrollPane con scroll horizontal automático
        JScrollPane scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void refrescar(String busqueda) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String tipoEnvio = esModoEnvio ? "ENVIO" : "RETIRO";
        String seleccion = (String) cbFiltroEstado.getSelectedItem();
        String filtroEstado = "Todos";

        if (seleccion.contains("No ")) filtroEstado = "Pendientes";
        else if (seleccion.equals("Despachados") || seleccion.equals("Entregados")) filtroEstado = "Completados";

        List<PedidoWeb> lista = dao.listar(tipoEnvio, busqueda, filtroEstado);

        for (PedidoWeb p : lista) {
            if (esModoEnvio) {
                // Fila larga para Envíos
                model.addRow(new Object[]{
                        p.getId(),
                        p.getFecha(),
                        p.getNombreCompleto(),
                        p.getDni(),
                        p.getEmail(),
                        p.getTelefono(),
                        p.getCp(),
                        p.getCiudad(),
                        p.getProvincia(),
                        p.getCalle() + " " + p.getNumero() + " " + p.getPisoDepto(),
                        p.getMetodoEnvio(),
                        "$" + p.getCostoEnvio(), // Asegurate que esto sea String o Double según tu modelo
                        p.getResumenArticulos(),
                        p.isDespachado()
                });
            } else {
                // Fila corta para Retiro
                model.addRow(new Object[]{
                        p.getId(),
                        p.getFecha(),
                        p.getNombreCompleto(),
                        "$" + p.getTotalFinal(),
                        p.getResumenArticulos(),
                        p.isDespachado()
                });
            }
        }
    }

    private void configurarTabla(int checkIndex) {
        // Estilos base
        table.setRowHeight(40);
        table.setBackground(new Color(240, 233, 225));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 204, 181));
        table.setSelectionBackground(new Color(180, 140, 90));

        // Ocultar ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);

        sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);

        Comparator<Object> comparadorNumerico = (o1, o2) -> {
            String s1 = (o1 == null) ? "0" : o1.toString();
            String s2 = (o2 == null) ? "0" : o2.toString();
            s1 = s1.replaceAll("[^0-9.,-]", "").trim();
            s2 = s2.replaceAll("[^0-9.,-]", "").trim();
            try {
                if (s1.contains(",")) { s1 = s1.replace(".", "").replace(",", "."); }
                if (s2.contains(",")) { s2 = s2.replace(".", "").replace(",", "."); }
                return Double.valueOf(s1).compareTo(Double.valueOf(s2));
            } catch (Exception e) { return s1.compareToIgnoreCase(s2); }
        };

        // Aplicar anchos y comparadores según modo
        if (esModoEnvio) {
            // Desactivar auto-resize para que aparezca scroll horizontal
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            // Anchos específicos
            table.getColumnModel().getColumn(1).setPreferredWidth(100); // Fecha
            table.getColumnModel().getColumn(2).setPreferredWidth(200); // Cliente
            table.getColumnModel().getColumn(3).setPreferredWidth(90); // DNI
            table.getColumnModel().getColumn(4).setPreferredWidth(180); // Email
            table.getColumnModel().getColumn(5).setPreferredWidth(120); // Tel
            table.getColumnModel().getColumn(9).setPreferredWidth(200); // Dirección
            table.getColumnModel().getColumn(10).setPreferredWidth(120); // Envio
            table.getColumnModel().getColumn(12).setPreferredWidth(500); // Detalle

            // Aplicar comparador a Costo
            sorter.setComparator(11, comparadorNumerico);

        } else {
            // Auto resize para llenar pantalla
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            table.getColumnModel().getColumn(1).setPreferredWidth(100); // Fecha
            table.getColumnModel().getColumn(2).setPreferredWidth(250); // Cliente
            table.getColumnModel().getColumn(3).setPreferredWidth(120); // Total
            table.getColumnModel().getColumn(4).setPreferredWidth(500); // Detalle

            // Aplicar comparador a Total
            sorter.setComparator(3, comparadorNumerico);
        }

        // Checkbox
        table.getColumnModel().getColumn(checkIndex).setPreferredWidth(100);
        table.getColumnModel().getColumn(checkIndex).setMaxWidth(100);
    }
}
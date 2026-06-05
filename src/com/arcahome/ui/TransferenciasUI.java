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

public class TransferenciasUI {
    private final PedidoWebDAO dao = new PedidoWebDAO();
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;

    public TransferenciasUI() {
    }

    public void abrirInterfaz() {
        JFrame frame = new JFrame("Transferencias Pendientes");
        frame.setIconImage(App.logo);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        // --- PANEL SUPERIOR ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        topPanel.setBackground(new Color(220, 204, 181));

        JTextField txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(250, 45));
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 20));

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setPreferredSize(new Dimension(130, 60));
        btnSearch.setBackground(new Color(240, 233, 225));
        btnSearch.setFont(new Font("Roboto", Font.PLAIN, 24));

        topPanel.add(txtSearch);
        topPanel.add(btnSearch);

        // --- DEFINICIÓN DE COLUMNAS ---
        String[] cols = new String[]{"ID", "Fecha", "Cliente", "DNI", "Email", "Teléfono", "Total", "Detalle Compra", "Recibido"};

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
                    boolean recibido = (boolean) model.getValueAt(row, checkIndex);
                    if (recibido) {
                        int idPedido = (int) model.getValueAt(row, 0);
                        
                        SwingUtilities.invokeLater(() -> {
                            Object[] options = {"Sí", "No"};
                            int confirm = JOptionPane.showOptionDialog(frame,
                                    "¿Seguro que recibiste el comprobante?",
                                    "Confirmación",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[0]);
                            if (confirm == JOptionPane.YES_OPTION) {
                                boolean success = dao.cambiarEstado(idPedido, "PAGADO");
                                if (success) {
                                    JOptionPane.showMessageDialog(frame, "Pedido marcado como PAGADO.");
                                    refrescar(txtSearch.getText().trim());
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Error al actualizar estado.", "Error", JOptionPane.ERROR_MESSAGE);
                                    model.setValueAt(false, row, checkIndex);
                                }
                            } else {
                                model.setValueAt(false, row, checkIndex);
                            }
                        });
                    }
                } catch (Exception ex) {}
            }
        });

        // Eventos Filtros
        btnSearch.addActionListener(e -> refrescar(txtSearch.getText().trim()));

        refrescar("");

        frame.add(topPanel, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void refrescar(String busqueda) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        List<PedidoWeb> lista = dao.listarTransferenciasPendientes(busqueda);

        for (PedidoWeb p : lista) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getFecha(),
                    p.getNombreCompleto(),
                    p.getDni(),
                    p.getEmail(),
                    p.getTelefono(),
                    "$" + p.getTotalFinal(),
                    p.getResumenArticulos(),
                    false // Checkbox arranca en false
            });
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

        // Auto resize
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Fecha
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Cliente
        table.getColumnModel().getColumn(3).setPreferredWidth(90);  // DNI
        table.getColumnModel().getColumn(4).setPreferredWidth(180); // Email
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // Teléfono
        table.getColumnModel().getColumn(6).setPreferredWidth(120); // Total
        table.getColumnModel().getColumn(7).setPreferredWidth(500); // Detalle Compra

        // Aplicar comparador a Total
        sorter.setComparator(6, comparadorNumerico);

        // Checkbox
        table.getColumnModel().getColumn(checkIndex).setPreferredWidth(100);
        table.getColumnModel().getColumn(checkIndex).setMaxWidth(100);
    }
}

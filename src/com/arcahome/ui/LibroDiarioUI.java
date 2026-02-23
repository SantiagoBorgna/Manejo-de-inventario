package com.arcahome.ui;

import com.arcahome.dao.LibroDiarioDAO;
import com.arcahome.model.VentaResumen;
import com.arcahome.main.App;
import com.arcahome.logic.GeneradorArchivos;
import com.toedter.calendar.JCalendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class LibroDiarioUI {
    private final LibroDiarioDAO dao = new LibroDiarioDAO();
    private JLabel lblVentasDia, lblGananciasDia, lblVentasMes, lblGananciasMes;
    private JTable table;
    private JCalendar calendar;

    public void abrirInterfaz() {
        JFrame frame = new JFrame("Libro Diario - " + InterfazPrincipal.sucursalActual);
        frame.setIconImage(App.logo);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1400, 800));
        frame.setLayout(new BorderLayout());

        Font fLabelsInfo = new Font("Roboto", Font.BOLD, 16);

        // PANEL SUPERIOR
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        topPanel.setBackground(new Color(220, 204, 181));
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        // Info Izquierda
        lblVentasDia = new JLabel("Ventas del día: $0.00"); lblVentasDia.setFont(fLabelsInfo);
        lblGananciasDia = new JLabel("Ganancias del día: $0.00"); lblGananciasDia.setFont(fLabelsInfo);
        JPanel leftInfo = new JPanel(); leftInfo.setLayout(new BoxLayout(leftInfo, BoxLayout.Y_AXIS));
        leftInfo.setOpaque(false);
        leftInfo.add(lblVentasDia); leftInfo.add(Box.createRigidArea(new Dimension(0, 10))); leftInfo.add(lblGananciasDia);

        // Calendario
        calendar = new JCalendar();
        calendar.setBackground(new Color(240, 233, 225));
        calendar.setForeground(new Color(50, 50, 50));
        calendar.getDayChooser().setDecorationBackgroundColor(new Color(180, 140, 90));
        calendar.getDayChooser().setWeekdayForeground(Color.BLACK);
        calendar.getDayChooser().setSundayForeground(Color.RED);
        calendar.getDayChooser().setWeekOfYearVisible(false);
        // Fuente
        calendar.setFont(new Font("Roboto", Font.BOLD, 16));
        calendar.getDayChooser().setFont(new Font("Roboto", Font.PLAIN, 14));
        calendar.getMonthChooser().getComboBox().setFont(new Font("Roboto", Font.BOLD, 16));
        // Bordes
        calendar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 140, 90), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Filtros
        JTextField txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(200, 40)); // Caja más alta
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 18));

        JComboBox<String> cbPago = new JComboBox<>(new String[]{"Todo", "Efectivo", "Transferencia", "Tarjeta de crédito", "Tarjeta de débito", "Cheque", "Dólares", "Nota de crédito", "Fiserv"});
        cbPago.setPreferredSize(new Dimension(220, 40));
        cbPago.setFont(new Font("Roboto", Font.PLAIN, 18));
        cbPago.setBackground(new Color(240, 233, 225));

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setPreferredSize(new Dimension(130, 60));
        btnFiltrar.setBackground(new Color(240, 233, 225));
        btnFiltrar.setFont(new Font("Roboto", Font.PLAIN, 24));

        // Info Derecha
        lblVentasMes = new JLabel("Ventas del mes: $0.00"); lblVentasMes.setFont(fLabelsInfo);
        lblGananciasMes = new JLabel("Ganancias del mes: $0.00"); lblGananciasMes.setFont(fLabelsInfo);
        JPanel rightInfo = new JPanel(); rightInfo.setLayout(new BoxLayout(rightInfo, BoxLayout.Y_AXIS));
        rightInfo.setOpaque(false);
        rightInfo.add(lblVentasMes); rightInfo.add(Box.createRigidArea(new Dimension(0, 10))); rightInfo.add(lblGananciasMes);

        topPanel.add(leftInfo);
        topPanel.add(calendar);
        topPanel.add(txtSearch);
        topPanel.add(cbPago);
        topPanel.add(btnFiltrar);
        topPanel.add(rightInfo);

        // TABLA
        String[] cols = {"Nombre", "Artículos", "Medio de pago", "Importe"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        configurarEstiloTabla(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        // PANEL INFERIOR
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomPanel.setBackground(new Color(220, 204, 181));
        bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));
        bottomPanel.setPreferredSize(new Dimension(0, 90));

        JButton btnPdf = new JButton("Generar PDF");
        btnPdf.setPreferredSize(new Dimension(200, 60));
        btnPdf.setBackground(new Color(240, 233, 225));
        btnPdf.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnPdf.addActionListener(e -> GeneradorArchivos.iniciarGeneracionPDF());
        bottomPanel.add(btnPdf);

        // EVENTOS
        btnFiltrar.addActionListener(e -> refrescarDatos(txtSearch.getText().trim(), (String)cbPago.getSelectedItem()));

        refrescarDatos("", "Todo");

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void configurarEstiloTabla(JTable t) {
        t.setRowHeight(35);
        t.setBackground(new Color(240, 233, 225));
        t.setFont(new Font("Arial", Font.PLAIN, 16));
        t.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
        t.getTableHeader().setBackground(new Color(220, 204, 181));
        t.getTableHeader().setForeground(Color.BLACK);
        t.setSelectionBackground(new Color(180, 140, 90));
        t.getTableHeader().setReorderingAllowed(false);
    }

    private void refrescarDatos(String cliente, String pago) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String fecha = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getDate());
        String mesAnio = new SimpleDateFormat("MM-yyyy").format(calendar.getDate());

        try {
            List<VentaResumen> ventas = dao.listarVentas(InterfazPrincipal.sucursalActual, fecha, cliente, pago);
            double tDia = 0, gDia = 0;
            for (VentaResumen v : ventas) {
                model.addRow(new Object[]{v.getCliente(), v.getArticulos(), v.getMedioPago(), "$"+v.getImporte()});
                tDia += v.getImporte();
                gDia += v.getGanancia();
            }
            lblVentasDia.setText("Ventas día: $" + String.format("%.2f", tDia));
            lblGananciasDia.setText("Ganancias día: $" + String.format("%.2f", gDia));

            double[] mensuales = dao.calcularTotalesMensuales(InterfazPrincipal.sucursalActual, mesAnio);
            lblVentasMes.setText("Ventas mes: $" + String.format("%.2f", mensuales[0]));
            lblGananciasMes.setText("Ganancias mes: $" + String.format("%.2f", mensuales[1]));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
package com.arcahome.ui;

import com.arcahome.main.App;
import com.arcahome.dao.NotaCreditoDAO;
import com.arcahome.model.NotaCredito;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotasCreditoUI {

    private final NotaCreditoDAO dao = new NotaCreditoDAO();
    private JTable table;

    public void abrirInterfaz() {
        JFrame frame = new JFrame("Notas de Crédito - " + InterfazPrincipal.sucursalActual);
        frame.setIconImage(App.logo);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1400, 800));
        frame.setLayout(new BorderLayout());

        // PANEL SUPERIOR
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        topPanel.setBackground(new Color(220, 204, 181));
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        JButton btnNueva = new JButton("Nueva Nota");
        btnNueva.setPreferredSize(new Dimension(200, 50)); // Botón de acción principal
        btnNueva.setBackground(new Color(240, 233, 225));
        btnNueva.setFont(new Font("Roboto", Font.PLAIN, 22));

        JTextField txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(300, 45)); // Input más ancho y alto
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 20));

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setPreferredSize(new Dimension(130, 50)); // Estándar 130x60
        btnSearch.setBackground(new Color(240, 233, 225));
        btnSearch.setFont(new Font("Roboto", Font.PLAIN, 24));

        topPanel.add(btnNueva);
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);

        // TABLA
        String[] columns = {"Cliente", "Fecha", "Monto Disponible", "Motivo"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        configurarEstiloTabla();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        // LISTENERS
        btnNueva.addActionListener(e -> abrirFormularioNueva());
        btnSearch.addActionListener(e -> refrescarTabla(txtSearch.getText().trim()));

        refrescarTabla(""); // Carga inicial

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void refrescarTabla(String filtro) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        try {
            List<NotaCredito> lista = dao.listar(filtro, InterfazPrincipal.sucursalActual);
            for (NotaCredito n : lista) {
                model.addRow(new Object[]{n.getCliente(), n.getFecha(), "$" + n.getMonto(), n.getMotivo()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar notas.");
        }
    }

    private void abrirFormularioNueva() {
        JFrame form = new JFrame("Nueva Nota de Crédito");
        form.setIconImage(App.logo);
        form.setSize(650, 470);
        form.setLocationRelativeTo(null);
        form.setResizable(false);

        JPanel panel = new JPanel(new SpringLayout());
        panel.setBackground(new Color(240, 233, 225));
        SpringLayout layout = (SpringLayout) panel.getLayout();
        form.add(panel);

        Font fLabel = new Font("Roboto", Font.PLAIN, 28);
        Font fInput = new Font("Arial", Font.PLAIN, 20);

        // COMPONENTES
        JLabel lblCliente = new JLabel("Cliente:"); lblCliente.setFont(fLabel);
        JTextField txtCliente = new JTextField();
        txtCliente.setPreferredSize(new Dimension(300, 45)); txtCliente.setFont(fInput);

        JLabel lblMonto = new JLabel("Monto:"); lblMonto.setFont(fLabel);
        JTextField txtMonto = new JTextField();
        txtMonto.setPreferredSize(new Dimension(300, 45)); txtMonto.setFont(fInput);

        JLabel lblMotivo = new JLabel("Motivo:"); lblMotivo.setFont(fLabel);
        JTextField txtMotivo = new JTextField();
        txtMotivo.setPreferredSize(new Dimension(300, 45)); txtMotivo.setFont(fInput);

        JButton btnGuardar = new JButton("GUARDAR");
        btnGuardar.setPreferredSize(new Dimension(250, 60));
        btnGuardar.setBackground(new Color(220, 204, 181));
        btnGuardar.setFont(new Font("Roboto", Font.PLAIN, 24));

        panel.add(lblCliente); panel.add(txtCliente);
        panel.add(lblMonto); panel.add(txtMonto);
        panel.add(lblMotivo); panel.add(txtMotivo);
        panel.add(btnGuardar);

        // POSICIONAMIENTO
        Component last = panel;

        // Cliente
        layout.putConstraint(SpringLayout.NORTH, txtCliente, 50, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, txtCliente, 250, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, lblCliente, -20, SpringLayout.WEST, txtCliente);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblCliente, 0, SpringLayout.VERTICAL_CENTER, txtCliente);
        last = txtCliente;

        // Monto
        layout.putConstraint(SpringLayout.NORTH, txtMonto, 30, SpringLayout.SOUTH, last);
        layout.putConstraint(SpringLayout.WEST, txtMonto, 250, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, lblMonto, -20, SpringLayout.WEST, txtMonto);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblMonto, 0, SpringLayout.VERTICAL_CENTER, txtMonto);
        last = txtMonto;

        // Motivo
        layout.putConstraint(SpringLayout.NORTH, txtMotivo, 30, SpringLayout.SOUTH, last);
        layout.putConstraint(SpringLayout.WEST, txtMotivo, 250, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, lblMotivo, -20, SpringLayout.WEST, txtMotivo);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblMotivo, 0, SpringLayout.VERTICAL_CENTER, txtMotivo);
        last = txtMotivo;

        // Botón
        layout.putConstraint(SpringLayout.NORTH, btnGuardar, 60, SpringLayout.SOUTH, last);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnGuardar, 0, SpringLayout.HORIZONTAL_CENTER, panel);

        // LÓGICA DE GUARDADO
        btnGuardar.addActionListener(e -> {
            try {
                if(txtCliente.getText().trim().isEmpty() || txtMonto.getText().trim().isEmpty()) {
                    throw new Exception("El nombre y el monto son obligatorios.");
                }

                String fecha = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                double montoVal = Double.parseDouble(txtMonto.getText().replace(",", ".").trim());

                NotaCredito n = new NotaCredito(
                        txtCliente.getText().trim(),
                        fecha,
                        montoVal,
                        txtMotivo.getText().trim(),
                        InterfazPrincipal.sucursalActual
                );

                if (dao.guardarOActualizar(n)) {
                    JOptionPane.showMessageDialog(form, "Nota de crédito registrada correctamente.");
                    form.dispose();
                    refrescarTabla("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(form, "El monto debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        form.setVisible(true);
    }

    private void configurarEstiloTabla() {
        table.setRowHeight(35);
        table.setBackground(new Color(240, 233, 225));
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(220, 204, 181));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(180, 140, 90));
        table.getTableHeader().setReorderingAllowed(false);
    }
}
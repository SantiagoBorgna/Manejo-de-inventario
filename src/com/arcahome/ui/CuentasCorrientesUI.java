package com.arcahome.ui;

import com.arcahome.dao.CuentaCorrienteDAO;
import com.arcahome.model.CuentaCorriente;
import com.arcahome.main.App;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class CuentasCorrientesUI {
    private final CuentaCorrienteDAO dao = new CuentaCorrienteDAO();
    private JTable table;

    public void abrirInterfaz() {
        JFrame frame = new JFrame("Cuentas Corrientes - El Arca Home");
        frame.setIconImage(App.logo);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1400, 800));
        frame.setLayout(new BorderLayout());

        // PANEL SUPERIOR
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        topPanel.setBackground(new Color(220, 204, 181));
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        JTextField txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(300, 45));
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 20));

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setPreferredSize(new Dimension(130, 50));
        btnSearch.setBackground(new Color(240, 233, 225));
        btnSearch.setFont(new Font("Roboto", Font.PLAIN, 24));

        topPanel.add(txtSearch);
        topPanel.add(btnSearch);

        // TABLA
        String[] cols = {"ID", "Cliente", "Descripción", "Fecha", "Monto", "Saldo total", "Saldar", "Imprimir"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c >= 6; } // Solo botones editables
        };
        table = new JTable(model);
        configurarTabla();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        // LISTENERS
        btnSearch.addActionListener(e -> refrescar(txtSearch.getText().trim()));

        refrescar(""); // Carga inicial

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void refrescar(String b) {
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);
        try {
            List<CuentaCorriente> lista = dao.listar(b, InterfazPrincipal.sucursalActual);
            for (CuentaCorriente c : lista) {
                m.addRow(new Object[]{c.getId(), c.getCliente(), c.getDescripcion(), c.getFecha(), c.getMonto(), c.getSaldoDeudor(), "Pagar", "PDF"});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void configurarTabla() {
        // Estilos Generales
        table.setRowHeight(35);
        table.setBackground(new Color(240, 233, 225));
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(220, 204, 181));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(180, 140, 90));
        table.getTableHeader().setReorderingAllowed(false);

        // OCULTAR COLUMNA ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);

        // Renderers para botones
        table.getColumn("Saldar").setCellRenderer(new ButtonRenderer("Saldar", new Color(181, 220, 181)));
        table.getColumn("Saldar").setCellEditor(new ButtonEditor("Saldar"));
        table.getColumn("Imprimir").setCellRenderer(new ButtonRenderer("PDF", new Color(181, 181, 220)));
        table.getColumn("Imprimir").setCellEditor(new ButtonEditor("PDF"));
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String t, Color c) { setText(t); setBackground(c); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) { return this; }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String type;

        public ButtonEditor(String type) {
            super(new JCheckBox());
            this.type = type;
        }

        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            int modelRow = t.convertRowIndexToModel(r);

            int id = (int) t.getModel().getValueAt(modelRow, 0);
            String cliente = (String) t.getModel().getValueAt(modelRow, 1);
            String desc = (String) t.getModel().getValueAt(modelRow, 2);
            String fecha = (String) t.getModel().getValueAt(modelRow, 3);
            String montoStr = t.getModel().getValueAt(modelRow, 4).toString().replace("$", "").replace(",", ".");
            String saldoStr = t.getModel().getValueAt(modelRow, 5).toString().replace("$", "").replace(",", ".");

            double monto = Double.parseDouble(montoStr);
            double saldo = Double.parseDouble(saldoStr);

            if (type.equals("Saldar")) {
                abrirVentanaPago(id, cliente, desc, fecha, monto, saldo);
            } else {
                generarPDF(cliente, desc, fecha, monto, saldo);
            }

            return new JButton(type);
        }
    }

    private void abrirVentanaPago(int id, String cliente, String desc, String fecha, double monto, double saldo) {
        JFrame framePago = new JFrame("Saldar Cuenta - " + cliente);
        framePago.setIconImage(App.logo);
        framePago.setSize(620, 450);
        framePago.setLocationRelativeTo(null);
        framePago.setResizable(false);

        JPanel panelPago = new JPanel(new SpringLayout());
        panelPago.setBackground(new Color(240, 233, 225));
        SpringLayout layout = (SpringLayout) panelPago.getLayout();
        framePago.add(panelPago);

        // Componentes
        JLabel lblTitulo = new JLabel(cliente);
        lblTitulo.setFont(new Font("Roboto", Font.BOLD, 26));

        JLabel lblSaldoInfo = new JLabel("Saldo deudor actual: $" + String.format("%.2f", monto));
        lblSaldoInfo.setFont(new Font("Roboto", Font.PLAIN, 20));

        JLabel lblMontoPago = new JLabel("Monto a pagar:");
        lblMontoPago.setFont(new Font("Roboto", Font.PLAIN, 24));
        JTextField txtMontoPago = new JTextField();
        txtMontoPago.setPreferredSize(new Dimension(220, 45));
        txtMontoPago.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel lblMedio = new JLabel("Medio de pago:");
        lblMedio.setFont(new Font("Roboto", Font.PLAIN, 24));
        JComboBox<String> cbMedio = new JComboBox<>(new String[]{"Efectivo", "Transferencia", "Tarjeta de crédito", "Tarjeta de débito", "Cheque", "Dólares"});
        cbMedio.setPreferredSize(new Dimension(220, 45));
        cbMedio.setFont(new Font("Roboto", Font.PLAIN, 20));

        JButton btnConfirmar = new JButton("Registrar Pago");
        btnConfirmar.setPreferredSize(new Dimension(250, 70));
        btnConfirmar.setBackground(new Color(220, 204, 181));
        btnConfirmar.setFont(new Font("Roboto", Font.PLAIN, 22));

        panelPago.add(lblTitulo); panelPago.add(lblSaldoInfo);
        panelPago.add(lblMontoPago); panelPago.add(txtMontoPago);
        panelPago.add(lblMedio); panelPago.add(cbMedio);
        panelPago.add(btnConfirmar);

        // Posicionamiento
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, lblTitulo, 0, SpringLayout.HORIZONTAL_CENTER, panelPago);
        layout.putConstraint(SpringLayout.NORTH, lblTitulo, 20, SpringLayout.NORTH, panelPago);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, lblSaldoInfo, 0, SpringLayout.HORIZONTAL_CENTER, panelPago);
        layout.putConstraint(SpringLayout.NORTH, lblSaldoInfo, 15, SpringLayout.SOUTH, lblTitulo);

        layout.putConstraint(SpringLayout.NORTH, txtMontoPago, 40, SpringLayout.SOUTH, lblSaldoInfo);
        layout.putConstraint(SpringLayout.WEST, txtMontoPago, 300, SpringLayout.WEST, panelPago);
        layout.putConstraint(SpringLayout.EAST, lblMontoPago, -20, SpringLayout.WEST, txtMontoPago);
        layout.putConstraint(SpringLayout.NORTH, lblMontoPago, 5, SpringLayout.NORTH, txtMontoPago);

        layout.putConstraint(SpringLayout.NORTH, cbMedio, 20, SpringLayout.SOUTH, txtMontoPago);
        layout.putConstraint(SpringLayout.WEST, cbMedio, 300, SpringLayout.WEST, panelPago);
        layout.putConstraint(SpringLayout.EAST, lblMedio, -20, SpringLayout.WEST, cbMedio);
        layout.putConstraint(SpringLayout.NORTH, lblMedio, 5, SpringLayout.NORTH, cbMedio);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnConfirmar, 0, SpringLayout.HORIZONTAL_CENTER, panelPago);
        layout.putConstraint(SpringLayout.NORTH, btnConfirmar, 50, SpringLayout.SOUTH, cbMedio);

        btnConfirmar.addActionListener(e -> {
            try {
                double pago = Double.parseDouble(txtMontoPago.getText().trim());
                if (pago <= 0 || pago > monto + 0.01) {
                    JOptionPane.showMessageDialog(framePago, "Monto inválido.");
                    return;
                }

                CuentaCorriente cc = new CuentaCorriente();
                cc.setId(id);
                cc.setCliente(cliente);
                cc.setDescripcion(desc);
                cc.setFecha(fecha);
                cc.setMonto(monto);
                cc.setSaldoDeudor(saldo);

                if (dao.registrarPago(cc, pago, (String)cbMedio.getSelectedItem(),
                        App.fechaActual, InterfazPrincipal.sucursalActual)) {

                    JOptionPane.showMessageDialog(framePago, "Pago registrado y Libro Diario actualizado.");
                    framePago.dispose();
                    refrescar("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(framePago, "Error: Ingrese un monto válido.");
            }
        });

        framePago.setVisible(true);
    }

    public void generarPDF(String cliente, String descripcion, String fecha, double monto, double saldoTotal) {
        try {
            String rutaDesktop = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Cuentas_Corrientes";
            File carpeta = new File(rutaDesktop);
            if (!carpeta.exists()) carpeta.mkdirs();

            String nombreArchivo = cliente.replaceAll(" ", "_") + "_" + fecha + ".pdf";
            String rutaCompleta = rutaDesktop + File.separator + nombreArchivo;

            // Crear Documento iText
            com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
            PdfWriter.getInstance(doc, new FileOutputStream(rutaCompleta));
            doc.open();

            com.itextpdf.text.Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            com.itextpdf.text.Font normal = FontFactory.getFont(FontFactory.HELVETICA, 12);
            LineSeparator separator = new LineSeparator();

            // Encabezado Dinámico según Sucursal
            String infoSucursal = InterfazPrincipal.sucursalActual.equals("Oncativo")
                    ? "Tomás Garzón 1085 - Oncativo, Cba"
                    : "Rivadavia 99 (Paseo de la Estación) - Oliva, Cba";

            doc.add(new Paragraph("EL ARCA HOME", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
            doc.add(new Paragraph(infoSucursal, normal));
            doc.add(new Paragraph("Fecha: " + fecha, normal));
            doc.add(new Chunk(separator));
            doc.add(new Paragraph("\n"));

            // Detalles de la Cuenta
            doc.add(new Paragraph("CLIENTE: " + cliente, bold));
            doc.add(new Paragraph("DETALLE: " + descripcion, normal));
            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("MONTO DE ESTA OPERACIÓN: $" + String.format("%.2f", monto), bold));
            doc.add(new Paragraph("SALDO TOTAL PENDIENTE: $" + String.format("%.2f", saldoTotal), bold));

            doc.add(new Paragraph("\n\n"));
            doc.add(new Chunk(separator));
            doc.add(new Paragraph("COMPROBANTE DE MOVIMIENTO INTERNO - NO VÁLIDO COMO FACTURA",
                    FontFactory.getFont(FontFactory.HELVETICA, 10, com.itextpdf.text.Font.ITALIC)));

            doc.close();

            JOptionPane.showMessageDialog(null, "PDF generado.");

            // Abrir el archivo automáticamente
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(rutaCompleta));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al crear PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
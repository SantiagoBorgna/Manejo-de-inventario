package com.arcahome.ui;

import com.arcahome.main.App;
import com.arcahome.dao.ProveedorDAO;
import com.arcahome.model.Proveedor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProveedoresUI {

    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private JTable proveedoresTable;

    public void abrirInterfaz() {
        JFrame frame = new JFrame("Gestión de Proveedores - El Arca Home");
        frame.setIconImage(App.logo);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1400, 800));
        frame.setLayout(new BorderLayout());

        // PANEL SUPERIOR
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        topPanel.setBackground(new Color(220, 204, 181));
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        JButton btnNuevo = new JButton("Nuevo proveedor");
        btnNuevo.setPreferredSize(new Dimension(220, 50));
        btnNuevo.setFont(new Font("Roboto", Font.PLAIN, 22));
        btnNuevo.setBackground(new Color(240, 233, 225));

        JTextField txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 45));
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 20));

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setPreferredSize(new Dimension(130, 50));
        btnSearch.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnSearch.setBackground(new Color(240, 233, 225));

        topPanel.add(btnNuevo);
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);

        // TABLA
        String[] columns = {"Nombre", "Firma", "Localidad", "Contacto", "Compra mínima"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        proveedoresTable = new JTable(model);
        configurarEstiloTabla();

        JScrollPane scrollPane = new JScrollPane(proveedoresTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        // LISTENERS
        btnNuevo.addActionListener(e -> abrirFormularioNuevo());
        btnSearch.addActionListener(e -> {
            refrescarTabla(txtSearch.getText().trim());
            txtSearch.setText("");
        });

        refrescarTabla("");

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void refrescarTabla(String filtro) {
        DefaultTableModel model = (DefaultTableModel) proveedoresTable.getModel();
        model.setRowCount(0);
        try {
            List<Proveedor> lista = proveedorDAO.listar(filtro);
            for (Proveedor p : lista) {
                model.addRow(new Object[]{p.getNombre(), p.getFirma(), p.getLocalidad(), p.getContacto(), p.getCompraMinima()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void abrirFormularioNuevo() {
        JFrame form = new JFrame("Nuevo Proveedor");
        form.setIconImage(App.logo);
        form.setSize(700, 600);
        form.setLocationRelativeTo(null);
        form.setResizable(false);

        JPanel p = new JPanel(new SpringLayout());
        p.setBackground(new Color(240, 233, 225));
        SpringLayout layout = (SpringLayout) p.getLayout();
        form.add(p);

        Font fLabel = new Font("Roboto", Font.PLAIN, 28);
        Font fInput = new Font("Arial", Font.PLAIN, 20);

        // Componentes y Posicionamiento Dinámico
        String[] labels = {"Nombre:", "Firma:", "Localidad:", "Contacto:", "Compra mínima:"};
        JTextField[] fields = new JTextField[5];
        Component last = p;

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(fLabel);
            fields[i] = new JTextField(15);
            fields[i].setFont(fInput);
            fields[i].setPreferredSize(new Dimension(300, 45)); // Inputs más grandes

            p.add(lbl);
            p.add(fields[i]);

            // Ajuste de anclajes para mayor separación
            layout.putConstraint(SpringLayout.NORTH, fields[i], i == 0 ? 40 : 35, i == 0 ? SpringLayout.NORTH : SpringLayout.SOUTH, last);
            layout.putConstraint(SpringLayout.WEST, fields[i], 320, SpringLayout.WEST, p);
            layout.putConstraint(SpringLayout.EAST, lbl, -20, SpringLayout.WEST, fields[i]);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, lbl, 0, SpringLayout.VERTICAL_CENTER, fields[i]);
            last = fields[i];
        }

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(200, 60));
        btnGuardar.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnGuardar.setBackground(new Color(220, 204, 181));
        p.add(btnGuardar);

        layout.putConstraint(SpringLayout.NORTH, btnGuardar, 50, SpringLayout.SOUTH, last);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnGuardar, 0, SpringLayout.HORIZONTAL_CENTER, p);

        btnGuardar.addActionListener(e -> {
            try {
                // Validación básica de campos vacíos
                for(JTextField f : fields) if(f.getText().trim().isEmpty()) throw new Exception("Campos incompletos");

                Proveedor prov = new Proveedor(
                        fields[0].getText().trim(),
                        fields[1].getText().trim(),
                        fields[2].getText().trim(),
                        fields[3].getText().trim(),
                        Double.parseDouble(fields[4].getText().trim())
                );

                if (proveedorDAO.insertar(prov)) {
                    JOptionPane.showMessageDialog(form, "Proveedor registrado con éxito.");
                    form.dispose();
                    refrescarTabla("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(form, "La compra mínima debe ser un número.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "Por favor complete todos los campos.");
            }
        });

        form.setVisible(true);
    }

    private void configurarEstiloTabla() {
        proveedoresTable.setRowHeight(30);
        proveedoresTable.setBackground(new Color(240, 233, 225));
        proveedoresTable.setFont(new Font("Arial", Font.PLAIN, 16));
        proveedoresTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
        proveedoresTable.getTableHeader().setBackground(new Color(220, 204, 181));
        proveedoresTable.getTableHeader().setForeground(Color.BLACK);
        proveedoresTable.setSelectionBackground(new Color(180, 140, 90));
        proveedoresTable.getTableHeader().setReorderingAllowed(false);
    }
}
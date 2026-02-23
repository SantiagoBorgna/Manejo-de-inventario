package com.arcahome.ui;

import com.arcahome.dao.VentaDAO;
import com.arcahome.dao.ArticuloDAO;
import com.arcahome.model.Articulo;
import com.arcahome.model.ItemCarrito;
import com.arcahome.main.App;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentasUI {
    private final VentaDAO ventaDAO = new VentaDAO();
    private final ArticuloDAO articuloDAO = new ArticuloDAO();
    private List<ItemCarrito> carrito = new ArrayList<>();
    private JLabel lblTotal = new JLabel("Total: $0");
    JFrame frame;

    public void abrirInterfaz() {
        frame = new JFrame("Venta - Sucursal " + InterfazPrincipal.sucursalActual);
        frame.setIconImage(App.logo);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1400, 800));
        frame.setLayout(new BorderLayout());

        // PANEL SUPERIOR
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(new Color(220, 204, 181));
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        JTextField txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(250, 40));
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setPreferredSize(new Dimension(130, 60));
        btnSearch.setBackground(new Color(240, 233, 225));
        btnSearch.setFont(new Font("Roboto", Font.PLAIN, 24));

        topPanel.add(txtSearch);
        topPanel.add(btnSearch);

        // PANEL CENTRAL
        String[] artCols = {"ID", "Nombre", "Descripción", "Stock", "Precio Unitario"};
        DefaultTableModel artModel = new DefaultTableModel(artCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable artTable = new JTable(artModel);
        configurarEstiloTabla(artTable);

        // Anchos columnas
        artTable.getColumnModel().getColumn(0).setMaxWidth(0);
        artTable.getColumnModel().getColumn(0).setMinWidth(0);
        artTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        artTable.getColumnModel().getColumn(1).setPreferredWidth(250);

        artTable.getColumnModel().getColumn(2).setPreferredWidth(600);

        artTable.getColumnModel().getColumn(3).setMinWidth(80);
        artTable.getColumnModel().getColumn(3).setMaxWidth(100);
        artTable.getColumnModel().getColumn(3).setPreferredWidth(90);

        artTable.getColumnModel().getColumn(4).setMinWidth(120);
        artTable.getColumnModel().getColumn(4).setMaxWidth(200);
        artTable.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane artScrollPane = new JScrollPane(artTable);
        artScrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        // PANEL DERECHO
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(400, 0));
        rightPanel.setBackground(new Color(240, 233, 225));
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        String[] cartCols = {"Artículo", "Cant", "Subtotal", "Eliminar"};
        DefaultTableModel cartModel = new DefaultTableModel(cartCols, 0);
        JTable cartTable = new JTable(cartModel);
        configurarEstiloTabla(cartTable);

        // Centrar columna eliminar
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        cartTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JPanel cartControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        cartControls.setBackground(new Color(220, 204, 181));
        cartControls.setPreferredSize(new Dimension(300, 55));

        JLabel lblCant = new JLabel("Cantidad:");
        lblCant.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField txtCant = new JTextField("", 3);
        txtCant.setFont(new Font("Arial", Font.PLAIN, 20));
        JButton btnAdd = new JButton("Añadir al carrito");
        btnAdd.setFont(new Font("Roboto", Font.PLAIN, 20));
        btnAdd.setBackground(new Color(240, 233, 225));

        cartControls.add(lblCant);
        cartControls.add(txtCant);
        cartControls.add(btnAdd);

        rightPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        rightPanel.add(cartControls, BorderLayout.SOUTH);

        // PANEL INFERIOR
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 10));
        bottomPanel.setBackground(new Color(220, 204, 181));
        bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        lblTotal.setFont(new Font("Roboto", Font.PLAIN, 22));
        JButton btnVender = new JButton("Vender");
        btnVender.setPreferredSize(new Dimension(120, 50));
        btnVender.setBackground(new Color(240, 233, 225));
        btnVender.setFont(new Font("Roboto", Font.PLAIN, 24));

        bottomPanel.add(lblTotal);
        bottomPanel.add(btnVender);

        cargarArticulosEnTabla(artModel, InterfazPrincipal.sucursalActual);

        // EVENTOS
        btnSearch.addActionListener(e -> buscarArticulos(artModel, txtSearch.getText().trim()));

        btnAdd.addActionListener(e -> {
            int row = artTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Selecciona un artículo primero.");
                return;
            }
            try {
                int id = (int) artTable.getValueAt(row, 0);
                int stockDisponible = (int) artTable.getValueAt(row, 3);
                int cantNueva = Integer.parseInt(txtCant.getText());

                if (cantNueva > stockDisponible) {
                    JOptionPane.showMessageDialog(frame, "No puedes vender más de lo que hay en stock.");
                    return;
                } else if (cantNueva < 1){
                    JOptionPane.showMessageDialog(frame, "No puedes vender esa cantidad.");
                    return;
                }

                // Lógica de unificación
                boolean encontrado = false;
                for (int i = 0; i < carrito.size(); i++) {
                    ItemCarrito itemExistente = carrito.get(i);

                    if (itemExistente.getIdArticulo() == id) {
                        int cantTotal = itemExistente.getCantidad() + cantNueva;
                        ItemCarrito itemActualizado = new ItemCarrito(id, itemExistente.getNombre(), cantTotal,
                                itemExistente.getPrecioVenta(), itemExistente.getPrecioCompra());
                        carrito.set(i, itemActualizado);

                        cartModel.setValueAt(cantTotal, i, 1); // Columna Cantidad
                        cartModel.setValueAt(itemActualizado.getSubtotalVenta(), i, 2);

                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    Articulo a = articuloDAO.obtenerPorId(id);
                    ItemCarrito nuevoItem = new ItemCarrito(a.getId(), a.getNombre(), cantNueva, a.getPrecioVenta(), a.getPrecioCompra());
                    carrito.add(nuevoItem);
                    cartModel.addRow(new Object[]{nuevoItem.getNombre(), nuevoItem.getCantidad(), nuevoItem.getSubtotalVenta(), "X"});
                }

                artTable.setValueAt(stockDisponible - cantNueva, row, 3);
                actualizarVisualCarrito();
                txtCant.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, ingrese un número válido en cantidad.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // btn eliminar
        cartTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = cartTable.rowAtPoint(e.getPoint());
                int col = cartTable.columnAtPoint(e.getPoint());

                if (row >= 0 && col == 3) {
                    String nombreArticulo = (String) cartModel.getValueAt(row, 0);
                    int cantidadDevuelta = (int) cartModel.getValueAt(row, 1);

                    for (int i = 0; i < artModel.getRowCount(); i++) {
                        if (artModel.getValueAt(i, 1).equals(nombreArticulo)) {
                            int stockActual = (int) artModel.getValueAt(i, 3);
                            artModel.setValueAt(stockActual + cantidadDevuelta, i, 3);
                            break;
                        }
                    }

                    carrito.remove(row);
                    cartModel.removeRow(row);

                    actualizarVisualCarrito();
                }
            }
        });

        btnVender.addActionListener(e -> {
            if (carrito.isEmpty()){
                JOptionPane.showMessageDialog(frame, "El carrito está vacio.");
                return;
            }
            double total = carrito.stream().mapToDouble(ItemCarrito::getSubtotalVenta).sum();
            confirmarVenta(total);
        });

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(artScrollPane, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        cartTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(70);

        frame.setVisible(true);
    }

    private void cargarArticulosEnTabla(DefaultTableModel model, String filtro) {
        model.setRowCount(0);
        try {
            List<Articulo> arts = articuloDAO.listar(filtro);
            for(Articulo a : arts) {
                int stock = InterfazPrincipal.sucursalActual.equals("Oncativo") ? a.getStockOncativo() : a.getStockOliva();
                model.addRow(new Object[]{a.getId(), a.getNombre(), a.getDescripcion(), stock, a.getPrecioVenta()});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void buscarArticulos(DefaultTableModel model, String busqueda) {
        model.setRowCount(0);
        try {
            List<Articulo> arts = articuloDAO.buscar(busqueda, InterfazPrincipal.sucursalActual);
            for(Articulo a : arts) {
                int stock = InterfazPrincipal.sucursalActual.equals("Oncativo") ? a.getStockOncativo() : a.getStockOliva();
                model.addRow(new Object[]{a.getId(), a.getNombre(), a.getDescripcion(), stock, a.getPrecioVenta()});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void configurarEstiloTabla(JTable t) {
        t.setRowHeight(30);
        t.setBackground(new Color(240, 233, 225));
        t.setFont(new Font("Arial", Font.PLAIN, 16));
        t.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
        t.getTableHeader().setBackground(new Color(220, 204, 181));
        t.setSelectionBackground(new Color(180, 140, 90));
        t.getTableHeader().setReorderingAllowed(false);
    }

    private void actualizarVisualCarrito() {
        double total = carrito.stream().mapToDouble(ItemCarrito::getSubtotalVenta).sum();
        lblTotal.setText("Total: $" + String.format("%.0f", total));
    }

    private void confirmarVenta(double totalBruto) {
        JFrame conf = new JFrame("Confirmar Venta");
        conf.setIconImage(App.logo);
        conf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        conf.setSize(800, 650);
        conf.setLocationRelativeTo(null);
        conf.setResizable(false);

        JPanel p = new JPanel(new SpringLayout());
        p.setBackground(new Color(240, 233, 225));
        SpringLayout layout = (SpringLayout) p.getLayout();
        conf.add(p);

        Font fLabel = new Font("Roboto", Font.PLAIN, 30);
        Font fInput = new Font("Arial", Font.PLAIN, 26);

        // COMPONENTES
        JLabel lblC = new JLabel("Nombre cliente:"); lblC.setFont(fLabel);
        JTextField txtCliente = new JTextField(9);
        txtCliente.setFont(fInput);

        JLabel lblM1 = new JLabel("Medio de pago:");
        lblM1.setFont(fLabel);
        JComboBox<String> cbMedio1 = new JComboBox<>(new String[]{"Efectivo", "Transferencia", "Tarjeta de crédito", "Tarjeta de débito", "Cheque", "Dólares", "Cuenta Corriente"});
        cbMedio1.setFont(new Font("Roboto", Font.PLAIN, 24));
        cbMedio1.setBackground(new Color(220, 204, 181));

        JCheckBox chkSegundoMedio = new JCheckBox("Agregar segundo medio de pago");
        chkSegundoMedio.setFont(new Font("Roboto", Font.PLAIN, 20));
        chkSegundoMedio.setOpaque(false);

        JComboBox<String> segundoMedioComboBox = new JComboBox<>(new String[]{"Efectivo", "Transferencia", "Tarjeta de crédito", "Tarjeta de débito", "Cheque", "Dólares", "Cuenta Corriente", "Nota de crédito"});
        segundoMedioComboBox.setFont(new Font("Roboto", Font.PLAIN, 24));
        segundoMedioComboBox.setBackground(new Color(220, 204, 181));
        segundoMedioComboBox.setVisible(false);

        JTextField txtMontoSegundoMedio = new JTextField("0", 5);
        txtMontoSegundoMedio.setFont(fInput);
        txtMontoSegundoMedio.setVisible(false);

        JCheckBox chkDesc15 = new JCheckBox("15% descuento");
        JCheckBox chkDesc20 = new JCheckBox("20% descuento");
        chkDesc15.setFont(new Font("Roboto", Font.PLAIN, 20));
        chkDesc20.setFont(new Font("Roboto", Font.PLAIN, 20));
        chkDesc15.setOpaque(false); chkDesc20.setOpaque(false);

        JLabel lblTotalLabel = new JLabel("Total: $" + String.format("%.2f", totalBruto));
        lblTotalLabel.setFont(new Font("Roboto", Font.BOLD, 32));

        JLabel lblDesglose = new JLabel(" ");
        lblDesglose.setFont(new Font("Roboto", Font.PLAIN, 20));

        JButton btnConfirmarFinal = new JButton("Confirmar");
        btnConfirmarFinal.setBackground(new Color(220, 204, 181));
        btnConfirmarFinal.setPreferredSize(new Dimension(200, 50));
        btnConfirmarFinal.setFont(new Font("Roboto", Font.PLAIN, 24));

        p.add(lblC); p.add(txtCliente);
        p.add(lblM1); p.add(cbMedio1);
        p.add(chkSegundoMedio); p.add(segundoMedioComboBox); p.add(txtMontoSegundoMedio);
        p.add(chkDesc15); p.add(chkDesc20);
        p.add(lblTotalLabel); p.add(lblDesglose);
        p.add(btnConfirmarFinal);

        // POSICIONAMIENTO
        layout.putConstraint(SpringLayout.WEST, lblC, 50, SpringLayout.WEST, p);
        layout.putConstraint(SpringLayout.NORTH, lblC, 30, SpringLayout.NORTH, p);
        layout.putConstraint(SpringLayout.WEST, txtCliente, 320, SpringLayout.WEST, p);
        layout.putConstraint(SpringLayout.NORTH, txtCliente, 30, SpringLayout.NORTH, p);

        layout.putConstraint(SpringLayout.WEST, lblM1, 50, SpringLayout.WEST, p);
        layout.putConstraint(SpringLayout.NORTH, lblM1, 100, SpringLayout.NORTH, p);
        layout.putConstraint(SpringLayout.WEST, cbMedio1, 320, SpringLayout.WEST, p);
        layout.putConstraint(SpringLayout.NORTH, cbMedio1, 100, SpringLayout.NORTH, p);

        // Más separación para el segundo medio
        layout.putConstraint(SpringLayout.WEST, chkSegundoMedio, 320, SpringLayout.WEST, p);
        layout.putConstraint(SpringLayout.NORTH, chkSegundoMedio, 170, SpringLayout.NORTH, p);

        layout.putConstraint(SpringLayout.WEST, segundoMedioComboBox, 320, SpringLayout.WEST, p);
        layout.putConstraint(SpringLayout.NORTH, segundoMedioComboBox, 220, SpringLayout.NORTH, p); // 50px de separación
        layout.putConstraint(SpringLayout.WEST, txtMontoSegundoMedio, 580, SpringLayout.WEST, p);
        layout.putConstraint(SpringLayout.NORTH, txtMontoSegundoMedio, 220, SpringLayout.NORTH, p);

        layout.putConstraint(SpringLayout.WEST, chkDesc15, 250, SpringLayout.WEST, p);
        layout.putConstraint(SpringLayout.NORTH, chkDesc15, 300, SpringLayout.NORTH, p);
        layout.putConstraint(SpringLayout.WEST, chkDesc20, 450, SpringLayout.WEST, p);
        layout.putConstraint(SpringLayout.NORTH, chkDesc20, 300, SpringLayout.NORTH, p);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, lblDesglose, 0, SpringLayout.HORIZONTAL_CENTER, p);
        layout.putConstraint(SpringLayout.NORTH, lblDesglose, 360, SpringLayout.NORTH, p);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, lblTotalLabel, 0, SpringLayout.HORIZONTAL_CENTER, p);
        layout.putConstraint(SpringLayout.NORTH, lblTotalLabel, 410, SpringLayout.NORTH, p);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnConfirmarFinal, 0, SpringLayout.HORIZONTAL_CENTER, p);
        layout.putConstraint(SpringLayout.NORTH, btnConfirmarFinal, 500, SpringLayout.NORTH, p);

        // LÓGICA DE CÁLCULO ACTUALIZADA
        final double[] totalFinalPersistente = {totalBruto};

        Runnable calcularTotalAction = () -> {
            double montoNC = 0;
            if (chkSegundoMedio.isSelected() && "Nota de crédito".equals(segundoMedioComboBox.getSelectedItem())) {
                try {
                    montoNC = Double.parseDouble(txtMontoSegundoMedio.getText().trim());
                } catch (Exception ignored) {
                }
            }

            double saldoPostNC = totalBruto - montoNC;
            if (saldoPostNC < 0) saldoPostNC = 0;

            double descCalculado = 0;
            if (chkDesc15.isSelected()) descCalculado = saldoPostNC * 0.15;
            else if (chkDesc20.isSelected()) descCalculado = saldoPostNC * 0.20;

            double totalCalculado = saldoPostNC - descCalculado;
            if (totalCalculado < 0) totalCalculado = 0;

            totalFinalPersistente[0] = totalCalculado;
            lblTotalLabel.setText("Total: $" + String.format("%.2f", totalCalculado));
            lblDesglose.setText(String.format("Nota Crédito: $%.2f | Descuento aplicado: $%.2f", montoNC, descCalculado));
        };

        // EVENTOS
        chkSegundoMedio.addActionListener(e -> {
            boolean s = chkSegundoMedio.isSelected();
            segundoMedioComboBox.setVisible(s);
            txtMontoSegundoMedio.setVisible(s);
            calcularTotalAction.run();
        });

        chkDesc15.addActionListener(e -> { if(chkDesc15.isSelected()) chkDesc20.setSelected(false); calcularTotalAction.run(); });
        chkDesc20.addActionListener(e -> { if(chkDesc20.isSelected()) chkDesc15.setSelected(false); calcularTotalAction.run(); });
        segundoMedioComboBox.addActionListener(e -> calcularTotalAction.run());

        // DocumentListener para que el total cambie mientras escribís el monto de la NC
        txtMontoSegundoMedio.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calcularTotalAction.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calcularTotalAction.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calcularTotalAction.run(); }
        });

        btnConfirmarFinal.addActionListener(e -> {
            String cliente = txtCliente.getText().trim();
            if (cliente.isEmpty()) {
                JOptionPane.showMessageDialog(conf, "Ingresá el nombre del cliente.");
                return;
            }

            try {
                String m1 = (String) cbMedio1.getSelectedItem();
                String m2 = chkSegundoMedio.isSelected() ? (String) segundoMedioComboBox.getSelectedItem() : null;

                // MONTO 2: Es el valor nominal que el usuario escribió (ej: la Nota de Crédito)
                double monto2 = chkSegundoMedio.isSelected() ? Double.parseDouble(txtMontoSegundoMedio.getText().trim()) : 0;

                // MONTO 1: Es lo que el cliente efectivamente paga con el medio principal
                double monto1 = totalFinalPersistente[0];

                if (ventaDAO.procesarVenta(cliente, carrito, monto1, m1, monto2, m2, InterfazPrincipal.sucursalActual, App.fechaActual)) {
                    JOptionPane.showMessageDialog(conf, "¡Venta registrada con éxito!");
                    conf.dispose();
                    frame.dispose();
                    this.carrito.clear();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(conf, "El monto del segundo medio no es un número válido.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(conf, "Error al procesar: " + ex.getMessage());
            }
        });

        conf.setVisible(true);
    }
}
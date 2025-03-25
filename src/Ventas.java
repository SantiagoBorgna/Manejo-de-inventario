import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ventas {

    public static void abrirInterfazVenta(String sucursalActual) {

        // Crear ventana de venta
        JFrame ventaFrame = new JFrame("Venta - Sucursal " + sucursalActual);
        ventaFrame.setIconImage(App.logo);
        ventaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventaFrame.setLayout(new BorderLayout());
        ventaFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        ventaFrame.setMinimumSize(new Dimension(1400, 800));

        // Panel superior (barra de búsqueda)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(new Color(220, 204, 181));
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton searchButton = new JButton("Buscar");
        searchButton.setPreferredSize(new Dimension(130, 60));
        searchButton.setBackground(new Color(240, 233, 225));
        searchButton.setFont(new Font("Roboto", Font.PLAIN, 24));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Tabla de artículos
        String[] columnNames = {"Nombre", "Descripción", "Stock", "Precio Unitario"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable articleTable = new JTable(tableModel);
        articleTable.setRowHeight(25);
        articleTable.setBackground(new Color(240, 233, 225));
        articleTable.setFont(new Font("Arial", Font.PLAIN, 14));
        articleTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        articleTable.getTableHeader().setBackground(new Color(220, 204, 181));
        articleTable.getTableHeader().setForeground(Color.BLACK);
        JScrollPane tableScrollPane = new JScrollPane(articleTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        // Panel derecho (carrito de compras)
        JPanel resumenPanel = new JPanel(new BorderLayout());
        resumenPanel.setBackground(new Color(240, 233, 225));
        resumenPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));
        String[] resumenColumnNames = {"Artículo", "Cantidad", "Subtotal"};
        DefaultTableModel resumenModel = new DefaultTableModel(resumenColumnNames, 0);
        JTable resumenTable = new JTable(resumenModel);
        resumenTable.setRowHeight(25);
        resumenTable.setBackground(new Color(240, 233, 225));
        resumenTable.setFont(new Font("Arial", Font.PLAIN, 14));
        resumenTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        resumenTable.getTableHeader().setBackground(new Color(220, 204, 181));
        resumenTable.getTableHeader().setForeground(Color.BLACK);
        JScrollPane resumenScrollPane = new JScrollPane(resumenTable);
        resumenScrollPane.setPreferredSize(new Dimension(300, 600));

        //Panel de cantidad
        JPanel cantidadPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        cantidadPanel.setPreferredSize(new Dimension(300,50));
        cantidadPanel.setBackground(new Color(220, 204, 181));

        JButton addToCartButton = new JButton("Añadir al carrito");
        addToCartButton.setPreferredSize(new Dimension(150, 40));
        addToCartButton.setBackground(new Color(240, 233, 225));
        addToCartButton.setFont(new Font("Roboto", Font.PLAIN, 16));

        JTextField quantityField = new JTextField(3);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel cantidadLabel = new JLabel("Cantidad:");
        cantidadLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
        cantidadPanel.add(cantidadLabel);
        cantidadPanel.add(quantityField);
        cantidadPanel.add(addToCartButton);

        resumenPanel.add(resumenScrollPane, BorderLayout.NORTH);
        resumenPanel.add(cantidadPanel, BorderLayout.SOUTH);

        // Panel inferior (cantidad y botón de añadir al carrito)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 5));
        bottomPanel.setBackground(new Color(220, 204, 181));
        bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));
        JLabel totalLabel = new JLabel("Total: $0");
        totalLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
        JButton venderButton = new JButton("Vender");
        venderButton.setPreferredSize(new Dimension(90, 45));
        venderButton.setBackground(new Color(240, 233, 225));
        venderButton.setFont(new Font("Roboto", Font.PLAIN, 16));
        bottomPanel.add(totalLabel);
        bottomPanel.add(venderButton);

        // Agregar componentes a la ventana
        ventaFrame.add(topPanel, BorderLayout.NORTH);
        ventaFrame.add(tableScrollPane, BorderLayout.CENTER);
        ventaFrame.add(resumenPanel, BorderLayout.EAST);
        ventaFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        searchButton.addActionListener(e -> {
            buscarArticulo(searchField.getText(), tableModel);
            searchField.setText("");
        });
        addToCartButton.addActionListener(e -> {
            agregarAlCarrito(articleTable, quantityField, resumenModel, totalLabel);
            quantityField.setText("");
        });
        venderButton.addActionListener(e -> {
            String texto = totalLabel.getText();
            String numeroStr = texto.replace("Total: $", "").trim();
            double montoTotal = Double.parseDouble(numeroStr);
            confirmarVenta(montoTotal, ventaFrame, resumenModel, sucursalActual);
        });

        // Mostrar la ventana
        ventaFrame.setVisible(true);
    }

    private static void confirmarVenta(double montoTotal, JFrame ventaFrame, DefaultTableModel resumenModel, String sucursalActual){

        JFrame resumenVentaFrame = new JFrame("Confirmar venta");
        resumenVentaFrame.setIconImage(App.logo);
        resumenVentaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resumenVentaFrame.setLayout(new BorderLayout());
        resumenVentaFrame.setLocation(370, 150);
        resumenVentaFrame.setSize(800, 470);

        // Poner todo en un panel
        JPanel panelVenta = new JPanel();
        panelVenta.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelVenta.setLayout(layout);
        resumenVentaFrame.add(panelVenta, BorderLayout.CENTER);

        //Nombre cliente
        JLabel labelNombre = new JLabel("Nombre cliente:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelVenta.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelVenta.add(txtNombre);

        //Medio de pago
        JLabel labelPago = new JLabel("Medio de pago:");
        labelPago.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelVenta.add(labelPago);
        JComboBox<String> medioDePagoComboBox = new JComboBox<>(new String[]{"Efectivo", "Transferencia", "Tarjeta de crédito", "Tarjeta de débito", "Cheque", "Dólares"});
        medioDePagoComboBox.setPreferredSize(new Dimension(250, 40));
        medioDePagoComboBox.setBackground(new Color(220, 204, 181));
        medioDePagoComboBox.setFont(new Font("Roboto", Font.PLAIN, 24));
        panelVenta.add(medioDePagoComboBox);

        //Nombre cliente
        JLabel labelTotal = new JLabel("Total: $" + montoTotal);
        labelTotal.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelVenta.add(labelTotal);

        //Boton confirmar
        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setPreferredSize(new Dimension(200, 80));
        btnConfirmar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnConfirmar.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnConfirmar.setMargin(new Insets(10, 12, 10, 12));
        panelVenta.add(btnConfirmar);

        //Acomodar componentes
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 20, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 20, SpringLayout.NORTH, container);
        //Medio de pago
        layout.putConstraint(SpringLayout.WEST, labelPago, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPago, 120, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, medioDePagoComboBox, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, medioDePagoComboBox, 120, SpringLayout.NORTH, container);
        //Monto total
        layout.putConstraint(SpringLayout.WEST, labelTotal, 340, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelTotal, 220, SpringLayout.NORTH, container);
        //btn confirmar
        layout.putConstraint(SpringLayout.WEST, btnConfirmar, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnConfirmar, 320, SpringLayout.NORTH, container);

        String articulos = "";

        for (int i = 0; i < resumenModel.getRowCount(); i++) {
            String nombre = (String) resumenModel.getValueAt(i, 0);
            int cantidad = (int) resumenModel.getValueAt(i, 1);

            articulos = articulos + cantidad + " " + nombre + " \n";
        }

        String articulosVenta = articulos;

        btnConfirmar.addActionListener(e -> {
            String nombreCliente = txtNombre.getText();
            String pago = (String) medioDePagoComboBox.getSelectedItem();
            finalizarVenta(ventaFrame, resumenModel, sucursalActual);
            resumenVentaFrame.setVisible(false);
            registrarVenta(sucursalActual, nombreCliente, articulosVenta, montoTotal, pago);
        });

        resumenVentaFrame.setVisible(true);
    }

    private static void buscarArticulo(String busqueda, DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)){
            String query;

            if (InterfazPrincipal.sucursalActual == "Oncativo"){
                query = "SELECT nombreArticulo, descripcionArticulo, cant1Articulo, precioVentaArticulo FROM articulos WHERE nombreArticulo LIKE ? OR idArticulo LIKE ?";
            } else if (InterfazPrincipal.sucursalActual == "Oliva"){
                query = "SELECT nombreArticulo, descripcionArticulo, cant2Articulo, precioVentaArticulo FROM articulos WHERE nombreArticulo LIKE ? OR idArticulo LIKE ?";
            } else {
                query = "SELECT nombreArticulo, descripcionArticulo, cant3Articulo, precioVentaArticulo FROM articulos WHERE nombreArticulo LIKE ? OR idArticulo LIKE ?";
            }

            tableModel.setRowCount(0); // Limpiar la tabla
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + busqueda + "%");
            statement.setString(2, "%" + busqueda + "%");
            ResultSet resultSet = statement.executeQuery();

            if (InterfazPrincipal.sucursalActual == "Oncativo"){
                while (resultSet.next()) {
                    tableModel.addRow(new Object[]{
                            resultSet.getString("nombreArticulo"),
                            resultSet.getString("descripcionArticulo"),
                            resultSet.getInt("cant1Articulo"),
                            resultSet.getDouble("precioVentaArticulo")
                    });
                }
            } else if (InterfazPrincipal.sucursalActual == "Oliva"){
                while (resultSet.next()) {
                    tableModel.addRow(new Object[]{
                            resultSet.getString("nombreArticulo"),
                            resultSet.getString("descripcionArticulo"),
                            resultSet.getInt("cant2Articulo"),
                            resultSet.getDouble("precioVentaArticulo")
                    });
                }
            } else {
                while (resultSet.next()) {
                    tableModel.addRow(new Object[]{
                            resultSet.getString("nombreArticulo"),
                            resultSet.getString("descripcionArticulo"),
                            resultSet.getInt("cant3Articulo"),
                            resultSet.getDouble("precioVentaArticulo")
                    });
                }
            }


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar artículo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void agregarAlCarrito(JTable articleTable, JTextField quantityField, DefaultTableModel resumenModel, JLabel totalLabel) {
        int selectedRow = articleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un artículo primero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cantidad = Integer.parseInt(quantityField.getText());
            int stock = (int) articleTable.getValueAt(selectedRow, 2);

            if (cantidad > stock) {
                JOptionPane.showMessageDialog(null, "No puedes vender más de lo que hay en stock.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nombre = (String) articleTable.getValueAt(selectedRow, 0);
            double precio = (double) articleTable.getValueAt(selectedRow, 3);
            double subtotal = cantidad * precio;

            // Actualizar stock en la tabla principal
            articleTable.setValueAt(stock - cantidad, selectedRow, 2);

            // Agregar al resumen
            resumenModel.addRow(new Object[]{nombre, cantidad, subtotal});

            // Actualizar total
            double total = 0;
            for (int i = 0; i < resumenModel.getRowCount(); i++) {
                total += (double) resumenModel.getValueAt(i, 2);
            }
            totalLabel.setText("Total: $" + total);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void finalizarVenta(JFrame ventaFrame, DefaultTableModel resumenModel, String sucursalActual) {

        String query;

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            connection.setAutoCommit(false);

            for (int i = 0; i < resumenModel.getRowCount(); i++) {
                String nombre = (String) resumenModel.getValueAt(i, 0);
                int cantidad = (int) resumenModel.getValueAt(i, 1);

                if (sucursalActual == "Oncativo"){
                    query = "UPDATE articulos SET cant1Articulo = cant1Articulo - ? WHERE nombreArticulo = ?";
                } else if (sucursalActual == "Oliva"){
                    query = "UPDATE articulos SET cant2Articulo = cant2Articulo - ? WHERE nombreArticulo = ?";
                } else {
                    query = "UPDATE articulos SET cant3Articulo = cant3Articulo - ? WHERE nombreArticulo = ?";
                }

                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, cantidad);
                stmt.setString(2, nombre);
                stmt.executeUpdate();
            }

            connection.commit();
            JOptionPane.showMessageDialog(null, "Venta realizada con éxito.");

            ventaFrame.dispose();

            switch (Inicio.rol) {
                case "superadmin": //victoria
                    Articulos.actualizarTabla();
                    break;
                case "admin": //admin oliva
                    Articulos.actualizarTabla2();
                    break;
                case "empleado1": //empleado el arca
                    Articulos.actualizarTabla3();
                    break;
                case "empleado2": //empleado el rincon
                    Articulos.actualizarTabla4();
                    break;
            }

        } catch (SQLException e) {
            try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                JOptionPane.showMessageDialog(null, "Error al realizar el rollback: " + rollbackEx.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Error al finalizar la venta: " + e.getMessage());
        }
    }

    private static void registrarVenta(String sucursal, String clienteVenta, String articulosVenta, double montoVenta, String pagoVenta) {

        String query = "INSERT INTO libro_diario (fechaVenta, sucursalVenta, clienteVenta, articulosVenta, montoVenta, medioDePagoVenta) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, new SimpleDateFormat("dd-MM-yyyy").format(new Date())); // Fecha actual
            statement.setString(2, sucursal);
            statement.setString(3, clienteVenta);
            statement.setString(4, articulosVenta);
            statement.setDouble(5, montoVenta);
            statement.setString(6, pagoVenta);

            statement.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar venta en el libro diario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

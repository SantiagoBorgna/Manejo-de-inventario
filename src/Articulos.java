import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Articulos {

    private static JTextArea detalleArticulo1;
    private static JTextArea detalleArticulo2;
    private static JTextArea detalleArticulo3;

    private static JLabel lblImagenArticulo = new JLabel();

    public static void loadDataFromSQLite(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Limpia el modelo antes de cargar nuevos datos

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            System.out.println("Conexión exitosa a SQLite.");

            String query = "SELECT nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, cant3Articulo, precioCompraArticulo, precioVentaArticulo FROM articulos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("Consulta ejecutada con éxito.");

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant1 = resultSet.getInt("cant1Articulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                int cant3 = resultSet.getInt("cant3Articulo");
                int total = cant1 + cant2 + cant3;
                double precioCompra = resultSet.getDouble("precioCompraArticulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");
                double ganancia = precioVenta - precioCompra;

                tableModel.addRow(new Object[]{nombre, descripcion, cant1, cant2, cant3, total, precioCompra, precioVenta, ganancia});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de SQLite", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void loadDataFromSQLite2(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Limpia el modelo antes de cargar nuevos datos

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            System.out.println("Conexión exitosa a SQLite.");

            String query = "SELECT nombreArticulo, descripcionArticulo, cant2Articulo, precioCompraArticulo, precioVentaArticulo FROM articulos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("Consulta ejecutada con éxito.");

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                double precioCompra = resultSet.getDouble("precioCompraArticulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");
                double ganancia = precioVenta - precioCompra;

                tableModel.addRow(new Object[]{nombre, descripcion, cant2, precioCompra, precioVenta, ganancia});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de SQLite", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void loadDataFromSQLite3(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Limpia el modelo antes de cargar nuevos datos

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            System.out.println("Conexión exitosa a SQLite.");

            String query = "SELECT nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, precioVentaArticulo FROM articulos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("Consulta ejecutada con éxito.");

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant1 = resultSet.getInt("cant1Articulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                int total = cant1 + cant2;
                double precioVenta = resultSet.getDouble("precioVentaArticulo");

                tableModel.addRow(new Object[]{nombre, descripcion, cant1, cant2, total, precioVenta});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de SQLite", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void loadDataFromSQLite4(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Limpia el modelo antes de cargar nuevos datos

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            System.out.println("Conexión exitosa a SQLite.");

            String query = "SELECT nombreArticulo, descripcionArticulo, cant3Articulo, precioVentaArticulo FROM articulos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("Consulta ejecutada con éxito.");

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant3 = resultSet.getInt("cant3Articulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");

                tableModel.addRow(new Object[]{nombre, descripcion, cant3, precioVenta});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de SQLite", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void actualizarTabla() {
        DefaultTableModel tableModel = (DefaultTableModel) App.listTable.getModel();
        tableModel.setRowCount(0); // Limpia las filas actuales

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            String query = "SELECT nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, cant3Articulo, precioCompraArticulo, precioVentaArticulo FROM articulos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant1 = resultSet.getInt("cant1Articulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                int cant3 = resultSet.getInt("cant3Articulo");
                int total = cant1 + cant2 + cant3;
                double precioCompra = resultSet.getDouble("precioCompraArticulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");
                double ganancia = precioVenta - precioCompra;

                tableModel.addRow(new Object[]{nombre, descripcion, cant1, cant2, cant3, total, precioCompra, precioVenta, ganancia});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar la tabla", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void actualizarTabla2() {
        DefaultTableModel tableModel = (DefaultTableModel) App.listTable.getModel();
        tableModel.setRowCount(0); // Limpia las filas actuales

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            String query = "SELECT nombreArticulo, descripcionArticulo, cant2Articulo, precioCompraArticulo, precioVentaArticulo FROM articulos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                double precioCompra = resultSet.getDouble("precioCompraArticulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");
                double ganancia = precioVenta - precioCompra;

                tableModel.addRow(new Object[]{nombre, descripcion, cant2, precioCompra, precioVenta, ganancia});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar la tabla", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void actualizarTabla3() {
        DefaultTableModel tableModel = (DefaultTableModel) App.listTable.getModel();
        tableModel.setRowCount(0); // Limpia las filas actuales

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            String query = "SELECT nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, precioVentaArticulo FROM articulos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant1 = resultSet.getInt("cant1Articulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                int total = cant1 + cant2;
                double precioVenta = resultSet.getDouble("precioVentaArticulo");

                tableModel.addRow(new Object[]{nombre, descripcion, cant1, cant2, total, precioVenta});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar la tabla", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void actualizarTabla4() {
        DefaultTableModel tableModel = (DefaultTableModel) App.listTable.getModel();
        tableModel.setRowCount(0); // Limpia las filas actuales

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            String query = "SELECT nombreArticulo, descripcionArticulo, cant3Articulo, precioVentaArticulo FROM articulos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant3 = resultSet.getInt("cant3Articulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");

                tableModel.addRow(new Object[]{nombre, descripcion, cant3, precioVenta});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar la tabla", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void filtrarArticulos(String busqueda, DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {

            tableModel.setRowCount(0); // Limpiar la tabla antes de agregar nuevas filas

            String query = "SELECT nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, cant3Articulo, precioCompraArticulo, precioVentaArticulo FROM articulos WHERE nombreArticulo LIKE ? OR idArticulo LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + busqueda + "%");
            statement.setString(2, "%" + busqueda + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant1 = resultSet.getInt("cant1Articulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                int cant3 = resultSet.getInt("cant3Articulo");
                int total = cant1 + cant2 + cant3;
                double precioCompra = resultSet.getDouble("precioCompraArticulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");
                double ganancia = precioVenta - precioCompra;

                tableModel.addRow(new Object[]{nombre, descripcion, cant1, cant2, cant3, total, precioCompra, precioVenta, ganancia});
            }

            // Resetear selección después de actualizar los datos
            App.listTable.clearSelection();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar artículos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void filtrarArticulos2(String busqueda, DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {

            tableModel.setRowCount(0); // Limpiar la tabla antes de agregar nuevas filas

            String query = "SELECT nombreArticulo, descripcionArticulo, cant2Articulo, precioCompraArticulo, precioVentaArticulo FROM articulos WHERE nombreArticulo LIKE ? OR idArticulo LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + busqueda + "%");
            statement.setString(2, "%" + busqueda + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                double precioCompra = resultSet.getDouble("precioCompraArticulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");
                double ganancia = precioVenta - precioCompra;

                tableModel.addRow(new Object[]{nombre, descripcion, cant2, precioCompra, precioVenta, ganancia});
            }

            // Resetear selección después de actualizar los datos
            App.listTable.clearSelection();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar artículos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void filtrarArticulos3(String busqueda, DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {

            tableModel.setRowCount(0); // Limpiar la tabla antes de agregar nuevas filas

            String query = "SELECT nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, precioVentaArticulo FROM articulos WHERE nombreArticulo LIKE ? OR idArticulo LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + busqueda + "%");
            statement.setString(2, "%" + busqueda + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant1 = resultSet.getInt("cant1Articulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                int total = cant1 + cant2;
                double precioVenta = resultSet.getDouble("precioVentaArticulo");

                tableModel.addRow(new Object[]{nombre, descripcion, cant1, cant2, total, precioVenta});
            }

            // Resetear selección después de actualizar los datos
            App.listTable.clearSelection();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar artículos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void filtrarArticulos4(String busqueda, DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {

            tableModel.setRowCount(0); // Limpiar la tabla antes de agregar nuevas filas

            String query = "SELECT nombreArticulo, descripcionArticulo, cant3Articulo, precioVentaArticulo FROM articulos WHERE nombreArticulo LIKE ? OR idArticulo LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + busqueda + "%");
            statement.setString(2, "%" + busqueda + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant3 = resultSet.getInt("cant3Articulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");

                tableModel.addRow(new Object[]{nombre, descripcion, cant3, precioVenta});
            }

            // Resetear selección después de actualizar los datos
            App.listTable.clearSelection();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar artículos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void abrirFormularioNuevoArticulo() {

        JFrame formulario = new JFrame("Nuevo Artículo");
        formulario.setIconImage(App.logo);
        formulario.setLocation(220, 0);
        formulario.setSize(1100, 850);
        formulario.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formulario.setLayout(new BorderLayout());
        formulario.setResizable(false);

        // Poner todo en un panel
        JPanel panelNuevoArticulo = new JPanel();
        panelNuevoArticulo.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelNuevoArticulo.setLayout(layout);
        formulario.add(panelNuevoArticulo, BorderLayout.CENTER);


        // Componentes del formulario
        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtNombre);

        JLabel labelDescripcion = new JLabel("Descripcion:");
        labelDescripcion.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelDescripcion);
        JTextField txtDescripcion = new JTextField();
        txtDescripcion.setPreferredSize(new Dimension(250, 40));
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtDescripcion);

        JLabel labelCant1 = new JLabel("Cantidad Oncativo:");
        labelCant1.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant1);
        JTextField txtCant1 = new JTextField();
        txtCant1.setPreferredSize(new Dimension(250, 40));
        txtCant1.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant1);

        JLabel labelCant2 = new JLabel("Cantidad Oliva:");
        labelCant2.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant2);
        JTextField txtCant2 = new JTextField();
        txtCant2.setPreferredSize(new Dimension(250, 40));
        txtCant2.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant2);

        JLabel labelCant3 = new JLabel("Cantidad El rincon:");
        labelCant3.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant3);
        JTextField txtCant3 = new JTextField();
        txtCant3.setPreferredSize(new Dimension(250, 40));
        txtCant3.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant3);

        JLabel labelPrecioCompra = new JLabel("Precio de compra:");
        labelPrecioCompra.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioCompra);
        JTextField txtPrecioCompra = new JTextField();
        txtPrecioCompra.setPreferredSize(new Dimension(250, 40));
        txtPrecioCompra.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioCompra);

        JLabel labelPrecioVenta = new JLabel("Precio de venta:");
        labelPrecioVenta.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioVenta);
        JTextField txtPrecioVenta = new JTextField();
        txtPrecioVenta.setPreferredSize(new Dimension(250, 40));
        txtPrecioVenta.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioVenta);

        JLabel labelProveedor = new JLabel("Proveedor:");
        labelProveedor.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelProveedor);
        JTextField txtProveedor = new JTextField();
        txtProveedor.setPreferredSize(new Dimension(250, 40));
        txtProveedor.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtProveedor);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(160, 80));
        btnGuardar.setBackground(new Color(220, 204, 181));
        btnGuardar.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnGuardar.setMargin(new Insets(10, 12, 10, 12));
        panelNuevoArticulo.add(btnGuardar);

        JButton btnAgregarImagenes = new JButton("Agregar\n Imágenes");
        btnAgregarImagenes.setPreferredSize(new Dimension(240, 80));
        btnAgregarImagenes.setBackground(new Color(220, 204, 181));
        btnAgregarImagenes.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnAgregarImagenes.setMargin(new Insets(10, 6, 10, 6));
        panelNuevoArticulo.add(btnAgregarImagenes);

        // Lista para almacenar las imágenes seleccionadas
        ArrayList<String> rutasImg = new ArrayList<>();

        // Etiquetas para mostrar las imágenes seleccionadas
        JLabel[] imagenLabels = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            imagenLabels[i] = new JLabel();
            imagenLabels[i].setPreferredSize(new Dimension(120, 120));
            imagenLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panelNuevoArticulo.add(imagenLabels[i]);
        }

        //Acomodar componentes
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 20, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 20, SpringLayout.NORTH, container);
        //Descripcion
        layout.putConstraint(SpringLayout.WEST, labelDescripcion, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelDescripcion, 100, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtDescripcion, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtDescripcion, 100, SpringLayout.NORTH, container);
        //Cant1
        layout.putConstraint(SpringLayout.WEST, labelCant1, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant1, 180, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant1, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant1, 180, SpringLayout.NORTH, container);
        //Cant2
        layout.putConstraint(SpringLayout.WEST, labelCant2, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant2, 260, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant2, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant2, 260, SpringLayout.NORTH, container);
        //Cant3
        layout.putConstraint(SpringLayout.WEST, labelCant3, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant3, 340, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant3, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant3, 340, SpringLayout.NORTH, container);
        //precio de compra
        layout.putConstraint(SpringLayout.WEST, labelPrecioCompra, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPrecioCompra, 420, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPrecioCompra, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPrecioCompra, 420, SpringLayout.NORTH, container);
        //precio de venta
        layout.putConstraint(SpringLayout.WEST, labelPrecioVenta, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPrecioVenta, 500, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPrecioVenta, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPrecioVenta, 500, SpringLayout.NORTH, container);
        //proveedor
        layout.putConstraint(SpringLayout.WEST, labelProveedor, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelProveedor, 580, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtProveedor, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtProveedor, 580, SpringLayout.NORTH, container);
        //btn agregar imagenes
        layout.putConstraint(SpringLayout.WEST, btnAgregarImagenes, 760, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnAgregarImagenes, 20, SpringLayout.NORTH, container);
        // previsualizar imagenes
        for (int i = 0; i < 4; i++) {
            layout.putConstraint(SpringLayout.WEST, imagenLabels[i], 826, SpringLayout.WEST, panelNuevoArticulo);
            layout.putConstraint(SpringLayout.NORTH, imagenLabels[i], 120 + (i * 140), SpringLayout.NORTH, panelNuevoArticulo);
        }
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnGuardar, 490, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnGuardar, 660, SpringLayout.NORTH, container);


        // Acción del botón para seleccionar imágenes
        btnAgregarImagenes.addActionListener(e -> {
            //Hacer linda la ventana
            LookAndFeel previousLF = UIManager.getLookAndFeel();

            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Para Windows
                SwingUtilities.updateComponentTreeUI(formulario);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar Imágenes");
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);

            // Establecer vista de iconos en vez de lista detallada (opcional)
            int seleccion = fileChooser.showOpenDialog(formulario);

            //Volver a estilo anterior
            try {
                UIManager.setLookAndFeel(previousLF);
                SwingUtilities.updateComponentTreeUI(formulario);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File[] archivosSeleccionados = fileChooser.getSelectedFiles();

                for (File archivo : archivosSeleccionados) {
                    if (rutasImg.size() < 4) {
                        try {
                            // Crear carpeta si no existe
                            File directorio = new File("imagenes_articulos");
                            if (!directorio.exists()) {
                                directorio.mkdir();
                            }

                            // Copiar archivo a la carpeta del proyecto
                            File destino = new File(directorio, archivo.getName());
                            Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            // Guardar la ruta relativa en la lista
                            rutasImg.add(destino.getPath());

                            // Mostrar miniatura
                            ImageIcon icon = new ImageIcon(new ImageIcon(destino.getAbsolutePath()).getImage()
                                    .getScaledInstance(120, 120, Image.SCALE_SMOOTH));
                            imagenLabels[rutasImg.size() - 1].setIcon(icon);

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(formulario, "Error al copiar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(formulario, "Solo puedes agregar hasta 4 imágenes.", "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                }
            }
        });

        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String descripcion = txtDescripcion.getText();
                int cant1 = Integer.parseInt(txtCant1.getText());
                int cant2 = Integer.parseInt(txtCant2.getText());
                int cant3 = Integer.parseInt(txtCant3.getText());
                double precioCompra = Double.parseDouble(txtPrecioCompra.getText());
                double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
                String proveedor = txtProveedor.getText();
                String ruta1 = rutasImg.size() > 0 ? rutasImg.get(0) : "";
                String ruta2 = rutasImg.size() > 1 ? rutasImg.get(1) : "";
                String ruta3 = rutasImg.size() > 2 ? rutasImg.get(2) : "";
                String ruta4 = rutasImg.size() > 3 ? rutasImg.get(3) : "";

                insertarArticulo(nombre, descripcion, cant1, cant2, cant3, precioCompra, precioVenta, proveedor, ruta1, ruta2, ruta3, ruta4);
                JOptionPane.showMessageDialog(formulario, "Artículo guardado correctamente.");
                formulario.dispose();
                actualizarTabla();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(formulario, "Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulario.setVisible(true);
    }

    public static void abrirFormularioNuevoArticulo2() {

        JFrame formulario = new JFrame("Nuevo Artículo");
        formulario.setIconImage(App.logo);
        formulario.setLocation(220, 40);
        formulario.setSize(1100, 750);
        formulario.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formulario.setLayout(new BorderLayout());
        formulario.setResizable(false);

        // Poner todo en un panel
        JPanel panelNuevoArticulo = new JPanel();
        panelNuevoArticulo.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelNuevoArticulo.setLayout(layout);
        formulario.add(panelNuevoArticulo, BorderLayout.CENTER);


        // Componentes del formulario
        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtNombre);

        JLabel labelDescripcion = new JLabel("Descripcion:");
        labelDescripcion.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelDescripcion);
        JTextField txtDescripcion = new JTextField();
        txtDescripcion.setPreferredSize(new Dimension(250, 40));
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtDescripcion);

        JLabel labelCant1 = new JLabel("Cantidad Oncativo:");
        labelCant1.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant1);
        JTextField txtCant1 = new JTextField();
        txtCant1.setPreferredSize(new Dimension(250, 40));
        txtCant1.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant1);

        JLabel labelCant2 = new JLabel("Cantidad Oliva:");
        labelCant2.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant2);
        JTextField txtCant2 = new JTextField();
        txtCant2.setPreferredSize(new Dimension(250, 40));
        txtCant2.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant2);

        JLabel labelPrecioCompra = new JLabel("Precio de compra:");
        labelPrecioCompra.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioCompra);
        JTextField txtPrecioCompra = new JTextField();
        txtPrecioCompra.setPreferredSize(new Dimension(250, 40));
        txtPrecioCompra.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioCompra);

        JLabel labelPrecioVenta = new JLabel("Precio de venta:");
        labelPrecioVenta.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioVenta);
        JTextField txtPrecioVenta = new JTextField();
        txtPrecioVenta.setPreferredSize(new Dimension(250, 40));
        txtPrecioVenta.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioVenta);

        JLabel labelProveedor = new JLabel("Proveedor:");
        labelProveedor.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelProveedor);
        JTextField txtProveedor = new JTextField();
        txtProveedor.setPreferredSize(new Dimension(250, 40));
        txtProveedor.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtProveedor);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(200, 80));
        btnGuardar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnGuardar.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnGuardar.setMargin(new Insets(10, 12, 10, 12));
        panelNuevoArticulo.add(btnGuardar);

        JButton btnAgregarImagenes = new JButton("Agregar\n Imágenes");
        btnAgregarImagenes.setPreferredSize(new Dimension(240, 80));
        btnAgregarImagenes.setBackground(new Color(220, 204, 181));
        btnAgregarImagenes.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnAgregarImagenes.setMargin(new Insets(10, 6, 10, 6));
        panelNuevoArticulo.add(btnAgregarImagenes);

        // Lista para almacenar las imágenes seleccionadas
        ArrayList<String> rutasImg = new ArrayList<>();

        // Etiquetas para mostrar las imágenes seleccionadas
        JLabel[] imagenLabels = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            imagenLabels[i] = new JLabel();
            imagenLabels[i].setPreferredSize(new Dimension(120, 120));
            imagenLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panelNuevoArticulo.add(imagenLabels[i]);
        }

        //Acomodar componentes
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 20, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 20, SpringLayout.NORTH, container);
        //Descripcion
        layout.putConstraint(SpringLayout.WEST, labelDescripcion, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelDescripcion, 100, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtDescripcion, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtDescripcion, 100, SpringLayout.NORTH, container);
        //Cant1
        layout.putConstraint(SpringLayout.WEST, labelCant1, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant1, 180, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant1, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant1, 180, SpringLayout.NORTH, container);
        //Cant2
        layout.putConstraint(SpringLayout.WEST, labelCant2, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant2, 260, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant2, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant2, 260, SpringLayout.NORTH, container);
        //precio de compra
        layout.putConstraint(SpringLayout.WEST, labelPrecioCompra, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPrecioCompra, 340, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPrecioCompra, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPrecioCompra, 340, SpringLayout.NORTH, container);
        //precio de venta
        layout.putConstraint(SpringLayout.WEST, labelPrecioVenta, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPrecioVenta, 420, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPrecioVenta, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPrecioVenta, 420, SpringLayout.NORTH, container);
        //proveedor
        layout.putConstraint(SpringLayout.WEST, labelProveedor, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelProveedor, 500, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtProveedor, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtProveedor, 500, SpringLayout.NORTH, container);
        //btn agregar imagenes
        layout.putConstraint(SpringLayout.WEST, btnAgregarImagenes, 760, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnAgregarImagenes, 20, SpringLayout.NORTH, container);
        // previsualizar imagenes
        for (int i = 0; i < 4; i++) {
            layout.putConstraint(SpringLayout.WEST, imagenLabels[i], 826, SpringLayout.WEST, panelNuevoArticulo);
            layout.putConstraint(SpringLayout.NORTH, imagenLabels[i], 120 + (i * 140), SpringLayout.NORTH, panelNuevoArticulo);
        }
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnGuardar, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnGuardar, 580, SpringLayout.NORTH, container);

        // Acción del botón para seleccionar imágenes
        btnAgregarImagenes.addActionListener(e -> {
            //Hacer linda la ventana
            LookAndFeel previousLF = UIManager.getLookAndFeel();

            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Para Windows
                SwingUtilities.updateComponentTreeUI(formulario);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar Imágenes");
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);

            // Establecer vista de iconos en vez de lista detallada (opcional)
            //UIManager.put("FileChooser.readOnly", Boolean.TRUE);
            int seleccion = fileChooser.showOpenDialog(formulario);

            //Volver a estilo anterior
            try {
                UIManager.setLookAndFeel(previousLF);
                SwingUtilities.updateComponentTreeUI(formulario);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File[] archivosSeleccionados = fileChooser.getSelectedFiles();

                for (File archivo : archivosSeleccionados) {
                    if (rutasImg.size() < 4) {
                        try {
                            // Crear carpeta si no existe
                            File directorio = new File("imagenes_articulos");
                            if (!directorio.exists()) {
                                directorio.mkdir();
                            }

                            // Copiar archivo a la carpeta del proyecto
                            File destino = new File(directorio, archivo.getName());
                            Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            // Guardar la ruta relativa en la lista
                            rutasImg.add(destino.getPath());

                            // Mostrar miniatura
                            ImageIcon icon = new ImageIcon(new ImageIcon(destino.getAbsolutePath()).getImage()
                                    .getScaledInstance(120, 120, Image.SCALE_SMOOTH));
                            imagenLabels[rutasImg.size() - 1].setIcon(icon);

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(formulario, "Error al copiar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(formulario, "Solo puedes agregar hasta 4 imágenes.", "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                }
            }
        });

        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String descripcion = txtDescripcion.getText();
                int cant1 = Integer.parseInt(txtCant1.getText());
                int cant2 = Integer.parseInt(txtCant2.getText());
                double precioCompra = Double.parseDouble(txtPrecioCompra.getText());
                double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
                String proveedor = txtProveedor.getText();
                String ruta1 = rutasImg.size() > 0 ? rutasImg.get(0) : "";
                String ruta2 = rutasImg.size() > 1 ? rutasImg.get(1) : "";
                String ruta3 = rutasImg.size() > 2 ? rutasImg.get(2) : "";
                String ruta4 = rutasImg.size() > 3 ? rutasImg.get(3) : "";

                insertarArticulo2(nombre, descripcion, cant1, cant2, precioCompra, precioVenta, proveedor, ruta1, ruta2, ruta3, ruta4);
                JOptionPane.showMessageDialog(formulario, "Artículo guardado correctamente.");
                formulario.dispose();
                actualizarTabla2();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(formulario, "Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulario.setVisible(true);
    }

    public static void abrirFormularioNuevoArticulo3() {

        JFrame formulario = new JFrame("Nuevo Artículo");
        formulario.setIconImage(App.logo);
        formulario.setLocation(220, 30);
        formulario.setSize(1100, 750);
        formulario.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formulario.setLayout(new BorderLayout());
        formulario.setResizable(false);

        // Poner todo en un panel
        JPanel panelNuevoArticulo = new JPanel();
        panelNuevoArticulo.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelNuevoArticulo.setLayout(layout);
        formulario.add(panelNuevoArticulo, BorderLayout.CENTER);


        // Componentes del formulario
        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtNombre);

        JLabel labelDescripcion = new JLabel("Descripcion:");
        labelDescripcion.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelDescripcion);
        JTextField txtDescripcion = new JTextField();
        txtDescripcion.setPreferredSize(new Dimension(250, 40));
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtDescripcion);

        JLabel labelCant1 = new JLabel("Cantidad Oncativo:");
        labelCant1.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant1);
        JTextField txtCant1 = new JTextField();
        txtCant1.setPreferredSize(new Dimension(250, 40));
        txtCant1.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant1);

        JLabel labelCant2 = new JLabel("Cantidad Oliva:");
        labelCant2.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant2);
        JTextField txtCant2 = new JTextField();
        txtCant2.setPreferredSize(new Dimension(250, 40));
        txtCant2.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant2);

        JLabel labelPrecioCompra = new JLabel("Precio de compra:");
        labelPrecioCompra.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioCompra);
        JTextField txtPrecioCompra = new JTextField();
        txtPrecioCompra.setPreferredSize(new Dimension(250, 40));
        txtPrecioCompra.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioCompra);

        JLabel labelPrecioVenta = new JLabel("Precio de venta:");
        labelPrecioVenta.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioVenta);
        JTextField txtPrecioVenta = new JTextField();
        txtPrecioVenta.setPreferredSize(new Dimension(250, 40));
        txtPrecioVenta.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioVenta);

        JLabel labelProveedor = new JLabel("Proveedor:");
        labelProveedor.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelProveedor);
        JTextField txtProveedor = new JTextField();
        txtProveedor.setPreferredSize(new Dimension(250, 40));
        txtProveedor.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtProveedor);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(200, 80));
        btnGuardar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnGuardar.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnGuardar.setMargin(new Insets(10, 12, 10, 12));
        panelNuevoArticulo.add(btnGuardar);

        JButton btnAgregarImagenes = new JButton("Agregar\n Imágenes");
        btnAgregarImagenes.setPreferredSize(new Dimension(240, 80));
        btnAgregarImagenes.setBackground(new Color(220, 204, 181));
        btnAgregarImagenes.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnAgregarImagenes.setMargin(new Insets(10, 6, 10, 6));
        panelNuevoArticulo.add(btnAgregarImagenes);

        // Lista para almacenar las imágenes seleccionadas
        ArrayList<String> rutasImg = new ArrayList<>();

        // Etiquetas para mostrar las imágenes seleccionadas
        JLabel[] imagenLabels = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            imagenLabels[i] = new JLabel();
            imagenLabels[i].setPreferredSize(new Dimension(120, 120));
            imagenLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panelNuevoArticulo.add(imagenLabels[i]);
        }

        //Acomodar componentes
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 20, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 20, SpringLayout.NORTH, container);
        //Descripcion
        layout.putConstraint(SpringLayout.WEST, labelDescripcion, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelDescripcion, 100, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtDescripcion, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtDescripcion, 100, SpringLayout.NORTH, container);
        //Cant1
        layout.putConstraint(SpringLayout.WEST, labelCant1, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant1, 180, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant1, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant1, 180, SpringLayout.NORTH, container);
        //Cant2
        layout.putConstraint(SpringLayout.WEST, labelCant2, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant2, 260, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant2, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant2, 260, SpringLayout.NORTH, container);
        //precio de compra
        layout.putConstraint(SpringLayout.WEST, labelPrecioCompra, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPrecioCompra, 340, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPrecioCompra, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPrecioCompra, 340, SpringLayout.NORTH, container);
        //precio de venta
        layout.putConstraint(SpringLayout.WEST, labelPrecioVenta, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPrecioVenta, 420, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPrecioVenta, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPrecioVenta, 420, SpringLayout.NORTH, container);
        //proveedor
        layout.putConstraint(SpringLayout.WEST, labelProveedor, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelProveedor, 500, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtProveedor, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtProveedor, 500, SpringLayout.NORTH, container);
        //btn agregar imagenes
        layout.putConstraint(SpringLayout.WEST, btnAgregarImagenes, 760, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnAgregarImagenes, 20, SpringLayout.NORTH, container);
        // previsualizar imagenes
        for (int i = 0; i < 4; i++) {
            layout.putConstraint(SpringLayout.WEST, imagenLabels[i], 826, SpringLayout.WEST, panelNuevoArticulo);
            layout.putConstraint(SpringLayout.NORTH, imagenLabels[i], 120 + (i * 140), SpringLayout.NORTH, panelNuevoArticulo);
        }
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnGuardar, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnGuardar, 580, SpringLayout.NORTH, container);

        btnAgregarImagenes.addActionListener(e -> {
            //Hacer linda la ventana
            LookAndFeel previousLF = UIManager.getLookAndFeel();

            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Para Windows
                SwingUtilities.updateComponentTreeUI(formulario);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar Imágenes");
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);

            // Establecer vista de iconos en vez de lista detallada (opcional)
            int seleccion = fileChooser.showOpenDialog(formulario);

            //Volver a estilo anterior
            try {
                UIManager.setLookAndFeel(previousLF);
                SwingUtilities.updateComponentTreeUI(formulario);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File[] archivosSeleccionados = fileChooser.getSelectedFiles();

                for (File archivo : archivosSeleccionados) {
                    if (rutasImg.size() < 4) {
                        try {
                            // Crear carpeta si no existe
                            File directorio = new File("imagenes_articulos");
                            if (!directorio.exists()) {
                                directorio.mkdir();
                            }

                            // Copiar archivo a la carpeta del proyecto
                            File destino = new File(directorio, archivo.getName());
                            Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            // Guardar la ruta relativa en la lista
                            rutasImg.add(destino.getPath());

                            // Mostrar miniatura
                            ImageIcon icon = new ImageIcon(new ImageIcon(destino.getAbsolutePath()).getImage()
                                    .getScaledInstance(120, 120, Image.SCALE_SMOOTH));
                            imagenLabels[rutasImg.size() - 1].setIcon(icon);

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(formulario, "Error al copiar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(formulario, "Solo puedes agregar hasta 4 imágenes.", "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                }
            }
        });

        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String descripcion = txtDescripcion.getText();
                int cant1 = Integer.parseInt(txtCant1.getText());
                int cant2 = Integer.parseInt(txtCant2.getText());
                double precioCompra = Double.parseDouble(txtPrecioCompra.getText());
                double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
                String proveedor = txtProveedor.getText();
                String ruta1 = rutasImg.size() > 0 ? rutasImg.get(0) : "";
                String ruta2 = rutasImg.size() > 1 ? rutasImg.get(1) : "";
                String ruta3 = rutasImg.size() > 2 ? rutasImg.get(2) : "";
                String ruta4 = rutasImg.size() > 3 ? rutasImg.get(3) : "";

                insertarArticulo2(nombre, descripcion, cant1, cant2, precioCompra, precioVenta, proveedor, ruta1, ruta2, ruta3, ruta4);
                JOptionPane.showMessageDialog(formulario, "Artículo guardado correctamente.");
                formulario.dispose();
                actualizarTabla3();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(formulario, "Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulario.setVisible(true);
    }

    public static void abrirFormularioNuevoArticulo4() {

        JFrame formulario = new JFrame("Nuevo Artículo");
        formulario.setIconImage(App.logo);
        formulario.setLocation(220, 50);
        formulario.setSize(1100, 710);
        formulario.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formulario.setLayout(new BorderLayout());
        formulario.setResizable(false);

        // Poner todo en un panel
        JPanel panelNuevoArticulo = new JPanel();
        panelNuevoArticulo.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelNuevoArticulo.setLayout(layout);
        formulario.add(panelNuevoArticulo, BorderLayout.CENTER);


        // Componentes del formulario
        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtNombre);

        JLabel labelDescripcion = new JLabel("Descripcion:");
        labelDescripcion.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelDescripcion);
        JTextField txtDescripcion = new JTextField();
        txtDescripcion.setPreferredSize(new Dimension(250, 40));
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtDescripcion);

        JLabel labelCant1 = new JLabel("Cantidad:");
        labelCant1.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant1);
        JTextField txtCant1 = new JTextField();
        txtCant1.setPreferredSize(new Dimension(250, 40));
        txtCant1.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant1);

        JLabel labelPrecioCompra = new JLabel("Precio de compra:");
        labelPrecioCompra.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioCompra);
        JTextField txtPrecioCompra = new JTextField();
        txtPrecioCompra.setPreferredSize(new Dimension(250, 40));
        txtPrecioCompra.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioCompra);

        JLabel labelPrecioVenta = new JLabel("Precio de venta:");
        labelPrecioVenta.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioVenta);
        JTextField txtPrecioVenta = new JTextField();
        txtPrecioVenta.setPreferredSize(new Dimension(250, 40));
        txtPrecioVenta.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioVenta);

        JLabel labelProveedor = new JLabel("Proveedor:");
        labelProveedor.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelProveedor);
        JTextField txtProveedor = new JTextField();
        txtProveedor.setPreferredSize(new Dimension(250, 40));
        txtProveedor.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtProveedor);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(200, 80));
        btnGuardar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnGuardar.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnGuardar.setMargin(new Insets(10, 12, 10, 12));
        panelNuevoArticulo.add(btnGuardar);

        JButton btnAgregarImagenes = new JButton("Agregar\n Imágenes");
        btnAgregarImagenes.setPreferredSize(new Dimension(240, 80));
        btnAgregarImagenes.setBackground(new Color(220, 204, 181));
        btnAgregarImagenes.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnAgregarImagenes.setMargin(new Insets(10, 6, 10, 6));
        panelNuevoArticulo.add(btnAgregarImagenes);

        // Lista para almacenar las imágenes seleccionadas
        ArrayList<String> rutasImg = new ArrayList<>();

        // Etiquetas para mostrar las imágenes seleccionadas
        JLabel[] imagenLabels = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            imagenLabels[i] = new JLabel();
            imagenLabels[i].setPreferredSize(new Dimension(120, 120));
            imagenLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panelNuevoArticulo.add(imagenLabels[i]);
        }

        //Acomodar componentes
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 20, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 20, SpringLayout.NORTH, container);
        //Descripcion
        layout.putConstraint(SpringLayout.WEST, labelDescripcion, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelDescripcion, 100, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtDescripcion, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtDescripcion, 100, SpringLayout.NORTH, container);
        //Cant1
        layout.putConstraint(SpringLayout.WEST, labelCant1, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant1, 180, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant1, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant1, 180, SpringLayout.NORTH, container);
        //precio de compra
        layout.putConstraint(SpringLayout.WEST, labelPrecioCompra, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPrecioCompra, 260, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPrecioCompra, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPrecioCompra, 260, SpringLayout.NORTH, container);
        //precio de venta
        layout.putConstraint(SpringLayout.WEST, labelPrecioVenta, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPrecioVenta, 340, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPrecioVenta, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPrecioVenta, 340, SpringLayout.NORTH, container);
        //proveedor
        layout.putConstraint(SpringLayout.WEST, labelProveedor, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelProveedor, 420, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtProveedor, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtProveedor, 420, SpringLayout.NORTH, container);
        //btn agregar imagenes
        layout.putConstraint(SpringLayout.WEST, btnAgregarImagenes, 760, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnAgregarImagenes, 20, SpringLayout.NORTH, container);
        // previsualizar imagenes
        for (int i = 0; i < 4; i++) {
            layout.putConstraint(SpringLayout.WEST, imagenLabels[i], 826, SpringLayout.WEST, panelNuevoArticulo);
            layout.putConstraint(SpringLayout.NORTH, imagenLabels[i], 120 + (i * 140), SpringLayout.NORTH, panelNuevoArticulo);
        }
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnGuardar, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnGuardar, 500, SpringLayout.NORTH, container);

        // Acción del botón para seleccionar imágenes
        btnAgregarImagenes.addActionListener(e -> {
            //Hacer linda la ventana
            LookAndFeel previousLF = UIManager.getLookAndFeel();

            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Para Windows
                SwingUtilities.updateComponentTreeUI(formulario);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar Imágenes");
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);

            // Establecer vista de iconos en vez de lista detallada (opcional)
            int seleccion = fileChooser.showOpenDialog(formulario);

            //Volver a estilo anterior
            try {
                UIManager.setLookAndFeel(previousLF);
                SwingUtilities.updateComponentTreeUI(formulario);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File[] archivosSeleccionados = fileChooser.getSelectedFiles();

                for (File archivo : archivosSeleccionados) {
                    if (rutasImg.size() < 4) {
                        try {
                            // Crear carpeta si no existe
                            File directorio = new File("imagenes_articulos");
                            if (!directorio.exists()) {
                                directorio.mkdir();
                            }

                            // Copiar archivo a la carpeta del proyecto
                            File destino = new File(directorio, archivo.getName());
                            Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            // Guardar la ruta relativa en la lista
                            rutasImg.add(destino.getPath());

                            // Mostrar miniatura
                            ImageIcon icon = new ImageIcon(new ImageIcon(destino.getAbsolutePath()).getImage()
                                    .getScaledInstance(120, 120, Image.SCALE_SMOOTH));
                            imagenLabels[rutasImg.size() - 1].setIcon(icon);

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(formulario, "Error al copiar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(formulario, "Solo puedes agregar hasta 4 imágenes.", "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                }
            }
        });

        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String descripcion = txtDescripcion.getText();
                int cant3 = Integer.parseInt(txtCant1.getText());
                double precioCompra = Double.parseDouble(txtPrecioCompra.getText());
                double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
                String proveedor = txtProveedor.getText();
                String ruta1 = rutasImg.size() > 0 ? rutasImg.get(0) : "";
                String ruta2 = rutasImg.size() > 1 ? rutasImg.get(1) : "";
                String ruta3 = rutasImg.size() > 2 ? rutasImg.get(2) : "";
                String ruta4 = rutasImg.size() > 3 ? rutasImg.get(3) : "";

                insertarArticulo3(nombre, descripcion, cant3, precioCompra, precioVenta, proveedor, ruta1, ruta2, ruta3, ruta4);
                JOptionPane.showMessageDialog(formulario, "Artículo guardado correctamente.");
                formulario.dispose();
                actualizarTabla4();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(formulario, "Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulario.setVisible(true);
    }

    public static void insertarArticulo(String nombre, String descripcion, int cant1, int cant2, int cant3, double precioCompra,
                                        double precioVenta, String proveedor, String img1, String img2, String img3, String img4) {

        String query = "INSERT INTO articulos (nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, cant3Articulo, precioCompraArticulo, precioVentaArticulo, proveedorArticulo, imagen1, imagen2, imagen3, imagen4) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Asignación de valores a los placeholders
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, descripcion);
            preparedStatement.setInt(3, cant1);
            preparedStatement.setInt(4, cant2);
            preparedStatement.setInt(5, cant3);
            preparedStatement.setDouble(6, precioCompra);
            preparedStatement.setDouble(7, precioVenta);
            preparedStatement.setString(8, proveedor);
            preparedStatement.setString(9, img1);
            preparedStatement.setString(10, img2);
            preparedStatement.setString(11, img3);
            preparedStatement.setString(12, img4);

            // Ejecutar la consulta
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Artículo insertado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar el artículo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void insertarArticulo2(String nombre, String descripcion, int cant1, int cant2, double precioCompra,
                                         double precioVenta, String proveedor, String img1, String img2, String img3, String img4) {

        String query = "INSERT INTO articulos (nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, cant3Articulo, precioCompraArticulo, precioVentaArticulo, proveedorArticulo, imagen1, imagen2, imagen3, imagen4) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Asignación de valores a los placeholders
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, descripcion);
            preparedStatement.setInt(3, cant1);
            preparedStatement.setInt(4, cant2);
            preparedStatement.setInt(5, 0);
            preparedStatement.setDouble(6, precioCompra);
            preparedStatement.setDouble(7, precioVenta);
            preparedStatement.setString(8, proveedor);
            preparedStatement.setString(9, img1);
            preparedStatement.setString(10, img2);
            preparedStatement.setString(11, img3);
            preparedStatement.setString(12, img4);

            // Ejecutar la consulta
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Artículo insertado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar el artículo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void insertarArticulo3(String nombre, String descripcion, int cant3, double precioCompra,
                                         double precioVenta, String proveedor, String img1, String img2, String img3, String img4) {

        String query = "INSERT INTO articulos (nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, cant3Articulo, precioCompraArticulo, precioVentaArticulo, proveedorArticulo, imagen1, imagen2, imagen3, imagen4) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Asignación de valores a los placeholders
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, descripcion);
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, cant3);
            preparedStatement.setDouble(6, precioCompra);
            preparedStatement.setDouble(7, precioVenta);
            preparedStatement.setString(8, proveedor);
            preparedStatement.setString(9, img1);
            preparedStatement.setString(10, img2);
            preparedStatement.setString(11, img3);
            preparedStatement.setString(12, img4);

            // Ejecutar la consulta
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Artículo insertado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar el artículo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void reponerArticulo() {

        JFrame reposicionFrame = new JFrame("Reposición de Artículo");
        reposicionFrame.setIconImage(App.logo);
        reposicionFrame.setLocation(370, 150);
        reposicionFrame.setSize(800, 470);
        reposicionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reposicionFrame.setLayout(new BorderLayout());
        reposicionFrame.setResizable(false);

        // Poner todo en un panel
        JPanel panelReposicion = new JPanel();
        panelReposicion.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelReposicion.setLayout(layout);
        reposicionFrame.add(panelReposicion, BorderLayout.CENTER);

        // Componentes del formulario
        //Nombre
        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelReposicion.add(txtNombre);

        //Cantidad
        JLabel labelCantidad = new JLabel("Cantidad:");
        labelCantidad.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelCantidad);
        JTextField txtCantidad = new JTextField();
        txtCantidad.setPreferredSize(new Dimension(250, 40));
        txtCantidad.setFont(new Font("Arial", Font.PLAIN, 16));
        panelReposicion.add(txtCantidad);

        //Sucursal
        JLabel labelSucursal = new JLabel("Seleccionar sucursal:");
        labelSucursal.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelSucursal);
        JComboBox<String> sucursalComboBox = new JComboBox<>(new String[]{"Oncativo", "Oliva", "El rincon"});
        sucursalComboBox.setPreferredSize(new Dimension(250, 40));
        sucursalComboBox.setBackground(new Color(220, 204, 181));
        sucursalComboBox.setFont(new Font("Roboto", Font.PLAIN, 24));
        panelReposicion.add(sucursalComboBox);

        //Boton reponer
        JButton btnReponer = new JButton("Reponer");
        btnReponer.setPreferredSize(new Dimension(200, 80));
        btnReponer.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnReponer.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnReponer.setMargin(new Insets(10, 12, 10, 12));
        panelReposicion.add(btnReponer);

        //Acomodar componentes
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 20, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 20, SpringLayout.NORTH, container);
        //Cant1
        layout.putConstraint(SpringLayout.WEST, labelCantidad, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCantidad, 120, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCantidad, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCantidad, 120, SpringLayout.NORTH, container);
        //Cant2
        layout.putConstraint(SpringLayout.WEST, labelSucursal, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelSucursal, 220, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, sucursalComboBox, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, sucursalComboBox, 220, SpringLayout.NORTH, container);
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnReponer, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnReponer, 320, SpringLayout.NORTH, container);

        // Acción del botón "Reponer"
        btnReponer.addActionListener(e -> {
            String identificador = txtNombre.getText().trim();
            String cantidadStr = txtCantidad.getText().trim();
            String sucursalSeleccionada = (String) sucursalComboBox.getSelectedItem();

            if (identificador.isEmpty() || cantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(reposicionFrame, "Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int cantidadReponer = Integer.parseInt(cantidadStr);

                // Determinar la columna a modificar según el depósito seleccionado
                String columna = "";

                switch (sucursalSeleccionada){
                    case "Oncativo":
                        columna = "cant1Articulo";
                        break;
                    case "Oliva":
                        columna = "cant2Articulo";
                        break;
                    case "El rincon":
                        columna = "cant3Articulo";
                        break;
                }

                // Actualizar la base de datos
                String query;
                boolean esID = identificador.matches("\\d+");

                if (esID) {
                    // Si el identificador es numérico, se asume que es ID
                    query = "UPDATE articulos SET " + columna + " = " + columna + " + ? WHERE idArticulo = ?";
                } else {
                    // Si el identificador es texto, se asume que es el nombre
                    query = "UPDATE articulos SET " + columna + " = " + columna + " + ? WHERE nombreArticulo = ?";
                }

                try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, cantidadReponer);
                    if (esID) {
                        preparedStatement.setInt(2, Integer.parseInt(identificador));
                    } else {
                        preparedStatement.setString(2, identificador);
                    }

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(reposicionFrame, "Reposición realizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                        // Cerrar el formulario de reposición
                        reposicionFrame.dispose();
                        actualizarTabla();
                    } else {
                        JOptionPane.showMessageDialog(reposicionFrame, "Artículo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(reposicionFrame, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(reposicionFrame, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reposicionFrame.setVisible(true);
    }

    public static void reponerArticulo2() {

        JFrame reposicionFrame = new JFrame("Reposición de Artículo");
        reposicionFrame.setIconImage(App.logo);
        reposicionFrame.setLocation(370, 150);
        reposicionFrame.setSize(800, 470);
        reposicionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reposicionFrame.setLayout(new BorderLayout());
        reposicionFrame.setResizable(false);

        // Poner todo en un panel
        JPanel panelReposicion = new JPanel();
        panelReposicion.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelReposicion.setLayout(layout);
        reposicionFrame.add(panelReposicion, BorderLayout.CENTER);

        // Componentes del formulario
        //Nombre
        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelReposicion.add(txtNombre);

        //Cantidad
        JLabel labelCantidad = new JLabel("Cantidad:");
        labelCantidad.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelCantidad);
        JTextField txtCantidad = new JTextField();
        txtCantidad.setPreferredSize(new Dimension(250, 40));
        txtCantidad.setFont(new Font("Arial", Font.PLAIN, 16));
        panelReposicion.add(txtCantidad);

        //Sucursal
        JLabel labelSucursal = new JLabel("Seleccionar sucursal:");
        labelSucursal.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelSucursal);
        JComboBox<String> sucursalComboBox = new JComboBox<>(new String[]{"Oliva", "Oncativo"});
        sucursalComboBox.setPreferredSize(new Dimension(250, 40));
        sucursalComboBox.setBackground(new Color(220, 204, 181));
        sucursalComboBox.setFont(new Font("Roboto", Font.PLAIN, 24));
        panelReposicion.add(sucursalComboBox);

        //Boton reponer
        JButton btnReponer = new JButton("Reponer");
        btnReponer.setPreferredSize(new Dimension(200, 80));
        btnReponer.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnReponer.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnReponer.setMargin(new Insets(10, 12, 10, 12));
        panelReposicion.add(btnReponer);

        //Acomodar componentes
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 20, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 20, SpringLayout.NORTH, container);
        //Cant1
        layout.putConstraint(SpringLayout.WEST, labelCantidad, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCantidad, 120, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCantidad, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCantidad, 120, SpringLayout.NORTH, container);
        //Cant2
        layout.putConstraint(SpringLayout.WEST, labelSucursal, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelSucursal, 220, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, sucursalComboBox, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, sucursalComboBox, 220, SpringLayout.NORTH, container);
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnReponer, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnReponer, 320, SpringLayout.NORTH, container);

        // Acción del botón "Reponer"
        btnReponer.addActionListener(e -> {
            String identificador = txtNombre.getText().trim();
            String cantidadStr = txtCantidad.getText().trim();
            String sucursalSeleccionada = (String) sucursalComboBox.getSelectedItem();

            if (identificador.isEmpty() || cantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(reposicionFrame, "Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int cantidadReponer = Integer.parseInt(cantidadStr);

                // Determinar la columna a modificar según el depósito seleccionado
                String columna = "";

                switch (sucursalSeleccionada){
                    case "Oncativo":
                        columna = "cant1Articulo";
                        break;
                    case "Oliva":
                        columna = "cant2Articulo";
                        break;
                }

                // Actualizar la base de datos
                String query;
                boolean esID = identificador.matches("\\d+");

                if (esID) {
                    // Si el identificador es numérico, se asume que es ID
                    query = "UPDATE articulos SET " + columna + " = " + columna + " + ? WHERE idArticulo = ?";
                } else {
                    // Si el identificador es texto, se asume que es el nombre
                    query = "UPDATE articulos SET " + columna + " = " + columna + " + ? WHERE nombreArticulo = ?";
                }

                try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, cantidadReponer);
                    if (esID) {
                        preparedStatement.setInt(2, Integer.parseInt(identificador));
                    } else {
                        preparedStatement.setString(2, identificador);
                    }

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(reposicionFrame, "Reposición realizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                        // Cerrar el formulario de reposición
                        reposicionFrame.dispose();
                        actualizarTabla2();
                    } else {
                        JOptionPane.showMessageDialog(reposicionFrame, "Artículo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(reposicionFrame, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(reposicionFrame, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reposicionFrame.setVisible(true);
    }

    public static void reponerArticulo3() {

        JFrame reposicionFrame = new JFrame("Reposición de Artículo");
        reposicionFrame.setIconImage(App.logo);
        reposicionFrame.setLocation(370, 150);
        reposicionFrame.setSize(800, 470);
        reposicionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reposicionFrame.setLayout(new BorderLayout());
        reposicionFrame.setResizable(false);

        // Poner todo en un panel
        JPanel panelReposicion = new JPanel();
        panelReposicion.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelReposicion.setLayout(layout);
        reposicionFrame.add(panelReposicion, BorderLayout.CENTER);

        // Componentes del formulario
        //Nombre
        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelReposicion.add(txtNombre);

        //Cantidad
        JLabel labelCantidad = new JLabel("Cantidad:");
        labelCantidad.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelCantidad);
        JTextField txtCantidad = new JTextField();
        txtCantidad.setPreferredSize(new Dimension(250, 40));
        txtCantidad.setFont(new Font("Arial", Font.PLAIN, 16));
        panelReposicion.add(txtCantidad);

        //Sucursal
        JLabel labelSucursal = new JLabel("Seleccionar sucursal:");
        labelSucursal.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelSucursal);
        JComboBox<String> sucursalComboBox = new JComboBox<>(new String[]{"Oliva", "Oncativo"});
        sucursalComboBox.setPreferredSize(new Dimension(250, 40));
        sucursalComboBox.setBackground(new Color(220, 204, 181));
        sucursalComboBox.setFont(new Font("Roboto", Font.PLAIN, 24));
        panelReposicion.add(sucursalComboBox);

        //Boton reponer
        JButton btnReponer = new JButton("Reponer");
        btnReponer.setPreferredSize(new Dimension(200, 80));
        btnReponer.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnReponer.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnReponer.setMargin(new Insets(10, 12, 10, 12));
        panelReposicion.add(btnReponer);

        //Acomodar componentes
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 20, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 20, SpringLayout.NORTH, container);
        //Cant1
        layout.putConstraint(SpringLayout.WEST, labelCantidad, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCantidad, 120, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCantidad, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCantidad, 120, SpringLayout.NORTH, container);
        //Cant2
        layout.putConstraint(SpringLayout.WEST, labelSucursal, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelSucursal, 220, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, sucursalComboBox, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, sucursalComboBox, 220, SpringLayout.NORTH, container);
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnReponer, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnReponer, 320, SpringLayout.NORTH, container);

        // Acción del botón "Reponer"
        btnReponer.addActionListener(e -> {
            String identificador = txtNombre.getText().trim();
            String cantidadStr = txtCantidad.getText().trim();
            String sucursalSeleccionada = (String) sucursalComboBox.getSelectedItem();

            if (identificador.isEmpty() || cantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(reposicionFrame, "Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int cantidadReponer = Integer.parseInt(cantidadStr);

                // Determinar la columna a modificar según el depósito seleccionado
                String columna = "";

                switch (sucursalSeleccionada){
                    case "Oncativo":
                        columna = "cant1Articulo";
                        break;
                    case "Oliva":
                        columna = "cant2Articulo";
                        break;
                }

                // Actualizar la base de datos
                String query;
                boolean esID = identificador.matches("\\d+");

                if (esID) {
                    // Si el identificador es numérico, se asume que es ID
                    query = "UPDATE articulos SET " + columna + " = " + columna + " + ? WHERE idArticulo = ?";
                } else {
                    // Si el identificador es texto, se asume que es el nombre
                    query = "UPDATE articulos SET " + columna + " = " + columna + " + ? WHERE nombreArticulo = ?";
                }

                try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, cantidadReponer);
                    if (esID) {
                        preparedStatement.setInt(2, Integer.parseInt(identificador));
                    } else {
                        preparedStatement.setString(2, identificador);
                    }

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(reposicionFrame, "Reposición realizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                        // Cerrar el formulario de reposición
                        reposicionFrame.dispose();
                        actualizarTabla3();
                    } else {
                        JOptionPane.showMessageDialog(reposicionFrame, "Artículo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(reposicionFrame, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(reposicionFrame, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reposicionFrame.setVisible(true);
    }

    public static void reponerArticulo4() {

        JFrame reposicionFrame = new JFrame("Reposición de Artículo");
        reposicionFrame.setIconImage(App.logo);
        reposicionFrame.setLocation(370, 150);
        reposicionFrame.setSize(800, 370);
        reposicionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reposicionFrame.setLayout(new BorderLayout());
        reposicionFrame.setResizable(false);

        // Poner todo en un panel
        JPanel panelReposicion = new JPanel();
        panelReposicion.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelReposicion.setLayout(layout);
        reposicionFrame.add(panelReposicion, BorderLayout.CENTER);

        // Componentes del formulario
        //Nombre
        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelReposicion.add(txtNombre);

        //Cantidad
        JLabel labelCantidad = new JLabel("Cantidad:");
        labelCantidad.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelReposicion.add(labelCantidad);
        JTextField txtCantidad = new JTextField();
        txtCantidad.setPreferredSize(new Dimension(250, 40));
        txtCantidad.setFont(new Font("Arial", Font.PLAIN, 16));
        panelReposicion.add(txtCantidad);

        //Boton reponer
        JButton btnReponer = new JButton("Reponer");
        btnReponer.setPreferredSize(new Dimension(200, 80));
        btnReponer.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnReponer.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnReponer.setMargin(new Insets(10, 12, 10, 12));
        panelReposicion.add(btnReponer);

        //Acomodar componentes
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 20, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 20, SpringLayout.NORTH, container);
        //Cant1
        layout.putConstraint(SpringLayout.WEST, labelCantidad, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCantidad, 120, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCantidad, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCantidad, 120, SpringLayout.NORTH, container);
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnReponer, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnReponer, 220, SpringLayout.NORTH, container);

        // Acción del botón "Reponer"
        btnReponer.addActionListener(e -> {
            String identificador = txtNombre.getText().trim();
            String cantidadStr = txtCantidad.getText().trim();

            if (identificador.isEmpty() || cantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(reposicionFrame, "Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int cantidadReponer = Integer.parseInt(cantidadStr);

                // Determinar la columna a modificar según el depósito seleccionado
                String columna = "cant3Articulo";

                // Actualizar la base de datos
                String query;
                boolean esID = identificador.matches("\\d+");

                if (esID) {
                    // Si el identificador es numérico, se asume que es ID
                    query = "UPDATE articulos SET " + columna + " = " + columna + " + ? WHERE idArticulo = ?";
                } else {
                    // Si el identificador es texto, se asume que es el nombre
                    query = "UPDATE articulos SET " + columna + " = " + columna + " + ? WHERE nombreArticulo = ?";
                }

                try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, cantidadReponer);
                    if (esID) {
                        preparedStatement.setInt(2, Integer.parseInt(identificador));
                    } else {
                        preparedStatement.setString(2, identificador);
                    }

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(reposicionFrame, "Reposición realizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                        // Cerrar el formulario de reposición
                        reposicionFrame.dispose();
                        actualizarTabla4();
                    } else {
                        JOptionPane.showMessageDialog(reposicionFrame, "Artículo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(reposicionFrame, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(reposicionFrame, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reposicionFrame.setVisible(true);
    }

    public static void definirArticulo() {

        JFrame formulario = new JFrame("Actualizar Artículo");
        formulario.setIconImage(App.logo);
        formulario.setLocation(465,  200);
        formulario.setSize(600, 340);
        formulario.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formulario.setLayout(new BorderLayout());
        formulario.setResizable(false);

        // Poner todo en un panel
        JPanel panelNuevoArticulo = new JPanel();
        panelNuevoArticulo.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelNuevoArticulo.setLayout(layout);
        formulario.add(panelNuevoArticulo, BorderLayout.CENTER);


        // Componentes del formulario
        JLabel intro = new JLabel("¿Qué artículo quieres actualizar?");
        intro.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(intro);

        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtNombre);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setPreferredSize(new Dimension(200, 80));
        btnConfirmar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnConfirmar.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnConfirmar.setMargin(new Insets(10, 12, 10, 12));
        panelNuevoArticulo.add(btnConfirmar);

        //Acomodar componentes
        //Intro
        layout.putConstraint(SpringLayout.WEST, intro, 70, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, intro, 20, SpringLayout.NORTH, container);
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 60, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 100, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 295, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 100, SpringLayout.NORTH, container);
        //btn confirmar
        layout.putConstraint(SpringLayout.WEST, btnConfirmar, 200, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnConfirmar, 180, SpringLayout.NORTH, container);

        btnConfirmar.addActionListener(e -> {
            try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)){
                String nombre = txtNombre.getText();
                int id = 0;

                String query = "SELECT idArticulo FROM articulos WHERE nombreArticulo LIKE ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, "%" + nombre + "%");
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    id = resultSet.getInt("idArticulo");
                }

                formulario.dispose();
                abrirFormularioActualizar(id);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar artículo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulario.setVisible(true);
    }

    private static void abrirFormularioActualizar(int idArticulo) {

        JFrame formulario = new JFrame("Nuevo Artículo");
        formulario.setIconImage(App.logo);
        formulario.setLocation(370, 0);
        formulario.setSize(800, 820);
        formulario.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formulario.setLayout(new BorderLayout());
        formulario.setResizable(false);

        // Poner todo en un panel
        JPanel panelNuevoArticulo = new JPanel();
        panelNuevoArticulo.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelNuevoArticulo.setLayout(layout);
        formulario.add(panelNuevoArticulo, BorderLayout.CENTER);


        // Componentes del formulario
        JLabel intro = new JLabel("¿Qué quieres actualizar?");
        intro.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(intro);

        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtNombre);

        JLabel labelDescripcion = new JLabel("Descripcion:");
        labelDescripcion.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelDescripcion);
        JTextField txtDescripcion = new JTextField();
        txtDescripcion.setPreferredSize(new Dimension(250, 40));
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtDescripcion);

        JLabel labelCant1 = new JLabel("Cantidad Oncativo:");
        labelCant1.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant1);
        JTextField txtCant1 = new JTextField();
        txtCant1.setPreferredSize(new Dimension(250, 40));
        txtCant1.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant1);

        JLabel labelCant2 = new JLabel("Cantidad Oliva:");
        labelCant2.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant2);
        JTextField txtCant2 = new JTextField();
        txtCant2.setPreferredSize(new Dimension(250, 40));
        txtCant2.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant2);

        JLabel labelCant3 = new JLabel("Cantidad El rincon:");
        labelCant3.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant3);
        JTextField txtCant3 = new JTextField();
        txtCant3.setPreferredSize(new Dimension(250, 40));
        txtCant3.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant3);

        JLabel labelPrecioCompra = new JLabel("Precio de compra:");
        labelPrecioCompra.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioCompra);
        JTextField txtPrecioCompra = new JTextField();
        txtPrecioCompra.setPreferredSize(new Dimension(250, 40));
        txtPrecioCompra.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioCompra);

        JLabel labelPrecioVenta = new JLabel("Precio de venta:");
        labelPrecioVenta.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioVenta);
        JTextField txtPrecioVenta = new JTextField();
        txtPrecioVenta.setPreferredSize(new Dimension(250, 40));
        txtPrecioVenta.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioVenta);

        JLabel labelProveedor = new JLabel("Proveedor:");
        labelProveedor.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelProveedor);
        JTextField txtProveedor = new JTextField();
        txtProveedor.setPreferredSize(new Dimension(250, 40));
        txtProveedor.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtProveedor);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setPreferredSize(new Dimension(200, 80));
        btnActualizar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnActualizar.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnActualizar.setMargin(new Insets(10, 12, 10, 12));
        panelNuevoArticulo.add(btnActualizar);

        //Acomodar componentes
        //Intro
        layout.putConstraint(SpringLayout.WEST, intro, 230, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, intro, 20, SpringLayout.NORTH, container);
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 90, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 90, SpringLayout.NORTH, container);
        //Descripcion
        layout.putConstraint(SpringLayout.WEST, labelDescripcion, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelDescripcion, 160, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtDescripcion, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtDescripcion, 160, SpringLayout.NORTH, container);
        //Cant1
        layout.putConstraint(SpringLayout.WEST, labelCant1, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant1, 230, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant1, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant1, 230, SpringLayout.NORTH, container);
        //Cant2
        layout.putConstraint(SpringLayout.WEST, labelCant2, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant2, 300, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant2, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant2, 300, SpringLayout.NORTH, container);
        //Cant3
        layout.putConstraint(SpringLayout.WEST, labelCant3, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCant3, 370, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCant3, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCant3, 370, SpringLayout.NORTH, container);
        //precio de compra
        layout.putConstraint(SpringLayout.WEST, labelPrecioCompra, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPrecioCompra, 440, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPrecioCompra, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPrecioCompra, 440, SpringLayout.NORTH, container);
        //precio de venta
        layout.putConstraint(SpringLayout.WEST, labelPrecioVenta, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPrecioVenta, 510, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPrecioVenta, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPrecioVenta, 510, SpringLayout.NORTH, container);
        //proveedor
        layout.putConstraint(SpringLayout.WEST, labelProveedor, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelProveedor, 580, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtProveedor, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtProveedor, 580, SpringLayout.NORTH, container);
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnActualizar, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnActualizar, 650, SpringLayout.NORTH, container);

        btnActualizar.addActionListener(e -> {
            try {
                int cant1 = -1;
                int cant2 = -1;
                int cant3 = -1;
                double precioCompra = -1.0;
                double precioVenta = -1.0;

                if (!txtCant1.getText().trim().isEmpty()) {
                    try {
                        cant1 = Integer.parseInt(txtCant1.getText().trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ingrese un número válido en Cantidad Oncativo.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (!txtCant2.getText().trim().isEmpty()) {
                    try {
                        cant2 = Integer.parseInt(txtCant2.getText().trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ingrese un número válido en Cantidad Oliva.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (!txtCant3.getText().trim().isEmpty()) {
                    try {
                        cant3 = Integer.parseInt(txtCant3.getText().trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ingrese un número válido en Cantidad El rincon.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (!txtPrecioCompra.getText().trim().isEmpty()) {
                    try {
                        precioCompra = Double.parseDouble(txtPrecioCompra.getText().trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ingrese un número válido en Precio de Compra.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (!txtPrecioVenta.getText().trim().isEmpty()) {
                    try {
                        precioVenta = Double.parseDouble(txtPrecioVenta.getText().trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ingrese un número válido en Precio de Venta.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                actualizarArticulo(idArticulo,
                        txtNombre.getText(),
                        txtDescripcion.getText(),
                        cant1,
                        cant2,
                        cant3,
                        precioCompra,
                        precioVenta,
                        txtProveedor.getText()
                );
                formulario.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: Asegúrate de ingresar números válidos en los campos de cantidad y precio.","Error de entrada", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulario.setVisible(true);
    }

    private static void actualizarArticulo(int idArticulo, String nombre, String descripcion, int cant1, int cant2,
                                           int cant3, double precioCompra, double precioVenta, String proveedor) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            StringBuilder query = new StringBuilder("UPDATE articulos SET ");
            java.util.List<String> updates = new ArrayList<>();
            List<Object> valores = new ArrayList<>();

            if (!nombre.isEmpty()) {
                updates.add("nombreArticulo = ?");
                valores.add(nombre);
            }
            if (!descripcion.isEmpty()) {
                updates.add("descripcionArticulo = ?");
                valores.add(descripcion);
            }
            if (cant1 != -1) {
                updates.add("cant1Articulo = ?");
                valores.add(cant1);
            }
            if (cant2 != -1) {
                updates.add("cant2Articulo = ?");
                valores.add(cant2);
            }
            if (cant3 != -1) {
                updates.add("cant3Articulo = ?");
                valores.add(cant3);
            }
            if (precioCompra != -1.0) {
                updates.add("precioCompraArticulo = ?");
                valores.add(precioCompra);
            }
            if (precioVenta != -1.0) {
                updates.add("precioVentaArticulo = ?");
                valores.add(precioVenta);
            }
            if (!proveedor.isEmpty()) {
                updates.add("proveedorArticulo = ?");
                valores.add(proveedor);
            }

            if (updates.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se ingresaron datos para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Construir la consulta SQL dinámica
            query.append(String.join(", ", updates));
            query.append(" WHERE idArticulo = ?");
            valores.add(idArticulo);

            try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
                for (int i = 0; i < valores.size(); i++) {
                    statement.setObject(i + 1, valores.get(i));
                }
                int filasAfectadas = statement.executeUpdate();

                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(null, "Artículo actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el artículo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            switch (Inicio.rol) {
                case "superadmin": //victoria
                    actualizarTabla();
                    break;
                case "admin": //admin oliva
                    actualizarTabla2();
                    break;
                case "empleado1": //empleado el arca
                    actualizarTabla3();
                    break;
                case "empleado2": //empleado el rincon
                    actualizarTabla4();
                    break;
            }

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el artículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String obtenerRutaImagen(int idArticulo) {
        String rutaImagen = null;
        String query = "SELECT imagen1 FROM articulos WHERE idArticulo = ?";

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idArticulo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                rutaImagen = resultSet.getString("imagen1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rutaImagen;
    }

    public static void mostrarDetalleArticulo() {
        lblImagenArticulo.setText("");
        int filaSeleccionada = App.listTable.getSelectedRow();
        int realFilaSeleccionada = App.listTable.convertRowIndexToModel(filaSeleccionada);
        if (filaSeleccionada != -1) {
            DefaultTableModel model = (DefaultTableModel) App.listTable.getModel();

            String nombre = (String) model.getValueAt(realFilaSeleccionada, 0);
            String descripcion = (String) model.getValueAt(realFilaSeleccionada, 1);
            int cant1 = (int) model.getValueAt(realFilaSeleccionada, 2);
            int cant2 = (int) model.getValueAt(realFilaSeleccionada, 3);
            int cant3 = (int) model.getValueAt(realFilaSeleccionada, 4);
            int total = cant1 + cant2 + cant3;
            Object precioCompraObj = model.getValueAt(realFilaSeleccionada, 6);
            double precioCompra = (precioCompraObj instanceof Number) ? ((Number) precioCompraObj).doubleValue() : 0.0;
            Object precioVentaObj = model.getValueAt(realFilaSeleccionada, 7);
            double precioVenta = (precioVentaObj instanceof Number) ? ((Number) precioVentaObj).doubleValue() : 0.0;
            String imagenRuta = obtenerRutaImagen(realFilaSeleccionada+1);

            // Formatear la información para mostrar en el JTextArea
            String detalles1 = "\n\n" +
                    "Nombre: " + nombre + "\n\n" +
                    "Descripción: " + descripcion;

            String detalles2 =
                    "Cantidad Oncativo: " + cant1 + "\n\n" +
                            "Cantidad Oliva: " + cant2 + "\n\n" +
                            "Cantidad El rincon: " + cant3 + "\n\n" +
                            "Cantidad total: " + total;

            String detalles3 = "Precio de compra: $" + precioCompra + "\n\n" +
                    "Precio de venta: $" + precioVenta + "\n\n" +
                    "Precio total: $" + precioVenta*total + "\n\n" +
                    "Ganancia: $" + (precioVenta - precioCompra);

            detalleArticulo1.setText(detalles1);
            detalleArticulo2.setText(detalles2);
            detalleArticulo3.setText(detalles3);

            if (imagenRuta != null && !imagenRuta.isEmpty()) {
                ImageIcon icono = new ImageIcon(new ImageIcon(imagenRuta).getImage().getScaledInstance(240, 200, Image.SCALE_SMOOTH));
                lblImagenArticulo.setIcon(icono);
            } else {
                lblImagenArticulo.setText("Sin imagen");
                lblImagenArticulo.setIcon(null);
            }
        }
    }

    public static void mostrarDetalleArticulo2() {
        lblImagenArticulo.setText("");
        int filaSeleccionada = App.listTable.getSelectedRow();
        int realFilaSeleccionada = App.listTable.convertRowIndexToModel(filaSeleccionada);
        if (filaSeleccionada != -1) {
            DefaultTableModel model = (DefaultTableModel) App.listTable.getModel();

            String nombre = (String) model.getValueAt(realFilaSeleccionada, 0);
            String descripcion = (String) model.getValueAt(realFilaSeleccionada, 1);
            int cant2 = (int) model.getValueAt(realFilaSeleccionada, 2);
            Object precioCompraObj = model.getValueAt(realFilaSeleccionada, 3);
            double precioCompra = (precioCompraObj instanceof Number) ? ((Number) precioCompraObj).doubleValue() : 0.0;
            Object precioVentaObj = model.getValueAt(realFilaSeleccionada, 4);
            double precioVenta = (precioVentaObj instanceof Number) ? ((Number) precioVentaObj).doubleValue() : 0.0;
            String imagenRuta = obtenerRutaImagen(realFilaSeleccionada+1);

            // Formatear la información para mostrar en el JTextArea
            String detalles1 = "\n\n" +
                    "Nombre: " + nombre + "\n\n" +
                    "Descripción: " + descripcion;

            String detalles2 = "\n\n" +
                    "Cantidad: " + cant2 + "\n\n" +
                            "Precio total: $" + precioVenta*cant2;

            String detalles3 = "\n" +
                    "Precio de compra: $" + precioCompra + "\n\n" +
                    "Precio de venta: $" + precioVenta + "\n\n" +
                    "Ganancia: $" + (precioVenta - precioCompra) ;

            detalleArticulo1.setText(detalles1);
            detalleArticulo2.setText(detalles2);
            detalleArticulo3.setText(detalles3);

            if (imagenRuta != null && !imagenRuta.isEmpty()) {
                ImageIcon icono = new ImageIcon(new ImageIcon(imagenRuta).getImage().getScaledInstance(240, 200, Image.SCALE_SMOOTH));
                lblImagenArticulo.setIcon(icono);
            } else {
                lblImagenArticulo.setText("Sin imagen");
                lblImagenArticulo.setIcon(null);
            }
        }
    }

    public static void mostrarDetalleArticulo3() {
        lblImagenArticulo.setText("");
        int filaSeleccionada = App.listTable.getSelectedRow();
        int realFilaSeleccionada = App.listTable.convertRowIndexToModel(filaSeleccionada);
        if (filaSeleccionada != -1) {
            DefaultTableModel model = (DefaultTableModel) App.listTable.getModel();

            String nombre = (String) model.getValueAt(realFilaSeleccionada, 0);
            String descripcion = (String) model.getValueAt(realFilaSeleccionada, 1);
            int cant1 = (int) model.getValueAt(realFilaSeleccionada, 2);
            int cant2 = (int) model.getValueAt(realFilaSeleccionada, 3);
            int total = cant1 + cant2;
            Object precioVentaObj = model.getValueAt(realFilaSeleccionada, 5);
            double precioVenta = (precioVentaObj instanceof Number) ? ((Number) precioVentaObj).doubleValue() : 0.0;
            String imagenRuta = obtenerRutaImagen(realFilaSeleccionada+1);

            // Formatear la información para mostrar en el JTextArea
            String detalles1 = "\n\n" +
                    "Nombre: " + nombre + "\n\n" +
                    "Descripción: " + descripcion;

            String detalles2 ="\n" +
                    "Cantidad Oncativo: " + cant1 + "\n\n" +
                            "Cantidad Oliva: " + cant2 + "\n\n" +
                            "Cantidad total: " + total;

            String detalles3 = "\n\n" +
                    "Precio de venta: $" + precioVenta + "\n\n" +
                    "Precio total: $" + precioVenta*total;

            detalleArticulo1.setText(detalles1);
            detalleArticulo2.setText(detalles2);
            detalleArticulo3.setText(detalles3);

            if (imagenRuta != null && !imagenRuta.isEmpty()) {
                ImageIcon icono = new ImageIcon(new ImageIcon(imagenRuta).getImage().getScaledInstance(240, 200, Image.SCALE_SMOOTH));
                lblImagenArticulo.setIcon(icono);
            } else {
                lblImagenArticulo.setText("Sin imagen");
                lblImagenArticulo.setIcon(null);
            }
        }
    }

    public static void mostrarDetalleArticulo4() {
        lblImagenArticulo.setText("");
        int filaSeleccionada = App.listTable.getSelectedRow();
        int realFilaSeleccionada = App.listTable.convertRowIndexToModel(filaSeleccionada);
        if (filaSeleccionada != -1) {
            DefaultTableModel model = (DefaultTableModel) App.listTable.getModel();

            String nombre = (String) model.getValueAt(realFilaSeleccionada, 0);
            String descripcion = (String) model.getValueAt(realFilaSeleccionada, 1);
            int cant3 = (int) model.getValueAt(realFilaSeleccionada, 2);
            Object precioVentaObj = model.getValueAt(realFilaSeleccionada, 3);
            double precioVenta = (precioVentaObj instanceof Number) ? ((Number) precioVentaObj).doubleValue() : 0.0;
            String imagenRuta = obtenerRutaImagen(realFilaSeleccionada+1);

            // Formatear la información para mostrar en el JTextArea
            String detalles1 = "\n\n" +
                    "Nombre: " + nombre + "\n\n" +
                    "Descripción: " + descripcion;

            String detalles2 ="\n\n\n" +
                    "Cantidad: " + cant3;

            String detalles3 = "\n\n" +
                    "Precio de venta: $" + precioVenta + "\n\n" +
                    "Precio total: $" + precioVenta*cant3;

            detalleArticulo1.setText(detalles1);
            detalleArticulo2.setText(detalles2);
            detalleArticulo3.setText(detalles3);

            if (imagenRuta != null && !imagenRuta.isEmpty()) {
                ImageIcon icono = new ImageIcon(new ImageIcon(imagenRuta).getImage().getScaledInstance(240, 200, Image.SCALE_SMOOTH));
                lblImagenArticulo.setIcon(icono);
            } else {
                lblImagenArticulo.setText("Sin imagen");
                lblImagenArticulo.setIcon(null);
            }
        }
    }

    public static JPanel crearPanelDetalle() {
        JPanel panelDetalle = new JPanel(new BorderLayout());
        panelDetalle.setPreferredSize(new Dimension(500, 100));
        panelDetalle.setBackground(new Color(240, 233, 225));

        // Panel de la imagen
        JPanel panelImagen = new JPanel(new GridLayout(1, 1, 0, 0));
        panelImagen.setPreferredSize(new Dimension(300, 300));
        panelImagen.setBackground(new Color(240, 233, 225)); // Gris claro
        panelImagen.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true)); // Borde sutil

        // Label de imagen
        lblImagenArticulo.setHorizontalAlignment(JLabel.CENTER);
        lblImagenArticulo.setPreferredSize(new Dimension(240, 240));

        // Agregar la imagen
        panelImagen.add(lblImagenArticulo);
        panelDetalle.add(panelImagen, BorderLayout.WEST);

        // Panel contenedor con GridLayout para organizar las 3 columnas
        JPanel panelInfo = new JPanel(new GridLayout(1, 3, 0, 0)); // Sin separación horizontal ni vertical
        panelInfo.setOpaque(false);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true), // Borde dorado suave exterior
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Espaciado interno
        ));

        // Crear áreas de texto con estilo y sin bordes internos
        detalleArticulo1 = crearTextArea();
        detalleArticulo2 = crearTextArea();
        detalleArticulo3 = crearTextArea();

        // Crear JScrollPane sin bordes que se fusionan visualmente
        JScrollPane scrollPane1 = crearScrollPane(detalleArticulo1);
        JScrollPane scrollPane2 = crearScrollPane(detalleArticulo2);
        JScrollPane scrollPane3 = crearScrollPane(detalleArticulo3);

        // Agregar los JScrollPane al panelInfo (en 3 columnas sin separación)
        panelInfo.add(scrollPane1);
        panelInfo.add(scrollPane2);
        panelInfo.add(scrollPane3);

        // Agregar panelInfo al panelDetalle
        panelDetalle.add(panelInfo, BorderLayout.CENTER);

        return panelDetalle;
    }

    private static JTextArea crearTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Roboto", Font.BOLD, 18));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Sin bordes entre columnas
        textArea.setMargin(new Insets(20, 20, 20, 20));
        return textArea;
    }

    private static JScrollPane crearScrollPane(JTextArea textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null); // Sin bordes
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        return scrollPane;
    }

    public static void iniciarActualizacionAutomatica(DefaultTableModel tableModel) {
        int intervalo = 120 * 1000;

        javax.swing.Timer timer = new javax.swing.Timer(intervalo, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTablaDesdeDB(tableModel);
            }
        });

        timer.start();
    }

    public static void iniciarActualizacionAutomatica2(DefaultTableModel tableModel) {
        int intervalo = 120 * 1000;

        javax.swing.Timer timer = new javax.swing.Timer(intervalo, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTablaDesdeDB2(tableModel);
            }
        });

        timer.start();
    }

    public static void iniciarActualizacionAutomatica3(DefaultTableModel tableModel) {
        int intervalo = 120 * 1000;

        javax.swing.Timer timer = new javax.swing.Timer(intervalo, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTablaDesdeDB3(tableModel);
            }
        });

        timer.start();
    }

    public static void iniciarActualizacionAutomatica4(DefaultTableModel tableModel) {
        int intervalo = 120 * 1000;

        javax.swing.Timer timer = new javax.swing.Timer(intervalo, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTablaDesdeDB4(tableModel);
            }
        });

        timer.start();
    }

    private static void actualizarTablaDesdeDB(DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            String query = "SELECT nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, cant3Articulo, precioCompraArticulo, precioVentaArticulo FROM articulos";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Limpiar la tabla antes de actualizar

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant1 = resultSet.getInt("cant1Articulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                int cant3 = resultSet.getInt("cant3Articulo");
                int total = cant1 + cant2 + cant3;
                double precioCompra = resultSet.getDouble("precioCompraArticulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");
                double ganancia = precioVenta - precioCompra;

                tableModel.addRow(new Object[]{nombre, descripcion, cant1, cant2, cant3, total, precioCompra, precioVenta, ganancia});
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar la tabla: " + e.getMessage());
        }
    }

    private static void actualizarTablaDesdeDB2(DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            String query = "SELECT nombreArticulo, descripcionArticulo, cant2Articulo, precioCompraArticulo, precioVentaArticulo FROM articulos";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Limpiar la tabla antes de actualizar

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                double precioCompra = resultSet.getDouble("precioCompraArticulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");
                double ganancia = precioVenta - precioCompra;

                tableModel.addRow(new Object[]{nombre, descripcion, cant2, precioCompra, precioVenta, ganancia});
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar la tabla: " + e.getMessage());
        }
    }

    private static void actualizarTablaDesdeDB3(DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            String query = "SELECT nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, precioVentaArticulo FROM articulos";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Limpiar la tabla antes de actualizar

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant1 = resultSet.getInt("cant1Articulo");
                int cant2 = resultSet.getInt("cant2Articulo");
                int total = cant1 + cant2;
                double precioVenta = resultSet.getDouble("precioVentaArticulo");

                tableModel.addRow(new Object[]{nombre, descripcion, cant1, cant2, total, precioVenta});
            }

            System.out.println("actalizar desde db 3");

        } catch (SQLException e) {
            System.err.println("Error al actualizar la tabla: " + e.getMessage());
        }
    }

    private static void actualizarTablaDesdeDB4(DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            String query = "SELECT nombreArticulo, descripcionArticulo, cant3Articulo, precioVentaArticulo FROM articulos";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Limpiar la tabla antes de actualizar

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreArticulo");
                String descripcion = resultSet.getString("descripcionArticulo");
                int cant3 = resultSet.getInt("cant3Articulo");
                double precioVenta = resultSet.getDouble("precioVentaArticulo");

                tableModel.addRow(new Object[]{nombre, descripcion, cant3, precioVenta});
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar la tabla: " + e.getMessage());
        }
    }

}

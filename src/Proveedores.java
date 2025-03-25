import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Proveedores {

    private static JTable proveedoresTable;

    public static void abrirInterfazProveedores(){

        JFrame proveedoresFrame = new JFrame("Proveedores");
        proveedoresFrame.setIconImage(App.logo);
        proveedoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        proveedoresFrame.setLayout(new BorderLayout());
        proveedoresFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        proveedoresFrame.setMinimumSize(new Dimension(1400, 800));

        //Panel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(new Color(220, 204, 181));
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));
        JButton addButton = new JButton("Nuevo proveedor");
        addButton.setPreferredSize(new Dimension(220, 60));
        addButton.setBackground(new Color(240, 233, 225));
        addButton.setFont(new Font("Roboto", Font.PLAIN, 24));
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton searchButton = new JButton("Buscar");
        searchButton.setPreferredSize(new Dimension(130, 60));
        searchButton.setBackground(new Color(240, 233, 225));
        searchButton.setFont(new Font("Roboto", Font.PLAIN, 24));
        topPanel.add(addButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Tabla de proveedores
        String[] columnNames = {"Nombre", "Firma", "Localidad", "Contacto", "Compra mínima"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        proveedoresTable = new JTable(tableModel);
        proveedoresTable.setRowHeight(25);
        proveedoresTable.setBackground(new Color(240, 233, 225));
        proveedoresTable.setFont(new Font("Arial", Font.PLAIN, 14));
        proveedoresTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        proveedoresTable.getTableHeader().setBackground(new Color(220, 204, 181));
        proveedoresTable.getTableHeader().setForeground(Color.BLACK);
        JScrollPane tableScrollPane = new JScrollPane(proveedoresTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        // cargar datos de sqlite
        loadProveedoresFromSQLite(tableModel);

        // Agregar componentes a la ventana
        proveedoresFrame.add(topPanel, BorderLayout.NORTH);
        proveedoresFrame.add(tableScrollPane, BorderLayout.CENTER);

        // Listeners
        addButton.addActionListener(e -> {
            abrirFormularioNuevoProveedor();
        });

        searchButton.addActionListener(e -> {
            filtrarProveedor(searchField.getText(), tableModel);
            searchField.setText("");
        });

        proveedoresFrame.setVisible(true);
    }

    private static void loadProveedoresFromSQLite(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Limpia el modelo antes de cargar nuevos datos

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {

            String query = "SELECT nombreProveedor, firmaProveedor, localidadProveedor, contactoProveedor, compraMinimaProveedor FROM proveedores";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("Consulta ejecutada con éxito.");

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreProveedor");
                String firma = resultSet.getString("firmaProveedor");
                String localidad = resultSet.getString("localidadProveedor");
                String contacto = resultSet.getString("contactoProveedor");
                double compraMinima = resultSet.getDouble("compraMinimaProveedor");

                tableModel.addRow(new Object[]{nombre, firma, localidad, contacto, compraMinima});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de SQLite", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void actualizarTablaProveedores() {
        System.out.println("Actualizando tabla de proveedores");
        DefaultTableModel tableModel = (DefaultTableModel) proveedoresTable.getModel();
        tableModel.setRowCount(0); // Limpia las filas actuales

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            String query = "SELECT nombreProveedor, firmaProveedor, localidadProveedor, contactoProveedor, compraMinimaProveedor FROM proveedores";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreProveedor");
                String firma = resultSet.getString("firmaProveedor");
                String localidad = resultSet.getString("localidadProveedor");
                String contacto = resultSet.getString("contactoProveedor");
                double compraMinima = resultSet.getDouble("compraMinimaProveedor");

                tableModel.addRow(new Object[]{nombre, firma, localidad, contacto, compraMinima});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar la tabla", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void abrirFormularioNuevoProveedor() {

        JFrame formulario = new JFrame("Nuevo Proveedor");
        formulario.setIconImage(App.logo);
        formulario.setLocation(370, 140);
        formulario.setSize(800, 570);
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

        JLabel labelFirma = new JLabel("Firma:");
        labelFirma.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelFirma);
        JTextField txtFirma = new JTextField();
        txtFirma.setPreferredSize(new Dimension(250, 40));
        txtFirma.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtFirma);

        JLabel labelLocalidad = new JLabel("Localidad:");
        labelLocalidad.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelLocalidad);
        JTextField txtLocalidad = new JTextField();
        txtLocalidad.setPreferredSize(new Dimension(250, 40));
        txtLocalidad.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtLocalidad);

        JLabel labelContacto = new JLabel("Contacto:");
        labelContacto.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelContacto);
        JTextField txtContacto = new JTextField();
        txtContacto.setPreferredSize(new Dimension(250, 40));
        txtContacto.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtContacto);

        JLabel labelCompra = new JLabel("Compra mínima:");
        labelCompra.setFont(new Font("Roboto", Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCompra);
        JTextField txtCompra = new JTextField();
        txtCompra.setPreferredSize(new Dimension(250, 40));
        txtCompra.setFont(new Font("Arial", Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCompra);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(200, 80));
        btnGuardar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnGuardar.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnGuardar.setMargin(new Insets(10, 12, 10, 12));
        panelNuevoArticulo.add(btnGuardar);

        //Acomodar componentes
        //Nombre
        layout.putConstraint(SpringLayout.WEST, labelNombre, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 20, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtNombre, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 20, SpringLayout.NORTH, container);
        //Firma
        layout.putConstraint(SpringLayout.WEST, labelFirma, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelFirma, 100, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtFirma, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtFirma, 100, SpringLayout.NORTH, container);
        //Localidad
        layout.putConstraint(SpringLayout.WEST, labelLocalidad, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelLocalidad, 180, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtLocalidad, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtLocalidad, 180, SpringLayout.NORTH, container);
        //Contacto
        layout.putConstraint(SpringLayout.WEST, labelContacto, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelContacto, 260, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtContacto, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtContacto, 260, SpringLayout.NORTH, container);
        //Compra minima
        layout.putConstraint(SpringLayout.WEST, labelCompra, 100, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelCompra, 340, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtCompra, 420, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtCompra, 340, SpringLayout.NORTH, container);
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnGuardar, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnGuardar, 420, SpringLayout.NORTH, container);

        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String firma = txtFirma.getText();
                String localidad = txtLocalidad.getText();
                String contacto = txtContacto.getText();
                double compraMinima = Double.parseDouble(txtCompra.getText());

                insertarProveedor(nombre, firma, localidad, contacto, compraMinima);
                JOptionPane.showMessageDialog(formulario, "Proveedor guardado correctamente.");
                formulario.dispose();
                actualizarTablaProveedores();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(formulario, "Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulario.setVisible(true);
    }

    public static void insertarProveedor(String nombre, String firma, String localidad, String contacto, double compraMinima) {

        String query = "INSERT INTO proveedores (nombreProveedor, firmaProveedor, localidadProveedor, contactoProveedor, compraMinimaProveedor) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Asignación de valores a los placeholders
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, firma);
            preparedStatement.setString(3, localidad);
            preparedStatement.setString(4, contacto);
            preparedStatement.setDouble(5, compraMinima);
            // Ejecutar la consulta
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Proveedor insertado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar el proveedor", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void filtrarProveedor(String busqueda, DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena)) {
            System.out.println("Buscando: " + busqueda);

            tableModel.setRowCount(0); // Limpiar la tabla antes de agregar nuevas filas

            String query = "SELECT nombreProveedor, firmaProveedor, localidadProveedor, contactoProveedor, compraMinimaProveedor FROM proveedores WHERE nombreProveedor LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + busqueda + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreProveedor");
                String firma = resultSet.getString("firmaProveedor");
                String localidad = resultSet.getString("localidadProveedor");
                String contacto = resultSet.getString("contactoProveedor");
                double compraMinima = resultSet.getDouble("compraMinimaProveedor");

                tableModel.addRow(new Object[]{nombre, firma, localidad, contacto, compraMinima});
            }

            // Resetear selección después de actualizar los datos
            App.listTable.clearSelection();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar proveedores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

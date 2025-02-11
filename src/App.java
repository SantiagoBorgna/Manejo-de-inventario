import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import com.toedter.calendar.JCalendar;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPCell;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.io.FileOutputStream;

public class App {

    static String url = "jdbc:sqlite:C:/Users/Usuario/Arca Home/Arcadb.db"; // Ruta al archivo de la base de datos

    static Image logo = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/logo arca.png"));
    private static String sucursalActual = "Oncativo";

    private static SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    private static String fechaActual;
    private static String busquedaActual;
    private static JTable listTable;
    private static JTable proveedoresTable;
    private static JTextArea detalleArticulo1;
    private static JTextArea detalleArticulo2;
    private static JTextArea detalleArticulo3;

    public static void main(String[] args) {

        // Marco principal
        JFrame frame = new JFrame("El Arca Home");
        frame.setIconImage(logo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1400, 800));

        // Panel superior
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 6));
        topPanel.setBackground(new Color(220, 204, 181)); // Color beige 179, 144, 122

        JButton btnReposition = new JButton("Reposición");
        JButton btnNewItem = new JButton("Nuevo artículo");
        JButton btnUpdate = new JButton("Actualizar artículo");
        JComboBox<String> cbSucursal = new JComboBox<>(new String[]{"Oncativo", "Oliva", "El rincon"});
        JTextField txtSearch = new JTextField("", 15);
        JButton btnSearch = new JButton("Buscar");
        JButton btnSale = new JButton("Venta");
        JButton btnLibroDiario = new JButton("Libro diario");
        JButton btnProveedores = new JButton("Proveedores");

        btnReposition.setBackground(new Color(240, 233, 225));
        btnReposition.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 24));
        btnReposition.setMargin(new Insets(10, 15, 10, 15));

        btnNewItem.setBackground(new Color(240, 233, 225));
        btnNewItem.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        btnNewItem.setMargin(new Insets(10, 15, 10, 15));

        btnUpdate.setBackground(new Color(240, 233, 225));
        btnUpdate.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        btnUpdate.setMargin(new Insets(10, 15, 10, 15));

        txtSearch.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        txtSearch.setMargin(new Insets(5, 10, 5, 10));

        cbSucursal.setBackground(new Color(240, 233, 225));
        cbSucursal.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        cbSucursal.setPreferredSize(new Dimension(130, 57));

        btnSearch.setBackground(new Color(240, 233, 225));
        btnSearch.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        btnSearch.setMargin(new Insets(10, 15, 10, 15));

        btnSale.setBackground(new Color(240, 233, 225));
        btnSale.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        btnSale.setMargin(new Insets(10, 15, 10, 15));

        btnLibroDiario.setBackground(new Color(240, 233, 225));
        btnLibroDiario.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        btnLibroDiario.setMargin(new Insets(10, 15, 10, 15));

        btnProveedores.setBackground(new Color(240, 233, 225));
        btnProveedores.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        btnProveedores.setMargin(new Insets(10, 15, 10, 15));

        topPanel.add(btnReposition);
        topPanel.add(btnNewItem);
        topPanel.add(btnUpdate);
        topPanel.add(cbSucursal);
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnSale);
        topPanel.add(btnLibroDiario);
        topPanel.add(btnProveedores);

        // Panel central
        JPanel centerPanel = new JPanel(new BorderLayout());
        // Panel de detalle de articulo
        JPanel panelDetalle = crearPanelDetalle();
        centerPanel.add(panelDetalle, BorderLayout.CENTER);

        // Panel para lista de elementos
        JPanel listPanel = new JPanel();
        listPanel.setPreferredSize(new Dimension(500, 500));
        listPanel.setLayout(new BorderLayout());

        String[] listColumns = {"Nombre", "Descripción", "Cantidad Oncativo", "Cantidad Oliva", "Cantidad El rincon", "Total", "Precio de compra", "Precio de venta", "Ganancia"};
        DefaultTableModel listTableModel = new DefaultTableModel(listColumns, 0);
        iniciarActualizacionAutomatica(listTableModel);
        listTable = new JTable(listTableModel);
        listTable.setRowHeight(25);
        listTable.setBackground(new Color(240, 233, 225));
        listTable.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        listTable.getTableHeader().setFont(new java.awt.Font("Roboto", Font.BOLD, 14));
        listTable.getTableHeader().setBackground(new Color(220, 204, 181));
        listTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane listScrollPane = new JScrollPane(listTable);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        centerPanel.add(listPanel, BorderLayout.SOUTH);

        // cargar datos de sqlite
        loadDataFromSQLite(listTableModel);

        // prender el método para generar los pdf
        iniciarGeneracionAutomaticaPDF();

        // Agregar componentes al marco
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        //Guardar sucursal actual
        cbSucursal.addActionListener(e -> {
            sucursalActual = (String) cbSucursal.getSelectedItem();
            System.out.println(sucursalActual);
        });

        // Eventos de botones
        btnSearch.addActionListener(e -> {
            busquedaActual = txtSearch.getText().trim();
            DefaultTableModel model = (DefaultTableModel) listTable.getModel();
            filtrarArticulos(busquedaActual, model);
            txtSearch.setText("");
        });

        btnNewItem.addActionListener(e ->{
            abrirFormularioNuevoArticulo();
        });

        btnReposition.addActionListener(e ->{
            reponerArticulo();
        });

        btnUpdate.addActionListener(e ->{
            definirArticulo();
        });

        btnSale.addActionListener(e ->{
            abrirInterfazVenta(sucursalActual);
        });

        btnLibroDiario.addActionListener(e ->{
            abrirInterfazLibroDiario(sucursalActual);
        });

        btnProveedores.addActionListener(e ->{
            abrirInterfazProveedores();
        });

        listTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && listTable.getSelectedRow() != -1) {
                mostrarDetalleArticulo();
            }
        });

        // Hacer visible la interfaz
        frame.setVisible(true);

    }

    private static void loadDataFromSQLite(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Limpia el modelo antes de cargar nuevos datos

        try (Connection connection = DriverManager.getConnection(url)) {
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

    private static void loadProveedoresFromSQLite(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Limpia el modelo antes de cargar nuevos datos

        try (Connection connection = DriverManager.getConnection(url)) {

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

    private static void abrirInterfazProveedores(){

        JFrame proveedoresFrame = new JFrame("Proveedores");
        proveedoresFrame.setIconImage(logo);
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
        addButton.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        JButton searchButton = new JButton("Buscar");
        searchButton.setPreferredSize(new Dimension(130, 60));
        searchButton.setBackground(new Color(240, 233, 225));
        searchButton.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        topPanel.add(addButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Tabla de proveedores
        String[] columnNames = {"Nombre", "Firma", "Localidad", "Contacto", "Compra mínima"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        proveedoresTable = new JTable(tableModel);
        proveedoresTable.setRowHeight(25);
        proveedoresTable.setBackground(new Color(240, 233, 225));
        proveedoresTable.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        proveedoresTable.getTableHeader().setFont(new java.awt.Font("Roboto", Font.BOLD, 14));
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

    private static void actualizarTabla() {
        DefaultTableModel tableModel = (DefaultTableModel) listTable.getModel();
        tableModel.setRowCount(0); // Limpia las filas actuales

        try (Connection connection = DriverManager.getConnection(url)) {
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

    private static void actualizarTablaProveedores() {
        System.out.println("Actualizando tabla de proveedores");
        DefaultTableModel tableModel = (DefaultTableModel) proveedoresTable.getModel();
        tableModel.setRowCount(0); // Limpia las filas actuales

        try (Connection connection = DriverManager.getConnection(url)) {
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

    private static void filtrarArticulos(String busqueda, DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(url)) {

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
            listTable.clearSelection();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar artículos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void abrirFormularioNuevoArticulo() {

        JFrame formulario = new JFrame("Nuevo Artículo");
        formulario.setIconImage(logo);
        formulario.setLocation(370, 0);
        formulario.setSize(800, 850);
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
        labelNombre.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtNombre);

        JLabel labelDescripcion = new JLabel("Descripcion:");
        labelDescripcion.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelDescripcion);
        JTextField txtDescripcion = new JTextField();
        txtDescripcion.setPreferredSize(new Dimension(250, 40));
        txtDescripcion.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtDescripcion);

        JLabel labelCant1 = new JLabel("Cantidad Oncativo:");
        labelCant1.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant1);
        JTextField txtCant1 = new JTextField();
        txtCant1.setPreferredSize(new Dimension(250, 40));
        txtCant1.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant1);

        JLabel labelCant2 = new JLabel("Cantidad Oliva:");
        labelCant2.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant2);
        JTextField txtCant2 = new JTextField();
        txtCant2.setPreferredSize(new Dimension(250, 40));
        txtCant2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant2);

        JLabel labelCant3 = new JLabel("Cantidad El rincon:");
        labelCant3.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant3);
        JTextField txtCant3 = new JTextField();
        txtCant3.setPreferredSize(new Dimension(250, 40));
        txtCant3.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant3);

        JLabel labelPrecioCompra = new JLabel("Precio de compra:");
        labelPrecioCompra.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioCompra);
        JTextField txtPrecioCompra = new JTextField();
        txtPrecioCompra.setPreferredSize(new Dimension(250, 40));
        txtPrecioCompra.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioCompra);

        JLabel labelPrecioVenta = new JLabel("Precio de venta:");
        labelPrecioVenta.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioVenta);
        JTextField txtPrecioVenta = new JTextField();
        txtPrecioVenta.setPreferredSize(new Dimension(250, 40));
        txtPrecioVenta.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioVenta);

        JLabel labelProveedor = new JLabel("Proveedor:");
        labelProveedor.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelProveedor);
        JTextField txtProveedor = new JTextField();
        txtProveedor.setPreferredSize(new Dimension(250, 40));
        txtProveedor.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtProveedor);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(200, 80));
        btnGuardar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnGuardar.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        btnGuardar.setMargin(new Insets(10, 12, 10, 12));
        panelNuevoArticulo.add(btnGuardar);

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
        //btn guardar
        layout.putConstraint(SpringLayout.WEST, btnGuardar, 300, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnGuardar, 660, SpringLayout.NORTH, container);

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

                insertarArticulo(nombre, descripcion, cant1, cant2, cant3, precioCompra, precioVenta, proveedor);
                JOptionPane.showMessageDialog(formulario, "Artículo guardado correctamente.");
                formulario.dispose();
                actualizarTabla();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(formulario, "Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulario.setVisible(true);
    }

    public static void insertarArticulo(String nombre, String descripcion, int cant1, int cant2, int cant3, double precioCompra, double precioVenta, String proveedor) {

        String query = "INSERT INTO articulos (nombreArticulo, descripcionArticulo, cant1Articulo, cant2Articulo, cant3Articulo, precioCompraArticulo, precioVentaArticulo, proveedorArticulo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url);
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

    private static void reponerArticulo() {

        JFrame reposicionFrame = new JFrame("Reposición de Artículo");
        reposicionFrame.setIconImage(logo);
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
        labelNombre.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelReposicion.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelReposicion.add(txtNombre);

        //Cantidad
        JLabel labelCantidad = new JLabel("Cantidad:");
        labelCantidad.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelReposicion.add(labelCantidad);
        JTextField txtCantidad = new JTextField();
        txtCantidad.setPreferredSize(new Dimension(250, 40));
        txtCantidad.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelReposicion.add(txtCantidad);

        //Sucursal
        JLabel labelSucursal = new JLabel("Seleccionar sucursal:");
        labelSucursal.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelReposicion.add(labelSucursal);
        JComboBox<String> sucursalComboBox = new JComboBox<>(new String[]{"Oncativo", "Oliva", "El rincon"});
        sucursalComboBox.setPreferredSize(new Dimension(250, 40));
        sucursalComboBox.setBackground(new Color(220, 204, 181));
        sucursalComboBox.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        panelReposicion.add(sucursalComboBox);

        //Boton reponer
        JButton btnReponer = new JButton("Reponer");
        btnReponer.setPreferredSize(new Dimension(200, 80));
        btnReponer.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnReponer.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
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
                //String columna = sucursalSeleccionada.equals("Oncativo") ? "cant1Articulo" : "cant2Articulo";
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

                try (Connection connection = DriverManager.getConnection(url);
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

    private static void abrirInterfazVenta(String sucursalActual) {

        // Crear ventana de venta
        JFrame ventaFrame = new JFrame("Venta - Sucursal " + sucursalActual);
        ventaFrame.setIconImage(logo);
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
        searchField.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        JButton searchButton = new JButton("Buscar");
        searchButton.setPreferredSize(new Dimension(130, 60));
        searchButton.setBackground(new Color(240, 233, 225));
        searchButton.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Tabla de artículos
        String[] columnNames = {"Nombre", "Descripción", "Stock", "Precio Unitario"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable articleTable = new JTable(tableModel);
        articleTable.setRowHeight(25);
        articleTable.setBackground(new Color(240, 233, 225));
        articleTable.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        articleTable.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 14));
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
        resumenTable.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        resumenTable.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 14));
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
        addToCartButton.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 16));

        JTextField quantityField = new JTextField(3);
        quantityField.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));

        JLabel cantidadLabel = new JLabel("Cantidad:");
        cantidadLabel.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 16));
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
        totalLabel.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 16));
        JButton venderButton = new JButton("Vender");
        venderButton.setPreferredSize(new Dimension(90, 45));
        venderButton.setBackground(new Color(240, 233, 225));
        venderButton.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 16));
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
        resumenVentaFrame.setIconImage(logo);
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
        labelNombre.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelVenta.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelVenta.add(txtNombre);

        //Medio de pago
        JLabel labelPago = new JLabel("Medio de pago:");
        labelPago.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelVenta.add(labelPago);
        JComboBox<String> medioDePagoComboBox = new JComboBox<>(new String[]{"Efectivo", "Transferencia", "Tarjeta de crédito", "Tarjeta de débito", "Cheque", "Dólares"});
        medioDePagoComboBox.setPreferredSize(new Dimension(250, 40));
        medioDePagoComboBox.setBackground(new Color(220, 204, 181));
        medioDePagoComboBox.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        panelVenta.add(medioDePagoComboBox);

        //Nombre cliente
        JLabel labelTotal = new JLabel("Total: $" + montoTotal);
        labelTotal.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelVenta.add(labelTotal);

        //Boton confirmar
        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setPreferredSize(new Dimension(200, 80));
        btnConfirmar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnConfirmar.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
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

        try (Connection connection = DriverManager.getConnection(url)){
            String query;

            if (sucursalActual == "Oncativo"){
                query = "SELECT nombreArticulo, descripcionArticulo, cant1Articulo, precioVentaArticulo FROM articulos WHERE nombreArticulo LIKE ? OR idArticulo LIKE ?";
            } else if (sucursalActual == "Oliva"){
                query = "SELECT nombreArticulo, descripcionArticulo, cant2Articulo, precioVentaArticulo FROM articulos WHERE nombreArticulo LIKE ? OR idArticulo LIKE ?";
            } else {
                query = "SELECT nombreArticulo, descripcionArticulo, cant3Articulo, precioVentaArticulo FROM articulos WHERE nombreArticulo LIKE ? OR idArticulo LIKE ?";
            }

            tableModel.setRowCount(0); // Limpiar la tabla
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + busqueda + "%");
            statement.setString(2, "%" + busqueda + "%");
            ResultSet resultSet = statement.executeQuery();

            if (sucursalActual == "Oncativo"){
                while (resultSet.next()) {
                    tableModel.addRow(new Object[]{
                            resultSet.getString("nombreArticulo"),
                            resultSet.getString("descripcionArticulo"),
                            resultSet.getInt("cant1Articulo"),
                            resultSet.getDouble("precioVentaArticulo")
                    });
                }
            } else if (sucursalActual == "Oliva"){
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

    private static void definirArticulo() {

        JFrame formulario = new JFrame("Actualizar Artículo");
        formulario.setIconImage(logo);
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
        intro.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(intro);

        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtNombre);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setPreferredSize(new Dimension(200, 80));
        btnConfirmar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnConfirmar.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
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
            try (Connection connection = DriverManager.getConnection(url)){
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
        formulario.setIconImage(logo);
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
        intro.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(intro);

        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtNombre);

        JLabel labelDescripcion = new JLabel("Descripcion:");
        labelDescripcion.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelDescripcion);
        JTextField txtDescripcion = new JTextField();
        txtDescripcion.setPreferredSize(new Dimension(250, 40));
        txtDescripcion.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtDescripcion);

        JLabel labelCant1 = new JLabel("Cantidad Oncativo:");
        labelCant1.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant1);
        JTextField txtCant1 = new JTextField();
        txtCant1.setPreferredSize(new Dimension(250, 40));
        txtCant1.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant1);

        JLabel labelCant2 = new JLabel("Cantidad Oliva:");
        labelCant2.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant2);
        JTextField txtCant2 = new JTextField();
        txtCant2.setPreferredSize(new Dimension(250, 40));
        txtCant2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant2);

        JLabel labelCant3 = new JLabel("Cantidad El rincon:");
        labelCant3.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCant3);
        JTextField txtCant3 = new JTextField();
        txtCant3.setPreferredSize(new Dimension(250, 40));
        txtCant3.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCant3);

        JLabel labelPrecioCompra = new JLabel("Precio de compra:");
        labelPrecioCompra.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioCompra);
        JTextField txtPrecioCompra = new JTextField();
        txtPrecioCompra.setPreferredSize(new Dimension(250, 40));
        txtPrecioCompra.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioCompra);

        JLabel labelPrecioVenta = new JLabel("Precio de venta:");
        labelPrecioVenta.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelPrecioVenta);
        JTextField txtPrecioVenta = new JTextField();
        txtPrecioVenta.setPreferredSize(new Dimension(250, 40));
        txtPrecioVenta.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtPrecioVenta);

        JLabel labelProveedor = new JLabel("Proveedor:");
        labelProveedor.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelProveedor);
        JTextField txtProveedor = new JTextField();
        txtProveedor.setPreferredSize(new Dimension(250, 40));
        txtProveedor.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtProveedor);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setPreferredSize(new Dimension(200, 80));
        btnActualizar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnActualizar.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
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

    private static void actualizarArticulo(int idArticulo, String nombre, String descripcion, int cant1, int cant2, int cant3, double precioCompra, double precioVenta, String proveedor) {

        try (Connection connection = DriverManager.getConnection(url)) {
            StringBuilder query = new StringBuilder("UPDATE articulos SET ");
            List<String> updates = new ArrayList<>();
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

            actualizarTabla();

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el artículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        try (Connection connection = DriverManager.getConnection(url)) {
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
            actualizarTabla();

        } catch (SQLException e) {
            try (Connection connection = DriverManager.getConnection(url)) {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                JOptionPane.showMessageDialog(null, "Error al realizar el rollback: " + rollbackEx.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Error al finalizar la venta: " + e.getMessage());
        }
    }

    private static void mostrarDetalleArticulo() {
        int filaSeleccionada = listTable.getSelectedRow();
        int realFilaSeleccionada = listTable.convertRowIndexToModel(filaSeleccionada);
        if (filaSeleccionada != -1) {
            DefaultTableModel model = (DefaultTableModel) listTable.getModel();

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

            // Formatear la información para mostrar en el JTextArea
            String detalles1 = "\n\n" +
                    "Nombre: " + nombre + "\n\n" +
                    "Descripción: " + descripcion;

            String detalles2 =
                    "Cantidad Oncativo: " + cant1 + "\n\n" +
                    "Cantidad Oliva: " + cant2 + "\n\n" +
                    "Cantidad El rincon: " + cant3 + "\n\n" +
                    "Cantidad total: " + total;

            String detalles3 = "\n" +
                    "Precio de compra: $" + precioCompra + "\n\n" +
                    "Precio de venta: $" + precioVenta + "\n\n" +
                    "Precio total: $" + precioVenta*total;

            detalleArticulo1.setText(detalles1);
            detalleArticulo2.setText(detalles2);
            detalleArticulo3.setText(detalles3);
        } else {
            return;
        }
    }

    private static JPanel crearPanelDetalle() {
        JPanel panelDetalle = new JPanel(new BorderLayout());
        panelDetalle.setPreferredSize(new Dimension(500, 100));
        panelDetalle.setBackground(new Color(240, 233, 225)); // Fondo crema claro  255, 248, 220

        // Panel de la imagen
        JPanel panelImagen = new JPanel();
        panelImagen.setPreferredSize(new Dimension(300, 150));
        panelImagen.setBackground(new Color(240, 233, 225)); // Gris claro
        panelImagen.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true)); // Borde sutil
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
        textArea.setFont(new java.awt.Font("Roboto", Font.BOLD, 18));
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

    private static void iniciarActualizacionAutomatica(DefaultTableModel tableModel) {
        int intervalo = 120 * 1000;

        javax.swing.Timer timer = new javax.swing.Timer(intervalo, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTablaDesdeDB(tableModel);
            }
        });

        timer.start();
    }

    private static void actualizarTablaDesdeDB(DefaultTableModel tableModel) {

        try (Connection connection = DriverManager.getConnection(url)) {
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

    private static void abrirFormularioNuevoProveedor() {

        JFrame formulario = new JFrame("Nuevo Proveedor");
        formulario.setIconImage(logo);
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
        labelNombre.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtNombre);

        JLabel labelFirma = new JLabel("Firma:");
        labelFirma.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelFirma);
        JTextField txtFirma = new JTextField();
        txtFirma.setPreferredSize(new Dimension(250, 40));
        txtFirma.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtFirma);

        JLabel labelLocalidad = new JLabel("Localidad:");
        labelLocalidad.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelLocalidad);
        JTextField txtLocalidad = new JTextField();
        txtLocalidad.setPreferredSize(new Dimension(250, 40));
        txtLocalidad.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtLocalidad);

        JLabel labelContacto = new JLabel("Contacto:");
        labelContacto.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelContacto);
        JTextField txtContacto = new JTextField();
        txtContacto.setPreferredSize(new Dimension(250, 40));
        txtContacto.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtContacto);

        JLabel labelCompra = new JLabel("Compra mínima:");
        labelCompra.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 30));
        panelNuevoArticulo.add(labelCompra);
        JTextField txtCompra = new JTextField();
        txtCompra.setPreferredSize(new Dimension(250, 40));
        txtCompra.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        panelNuevoArticulo.add(txtCompra);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(200, 80));
        btnGuardar.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnGuardar.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
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

        try (Connection connection = DriverManager.getConnection(url);
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

        try (Connection connection = DriverManager.getConnection(url)) {
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
            listTable.clearSelection();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar proveedores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void registrarVenta(String sucursal, String clienteVenta, String articulosVenta, double montoVenta, String pagoVenta) {

        String query = "INSERT INTO libro_diario (fechaVenta, sucursalVenta, clienteVenta, articulosVenta, montoVenta, medioDePagoVenta) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(new Date())); // Fecha actual
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

    private static void abrirInterfazLibroDiario(String sucursalActual){

        // Crear ventana de Libro diario
        JFrame libroFrame = new JFrame("Libro Diario - Sucursal " + sucursalActual);
        libroFrame.setIconImage(logo);
        libroFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        libroFrame.setLayout(new BorderLayout());
        libroFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        libroFrame.setMinimumSize(new Dimension(1400, 800));

        // Panel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(new Color(220, 204, 181));
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        JCalendar calendar = new JCalendar();
        calendar.setBackground(new Color(240, 233, 225));
        calendar.setForeground(new Color(50, 50, 50));
        calendar.getDayChooser().setDecorationBackgroundColor(new Color(180, 140, 90));
        calendar.getDayChooser().setWeekdayForeground(Color.BLACK);
        calendar.getDayChooser().setSundayForeground(Color.RED);
        calendar.getDayChooser().setWeekOfYearVisible(false);
        // Fuente
        calendar.setFont(new java.awt.Font("Roboto", Font.BOLD, 16));
        calendar.getDayChooser().setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 14));
        calendar.getMonthChooser().getComboBox().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 16));
        // Bordes
        calendar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JComboBox<String> cbMedioDePago = new JComboBox<>(new String[]{"Todo", "Efectivo", "Transferencia", "Tarjeta de crédito", "Tarjeta de débito", "Cheque", "Dólares"});
        cbMedioDePago.setBackground(new Color(240, 233, 225));
        cbMedioDePago.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));
        cbMedioDePago.setPreferredSize(new Dimension(  200, 57));

        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));

        JButton filtrarButton = new JButton("Filtrar");
        filtrarButton.setPreferredSize(new Dimension(130, 60));
        filtrarButton.setBackground(new Color(240, 233, 225));
        filtrarButton.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 24));

        topPanel.add(calendar);
        topPanel.add(searchField);
        topPanel.add(cbMedioDePago);
        topPanel.add(filtrarButton);

        // Tabla de ventas
        String[] columnNames = {"Nombre", "Artículos", "Medio de pago", "Importe"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable libroTable = new JTable(tableModel);
        libroTable.setRowHeight(50);
        libroTable.setBackground(new Color(240, 233, 225));
        libroTable.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        libroTable.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 14));
        libroTable.getTableHeader().setBackground(new Color(220, 204, 181));
        libroTable.getTableHeader().setForeground(Color.BLACK);
        JScrollPane tableScrollPane = new JScrollPane(libroTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        // Panel inferior
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 5));
        bottomPanel.setBackground(new Color(220, 204, 181));
        bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));
        bottomPanel.setPreferredSize(new Dimension(0, 70));

        // Agregar componentes a la ventana
        libroFrame.add(topPanel, BorderLayout.NORTH);
        libroFrame.add(tableScrollPane, BorderLayout.CENTER);
        libroFrame.add(bottomPanel, BorderLayout.SOUTH);

        cargarLibroDiario(tableModel, sucursalActual);

        //Listener
        filtrarButton.addActionListener(e -> {
            filtrarLibroDiario(calendar, searchField.getText(), (String) cbMedioDePago.getSelectedItem(), sucursalActual, tableModel);
            searchField.setText("");
        });

        // Mostrar la ventana
        libroFrame.setVisible(true);
    }

    private static void cargarLibroDiario(DefaultTableModel tableModel, String sucursal) {

        fechaActual = formatoFecha.format(new Date());

        try (Connection connection = DriverManager.getConnection(url)) {
            tableModel.setRowCount(0); // Limpiar la tabla antes de cargar datos

            String query = "SELECT clienteVenta, articulosVenta, montoVenta, medioDePagoVenta FROM libro_diario WHERE sucursalVenta = ? AND fechaVenta = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, sucursal);
            statement.setString(2, fechaActual);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String cliente = resultSet.getString("clienteVenta");
                String articulos = resultSet.getString("articulosVenta");
                double monto = resultSet.getDouble("montoVenta");
                String pago = resultSet.getString("medioDePagoVenta");

                tableModel.addRow(new Object[]{cliente, articulos, pago, monto});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el libro diario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void filtrarLibroDiario(JCalendar calendar, String nombreCliente, String pago, String sucursal, DefaultTableModel tableModel) {

        // Obtener valores de los filtros
        Date fechaSeleccionada = calendar.getDate();

        // Construir la consulta SQL dinámicamente
        StringBuilder query = new StringBuilder("SELECT * FROM libro_diario WHERE 1=1");
        List<Object> parametros = new ArrayList<>();

        // Filtrar por fecha (si se seleccionó una)
        if (fechaSeleccionada != null) {
            query.append(" AND DATE(fechaVenta) = ?");
            String fechaFormateada = new SimpleDateFormat("yyyy-MM-dd").format(fechaSeleccionada);
            parametros.add(fechaFormateada);
        }

        // Filtrar por nombre del cliente (si se ingresó)
        if (!nombreCliente.isEmpty()) {
            query.append(" AND clienteVenta LIKE ?");
            parametros.add("%" + nombreCliente + "%");
        }

        // Filtrar por método de pago (si se seleccionó y no es "Todos")
        if (pago != null && !pago.equals("Todo")) {
            query.append(" AND medioDePagoVenta = ?");
            parametros.add(pago);
        }

        query.append(" AND sucursalVenta = ?");
        parametros.add(sucursal);

        try (Connection connection = DriverManager.getConnection(url)) {
            PreparedStatement statement = connection.prepareStatement(query.toString());

            // Asignar parámetros a la consulta
            for (int i = 0; i < parametros.size(); i++) {
                statement.setObject(i + 1, parametros.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            tableModel.setRowCount(0); // Limpiar la tabla antes de agregar nuevos resultados

            // Agregar resultados a la tabla
            while (resultSet.next()) {
                String cliente = resultSet.getString("clienteVenta");
                String articulos = resultSet.getString("articulosVenta");
                double monto = resultSet.getDouble("montoVenta");
                String medioPago = resultSet.getString("medioDePagoVenta");

                tableModel.addRow(new Object[]{cliente, articulos, medioPago, monto});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al filtrar ventas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void generarArchivoLibroDiario(String sucursal, String fecha) {
        com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
        try {
            String nombreArchivo = "C:/Users/Usuario/Arca Home/libro diario " + sucursal + "/LibroDiario_" + sucursal + "_" + fecha + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            // Fuente personalizada
            Font tituloFuente = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, com.itextpdf.text.BaseColor.DARK_GRAY);
            Font textoFuente = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, com.itextpdf.text.BaseColor.BLACK);

            // Título del PDF
            com.itextpdf.text.Paragraph titulo = new com.itextpdf.text.Paragraph("Libro Diario - : " + sucursal, tituloFuente);
            titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(new com.itextpdf.text.Paragraph("Fecha: " + fecha, textoFuente));
            documento.add(new com.itextpdf.text.Paragraph("\n"));

            // Conectar a la base de datos
            Connection connection = DriverManager.getConnection(url);

            // Consulta para obtener las ventas del día para la sucursal
            String query = "SELECT clienteVenta, fechaVenta, montoVenta, medioDePagoVenta FROM libro_diario WHERE sucursalVenta = ? AND fechaVenta = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, sucursal);
            statement.setString(2, fecha);

            ResultSet resultSet = statement.executeQuery();

            // Tabla para mostrar las ventas
            com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(4); // 4 columnas: Cliente, Fecha, Total, Medio de Pago
            tabla.setWidthPercentage(100);
            tabla.setWidths(new int[]{3, 3, 2, 2}); // Proporción de columnas

            // Encabezados de la tabla
            tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("Cliente", textoFuente)));
            tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("Fecha", textoFuente)));
            tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("Total", textoFuente)));
            tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("Medio de Pago", textoFuente)));

            double totalDiario = 0;

            // Agregar las ventas a la tabla
            while (resultSet.next()) {
                tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase(resultSet.getString("cliente"), textoFuente)));
                tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase(resultSet.getString("fecha"), textoFuente)));
                double totalVenta = resultSet.getDouble("total");
                tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("$" + totalVenta, textoFuente)));
                tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase(resultSet.getString("medioPago"), textoFuente)));
                totalDiario += totalVenta;
            }

            documento.add(tabla);
            documento.add(new com.itextpdf.text.Paragraph("\n"));

            // Total del día
            com.itextpdf.text.Paragraph totalDia = new com.itextpdf.text.Paragraph("Total del Día: $" + totalDiario, tituloFuente);
            totalDia.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            documento.add(totalDia);

            JOptionPane.showMessageDialog(null, "Libro Diario generado exitosamente: " + nombreArchivo);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el libro diario: " + e.getMessage());
        } finally {
            documento.close();
        }
    }

    public static void iniciarGeneracionAutomaticaPDF() {
        Timer timer = new java.util.Timer();

        // Calcular la hora para ejecutar la tarea: 23:59 cada día
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 0);

        Date horaEjecucion = calendario.getTime();
        long intervaloDia = 24 * 60 * 60 * 1000; // 24 horas en milisegundos

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Obtener la fecha actual en formato YYYY-MM-DD
                String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                // Generar el PDF para cada sucursal
                generarArchivoLibroDiario("Oncativo", fechaActual);
                generarArchivoLibroDiario("Oliva", fechaActual);
            }
        }, horaEjecucion, intervaloDia);
    }
}




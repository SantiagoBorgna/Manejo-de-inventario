import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InterfazPrincipal {

    public static String sucursalActual = "";

    private static String busquedaActual;

    public static void abrirInterfazPrincipal(){

        sucursalActual = "Oncativo";

        // Marco principal
        JFrame frame = new JFrame("El Arca Home");
        frame.setIconImage(App.logo);
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
        btnReposition.setFont(new Font("Arial", Font.PLAIN, 24));
        btnReposition.setMargin(new Insets(10, 15, 10, 15));

        btnNewItem.setBackground(new Color(240, 233, 225));
        btnNewItem.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnNewItem.setMargin(new Insets(10, 15, 10, 15));

        btnUpdate.setBackground(new Color(240, 233, 225));
        btnUpdate.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnUpdate.setMargin(new Insets(10, 15, 10, 15));

        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.setMargin(new Insets(5, 10, 5, 10));

        cbSucursal.setBackground(new Color(240, 233, 225));
        cbSucursal.setFont(new Font("Roboto", Font.PLAIN, 24));
        cbSucursal.setPreferredSize(new Dimension(130, 57));

        btnSearch.setBackground(new Color(240, 233, 225));
        btnSearch.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnSearch.setMargin(new Insets(10, 15, 10, 15));

        btnSale.setBackground(new Color(240, 233, 225));
        btnSale.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnSale.setMargin(new Insets(10, 15, 10, 15));

        btnLibroDiario.setBackground(new Color(240, 233, 225));
        btnLibroDiario.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnLibroDiario.setMargin(new Insets(10, 15, 10, 15));

        btnProveedores.setBackground(new Color(240, 233, 225));
        btnProveedores.setFont(new Font("Roboto", Font.PLAIN, 24));
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
        JPanel panelDetalle = Articulos.crearPanelDetalle();
        centerPanel.add(panelDetalle, BorderLayout.CENTER);

        // Panel para lista de elementos
        JPanel listPanel = new JPanel();
        listPanel.setPreferredSize(new Dimension(500, 595));
        listPanel.setLayout(new BorderLayout());

        String[] listColumns = {"Nombre", "Descripción", "Cantidad Oncativo", "Cantidad Oliva", "Cantidad El rincon", "Total", "Precio de compra", "Precio de venta", "Ganancia"};
        DefaultTableModel listTableModel = new DefaultTableModel(listColumns, 0);
        Articulos.iniciarActualizacionAutomatica(listTableModel);
        App.listTable = new JTable(listTableModel);
        App.listTable.setRowHeight(25);
        App.listTable.setBackground(new Color(240, 233, 225));
        App.listTable.setFont(new Font("Arial", Font.PLAIN, 14));
        App.listTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        App.listTable.getTableHeader().setBackground(new Color(220, 204, 181));
        App.listTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane listScrollPane = new JScrollPane(App.listTable);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        centerPanel.add(listPanel, BorderLayout.SOUTH);

        // cargar datos de sqlite
        Articulos.loadDataFromSQLite(listTableModel);

        // prender el método para generar los pdf
        GeneradorArchivos.iniciarGeneracionAutomaticaPDF();

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
            DefaultTableModel model = (DefaultTableModel) App.listTable.getModel();
            Articulos.filtrarArticulos(busquedaActual, model);
            txtSearch.setText("");
        });

        btnNewItem.addActionListener(e ->{
            Articulos.abrirFormularioNuevoArticulo();
        });

        btnReposition.addActionListener(e ->{
            Articulos.reponerArticulo();
        });

        btnUpdate.addActionListener(e ->{
            Articulos.definirArticulo();
        });

        btnSale.addActionListener(e ->{
            Ventas.abrirInterfazVenta(sucursalActual);
        });

        btnLibroDiario.addActionListener(e ->{
            LibroDiario.abrirInterfazLibroDiario(sucursalActual);
        });

        btnProveedores.addActionListener(e ->{
            Proveedores.abrirInterfazProveedores();
        });

        App.listTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && App.listTable.getSelectedRow() != -1) {
                Articulos.mostrarDetalleArticulo();
            }
        });

        // Hacer visible la interfaz
        frame.setVisible(true);
    }

    public static void abrirInterfazPrincipal2(){

        sucursalActual = "Oliva";

        // Marco principal
        JFrame frame = new JFrame("El Arca Home");
        frame.setIconImage(App.logo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1400, 800));

        // Panel superior
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 35, 6));
        topPanel.setBackground(new Color(220, 204, 181)); // Color beige 179, 144, 122

        JButton btnReposition = new JButton("Reposición");
        JButton btnNewItem = new JButton("Nuevo artículo");
        JButton btnUpdate = new JButton("Actualizar artículo");
        JTextField txtSearch = new JTextField("", 15);
        JButton btnSearch = new JButton("Buscar");
        JButton btnSale = new JButton("Venta");
        JButton btnLibroDiario = new JButton("Libro diario");

        btnReposition.setBackground(new Color(240, 233, 225));
        btnReposition.setFont(new Font("Arial", Font.PLAIN, 24));
        btnReposition.setMargin(new Insets(10, 15, 10, 15));

        btnNewItem.setBackground(new Color(240, 233, 225));
        btnNewItem.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnNewItem.setMargin(new Insets(10, 15, 10, 15));

        btnUpdate.setBackground(new Color(240, 233, 225));
        btnUpdate.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnUpdate.setMargin(new Insets(10, 15, 10, 15));

        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.setMargin(new Insets(5, 10, 5, 10));

        btnSearch.setBackground(new Color(240, 233, 225));
        btnSearch.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnSearch.setMargin(new Insets(10, 15, 10, 15));

        btnSale.setBackground(new Color(240, 233, 225));
        btnSale.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnSale.setMargin(new Insets(10, 15, 10, 15));

        btnLibroDiario.setBackground(new Color(240, 233, 225));
        btnLibroDiario.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnLibroDiario.setMargin(new Insets(10, 15, 10, 15));

        topPanel.add(btnReposition);
        topPanel.add(btnNewItem);
        topPanel.add(btnUpdate);
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnSale);
        topPanel.add(btnLibroDiario);

        // Panel central
        JPanel centerPanel = new JPanel(new BorderLayout());
        // Panel de detalle de articulo
        JPanel panelDetalle = Articulos.crearPanelDetalle();
        centerPanel.add(panelDetalle, BorderLayout.CENTER);

        // Panel para lista de elementos
        JPanel listPanel = new JPanel();
        listPanel.setPreferredSize(new Dimension(500, 595));
        listPanel.setLayout(new BorderLayout());

        String[] listColumns = {"Nombre", "Descripción", "Cantidad en stock", "Precio de compra", "Precio de venta", "Ganancia"};
        DefaultTableModel listTableModel = new DefaultTableModel(listColumns, 0);
        Articulos.iniciarActualizacionAutomatica2(listTableModel);
        App.listTable = new JTable(listTableModel);
        App.listTable.setRowHeight(25);
        App.listTable.setBackground(new Color(240, 233, 225));
        App.listTable.setFont(new Font("Arial", Font.PLAIN, 14));
        App.listTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        App.listTable.getTableHeader().setBackground(new Color(220, 204, 181));
        App.listTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane listScrollPane = new JScrollPane(App.listTable);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        centerPanel.add(listPanel, BorderLayout.SOUTH);

        // cargar datos de sqlite
        Articulos.loadDataFromSQLite2(listTableModel);

        // prender el método para generar los pdf
        GeneradorArchivos.iniciarGeneracionAutomaticaPDF();

        // Agregar componentes al marco
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Eventos de botones
        btnSearch.addActionListener(e -> {
            busquedaActual = txtSearch.getText().trim();
            DefaultTableModel model = (DefaultTableModel) App.listTable.getModel();
            Articulos.filtrarArticulos2(busquedaActual, model);
            txtSearch.setText("");
        });

        btnNewItem.addActionListener(e ->{
            Articulos.abrirFormularioNuevoArticulo2();
        });

        btnReposition.addActionListener(e ->{
            Articulos.reponerArticulo2();
        });

        btnUpdate.addActionListener(e ->{
            Articulos.definirArticulo();
        });

        btnSale.addActionListener(e ->{
            Ventas.abrirInterfazVenta(sucursalActual);
        });

        btnLibroDiario.addActionListener(e ->{
            LibroDiario.abrirInterfazLibroDiario(sucursalActual);
        });

        App.listTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && App.listTable.getSelectedRow() != -1) {
                Articulos.mostrarDetalleArticulo2();
            }
        });

        // Hacer visible la interfaz
        frame.setVisible(true);
    }

    public static void abrirInterfazPrincipal3(){

        sucursalActual = "Oncativo";

        // Marco principal
        JFrame frame = new JFrame("El Arca Home");
        frame.setIconImage(App.logo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1400, 800));

        // Panel superior
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 35, 6));
        topPanel.setBackground(new Color(220, 204, 181));

        JButton btnReposition = new JButton("Reposición");
        JButton btnNewItem = new JButton("Nuevo artículo");
        JButton btnUpdate = new JButton("Actualizar artículo");
        JComboBox<String> cbSucursal = new JComboBox<>(new String[]{"Oncativo", "Oliva"});
        JTextField txtSearch = new JTextField("", 15);
        JButton btnSearch = new JButton("Buscar");
        JButton btnSale = new JButton("Venta");
        JButton btnLibroDiario = new JButton("Libro diario");

        btnReposition.setBackground(new Color(240, 233, 225));
        btnReposition.setFont(new Font("Arial", Font.PLAIN, 24));
        btnReposition.setMargin(new Insets(10, 15, 10, 15));

        btnNewItem.setBackground(new Color(240, 233, 225));
        btnNewItem.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnNewItem.setMargin(new Insets(10, 15, 10, 15));

        btnUpdate.setBackground(new Color(240, 233, 225));
        btnUpdate.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnUpdate.setMargin(new Insets(10, 15, 10, 15));

        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.setMargin(new Insets(5, 10, 5, 10));

        cbSucursal.setBackground(new Color(240, 233, 225));
        cbSucursal.setFont(new Font("Roboto", Font.PLAIN, 24));
        cbSucursal.setPreferredSize(new Dimension(130, 57));

        btnSearch.setBackground(new Color(240, 233, 225));
        btnSearch.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnSearch.setMargin(new Insets(10, 15, 10, 15));

        btnSale.setBackground(new Color(240, 233, 225));
        btnSale.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnSale.setMargin(new Insets(10, 15, 10, 15));

        btnLibroDiario.setBackground(new Color(240, 233, 225));
        btnLibroDiario.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnLibroDiario.setMargin(new Insets(10, 15, 10, 15));

        topPanel.add(btnReposition);
        topPanel.add(btnNewItem);
        topPanel.add(btnUpdate);
        topPanel.add(cbSucursal);
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnSale);
        topPanel.add(btnLibroDiario);

        // Panel central
        JPanel centerPanel = new JPanel(new BorderLayout());
        // Panel de detalle de articulo
        JPanel panelDetalle = Articulos.crearPanelDetalle();
        centerPanel.add(panelDetalle, BorderLayout.CENTER);

        // Panel para lista de elementos
        JPanel listPanel = new JPanel();
        listPanel.setPreferredSize(new Dimension(500, 595));
        listPanel.setLayout(new BorderLayout());

        String[] listColumns = {"Nombre", "Descripción", "Cantidad Oncativo", "Cantidad Oliva", "Total", "Precio de venta"};
        DefaultTableModel listTableModel = new DefaultTableModel(listColumns, 0);
        Articulos.iniciarActualizacionAutomatica3(listTableModel);
        App.listTable = new JTable(listTableModel);
        App.listTable.setRowHeight(25);
        App.listTable.setBackground(new Color(240, 233, 225));
        App.listTable.setFont(new Font("Arial", Font.PLAIN, 14));
        App.listTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        App.listTable.getTableHeader().setBackground(new Color(220, 204, 181));
        App.listTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane listScrollPane = new JScrollPane(App.listTable);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        centerPanel.add(listPanel, BorderLayout.SOUTH);

        // cargar datos de sqlite
        Articulos.loadDataFromSQLite3(listTableModel);

        // prender el método para generar los pdf
        GeneradorArchivos.iniciarGeneracionAutomaticaPDF();

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
            DefaultTableModel model = (DefaultTableModel) App.listTable.getModel();
            Articulos.filtrarArticulos3(busquedaActual, model);
            txtSearch.setText("");
        });

        btnNewItem.addActionListener(e ->{
            Articulos.abrirFormularioNuevoArticulo3();
        });

        btnReposition.addActionListener(e ->{
            Articulos.reponerArticulo3();
        });

        btnUpdate.addActionListener(e ->{
            Articulos.definirArticulo();
        });

        btnSale.addActionListener(e ->{
            Ventas.abrirInterfazVenta(sucursalActual);
        });

        btnLibroDiario.addActionListener(e ->{
            LibroDiario.abrirInterfazLibroDiario(sucursalActual);
        });

        App.listTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && App.listTable.getSelectedRow() != -1) {
                Articulos.mostrarDetalleArticulo3();
            }
        });

        // Hacer visible la interfaz
        frame.setVisible(true);
    }

    public static void abrirInterfazPrincipal4(){

        sucursalActual = "El rincon";
        // Marco principal
        JFrame frame = new JFrame("El rincón");
        frame.setIconImage(App.logo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1400, 800));

        // Panel superior
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 6));
        topPanel.setBackground(new Color(220, 204, 181));

        JButton btnReposition = new JButton("Reposición");
        JButton btnNewItem = new JButton("Nuevo artículo");
        JButton btnUpdate = new JButton("Actualizar artículo");
        JTextField txtSearch = new JTextField("", 15);
        JButton btnSearch = new JButton("Buscar");
        JButton btnSale = new JButton("Venta");
        JButton btnLibroDiario = new JButton("Libro diario");

        btnReposition.setBackground(new Color(240, 233, 225));
        btnReposition.setFont(new Font("Arial", Font.PLAIN, 24));
        btnReposition.setMargin(new Insets(10, 15, 10, 15));

        btnNewItem.setBackground(new Color(240, 233, 225));
        btnNewItem.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnNewItem.setMargin(new Insets(10, 15, 10, 15));

        btnUpdate.setBackground(new Color(240, 233, 225));
        btnUpdate.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnUpdate.setMargin(new Insets(10, 15, 10, 15));

        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.setMargin(new Insets(5, 10, 5, 10));

        btnSearch.setBackground(new Color(240, 233, 225));
        btnSearch.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnSearch.setMargin(new Insets(10, 15, 10, 15));

        btnSale.setBackground(new Color(240, 233, 225));
        btnSale.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnSale.setMargin(new Insets(10, 15, 10, 15));

        btnLibroDiario.setBackground(new Color(240, 233, 225));
        btnLibroDiario.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnLibroDiario.setMargin(new Insets(10, 15, 10, 15));

        topPanel.add(btnReposition);
        topPanel.add(btnNewItem);
        topPanel.add(btnUpdate);
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnSale);
        topPanel.add(btnLibroDiario);

        // Panel central
        JPanel centerPanel = new JPanel(new BorderLayout());
        // Panel de detalle de articulo
        JPanel panelDetalle = Articulos.crearPanelDetalle();
        centerPanel.add(panelDetalle, BorderLayout.CENTER);

        // Panel para lista de elementos
        JPanel listPanel = new JPanel();
        listPanel.setPreferredSize(new Dimension(500, 595));
        listPanel.setLayout(new BorderLayout());

        String[] listColumns = {"Nombre", "Descripción", "Cantidad", "Precio de venta"};
        DefaultTableModel listTableModel = new DefaultTableModel(listColumns, 0);
        Articulos.iniciarActualizacionAutomatica4(listTableModel);
        App.listTable = new JTable(listTableModel);
        App.listTable.setRowHeight(25);
        App.listTable.setBackground(new Color(240, 233, 225));
        App.listTable.setFont(new Font("Arial", Font.PLAIN, 14));
        App.listTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        App.listTable.getTableHeader().setBackground(new Color(220, 204, 181));
        App.listTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane listScrollPane = new JScrollPane(App.listTable);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        centerPanel.add(listPanel, BorderLayout.SOUTH);

        // cargar datos de sqlite
        Articulos.loadDataFromSQLite4(listTableModel);

        // prender el método para generar los pdf
        GeneradorArchivos.iniciarGeneracionAutomaticaPDF();

        // Agregar componentes al marco
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Eventos de botones
        btnSearch.addActionListener(e -> {
            busquedaActual = txtSearch.getText().trim();
            DefaultTableModel model = (DefaultTableModel) App.listTable.getModel();
            Articulos.filtrarArticulos4(busquedaActual, model);
            txtSearch.setText("");
        });

        btnNewItem.addActionListener(e ->{
            Articulos.abrirFormularioNuevoArticulo4();
        });

        btnReposition.addActionListener(e ->{
            Articulos.reponerArticulo4();
        });

        btnUpdate.addActionListener(e ->{
            Articulos.definirArticulo();
        });

        btnSale.addActionListener(e ->{
            Ventas.abrirInterfazVenta(sucursalActual);
        });

        btnLibroDiario.addActionListener(e ->{
            LibroDiario.abrirInterfazLibroDiario(sucursalActual);
        });

        App.listTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && App.listTable.getSelectedRow() != -1) {
                Articulos.mostrarDetalleArticulo4();
            }
        });

        // Hacer visible la interfaz
        frame.setVisible(true);
    }
}

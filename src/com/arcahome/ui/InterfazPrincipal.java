package com.arcahome.ui;

import com.arcahome.dao.ArticuloDAO;
import com.arcahome.logic.Inicio;
import com.arcahome.main.App;
import com.arcahome.logic.GeneradorArchivos;
import com.arcahome.model.Articulo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InterfazPrincipal {

    public static String sucursalActual = "";
    private static ArticulosUI articulosUI = new ArticulosUI();
    private static ProveedoresUI proveedoresUI = new ProveedoresUI();
    private static CuentasCorrientesUI cuentasCorrientesUI = new CuentasCorrientesUI();
    private static NotasCreditoUI notasCreditoUI = new NotasCreditoUI();
    private static VentasUI ventasUI = new VentasUI();
    private static LibroDiarioUI libroDiarioUI = new LibroDiarioUI();

    public static void abrirInterfaz(String rol) {
        sucursalActual = (rol.equals("admin") || rol.equals("empleado2")) ? "Oliva" : "Oncativo";

        JFrame frame = new JFrame("El Arca Home - " + sucursalActual);
        frame.setIconImage(App.logo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1400, 800));
        frame.setLayout(new BorderLayout());

        // PANEL SUPERIOR
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        topPanel.setBackground(new Color(220, 204, 181));

        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(180, 140, 90)), // Borde inferior
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding interno
        ));

        // GRUPO 1: GESTIÓN DE ARTÍCULOS
        JButton btnReposition = crearBotonTop("Reponer");
        JButton btnNewItem = crearBotonTop("<html><center>Nuevo<br>artículo</center></html>");
        JButton btnUpdate = crearBotonTop("<html><center>Actualizar<br>artículo</center></html>");

        topPanel.add(btnReposition);
        topPanel.add(btnNewItem);
        topPanel.add(btnUpdate);

        // Separador visual
        topPanel.add(crearSeparador());

        // GRUPO 2: SUCURSAL
        JComboBox<String> cbSucursal = new JComboBox<>(new String[]{"Oncativo", "Oliva"});
        if (rol.equals("superadmin")) {
            cbSucursal.setSelectedItem(sucursalActual);
            cbSucursal.setFont(new Font("Roboto", Font.PLAIN, 15));
            cbSucursal.setBackground(new Color(240, 233, 225));
            cbSucursal.setPreferredSize(new Dimension(90, 50));
            topPanel.add(cbSucursal);
            topPanel.add(crearSeparador());
        }

        // GRUPO 3: BÚSQUEDA
        JTextField txtSearch = new JTextField("", 15);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(250, 50));

        JButton btnSearch = crearBotonTop("Buscar");

        topPanel.add(txtSearch);
        topPanel.add(btnSearch);

        // Separador visual
        topPanel.add(crearSeparador());

        // GRUPO 4: OPERACIONES
        JButton btnSale = crearBotonTop("Venta");

        JButton btnLibroDiario = crearBotonTop("<html><center>Libro<br>Diario</center></html>");
        JButton btnCuentasCorrientes = crearBotonTop("<html><center>Cuentas<br>Corrientes</center></html>");
        JButton btnNotasCredito = crearBotonTop("<html><center>Notas de<br>Crédito</center></html>");

        topPanel.add(btnSale);
        topPanel.add(btnLibroDiario);

        if (rol.equals("superadmin")) {
            JButton btnProveedores = crearBotonTop("Proveedores");
            topPanel.add(btnProveedores);
            btnProveedores.addActionListener(e -> proveedoresUI.abrirInterfaz());
        }

        topPanel.add(btnCuentasCorrientes);
        topPanel.add(btnNotasCredito);

        if (rol.equals("superadmin") || rol.equals("empleado1")){
            // Separador visual
            topPanel.add(crearSeparador());

            // GRUPO 5: TIENDA WEB
            JButton btnPedidosWeb = crearBotonTop("<html><center>Pedidos<br>Web</center></html>");
            JButton btnEnviosWeb = crearBotonTop("<html><center>Envios<br>Web</center></html>");
            topPanel.add(btnPedidosWeb);
            topPanel.add(btnEnviosWeb);
            btnPedidosWeb.addActionListener(e -> new PedidosWebUI(false).abrirInterfaz());
            btnEnviosWeb.addActionListener(e -> new PedidosWebUI(true).abrirInterfaz());
        }

        // PANEL CENTRAL
        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel panelDetalle = articulosUI.crearPanelDetalle();
        panelDetalle.setPreferredSize(new Dimension(0, 280));
        centerPanel.add(panelDetalle, BorderLayout.NORTH);

        // Configuración de Tabla
        String[] listColumns = getColumnasSegunRol(rol);
        DefaultTableModel listTableModel = new DefaultTableModel(listColumns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        App.listTable = new JTable(listTableModel);
        configurarEstiloTabla(rol);

        JScrollPane listScrollPane = new JScrollPane(App.listTable);
        centerPanel.add(listScrollPane, BorderLayout.CENTER);

        // Carga inicial
        articulosUI.cargarTablaArticulos(listTableModel, sucursalActual);
        articulosUI.iniciarTimerActualizacion(sucursalActual);
        GeneradorArchivos.iniciarGeneracionAutomaticaPDF();

        // EVENTOS
        btnSearch.addActionListener(e -> {
            articulosUI.filtrarArticulos(txtSearch.getText().trim());
            txtSearch.setText("");
        });
        btnNewItem.addActionListener(e -> articulosUI.abrirFormularioArticulo(null));
        btnReposition.addActionListener(e -> articulosUI.abrirReposicion(obtenerIdSeleccionado(), obtenerNombreSeleccionado()));
        btnUpdate.addActionListener(e -> {
            Articulo articuloSeleccionado = obtenerArticuloSeleccionado();

            if (articuloSeleccionado != null) {
                articulosUI.abrirFormularioArticulo(articuloSeleccionado);
            } else {
                JOptionPane.showMessageDialog(frame, "Debes seleccionar un artículo para actualizar.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });
        btnSale.addActionListener(e -> ventasUI.abrirInterfaz());
        btnLibroDiario.addActionListener(e -> libroDiarioUI.abrirInterfaz());
        btnCuentasCorrientes.addActionListener(e -> cuentasCorrientesUI.abrirInterfaz());
        btnNotasCredito.addActionListener(e -> notasCreditoUI.abrirInterfaz());

        cbSucursal.addActionListener(e -> {
            sucursalActual = (String) cbSucursal.getSelectedItem();
            articulosUI.refrescarTabla(sucursalActual);
        });

        App.listTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && App.listTable.getSelectedRow() != -1) {
                articulosUI.mostrarDetalleArticulo();
            }
        });

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JButton crearBotonTop(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(80, 60));
        btn.setBackground(new Color(240, 233, 225));
        btn.setFont(new Font("Roboto", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 1, true));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(250, 245, 240));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(240, 233, 225));
            }
        });
        return btn;
    }

    // Línea vertical sutil para separar grupos de botones
    private static JComponent crearSeparador() {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(10, 70));
        sep.setForeground(new Color(180, 140, 90));
        sep.setBackground(new Color(220, 204, 181));
        return sep;
    }

    private static GridBagConstraints getGBC(int x, GridBagConstraints gbc) {
        gbc.gridx = x;
        return gbc;
    }

    private static String[] getColumnasSegunRol(String rol) {
        if (rol.equals("superadmin"))
            return new String[]{"ID", "Nombre", "Descripción", "Oncativo", "Oliva", "Total", "Precio Compra", "Precio Venta", "Ganancia"};
        if (rol.equals("admin"))
            return new String[]{"ID", "Nombre", "Descripción", "Stock", "Precio Compra", "Precio Venta", "Ganancia"};
        return new String[]{"ID", "Nombre", "Descripción", "Stock", "Precio Venta"};
    }

    private static void configurarEstiloTabla(String rol) {
        App.listTable.setRowHeight(30);
        App.listTable.setBackground(new Color(240, 233, 225));
        App.listTable.setFont(new Font("Arial", Font.PLAIN, 16));
        App.listTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 16));
        App.listTable.getTableHeader().setBackground(new Color(220, 204, 181));
        App.listTable.getTableHeader().setForeground(Color.BLACK);
        App.listTable.setSelectionBackground(new Color(180, 140, 90));
        App.listTable.getTableHeader().setReorderingAllowed(false);

        App.listTable.getColumnModel().getColumn(0).setMinWidth(0);
        App.listTable.getColumnModel().getColumn(0).setMaxWidth(0);
        App.listTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        // Orden
        javax.swing.table.TableRowSorter<javax.swing.table.TableModel> sorter = new javax.swing.table.TableRowSorter<>(App.listTable.getModel());
        App.listTable.setRowSorter(sorter);

        java.util.Comparator<Object> comparadorNumerico = (o1, o2) -> {
            String s1 = (o1 == null) ? "0" : o1.toString();
            String s2 = (o2 == null) ? "0" : o2.toString();

            s1 = s1.replaceAll("[^0-9.,-]", "").trim();
            s2 = s2.replaceAll("[^0-9.,-]", "").trim();

            Double d1 = parsearNormalizado(s1);
            Double d2 = parsearNormalizado(s2);

            return d1.compareTo(d2);
        };

        // Lógica específica para Empleados
        if (rol.equals("empleado1") || rol.equals("empleado2")) {
            // Estructura: 0:ID | 1:Nombre | 2:Descripción | 3:Cantidad | 4:Precio Venta

            // Columna NOMBRE (Índice 1): Ancho medio
            App.listTable.getColumnModel().getColumn(1).setPreferredWidth(250);

            // Columna DESCRIPCIÓN (Índice 2): Mas ancho
            App.listTable.getColumnModel().getColumn(2).setPreferredWidth(600);

            sorter.setComparator(3, comparadorNumerico);
            sorter.setComparator(4, comparadorNumerico);

            // Columna CANTIDAD (Índice 3): Bien finita
            App.listTable.getColumnModel().getColumn(3).setMinWidth(80);
            App.listTable.getColumnModel().getColumn(3).setMaxWidth(100);
            App.listTable.getColumnModel().getColumn(3).setPreferredWidth(90);

            // Columna PRECIO (Índice 4): Ancho fijo razonable
            App.listTable.getColumnModel().getColumn(4).setMinWidth(120);
            App.listTable.getColumnModel().getColumn(4).setMaxWidth(200);
            App.listTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        } else if (rol.equals("admin")) {
            App.listTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            App.listTable.getColumnModel().getColumn(2).setPreferredWidth(600);

            for (int i = 3; i <= 6; i++) sorter.setComparator(i, comparadorNumerico);

            App.listTable.getColumnModel().getColumn(3).setMinWidth(80);
            App.listTable.getColumnModel().getColumn(3).setMaxWidth(100);
            App.listTable.getColumnModel().getColumn(3).setPreferredWidth(90);

        } else if (rol.equals("superadmin")){
            App.listTable.getColumnModel().getColumn(1).setPreferredWidth(180);
            App.listTable.getColumnModel().getColumn(2).setPreferredWidth(350);

            for (int i = 3; i <= 8; i++) sorter.setComparator(i, comparadorNumerico);

            int anchoStock = 70;
            for (int i = 3; i <= 5; i++) {
                App.listTable.getColumnModel().getColumn(i).setMinWidth(60);
                App.listTable.getColumnModel().getColumn(i).setMaxWidth(90);
                App.listTable.getColumnModel().getColumn(i).setPreferredWidth(anchoStock);
            }

            int anchoPrecio = 100;
            for (int i = 6; i <= 8; i++) {
                App.listTable.getColumnModel().getColumn(i).setMinWidth(90);
                App.listTable.getColumnModel().getColumn(i).setPreferredWidth(anchoPrecio);
            }
        }

        //Orden inicial
        java.util.List<javax.swing.RowSorter.SortKey> sortKeys = new java.util.ArrayList<>();
        sortKeys.add(new javax.swing.RowSorter.SortKey(1, javax.swing.SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

    private static Double parsearNormalizado(String s) {
        if (s.isEmpty()) return 0.0;
        try {
            if (s.contains(",")) {
                s = s.replace(".", "");
                s = s.replace(",", ".");
            }
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static int obtenerIdSeleccionado() {
        int row = App.listTable.getSelectedRow();
        return (row != -1) ? (int) App.listTable.getModel().getValueAt(App.listTable.convertRowIndexToModel(row), 0) : -1;
    }

    private static String obtenerNombreSeleccionado() {
        int row = App.listTable.getSelectedRow();
        return (row != -1) ? (String) App.listTable.getModel().getValueAt(App.listTable.convertRowIndexToModel(row), 1) : "";
    }

    private static Articulo obtenerArticuloSeleccionado() {
        int id = obtenerIdSeleccionado();
        try { return id != -1 ? new ArticuloDAO().obtenerPorId(id) : null; }
        catch (Exception e) { return null; }
    }
}
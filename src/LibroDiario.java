import com.toedter.calendar.JCalendar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LibroDiario {

    private static SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    private static String fechaActual;

    public static void abrirInterfazLibroDiario(String sucursalActual){

        // Crear ventana de Libro diario
        JFrame libroFrame = new JFrame("Libro Diario - Sucursal " + sucursalActual);
        libroFrame.setIconImage(App.logo);
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
        calendar.setFont(new Font("Roboto", Font.BOLD, 16));
        calendar.getDayChooser().setFont(new Font("Roboto", Font.PLAIN, 14));
        calendar.getMonthChooser().getComboBox().setFont(new Font("Roboto", Font.BOLD, 16));
        // Bordes
        calendar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JComboBox<String> cbMedioDePago = new JComboBox<>(new String[]{"Todo", "Efectivo", "Transferencia", "Tarjeta de crédito", "Tarjeta de débito", "Cheque", "Dólares"});
        cbMedioDePago.setBackground(new Color(240, 233, 225));
        cbMedioDePago.setFont(new Font("Roboto", Font.PLAIN, 24));
        cbMedioDePago.setPreferredSize(new Dimension(  200, 57));

        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton filtrarButton = new JButton("Filtrar");
        filtrarButton.setPreferredSize(new Dimension(130, 60));
        filtrarButton.setBackground(new Color(240, 233, 225));
        filtrarButton.setFont(new Font("Roboto", Font.PLAIN, 24));

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
        libroTable.setFont(new Font("Arial", Font.PLAIN, 14));
        libroTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
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

        try (Connection connection = DriverManager.getConnection(App.url)) {
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

        try (Connection connection = DriverManager.getConnection(App.url)) {
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

}

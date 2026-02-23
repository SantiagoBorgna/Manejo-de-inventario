package com.arcahome.ui;

import com.arcahome.dao.ArticuloDAO;
import com.arcahome.logic.Inicio;
import com.arcahome.model.Articulo;
import com.arcahome.main.App;
import com.arcahome.util.ImgurUploader;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticulosUI {

    private final ArticuloDAO articuloDAO;
    private List<String> rutasImg = new ArrayList<>();
    private int articuloSeleccionadoId = -1;
    private String articuloSeleccionadoNombre = "";

    private JPanel[] imagenPanels = new JPanel[4];
    private JPanel panel;

    private JTextArea detalleArticulo1, detalleArticulo2, detalleArticulo3;
    private JLabel lblImagenArticulo = new JLabel();

    public ArticulosUI() {
        this.articuloDAO = new ArticuloDAO();
    }

    public void cargarTablaArticulos(DefaultTableModel tableModel, String sucursal) {
        tableModel.setRowCount(0); // Limpia la tabla

        try {
            List<Articulo> lista = articuloDAO.listar(sucursal);

            for (Articulo art : lista) {
                Object[] fila;

                //Lógica de Columnas según el Rol
                if (Inicio.rol.equals("superadmin")) {
                    fila = new Object[]{
                            art.getId(), art.getNombre(), art.getDescripcion(),
                            art.getStockOncativo(), art.getStockOliva(), art.getStockTotal(),
                            art.getPrecioCompra(), art.getPrecioVenta(), App.formato1.format(art.getGanancia())
                    };
                }
                else if (Inicio.rol.equals("admin")) {
                    fila = new Object[]{
                            art.getId(), art.getNombre(), art.getDescripcion(),
                            art.getStockOliva(), art.getPrecioCompra(), art.getPrecioVenta(),
                            App.formato1.format(art.getGanancia())
                    };
                }
                else {
                    int stock = sucursal.equals("Oncativo") ? art.getStockOncativo() : art.getStockOliva();
                    fila = new Object[]{art.getId(), art.getNombre(), art.getDescripcion(), stock, art.getPrecioVenta()};
                }
                tableModel.addRow(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos desde la nube", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refrescarTabla(String sucursal) {
        DefaultTableModel model = (DefaultTableModel) App.listTable.getModel();
        cargarTablaArticulos(model, sucursal);
    }

    public void iniciarTimerActualizacion(String sucursal) {
        Timer timer = new Timer(120000, e -> { // 2 minutos
            System.out.println("Actualizando tabla automáticamente...");
            refrescarTabla(sucursal);
        });
        timer.start();
    }

    public void abrirFormularioArticulo(Articulo articuloExistente) {
        boolean esEdicion = (articuloExistente != null);
        JFrame formulario = new JFrame(esEdicion ? "Actualizar Artículo" : "Nuevo Artículo");
        formulario.setIconImage(App.logo);
        formulario.setExtendedState(Frame.MAXIMIZED_BOTH);
        formulario.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel(new SpringLayout());
        panel.setBackground(new Color(240, 233, 225));
        SpringLayout layout = (SpringLayout) panel.getLayout();
        formulario.add(panel);

        Font fuenteLabel = new Font("Roboto", Font.PLAIN, 30);
        Font fuenteInput = new Font("Arial", Font.PLAIN, 18);

        // Componentes base
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(fuenteLabel);
        JTextField txtNombre = new JTextField(esEdicion ? articuloExistente.getNombre() : "");
        txtNombre.setPreferredSize(new Dimension(250, 40));
        txtNombre.setFont(fuenteInput);
        panel.add(lblNombre); panel.add(txtNombre);

        JLabel lblDesc = new JLabel("Descripcion:");
        lblDesc.setFont(fuenteLabel);
        JTextField txtDesc = new JTextField(esEdicion ? articuloExistente.getDescripcion() : "");
        txtDesc.setPreferredSize(new Dimension(250, 40));
        txtDesc.setFont(fuenteInput);
        panel.add(lblDesc); panel.add(txtDesc);

        JLabel lblDim = new JLabel("Dimensiones:");
        lblDim.setFont(fuenteLabel);
        panel.add(lblDim);

        JTextField txtPeso = new JTextField(esEdicion ? String.valueOf(articuloExistente.getPeso()) : "0");
        txtPeso.setPreferredSize(new Dimension(50, 40));
        JLabel labelPeso = new JLabel("Peso (kg)");
        labelPeso.setFont(new Font("Roboto", Font.PLAIN, 12));
        JTextField txtAlto = new JTextField(esEdicion ? String.valueOf(articuloExistente.getAlto()) : "0");
        txtAlto.setPreferredSize(new Dimension(50, 40));
        JLabel labelAlto = new JLabel("Altura (cm)");
        labelAlto.setFont(new Font("Roboto", Font.PLAIN, 12));
        JTextField txtAncho = new JTextField(esEdicion ? String.valueOf(articuloExistente.getAncho()) : "0");
        txtAncho.setPreferredSize(new Dimension(50, 40));
        JLabel labelAncho = new JLabel("Ancho (cm)");
        labelAncho.setFont(new Font("Roboto", Font.PLAIN, 12));
        JTextField txtProf = new JTextField(esEdicion ? String.valueOf(articuloExistente.getProfundidad()) : "0");
        txtProf.setPreferredSize(new Dimension(50, 40));
        JLabel labelProf = new JLabel("Profundidad (cm)");
        labelProf.setFont(new Font("Roboto", Font.PLAIN, 12));

        panel.add(labelPeso); panel.add(labelAlto); panel.add(labelAncho); panel.add(labelProf);
        panel.add(txtPeso); panel.add(txtAlto); panel.add(txtAncho); panel.add(txtProf);

        JLabel lblCant = new JLabel("Cantidad:");
        lblCant.setFont(fuenteLabel);
        int stockVal = esEdicion ? (InterfazPrincipal.sucursalActual.equals("Oncativo") ? articuloExistente.getStockOncativo() : articuloExistente.getStockOliva()) : 0;
        JTextField txtCant = new JTextField(String.valueOf(stockVal));
        txtCant.setPreferredSize(new Dimension(250, 40));
        txtCant.setFont(fuenteInput);
        panel.add(lblCant); panel.add(txtCant);

        JLabel lblCat = new JLabel("Categoria:");
        lblCat.setFont(fuenteLabel);
        JComboBox<String> cbCat = new JComboBox<>(new String[]{"BAZAR", "BAÑO", "DECORACIÓN", "TEXTIL", "SILLONES", "BANQUETAS", "SILLAS", "OFICINA", "MESAS", "MESAS RATONAS", "MUEBLES EXTERIOR"});
        if(esEdicion) cbCat.setSelectedItem(articuloExistente.getCategoria());
        cbCat.setPreferredSize(new Dimension(250, 40));
        cbCat.setFont(fuenteInput);
        panel.add(lblCat); panel.add(cbCat);

        JLabel lblPVenta = new JLabel("Precio de venta:");
        lblPVenta.setFont(fuenteLabel);
        JTextField txtPVenta = new JTextField(esEdicion ? String.valueOf(articuloExistente.getPrecioVenta()) : "0");
        txtPVenta.setPreferredSize(new Dimension(250, 40));
        txtPVenta.setFont(fuenteInput);
        panel.add(lblPVenta); panel.add(txtPVenta);

        // Campos condicionales
        JTextField txtPCompra = new JTextField();
        JLabel lblPCompra = new JLabel("Precio de compra:");
        lblPCompra.setFont(fuenteLabel);
        if (Inicio.rol.equals("superadmin") || Inicio.rol.equals("admin")) {
            txtPCompra.setText(esEdicion ? String.valueOf(articuloExistente.getPrecioCompra()) : "0");
            txtPCompra.setPreferredSize(new Dimension(250, 40));
            txtPCompra.setFont(fuenteInput);
            panel.add(lblPCompra); panel.add(txtPCompra);
        }

        JTextField txtProveedor = new JTextField();
        JLabel lblProv = new JLabel("Proveedor:");
        lblProv.setFont(fuenteLabel);
        if (Inicio.rol.equals("superadmin")) {
            txtProveedor.setText(esEdicion ? articuloExistente.getProveedor() : "");
            txtProveedor.setPreferredSize(new Dimension(250, 40));
            txtProveedor.setFont(fuenteInput);
            panel.add(lblProv); panel.add(txtProveedor);
        }

        JButton btnImg = new JButton("Agregar Imágenes");
        btnImg.setPreferredSize(new Dimension(240, 60));
        btnImg.setBackground(new Color(220, 204, 181));
        btnImg.setFont(new Font("Roboto", Font.PLAIN, 24));
        panel.add(btnImg);

        for (int i = 0; i < 4; i++) {
            JPanel pi = new JPanel(new BorderLayout());
            pi.setPreferredSize(new Dimension(110, 130));
            pi.setBackground(new Color(240, 233, 225));
            JLabel lbl = new JLabel(); lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JButton btnE = new JButton("X"); btnE.setEnabled(false); btnE.setForeground(Color.RED);
            pi.add(lbl, BorderLayout.CENTER); pi.add(btnE, BorderLayout.SOUTH);
            imagenPanels[i] = pi;
            panel.add(pi);
            layout.putConstraint(SpringLayout.WEST, pi, 60, SpringLayout.WEST, btnImg);
            if (i == 0) layout.putConstraint(SpringLayout.NORTH, pi, 20, SpringLayout.SOUTH, btnImg);
            else layout.putConstraint(SpringLayout.NORTH, pi, 10, SpringLayout.SOUTH, imagenPanels[i-1]);
        }

        JButton btnGuardar = new JButton(esEdicion ? "Actualizar" : "Guardar");
        btnGuardar.setPreferredSize(new Dimension(160, 80));
        btnGuardar.setBackground(new Color(220, 204, 181));
        btnGuardar.setFont(new Font("Roboto", Font.PLAIN, 24));
        panel.add(btnGuardar);

        int inputX = 500;

        // Nombre
        layout.putConstraint(SpringLayout.NORTH, txtNombre, 50, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, txtNombre, inputX, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, lblNombre, -20, SpringLayout.WEST, txtNombre);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblNombre, 0, SpringLayout.VERTICAL_CENTER, txtNombre);

        // Descripcion
        layout.putConstraint(SpringLayout.NORTH, txtDesc, 30, SpringLayout.SOUTH, txtNombre);
        layout.putConstraint(SpringLayout.WEST, txtDesc, inputX, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, lblDesc, -20, SpringLayout.WEST, txtDesc);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblDesc, 0, SpringLayout.VERTICAL_CENTER, txtDesc);

        // Dimensiones (Fila de 4 inputs)
        layout.putConstraint(SpringLayout.NORTH, txtPeso, 40, SpringLayout.SOUTH, txtDesc);
        layout.putConstraint(SpringLayout.WEST, txtPeso, inputX, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, labelPeso, -20, SpringLayout.NORTH, txtPeso);
        layout.putConstraint(SpringLayout.WEST, labelPeso, 0, SpringLayout.WEST, txtPeso);
        layout.putConstraint(SpringLayout.EAST, lblDim, -20, SpringLayout.WEST, txtPeso);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblDim, 0, SpringLayout.VERTICAL_CENTER, txtPeso);

        layout.putConstraint(SpringLayout.WEST, txtAlto, 20, SpringLayout.EAST, txtPeso);
        layout.putConstraint(SpringLayout.NORTH, txtAlto, 0, SpringLayout.NORTH, txtPeso);
        layout.putConstraint(SpringLayout.NORTH, labelAlto, -20, SpringLayout.NORTH, txtAlto);
        layout.putConstraint(SpringLayout.WEST, labelAlto, 0, SpringLayout.WEST, txtAlto);

        layout.putConstraint(SpringLayout.WEST, txtAncho, 20, SpringLayout.EAST, txtAlto);
        layout.putConstraint(SpringLayout.NORTH, txtAncho, 0, SpringLayout.NORTH, txtPeso);
        layout.putConstraint(SpringLayout.NORTH, labelAncho, -20, SpringLayout.NORTH, txtAncho);
        layout.putConstraint(SpringLayout.WEST, labelAncho, 0, SpringLayout.WEST, txtAncho);

        layout.putConstraint(SpringLayout.WEST, txtProf, 20, SpringLayout.EAST, txtAncho);
        layout.putConstraint(SpringLayout.NORTH, txtProf, 0, SpringLayout.NORTH, txtPeso);
        layout.putConstraint(SpringLayout.NORTH, labelProf, -20, SpringLayout.NORTH, txtProf);
        layout.putConstraint(SpringLayout.WEST, labelProf, 0, SpringLayout.WEST, txtProf);

        // Cantidad
        layout.putConstraint(SpringLayout.NORTH, txtCant, 30, SpringLayout.SOUTH, txtPeso);
        layout.putConstraint(SpringLayout.WEST, txtCant, inputX, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, lblCant, -20, SpringLayout.WEST, txtCant);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblCant, 0, SpringLayout.VERTICAL_CENTER, txtCant);

        // Categoria
        layout.putConstraint(SpringLayout.NORTH, cbCat, 30, SpringLayout.SOUTH, txtCant);
        layout.putConstraint(SpringLayout.WEST, cbCat, inputX, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, lblCat, -20, SpringLayout.WEST, cbCat);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblCat, 0, SpringLayout.VERTICAL_CENTER, cbCat);

        // Precios
        Component ultimoRef = lblCat;
        if (Inicio.rol.contains("admin")) {
            layout.putConstraint(SpringLayout.NORTH, txtPCompra, 30, SpringLayout.SOUTH, ultimoRef);
            layout.putConstraint(SpringLayout.WEST, txtPCompra, inputX, SpringLayout.WEST, panel);
            layout.putConstraint(SpringLayout.EAST, lblPCompra, -20, SpringLayout.WEST, txtPCompra);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblPCompra, 0, SpringLayout.VERTICAL_CENTER, txtPCompra);
            ultimoRef = txtPCompra;
        }

        layout.putConstraint(SpringLayout.NORTH, txtPVenta, 30, SpringLayout.SOUTH, ultimoRef);
        layout.putConstraint(SpringLayout.WEST, txtPVenta, inputX, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, lblPVenta, -20, SpringLayout.WEST, txtPVenta);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblPVenta, 0, SpringLayout.VERTICAL_CENTER, txtPVenta);

        // Proveedor
        ultimoRef = txtPVenta;
        if (Inicio.rol.contains("admin")) {
            layout.putConstraint(SpringLayout.NORTH, txtProveedor, 30, SpringLayout.SOUTH, ultimoRef);
            layout.putConstraint(SpringLayout.WEST, txtProveedor, inputX, SpringLayout.WEST, panel);
            layout.putConstraint(SpringLayout.EAST, lblProv, -20, SpringLayout.WEST, txtProveedor);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblProv, 0, SpringLayout.VERTICAL_CENTER, txtProveedor);

            ultimoRef = txtProveedor;
        }

        // Botón Guardar
        layout.putConstraint(SpringLayout.NORTH, btnGuardar, 20, SpringLayout.SOUTH, ultimoRef);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnGuardar, 0, SpringLayout.HORIZONTAL_CENTER, panel);

        // Panel de Imágenes (A la derecha)
        layout.putConstraint(SpringLayout.WEST, btnImg, 80, SpringLayout.EAST, txtNombre);
        layout.putConstraint(SpringLayout.NORTH, btnImg, 0, SpringLayout.NORTH, txtNombre);

        // Acciones
        btnImg.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            if (fc.showOpenDialog(formulario) == JFileChooser.APPROVE_OPTION) {
                for (File f : fc.getSelectedFiles()) {
                    try {
                        String url = ImgurUploader.subirImagen(f);
                        for (int i=0; i<4; i++) if (rutasImg.get(i).isEmpty()) { rutasImg.set(i, url); break; }
                        actualizarImagenes();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(formulario, "Error Imgur"); }
                }
            }
        });

        btnGuardar.addActionListener(e -> {
            try {
                Articulo art = esEdicion ? articuloExistente : new Articulo();
                art.setNombre(txtNombre.getText());
                art.setDescripcion(txtDesc.getText());
                art.setPrecioVenta(Double.parseDouble(txtPVenta.getText()));
                art.setPeso(Double.parseDouble(txtPeso.getText()));
                art.setAlto(Double.parseDouble(txtAlto.getText()));
                art.setAncho(Double.parseDouble(txtAncho.getText()));
                art.setProfundidad(Double.parseDouble(txtProf.getText()));
                art.setCategoria((String)cbCat.getSelectedItem());
                art.setImagen1(rutasImg.size() > 0 ? rutasImg.get(0) : "");
                art.setImagen2(rutasImg.size() > 1 ? rutasImg.get(1) : "");
                art.setImagen3(rutasImg.size() > 2 ? rutasImg.get(2) : "");
                art.setImagen4(rutasImg.size() > 3 ? rutasImg.get(3) : "");
                if (panel.isAncestorOf(txtPCompra)) art.setPrecioCompra(Double.parseDouble(txtPCompra.getText()));
                if (panel.isAncestorOf(txtProveedor)) art.setProveedor(txtProveedor.getText());
                int c = Integer.parseInt(txtCant.getText());
                if (InterfazPrincipal.sucursalActual.equals("Oncativo")) art.setStockOncativo(c); else art.setStockOliva(c);
                art.setCiudad(InterfazPrincipal.sucursalActual);
                art.setNegocio("Arca home");

                if (esEdicion) articuloDAO.actualizar(art); else articuloDAO.insertar(art);
                formulario.dispose();
                refrescarTabla(InterfazPrincipal.sucursalActual);
            } catch (Exception ex) { JOptionPane.showMessageDialog(formulario, "Error en datos"); }
        });

        rutasImg.clear();
        for(int i=0; i<4; i++) rutasImg.add("");
        formulario.setVisible(true);
    }

    private void actualizarImagenes() {
        for (int i = 0; i < 4; i++) {
            JLabel lblImagen = (JLabel) imagenPanels[i].getComponent(0);
            JButton btnEliminar = (JButton) imagenPanels[i].getComponent(1);

            // Limpiar imagen y eliminar cualquier ActionListener anterior
            lblImagen.setIcon(null);
            for (ActionListener al : btnEliminar.getActionListeners()) {
                btnEliminar.removeActionListener(al);
            }

            String url = rutasImg.get(i);
            if (url != null && !url.isEmpty()) {
                try {
                    ImageIcon icon = new ImageIcon(new ImageIcon(new URL(url)).getImage()
                            .getScaledInstance(110, 110, Image.SCALE_SMOOTH));
                    lblImagen.setIcon(icon);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                btnEliminar.setEnabled(true);
                final int index = i;
                btnEliminar.addActionListener(e -> {
                    rutasImg.set(index, "");  // dejar vacío ese slot
                    actualizarImagenes();
                });
            } else {
                btnEliminar.setEnabled(false);  // no hay imagen, desactivar botón
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    public void abrirReposicion(int idSeleccionado, String nombreArticulo) {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un artículo.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFrame reposicionFrame = new JFrame("Reposición de Artículo");
        reposicionFrame.setIconImage(App.logo);
        reposicionFrame.setSize(600, 320);
        reposicionFrame.setLocationRelativeTo(null);
        reposicionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reposicionFrame.setResizable(false);

        JPanel panelReposicion = new JPanel(new SpringLayout());
        panelReposicion.setBackground(new Color(240, 233, 225));
        SpringLayout layout = (SpringLayout) panelReposicion.getLayout();
        reposicionFrame.add(panelReposicion);

        // COMPONENTES
        JLabel labelNombre = new JLabel("Reponiendo: " + nombreArticulo);
        labelNombre.setFont(new Font("Roboto", Font.PLAIN, 24));
        panelReposicion.add(labelNombre);

        JLabel labelCantidad = new JLabel("Cantidad:");
        labelCantidad.setFont(new Font("Roboto", Font.PLAIN, 24));
        panelReposicion.add(labelCantidad);

        JTextField txtCantidad = new JTextField();
        txtCantidad.setPreferredSize(new Dimension(250, 40));
        txtCantidad.setFont(new Font("Arial", Font.PLAIN, 24));
        panelReposicion.add(txtCantidad);

        JButton btnReponer = new JButton("Reponer");
        btnReponer.setPreferredSize(new Dimension(200, 70));
        btnReponer.setBackground(new Color(220, 204, 181));
        btnReponer.setFont(new Font("Roboto", Font.PLAIN, 24));
        panelReposicion.add(btnReponer);

        // Posicionamiento
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelNombre, 0, SpringLayout.HORIZONTAL_CENTER, panelReposicion);
        layout.putConstraint(SpringLayout.NORTH, labelNombre, 30, SpringLayout.NORTH, panelReposicion);

        layout.putConstraint(SpringLayout.NORTH, txtCantidad, 100, SpringLayout.NORTH, panelReposicion);
        layout.putConstraint(SpringLayout.WEST, txtCantidad, 250, SpringLayout.WEST, panelReposicion);
        layout.putConstraint(SpringLayout.EAST, labelCantidad, -20, SpringLayout.WEST, txtCantidad);
        layout.putConstraint(SpringLayout.NORTH, labelCantidad, 4, SpringLayout.NORTH, txtCantidad);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnReponer, 0, SpringLayout.HORIZONTAL_CENTER, panelReposicion);
        layout.putConstraint(SpringLayout.NORTH, btnReponer, 180, SpringLayout.NORTH, panelReposicion);

        // Acciones
        btnReponer.addActionListener(e -> {
            String cantidadStr = txtCantidad.getText().trim();
            if (cantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(reposicionFrame, "Por favor, ingresa una cantidad.");
                return;
            }

            try {
                int cantidad = Integer.parseInt(cantidadStr);

                boolean exito = articuloDAO.actualizarStock(idSeleccionado, cantidad, InterfazPrincipal.sucursalActual);

                if (exito) {
                    JOptionPane.showMessageDialog(reposicionFrame, "Stock actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    reposicionFrame.dispose();
                    refrescarTabla(InterfazPrincipal.sucursalActual);
                } else {
                    JOptionPane.showMessageDialog(reposicionFrame, "No se pudo actualizar el stock.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(reposicionFrame, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(reposicionFrame, "Error de conexión.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reposicionFrame.setVisible(true);
    }

    public void filtrarArticulos(String busqueda) {
        DefaultTableModel tableModel = (DefaultTableModel) App.listTable.getModel();
        tableModel.setRowCount(0);

        try {
            List<Articulo> lista = articuloDAO.buscar(busqueda, InterfazPrincipal.sucursalActual);

            for (Articulo art : lista) {
                Object[] fila;

                if (Inicio.rol.equals("superadmin")) {
                    fila = new Object[]{
                            art.getId(), art.getNombre(), art.getDescripcion(),
                            art.getStockOncativo(), art.getStockOliva(), art.getStockTotal(),
                            art.getPrecioCompra(), art.getPrecioVenta(), App.formato1.format(art.getGanancia())
                    };
                }
                else if (Inicio.rol.equals("admin")) {
                    fila = new Object[]{
                            art.getId(), art.getNombre(), art.getDescripcion(),
                            art.getStockOliva(), art.getPrecioCompra(), art.getPrecioVenta(),
                            App.formato1.format(art.getGanancia())
                    };
                }
                else {
                    int stock = InterfazPrincipal.sucursalActual.equals("Oncativo") ? art.getStockOncativo() : art.getStockOliva();
                    fila = new Object[]{
                            art.getId(), art.getNombre(), art.getDescripcion(),
                            stock, art.getPrecioVenta()
                    };
                }
                tableModel.addRow(fila);
            }

            App.listTable.clearSelection();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar artículos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarDetalleArticulo() {
        lblImagenArticulo.setIcon(null);
        lblImagenArticulo.setText("Cargando...");

        int filaSeleccionada = App.listTable.getSelectedRow();
        if (filaSeleccionada == -1) return;

        int modelRow = App.listTable.convertRowIndexToModel(filaSeleccionada);
        DefaultTableModel model = (DefaultTableModel) App.listTable.getModel();

        int idArticulo = (int) model.getValueAt(modelRow, 0);
        this.articuloSeleccionadoId = idArticulo;

        try {
            Articulo art = articuloDAO.obtenerPorId(idArticulo);
            if (art == null) return;

            this.articuloSeleccionadoNombre = art.getNombre();

            // Formateamos detalles según ROL
            String d1 = "Nombre: " + art.getNombre() + "\n\nDescripción: " + art.getDescripcion();
            String d2 = "";
            String d3 = "";

            if (Inicio.rol.equals("superadmin")) {
                d2 = "Stock Oncativo: " + art.getStockOncativo() + "\n\nStock Oliva: " + art.getStockOliva() +
                        "\n\nTotal: " + art.getStockTotal();
                d3 = "Costo: $" + App.formato1.format(art.getPrecioCompra()) + "\n\nVenta: $" + App.formato1.format(art.getPrecioVenta()) +
                        "\n\nGanancia: $" + App.formato1.format(art.getGanancia());
            }
            else if (Inicio.rol.equals("admin")) {
                d2 = "Stock Oliva: " + art.getStockOliva();
                d3 = "Costo: $" + App.formato1.format(art.getPrecioCompra()) + "\n\nVenta: $" + App.formato1.format(art.getPrecioVenta());
            }
            else {
                int stockLocal = InterfazPrincipal.sucursalActual.equals("Oncativo") ? art.getStockOncativo() : art.getStockOliva();
                d2 = "Stock disponible: " + stockLocal;
                d3 = "Precio de venta: $" + App.formato1.format(art.getPrecioVenta());
            }

            detalleArticulo1.setText(d1);
            detalleArticulo2.setText(d2);
            detalleArticulo3.setText(d3);

            // Carga de Imagen
            if (art.getImagen1() != null && !art.getImagen1().isEmpty()) {
                new Thread(() -> {
                    try {
                        ImageIcon icono = new ImageIcon(new URL(art.getImagen1()));
                        Image img = icono.getImage().getScaledInstance(260, 260, Image.SCALE_SMOOTH);
                        SwingUtilities.invokeLater(() -> {
                            lblImagenArticulo.setText("");
                            lblImagenArticulo.setIcon(new ImageIcon(img));
                        });
                    } catch (Exception e) {
                        SwingUtilities.invokeLater(() -> lblImagenArticulo.setText("Sin imagen"));
                    }
                }).start();
            } else {
                lblImagenArticulo.setText("Sin imagen");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener detalles.");
        }
    }

    public JPanel crearPanelDetalle() {
        JPanel panelDetalle = new JPanel(new BorderLayout());
        panelDetalle.setPreferredSize(new Dimension(500, 280));
        panelDetalle.setBackground(new Color(240, 233, 225));

        // Panel Imagen
        JPanel panelImagen = new JPanel(new GridLayout(1, 1));
        panelImagen.setPreferredSize(new Dimension(280, 280));
        panelImagen.setBackground(new Color(240, 233, 225));
        panelImagen.setBorder(BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true));

        lblImagenArticulo.setHorizontalAlignment(JLabel.CENTER);
        panelImagen.add(lblImagenArticulo);
        panelDetalle.add(panelImagen, BorderLayout.WEST);

        // Panel Info (3 Columnas)
        JPanel panelInfo = new JPanel(new GridLayout(1, 3));
        panelInfo.setOpaque(false);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 140, 90), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        detalleArticulo1 = crearTextArea();
        detalleArticulo2 = crearTextArea();
        detalleArticulo3 = crearTextArea();

        panelInfo.add(new JScrollPane(detalleArticulo1) {{ setBorder(null); setOpaque(false); getViewport().setOpaque(false); }});
        panelInfo.add(new JScrollPane(detalleArticulo2) {{ setBorder(null); setOpaque(false); getViewport().setOpaque(false); }});
        panelInfo.add(new JScrollPane(detalleArticulo3) {{ setBorder(null); setOpaque(false); getViewport().setOpaque(false); }});

        panelDetalle.add(panelInfo, BorderLayout.CENTER);
        return panelDetalle;
    }

    private JTextArea crearTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Roboto", Font.BOLD, 20));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return textArea;
    }
}
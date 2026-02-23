package com.arcahome.logic;

import com.arcahome.main.App;
import com.arcahome.ui.InterfazPrincipal;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Inicio {
    public static String rol = "";

    public static void mostrarLogin() {
        JFrame loginFrame = new JFrame("Inicio de Sesión");
        loginFrame.setIconImage(App.logo);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setSize(530, 370);
        loginFrame.setLocationRelativeTo(null);

        JPanel panelLogin = new JPanel();
        panelLogin.setBackground(new Color(240, 233, 225));
        SpringLayout layout = new SpringLayout();
        panelLogin.setLayout(layout);
        loginFrame.add(panelLogin, BorderLayout.CENTER);

        // Fuentes
        Font labelFont = new Font("Roboto", Font.PLAIN, 26);
        Font inputFont = new Font("Arial", Font.PLAIN, 26);
        Font buttonFont = new Font("Roboto", Font.PLAIN, 24);

        // Etiquetas y campos
        JLabel labelUsuario = new JLabel("Usuario:");
        labelUsuario.setFont(labelFont);
        panelLogin.add(labelUsuario);

        JTextField txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(220, 45));
        txtUsuario.setFont(inputFont);
        panelLogin.add(txtUsuario);

        JLabel labelPass = new JLabel("Contraseña:");
        labelPass.setFont(labelFont);
        panelLogin.add(labelPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(220, 45));
        txtPass.setFont(inputFont);
        panelLogin.add(txtPass);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(200, 60));
        btnLogin.setBackground(new Color(220, 204, 181));
        btnLogin.setFont(buttonFont);
        btnLogin.setMargin(new Insets(10, 12, 10, 12));
        panelLogin.add(btnLogin);

        int xLabel = 60;
        int xField = 240;

        // Usuario
        layout.putConstraint(SpringLayout.WEST, labelUsuario, xLabel, SpringLayout.WEST, panelLogin);
        layout.putConstraint(SpringLayout.NORTH, labelUsuario, 50, SpringLayout.NORTH, panelLogin);

        layout.putConstraint(SpringLayout.WEST, txtUsuario, xField, SpringLayout.WEST, panelLogin);
        layout.putConstraint(SpringLayout.NORTH, txtUsuario, 46, SpringLayout.NORTH, panelLogin);

        // Contraseña
        layout.putConstraint(SpringLayout.WEST, labelPass, xLabel, SpringLayout.WEST, panelLogin);
        layout.putConstraint(SpringLayout.NORTH, labelPass, 115, SpringLayout.NORTH, panelLogin);

        layout.putConstraint(SpringLayout.WEST, txtPass, xField, SpringLayout.WEST, panelLogin);
        layout.putConstraint(SpringLayout.NORTH, txtPass, 111, SpringLayout.NORTH, panelLogin);

        // Botón (más abajo que antes)
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnLogin, 0, SpringLayout.HORIZONTAL_CENTER, panelLogin);
        layout.putConstraint(SpringLayout.NORTH, btnLogin, 205, SpringLayout.NORTH, panelLogin);

        // Acción
        btnLogin.addActionListener(e -> {
            String user = txtUsuario.getText();
            String password = new String(txtPass.getPassword());

            rol = autenticarUsuario(user, password);
            if (rol != null) {
                loginFrame.dispose();
                iniciarAplicacionSegunRol(rol);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.setVisible(true);
    }

    private static String autenticarUsuario(String user, String password) {
        String rol = null;
        try (Connection conn = com.arcahome.db.DatabaseConnection.getConnection()) {
            String query = "SELECT rolUsuario FROM usuarios WHERE nombreUsuario = ? AND passUsuario = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, user);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                rol = resultSet.getString("rolUsuario");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión con AWS");
            e.printStackTrace();
        }
        return rol;
    }

    private static void iniciarAplicacionSegunRol(String rol) {
        Inicio.rol = rol;

        InterfazPrincipal.abrirInterfaz(rol);
    }
}

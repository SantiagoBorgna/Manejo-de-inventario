import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Inicio {
    static String rol = "";

    public static void mostrarLogin() {
        JFrame loginFrame = new JFrame("Inicio de Sesión");
        loginFrame.setIconImage(App.logo);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setSize(600, 350);
        loginFrame.setLocation(480, 200);

        JPanel panelLogin = new JPanel();
        panelLogin.setBackground(new Color(240, 233, 225));
        Container container = new Container();
        SpringLayout layout = new SpringLayout();
        panelLogin.setLayout(layout);
        loginFrame.add(panelLogin, BorderLayout.CENTER);

        JLabel labelUsuario = new JLabel("Usuario:");
        labelUsuario.setFont(new Font("Roboto", Font.PLAIN, 22));
        panelLogin.add(labelUsuario);
        JTextField txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(250, 40));
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 22));
        panelLogin.add(txtUsuario);

        JLabel labelPass = new JLabel("Contraseña:");
        labelPass.setFont(new Font("Roboto", Font.PLAIN, 22));
        panelLogin.add(labelPass);
        JPasswordField txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(250, 40));
        txtPass.setFont(new Font("Arial", Font.PLAIN, 22));
        panelLogin.add(txtPass);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(200, 80));
        btnLogin.setBackground(new Color(220, 204, 181)); //255, 248, 220
        btnLogin.setFont(new Font("Roboto", Font.PLAIN, 24));
        btnLogin.setMargin(new Insets(10, 12, 10, 12));
        panelLogin.add(btnLogin);

        //Acomodar componentes
        //Usuario
        layout.putConstraint(SpringLayout.WEST, labelUsuario, 50, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelUsuario, 45, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtUsuario, 300, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtUsuario, 40, SpringLayout.NORTH, container);
        //Contraseña
        layout.putConstraint(SpringLayout.WEST, labelPass, 50, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, labelPass, 105, SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.WEST, txtPass, 300, SpringLayout.EAST, container);
        layout.putConstraint(SpringLayout.NORTH, txtPass, 100, SpringLayout.NORTH, container);
        //btn login
        layout.putConstraint(SpringLayout.WEST, btnLogin, 200, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, btnLogin, 180, SpringLayout.NORTH, container);

        btnLogin.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String password = new String(txtPass.getPassword());

            rol = autenticarUsuario(usuario, password);
            if (rol != null) {
                loginFrame.dispose();
                iniciarAplicacionSegunRol(rol);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
    }

    private static String autenticarUsuario(String usuario, String password) {
        String rol = null;

        try (Connection conn = DriverManager.getConnection(App.url)) {
            String query = "SELECT rolUsuario FROM usuarios WHERE nombreUsuario = ? AND passUsuario = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, usuario);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                rol = resultSet.getString("rolUsuario");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rol;
    }

    private static void iniciarAplicacionSegunRol(String rol) {
        switch (rol) {
            case "superadmin": //victoria
                InterfazPrincipal.abrirInterfazPrincipal(); // Acceso total
                break;
            case "admin": //admin oliva
                InterfazPrincipal.abrirInterfazPrincipal2();
                break;
            case "empleado1": //empleado el arca
                InterfazPrincipal.abrirInterfazPrincipal3();
                break;
            case "empleado2": //empleado el rincon
                InterfazPrincipal.abrirInterfazPrincipal4();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Acceso no permitido", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
        }
    }
}

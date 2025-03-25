import javax.swing.*;
import java.awt.*;

public class App {

    static String url = "jdbc:mysql://127.0.0.1:3306/arcadb";

    static String usuario = "santi";

    static String contrasena = "";

    static Image logo = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/logo arca.png"));

    static JTable listTable;

    public static void main(String[] args) {
        Inicio.mostrarLogin();
    }
}
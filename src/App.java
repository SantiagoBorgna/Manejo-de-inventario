import javax.swing.*;
import java.awt.*;

public class App {

    static String rolIngresado = "";

    static String url = "jdbc:sqlite:C:/Users/Usuario/Arca Home/Arcadb.db"; // Ruta al archivo de la base de datos

    static Image logo = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/logo arca.png"));

    static JTable listTable;

    public static void main(String[] args) {

        Inicio.mostrarLogin();
    }
}
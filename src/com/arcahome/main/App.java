package com.arcahome.main;

import com.arcahome.logic.Inicio;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.JTable;
import java.text.DecimalFormat;

public class App {

    public static Image logo = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/logo arca.png"));
    public static JTable listTable;
    public static DecimalFormat formato1 = new DecimalFormat("#.00");
    public static String fechaActual = new java.text.SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Inicio.mostrarLogin();
        });
    }
}


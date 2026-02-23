package com.arcahome.logic;

import com.arcahome.dao.LibroDiarioDAO;
import com.arcahome.model.VentaResumen;
import com.arcahome.ui.InterfazPrincipal;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;

public class GeneradorArchivos {

    private static final LibroDiarioDAO dao = new LibroDiarioDAO();

    private static void generarArchivoLibroDiario(String sucursal, String fecha) {
        Document documento = new Document();
        try {
            // Configuración de Ruta
            String desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Libro_Diario_" + sucursal;
            new File(desktopPath).mkdirs();
            String nombreArchivo = desktopPath + File.separator + "LibroDiario_" + sucursal + "_" + fecha + ".pdf";

            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            // Estilos
            Font tituloFuente = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
            Font textoFuente = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            Font headerFuente = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

            // Título
            Paragraph titulo = new Paragraph("Libro Diario - El Arca Home " + sucursal, tituloFuente);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(new Paragraph("Fecha: " + fecha, textoFuente));
            documento.add(new Paragraph("\n"));

            // Obtener Datos desde el DAO
            List<VentaResumen> ventas = dao.listarVentas(sucursal, fecha, "", "Todo");

            // Crear Tabla
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);

            // Encabezados con color
            String[] headers = {"Cliente", "Descripción", "Medio Pago", "Importe"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFuente));
                //cell.setBackgroundColor(new BaseColor(180, 140, 90));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                tabla.addCell(cell);
            }

            double totalDiario = 0;
            for (VentaResumen v : ventas) {
                tabla.addCell(new Phrase(v.getCliente(), textoFuente));
                tabla.addCell(new Phrase(v.getArticulos(), textoFuente));
                tabla.addCell(new Phrase(v.getMedioPago(), textoFuente));
                tabla.addCell(new Phrase("$" + String.format("%.2f", v.getImporte()), textoFuente));
                totalDiario += v.getImporte();
            }

            documento.add(tabla);
            documento.add(new Paragraph("\n"));

            // Total
            Paragraph totalDia = new Paragraph("TOTAL DEL DÍA: $" + String.format("%.2f", totalDiario), tituloFuente);
            totalDia.setAlignment(Element.ALIGN_RIGHT);
            documento.add(totalDia);

            // Solo mostrar mensaje si no es la tarea automática
            // JOptionPane.showMessageDialog(null, "Libro Diario generado para " + sucursal);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            documento.close();
        }
    }

    public static void iniciarGeneracionAutomaticaPDF() {
        Timer timer = new Timer();
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.HOUR_OF_DAY, 20);
        calendario.set(Calendar.MINUTE, 15);
        calendario.set(Calendar.SECOND, 0);

        // Si ya pasaron las 20:15, programar para manana
        if (calendario.getTime().before(new Date())) {
            calendario.add(Calendar.DATE, 1);
        }

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ejecutarSegunSucursal();
            }
        }, calendario.getTime(), 24 * 60 * 60 * 1000);
    }

    public static void iniciarGeneracionPDF() {
        ejecutarSegunSucursal();
        JOptionPane.showMessageDialog(null, "Proceso de generación de PDF finalizado.");
    }

    private static void ejecutarSegunRol() {
        String fecha = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        switch (Inicio.rol) {
            case "superadmin":
                generarArchivoLibroDiario("Oncativo", fecha);
                generarArchivoLibroDiario("Oliva", fecha);
                break;
            case "admin":
            case "empleado2":
                generarArchivoLibroDiario("Oliva", fecha);
                break;
            case "empleado1":
                generarArchivoLibroDiario("Oncativo", fecha);
                break;
        }
    }

    private static void ejecutarSegunSucursal(){
        String fecha = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        switch (InterfazPrincipal.sucursalActual){
            case "Oncativo":
                generarArchivoLibroDiario("Oncativo", fecha);
                break;
            case "Oliva":
                generarArchivoLibroDiario("Oliva", fecha);
                break;
        }
    }
}
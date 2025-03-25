import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GeneradorArchivos {

    private static void generarArchivoLibroDiario(String sucursal, String fecha) {
        com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
        try {
            String nombreArchivo = "C:/Users/santi/Santiago Borgna/Proyecto Arca Home/Arca Home/libro diario " + sucursal + "/LibroDiario_" + sucursal + "_" + fecha + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            // Fuente personalizada
            Font tituloFuente = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, com.itextpdf.text.BaseColor.DARK_GRAY);
            Font textoFuente = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, com.itextpdf.text.BaseColor.BLACK);

            // Título del PDF
            com.itextpdf.text.Paragraph titulo = new com.itextpdf.text.Paragraph("Libro Diario " + sucursal, tituloFuente);
            titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(new com.itextpdf.text.Paragraph("Fecha: " + fecha, textoFuente));
            documento.add(new com.itextpdf.text.Paragraph("\n"));

            // Conectar a la base de datos
            Connection connection = DriverManager.getConnection(App.url, App.usuario, App.contrasena);

            // Consulta para obtener las ventas del día para la sucursal
            String query = "SELECT clienteVenta, fechaVenta, montoVenta, medioDePagoVenta, articulosVenta FROM libro_diario WHERE sucursalVenta = ? AND fechaVenta = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, sucursal);
            statement.setString(2, fecha);

            ResultSet resultSet = statement.executeQuery();

            // Tabla para mostrar las ventas
            com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new int[]{2, 2, 2, 2, 2}); // Proporción de columnas

            // Encabezados de la tabla
            tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("Fecha", textoFuente)));
            tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("Descripción", textoFuente)));
            tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("Medio de Pago", textoFuente)));
            tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("Cliente", textoFuente)));
            tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("Total", textoFuente)));

            double totalDiario = 0;

            // Agregar las ventas a la tabla
            while (resultSet.next()) {
                tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase(resultSet.getString("fechaVenta"), textoFuente)));
                tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase(resultSet.getString("articulosVenta"), textoFuente)));
                tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase(resultSet.getString("medioDePagoVenta"), textoFuente)));
                tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase(resultSet.getString("clienteVenta"), textoFuente)));
                double totalVenta = resultSet.getDouble("montoVenta");
                tabla.addCell(new PdfPCell(new com.itextpdf.text.Phrase("$" + totalVenta, textoFuente)));

                totalDiario += totalVenta;
            }

            documento.add(tabla);
            documento.add(new com.itextpdf.text.Paragraph("\n"));

            // Total del día
            com.itextpdf.text.Paragraph totalDia = new com.itextpdf.text.Paragraph("Total: $" + totalDiario, tituloFuente);
            totalDia.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            documento.add(totalDia);

            JOptionPane.showMessageDialog(null, "Libro Diario generado exitosamente: " + sucursal);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el libro diario: " + e.getMessage());
        } finally {
            documento.close();
        }
    }

    public static void iniciarGeneracionAutomaticaPDF() {
        Timer timer = new java.util.Timer();

        // Calcular la hora para ejecutar la tarea: 20:30 cada día
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.HOUR_OF_DAY, 20);
        calendario.set(Calendar.MINUTE, 30);
        calendario.set(Calendar.SECOND, 0);

        Date horaEjecucion = calendario.getTime();
        long intervaloDia = 24 * 60 * 60 * 1000; // 24 horas en milisegundos

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Obtener la fecha actual en formato dd-MM-yyyy
                String fechaActual = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                // Generar el PDF para cada sucursal
                generarArchivoLibroDiario("Oncativo", fechaActual);
                generarArchivoLibroDiario("Oliva", fechaActual);
                generarArchivoLibroDiario("El rincon", fechaActual);
            }
        }, horaEjecucion, intervaloDia);
    }
}

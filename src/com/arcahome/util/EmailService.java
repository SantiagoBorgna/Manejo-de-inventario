package com.arcahome.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EmailService {
    private static final String API_URL = "https://api.resend.com/emails";

    public static boolean enviarCorreoConfirmacion(String toEmail, String clienteNombre, int idPedido, String resumenArticulos, double total, String metodoEnvio) {
        String apiKey = obtenerApiKey();
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("API Key de Resend no encontrada en config.properties");
            return false;
        }

        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey.trim());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String subject = "¡Pago Exitoso! Tu pedido en El Arca Home (#" + idPedido + ")";
            
            // Format total as Argentine Peso
            String totalStr = String.format(Locale.forLanguageTag("es-AR"), "%,.2f", total);

            String htmlBody = construirHtml(clienteNombre, resumenArticulos, totalStr, metodoEnvio);
            String logoBase64 = obtenerLogoBase64();

            // Escape strings for JSON
            String jsonInputString = "{"
                    + "\"from\": \"ventas@elarcahome.com.ar\","
                    + "\"to\": [\"" + escapeJson(toEmail) + "\"],"
                    + "\"subject\": \"" + escapeJson(subject) + "\","
                    + "\"html\": \"" + escapeJson(htmlBody) + "\"";
            
            if (logoBase64 != null) {
                jsonInputString += ","
                        + "\"attachments\": ["
                        + "  {"
                        + "    \"filename\": \"logo.png\","
                        + "    \"content\": \"" + logoBase64 + "\","
                        + "    \"content_id\": \"logo_arca\""
                        + "  }"
                        + "]";
            }
            jsonInputString += "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code >= 200 && code < 300) {
                return true;
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.err.println("Error Resend (" + code + "): " + response.toString());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String obtenerApiKey() {
        Properties prop = new Properties();
        File f = new File("config.properties");
        if (!f.exists()) return null;
        try (InputStream input = new FileInputStream(f)) {
            prop.load(input);
            return prop.getProperty("RESEND_API_KEY");
        } catch (IOException ex) {
            return null;
        }
    }

    private static String obtenerLogoBase64() {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get("src/com/arcahome/resources/logo arca.png"));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo local: " + e.getMessage());
            return null;
        }
    }

    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "").replace("\r", "");
    }

    private static String construirHtml(String clienteNombre, String resumenArticulos, String total, String metodoEnvio) {
        String safeResumen = (resumenArticulos == null) ? "" : resumenArticulos.replaceAll("\\(id[^\\)]+\\)", "").replace("\n", "<br>");
        String safeMetodo = (metodoEnvio == null) ? "" : metodoEnvio;
        
        String baseHtml = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #000000; margin: 0; padding: 40px 0; background-color: #f9f9f9; width: 100%;'>" +
                "<div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 40px; text-align: center; border: 1px solid #eaeaea; border-radius: 8px;'>" +
                
                "<!-- Logo -->" +
                "<div style='text-align: center; margin-bottom: 20px;'>" +
                "<img src='cid:logo_arca' alt='El Arca Home' width='140' height='140' style='width: 140px; height: 140px; border-radius: 50%; display: inline-block; margin: 0 auto;'/>" +
                "</div>" +

                "<h1 style='font-size: 24px; margin-bottom: 10px; color: #000000;'>¡Pago exitoso, " + clienteNombre + "!</h1>" +
                "<p style='font-size: 14px; color: #000000; margin-bottom: 30px;'>Tu pedido ha sido pagado y confirmado. ¡Ya estamos preparando todo!</p>" +
                
                "<div style='text-align: left;'>" +
                "<h3 style='font-size: 16px; margin-bottom: 15px; font-weight: bold; color: #000000;'>Resumen de tu compra</h3>" +
                "<div style='background-color: #fbfbfb; border: 1px solid #f0f0f0; border-radius: 6px; padding: 20px; margin-bottom: 30px; color: #000000;'>" +
                "<p style='margin: 0 0 10px; font-size: 14px; line-height: 1.5;'>" + safeResumen + "</p>" +
                "<hr style='border: none; border-top: 1px solid #eeeeee; margin: 15px 0;'/>" +
                "<p style='margin: 0 0 15px; font-size: 14px;'><strong>Envío:</strong> " + safeMetodo + "</p>" +
                "<hr style='border: none; border-top: 1px solid #eeeeee; margin: 15px 0;'/>" +
                "<p style='margin: 0; font-size: 16px; text-align: right; color: #000000; font-weight: bold;'>Total: $" + total + "</p>" +
                "</div>" +
                "</div>" +
                
                "<a href='https://elarcahome.com.ar' style='display: inline-block; background-color: #111; color: #fff; text-decoration: none; padding: 12px 24px; font-weight: bold; border-radius: 6px; font-size: 14px;'>Volver a la tienda</a>" +
                
                "<p style='margin-top: 40px; font-size: 11px; color: #999;'>Este es un correo automático de El Arca Home, por favor no respondas a esta dirección.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
        return baseHtml;
    }
}

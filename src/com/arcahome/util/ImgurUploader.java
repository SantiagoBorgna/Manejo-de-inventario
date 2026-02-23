package com.arcahome.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Base64;

public class ImgurUploader {
    static final String CLIENT_ID = "e4a0f4ce256e2ed";

    public static String subirImagen(File imagen) throws IOException {
        URL url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);

        byte[] imageBytes = Files.readAllBytes(imagen.toPath());
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
        String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(encodedImage, "UTF-8");

        try (OutputStream out = conn.getOutputStream()) {
            out.write(data.getBytes());
        }

        if (conn.getResponseCode() == 200) {
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // Extraer la URL de la respuesta JSON
            String json = response.toString();
            int linkIndex = json.indexOf("\"link\":\"") + 8;
            int endIndex = json.indexOf("\"", linkIndex);
            return json.substring(linkIndex, endIndex).replace("\\/", "/");
        } else {
            throw new IOException("Error al subir imagen: " + conn.getResponseMessage());
        }
    }
}
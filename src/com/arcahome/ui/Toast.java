package com.arcahome.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Toast extends JWindow {
    private final int miliseconds;

    public Toast(String message, int miliseconds) {
        this.miliseconds = miliseconds;
        
        setLayout(new BorderLayout());
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.BLACK);
        label.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        add(label, BorderLayout.CENTER);
        
        getContentPane().setBackground(new Color(220, 204, 181, 230));
        pack();
        
        try {
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        } catch (Exception e) {
            // Si la forma redondeada no es soportada
        }
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height - 150);
        
        setAlwaysOnTop(true);
    }

    public void showToast() {
        try {
            if (GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
                setOpacity(0);
                setVisible(true);
                
                // Fade In
                for (double d = 0.0; d < 1.0; d += 0.1) {
                    Thread.sleep(20);
                    setOpacity((float) Math.min(d, 1.0));
                }
                setOpacity(1.0f);
                
                // Esperar
                Thread.sleep(miliseconds);
                
                // Fade Out
                for (double d = 1.0; d > 0.0; d -= 0.1) {
                    Thread.sleep(20);
                    setOpacity((float) Math.max(d, 0.0));
                }
            } else {
                setVisible(true);
                Thread.sleep(miliseconds);
            }
            dispose();
            
        } catch (Exception e) {
            e.printStackTrace();
            dispose();
        }
    }

    public static void mostrar(String message) {
        new Thread(() -> {
            Toast toast = new Toast(message, 3000);
            toast.showToast();
        }).start();
    }
}

package ui;

import ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // PostgreSQL JDBC sürücüsünü yükle
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver bulunamadı.");
            e.printStackTrace();
            return;
        }
        
        // Swing arayüzünü başlat
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
package main;

import com.formdev.flatlaf.FlatDarkLaf; // Import the theme
import ui.LoginScreen;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Apply the modern look and feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize LaF. Using default look and feel.");
        }

        // Run the rest of the application
        SwingUtilities.invokeLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });
    }
}
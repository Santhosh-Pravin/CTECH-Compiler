package ui;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class LockedStudentFrame extends JFrame {

    protected String username;
    protected String name;

    public LockedStudentFrame(String username, String name) {
        this.username = username;
        this.name = name;

        // Apply all locking and fullscreen logic here
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout()); // Set a default layout

        // Add the window listener to prevent closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // This override prevents the window from closing via system commands like Alt+F4
            }
        });

        // Create a consistent bottom panel for the exit button
        JButton requestExit = new JButton("Forfeit and Exit");
        requestExit.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        southPanel.add(requestExit);
        add(southPanel, BorderLayout.SOUTH);

        requestExit.addActionListener(e -> handleExitRequest());
    }

    // A single, reusable method to handle the forfeit logic
    private void handleExitRequest() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to forfeit the test and exit the application?",
                "Confirm Forfeit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0); // Forfeit and close the entire application
        }
    }
}
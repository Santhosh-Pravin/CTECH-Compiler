package ui;

import api.ApiService;
import com.formdev.flatlaf.FlatClientProperties;
import org.json.JSONArray;
import javax.swing.*;
import java.awt.*;

// Extend our new locked frame instead of JFrame
public class StudentDashboard extends LockedStudentFrame {

    public StudentDashboard(String username, String name) {
        // Call the parent constructor to get all the locking features
        super(username, name);
        setTitle("Student Dashboard - " + name);

        // --- Top Panel ---
        JLabel titleLabel = new JLabel("Welcome, " + name, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(45, 45, 45));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel with Buttons ---
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

        JButton btnOpenTest = new JButton("Open Assigned Tests");
        JButton btnViewSubmissions = new JButton("View My Submissions (Coming)");
        JButton btnTakeViva = new JButton("Take Viva (Coming)");

        btnOpenTest.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        btnViewSubmissions.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        btnTakeViva.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);

        centerPanel.add(btnOpenTest);
        centerPanel.add(btnViewSubmissions);
        centerPanel.add(btnTakeViva);
        add(centerPanel, BorderLayout.CENTER);

        // The locking code and exit button are now handled by LockedStudentFrame.

        btnOpenTest.addActionListener(e -> {
            try {
                String response = ApiService.getAvailableSnippets();
                JSONArray snippets = new JSONArray(response);

                if (snippets.length() == 0) {
                    JOptionPane.showMessageDialog(this, "No tests have been assigned yet.", "No Tests", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                new TestListScreen(this.username, this.name, snippets).setVisible(true);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to fetch tests: " + ex.getMessage(), "API Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnViewSubmissions.addActionListener(e -> JOptionPane.showMessageDialog(this, "Functionality to view your submissions is coming soon!"));
        btnTakeViva.addActionListener(e -> JOptionPane.showMessageDialog(this, "Viva functionality is coming soon!"));
    }
}
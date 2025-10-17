package ui;

import api.ApiService;
import com.formdev.flatlaf.FlatClientProperties;
import org.json.JSONArray;
import javax.swing.*;
import java.awt.*;

public class MentorDashboard extends JFrame {
    private final String username;
    private final String name;

    public MentorDashboard(String username, String name) {
        this.username = username;
        this.name = name;
        setTitle("Mentor Dashboard");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Welcome Mentor, " + name, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(3, 1, 10, 20));
        center.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        JButton btnCreateStudent = new JButton("Create Student Account");
        JButton btnCreateSnippet = new JButton("Create & Assign Snippet");
        JButton btnViewResults = new JButton("View/Extract All Submissions");

        // Apply modern button styling
        btnCreateStudent.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        btnCreateSnippet.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        btnViewResults.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);

        center.add(btnCreateStudent);
        center.add(btnCreateSnippet);
        center.add(btnViewResults);
        add(center, BorderLayout.CENTER);

        // --- Action Listeners ---
        btnCreateStudent.addActionListener(e -> handleCreateStudent());
        btnCreateSnippet.addActionListener(e -> handleCreateSnippet());
        btnViewResults.addActionListener(e -> handleViewResults());
    }

    private void handleCreateStudent() {
        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField su = new JTextField();
        JPasswordField sp = new JPasswordField();
        JTextField sname = new JTextField();
        JPasswordField mentorPass = new JPasswordField();
        p.add(new JLabel("Student Username:")); p.add(su);
        p.add(new JLabel("Student Password:")); p.add(sp);
        p.add(new JLabel("Student Name:")); p.add(sname);
        p.add(new JLabel("Your Mentor Password (confirm):")); p.add(mentorPass);

        int ok = JOptionPane.showConfirmDialog(this, p, "Create Student Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok == JOptionPane.OK_OPTION) {
            try {
                ApiService.registerStudent(this.username, new String(mentorPass.getPassword()).trim(),
                        su.getText().trim(), new String(sp.getPassword()).trim(), sname.getText().trim());
                JOptionPane.showMessageDialog(this, "Student created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to create student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleCreateSnippet() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        JTextField titleField = new JTextField();
        JTextArea codeArea = new JTextArea(10, 40);
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        codeArea.setLineWrap(true);
        codeArea.setWrapStyleWord(true);

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        formPanel.add(new JLabel("Snippet Title:"));
        formPanel.add(titleField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(new JLabel("Code Snippet:"));

        p.add(formPanel, BorderLayout.NORTH);
        p.add(new JScrollPane(codeArea), BorderLayout.CENTER);

        int ok = JOptionPane.showConfirmDialog(this, p, "Create New Code Snippet", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (ok == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String code = codeArea.getText().trim();

            if (title.isEmpty() || code.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and code cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                ApiService.createSnippet(title, code);
                JOptionPane.showMessageDialog(this, "Snippet created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to create snippet: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleViewResults() {
        new SwingWorker<JSONArray, Void>() {
            @Override
            protected JSONArray doInBackground() throws Exception {
                String response = ApiService.getAllSubmissions();
                return new JSONArray(response);
            }

            @Override
            protected void done() {
                try {
                    JSONArray submissions = get();
                    if (submissions.length() == 0) {
                        JOptionPane.showMessageDialog(MentorDashboard.this, "No student submissions have been recorded yet.", "No Data", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    new SubmissionsViewerScreen(submissions).setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MentorDashboard.this, "Failed to fetch submissions: " + ex.getMessage(), "API Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}
package ui;

import api.ApiService;
import com.formdev.flatlaf.FlatClientProperties;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton mentorRegisterBtn;

    public LoginScreen() {
        setTitle("CTECH Compiler - Login");
        setSize(420, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Center the panel

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("CTECH Compiler", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        JLabel subtitle = new JLabel("AI-Powered Examination Portal", SwingConstants.CENTER);
        subtitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 20, 10);
        panel.add(subtitle, gbc);
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username");
        panel.add(usernameField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        panel.add(passwordField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        panel.add(loginButton, gbc);

        gbc.gridy = 5;
        mentorRegisterBtn = new JButton("Register First Mentor");
        mentorRegisterBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        panel.add(mentorRegisterBtn, gbc);

        loginButton.addActionListener(e -> handleLogin());

        mentorRegisterBtn.addActionListener(e -> handleMentorRegistration());
    }

    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            JSONObject response = ApiService.login(user, pass);
            String role = response.getString("role");
            String name = response.getString("name");

            dispose();
            if ("MENTOR".equalsIgnoreCase(role))  {
                new MentorDashboard(user, name).setVisible(true);
            } else {
                new StudentDashboard(user, name).setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleMentorRegistration() {
        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField man = new JTextField();
        JPasswordField mp = new JPasswordField();
        JTextField mname = new JTextField();
        p.add(new JLabel("Mentor Username:")); p.add(man);
        p.add(new JLabel("Mentor Password:")); p.add(mp);
        p.add(new JLabel("Mentor Name:")); p.add(mname);
        int ok = JOptionPane.showConfirmDialog(this, p, "Create First Mentor", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            try {
                String res = ApiService.registerMentor(man.getText().trim(), new String(mp.getPassword()).trim(), mname.getText().trim());
                JOptionPane.showMessageDialog(this, "Mentor created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to create mentor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
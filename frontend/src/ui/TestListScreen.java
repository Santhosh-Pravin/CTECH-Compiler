package ui;

import com.formdev.flatlaf.FlatClientProperties;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;

// Extend our new locked frame
public class TestListScreen extends LockedStudentFrame {

    public TestListScreen(String username, String name, JSONArray snippets) {
        // Call the parent to get locking features
        super(username, name);
        setTitle("Available Tests - " + name);

        JLabel titleLabel = new JLabel("Select a test to begin", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < snippets.length(); i++) {
            JSONObject snippet = snippets.getJSONObject(i);
            listPanel.add(createTestPanel(snippet));
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        // Add to CENTER to avoid overlapping the exit button in the SOUTH
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTestPanel(JSONObject snippet) {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        String title = snippet.getString("title");
        // optString safely returns an empty string "" if the key is missing or null
        String code = snippet.optString("code", "");

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Test");
        startButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        startButton.addActionListener(e -> {
            // Open the compiler screen with the selected snippet
            dispose(); // Close the test list
            new CompilerScreen(username, title, code).setVisible(true);
        });
        panel.add(startButton, BorderLayout.EAST);

        return panel;
    }
}
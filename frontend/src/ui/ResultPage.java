package ui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ResultPage extends JFrame {
    public ResultPage(String username) {
        setTitle("CTECH Compiler - Results");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Test Results for " + username, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        //Map<String, String> results = DummyAPI.getResults(username);

        /*JPanel resultPanel = new JPanel(new GridLayout(results.size(), 2, 10, 10));
        for (Map.Entry<String, String> entry : results.entrySet()) {
            resultPanel.add(new JLabel(entry.getKey() + ": "));
            resultPanel.add(new JLabel(entry.getValue()));
        }

        add(resultPanel, BorderLayout.CENTER);

        RoundedButton backBtn = new RoundedButton("Back to Dashboard");
        backBtn.addActionListener(e -> {
            dispose();
            new DashboardScreen(username).setVisible(true);
        });
        add(backBtn, BorderLayout.SOUTH);*/
    }
}

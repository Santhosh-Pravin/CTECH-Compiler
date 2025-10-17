package ui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VivaScreen extends JFrame {
    public VivaScreen(String username) {
        setTitle("AI Viva - CTECH Compiler");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JTextArea vivaQuestions = new JTextArea();
        vivaQuestions.setEditable(false);
        vivaQuestions.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        //List<String> questions = DummyAPI.generateViva(username);
        //vivaQuestions.setText(String.join("\n\n", questions));

        RoundedButton finishBtn = new RoundedButton("Finish Viva");

        add(new JScrollPane(vivaQuestions), BorderLayout.CENTER);
        add(finishBtn, BorderLayout.SOUTH);

        /*finishBtn.addActionListener(e -> {
            int score = DummyAPI.evaluateViva(username);
            JOptionPane.showMessageDialog(this, "Viva completed!\nYour Score: " + score);
            dispose();
            new ResultPage(username).setVisible(true);
        });*/
    }
}

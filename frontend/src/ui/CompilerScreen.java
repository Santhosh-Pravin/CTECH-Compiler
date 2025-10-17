package ui;

import api.ApiService;
import com.formdev.flatlaf.FlatClientProperties;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class CompilerScreen extends LockedStudentFrame {
    private final JTextArea codeArea;
    private final JButton submitBtn;
    private final JButton vivaBtn;
    private String submittedCode = null;
    private Long submissionId = null; // New field to store the submission ID

    public CompilerScreen(String username, String snippetTitle, String snippetCode) {
        super(username, snippetTitle); // Pass title as the "name" for the frame
        setTitle("CTECH Compiler - " + snippetTitle);

        // --- Top Panel ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Compiler:"));
        JComboBox<String> compilerChoice = new JComboBox<>(new String[]{"Java", "C", "C++", "Python", "Shell"});
        topPanel.add(compilerChoice);
        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel ---
        codeArea = new JTextArea(snippetCode);
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        add(new JScrollPane(codeArea), BorderLayout.CENTER);

        // --- Bottom Panel ---
        // Get the panel created by the parent LockedStudentFrame
        JPanel southPanel = (JPanel) getContentPane().getComponent(1);
        southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Ensure right alignment

        submitBtn = new JButton("Submit Code");
        vivaBtn = new JButton("Take Viva");
        submitBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        vivaBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        vivaBtn.setEnabled(false); // Disabled until code is submitted

        // Add our new buttons before the existing "Forfeit" button
        southPanel.add(submitBtn, 0);
        southPanel.add(vivaBtn, 1);

        // --- Action Listeners ---
        submitBtn.addActionListener(e -> handleSubmitCode(username, snippetTitle));
        vivaBtn.addActionListener(e -> handleTakeViva());
    }

    private void handleSubmitCode(String username, String snippetTitle) {
        submittedCode = codeArea.getText().trim();
        if (submittedCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Code cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        submitBtn.setEnabled(false);
        submitBtn.setText("Submitting...");

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                // Call the API to submit the code
                return ApiService.submitCode(username, snippetTitle, submittedCode);
            }

            @Override
            protected void done() {
                try {
                    String response = get(); // Get the full JSON response
                    JSONObject responseJson = new JSONObject(response);
                    submissionId = responseJson.getLong("id"); // IMPORTANT: Capture the ID

                    JOptionPane.showMessageDialog(CompilerScreen.this, "Code submitted successfully! You can now take the viva.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    vivaBtn.setEnabled(true);
                    submitBtn.setText("Submitted");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(CompilerScreen.this, "Submission failed: " + e.getMessage(), "API Error", JOptionPane.ERROR_MESSAGE);
                    submitBtn.setEnabled(true);
                    submitBtn.setText("Submit Code");
                }
            }
        }.execute();
    }

    private void handleTakeViva() {
        if (submittedCode == null || submissionId == null) {
            JOptionPane.showMessageDialog(this, "You must submit your code first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        vivaBtn.setEnabled(false);
        vivaBtn.setText("Generating Question...");

        new SwingWorker<String, Void>() {
            private String question; // To hold the question for the next step

            @Override
            protected String doInBackground() throws Exception {
                // Call the AI to generate a question
                question = ApiService.generateVivaQuestion(submittedCode);
                return question;
            }

            @Override
            protected void done() {
                try {
                    get(); // Check for errors
                    String answer = JOptionPane.showInputDialog(CompilerScreen.this,
                            "<html><b>AI Viva Question:</b><br><p style='width: 300px;'>" + question + "</p></html>",
                            "Viva Exam", JOptionPane.QUESTION_MESSAGE);

                    if (answer != null && !answer.trim().isEmpty()) {
                        // If student provides an answer, call the evaluation service
                        evaluateAnswer(question, answer);
                    } else {
                        JOptionPane.showMessageDialog(CompilerScreen.this, "Viva was cancelled or no answer was provided.", "Viva Cancelled", JOptionPane.WARNING_MESSAGE);
                        vivaBtn.setEnabled(true);
                        vivaBtn.setText("Take Viva Again");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(CompilerScreen.this, "Failed to get viva question: " + e.getMessage(), "API Error", JOptionPane.ERROR_MESSAGE);
                    vivaBtn.setEnabled(true);
                    vivaBtn.setText("Take Viva Again");
                }
            }
        }.execute();
    }

    private void evaluateAnswer(String question, String answer) {
        vivaBtn.setText("Evaluating...");

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                // Call the new evaluation API
                return ApiService.evaluateViva(submissionId, submittedCode, question, answer);
            }

            @Override
            protected void done() {
                try {
                    String evalResponse = get();
                    JSONObject evalJson = new JSONObject(evalResponse);
                    int score = evalJson.getInt("score");
                    String feedback = evalJson.getString("feedback");

                    // Show the final result to the student
                    JOptionPane.showMessageDialog(CompilerScreen.this,
                            "<html><b>Viva Submitted!</b><br><br>Score: <b>" + score + "/10</b><br>Feedback: " + feedback + "</html>",
                            "Result", JOptionPane.INFORMATION_MESSAGE);
                    vivaBtn.setText("Viva Complete");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(CompilerScreen.this, "Failed to evaluate viva answer: " + e.getMessage(), "API Error", JOptionPane.ERROR_MESSAGE);
                    vivaBtn.setEnabled(true);
                    vivaBtn.setText("Take Viva Again");
                }
            }
        }.execute();
    }

}
package ui;

import api.ApiService;
import com.formdev.flatlaf.FlatClientProperties;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SubmissionsViewerScreen extends JFrame {

    public SubmissionsViewerScreen(JSONArray submissions) {
        setTitle("All Student Submissions");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Title ---
        JLabel titleLabel = new JLabel("Submission Results", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- Table of Results ---
        String[] columnNames = {"Student", "Snippet Title", "Viva Score", "Viva Question", "Viva Answer", "AI Feedback"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setDefaultEditor(Object.class, null); // Make table read-only

        // Populate the table with data from the API
        for (int i = 0; i < submissions.length(); i++) {
            JSONObject sub = submissions.getJSONObject(i);
            Object[] row = new Object[]{
                    sub.optString("studentUsername", "N/A"),
                    sub.optString("snippetTitle", "N/A"),
                    sub.optInt("vivaScore", 0),
                    sub.optString("vivaQuestion", "-"),
                    sub.optString("vivaAnswer", "-"),
                    sub.optString("vivaFeedback", "-")
            };
            tableModel.addRow(row);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Bottom Panel with Export Button ---
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton exportBtn = new JButton("Export to Excel");
        exportBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        southPanel.add(exportBtn);
        add(southPanel, BorderLayout.SOUTH);

        // --- Action Listener for the Export Button ---
        exportBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Excel File");
            fileChooser.setSelectedFile(new File("submissions.xlsx"));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                // Use SwingWorker to download the file without freezing the UI
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try (InputStream in = ApiService.downloadSubmissionsFile();
                             OutputStream out = new FileOutputStream(fileToSave)) {
                            in.transferTo(out); // Efficiently copy the downloaded data to the file
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get(); // This will throw an exception if the background task failed
                            JOptionPane.showMessageDialog(SubmissionsViewerScreen.this, "File downloaded successfully to:\n" + fileToSave.getAbsolutePath(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(SubmissionsViewerScreen.this, "Failed to download file: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
            }
        });
    }
}

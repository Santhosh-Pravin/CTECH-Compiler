package com.ctechcompiler.backend.service;

import com.ctechcompiler.backend.model.Submission;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportSubmissionsToExcel(List<Submission> submissions) throws IOException {
        String[] columns = {"Student Username", "Snippet Title", "Viva Score", "Viva Question", "Viva Answer", "AI Feedback", "Submitted Code"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Submissions");

            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
            }

            // Data
            int rowIdx = 1;
            for (Submission submission : submissions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(submission.getStudentUsername());
                row.createCell(1).setCellValue(submission.getSnippetTitle());
                row.createCell(2).setCellValue(submission.getVivaScore());
                row.createCell(3).setCellValue(submission.getVivaQuestion());
                row.createCell(4).setCellValue(submission.getVivaAnswer());
                row.createCell(5).setCellValue(submission.getVivaFeedback());
                row.createCell(6).setCellValue(submission.getSubmittedCode());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}


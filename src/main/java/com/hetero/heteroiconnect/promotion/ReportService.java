package com.hetero.heteroiconnect.promotion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class ReportService {
 
    // Method to generate a PDF report based on the empcode
    public File generatePDF(String empcode) throws JRException, IOException {
        // Path to your JRXML file on the local file system (make sure it's correct)
        String jrxmlFilePath = "C:/promotion/PromotionLetter.jrxml"; // Update with the actual file path

        // Load the JRXML file using FileInputStream
        FileInputStream jrxmlInputStream = new FileInputStream(jrxmlFilePath);

        // Compile the JRXML file into a JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);

        // Set the parameters for the report (empcode will be used in the report)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("EmpCode", 10515);

        // Fill the report with data (using JREmptyDataSource as we are not using any real data)
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        // Define output file for the PDF
        File outputPdf = new File("C:/promotion/employee_report_" + empcode + ".pdf");

        // Export the filled report to PDF
        JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(outputPdf));

        // Close the InputStream after use
        jrxmlInputStream.close();

        return outputPdf; // Return the generated PDF file
    }
}

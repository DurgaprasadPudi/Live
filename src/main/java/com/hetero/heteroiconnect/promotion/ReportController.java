package com.hetero.heteroiconnect.promotion;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;

@RestController
public class ReportController {

    private final ReportService reportService;
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    
    @PostMapping("/downloadReport")
    public ResponseEntity<FileSystemResource> downloadReport(@RequestBody EmpCodeRequest empCodeRequest) {
        if (empCodeRequest.getEmpcode() == null || empCodeRequest.getEmpcode().isEmpty()) {
            //return new ResponseEntity<>("Empcode is required", HttpStatus.BAD_REQUEST); // For bad request
        }

        try {
            // Generate the report and get the PDF file
            File pdfFile = reportService.generatePDF(empCodeRequest.getEmpcode());

            // Create a FileSystemResource to serve the file
            FileSystemResource fileResource = new FileSystemResource(pdfFile);

            // Set response headers to indicate file download
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + pdfFile.getName());
            headers.add("Content-Type", "application/pdf");

            // Return the file as response with proper headers
            return new ResponseEntity<>(fileResource, headers, HttpStatus.OK);

        } catch (IOException | JRException e) {
            logger.error("Error generating report for empcode: {}", empCodeRequest.getEmpcode(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle error gracefully
        }
    }

    public static class EmpCodeRequest {
        private String empcode;

        public String getEmpcode() {
            return empcode;
        }

        public void setEmpcode(String empcode) {
            this.empcode = empcode;
        }
    }
}

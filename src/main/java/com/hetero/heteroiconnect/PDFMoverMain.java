package com.hetero.heteroiconnect;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PDFMoverMain {

    public static void main(String[] args) throws SQLException {
        // Setup JDBC connection
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.30.71:3306/hclhrm_prod"); // Update DB name and port if needed
        dataSource.setUsername("hcluser");
        dataSource.setPassword("hcluser!23");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // Run query
        String sql = "SELECT p.employeesequenceno, BU.CALLNAME AS FLODERNAME, IF(PP.PAN IS NULL OR LENGTH(PP.PAN)=0, 'NA', PP.PAN) AS PAN FROM hclhrm_prod.tbl_employee_primary p LEFT JOIN hclhrm_prod.tbl_employee_personalinfo PP ON p.employeeid = PP.employeeid LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.businessunitid = p.companyid HAVING PAN != 'NA'";


        String sourceDir = "D:\\16AANDB2025\\16AANDB2025\\";
        String targetBase = "D:\\target-folder\\";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> row : results) {
            String folderName = String.valueOf(row.get("FLODERNAME")).trim();
            String pan = String.valueOf(row.get("PAN")).trim();

            if (folderName.equalsIgnoreCase("HYD") || folderName.equalsIgnoreCase("MUM")) {
                Path sourcePath = Paths.get(sourceDir + pan + ".pdf");
                Path targetDir = Paths.get(targetBase + folderName);
                Path targetPath = targetDir.resolve(pan + ".pdf");

                if (Files.exists(sourcePath)) {
                    try {
                        if (!Files.exists(targetDir)) {
                            Files.createDirectories(targetDir);
                        }

                        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Moved: " + pan + ".pdf to " + folderName);
                    } catch (IOException e) {
                        System.err.println("Failed to move " + pan + ": " + e.getMessage());
                    }
                } else {
                    System.out.println("Missing file: " + sourcePath);
                }
            }
        }

        System.out.println("File move operation completed.");
    }
}

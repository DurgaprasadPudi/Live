package com.hetero.heteroiconnect.payslips;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONArray;

@RestController
@RequestMapping("/Payslips")
public class Payslipscontroller {
  
	    @Autowired
	    PayslipRepository reposity;
	    
	    @Autowired 
	    private EntityManager entityManager;
	    
		@Autowired
		JdbcTemplate jdbcTemplate;
		 
		
		 private final String uploadDir = "C:\\UploadsFiles"; // Change this to your desired directory

			@PostMapping("GetPayperiod")
			public LinkedHashMap<String, Object> employeepayperiod(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
                 JSONArray Values = new JSONArray();
				  
				 Values=reposity.employeepayperiod();
				 
				 response.put("Payperiod", Values);
				return response;
			}
			
			
			 @PostMapping("/upload")
			    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
			                                             @RequestParam("payPeriod") String payPeriod) {
			        if (file.isEmpty()) {
			            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			                                 .body("{\"error\":\"File is empty\"}");
			        }

			        try {
			            // Save the uploaded CSV file to the server
			            Path filePath = Paths.get(uploadDir, file.getOriginalFilename());
			            Files.createDirectories(filePath.getParent());
			            file.transferTo(filePath.toFile());

			            // Validate CSV structure and extract employee codes
			            List<String> employeeCodes = validateCSV(filePath);
			            if (employeeCodes.isEmpty()) {
			                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			                                     .body("{\"error\":\"No valid employee codes found.\"}");
			            }

			            // Execute SQL query to get source and destination paths
			            List<String[]> paths = getPaths(employeeCodes, payPeriod);
			            List<String> missingFiles = new ArrayList<>();

			            // Copy files from source to destination
			            for (String[] path : paths) {
			                String source = path[0];
			                String destination = path[1];
			                List<String> failedCodes = copyFiles(source, destination);
			                missingFiles.addAll(failedCodes);
			            }

			            // Check for any missing files and respond accordingly
			            if (!missingFiles.isEmpty()) {
			                // Return a 200 OK response with a message about missing source files
			                return ResponseEntity.ok("{\"message\":\"Source folder does not exist or employee codes are missing for the following files: " 
			                                          + String.join(", ", missingFiles) + "\"}");
			            }

			            return ResponseEntity.ok("{\"message\":\"Payslips uploaded successfully.\"}");
			            
			        } catch (IOException e) {
			            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			                                 .body("{\"error\":\"Failed to upload file: " + e.getMessage() + "\"}");
			        }
			    }

			    private List<String> validateCSV(Path filePath) throws IOException {
			        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath)))) {
			            String headerLine = reader.readLine();
			            if (headerLine == null || !"EmployeeCode".equals(headerLine.trim())) {
			                throw new IOException("Invalid CSV format. Expected 'EmployeeCode' header.");
			            }

			            // Read employee codes from the CSV
			            return reader.lines()
			                    .map(String::trim)
			                    .filter(line -> !line.isEmpty()) // Exclude empty lines
			                    .collect(Collectors.toList());
			        }
			    }

			    private List<String[]> getPaths(List<String> employeeCodes, String payPeriod) {
			        // Validate and trim payPeriod
			        payPeriod = payPeriod != null ? payPeriod.trim() : "";
			        if (!isValidPayPeriod(payPeriod)) {
			            throw new IllegalArgumentException("Invalid pay period: " + payPeriod);
			        }

			        // Construct the list of employee codes for the SQL IN clause
			        String employeeCodesPlaceholder = String.join(",", employeeCodes.stream()
			                .map(code -> "'" + code + "'")
			                .collect(Collectors.toList()));

			        String sourcePath = "C:/UploadsFiles/" + payPeriod + "/";
			        String destinationPath = "C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/null/BKP/" + payPeriod + "/";

			        // Create the SQL query with the constructed employee codes
			        String queryStr = "SELECT CONCAT('" + sourcePath + "',COMPANYID,'/',EMPLOYEESEQUENCENO,'.pdf') AS source," +
			                          "CONCAT('" + destinationPath + "',COMPANYID,'/',EMPLOYEESEQUENCENO,'.pdf') AS destination " +
			                          "FROM hclhrm_prod.tbl_employee_primary " +
			                          "WHERE employeesequenceno IN (" + employeeCodesPlaceholder + ")";

			        Query query = entityManager.createNativeQuery(queryStr);
			        List<Object[]> results = query.getResultList();

			        // Convert the Object arrays to String arrays
			        List<String[]> paths = new ArrayList<>();
			        for (Object[] result : results) {
			            String[] path = new String[2];
			            path[0] = (String) result[0]; // source
			            path[1] = (String) result[1]; // destination
			            paths.add(path);
			        }

			        return paths;
			    }

			    private boolean isValidPayPeriod(String payPeriod) {
			        // Define your criteria for a valid pay period
			        return payPeriod.matches("^[a-zA-Z0-9_]+$"); // Adjust the regex based on your needs
			    }

			    private List<String> copyFiles(String sourcePath, String destinationPath) throws IOException {
			        List<String> failedCodes = new ArrayList<>();
			        Path sourceFile = Paths.get(sourcePath);
			        Path destinationFile = Paths.get(destinationPath);

			        // Check if the source file exists
			        if (!Files.exists(sourceFile)) {
			            System.out.println("Source file does not exist: " + sourcePath);
			            failedCodes.add(sourcePath); // Track the missing source file
			            return failedCodes; // Return immediately
			        }

			        // Check if the destination file already exists
			        if (Files.exists(destinationFile)) {
			            System.out.println("Destination file already exists: " + destinationPath);
			            return failedCodes; // No need to track, just return
			        }

			        // Create the destination directory if it doesn't exist
			        if (destinationFile.getParent() != null && !Files.exists(destinationFile.getParent())) {
			            Files.createDirectories(destinationFile.getParent());
			        }

			        // Copy the source file to the destination
			        Files.copy(sourceFile, destinationFile);
			        System.out.println("File copied to: " + destinationFile);

			        return failedCodes; // Return the list of failed codes (empty if no failures)
			    }



			
}


package com.hetero.heteroiconnect.jobcode;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
 
@RestController
@RequestMapping("/Job")
public class Jobcodecontroller {
	@Autowired  
	private JobcodeService jobcodeService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping(value = "/JobDescription/{emp}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getPdf(@PathVariable("emp") String emp) {
        String JD = "";
        byte[] pdfBytes = null;

        try {
            String sql = "SELECT c.PDF FROM HCLHRM_PROD.tbl_employee_primary a " +
                         "JOIN test.tbl_employee_jobcode_mapping b ON a.employeesequenceno = b.employeeid " +
                         "LEFT JOIN test.tbl_job_codes c ON c.position_id = b.position_id " +
                         "WHERE a.employeesequenceno = ? AND b.status = 1001";

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, emp);
            
            if (!rows.isEmpty()) {
                JD = (String) rows.get(0).get("PDF");
            }

            if (!JD.isEmpty()) {
                File file = new File("C:\\Jobcodes\\" + JD);
                if (file.exists()) {
                    try (FileInputStream fileInputStream = new FileInputStream(file)) {
                        pdfBytes = IOUtils.toByteArray(fileInputStream);
                    }
                } else {
                    // Handle case where file does not exist
                    return ResponseEntity.notFound().build();
                }
            } else {
                // Handle case where JD is empty
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            // Log the exception properly
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    
    @PostMapping(value = "/JobDescription/checkJD")
    public LinkedHashMap<String, Object> checkPdfExistence(@RequestBody String login) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            // Extract empID from JSON payload
            org.json.JSONObject object = new org.json.JSONObject(login);
            String empID = object.getString("empID");

            // Query to check if record exists for the empID
            String sql = "SELECT 1 FROM HCLHRM_PROD.tbl_employee_primary a " +
                         "JOIN test.tbl_employee_jobcode_mapping b ON a.employeesequenceno = b.employeeid " +
                         "LEFT JOIN test.tbl_job_codes c ON c.position_id = b.position_id " +
                         "WHERE a.employeesequenceno = ? AND b.status = 1001";

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, empID);

            if (!rows.isEmpty()) {
                response.put("result", "TRUE"); // Record exists
            } else {
                response.put("result", "FALSE"); // No record found for empID
            }

        } catch (Exception e) {
            // Log the exception properly
            e.printStackTrace();
            response.put("error", "Error checking PDF existence");
        }

        return response;   
    }

//    @GetMapping(value = "/Notice/{emp}")
//    public LinkedHashMap<String, Object> Notice(@PathVariable("emp") String emp) {
//        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
//        try {
//            // Insert record for the empID
//            String insertSql = "INSERT INTO test.tbl_employee_notice (EMPLOYEEID, STATUS) VALUES (?, '1001')";
//            
//            System.out.println(emp+"----->");
//            int rowsAffected = jdbcTemplate.update(insertSql, emp);
//            if (rowsAffected > 0) {
//                response.put("result", "TRUE"); // Insert successful
//            } else {
//                response.put("result", "FALSE"); // Insert failed
//            } 
// 
//        } catch (Exception e) {
//            // Log the exception properly
//            e.printStackTrace();
//            response.put("error", "Error inserting record");
//        }
//
//        return response;
//    }

    @GetMapping(value = "/Notice/{emp}")
    public LinkedHashMap<String, Object> Notice(@PathVariable("emp") String emp) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            // First, check if the employee already has an entry in the tbl_employee_notice table
            String checkSql = "SELECT COUNT(*) FROM test.tbl_employee_notice WHERE EMPLOYEEID = ?";
            int count = jdbcTemplate.queryForObject(checkSql, Integer.class, emp);
            
            // If the employee exists, return a message saying no insert is needed
            if (count > 0) {
                response.put("result", "EMPLOYEE EXISTS"); // Employee already exists in the table
            } else {
                // If the employee does not exist, proceed to insert the record
                String insertSql = "INSERT INTO test.tbl_employee_notice (EMPLOYEEID, STATUS) VALUES (?, '1001')";
                int rowsAffected = jdbcTemplate.update(insertSql, emp);
                
                if (rowsAffected > 0) {
                    response.put("result", "TRUE"); // Insert successful
                } else {
                    response.put("result", "FALSE"); // Insert failed
                }
            }
        } catch (Exception e) {
            // Log the exception properly
            e.printStackTrace();
            response.put("error", "Error inserting record");
        }

        return response;
    }




	@PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<String> uploadFile(@RequestParam("jcode") String jcode, @RequestParam("eid") String eid,
			@RequestParam("file") MultipartFile file) {
		return jobcodeService.uploadFile(jcode, eid, file);
	}

	@PostMapping(value = "/jcheck", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> checkJobcode(@RequestBody String jcode) {
		System.out.println(jcode);
		return jobcodeService.checkJobcode(jcode);
	}

	@PostMapping(value = "/getdata", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> checkJobcodeGetData(@RequestBody String eid) {
		return jobcodeService.checkJobcodeGetData(eid);
	}
	
	@PostMapping(value = "/checkFile",consumes = "application/json", produces = "application/json")
	public  ResponseEntity<String> checkFile(@RequestBody String file) {
		return jobcodeService.checkFile(file);
	}

}

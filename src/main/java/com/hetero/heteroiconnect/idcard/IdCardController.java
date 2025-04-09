package com.hetero.heteroiconnect.idcard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController 
@RequestMapping("/idcard")
public class IdCardController {
	private static final Logger logger = LoggerFactory.getLogger(IdCardController.class);

	@Autowired
	private IdCardService idCardService;

//	@GetMapping("/generate")
//	public Map<String, Object> generateIdCard(@RequestParam String empId, @RequestPart MultipartFile image) {
//	    Map<String, Object> response = new HashMap<>();
//
//	    try {
//	        Optional<EmployeeDTO> employeeDTO = Optional.ofNullable(idCardService.generateIdCard(empId, image));
//
//	        if (employeeDTO.isPresent()) {
//	            EmployeeDTO dto = employeeDTO.get();
//	            response.put("success", true);
//	            response.put("message", "ID card generated successfully!");
//	            response.put("data", dto);
//	        } else {
//	            response.put("success", false);
//	            response.put("message", "Employee not found with the ID: " + empId);
//	            response.put("data", null);
//	        }
//
//	    } catch (IOException e) {
//	        response.put("success", false);
//	        response.put("message", "Error generating ID card: " + e.getMessage());
//	        response.put("data", null);
//	    }
//
//	    return response;
//	}

//	@GetMapping("/empdata")
//	public Map<String, Object> empData(@RequestParam String empId) {
//	    Map<String, Object> response = new HashMap<>();
//
//	    try {
//	        Optional<EmployeeDTO> employeeDTO = Optional.ofNullable(idCardService.empData(empId));
//
//	        if (employeeDTO.isPresent()) {
//	            EmployeeDTO dto = employeeDTO.get();
//	            response.put("success", true);
//	            response.put("message", "Data Fetched successfully!");
//	            response.put("data", dto);
//	        } else {
//	            response.put("success", false);
//	            response.put("message", "Employee not found with the ID: " + empId);
//	            response.put("data", null);
//	        }
//
//	    } catch (IOException e) {
//	        response.put("success", false);
//	        response.put("message", "Error fetching employee data: " + e.getMessage());
//	        response.put("data", null);
//	    }
//
//	    return response;
//	}


	@GetMapping("/bloodgroup")
	public List<BloodGroupDTO> getBloodGroups() {
		return idCardService.getBloodGroups();
	}

	@GetMapping("/address")
	public List<AddressDTO> getAllAddresses() {
		return idCardService.getAllAddresses();
	}

	@GetMapping("/employeedetails")
    public ResponseEntity<List<EmployeeDetailsDTO>> getEmployeeDetails() {
        logger.info("Request received to fetch employee details");

        try {
            List<EmployeeDetailsDTO> employeeDetails = idCardService.getEmployeeDetails();
            if (employeeDetails.isEmpty()) {
                logger.warn("No employee details found, returning empty list.");
            }
            return ResponseEntity.ok(employeeDetails);
        } catch (Exception e) {
            logger.error("Failed to fetch employee details", e);
            return ResponseEntity.status(500).body(null);
        }
    }

	 
	@PostMapping("/insertempimage")
	public ResponseEntity<EmployeeImageDTO> insertEmployeeDetails(
	        @RequestParam("employeeId") String employeeId,
	        @RequestParam("addressId") String addressId,
	        @RequestParam("bloodGroupId") String bloodGroupId,
	        @RequestParam(value = "employeeImage", required = false) MultipartFile employeeImage) {

	    try {
	        logger.info("Processing details for employeeId: {}", employeeId);

	        EmployeeImageDTO responseDTO = idCardService.saveEmployeeImageDetails(employeeId, addressId, bloodGroupId, employeeImage);
	        return ResponseEntity.ok(responseDTO);
	    } catch (IOException e) {
	        logger.error("Failed to process details for employeeId: {}", employeeId, e);
	        return ResponseEntity.status(500).body(null);
	    }
	}
	
	// new
//		@PostMapping("/generatecard")
//		public ResponseEntity<String> getIdCard(@RequestParam("employeeId") String empId) {
//			System.out.println("came"+empId);
//			try {
//				idCardService.getIdCard(empId);
//				return ResponseEntity.status(HttpStatus.OK)
//						.body("true");
//			} catch (IOException e) {
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//						.body("Failed to generate ID card for EmpId: " + empId + ". Error: " + e.getMessage());
//			}
//		}
	
	
	@PostMapping("/generatecard")
	public ResponseEntity<byte[]> getIdCard(@RequestParam("employeeId") String empId,@RequestParam("code") String code) throws Exception {
	    System.out.println("Request received for Employee ID: " + empId);
	    try {
	        // Call the service to generate the PDF
	        String pdfFilePath = idCardService.getIdCard(empId,code);
	        
	        // Read the generated PDF file into a byte array
	        Path filePath = Paths.get(pdfFilePath);
	        if (!Files.exists(filePath)) {
	            throw new FileNotFoundException("Generated file not found for EmpId: " + empId);
	        }
	        
	        byte[] fileContent = Files.readAllBytes(filePath);
	        
	        // Set the response headers for file download
	        String contentType = "application/pdf"; // Set appropriate content type for the file
	        String fileName = empId + "_id_card_new.pdf"; // File name to be sent in the response header

	        return ResponseEntity.status(HttpStatus.OK)
	                .header("Content-Type", contentType)
	                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
	                .body(fileContent);
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(("Failed to generate ID card for EmpId: " + empId + ". Error: " + e.getMessage()).getBytes());
	    }
	} 

	@GetMapping("/image/{imageName:.+}") // Handles GET requests for dynamic image names with extension
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
        try {
            // Path to the image file (dynamically determined by imageName)
            Path imagePath = Paths.get("C:/HeteroIdImages/" + imageName);  // Change the path as needed
            byte[] imageBytes = Files.readAllBytes(imagePath);

            // Dynamically set the content type based on file extension
            String contentType = "image/jpeg"; // Default to jpeg
            if (imageName.endsWith(".png")) {
                contentType = "image/png";
            } else if (imageName.endsWith(".jpg") || imageName.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)  // Set content type dynamically
                    .body(imageBytes);  // Return the image data as response body
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);  // Return error if the image is not found
        }
    }

}

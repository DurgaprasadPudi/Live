package com.hetero.heteroiconnect.idcard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Service
public class IdCardService {
	private static final Logger logger = LoggerFactory.getLogger(IdCardService.class);

	@Autowired
	private EmployeeRepository employeeRepository;
	@Value("${employee.image.upload.path}")
	private String imageUploadPath;
//	private final String imageUploadPath = "C:\\EmployeeIdcardImages";

	public EmployeeDTO generateIdCard(String empId, MultipartFile image) throws IOException {
		List<Object[]> employeeData = employeeRepository.getEmployeeIdCards(empId);

		return Optional.ofNullable(employeeData.isEmpty() ? null : employeeData.get(0)).map(employee -> {
			EmployeeDTO employeeDTO = new EmployeeDTO();
			employeeDTO.setName((String) employee[0]);
			employeeDTO.setDesignation((String) employee[1]);
			employeeDTO.setDepartment((String) employee[2]);
			employeeDTO.setBloodGroup((String) employee[3]);
			employeeDTO.setEmpId(empId);
			return employeeDTO;
		}).map(dto -> {
			try {
				String directoryPath = "C:/HeteroIdImages/";
				File directory = new File(directoryPath);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				byte[] imageBytes = image.getBytes();
				dto.setImage(imageBytes);
				File imageFile = new File(directoryPath + empId + "_id_card.png");
				try (FileOutputStream fos = new FileOutputStream(imageFile)) {
					fos.write(imageBytes);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return dto;
		}).orElseThrow(() -> new IOException("No employee found with the provided employee ID."));
	}

	public EmployeeDTO empData(String empId) throws IOException {
		// Fetch employee data from the repository
		List<Object[]> employeeData = employeeRepository.getEmployeeIdCards(empId);

		// Use Optional to safely handle empty employee data
		return Optional.ofNullable(employeeData.isEmpty() ? null : employeeData.get(0)).map(employee -> {
			// Map the employee data to EmployeeDTO
			EmployeeDTO employeeDTO = new EmployeeDTO();
			employeeDTO.setName((String) employee[0]);
			employeeDTO.setDesignation((String) employee[1]);
			employeeDTO.setDepartment((String) employee[2]);
			employeeDTO.setBloodGroup((String) employee[3]);
			employeeDTO.setEmpId(empId);

			// Handle image file
			String directoryPath = "C:/HeteroIdImages/" + empId + "_id_card.png";
			File imageFile = new File(directoryPath);

			// Read image bytes if the image exists
			if (imageFile.exists()) {
				try (FileInputStream fis = new FileInputStream(imageFile)) {
					byte[] imageBytes = new byte[(int) imageFile.length()];
					fis.read(imageBytes); // Read image file into byte array
					employeeDTO.setImage(imageBytes);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// If image doesn't exist, set a default value or handle the error
				employeeDTO.setImage(null); // Or some default image if needed
			}

			return employeeDTO;
		}).orElseThrow(() -> new IOException("No employee found with the provided employee ID."));
	}

	public List<BloodGroupDTO> getBloodGroups() {
		List<BloodGroupDTO> bloodGroupList = new ArrayList<>();
		BloodGroupDTO customDTO = new BloodGroupDTO();
		customDTO.setBloodGroupId(0L);
		customDTO.setBloodGroupName("--Select--");
		bloodGroupList.add(customDTO);
		List<Object[]> results = employeeRepository.getBloodGroups();
		results.stream().map(row -> {
			BloodGroupDTO dto = new BloodGroupDTO();
			dto.setBloodGroupId(((Number) row[0]).longValue());
			dto.setBloodGroupName((String) row[1]);
			return dto;
		}).forEach(bloodGroupList::add);
		return bloodGroupList;
	}

	public List<AddressDTO> getAllAddresses() {
		List<Object[]> results = employeeRepository.getAddress();
		return results.stream().map(row -> {
			AddressDTO dto = new AddressDTO();
			dto.setId(((Number) row[0]).longValue());
			dto.setCompanyName((String) row[1]);
			dto.setAddress((String) row[2]);
			return dto;
		}).collect(Collectors.toList());
	}

	public List<EmployeeDetailsDTO> getEmployeeDetails() {
		logger.info("Fetching employee details from the repository");

		try {
			List<Object[]> results = employeeRepository.getEmployeeDetails();
			if (results.isEmpty()) {
				logger.warn("No employee details found in the repository.");
			}

			return results.stream().map(row -> {
				EmployeeDetailsDTO dto = new EmployeeDetailsDTO();
				try {
					dto.setEmployeeId(((Number) row[0]).longValue());
					dto.setName((String) row[1]);
					dto.setBuName((String) row[2]);
					dto.setDateOfJoin((Date) row[3]);
					dto.setCostCenter((String) row[4]);
					dto.setCode((String) row[5]);
					dto.setDesignationCheck(Boolean.valueOf((String) row[6]));
					dto.setDepartmentCheck(Boolean.valueOf((String) row[7]));
					dto.setDepartment((String) row[8]);
					dto.setDesignation((String) row[9]);
					dto.setAddressId((String) row[10]);
					dto.setBloodGroupId((String) row[11]);
					dto.setImagePath((String) row[12]);
					String imagePath = (String) row[12];
					dto.setCompanyName((String) row[13]);
					dto.setAddress((String) row[14]);
					dto.setBloodGroupName((String) row[15]);
					dto.setBackgroundImagename((String) row[16]);
					if (!"NA".equalsIgnoreCase(imagePath)) {
						try {
							byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
							dto.setImageBytes(imageBytes);
						} catch (Exception ex) {
							logger.error("The image path is not present in the folder :{}", imagePath);
							logger.error("Failed to read image file: {}", imagePath, ex);
							dto.setImageBytes(null);
						}
					} else {
						dto.setImageBytes(null);
					}
				} catch (Exception e) {
					logger.error("Error mapping row to DTO: {}", row, e);
				}
				return dto;
			}).collect(Collectors.toList());

		} catch (Exception e) {
			logger.error("Failed to fetch employee details", e);
			throw new RuntimeException("Error fetching employee details", e);
		}
	}

	public EmployeeImageDTO saveEmployeeImageDetails(String employeeId, String addressId, String bloodGroupId,
			MultipartFile employeeImage) throws IOException {
		logger.info("Starting to save employee details for employeeId: {}", employeeId);

		String imagePathString = null;

		if (employeeImage != null && !employeeImage.isEmpty()) {
			if (imageUploadPath == null || imageUploadPath.trim().isEmpty()) {
				logger.error("Image upload path is not configured correctly.");
				throw new IOException("Image upload path is not configured correctly.");
			}
			logger.debug("Image Upload Path: {}", imageUploadPath);

			Path imagePath = Paths
					.get(imageUploadPath + File.separator + employeeId + "_" + employeeImage.getOriginalFilename());
			logger.info("Image path resolved: {}", imagePath);

			File directory = new File(imageUploadPath);
			if (!directory.exists()) {
				logger.info("Directory does not exist, creating directory...");
				directory.mkdirs();
			}

			try {
				Files.write(imagePath, employeeImage.getBytes());
				logger.info("File saved successfully at: {}", imagePath);
				imagePathString = imagePath.toString();
			} catch (IOException e) {
				logger.error("Failed to save file at path: {}", imagePath, e);
				throw e;
			}
		} else {
			logger.warn("No employee image provided for employeeId: {}", employeeId);
		}

		try {
			employeeRepository.insertOrUpdateEmployeeDetails(employeeId, addressId, bloodGroupId, imagePathString);
			logger.info("Employee details inserted/updated successfully for employeeId: {}", employeeId);
		} catch (Exception e) {
			logger.error("Failed to update employee details in the database for employeeId: {}", employeeId, e);
			throw new IOException("Failed to update employee details", e);
		}

		EmployeeImageDTO responseDTO = new EmployeeImageDTO();
		responseDTO.setEmployeeId(employeeId);
		responseDTO.setAddressId(addressId);
		responseDTO.setBloodGroupId(bloodGroupId);
		responseDTO.setEmployeeImagePath(imagePathString);

		logger.info("Returning EmployeeImageDTO with employeeImagePath: {}", imagePathString);
		return responseDTO;
	}

	// @Transactional(rollbackFor = Throwable.class)
	public String getIdCard(String empId,String code) throws Exception {
	    List<Object[]> employeeData = employeeRepository.getEmployeeIdCards(empId);

	    // Use a file path on the C: drive for the template
	    ///if(data[5].toString())
	    String templateFilePath ="";
	    if(code.equals("HHC"))
	    {
	    	   templateFilePath = "C:/HeteroIdImages/templates/heteroidcard.html"; // Path on the C: drive
	    }
	    else 
	    {
	    	  templateFilePath = "C:/HeteroIdImages/templates/azistaidcard.html"; // Path on the C: drive
	    }
	    
	    //String templateFilePath = "C:/HeteroIdImages/templates/azistaidcard.html"; // Path on the C: drive
	    
	    File templateFile = new File(templateFilePath);

	    if (!templateFile.exists()) {
	        throw new FileNotFoundException("Template file not found in path: " + templateFilePath);
	    }

	    // Read the content of the template file
	    String htmlContent = new String(Files.readAllBytes(templateFile.toPath()));

	    if (!employeeData.isEmpty()) {
	        Object[] data = employeeData.get(0);
	        String imagePath = data[17].toString();
	         
	        File ProfileImageFile = new File(imagePath);
	        
	        String companyImagePath = "C:/HeteroIdImages/"+data[16].toString();
	        File companyImageFile = new File(companyImagePath);

	        String address = (data[14] != null) ? data[14].toString() : "--";
	        address = address.replace("&", "&amp;"); // Escape special characters

	        // Replace placeholders in the HTML template with actual values
	        htmlContent = htmlContent.replace("{{NAME}}", escapeHtml(data[1] != null ? data[1].toString() : "--"))
	            .replace("{{DESIGNATION_NAME}}", escapeHtml(data[9] != null ? data[9].toString() : "--"))
	            .replace("{{DEPARTMENT_NAME}}", escapeHtml(data[8] != null ? data[8].toString() : "--"))
	            .replace("{{BLOODGROUPNAME}}", escapeHtml(data[15] != null ? data[15].toString() : "--"))
	            .replace("{{COMPANYNAME}}", escapeHtml(data[13] != null ? data[13].toString() : "--"))
	            .replace("{{ADDRESS}}", escapeHtml(data[14] != null ? data[14].toString() : "--"))
	            .replace("{{EMPLOYEEID}}", escapeHtml(empId))  // Emp ID might contain special chars too
	            .replace("{{IMAGE}}", ProfileImageFile.toURI().toString())
	            .replace("{{HHC_IMAGE}}", companyImageFile.toURI().toString());

	        // Generate the PDF and return the path
	        String pdfFilePath = generateImageFromHtml(htmlContent, empId);
	        return pdfFilePath;
	    }

	    if (employeeData.isEmpty()) {
	        throw new EmployeeNotFoundException("Employee with EmpId " + empId + " not found.");
	    }

	    return null;
	}


	/*
	 * private void generateImageFromHtml(String htmlContent, String empId) throws
	 * FileNotFoundException, IOException { ITextRenderer renderer = new
	 * ITextRenderer(); renderer.setDocumentFromString(htmlContent);
	 * renderer.layout(); String pdfFilePath = "C:/HeteroIdImages/pdf/" + empId +
	 * "_id_card.pdf"; File pdfFile = new File(pdfFilePath);
	 * pdfFile.getParentFile().mkdirs(); try (FileOutputStream pdfOutputStream = new
	 * FileOutputStream(pdfFile)) { try { renderer.createPDF(pdfOutputStream); }
	 * catch (DocumentException e) { e.printStackTrace(); } } }
	 */

//	public void generateImageFromHtml(String htmlContent, String empId) throws Exception {
//		String pdfFilePath = "C:/HeteroIdImages/pdf/" + empId + "_id_card_new.pdf";
//		File pdfFile = new File(pdfFilePath);
//		pdfFile.getParentFile().mkdirs();
//		try (OutputStream outputStream = new FileOutputStream(pdfFile)) {
//			PdfRendererBuilder builder = new PdfRendererBuilder();
//			builder.useFastMode();
//			builder.withHtmlContent(htmlContent, null);
//			builder.toStream(outputStream);
//			builder.run();
//		}
//	}
	
	public String generateImageFromHtml(String htmlContent, String empId) throws Exception {
	    String pdfFilePath = "C:/HeteroIdImages/pdf/" + empId + "_id_card_new.pdf";
	    File pdfFile = new File(pdfFilePath);
	    pdfFile.getParentFile().mkdirs(); // Create the directory if it doesn't exist
	    try (OutputStream outputStream = new FileOutputStream(pdfFile)) {
	        PdfRendererBuilder builder = new PdfRendererBuilder();
	        builder.useFastMode();
	        builder.withHtmlContent(htmlContent, null);
	        builder.toStream(outputStream);
	        builder.run();
	    }
	    return pdfFilePath; // Return the path to the generated PDF
	}

	
	// Utility function to escape HTML special characters
				public String escapeHtml(String input) {
				    if (input == null) return "";
				    return input.replace("&", "&amp;")
				                .replace("<", "&lt;")
				                .replace(">", "&gt;")
				                .replace("\"", "&quot;")
				                .replace("'", "&#39;");
				}


}

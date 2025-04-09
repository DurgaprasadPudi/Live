package com.hetero.heteroiconnect.familydetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

 @Repository
public class EmployeeService {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	private static final long MAX_FILE_SIZE = 6 * 1024 * 1024;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void insertEmployeeData(EmployeeDataInsertDTO employeeData, MultipartFile empFile) {
		String filePath = saveFile(empFile).orElse(null);
		System.out.println(filePath+"orginal Image");
		employeeData.setFilePath(filePath);

		
		try {
			String checkEmployeeSQL = "SELECT COUNT(*) FROM test.tbl_sindhuri_employee_data WHERE EmpCode = ?";
			Integer count = jdbcTemplate.queryForObject(checkEmployeeSQL, Integer.class, employeeData.getEmpCode());
System.err.println("step 1"+count);
			if (count != null && count > 0) {
				String errorMessage = "Employee with EmpCode " + employeeData.getEmpCode() + " already exists.";
				logger.warn(errorMessage);
				throw new EmployeeExistsException(errorMessage);
			} else {
				String fetchEmployeeDetailsSQL = "SELECT IFNULL(p.DATEOFBIRTH, 'NA') AS DATEOFBIRTH, "
						+ "IFNULL(g.NAME, 'NA') AS Gender, IFNULL(b.BLOODGROUPNAME, 'NA') AS BloodGroup "
						+ "FROM hclhrm_prod.tbl_employee_primary p "
						+ "LEFT JOIN hcladm_prod.tbl_gender g ON p.gender = g.gender "
						+ "LEFT JOIN hclhrm_prod.tbl_employee_personalinfo i ON p.employeeid = i.employeeid "
						+ "LEFT JOIN hcladm_prod.tbl_bloodgroups b ON i.bloodgroupid = b.bloodgroupid "
						+ "WHERE p.employeesequenceno = ?";

				Map<String, Object> employeeDetails = jdbcTemplate.queryForMap(fetchEmployeeDetailsSQL,
						employeeData.getEmpCode());

				String dob = (String) employeeDetails.getOrDefault("DATEOFBIRTH", "NA");
				String gender = (String) employeeDetails.getOrDefault("Gender", "NA");
				String bloodGroup = (String) employeeDetails.getOrDefault("BloodGroup", "NA");
				String insertEmployeeSQL = "INSERT INTO test.tbl_sindhuri_employee_data (EmpCode, PAN, Aadhar, Mobile, "
						+ "Whatsappnumber, Maritalstatus, filepath, BloodGroup, DOB, Gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				jdbcTemplate.update(insertEmployeeSQL, employeeData.getEmpCode(), employeeData.getPan(),
						employeeData.getAadhar(), employeeData.getMobile(), employeeData.getWhatsappNumber(),
						employeeData.getMaritalStatus(), employeeData.getFilePath(), bloodGroup, dob, gender);

				logger.info("Successfully inserted employee data for EmpCode {}", employeeData.getEmpCode());
			}
		} catch (EmployeeExistsException e) {
			logger.error("Employee already exists: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Error inserting employee data for EmpCode {}: {}", employeeData.getEmpCode(), e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to insert employee data", e);
		}
	}

	public void insertFamilyData(List<FamilyDataInsertDTO> familyDataList, List<MultipartFile> familyFiles) {
		for (int i = 0; i < familyDataList.size(); i++) {
			FamilyDataInsertDTO familyData = familyDataList.get(i);
			MultipartFile familyFile = familyFiles.get(i);
			String filePath = saveFile(familyFile).orElse(null);
			familyData.setFilePath(filePath);
			try {
				String insertFamilySQL = "INSERT INTO test.tbl_sindhuri_employee_family (name, relation, gender, dob, blood_group, aadhar, occupation, filepath, empcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				jdbcTemplate.update(insertFamilySQL, familyData.getName(), familyData.getRelation(),
						familyData.getGender(), familyData.getDob(), familyData.getBloodGroup(), familyData.getAadhar(),
						familyData.getOccupation(), familyData.getFilePath(), familyData.getEmpCode());
				logger.info("Inserted family data for EmpCode {}", familyData.getEmpCode());
			} catch (Exception e) {
				logger.error("Error inserting family data for EmpCode {}: {}", familyData.getEmpCode(), e.getMessage());
				throw new RuntimeException("Failed to insert family data", e);
			}
		}
	}

	private Optional<String> saveFile(MultipartFile file) {
	    String fileName = file.getOriginalFilename();

	    // Ensure file name is not empty or null
	    if (fileName == null || fileName.isEmpty()) {
	        logger.error("No file name provided");
	        return Optional.empty();
	    }

	    // Define the path for storing files
	    Path path = Paths.get("C:/sindhuriuploads/" + fileName);

	    // Check if the file size exceeds the maximum limit
//	    long MAX_FILE_SIZE = 5242880; // 5 MB in bytes
//	    if (file.getSize() > MAX_FILE_SIZE) {
//	        logger.error("File {} exceeds the maximum allowed size of 5 MB", fileName);
//	        return Optional.empty();
//	    }

	    try {
	        // Ensure the target directory exists
	        Files.createDirectories(path.getParent());

	        // Save the file to the system
	        Files.write(path, file.getBytes());

	        // Return only the file name (not the full path) to save in the database
	        logger.info("File saved successfully: {}", fileName);
	        return Optional.of(fileName);
	    } catch (IOException e) {
	        logger.error("Error saving file {}: {}", fileName, e.getMessage());
	        return Optional.empty();
	    }
	}
	public EmployeeWithFamilyDTO getEmployeeWithFamilyData(String empCode) {
		
		
		try {
			String employeeDataQuery = "SELECT * FROM test.tbl_sindhuri_employee_data WHERE empcode = ?";
			EmployeeDataDTO employeeData = jdbcTemplate.queryForObject(employeeDataQuery, new Object[] { empCode },
					new EmployeeDataRowMapper());

			String familyDataQuery = "SELECT * FROM test.tbl_sindhuri_employee_family WHERE  status =1001 and empcode = ?";
			List<FamilyDataDTO> familyDataList = jdbcTemplate.query(familyDataQuery, new Object[] { empCode },
					new FamilyDataRowMapper());
			logger.info("Successfully fetched data for empCode: {}", empCode);
			return new EmployeeWithFamilyDTO(employeeData, familyDataList);

		} catch (Exception e) {
			logger.error("Error fetching data for empCode {}: {}", empCode, e.getMessage());
			return null;
		}
	}

	private static class EmployeeDataRowMapper implements org.springframework.jdbc.core.RowMapper<EmployeeDataDTO> {
	    @Override
	    public EmployeeDataDTO mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
	    	System.out.println(rs.getString("filepath"));
	        EmployeeDataDTO employeeData = new EmployeeDataDTO();
	        employeeData.setEmpCode(rs.getString("empcode"));
	        employeeData.setPan(rs.getString("pan"));
	        employeeData.setAadhar(rs.getString("aadhar"));
	        employeeData.setMobile(rs.getString("mobile"));
	        employeeData.setWhatsappNumber(rs.getString("whatsappnumber"));
	        employeeData.setMaritalStatus(rs.getString("maritalstatus"));

	        String filePath = "C:" + File.separator + "sindhuriuploads" + File.separator + rs.getString("filepath");
	        employeeData.setOriginalFilePath(filePath);
	        if (filePath != null && !filePath.isEmpty()) {
	            try {
	                byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
	                employeeData.setFilePath(fileContent);
	                logger.info("Loaded file content for empCode {} from path {}", employeeData.getEmpCode(), filePath);

	            } catch (IOException e) {
	                logger.error("Error reading file at path {}: {}", filePath, e.getMessage());
	                employeeData.setFilePath(new byte[0]);
	            }
	        }

	        return employeeData;
	    }
	}

	private static class FamilyDataRowMapper implements org.springframework.jdbc.core.RowMapper<FamilyDataDTO> {
	    @Override
	    public FamilyDataDTO mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
	        FamilyDataDTO familyData = new FamilyDataDTO();
	        familyData.setSno(rs.getString("sno"));
	        familyData.setName(rs.getString("name"));
	        familyData.setRelation(rs.getString("relation"));
	        familyData.setGender(rs.getString("gender"));
	        familyData.setDob(rs.getDate("dob"));
	        familyData.setBloodGroup(rs.getString("blood_group"));
	        familyData.setAadhar(rs.getString("aadhar"));
	        familyData.setOccupation(rs.getString("occupation"));

	        String filePath = "C:" + File.separator + "sindhuriuploads" + File.separator + rs.getString("filepath");
	        familyData.setOriginalFilePath(filePath);
	        if (filePath != null && !filePath.isEmpty()) {
	            try {
	                byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
	                familyData.setFilePath(fileContent);
	                logger.info("Loaded file content for family member {} from path {}", familyData.getEmpCode(),
	                        filePath);
	            } catch (IOException e) {
	                logger.error("Error reading file at path {}: {}", filePath, e.getMessage());
	                familyData.setFilePath(new byte[0]);
	            }
	        }
	        familyData.setEmpCode(rs.getString("empcode"));
	        return familyData;
	    }
	}


	public boolean deleteEmployeeBySno(int sno) {
		String deleteQuery = "UPDATE test.tbl_sindhuri_employee_family SET status = 1002 WHERE sno = ?";
		try {
			logger.info("Attempting to delete employee with sno: {}", sno);
			int rowsAffected = jdbcTemplate.update(deleteQuery, sno);
			if (rowsAffected > 0) {
				logger.info("Employee with sno {} marked as deleted (status set to 1002).", sno);
				return true;
			} else {
				logger.warn("No rows affected for sno {}. Employee may not exist.", sno);
				return false;
			}
		} catch (DataAccessException e) {
			logger.error("Error deleting employee with sno {}: {}", sno, e.getMessage());
			return false;
		}
	}
//Main employee Update 
	/*
	 * public void updateEmployeeData(EmployeeDataInsertDTO employeeData,
	 * MultipartFile empFile) { String filePath = null; filePath =
	 * saveFile(empFile).orElse(null); employeeData.setFilePath(filePath); try {
	 * String updateSQL =
	 * "UPDATE test.tbl_sindhuri_employee_data SET PAN = ?, Aadhar = ?, Mobile = ?, Whatsappnumber = ?, filepath = ?, Maritalstatus = ? WHERE EmpCode = ?"
	 * ; jdbcTemplate.update(updateSQL, employeeData.getPan(),
	 * employeeData.getAadhar(), employeeData.getMobile(),
	 * employeeData.getWhatsappNumber(), employeeData.getFilePath(),
	 * employeeData.getMaritalStatus(), employeeData.getEmpCode());
	 * 
	 * } catch (Exception e) {
	 * System.out.println("Database error while updating employee data: " +
	 * e.getMessage()); throw new
	 * RuntimeException("Failed to update employee data in the database."); } }
	 */

	public void updateEmployeeData(EmployeeDataInsertDTO employeeData, MultipartFile empFile) {
		String filePath = saveFile(empFile).orElse(null);
		employeeData.setFilePath(filePath);

		try {
			String updateSQL;
			Object[] params;

			if (filePath != null) {
				// Update filepath if a valid path is provided
				updateSQL = "UPDATE test.tbl_sindhuri_employee_data SET PAN = ?, Aadhar = ?, Mobile = ?, Whatsappnumber = ?, filepath = ?, Maritalstatus = ? WHERE EmpCode = ?";
				params = new Object[] { employeeData.getPan(), employeeData.getAadhar(), employeeData.getMobile(),
						employeeData.getWhatsappNumber(), filePath, employeeData.getMaritalStatus(),
						employeeData.getEmpCode() };
				logger.info("Including file path in update query for EmpCode: {}", employeeData.getEmpCode());

			} else {
				// Skip filepath update if filePath is null
				updateSQL = "UPDATE test.tbl_sindhuri_employee_data SET PAN = ?, Aadhar = ?, Mobile = ?, Whatsappnumber = ?, Maritalstatus = ? WHERE EmpCode = ?";
				params = new Object[] { employeeData.getPan(), employeeData.getAadhar(), employeeData.getMobile(),
						employeeData.getWhatsappNumber(), employeeData.getMaritalStatus(), employeeData.getEmpCode() };

				logger.info("File path not included in update query for EmpCode: {}", employeeData.getEmpCode());
			}

			jdbcTemplate.update(updateSQL, params);
			logger.info("Employee data updated successfully for EmpCode: {}", employeeData.getEmpCode());

		} catch (DataAccessException e) {
			logger.error("Database error while updating employee data for EmpCode {}: {}", employeeData.getEmpCode(),
					e.getMessage());
			throw new RuntimeException("Failed to update employee data in the database.");
		} catch (Exception e) {
			logger.error("Unexpected error while updating employee data for EmpCode {}: {}", employeeData.getEmpCode(),
					e.getMessage());
			throw new RuntimeException("Failed to update employee data.");
		}
	}

	public void insertFamilyMembers(List<FamilyDataInsertDTO> familyDataList, List<MultipartFile> familydetailsFiles) {
		for (int i = 0; i < familyDataList.size(); i++) {
			FamilyDataInsertDTO familyData = familyDataList.get(i);
			String filePath = null;
			if (i < familydetailsFiles.size()) {
				MultipartFile file = familydetailsFiles.get(i);
				filePath = saveFile(file).orElse(null);
			}
			familyData.setFilePath(filePath);

			try {
				/*
				 * String checkFamilySQL =
				 * "SELECT COUNT(*) FROM test.tbl_sindhuri_employee_family WHERE empcode = ? AND aadhar = ?"
				 * ; Integer count = jdbcTemplate.queryForObject(checkFamilySQL, Integer.class,
				 * familyData.getEmpCode(), familyData.getAadhar());
				 */

				//if (count != null && count == 0) {
					String insertFamilySQL = "INSERT INTO test.tbl_sindhuri_employee_family (name, relation, gender, dob, blood_group, aadhar, occupation, filepath, empcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					jdbcTemplate.update(insertFamilySQL, familyData.getName(), familyData.getRelation(),
							familyData.getGender(), familyData.getDob(), familyData.getBloodGroup(),
							familyData.getAadhar(), familyData.getOccupation(), familyData.getFilePath(),
							familyData.getEmpCode());
					logger.info("Inserted family member data for EmpCode: {} and Aadhar: {}", familyData.getEmpCode(),
							familyData.getAadhar());
				/*
					 * else { throw new FamilyMemberExistsException("Family member with empCode " +
					 * familyData.getEmpCode() + " and Aadhar " + familyData.getAadhar() +
					 * " already exists."); }
					 */
			} catch (DataAccessException e) {
				logger.error(
						"Database error while checking or inserting family member for EmpCode {} and Aadhar {}: {}",
						familyData.getEmpCode(), familyData.getAadhar(), e.getMessage());
			} catch (FamilyMemberExistsException e) {
				throw e;
			} catch (Exception e) {
				logger.warn("Family member already exists for EmpCode {} and Aadhar {}: {}", familyData.getEmpCode(),
						familyData.getAadhar(), e.getMessage());
				throw e;
			}
		}
	}

	public class FamilyMemberExistsException extends RuntimeException {
		public FamilyMemberExistsException(String message) {
			super(message);
		}
	}

	public class EmployeeExistsException extends RuntimeException {
		public EmployeeExistsException(String message) {
			super(message);
		}
	}

}

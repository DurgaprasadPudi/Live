package com.hetero.heteroiconnect.insurancefiles;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.requisition.forms.ApiResponse;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Repository
public class InsuranceFilesRepository {

	private static final Logger logger = LoggerFactory.getLogger(InsuranceFilesRepository.class);

	private final JdbcTemplate jdbcTemplate;
	private final MessageBundleSource messageBundleSource;

	@Value("${insurance.employee.files}")
	private String employeeFilesPath;

	@Value("${insurance.family.files}")
	private String familyFilesPath;

	public InsuranceFilesRepository(JdbcTemplate jdbcTemplate, MessageBundleSource messageBundleSource) {
		this.jdbcTemplate = jdbcTemplate;
		this.messageBundleSource = messageBundleSource;
	}

	public ApiResponse uploadFiles(String type, List<MultipartFile> files) {
		logger.info("Entered uploadFiles method with type: {}", type);

		int inserted = 0;
		int updated = 0;

		try {
			for (MultipartFile file : files) {
				String originalFilename = file.getOriginalFilename();
				if (originalFilename == null || !originalFilename.toUpperCase().endsWith(".PDF")) {
					logger.warn("Skipping invalid file: {}", originalFilename);
					continue;
				}

				String employeeId = originalFilename.replaceAll("[^0-9]", "").trim();
				logger.info("Processing file: {}, Extracted employee ID: {}", originalFilename, employeeId);

				String folderPath = "1".equals(type) ? employeeFilesPath : familyFilesPath;
				String fullPath = folderPath + File.separator + originalFilename;
				logger.debug("Saving file to: {}", fullPath);

				File targetFile = new File(folderPath, originalFilename);
				saveFileToDisk(file, targetFile);

				String checkSql = "SELECT COUNT(*) FROM test.tbl_insurance_pdf_details WHERE employee_id = ?";
				Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { employeeId }, Integer.class);

				if (count != null && count > 0) {
					String updateSql = "1".equals(type)
							? "UPDATE test.tbl_insurance_pdf_details SET self_file_path = ?, lupdate = CURRENT_TIMESTAMP WHERE employee_id = ?"
							: "UPDATE test.tbl_insurance_pdf_details SET family_file_path = ?, lupdate = CURRENT_TIMESTAMP WHERE employee_id = ?";
					jdbcTemplate.update(updateSql, fullPath, employeeId);
					updated++;
					logger.info("Updated record for employeeId: {}", employeeId);
				} else {
					String insertSql = "1".equals(type)
							? "INSERT INTO test.tbl_insurance_pdf_details (employee_id, self_file_path) VALUES (?, ?)"
							: "INSERT INTO test.tbl_insurance_pdf_details (employee_id, family_file_path) VALUES (?, ?)";
					jdbcTemplate.update(insertSql, employeeId, fullPath);
					inserted++;
					logger.info("Inserted record for employeeId: {}", employeeId);
				}
			}
		} catch (Exception e) {
			String message = messageBundleSource.getmessagebycode("insurance.file.error", new Object[] {});
			logger.error("Error occurred while uploading files: {}", e.getMessage(), e);
			throw new UserWorkSheetUploadException(message, e);
		}

		String successMessage = "Files processed successfully. Inserted: " + inserted + ", Updated: " + updated;
		logger.info(successMessage);
		return new ApiResponse(successMessage);
	}

//	private void saveFileToDisk(MultipartFile file, File targetFile) throws IOException {
//		File directory = targetFile.getParentFile();
//		if (!directory.exists()) {
//			boolean created = directory.mkdirs();
//			logger.debug("Directory created: {}", created);
//		}
//
//		try (FileOutputStream fos = new FileOutputStream(targetFile)) {
//			fos.write(file.getBytes());
//			logger.debug("File saved: {}", targetFile.getAbsolutePath());
//		}
//	}
	
	private void saveFileToDisk(MultipartFile file, File targetFile) throws IOException {
	    File directory = targetFile.getParentFile();
	    if (!directory.exists()) {
	        boolean created = directory.mkdirs();
	        logger.debug("Directory created: {}", created);
	    }

	    try (InputStream in = file.getInputStream();
	         OutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile))) {

	        byte[] buffer = new byte[8192]; // 8 KB buffer
	        int bytesRead;
	        while ((bytesRead = in.read(buffer)) != -1) {
	            out.write(buffer, 0, bytesRead);
	        }

	        logger.debug("File saved: {}", targetFile.getAbsolutePath());
	    }
	}

	
	
	
	

	public InsuranceFileDTO getEmployeeInsuranceDetails(Integer loginId) {
		String sql = "SELECT a. employeesequenceno as employee_id, b.self_file_path, b.family_file_path, "
				+ "a.callname AS emp_name, c.name AS bu_name, d.name AS cost_center_name, "
				+ "DATE_FORMAT(e.dateofjoin, '%d-%m-%Y') AS doj " + "FROM hclhrm_prod.tbl_employee_primary a "
				+ "LEFT JOIN test.tbl_insurance_pdf_details b ON a.employeesequenceno = b.employee_id AND b.status = 1001 "
				+ "LEFT JOIN hcladm_prod.tbl_businessunit c ON a.companyid = c.businessunitid "
				+ "LEFT JOIN hcladm_prod.tbl_costcenter d ON a.costcenterid = d.costcenterid "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_profile e ON a.employeeid = e.employeeid "
				+ "WHERE a.employeesequenceno = ?";

		return jdbcTemplate.query(sql, (ResultSet rs) -> {
			if (rs.next()) {
				String selfPath = rs.getString("self_file_path");
				String familyPath = rs.getString("family_file_path");

				return new InsuranceFileDTO(rs.getString("employee_id"), getFileName(selfPath),
						getFileContentAsBytes(selfPath), rs.getString("emp_name"), rs.getString("bu_name"),
						rs.getString("cost_center_name"), rs.getString("doj"), getFileName(familyPath),
						getFileContentAsBytes(familyPath));
			}
			return null;
		}, loginId);
	}

	public List<InsuranceFileDTO> getAllInsuranceFiles(Integer type) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT a.employee_id, a.self_file_path, a.family_file_path, ")
				.append("b.callname AS emp_name, c.name AS bu_name, d.name AS cost_center_name, ")
				.append("DATE_FORMAT(e.dateofjoin, '%d-%m-%Y') AS doj ")
				.append("FROM test.tbl_insurance_pdf_details a ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.employee_id = b.employeesequenceno ")
				.append("LEFT JOIN hcladm_prod.tbl_businessunit c ON b.companyid = c.businessunitid ")
				.append("LEFT JOIN hcladm_prod.tbl_costcenter d ON b.costcenterid = d.costcenterid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_profile e ON b.employeeid = e.employeeid ")
				.append("WHERE a.status = 1001 ");

		if (type != null && type == 1) {
			sqlBuilder.append("AND a.self_file_path IS NOT NULL AND a.self_file_path != '' ");
		} else if (type != null && type == 2) {
			sqlBuilder.append("AND a.family_file_path IS NOT NULL AND a.family_file_path != '' ");
		} else {
			throw new IllegalArgumentException("Invalid type. Expected 1 (self) or 2 (family).");
		}
		List<InsuranceFileDTO> resultList = new ArrayList<>();
		try {
			jdbcTemplate.query(sqlBuilder.toString(), rs -> {
				String employeeId = rs.getString("employee_id");
				String empName = rs.getString("emp_name");
				String buName = rs.getString("bu_name");
				String costCenterName = rs.getString("cost_center_name");
				String doj = rs.getString("doj");
				String filePath = (type == 1) ? rs.getString("self_file_path") : rs.getString("family_file_path");
				String fileName = getFileName(filePath);
				InsuranceFileDTO dto = new InsuranceFileDTO(employeeId, fileName, new byte[0], empName, buName,
						costCenterName, doj, "", new byte[0]);
				resultList.add(dto);
			});
		} catch (Exception e) {
			logger.error("Error fetching insurance files: {}", e.getMessage(), e);
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("insurance.file.error", new Object[] {}), e);
		}
		return resultList;
	}

	private byte[] getFileContentAsBytes(String filePath) {
		if (filePath == null || filePath.trim().isEmpty()) {
			return new byte[0];
		}
		Path path = Paths.get(filePath);
		try {
			return Files.readAllBytes(path);
		} catch (NoSuchFileException e) {
			logger.warn("File not found: {}", filePath);
			return new byte[0];
		} catch (IOException e) {
			logger.error("Error reading file: {}", filePath, e);
			return new byte[0];
		}
	}

	private String getFileName(String fullPath) {
		if (fullPath == null || fullPath.trim().isEmpty()) {
			return "NA";
		}
		return Paths.get(fullPath).getFileName().toString();
	}
}

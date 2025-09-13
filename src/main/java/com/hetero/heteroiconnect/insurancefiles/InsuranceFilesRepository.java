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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.contractdetails.FileUtil;
import com.hetero.heteroiconnect.requisition.forms.ApiResponse;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.model.Master;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;
import com.ibm.icu.text.SimpleDateFormat;

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

	public List<HrFormDTO> getHrForms(int buId) {
		String sql = "SELECT bu_id,file_name, file_path FROM test.tbl_hr_registration_forms WHERE bu_id= ? and  status = 1001";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			String fileName = rs.getString("file_name");
			String filePath = rs.getString("file_path");
			byte[] fileContent = FileUtil.getFileContentAsBytes(filePath);
			return new HrFormDTO(rs.getInt("bu_id"), fileName, fileContent);
		}, buId);
	}

	public List<HrFormDTO> getMasterHrForms() {
		String sql = "select bu_id ,bu_name,bu_image  from test.tbl_hr_forms_bu_map where status=1001";
		//System.out.println("hi");
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			String fileName = rs.getString("bu_name");
			String filePath = rs.getString("bu_image");
			byte[] fileContent = FileUtil.getFileContentAsBytes(filePath);
			return new HrFormDTO(rs.getInt("bu_id"), fileName, fileContent);
		});
	}

	public List<HrFormDTO> getSOPForms() {
		String sql = "select id ,file_name,file_path  from test.tbl_hr_sop_forms where status=1001";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			String fileName = rs.getString("file_name");
			String filePath = rs.getString("file_path");
			byte[] fileContent = FileUtil.getFileContentAsBytes(filePath);
			return new HrFormDTO(rs.getInt("id"), fileName, fileContent);
		});
	}

	public Boolean getDates() {
		String currentMonthYear = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String sql = "SELECT COUNT(*) " + "FROM test.tbl_family_insurance_enable_dates " + "WHERE status = 1001 "
				+ "AND ? BETWEEN from_date AND to_date LIMIT 1";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] { currentMonthYear }, Integer.class);
		return count != null && count > 0;
	}

	public EmployeeBasicDetailsDTO getEmployeeDetails(int empId) {
		String sql = "SELECT " + "A.EMPLOYEESEQUENCENO AS empId, " + "A.CALLNAME AS name, " + "BU.NAME AS division, "
				+ "IFNULL(DEP.NAME, '') AS department, " + "IFNULL(DES.NAME, '') AS designation, "
				+ "GEN.NAME AS gender, " + "IFNULL(DATE_FORMAT(PROFILE.DATEOFJOIN, '%d-%m-%Y'), '') AS doj, "
				+ "DATE_FORMAT(A.DATEOFBIRTH, '%d-%m-%Y') AS dob,info.marital_status "
				+ "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A "
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID = BU.BUSINESSUNITID "
				+ "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE PROFILE ON A.EMPLOYEEID = PROFILE.EMPLOYEEID "
				+ "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS DD ON A.EMPLOYEEID = DD.EMPLOYEEID "
				+ "LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID = DEP.DEPARTMENTID "
				+ "LEFT JOIN HCLADM_PROD.TBL_DESIGNATION DES ON DD.DESIGNATIONID = DES.DESIGNATIONID "
				+ "LEFT JOIN HCLADM_PROD.TBL_GENDER GEN ON A.GENDER = GEN.GENDER "
				+ "LEFT JOIN test.tbl_employee_marital_info info on a.employeesequenceno = info.employee_id "
				+ "WHERE A.EMPLOYEESEQUENCENO = ?";

		return jdbcTemplate.queryForObject(sql,
				(rs, rowNum) -> new EmployeeBasicDetailsDTO(getFlag(rs.getString("doj")), rs.getString("empId"),
						rs.getString("name"), rs.getString("division"), rs.getString("department"),
						rs.getString("designation"), rs.getString("gender"), rs.getString("doj"), rs.getString("dob"),
						rs.getString("marital_status"), getGrossPremium(rs.getString("empId")),
						getFamilyDetails(empId)),
				empId);
	}

	public Boolean getFlag(String doj) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			// LocalDate givenDate = LocalDate.parse("01-08-2025", formatter);
			LocalDate givenDate = LocalDate.parse(doj, formatter);
			LocalDate now = LocalDate.now();
			boolean isSameMonthYear = givenDate.getMonthValue() == now.getMonthValue()
					&& givenDate.getYear() == now.getYear();
			return isSameMonthYear ? true : getDates();
		} catch (Exception e) {
			return false;
		}
	}

	public List<FamilyInsuranceDetailsDTO> getFamilyDetails(int empId) {
		String sql = "SELECT a.family_id AS familyId, a.employee_id AS employeeId, b.relation_name AS relationName, "
				+ "a.fullname AS fullname, a.gender AS gender, DATE_FORMAT(a.dob, '%d-%m-%Y') AS dob, a.age AS age,status_flag "
				+ "FROM test.tbl_family_insurance_details a "
				+ "LEFT JOIN test.tbl_master_relations b ON a.relation_id = b.relation_id "
				+ "WHERE a.employee_id = ? AND a.status = 1001";
		try {
			return jdbcTemplate.query(sql.toString(),
					(rs, rowNum) -> new FamilyInsuranceDetailsDTO(rs.getInt("familyId"), rs.getInt("employeeId"),
							rs.getString("relationName"), rs.getString("fullname"), rs.getString("gender"),
							rs.getString("dob"), rs.getString("age"), rs.getInt("status_flag")),
					empId);
		} catch (DataAccessException e) {
			return Collections.emptyList();
		}
	}

	public List<Master> getRelation() {
		String sql = "select  relation_id ,relation_name from test.tbl_master_relations  where status=1001";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master relation = new Master();
			relation.setId(rs.getInt("relation_id"));
			relation.setName(rs.getString("relation_name"));
			return relation;
		});
	}

	public ApiResponse deleteFamilyMember(int familyMemberId) {
		String sql = "UPDATE test.tbl_family_insurance_details SET status = 1002 WHERE family_id = ? and status=1001";
		int updatedRows = jdbcTemplate.update(sql, familyMemberId);
		String message = (updatedRows > 0) ? "Family member deleted successfully."
				: "No family member found with the given ID.";
		return new ApiResponse(message);
	}

	public ApiResponse getIntrestFlag(int familyMemberId, int flag) {
		String sql = "UPDATE test.tbl_family_insurance_details SET status_flag = ? WHERE family_id = ? and status=1001";
		int updatedRows = jdbcTemplate.update(sql, flag, familyMemberId);
		String message = (updatedRows > 0) ? "Interest shared successfully."
				: "No family member found with the given ID.";
		return new ApiResponse(message);
	}

	public String getDOJ(int empId) {
		String sql = "SELECT DATE_FORMAT(profile.DATEOFJOIN, '%d-%m-%Y') AS doj"
				+ " FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A " + "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE PROFILE "
				+ "ON A.EMPLOYEEID = PROFILE.EMPLOYEEID " + "WHERE A.employeesequenceno = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { empId }, String.class);
	}

	public ApiResponse saveFamilyMembers(UploadFamilyMembersDetails uploadDetails) {
		String insertFamilySql = "INSERT INTO test.tbl_family_insurance_details "
				+ "(employee_id, relation_id, fullname, gender, dob, age, status_flag, status, created_date_time) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, 1001, NOW())";

		String insertMaritalSql = "INSERT INTO test.tbl_employee_marital_info (employee_id, marital_status) "
				+ "VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE marital_status = VALUES(marital_status)";

		jdbcTemplate.update(insertMaritalSql, uploadDetails.getEmployeeId(), uploadDetails.getMaritalStatus());
		uploadDetails.getFamilyDetailsUpload().forEach(detail -> {
			jdbcTemplate.update(insertFamilySql, uploadDetails.getEmployeeId(), detail.getRelation(),
					detail.getFullname(), detail.getGender(), detail.getDob(), detail.getAge(), detail.getFlag());
		});
		return new ApiResponse("Family members inserted successfully.");
	}

	public List<EmployeeInsuranceCompleteDetailsDTO> getInsurancePremiumDetails() {
		String sql = "SELECT " + "A.EMPLOYEESEQUENCENO AS empId, " + "A.CALLNAME AS name, 'EMP' AS relation, "
				+ "BU.NAME AS division, " + "IFNULL(DEP.NAME, '') AS department, "
				+ "IFNULL(DES.NAME, '') AS designation, "
				+ "IFNULL(DATE_FORMAT(PROFILE.DATEOFJOIN, '%d-%m-%Y'), '') AS doj, " + "GEN.NAME AS gender, "
				+ "DATE_FORMAT(A.DATEOFBIRTH, '%d-%m-%Y') AS dob, "
				+ "TIMESTAMPDIFF(YEAR, A.DATEOFBIRTH, CURDATE()) AS age, " + "info.marital_status, "
				+ "dm.sum_insurance " + "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A "
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID = BU.BUSINESSUNITID "
				+ "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE PROFILE ON A.EMPLOYEEID = PROFILE.EMPLOYEEID "
				+ "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS DD ON A.EMPLOYEEID = DD.EMPLOYEEID "
				+ "LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID = DEP.DEPARTMENTID "
				+ "LEFT JOIN HCLADM_PROD.TBL_DESIGNATION DES ON DD.DESIGNATIONID = DES.DESIGNATIONID "
				+ "LEFT JOIN HCLADM_PROD.TBL_GENDER GEN ON A.GENDER = GEN.GENDER "
				+ "LEFT JOIN test.tbl_employee_marital_info info ON A.EMPLOYEESEQUENCENO = info.employee_id "
				+ "LEFT JOIN test.tbl_insurance_designation_map dm ON DD.DESIGNATIONID = dm.designation_id "
				+ "WHERE A.EMPLOYEESEQUENCENO IN (SELECT employee_id FROM test.tbl_employee_marital_info)";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new EmployeeInsuranceCompleteDetailsDTO(rs.getString("empId"), rs.getString("name"),
						rs.getString("relation"), rs.getString("division"), rs.getString("department"),
						rs.getString("designation"), rs.getString("doj"), rs.getString("gender"), rs.getString("dob"),
						String.valueOf(rs.getInt("age")), rs.getString("marital_status"),
						getGrossPremium(rs.getString("empId")), getFamilyPremiumDetails(rs.getString("empId"))));
	}

	// getGrossPremium(rs.getString("empId"))
	public List<FamilyInsuranceCompleteDetailsDTO> getFamilyPremiumDetails(String empId) {
		String sql = "SELECT " + "a.employee_id, " + "a.fullname, " + "IFNULL(b.relation_name, '') AS relation_name, "
				+ "IFNULL(a.gender, '') AS gender, " + "DATE_FORMAT(dob, '%d-%m-%Y')  AS dob, "
				+ "IFNULL(a.age, 0) AS age, " + "'' AS division, " + "'' AS department, " + "'' AS designation, "
				+ "'' AS doj, " + "'' AS marital_status, " + "'' AS sum_insurance, " + "'0' AS grossPremium "
				+ "FROM test.tbl_family_insurance_details a "
				+ "LEFT JOIN test.tbl_master_relations b ON a.relation_id = b.relation_id "
				+ "WHERE a.employee_id = ? AND a.status_flag = 1 AND a.status = 1001";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new FamilyInsuranceCompleteDetailsDTO(rs.getString("employee_id"),
						rs.getString("fullname"), rs.getString("relation_name"), rs.getString("division"),
						rs.getString("department"), rs.getString("designation"), rs.getString("doj"),
						rs.getString("gender"), rs.getString("dob"), String.valueOf(rs.getInt("age")),
						rs.getString("marital_status"), rs.getString("sum_insurance"), rs.getString("grossPremium")),
				empId);
	}

	// IFNULL( interest_flag,1) as
	public PremiumInfoDTO getGrossPremium(String empId) {
		String sql = "SELECT id, sum_insurance, gross_premium, IFNULL(NULLIF(interest_flag,'NA'),'NA' )as interest_flag   "
				+ "FROM test.tbl_family_insurance_premium_info "
				+ "WHERE employee_id = ? and status=1001  AND YEAR(created_date_time) = YEAR(CURDATE())"
				+ "ORDER BY id DESC LIMIT 1";
		try {
			return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
				return new PremiumInfoDTO(rs.getInt("id"), rs.getString("sum_insurance"), rs.getString("gross_premium"),
						rs.getString("interest_flag"));
			}, empId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void insertPremiumDetails(List<Object[]> validRecords) {
		String insertSql = "INSERT INTO test.tbl_family_insurance_premium_info "
				+ "(employee_id, sum_insurance, gross_premium, created_date_time) VALUES (?, ?, ?, NOW())";
		validRecords.forEach(record -> jdbcTemplate.update(insertSql, record));
	}

	public ApiResponse updateInterestStatus(int premiumInfoId, int flag) {
		String sql = "UPDATE test.tbl_family_insurance_premium_info "
				+ "SET interest_flag = ?, interest_showed_date = NOW() " + "WHERE id = ? AND status = 1001";

		int updatedRows = jdbcTemplate.update(sql, flag, premiumInfoId);
		String message = (updatedRows > 0) ? "Interest shared successfully." : "No record found with the given ID.";
		return new ApiResponse(message);
	}

}

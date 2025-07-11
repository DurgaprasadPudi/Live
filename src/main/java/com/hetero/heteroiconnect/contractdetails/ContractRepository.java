package com.hetero.heteroiconnect.contractdetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hetero.heteroiconnect.worksheet.model.Master;

@Repository
public class ContractRepository {

	private final JdbcTemplate jdbcTemplate;
	@Value("${contractdetails.folder-path}")
	private String folderPath;

	public ContractRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void uploadContractEmployee(ContractPersonDetailsDTO dto) {
		String sql = "INSERT INTO test.tbl_contract_person_details "
				+ "(company_id, contract_id, employee_id, employee_name, doj, gender_id , department, permanent_address, present_address, mobile_number, aadhar_number, file, dob,  status, created_date_time) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, 1001, now())";
		jdbcTemplate.update(sql, dto.getCompanyId(), dto.getContractId(), dto.getEmployeeId(), dto.getEmployeeName(),
				dto.getDoj(), dto.getGender(), dto.getDepartment(), dto.getPermanentAddress(), dto.getPresentAddress(),
				dto.getMobileNumber(), dto.getAadharNumber(), dto.getFile(), dto.getDob());
	}

	public List<Master> getCompany() {
		String sql = "select  company_id ,company_name from test.tbl_contract_company_details  where status=1001";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master company = new Master();
			company.setId(rs.getInt("company_id"));
			company.setName(rs.getString("company_name"));
			return company;
		});
	}

	public List<Master> getContracts() {
		String sql = "select contract_id,contract_name from test.tbl_contracts where status=1001";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master contract = new Master();
			contract.setId(rs.getInt("contract_id"));
			contract.setName(rs.getString("contract_name"));
			return contract;
		});
	}

	public List<Master> getGender() {
		String sql = "select gender,name from HCLADM_PROD.tbl_gender";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master gender = new Master();
			gender.setId(rs.getInt("gender"));
			gender.setName(rs.getString("name"));
			return gender;
		});
	}

	public List<ContractDetailsDTO> getAllContractDetails() {
		String sql = "SELECT " + "a.contract_person_id AS contractDetailId, " + "a.company_id AS companyId, "
				+ "b.company_name AS companyName, " + "a.contract_id AS contractId, "
				+ "c.contract_name AS contractName, " + "c.mobile_number AS contractPersonMobileNumber, "
				+ "a.employee_id AS employeeId, " + "a.employee_name AS employeeName, " + "a.doj AS doj, "
				+ "a.gender_id AS genderId, " + "d.name AS genderName, " + "a.department AS department, "
				+ "a.permanent_address AS permanentAddress, " + "a.present_address AS presentAddress, "
				+ "a.mobile_number AS mobileNumber, " + "a.aadhar_number AS aadharNumber, " + "a.file AS fileName, "
				+ "DATE_FORMAT(a.created_date_time, '%Y-%m-%d') AS createdDateTime, " + "a.dob "
				+ "FROM test.tbl_contract_person_details a "
				+ "LEFT JOIN test.tbl_contract_company_details b ON a.company_id = b.company_id "
				+ "LEFT JOIN test.tbl_contracts c ON a.contract_id = c.contract_id "
				+ "LEFT JOIN HCLADM_PROD.tbl_gender d ON a.gender_id = d.gender " + "WHERE a.status = 1001";
		return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToDto(rs));
	}

	private ContractDetailsDTO mapRowToDto(ResultSet rs) throws SQLException {
		String fileName = rs.getString("fileName");
		String filePath = folderPath + fileName;
		byte[] fileBytes = FileUtil.getFileContentAsBytes(filePath);

		return new ContractDetailsDTO(rs.getInt("contractDetailId"), rs.getInt("companyId"),
				rs.getString("companyName"), rs.getInt("contractId"), rs.getString("contractName"),
				rs.getString("contractPersonMobileNumber"), rs.getInt("employeeId"), rs.getString("employeeName"),
				rs.getString("doj"), rs.getInt("genderId"), rs.getString("genderName"), rs.getString("department"),
				rs.getString("permanentAddress"), rs.getString("presentAddress"), rs.getString("mobileNumber"),
				rs.getString("aadharNumber"), fileName, fileBytes, rs.getString("createdDateTime"),
				rs.getString("dob"));
	}

	public String deleteEmployeeData(int id) {
		String sql = "UPDATE test.tbl_contract_person_details SET status = 1002 WHERE contract_person_id = ?";
		int updatedRows = jdbcTemplate.update(sql, id);
		return updatedRows > 0 ? "Employee deleted successfully." : "Employee not found or already deleted.";
	}

}

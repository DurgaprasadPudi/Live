package com.hetero.heteroiconnect.bankinformation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BankInformationRepository {

	private final JdbcTemplate jdbcTemplate;

	public BankInformationRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// hclhrm_prod_others

	public List<EmployeeBankDTO> getEmployeeBankDetails() {
		String sql = "SELECT empid, bankifc, bankacc " + "FROM hclhrm_prod_others.tbl_emp_bank_ifc "
				+ "WHERE status = 1001";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new EmployeeBankDTO(0, rs.getInt("empid"),
				rs.getString("bankifc"), rs.getString("bankacc"), 0));
	}

	public void insertEmployeeBankRecordDuplicates(int empId, String ifsc, String accountNo, int bankId,
			int excelSheetNo, int type, int loginId) {
		String insertSql = "INSERT INTO test.tbl_employee_bank_details "
				+ "(employee_id, ifsc_code, account_number, bank_id, excel_sheet_no, type, login_id, status, created_date_time) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, 1001, NOW())";
		jdbcTemplate.update(insertSql, empId, ifsc, accountNo, bankId, excelSheetNo, type, loginId);
	}

	/*
	 * public void insertEmployeeBankRecordInOriginal(EmployeeBankDTO dto, int
	 * bankId) { String sql = "INSERT INTO test.tbl_emp_bank_ifc_dummy " +
	 * "(EMPID, BANKID, BANKIFC, BANKACC, STATUS) VALUES (?, ?, ?, ?, 1001)";
	 * jdbcTemplate.update(sql, dto.getEmpId(), bankId, dto.getBankIfsc(),
	 * dto.getBankAccount()); }
	 */

	/*
	 * public List<Integer> getEmployeeIdsBySequenceNos(List<Integer> sequenceNos) {
	 * if (sequenceNos == null || sequenceNos.isEmpty()) { return
	 * Collections.emptyList(); } String inSql =
	 * sequenceNos.stream().map(String::valueOf).collect(Collectors.joining(","));
	 * String sql =
	 * "SELECT employeeid FROM hclhrm_prod.tbl_employee_primary WHERE employeesequenceno IN ("
	 * + inSql + ")"; try { List<Integer> employeeIds = jdbcTemplate.query(sql, (rs,
	 * rowNum) -> rs.getInt("employeeid")); return employeeIds.isEmpty() ?
	 * Collections.emptyList() : employeeIds; } catch (Exception e) { return
	 * Collections.emptyList(); } }
	 */

	public int getLastExcelSheetNo() {
		String sql = "SELECT IFNULL(MAX(excel_sheet_no), 0) AS last_excel_sheet_no "
				+ "FROM test.tbl_employee_bank_details";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public List<Integer> getEmployeeSequenceNumbersForType4() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.employee_id ").append("FROM test.tbl_employee_bank_details a ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.employee_id = b.employeeid ")
				.append("WHERE a.type = 4 AND a.status = 1001");

		return jdbcTemplate.queryForList(sql.toString(), Integer.class);
	}

	public Map<Integer, Integer> getEmployeeIdMapBySequenceNos(List<Integer> sequenceNos) {
		if (sequenceNos == null || sequenceNos.isEmpty()) {
			return Collections.emptyMap();
		}
		String inSql = sequenceNos.stream().map(String::valueOf).collect(Collectors.joining(","));
		String sql = "SELECT employeesequenceno, employeeid " + "FROM hclhrm_prod.tbl_employee_primary "
				+ "WHERE status=1001 and  employeesequenceno IN (" + inSql + ")";
		try {
			return jdbcTemplate.query(sql, rs -> {
				Map<Integer, Integer> map = new HashMap<>();
				while (rs.next()) {
					map.put(rs.getInt("employeesequenceno"), rs.getInt("employeeid"));
				}
				return map;
			});
		} catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	public String getIfscNameByBankId(int bankId) {
		String sql = "SELECT ifsc_name FROM test.tbl_bank_ifsc_validation_mapping WHERE bank_id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { bankId }, String.class);
		} catch (Exception e) {
			return null;
		}
	}

	public List<BankDTO> getLatestEmployeeBankDetails(int bankId, int loginId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.sno, ")
				.append("IFNULL(b.employeesequenceno, a.employee_id) AS employee_code, b.employeeid,")
				.append("b.callname, a.ifsc_code, a.account_number, a.type,a.inserted_status ")
				.append("FROM test.tbl_employee_bank_details a ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.employee_id = b.employeeid ")
				.append("WHERE a.bank_id = ? and a.login_id = ? ")
				.append("AND a.excel_sheet_no = (SELECT MAX(excel_sheet_no) FROM test.tbl_employee_bank_details where bank_id= ? and login_id = ? and status=1001) ")
				.append("AND a.status = 1001");

		return jdbcTemplate.query(sql.toString(),
				(rs, rowNum) -> new BankDTO(rs.getInt("sno"), rs.getString("employee_code"), rs.getString("callname"),
						rs.getString("ifsc_code"), rs.getString("account_number"), rs.getInt("type"),
						rs.getString("employeeid"), rs.getInt("inserted_status")),
				bankId, loginId, bankId, loginId);
	}

	public List<EmployeeBankDTO> bulkInsertEmployeeBankDetails(List<EmployeeBankDTO> employeeBankList) {
		String sql = "INSERT INTO test.tbl_emp_bank_ifc_dummy (empid, bankid, bankifc, bankacc,status) VALUES (?, ?, ?, ?,1001)";
		jdbcTemplate.batchUpdate(sql, employeeBankList, 1000, (ps, dto) -> {
			ps.setInt(1, dto.getEmpId());
			ps.setInt(2, dto.getBankId());
			ps.setString(3, dto.getBankIfsc());
			ps.setString(4, dto.getBankAccount());
		});
		return employeeBankList;
	}

	public int updatedToMainTableStatus(List<Integer> snoList) {
		String placeholders = snoList.stream().map(sno -> "?").collect(Collectors.joining(", "));
		String sql = "UPDATE test.tbl_employee_bank_details "
				+ "SET inserted_status = 1002, inserted_date_time = NOW() " + "WHERE sno IN (" + placeholders + ")";
		Object[] params = snoList.toArray();
		int rowsUpdated = jdbcTemplate.update(sql, params);
		return rowsUpdated;
	}

	public int areAllType4(List<Integer> snoList) {
		String inSql = snoList.stream().map(String::valueOf).collect(Collectors.joining(","));
		String sql = "SELECT CASE " + "WHEN COUNT(CASE WHEN type = 4 THEN 1 END) = COUNT(*) "
				+ "THEN 1 ELSE 0 END AS all_type4 " + "FROM test.tbl_employee_bank_details " + "WHERE sno IN (" + inSql
				+ ")";
		try {
			Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
			return result;
		} catch (Exception e) {
			return 0;
		}
	}

	public List<Map<String, Object>> getDistinctExcelSheetNos(int loginId, int bankId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append("excel_sheet_no, ").append("CASE ")
				.append("WHEN SUM(CASE WHEN type = 4 THEN 1 ELSE 0 END) > 0 ")
				.append("AND SUM(CASE WHEN type = 4 AND inserted_status = 1002 THEN 1 ELSE 0 END) = ")
				.append("SUM(CASE WHEN type = 4 THEN 1 ELSE 0 END) ").append("THEN 1 ELSE 0 END AS status ")
				.append("FROM test.tbl_employee_bank_details ").append("WHERE login_id = ? ").append("AND bank_id = ? ")
				.append("AND status = 1001 ").append("GROUP BY excel_sheet_no");

		return jdbcTemplate.queryForList(sql.toString(), loginId, bankId);
	}

	public List<BankDTO> getOverallEmployeeBankDetails(int excelSheetNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.sno, ")
				.append("IFNULL(b.employeesequenceno, a.employee_id) AS employee_code, b.employeeid,")
				.append("b.callname, a.ifsc_code, a.account_number, a.type,a.inserted_status ")
				.append("FROM test.tbl_employee_bank_details a ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.employee_id = b.employeeid ")
				.append("WHERE a.excel_sheet_no = ? ").append("AND a.status = 1001");

		return jdbcTemplate.query(sql.toString(),
				(rs, rowNum) -> new BankDTO(rs.getInt("sno"), rs.getString("employee_code"), rs.getString("callname"),
						rs.getString("ifsc_code"), rs.getString("account_number"), rs.getInt("type"),
						rs.getString("employeeid"), rs.getInt("inserted_status")),
				excelSheetNo);
	}
}

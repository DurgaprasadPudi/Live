package com.hetero.heteroiconnect.fuelanddriverhistory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FuelAndDriverRepository {

	private final JdbcTemplate jdbcTemplate;

	public FuelAndDriverRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<FuelAndDriverResponseDTO> getFuelAndDriverDetails(String payPeriod) {
		String sql = "SELECT * FROM ( "
				+ "SELECT FDH.fuel_driver_id, A.EMPLOYEESEQUENCENO, ? AS Payperiod, A.callname AS EMPLOYEE_NAME, "
				+ "DES.name AS DEPT_NAME, DEP.name AS DESG_NAME, CTC.EFFECTIVEDATE, "
				+ "ROUND(IFNULL(MAX(IF(XX.COMPONENTID = 9001, XY.COMPONENTVALUE, 0)), 0) / 12) AS FUELANDMAINTENANCE, "
				+ "ROUND(IFNULL(MAX(IF(XX.COMPONENTID = 9006, XY.COMPONENTVALUE, 0)), 0) / 12) AS DRIVERSALARY, "
				+ "ROUND((IFNULL(MAX(IF(XX.COMPONENTID = 9001, XY.COMPONENTVALUE, 0)), 0) / 12) + "
				+ "(IFNULL(MAX(IF(XX.COMPONENTID = 9006, XY.COMPONENTVALUE, 0)), 0) / 12)) AS TOTAL_AMOUNT, "
				+ "IFNULL(FDH.claimed_amount,'NA') claimed_amount, IFNULL(FDH.bill_flag,'NA') bill_flag,IFNULL( FDH.comment ,'NA')comment, "
				+ "IFNULL(FDH.processed_status,'NA') processed_status, "
				+ "IFNULL(DATE_FORMAT(FDH.created_date_time, '%d-%m-%Y'), 'NA') AS formatted_created_date, "
				+ "IFNULL(DATE_FORMAT(FDH.processed_date, '%d-%m-%Y'), 'NA') AS formatted_processed_date "
				+ "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A "
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID = BU.BUSINESSUNITID "
				+ "LEFT JOIN test.empl_states EMPST ON A.EMPLOYEEID = EMPST.EMPLOYEEID "
				+ "LEFT JOIN (SELECT EMPLOYEEID AS ctcempid, MAX(CTCTRANSACTIONID) AS Maxid, MAX(EFFECTIVEDATE) AS EFFECTIVEDATE "
				+ "FROM HCLHRM_PROD.TBL_EMPLOYEE_CTC GROUP BY EMPLOYEEID) CTC ON CTC.ctcempid = A.EMPLOYEEID "
				+ "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_CTC_DETAILS CTCD ON CTCD.CTCTRANSACTIONID = CTC.Maxid "
				+ "LEFT JOIN test.fuel_maintenance XY ON XY.CTCTRANSACTIONID = CTC.Maxid AND XY.employeeid = A.EMPLOYEEID "
				+ "LEFT JOIN test.tbl_new_components XX ON XX.COMPONENTID = XY.COMPONENTID "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_payperiod_details P ON P.employeeid = A.employeeid "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_details kk ON A.employeeid = kk.employeeid "
				+ "LEFT JOIN HCLADM_PROD.TBL_DESIGNATION DES ON kk.designationid = des.designationid "
				+ "LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON kk.departmentid = dep.departmentid "
				+ "LEFT JOIN test.tbl_employee_fuel_driver_history FDH ON FDH.employee_id = A.EMPLOYEESEQUENCENO AND FDH.pay_period = ? AND FDH.status = 1001 "
				+ "WHERE A.STATUS IN (1001, 1092, 1401) AND P.payperiod = ? AND BU.CALLNAME = 'HYD' "
				+ "GROUP BY A.EMPLOYEEID, CTC.EFFECTIVEDATE) AS derived_data "
				+ "WHERE derived_data.FUELANDMAINTENANCE != 0 OR derived_data.DRIVERSALARY != 0";

		return jdbcTemplate.query(sql, new Object[] { payPeriod, payPeriod, payPeriod }, fuelAndDriverRowMapper());
	}

	private RowMapper<FuelAndDriverResponseDTO> fuelAndDriverRowMapper() {
		return (rs, rowNum) -> {
			FuelAndDriverResponseDTO dto = new FuelAndDriverResponseDTO();
			dto.setFuelDriverId(rs.getInt("fuel_driver_id"));
			dto.setEmployeeSequenceNo(rs.getString("EMPLOYEESEQUENCENO"));
			dto.setPayperiod(rs.getString("Payperiod"));
			dto.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
			dto.setDeptName(rs.getString("DEPT_NAME"));
			dto.setDesgName(rs.getString("DESG_NAME"));
			dto.setEffectiveDate(rs.getString("EFFECTIVEDATE"));
			dto.setFuelAndMaintenance(rs.getString("FUELANDMAINTENANCE"));
			dto.setDriverSalary(rs.getString("DRIVERSALARY"));
			dto.setTotalAmount(rs.getString("TOTAL_AMOUNT"));
			dto.setClaimedAmount(rs.getString("claimed_amount"));
			dto.setBillFlag(rs.getString("bill_flag"));
			dto.setComments(rs.getString("comment"));
			dto.setProcessedFlag(rs.getString("processed_status"));
			dto.setReceivedDate(rs.getString("formatted_created_date"));
			dto.setProcessedDate(rs.getString("formatted_processed_date"));
			return dto;
		};
	}

	public List<Map<String, String>> getPayPeriodsWithMonthYear() {
		String sql = "SELECT " + "    payperiod, "
				+ "    CONCAT(MONTHNAME(STR_TO_DATE(CONCAT(payperiod, '01'), '%Y%m%d')), '-', "
				+ "           YEAR(STR_TO_DATE(CONCAT(payperiod, '01'), '%Y%m%d'))) AS monthYear "
				+ "FROM hclhrm_prod.tbl_employee_payperiod_details "
				+ "WHERE payperiod >= CONCAT(YEAR(CURDATE()), '04') "
				+ "AND payperiod <= CONCAT(YEAR(CURDATE()), LPAD(MONTH(CURDATE()), 2, '0')) "
				+ "GROUP BY payperiod  ORDER BY payperiod DESC  ";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Map<String, String> map = new HashMap<>();
			map.put("payperiod", rs.getString("payperiod"));
			map.put("monthYear", rs.getString("monthYear"));
			return map;
		});
	}

	public String addFuelDriverHistory(List<FuelDriverHistoryInsertDTO> dtos) {
		String sql = "INSERT INTO test.tbl_employee_fuel_driver_history "
				+ "(employee_id, pay_period, claimed_amount, bill_flag, comment, status, created_by, created_date_time, processed_status, processed_date, processed_by) "
				+ "VALUES (?, ?, ?, ?, ?, 1001, ?, NOW(), ?, ?, ?)";

		List<Object[]> batchArgs = dtos.stream().map(dto -> {
			int processedStatus = dto.getProcessedFlag();
			String processedBy = null;
			Timestamp processedDate = null;
			if (processedStatus == 1) {
				processedBy = dto.getCreatedBy();
				processedDate = Timestamp.valueOf(LocalDateTime.now());
			}
			return new Object[] { dto.getEmployeeId(), dto.getPayPeriod(), dto.getClaimedAmount(), dto.getBillFlag(),
					dto.getComment(), dto.getCreatedBy(), processedStatus, processedDate, processedBy };
		}).collect(Collectors.toList());
		jdbcTemplate.batchUpdate(sql, batchArgs);
		return "All records inserted successfully";
	}

	public String updateProcessDetails(ProcessDTO dto) {
		int updatedRows = 0;
		if (dto.isReceivedFlag()) {
			String billFlagSql = "UPDATE test.tbl_employee_fuel_driver_history SET " + "bill_flag = 1, "
					+ "created_date_time = NOW(), " + "created_by = ? " + "WHERE fuel_driver_id = ? AND status = 1001";
			updatedRows = jdbcTemplate.update(billFlagSql, dto.getProcessedBy(), dto.getFuelDriverId());
		} else {
			String processSql = "UPDATE test.tbl_employee_fuel_driver_history SET " + "processed_status = 1, "
					+ "processed_date = NOW(), " + "processed_by = ? " + "WHERE fuel_driver_id = ? AND status = 1001";
			updatedRows = jdbcTemplate.update(processSql, dto.getProcessedBy(), dto.getFuelDriverId());
		}
		return updatedRows > 0 ? "Update successful" : "No record updated";
	}

}

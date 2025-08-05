package com.hetero.heteroiconnect.sfa_attendance;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SfaRepository {
	private JdbcTemplate jdbcTemplate;

	public SfaRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<String> getEmployeeList() {
		StringBuilder employeesQuery = new StringBuilder();
		employeesQuery.append(" SELECT employeesequenceno FROM  hclhrm_prod.tbl_employee_primary ");
		employeesQuery.append(" WHERE companyid IN(19,20,21,22) AND status IN(1001,1092,1401) ");
		return jdbcTemplate.queryForList(employeesQuery.toString(), String.class);
	}

	public Map<String, Object> getPayrollDates() {
		StringBuilder payrollDatesQuery = new StringBuilder();
		payrollDatesQuery.append(" SELECT ");
		payrollDatesQuery.append(" FROMDATE AS PAYPERIODFROMDATE, ");
		payrollDatesQuery.append(" DATE_ADD( ");
		payrollDatesQuery.append(" FROMDATE, ");
		payrollDatesQuery.append(" INTERVAL DAY(LAST_DAY(DATE_ADD(FROMDATE, INTERVAL 1 MONTH)) - 1) DAY ");
		payrollDatesQuery.append(" ) AS PAYPERIODTODATE ");
		payrollDatesQuery.append(" FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES ");
		payrollDatesQuery.append(" WHERE transactiontypeid = 1 ");
		payrollDatesQuery.append("  AND businessunitid = 11 ");
		payrollDatesQuery.append(" AND transactionduration = DATE_FORMAT(CURDATE(), '%Y%m') ");
		payrollDatesQuery.append(" GROUP BY TRANSACTIONDURATION ");
		payrollDatesQuery.append(" ORDER BY transactionduration DESC ");
		payrollDatesQuery.append(" LIMIT 12 ");
		return jdbcTemplate.queryForMap(payrollDatesQuery.toString());
	}
}

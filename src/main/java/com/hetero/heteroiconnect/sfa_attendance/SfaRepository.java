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
		payrollDatesQuery.append(" FROMDATE AS startDate, ");
		payrollDatesQuery.append(" DATE_ADD( ");
		payrollDatesQuery.append(" FROMDATE, ");
		payrollDatesQuery.append(" INTERVAL DAY(LAST_DAY(DATE_ADD(FROMDATE, INTERVAL 1 MONTH)) - 1) DAY ");
		payrollDatesQuery.append(" ) AS endDate ");
		payrollDatesQuery.append(" FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES ");
		payrollDatesQuery.append(" WHERE transactiontypeid = 1 ");
		payrollDatesQuery.append("  AND businessunitid = 11 ");
		payrollDatesQuery.append(" AND transactionduration = DATE_FORMAT(CURDATE(), '%Y%m') ");
		payrollDatesQuery.append(" GROUP BY TRANSACTIONDURATION ");
		payrollDatesQuery.append(" ORDER BY transactionduration DESC ");
		payrollDatesQuery.append(" LIMIT 12 ");
		return jdbcTemplate.queryForMap(payrollDatesQuery.toString());
	}

	public Map<String, Object> getEmployeeDetails(String empId) {
		StringBuilder employeeDetailsQuery = new StringBuilder();
		employeeDetailsQuery.append(
				" SELECT a.callname Name,b.name Status,g.name CostCenter,c.name Division,e.name Department,f.name Designation ");
		employeeDetailsQuery.append(" FROM hclhrm_prod.tbl_employee_primary a ");
		employeeDetailsQuery.append(" LEFT JOIN hclhrm_prod.tbl_status_codes b ON b.status=a.status ");
		employeeDetailsQuery.append(" LEFT JOIN hcladm_prod.tbl_businessunit c ON c.businessunitid=a.companyid ");
		employeeDetailsQuery
				.append(" LEFT JOIN hclhrm_prod.tbl_employee_professional_details d ON d.employeeid=a.employeeid ");
		employeeDetailsQuery.append(" LEFT JOIN hcladm_prod.tbl_department e ON e.departmentid=d.departmentid ");
		employeeDetailsQuery.append(" LEFT JOIN hcladm_prod.tbl_designation f ON f.designationid=d.designationid ");
		employeeDetailsQuery.append(" LEFT JOIN hcladm_prod.tbl_costcenter g ON g.costcenterid=a.costcenterid ");
		employeeDetailsQuery.append(" WHERE employeesequenceno=? ");
		return jdbcTemplate.queryForMap(employeeDetailsQuery.toString(), empId);
	}
}

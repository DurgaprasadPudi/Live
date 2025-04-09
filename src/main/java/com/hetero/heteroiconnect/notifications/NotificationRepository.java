package com.hetero.heteroiconnect.notifications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationRepository {
	private JdbcTemplate jdbcTemplate;

	public NotificationRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> appNotifications(int empId) {
		List<Map<String, Object>> notifications = new ArrayList<>();

		// Query 1: Manager's Assessment Notifications
		String managerQuery = "SELECT COUNT(*) FROM assessment_form.tbl_hr_processingempinfo WHERE next_approverid = ? AND STATUS IN (1002, 1003)";
		int pendingAssessments = jdbcTemplate.queryForObject(managerQuery, Integer.class, empId);
		if (pendingAssessments > 0) {
			notifications.add(createNotification("Pending Employee Assessment Submission", "/assesmentForm",
					"AS"));
		}

		// Query 2: HR Privileges Check
		String hrPrivilegeQuery = "SELECT count(*) FROM hclhrm_prod_others.tbl_employee_iconnect_privileges WHERE employeeid=? AND privilegeid=14 AND status=1001";
		int hasHrPrivilege = jdbcTemplate.queryForObject(hrPrivilegeQuery, Integer.class, empId);

		// Query 3: HR Data Query
		StringBuilder hrDataQuery = new StringBuilder();
		hrDataQuery.append(" SELECT DISTINCT ").append(" p.employeesequenceno AS empId, ")
				.append(" p.callname AS callName, ").append(" IFNULL(C.NAME, '--') AS bu, ")
				.append(" depart.name AS department, ").append(" co.NAME AS fieldType, ").append(" t.name AS empType, ")
				.append(" IFNULL(DATE_FORMAT(b.dateofjoin, '%d-%m-%Y'), '--') AS doj, ")
				.append(" concat(h.employeesequenceno,' - ',IFNULL(h.CALLNAME, '--')) AS nextApprover ")
				.append(" FROM hclhrm_prod.tbl_employee_primary p ")
				.append(" LEFT JOIN HCLHRM_PROD.tbl_employment_types t ON t.employmenttypeid = p.employmenttypeid ")
				.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE b ON b.employeeid = p.employeeid ")
				.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS d ON p.EMPLOYEEID = d.EMPLOYEEID ")
				.append(" LEFT JOIN hcladm_prod.tbl_department AS depart ON depart.DEPARTMENTID = d.DEPARTMENTID ")
				.append(" LEFT JOIN hclhrm_prod.tbl_employee_primary h ON d.managerid = h.EMPLOYEEID ")
				.append(" LEFT JOIN hcladm_prod.tbl_businessunit C ON C.BUSINESSUNITID = p.COMPANYID ")
				.append(" LEFT JOIN hcladm_prod.tbl_costcenter co ON co.costcenterid = p.costcenterid ")
				.append(" WHERE p.status IN (1001, 1401, 1092) ").append(" AND c.callname IN ('hyd') ")
				.append(" AND p.companyid != 25 ").append(" AND p.EMPLOYMENTTYPEID = 2 ")
				.append(" AND co.name = 'OFFICE' ")
				.append(" AND TIMESTAMPDIFF(MONTH, b.dateofjoin, DATE_ADD(CURDATE(), INTERVAL 30 DAY)) >= 6 ")
				.append(" AND p.employeesequenceno NOT IN ( ")
				.append("    SELECT emp_id FROM assessment_form.tbl_hr_processingempinfo ")
				.append("    WHERE emp_id NOT IN (0) AND status IN (1002, 1003, 1005, 1004) ")
				.append(" ) ORDER BY EmpId ASC ");

		boolean hasPendingHrAssessments = !jdbcTemplate.queryForList(hrDataQuery.toString()).isEmpty();

		if (hasHrPrivilege > 0 && hasPendingHrAssessments) {
			notifications.add(createNotification("Pending Employee Assessment Initiation", "/assessmentProcess",
					"AI"));
		}

		return notifications;
	}

	private Map<String, Object> createNotification(String description, String link, String icon) {
		Map<String, Object> notification = new HashMap<>();
		notification.put("description", description);
		notification.put("link", link);
		notification.put("icon", icon);
		return notification;
	}
}

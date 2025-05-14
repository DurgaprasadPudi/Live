package com.hetero.heteroiconnect.worksheet.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hetero.heteroiconnect.worksheet.report.entity.DummyEntity;

 

@Repository
public interface WorksheetDownloadRepository extends JpaRepository<DummyEntity, Integer> {

	@Query(value = "SELECT DISTINCT a.team_id, a.name FROM daily_worksheets.tbl_teams a JOIN daily_worksheets.tbl_teams_reportee_map b ON a.team_id = b.team_id WHERE (30546 = :employeeid OR 30020 = :employeeid OR b.reporting_manager = :employeeid) AND a.status = 1001", nativeQuery = true)
	List<Object[]> tabs(@Param("employeeid") String employeeid);

	@Query(value = "SELECT a.employee_id, a.task_date, j.name AS teamname, a.name AS employeename, a.time_block, "
			+ "       a.task_description, a.project_name, a.module, "
			+ "       IFNULL(CONCAT(p.employeesequenceno, ' - ', p.callname), '--') AS dependent_person, "
			+ "       c.name AS category_name, b.name AS activity_name, f.name AS priority_name, "
			+ "       d.name AS outcome_name, h.name AS tasktype_name, e.name AS planned_adhoc_name, "
			+ "       g.name AS taskalignment_name, a.start_time, a.end_time, a.duration, "
			+ "       IFNULL(NULLIF(a.remarks, ''), '--') AS remarks, i.name AS workplace_name, " + "       CASE "
			+ "           WHEN a.status = 1001 THEN 'Not Sent for Approval' "
			+ "           WHEN a.status = 1002 THEN 'Auto Approved' " + "           ELSE 'Unknown Status' "
			+ "       END AS status, k.att_in,   k.att_out, k.NET_HOURS "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN daily_worksheets.tbl_activity b ON a.activity_id = b.activity_id "
			+ "LEFT JOIN daily_worksheets.tbl_category c ON a.category_id = c.category_id "
			+ "LEFT JOIN daily_worksheets.tbl_outcome d ON a.outcome_id = d.outcome_id "
			+ "LEFT JOIN daily_worksheets.tbl_planned_adhoc e ON a.planned_adhoc_id = e.planned_adhoc_id "
			+ "LEFT JOIN daily_worksheets.tbl_priority f ON a.priority_id = f.priority_id "
			+ "LEFT JOIN daily_worksheets.tbl_task_alignment g ON a.taskalignment_id = g.taskalignment_id "
			+ "LEFT JOIN daily_worksheets.tbl_tasktype h ON a.tasktype_id = h.tasktype_id "
			+ "LEFT JOIN daily_worksheets.tbl_workplace i ON a.workplace_id = i.workplace_id "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.dependent_person = p.employeesequenceno "
			+ "LEFT JOIN daily_worksheets.tbl_teams j ON a.team = j.team_id "
			+ "LEFT JOIN test_mum.tbl_att_leave_in_out_status_report k "
			+ "ON k.employeeid = a.employee_id AND k.dateon = a.task_date " + "WHERE a.team = :teamId "
			+ "AND YEAR(a.task_date) = :year " + "AND MONTH(a.task_date) = :month "
			+ "ORDER BY a.employee_id, a.task_date", nativeQuery = true)
	List<Object[]> fetchEmployeeWorksheets(@Param("teamId") int teamId, @Param("year") int year,
			@Param("month") int month);

	@Query(value = "SELECT a.employee_id, a.task_date, j.name AS teamname, a.name AS employeename, a.time_block, "
			+ "       a.task_description, a.project_name, a.module, "
			+ "       IFNULL(CONCAT(p.employeesequenceno, ' - ', p.callname), '--') AS dependent_person, "
			+ "       c.name AS category_name, b.name AS activity_name, f.name AS priority_name, "
			+ "       d.name AS outcome_name, h.name AS tasktype_name, e.name AS planned_adhoc_name, "
			+ "       g.name AS taskalignment_name, a.start_time, a.end_time, a.duration, "
			+ "       IFNULL(NULLIF(a.remarks, ''), '--') AS remarks, i.name AS workplace_name, " + "       CASE "
			+ "           WHEN a.status = 1001 THEN 'Not Sent for Approval' "
			+ "           WHEN a.status = 1002 THEN 'Auto Approved' " + "           ELSE 'Unknown Status' "
			+ "       END AS status ,k.att_in,   k.att_out, k.NET_HOURS "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN daily_worksheets.tbl_activity b ON a.activity_id = b.activity_id "
			+ "LEFT JOIN daily_worksheets.tbl_category c ON a.category_id = c.category_id "
			+ "LEFT JOIN daily_worksheets.tbl_outcome d ON a.outcome_id = d.outcome_id "
			+ "LEFT JOIN daily_worksheets.tbl_planned_adhoc e ON a.planned_adhoc_id = e.planned_adhoc_id "
			+ "LEFT JOIN daily_worksheets.tbl_priority f ON a.priority_id = f.priority_id "
			+ "LEFT JOIN daily_worksheets.tbl_task_alignment g ON a.taskalignment_id = g.taskalignment_id "
			+ "LEFT JOIN daily_worksheets.tbl_tasktype h ON a.tasktype_id = h.tasktype_id "
			+ "LEFT JOIN daily_worksheets.tbl_workplace i ON a.workplace_id = i.workplace_id "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.dependent_person = p.employeesequenceno "
			+ "LEFT JOIN daily_worksheets.tbl_teams j ON a.team = j.team_id "
			+ "LEFT JOIN test_mum.tbl_att_leave_in_out_status_report k "
			+ "ON k.employeeid = a.employee_id AND k.dateon = a.task_date " + "WHERE a.team = :teamId "
			+ "AND a.task_date >= :fromDate " + "AND a.task_date <= :toDate "
			+ "ORDER BY a.employee_id, a.task_date", nativeQuery = true)
	List<Object[]> betweenDateEmployeeWorksheets(@Param("teamId") int teamId, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

	@Query(value = "SELECT a.employee_id, a.task_date, j.name AS teamname, a.name AS employeename, a.time_block, "
			+ "       a.task_description, a.project_name, a.module, "
			+ "       IFNULL(CONCAT(p.employeesequenceno, ' - ', p.callname), '--') AS dependent_person, "
			+ "       c.name AS category_name, b.name AS activity_name, f.name AS priority_name, "
			+ "       d.name AS outcome_name, h.name AS tasktype_name, e.name AS planned_adhoc_name, "
			+ "       g.name AS taskalignment_name, a.start_time, a.end_time, a.duration, "
			+ "       IFNULL(NULLIF(a.remarks, ''), '--') AS remarks, i.name AS workplace_name, " + "       CASE "
			+ "           WHEN a.status = 1001 THEN 'Not Sent for Approval' "
			+ "           WHEN a.status = 1002 THEN 'Auto Approved' " + "           ELSE 'Unknown Status' "
			+ "       END AS status,k.att_in,   k.att_out, k.NET_HOURS  "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN daily_worksheets.tbl_activity b ON a.activity_id = b.activity_id "
			+ "LEFT JOIN daily_worksheets.tbl_category c ON a.category_id = c.category_id "
			+ "LEFT JOIN daily_worksheets.tbl_outcome d ON a.outcome_id = d.outcome_id "
			+ "LEFT JOIN daily_worksheets.tbl_planned_adhoc e ON a.planned_adhoc_id = e.planned_adhoc_id "
			+ "LEFT JOIN daily_worksheets.tbl_priority f ON a.priority_id = f.priority_id "
			+ "LEFT JOIN daily_worksheets.tbl_task_alignment g ON a.taskalignment_id = g.taskalignment_id "
			+ "LEFT JOIN daily_worksheets.tbl_tasktype h ON a.tasktype_id = h.tasktype_id "
			+ "LEFT JOIN daily_worksheets.tbl_workplace i ON a.workplace_id = i.workplace_id "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.dependent_person = p.employeesequenceno "
			+ "LEFT JOIN daily_worksheets.tbl_teams j ON a.team = j.team_id "
			+ "LEFT JOIN test_mum.tbl_att_leave_in_out_status_report k "
			+ "ON k.employeeid = a.employee_id AND k.dateon = a.task_date " + "WHERE a.employee_id = :employeeid "
			+ "AND YEAR(a.task_date) = :year " + "AND MONTH(a.task_date) = :month "
			+ "ORDER BY a.employee_id, a.task_date", nativeQuery = true)
	List<Object[]> EmployeeWorksheets(@Param("employeeid") String employeeid, @Param("year") int year,
			@Param("month") int month);

	@Query(value = "SELECT a.employee_id, a.task_date, j.name AS teamname, a.name AS employeename, a.time_block, "
			+ "       a.task_description, a.project_name, a.module, "
			+ "       IFNULL(CONCAT(p.employeesequenceno, ' - ', p.callname), '--') AS dependent_person, "
			+ "       c.name AS category_name, b.name AS activity_name, f.name AS priority_name, "
			+ "       d.name AS outcome_name, h.name AS tasktype_name, e.name AS planned_adhoc_name, "
			+ "       g.name AS taskalignment_name, a.start_time, a.end_time, a.duration, "
			+ "       IFNULL(NULLIF(a.remarks, ''), '--') AS remarks, i.name AS workplace_name, " + "       CASE "
			+ "           WHEN a.status = 1001 THEN 'Not Sent for Approval' "
			+ "           WHEN a.status = 1002 THEN 'Auto Approved' " + "           ELSE 'Unknown Status' "
			+ "       END AS status,k.att_in,   k.att_out, k.NET_HOURS "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN daily_worksheets.tbl_activity b ON a.activity_id = b.activity_id "
			+ "LEFT JOIN daily_worksheets.tbl_category c ON a.category_id = c.category_id "
			+ "LEFT JOIN daily_worksheets.tbl_outcome d ON a.outcome_id = d.outcome_id "
			+ "LEFT JOIN daily_worksheets.tbl_planned_adhoc e ON a.planned_adhoc_id = e.planned_adhoc_id "
			+ "LEFT JOIN daily_worksheets.tbl_priority f ON a.priority_id = f.priority_id "
			+ "LEFT JOIN daily_worksheets.tbl_task_alignment g ON a.taskalignment_id = g.taskalignment_id "
			+ "LEFT JOIN daily_worksheets.tbl_tasktype h ON a.tasktype_id = h.tasktype_id "
			+ "LEFT JOIN daily_worksheets.tbl_workplace i ON a.workplace_id = i.workplace_id "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.dependent_person = p.employeesequenceno "
			+ "LEFT JOIN daily_worksheets.tbl_teams j ON a.team = j.team_id "
			+ "LEFT JOIN test_mum.tbl_att_leave_in_out_status_report k "
			+ "ON k.employeeid = a.employee_id AND k.dateon = a.task_date " + "WHERE a.employee_id = :employeeid "
			+ "AND a.task_date >= :fromDate " + "AND a.task_date <= :toDate "
			+ "ORDER BY a.employee_id, a.task_date", nativeQuery = true)
	List<Object[]> dateEmployeeWorksheets(@Param("employeeid") String employeeid, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

	@Query(value = "SELECT DISTINCT a.employee_id, b.callname, c.name AS Teamname,i.name AS employemntType "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON b.employeesequenceno = a.employee_id "
			+ "LEFT JOIN daily_worksheets.tbl_teams c ON c.team_id = a.team "
			+ "LEFT JOIN hclhrm_prod.tbl_employment_types i ON i.employmenttypeid = b.employmenttypeid "
			+ "WHERE YEAR(a.task_date) = :year AND MONTH(a.task_date) = :month "
			+ "AND a.team = :tabId", nativeQuery = true)
	List<Object[]> fetchEmployeeWorkData(@Param("tabId") Integer tabId, @Param("year") Integer year,
			@Param("month") Integer month);

	@Query(value = "SELECT DISTINCT a.employee_id, b.callname, c.name AS Teamname,i.name AS employemntType "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON b.employeesequenceno = a.employee_id "
			+ "LEFT JOIN daily_worksheets.tbl_teams c ON c.team_id = a.team "
			+ "LEFT JOIN hclhrm_prod.tbl_employment_types i ON i.employmenttypeid = b.employmenttypeid "
			+ "WHERE a.task_date >= :fromDate AND a.task_date <= :toDate " + "AND a.team = :tabId", nativeQuery = true)
	List<Object[]> fetchEmployeeWorkDataByDate(@Param("tabId") Integer tabId, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

	@Query(value = "SELECT DISTINCT a.employee_id, b.callname, c.name AS Teamname,i.name AS employemntType "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON b.employeesequenceno = a.employee_id "
			+ " JOIN daily_worksheets.tbl_teams c ON c.team_id = a.team "
			+ "LEFT JOIN hclhrm_prod.tbl_employment_types i ON i.employmenttypeid = b.employmenttypeid "
			+ "WHERE YEAR(a.task_date) = :year AND MONTH(a.task_date) = :month "
			+ "AND a.employee_id = :employeeid AND c.name IS NOT NULL AND i.name IS NOT NULL", nativeQuery = true)
	List<Object[]> fetchEmployeeWorkDataByEmployeeeids(@Param("employeeid") String employeeid,
			@Param("year") Integer year, @Param("month") Integer month);

	@Query(value = "SELECT DISTINCT a.employee_id, b.callname, c.name AS Teamname,i.name AS employemntType "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON b.employeesequenceno = a.employee_id "
			+ "LEFT JOIN daily_worksheets.tbl_teams c ON c.team_id = a.team "
			+ "LEFT JOIN hclhrm_prod.tbl_employment_types i ON i.employmenttypeid = b.employmenttypeid "
			+ "WHERE a.task_date >= :fromDate AND a.task_date <= :toDate "
			+ "AND a.employee_id = :employeeid  AND c.name IS NOT NULL AND i.name IS NOT NULL", nativeQuery = true)
	List<Object[]> fetchEmployeeWorkDataByEmployeeeidfromandtodate(@Param("employeeid") String employeeid,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate);

	@Query(value = "SELECT DISTINCT b.team_id, b.name AS TeamNames " + "FROM daily_worksheets.tbl_teams_reportee_map a "
			+ "LEFT JOIN daily_worksheets.tbl_teams b ON a.team_id = b.team_id "
			+ "WHERE (:employeeid = '30546' OR :employeeid = '30020' OR a.reporting_manager = :employeeid)", nativeQuery = true)
	List<Object[]> getTeams(@Param("employeeid") String employeeid);

	

	@Query(value = "SELECT DISTINCT a.employee_id, b.callname, c.name AS Teamname, i.name AS employmentType, a.team "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON b.employeesequenceno = a.employee_id "
			+ "LEFT JOIN daily_worksheets.tbl_teams c ON c.team_id = a.team "
			+ "LEFT JOIN hclhrm_prod.tbl_employment_types i ON i.employmenttypeid = b.employmenttypeid "
			+ "WHERE YEAR(a.task_date) = :year " + "AND MONTH(a.task_date) = :month " + "AND a.employee_id IN ( "
			+ "   SELECT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary M ON M.employeeid = P.managerid "
			+ "   JOIN daily_worksheets.tbl_employee_worksheet xx ON xx.employee_id = E.employeesequenceno "
			+ "   WHERE M.employeesequenceno IN (:employeeid) " + "   UNION ALL "
			+ "   SELECT DISTINCT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   WHERE E.employeesequenceno = :employeeid " + "   AND E.status IN (1001, 1061, 1092, 1401) "
			+ ")", nativeQuery = true)
	List<Object[]> findEmployeesByManager(@Param("employeeid") String employeeid, @Param("year") Integer year,
			@Param("month") Integer month);

	@Query(value = "SELECT DISTINCT a.employee_id, b.callname, c.name AS Teamname, i.name AS employmentType, a.team "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON b.employeesequenceno = a.employee_id "
			+ "LEFT JOIN daily_worksheets.tbl_teams c ON c.team_id = a.team "
			+ "LEFT JOIN hclhrm_prod.tbl_employment_types i ON i.employmenttypeid = b.employmenttypeid "
			+ "WHERE a.task_date >= :fromDate AND a.task_date <= :toDate " + "AND a.employee_id IN ( "
			+ "   SELECT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary M ON M.employeeid = P.managerid "
			+ "   JOIN daily_worksheets.tbl_employee_worksheet xx ON xx.employee_id = E.employeesequenceno "
			+ "   WHERE M.employeesequenceno IN (:employeeid) " + "   UNION ALL "
			+ "   SELECT DISTINCT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   WHERE E.employeesequenceno = :employeeid " + "   AND E.status IN (1001, 1061, 1092, 1401) "
			+ ")", nativeQuery = true)
	List<Object[]> findEmployeesByManagerByDateRange(@Param("employeeid") String employeeid,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate);

	@Query(value = "SELECT DISTINCT a.employee_id, b.callname, c.name AS Teamname, i.name AS employmentType, a.team "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON b.employeesequenceno = a.employee_id "
			+ "LEFT JOIN daily_worksheets.tbl_teams c ON c.team_id = a.team "
			+ "LEFT JOIN hclhrm_prod.tbl_employment_types i ON i.employmenttypeid = b.employmenttypeid "
			+ "WHERE YEAR(a.task_date) = :year " + "AND MONTH(a.task_date) = :month " + "AND a.team = :tabId "
			+ "AND a.employee_id IN ( " + "   SELECT E.employeesequenceno "
			+ "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary M ON M.employeeid = P.managerid "
			+ "   JOIN daily_worksheets.tbl_employee_worksheet xx ON xx.employee_id = E.employeesequenceno "
			+ "   WHERE M.employeesequenceno IN (:employeeid) " + "   UNION ALL "
			+ "   SELECT DISTINCT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   WHERE E.employeesequenceno = :employeeid " + "   AND E.status IN (1001, 1061, 1092, 1401) "
			+ ")", nativeQuery = true)
	List<Object[]> findEmployeesByManagerbyTeams(@Param("tabId") Integer teamS, @Param("employeeid") String employeeid,
			@Param("year") Integer year, @Param("month") Integer month);

	@Query(value = "SELECT DISTINCT a.employee_id, b.callname, c.name AS Teamname, i.name AS employmentType, a.team "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON b.employeesequenceno = a.employee_id "
			+ "LEFT JOIN daily_worksheets.tbl_teams c ON c.team_id = a.team "
			+ "LEFT JOIN hclhrm_prod.tbl_employment_types i ON i.employmenttypeid = b.employmenttypeid "
			+ "WHERE a.task_date >= :fromDate AND a.task_date <= :toDate " + "AND a.team = :tabId "
			+ "AND a.employee_id IN ( " + "   SELECT E.employeesequenceno "
			+ "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary M ON M.employeeid = P.managerid "
			+ "   JOIN daily_worksheets.tbl_employee_worksheet xx ON xx.employee_id = E.employeesequenceno "
			+ "   WHERE M.employeesequenceno IN (:employeeid) " + "   UNION ALL "
			+ "   SELECT DISTINCT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   WHERE E.employeesequenceno = :employeeid " + "   AND E.status IN (1001, 1061, 1092, 1401) "
			+ ")", nativeQuery = true)
	List<Object[]> findEmployeesByManagerByTeamAndDateRange(@Param("tabId") Integer teamS,
			@Param("employeeid") String employeeid, @Param("fromDate") String fromDate, @Param("toDate") String toDate);

	@Query(value = "SELECT a.employee_id, " + "a.task_date, " + "j.name AS teamname, " + "a.name AS employeename, "
			+ "a.time_block, " + "a.task_description, " + "a.project_name, " + "a.module, "
			+ "IFNULL(CONCAT(p.employeesequenceno, ' - ', p.callname), '--') AS dependent_person, "
			+ "c.name AS category_name, " + "b.name AS activity_name, " + "f.name AS priority_name, "
			+ "d.name AS outcome_name, " + "h.name AS tasktype_name, " + "e.name AS planned_adhoc_name, "
			+ "g.name AS taskalignment_name, " + "a.start_time, " + "a.end_time, " + "a.duration, "
			+ "IFNULL(NULLIF(a.remarks, ''), '--') AS remarks, " + "i.name AS workplace_name, " + "CASE "
			+ "   WHEN a.status = 1001 THEN 'Not Sent for Approval' " + "   WHEN a.status = 1002 THEN 'Auto Approved' "
			+ "   ELSE 'Unknown Status' " + "END AS status, " + "k.att_in, " + "k.att_out, " + "k.NET_HOURS "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN daily_worksheets.tbl_activity b ON a.activity_id = b.activity_id "
			+ "LEFT JOIN daily_worksheets.tbl_category c ON a.category_id = c.category_id "
			+ "LEFT JOIN daily_worksheets.tbl_outcome d ON a.outcome_id = d.outcome_id "
			+ "LEFT JOIN daily_worksheets.tbl_planned_adhoc e ON a.planned_adhoc_id = e.planned_adhoc_id "
			+ "LEFT JOIN daily_worksheets.tbl_priority f ON a.priority_id = f.priority_id "
			+ "LEFT JOIN daily_worksheets.tbl_task_alignment g ON a.taskalignment_id = g.taskalignment_id "
			+ "LEFT JOIN daily_worksheets.tbl_tasktype h ON a.tasktype_id = h.tasktype_id "
			+ "LEFT JOIN daily_worksheets.tbl_workplace i ON a.workplace_id = i.workplace_id "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.dependent_person = p.employeesequenceno "
			+ "LEFT JOIN daily_worksheets.tbl_teams j ON a.team = j.team_id "
			+ "LEFT JOIN test_mum.tbl_att_leave_in_out_status_report k "
			+ "   ON k.employeeid = a.employee_id AND k.dateon = a.task_date " + "WHERE YEAR(a.task_date) = :year "
			+ "AND MONTH(a.task_date) = :month " + "AND a.employee_id IN (" + "   SELECT E.employeesequenceno "
			+ "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary M ON M.employeeid = P.managerid "
			+ "   JOIN daily_worksheets.tbl_employee_worksheet xx ON xx.employee_id = E.employeesequenceno "
			+ "   WHERE M.employeesequenceno IN (:employeeid) " + "   UNION ALL "
			+ "   SELECT DISTINCT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   WHERE E.employeesequenceno = :employeeid " + "   AND E.status IN (1001, 1061, 1092, 1401) " + ")"
			+ "ORDER BY a.employee_id, a.task_date", nativeQuery = true)
	List<Object[]> fetchManagerUnderDataByEmployeeIdAndMonth(@Param("year") int year, @Param("month") int month,
			@Param("employeeid") String employeeid);

	@Query(value = "SELECT a.employee_id, " + "a.task_date, " + "j.name AS teamname, " + "a.name AS employeename, "
			+ "a.time_block, " + "a.task_description, " + "a.project_name, " + "a.module, "
			+ "IFNULL(CONCAT(p.employeesequenceno, ' - ', p.callname), '--') AS dependent_person, "
			+ "c.name AS category_name, " + "b.name AS activity_name, " + "f.name AS priority_name, "
			+ "d.name AS outcome_name, " + "h.name AS tasktype_name, " + "e.name AS planned_adhoc_name, "
			+ "g.name AS taskalignment_name, " + "a.start_time, " + "a.end_time, " + "a.duration, "
			+ "IFNULL(NULLIF(a.remarks, ''), '--') AS remarks, " + "i.name AS workplace_name, " + "CASE "
			+ "   WHEN a.status = 1001 THEN 'Not Sent for Approval' " + "   WHEN a.status = 1002 THEN 'Auto Approved' "
			+ "   ELSE 'Unknown Status' " + "END AS status, " + "k.att_in, " + "k.att_out, " + "k.NET_HOURS "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN daily_worksheets.tbl_activity b ON a.activity_id = b.activity_id "
			+ "LEFT JOIN daily_worksheets.tbl_category c ON a.category_id = c.category_id "
			+ "LEFT JOIN daily_worksheets.tbl_outcome d ON a.outcome_id = d.outcome_id "
			+ "LEFT JOIN daily_worksheets.tbl_planned_adhoc e ON a.planned_adhoc_id = e.planned_adhoc_id "
			+ "LEFT JOIN daily_worksheets.tbl_priority f ON a.priority_id = f.priority_id "
			+ "LEFT JOIN daily_worksheets.tbl_task_alignment g ON a.taskalignment_id = g.taskalignment_id "
			+ "LEFT JOIN daily_worksheets.tbl_tasktype h ON a.tasktype_id = h.tasktype_id "
			+ "LEFT JOIN daily_worksheets.tbl_workplace i ON a.workplace_id = i.workplace_id "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.dependent_person = p.employeesequenceno "
			+ "LEFT JOIN daily_worksheets.tbl_teams j ON a.team = j.team_id "
			+ "LEFT JOIN test_mum.tbl_att_leave_in_out_status_report k "
			+ "   ON k.employeeid = a.employee_id AND k.dateon = a.task_date "
			+ "WHERE a.task_date >= :fromDate AND a.task_date <= :toDate " + "AND a.employee_id IN ("
			+ "   SELECT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary M ON M.employeeid = P.managerid "
			+ "   JOIN daily_worksheets.tbl_employee_worksheet xx ON xx.employee_id = E.employeesequenceno "
			+ "   WHERE M.employeesequenceno IN (:employeeid) " + "   UNION ALL "
			+ "   SELECT DISTINCT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   WHERE E.employeesequenceno = :employeeid " + "   AND E.status IN (1001, 1061, 1092, 1401) " + ") "
			+ "ORDER BY a.employee_id, a.task_date", nativeQuery = true)
	List<Object[]> fetchManagerUnderDataByEmployeeIdAndDateRange(@Param("employeeid") String employeeid,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate);

	@Query(value = "SELECT a.employee_id, " + "a.task_date, " + "j.name AS teamname, " + "a.name AS employeename, "
			+ "a.time_block, " + "a.task_description, " + "a.project_name, " + "a.module, "
			+ "IFNULL(CONCAT(p.employeesequenceno, ' - ', p.callname), '--') AS dependent_person, "
			+ "c.name AS category_name, " + "b.name AS activity_name, " + "f.name AS priority_name, "
			+ "d.name AS outcome_name, " + "h.name AS tasktype_name, " + "e.name AS planned_adhoc_name, "
			+ "g.name AS taskalignment_name, " + "a.start_time, " + "a.end_time, " + "a.duration, "
			+ "IFNULL(NULLIF(a.remarks, ''), '--') AS remarks, " + "i.name AS workplace_name, " + "CASE "
			+ "   WHEN a.status = 1001 THEN 'Not Sent for Approval' " + "   WHEN a.status = 1002 THEN 'Auto Approved' "
			+ "   ELSE 'Unknown Status' " + "END AS status, " + "k.att_in, " + "k.att_out, " + "k.NET_HOURS "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN daily_worksheets.tbl_activity b ON a.activity_id = b.activity_id "
			+ "LEFT JOIN daily_worksheets.tbl_category c ON a.category_id = c.category_id "
			+ "LEFT JOIN daily_worksheets.tbl_outcome d ON a.outcome_id = d.outcome_id "
			+ "LEFT JOIN daily_worksheets.tbl_planned_adhoc e ON a.planned_adhoc_id = e.planned_adhoc_id "
			+ "LEFT JOIN daily_worksheets.tbl_priority f ON a.priority_id = f.priority_id "
			+ "LEFT JOIN daily_worksheets.tbl_task_alignment g ON a.taskalignment_id = g.taskalignment_id "
			+ "LEFT JOIN daily_worksheets.tbl_tasktype h ON a.tasktype_id = h.tasktype_id "
			+ "LEFT JOIN daily_worksheets.tbl_workplace i ON a.workplace_id = i.workplace_id "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.dependent_person = p.employeesequenceno "
			+ "LEFT JOIN daily_worksheets.tbl_teams j ON a.team = j.team_id "
			+ "LEFT JOIN test_mum.tbl_att_leave_in_out_status_report k "
			+ "   ON k.employeeid = a.employee_id AND k.dateon = a.task_date "
			+ "WHERE a.task_date >= :fromDate AND a.task_date <= :toDate AND a.team = :tabId "
			+ "AND a.employee_id IN (" + "   SELECT E.employeesequenceno "
			+ "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary M ON M.employeeid = P.managerid "
			+ "   JOIN daily_worksheets.tbl_employee_worksheet xx ON xx.employee_id = E.employeesequenceno "
			+ "   WHERE M.employeesequenceno IN (:employeeid) " + "   UNION ALL "
			+ "   SELECT DISTINCT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   WHERE E.employeesequenceno = :employeeid " + "   AND E.status IN (1001, 1061, 1092, 1401) " + ") "
			+ "ORDER BY a.employee_id, a.task_date", nativeQuery = true)
	List<Object[]> fetchManagerUnderDataByEmployeeIdAndDateRangeTeam(@Param("tabId") Integer teamS,
			@Param("employeeid") String employeeid, @Param("fromDate") String fromDate, @Param("toDate") String toDate);

	@Query(value = "SELECT a.employee_id, " + "a.task_date, " + "j.name AS teamname, " + "a.name AS employeename, "
			+ "a.time_block, " + "a.task_description, " + "a.project_name, " + "a.module, "
			+ "IFNULL(CONCAT(p.employeesequenceno, ' - ', p.callname), '--') AS dependent_person, "
			+ "c.name AS category_name, " + "b.name AS activity_name, " + "f.name AS priority_name, "
			+ "d.name AS outcome_name, " + "h.name AS tasktype_name, " + "e.name AS planned_adhoc_name, "
			+ "g.name AS taskalignment_name, " + "a.start_time, " + "a.end_time, " + "a.duration, "
			+ "IFNULL(NULLIF(a.remarks, ''), '--') AS remarks, " + "i.name AS workplace_name, " + "CASE "
			+ "   WHEN a.status = 1001 THEN 'Not Sent for Approval' " + "   WHEN a.status = 1002 THEN 'Auto Approved' "
			+ "   ELSE 'Unknown Status' " + "END AS status, " + "k.att_in, " + "k.att_out, " + "k.NET_HOURS "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN daily_worksheets.tbl_activity b ON a.activity_id = b.activity_id "
			+ "LEFT JOIN daily_worksheets.tbl_category c ON a.category_id = c.category_id "
			+ "LEFT JOIN daily_worksheets.tbl_outcome d ON a.outcome_id = d.outcome_id "
			+ "LEFT JOIN daily_worksheets.tbl_planned_adhoc e ON a.planned_adhoc_id = e.planned_adhoc_id "
			+ "LEFT JOIN daily_worksheets.tbl_priority f ON a.priority_id = f.priority_id "
			+ "LEFT JOIN daily_worksheets.tbl_task_alignment g ON a.taskalignment_id = g.taskalignment_id "
			+ "LEFT JOIN daily_worksheets.tbl_tasktype h ON a.tasktype_id = h.tasktype_id "
			+ "LEFT JOIN daily_worksheets.tbl_workplace i ON a.workplace_id = i.workplace_id "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.dependent_person = p.employeesequenceno "
			+ "LEFT JOIN daily_worksheets.tbl_teams j ON a.team = j.team_id "
			+ "LEFT JOIN test_mum.tbl_att_leave_in_out_status_report k "
			+ "   ON k.employeeid = a.employee_id AND k.dateon = a.task_date " + "WHERE YEAR(a.task_date) = :year "
			+ "AND MONTH(a.task_date) = :month AND a.team = :tabId " + "AND a.employee_id IN ("
			+ "   SELECT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary M ON M.employeeid = P.managerid "
			+ "   JOIN daily_worksheets.tbl_employee_worksheet xx ON xx.employee_id = E.employeesequenceno "
			+ "   WHERE M.employeesequenceno IN (:employeeid) " + "   UNION ALL "
			+ "   SELECT DISTINCT E.employeesequenceno " + "   FROM hclhrm_prod.tbl_employee_professional_details P "
			+ "   INNER JOIN hclhrm_prod.tbl_employee_primary E ON E.employeeid = P.employeeid "
			+ "   WHERE E.employeesequenceno = :employeeid " + "   AND E.status IN (1001, 1061, 1092, 1401) " + ")"
			+ "ORDER BY a.employee_id, a.task_date", nativeQuery = true)
	List<Object[]> fetchManagerUnderDataByEmployeeIdMonth(@Param("tabId") Integer teamS,
			@Param("employeeid") String employeeid, @Param("year") int year, @Param("month") int month);
	
	
	
	@Query(value = "SELECT DISTINCT a.employee_id, b.callname, c.name AS Teamname,i.name AS employemntType "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON b.employeesequenceno = a.employee_id "
			+ "LEFT JOIN daily_worksheets.tbl_teams c ON c.team_id = a.team "
			+ "LEFT JOIN hclhrm_prod.tbl_employment_types i ON i.employmenttypeid = b.employmenttypeid "
			+ "WHERE YEAR(a.task_date) = :year AND MONTH(a.task_date) = :month "
			+ "AND a.team = :tabId", nativeQuery = true)
	List<Object[]> fetchEmployeeWorkDataByEmployeeeidMonth(@Param("tabId") Integer tabId, @Param("year") Integer year,
			@Param("month") Integer month);

	@Query(value = "SELECT DISTINCT a.employee_id, b.callname, c.name AS Teamname,i.name AS employemntType "
			+ "FROM daily_worksheets.tbl_employee_worksheet a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON b.employeesequenceno = a.employee_id "
			+ "LEFT JOIN daily_worksheets.tbl_teams c ON c.team_id = a.team "
			+ "LEFT JOIN hclhrm_prod.tbl_employment_types i ON i.employmenttypeid = b.employmenttypeid "
			+ "WHERE a.task_date >= :fromDate AND a.task_date <= :toDate " + "AND a.team = :tabId", nativeQuery = true)
	List<Object[]> fetchEmployeeWorkDataByEmployeeeid(@Param("tabId") Integer tabId, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

}

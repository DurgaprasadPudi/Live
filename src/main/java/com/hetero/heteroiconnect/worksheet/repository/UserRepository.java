package com.hetero.heteroiconnect.worksheet.repository;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hetero.heteroiconnect.worksheet.exception.TaskOverlapException;
import com.hetero.heteroiconnect.worksheet.model.DailyWorkSheet;
import com.hetero.heteroiconnect.worksheet.model.EmployeeSummary;
import com.hetero.heteroiconnect.worksheet.model.TotalWorkingHours;
import com.hetero.heteroiconnect.worksheet.model.UserWorksheet;

 

@Repository
public class UserRepository {
	private JdbcTemplate jdbcTemplate;

	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public UserWorksheet addUserData(UserWorksheet userWorksheet) {
		LocalTime startTime = userWorksheet.getStartTime().toLocalTime();
		Random random = new Random();
		startTime = startTime.plusSeconds(random.nextInt(60));
		Time updatedStartTime = Time.valueOf(startTime);

		Date taskDate = userWorksheet.getTaskDate();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(taskDate);

		StringBuilder overlapQueryBuilder = new StringBuilder();
		List<Map<String, Object>> result;

		if (userWorksheet.getSno() == null) {
			overlapQueryBuilder.append("SELECT task_date, start_time, end_time ")
					.append("FROM daily_worksheets.tbl_employee_worksheet ").append("WHERE employee_id = ? ")
					.append("AND task_date = ? ")
					.append("AND (? BETWEEN start_time AND end_time OR ? BETWEEN start_time AND end_time ")
					.append("OR start_time BETWEEN ? AND ? OR end_time BETWEEN ? AND ?)");

			result = jdbcTemplate.queryForList(overlapQueryBuilder.toString(), userWorksheet.getEmployeeId(),
					formattedDate, updatedStartTime, userWorksheet.getEndTime(), updatedStartTime,
					userWorksheet.getEndTime(), updatedStartTime, userWorksheet.getEndTime());
		} else {
			overlapQueryBuilder.append("SELECT task_date, start_time, end_time ")
					.append("FROM daily_worksheets.tbl_employee_worksheet ").append("WHERE employee_id = ? ")
					.append("AND task_date = ? AND sno != ? ")
					.append("AND (? BETWEEN start_time AND end_time OR ? BETWEEN start_time AND end_time ")
					.append("OR start_time BETWEEN ? AND ? OR end_time BETWEEN ? AND ?)");

			result = jdbcTemplate.queryForList(overlapQueryBuilder.toString(), userWorksheet.getEmployeeId(),
					formattedDate, userWorksheet.getSno(), updatedStartTime, userWorksheet.getEndTime(),
					updatedStartTime, userWorksheet.getEndTime(), updatedStartTime, userWorksheet.getEndTime());
		}

		if (!result.isEmpty()) {
			throw new TaskOverlapException("A task already planned on " + formattedDate + " between the From Time: "
					+ userWorksheet.getStartTime() + " & To Time: " + userWorksheet.getEndTime()
					+ ", Kindly select a different time frame to continue.");
		}

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO daily_worksheets.tbl_employee_worksheet (")
				.append("sno, employee_id, task_date, team, name, time_block, task_description, workplace_id, ")
				.append("project_name, module, dependent_person, category_id, activity_id, priority_id, outcome_id, ")
				.append("tasktype_id, planned_adhoc_id, taskalignment_id, start_time, end_time, duration, remarks, ")
				.append("reporting_manager, status, created_date_time) ")
				.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ")
				.append("ON DUPLICATE KEY UPDATE ")
				.append("employee_id = VALUES(employee_id), task_date = VALUES(task_date), team = VALUES(team), ")
				.append("name = VALUES(name), time_block = VALUES(time_block), task_description = VALUES(task_description), ")
				.append("workplace_id = VALUES(workplace_id), project_name = VALUES(project_name), ")
				.append("module = VALUES(module), dependent_person = VALUES(dependent_person), ")
				.append("category_id = VALUES(category_id), activity_id = VALUES(activity_id), ")
				.append("priority_id = VALUES(priority_id), outcome_id = VALUES(outcome_id), ")
				.append("tasktype_id = VALUES(tasktype_id), planned_adhoc_id = VALUES(planned_adhoc_id), ")
				.append("taskalignment_id = VALUES(taskalignment_id), start_time = VALUES(start_time), ")
				.append("end_time = VALUES(end_time), duration = VALUES(duration), remarks = VALUES(remarks), ")
				.append("reporting_manager = VALUES(reporting_manager)");

		jdbcTemplate.update(sql.toString(), userWorksheet.getSno(), userWorksheet.getEmployeeId(), formattedDate,
				userWorksheet.getTeam(), userWorksheet.getName(), userWorksheet.getTimeBlock(),
				userWorksheet.getTaskDescription(), userWorksheet.getWorkPlaceId(), userWorksheet.getProjectName(),
				userWorksheet.getModule(), userWorksheet.getDependentPerson(), userWorksheet.getCategoryId(),
				userWorksheet.getActivityId(), userWorksheet.getPriorityId(), userWorksheet.getOutcomeId(),
				userWorksheet.getTaskTypeId(), userWorksheet.getPlannedAdhocId(), userWorksheet.getTaskAlignmentId(),
				updatedStartTime, userWorksheet.getEndTime(), userWorksheet.getDuration(), userWorksheet.getRemarks(),
				userWorksheet.getReportingManager(), 1001, LocalDateTime.now());
		return userWorksheet;
	}

	public TotalWorkingHours getDailyWorkSheet(int employeeId) {
		StringBuilder query = new StringBuilder();
		query.append(
				"SELECT a.sno, a.employee_id, a.task_date, a.team, a.name, a.time_block, a.task_description, a.project_name, ")
				.append("IFNULL(NULLIF(a.dependent_person,''),'--') as dependent_person, c.name as category_name, b.name as activity_name, f.name as priority_name, ")
				.append("d.name as outcome_name, h.name as tasktype_name, e.name as planned_adhoc_name, ")
				.append("g.name as taskalignment_name, ")
				.append("DATE_FORMAT(a.start_time, '%H:%i:00') as start_time, ")
				.append("DATE_FORMAT(a.end_time, '%H:%i:00') as end_time, ")
				.append("a.duration, IFNULL(NULLIF(a.remarks, ''), '--') AS remarks, i.name as workplace_name, a.module, a.category_id, a.activity_id, a.priority_id, a.outcome_id, a.tasktype_id, a.workplace_id, a.taskalignment_id, a.planned_adhoc_id, IFNULL(NULLIF(p.callname, ''),'--') AS callname, j.name as teamname ")
				.append("FROM daily_worksheets.tbl_employee_worksheet a ")
				.append("LEFT JOIN daily_worksheets.tbl_activity b ON a.activity_id = b.activity_id ")
				.append("LEFT JOIN daily_worksheets.tbl_category c ON a.category_id = c.category_id ")
				.append("LEFT JOIN daily_worksheets.tbl_outcome d ON a.outcome_id = d.outcome_id ")
				.append("LEFT JOIN daily_worksheets.tbl_planned_adhoc e ON a.planned_adhoc_id = e.planned_adhoc_id ")
				.append("LEFT JOIN daily_worksheets.tbl_priority f ON a.priority_id = f.priority_id ")
				.append("LEFT JOIN daily_worksheets.tbl_task_alignment g ON a.taskalignment_id = g.taskalignment_id ")
				.append("LEFT JOIN daily_worksheets.tbl_tasktype h ON a.tasktype_id = h.tasktype_id ")
				.append("LEFT JOIN daily_worksheets.tbl_workplace i ON a.workplace_id = i.workplace_id ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary  p on a.dependent_person = p.employeesequenceno ")
				.append("LEFT JOIN daily_worksheets.tbl_teams j on a.team = j.team_id ")
				.append("WHERE a.employee_id = ? ").append("AND a.status = 1001 AND a.manager_status= 'P' ")
				.append("ORDER BY a.task_date, a.start_time");

		String totalTimeQuery = "SELECT IFNULL(TIME_FORMAT(SEC_TO_TIME(SUM(TIME_TO_SEC(duration))), '%H:%i'), '00:00') AS total_time "
				+ "FROM daily_worksheets.tbl_employee_worksheet "
				+ "WHERE employee_id = ? AND status IN (1001,1002) AND task_date = CURDATE()";

		String totalDuration = Optional
				.ofNullable(jdbcTemplate.queryForObject(totalTimeQuery, String.class, employeeId))
				.filter(s -> !s.isEmpty()).orElse("00:00");
		String workDurationMessage = calculateWorkDuration(totalDuration);
		List<DailyWorkSheet> dailyWorkSheets = jdbcTemplate.query(query.toString(),
				(rs, rowNum) -> new DailyWorkSheet(rs.getInt("sno"), rs.getInt("employee_id"), rs.getDate("task_date"),
						rs.getString("team"), rs.getString("name"), rs.getString("time_block"),
						rs.getString("task_description"), rs.getString("project_name"),
						rs.getString("dependent_person"), rs.getString("category_name"), rs.getString("activity_name"),
						rs.getString("priority_name"), rs.getString("outcome_name"), rs.getString("tasktype_name"),
						rs.getString("planned_adhoc_name"), rs.getString("taskalignment_name"),
						rs.getTime("start_time"), rs.getTime("end_time"), rs.getString("duration"),
						rs.getString("remarks"), rs.getString("workplace_name"), rs.getString("module"),
						rs.getInt("category_id"), rs.getInt("activity_id"), rs.getInt("priority_id"),
						rs.getInt("outcome_id"), rs.getInt("tasktype_id"), rs.getInt("workplace_id"),
						rs.getInt("taskalignment_id"), rs.getInt("planned_adhoc_id"), rs.getString("callname"),
						rs.getString("teamname")),
				employeeId).stream().collect(Collectors.toList());
		return new TotalWorkingHours(workDurationMessage, dailyWorkSheets);
	}

	private String calculateWorkDuration(String totalDuration) {
		if ("00:00".equalsIgnoreCase(totalDuration)) {
			return "Please fill out the worksheet for the day.";
		}
		String[] parts = totalDuration.split(":");
		int hours = Integer.parseInt(parts[0]);
		int minutes = Integer.parseInt(parts[1]);
		int requiredHours = 9;
		int requiredMinutes = 0;
		int totalWorkedMinutes = (hours * 60) + minutes;
		int requiredTotalMinutes = (requiredHours * 60) + requiredMinutes;
		int remainingMinutes = requiredTotalMinutes - totalWorkedMinutes;
		return (remainingMinutes > 0) ? String.format(
				"You have updated the worksheet for today with %dH:%dM. You still need to fill %dH:%dM to meet your daily requirement.",
				hours, minutes, remainingMinutes / 60, remainingMinutes % 60)
				: "You have filled your required working hours for today.";
	}

	public String submitForApproval(int employeeId) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE daily_worksheets.tbl_employee_worksheet ")
				.append("SET status = 1002, manager_status = 'A' ").append("WHERE employee_id = ? ")
				.append("AND status = 1001");
		int rowsUpdated = jdbcTemplate.update(query.toString(), employeeId);
		if (rowsUpdated > 0) {
			return "Status updated successfully for employeeId: " + employeeId;
		} else {
			return "No records found for employeeId: " + employeeId + " to update.";
		}
	}

	public List<EmployeeSummary> getUserWeekSummaryWorkSheet(int employeeId) {
		StringBuilder query = new StringBuilder();
		query.append(
				"SELECT a.employee_id, a.task_date, a.team, a.name, a.time_block, a.task_description, a.project_name, ")
				.append("IFNULL(NULLIF(a.dependent_person,''),'--') as dependent_person, c.name AS category_name, b.name AS activity_name, f.name AS priority_name, ")
				.append("d.name AS outcome_name, h.name AS tasktype_name, e.name AS planned_adhoc_name, ")
				.append("g.name AS taskalignment_name, ")
				.append("DATE_FORMAT(a.start_time, '%H:%i:00') AS start_time, ")
				.append("DATE_FORMAT(a.end_time, '%H:%i:00') AS end_time, ")
				.append("a.duration, IFNULL(NULLIF(a.remarks,''),'--')  as remarks, a.manager_status, i.name as workplace_name, a.module, p.callname, j.name as teamname ")
				.append("FROM daily_worksheets.tbl_employee_worksheet a ")
				.append("LEFT JOIN daily_worksheets.tbl_activity b ON a.activity_id = b.activity_id ")
				.append("LEFT JOIN daily_worksheets.tbl_category c ON a.category_id = c.category_id ")
				.append("LEFT JOIN daily_worksheets.tbl_outcome d ON a.outcome_id = d.outcome_id ")
				.append("LEFT JOIN daily_worksheets.tbl_planned_adhoc e ON a.planned_adhoc_id = e.planned_adhoc_id ")
				.append("LEFT JOIN daily_worksheets.tbl_priority f ON a.priority_id = f.priority_id ")
				.append("LEFT JOIN daily_worksheets.tbl_task_alignment g ON a.taskalignment_id = g.taskalignment_id ")
				.append("LEFT JOIN daily_worksheets.tbl_tasktype h ON a.tasktype_id = h.tasktype_id ")
				.append("LEFT JOIN daily_worksheets.tbl_workplace i ON a.workplace_id = i.workplace_id ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary  p on a.dependent_person = p.employeesequenceno ")
				.append("LEFT JOIN daily_worksheets.tbl_teams j on a.team = j.team_id ")
				.append("WHERE a.employee_id = ? ").append("AND a.status = 1002 ")
				.append("AND a.task_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND CURDATE() ")
				.append("ORDER BY a.task_date, a.start_time ");

		return jdbcTemplate.query(query.toString(), (rs, rowNum) -> {
			EmployeeSummary activity = new EmployeeSummary();
			activity.setEmployeeId(rs.getInt("employee_id"));
			activity.setTaskDate(rs.getDate("task_date"));
			activity.setTeam(rs.getString("team"));
			activity.setName(rs.getString("name"));
			activity.setTimeBlock(rs.getString("time_block"));
			activity.setTaskDescription(rs.getString("task_description"));
			activity.setProjectName(rs.getString("project_name"));
			activity.setDependentPerson(rs.getString("dependent_person"));
			activity.setCategoryName(rs.getString("category_name"));
			activity.setActivityName(rs.getString("activity_name"));
			activity.setPriorityName(rs.getString("priority_name"));
			activity.setOutcomeName(rs.getString("outcome_name"));
			activity.setTaskTypeName(rs.getString("tasktype_name"));
			activity.setPlannedAdhocName(rs.getString("planned_adhoc_name"));
			activity.setTaskAlignmentName(rs.getString("taskalignment_name"));
			activity.setStartTime(rs.getTime("start_time"));
			activity.setEndTime(rs.getTime("end_time"));
			activity.setDuration(rs.getString("duration"));
			activity.setRemarks(rs.getString("remarks"));
			if (rs.getString("manager_status").equalsIgnoreCase("P")) {
				activity.setManagerStatus("Pending");
			} else if (rs.getString("manager_status").equalsIgnoreCase("A")) {
				activity.setManagerStatus("Approved");
			} else {
				activity.setManagerStatus("Rejected");
			}
			activity.setWorkPlace(rs.getString("workplace_name"));
			activity.setModule(rs.getString("module"));
			activity.setDependentPersonName(rs.getString("callname"));
			activity.setTeamName(rs.getString("teamname"));
			return activity;
		}, employeeId);
	}

	public String deleteTask(int sno) {
		String query = "DELETE FROM daily_worksheets.tbl_employee_worksheet WHERE sno = ? AND status = 1001";
		int rowsUpdated = jdbcTemplate.update(query, sno);
		if (rowsUpdated > 0) {
			return "Task deleted successfully for sno: " + sno;
		} else {
			return "No task found for sno: " + sno + " with status 1001.";
		}
	}

	/*
	 * public UpdateWorkSheet updateWorkSheet(UpdateWorkSheet updateWorkSheet) {
	 * LocalTime startTime = updateWorkSheet.getStartTime().toLocalTime(); Random
	 * random = new Random(); startTime = startTime.plusSeconds(random.nextInt(60));
	 * Time updatedStartTime = Time.valueOf(startTime);
	 * 
	 * Date taskDate = updateWorkSheet.getTaskDate(); SimpleDateFormat formatter =
	 * new SimpleDateFormat("yyyy-MM-dd"); String formattedDate =
	 * formatter.format(taskDate);
	 * 
	 * StringBuilder overlapQueryBuilder = new StringBuilder();
	 * overlapQueryBuilder.append("SELECT task_date, start_time, end_time ")
	 * .append("FROM daily_worksheets.tbl_employee_worksheet ").
	 * append("WHERE employee_id = ? ") .append("AND task_date = ?  AND sno != ? ")
	 * .append("AND (? BETWEEN start_time AND end_time OR ? BETWEEN start_time AND end_time "
	 * ) .append("OR start_time BETWEEN ? AND ? OR end_time BETWEEN ? AND ?)");
	 * String overlapQuery = overlapQueryBuilder.toString(); List<Map<String,
	 * Object>> result = jdbcTemplate.queryForList(overlapQuery,
	 * updateWorkSheet.getEmployeeId(), formattedDate, updateWorkSheet.getSno(),
	 * updatedStartTime, updateWorkSheet.getEndTime(), updatedStartTime,
	 * updateWorkSheet.getEndTime(), updatedStartTime,
	 * updateWorkSheet.getEndTime());
	 * 
	 * if (!result.isEmpty()) { throw new
	 * TaskOverlapException("A task already planned on " + formattedDate +
	 * " between the From Time : " + updateWorkSheet.getStartTime() + " & To Time: "
	 * + updateWorkSheet.getEndTime() +
	 * ", Kindly select  a different time frame to continue."); } StringBuilder
	 * updateQuery = new StringBuilder();
	 * updateQuery.append("UPDATE daily_worksheets.tbl_employee_worksheet SET ")
	 * .append("team = ?, name = ?, time_block = ?, task_description = ?, workplace_id = ?, "
	 * )
	 * .append("project_name = ?, module = ?, dependent_person = ?, category_id = ?, activity_id = ?, "
	 * )
	 * .append("priority_id = ?, outcome_id = ?, tasktype_id = ?, planned_adhoc_id = ?, "
	 * )
	 * .append("taskalignment_id = ?, start_time = ?, end_time = ?, duration = ?, remarks = ?, "
	 * )
	 * .append("reporting_manager = ?, status = ?, created_date_time = ?, employee_id = ?, task_date = ? "
	 * ) .append("WHERE sno = ?");
	 * 
	 * jdbcTemplate.update(updateQuery.toString(), updateWorkSheet.getTeam(),
	 * updateWorkSheet.getName(), updateWorkSheet.getTimeBlock(),
	 * updateWorkSheet.getTaskDescription(), updateWorkSheet.getWorkPlaceId(),
	 * updateWorkSheet.getProjectName(), updateWorkSheet.getModule(),
	 * updateWorkSheet.getDependentPerson(), updateWorkSheet.getCategoryId(),
	 * updateWorkSheet.getActivityId(), updateWorkSheet.getPriorityId(),
	 * updateWorkSheet.getOutcomeId(), updateWorkSheet.getTaskTypeId(),
	 * updateWorkSheet.getPlannedAdhocId(), updateWorkSheet.getTaskAlignmentId(),
	 * updatedStartTime, updateWorkSheet.getEndTime(),
	 * updateWorkSheet.getDuration(), updateWorkSheet.getRemarks(),
	 * updateWorkSheet.getReportingManager(), 1001, LocalDateTime.now(),
	 * updateWorkSheet.getEmployeeId(), formattedDate, updateWorkSheet.getSno());
	 * return updateWorkSheet; }
	 */
}

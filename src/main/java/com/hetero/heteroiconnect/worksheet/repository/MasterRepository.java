package com.hetero.heteroiconnect.worksheet.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hetero.heteroiconnect.worksheet.model.Master;
 

@Repository
public class MasterRepository {
	private JdbcTemplate jdbcTemplate;

	public MasterRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Master> getTaskAlignment() {
		String sql = "SELECT taskalignment_id ,name FROM daily_worksheets.tbl_task_alignment t where status=1001 order by taskalignment_id";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master taskAlignment = new Master();
			taskAlignment.setId(rs.getInt("taskalignment_id"));
			taskAlignment.setName(rs.getString("name"));
			return taskAlignment;
		});
	}

	public List<Master> getCategory() {
		String sql = "SELECT category_id ,name FROM daily_worksheets.tbl_category  where status=1001 order by category_id";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master category = new Master();
			category.setId(rs.getInt("category_id"));
			category.setName(rs.getString("name"));
			return category;
		});
	}

	public List<Master> getPriority() {
		String sql = "SELECT priority_id ,name FROM daily_worksheets.tbl_priority where status=1001 order by priority_id";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master priority = new Master();
			priority.setId(rs.getInt("priority_id"));
			priority.setName(rs.getString("name"));
			return priority;
		});
	}

	public List<Master> getTaskType() {
		String sql = "SELECT tasktype_id,name FROM daily_worksheets.tbl_tasktype  where status=1001 order by tasktype_id";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master taskType = new Master();
			taskType.setId(rs.getInt("tasktype_id"));
			taskType.setName(rs.getString("name"));
			return taskType;
		});
	}

	public List<Master> getOutcome() {
		String sql = "SELECT outcome_id,name FROM daily_worksheets.tbl_outcome   where status=1001 order by outcome_id";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master outcome = new Master();
			outcome.setId(rs.getInt("outcome_id"));
			outcome.setName(rs.getString("name"));
			return outcome;
		});
	}

	public List<Master> getPlannedorAdhoc() {
		String sql = "SELECT planned_adhoc_id,name FROM daily_worksheets.tbl_planned_adhoc  where status=1001 order by planned_adhoc_id";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master planned = new Master();
			planned.setId(rs.getInt("planned_adhoc_id"));
			planned.setName(rs.getString("name"));
			return planned;
		});
	}

	public List<Master> getActivities(int categoryId) {
		StringBuilder query = new StringBuilder();
		query.append(" select b.activity_id as activity_id ,b.name as activity_name ")
				.append(" from daily_worksheets.tbl_category a ")
				.append(" left join daily_worksheets.tbl_activity b on a.category_id =b.category_id ")
				.append(" where a.category_id=? and a.status=1001 and b.status=1001 ");
		return jdbcTemplate.query(query.toString(), (rs, rowNum) -> {
			Master activity = new Master();
			activity.setId(rs.getInt("activity_id"));
			activity.setName(rs.getString("activity_name"));
			return activity;
		}, categoryId);
	}

	public List<Master> getWorkPlace() {
		String sql = "SELECT workplace_id ,name FROM daily_worksheets.tbl_workplace t where status=1001 order by workplace_id";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Master workPlace = new Master();
			workPlace.setId(rs.getInt("workplace_id"));
			workPlace.setName(rs.getString("name"));
			return workPlace;
		});
	}

//	public List<Master> getTeam(int reportingId) {
//		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT DISTINCT a.team_id, a.name ").append("FROM daily_worksheets.tbl_teams a ")
//				.append("LEFT JOIN daily_worksheets.tbl_teams_reportee_map b ").append("ON a.team_id = b.team_id ")
//				.append("WHERE NOT EXISTS (").append("SELECT 1 FROM daily_worksheets.tbl_teams_reportee_map ")
//				.append("WHERE reporting_manager = ?) ").append("OR b.reporting_manager = ? ")
//				.append(" AND a.status=1001 AND b.status=1001 ORDER BY a.team_id");
//		return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
//			Master team = new Master();
//			team.setId(rs.getInt("team_id"));
//			team.setName(rs.getString("name"));
//			return team;
//		}, reportingId, reportingId);
//	}
	
	public List<Master> getTeam(int reportingId, int loginBy) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT a.team_id, a.name ").append("FROM daily_worksheets.tbl_teams a ")
				.append("LEFT JOIN daily_worksheets.tbl_teams_reportee_map b ON a.team_id = b.team_id ")
				.append("WHERE ( ").append("    NOT EXISTS ( ")
				.append("        SELECT 1 FROM daily_worksheets.tbl_teams_reportee_map WHERE reporting_manager = ? ")
				.append("    ) ").append("    OR EXISTS ( ")
				.append("        SELECT 1 FROM daily_worksheets.tbl_teams_reportee_map c ")
				.append("        WHERE c.reporting_manager = ( ").append("            CASE ")
				.append("                WHEN EXISTS (SELECT 1 FROM daily_worksheets.tbl_teams_reportee_map WHERE reporting_manager = ?) ")
				.append("                THEN ? ").append("                ELSE ? ").append("            END ")
				.append("        ) AND a.team_id = c.team_id ").append("    ) ").append(") ")
				.append("AND a.status = 1001 ").append("AND (b.status = 1001 OR b.status IS NULL) ")
 
				.append("ORDER BY a.team_id");
 
		return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
			Master team = new Master();
			team.setId(rs.getInt("team_id"));
			team.setName(rs.getString("name"));
			return team;
		}, reportingId, loginBy, loginBy, reportingId);
	}
	

	public List<Master> getDependentName(String name) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT employeesequenceno, callname ").append("FROM hclhrm_prod.tbl_employee_primary ")
				.append("WHERE employeeid NOT IN (0, 1) ").append("AND status IN (1001, 1061, 1092, 1401) ")
				.append("AND (callname LIKE ? OR employeesequenceno LIKE ?)").append("LIMIT 10");
		return jdbcTemplate.query(query.toString(), (rs, rowNum) -> {
			Master dependentName = new Master();
			dependentName.setId(rs.getInt("employeesequenceno"));
			dependentName.setName(rs.getString("callname"));
			return dependentName;
		}, "%" + name + "%", "%" + name + "%");
	}  

}
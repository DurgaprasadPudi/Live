package com.hetero.heteroiconnect.allowance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.iconnectrights.BadRequestException;

 

@Service
public class AllowanceServiceImpl implements AllowanceService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Object getComponent() {
		String sql = "SELECT id as componentId,allowance_id as allowanceId, allowance_name as allowanceName ,displayname "
				+ "FROM emp_allowance.allowance_type_master WHERE status = 1001";
		List<Map<String, Object>> components = jdbcTemplate.queryForList(sql);
		return components;
	}
	@Transactional
	public String insertAllowancesByType(List<EmployeeAllowanceDTO> dtoList) {
	    Map<String, String> TABLE_MAP = new HashMap<String, String>() {{
	        put("localallowance", "emp_allowance.local_employee_allowance");
	        put("additions", "emp_allowance.additions_employee_allowance");
	        put("specialhra", "emp_allowance.special_hra_employee_allowance");
	    }};

	    dtoList.forEach(dto -> {
	        String tableName = TABLE_MAP.get(dto.getType().toLowerCase());
	        if (tableName == null) {
	            throw new BadRequestException("Invalid allowance type: " + dto.getType());
	        }

	        dto.getComponents().forEach(comp -> {
	 
	            String maxDateSql = "SELECT MAX(effective_date) FROM " + tableName
	                    + " WHERE employee_id = ? AND allowance_id = ? AND status=1001";

	            LocalDate maxDate = jdbcTemplate.queryForObject(
	                    maxDateSql,
	                    new Object[]{dto.getEmployeeId(), comp.getAllowanceId()},
	                    LocalDate.class
	            );

	            if (maxDate != null && comp.getEffectiveDate().isBefore(maxDate)) {
	                throw new BadRequestException(
	                        "For employee ID " + dto.getEmployeeId() +
	                        ", the effective date " + comp.getEffectiveDate() +
	                        " must not be earlier than the current maximum effective date " + maxDate + "."
	                );
	            }
	        });

	        String sql = "INSERT INTO " + tableName
	                + " (employee_id, allowance_id, allowance_amount, effective_date, created_by, created_date) "
	                + "VALUES (?, ?, ?, ?, ?, NOW())";

	        try {
	            jdbcTemplate.batchUpdate(sql, dto.getComponents(), dto.getComponents().size(), (ps, comp) -> {
	                ps.setInt(1, dto.getEmployeeId());
	                ps.setInt(2, comp.getAllowanceId());
	                ps.setBigDecimal(3, comp.getAllowanceAmount() != null ? comp.getAllowanceAmount() : BigDecimal.ZERO);
	                ps.setDate(4, java.sql.Date.valueOf(comp.getEffectiveDate()));
	                ps.setString(5, dto.getCreatedBy());
	            });
	        } catch (DuplicateKeyException e) {
	        	throw new BadRequestException(
	        		    "Allowance for employee ID " + dto.getEmployeeId() +
	        		    " already exists with effective date ,Please deactivate the existing record before adding a new one.");

	        }
	    });

	    return "Allowances inserted successfully";
	}


	@Override
	public List<EmployeeAllowanceDTO> getAllAllowances(String type) {
		String tableName;
		switch (type.toLowerCase()) {
		case "localallowance":
			tableName = "emp_allowance.local_employee_allowance";
			break;
		case "specialhra":
			tableName = "emp_allowance.special_hra_employee_allowance";
			break;
		case "additions":
			tableName = "emp_allowance.additions_employee_allowance";
			break;
		default:
			throw new BadRequestException("❌ Invalid type. Allowed: localallowance, specialhra, additions");
		}

		String sql = "SELECT ea.id, ea.employee_id, ep.callname AS employeeName, "
				+ "ea.allowance_id, ea.allowance_amount, ea.effective_date, "
				+ "pd.INCREMENTTYPEID, ea.created_by, ea.created_date, "
				+ "d.name AS department, e.name AS designation, f.name AS businessUnit, " + "g.allowance_name, "
				+ "CASE WHEN pd.INCREMENTTYPEID = 1 THEN 'STROKE' ELSE 'REGULAR' END AS employeeType " + "FROM "
				+ tableName + " ea "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ea.employee_id = ep.employeesequenceno "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_details pd ON pd.employeeid = ep.employeeid "
				+ "LEFT JOIN hcladm_prod.tbl_department d ON d.departmentid = pd.departmentid "
				+ "LEFT JOIN hcladm_prod.tbl_designation e ON e.designationid = pd.designationid "
				+ "LEFT JOIN hcladm_prod.tbl_businessunit f ON f.businessunitid = ep.companyid "
				+ "LEFT JOIN emp_allowance.allowance_type_master g ON g.allowance_id = ea.allowance_id "
				+ "WHERE ea.status = 1001 ORDER BY ea.created_date DESC";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

		return rows.stream().collect(Collectors.groupingBy(r -> (Integer) r.get("employee_id"))).values().stream()
				.map(list -> {
					Map<String, Object> first = list.get(0);
					EmployeeAllowanceDTO dto = new EmployeeAllowanceDTO();
					dto.setEmployeeId((Integer) first.get("employee_id"));
					dto.setEmployeeName((String) first.get("employeeName"));
					dto.setDepartment((String) first.get("department"));
					dto.setDesignation((String) first.get("designation"));
					dto.setBusinessUnit((String) first.get("businessUnit"));
					dto.setCreatedBy((String) first.get("created_by"));
					dto.setEmployeeType((String) first.get("employeeType"));

					List<AllowanceComponent> comps = list.stream().map(r -> {
						AllowanceComponent c = new AllowanceComponent();
						c.setId((Integer) r.get("id"));
						c.setAllowanceId((Integer) r.get("allowance_id"));
						c.setAllowanceAmount((BigDecimal) r.get("allowance_amount"));
						c.setAllowanceName((String) r.get("allowance_name"));
						java.sql.Date d = (java.sql.Date) r.get("effective_date");
						if (d != null)
							c.setEffectiveDate(d.toLocalDate());
						c.setCreatedBy((String) r.get("created_by"));
						return c;
					}).collect(Collectors.toList());

					dto.setComponents(comps);
					dto.setTotalAllowanceAmount(comps.stream()
							.map(c -> c.getAllowanceAmount() != null ? c.getAllowanceAmount() : BigDecimal.ZERO)
							.reduce(BigDecimal.ZERO, BigDecimal::add).toPlainString());
					return dto;
				}).collect(Collectors.toList());
	}

	@Transactional
	public String getComponentRemove(int id, String type, int updatedBy) {
		String sql = null;

		if ("localallowance".equalsIgnoreCase(type)) {
			sql = "UPDATE emp_allowance.local_employee_allowance "
					+ "SET status = 1002,updated_by=?, updated_timestamp = NOW() WHERE id = ? and status=1001";
		} else if ("additions".equalsIgnoreCase(type)) {
			sql = "UPDATE emp_allowance.additions_employee_allowance "
					+ "SET status = 1002, updated_by=?,updated_timestamp = NOW() WHERE id = ? and status=1001";
		} else if ("specialhra".equalsIgnoreCase(type)) {
			sql = "UPDATE emp_allowance.special_hra_employee_allowance "
					+ "SET status = 1002,updated_by=?, updated_timestamp = NOW() WHERE id = ? and status=1001";
		} else {
			return "Invalid type provided. Use: local / additions / deductions";
		}

		int rows = jdbcTemplate.update(sql, updatedBy, id);
		return (rows > 0) ? type + " component removed successfully" : "No record found for id " + id + " in " + type;
	}

	@Override
	public List<EmployeeAllowanceDTO> getAllowancesByEmployeeid(int employeeid, String type) {
		String tableName;
		switch (type.toLowerCase()) {
		case "localallowance":
			tableName = "emp_allowance.local_employee_allowance";
			break;
		case "additions":
			tableName = "emp_allowance.additions_employee_allowance";
			break;
		case "specialhra":
			tableName = "emp_allowance.special_hra_employee_allowance";
			break;
		default:
			throw new BadRequestException("Invalid type: " + type);
		}

		String sql = "SELECT ea.id, ea.employee_id, ea.allowance_id, ea.allowance_amount, ea.effective_date, "
				+ "ea.created_by, ea.created_date, ea.updated_by, ea.updated_timestamp, "
				+ "d.name AS department, e.name AS designation, f.name AS businessUnit, g.allowance_name " + "FROM "
				+ tableName + " ea "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ea.employee_id = ep.employeesequenceno "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_details pd ON pd.employeeid = ep.employeeid "
				+ "LEFT JOIN hcladm_prod.tbl_department d ON d.departmentid = pd.departmentid "
				+ "LEFT JOIN hcladm_prod.tbl_designation e ON e.designationid = pd.designationid "
				+ "LEFT JOIN hcladm_prod.tbl_businessunit f ON f.businessunitid = ep.companyid "
				+ "LEFT JOIN emp_allowance.allowance_type_master g ON g.allowance_id = ea.allowance_id "
				+ "WHERE ea.status = 1001 AND ea.employee_id = ?";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, employeeid);
		if (rows.isEmpty()) {
			return Collections.emptyList();
		}

		EmployeeAllowanceDTO dto = new EmployeeAllowanceDTO();
		dto.setEmployeeId(employeeid);
		dto.setType(type);
		dto.setDepartment((String) rows.get(0).get("department"));
		dto.setDesignation((String) rows.get(0).get("designation"));
		dto.setBusinessUnit((String) rows.get(0).get("businessUnit"));
		dto.setCreatedBy((String) rows.get(0).get("created_by"));

		List<AllowanceComponent> components = rows.stream().map(row -> {
			AllowanceComponent comp = new AllowanceComponent();
			comp.setId((Integer) row.get("id"));
			comp.setAllowanceId((Integer) row.get("allowance_id"));
			BigDecimal amount = (BigDecimal) row.get("allowance_amount");
			comp.setAllowanceAmount(amount);
			comp.setAllowanceName((String) row.get("allowance_name"));
			java.sql.Date sqlDate = (java.sql.Date) row.get("effective_date");
			if (sqlDate != null) {
				comp.setEffectiveDate(sqlDate.toLocalDate());
			}
			comp.setCreatedBy((String) row.get("created_by"));
			return comp;
		}).collect(Collectors.toList());

		dto.setComponents(components);
		return Collections.singletonList(dto);
	}

	public Object getAllAllowanceEmployeeInfo(String type) {
	    String tableName;
	    switch (type.toLowerCase()) {
	        case "localallowance":
	            tableName = "emp_allowance.local_employee_allowance";
	            break;
	        case "additions":
	            tableName = "emp_allowance.additions_employee_allowance";
	            break;
	        case "specialhra":
	            tableName = "emp_allowance.special_hra_employee_allowance";
	            break;
	        default:
	            throw new BadRequestException("❌ Invalid type. Allowed: localallowance, additions, specialhra");
	    }

	    StringBuffer sql = new StringBuffer();

	    sql.append("SELECT ");
	    sql.append("P.employeesequenceno AS EmployeeSeq, ");
	    sql.append("P.employeeid AS Employeeid, ");
	    sql.append("ST.NAME AS STATUS, ");
	    sql.append("P.callname AS Employeename, ");
	    sql.append("IFNULL(B.dateofjoin, '0000-00-00') AS DOJ, ");
	    sql.append("BU.NAME AS BUNAME, ");
	    sql.append("DE.name AS Department, ");
	    sql.append("DES.name AS Designation, ");
	    sql.append("PC.email AS EMPLOYEEMAIL, ");
	    sql.append("M.callname AS ManagerName, ");
	    sql.append("MPC.email AS ManagerEmail, ");
	    sql.append("CONCAT( ");
	    sql.append("    TIMESTAMPDIFF(YEAR, B.dateofjoin, CURDATE()), ' years ', ");
	    sql.append("    TIMESTAMPDIFF(MONTH, B.dateofjoin, CURDATE()) % 12, ' months' ");
	    sql.append(") AS Experience, ");
	    sql.append("COS.name AS CostCenter, ");
	    sql.append("CASE WHEN T.INCREMENTTYPEID = 1 THEN 'STROKE' ELSE 'REGULAR' END AS EmployeeType, ");
	    sql.append("IFNULL(SUM(EA.allowance_amount), 0) AS TotalAllowance, ");
	    sql.append("GROUP_CONCAT(CONCAT(G.allowance_name, ': ', EA.allowance_amount) SEPARATOR ', ') AS AllowanceDetails ");
	    sql.append("FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY P ");
	    sql.append("LEFT JOIN HCLHRM_PROD.tbl_status_codes ST ON ST.STATUS = P.STATUS ");
	    sql.append("LEFT JOIN hcladm_prod.tbl_businessunit BU ON BU.businessunitid = P.companyid ");
	    sql.append("LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE B ON B.employeeid = P.employeeid ");
	    sql.append("LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON P.employeeid = D.employeeid ");
	    sql.append("LEFT JOIN hcladm_prod.tbl_department DE ON DE.departmentid = D.departmentid ");
	    sql.append("LEFT JOIN hcladm_prod.tbl_designation DES ON DES.designationid = D.designationid ");
	    sql.append("LEFT JOIN hcladm_prod.tbl_costcenter COS ON COS.costcenterid = P.costcenterid ");
	    sql.append("LEFT JOIN hclhrm_prod.tbl_employee_professional_contact PC ON P.employeeid = PC.employeeid ");
	    sql.append("LEFT JOIN hclhrm_prod.tbl_employee_primary M ON M.employeeid = D.managerid ");
	    sql.append("LEFT JOIN hclhrm_prod.tbl_employee_professional_contact MPC ON MPC.employeeid = M.employeeid ");
	    sql.append("LEFT JOIN hcladm_prod.tbl_increment_type T ON T.INCREMENTTYPEID = D.INCREMENTTYPEID ");
	    sql.append("LEFT JOIN " + tableName + " EA ");
	    sql.append("       ON EA.employee_id = P.employeesequenceno AND EA.status = 1001 ");
	    sql.append("LEFT JOIN emp_allowance.allowance_type_master G ");
	    sql.append("       ON G.allowance_id = EA.allowance_id ");
	    sql.append("WHERE P.status In (1001,1092,1401) ");
	    sql.append("  AND BU.callname IN ('HYD') "); 
	    sql.append("GROUP BY P.employeesequenceno ");
	    sql.append("ORDER BY P.employeesequenceno");

	    return jdbcTemplate.queryForList(sql.toString());
	}

}

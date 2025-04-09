package com.hetero.heteroiconnect.jobcode;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class JobcodeDaoImp implements JobcodeDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final Logger logger = LoggerFactory.getLogger(JobcodeDaoImp.class);

	public Map<String, Object> checkFile(String file) {
		String query = "SELECT * FROM test.tbl_job_codes WHERE PDF='" + file+"'";
		try {
			return jdbcTemplate.queryForMap(query);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}

	}

	public int uploadFile(String jcode, String eid, MultipartFile file) {
		String fileName = file.getOriginalFilename();
		String filePath = "C:\\Jobcodes\\";
		Connection con = null;
		Statement st = null;
		int i = 0;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			con.setAutoCommit(false);
			st = con.createStatement();
			String query1 = "INSERT INTO test.tbl_job_codes(TITLE,PDF) VALUES('" + jcode + "','" + fileName + "')";
			String query2 = "INSERT INTO test.tbl_employee_jobcode_mapping(EMPLOYEEID,POSITION_ID,LUPDATE) select '"
					+ eid + "',POSITION_ID,CURRENT_TIMESTAMP() FROM test.tbl_job_codes WHERE title='" + jcode+"'";

			if (st.executeUpdate(query1) > 0) {
				if (st.executeUpdate(query2) > 0) {
					File path = new File(filePath);
					if (path.exists()) {
						file.transferTo(new File(filePath + fileName));
						con.commit();
						logger.info("JD uploaded for employee: " + eid);
						i = 1;
					} else {
						if (path.mkdir() == true) {
							file.transferTo(new File(filePath + fileName));
							con.commit();
							logger.info("JD uploaded for employee: " + eid);
							i = 1;
						}
					}
				} else {
					con.rollback();
					logger.info("Error uploading JD, transaction rollback successful");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
				logger.error("Error uploading JD, transaction rollback successful");
			} catch (SQLException f) {
				f.printStackTrace();
			}
		} finally {
			try {
				if (con != null) {
					con.close();
					logger.info("connection closed successful");

				}
				if (st != null) {
					st.close();
					logger.info("statement closed successful");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i;

	}

	public Map<String,Object> checkJobcode(String jcode) {
		String query = "SELECT * FROM test.tbl_job_codes WHERE TITLE='" + jcode+"'";
		try {
			return jdbcTemplate.queryForMap(query);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, Object> checkJobcodeEmp(String eid) {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT B.title 'jcode', 1001 'jstatus'");
		query.append(" FROM test.tbl_employee_jobcode_mapping A ");
		query.append(" JOIN test.tbl_job_codes B ON A.position_id=B.position_id ");
		query.append(" WHERE A.employeeid='" + eid+"'");

		try {
			return jdbcTemplate.queryForMap(query.toString());
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			logger.info("Job code not found for employee " + eid);
			return null;
		}
	}

	public EmployeeJobcode GetEmpData(String eid) {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT A.callname 'name',D.name 'designation',C.name 'department',E.name 'bu',F.name 'status' ");
		query.append(" FROM hclhrm_prod.tbl_employee_primary A ");
		query.append(" LEFT JOIN hclhrm_prod.tbl_employee_professional_details B ON  A.employeeid=B.employeeid ");
		query.append(" LEFT JOIN hcladm_prod.tbl_department C ON B.departmentid=C.departmentid ");
		query.append(" LEFT JOIN hcladm_prod.tbl_designation D ON B.designationid=D.designationid ");
		query.append(" LEFT JOIN hcladm_prod.tbl_businessunit E ON A.companyid=E.businessunitid ");
		query.append(" LEFT JOIN hclhrm_prod.tbl_status_codes F ON A.status=F.status ");
		query.append(" WHERE A.employeesequenceno='" + eid+"'");

		try {
			return (EmployeeJobcode) jdbcTemplate.queryForObject(query.toString(), (rs, rowNum) -> {
				return new EmployeeJobcode().setName(rs.getString("name")).setStatus(rs.getString("status"))
						.setDepartment(rs.getString("department")).setDesignation(rs.getString("designation"))
						.setBu(rs.getString("bu"));
			});
		} catch (EmptyResultDataAccessException e) {
			logger.info("employee " + eid + " not found");
			e.printStackTrace();
			return null;
		}
	}

	public Object checkJobcodeGetData(String eid) {
		Map<String, Object> jcode = checkJobcodeEmp(eid);
		if (jcode == null) {
			return GetEmpData(eid);
		} else {
			logger.info("Jobcode of employee " + eid + " is " + jcode.get("jcode"));
			return jcode;
		}
	}

}

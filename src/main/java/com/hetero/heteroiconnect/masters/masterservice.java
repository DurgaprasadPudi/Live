package com.hetero.heteroiconnect.masters;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.hetero.heteroiconnect.masters.entity.Department;
import com.hetero.heteroiconnect.masters.entity.Designation;

 

@Service
public class masterservice {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory.getLogger(masterservice.class);

	public List<Map<String, Object>> getDistinctBusinessUnits(int empCode) {
		String sql = "SELECT BU.BUSINESSUNITID, BU.NAME AS BUNAME " + "FROM hclhrm_prod.tbl_employee_primary a "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_businessunit b ON a.employeeid = b.employeeid  "
				+ "LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.BUSINESSUNITID = b.BUSINESSUNITID "
				+ "WHERE a.employeesequenceno = ?";
		logger.debug("Executing SQL query: {}", sql);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, empCode);
		logger.debug("Query result: {}", result);
		return result;
	}

	public Object fetchDepartments() {
		try {
			String sql = "SELECT parentdepartmentid departmentid, NAME, code, status, departmentid AS sectionid FROM hcladm_prod.tbl_department WHERE parentdepartmentid != 0 AND status = 1001 ORDER BY name ASC";
			logger.debug(" department fetching query: {}", sql);
			return jdbcTemplate.queryForList(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.debug("An exception occurred fetching departments: ", ex);
			return new ArrayList<>();
		}
	}
	

	public Object fetchDesignations() {
		try {
			String sql = "SELECT designationid,name,code,status,createdby FROM hcladm_prod.tbl_designation t ORDER BY name ASC";
			return jdbcTemplate.queryForList(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.debug("An exception occurred fetching designations: ", ex.getMessage());
			return new ArrayList<>();
		}
	}

	public Object fetchuniversitys() {
		try {
			String sql = "SELECT * FROM hcladm_prod.tbl_university t ORDER BY name ASC";
			return jdbcTemplate.queryForList(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.debug("An exception occurred fetching universitys: ", ex);
			return new ArrayList<>();
		}
	}
	public ResponseEntity<Map<String, Object>> insertUniversity(String name, int createdBy, String type) {
	    Map<String, Object> response = new HashMap<>();
	    if (name == null || name.trim().isEmpty()) {
	        response.put("status", "error");
	        response.put("message", "University name cannot be null or empty");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    String checkDuplicateSql = "SELECT COUNT(*) FROM hcladm_prod.tbl_university WHERE name = ?";

	    try {
	        int count = jdbcTemplate.queryForObject(checkDuplicateSql, new Object[] { name }, Integer.class);
	        if (count > 0) {
	            response.put("status", "error");
	            response.put("message", "University with the name '" + name + "' already exists.");
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }
	        String insertUniversitySql = "INSERT INTO hcladm_prod.tbl_university (name) VALUES (?)";
	        int rowsAffected = jdbcTemplate.update(insertUniversitySql, name);
	        if (rowsAffected > 0) {
	            response.put("status", "success");
	            response.put("message", "University inserted successfully");
	            String getUniversityIdSql = "SELECT LAST_INSERT_ID()";
	            int universityId = jdbcTemplate.queryForObject(getUniversityIdSql, Integer.class);
	            String insertMasterLogSql = "INSERT INTO test.masterlog (id, createdby, type, datecreated, modifiedby, modifiedDate, name) "
	                    + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, NULL, ?)";
	            int rowsInMasterLog = jdbcTemplate.update(insertMasterLogSql, universityId, createdBy, type, 0, name);
	            if (rowsInMasterLog > 0) {
	                response.put("masterlogStatus", "success");
	                response.put("masterlogMessage", "Log entry added to masterlog successfully.");
	            } else {
	                response.put("masterlogStatus", "failure");
	                response.put("masterlogMessage", "Failed to insert log entry into masterlog.");
	            }
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } else {
	            response.put("status", "failure");
	            response.put("message", "No rows affected");
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	    } catch (Exception ex) {
	        response.put("status", "error");
	        response.put("message", "Error occurred while inserting the university");
	        response.put("error", ex.getMessage());
	        logger.debug("Exception in insertUniversity", ex.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	public ResponseEntity<Map<String, Object>> insertDepartment(String name, String code, int status, int createdby, String type) {
	    Map<String, Object> response = new HashMap<>();
	    if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty() || status == 0) {
	        response.put("status", "error");
	        response.put("message", "Parameters 'name', 'code', and 'status' cannot be null or empty");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    String sqlCheckDepartmentExists = "SELECT COUNT(*) FROM hcladm_prod.tbl_department WHERE name = ?";
	    Integer existingDepartmentCount = jdbcTemplate.queryForObject(sqlCheckDepartmentExists, Integer.class, name);
	    if (existingDepartmentCount != null && existingDepartmentCount > 0) {
	        response.put("status", "error");
	        response.put("message", "Department with the same name already exists");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    String sqlInsertFirstDepartment = "INSERT INTO hcladm_prod.tbl_department (name, code, status, createdby, datecreated) VALUES (?,?,?,?,?)";
	    
	    try {
	        KeyHolder keyHolder = new GeneratedKeyHolder();
	        jdbcTemplate.update(connection -> {
	            PreparedStatement ps = connection.prepareStatement(sqlInsertFirstDepartment, Statement.RETURN_GENERATED_KEYS);
	            ps.setString(1, name);
	            ps.setString(2, code);
	            ps.setInt(3, status);
	            ps.setInt(4, createdby);
	            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
	            ps.setTimestamp(5, currentTimestamp);
	            return ps;
	        }, keyHolder);
	        
	        Number departmentId = keyHolder.getKey();
	        
	        if (departmentId == null) {
	            response.put("status", "failure");
	            response.put("message", "Failed to retrieve the generated department ID");
	            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	        String sqlInsertSecondDepartment = "INSERT INTO hcladm_prod.tbl_department (name, code, parentdepartmentid, status, createdby, datecreated) VALUES (?,?,?,?,?,?)";
	        int rowsAffected = jdbcTemplate.update(sqlInsertSecondDepartment, name, code, departmentId, status, createdby, new java.sql.Timestamp(System.currentTimeMillis()));

	        if (rowsAffected > 0) {
	            response.put("status", "success");
	            response.put("message", "Departments inserted successfully");
	            response.put("rowsAffected", rowsAffected);
	            response.put("firstDepartmentId", departmentId);
	            String insertMasterLogSql = "INSERT INTO test.masterlog (id, type, createdby, modifiedby, modifiedDate, name) "
	                    + "VALUES (?, ?, ?, ?, current_timestamp, ?)";
	            int rowsInMasterLog = jdbcTemplate.update(insertMasterLogSql, departmentId, type, createdby, 0, name);
	            if (rowsInMasterLog > 0) {
	                response.put("masterlogStatus", "success");
	                response.put("masterlogMessage", "Log entry added to masterlog successfully.");
	            } else {
	                response.put("masterlogStatus", "failure");
	                response.put("masterlogMessage", "Failed to insert log entry into masterlog.");
	            }

	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } else {
	            response.put("status", "failure");
	            response.put("message", "Failed to insert the second department");
	            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    } catch (Exception ex) {
	        response.put("status", "error");
	        response.put("message", "Error occurred while inserting the department");
	        response.put("error", ex.getMessage());
	        logger.error("Error occurred while inserting the department: " + ex.getMessage(), ex);
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

//	public ResponseEntity<Map<String, Object>> insertDesignation(String name, String code, int status, int createdby,
//			String type) {
//		Map<String, Object> response = new HashMap<>();
//		if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty() || status == 0) {
//			response.put("status", "error");
//			response.put("message", "Parameters name, code, and status cannot be null or empty");
//			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//		}
//		String sqlCheckDuplicate = "SELECT COUNT(*) FROM hcladm_prod.tbl_designation WHERE name = ?";
//		Integer existingDesignationCount = jdbcTemplate.queryForObject(sqlCheckDuplicate, Integer.class, name);
//
//		if (existingDesignationCount != null && existingDesignationCount > 0) {
//			response.put("status", "error");
//			response.put("message", "Designation with the same name already exists");
//			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//		}
//
//		try {
//			String sql = "INSERT INTO hcladm_prod.tbl_designation (name, code, status, createdby, datecreated) VALUES (?, ?, ?, ?, ?)";
//			int rowsAffected = jdbcTemplate.update(sql, name, code, status, createdby,
//					new java.sql.Timestamp(System.currentTimeMillis()));
//
//			if (rowsAffected > 0) {
//				response.put("status", "success");
//				response.put("message", "Designation inserted successfully");
//				response.put("rowsAffected", rowsAffected);
//				String insertMasterLogSql = "INSERT INTO test.masterlog (id, type, createdby, modifiedby, modifiedDate) "
//						+ "VALUES (?, ?, ?, ?, current_timestamp)";
//				int designationId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
//				int rowsInMasterLog = jdbcTemplate.update(insertMasterLogSql, designationId, type, createdby, 0);
//
//				if (rowsInMasterLog > 0) {
//					response.put("masterlogStatus", "success");
//					response.put("masterlogMessage", "Log entry added to masterlog successfully.");
//				} else {
//					response.put("masterlogStatus", "failure");
//					response.put("masterlogMessage", "Failed to insert log entry into masterlog.");
//				}
//
//				return new ResponseEntity<>(response, HttpStatus.OK);
//			} else {
//				response.put("status", "failure");
//				response.put("message", "No rows affected");
//				return new ResponseEntity<>(response, HttpStatus.OK);
//			}
//		} catch (Exception ex) {
//			response.put("status", "error");
//			response.put("message", "Error occurred while inserting the designation");
//			response.put("error", ex.getMessage());
//			logger.debug("Exception insert Designation", ex.getMessage());
//			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	public ResponseEntity<Map<String, Object>> insertDesignation(String name, String code, int status, int createdby, String type) {
	    Map<String, Object> response = new HashMap<>();
	    
	    // Validate input parameters
	    if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty() || status == 0) {
	        response.put("status", "error");
	        response.put("message", "Parameters name, code, and status cannot be null or empty");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    // Check for duplicate designation name
	    String sqlCheckDuplicate = "SELECT COUNT(*) FROM hcladm_prod.tbl_designation WHERE name = ?";
	    Integer existingDesignationCount = jdbcTemplate.queryForObject(sqlCheckDuplicate, Integer.class, name);

	    if (existingDesignationCount != null && existingDesignationCount > 0) {
	        response.put("status", "error");
	        response.put("message", "Designation with the same name already exists");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    try {
	        String sql = "INSERT INTO hcladm_prod.tbl_designation (name, code, status, createdby, datecreated) VALUES (?, ?, ?, ?, ?)";
	        int rowsAffected = jdbcTemplate.update(sql, name, code, status, createdby, new java.sql.Timestamp(System.currentTimeMillis()));

	        if (rowsAffected > 0) {
	            response.put("status", "success");
	            response.put("message", "Designation inserted successfully");
	            response.put("rowsAffected", rowsAffected);
	            int designationId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
	            String insertMasterLogSql = "INSERT INTO test.masterlog (id, type, createdby, modifiedby, modifiedDate, name) "
	                    + "VALUES (?, ?, ?, ?, current_timestamp, ?)";
	            int rowsInMasterLog = jdbcTemplate.update(insertMasterLogSql, designationId, type, createdby, 0, name);
	            if (rowsInMasterLog > 0) {
	                response.put("masterlogStatus", "success");
	                response.put("masterlogMessage", "Log entry added to masterlog successfully.");
	            } else {
	                response.put("masterlogStatus", "failure");
	                response.put("masterlogMessage", "Failed to insert log entry into masterlog.");
	            }

	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } else {
	            response.put("status", "failure");
	            response.put("message", "No rows affected");
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	    } catch (Exception ex) {
	        response.put("status", "error");
	        response.put("message", "Error occurred while inserting the designation");
	        response.put("error", ex.getMessage());
	        logger.debug("Exception insert Designation", ex.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	public ResponseEntity<Map<String, Object>> updateUniversity(int universityid, String name, int createdby, String type) {
	    Map<String, Object> response = new HashMap<>();

	    if (name == null || name.trim().isEmpty() || universityid == 0) {
	        response.put("status", "error");
	        response.put("message", "University name and universityid cannot be null or empty");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    
	    String currentNameSql = "SELECT name FROM hcladm_prod.tbl_university WHERE universityid = ?";
	    String existingName = jdbcTemplate.queryForObject(currentNameSql, String.class, universityid);
	    
	    String checkSql = "SELECT COUNT(*) FROM hcladm_prod.tbl_university WHERE name = ? AND universityid != ?";
	    int count = jdbcTemplate.queryForObject(checkSql, Integer.class, name, universityid);
	    if (count > 0) {
	        response.put("status", "error");
	        response.put("message", "University with the name '" + name + "' already exists.");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    String sql = "UPDATE hcladm_prod.tbl_university SET name=? WHERE universityid=?";
	    try {
	        int rowsAffected = jdbcTemplate.update(sql, name, universityid);

	        if (rowsAffected > 0) {
	            response.put("status", "success");
	            response.put("message", "University updated successfully");
	            response.put("rowsAffected", rowsAffected);

	            String insertMasterLogSql = "INSERT INTO test.masterlog (id, type, createdby, modifiedby, modifiedDate, existingName, name) "
                        + "VALUES (?, ?, ?, ?, current_timestamp, ?, ?)";
              int rowsInMasterLog = jdbcTemplate.update(insertMasterLogSql, universityid, type, 0, createdby, existingName, name);

	            if (rowsInMasterLog > 0) {
	                response.put("masterlogStatus", "success");
	                response.put("masterlogMessage", "Log entry added to masterlog successfully.");
	            } else {
	                response.put("masterlogStatus", "failure");
	                response.put("masterlogMessage", "Failed to insert log entry into masterlog.");
	            }

	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } else {
	            response.put("status", "failure");
	            response.put("message", "No rows affected");
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	    } catch (Exception ex) {
	        response.put("status", "error");
	        response.put("message", "Error occurred while updating the university");
	        response.put("error", ex.getMessage());
	        logger.debug("Exception update University", ex.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

//	public ResponseEntity<Map<String, Object>> updateDesignation(int designationid, String name, String code,
//			int status, int modifiedby, String type) {
//		Map<String, Object> response = new HashMap<>();
//
//		if (name == null || name.trim().isEmpty() || designationid == 0 || code == null || code.trim().isEmpty()
//				|| status == 0) {
//			response.put("status", "error");
//			response.put("message", "Designation name, designationid, code, and status cannot be null or empty");
//			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//		}
//
//		String sqlCheckDuplicate = "SELECT COUNT(*) FROM hcladm_prod.tbl_designation WHERE name = ? AND designationid != ?";
//		Integer existingDesignationCount = jdbcTemplate.queryForObject(sqlCheckDuplicate, Integer.class, name,
//				designationid);
//
//		if (existingDesignationCount != null && existingDesignationCount > 0) {
//			response.put("status", "error");
//			response.put("message", "Designation with the same name already exists");
//			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//		}
//		String sql = "UPDATE hcladm_prod.tbl_designation SET name=?, code=?, status=?, datemodified=?, modifiedby=? WHERE designationid=?";
//		try {
//			int rowsAffected = jdbcTemplate.update(sql, name, code, status,
//					new java.sql.Timestamp(System.currentTimeMillis()), modifiedby, designationid);
//
//			if (rowsAffected > 0) {
//				response.put("status", "success");
//				response.put("message", "Designation updated successfully");
//				response.put("rowsAffected", rowsAffected);
//
//				String insertMasterLogSql = "INSERT INTO test.masterlog (id, type, createdby, modifiedby, modifiedDate) "
//						+ "VALUES (?, ?, ?, ?, current_timestamp)";
//
//				int rowsInMasterLog = jdbcTemplate.update(insertMasterLogSql, designationid, type, 0,
//						modifiedby);
//
//				if (rowsInMasterLog > 0) {
//					response.put("masterlogStatus", "success");
//					response.put("masterlogMessage", "Log entry added to masterlog successfully.");
//				} else {
//					response.put("masterlogStatus", "failure");
//					response.put("masterlogMessage", "Failed to insert log entry into masterlog.");
//				}
//
//				return new ResponseEntity<>(response, HttpStatus.OK);
//			} else {
//				response.put("status", "failure");
//				response.put("message", "No rows affected");
//				return new ResponseEntity<>(response, HttpStatus.OK);
//			}
//		} catch (Exception ex) {
//			response.put("status", "error");
//			response.put("message", "Error occurred while updating the designation");
//			response.put("error", ex.getMessage());
//			logger.debug("Exception update Designation", ex.getMessage());
//			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	public ResponseEntity<Map<String, Object>> updateDesignation(int designationid, String name, String code,
	        int status, int modifiedby, String type) {
	    Map<String, Object> response = new HashMap<>();

	    if (name == null || name.trim().isEmpty() || designationid == 0 || code == null || code.trim().isEmpty()
	            || status == 0) {
	        response.put("status", "error");
	        response.put("message", "Designation name, designationid, code, and status cannot be null or empty");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    String sqlCheckDuplicate = "SELECT COUNT(*) FROM hcladm_prod.tbl_designation WHERE name = ? AND designationid != ?";
	    Integer existingDesignationCount = jdbcTemplate.queryForObject(sqlCheckDuplicate, Integer.class, name,
	            designationid);

	    if (existingDesignationCount != null && existingDesignationCount > 0) {
	        response.put("status", "error");
	        response.put("message", "Designation with the same name already exists");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    String sqlGetCurrentName = "SELECT name FROM hcladm_prod.tbl_designation WHERE designationid = ?";
	    String existingName = jdbcTemplate.queryForObject(sqlGetCurrentName, String.class, designationid);
	    String sql = "UPDATE hcladm_prod.tbl_designation SET name=?, code=?, status=?, datemodified=?, modifiedby=? WHERE designationid=?";
	    try {
	        int rowsAffected = jdbcTemplate.update(sql, name, code, status, new java.sql.Timestamp(System.currentTimeMillis()),
	                modifiedby, designationid);

	        if (rowsAffected > 0) {
	            response.put("status", "success");
	            response.put("message", "Designation updated successfully");
	            response.put("rowsAffected", rowsAffected);
	            String insertMasterLogSql = "INSERT INTO test.masterlog (id, type, createdby, modifiedby, modifiedDate, existingname, name) "
	                    + "VALUES (?, ?, ?, ?, current_timestamp, ?, ?)";

	            int rowsInMasterLog = jdbcTemplate.update(insertMasterLogSql, designationid, type, 0, modifiedby, existingName, name);

	            if (rowsInMasterLog > 0) {
	                response.put("masterlogStatus", "success");
	                response.put("masterlogMessage", "Log entry added to masterlog successfully.");
	            } else {
	                response.put("masterlogStatus", "failure");
	                response.put("masterlogMessage", "Failed to insert log entry into masterlog.");
	            }

	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } else {
	            response.put("status", "failure");
	            response.put("message", "No rows affected");
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	    } catch (Exception ex) {
	        response.put("status", "error");
	        response.put("message", "Error occurred while updating the designation");
	        response.put("error", ex.getMessage());
	        logger.debug("Exception update Designation", ex.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	public Object assignDepartment(int businessunitid) {
		String sql = "SELECT businessunitid,departmentid,sectionid,levelid,status,createdby FROM hcladm_prod.tbl_businessunit_departments WHERE businessunitid=? and status=1001";
		return jdbcTemplate.queryForList(sql, businessunitid);
	}

	
	public Object assignDepartmentinsert(List<Department> departments) {
	    Map<String, Object> response = new HashMap<>();
	    if (departments == null || departments.isEmpty()) {
	        response.put("status", "error");
	        response.put("message", "Department list cannot be empty");
	        return response;
	    }
	    
	    try {
	        List<Department> deleteDepartments = new ArrayList<>();
	        List<Department> insertDepartments = new ArrayList<>();
	        
	        for (Department dept : departments) {
	           
	            if (dept.getStatus() == 1002) {
	                deleteDepartments.add(dept); 
	            } else if (dept.getStatus() == 1001) {
	                insertDepartments.add(dept); 
	            }
	        }
	        if (!deleteDepartments.isEmpty()) {
	            batchDeleteDepartments(deleteDepartments);
	        }
	        if (!insertDepartments.isEmpty()) {
	            batchInsertDepartments(insertDepartments);
	        }

	        response.put("status", "success");
	        response.put("message", "Departments processed successfully");
	        return response;
	    } catch (Exception e) {
	        response.put("status", "error");
	        response.put("message", "Database error: " + e.getMessage());
	        logger.debug("Exception process Departments", e.getMessage());
	        return response;
	    }
	}

	private void batchDeleteDepartments(List<Department> deleteDepartments) {
	    String sqlDelete = "DELETE FROM hcladm_prod.tbl_businessunit_departments "
	            + "WHERE businessunitid = ? AND departmentid = ?";
	    List<Object[]> batchDeleteArgs = new ArrayList<>();
	    for (Department dept : deleteDepartments) {
	        batchDeleteArgs.add(new Object[] { dept.getBusinessunitid(), dept.getDepartmentid() });
	    }
	    jdbcTemplate.batchUpdate(sqlDelete, batchDeleteArgs);
	}

	private void batchInsertDepartments(List<Department> insertDepartments) {
	    String sqlInsert = "INSERT INTO hcladm_prod.tbl_businessunit_departments (businessunitid, departmentid, status,sectionid) "
	            + "VALUES (?, ?, ?,?)";
	    List<Object[]> batchInsertArgs = new ArrayList<>();
	    for (Department dept : insertDepartments) {
	        batchInsertArgs.add(new Object[] { dept.getBusinessunitid(), dept.getDepartmentid(), dept.getStatus(),dept.getSectionid() });
	    }
	    jdbcTemplate.batchUpdate(sqlInsert, batchInsertArgs);
	}
	public Object assignDesignationinsert(List<Designation> designations) {
	    Map<String, Object> response = new HashMap<>();
	    
	    if (designations == null || designations.isEmpty()) {
	        response.put("status", "error");
	        response.put("message", "Designation list cannot be empty");
	        return response;
	    }

	    try {
	        List<Designation> deleteDesignations = new ArrayList<>();
	        List<Designation> insertDesignations = new ArrayList<>();
	        for (Designation design : designations) {
	            if (design.getBusinessunitid() <= 0 || design.getDesignationid() <= 0 || design.getStatus() <= 0) {
	                response.put("status", "error");
	                response.put("message", "parameters businessunitid, designationid, and status cannot be 0 or negative");
	                return response;
	            }
	            if (design.getStatus() == 1002) {
	                deleteDesignations.add(design); 
	            } else if (design.getStatus() == 1001) {
	                insertDesignations.add(design);  
	            }
	        }
	        if (!deleteDesignations.isEmpty()) {
	            batchDeleteDesignations(deleteDesignations);
	        }
	        if (!insertDesignations.isEmpty()) {
	            batchInsertDesignations(insertDesignations);
	        }

	        response.put("status", "success");
	        response.put("message", "Designations processed successfully");
	        return response;

	    } catch (Exception e) {
	        response.put("status", "error");
	        response.put("message", "Database error: " + e.getMessage());
	        logger.debug("Exception insert Designations", e.getMessage());
	        return response;
	    }
	}

	private void batchDeleteDesignations(List<Designation> deleteDesignations) {
	    String sqlDelete = "DELETE FROM hcladm_prod.tbl_businessunit_designations "
	            + "WHERE businessunitid = ? AND designationid = ?";
	    List<Object[]> batchDeleteArgs = new ArrayList<>();
	    for (Designation design : deleteDesignations) {
	        batchDeleteArgs.add(new Object[] { design.getBusinessunitid(), design.getDesignationid() });
	    }
	    jdbcTemplate.batchUpdate(sqlDelete, batchDeleteArgs);
	}

	private void batchInsertDesignations(List<Designation> insertDesignations) {
	    String sqlInsert = "INSERT INTO hcladm_prod.tbl_businessunit_designations (businessunitid, designationid, status, createdby) "
	            + "VALUES (?, ?, ?, ?)";
	    List<Object[]> batchInsertArgs = new ArrayList<>();
	    for (Designation design : insertDesignations) {
	        batchInsertArgs.add(new Object[] { design.getBusinessunitid(), design.getDesignationid(), design.getStatus(), design.getCreatedBy() });
	    }
	    jdbcTemplate.batchUpdate(sqlInsert, batchInsertArgs);
	}


	public Object assignDesignation(int businessunitid) {
		String sql = "SELECT businessunitid,designationid,status,createdby FROM hcladm_prod.tbl_businessunit_designations WHERE businessunitid=? and status=1001";
		return jdbcTemplate.queryForList(sql, businessunitid);
	}
	
	
	public ResponseEntity<Map<String, Object>> updateDepartment(String departmentId, String name, String code, int status, int modifiedBy, String type) {
	    Map<String, Object> response = new HashMap<>();
	    if (departmentId == null || name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty() || status == 0) {
	        response.put("status", "error");
	        response.put("message", "Parameters 'departmentId', 'name', 'code', and 'status' cannot be null or empty");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    String sqlCheckDepartmentExists = "SELECT COUNT(*) FROM hcladm_prod.tbl_department WHERE departmentid = ?";
	    Integer existingDepartmentCount = jdbcTemplate.queryForObject(sqlCheckDepartmentExists, Integer.class, departmentId);
	    if (existingDepartmentCount == null || existingDepartmentCount == 0) {
	        response.put("status", "error");
	        response.put("message", "Department not found with the given ID");
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }
	    String sqlGetExistingName = "SELECT name FROM hcladm_prod.tbl_department WHERE departmentid = ?";
	    String existingName = jdbcTemplate.queryForObject(sqlGetExistingName, String.class, departmentId);
	    String sqlCheckDuplicateName = "SELECT COUNT(*) FROM hcladm_prod.tbl_department WHERE name = ? AND departmentid != ?";
	    Integer existingNameCount = jdbcTemplate.queryForObject(sqlCheckDuplicateName, Integer.class, name, departmentId);
	    if (existingNameCount != null && existingNameCount > 0) {
	        response.put("status", "error");
	        response.put("message", "Department with the same name already exists");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    String[] departmentIds = departmentId.split(",");
	    StringBuilder inClause = new StringBuilder("IN (");
	    for (int i = 0; i < departmentIds.length; i++) {
	        inClause.append("?");
	        if (i < departmentIds.length - 1) {
	            inClause.append(",");
	        }
	    }
	    inClause.append(")");
	    String sqlUpdateDepartment = "UPDATE hcladm_prod.tbl_department SET name = ?, code = ?, status = ?, datemodified = ?, modifiedby = ? WHERE departmentid " + inClause.toString();

	    try {
	        Object[] args = new Object[departmentIds.length + 5];
	        args[0] = name;
	        args[1] = code;
	        args[2] = status;
	        args[3] = new java.sql.Timestamp(System.currentTimeMillis());
	        args[4] = modifiedBy;
	        System.arraycopy(departmentIds, 0, args, 5, departmentIds.length);

	        int rowsAffected = jdbcTemplate.update(sqlUpdateDepartment, args);

	        if (rowsAffected > 0) {
	            response.put("status", "success");
	            response.put("message", "Department updated successfully");
	            response.put("rowsAffected", rowsAffected);
	            //String insertDepartmentLogSql = "INSERT INTO test.masterlog (id, type, createdby, modifiedby, modifiedDate, existingName, name) VALUES (?, ?, ?, ?, current_timestamp, ?, ?)";
	            //jdbcTemplate.update(insertDepartmentLogSql, departmentId, type, modifiedBy, modifiedBy, existingName, name);

	            
	            String insertDepartmentLogSql = "INSERT INTO test.masterlog (id, type, createdby, modifiedby, modifiedDate, existingName, name) VALUES (?, ?, ?, ?, current_timestamp, ?, ?)";
	         String[] departmentIdsArray = departmentId.split(",");
	         List<Object[]> batchArgs = new ArrayList<>();
	         for (String deptId : departmentIdsArray) {
	             batchArgs.add(new Object[]{deptId, type, modifiedBy, modifiedBy, existingName, name});
	         }
 
	         try {
	             jdbcTemplate.batchUpdate(insertDepartmentLogSql, batchArgs);
	         } catch (Exception ex) {
	             logger.error("Error occurred while inserting logs for departments: " + ex.getMessage(), ex);
	           
	         }
	            
	            String sqlCheckParentDepartment = "SELECT parentdepartmentid FROM hcladm_prod.tbl_department WHERE departmentid = ?";
	            Integer parentDepartmentId = jdbcTemplate.queryForObject(sqlCheckParentDepartment, Integer.class, departmentId);
	            if (parentDepartmentId != null && parentDepartmentId > 0) {
	                String sqlCheckParentDepartmentDetails = "SELECT name, code, status FROM hcladm_prod.tbl_department WHERE departmentid = ?";
	                Map<String, Object> parentDeptDetails = jdbcTemplate.queryForMap(sqlCheckParentDepartmentDetails, parentDepartmentId);
	                if (!parentDeptDetails.get("name").equals(name) || !parentDeptDetails.get("code").equals(code) || !parentDeptDetails.get("status").equals(status)) {
	                    String sqlUpdateParentDepartment = "UPDATE hcladm_prod.tbl_department SET name = ?, code = ?, status = ?, datemodified = ?, modifiedby = ? WHERE departmentid in (?)";
	                    int rowsAffectedParent = jdbcTemplate.update(sqlUpdateParentDepartment, name, code, status, new java.sql.Timestamp(System.currentTimeMillis()), modifiedBy, parentDepartmentId);

	                    if (rowsAffectedParent > 0) {
	                        response.put("parentDepartmentStatus", "success");
	                        response.put("parentDepartmentMessage", "Parent department updated successfully.");
	                    } else {
	                        response.put("parentDepartmentStatus", "failure");
	                        response.put("parentDepartmentMessage", "Failed to update the parent department.");
	                      }
	                } else {
	                    response.put("parentDepartmentStatus", "skipped");
	                    response.put("parentDepartmentMessage", "Parent department does not require an update.");
	                }
	            }
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } else {
	            response.put("status", "failure");
	            response.put("message", "No rows affected during update, possibly no changes were made");
	            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    } catch (Exception ex) {
	        response.put("status", "error");
	        response.put("message", "Error occurred while updating the department");
	        response.put("error", ex.getMessage());
	        logger.error("Error occurred while updating the department: " + ex.getMessage(), ex);
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
 
	//////
	
	public List<Map<String, Object>> educationLevel() {
		String sql = "SELECT educationlevelid,NAME FROM hcladm_prod.tbl_education_levels t";
		return jdbcTemplate.queryForList(sql);
	}

	public List<Map<String, Object>> educationQualification(int educationlevelid) {
		String sql = "SELECT qualificationid, qualificationname, a.educationlevelid FROM hcladm_prod.tbl_educational_qualifications a LEFT JOIN hcladm_prod.tbl_education_levels b ON a.educationlevelid = b.educationlevelid WHERE a.educationlevelid = ?";
		return jdbcTemplate.queryForList(sql, educationlevelid);
	}

	public List<Map<String, Object>> qualificationBranch(String qualificationid) {
		String sql = "SELECT a.qualificationid,a.branchid,a.branchname  FROM hcladm_prod.tbl_qualification_branch a join hcladm_prod.tbl_educational_qualifications b on a.qualificationid=b.qualificationid where b.qualificationid=?";
		return jdbcTemplate.queryForList(sql, qualificationid);
	}
	
        @Transactional
		public ResponseEntity<Map<String, Object>> insertQualificationLevel(String qualificationname, int educationlevelid,
				String code, int createdby, String type) {
			Map<String, Object> response = new HashMap<>();
			String checkSql = "SELECT COUNT(*) FROM hcladm_prod.tbl_educational_qualifications WHERE qualificationname = ?";
			try {
				if (jdbcTemplate.queryForObject(checkSql, Integer.class, qualificationname) > 0) {
					response.put("status", "error");
					response.put("message", "Duplicate entry for qualificationname: " + qualificationname);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
				String insertSql = "INSERT INTO hcladm_prod.tbl_educational_qualifications(qualificationid, qualificationname, educationlevelid, code, qualificationdisplayname) VALUES (?, ?, ?, ?, ?)";
				int rowsAffected = jdbcTemplate.update(insertSql, qualificationname, qualificationname, educationlevelid,
						code, qualificationname);
				if (rowsAffected > 0) {
					String insertLogSql = "INSERT INTO test.qualificationmasterlog (qualificationid, name, existingname, createdby,type) VALUES (?, ?, NULL, ?,?)";
					int logRowsAffected = jdbcTemplate.update(insertLogSql, qualificationname, qualificationname, createdby,
							type);
					if (logRowsAffected > 0) {
						response.put("status", "success");
						response.put("message", "Insertion successful and logged.");
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						response.put("status", "error");
						response.put("message", "Qualification inserted, but logging failed.");
						return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} else {
					response.put("status", "failure");
					response.put("message", "No rows affected during qualification insertion.");
					return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception ex) {
				response.put("error", ex.getMessage());
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		public ResponseEntity<Map<String, Object>> insertBranch(String qualificationid, String branchname, int createdby,
				String type) {
			Map<String, Object> response = new HashMap<>();
			String checkSql = "SELECT COUNT(*) FROM hcladm_prod.tbl_qualification_branch WHERE branchname = ?";
			try {
				int count = jdbcTemplate.queryForObject(checkSql, Integer.class, branchname);
				if (count > 0) {
					response.put("status", "error");
					response.put("message", "Duplicate entry found for branchname.");
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
				String insertBranchSql = "INSERT INTO hcladm_prod.tbl_qualification_branch (qualificationid, branchid, branchname, branchdisplayname) "
						+ "VALUES (?, ?, ?, ?)";
				int rowsAffected = jdbcTemplate.update(insertBranchSql, qualificationid, branchname, branchname,
						branchname);
				if (rowsAffected > 0) {
					String insertLogSql = "INSERT INTO test.qualificationmasterlog (qualificationid, name, existingname, createdby,type) "
							+ "VALUES (?, ?, NULL, ?,?)";
					int logRowsAffected = jdbcTemplate.update(insertLogSql, qualificationid, branchname, createdby, type);
					if (logRowsAffected > 0) {
						response.put("status", "success");
						response.put("message", "Branch inserted and log recorded successfully.");
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						response.put("status", "error");
						response.put("message", "Branch inserted, but log recording failed.");
						return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} else {
					response.put("status", "failure");
					response.put("message", "Insertion failed, no rows affected.");
					return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (DataAccessException e) {
				response.put("status", "error");
				response.put("message", "Database error occurred.");
				response.put("error", e.getMessage());
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (Exception e) {
				response.put("status", "error");
				response.put("message", "An unexpected error occurred.");
				response.put("error", e.getMessage());
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		public Object getQualification() {
			String sql = "select * from hcladm_prod.tbl_educational_qualifications a";
			return jdbcTemplate.queryForList(sql);
		}

		public Object editqualificationlevel(String qualificationid, String qualificationname, int educationlevelid,
				String code, int createdby, String type) {
			if (educationlevelid == 0) {
				return ResponseEntity.badRequest().body("Education level ID is required.");
			}
			String checkDuplicateSql = "SELECT COUNT(*) FROM hcladm_prod.tbl_educational_qualifications WHERE qualificationname = ?";
			int duplicateCount = jdbcTemplate.queryForObject(checkDuplicateSql, Integer.class, qualificationname);
			if (duplicateCount > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate qualification name exists.");
			}
			String fetchExistingSql = "SELECT qualificationname FROM hcladm_prod.tbl_educational_qualifications WHERE qualificationid = ?";
			String existingQualificationName = null;
			try {
				existingQualificationName = jdbcTemplate.queryForObject(fetchExistingSql, String.class, qualificationid);
			} catch (EmptyResultDataAccessException e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Qualification not found.");
			}
			String insertLogSql = "INSERT INTO test.qualificationmasterlog (qualificationid, name, existingname, modifiedby,modifieddate,type) "
					+ "VALUES (?, ?, ?, ?,CURRENT_TIMESTAMP,?)";
			int logRowsAffected = jdbcTemplate.update(insertLogSql, qualificationid, qualificationname,
					existingQualificationName, createdby, type);
			if (logRowsAffected <= 0) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to log the update.");
			}
			String updateSql = "UPDATE hcladm_prod.tbl_educational_qualifications SET "
					+ "qualificationid=?,qualificationname = ?, educationlevelid = ?, code = ?, qualificationdisplayname = ? "
					+ "WHERE qualificationid = ?";
			int rowsUpdated = jdbcTemplate.update(updateSql, qualificationname, qualificationname, educationlevelid, code,
					qualificationname, qualificationid);
			if (rowsUpdated > 0) {
				return ResponseEntity.ok("Qualification updated successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Qualification not found.");
			}
		}

		public Object getBranch() {
			String sql = "SELECT qualificationid,branchid,branchname,branchdisplayname FROM hcladm_prod.tbl_qualification_branch";
			return jdbcTemplate.queryForList(sql);
		}

		public Object editbranch(String qualificationid, String branchid, String branchname, int createdby, String type) {
			try {
				if (qualificationid == null || qualificationid.isEmpty()) {
					return ResponseEntity.badRequest().body("Qualification ID is required.");
				}

				String checkDuplicateSql = "SELECT COUNT(*) FROM hcladm_prod.tbl_qualification_branch WHERE branchname = ?";
				int duplicateCount = jdbcTemplate.queryForObject(checkDuplicateSql, Integer.class, branchname);
				if (duplicateCount > 0) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate Branch Name exists.");
				}
				String fetchExistingBranchNameSql = "SELECT branchname FROM hcladm_prod.tbl_qualification_branch WHERE branchid = ?";
				List<String> existingBranchNames = jdbcTemplate.queryForList(fetchExistingBranchNameSql, String.class,
						branchid);

				if (existingBranchNames.isEmpty()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Branch ID not found.");
				} else if (existingBranchNames.size() > 1) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body("Multiple records found for the same branch ID.");
				} else {
					String existingBranchName = existingBranchNames.get(0);
					String insertLogSql = "INSERT INTO test.qualificationmasterlog (qualificationid, name, existingname, modifiedby, modifieddate, type) "
							+ "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
					int logRowsAffected = jdbcTemplate.update(insertLogSql, qualificationid, branchname, existingBranchName,
							createdby, type);

					if (logRowsAffected <= 0) {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to log the update.");
					}
					String updateSql = "UPDATE hcladm_prod.tbl_qualification_branch SET qualificationid=?,branchid = ? ,branchname = ?, branchdisplayname = ? WHERE branchid = ?";
					int rowsUpdated = jdbcTemplate.update(updateSql, qualificationid, branchname, branchname, branchname,
							branchid);
					if (rowsUpdated > 0) {
						return ResponseEntity.ok("Branch updated and log recorded successfully.");
					} else {
						return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Branch not found.");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("An error occurred while updating the branch.");
			}
		}

		public Object leavebalanceEligibleEmp(String employeeSequenceNo) {
			String sql = "SELECT P.employeesequenceno AS 'EmployeeSeq', p.employeeid as 'Employeeid', ST.NAME AS 'STATUS', P.callname AS 'Employeename', IFNULL(b.dateofjoin, '0000-00-00') AS 'DOJ', BU.NAME AS 'BUNAME', de.name AS 'Department', des.name AS 'Designation',CONCAT(\r\n"
					+ "        TIMESTAMPDIFF(YEAR, b.dateofjoin, CURDATE()), ' years ', \r\n"
					+ "        TIMESTAMPDIFF(MONTH, b.dateofjoin, CURDATE()) % 12, ' months'\r\n"
					+ "    ) AS 'Experience' FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY P LEFT JOIN HCLHRM_PROD.tbl_status_codes ST ON ST.STATUS = P.STATUS LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.businessunitid = p.companyid LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE b ON b.employeeid = p.employeeid LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS d ON p.EMPLOYEEID = d.EMPLOYEEID LEFT JOIN hcladm_prod.tbl_department de ON de.departmentid = d.departmentid LEFT JOIN hcladm_prod.tbl_designation des ON des.designationid = d.designationid LEFT JOIN hcladm_prod.tbl_costcenter cos ON cos.costcenterid = p.costcenterid WHERE p.status IN (1001) and cos.name='OFFICE' AND bu.callname IN (SELECT BUP.CALLNAME FROM hclhrm_prod.tbl_employee_primary p LEFT JOIN hcladm_prod.tbl_businessunit bup ON p.companyid = bup.businessunitid WHERE p.employeesequenceno = ?)  ORDER BY P.employeesequenceno";
			return jdbcTemplate.queryForList(sql, employeeSequenceNo);
		}

		public Object leavebalance(String employeeSequenceNo, String location) {
			String sql = "SELECT P.EMPLOYEESEQUENCENO,q.EMPLOYEEID,q.LEAVETYPEID,q.YEAR,LE.NAME,q.QUANTITY, q.AVAILABLEQTY, q.USEDQTY ,"
					+ "q.HOLD FROM hclhrm_prod_others.tbl_emp_leave_quota q left join hclhrm_prod.tbl_employee_primary p on p.employeeid=q.employeeid "
					+ "left join hclhrm_prod.tbl_leave_type le on le.leavetypeid=q.leavetypeid where q.status=1001 and p.employeesequenceno=? and year=year(curdate()) ";

//			if ("HYD".equals(location) || "ASSAM".equals(location)) {
//				sql += "AND q.LEAVETYPEID NOT IN (1, 2, 4) ";
	//
//			}
			return jdbcTemplate.queryForList(sql, employeeSequenceNo);
		}
		
		
		
		
		
		
//		public Object leaveunassignedleavebalance(String employeeSequenceNo,String location) {
//			String sql = "SELECT LEAVETYPEID,NAME,YEAR(CURDATE()) YEAR FROM hclhrm_prod.tbl_leave_type\r\n"
//					+ "where status='1001' and leavetypeid not in(SELECT q.LEAVETYPEID  FROM hclhrm_prod_others.tbl_emp_leave_quota q\r\n"
//					+ "left join hclhrm_prod.tbl_employee_primary p on p.employeeid=q.employeeid\r\n"
//					+ "left join hclhrm_prod.tbl_leave_type le on le.leavetypeid=q.leavetypeid\r\n"
//					+ "where q.status=1001 and p.employeesequenceno=? and year=YEAR(CURDATE()))";
//			
//			if ("HYD".equals(location) || "ASSAM".equals(location)) {
//				sql += "AND LEAVETYPEID NOT IN (1, 2, 4) ";
	//
//			}
//			return jdbcTemplate.queryForList(sql, employeeSequenceNo);
//		}
		
		 
	    
	 



		public Object leaveunassignedleavebalance(String employeesequenceno,String location) {
		    // First, call the procedure to get @result
		    
			
			// First, execute the stored procedure to get @result
			String procedureSql = "CALL procedure.GetSLEmployeeSequenceNo(?, @result)";
			jdbcTemplate.update(procedureSql, employeesequenceno);

			// Then, execute the SELECT query using the @result variable
			String sql = "SELECT LEAVETYPEID, NAME, YEAR(CURDATE()) AS YEAR, @result AS EmployeeSequenceNo "
			                 + "FROM hclhrm_prod.tbl_leave_type "
			                 + "WHERE status = '1001' "
			                 + "AND leavetypeid NOT IN ("
			                 + "    SELECT q.LEAVETYPEID "
			                 + "    FROM hclhrm_prod_others.tbl_emp_leave_quota q "
			                 + "    LEFT JOIN hclhrm_prod.tbl_employee_primary p ON p.employeeid = q.employeeid "
			                 + "    LEFT JOIN hclhrm_prod.tbl_leave_type le ON le.leavetypeid = q.leavetypeid "
			                 + "    WHERE q.status = 1001 "
			                 + "    AND p.employeesequenceno = ? "
			                 + "    AND q.year = YEAR(CURDATE())"
			                 + ") "
			                 + "AND leavetypeid != IF(@result = 1, 2, 0)  ";
		    
		    
		    if ("HYD".equals(location) || "ASSAM".equals(location)) {
				sql += "AND LEAVETYPEID NOT IN (1, 2, 4) ";

		}

//		    // Return the result as a list of LeaveType objects
//		    return jdbcTemplate.queryForList(
//		            sql, 
//		            new Object[] { result, employeesequenceno, result }
//		        );
		    
		    return jdbcTemplate.queryForList(sql,employeesequenceno);
		}

		
		public Object leaveaction(String leavetypeid, String employeeid, String createdby,
				String actiontype, String Total, String employeesequenceno) {

			//System.err.println(actiontype);
			String history = "INSERT INTO hclhrm_prod_others.tbl_emp_leave_quota_history "
					+ "SELECT EMPLOYEEID, LEAVETYPEID, YEAR, QUANTITY, AVAILABLEQTY, USEDQTY, HOLD, DAYMODE, MAXLEAVE, BACKDATE, COUNT_WOFF, "
					+ "COUNT_HOLIDAY, STATUS, LOGID, createdby, DATECREATED, MODIFIEDBY, DATEMODIFIED, VERIFIEDBY, DATEVERIFIED, now(), MINIMU_LEAVE, FOR_MONTH, LEAVE_MODE "
					+ "FROM hclhrm_prod_others.tbl_emp_leave_quota "
					+ "WHERE YEAR = YEAR(CURDATE()) AND LEAVETYPEID = ? AND EMPLOYEEID = ?";

			if (actiontype.equals("Assign")) {
				String countSql = "SELECT COUNT(*) FROM hclhrm_prod_others.tbl_emp_leave_quota WHERE leavetypeid = ? AND year = YEAR(CURDATE()) AND employeeid = ?";

				try {
					int count = jdbcTemplate.queryForObject(countSql, Integer.class, leavetypeid, employeeid);
					int row = jdbcTemplate.update(history, leavetypeid, employeeid);
					if (count == 1) {
						String updateSql = "UPDATE hclhrm_prod_others.tbl_emp_leave_quota " + "SET status = '1001' "
								+ "WHERE leavetypeid = ? AND employeeid = ? " + "AND year = YEAR(CURDATE())";

						//System.err.println(employeeid + "--" + actiontype + "--" + leavetypeid);

						if (row > 0) {
							return jdbcTemplate.update(updateSql, leavetypeid, employeeid);
						}
					} else {
						 
						String insertSql="";
						  if(leavetypeid.equals("6")) {
							   
					      insertSql = "INSERT INTO hclhrm_prod_others.tbl_emp_leave_quota "
									+ "SELECT  ?, ?, YEAR(CURDATE()),11, 11, 0.0, 0.0,0, 11, 3, 0, 0.0, 1001,0,?, NOW(),0, NOW(), "
									+ "0, NOW(), NOW(),0, 0, 'OP' FROM dual";
							 
							
						}else {
						
						  insertSql = "INSERT INTO hclhrm_prod_others.tbl_emp_leave_quota "
								+ "SELECT ?, ?, YEAR(NOW()), 0.0, 0.0, 0.0, 0.0, 1, 365, 3, 0, 0.0, 1001, "
								+ "0, ?, NOW(), 0, NOW(), 0, NOW(), NOW(), 0, 0, 'OP' FROM dual";
						
						}

						//System.err.println(employeeid + "--" + actiontype + "--" + leavetypeid +"-----"+employeesequenceno);
							return jdbcTemplate.update(insertSql, employeeid, leavetypeid,createdby);
					}
				} catch (DataAccessException e) {
					e.printStackTrace();
					return "Error executing SQL: " + e.getMessage();

				}
			}

			else if (actiontype.equals("Lock")) {

				String sql = "UPDATE hclhrm_prod_others.tbl_emp_leave_quota " + "SET status = 1002 "
						+ "WHERE leavetypeid = ? AND employeeid = ?  AND year = YEAR(CURDATE())";

				try {
					int row = jdbcTemplate.update(history, leavetypeid, employeeid);
					// System.err.println(employeeid+"--"+actiontype+"--"+leavetypeid+"createdby"+createdby);
					if (row > 0) {
						return jdbcTemplate.update(sql, leavetypeid, employeeid);
					}

				} catch (DataAccessException e) {
					e.printStackTrace();
					return "Error executing SQL: " + e.getMessage();
				}
			} else if (actiontype.equals("Update")) {

				String sql = "UPDATE hclhrm_prod_others.tbl_emp_leave_quota "
						+ "SET Quantity = ?, AvailableQty = ?,status =1001 "
						+ "WHERE employeeid = ? AND leavetypeid = ? AND year = YEAR(CURDATE())";

				//System.err.println(employeeid + "--" + actiontype + "--" + leavetypeid);
				try {
					//System.out.println("Update" + leavetypeid + "employeeid" + employeeid + "Total" + Total);

					int row = jdbcTemplate.update(history, leavetypeid, employeeid);

					if (row > 0) {
						return jdbcTemplate.update(sql, Total, Total, employeeid, leavetypeid);
					}

				} catch (DataAccessException e) {
					e.printStackTrace();
					return "Error executing SQL: " + e.getMessage();
				}
			} else if (actiontype.equals("Add")) {

				String insertSql = "";
				if (leavetypeid.equals("1") || leavetypeid.equals("2")) {
					insertSql = "INSERT INTO hclhrm_prod_others.tbl_emp_leave_quota "
							+ "SELECT P.EMPLOYEEID,?, YEAR(CURDATE()),?, ?, 0.0, 0.0, 0, 2, 3, 0, 0.0, 1001,0,?, NOW(),0, NOW(), "
							+ "0, NOW(), NOW(), 0,IF(p.employmenttypeid = 1, '0', '1'), 'OP' "
							+ "FROM hclhrm_prod.tbl_employee_primary p WHERE p.employeesequenceno = ?";
				} else if (leavetypeid.equals("4")) {
					insertSql = "INSERT INTO hclhrm_prod_others.tbl_emp_leave_quota "
							+ "SELECT P.EMPLOYEEID,?, YEAR(CURDATE()),?, ?, 0.0, 0.0, 0, 30, 3, 0, 0.0, 1001,0,?, NOW(),0, NOW(), "
							+ "0, NOW(), NOW(), 3, 0, 'OP' "
							+ "FROM hclhrm_prod.tbl_employee_primary p WHERE p.employeesequenceno = ?";
				}else if(leavetypeid.equals("3")) {
//					insertSql = "INSERT INTO hclhrm_prod_others.tbl_emp_leave_quota "
//						    + "SELECT P.EMPLOYEEID, ?, YEAR(CURDATE()),?, ?, 0, 0, 0, ?, 3, 0, 0.0, 1001, 0, ?, NOW(), 0, NOW(), NOW(), 0, 0, 'OP' "
//						    + "FROM hclhrm_prod.tbl_employee_primary p "
//						    + "WHERE p.employeesequenceno = ?";
					
					insertSql = "INSERT INTO hclhrm_prod_others.tbl_emp_leave_quota "
							+ "SELECT P.EMPLOYEEID,?, YEAR(CURDATE()),?, ?, 0.0, 0.0, 0, ?, 3, 0, 0.0, 1001,0,?, NOW(),0, NOW(), "
							+ "0, NOW(), NOW(), 0, 0, 'OP' "
							+ "FROM hclhrm_prod.tbl_employee_primary p WHERE p.employeesequenceno = ?";
				}
				
				
				//System.out.println(insertSql);
				//int row = jdbcTemplate.update(history, createdby, leavetypeid, employeeid);

				try {
					if(leavetypeid.equals("3")) {
						return jdbcTemplate.update(insertSql, leavetypeid, Total, Total,Total,createdby, employeesequenceno);
					}
					else {
						return jdbcTemplate.update(insertSql, leavetypeid, Total, Total,createdby, employeesequenceno);
					}
				} catch (DataAccessException e) {
					e.printStackTrace();
					return "Error executing SQL: " + e.getMessage();
				}
			}
			return "Invalid action type";
		}

		public List<Map<String, Object>> getDistinctPayPeriod() {
			String sql = "SELECT DISTINCT(year) FROM hclhrm_prod_others.tbl_emp_leave_quota ORDER BY year DESC LIMIT 5";
			return jdbcTemplate.queryForList(sql);
		}

		public Object leaveReport(String payperioddate) {
		    String sql = "SELECT "
		            + "    EMPLOYEESEQUENCENO 'EMPLOYEEID', "
		            + "    A.CALLNAME, "
		            + "    B.NAME 'BU_NAME', "
		            + "    IF(pro.dateofjoin = '0000-00-00', NULL, pro.dateofjoin) AS DOJ, "
		            + "    s.name AS Status, "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=1 THEN qu.QUANTITY END), '0') 'CL_QUANTITY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=1 THEN qu.USEDQTY END), '0') 'CL_USEQTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=1 THEN qu.AVAILABLEQTY END), '0') 'CL_AVAILABLEQTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=2 THEN qu.QUANTITY END), '0') 'SL_QUANTITY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=2 THEN qu.AVAILABLEQTY END), '0') 'SL_AVAILABLEQTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=2 THEN qu.USEDQTY END), '0') 'SL_USEDQTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=3 THEN qu.QUANTITY END), '0') 'MATERNITY_LEAVE_QUANTITY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=3 THEN qu.AVAILABLEQTY END), '0') 'MATERNITY_LEAVE_AVAILABLEQTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=3 THEN qu.USEDQTY END), '0') 'MATERNITY_LEAVE_USEDQTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=4 THEN qu.QUANTITY END), '0') 'EL_QUANTITY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=4 THEN qu.AVAILABLEQTY END), '0') 'EL_AVAILABLEQTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=4 THEN qu.USEDQTY END), '0') 'EL_USEDQTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=6 THEN qu.QUANTITY END), '0') 'MARRIAGE_LEAVE_QUANTITY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=6 THEN qu.AVAILABLEQTY END), '0') 'MARRIAGE_LEAVE_AVAILABLEQTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=6 THEN qu.USEDQTY END), '0') 'MARRIAGE_LEAVE_USEDQTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=7 THEN qu.USEDQTY END), '0') 'COFF', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=14 THEN qu.USEDQTY END), '0') 'ONDUTY', "
		            + "    IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=16 THEN qu.USEDQTY END), '0') 'WORKFROMHOME' "
		            + "FROM "
		            + "    hclhrm_prod.tbl_employee_primary a "
		            + "LEFT JOIN "
		            + "    hcladm_prod.tbl_status_codes s ON s.status = a.status "
		            + "LEFT JOIN "
		            + "    hcladm_prod.tbl_businessunit b ON b.businessunitid = a.companyid "
		            + "LEFT JOIN "
		            + "    hclhrm_prod_others.tbl_emp_leave_quota qu ON qu.employeeid = a.employeeid "
		            + "LEFT JOIN "
		            + "    hclhrm_prod.tbl_employee_profile pro ON pro.employeeid = a.employeeid "
		            + "WHERE "
		            + "    qu.year = ? "
		            + "GROUP BY "
		            + "    a.employeeid";
		    
		    return jdbcTemplate.queryForList(sql, payperioddate);
		}




	 

	
}

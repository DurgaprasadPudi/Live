package com.hetero.heteroiconnect.promotion;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.promotion.exception.EmployeeAlreadyFoundException;
import com.hetero.heteroiconnect.promotion.exception.EmployeeNotFoundException;

 
   
 

 
@Service
public class PromotionService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getEmployees() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT ").append("a.employeeid AS empid, ").append("a.employeesequenceno AS employeeid, ")
				.append("c.transactionid, ").append("a.callname, ").append("f.callname AS location, ")
				.append("d.name AS typeoftransfer, ").append("d.TRANSFERTYPEID AS transferTypeId, ")
				.append("e.TRANSFERID AS transferId, ").append("e.transfername, ").append("f.name AS buname, ")
				.append("f.BUSINESSUNITID AS businessUnitId, ").append("g.name AS deptname, ")
				.append("g.departmentid AS departmentId, ").append("i.name AS prev_des, ")
				.append("h.DESIGNATIONID AS designationId, ").append("h.name AS current_designation, ")
				.append("h.DESIGNATIONID AS promotionTo, ").append("c.transferreddate AS transferredDate, ")
				.append("c.reportingid, ").append("r.callname AS reportername, ").append("c.reportingdate ,C.flag ")
				.append("FROM ").append("hclhrm_prod.tbl_employee_primary a ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_professional_details b ON a.employeeid = b.employeeid ")
				.append("JOIN hclhrm_prod.tbl_employee_transfers_temp c ON c.employeeid = b.employeeid AND c.transferid = 2 ")
				.append("LEFT JOIN hclhrm_prod.tbl_transfer_types d ON d.transfertypeid = c.transfertypeid ")
				.append("LEFT JOIN hclhrm_prod.tbl_transfer_details e ON e.transferid = c.transferid ")
				.append("LEFT JOIN hcladm_prod.tbl_businessunit f ON f.businessunitid = c.businessunitid ")
				.append("LEFT JOIN hcladm_prod.tbl_department g ON g.departmentid = c.departmentid ")
				.append("LEFT JOIN hcladm_prod.tbl_designation h ON h.designationid = c.designationid ")
				.append("LEFT JOIN hcladm_prod.tbl_designation i ON i.designationid = b.designationid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary r ON r.employeesequenceno = c.reportingid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary rp ON rp.employeeid = c.reportingid ")
				.append("WHERE ").append("a.status = 1001 and c.status = 1001 order by c.lupdate desc ");

		return jdbcTemplate.queryForList(query.toString());
	}

	public Object getByEmpid(int employeeseq) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT ").append("a.employeesequenceno AS empid, ").append("a.employeeid, ")
				.append("b.designationid, ").append("e.name as designationname, ").append("b.departmentid, ").append("a.companyid AS buid, ")
				.append("f.name AS buname, ").append("g.employmenttypeid ")
				.append("FROM hclhrm_prod.tbl_employee_primary a ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_professional_details b ON a.employeeid = b.employeeid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_profile_businessunit c ON c.employeeid = b.employeeid ")
				.append("LEFT JOIN hcladm_prod.tbl_department d ON d.departmentid = b.departmentid ")
				.append("LEFT JOIN hcladm_prod.tbl_designation e ON e.designationid = b.designationid ")
				.append("LEFT JOIN hcladm_prod.tbl_businessunit f ON f.businessunitid = a.companyid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employment_types g ON g.employmenttypeid = a.employmenttypeid ")
				.append("WHERE a.employeesequenceno = ? AND a.status = 1001 ").append("GROUP BY a.employeesequenceno");
		
		System.err.println("employeeseq-----"+query.toString());

		
			List<Map<String, Object>> result = jdbcTemplate.queryForList(query.toString(), employeeseq);
			
			//System.err.println("employeeseq-----"+employeeseq);
			if (result.isEmpty()) {
				
				System.err.println("employeeseq-----"+employeeseq);
				throw new EmployeeNotFoundException("Employee not found for employee sequence number: " + employeeseq);
			}
			return result;
		

	}

	public Object getTransferTypes() {
		String sql = "select TRANSFERTYPEID,NAME from hclhrm_prod.tbl_transfer_types";
		return jdbcTemplate.queryForList(sql);
	}

	public Object getTransferDetails() {
		String sql = "select TRANSFERID,TRANSFERNAME from hclhrm_prod.tbl_transfer_details where status=1001 and TRANSFERID =2 ";
		return jdbcTemplate.queryForList(sql);
	}

	public Object getBusinessUnit() {
		String sql = "select BUSINESSUNITID,NAME from  hcladm_prod.tbl_businessunit";
		return jdbcTemplate.queryForList(sql);
	}

	public Object getDepts() {
//		String sql = "SELECT concat(parentdepartmentid,'#',departmentid) departmentid, NAME FROM hcladm_prod.tbl_department\r\n"
//				+ "WHERE parentdepartmentid != 0 AND status = 1001 ORDER BY name ASC";
		String sql = "SELECT  PARENTDEPARTMENTID as departmentid, departmentid as sectionid,NAME FROM hcladm_prod.tbl_department\r\n"
				+ "WHERE parentdepartmentid != 0 AND status = 1001 ORDER BY PARENTDEPARTMENTID ASC";
		return jdbcTemplate.queryForList(sql);
	}

	public Object getDesigs() {
		String sql = "SELECT DESIGNATIONID,NAME FROM hcladm_prod.tbl_designation t ORDER BY designationid ASC";
		return jdbcTemplate.queryForList(sql);
	}

	public ResponseEntity<?> registation(PromotionRegistation promotionRegistation) {
		System.out.println("Designation ID: " + promotionRegistation.getDesignationid());
		System.out.println("Employee ID: " + promotionRegistation.getEmployeeid());
		String checkQuery = "SELECT COUNT(*) FROM hclhrm_prod.tbl_employee_transfers_temp WHERE designationid = ? AND employeeid = ? and status=1001";
		try {
			int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, promotionRegistation.getDesignationid(),
					promotionRegistation.getEmployeeid());

			if (count > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Record already exists with the same Designation ID and Employee ID.");
			}
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO hclhrm_prod.tbl_employee_transfers_temp ")
					.append("(EMPLOYEEID, TRANSFERTYPEID, TRANSFERID, BUSINESSUNITID, DEPARTMENTID, SECTIONID, ")
					.append("DESIGNATIONID, TRANSFERREDDATE, REPORTINGID, REPORTINGDATE, ")
					.append("CREATEDBY, DATECREATED) ").append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())");

			int rowsAffected = jdbcTemplate.update(query.toString(),
					promotionRegistation.getEmployeeid() == 0 ? null : promotionRegistation.getEmployeeid(),
					promotionRegistation.getTransfertypeid() == 0 ? null : promotionRegistation.getTransfertypeid(),
					promotionRegistation.getTransferid() == 0 ? null : promotionRegistation.getTransferid(),
					promotionRegistation.getBusinessunitid() == 0 ? null : promotionRegistation.getBusinessunitid(),
					promotionRegistation.getDepartmentid() == 0 ? null : promotionRegistation.getDepartmentid(),
					promotionRegistation.getSectionid() == 0 ? null : promotionRegistation.getSectionid(),
					promotionRegistation.getDesignationid() == 0 ? null : promotionRegistation.getDesignationid(),
					promotionRegistation.getTransferreddate(),
					promotionRegistation.getReportingid() == 0 ? null : promotionRegistation.getReportingid(),
					promotionRegistation.getReportingdate(),
					promotionRegistation.getCreatedby() == 0 ? null : promotionRegistation.getCreatedby());

			if (rowsAffected > 0) {
				return ResponseEntity.status(HttpStatus.OK).body("Insertion successful!");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insertion failed. No rows affected.");
			}
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal Server Error: " + e.getMessage());
		}

	}

	public Object update(int transactionid, PromotionRegistation promotionRegistation) {
		StringBuffer query = new StringBuffer();
		query.append("UPDATE hclhrm_prod.tbl_employee_transfers_temp SET ").append("TRANSFERTYPEID = ?, ")
				.append("TRANSFERID = ?, ").append("BUSINESSUNITID = ?, ").append("DEPARTMENTID = ?, ")
				.append("SECTIONID = ?, ").append("DESIGNATIONID = ?, ").append("TRANSFERREDDATE = ?, ")
				.append("REPORTINGID = ?, ").append("REPORTINGDATE = ?, ").append("datemodified = NOW(),FLAG='P'  ")
				.append("WHERE transactionid = ?");

		try {
			int rowsAffected = jdbcTemplate.update(query.toString(),
					promotionRegistation.getTransfertypeid() == 0 ? null : promotionRegistation.getTransfertypeid(),
					promotionRegistation.getTransferid() == 0 ? null : promotionRegistation.getTransferid(),
					promotionRegistation.getBusinessunitid() == 0 ? null : promotionRegistation.getBusinessunitid(),
					promotionRegistation.getDepartmentid() == 0 ? null : promotionRegistation.getDepartmentid(),
					promotionRegistation.getSectionid() == 0 ? null : promotionRegistation.getSectionid(),
					promotionRegistation.getDesignationid() == 0 ? null : promotionRegistation.getDesignationid(),
					promotionRegistation.getTransferreddate(),
					promotionRegistation.getReportingid() == 0 ? null : promotionRegistation.getReportingid(),
					promotionRegistation.getReportingdate(), transactionid);

			if (rowsAffected > 0) {
				return ResponseEntity.status(HttpStatus.OK).body("Updation successful!");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Updation failed. No rows affected.");
			}
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal Server Error: " + e.getMessage());
		}
	}

	public Object delete(int transactionid) {
		StringBuffer query = new StringBuffer();
		query.append("UPDATE hclhrm_prod.tbl_employee_transfers_temp SET STATUS = 1002 WHERE transactionid = ?");
		int rowsAffected = jdbcTemplate.update(query.toString(), transactionid);
		if (rowsAffected > 0) {
			return "Update successful, " + rowsAffected + " row(s) updated.";
		} else {
			return "No record found with the given transactionid.";
		}
	}

	public List<Map<String, Object>> getReporties(String search) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT DISTINCT b.employeesequenceno as id, b.callname as name ")
				.append("FROM hclhrm_prod.tbl_employee_professional_details a ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.managerid = b.employeeid ")
				.append("WHERE (b.callname LIKE '%").append(search).append("%' ")
				.append("OR b.employeesequenceno LIKE '%").append(search).append("%') ").append("AND b.status = 1001 ")
				.append("LIMIT 10");

		return jdbcTemplate.queryForList(query.toString());
	}

	public Object getStatus() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT EMPLOYMENTTYPEID,NAME FROM hclhrm_prod.tbl_employment_types  where status=1001");
		return jdbcTemplate.queryForList(query.toString());
	}

//	public Object insertregistation(ConfirmationRegistation confirmationRegistation) {
//		try {
//			String insertOrUpdateQuery = "INSERT INTO test.tbl_employee_confirmation_details "
//					+ "(EMPLOYEEID, DESIGNATIONID, DEPARTMENTID, SECTIONID, EMPLOYMENTTYPEID, ONDATE, COMMENTS, STATUS) "
//					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " + "ON DUPLICATE KEY UPDATE "
//					+ "DESIGNATIONID = VALUES(DESIGNATIONID), " + "DEPARTMENTID = VALUES(DEPARTMENTID), "
//					+ "SECTIONID = VALUES(SECTIONID), " + "EMPLOYMENTTYPEID = VALUES(EMPLOYMENTTYPEID), "
//					+ "ONDATE = VALUES(ONDATE), " + "COMMENTS = VALUES(COMMENTS), " + "STATUS = VALUES(STATUS)";
//
//			int rowsAffected = jdbcTemplate.update(insertOrUpdateQuery, confirmationRegistation.getEmployeeid(),
//					confirmationRegistation.getDesignationid(), confirmationRegistation.getDepartmentid(),
//					confirmationRegistation.getSectionid(), confirmationRegistation.getEmploymenttypeid(),
//					confirmationRegistation.getConfirmationdate(), confirmationRegistation.getComments(), 1001);
//
//			if (rowsAffected > 0) {
//				return ResponseEntity.status(HttpStatus.OK).body("Insertion successful!");
//				
//				
//			} else {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No rows affected.");
//			}
//		} catch (DataAccessException e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Internal Server Error: " + e.getMessage());
//		}
//	}
	
	
	
	@Transactional
	public Object insertregistation(ConfirmationRegistation confirmationRegistation) {
	    try {
	      
	        String checkQuery = "SELECT count(*) FROM test.tbl_employee_confirmation_details WHERE employeeid=? AND status=1001";
	        int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, confirmationRegistation.getEmployeeid());
 
	        if (count > 0) {
	        	throw new EmployeeAlreadyFoundException("Employee already confirmed. Duplicate entry detected. Please use the edit option below on the displayed screen to search for the employee and update the record");
	        }
	        String insertOrUpdateQuery = "INSERT INTO test.tbl_employee_confirmation_details "
	                + "(EMPLOYEEID, DESIGNATIONID, DEPARTMENTID, SECTIONID, EMPLOYMENTTYPEID, ONDATE, COMMENTS, STATUS) "
	                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) "
	                + "ON DUPLICATE KEY UPDATE "
	                + "DESIGNATIONID = VALUES(DESIGNATIONID), "
	                + "DEPARTMENTID = VALUES(DEPARTMENTID), "
	                + "SECTIONID = VALUES(SECTIONID), "
	                + "EMPLOYMENTTYPEID = VALUES(EMPLOYMENTTYPEID), "
	                + "ONDATE = VALUES(ONDATE), "
	                + "COMMENTS = VALUES(COMMENTS), "
	                + "STATUS = VALUES(STATUS)";
 
	        int rowsAffected = jdbcTemplate.update(insertOrUpdateQuery,
	                confirmationRegistation.getEmployeeid(),
	                confirmationRegistation.getDesignationid(),
	                confirmationRegistation.getDepartmentid(),
	                confirmationRegistation.getSectionid(),
	                confirmationRegistation.getEmploymenttypeid(),
	                confirmationRegistation.getConfirmationdate(),
	                confirmationRegistation.getComments(),
	                1001);
 
	        if (rowsAffected > 0) {
	            boolean employeePrimaryUpdated = updateEmployeePrimary(confirmationRegistation.getEmployeeid());
 
	            if (employeePrimaryUpdated) {
	                boolean leaveQuotaUpdated = updateLeaveQuota(confirmationRegistation.getEmployeeid());
 
	                if (leaveQuotaUpdated) {
	                    return ResponseEntity.status(HttpStatus.OK).body("Insertion Sucessfull");
	                } else {
	                  
	                    throw new RuntimeException("Leave quota update failed.");
	                }
	            } else {
	              
	                throw new RuntimeException("Employee is not probition");
	            }
	        } else {
	           
	            throw new RuntimeException("No rows affected during insertion.");
	        }
	    } catch (DataAccessException e) {
	        System.err.println("Database error: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Internal Server Error: " + e.getMessage());
	    }
	}
 
	private boolean updateEmployeePrimary(int employeeid) {
	    String updateQuery = "UPDATE hclhrm_prod.tbl_employee_primary "
	            + "SET employmenttypeid = 1 "
	            + "WHERE employeeid = ? AND employmenttypeid = 2 AND status = 1001";
	    
	    int updateRows = jdbcTemplate.update(updateQuery, employeeid);
	    return updateRows > 0;
	}
 
	private boolean updateLeaveQuota(int employeeid) {
	    String leaveQuotaUpdateQuery = "UPDATE hclhrm_prod_others.tbl_emp_leave_quota "
	            + "SET maxleave = 3, for_month = 0 "
	            + "WHERE employeeid IN (SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary "
	            + "WHERE employeeid = ?) "
	            + "AND status = 1001 AND leavetypeid IN (1, 2)";
	    int leaveQuotaRowsAffected = jdbcTemplate.update(leaveQuotaUpdateQuery, employeeid);
	    return leaveQuotaRowsAffected > 0;
	}
	

	public Object fetchConfirmationData() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT a.employeesequenceno AS empid, a.employeeid AS employeeid, ").append(
				"b.DESIGNATIONID, b.DEPARTMENTID,c.name as DEPTNAME,d.name as DESIGNNAME,f.name as STATUSNAME,e.name as BUNAME, b.EMPLOYMENTTYPEID, b.ONDATE, b.COMMENTS ")
				.append("FROM hclhrm_prod.tbl_employee_primary a ")
				.append("LEFT JOIN test.tbl_employee_confirmation_details b ON a.employeeid = b.employeeid ")
				.append("LEFT JOIN hcladm_prod.tbl_department c ON c.departmentid = b.departmentid ")
				.append("LEFT JOIN hcladm_prod.tbl_designation d ON d.designationid = b.designationid ")
				.append("LEFT JOIN hcladm_prod.tbl_businessunit e ON e.businessunitid = a.companyid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employment_types f ON f.employmenttypeid = b.employmenttypeid ")
				.append("WHERE b.status = 1001 and a.status in(1001,1092,1401) and e.callname='HYD' and date(b.lupdate)>='2025-03-01'");
		return jdbcTemplate.queryForList(query.toString());
	}

	public Object updateconfirmation(int employeeid, ConfirmationRegistation confirmationRegistation) {
		StringBuffer query = new StringBuffer();
		query.append("UPDATE test.tbl_employee_confirmation_details ").append("SET DESIGNATIONID = ?, ")
				.append("DEPARTMENTID = ?, ").append("SECTIONID = ?, ").append("EMPLOYMENTTYPEID = ?, ")
				.append("ONDATE = ?, ").append("COMMENTS = ?, ").append("STATUS = ? ").append("WHERE EMPLOYEEID = ?");

		try {
			int rowsAffected = jdbcTemplate.update(query.toString(), confirmationRegistation.getDesignationid(),
					confirmationRegistation.getDepartmentid(), confirmationRegistation.getSectionid(),
					confirmationRegistation.getEmploymenttypeid(), confirmationRegistation.getConfirmationdate(),
					confirmationRegistation.getComments(), 1001, employeeid);

			if (rowsAffected > 0) {
				return ResponseEntity.status(HttpStatus.OK).body("Update successful!");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed. No rows affected.");
			}
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal Server Error: " + e.getMessage());
		}
	}

	public Object deleteEmployee(int employeeid, int employmenttypeid) {
		StringBuffer query = new StringBuffer();
		query.append(
				"UPDATE test.tbl_employee_confirmation_details  SET STATUS = 1002 WHERE employmenttypeid=? and employeeid = ?");
		int rowsAffected = jdbcTemplate.update(query.toString(), employmenttypeid, employeeid);
		if (rowsAffected > 0) {
			return "Update successful, " + rowsAffected + " row(s) updated.";
		} else {
			return "No record found with the given transactionid.";
		}
	}
	
	public String checkconfirmation(String employeeId) {
//        String query = "SELECT IF(CURDATE() >= i.RELEASE_DATE, 'TRUE', 'FALSE') AS VIEW " +
//                       "FROM test.tbl_increment_letter i " +
//                       "WHERE i.employeeid = ? AND i.FLAG = 'S'";
		
		
		String query = "SELECT IF(C.FILE_PATH = '0', 'FALSE', 'TRUE') AS VIEW " +
	               "FROM TEST.TBL_EMPLOYEE_CONFIRMATION_DETAILS C " +
	               "LEFT JOIN hclhrm_prod.tbl_employee_primary P ON P.EMPLOYEEID = C.EMPLOYEEID " +
	               "LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.businessunitid = p.companyid " +
	               "WHERE p.EMPLOYEESEQUENCENO = ? AND C.FLAG = 'S'";

        
        try {
            // Execute query using jdbcTemplate with prepared statement
            String view = jdbcTemplate.queryForObject(query, new Object[]{employeeId}, String.class);

            return view; // Return the VIEW value obtained from the query

        } catch (EmptyResultDataAccessException e) {
            // Handle case where no rows are returned by the query
            System.out.println("No record found for employeeId: " + employeeId);
            return "FALSE"; // Return "FALSE" if no matching record found
        } catch (Exception err) {
            // Catch any other exceptions
            System.out.println("Exception in checkincrementrespository method: " + err.getMessage());
            err.printStackTrace(); // Print stack trace for detailed error information
            return "FALSE"; // Return "FALSE" in case of any exception
        }
    }

}

package com.hetero.heteroiconnect.iconnectrights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RightsAssignServiceImpl implements RightsAssignService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

 
	@Override
	public List<Map<String, Object>> getRights() {
		String sql = "SELECT PRIVILEGEID, DISPLAYNAME as NAME FROM hclhrm_prod_others.iconnect_rights WHERE STATUS = 1001 and PRIVILEGEID not in (1,2,3,4,5,6,7,8,9,11,13,23,10,22,26,35,21)";

		List<Map<String, Object>> rightsList = jdbcTemplate.queryForList(sql);
		return rightsList;
	}

	public Object addRightAssign(List<RightsAssignDTO> rightsList) {
	    Map<String, Object> response = new HashMap<>();
	    if (rightsList == null || rightsList.isEmpty()) {
	        response.put("status", "error");
	        response.put("message", "Rights list cannot be empty");
	        return response;
	    }

	    try {
	        List<RightsAssignDTO> updateRights = new ArrayList<>();
	        List<RightsAssignDTO> insertRights = new ArrayList<>();

	        for (RightsAssignDTO right : rightsList) {
	        	  if (right.getEmployeeid() == 0) {
	                  throw new BadRequestException("Invalid employeeId: 0 is not allowed");
	              }
	            if (right.getStatus() == 1002) {  
	                updateRights.add(right);
	            } else if (right.getStatus() == 1001) { 
	                insertRights.add(right);
	            }
	        }

	        if (!updateRights.isEmpty()) {
	            batchUpdateRightsStatus(updateRights);
	        }
	        if (!insertRights.isEmpty()) {
	            batchInsertRights(insertRights);
	        }

	        response.put("status", "success");
	        response.put("message", "Rights processed successfully");
	        return response;
	    } catch (Exception e) {
	        response.put("status", "error");
	        response.put("message", "Database error: " + e.getMessage());
	        return response;
	    }
	}
	private void batchUpdateRightsStatus(List<RightsAssignDTO> updateRights) {
	    String sqlUpdate = "UPDATE hclhrm_prod_others.tbl_employee_iconnect_privileges " +
	                       "SET status = 1002, updatedby = ?, updatedtime = NOW() " +
	                       "WHERE employeeid = ? AND privilegeid = ? AND status = 1001";

	    List<Object[]> batchUpdateArgs = new ArrayList<>();
	    for (RightsAssignDTO right : updateRights) {
	    	 if (right.getEmployeeid() == 0) {
                 throw new BadRequestException("Invalid employeeId: 0 is not allowed");
             }
	        batchUpdateArgs.add(new Object[]{
	            right.getUpdatedby(),    
	            right.getEmployeeid(),
	            right.getPrivilegeId()
	        });
	    }

	    jdbcTemplate.batchUpdate(sqlUpdate, batchUpdateArgs);
	}
	private void batchInsertRights(List<RightsAssignDTO> insertRights) {
	    String sqlInsert = "INSERT INTO hclhrm_prod_others.tbl_employee_iconnect_privileges " +
	                       "(employeeid, privilegeid, createdby, status) " +
	                       "VALUES (?, ?, ?, ?)";

	    List<Object[]> batchInsertArgs = new ArrayList<>();
	    for (RightsAssignDTO right : insertRights) {
	    	 if (right.getEmployeeid() == 0) {
                 throw new BadRequestException("Invalid employeeId: 0 is not allowed");
             }
	        batchInsertArgs.add(new Object[]{
	            right.getEmployeeid(),
	            right.getPrivilegeId(),
	            right.getCreatedby(),   
	            right.getStatus()
	        });
	    }

	    jdbcTemplate.batchUpdate(sqlInsert, batchInsertArgs);
	}


	@Override
	public List<RightsAssignDTO> getAssignedDataEmployeeid(int employeeid) {
		String sql = "SELECT privilegeid, employeeid,status,createdby,updatedby " + "FROM hclhrm_prod_others.tbl_employee_iconnect_privileges "
				+ "WHERE employeeid = ? AND status = 1001";

		return jdbcTemplate.query(sql, new Object[] { employeeid }, (rs, rowNum) -> {
			RightsAssignDTO dto = new RightsAssignDTO();
			dto.setPrivilegeId(rs.getInt("privilegeid"));
			dto.setEmployeeid(rs.getInt("employeeid"));
			dto.setStatus(rs.getInt("status"));
			dto.setCreatedby(rs.getInt("createdby"));
			dto.setUpdatedby(rs.getInt("updatedby"));
			return dto;
		});
	}
	
}

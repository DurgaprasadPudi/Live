package com.hetero.heteroiconnect.familydetailsreports;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;

@Repository
public class familydetailsreportRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	 
	@SuppressWarnings("rawtypes")
	public JSONArray GetFamilyDetails() {
	    JSONArray empDetails = new JSONArray();
	    try {
	        String EMPINFO = "SELECT p.employeesequenceno AS 'EMPLOYEE ID', "
	                       + "p.callname AS 'EMPLOYEE NAME', "
	                       + "UPPER(F.name) AS 'RELATION NAME', "
	                       + "UPPER(F.relation) AS 'RELATION TYPE', "
	                       + "UPPER(F.gender) AS 'GENDER', "
	                       + "DATE_FORMAT(F.dob, '%Y-%m-%d') AS 'DATE OF BIRTH', "
	                       + "F.blood_group AS 'BLOOD GROUP', "
	                       + "F.aadhar AS 'RELATION AADHAR NUMBER', "
	                       + "UPPER(F.occupation) AS 'RELATION OCCUPATION' "
	                       + "FROM test.tbl_sindhuri_employee_data D "
	                       + "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON D.empcode = p.employeesequenceno "
	                       + "LEFT JOIN test.tbl_sindhuri_employee_family F ON F.EMPCODE = D.EMPCODE "
	                       + "LEFT JOIN hclhrm_prod.tbl_status_codes s ON s.status = p.status "
	                       + "LEFT JOIN hcladm_prod.tbl_costcenter cen ON p.costcenterid = cen.costcenterid "
	                       + "LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.BUSINESSUNITID = p.COMPANYID "
	                       + "LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON p.EMPLOYEEID = DD.EMPLOYEEID "
	                       + "LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID = DEP.DEPARTMENTID;";
	        
	        List<Map<String, Object>> rows = jdbcTemplate.queryForList(EMPINFO);
	        
	        for (@SuppressWarnings("rawtypes") Map row : rows) {
	            net.sf.json.JSONObject employeeDetail = new net.sf.json.JSONObject();
	            for (Object key : row.keySet()) {
	                employeeDetail.put(key, row.get(key));
	            }
	            empDetails.add(employeeDetail);
	        }
	    } catch (Exception err) {
	        System.out.println("Exception at GetFamilyDetails: " + err);
	    }
	    return empDetails;
	}

	
	 
	
	@SuppressWarnings("rawtypes")
	public JSONArray GetEmployeeDetails() {
	    JSONArray empDetails = new JSONArray();
	    try {
	        String EMPINFO = "SELECT p.employeesequenceno AS 'EMPLOYEE ID', "
	                       + "p.callname AS 'EMPLOYEE NAME', "
	                       + "s.name AS 'STATUS', "
	                       + "cen.name AS 'COST CENTER', "
	                       + "bu.name AS 'DIVISION', "
	                       + "IFNULL(DEP.NAME, '') AS 'DEPARTMENT', "
	                       + "D.PAN AS 'PAN', "
	                       + "D.AADHAR AS 'AADHAR', "
	                       + "D.MOBILE AS 'MOBILE', "
	                       + "D.WHATSAPPNUMBER AS 'WHATSAPP NUMBER', "
	                       + "D.MARITALStatus AS 'MARITAL STATUS' "
	                       + "FROM test.tbl_sindhuri_employee_data D "
	                       + "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON D.empcode = p.employeesequenceno "
	                       + "LEFT JOIN hclhrm_prod.tbl_status_codes s ON s.status = p.status "
	                       + "LEFT JOIN hcladm_prod.tbl_costcenter cen ON p.costcenterid = cen.costcenterid "
	                       + "LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.BUSINESSUNITID = p.COMPANYID "
	                       + "LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON p.EMPLOYEEID = DD.EMPLOYEEID "
	                       + "LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID = DEP.DEPARTMENTID;";
	        
	        List<Map<String, Object>> rows = jdbcTemplate.queryForList(EMPINFO);
	        
	        for (@SuppressWarnings("rawtypes") Map row : rows) {
	            net.sf.json.JSONObject employeeDetail = new net.sf.json.JSONObject();
	            for (Object key : row.keySet()) {
	                employeeDetail.put(key, row.get(key));
	            }
	            empDetails.add(employeeDetail);
	        }
	    } catch (Exception err) {
	        System.out.println("Exception at GetFamilyDetails: " + err);
	    }
	    return empDetails;
	}

		
		 
}

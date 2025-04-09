package com.hetero.heteroiconnect.payslips;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;

@Repository
public class PayslipRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	 
	 
	@SuppressWarnings("rawtypes")
	public JSONArray employeepayperiod()
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		try {
			//String EMPINFO="SELECT PAYPERIOD FROM hclhrm_prod.tbl_businessunit_payroll_process WHERE STATUS = 'C' AND PAYPERIOD BETWEEN (CASE WHEN MONTH(CURDATE()) >= 4 THEN CONCAT(YEAR(CURDATE()), '04') ELSE CONCAT(YEAR(CURDATE()) - 1, '04') END) AND DATE_FORMAT(CURDATE(), '%Y%m') GROUP BY PAYPERIOD ORDER BY PAYPERIOD DESC " ;
			
			String EMPINFO="SELECT PAYPERIOD FROM hclhrm_prod.tbl_businessunit_payroll_process WHERE STATUS = 'C' GROUP BY PAYPERIOD ORDER BY PAYPERIOD DESC LIMIT 3" ;
			
			  
			List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  
				  ADD=new net.sf.json.JSONObject();
				  ADD.put("PAYPERIOD",row.get("PAYPERIOD"));
				 // ADD.put("BUNAME",row.get("BUNAME"));
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	 
		
		 
}

package com.hetero.heteroiconnect.assessment_confirmation;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
public class Assessment_confirmationRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;


	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	public synchronized JSONArray designations() throws SQLException {

		JSONArray design_arr = new JSONArray();
		String query=" SELECT DESIGNATIONID,CODE FROM HCLADM_PROD.TBL_DESIGNATION ORDER BY CODE ASC; ";
		System.out.println("designations ::::"+query );
		List<Map<String, Object>> list= jdbcTemplate.queryForList(query) ;

		for (@SuppressWarnings("rawtypes") Map map : list) {
			JSONObject jsonObj1 = new JSONObject();
			jsonObj1.put("Designid", map.get("DESIGNATIONID"));
			jsonObj1.put("Name", map.get("CODE"));
			design_arr.add(jsonObj1);
		}

		return design_arr;

	}
	
	
	public synchronized JSONArray departments() throws SQLException {

		JSONArray department_arr = new JSONArray();
		String query=" SELECT DEPARTMENTID,NAME FROM HCLADM_PROD.TBL_DEPARTMENT WHERE PARENTDEPARTMENTID=0 AND DEPARTMENTID!=0 ORDER BY CODE ASC; ";
		System.out.println("designations ::::"+query );
		List<Map<String, Object>> list= jdbcTemplate.queryForList(query) ;

		for (@SuppressWarnings("rawtypes") Map map : list) {
			JSONObject jsonObj1 = new JSONObject();
			jsonObj1.put("Departmentid", map.get("DEPARTMENTID"));
			jsonObj1.put("Name", map.get("NAME"));
			department_arr.add(jsonObj1);
		}

		return department_arr;

	}
	
	
	
	public synchronized JSONArray employementtypes() throws SQLException {

		JSONArray employementtype_arr = new JSONArray();
		String query=" SELECT * FROM hclhrm_prod.tbl_employment_types ; ";
		System.out.println("designations ::::"+query );
		List<Map<String, Object>> list= jdbcTemplate.queryForList(query) ;

		for (@SuppressWarnings("rawtypes") Map map : list) {
			JSONObject jsonObj1 = new JSONObject();
			jsonObj1.put("EMPLOYMENTTYPEID", map.get("EMPLOYMENTTYPEID"));
			jsonObj1.put("Name", map.get("NAME"));
			jsonObj1.put("STATUS", map.get("STATUS"));
			
			employementtype_arr.add(jsonObj1);
		}

		return employementtype_arr;

	}
	
	  
	public synchronized int confirmationinserting(Assessment_confirmationPojo assess) throws   Exception{ //, Assessment_confirmationPojo ass
		
		int counter=0 , counter2 = 0, counter3 = 0 ;
		StringBuffer  query= new StringBuffer();
		StringBuffer  query2= new StringBuffer();
		String  query3="";
		String  query4="";
		
		query.append(" insert into hclhrm_prod.tbl_employee_primary_history(EMPLOYEEID, COSTCENTERID, ");
		query.append(" FIRSTNAME, LASTNAME, CALLNAME, GENDER, TITLE, DATEOFBIRTH, STATUS, COMPANYID, ");
		query.append(" EMPLOYMENTTYPEID, EMPLOYEESEQUENCENO, APPLICANTID, OFFERLETTERID, ");
		query.append(" CREATEDBY, DATECREATED, MODIFIEDBY, DATEMODIFIED, VERIFIEDBY, DATEVERIFIED, LOGID, PREVLUPDATE) ");
		query.append(" Select A.EMPLOYEEID, A.COSTCENTERID, A.FIRSTNAME, A.LASTNAME, A.CALLNAME, A.GENDER, A.TITLE,  ");
		query.append(" A.DATEOFBIRTH, A.STATUS, A.COMPANYID,");
		query.append(" A.EMPLOYMENTTYPEID, A.EMPLOYEESEQUENCENO, A.APPLICANTID, A.OFFERLETTERID,A.CREATEDBY,");
		query.append(" A.DATECREATED, A.MODIFIEDBY, A.DATEMODIFIED, A.VERIFIEDBY, A.DATEVERIFIED, A.LOGID, A.LUPDATE ");
		query.append(" from hclhrm_prod.tbl_employee_primary A where A.employeesequenceno='"+assess.getEmpid()+"'; ");
		
		System.out.println("Primary history insertion ::::"+query.toString() );
		counter=jdbcTemplate.update(query.toString());
		 
		query2.append("Insert into test.tbl_employee_confirmation_details_main(EMPLOYEEID,DESIGNATIONID, ");
		query2.append("DEPARTMENTID, SECTIONID, EMPLOYMENTTYPEID, ONDATE, COMMENTS, STATUS,CREATEDBY) ");
		query2.append("values ((select employeeid from hclhrm_prod.tbl_employee_primary where EMPLOYEESEQUENCENO='"+assess.getEmpid()+"') ");
		query2.append(",0,0,0,1,'"+assess.getConfirmationDate()+"','"+assess.getRemarks()+"',1001,"+assess.getCreatedby()+") ; ");
		
		
		counter2=jdbcTemplate.update(query2.toString()); 
		
		query3="update  hclhrm_prod.tbl_employee_primary set EMPLOYMENTTYPEID=1 where employeesequenceno="+assess.getEmpid()+";";
		
		System.out.println(" confirmation details insertion ::::"+query2.toString() );
		
		query4="UPDATE hclhrm_prod_others.tbl_emp_leave_quota set maxleave='3' , for_month='0' where employeeid in (SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno in("+assess.getEmpid()+")) and status=1001  and leavetypeid in(1,2) ";
		
		int Upadte_EmployeeQuota=0; 
		
		  if(counter>0&&counter2>0) { 
			  counter3=jdbcTemplate.update(query3.toString()); 
			  Upadte_EmployeeQuota=jdbcTemplate.update(query4.toString());
			  
			  if(counter3>0&&Upadte_EmployeeQuota>0)
			   {
				 counter=1;
			   }
			  else
			  {
				  counter=0;
			  }
			 }
		   
		return counter;

	}
	
	

}

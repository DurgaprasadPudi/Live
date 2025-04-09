package com.hetero.heteroiconnect.vaccine;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;

@Repository
public class VaccineRepositry {
	@Autowired
	JdbcTemplate jdbcTemplate;
	 
	@SuppressWarnings("rawtypes")
	public JSONArray EmpInfo(String Empid)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		try {
			String EMPINFO=" select  p.employeesequenceno as 'EmpCode',p.callname as 'EmpName',  IFNULL(p.dateofbirth,'0000-00-00') as 'DATEOFBIRTH',  CONCAT(FLOOR((TIMESTAMPDIFF(MONTH, p.dateofbirth, CURDATE()) / 12)), ' YEARS ',  MOD(TIMESTAMPDIFF(MONTH, p.dateofbirth, CURDATE()), 12) , ' MONTHS') AS 'AGE',   s.name as 'STATUS', cen.name as 'OFFICE', bu.name as 'division', IFNULL(DEP.NAME,'') as 'DEPARTMENT', p.employeeid , ifnull(vd.vaccineID,0) vaccineID, ifnull(vd.first_dose,'NO') first_dose , ifnull(date_format(vd.first_dose_date,'%Y-%m-%d'),'') first_dose_date, ifnull(vd.second_dose,'NO') second_dose , IF(vd.second_dose_date='' || vd.second_dose_date is null ,'', date_format(vd.second_dose_date,'%Y-%m-%d')) second_dose_date  from hclhrm_prod.tbl_employee_primary p left join hclhrm_prod.tbl_status_codes s on s.status=p.status  left join hcladm_prod.tbl_costcenter cen on p.costcenterid=cen.costcenterid  LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.BUSINESSUNITID=p.COMPANYID  LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON p.EMPLOYEEID=DD.EMPLOYEEID  LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID=DEP.DEPARTMENTID  LEFT JOIN test.tbl_employee_vaccine_details vd on vd.employeeid=p.employeesequenceno  where  bu.callname in ('HYD','MUM') and p.status=1001 and p.employeesequenceno in ('"+Empid+"');" ;
			
			//System.out.println(EMPINFO.toString());
			List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			  
			 for (@SuppressWarnings("rawtypes") Map row : rows) {
				  ADD=new net.sf.json.JSONObject();
				/*  Employeeid, EmpName, DATEOFBIRTH, AGE, STATUS, OFFICE, division, DEPARTMENT,
				  vaccineID, first_dose, first_dose_date, second_dose, second_dose_date*/
				  
				  ADD.put("EmpCode",row.get("EmpCode"));
				  ADD.put("Fullname",row.get("EmpName"));
				  ADD.put("DATEOFBIRTH",row.get("DATEOFBIRTH"));
				  ADD.put("AGE",row.get("AGE")); 
				  ADD.put("STATUS",row.get("STATUS"));
				  ADD.put("OFFICE",row.get("OFFICE"));
				  ADD.put("DIVISION",row.get("DIVISION"));
				  ADD.put("DEPARTMENT",row.get("DEPARTMENT"));
				  ADD.put("VACCINEID",row.get("vaccineID"));
				  ADD.put("FIRST_DOSE",row.get("FIRST_DOSE"));
				  ADD.put("FIRST_DOSE_DATE",row.get("FIRST_DOSE_DATE"));
				  ADD.put("SECOND_DOSE",row.get("SECOND_DOSE"));
				  ADD.put("SECOND_DOSE_DATE",row.get("SECOND_DOSE_DATE"));
				  
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public JSONArray vaccinetypes(String Empid)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		try {
			String EMPINFO="SELECT SNO,NAME FROM test.tbl_vaccine_details where status=1001" ;
			 
			List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			
			 for (@SuppressWarnings("rawtypes") Map row : rows) {
				 
				  ADD=new net.sf.json.JSONObject();
				  ADD.put("SNO",row.get("SNO"));
				  ADD.put("NAME",row.get("NAME"));
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
 
	@SuppressWarnings("rawtypes")
	public int vaccinerequest(Vaccine vc) throws SQLException ,Exception 
	{
		 int count=0;
		 String sql ="";
		 
		 System.out.println(vc.toString());
		 
		 boolean first_flag=true; 
		 boolean secound_flag=true; 
		 
		 if(vc.getFirst_dose()==null)
		 {
			 first_flag=false;
		 }
		 if(vc.getSecond_dose()==null)
		 {
			 secound_flag=false;
		 }
		 
		 
 	 if(first_flag&&secound_flag)
 		 {
 		     if(vc.getFirst_dose().equalsIgnoreCase("YES")&&vc.getSecond_dose().equalsIgnoreCase("YES"))
 		     {
 		    	 sql = "INSERT INTO test.tbl_employee_vaccine_details (EMPLOYEEID,VACCINEID, FIRST_DOSE,FIRST_DOSE_DATE,FIRST_DOSE_CREATEDBY,FIRST_DOSE_ACTION_DATE,SECOND_DOSE,SECOND_DOSE_DATE,SECOND_DOSE_CREATEDBY,SECOND_DOSE_ACTION_DATE) VALUES('"+vc.getEmpcode()+"', "+vc.getVaccineid()+", 'YES','"+vc.getFirst_dose_date()+"','"+vc.getCreatedby()+"',NOW(),'"+vc.getSecond_dose()+"','"+vc.getSecond_dose_date()+"','"+vc.getCreatedby()+"',NOW()) ON DUPLICATE KEY UPDATE VACCINEID='"+vc.getVaccineid()+"',FIRST_DOSE='YES',FIRST_DOSE_DATE='"+vc.getFirst_dose_date()+"',FIRST_DOSE_CREATEDBY='"+vc.getCreatedby()+"',FIRST_DOSE_ACTION_DATE=NOW(),"
 				 		+ "SECOND_DOSE='YES',SECOND_DOSE_DATE='"+vc.getSecond_dose_date()+"',SECOND_DOSE_CREATEDBY='"+vc.getCreatedby()+"',SECOND_DOSE_ACTION_DATE=NOW() ";
 		     }
 		     
	 }
 	  else if(first_flag&&vc.getFirst_dose().equalsIgnoreCase("YES"))
		 {
			 sql = "INSERT INTO test.tbl_employee_vaccine_details"
			 		+ " (EMPLOYEEID,VACCINEID, FIRST_DOSE,FIRST_DOSE_DATE,FIRST_DOSE_CREATEDBY,FIRST_DOSE_ACTION_DATE,SECOND_DOSE)"
			 		+ " VALUES('"+vc.getEmpcode()+"', "+vc.getVaccineid()+", 'YES','"+vc.getFirst_dose_date()+"','"+vc.getCreatedby()+"',NOW(),'false') ON DUPLICATE KEY UPDATE VACCINEID='"+vc.getVaccineid()+"',FIRST_DOSE='YES',FIRST_DOSE_DATE='"+vc.getFirst_dose_date()+"',FIRST_DOSE_CREATEDBY='"+vc.getCreatedby()+"',FIRST_DOSE_ACTION_DATE=NOW(),SECOND_DOSE='false'";
		 }
			 else if(secound_flag&&vc.getSecond_dose().equalsIgnoreCase("YES"))
			 {
			  sql = " INSERT INTO test.tbl_employee_vaccine_details"
			  		+ " (EMPLOYEEID,VACCINEID, SECOND_DOSE,SECOND_DOSE_DATE,SECOND_DOSE_CREATEDBY,SECOND_DOSE_ACTION_DATE)"
			  		+ " VALUES('"+vc.getEmpcode()+"', "+vc.getVaccineid()+", 'YES','"+vc.getSecond_dose_date()+"','"+vc.getCreatedby()+"',NOW()) "
			  		+ "ON DUPLICATE KEY UPDATE VACCINEID='"+vc.getVaccineid()+"',SECOND_DOSE='YES',SECOND_DOSE_DATE='"+vc.getSecond_dose_date()+"',SECOND_DOSE_CREATEDBY='"+vc.getCreatedby()+"',SECOND_DOSE_ACTION_DATE=NOW()";
			 }
	 
		
		 System.out.println(sql);
		 count=jdbcTemplate.update(sql);
		 
		return count;
	}
	@SuppressWarnings("rawtypes")
	public int familyrequest(Family fm) throws SQLException ,Exception 
	{
		 int count=0;
		 String sql ="";
		 
		 System.out.println(fm.toString());
		 
		 boolean first_flag=true; 
		 boolean secound_flag=true; 
		 
		 if(fm.getFirst_dose()==null)
		 {
			 first_flag=false;
		 }
		 if(fm.getSecond_dose()==null)
		 {
			 secound_flag=false;
		 }
		 
		 if(first_flag&&secound_flag)
 		 {
 		     if(fm.getFirst_dose().equalsIgnoreCase("YES")&&fm.getSecond_dose().equalsIgnoreCase("YES"))
 		     {
 		    	sql = "  INSERT INTO test.tbl_employee_vaccine_details_family (EMPLOYEEID,VACCINEID,FIRST_DOSE,FIRST_DOSE_DATE,FIRST_DOSE_COMMENTS,FIRST_DOSE_CREATEDBY,FIRST_DOSE_ACTION_DATE,PERSON_NAME,RELATIONID,AADHAR, MOBILE,SECOND_DOSE,SECOND_DOSE_DATE,SECOND_DOSE_COMMENTS,SECOND_DOSE_CREATEDBY,SECOND_DOSE_ACTION_DATE) VALUES('"+fm.getEmployeeid()+"', '"+fm.getVaccineid()+"', 'YES','"+fm.getFirst_dose_date()+"','','"+fm.getCreatedby()+"',NOW(),'"+fm.getPersonname()+"','"+fm.getRelationid()+"','"+fm.getaadhar()+"','"+fm.getMobile()+"','"+fm.getSecond_dose()+"','"+fm.getSecond_dose_date()+"','','"+fm.getCreatedby()+"',now());";
 		     }
	       }
 	  else if(first_flag&&fm.getFirst_dose().equalsIgnoreCase("YES"))
		 {
			 sql = "INSERT INTO test.tbl_employee_vaccine_details_family"
			 		+ " (EMPLOYEEID, PERSON_NAME, RELATIONID, VACCINEID, FIRST_DOSE, FIRST_DOSE_DATE, FIRST_DOSE_COMMENTS,FIRST_DOSE_ACTION_DATE,FIRST_DOSE_CREATEDBY,AADHAR, MOBILE,SECOND_DOSE,SECOND_DOSE_DATE)"
			 		+ " VALUES('"+fm.getEmployeeid()+"','"+fm.getPersonname()+"','"+fm.getRelationid()+"','"+fm.getVaccineid()+"','"+fm.getFirst_dose()+"','"+fm.getFirst_dose_date()+"','',NOW(),'"+fm.getCreatedby()+"','"+fm.getAadhar()+"','"+fm.getMobile()+"','false','') ON "
			 				+ "DUPLICATE KEY UPDATE PERSON_NAME='"+fm.getPersonname()+"', RELATIONID='"+fm.getRelationid()+"', VACCINEID='"+fm.getVaccineid()+"', FIRST_DOSE='"+fm.getFirst_dose()+"', FIRST_DOSE_DATE='"+fm.getFirst_dose_date()+"', FIRST_DOSE_COMMENTS='',FIRST_DOSE_ACTION_DATE=NOW(),FIRST_DOSE_CREATEDBY='"+fm.getCreatedby()+"',AADHAR='"+fm.getaadhar()+"', MOBILE='"+fm.getMobile()+"',SECOND_DOSE='fasle',SECOND_DOSE_DATE='' "; }
			 else if(secound_flag&&fm.getSecond_dose().equalsIgnoreCase("YES"))
			 {
			  sql = " INSERT INTO test.tbl_employee_vaccine_details_family"
			  		+ " (EMPLOYEEID,PERSON_NAME,VACCINEID, RELATIONID, SECOND_DOSE, SECOND_DOSE_DATE, SECOND_DOSE_COMMENTS, SECOND_DOSE_CREATEDBY,SECOND_DOSE_ACTION_DATE)"
			  		+ " VALUES('"+fm.getEmployeeid()+"','"+fm.getPersonname()+"','"+fm.getVaccineid()+"','"+fm.getRelationid()+"','"+fm.getSecond_dose()+"','"+fm.getSecond_dose_date()+"','','"+fm.getCreatedby()+"',NOW()) "
			  		+ "ON DUPLICATE KEY UPDATE EMPLOYEEID='"+fm.getEmployeeid()+"',PERSON_NAME='"+fm.getPersonname()+"',VACCINEID='"+fm.getVaccineid()+"', RELATIONID='"+fm.getRelationid()+"',SECOND_DOSE='"+fm.getSecond_dose()+"', SECOND_DOSE_DATE='"+fm.getSecond_dose_date()+"', SECOND_DOSE_COMMENTS='', SECOND_DOSE_CREATEDBY='"+fm.getCreatedby()+"',SECOND_DOSE_ACTION_DATE=NOW()";
			 }
		  System.out.println(sql);
		 count=jdbcTemplate.update(sql);
		return count;
	}
	@SuppressWarnings("rawtypes")
	public JSONArray Family(String Empid)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		try {
			//String EMPINFO="SELECT RS.RELATIONNAME,CONCAT(b.FIRSTNAME,' ',b.LASTNAME) NAME,gen.NAME GENDER, IF(b.isdependent='1','YES','NO') ISDEPENDENT  FROM hclhrm_prod.tbl_employee_primary a left join hclhrm_prod.tbl_employee_family b on b.employeeid=a.employeeid left join hcladm_prod.tbl_relations rs on rs.RELATIONID=b.RELATIONID left join  hcladm_prod.tbl_gender gen on gen.gender=b.gender where employeesequenceno="+Empid+"" ;
			 String EMPINFO="SELECT employeesequenceno as 'Employeeid',RS.RELATIONNAME ,RS.RELATIONID,CONCAT(b.FIRSTNAME,' ',b.LASTNAME) AS 'NAME',gen.NAME GENDER, date_format(IFNULL(b.DATEOFBIRTH,'0000-00-00'),'%d-%m-%Y') AS DATEOFBIRTH, IFNULL(CONCAT(FLOOR((TIMESTAMPDIFF(MONTH, b.dateofbirth, CURDATE()) / 12)), ' YEARS ', MOD(TIMESTAMPDIFF(MONTH, b.dateofbirth, CURDATE()), 12) , ' MONTHS'),'') AS 'AGE', IF(b.isdependent='1','YES','NO') ISDEPENDENT, ifnull(vd.vaccineID,0) vaccineID ,ifnull(vd.first_dose,'NO') first_dose , ifnull(date_format(vd.first_dose_date,'%Y-%m-%d'),'') first_dose_date, ifnull(vd.second_dose,'NO') second_dose , IF(vd.second_dose_date='' || vd.second_dose_date is null ,'',  date_format(vd.second_dose_date,'%Y-%m-%d')) second_dose_date ,IFNULL(vd.AADHAR,'') AADHAR,IFNULL(vd.MOBILE,'') MOBILE FROM hclhrm_prod.tbl_employee_primary a left join hclhrm_prod.tbl_employee_family b on b.employeeid=a.employeeid left join hcladm_prod.tbl_relations rs on rs.RELATIONID=b.RELATIONID left join  hcladm_prod.tbl_gender gen on gen.gender=b.gender left join test.tbl_employee_vaccine_details_family vd on vd.employeeid=a.employeesequenceno and  vd.person_name=CONCAT(b.FIRSTNAME,' ',b.LASTNAME) and b.relationid=vd.RELATIONID where employeesequenceno="+Empid+" and b.isdependent='1' and b.status=1001 ";
			List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
		 
		/*	Employeeid, RELATIONNAME, EmpName, GENDER, DATEOFBIRTH, AGE, ISDEPENDENT, vaccineID,
			first_dose, first_dose_date, second_dose, second_dose_date, AADHAR, MOBILE*/
			 int i=1;
			 for (@SuppressWarnings("rawtypes") Map row : rows) {
				  
				  ADD=new net.sf.json.JSONObject();
				  
				  ADD.put("RELATIONNAME",row.get("RELATIONNAME"));
				  ADD.put("RELATIONID",row.get("RELATIONID"));
				  ADD.put("NAME",row.get("NAME"));
				  ADD.put("GENDER",row.get("GENDER"));
				  ADD.put("DATEOFBIRTH",row.get("DATEOFBIRTH"));
				  ADD.put("AGE",row.get("AGE"));
				  ADD.put("ISDEPENDENT",row.get("ISDEPENDENT"));
				  ADD.put("AADHAR",row.get("AADHAR"));
				  ADD.put("MOBILE",row.get("MOBILE"));
				  ADD.put("VACCINEID",row.get("vaccineID"));
				  ADD.put("FIRST_DOSE",row.get("FIRST_DOSE"));
				  ADD.put("FIRST_DOSE_DATE",row.get("FIRST_DOSE_DATE"));
				  ADD.put("SECOND_DOSE",row.get("SECOND_DOSE"));
				  ADD.put("SECOND_DOSE_DATE",row.get("SECOND_DOSE_DATE"));
				  
				  ADD.put("INDEX",i);
					 i++;
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	 
	}

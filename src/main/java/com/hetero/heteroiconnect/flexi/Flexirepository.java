package com.hetero.heteroiconnect.flexi;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hetero.heteroiconnect.user.LoginUserRepository;

import net.sf.json.JSONArray;

@Repository
public class Flexirepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	LoginUserRepository mstUserRepository;
	 
	@SuppressWarnings("rawtypes")
	public JSONArray employeebusinessunit(String Empid)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		try {
			String EMPINFO="SELECT K.BUSINESSUNITID BID,c.name BUNAME FROM hclhrm_prod.tbl_employee_primary B LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_BUSINESSUNIT K  ON B.EMPLOYEEID= K.EMPLOYEEID LEFT JOIN hcladm_prod.tbl_businessunit C ON C.BUSINESSUNITID=K.BUSINESSUNITID where B.EMPLOYEESEQUENCENO IN ("+Empid+") and C.status=1001;" ;
			 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  
				  ADD=new net.sf.json.JSONObject();
				  ADD.put("BID",row.get("BID"));
				  ADD.put("BUNAME",row.get("BUNAME"));
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	
	 
		@SuppressWarnings("rawtypes")
		public JSONArray flexilist(String empId,String divisionType,String Reporttype)
		{
			JSONArray empid= new JSONArray();
			net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
			 
			String Querytype="";
			String Buttontype="";
			
			if(Reporttype.equalsIgnoreCase("Assign_Policy")){
				
				Querytype="not";
				Buttontype="ADD";
			}
			else if(Reporttype.equalsIgnoreCase("Remove_Policy"))
			{
				Querytype="";
				Buttontype="Remove";
			}
			  
						 StringBuffer flexiQuery= new StringBuffer();
						 flexiQuery.append(" select  p.employeesequenceno as 'Empseq',p.callname as 'EmpName', s.name as 'STATUS', ");
						 flexiQuery.append(" cen.name as 'OFFICE', bu.name as 'division', IFNULL(DEP.NAME,'') as 'DEPARTMENT',p.employeeid ");
						// flexiQuery.append(" IF(p.employeesequenceno=f.employeesequenceno,'true','false')as 'action' ");
						 flexiQuery.append(" from hclhrm_prod.tbl_employee_primary p ");
						 //flexiQuery.append(" left join  test.tbl_emp_flexi_policy f on p.employeesequenceno=f.employeesequenceno ");
						 flexiQuery.append(" left join hclhrm_prod.tbl_status_codes s on s.status=p.status ");
						 flexiQuery.append(" left join hcladm_prod.tbl_costcenter cen on p.costcenterid=cen.costcenterid ");
						 flexiQuery.append(" LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.BUSINESSUNITID=p.COMPANYID ");
						 flexiQuery.append(" LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON p.EMPLOYEEID=DD.EMPLOYEEID ");
						 flexiQuery.append(" LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID=DEP.DEPARTMENTID ");
						 //flexiQuery.append(" where cen.name in ('office')and bu.callname in ('hyd') ");
						// flexiQuery.append(" and p.companyid in (11) and p.status=1001 and p.employeesequenceno=11440; ");
						 
						 if(!divisionType.equalsIgnoreCase("0")&&empId.length()==0) {
							 flexiQuery.append(" where cen.name in ('office')and bu.callname in ('hyd') and p.companyid IN ("+divisionType+")  and p.employeesequenceno "+Querytype+" in(Select employeesequenceno from test.tbl_emp_flexi_policy) and p.status=1001");
							}
						
						 else if(divisionType.equalsIgnoreCase("0")&&empId.length()!=0){
							 flexiQuery.append(" where cen.name in ('office')and bu.callname in ('hyd') and p.employeesequenceno IN ("+empId+") and p.employeesequenceno "+Querytype+" in(Select employeesequenceno from test.tbl_emp_flexi_policy) and p.status=1001 ");
							}
						 
						 else
						 {
							 flexiQuery.append(" where cen.name in ('office')and bu.callname in ('hyd') and p.employeesequenceno IN ("+empId+") and p.companyid IN ("+divisionType+")  and p.employeesequenceno "+Querytype+" in(Select employeesequenceno from test.tbl_emp_flexi_policy) and p.status=1001 ");
				 
						 }
                                
						 
						 System.out.println(flexiQuery.toString());
						 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(flexiQuery.toString());
								// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
								  for (@SuppressWarnings("rawtypes") Map row : rows) {
									   
									  ADD=new net.sf.json.JSONObject();
									  ADD.put("Employeeid",row.get("Empseq"));
									  ADD.put("EmpName",row.get("EmpName"));
									  ADD.put("STATUS",row.get("STATUS"));
									  ADD.put("OFFICE",row.get("OFFICE"));
									  ADD.put("DIVISION",row.get("division"));
									  ADD.put("DEPARTMENT",row.get("DEPARTMENT"));
									  ADD.put("ADD",Buttontype);
									 
									  empid.add(ADD);
						        }
								 
						 
						  
			return empid;
		}
		
		
		@SuppressWarnings("rawtypes")
		public int Assign_Remove(String Buttontype,String Employeeid,String USEDRID)
		{
		 

			String Query="";
			   if(Buttontype.equalsIgnoreCase("ADD"))
			{
				   Query= " insert into test.tbl_emp_flexi_policy (EMPLOYEESEQUENCENO,EMPLOYEEID,ADDBY) select employeesequenceno,employeeid,'"+Employeeid+"' from hclhrm_prod.tbl_employee_primary where employeesequenceno in("+USEDRID+"); ";
				 
			}
			else if(Buttontype.equalsIgnoreCase("Remove") )
			{
				Query= "update test.tbl_emp_flexi_policy set employeesequenceno=concat(employeesequenceno,01), "
						+ "delid="+USEDRID+", deldate=now(), status=1001, delby="+Employeeid+"  where employeesequenceno in("+USEDRID+") and status=1001; ";
			 
			} 
			
			 
			 int count=0;
			try {
				// count = jdbcTemplate.update("UPDATE hclhrm_prod_others.tbl_emp_leave_quota T1 LEFT JOIN hclhrm_prod.tbl_employee_primary T2 ON  T2.employeeid = T1.employeeid SET T1.backdate=(SELECT DATEDIFF(curdate(),?)+1) WHERE T2.employeesequenceno in (?) and  T1.LEAVETYPEID in(?) and T1.status=1001",Date,Employeeid,""+Leavetypeid+""); 
				 
				 
				 count=jdbcTemplate.update(Query);
				 
				 
				}catch(Exception err){
					System.out.println("Exception at reverse" +err);
				}
			return count;
		}
		
		
		
		@SuppressWarnings("rawtypes")
		public JSONArray Sat_Policy_list(String empId,String divisionType,String Reporttype)
		{
			JSONArray empid= new JSONArray();
			net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
			 
			String Querytype="";
			String Buttontype="";
			
			if(Reporttype.equalsIgnoreCase("Assign_Policy")){
				
				Querytype="not";
				Buttontype="ADD";
			}
			else if(Reporttype.equalsIgnoreCase("Remove_Policy"))
			{
				Querytype="";
				Buttontype="Remove";
			}
			  
						 StringBuffer flexiQuery= new StringBuffer();
						 flexiQuery.append(" select  p.employeesequenceno as 'Empseq',p.callname as 'EmpName', s.name as 'STATUS', ");
						 flexiQuery.append(" cen.name as 'OFFICE', bu.name as 'division', IFNULL(DEP.NAME,'') as 'DEPARTMENT',p.employeeid ");
						// flexiQuery.append(" IF(p.employeesequenceno=f.employeesequenceno,'true','false')as 'action' ");
						 flexiQuery.append(" from hclhrm_prod.tbl_employee_primary p ");
						 //flexiQuery.append(" left join  test.tbl_emp_flexi_policy f on p.employeesequenceno=f.employeesequenceno ");
						 flexiQuery.append(" left join hclhrm_prod.tbl_status_codes s on s.status=p.status ");
						 flexiQuery.append(" left join hcladm_prod.tbl_costcenter cen on p.costcenterid=cen.costcenterid ");
						 flexiQuery.append(" LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.BUSINESSUNITID=p.COMPANYID ");
						 flexiQuery.append(" LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON p.EMPLOYEEID=DD.EMPLOYEEID ");
						 flexiQuery.append(" LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID=DEP.DEPARTMENTID ");
						 //flexiQuery.append(" where cen.name in ('office')and bu.callname in ('hyd') ");
						// flexiQuery.append(" and p.companyid in (11) and p.status=1001 and p.employeesequenceno=11440; ");
						 
						 if(!divisionType.equalsIgnoreCase("0")&&empId.length()==0) {
							 flexiQuery.append(" where cen.name in ('office')and bu.callname in ('hyd') and p.companyid IN ("+divisionType+")  and p.employeesequenceno "+Querytype+" in(SELECT employee_seq FROM test.tbl_emp_saturday_policy) and p.status=1001");
							}
						
						 else if(divisionType.equalsIgnoreCase("0")&&empId.length()!=0){
							 flexiQuery.append(" where cen.name in ('office')and bu.callname in ('hyd') and p.employeesequenceno IN ("+empId+") and p.employeesequenceno "+Querytype+" in(SELECT employee_seq FROM test.tbl_emp_saturday_policy) and p.status=1001 ");
							}
						 
						 else
						 {
							 flexiQuery.append(" where cen.name in ('office')and bu.callname in ('hyd') and p.employeesequenceno IN ("+empId+") and p.companyid IN ("+divisionType+")  and p.employeesequenceno "+Querytype+" in(SELECT employee_seq FROM test.tbl_emp_saturday_policy) and p.status=1001 ");
				 
						 }
                                
						 
						 System.out.println(flexiQuery.toString());
						 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(flexiQuery.toString());
								// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
								  for (@SuppressWarnings("rawtypes") Map row : rows) {
									   
									  ADD=new net.sf.json.JSONObject();
									  ADD.put("Employeeid",row.get("Empseq"));
									  ADD.put("EmpName",row.get("EmpName"));
									  ADD.put("STATUS",row.get("STATUS"));
									  ADD.put("OFFICE",row.get("OFFICE"));
									  ADD.put("DIVISION",row.get("division"));
									  ADD.put("DEPARTMENT",row.get("DEPARTMENT"));
									  ADD.put("ADD",Buttontype);
									 
									  empid.add(ADD);
						        }
								 
						 
						  
			return empid;
		}

		
		
		
		@SuppressWarnings("rawtypes")
		public int Assign_Sat_Policy(String Buttontype,String Employeeid,String USEDRID)
		{
		 

			String Query="";
			   if(Buttontype.equalsIgnoreCase("ADD"))
			{
				   Query= " insert into test.tbl_emp_saturday_policy (EMPLOYEE_SEQ, DAYNAME, STATUS) select employeesequenceno,'HL',1001 from hclhrm_prod.tbl_employee_primary where employeesequenceno in("+USEDRID+"); ";
				 
			}
			else if(Buttontype.equalsIgnoreCase("Remove") )
			{
				Query= "DELETE FROM test.tbl_emp_saturday_policy  where employee_seq="+USEDRID+" ";
			 
			} 
			
			 
			 int count=0;
			try {
				// count = jdbcTemplate.update("UPDATE hclhrm_prod_others.tbl_emp_leave_quota T1 LEFT JOIN hclhrm_prod.tbl_employee_primary T2 ON  T2.employeeid = T1.employeeid SET T1.backdate=(SELECT DATEDIFF(curdate(),?)+1) WHERE T2.employeesequenceno in (?) and  T1.LEAVETYPEID in(?) and T1.status=1001",Date,Employeeid,""+Leavetypeid+""); 
				 
				 
				 count=jdbcTemplate.update(Query);
				 
				 
				}catch(Exception err){
					System.out.println("Exception at reverse" +err);
				}
			return count;
		}
		
		
		
		
		 
		@SuppressWarnings("rawtypes")
		public JSONArray getpayslips(String Empid)
		{
		
			JSONArray empid= new JSONArray();
			net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
			try {
				//String EMPINFO="SELECT PAYPERIOD,DATE_FORMAT(CONCAT(LEFT(PAYPERIOD,4),'-',RIGHT(PAYPERIOD,2),'-','01'), '%b') MONTHNAME,YEAR(CONCAT(LEFT(PAYPERIOD,4),'-',RIGHT(PAYPERIOD,2),'-','01')) YEAR,BUSINESSUNITID FROM hclhrm_prod.tbl_employee_payperiod_details where employeesequenceno="+Empid+" order by  payperiod desc limit 3" ;
				
				String EMPINFO="SELECT DD.PAYPERIOD,DATE_FORMAT(CONCAT(LEFT(DD.PAYPERIOD,4),'-',RIGHT(DD.PAYPERIOD,2),'-','01'), '%b') MONTHNAME,YEAR(CONCAT(LEFT(DD.PAYPERIOD,4),'-',RIGHT(DD.PAYPERIOD,2),'-','01')) YEAR,DD.BUSINESSUNITID FROM hclhrm_prod.tbl_employee_payperiod_details dd LEFT JOIN hclhrm_prod.tbl_businessunit_payroll_process pc on pc.payperiod=dd.payperiod and pc.businessunitid=dd.businessunitid where employeesequenceno="+Empid+" and pc.STATUS='C' order by  payperiod desc limit 3";
				
				
				List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
				// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
				  for (@SuppressWarnings("rawtypes") Map row : rows) {
					  
					  ADD=new net.sf.json.JSONObject();
					  ADD.put("PAYPERIOD",row.get("PAYPERIOD"));
					  ADD.put("MONTHNAME",row.get("MONTHNAME"));
					  ADD.put("YEAR",row.get("YEAR"));
					  ADD.put("BUSINESSUNITID",row.get("BUSINESSUNITID"));
					  //null\BKP
					  File f = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\null\\BKP\\"+row.get("PAYPERIOD")+"\\"+row.get("BUSINESSUNITID")+"\\"+Empid+".pdf");
					  if (f.exists())
				        {
				        	 ADD.put("URL","https://sso.heterohcl.com/null/FileNotFound.pdf");
				        }
				         else
				           {
				                	ADD.put("URL","https://sso.heterohcl.com/null/FileNotFound.pdf");
				           }
				 
					  //ADD.put("URL","http://iconnect.heterohcl.com/EmployeeAuth/download/files_mydesk/"+Empid+"/"+row.get("PAYPERIOD")+"-PAYSLIP-"+Empid+".pdf");
					  empid.add(ADD);
		        }
				}catch(Exception err){
					System.out.println("Exception at reverse" +err);
				}
			return empid;
		}
		
		
		@SuppressWarnings("rawtypes")
		public JSONArray getITS(String Empid)
		{
		
			JSONArray empid= new JSONArray();
			net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
			try {
				  
					  ADD=new net.sf.json.JSONObject();
					    
					  File FY = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ForCast\\"+Empid+".pdf");

					  
					  
					  String PAN=mstUserRepository.PanNumber(Empid);
					  
					  System.out.println(PAN);
					  
					  File FORM16A =null;
					  
					  boolean flag=false;
					  
					  if(!PAN.equalsIgnoreCase("0"))
					  {
						  flag=true;
						  FORM16A = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ForCast\\16AANDB2022\\"+PAN+".pdf");
					  }
					  if (FY.exists())
				        {
				        	  ADD.put("FY","https://sso.heterohcl.com/ForCast/"+Empid+".pdf");
				        }
					   else
					   {
						      ADD.put("FY","https://sso.heterohcl.com/null/FileNotFound.pdf");
					   }
					  
					   if (FORM16A.exists()&&!PAN.equalsIgnoreCase("0"))
				        {
				        	  ADD.put("FORM16A","https://sso.heterohcl.com/ForCast/16AANDB2022/"+PAN+".pdf");
				        }
				        else
				         {
				              ADD.put("FORM16A","https://sso.heterohcl.com/null/FileNotFound.pdf");
				         }
				 
					  //ADD.put("URL","http://iconnect.heterohcl.com/EmployeeAuth/download/files_mydesk/"+Empid+"/"+row.get("PAYPERIOD")+"-PAYSLIP-"+Empid+".pdf");
					  empid.add(ADD);
		         
				}catch(Exception err){
					System.out.println("Exception at reverse" +err);
				}
			return empid;
		}
		
		 
}

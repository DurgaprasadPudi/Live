package com.hetero.heteroiconnect.leavemanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;

@Repository
public class LeavequotaRepositry {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DataSource dataSource;

	   
       public void setDataSource(DataSource dataSource) {
           this.jdbcTemplate = new JdbcTemplate(dataSource);
       }
	
	  

	@SuppressWarnings("rawtypes")
	public JSONArray EmpInfo(String Empid)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		try {
			String EMPINFO="select p.STATUS,S.name DISPLAYNAME,p.employeeid 'HRMSEMPLOYEEID',p.employeesequenceno 'EmpCode', p.callname'Fullname', ifnull(C.NAME,'--') 'DIVISION', ifnull(E.NAME,'--')'DEPT', IFNULL(F.name,'--')'DESIGNATION',s.name 'EMPSTATUS',t.name 'EmploymentType' ,CONVERT(TIMESTAMPDIFF(MONTH,CUREXP.DATEOFJOIN, curdate()),CHAR) 'Cur_Exp'  	from hclhrm_prod.tbl_employee_primary p 	LEFT JOIN hcladm_prod.tbl_businessunit C ON C.BUSINESSUNITID=p.COMPANYID 	LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON p.EMPLOYEEID=D.EMPLOYEEID 	 LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT E ON D.DEPARTMENTID=E.DEPARTMENTID           LEFT JOIN hcladm_prod.tbl_designation F ON D.designationid=F.designationid           left join hclhrm_prod.tbl_status_codes s on s.status=p.status         left join HCLHRM_PROD.tbl_employment_types t on t.employmenttypeid=p.employmenttypeid LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE  CUREXP ON P.EMPLOYEEID=CUREXP.EMPLOYEEID where p.employeesequenceno in ('"+Empid+"');" ;
			 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  
				  ADD=new net.sf.json.JSONObject();
				  
				  ADD.put("STATUS",row.get("STATUS"));
				  ADD.put("DISPLAYNAME",row.get("DISPLAYNAME"));
				  ADD.put("HRMSEMPLOYEEID",row.get("HRMSEMPLOYEEID"));
				  ADD.put("EmpCode",row.get("EmpCode"));
				  ADD.put("Fullname",row.get("Fullname"));
				  ADD.put("DIVISION",row.get("DIVISION"));
				  ADD.put("DEPT",row.get("DEPT"));
				  ADD.put("DESIGNATION",row.get("DESIGNATION"));
				  ADD.put("EMPSTATUS",row.get("EMPSTATUS"));
				  ADD.put("EmploymentType",row.get("EmploymentType"));
				  ADD.put("Cur_Exp",row.get("Cur_Exp"));
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	@SuppressWarnings("rawtypes")
	public String emptransaction()
	{
		 String Date="";
		try {
			//String EMPINFO="SELECT if(FROMDATE < concat(YEAR(CURDATE()),'-01-01'),concat(YEAR(CURDATE()),'-01-01'),FROMDATE)  FROMDATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where TRANSACTIONDURATION in (SELECT MAX(TRANSACTIONDURATION)TODATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where businessunitid=11 and TRANSACTIONTYPEID=1)  and businessunitid=11 and TRANSACTIONTYPEID=1" ;
			
			String EMPINFO="SELECT MAX(FROMDATE) FROMDATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where businessunitid=11 and TRANSACTIONTYPEID=1 limit 1";
			List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  
				  Date= row.get("FROMDATE").toString();
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return Date;
	}
	@SuppressWarnings("rawtypes")
	public JSONArray empshowleavetypes(String Empid)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		try {
			String EMPINFO="SELECT le.leavetypeid 'TYPE',LE.name 'NAME',  b.availableqty 'Available' , b.backdate 'ENABLE' FROM hclhrm_prod.tbl_employee_primary a left join hclhrm_prod_others.tbl_emp_leave_quota b on b.employeeid=a.employeeid left join hclhrm_prod.tbl_leave_type le on le.leavetypeid=b.leavetypeid where employeesequenceno in ('"+Empid+"') and b.status=1001 and a.status=1001 and if(b.daymode=0,b.availableqty!=0,b.availableqty=0) and b.leavetypeid!=15" ;
			 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  
				  ADD=new net.sf.json.JSONObject();
				  
				  //TYPE, DATE, Available, ENABLE
				  ADD.put("TYPE",row.get("TYPE"));
				  ADD.put("NAME",row.get("NAME"));
				  ADD.put("Available",row.get("Available"));
				  ADD.put("ENABLE",row.get("ENABLE"));
				  ADD.put("isSelected",false);
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	//	
	
	
	public JSONArray UnAttendanceReq(String Empid)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		try {
			//String EMPINFO="SELECT dateon,if(b.transactiondate is null,dateon,'Exist') 'DATA', if(b.transactiondate is null,dateon,'Exist') 'DATE', if(b.transactiondate is null,'1','0') 'DATACOUNT' FROM test_mum.tbl_att_leave_in_out_status_report a left join hclhrm_prod_others.tbl_attendance_adjustments b on a.employeeid=b.employeeid and b.transactiondate=a.dateon where a.employeeid in ('"+Empid+"') and a.dateon>=(SELECT max(fromdate) FROM hclhrm_prod_others.tbl_iconnect_transaction_dates where businessunitid=11 group by businessunitid) and  a.daytype='WDAY' and (a.att_in>'09:45:00'||timediff(a.att_out,if(a.att_in<'09:00:00','09:00:00',a.att_in)) <'09:00:00' ) and (LEAVE_COUTN IS NULL || LEAVE_COUTN=0)  and  dateon not in(SELECT REQ_DATE FROM hclhrm_prod_others.tbl_emp_attn_req where employeeid in ('"+Empid+"') and REQ_TYPE='AR')   and a.att_in not in ('00:00:00')  and a.att_out not in ('00:00:00') and dateon<curdate()  and if(b.transactiondate is null,'1','0')=1 group by dateon" ;
			
			String EMPINFO="SELECT dateon,if(b.transactiondate is null,dateon,'Exist') 'DATA', if(b.transactiondate is null,dateon,'Exist') 'DATE', if(b.transactiondate is null,'1','0') 'DATACOUNT' FROM test_mum.tbl_att_leave_in_out_status_report a left join hclhrm_prod_others.tbl_attendance_adjustments b on a.employeeid=b.employeeid and b.transactiondate=a.dateon where a.employeeid in ('"+Empid+"') and a.dateon>=(SELECT max(fromdate) FROM hclhrm_prod_others.tbl_iconnect_transaction_dates where businessunitid=11 group by businessunitid) and  a.daytype='WDAY' and (a.att_in>'09:45:00'|| timediff(a.att_out,a.att_in) <'09:00:00') and (LEAVE_COUTN IS NULL || LEAVE_COUTN=0)  and  dateon not in(SELECT REQ_DATE FROM hclhrm_prod_others.tbl_emp_attn_req where employeeid in ('"+Empid+"') and REQ_TYPE='AR')   and a.att_in not in ('00:00:00')  and a.att_out not in ('00:00:00') and dateon<curdate()  and if(b.transactiondate is null,'1','0')=1 group by dateon" ;
			
			
			List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  
				  ADD=new net.sf.json.JSONObject();
				//  dateon, DATA, DATE, DATACOUNT
				  ADD.put("DATE",row.get("DATE"));
				  ADD.put("DATACOUNT",row.get("DATACOUNT"));
				  ADD.put("isSelected",false);
				  
				  
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	
	
	
	
	@SuppressWarnings("rawtypes")
	public int UnfreezRequest(String Date,String Employeeid,String Leavetypeid,String loginemployeeid)
	{
		
		 
		 int count=0;
		try {
			// count = jdbcTemplate.update("UPDATE hclhrm_prod_others.tbl_emp_leave_quota T1 LEFT JOIN hclhrm_prod.tbl_employee_primary T2 ON  T2.employeeid = T1.employeeid SET T1.backdate=(SELECT DATEDIFF(curdate(),?)+1) WHERE T2.employeesequenceno in (?) and  T1.LEAVETYPEID in(?) and T1.status=1001",Date,Employeeid,""+Leavetypeid+""); 
			 
			 String sql = "UPDATE hclhrm_prod_others.tbl_emp_leave_quota T1 LEFT JOIN hclhrm_prod.tbl_employee_primary T2 ON  T2.employeeid = T1.employeeid SET T1.backdate=(SELECT DATEDIFF(curdate(),'"+Date+"')+1) WHERE T2.employeesequenceno in ("+Employeeid+") and  T1.LEAVETYPEID in("+Leavetypeid+") and T1.status=1001";
			  
			 System.out.println(sql);
			 
			 count=jdbcTemplate.update(sql);
			 
			  if (count > 0) {
		            // Only insert into history if the update was successful
		            String insertSql = "INSERT INTO hclhrm_prod_others.tbl_unfreez_dates_history " +
		                               "(EMPID,FROMDATE, LEAVETYPEID, REQUEST_TYPE, CREATED_BY) VALUES (?, ?, ?, ?, ?)";
		            jdbcTemplate.update(insertSql,Employeeid, Date, Leavetypeid, "LR", loginemployeeid);
		        }
			 
			 
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return count;
	}
	
	
	//@SuppressWarnings("rawtypes")
	public int leavequota(String Quantity,String Employeeid,String Leavetypeid) throws SQLException  
	{
		 int count=0;
		String MINIMU_LEAVE="0";
		if(Leavetypeid.equalsIgnoreCase("4"))
		{
			MINIMU_LEAVE="3";
		}
		String MaxLeave="2";
		String CountWeekOff="0";
		String Status="0";
		String DAYMODE="1";
		
		  if(Leavetypeid.equalsIgnoreCase("16") ||Leavetypeid.equalsIgnoreCase("14") || Leavetypeid.equalsIgnoreCase("7") || Leavetypeid.equalsIgnoreCase("07")){
		 	DAYMODE="1";
		     } 
		 
		StringBuffer CLI=new StringBuffer();
		    CLI.append("INSERT INTO HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_QUOTA(EMPLOYEEID, LEAVETYPEID,YEAR,QUANTITY,AVAILABLEQTY,USEDQTY,BACKDATE,STATUS,LOGID,CREATEDBY,DATECREATED,LUPDATE,MAXLEAVE,COUNT_WOFF,FOR_MONTH,COUNT_HOLIDAY,MINIMU_LEAVE,DAYMODE) ");
			CLI.append(" SELECT A.EMPLOYEEID,"+Leavetypeid+",YEAR(CURDATE()),"+Quantity+","+Quantity+",0,2,1001,0, ");
			CLI.append(" (SELECT EMPLOYEEID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY WHERE EMPLOYEESEQUENCENO IN ("+Employeeid+")) ,NOW(),NOW(),"+MaxLeave+","+CountWeekOff+","+Status+","+CountWeekOff+","+MINIMU_LEAVE+","+DAYMODE+" FROM ");
			CLI.append("HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A WHERE EMPLOYEESEQUENCENO IN ("+Employeeid+") ");
		   
			 
            count=jdbcTemplate.update(CLI.toString());
            
		return count;
	}
	
	//Query=" INSERT INTO hclhrm_prod_others.tbl_attendance_adjustments (EMPLOYEEID, TRANSACTIONDATE, CREATEDBY,FLAG, DATECREATED, LUPDATE) values (?,?,?,'0',NOW(),NOW()); ";
	
	public int unfreez_Att_Req_add (String userid,String datelist,String employeeid) throws SQLException
	{
		int j=0;
		
		 String Query = "INSERT INTO hclhrm_prod_others.tbl_attendance_adjustments (EMPLOYEEID, TRANSACTIONDATE, CREATEDBY,FLAG, DATECREATED, LUPDATE) values (?,?,?,'0',NOW(),NOW()) ";
	     PreparedStatement preparedStatement = null;
	     Connection connection = dataSource.getConnection();
	     preparedStatement =connection.prepareStatement(Query);
	      
		
	     for (int i = 0; i<datelist.split(",").length;i++) {
	    	     
	    	 preparedStatement.setString(1, userid);
     	     preparedStatement.setString(2, datelist.split(",")[i]);
     	     preparedStatement.setString(3, employeeid);
     	     
     	    preparedStatement.addBatch();
     	    
	    	}
	     int [] affectedRecords  = preparedStatement.executeBatch();
	      
	       j=1;
	      jdbcTemplate.getDataSource().getConnection().close();
		return j;
	  }
	}

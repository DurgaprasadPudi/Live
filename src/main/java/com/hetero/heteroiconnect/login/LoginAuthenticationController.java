package com.hetero.heteroiconnect.login;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.applicationproperty.Propertyconfig;
import com.hetero.heteroiconnect.classes.Assamattendance;
import com.hetero.heteroiconnect.classes.CallFunction;
import com.hetero.heteroiconnect.classes.CallingStoredProcedure_17;
import com.hetero.heteroiconnect.user.LoginUser;
import com.hetero.heteroiconnect.user.LoginUserRepository;
import com.hetero.heteroiconnect.userRepositry.Birthdaylist;
import com.hetero.heteroiconnect.userRepositry.Department;
import com.hetero.heteroiconnect.userRepositry.Experience;
import com.hetero.heteroiconnect.userRepositry.Holidaylist;
import com.hetero.heteroiconnect.userRepositry.HrDocuments;
import com.hetero.heteroiconnect.userRepositry.LeaveQuota;
import com.hetero.heteroiconnect.userRepositry.LeaveSummary;
import com.hetero.heteroiconnect.userRepositry.MangerAttendanceReq;
import com.hetero.heteroiconnect.userRepositry.MangerLeaveSummary;
import com.hetero.heteroiconnect.userRepositry.Payperiod;
import com.hetero.heteroiconnect.userRepositry.ReporteeManger;

import net.sf.json.JSONArray;
  

//@CrossOrigin(origins = "http://172.19.1.101:4200")
//@CrossOrigin(origins = "http://172.19.1.84:4200")
@RestController
@RequestMapping("/loginaction")
public class LoginAuthenticationController {
	
	
	      
	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	LoginUserRepository mstUserRepository;
     @Autowired
	CallFunction call; 
     
     
     @Autowired
     CallingStoredProcedure_17 Ahemd; 
   
     @Autowired
     Assamattendance Assam; 
     @Autowired
 	JdbcTemplate jdbcTemplate;
      
	 @Autowired
	 private Propertyconfig Propertyconfig;
	 private String UPLOADED_FOLDER = Propertyconfig.getprofileimagepath();
	 
	 private String HR_DOCUMENTS = Propertyconfig.getHR_DOCUMENTS_PDF();
	 
	/// Employee Login Details
	//@EncryptBody(value = EncryptBodyMethod.AES)
	 

	@PostMapping("login")
	public LinkedHashMap<String, Object> user(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		LoginUser user = null;
		System.out.println("Login"+UPLOADED_FOLDER);
		user = mstUserRepository.findByUsernameandpassword(object.getString("userName"), "" + object.get("password"));
		if(user== null){
			  String msg = "PLEASE CHECK THE ENTERED USER CREDENTIALS.";
			  response.put("status",0);
			  response.put("user",user);
		}else{
			List<ReporteeManger> reporteemanger=null;
			reporteemanger=new ArrayList<ReporteeManger>();
		    String reporteemanger_QRY = "";
		    reporteemanger_QRY= "SELECT CONVERT(IFNULL(IS_MANAGER,''),CHAR),CONVERT(IFNULL(MP.EMPLOYEESEQUENCENO,''),CHAR) EMPID,IFNULL(MPRO.EMAIL,''),CONVERT(IFNULL(ST.STATUS,''),CHAR) CODE,IFNULL(ST.NAME,'') FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS MM  ON MM.EMPLOYEEID=A.EMPLOYEEID LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY MP ON MM.MANAGERID=MP.EMPLOYEEID  LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_CONTACT MPRO  ON MPRO.EMPLOYEEID=MP.EMPLOYEEID LEFT JOIN HCLHRM_PROD.tbl_status_codes ST ON  ST.STATUS=MP.STATUS LEFT JOIN (select if(count(*)>0,'Y','N') IS_MANAGER,IFNULL(A.EMPLOYEEID,0) EMPLOYEEID from HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS A join hclhrm_prod.tbl_employee_primary b on b.employeeid=A.managerid where B.employeesequenceno in ("+object.getString("userName")+") LIMIT 1) YY ON 1=1 where A.EMPLOYEESEQUENCENO="+object.getString("userName")+"" ;
		         List<Object[]> Experience_Obj = entityManager.createNativeQuery(reporteemanger_QRY).getResultList();
		         for (Object temp[] : Experience_Obj) {
		        	 ReporteeManger obj1 = new ReporteeManger(temp[0].toString(),temp[1].toString(),temp[2].toString(),temp[3].toString(),temp[4].toString());
		        	 reporteemanger.add(obj1);
		         } 
				
		         
		         
		      // User Track
		      mstUserRepository.usertrack(object.getString("userName"),"login",object.getString("application"));
							 
		         
			//System.out.println("111");
			String PWDStr = "WELCOME TO ICONNECT";
			String PWDMsg = PWDStr.replaceAll(" ", "%20");
			response.put("status",1);
			response.put("user",user);
			response.put("Manger",reporteemanger);
			
		}
		return response;
	}
	
	
	
	@PostMapping("templogin")
	public LinkedHashMap<String, Object> usertemplogin(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		LoginUser user = null;
		System.out.println("Login"+UPLOADED_FOLDER);
		user = mstUserRepository.findByUsernameandpasswordTemp(object.getString("userName"), "" + object.get("password"));
		if(user== null){
			  String msg = "PLEASE CHECK THE ENTERED USER CREDENTIALS.";
			  response.put("status",0);
			  response.put("user",user);
		}else{
			List<ReporteeManger> reporteemanger=null;
			reporteemanger=new ArrayList<ReporteeManger>();
		    String reporteemanger_QRY = "";
		    reporteemanger_QRY= "SELECT CONVERT(IFNULL(IS_MANAGER,''),CHAR),CONVERT(IFNULL(MP.EMPLOYEESEQUENCENO,''),CHAR) EMPID,IFNULL(MPRO.EMAIL,''),CONVERT(IFNULL(ST.STATUS,''),CHAR) CODE,IFNULL(ST.NAME,'') FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS MM  ON MM.EMPLOYEEID=A.EMPLOYEEID LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY MP ON MM.MANAGERID=MP.EMPLOYEEID  LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_CONTACT MPRO  ON MPRO.EMPLOYEEID=MP.EMPLOYEEID LEFT JOIN HCLHRM_PROD.tbl_status_codes ST ON  ST.STATUS=MP.STATUS LEFT JOIN (select if(count(*)>0,'Y','N') IS_MANAGER,IFNULL(A.EMPLOYEEID,0) EMPLOYEEID from HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS A join hclhrm_prod.tbl_employee_primary b on b.employeeid=A.managerid where B.employeesequenceno in ("+object.getString("userName")+") LIMIT 1) YY ON 1=1 where A.EMPLOYEESEQUENCENO="+object.getString("userName")+"" ;
		         List<Object[]> Experience_Obj = entityManager.createNativeQuery(reporteemanger_QRY).getResultList();
		         for (Object temp[] : Experience_Obj) {
		        	 ReporteeManger obj1 = new ReporteeManger(temp[0].toString(),temp[1].toString(),temp[2].toString(),temp[3].toString(),temp[4].toString());
		        	 reporteemanger.add(obj1);
		         } 
				
		         
		         
		      // User Track
		      mstUserRepository.usertrack(object.getString("userName"),"login",object.getString("application"));
							 
		         
			//System.out.println("111");
			String PWDStr = "WELCOME TO ICONNECT";
			String PWDMsg = PWDStr.replaceAll(" ", "%20");
			response.put("status",1);
			response.put("user",user);
			response.put("Manger",reporteemanger);
			
		}
		return response;
	}
	
	
	
	// Employee Experience
	
	@PostMapping("experience")
	public LinkedHashMap<String, Object> Experience(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		 
		List<Experience> experience=null;
		experience=new ArrayList<Experience>();
			
	    String Experience_QRY = "";
	    Experience_QRY= "SELECT CONCAT(SUBSTRING_INDEX(EXPERIENCE/12,'.',1),'.',ROUND((CONCAT('0.',SUBSTRING_INDEX(EXPERIENCE/12,'.',-1)))*12,0)) EXPERIENCE ,EMPLOYERNAME,DESIGNATION FROM  HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY a left join  tbl_employee_experience_details b on a.employeeid=b.employeeid where a.employeesequenceno="+object.getString("empID")+" and b.status=1001 GROUP BY TRANSACTIONID" ;
	         
	         List<Object[]> Experience_Obj = entityManager.createNativeQuery(Experience_QRY).getResultList();
	         for (Object temp[] : Experience_Obj) {
	        	 Experience obj1 = new Experience(temp[0].toString(),temp[1].toString(),temp[2].toString());
	        	 experience.add(obj1);
	         } 
			
	        
		      
	         response.put("experience",experience);
			 
		return response;
	}
	
	
	
	@PostMapping("session")
	public LinkedHashMap<String, Object> session(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		 net.sf.json.JSONObject addobj=new net.sf.json.JSONObject();
		List<Experience> experience=null;
		experience=new ArrayList<Experience>();
		JSONArray add = new JSONArray();
	    String session_QRY = "";
	    session_QRY= "SELECT 'flag' flag,IF(MAX(DATE(ACCESSDATE))=CURDATE()=1,'true','false') SESSIONDATE FROM hclhrm_prod_others.tbl_iconnect_user_history where userid= "+object.getString("empID")+" and application='"+object.getString("application")+"' AND ACCESSDATE >= CURDATE()  GROUP BY USERID ";
	         
	    System.out.println("session_QRY"+session_QRY);
	         List<Object[]> Experience_Obj = entityManager.createNativeQuery(session_QRY).getResultList();
	         for (Object temp[] : Experience_Obj) {
	          	  addobj=new net.sf.json.JSONObject();
	        	  addobj.put("flag", temp[1].toString());
	        	  add.add(addobj);
	         } 
	        System.out.println(add.size()+"****");
	        if(add.size()==0)
	        {
	        	 addobj.put("flag","true");
	        	 add.add(addobj);
	        }
	    response.put("session",add);
		return response;
	}
	
	
	
	
	/// Employee Attendance HYD & Azista & Assam
	 
	@PostMapping("attendance")
	public LinkedHashMap<String, Object> attendance(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		
		JSONArray values = new JSONArray();
		System.out.println(object.toString());
		 
		 
			String fromdate="";
			String todate="";
			String BUID="";
			
			String Common_QRY="";
			 // Payperiod View 
			 if(object.getBoolean("payperiodSwitch")&&!object.getString("payperiod").equalsIgnoreCase("default"))
	    	 {
	 	    	 Common_QRY= "SELECT  BUSINESSUNITID,TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),'Upcoming Month')) MONTHNAME,CONVERT(LEFT(TRANSACTIONDURATION,4),CHAR) YEAR,CONVERT(FROMDATE,CHAR), TODATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1  and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in('"+object.getString("empID")+"')) and TRANSACTIONDURATION='"+object.getString("payperiod")+"' GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12" ;
	    	 }
			 // Monthwise View 
	    	 else if(!object.getBoolean("payperiodSwitch")&&!object.getString("payperiod").equalsIgnoreCase("default"))
	    	 {
	 	    	 Common_QRY= "SELECT  BUSINESSUNITID,TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),'Upcoming Month')) MONTHNAME,CONVERT(LEFT(TRANSACTIONDURATION,4),CHAR) YEAR, CONVERT(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01'),CHAR) FROMDATE, LAST_DAY(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01')) TODATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1  and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in('"+object.getString("empID")+"')) and TRANSACTIONDURATION='"+object.getString("payperiod")+"' GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12;" ;
	    	 } 
			 // Default View 
	    	 else if(object.getBoolean("payperiodSwitch")&&object.getString("payperiod").equalsIgnoreCase("default"))
	    	 {
	    		 /* String FROMDATE_QRY = "";
				 FROMDATE_QRY= "SELECT BUSINESSUNITID,MAX(FROMDATE) 'MAXDATE' FROM hclhrm_prod_others.tbl_iconnect_transaction_dates where transactiontypeid=1 and businessunitid in( SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in("+object.getString("empID")+")) " ;
		         */  
	    		 Common_QRY= "SELECT BUSINESSUNITID,TRANSACTIONDURATION 'PAYPERIOD','MONTHNAME','YEAR',CONVERT(MAX(FROMDATE),CHAR) 'MAXDATE',CURDATE()  TODAYDATE FROM hclhrm_prod_others.tbl_iconnect_transaction_dates where transactiontypeid=1 and businessunitid in( SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in("+object.getString("empID")+")) " ;
	    		 //PAYPERIOD, MONTHNAME, YEAR, FROMDATE, TODATE
	    	 }
			  
			 System.out.println(Common_QRY);
			 
			 List<Object[]> FROMDATE_Obj = entityManager.createNativeQuery(Common_QRY).getResultList();
	         for (Object temp[] : FROMDATE_Obj) {
	        	// temp[0].toString()
	        	
	        	 BUID=temp[0].toString();
	        	 fromdate=temp[4].toString();
	        	 todate=temp[5].toString();
	         } 
			 String comparedateTodayformat="";
		 
			JSONArray colorcodee = new JSONArray();
			Map<String, String> COLORS = new HashMap();
				
		    String colorcode_QRY = "";
		    colorcode_QRY= "SELECT COLORCODE,COLORCODENAME,DISPLAYNAME,CASENAME,DATE_FORMAT(CURDATE(),'%d-%m-%Y') TODAYDATE FROM hclhrm_prod_others.tbl_iconnect_colour_codes where status=1001 ";
		         
		         List<Object[]> Experience_Obj = entityManager.createNativeQuery(colorcode_QRY).getResultList();
		         for (Object temp[] : Experience_Obj) {
		        	// Colorcode obj1 = new Colorcode(temp[0].toString(),temp[1].toString(),temp[2].toString());
		        	  
		        	// String[] arr = { "GeeksforGeeks", "A computer portal" }; 
		        	 
		        	 net.sf.json.JSONObject addobj=new net.sf.json.JSONObject();
		        	 
		        	 addobj.put("colorcode", temp[0].toString());
		        	 addobj.put("colorcodename", temp[1].toString());
		        	 addobj.put("displayname", temp[2].toString()); 
		        	 addobj.put(temp[3].toString(), temp[0].toString());
		        	// addobj.put(temp[1].toString(), temp[0].toString());
		        	 
		        	// System.out.println(temp[0].toString());
		        	 
		        	 colorcodee.add(addobj);
		        	// System.out.println(colorcodee.toString());
		        	 
		        	  COLORS.put(temp[3].toString(), temp[0].toString()+"&"+ temp[2].toString());
		        	// COLORS.put("displayname", temp[2].toString());
		        	// colorcode.add(BeanUtils.populate(obj1,map));
		        	// colorcode.add((arr)Colorcode);
		        	  
		        	 comparedateTodayformat=temp[4].toString();
		         } 
		         
		         
		         
		         System.out.println("CALL");
		         
		         System.out.println(todate+"todate");
		         
		         if(BUID.equalsIgnoreCase("17"))
		         {
		        	 values=Ahemd.proc_with_resultset(object.getString("empID"),fromdate,todate,BUID,COLORS,comparedateTodayformat); 
					  
		         }
		         else if(BUID.equalsIgnoreCase("15")||BUID.equalsIgnoreCase("16")||BUID.equalsIgnoreCase("33")||BUID.equalsIgnoreCase("34"))
		         {
		        	 values=Assam.proc_with_resultset(object.getString("empID"),fromdate,todate,BUID,COLORS,comparedateTodayformat); 
					  
		         }
		         
//		         else if(BUID.equalsIgnoreCase("42"))
//		         {
//		        	 values=Composites.proc_with_resultset(object.getString("empID"),fromdate,todate,BUID,COLORS,comparedateTodayformat); 
//					  
//		         }
		         else
		         {
		        	 // logs
		        	 values=call.proc_with_resultset(object.getString("empID"),fromdate,todate,BUID,COLORS,comparedateTodayformat); 
					  
		         }
		         
		         // User Track
			      mstUserRepository.usertrack(object.getString("empID"),"attendance",object.getString("application"));    
					
		    response.put("colorcode",colorcodee);
			response.put("INOUT",values);

			return response;
	}
	
	
	/// Transactions Based on Joining Employee  
	 
	@PostMapping("transactiondates")
	public LinkedHashMap<String, Object> transactiondates(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
	 
		    List<Payperiod> leavequotaobj = null;
		       
		    leavequotaobj = new ArrayList<Payperiod>();
			
	    	 System.out.println("Payperiod");
	    	 
	    	 
	    	 String Common_QRY="";
	    	 System.out.println(login);
	    	/*  if(object.getBoolean("payperiodSwitch"))
	    	 {
	    		///Payperiod wise 
	 	    	 Common_QRY= "SELECT  TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),'Current Month')) MONTHNAME, LEFT(TRANSACTIONDURATION,4) YEAR,FROMDATE, TODATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1  and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in('"+object.getString("empID")+"')) GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12" ;
	    	 }*/
	    	/* else
	    	 {
	 	    	///Month wise 
	 	    	 Common_QRY= "SELECT  TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),'Current Month')) MONTHNAME, LEFT(TRANSACTIONDURATION,4) YEAR, CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01') FROMDATE, LAST_DAY(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01')) TODATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1  and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in('"+object.getString("empID")+"')) GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12;" ;
	    	 } 
	    	 */
	    	  if(object.getBoolean("payperiodSwitch"))
		    	 {
	    		  ///Common_QRY=  "SELECT  TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),'Upcoming Month')) PAYPERIODMONTHNAME, LEFT(TRANSACTIONDURATION,4) PAYPERIODYEAR,FROMDATE PAYPERIODFROMDATE,DATE_ADD(FROMDATE,INTERVAL DAY(LAST_DAY(DATE_ADD(FROMDATE,INTERVAL 1 MONTH))-1) DAY)  PAYPERIODTODATE, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),  IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1,  MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',  RIGHT(TRANSACTIONDURATION,2),'-','01')),'Upcoming Month')) MONTHNAME,  LEFT(TRANSACTIONDURATION,4) YEAR, CONCAT(LEFT(TRANSACTIONDURATION,4),'-',  RIGHT(TRANSACTIONDURATION,2),'-01')  FROMDATE,  LAST_DAY(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01')) TODATE  FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1 and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY  where employeesequenceno in('"+object.getString("empID")+"'))  GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12";
	    	 
	    		//  Common_QRY=  "SELECT  TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',  RIGHT(TRANSACTIONDURATION,2),'-','01')))) PAYPERIODMONTHNAME, LEFT(TRANSACTIONDURATION,4) PAYPERIODYEAR,FROMDATE PAYPERIODFROMDATE,DATE_ADD(FROMDATE,INTERVAL DAY(LAST_DAY(DATE_ADD(FROMDATE,INTERVAL 1 MONTH))-1) DAY)  PAYPERIODTODATE, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),  IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1,  MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',  RIGHT(TRANSACTIONDURATION,2),'-','01')),MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',  RIGHT(TRANSACTIONDURATION,2),'-','01')))) MONTHNAME,LEFT(TRANSACTIONDURATION,4) YEAR, CONCAT(LEFT(TRANSACTIONDURATION,4),'-',  RIGHT(TRANSACTIONDURATION,2),'-01')  FROMDATE,  LAST_DAY(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01')) TODATE  FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1 and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY  where employeesequenceno in('"+object.getString("empID")+"'))  GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12 ";
		    		 
	    		  Common_QRY=  "SELECT  TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',  RIGHT(TRANSACTIONDURATION,2),'-','01')))) PAYPERIODMONTHNAME, CONVERT(LEFT(TRANSACTIONDURATION,4), CHAR) PAYPERIODYEAR,FROMDATE PAYPERIODFROMDATE,DATE_ADD(FROMDATE,INTERVAL DAY(LAST_DAY(DATE_ADD(FROMDATE,INTERVAL 1 MONTH))-1) DAY)  PAYPERIODTODATE, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),  IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1,  MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',  RIGHT(TRANSACTIONDURATION,2),'-','01')),MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',  RIGHT(TRANSACTIONDURATION,2),'-','01')))) MONTHNAME,CONVERT(LEFT(TRANSACTIONDURATION,4),CHAR) YEAR, CONVERT(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',  RIGHT(TRANSACTIONDURATION,2),'-01'),CHAR)  FROMDATE,  LAST_DAY(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01')) TODATE  FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1 and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY  where employeesequenceno in('"+object.getString("empID")+"'))  GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12 ";
	    		  
		    	 }
	    	   
	    	 
	             
	    	  
	         List<Object[]> LEAVEQUOTA_Obj = entityManager.createNativeQuery(Common_QRY).getResultList();
	         for (Object temp[] : LEAVEQUOTA_Obj) {
	        	 
	        	 
	        	 Payperiod obj1 = new Payperiod(temp[0].toString(),temp[1].toString(),temp[2].toString(),temp[3].toString(),temp[4].toString(),temp[5].toString(),temp[6].toString(),temp[7].toString(),temp[8].toString());
	        	 leavequotaobj.add(obj1);
	         } 
	         
	        
	        
			response.put("Payperiod",leavequotaobj);
		 
		return response;
	}
	
	
	/// LeaveQuota Employee
	
	@PostMapping("leavequota")
	public LinkedHashMap<String, Object> leavequota(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
	 
		    List<LeaveQuota> leavequotaobj = null;
		    
		    leavequotaobj = new ArrayList<LeaveQuota>();
			
	    	 System.out.println("Leave Quota");
			/// USED QUOTA
	    	 String LEAVEQUOTA_QRY = "";
	    	 LEAVEQUOTA_QRY= "SELECT le.NAME NAME,a.quantity QUANTITY,a.AVAILABLEQTY AVAILQTY,a.usedqty USEDQTY,le.SHORTNAME LEAVETYPE FROM hclhrm_prod_others.tbl_emp_leave_quota a left join hclhrm_prod.tbl_leave_type le on le.leavetypeid=a.leavetypeid where a.employeeid in  (SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno in("+object.getString("empID")+" )) and a.status in(1001,1401) " ;
	          
	         List<Object[]> LEAVEQUOTA_Obj = entityManager.createNativeQuery(LEAVEQUOTA_QRY).getResultList();
	         for (Object temp[] : LEAVEQUOTA_Obj) {
	        	 LeaveQuota obj1 = new LeaveQuota(temp[0].toString(),Double.parseDouble(temp[1].toString()),Double.parseDouble(temp[2].toString()),Double.parseDouble(temp[3].toString()),temp[4].toString());
	        	 leavequotaobj.add(obj1);
	         } 
	         
	         
	         // User Track
		      mstUserRepository.usertrack(object.getString("empID"),"leavequota",object.getString("application"));    
	         
			response.put("LeaveQuota",leavequotaobj);
		 
		return response;
	}
	
	/// Login Count
	
	@PostMapping("todaylogintime")
	public LinkedHashMap<String, Object> todaylogintime(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
	 
		 
		List<String> Todaytime=null;
	       /// Today Login
	         String DATE_QRY = "";
	         String logintime_QRY = "";
	         DATE_QRY= "SELECT  DATE_FORMAT(CURDATE(), '%Y-%m-%d')  TODAYDATE FROM DUAL;" ;
	         
	         List<String> DATE_Obj = entityManager.createNativeQuery(DATE_QRY).getResultList();
	         
	        System.out.println(DATE_Obj.get(0));
	        
			 if(DATE_Obj.get(0)!=null)
			 {
				 logintime_QRY= "select IF ( DATE_FORMAT(HH.DATEOFJOIN,'%Y-%m-%d') >'"+DATE_Obj.get(0)+"','--',IFNULL(MAX(CASE WHEN a.DATEON='"+DATE_Obj.get(0)+"' THEN IF(a.DAY_STATUS='P' , concat(a.ATT_IN,'~',a.ATT_OUT,'~',if(xc.req_type='AR','AR',ifnull(b.leave_type,'--'))) , if(a.DAY_STATUS='A' AND a.DAYTYPE!='HL',if(dayname(a.DATEON)='Sunday',IF(b.leave_type is null,'WOFF~A',concat(b.leave_type,'~',b.MANAGER_STATUS)),concat(b.leave_type,'~',b.MANAGER_STATUS)), if(a.DAYTYPE='HL', IF(b.leave_type is null,concat(a.DAYTYPE,'~A'),concat(b.leave_type,'~',b.MANAGER_STATUS)) ,a.DAY_STATUS))) END),'A') ) AS '"+DATE_Obj.get(0)+"'     from hclhrm_prod.tbl_employee_primary K   left join test_mum.tbl_att_leave_in_out_status_report a on a.employeeid=k.employeesequenceno  left join hclhrm_prod_others.tbl_emp_attn_req xc on xc.employeeid=k.employeesequenceno  and a.dateon=xc.REQ_DATE and xc.req_type='AR'  left join hclhrm_prod_others.tbl_emp_leave_report B ON b.employeeid=a.employeeid AND a.dateon=b.leaveon and   b.MANAGER_STATUS in ('A','P')   LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON k.COMPANYID=BU.BUSINESSUNITID    LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON K.EMPLOYEEID=DD.EMPLOYEEID    LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID=DEP.DEPARTMENTID      LEFT JOIN HCLADM_PROD.TBL_COSTCENTER G ON K.COSTCENTERID=G.COSTCENTERID       LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE HH ON K.EMPLOYEEID=HH.EMPLOYEEID      LEFT JOIN hclhrm_prod.tbl_status_codes ST ON ST.STATUS=K.STATUS       where K.employeesequenceno in("+object.getString("empID")+")   and a.dateon between       '"+DATE_Obj.get(0)+"' and '"+DATE_Obj.get(0)+"'    group by a.employeeid" ; 
				
				 Todaytime = entityManager.createNativeQuery(logintime_QRY).getResultList();
			 }
	        
			 System.out.println(logintime_QRY);
			 System.out.println(Todaytime.toString().length()+"Length-----");
			 
			 String formart="";
			 
			 if(Todaytime.toString().length()==2)
			 {
				 formart="A";
			 }
			 else
			 {
				 formart=Todaytime.get(0);
			 }
			 
			
			
			String intime="";
			String outtime="";
			
		   String DisplayFlag="T";
		   
			
			if(formart.equalsIgnoreCase("A"))
			{
				intime="00:00:00";
				outtime="00:00:00";
			}
			else
			{
				
				System.out.println(formart);
				intime=formart.split("~")[0];
				
				System.out.println(formart.split("~").length+"~|Length");
				outtime=formart.split("~")[1];
				
				
				if(outtime.equalsIgnoreCase("A")||outtime.equalsIgnoreCase("P"))
				{
					DisplayFlag="L";
				}
				else
				{
					//Same time
					if(intime.equalsIgnoreCase(outtime))
					{
						outtime="00:00:00";
					}
					
				}
				
			}
      	  
			     
			 // response.put("todaylogintime",Todaytime);
			  response.put("displayflag",DisplayFlag);
			  response.put("intime",intime);
			  response.put("outtime",outtime);
			  
			//  
		return response;
	}
	
	//select name from hcllcm_prod.tbl_location where locationtype=2 and status=1001 and name like '%KAV%' limit 100;
	
	@PostMapping("locationsearch")
	public LinkedHashMap<String, Object> locationsearch(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
	 
		    
		    JSONArray locations = new JSONArray();
	    	 
			/// USED QUOTA
	    	 String LocationQUOTA_QRY = "";
	    	 LocationQUOTA_QRY= " select LOCATIONID,name from hcllcm_prod.tbl_location where locationtype=2 and status=1001 and name like  '%"+object.getString("locationname")+"%' and status=1001 limit 100 " ;
	          
	         List<Object[]> Location_Obj = entityManager.createNativeQuery(LocationQUOTA_QRY).getResultList();
	         for (Object temp[] : Location_Obj) {
	             net.sf.json.JSONObject addobj=new net.sf.json.JSONObject();
	             addobj.put("name",  temp[1].toString());
	        	 locations.add(addobj);
	         } 
	         
	         
	         
			response.put("locationsearch",locations);
		 
		return response;
	}
	
	
	@PostMapping("todaylogin")
	public LinkedHashMap<String, Object> todaylogin(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
	 
		List<String> TodayLogins=null;
	       /// Today Login
	         String TODAYLOGINS_QRY = "";
	         TODAYLOGINS_QRY= "SELECT COUNT(*) TODAYLOGINS FROM hclhrm_prod_others.tbl_iconnect_usage_monitoring where DATE(ACCESSDATE)=CURDATE();" ;
	         
	          TodayLogins = entityManager.createNativeQuery(TODAYLOGINS_QRY).getResultList();
			  response.put("TodayLogins",TodayLogins);
			 
		return response;
	}
	
	//holoiday list 
	@PostMapping("holidaylist")
	public LinkedHashMap<String, Object> holidaylist(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
	 
		List<Holidaylist> holidaylist = null;
		holidaylist=new ArrayList<Holidaylist>();
	         /// Holiday List
	         String Holidaylist_QRY = ""; 
	        // Holidaylist_QRY= "SELECT b.comments 'DAYNAME',DATE_FORMAT(b.HOLIDAYDATE,'%Y-%m-%d')'DATE' FROM hclhrm_prod.tbl_employee_primary a left join hclhrm_prod.tbl_holidays b on a.companyid=b.businessunitid  where employeesequenceno="+object.getString("empID")+" and b.statusid=1001 and year(b.HOLIDAYDATE)=YEAR(CURDATE()) ORDER BY  b.HOLIDAYDATE asc" ;
	         
//	         Holidaylist_QRY= "SELECT T.*FROM (SELECT b.comments 'DAYNAME',DATE_FORMAT(b.HOLIDAYDATE,'%Y-%m-%d')'DATE' FROM hclhrm_prod.tbl_employee_primary a left join hclhrm_prod.tbl_holidays b on a.companyid=b.businessunitid "
//	         		+ " where employeesequenceno="+object.getString("empID")+" and b.statusid=1001 and year(b.HOLIDAYDATE)=YEAR(CURDATE()) UNION ALL  "
//	         		+ " SELECT t.comments 'DAYNAME',DATE_FORMAT(t.HOLIDAYDATE,'%Y-%m-%d')'DATE' FROM test.tbl_emp_saturday_policy st "
//	         		+ " left join hclhrm_prod.tbl_employee_primary pp on pp.employeesequenceno=st.employee_seq "
//	         		+ " left join test.tbl_holidays_special t on  pp.companyid=t.businessunitid where pp.employeesequenceno="+object.getString("empID")+" and t.statusid=1001 "
//	         		+ " and year(t.HOLIDAYDATE)=YEAR(CURDATE()) )AS T ORDER BY T.DATE ASC  ";
//	         
	         
	         
	           Holidaylist_QRY = "SELECT T.* FROM ( "
                     + "    SELECT hd.comments AS 'DAYNAME', "
                     + "           DATE_FORMAT(hd.holidaydate, '%Y-%m-%d') AS 'DATE' "
                     + "    FROM hclhrm_prod.tbl_employee_primary p "
                     + "    JOIN hclhrm_prod.tbl_employee_professional_details info ON info.employeeid = p.employeeid "
                     + "    JOIN hcllcm_prod.tbl_location A ON A.LOCATIONID = info.worklocationid "
                     + "    JOIN hcllcm_prod.tbl_location b ON A.parent = b.locationid AND b.locationtype = 1 "
                     + "    JOIN hclhrm_prod.tbl_holiday_location_mapping map ON b.locationid = map.stateid "
                     + "        AND map.BUSINESSUNITID = P.companyid "
                     + "    JOIN hclhrm_prod.tbl_holidays hd ON map.holidayid = hd.holidayid "
                     + "    WHERE hd.statusid = 1001 "
                     + "      AND YEAR(hd.holidaydate) = YEAR(CURDATE()) "
                     + "      AND p.employeesequenceno = :empID "
                     + "    UNION ALL "
                     + "    SELECT hd.comments AS 'DAYNAME', "
                     + "           DATE_FORMAT(hd.holidaydate, '%Y-%m-%d') AS 'DATE' "
                     + "    FROM hclhrm_prod.tbl_employee_primary p "
                     + "    JOIN hclhrm_prod.tbl_holidays hd ON hd.businessunitid = p.companyid "
                     + "    LEFT JOIN hclhrm_prod.tbl_holiday_location_mapping map ON map.holidayid = hd.holidayid "
                     + "    WHERE hd.statusid = 1001 "
                     + "      AND YEAR(hd.holidaydate) = YEAR(CURDATE()) "
                     + "      AND p.employeesequenceno = :empID "
                     + "      AND map.holidayid IS NULL "
                     + "      AND NOT EXISTS ( "
                     + "          SELECT 1 "
                     + "          FROM hclhrm_prod.tbl_employee_primary p2 "
                     + "          JOIN hclhrm_prod.tbl_employee_professional_details info2 ON info2.employeeid = p2.employeeid "
                     + "          JOIN hcllcm_prod.tbl_location A2 ON A2.LOCATIONID = info2.worklocationid "
                     + "          JOIN hcllcm_prod.tbl_location b2 ON A2.parent = b2.locationid AND b2.locationtype = 1 "
                     + "          JOIN hclhrm_prod.tbl_holiday_location_mapping map2 ON b2.locationid = map2.stateid "
                     + "              AND map2.BUSINESSUNITID = P2.companyid "
                     + "          JOIN hclhrm_prod.tbl_holidays hd2 ON map2.holidayid = hd2.holidayid "
                     + "          WHERE hd2.statusid = 1001 "
                     + "            AND YEAR(hd2.holidaydate) = YEAR(CURDATE()) "
                     + "            AND p2.employeesequenceno = :empID "
                     + "      ) "
                     + "    UNION ALL "
                     + "    SELECT t.comments AS 'DAYNAME', "
                     + "           DATE_FORMAT(t.HOLIDAYDATE, '%Y-%m-%d') AS 'DATE' "
                     + "    FROM test.tbl_emp_saturday_policy st "
                     + "    LEFT JOIN hclhrm_prod.tbl_employee_primary pp ON pp.employeesequenceno = st.employee_seq "
                     + "    LEFT JOIN test.tbl_holidays_special t ON pp.companyid = t.businessunitid "
                     + "    WHERE pp.employeesequenceno = :empID "
                     + "      AND t.statusid = 1001 "
                     + "      AND YEAR(t.HOLIDAYDATE) = YEAR(CURDATE()) "
                     + ") AS T "
                     + "ORDER BY T.DATE ASC";

//Set the empID parameter safely
Query query = entityManager.createNativeQuery(Holidaylist_QRY);
query.setParameter("empID", object.getString("empID"));

//Execute the query and process the results
List<Object[]> Holidaylist_Obj = query.getResultList();
for (Object[] temp : Holidaylist_Obj) {
 Holidaylist obj2 = new Holidaylist(temp[0].toString(), temp[1].toString());
 holidaylist.add(obj2);
}
	         //holidaylist=null;
	         // User Track
		      mstUserRepository.usertrack(object.getString("empID"),"holidaylist",object.getString("application"));    
			response.put("Holidaylist",holidaylist);
			return response;
	}
	
	/// all Birthday List
	@PostMapping("birthdaylist")
	public LinkedHashMap<String, Object> birthdaylist(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
	 
		List<Birthdaylist> birthdaylist=null;
		
		birthdaylist=new ArrayList<Birthdaylist>();
		
		  
	       /// Birthday List
	         String Birthdaylist_QRY = "";
	         //Birthdaylist_QRY= "SELECT  A.employeesequenceno EMPID,A.CALLNAME ENAME,E.NAME,ifnull(C.NAME,'NA') DEPARTMENT,ifnull(D.EMAIL,'NA')  Email,ifnull(D.MOBILE,'') Mobile ,DATEOFBIRTH,GEN.GENDER 'GENDERID',DATE_FORMAT(A.DATEOFBIRTH,'%m-%d') 'BIRTHDAYDATEFORMAT',DATE_FORMAT(A.DATEOFBIRTH,'%b') FILTERMONTH ,DAYNAME(CONCAT(YEAR(CURDATE()),'-',DATE_FORMAT(A.DATEOFBIRTH,'%m-%d'))) DAYNAME,IFNULL(CONCAT('iconnectpics/', PRO.employeeid,'/',PRO.filename),0) PROFILEPIC  FROM  hclhrm_prod.tbl_employee_primary A  LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS B ON A.EMPLOYEEID=B.EMPLOYEEID  LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT C ON B.DEPARTMENTID=C.DEPARTMENTID  LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_CONTACT D ON A.EMPLOYEEID=D.EMPLOYEEID  LEFT JOIN hcladm_prod.tbl_businessunit E ON E.BUSINESSUNITID=A.COMPANYID  JOIN(  select A.DEPARTMENTID DEPARTMENTID,b.companyid companyid  from  HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS A  left join hclhrm_prod.tbl_employee_primary b  	on a.employeeid=b.employeeid  where b.employeesequenceno in("+object.getString("empID")+")  ) xy on xy.DEPARTMENTID=B.DEPARTMENTID AND xy.companyid=A.companyid  LEFT JOIN HCLADM_PROD.tbl_gender GEN ON A.GENDER=GEN.GENDER   LEFT JOIN hclhrm_prod_others.tbl_iconnect_profile_pics PRO ON PRO.EMPLOYEEID=A.employeesequenceno WHERE A.STATUS=1001  and DAYNAME(CONCAT(YEAR(CURDATE()),'-',DATE_FORMAT(A.DATEOFBIRTH,'%m-%d'))) IS NOT NULL order by MONTH(A.DATEOFBIRTH) , DATE_FORMAT(A.DATEOFBIRTH,'%d');" ;
	         Birthdaylist_QRY= "SELECT  A.employeesequenceno EMPID,A.CALLNAME ENAME,E.NAME,ifnull(C.NAME,'NA') DEPARTMENT,ifnull(D.EMAIL,'NA')  Email,ifnull(D.MOBILE,'') Mobile ,DATEOFBIRTH,GEN.GENDER 'GENDERID',DATE_FORMAT(A.DATEOFBIRTH,'%m-%d') 'BIRTHDAYDATEFORMAT',DATE_FORMAT(A.DATEOFBIRTH,'%b') FILTERMONTH ,DAYNAME(CONCAT(YEAR(CURDATE()),'-',DATE_FORMAT(A.DATEOFBIRTH,'%m-%d'))) DAYNAME,CONVERT(IFNULL(CONCAT('iconnectpics/', PRO.employeeid,'/',PRO.filename),0),CHAR) PROFILEPIC  FROM  hclhrm_prod.tbl_employee_primary A  LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS B ON A.EMPLOYEEID=B.EMPLOYEEID  LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT C ON B.DEPARTMENTID=C.DEPARTMENTID  LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_CONTACT D ON A.EMPLOYEEID=D.EMPLOYEEID  LEFT JOIN hcladm_prod.tbl_businessunit E ON E.BUSINESSUNITID=A.COMPANYID LEFT JOIN HCLADM_PROD.tbl_gender GEN ON A.GENDER=GEN.GENDER   LEFT JOIN hclhrm_prod_others.tbl_iconnect_profile_pics PRO ON PRO.EMPLOYEEID=A.employeesequenceno WHERE A.STATUS=1001 and a.employeesequenceno not  in(10447)  and DAYNAME(CONCAT(YEAR(CURDATE()),'-',DATE_FORMAT(A.DATEOFBIRTH,'%m-%d'))) IS NOT NULL order by MONTH(A.DATEOFBIRTH) , DATE_FORMAT(A.DATEOFBIRTH,'%d');" ;
	          
	         List<Object[]> Birthdaylist_obj = entityManager.createNativeQuery(Birthdaylist_QRY).getResultList();
	         for (Object temp[] : Birthdaylist_obj) {
	        	 Birthdaylist obj3 = new Birthdaylist(temp[0].toString(),temp[1].toString(),temp[2].toString(),temp[3].toString(),temp[4].toString(),temp[5].toString(),temp[6].toString(),temp[7].toString(),temp[8].toString(),temp[9].toString(),temp[10].toString(),temp[11].toString());
	        	 birthdaylist.add(obj3);
	         } 
	         // User Track
		      mstUserRepository.usertrack(object.getString("empID"),"birthdaylist",object.getString("application"));    
			
	         response.put("Birthdaylist",birthdaylist);
	         
		return response;
	}
	  
	// Employee Department View
	@PostMapping("department")
	public LinkedHashMap<String, Object> department(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		 
		List<Department> department=null;
		department=new ArrayList<Department>();
		department=new ArrayList<Department>();
		
	       /// Department List
	         String Department_info_QRY = "";
	         Department_info_QRY= "SELECT A.employeesequenceno EMPID,LOWER(A.CALLNAME) EMPNAME,LOWER(E.NAME) BUNAME,C.CODE Department,trim(IFNULL(F.name,'NA')) designation,IFNULL(trim(D.EMAIL),'') EMAIL,IFNULL(D.MOBILE,'') MOBILE,C.CODE,A.GENDER GENDERID,CONVERT(IFNULL(CONCAT('iconnectpics/', PRO.employeeid,'/',PRO.filename),0),CHAR) PROFILEPIC FROM hclhrm_prod.tbl_employee_primary A LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS B ON A.EMPLOYEEID=B.EMPLOYEEID LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT C ON B.DEPARTMENTID=C.DEPARTMENTID LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_CONTACT D ON A.EMPLOYEEID=D.EMPLOYEEID LEFT JOIN hcladm_prod.tbl_businessunit E ON E.BUSINESSUNITID=A.COMPANYID LEFT JOIN hcladm_prod.tbl_designation F ON B.designationid=F.designationid  LEFT JOIN hclhrm_prod_others.tbl_iconnect_profile_pics PRO ON PRO.EMPLOYEEID=A.employeesequenceno WHERE A.STATUS=1001 and C.DEPARTMENTID= (select DEPARTMENTID from HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS where employeeid in( select employeeid from hclhrm_prod.tbl_employee_primary where employeesequenceno in ("+object.getString("empID")+") )) and  a.companyid in(select companyid from hclhrm_prod.tbl_employee_primary where employeesequenceno in ("+object.getString("empID")+"))" ;
	         
	         List<Object[]> Department_info_obj = entityManager.createNativeQuery(Department_info_QRY).getResultList();
	         
	        // System.out.println(Department_info_obj.lastIndexOf(0)+"^^^^^^^&^^^^^^^^");
	         for (Object temp[] : Department_info_obj) {
	        	 
	        //	 System.out.println(temp[0].toString());
	        	 
	        	  
	        		 Department obj4 = new Department(temp[0].toString(),temp[1].toString(),temp[2].toString(),temp[3].toString(),temp[4].toString(),temp[5].toString(),temp[6].toString(),temp[7].toString(),temp[8].toString(),temp[9].toString());
		        	 department.add(obj4);
	        	 
	        	
	         } 
	         
	        
	         
	         // User Track
		      mstUserRepository.usertrack(object.getString("empID"),"department",object.getString("application"));   
		      
			response.put("department",department);
			 
		return response;
	}
	 
	// Employee Leave Summary 
	@PostMapping("leavesummary")
	public LinkedHashMap<String, Object> leavesummary(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		
		List<LeaveSummary> leaveSummary=null;
		
	
		leaveSummary=new ArrayList<LeaveSummary>();
	         
	         String LeaveSummary_QRY = "";
	         LeaveSummary_QRY= "SELECT A.REQ_DATE APPLIEDDATE,A.FROM_DATE,A.TO_DATE, lev.LEAVE_COUNT_BT_DAYS DAYS, LEV.LEAVE_TYPE, concat(A.MESSAGE,' & Subject:',A.SUBJECT) MESSAGE , IF(A.MAIL_STATUS='S','Sent',IF(A.MAIL_STATUS='F','Failed','Processing')) MAIL_STATUS, IF(FLAG='P','Pending',if(FLAG='A','APPROVED',if(FLAG='SC','SELF CANCELLED BY EMPLOYEE:', IF(FLAG='MC','CANCELLED BY YOUR MANAGER', IF(FLAG='R','REJECTED','UNKNOWN'))))) FLAG,IFNULL(A.COMMENTS,'0') COMMENTS,A.RID RID_R,A.FLAG FLAG_S, IF(DATE_FORMAT(A.FROM_DATE,'%Y-%m-%d')<=CURDATE(),1,0) DTFLAG ,IFNULL(A.APPROVEDNAME,0) APPROVEDNAME FROM HCLHRM_PROD_OTHERS.TBL_EMP_ATTN_REQ A LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY B ON A.EMPLOYEEID=B.EMPLOYEESEQUENCENO LEFT JOIN ( SELECT LEAVE_COUNT_BT_DAYS, LEAVE_TYPE,RID FROM hclhrm_prod_others.tbl_emp_leave_report GROUP BY RID) LEV ON LEV.RID=A.RID WHERE REQ_TYPE!='AR' AND A.EMPLOYEEID='"+object.getString("empID")+"' AND A.FLAG in('A','R','P','MC') and A.RID>73 and A.status=1001 and  A.REQ_DATE>=DATE_ADD(CURDATE(),INTERVAL -1 YEAR) GROUP BY A.RID  ORDER BY A.REQ_DATE DESC " ;
		        
	         List<Object[]> LeaveSummary_obj = entityManager.createNativeQuery(LeaveSummary_QRY).getResultList();
	         
	         for (Object temp[] : LeaveSummary_obj) {
	        	  LeaveSummary obj5 = new LeaveSummary(temp[0].toString(),temp[1].toString(),temp[2].toString(),temp[3].toString(),temp[4].toString(),temp[5].toString(),temp[6].toString(),temp[7].toString(),temp[8].toString(),temp[9].toString(),temp[10].toString(),temp[11].toString(),temp[12].toString());
	        	 leaveSummary.add(obj5); 
	        	// System.out.println(temp[8].toString());
	         } 
	         // User Track
		      mstUserRepository.usertrack(object.getString("empID"),"leavesummary",object.getString("application")); 
		      
		      
	         response.put("LeaveSummary",leaveSummary); 
	         
		return response;
	}

	
	
	// Employee Leave cancel
  @PostMapping("LeaveSelfCancel")
	public int LeaveSelfCancel(@RequestBody String Leave) throws JSONException {
		//LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(Leave);
		
		System.out.println(object.toString());
	 	int user = 0;
		 user = mstUserRepository.findbySelfCancel(object.getString("reasonComment"),object.getString("empID"),object.getString("empName"),object.getString("rid_R"));
		 System.out.println(user);
		 
		return user;
	} 
	
  //// Manager Leave Accept
  @PostMapping("Leaveaccept")
 	public int Leaveaccept(@RequestBody String Leave) throws JSONException {
 		//LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
 		JSONObject object = new JSONObject(Leave);
 		System.out.println(object.toString());
 		int user = 0;
 		try {
 	 		 user = mstUserRepository.findbyLeaveaccept(object.getString("managereqflag"),object.getString("reasonComment"),object.getString("LOGINID"),object.getString("LOGINNAME"),object.getString("id"),object.getString("rid"));
 		}
 		catch(Exception e)
 		{
 			try {
				  Thread.sleep(200);
				 user = mstUserRepository.findbyLeaveaccept(object.getString("managereqflag"),object.getString("reasonComment"),object.getString("LOGINID"),object.getString("LOGINNAME"),object.getString("id"),object.getString("rid"));
			} catch (Exception e1) {
				try {
					  Thread.sleep(200);
					  user = mstUserRepository.findbyLeaveaccept(object.getString("managereqflag"),object.getString("reasonComment"),object.getString("LOGINID"),object.getString("LOGINNAME"),object.getString("id"),object.getString("rid"));
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				e1.printStackTrace();
			}
 		}
 		// System.out.println(user);
 		return user;
 	}
  
   	
  // Manager Approvals 
  @PostMapping("managersummary")
	public LinkedHashMap<String, Object> Managersummary(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		
		List<MangerLeaveSummary> managerleavesummary=null;
		List<MangerAttendanceReq> mangerattendanceReq=null;
		
		managerleavesummary=new ArrayList<MangerLeaveSummary>();
		mangerattendanceReq=new ArrayList<MangerAttendanceReq>();
	         
	         String managers_QRY = "";
	        // managers_QRY= "	SELECT A.EMPLOYEESEQUENCENO ID,A.CALLNAME NAME,E.NAME DEPT,B.SUBJECT,  CONCAT(DATE_FORMAT(B.FROM_DATE,'%d-%m-%Y'),'--',DATE_FORMAT(B.TO_DATE,'%d-%m-%Y')) DURATION,   IFNULL(C.TOTA_DAYS,DATEDIFF(B.TO_DATE,B.FROM_DATE)+1) DAYS,C.LEAVE_TYPE,    IF(B.FLAG='A','Approved',IF(B.FLAG='R','Rejected','Pending')) Manager_Status,B.RID,  B.FLAG,B.MESSAGE,txn.TODATE,   if(DATE_FORMAT(B.FROM_DATE,'%Y-%m-%d')<'2019-01-01' ,1,0) BUTTACT   FROM  HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A  LEFT JOIN HCLHRM_PROD_OTHERS.TBL_EMP_ATTN_REQ B ON A.EMPLOYEESEQUENCENO=B.EMPLOYEEID   LEFT JOIN HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_REQ C ON B.RID=C.RID    LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON A.EMPLOYEEID=D.EMPLOYEEID  join(    select p.employeesequenceno EmpCode,p.companyid    from hclhrm_prod.tbl_employee_primary p    LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON d.EMPLOYEEID=p.EMPLOYEEID  LEFT JOIN hclhrm_prod.tbl_employee_primary H ON D.managerid=H.EMPLOYEEID    where  h.employeesequenceno='"+object.getString("empID")+"'  )x on x.EmpCode=A.employeesequenceno  join(   SELECT max(PAYPERIOD) payperiod,businessunitid  FROM  hclhrm_prod.tbl_businessunit_payroll_process  group by businessunitid  )y on 1=1 and y.businessunitid=x.companyid  join (  select TODATE,businessunitid ,  transactionduration,transactiontypeid from hclhrm_prod_others.tbl_iconnect_transaction_dates  where transactiontypeid=1  )txn  on txn.businessunitid=y.businessunitid and  txn.transactionduration=y.payperiod and txn.transactiontypeid=1  LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT E ON D.DEPARTMENTID=E.DEPARTMENTID  WHERE  B.REQ_TYPE='LR'   and  B.EMPLOYEEID!='"+object.getString("empID")+"'  and  B.FLAG in('P','A') AND B.EMPLOYEEID is not null  AND B.FROM_DATE >txn.TODATE order by B.RID" ;
		      
	         managers_QRY= "SELECT A.EMPLOYEESEQUENCENO ID,A.CALLNAME NAME,E.NAME DEPT,B.SUBJECT, CONCAT(DATE_FORMAT(B.FROM_DATE,'%d-%m-%Y'),'<--->',DATE_FORMAT(B.TO_DATE,'%d-%m-%Y')) DURATION, IFNULL(C.TOTA_DAYS,DATEDIFF(B.TO_DATE,B.FROM_DATE)+1) DAYS,C.LEAVE_TYPE, IF(B.FLAG='A','Approved',IF(B.FLAG='R','Rejected','Pending')) Manager_Status,B.RID,B.FLAG,B.MESSAGE,txn.TODATE, if(DATE_FORMAT(B.FROM_DATE,'%Y-%m-%d')<'2019-01-01' ,1,0) BUTTACT FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A LEFT JOIN HCLHRM_PROD_OTHERS.TBL_EMP_ATTN_REQ B ON A.EMPLOYEESEQUENCENO=B.EMPLOYEEID JOIN HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_REQ C ON B.RID=C.RID  AND B.REQ_TYPE=C.REQ_TYPE and C.EMPLOYEEID=B.EMPLOYEEID LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON A.EMPLOYEEID=D.EMPLOYEEID join( select p.employeesequenceno EmpCode,p.companyid from hclhrm_prod.tbl_employee_primary p LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON d.EMPLOYEEID=p.EMPLOYEEID LEFT JOIN hclhrm_prod.tbl_employee_primary H ON D.managerid=H.EMPLOYEEID where  h.employeesequenceno='"+object.getString("empID")+"' )x on x.EmpCode=A.employeesequenceno join( SELECT MAX(TRANSACTIONDURATION) payperiod, BUSINESSUNITID FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES WHERE TRANSACTIONTYPEID=1 and BUSINESSUNITID in (SELECT companyid from hclhrm_prod.tbl_employee_primary where employeesequenceno='"+object.getString("empID")+"') )y on 1=1 join ( select FROMDATE AS TODATE,businessunitid ,transactionduration,transactiontypeid from HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1 )txn on txn.businessunitid=y.businessunitid and txn.transactionduration=y.payperiod and txn.transactiontypeid=1 LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT E ON D.DEPARTMENTID=E.DEPARTMENTID WHERE  B.REQ_TYPE='LR' and C.REQ_TYPE='LR' and  B.EMPLOYEEID!='"+object.getString("empID")+"' and  B.FLAG in('P','A') AND B.EMPLOYEEID is not null AND date_format(B.FROM_DATE,'%Y-%m-%d') >= txn.TODATE and date_format(C.FROM_DATE,'%Y-%m-%d') >= txn.TODATE order by B.FLAG desc ";
	         
	          System.out.println(managers_QRY);
			List<Object[]> managersummary_obj = entityManager.createNativeQuery(managers_QRY).getResultList();
	         
	         for (Object temp[] : managersummary_obj) {
	        	 MangerLeaveSummary obj5 = new MangerLeaveSummary(temp[0].toString(),temp[1].toString(),temp[2].toString(),temp[3].toString(),temp[4].toString(),temp[5].toString(),temp[6].toString(),temp[7].toString(),temp[8].toString(),temp[9].toString(),temp[10].toString(),temp[11].toString(),temp[12].toString());
	        	 managerleavesummary.add(obj5); 
	        	// System.out.println(temp[8].toString());
	         } 
	         
	         String mangerattendanceReq_QRY = "";
	         mangerattendanceReq_QRY= " SELECT A.EMPLOYEESEQUENCENO ID,A.CALLNAME NAME,E.NAME DEPT,B.SUBJECT,   CONCAT(B.FROM_DATE,'--',B.TO_DATE)   DURATION,  B.TOTA_HOURS NET_HOURS,IF(B.FLAG='A','Approved',IF(B.FLAG='R','Rejected','Pending'))Manager_Status,B.RID,B.FLAG,   B.MESSAGE,B.REQ_DATE FROM   HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A   LEFT JOIN HCLHRM_PROD_OTHERS.TBL_EMP_ATTN_REQ B ON A.EMPLOYEESEQUENCENO=B.EMPLOYEEID   LEFT JOIN HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_REQ C ON B.RID=C.RID   LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON A.EMPLOYEEID=D.EMPLOYEEID   join(select p.employeesequenceno EmpCode,p.companyid from hclhrm_prod.tbl_employee_primary p   LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON d.EMPLOYEEID=p.EMPLOYEEID   LEFT JOIN hclhrm_prod.tbl_employee_primary H ON   D.managerid=H.EMPLOYEEID where  h.employeesequenceno='"+object.getString("empID")+"')x on x.EmpCode=A.employeesequenceno   join(SELECT max(PAYPERIOD) payperiod,businessunitid  FROM   hclhrm_prod.tbl_businessunit_payroll_process group by businessunitid)y on 1=1 and y.businessunitid=x.companyid join (select TODATE,businessunitid ,transactionduration,transactiontypeid from hclhrm_prod_others.tbl_iconnect_transaction_dates where transactiontypeid=1)txn on txn.businessunitid=y.businessunitid and txn.transactionduration=y.payperiod and txn.transactiontypeid=1        LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT E ON D.DEPARTMENTID=E.DEPARTMENTID         WHERE  B.REQ_TYPE='AR' and  B.EMPLOYEEID!='"+object.getString("empID")+"' and  B.FLAG in('P','A') AND B.EMPLOYEEID is not null AND B.REQ_DATE >= txn.TODATE order by B.FLAG desc" ;
	         
			List<Object[]> mangerattendanceReq_obj = entityManager.createNativeQuery(mangerattendanceReq_QRY).getResultList();
	         
	         for (Object temp[] : mangerattendanceReq_obj) {
	        	 MangerAttendanceReq obj5 = new MangerAttendanceReq(temp[0].toString(),temp[1].toString(),temp[2].toString(),temp[3].toString(),temp[4].toString(),temp[5].toString(),temp[6].toString(),temp[7].toString(),temp[8].toString(),temp[9].toString(),temp[10].toString());
	        	 mangerattendanceReq.add(obj5); 
	        	// System.out.println(temp[8].toString());
	         } 
	         
	         // User Track
		      mstUserRepository.usertrack(object.getString("empID"),"managersummary",object.getString("application")); 
		      
	         response.put("managerleavesummary",managerleavesummary); 
	         response.put("managerattendancereq",mangerattendanceReq); 
	         
		return response;
	}
	 
  /// Employee Raise Attenadnce Request.
	    @PostMapping("attrequest")
		public int attrequest(@RequestBody String attrequest) throws JSONException {
			//LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			JSONObject object = new JSONObject(attrequest);
			
			Random rand = new Random();
			int nRand = rand.nextInt(90000) + 10000;
			
			System.out.println(object.getString("EMPLOYEEID")+"----"+object.getString("reasonComment"));
			System.out.println(object.getString("FROM_DATE")+"----"+object.getString("TO_DATE"));
			System.out.println(object.getString("TO_EMAIL"));
		 
			   int user = 0;
			  user = mstUserRepository.findbyattrequest(
					  object.getString("EMPLOYEEID"),object.getString("ReqType"),object.getString("REQ_DATE"),
					  object.getString("reasonComment"),String.valueOf(nRand),object.getString("FROM_DATE"),object.getString("TO_DATE"),
					  object.getString("netHours"),object.getString("TO_EMAIL"),object.getString("TOTA_HOURS"));
			 System.out.println(user); 
			 
			
			return user;
		}  
	    
	    
	    ////  PROFILE PIC
	    
	     @PostMapping("profilepic")
	    public int  uploadFileMulti(@RequestParam("file") MultipartFile uploadfiles,@RequestParam("empID") String empID) throws IOException {
	        //  logger.debug("Multiple file upload!");
	        // Get file name
	    	  System.out.println("i am in " +  uploadfiles.getOriginalFilename());
	    	  
	    	//  System.out.println(getFileExtension(uploadfiles.getContentType()));
	    	  
	    	  
	    	   
	    	 boolean flag=uploadFile(uploadfiles,empID);
	    	 int user = 0;
	    	 if(flag)
	    	 {
	    		 
	    		// String fullpath=UPLOADED_FOLDER+empID+"\\"+uploadfiles.getOriginalFilename();
	    		 
	    		 //System.out.println(fullpath);
	    		 
	    		 System.out.println(uploadfiles.getOriginalFilename()+"GETNAME");
	    		 
	    		 user = mstUserRepository.profilepic(empID,UPLOADED_FOLDER,uploadfiles.getOriginalFilename(),"");
	    	 }
	    	 // User Track
		     // mstUserRepository.usertrack(empID,"profilepic",object.getString("application")); 
	        return user;
	    } 
	    
	     @PostMapping("changepassword")
		    public int  changepassword(@RequestBody String data) throws IOException {
	    	 JSONObject object = new JSONObject(data);
	  		
	  		 System.out.println(object.toString());
	  	 	 int user = 0;
	  		 user = mstUserRepository.changepassword(object.getString("confrmPassword"),object.getString("empID"));
	  		 System.out.println(user);
	  		 // User Track
		      mstUserRepository.usertrack(object.getString("empID"),"changepassword",object.getString("application")); 
	  	     return user;
		    } 
	     public boolean uploadFile(MultipartFile file,String empID) throws FileSystemException {
	    	 boolean flag=false;
	        try {
	        	
	         //Path path = Paths.get(new File(UPLOADED_FOLDER+"/"+empID).mkdir() + file.getOriginalFilename());
	         
	        // System.out.println(File.separator);
	        	
	        	new File(UPLOADED_FOLDER+"/"+empID).mkdir();
	        	
	           Path copyLocation = Paths
	                .get(UPLOADED_FOLDER+"/"+empID+File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
	           
	           System.out.println(copyLocation);
	            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING); 
	            
	            flag=true;
	            
	        } catch (Exception e) {
	        	//flag=false;
	            e.printStackTrace();
	            throw new FileSystemException("Could not store file " + file.getOriginalFilename()
	                + ". Please try again!");
	        }
			return flag;
	    } 
	     
	     
	 	 
	 	
	 	
	 	@PostMapping("profilepicview")
		public LinkedHashMap<String, Object> profilepicview(@RequestBody String login) throws JSONException {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			JSONObject object = new JSONObject(login);
			 
	         String Profilepicview=mstUserRepository.findByProfilepicview(object.getString("empID"));
	         
	         if(Profilepicview==null)
	         {
	        	 Profilepicview="0";
	         }
		         response.put("profilepicview",Profilepicview); 
		         
			return response;
		}
	 	  
	 	@PostMapping("hrdocuments")
			public LinkedHashMap<String, Object> hrdocuments(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				JSONObject object = new JSONObject(login);
				
				//String Sql="SELECT DISPLAYNAME, PDF_FILE, IMAGE_FILE FROM hclhrm_prod_others.tbl_hr_documents where  location in(SELECT BU.CALLNAME LOCATION FROM hclhrm_prod.tbl_employee_primary pp  left join hcladm_prod.tbl_businessunit bu on bu.businessunitid=pp.companyid where pp.employeesequenceno="+object.getString("empID")+" AND PP.STATUS=1001)";
				String Sql="SELECT DISPLAYNAME, PDF_FILE, IMAGE_FILE FROM hclhrm_prod_others.tbl_hr_documents where  location in(SELECT BU.CALLNAME LOCATION FROM hclhrm_prod.tbl_employee_primary pp  left join  hcladm_prod.tbl_businessunit bu on bu.businessunitid=pp.companyid  where pp.employeesequenceno="+object.getString("empID")+" AND PP.STATUS=1001) UNION ALL SELECT DISPLAYNAME, PDF_FILE, IMAGE_FILE FROM hclhrm_prod_others.tbl_hr_documents where  location in(SELECT IF(BU.BUSINESSUNITID IN (15,16,33,34), 'ASSAM', BU.CODE) LOCATION FROM hclhrm_prod.tbl_employee_primary pp  left join  hcladm_prod.tbl_businessunit bu on bu.businessunitid=pp.companyid  where pp.employeesequenceno="+object.getString("empID")+" AND PP.STATUS=1001)"
						+ "UNION ALL " + 
						"SELECT DISPLAYNAME, PDF_FILE, IMAGE_FILE\r\n" + 
						"FROM hclhrm_prod_others.tbl_hr_documents\r\n" + 
						"WHERE location IN (\r\n" + 
						"    SELECT IF(BU.BUSINESSUNITID in(15,16,33,34),'ASSAM','') AS LOCATION\r\n" + 
						"    FROM hclhrm_prod.tbl_employee_primary PP\r\n" + 
						"    LEFT JOIN hcladm_prod.tbl_businessunit BU ON BU.businessunitid = PP.companyid\r\n" + 
						"    WHERE PP.employeesequenceno = "+object.getString("empID")+" AND PP.STATUS = 1001\r\n" + 
						")";
				 List<HrDocuments> Documents=null;
					
				 Documents=new ArrayList<HrDocuments>();
		          
				 List<Object[]> hrdocuments = entityManager.createNativeQuery(Sql).getResultList();
		         for (Object temp[] : hrdocuments) {
		        	 HrDocuments obj5 = new HrDocuments(temp[0].toString(),HR_DOCUMENTS+"PDF//"+temp[1].toString(),HR_DOCUMENTS+"IMG//"+temp[2].toString());
		        	 Documents.add(obj5); 
		        	
		         } 
			    response.put("hrdocuments",Documents); 
				return response;
			}
	 	
	 	@PostMapping("appraisal")
		public LinkedHashMap<String, Object> appraisal(@RequestBody String login) throws JSONException {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			JSONObject object = new JSONObject(login);
			 
	         String appraisal=mstUserRepository.appraisal(object.getString("empID"));
	          
		         response.put("appraisal",appraisal); 
		         
			return response;
		}
	 	
	 	
	 	@PostMapping("announcement")
		public LinkedHashMap<String, Object> accoucement(@RequestBody String login) throws JSONException {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			JSONObject object = new JSONObject(login);
			
			
			String Sql="SELECT a.NAME annoucement,'iconnect-2.0 New Version is Live Now!' defaultname FROM hclhrm_prod_others.tbl_bu_annoucement a left join hcladm_prod.tbl_businessunit b on a.businessunitid=b.businessunitid where b.businessunitid="+object.getString("buid")+"";
			
			//JSONArray accoucement = new JSONArray();
			ArrayList<String> list=new ArrayList<String>();
			 
	          List<Object[]> Experience_Obj = entityManager.createNativeQuery(Sql).getResultList();
	         for (Object temp[] : Experience_Obj) {
	        	
	        	  //net.sf.json.JSONObject addobj=new net.sf.json.JSONObject();
	        	 
	        	// addobj.put("defaultname", "iconnect-2.0 New Version is Live Now!");
	        	// addobj.put("announcement", temp[0].toString());
	        	  
	        	// accoucement.add(addobj);
	        	 list.add(temp[0].toString());
	        	 list.add(temp[1].toString());
	        	
	         } 
	        
		         response.put("announcement",list); 
		         
			return response;
		}
	 	
	  

	 	  @GetMapping("currentdate")
	 	    public LinkedHashMap<String, Object> getCurrentDate() {
	 	        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

	 	        // Query to fetch the current date
	 	        String sql = "SELECT NOW() AS CURRENTDATE";

	 	        // Execute the query for the current date (fetching a single result directly)
	 	        List<Object> result = entityManager.createNativeQuery(sql).getResultList();

	 	        // Extract the current date (which is returned as a Timestamp)
	 	        Timestamp currentTimestamp = result.isEmpty() ? null : (Timestamp) result.get(0);

	 	        // Convert Timestamp to String with a custom format if not null
	 	        String currentDate = null;
	 	        if (currentTimestamp != null) {
	 	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 	            currentDate = sdf.format(new Date(currentTimestamp.getTime()));
	 	        }

	 	        // Add the formatted current date to the response
	 	        response.put("currentDate", currentDate);

	 	        return response;
	 	    }
 
}

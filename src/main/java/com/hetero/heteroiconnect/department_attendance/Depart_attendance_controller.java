package com.hetero.heteroiconnect.department_attendance;

import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;
  
 
@RestController
@RequestMapping("/department_attendance")
public class Depart_attendance_controller {
	      
	@PersistenceContext
	EntityManager entityManager;
	 
     @Autowired
     call_function_department call; 
     
     @Autowired
     Call_offisto_attendance calloff; 
      
	@PostMapping("manager_attendance")
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
	 	    	 Common_QRY= "SELECT  BUSINESSUNITID,TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),'Current Month')) MONTHNAME, CONVERT(LEFT(TRANSACTIONDURATION,4),CHAR) YEAR,CONVERT(FROMDATE,CHAR), TODATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1  and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in('"+object.getString("empID")+"')) and TRANSACTIONDURATION='"+object.getString("payperiod")+"' GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12" ;
	    	 }
			 // Monthwise View 
	    	 else if(!object.getBoolean("payperiodSwitch")&&!object.getString("payperiod").equalsIgnoreCase("default"))
	    	 {
	 	    	 Common_QRY= "SELECT  BUSINESSUNITID,TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),'Current Month')) MONTHNAME,CONVERT(LEFT(TRANSACTIONDURATION,4),CHAR) YEAR, CONVERT(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01'),CHAR) FROMDATE, LAST_DAY(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01')) TODATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1  and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in('"+object.getString("empID")+"')) and TRANSACTIONDURATION='"+object.getString("payperiod")+"' GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12;" ;
	    	 } 
			 // Default View 
	    	 else if(object.getBoolean("payperiodSwitch")&&object.getString("payperiod").equalsIgnoreCase("default"))
	    	 {
	    		 /* String FROMDATE_QRY = "";
				 FROMDATE_QRY= "SELECT BUSINESSUNITID,MAX(FROMDATE) 'MAXDATE' FROM hclhrm_prod_others.tbl_iconnect_transaction_dates where transactiontypeid=1 and businessunitid in( SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in("+object.getString("empID")+")) " ;
		         */  
	    		 Common_QRY= "SELECT BUSINESSUNITID,TRANSACTIONDURATION 'PAYPERIOD','MONTHNAME','YEAR',MAX(CONVERT(FROMDATE,CHAR)) 'MAXDATE',CURDATE()  TODAYDATE FROM hclhrm_prod_others.tbl_iconnect_transaction_dates where transactiontypeid=1 and businessunitid in( SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in("+object.getString("empID")+")) " ;
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
			 
			 
		         System.out.println("CALL");
		         
		         System.out.println(todate+"todate");
		         
		         values=call.Mangeratt(object.getString("empID"),fromdate,todate);
		        	
		        	 response.put("manager_attendance",values);

			return response;
	}
	
	
	
	
	@PostMapping("officesisto_manager_attendance")
	public LinkedHashMap<String, Object> offisoattendance(@RequestBody String login) throws Exception {
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
	 	    	 Common_QRY= "SELECT  BUSINESSUNITID,TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),'Current Month')) MONTHNAME,CONVERT(LEFT(TRANSACTIONDURATION,4),CHAR) YEAR,CONVERT(FROMDATE,CHAR), TODATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1  and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in('"+object.getString("empID")+"')) and TRANSACTIONDURATION='"+object.getString("payperiod")+"' GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12" ;
	    	 }
			 // Monthwise View 
	    	 else if(!object.getBoolean("payperiodSwitch")&&!object.getString("payperiod").equalsIgnoreCase("default"))
	    	 {
	 	    	 Common_QRY= "SELECT  BUSINESSUNITID,TRANSACTIONDURATION PAYPERIOD, IF(transactionduration<DATE_FORMAT(now(),'%Y%m'), MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')), IF(SUM(IF(transactionduration=DATE_FORMAT(now(),'%Y%m'),'1','0'))=1, MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-','01')),'Current Month')) MONTHNAME, CONVERT(LEFT(TRANSACTIONDURATION,4),CHAR) YEAR, CONVERT(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01'),CHAR) FROMDATE, LAST_DAY(CONCAT(LEFT(TRANSACTIONDURATION,4),'-',RIGHT(TRANSACTIONDURATION,2),'-01')) TODATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where transactiontypeid=1  and businessunitid in(SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in('"+object.getString("empID")+"')) and TRANSACTIONDURATION='"+object.getString("payperiod")+"' GROUP BY TRANSACTIONDURATION ORDER BY transactionduration desc limit 12;" ;
	    	 } 
			 // Default View 
	    	 else if(object.getBoolean("payperiodSwitch")&&object.getString("payperiod").equalsIgnoreCase("default"))
	    	 {
	    		 /* String FROMDATE_QRY = "";
				 FROMDATE_QRY= "SELECT BUSINESSUNITID,MAX(FROMDATE) 'MAXDATE' FROM hclhrm_prod_others.tbl_iconnect_transaction_dates where transactiontypeid=1 and businessunitid in( SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in("+object.getString("empID")+")) " ;
		         */  
	    		 Common_QRY= "SELECT BUSINESSUNITID,TRANSACTIONDURATION 'PAYPERIOD','MONTHNAME','YEAR',MAX(CONVERT(FROMDATE,CHAR)) 'MAXDATE',CURDATE()  TODAYDATE FROM hclhrm_prod_others.tbl_iconnect_transaction_dates where transactiontypeid=1 and businessunitid in( SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where employeesequenceno in("+object.getString("empID")+")) " ;
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
			 
			 
		         System.out.println("CALL");
		         
		         System.out.println(todate+"todate");
		         
		         values=calloff.Mangeratt(object.getString("empID"),fromdate,todate);
		        	
		        	 response.put("manager_attendance",values);

			return response;
	}
	
 
}

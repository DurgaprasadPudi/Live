package com.hetero.heteroiconnect.leavemanagement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;

@RestController
@RequestMapping("/leavemanagement")
public class LeavemangementController {
 
	    @PersistenceContext
	 	EntityManager entityManager;
	    
	    @Autowired
	    Leaveapply leave;
	    
	    @Autowired
	    LeavequotaRepositry reposity;
	    
		@Autowired
	 	private JdbcTemplate jdbcTemplate;
	    
	    
		 	@PostMapping("Leavetypes")
			public LinkedHashMap<String, Object> Leavetypes(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				
				 
				 List<Leavemangement> leavetypes = null;
			       
				 leavetypes = new ArrayList<Leavemangement>();
				
				 String MAXDATE="SELECT BUSINESSUNITID,MAX(TRANSACTIONDURATION) 'PAYPERIOD','MONTHNAME','YEAR', MAX(FROMDATE) 'MAXDATE',CURDATE()  TODAYDATE FROM hclhrm_prod_others.tbl_iconnect_transaction_dates where transactiontypeid=1 and businessunitid in( SELECT COMPANYID FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY where  employeesequenceno in("+object.getString("empID")+"))";
				  
				 
				 String fromdate="";
				 
				 
				 System.out.println(MAXDATE);
				 
				 List<Object[]> FROMDATE_Obj = entityManager.createNativeQuery(MAXDATE).getResultList();
		         for (Object temp[] : FROMDATE_Obj) {
		        	// temp[0].toString()
		        	
		        	// BUID=temp[0].toString();
		        	 fromdate=temp[4].toString();
		        	 //todate=temp[5].toString();
		         } 
				 
				 String Leavetypes="select C.employeesequenceno,trim(A.SHORTNAME) SHORTNAME, B.quantity ,if(trim(A.SHORTNAME)='EL' AND B.AVAILABLEQTY<3, 0 ,B.AVAILABLEQTY) AVAILABLEQTY,B.AVAILABLEQTY AVAILABLEQTY1 , B.HOLD, B.quantity+B.HOLD as totalavl, b.USEDQTY, trim(A.NAME) Fullname,B.DAYMODE,if(B.daymode=0,if(B.availableqty<=B.maxleave,if(trim(A.SHORTNAME)='EL' AND B.AVAILABLEQTY<3,0,B.AVAILABLEQTY),B.maxleave),B.maxleave) MAXLEAVE_C ,B.COUNT_WOFF,  if('"+fromdate+"'=now(),0,if('"+fromdate+"' < now(),datediff(now(),'"+fromdate+"'),if('"+fromdate+"' > now(),4,0))) as bkdays , B.BACKDATE from  hclhrm_prod.tbl_leave_type A,   hclhrm_prod_others.tbl_emp_leave_quota B,  hclhrm_prod.tbl_employee_primary C  where B.employeeid=C.employeeid and C.employeesequenceno in("+object.getString("empID")+") and  B.Leavetypeid=A.Leavetypeid  and b.status=1001";
				 
				 System.out.println(Leavetypes);
				 List<Object[]> Leavetypes_Obj = entityManager.createNativeQuery(Leavetypes).getResultList();
		         for (Object temp[] : Leavetypes_Obj) {
		        	// temp[0].toString()
		        	//3;; 9;;;
		        	 
		        	   // DAY MODE="0","1" ,Available!=0;
		        	  // DAY MODE="0" ,Available!=0;
		        	// Dn't Add Select Options
		        	 
		        	 //((temp[9].toString().equalsIgnoreCase("0")&&!temp[3].toString().equalsIgnoreCase("0")))||
		        	 
		        	 System.out.println(temp[3].toString()+"AVAli----**---DayMode"+temp[9].toString());
		        	
		        	if(temp[9].toString().equalsIgnoreCase("1"))
		        	{
		        		 Leavemangement le=new Leavemangement(temp[0].toString(),temp[1].toString(),temp[2].toString(),temp[3].toString(),temp[4].toString(),temp[5].toString(),temp[6].toString(),temp[7].toString(),temp[8].toString(),temp[9].toString(),temp[10].toString(),temp[11].toString(),temp[12].toString(),temp[13].toString());
			        	 leavetypes.add(le);
		        	}
		        	 
		        	else if(temp[9].toString().equalsIgnoreCase("0")&&!temp[3].toString().equalsIgnoreCase("0.0"))
		        	{
		        		 Leavemangement le=new Leavemangement(temp[0].toString(),temp[1].toString(),temp[2].toString(),temp[3].toString(),temp[4].toString(),temp[5].toString(),temp[6].toString(),temp[7].toString(),temp[8].toString(),temp[9].toString(),temp[10].toString(),temp[11].toString(),temp[12].toString(),temp[13].toString());
			        	 leavetypes.add(le);
		        	}
		        	
		           
		         } 
				 
			     response.put("Leavetypes",leavetypes); 
			         
				return response;
			}
		 	
 
		 

		 	public Map<String, Object> getDateValidation(String fromDate, String empId) {
		 	    String sql = "SELECT MAX(d.fromdate) AS FROMDATE, " +
		 	                 "IF(MAX(d.fromdate) <= ?, 'TRUE', 'FALSE') AS DATE_VALID " +
		 	                 "FROM hclhrm_prod_others.tbl_iconnect_transaction_dates d " +
		 	                 "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON p.companyid = d.businessunitid " +
		 	                 "WHERE d.transactiontypeid = 1 AND p.employeesequenceno = ?";

		 	    return jdbcTemplate.queryForMap(sql, fromDate, empId);
		 	}

		 	
		 	@SuppressWarnings("unchecked")
			@PostMapping("eligibleleaves")
			public LinkedHashMap<String, Object> Eligibleleaves(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				
				System.out.println(object);
				
				 net.sf.json.JSONObject addobj=new net.sf.json.JSONObject();
				 /*List<Leavemangement> leavetypes = null;
			       
				 leavetypes = new ArrayList<Leavemangement>();*/
				 
				 JSONArray Values = new JSONArray();
				  
				 String CommonQuery="";
				 
				 
				// SelectedLeaveType!="LOP" && SelectedLeaveType!="OD" && SelectedLeaveType!="WFH"
				 
				 //String Date_Validations
				  
					    Map<String, Object> result = getDateValidation(object.getString("fromdate"), object.getString("empID"));

					    String dateValid = result.get("DATE_VALID") != null ? result.get("DATE_VALID").toString() : "FALSE";
					    String maxFromDate = result.get("FROMDATE") != null ? result.get("FROMDATE").toString() : null;
 
				 if(!object.getString("leavetype").equalsIgnoreCase("LOP")
						 &&!object.getString("leavetype").equalsIgnoreCase("OD")&&!object.getString("leavetype").equalsIgnoreCase("WFH"))
				 
				 { 
					 
					 
				 if(object.getString("leavetype").equalsIgnoreCase("SL"))
				 {
					 CommonQuery="select C.employeesequenceno,DATE_ADD('"+object.getString("fromdate")+"' , INTERVAL if(ifnull(B.MINIMU_LEAVE,0)=0,1, if(B.MINIMU_LEAVE>=round(b.availableqty,0),round(b.availableqty,0),B.MINIMU_LEAVE)  ) -1 DAY ) DATE,  if(B.daymode=0,if(round(b.availableqty,0)<=ifnull(B.MINIMU_LEAVE,0),round(b.availableqty,0),ifnull(B.MINIMU_LEAVE,0)),B.MINIMU_LEAVE)   MINLEAVE  , b.count_woff,b.count_holiday,trim(A.SHORTNAME) SHORTNAME,B.quantity , round(b.availableqty,0) AS availableqty ,  B.HOLD, B.quantity+B.HOLD as totalavl, b.USEDQTY, trim(A.NAME) Fullname,  B.DAYMODE,if(B.daymode=0,if(round(b.availableqty,0)<=B.maxleave,round(b.availableqty,0),B.maxleave),B.maxleave) MAXLEAVE_C ,B.COUNT_WOFF,B.BACKDATE , if(B.daymode=0,if(b.availableqty<=B.maxleave,b.availableqty,B.maxleave),B.maxleave) MAXLEAVE_C1 , if(B.daymode=0,if(b.availableqty<=ifnull(B.MINIMU_LEAVE,0),b.availableqty,ifnull(B.MINIMU_LEAVE,0)),B.MINIMU_LEAVE)   MINLEAVE1 , b.availableqty AS Ablt ,abs(round(b.availableqty,0)-b.availableqty) AS offday,ifnull(B.FOR_MONTH,0) As permonth , (abs(datediff('"+object.getString("fromdate")+"', if(date_format(DATE_ADD('"+object.getString("fromdate")+"',INTERVAL if(B.daymode=0,if(round(b.availableqty,0)<=B.maxleave,round(b.availableqty,0),B.maxleave),B.maxleave) DAY),'%Y-%m-%d')<=now(),  date_format(DATE_ADD('"+object.getString("fromdate")+"',INTERVAL if(B.daymode=0,if(round(b.availableqty,0)<=B.maxleave,round(b.availableqty,0),B.maxleave),B.maxleave) DAY),'%Y-%m-%d')  ,now())    )) +1) as SLdatdiff  , if( date_format(DATE_ADD('"+object.getString("fromdate")+"', INTERVAL 1 DAY ),'%Y-%m-%d')=date_format(now(),'%Y-%m-%d'),2,1) VENUFLG  from  hclhrm_prod.tbl_leave_type A,   hclhrm_prod_others.tbl_emp_leave_quota B,  hclhrm_prod.tbl_employee_primary C  where B.employeeid=C.employeeid and C.employeesequenceno in("+object.getString("empID")+") and  B.Leavetypeid=A.Leavetypeid and  b.status=1001 and A.SHORTNAME='"+object.getString("leavetype")+"' ";
					 
				 }
				 else
				 {
					 CommonQuery="select C.employeesequenceno,DATE_ADD('"+object.getString("fromdate")+"' , INTERVAL if(ifnull(B.MINIMU_LEAVE,0)=0,1, if(B.MINIMU_LEAVE>=round(b.availableqty,0),round(b.availableqty,0),B.MINIMU_LEAVE)  )-1 DAY ) DATE,  if(B.daymode=0,if(round(b.availableqty,0)<=ifnull(B.MINIMU_LEAVE,0),round(b.availableqty,0),ifnull(B.MINIMU_LEAVE,0)),B.MINIMU_LEAVE)   MINLEAVE  , b.count_woff,b.count_holiday,trim(A.SHORTNAME) SHORTNAME,B.quantity , round(b.availableqty,0) AS availableqty ,  B.HOLD, B.quantity+B.HOLD as totalavl, b.USEDQTY, trim(A.NAME) Fullname,  B.DAYMODE,if(B.daymode=0,if(round(b.availableqty,0)<=B.maxleave,round(b.availableqty,0),B.maxleave),B.maxleave) MAXLEAVE_C ,B.COUNT_WOFF,B.BACKDATE , if(B.daymode=0,if(b.availableqty<=B.maxleave,b.availableqty,B.maxleave),B.maxleave) MAXLEAVE_C1 , if(B.daymode=0,if(b.availableqty<=ifnull(B.MINIMU_LEAVE,0),b.availableqty,ifnull(B.MINIMU_LEAVE,0)),B.MINIMU_LEAVE)   MINLEAVE1 , b.availableqty AS Ablt ,abs(round(b.availableqty,0)-b.availableqty) AS offday,ifnull(B.FOR_MONTH,0) As permonth , abs(datediff('"+object.getString("fromdate")+"', now())) SLdatdiff from  hclhrm_prod.tbl_leave_type A,   hclhrm_prod_others.tbl_emp_leave_quota B,  hclhrm_prod.tbl_employee_primary C  where B.employeeid=C.employeeid and C.employeesequenceno in('"+object.getString("empID")+"') and  B.Leavetypeid=A.Leavetypeid and  b.status=1001 and A.SHORTNAME='"+object.getString("leavetype")+"' ";
				 }
				 
				 
				 System.out.println(CommonQuery);
				 
				// double Weak_holiday=0,availableQuantity=0;
				 
				 String BuID="";
				 
				 double availableQuantity=0,holidays=0,Weak_holiday=0,MinLeaveLimit=0, availableQuantity_temp=0,holidays_temp=0,Weak_holiday_temp=0,MinLeaveLimit_temp=0;
					
				 
				 List<Object[]> CommonQuery_Obj = entityManager.createNativeQuery(CommonQuery).getResultList();
		         for (Object temp[] : CommonQuery_Obj) {
		        	 
		        	 
		        	 Weak_holiday=Double.parseDouble(temp[3].toString());
		        	 availableQuantity=Double.parseDouble(temp[13].toString());
		        	  
		        	
		        	  
		        	 addobj.put("availableQuantity", temp[13].toString());
		        	 addobj.put("DATES", temp[1].toString());
		        	 addobj.put("OFFDAY", temp[19].toString());
		        	 addobj.put("ACTUALAVAIL", temp[18].toString());
		        	 addobj.put("Min", temp[17].toString());
		        	 addobj.put("Max", temp[16].toString());
		        	 addobj.put("Quantity", temp[18].toString());
		        	 addobj.put("Shortname", temp[5].toString());
		        	 
		        	 
		        	 
		        	 
		        	 double  SLdatdiff= Double.parseDouble(temp[21].toString()); 
		        	   availableQuantity_temp=Double.parseDouble(temp[16].toString());	 
						       
						if(object.getString("leavetype").equalsIgnoreCase("SL") && availableQuantity_temp <= SLdatdiff){
							
							addobj.put("SL_MX_LEAVE", availableQuantity_temp);
							 
						}else if(object.getString("leavetype").equalsIgnoreCase("SL") && availableQuantity_temp >= SLdatdiff){
							
							addobj.put("SL_MX_LEAVE", SLdatdiff); 
						}else{
							
							addobj.put("SL_MX_LEAVE", SLdatdiff); 
						}
		        	  
						
		         } 
		         
		         
		       
		         
		         if(Weak_holiday>0){
		        	  
		        	 int looplimit=0;
		        	 double workingdates=0;
		        	 int interval=0;
		        	
		        	 double Incr=availableQuantity-1;
		        	 for(int i=0;i<=5;i++)		
		        	 {	
		        		 StringBuffer holidaycount=new StringBuffer();
		        		 double inceloop=Incr+i;
		        		 holidays=0;
		        		 workingdates=0;
		        		    
		        		 
		        		    if(BuID.equalsIgnoreCase("151") ||BuID.equalsIgnoreCase("161") ){
		        		         holidaycount.append(" select selected_date,IF(DAYNAME(selected_date)='Saturday','WOFF','WDAY') DAYTYPE,  ");
		        		    }else{
		        		    	 holidaycount.append(" select selected_date,IF(DAYNAME(selected_date)='SUNDAY','WOFF','WDAY') DAYTYPE,  ");
		        		    }
		        		    
		        		    if(BuID.equalsIgnoreCase("151") ||BuID.equalsIgnoreCase("161") ){ 
		        			holidaycount.append(" IF(IF(DAYNAME(selected_date)='Saturday','WOFF','WDAY')='WDAY',1,0) STATUS,V2.holidaydate, ");
		        			holidaycount.append(" if(DAYNAME(selected_date)='Saturday' && selected_date=V2.holidaydate,1,if(DAYNAME(selected_date)='Saturday',1,if(selected_date=V2.holidaydate,1,0))) AS HW ");
		        		    }else{
		        		    	holidaycount.append(" IF(IF(DAYNAME(selected_date)='SUNDAY','WOFF','WDAY')='WDAY',1,0) STATUS,V2.holidaydate, ");
		        				holidaycount.append(" if(DAYNAME(selected_date)='SUNDAY' && selected_date=V2.holidaydate,1,if(DAYNAME(selected_date)='SUNDAY',1,if(selected_date=V2.holidaydate,1,0))) AS HW ");
		        			  
		        		    	
		        		    }
		        			
		        			holidaycount.append(" FROM ");
		        			holidaycount.append(" (select adddate('1970-01-01',t4.i*10000 + t3.i*1000 + t2.i*100 + t1.i*10 + t0.i) selected_date from ");
		        			holidaycount.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0, ");
		        			holidaycount.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, ");
		        			holidaycount.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, ");
		        			holidaycount.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3, ");
		        			holidaycount.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v ");
		        			holidaycount.append(" left join( ");
		        			holidaycount.append(" select holidaydate from hclhrm_prod.tbl_holidays ");
		        			holidaycount.append(" where statusid=1001 and businessunitid in(select companyid from hclhrm_prod.tbl_employee_primary ");
		        			holidaycount.append(" where employeesequenceno in('"+object.getString("empID")+"')) ");
		        			holidaycount.append(" )V2 on V2.holidaydate=V.selected_date ");
		        			holidaycount.append(" where selected_date between '"+object.getString("fromdate")+"' and ");
		        			holidaycount.append("( SELECT '"+object.getString("fromdate")+"' +INTERVAL ("+inceloop+" ) DAY) ");
		        			
		        			System.out.println("holidaycount ::"+holidaycount.toString());
		        			System.out.println(workingdates +"holidaycount loop ::"+inceloop);
		        			
		        			
		        			 
				        	 List<Object[]> holidaycount_Obj = entityManager.createNativeQuery(holidaycount.toString()).getResultList();
					          
		     				  for (Object temp[] : holidaycount_Obj) {
		     					  
		     					 holidays=holidays+Double.parseDouble(temp[4].toString());
		          				if(Double.parseDouble(temp[4].toString())==0){
		          				   workingdates=workingdates+1;
		          				}
		          			
		     					  
		     				  }
		     				  
		     				 if(availableQuantity==workingdates){
			        				break;
			        			 }
		        		 
		        		  
		             }  // for loop closed
		        	 
		        	 /// Close At Weak_holiday
		        	 
		         }
		         
		         

					 double finalcount=availableQuantity+holidays;
					 
					 addobj.put("availableQuantity", finalcount);
					// addobj.put("todayDateFlag",""+todayDateFlag+""); 
		         
		 	}
				 
				 else
				 {
					 
					 
					 
/*
			   			Res=(ResultSet)DataObj.FetchData_Emp_DOB("select if(date_format('"+todaydate+"','%Y-%m-%d')=date_format(now(),'%Y-%m-%d'),0,1 ) validflag from dual ", "date&serverDateValidation", Res ,conn);
			   			
			   			//Res=(ResultSet)DataObj.FetchData_Emp_DOB("select if(date_format(now(),'%Y-%m-%d')=date_format(now(),'%Y-%m-%d'),0,1 ) validflag from dual ", "date&serverDateValidation", Res ,conn);
			   	   		
			   			
			   			if(Res.next()){
			   				
			   				if(Res.getInt("validflag")==0){
			   				 todayDateFlag=true;
			   				
			   				}else{
			   					todayDateFlag=false;
			   				}

			   			}
					 */
					 
					    addobj.put("availableQuantity", "0");
				 		addobj.put("Atten_Req_Message", "No Data");
				 		addobj.put("DATES",  object.getString("fromdate"));
				 		addobj.put("OFFDAY", "0");
				 		addobj.put("ACTUALAVAIL", "0");
				 		addobj.put("SL_MX_LEAVE",0); 
				 		addobj.put("todayDateFlag","false"); 
				 }
		         
		      
		         
		         
		         Values.add(addobj);
				 
		         response.put("Datevalidations",dateValid); 
		         response.put("StartDate",maxFromDate); 
			     response.put("eligibleleaves",Values); 
			         
				return response;
			}
		 	
		 @PostMapping(value = "/applyleave",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
		 	        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
			public LinkedHashMap<String, Object> Applyleave(@RequestBody Leave login) throws JSONException, SQLException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				//org.json.JSONObject object = new org.json.JSONObject(login);
				Map hm=new HashMap();
				//Leave leavebean=new Leave();
				//leavebean.set
				
				//System.out.println(login.getLeave_Type()+"----"+login.getFrom_date());
				
				hm=leave.ApplyLeave(login);
				 
				  response.put("leaveapply",hm); 
				  return response;
				}
		 
		 
			 
			@PostMapping("empinfo")
			public LinkedHashMap<String, Object> Empinfo(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
				 JSONArray Values = new JSONArray();
				  
				 
				 
				 Values=reposity.EmpInfo(object.getString("userid"));
				 
				 response.put("info", Values);
				return response;
			}
			
			@PostMapping("emptransaction")
			public LinkedHashMap<String, Object> Emptransaction(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
				 String Fromdate="";
				 Fromdate=reposity.emptransaction();
				 
				 response.put("info", Fromdate);
				return response;
			}
			
			
			@PostMapping("empshowleavetypes")
			public LinkedHashMap<String, Object> empshowleavetypes(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
                 JSONArray Values = new JSONArray();
				  
				 Values=reposity.empshowleavetypes(object.getString("userid"));
				 
				 response.put("empshowleavetypes", Values);
				return response;
			}
			
			
			@PostMapping("unfreezAttendanceReq")
			public LinkedHashMap<String, Object> UnAttendanceReq(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
                 JSONArray Values = new JSONArray();
				 Values=reposity.UnAttendanceReq(object.getString("userid"));
				 response.put("UnAttendanceReq", Values);
				return response;
			}
			
			@PostMapping("unfreezrequest")
		   	public LinkedHashMap<String, Object> UnfreezRequest(@RequestBody String login) throws Exception {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				
				//net.sf.json.JSONObject main = new net.sf.json.JSONObject();
				
				System.out.println(object);
				
				int count = 0;
				count= reposity.UnfreezRequest(object.getString("date"),object.getString("userid"),object.getString("leavetypeid"),object.getString("empID"));
				  response.put("count",count);
					return response;
			}
			
			@PostMapping("leavequota")
		   	public LinkedHashMap<String, Object> leavequota(@RequestBody String login) throws Exception {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				
				//net.sf.json.JSONObject main = new net.sf.json.JSONObject();
				
				System.out.println(object);
				
				int count = 0;
				count= reposity.UnfreezRequest(object.getString("date"),object.getString("userid"),object.getString("leavetypeid"),object.getString("empID"));
				  response.put("count",count);
					return response;
			}
			
			@PostMapping("unfreez_Att_Req_add")
		   	public LinkedHashMap<String, Object> unfreez_Att_Req_add(@RequestBody String login) throws Exception {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				
				//net.sf.json.JSONObject main = new net.sf.json.JSONObject();
				
				System.out.println(object);
				
				int count = 0;
				count= reposity.unfreez_Att_Req_add(object.getString("userid"),object.getString("datelist"),object.getString("empID"));
				  
				response.put("count",count);
					return response;
			}
			
			
}

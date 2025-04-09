package com.hetero.heteroiconnect.department_attendance;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hetero.heteroiconnect.classes.Attcompre;
import com.hetero.heteroiconnect.classes.Attenance;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
 
@Repository
public class call_function_department {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	JSONArray final_attendance_arr= null;
	JSONObject attendance_arr= null;
	
    JSONObject Daily_30day= null;;
	JSONObject Daily_30day_titles= null;;
	//final_attendance_arr=null;

	
	@SuppressWarnings({ "rawtypes" })
	
	
	public synchronized  JSONArray Mangeratt(String employeeid,String FromDate,String ToDate)
	{
		
		JSONArray values = new JSONArray();
	  	StringBuffer manager_under_employee=new StringBuffer();
	         
        manager_under_employee.append(" select distinct p.employeesequenceno,p.callname,if(ifnull(DD.employeeid,0)>0,1,0) ISMANAGE,p.companyid ");
      	manager_under_employee.append(" from hclhrm_prod.tbl_employee_primary p ");
      	manager_under_employee.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON d.EMPLOYEEID=p.EMPLOYEEID ");
      	manager_under_employee.append(" LEFT JOIN hclhrm_prod.tbl_employee_primary H ON D.managerid=H.EMPLOYEEID ");
      	manager_under_employee.append(" left join hclhrm_prod.tbl_employee_professional_contact cc on cc.employeeid=D.managerid ");
      	manager_under_employee.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS DD ON DD.managerid=P.EMPLOYEEID ");
      	manager_under_employee.append(" where  h.employeesequenceno="+employeeid+" and H.status=1001 and P.STATUS=1001 ");
      	
      	Map datamap=new HashMap();
      	int loop=0;
      	   List<Map<String, Object>>  rows = jdbcTemplate.queryForList(manager_under_employee.toString());
				 
				 for (@SuppressWarnings("rawtypes") Map row : rows) {
					 //row.get("HW").toString()
					 
					 
					    String BUID=row.get("companyid").toString();
	      				String employeesequenceno=row.get("employeesequenceno").toString();
	      				String callname=row.get("callname").toString(); 
	      				String isManager=row.get("ISMANAGE").toString();  
	      				
	      				datamap=attendance(FromDate,  ToDate, BUID,  employeesequenceno,callname ,isManager);
	      			// values=call.proc_with_resultset(object.getString("empID"),fromdate,todate,BUID); 
	      					
	      				
	      				System.out.println("********"+datamap.get("ATT_DATA"));
	      				
	      				values.add(datamap.get("ATT_DATA"));
	      				if(loop>=0){
	      				}
	      				//ATT_MONTHS.put(Res.getString(1), Res.getString(2));
	      				loop++;
					 
				 }
      	 
      		 
      		
		return values;
		
	}
	
	
	
	
	
	@SuppressWarnings({ "unchecked", "unchecked" })
	
	public  synchronized Map attendance(String FromDate, String ToDate, String BuID,  String username,String Callname,String isManager) {
 
		@SuppressWarnings("unused")
		String cur_date="";
		//int total_dedHours=0;
		final_attendance_arr= new JSONArray();
		attendance_arr= new JSONObject();
		
	 	//System.out.println("HAU");

		  Daily_30day= new JSONObject();
		
		 Daily_30day_titles= new JSONObject();
	       
	          Map mp=new HashMap();
	          
	          
	          
	          
	          jdbcTemplate.execute(  
	   	           new CallableStatementCreator() {   
	   	              public CallableStatement createCallableStatement(Connection con) throws SQLException {
	   	            	  
	   	            	  
	   	            	CallableStatement cstmt = con.prepareCall("{call test_mum.Call_Attendance_manager(?,?,?,?)}");
	   	            	
	   	            	//CallableStatement cstmt = con.prepareCall("{CALL test.Call_Attendance(?,?,?,?)}");
	   	            	
	   	            
	   	     		
	   	         	//	cstmt.setInt(1, 20314);
	   	         	
	   	         		//cstmt.setString(2, "2019-11-01"); //"2020-11-09"    2019-12-24  
	   	         		//cstmt.setString(3, "2019-12-28");//"2020-11-15"
	   	         		//cstmt.setInt(4, 11);
	   	         		
	   	         		
	   	         		cstmt.setInt(1, Integer.parseInt(username));
	   	         		  //cstmt.setString(2, "2020-10-22");  //2020-11-25,2020-12-03 , 10508
	   	         			cstmt.setString(2, FromDate); //"2020-11-09"    2019-12-24  
	   	         			cstmt.setString(3, ToDate);//"2020-11-15"
	   	         			cstmt.setInt(4,  Integer.parseInt(BuID));
	   	         			
	   	       			
	   	       			System.out.println(cstmt.toString());
	   	       			
	   	                 return cstmt;
	   	              }   
	   	           }, new CallableStatementCallback() {   
	   	               public Map doInCallableStatement(CallableStatement cstmt) 
	   	            		   throws SQLException, DataAccessException {
	   	            	   
	   	                
	   	            	   double total_dedHours=0;
	   	            		Map FirstObj=new HashMap();
	   	            		Map Min_Obj=new HashMap();   
	   	            	     
	   	            		//Attendance_Calculation attendance_Calculation= new Attendance_Calculation();
	   	           	   ResultSet rs =null;
	   	           		
	   	           		rs =cstmt.executeQuery();  
	   	                // cs.execute();
	   	                  // rs = cs.getResultSet();
	   	           		
	   	           	 
	   					Min_Obj = Attcompre.getattencedata(rs);
	              			FirstObj.putAll(Min_Obj);
	              			rs =null;
	              			
	              			cstmt.getMoreResults();
	              			rs = cstmt.getResultSet();
	              			Min_Obj= Attcompre.getattencedata(rs);
	              			FirstObj.putAll(Min_Obj);
	              			rs =null;
	              			
	              			cstmt.getMoreResults();
	              			rs =cstmt.getResultSet();
	              			Min_Obj= Attcompre.getattencedata(rs);
	              			FirstObj.putAll(Min_Obj);
	              			rs =null;
	              			
	              			cstmt.getMoreResults();
	              			rs = cstmt.getResultSet();
	              			Min_Obj= Attcompre.getattencedata(rs);
	              			FirstObj.putAll(Min_Obj);
	   	           		 
	              			rs =null;
	              			cstmt.getMoreResults();
	              			rs = cstmt.getResultSet();
	              			
	              			int EMPHRS_CONVERT_MINITES=0;
	              			int totalmintes=0;
	              			//System.out.println("FirstObj-->" +FirstObj.toString());
	              			/// ***************** CalClation Part *****************************/
	              			
	              			
	             			  
	              			while(rs.next()) {
	              				try {
	              					double Deduction_Hours=0;
	              				JSONObject attendance_obj= new JSONObject();
	              			
	              				 String leave_approval_status="No Request";
	              				 String attendance_approval_status="No Request";
	              				 
	              				String  DEDCOLOR="";
	              			    String	DAF="none";
	              			    
	              			    String DISPLAYCOLOR="none";
	              			    String DISPLAYNAME="none";
	              			    
	              			   String DISPLAYINOUT="none";
	              			    
	              			    String LABLE="0";
	              				 
	              		    	  String DATEON=rs.getString("dateon");
	              		    	  String DATEON_VIEW=rs.getString("DAYVIEW");
	              		    	  String ACT_IN = rs.getString("ACT_IN");
	              		    	  String ACT_OUT = rs.getString("ACT_OUT");
	              		    	  String att_in = rs.getString("att_in");
	              		    	  String att_out = rs.getString("att_out"); 
	              		    	
	              		    	  String net_hours = rs.getString("ACT_NETTIME");//DAYTYPE  net_hours   ACT_NETTIME
	              		    	  
	              		    	//  System.out.println(net_hours+"....;;;;;;;;;;;;;......."+DATEON);
	              		    	  
	              		    	  String net_hours_disp = rs.getString("net_hours");
	              		    	  
	              		    	  ////ACT_NETTIME
	              		    	  
	              		    	  String DAYTYPE = rs.getString("DAYTYPE");
	              		    	  String employeeid=rs.getString("employeeid");
	              		    	  String flexi=rs.getString("flexi");
	              		    	  
	              		    	 // System.out.println(DAYTYPE +"."+DATEON);
	              		    	  
	              		    	 // System.out.println(FirstObj.get(DATEON+"_leaveon"));
	              		    	  
	              		    	LocalDate today = LocalDate.parse("2023-04-24");
		             			  LocalDate pastDate = LocalDate.parse(DATEON);
		             			  int compareValue = today.compareTo(pastDate);
		             			  
		             			  
	              		  
	              		    	 String half_day=  "";
	              		    	 
	              		    	Object halfDayValue = FirstObj.get(DATEON + "_HALF_DAY");
	               		    	String halfDayResult = halfDayValue != null ? halfDayValue.toString() : "0";
	               		    	
	              		    	if( FirstObj.get(DATEON+"_HOLIDAYDATE")!=null  ){
	              		    		// System.out.println(" DATEON---->HOLIDAY " +FirstObj.get(DATEON+"_HOLIDAYDATE") +">>>>>>>..."+FirstObj.get(DATEON+"_COMMENTS"));
	              		    		 DAYTYPE=FirstObj.get(DATEON+"_COMMENTS").toString();
	              		    		 leave_approval_status="HL";
	              		    	 }
	              		    	else if(DAYTYPE.equalsIgnoreCase("WOFF") && att_in.equalsIgnoreCase("00:00:00") && att_out.equalsIgnoreCase("00:00:00")){
	              		    		 //System.out.println("  woff ** SUNDAY  **  NO  office ....");
	              		    		 leave_approval_status="No Request";
	              		    	}
	              		    	
	              		    	
	              		    	
	              		    	 else if(DAYTYPE.equalsIgnoreCase("WOFF") && att_in!="00:00:00" && att_out!="00:00:00" ) {
	              		    		
	              		    		 leave_approval_status="No Request";
	              		    		 
	              		    	 }else if(FirstObj.get(DATEON+"_LEAVEON")!=null && !FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") && !FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half")){
	              		    		 
	              		    		 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
	              	    				 
	              	    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
	              	    				 
	              	    			 }else{
	              	    			 
	              	    			 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()  ;
	              	    			 }
	              	    			 
	              		    		 
	              		    		 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("A")) {
	              						 leave_approval_status="Approved";
	              						// DEDCOLOR= "#00ffbf";
	              						 DAF=	"none";
	              					 }
	              		    		 else	 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("R")) {
	              						 leave_approval_status="Rejected";
	              						// DEDCOLOR= "#ffb3ff";
	              						 DAF=	"none";
	              					 }
	              		    		 else if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("P")) {
	              						 leave_approval_status="Pending";
	              						 DAF=	"none";
	              					 }
	              					 else {
	              						// leave_approval_status="Pending";
	              						 DAF=	"none";
	              					 }
	              		    		 
	              		    		 
	              		    	 }
	              		    	
	              		    	 else if(DAYTYPE.equalsIgnoreCase("WDAY") && att_in.equalsIgnoreCase("00:00:00") && att_out.equalsIgnoreCase("00:00:00")) {
	              		    		 
	              		    		// System.out.println("  ***************** --- CHECK LEAVE TAKEN OR NOT----  ***************** ");
	              		    		 if( FirstObj.get(DATEON+"_LEAVEON")!=null  ){
	              			    		// System.out.println(" DATEON---->LEAVE " +FirstObj.get(DATEON+"_leaveon") +">>>>>>>..."+FirstObj.get(DATEON+"_leave_type"));
	              			    		 
	              		    			 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
	              		    				 
	              		    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
	              		    				 
	              		    			 }else{
	              		    			 
	              		    			 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()  ;
	              		    			 }
	              		    			 
	              			    		 
	              			    		 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("A")) {
	              	    					 leave_approval_status="Approved";
	              	    					// DEDCOLOR= "#00ffbf";
	              	    					 DAF=	"none";
	              	    				 }
	              			    		 else	 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("R")) {
	              	    					 leave_approval_status="Rejected";
	              	    					// DEDCOLOR= "#ffb3ff";
	              	    					 DAF=	"none";
	              	    				 }
	              			    		 else if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("P")) {
	              	    					 leave_approval_status="Pending";
	              	    					 DAF=	"none";
	              	    				 }
	              	    				 else {
	              	    					// leave_approval_status="Pending";
	              	    					 DAF=	"none";
	              	    				 }
	              			    	 }
	              		    		 
	              		    	 } 
	              		    	 else if(DAYTYPE.equalsIgnoreCase("WDAY") && att_in.equalsIgnoreCase(att_out) && att_out!="00:00:00" && att_in!="00:00:00" && FirstObj.get(DATEON+"_HALF_DAY")==null ){
	              			    		
	              		    		 Deduction_Hours=4;
	              			    		DEDCOLOR=	 "#F7D358";
	              			    		 DAF=	" ";
	              			    		 
	              			    		 //FirstObj.get(DATEON+"_FLAG")==null
	              			    		 
	              			    		 if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("A")) {
	              		    				 leave_approval_status="Approved";
	              		    				 Deduction_Hours=0;
	              		    				 DEDCOLOR= "#00ffbf";
	              		    				 DAF=	"none";
	              	    				 }
	              		    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("P")) {
	              		    				 leave_approval_status="Pending";
	              		    				// Deduction_Hours=4;
	              		    				 DEDCOLOR="#F7D358";
	              		    				 DAF="none";
	              	    				 }
	              		    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("R")) {
	              		    				 leave_approval_status="Rejected";
	              		    				 //Deduction_Hours=4;
	              	    					 DEDCOLOR= "#ffb3ff";
	              	    					 DAF=	"none";
	              	    				 }
	              			    		 
	              			    		 if( FirstObj.get(DATEON+"_LEAVEON")!=null  ){
	              					    		// System.out.println(" DATEON---->LEAVE " +FirstObj.get(DATEON+"_leaveon") +">>>>>>>..."+FirstObj.get(DATEON+"_leave_type"));
	              					    		 
	              				    			 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
	              				    				 
	              				    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
	              				    				 //DAF=	"";
	              				    			 }else{
	              				    			 
	              				    			 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()  ;
	              				    			 DAF=	"none";
	              				    			 }
	              				    			 
	              					    		 
	              					    		 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("A")) {
	              			    					 leave_approval_status="Approved";
	              			    					// DEDCOLOR= "#00ffbf";
	              			    					 //DAF=	"none";
	              			    				 }
	              					    		 else	 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("R")) {
	              			    					 leave_approval_status="Rejected";
	              			    					// DEDCOLOR= "#ffb3ff";
	              			    					// DAF=	"none";
	              			    				 }
	              					    		 else if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("P")) {
	              			    					 leave_approval_status="Pending";
	              			    					// DAF=	"none";
	              			    				 }
	              			    				 else {
	              			    					// leave_approval_status="Pending";
	              			    					// DAF=	"none";
	              			    				 }
	              					    	 }
	              			    		 
	              			    	}
	              		    	
	              		    	 else if(DAYTYPE.equalsIgnoreCase("WDAY") && att_in!="00:00:00" && att_out!="00:00:00"   ) {
	              		    		//&&  !att_in.equalsIgnoreCase(att_out)
	              		    		 
	              		    		// System.out.println("  ***************** --- CALCULATE ATTENDANCE ----  *****************  "+DATEON);
	              		    		 leave_approval_status="No Request";
	              		    		if(FirstObj.get(DATEON+"_HALF_DAY")==null || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("false")) {
	              		    			 System.out.println(att_in+"   ---  HALF_DAY null----  "+FirstObj.get(DATEON+"_FLAG"));
	              		    			 //Deduction_Hours= attendance_Calculation.checkingAttendance(  con , att_in , att_out, net_hours ,DAYTYPE);
	              		    			//Deduction_Hours= Attenance_backup.checkingAttendance(att_in , att_out, net_hours ,DAYTYPE);
	              		    			//Deduction_Hours=Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);
	              		    			 
	              		    			
	              		    			if(BuID.equalsIgnoreCase("42")&&(compareValue < 0||compareValue==0))
	               		    			{
	               		    				System.out.println("Yescall");
	               		    			 Deduction_Hours= Attenance.CompositesAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);
	               		    			 
	               		    			}
	               		    			else
	               		    			{
	               		    			 Deduction_Hours= Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON,halfDayResult);
	               		    			 
	               		    			}
	              		    			
	              		    			 DAF="none";
	              		    			 if(Deduction_Hours>0 && FirstObj.get(DATEON+"_FLAG")==null ) {
	              		    				 DEDCOLOR=	 "#F7D358";
	              		    				 DAF="";
	              		    			 }
	              		    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("A")) {
	              		    				 leave_approval_status="Approved";
	              		    				 Deduction_Hours=0;
	              		    				 DEDCOLOR= "#00ffbf";
	              		    				 DAF=	"none";
	              	    				 }
	              		    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("P")) {
	              		    				 leave_approval_status="Pending";
	              		    				 //Deduction_Hours=0;
	              		    				 DEDCOLOR="#F7D358";
	              		    				 DAF="none";
	              	    				 }
	              		    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("R")) {
	              		    				 leave_approval_status="Rejected";
	              	    					 DEDCOLOR= "#ffb3ff";
	              	    					 DAF=	"none";
	              	    				 }
	              	    				else {
	              	    					 //leave_approval_status="Pending";
	              	    					 DAF=	"none";
	              	    				 }
	              		    			 
	              		    			 if( FirstObj.get(DATEON+"_LEAVEON")!=null  ){
	              					    		// System.out.println(" DATEON---->LEAVE " +FirstObj.get(DATEON+"_leaveon") +">>>>>>>..."+FirstObj.get(DATEON+"_leave_type"));
	              					    		 
	              				    			 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
	              				    				 
	              				    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
	              				    				 //DAF=	"";
	              				    			 }else{
	              				    			 
	              				    			 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()  ;
	              				    			 DAF=	"none";
	              				    			 }
	              				    			 
	              					    		 
	              					    		 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("A")) {
	              			    					 leave_approval_status="Approved";
	              			    					// DEDCOLOR= "#00ffbf";
	              			    					 //DAF=	"none";
	              			    				 }
	              					    		 else	 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("R")) {
	              			    					 leave_approval_status="Rejected";
	              			    					// DEDCOLOR= "#ffb3ff";
	              			    					// DAF=	"none";
	              			    				 }
	              					    		 else if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("P")) {
	              			    					 leave_approval_status="Pending";
	              			    					// DAF=	"none";
	              			    				 }
	              			    				 else {
	              			    					// leave_approval_status="Pending";
	              			    					// DAF=	"none";
	              			    				 }
	              					    	 }
	              		    			 
	              		    			 
	              		    			 
	              		    			 
	              		    			
	              		    		}
	              		    			
	              		    		else if(FirstObj.get(DATEON+"_HALF_DAY")!=null && FirstObj.get(DATEON+"_HALF_DAY").toString()!="false" && DAYTYPE.equalsIgnoreCase("WDAY")){
	              		    			
	              		    			DAYTYPE=FirstObj.get(DATEON+"_HALF_DAY").toString();
	              		    			//Deduction_Hours= attendance_Calculation.checkingAttendance(  con , att_in , att_out, net_hours ,DAYTYPE);
	              		    			//Deduction_Hours= Attenance_backup.checkingAttendance(att_in , att_out, net_hours ,DAYTYPE);
	              		    			///Deduction_Hours=Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);

	               		    			if(BuID.equalsIgnoreCase("42")&&(compareValue < 0||compareValue==0))
	               		    			{
	               		    				System.out.println("Yescall");
	               		    			 Deduction_Hours= Attenance.CompositesAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);
	               		    			 
	               		    			}
	               		    			else
	               		    			{
	               		    			 Deduction_Hours= Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON,halfDayResult);
	               		    			 
	               		    			}
	              		    			
	              		    			
	              		    			
	              	                 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
	              		    				 
	              		    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
	              		    				 
	              		    			 }else{
	              		    			 
	              		    			 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()  ;
	              		    			 }
	              		    			 
	              	                      
	              	                 //******************************* VENU
	              	                 if(Deduction_Hours>0 && FirstObj.get(DATEON+"_FLAG")==null ) {
	              	    				 DEDCOLOR=	 "#F7D358";
	              	    				 DAF="";
	              	    			 }
	              	    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("A")) {
	              	    				 leave_approval_status="Approved";
	              	    				 Deduction_Hours=0;
	              	    				 DEDCOLOR= "#00ffbf";
	              	    				 DAF=	"none";
	              					 }
	              	    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("P")) {
	              	    				 leave_approval_status="Pending";
	              	    				 //Deduction_Hours=0;
	              	    				 DEDCOLOR="#F7D358";
	              	    				 DAF="none";
	              					 }
	              	    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("R")) {
	              	    				 leave_approval_status="Rejected";
	              						 DEDCOLOR= "#ffb3ff";
	              						 DAF=	"none";
	              					 }
	              	                 
	              	                
	              		    			 
	              		    			 
	              		    			 
	              		    		}
	              		    		else if(FirstObj.get(DATEON+"_HALF_DAY").toString()!="false" && DAYTYPE.equalsIgnoreCase("WDAY") &&  FirstObj.get(DATEON+"_FLAG")!=null) {
	              		    			
	              		    			// Deduction_Hours= attendance_Calculation.checkingAttendance(  con , att_in , att_out, net_hours ,DAYTYPE);
	              		    			//Deduction_Hours= Attenance_backup.checkingAttendance(att_in , att_out, net_hours ,DAYTYPE);
	              		    			//Deduction_Hours=Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);

	               		    			if(BuID.equalsIgnoreCase("42")&&(compareValue < 0||compareValue==0))
	               		    			{
	               		    				System.out.println("Yescall");
	               		    			 Deduction_Hours= Attenance.CompositesAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);
	               		    			 
	               		    			}
	               		    			else
	               		    			{
	               		    			 Deduction_Hours= Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON,halfDayResult);
	               		    			 
	               		    			}
	              		    			 if(Deduction_Hours>0 || FirstObj.get(DATEON+"_FLAG")==null) {
	              		    				 DEDCOLOR=	 "#F7D358";
	              		    			 }
	              		    			 else  if(FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("A") &&  FirstObj.get(DATEON+"_FLAG")!=null){
	              		    				 attendance_approval_status="Approved";
	              		    				 Deduction_Hours=0;
	              		    				 DEDCOLOR= "#00ffbf";
	              		    				 DAF=	"none";
	              	    				 }
	              		    			 else if(FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("R") &&  FirstObj.get(DATEON+"_FLAG")!=null) {
	              	    					 attendance_approval_status="Rejected";
	              	    					 DEDCOLOR= "#ffb3ff";
	              	    					 DAF=	"none";
	              	    				 }
	              	    				 else {
	              	    					 attendance_approval_status="Pending";
	              	    					 DAF=" ";
	              	    					//EDCOLOR= "#fffcff";
	              	    				 }
	              		    			 
	              		    		}
	              		    		 
	              		    	 } 
	              		    	   if(Deduction_Hours>0 &&  DATEON.equalsIgnoreCase(cur_date)){
	              					  Deduction_Hours=0;
	              					  DAF=	"none";
	              				 }
	              		    	total_dedHours=Deduction_Hours+total_dedHours;
	              		    	//attendance_obj.put("DATE", DATEON);
	              		    	attendance_obj.put("DATE", DATEON_VIEW);
	              		    	//DATEON_VIEW
	              		    	
	              		    	attendance_obj.put("FIN", att_in);
	              		    	attendance_obj.put("FOUT", att_out);
	              		    	
	              		    	//attendance_obj.put("PERDAY", net_hours);
	              		    	attendance_obj.put("PERDAY", net_hours_disp);
	              		    	
	              		    	attendance_obj.put("DEDHOURS", total_dedHours);
	              		    	attendance_obj.put("DAYTYPE", DAYTYPE);
	              		    	attendance_obj.put("Adjustments", "");
	              		    	attendance_obj.put("DAREQ", leave_approval_status);
	              		    	attendance_obj.put("DEDCOLOR", DEDCOLOR);
	              		    	boolean att_req=false;
	              		    	String att_req_date="";
	              		    	try{
	              		    		att_req_date=FirstObj.get(DATEON+"_ENABLE_REQ").toString();
	              		    		if(att_req_date!=null && att_req_date.equalsIgnoreCase(DATEON)){
	              		    			att_req=true;
	              		    		}
	              		    		
	              		    		
	              		    	}catch(Exception ex){
	              		    		att_req=false;
	              		    		//ex.printStackTrace();
	              		    	}
	              		    	if(flexi.equalsIgnoreCase("1") && att_req ){
	              		    		attendance_obj.put("DAF",DAF);
	              		    	}
	              		    	else{
	              		    		attendance_obj.put("DAF","none");
	              		    	}
	              		    	
	              		    	//attendance_obj.put("DAF",DAF);
	              		    	attendance_obj.put("LESSHRS", Deduction_Hours);
	              		    	attendance_obj.put("PLS", "NA");
	              		    	attendance_obj.put("INNER", DATEON+"#"+att_in+"#"+att_out+"#"+net_hours+"#"+Deduction_Hours);
	              		    	
	              		    	final_attendance_arr.add(attendance_obj);
	              		    	//System.out.println(total_dedHours+"************************TOTAL");
	              		    	
	              		    	
	              		    	Daily_30day.put("ID",username);
	              		    	Daily_30day.put("NAME",Callname);
	              		    	if(isManager.equalsIgnoreCase("1")){
	              		     	  Daily_30day.put("isManager"," ");
	              		    	}else{
	              		    		Daily_30day.put("isManager","none");
	              		    	}
	              		    	//String DayType="";
	              		    	
	              		    	if(BuID.equalsIgnoreCase("17")||BuID.equalsIgnoreCase("14")||BuID.equalsIgnoreCase("15")||BuID.equalsIgnoreCase("34")||BuID.equalsIgnoreCase("34")){
	              		    		DEDCOLOR="";
	              		    	}
	              		    	if(DAYTYPE.equalsIgnoreCase("WDAY")){
	              		    		
	              		    		Daily_30day.put(DATEON,att_in+"-"+att_out+"~"+DEDCOLOR);
	              		    		
	              		    	}else{
	              		    		
	              		    	   Daily_30day.put(DATEON,DAYTYPE+"~"+DEDCOLOR+"");
	              		    	
	              		    	}
	              		    	
	              		    	Daily_30day_titles.put(DATEON_VIEW, DATEON_VIEW);
	              		   	
	   			 
	              		    	
	              				}catch(Exception ex) {
	              					ex.printStackTrace();
	              				}
	              		    	
	              		      }
	              			 
	              			mp.put("TITLES", Daily_30day_titles);
	              			mp.put("ATT_DATA", Daily_30day);
	              			
	              			return mp;
	   	                // return null;
	   	           }
	   	        });
	          
	          
	          
	          
	          return mp;
	      
	  }
	
}
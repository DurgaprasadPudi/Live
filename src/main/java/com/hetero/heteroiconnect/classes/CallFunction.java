package com.hetero.heteroiconnect.classes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
 
@Repository
public class CallFunction {

	
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	JSONArray final_attendance_arr= null;
	JSONObject attendance_arr= null;
	
	//final_attendance_arr=null;

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	
	
	public synchronized  JSONArray proc_with_resultset(String login,String formdate,String TodayDate,String BUID,Map COLORS,String comparedateTodayformat) throws Exception {  
		String cur_date="";
		//int total_dedHours=0;
		final_attendance_arr= new JSONArray();
		attendance_arr= new JSONObject();
		
	 	//System.out.println("HAU");
		 
	        jdbcTemplate.execute(  
	           new CallableStatementCreator() {   
	              public CallableStatement createCallableStatement(Connection con) throws SQLException {   
	                 String storedProc = "{CALL test.Call_Attendance(?,?,?,?)}";   
	                 CallableStatement cs = con.prepareCall(storedProc);   
	                                  /* cs.setInt(1, 30); //Set the value of the input parameter
	                 cs.setInt(2, 35);*/
	                cs.setString(1, login);
	       			cs.setString(2,formdate); //"2020-11-09"    2019-12-24  
	       			cs.setString(3,TodayDate);
	       			cs.setString(4,  BUID);
	       		//	System.out.println("HAU");
	       			
	       			System.out.println(cs.toString());
	       			
	                 return cs;
	              }   
	           }, new CallableStatementCallback() {   
	               public JSONArray doInCallableStatement(CallableStatement cs) 
	            		   throws SQLException, DataAccessException {
	            	   
	            	   double total_dedHours=0;
	            		Map FirstObj=new HashMap();
	            		Map Min_Obj=new HashMap();   
	            	     
	            		//Attendance_Calculation attendance_Calculation= new Attendance_Calculation();
	           	   ResultSet rs =null;
	           		
	           		rs =cs.executeQuery();  
	                // cs.execute();
	                  // rs = cs.getResultSet();
	           		
	           	 
					Min_Obj = Attcompre.getattencedata(rs);
           			FirstObj.putAll(Min_Obj);
           			rs =null;
           			
           			cs.getMoreResults();
           			rs = cs.getResultSet();
           			Min_Obj= Attcompre.getattencedata(rs);
           			FirstObj.putAll(Min_Obj);
           			rs =null;
           			
           			cs.getMoreResults();
           			rs =cs.getResultSet();
           			Min_Obj= Attcompre.getattencedata(rs);
           			FirstObj.putAll(Min_Obj);
           			rs =null;
           			
           			cs.getMoreResults();
           			rs = cs.getResultSet();
           			Min_Obj= Attcompre.getattencedata(rs);
           			FirstObj.putAll(Min_Obj);
	           		 
           			rs =null;
           			cs.getMoreResults();
           			rs = cs.getResultSet();
           			
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
           		    	  System.err.println(flexi+"flexi--->");
           		    	  
           		    	  if(BUID.equalsIgnoreCase("10"))
           		    	  {
           		    		flexi="1";
           		    	  }
           		    	  
           		    	  
           		    	  // SAT_POLICY
           		    	 //String SAT_POLICY=rs.getString("SAT_POLICY");
           		    	  
           		    	 
           		    	 System.out.println(DATEON+"<---DATE WITH DAYTYPE-->"+DAYTYPE+"att_in"+att_in+"att_out"+att_out);
           		    	 
           		    	  
           		    	 // System.out.println(DAYTYPE +"."+DATEON);
           		    	  
           		    	// System.out.println(BUID+"jjj");
           		    	 
           		    	 LocalDate today = LocalDate.parse("2023-04-24");
           			  LocalDate pastDate = LocalDate.parse(DATEON);

           			  int compareValue = today.compareTo(pastDate);
           		    	  
           		    	  
           		    	  String Saturday="";
           		        // System.out.println(Saturday);
           		    	  
           		  
           		    	  String half_day=  "";
           		    	  
           		    	  
           		    	  
           		    	Object halfDayValue = FirstObj.get(DATEON + "_HALF_DAY");
           		    	System.err.println(halfDayValue+"halfDayValue");
           		    	String halfDayResult = halfDayValue != null ? halfDayValue.toString() : "0";
           		    	  
           		    	if( FirstObj.get(DATEON+"_HOLIDAYDATE")!=null  ){
           		    		// System.out.println(" DATEON---->HOLIDAY " +FirstObj.get(DATEON+"_HOLIDAYDATE") +">>>>>>>..."+FirstObj.get(DATEON+"_COMMENTS"));
           		    		 DAYTYPE=FirstObj.get(DATEON+"_COMMENTS").toString();
           		    		 leave_approval_status="HL";
           		    		 
           		    		  DISPLAYCOLOR=COLORS.get("HL").toString().split("&")[0];
           		    		  DISPLAYNAME=COLORS.get("HL").toString().split("&")[1];
           		    	 }
           		    	
         		    	else if(DAYTYPE.equalsIgnoreCase("HL")&&att_in.equalsIgnoreCase("00:00:00") && att_out.equalsIgnoreCase("00:00:00"))
          		    	{
         		    		
         		    		 //System.out.println("DURGAPRASAD -----Saturday");
         		    		 
            			    	 java.time.LocalDate date = java.time.LocalDate.parse(DATEON);
            			         DayOfWeek dayOfWeek = date.getDayOfWeek();
            			         int dayOfWeekInMonth = (date.getDayOfMonth() - 1) / 7 + 1;
            			        // System.out.println("  Day-of-Week in Month: " + dayOfWeekInMonth);
            			         
            			      //    String Saturday="No";
            			         // 3rd Saturday 
            			          
            			       //   boolean isAfter = java.time.LocalDate.parse("2022-06-11").isBefore(date);		//true
            			       //   boolean isEqual = java.time.LocalDate.parse("2022-06-11").isEqual(date);
            			          
            			         // String Saturday="";
            			         
            			              if (dayOfWeek == DayOfWeek.SATURDAY &&dayOfWeekInMonth==1) {
       			                  // System.out.println("  It is 3rd a Saturday");
       			                          Saturday="1st Saturday";
       			                        } 
            			         	   //2nd Saturday logic
            			         	   if(dayOfWeek == DayOfWeek.SATURDAY &&dayOfWeekInMonth==2){
            			                  // System.out.println(" It is 2nd a Saturday");
            			                   Saturday="2nd Saturday";
            			                }
            			               if (dayOfWeek == DayOfWeek.SATURDAY &&dayOfWeekInMonth==3) {
            			                  // System.out.println("  It is 3rd a Saturday");
            			                   Saturday="3rd Saturday";
            			               } 
            			               
            			               if (dayOfWeek == DayOfWeek.SATURDAY &&dayOfWeekInMonth==4) {
             			                  // System.out.println("  It is 4th a Saturday");
             			                   Saturday="4th Saturday";
             			               } 
            			               
            			      DAYTYPE=Saturday;
        		    		  leave_approval_status="HL";
        		    		  DISPLAYCOLOR=COLORS.get("HL").toString().split("&")[0];
      		    		      DISPLAYNAME=COLORS.get("HL").toString().split("&")[1];
           		    	}
          		    	
           		    	// WOFF
           		    	else if(DAYTYPE.equalsIgnoreCase("WOFF") && att_in.equalsIgnoreCase("00:00:00") && att_out.equalsIgnoreCase("00:00:00")){
           		    		 //System.out.println("  woff ** SUNDAY  **  NO  office ....");
           		    		 leave_approval_status="No Request";
           		    		 
           		    	 // DISPLAYCOLOR=COLORS.get("WEEKLYOFFHOLIDAY").toString();
           		    	  
           		    	  
           		    		DISPLAYCOLOR=COLORS.get("WOFF").toString().split("&")[0];
       		    		  DISPLAYNAME=COLORS.get("WOFF").toString().split("&")[1];
      		    		 // System.out.println();
      		    		  
      		    		//DISPLAYINOUT="none";
      		    		  
      		    		  ///HOLIDAY CHECK OR NOT
           		    	  
           		    	 // System.out.println(COLORS.get("Weekly Off/Holiday").toString());
           		    		//Weekly Off/Holiday
      		    		
      		    		 
      		    		
           		    	}
           		    	
           		    	
           		    	
           		    	 else if(DAYTYPE.equalsIgnoreCase("WOFF") && att_in!="00:00:00" && att_out!="00:00:00" ) {
           		    		
           		    		 leave_approval_status="No Request";
           		    		// System.out.println("CHEKKKK");
           		    		 
           		    		
           		    		 
           		    	 }
           		    	 else if(FirstObj.get(DATEON+"_LEAVEON")!=null && !FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") && !FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half")){
           		    		 
           		    		 
           		    		 System.out.println(FirstObj.get(DATEON+"_LEAVEON")+"^^^^^^^");
           		    		 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
           	    				 
           	    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
           	    				 
           	    				 System.out.println("***************1********"); 
           	    				  
           	    				 
           	    			 }else{
           	    			 
           	    			    DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString();
           	    			    System.out.println("***************2********"+DAYTYPE); 
           	    			 
           	    			if(DAYTYPE.equalsIgnoreCase("WFH")||DAYTYPE.equalsIgnoreCase("OD"))
           	    			{
           	    				DISPLAYCOLOR=COLORS.get(DAYTYPE).toString().split("&")[0];
          		    		    DISPLAYNAME=COLORS.get(DAYTYPE).toString().split("&")[1];
          		    		    System.out.println("PRASAD"+DAYTYPE);
           	    			}
           	    			else
           	    			{
           	    				DISPLAYCOLOR=COLORS.get("LEAVE").toString().split("&")[0];
          		    		    DISPLAYNAME=COLORS.get("LEAVE").toString().split("&")[1];
           	    			}
           	    			
           	    			//DISPLAYCOLOR=COLORS.get(DAYTYPE).toString().split("&")[0];
           	    		   //  DISPLAYINOUT="none";
           	    		 
           	    		 
           	    			//System.out.println("att_in"+att_in);
           	    			//// Incase LEAVE and in out exist
           	    			/*if(!att_in.equalsIgnoreCase("00:00:00") ||!att_out.equalsIgnoreCase("00:00:00"))
           	    			{
           	    			   DISPLAYINOUT="block";
           	    			}*/
           	    			////Only leave apply
           	    			
           	    		 
           	    			    
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
           		    	
           		    	// WDAY Logic
           		    	
           		    	 else if(DAYTYPE.equalsIgnoreCase("WDAY") && att_in.equalsIgnoreCase("00:00:00") && att_out.equalsIgnoreCase("00:00:00")) {
           		    		 
           		    		// System.out.println("  ***************** --- CHECK LEAVE TAKEN OR NOT----  ***************** ");
           		    		 if( FirstObj.get(DATEON+"_LEAVEON")!=null  ){
           			    		// System.out.println(" DATEON---->LEAVE " +FirstObj.get(DATEON+"_leaveon") +">>>>>>>..."+FirstObj.get(DATEON+"_leave_type"));
           			    		 
           		    			 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
           		    				 
           		    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
           		    				 
           		    				 System.out.println("***************3********"); 
           		    				 
           		    				// String halfdayappend=FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half")?"1st Half":"2nd Half";
           		    			//&&& 
          		    		   ///   
           		    		         DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString();
           		    		      System.out.println("***************33********"+DAYTYPE+"COLORS"+COLORS); 
          		    		         
          		    		       // DISPLAYCOLOR=COLORS.get(DAYTYPE).toString().split("&")[0];
           		    		       // DISPLAYNAME=COLORS.get(DAYTYPE).toString().split("&")[1];
          		    		        DISPLAYCOLOR=COLORS.get("LEAVE").toString().split("&")[0];
             		    		    DISPLAYNAME=COLORS.get("LEAVE").toString().split("&")[1];
           		    			 
           		    			 }else{
           		    			 
           		    			 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()  ;
           		    			 
           		    			 System.out.println("Prasad"+DAYTYPE);
           		    			 System.out.println("***************4********"); 
           		    			  
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
           	    					 System.out.println("**********5******");
           	    				 }
           			    	 }
           		    		 else
           		    		 {
           		    			 System.out.println("**********ABSENT******");
           		    			
          		    		    
          		    		  if(comparedateTodayformat.equalsIgnoreCase(DATEON_VIEW))
                 		    	{
        		    		     DAYTYPE=DAYTYPE;
        		    		     DISPLAYCOLOR=COLORS.get("INOFFICE").toString().split("&")[0];
       		    		         DISPLAYNAME=COLORS.get("INOFFICE").toString().split("&")[1];
       		    		     //   DISPLAYINOUT="block";
                 		    	}
          		    		  else
          		    		  {
          		    			DISPLAYCOLOR=COLORS.get("ABSENT").toString().split("&")[0];
          		    		    DISPLAYNAME=COLORS.get("ABSENT").toString().split("&")[1];
          		    		    DAYTYPE=COLORS.get("ABSENT").toString().split("&")[1];
          		    		   // DISPLAYINOUT="none";
          		    		  }
          		    		                   		    	
           		    		 }
           		    	 } 
           		    	
           		    	
           		    	////// ----Half Day Checking-----////
           		    	
           		    	 else if(DAYTYPE.equalsIgnoreCase("WDAY") && att_in.equalsIgnoreCase(att_out) && att_out!="00:00:00" && att_in!="00:00:00" && FirstObj.get(DATEON+"_HALF_DAY")==null ){
           			    		
           		    		// System.out.println("Call checkingAttendance-Method+"+DATEON);
           		    		 
           		    		 Deduction_Hours=8;
           			    		DEDCOLOR=	 "#F7D358";
           			    		 DAF=	" ";
           			    		 
           			    		  DISPLAYCOLOR=COLORS.get("REQUEST").toString().split("&")[0];
             		    		  DISPLAYNAME=COLORS.get("REQUEST").toString().split("&")[1];
           			    		 
           			    		 //FirstObj.get(DATEON+"_FLAG")==null
           			    		 
           			    		 if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("A")) {
           		    				 leave_approval_status="Approved";
           		    				 Deduction_Hours=0;
           		    				 DEDCOLOR= "#00ffbf";
           		    				 DAF=	"none";
           		    				 
           		    			  DISPLAYCOLOR=COLORS.get("REQUESTACCEPTED").toString().split("&")[0];
             		    		  DISPLAYNAME=COLORS.get("REQUESTACCEPTED").toString().split("&")[1];
           	    				 }
           		    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("P")) {
           		    				 leave_approval_status="Pending";
           		    				// Deduction_Hours=4;
           		    				 DEDCOLOR="#F7D358";
           		    				 DAF="none";
           		    				 
           		    				  DISPLAYCOLOR=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[0];
                 		    		  DISPLAYNAME=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[1];
           	    				 }
           		    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("R")) {
           		    				 leave_approval_status="Rejected";
           		    				 //Deduction_Hours=4;
           	    					 DEDCOLOR= "#ffb3ff";
           	    					 DAF=	"none";
           	    					 
           	    					  DISPLAYCOLOR=COLORS.get("REJECTEDREQUEST").toString().split("&")[0];
                		    		  DISPLAYNAME=COLORS.get("REJECTEDREQUEST").toString().split("&")[1];
           	    				 }
           			    		 
           			    		 if( FirstObj.get(DATEON+"_LEAVEON")!=null  ){
           					    		// System.out.println(" DATEON---->LEAVE " +FirstObj.get(DATEON+"_leaveon") +">>>>>>>..."+FirstObj.get(DATEON+"_leave_type"));
           					    		 
           				    			 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
           				    				 
           				    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
           				    				 //DAF=	"";
           				    			 
           				    			     System.out.println("******1**1st******");
           				    			 }else{
           				    			 
           				    			 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()  ;
           				    			 
           				    			 System.out.println(DAYTYPE+"DAYTYPE---Prasad");
           				    			 
           				    		     System.out.println("******2**1st******");
           				    			 DAF=	"none";
           				    			 }
           				    			 
           					    		 
           					    		 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("A")) {
           			    					 leave_approval_status="Approved";
           			    					// DEDCOLOR= "#00ffbf";
           			    					 //DAF=	"none";
           			    					 
           			    					 System.out.println("******3**1st******");
           			    					 
           			    					 DISPLAYCOLOR=COLORS.get("REQUESTACCEPTED").toString().split("&")[0];
           			    				 }
           					    		 else	 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("R")) {
           			    					 leave_approval_status="Rejected";
           			    					// DEDCOLOR= "#ffb3ff";
           			    					// DAF=	"none";
           			    					 System.out.println("******4**1st******");
           			    					 
           			    					 DISPLAYCOLOR=COLORS.get("REJECTEDREQUEST").toString().split("&")[0];
           			    				 }
           					    		 else if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("P")) {
           			    					 leave_approval_status="Pending";
           			    					// DAF=	"none";
           			    					 System.out.println("******5**1st******");
           			    					 
           			    					 DISPLAYCOLOR=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[0];
           			    				 }
           			    				 else {
           			    					// leave_approval_status="Pending";
           			    					// DAF=	"none";
           			    					 System.out.println("******6**1st******");
           			    				 }
           					    	 }
           			    		 
           			    	}
           		    	
           		    	////*** Working Days 
           		    	
           		    	 else if(DAYTYPE.equalsIgnoreCase("WDAY") && att_in!="00:00:00" && att_out!="00:00:00"   ) {
           		    		//&&  !att_in.equalsIgnoreCase(att_out)
           		    		 
           		    		// System.out.println("  ***************** --- CALCULATE ATTENDANCE ----  *****************  "+DATEON);
           		    		 leave_approval_status="No Request";
           		    		if(FirstObj.get(DATEON+"_HALF_DAY")==null || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("false")) {
           		    			// System.out.println(att_in+"   ---  HALF_DAY null----  "+FirstObj.get(DATEON+"_FLAG"));
           		    			 
           		    			
           		    			
           		    			if(flexi.equalsIgnoreCase("0")&&BUID.equalsIgnoreCase("42")&&(compareValue < 0||compareValue==0))
           		    			{
           		    				System.out.println("Yescall");
           		    			 Deduction_Hours= Attenance.CompositesAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);
           		    			 
           		    			}
           		    			else
           		    			{
           		    			 Deduction_Hours= Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON,halfDayResult);
           		    			  System.out.println("Call checkingAttendance-Method+"+DATEON);
           		    			}
           		    			
           		    			
           		    			
           		    			 
           		    			 
           		    			 DAF="none";
           		    			 
           		    			//System.out.println(Deduction_Hours+"<$$$$$$*Prasad*$$$$$$"+FirstObj.get(DATEON+"_FLAG"));
           		    			 
           		    			 if(Deduction_Hours>0 && FirstObj.get(DATEON+"_FLAG")==null ) {
           		    				 DEDCOLOR=	 "#F7D358";
           		    				 DAF="";
           		    				 

               		    			// System.out.println(Deduction_Hours+"<$$$$$$*Prasad*$$$$$$"+FirstObj.get(DATEON+"_FLAG"));
               		    			 
           		    				 
           		    				  DISPLAYCOLOR=COLORS.get("REQUEST").toString().split("&")[0];
                  		    		  DISPLAYNAME=COLORS.get("REQUEST").toString().split("&")[1];
           		    				  
           		    			 }
           		    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("A")) {
           		    				 leave_approval_status="Approved";
           		    				 Deduction_Hours=0;
           		    				 DEDCOLOR= "#00ffbf";
           		    				 DAF=	"none";
           		    				 
           		    				  DISPLAYCOLOR=COLORS.get("REQUESTACCEPTED").toString().split("&")[0];
                 		    		  DISPLAYNAME=COLORS.get("REQUESTACCEPTED").toString().split("&")[1];
           		    				 
           		    			     
           	    				 }
           		    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("P")) {
           		    				 leave_approval_status="Pending";
           		    				 //Deduction_Hours=0;
           		    				 DEDCOLOR="#F7D358";
           		    				 DAF="none";
           		    				 
           		    				  DISPLAYCOLOR=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[0];
                		    		  DISPLAYNAME=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[1];
           		    				 
           	    				 }
           		    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("R")) {
           		    				 leave_approval_status="Rejected";
           	    					 DEDCOLOR= "#ffb3ff";
           	    					 DAF=	"none";
           	    					 
           	    					DISPLAYCOLOR=COLORS.get("REJECTEDREQUEST").toString().split("&")[0];
              		    		    DISPLAYNAME=COLORS.get("REJECTEDREQUEST").toString().split("&")[1];
           	    					 
           	    				 }
           	    				else {
           	    					 //leave_approval_status="Pending";
           	    					 DAF=	"none";
           	    					 
           	    					 
           	    					  DISPLAYCOLOR=COLORS.get("WORKINGDAY").toString().split("&")[0];
                 		    		  DISPLAYNAME=COLORS.get("WORKINGDAY").toString().split("&")[1];
           	    				 }
           		    			 
           		    			 if( FirstObj.get(DATEON+"_LEAVEON")!=null  ){
           					    		// System.out.println(" DATEON---->LEAVE " +FirstObj.get(DATEON+"_leaveon") +">>>>>>>..."+FirstObj.get(DATEON+"_leave_type"));
           					    		 
           				    			 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
           				    				 
           				    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
           				    				 //DAF=	"";
           				    				 
           				    				 System.out.println("******7**1st******"+DAYTYPE);
           				    			 }else{
           				    			 
           				    			 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()  ;
           				    			 DAF=	"none";
           				    			 System.out.println("******8**1st******");
           				    			 }
           				    			 
           					    		 
           					    		 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("A")) {
           			    					 leave_approval_status="Approved";
           			    					// DEDCOLOR= "#00ffbf";
           			    					 //DAF=	"none";
           			    					 System.out.println("******8**1st******");
           			    					 
           			    					 DISPLAYCOLOR=COLORS.get("REQUESTACCEPTED").toString().split("&")[0];
           			    				 }
           					    		 else	 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("R")) {
           			    					 leave_approval_status="Rejected";
           			    					// DEDCOLOR= "#ffb3ff";
           			    					// DAF=	"none";
           			    					 System.out.println("******9**1st******");
           			    					 DISPLAYCOLOR=COLORS.get("REJECTEDREQUEST").toString().split("&")[0];
           			    					
           			    				 }
           					    		 else if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("P")) {
           			    					 leave_approval_status="Pending";
           			    					// DAF=	"none";
           			    					System.out.println("******10**1st******");
           			    					
           			    				    DISPLAYCOLOR=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[0];
           			    				 }
           			    				 else {
           			    					// leave_approval_status="Pending";
           			    					// DAF=	"none";
           			    					System.out.println("******11**1st******");
           			    				 }
           					    	 }
           		    			 
           		    			 
           		    			 
           		    			 
           		    			
           		    		}
           		    			
           		    		else if(FirstObj.get(DATEON+"_HALF_DAY")!=null && FirstObj.get(DATEON+"_HALF_DAY").toString()!="false" && DAYTYPE.equalsIgnoreCase("WDAY")){
           		    			
           		    			DAYTYPE=FirstObj.get(DATEON+"_HALF_DAY").toString();
           		    			//Deduction_Hours= Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);
           		    			

           		    			if(BUID.equalsIgnoreCase("42")&&(compareValue < 0||compareValue==0))
           		    			{
           		    				System.out.println("Yescall");
           		    			 Deduction_Hours= Attenance.CompositesAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);
           		    			 
           		    			}
           		    			else
           		    			{
           		    			 Deduction_Hours= Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON,halfDayResult);
           		    			 
           		    			}
           		    			
           		    			
           		    			
           		    			System.out.println(Deduction_Hours+"Deduction_Hours---Prasad");
           	                 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
           	                	 
           	                	 /*System.out.println(FirstObj.get(DATEON+"_leave_type").toString()+"PRASAD");
           	                	 
           	                	System.out.println(FirstObj.get(DATEON+"_HALF_DAY").toString()+"PRASAD");*/
           		    				 
           		    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
           		    				
           		    				System.out.println("******12**1st******"+DAYTYPE);
           		    				
           		    			  DISPLAYCOLOR=COLORS.get("LEAVE").toString().split("&")[0];
               		    		  DISPLAYNAME=COLORS.get("LEAVE").toString().split("&")[1];
           		    				
           		    			 }else{
           		    			 
           		    			 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()  ;
           		    			System.out.println("******13**1st******");
           		    			 }
           		    			 
           	                      
           	                 //******************************* VENU
           	            //  System.out.println(Deduction_Hours+"<$$$$$$*PUDI*$$$$$$"+FirstObj.get(DATEON+"_FLAG"));
    		    			 
           	                 if(Deduction_Hours>0 && FirstObj.get(DATEON+"_FLAG")==null ) {
           	    				 DEDCOLOR=	 "#F7D358";
           	    				 DAF="";
           	    				 
           	    			 }
           	    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("A")) {
           	    				 leave_approval_status="Approved";
           	    				 Deduction_Hours=0;
           	    				 DEDCOLOR= "#00ffbf";
           	    				 DAF=	"none";
           	    				 
           	    				 DISPLAYCOLOR=COLORS.get("REQUESTACCEPTED").toString().split("&")[0];
           					 }
           	    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("P")) {
           	    				 leave_approval_status="Pending";
           	    				 //Deduction_Hours=0;
           	    				 DEDCOLOR="#F7D358";
           	    				 DAF="none";
           	    				 DISPLAYCOLOR=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[0];
           					 }
           	    			 else if(FirstObj.get(DATEON+"_FLAG")!=null && FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("R")) {
           	    				 leave_approval_status="Rejected";
           						 DEDCOLOR= "#ffb3ff";
           						 DAF=	"none";
           						 DISPLAYCOLOR=COLORS.get("REJECTEDREQUEST").toString().split("&")[0];
           					 }
           	                 
           	                
           		    			 
           		    			 
           		    			 
           		    		}
           		    		else if(FirstObj.get(DATEON+"_HALF_DAY").toString()!="false" && DAYTYPE.equalsIgnoreCase("WDAY") &&  FirstObj.get(DATEON+"_FLAG")!=null) {
           		    			
           		    			// Deduction_Hours= Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);
           		    			 

           		    			if(BUID.equalsIgnoreCase("42")&&(compareValue < 0||compareValue==0))
           		    			{
           		    				System.out.println("Yescall");
           		    			 Deduction_Hours= Attenance.CompositesAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON);
           		    			 
           		    			}
           		    			else
           		    			{
           		    			 Deduction_Hours= Attenance.checkingAttendance(att_in , att_out, net_hours_disp ,DAYTYPE,DATEON,halfDayResult);
           		    			 
           		    			}
           		    			
           		    			
           		    			 System.out.println(Deduction_Hours+"Deduction_Hours---Prasad");
           		    			 if(Deduction_Hours>0 || FirstObj.get(DATEON+"_FLAG")==null) {
           		    				 DEDCOLOR=	 "#F7D358";
           		    				 
           		    				System.out.println("******13**1st******");
           		    			 }
           		    			 
           		    			 else  if(FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("A") &&  FirstObj.get(DATEON+"_FLAG")!=null){
           		    				 attendance_approval_status="Approved";
           		    				 Deduction_Hours=0;
           		    				 DEDCOLOR= "#00ffbf";
           		    				 DAF=	"none";
           		    				System.out.println("******14**1st******");
           	    				 }
           		    			 else if(FirstObj.get(DATEON+"_FLAG").toString().equalsIgnoreCase("R") &&  FirstObj.get(DATEON+"_FLAG")!=null) {
           	    					 attendance_approval_status="Rejected";
           	    					 DEDCOLOR= "#ffb3ff";
           	    					 DAF=	"none";
           	    					System.out.println("******15**1st******");
           	    				 }
           	    				 else {
           	    					 attendance_approval_status="Pending";
           	    					 DAF=" ";
           	    					//EDCOLOR= "#fffcff";
           	    					System.out.println("******16**1st******");
           	    				 }
           		    			  
           		    		}
           		    		
           		    	 
           		    	 } 
           		    	
     // System.out.println(DATEON+"-----******----"+comparedateTodayformat);
           		    	
           		    	   if(Deduction_Hours>0 &&  DATEON_VIEW.equalsIgnoreCase(comparedateTodayformat)){
           					
           					  Deduction_Hours=0;
           					  DAF=	"none";
           					  
           					 
           				 }
           		    	   
           		    	total_dedHours=Deduction_Hours+total_dedHours;
           		    	
           		    	
           		    	
           		    	
           		    	if(FirstObj.get(DATEON+"_LEAVEON")!=null)
      		    		 {
      		    			//DISPLAYCOLOR=COLORS.get(DAYTYPE).toString().split("&")[0];
     		    		   //  DISPLAYNAME=FirstObj.get(DATEON+"_LEAVE_TYPE").toString();
           		    		/*DISPLAYNAME=FirstObj.get(DATEON+"_LEAVE_TYPE").toString();
     		    		    
     		    		     DAYTYPE=DISPLAYNAME;*/
     		    		     
     		    			DISPLAYNAME=leave_approval_status;
     		    			
     		    			 
     		    			if(FirstObj.get(DATEON+"_HALF_DAY")!=null && !FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("false"))
     		    			{
     		    				DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
     		    			}
     		    			else
     		    			{
     		    				DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString();
     		    			}
     		    		    
    		    		     
     		    		    
     		    		    System.out.println("******LEAVEEE*****"+DAYTYPE);
     		    		    
     		    		    
     		    		   //// Leave Approve Or Pending  
     		    		     
     		    		   if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("A")) {
			    					 
			    					 ///DISPLAYCOLOR=COLORS.get("REQUESTACCEPTED").toString().split("&")[0];
			    					 
			    					 leave_approval_status="Approved";
			    					 DISPLAYNAME=leave_approval_status;
			    					 
			    					 System.out.println("*****APPROVED****");
			    				 }
					    		 else 	
     		    		     
     		    		       if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("R")) {
			    					 
			    					 DISPLAYCOLOR=COLORS.get("REJECTEDREQUEST").toString().split("&")[0];
			    					 
			    					 leave_approval_status="Rejected";
			    					 DISPLAYNAME=leave_approval_status;
			    				 }
					    		 else if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("P")) {
			    				 
			    					 DISPLAYCOLOR=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[0];
			    					 
			    					 leave_approval_status="Pending"; 
			    					 DISPLAYNAME=leave_approval_status;
			    					 
			    					 
			    				 }
     		    		    
     		    		    
     		    		    
      		    		 }
           		    	
           		    // DISPLAYINOUT="none";
           		    	
           		    	if(!att_in.equalsIgnoreCase("00:00:00") ||!att_out.equalsIgnoreCase("00:00:00"))
       	    			{
       	    			   DISPLAYINOUT="block";
       	    			  // DISPLAYCOLOR=COLORS.get(DAYTYPE).toString().split("&")[0];
       	    			   if(DAYTYPE.equalsIgnoreCase("HL")||DAYTYPE.equalsIgnoreCase("WOFF"))
       	    			   {
       	    				  DISPLAYCOLOR=COLORS.get(DAYTYPE).toString().split("&")[0];
       	    			   }
       	    			}
           		    	
           		    	 if(comparedateTodayformat.equalsIgnoreCase(DATEON_VIEW)&&DAYTYPE.equalsIgnoreCase("WDAY")&&(att_in!="00:00:00"||att_out!="00:00:00"))
           		    	{
  		    		       DISPLAYCOLOR=COLORS.get("INOFFICE").toString().split("&")[0];
 		    		       DISPLAYNAME=COLORS.get("INOFFICE").toString().split("&")[1];
 		    		       DISPLAYINOUT="block";
 		    		       
 		    		       if(att_out!="00:00:00"&&att_in!="00:00:00")
 		    		       {
 		    		    	  if(att_in.equalsIgnoreCase(att_out))
 		    		    	  {
 		    		    		 att_out="00:00:00";  
 		    		    	  }
 		    		       }
 		    		       
           		    	} 
           		    	else
         		    	 {
         		    		 /// Same punch daily
       	    		    	  if(att_in.equalsIgnoreCase(att_out))
       	    		    	  {
       	    		    		 att_out="00:00:00";  
       	    		    	  }
       	    		       
         		    	 }
           		    	
           		    	
           		    	
           		    	//attendance_obj.put("DATE", DATEON);
           		    	attendance_obj.put("date", DATEON_VIEW);
           		    	//DATEON_VIEW
           		    	
           		    	attendance_obj.put("in", att_in);
           		    	attendance_obj.put("out", att_out);
           		    	
           		    	//attendance_obj.put("PERDAY", net_hours);
           		    	attendance_obj.put("netHours", net_hours_disp);
           		    	
           		    	attendance_obj.put("DEDHOURS", total_dedHours);
           		    	attendance_obj.put("dayType", DAYTYPE);
           		    	attendance_obj.put("Adjustments", "");
           		    	attendance_obj.put("DAREQ", leave_approval_status);
           		    	
           		    	
           		    	///System.out.println(COLORS.get("WORKINGDAY")+"WORKINGDAY");
           		    	
           		    	attendance_obj.put("DEDCOLOR", DEDCOLOR);
           		    	
           		    	
           		    	////--- Prasad----///
           		   /*  if((att_in!="00:00:00" || att_out!="00:00:00"))
          		      {
          		    	DISPLAYINOUT="block";
          		     
          		      }*/
          		     
           		     
           		
           		     
           		    	
           		    	
           		    	
           		    	///IN OFFICE CASE
           		    	
           		    
           		    	
           		    	/*String pattern = "yyyy-MM-dd";
           		    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

           		    	String date = simpleDateFormat.format(TodayDate);
           		    	System.out.println(date);*/
           		    	
           		 	//  System.out.println(TodayDate.split("/").reverse().join("-"));
           		    	
           		    	/*LocalDateTime ldt = LocalDateTime.parse(time);
           		    	System.out.println(DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH).format(ldt));
           		    	System.out.println(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(ldt));
           		    	System.out.println(ldt);*/
           		    	
           		    	//System.out.println("comparedateTodayformat"+comparedateTodayformat+"*************"+DATEON_VIEW);
           		    	 
           		    	//System.out.println(TodayDate+"TodayDate"+DATEON_VIEW);
           		    	
           		    	attendance_obj.put("DISPLAYCOLOR", DISPLAYCOLOR);
           		    	attendance_obj.put("DISPLAYNAME", DISPLAYNAME);
           		    	
           		    	
           		    	//// IN OUT HL/WOFF/SL/--- 
           		    	
           		    	attendance_obj.put("DISPLAYINOUT", DISPLAYINOUT);
           		    	
           		    	
           		    	
           		    	////
           		    	//COLOR CODE
           		    	
           		    	/*System.out.println(COLORS);
           		    	
           		    	System.out.println(COLORS.get("WORKINGDAY")+"WORKINGDAY");
           		    	
           		    	//if(att_in.equalsIgnoreCase(""))
           		    	
           		    	attendance_obj.put("DISPLAYCOLOR", DEDCOLOR);*/
           		    	
           		    	 
           		    	
           		    	boolean att_req=false;
           		    	String att_req_date="";
           		    	
           		    	//System.out.println("CHECK Requetsed"+FirstObj.get(DATEON+"_ENABLE_REQ").toString());
           		    	try{
           		    		att_req_date=FirstObj.get(DATEON+"_ENABLE_REQ").toString();
           		    		if(att_req_date!=null && att_req_date.equalsIgnoreCase(DATEON)){
           		    			att_req=true;
           		    		}
           		    		
           		    		
           		    	}catch(Exception ex){
           		    		att_req=false;
           		    		//ex.printStackTrace();
           		    	}
           		    	if(flexi.equalsIgnoreCase("1") && att_req ||att_req){
           		    		attendance_obj.put("DAF",DAF);
           		    		
           		    		
           		    	}
           		    	else{
           		    		attendance_obj.put("DAF","none");
           		    	}
           		    	//
           		    	

           		    
           		    	if(Deduction_Hours>=1&&DISPLAYNAME.equalsIgnoreCase("Request"))
           		    	{
           		    		LABLE="1";
           		    	}
           		    	
           		    	attendance_obj.put("LABLE", LABLE);
           		    	//attendance_obj.put("DAF",DAF);
           		    	
           		    	
           		    	attendance_obj.put("LESSHRS", Deduction_Hours);
           		    	 
           		    	attendance_obj.put("PLS", "NA");
           		    	attendance_obj.put("INNER", DATEON+"#"+att_in+"#"+att_out+"#"+net_hours+"#"+Deduction_Hours);
           		    	 
           		    	
           		    	
           		    	
           		   /// Monthly Working Hours 
           		    	
              		   /*  
              		     
              		     int EMPHRS=Integer.parseInt(rs.getString("net_hours").split(":")[0]);
              		     int EMPMINUTES=Integer.parseInt(rs.getString("net_hours").split(":")[1]);
              		     int EMPSEC=Integer.parseInt(rs.getString("net_hours").split(":")[2]);
              		     
              		  Double WorkingHours=0.00; 
              		 
              			  EMPHRS_CONVERT_MINITES=EMPHRS* 60;
     				    
       				   
       				 //  System.out.println(totalmintes+"totalmintes");
       				   
       				   EMPSEC=EMPSEC+EMPSEC;
       				  // totalmintes=+totalmintes;
       				   //System.out.println(EMPSEC+"******MINES***"+EMPSEC/60+"EMPSEC");
       				   
       				   int EMPSECTOMINTES=EMPSEC/60;
       				     
       				   totalmintes=totalmintes+(EMPHRS_CONVERT_MINITES+ EMPMINUTES+EMPSECTOMINTES);
       				   ////
       				   
       				   
       				   DecimalFormat df2 = new DecimalFormat("0.00");
      					//System.out.println(df2.format(Ded_Hours)+"df2.format(Ded_Hours)");
      					
      					  
      					  int hours =totalmintes/ 60; //since both are ints, you get an int
      					  int minutes = totalmintes % 60;
      					  
      					  String i=""; 
      					  if(minutes<=9)
      					  {
      						i="0";
      					  }
      					  
      					 
      					    WorkingHours=Double.parseDouble(String.valueOf(hours))+Double.parseDouble("."+i+""+minutes);
      					 
       				 //  System.out.println(WorkingHours+"WorkingHours");
       				    
              		  
              		attendance_obj.put("WorkingHours", WorkingHours);*/
           		    
           		    	final_attendance_arr.add(attendance_obj);
           		    	//System.out.println(total_dedHours+"************************TOTAL");
           		   	
			 
           		    	
           				}catch(Exception ex) {
           					ex.printStackTrace();
           				}
           		    	
           		      }
           			 
           		    
           			
           			return final_attendance_arr;
	                // return null;
	           }
	        });
	        
	       
	        jdbcTemplate.getDataSource().getConnection().close();
	        
	        
	       // System.out.println(final_attendance_arr+"12345:::::::");
	        return final_attendance_arr; 
	}
	
	 

}
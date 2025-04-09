package com.hetero.heteroiconnect.classes;
 
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	public class CallingStoredProcedure_17 {


	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	JSONArray final_attendance_arr= null;
	JSONObject attendance_arr= null;
		 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  synchronized JSONArray proc_with_resultset(String login,String formdate,String TodayDate,String BUID,Map COLORS,String comparedateTodayformat) throws Exception {
		
			//public  synchronized JSONArray   attendance(Connection con , String FromDate, String ToDate, String BuID,  String username  ) { //, String FromDate, String ToDate, String BuID,  String username
				
		final_attendance_arr= new JSONArray();
		attendance_arr= new JSONObject();

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
            	
	            	   
		        String cur_date="";
				int total_dedHours=0;
				int total_dedMints=0;
				String  str_total_dedHours="";
			  
		 
			 Map FirstObj=new HashMap();
			 Map Min_Obj=new HashMap();
		
			ResultSet rs=null;
		 	
		//	Attendance_Calculation_17 attendance_Calculation= new Attendance_Calculation_17();
			try{
				rs =cs.executeQuery();
				// cstmt.execute();
				
				Min_Obj = Attcompre.getattencedata(rs);
				FirstObj.putAll(Min_Obj);
				rs =null;
				cs.getMoreResults();
				rs = cs.getResultSet();
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
				Min_Obj= Attcompre.getattencedata(rs);
				FirstObj.putAll(Min_Obj);
			}catch(Exception err){
				err.printStackTrace();
			}
			rs =null;
			cs.getMoreResults();
			rs = cs.getResultSet();
			System.out.println("FirstObj-->" +FirstObj.toString());
			int in_9_10_counter=0;
			/// ***************** CalClation Part *****************************/
			 String flexi="1";
			while(rs.next()) {
				try {
					int Deduction_Hours=0 , deductionMts=0;
					
					String str_Deduction_Hours="";
							int LogInH=0,LogInM=0, LogInSec=0 , LogOutH=0,LogOutM=0 , LogOutSec=0;
							
							
							  String DISPLAYCOLOR="none";
		           			  String DISPLAYNAME="none";
		           			  String DISPLAYINOUT="none";
		           			  String LABLE="0";			
							
				
				JSONObject attendance_obj= new JSONObject();
				String []ded_arr=null ; 
				String []LogINTime=null ,LogOUTTime=null ;
				 String leave_approval_status="No Request";
				 String attendance_approval_status="No Request";
				 
				String  DEDCOLOR="";
			    String	DAF="none";
				 
		    	  String DATEON=rs.getString("dateon");
		    	  String DATEON_VIEW=rs.getString("DAYVIEW");
		    	  String ACT_IN = rs.getString("ACT_IN");
		    	  String ACT_OUT = rs.getString("ACT_OUT");
		    	  String att_in = rs.getString("att_in");
		    	  String att_out = rs.getString("att_out"); 
		    	  String net_hours = rs.getString("ACT_NETTIME");//DAYTYPE  net_hours   ACT_NETTIME
		    	  String net_hours_disp = rs.getString("net_hours");
		    	  String DAYTYPE = rs.getString("DAYTYPE");
		    	  String employeeid=rs.getString("employeeid");
		    	//  String flexi=rs.getString("flexi");
		    	  //flexi="1";
		    	  System.out.println(DAYTYPE +". flexi     "+DATEON);

		    	  LogINTime=att_in.split(":");
		    	  LogInH=Integer.parseInt(LogINTime[0]);
		    	  LogInM=Integer.parseInt(LogINTime[1]);

		    	  LogOUTTime=att_out.split(":");
		    	  LogOutH=Integer.parseInt(LogOUTTime[0]);
		    	  LogOutM=Integer.parseInt(LogOUTTime[1]);


		    	  
		    	  /**************************  CHECKING 3 TIMES  ELIGIBILITY ***************************************/
		    	  if(LogInH==9 && LogInM<=10 && LogInM>0 && ((LogOutH==17 && LogOutM>= 30)||(LogOutH>17 && LogOutM>= 00))){
		 				
		    			 in_9_10_counter=in_9_10_counter+1;
		    			 
		    			 System.out.println(in_9_10_counter+".............in_9_10_counter");
					 }
		    		 /**************************  CHECKING 3 TIMES  ELIGIBILITY ***************************************/
		    		 
		  
		    	  String half_day=  "";
		    	if( FirstObj.get(DATEON+"_HOLIDAYDATE")!=null  ){
		    		// System.out.println(" DATEON---->HOLIDAY " +FirstObj.get(DATEON+"_HOLIDAYDATE") +">>>>>>>..."+FirstObj.get(DATEON+"_COMMENTS"));
		    		 DAYTYPE=FirstObj.get(DATEON+"_COMMENTS").toString();
		    		  //leave_approval_status="No Request";
		    		 
		    		  leave_approval_status="HL";
   		    		 
  		    		  DISPLAYCOLOR=COLORS.get("HL").toString().split("&")[0];
  		    		  DISPLAYNAME=COLORS.get("HL").toString().split("&")[1];
		    		 
		    		 
		    	 }
		   	 else if(FirstObj.get(DATEON+"_LEAVEON")!=null && !FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") && !FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half")){
	    		 
	    		 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
					 
					 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
					 
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
		    	else if(DAYTYPE.equalsIgnoreCase("WOFF") && att_in.equalsIgnoreCase("00:00:00") && att_out.equalsIgnoreCase("00:00:00")){
		    		 //System.out.println("  woff ** SUNDAY  **  NO  office ....");
		    		 leave_approval_status="No Request";
		    		 
		    		  
    		    		DISPLAYCOLOR=COLORS.get("WOFF").toString().split("&")[0];
		    		    DISPLAYNAME=COLORS.get("WOFF").toString().split("&")[1];
		    	}
		    	
		    	
		    	
		    	 else if(DAYTYPE.equalsIgnoreCase("WOFF") && att_in!="00:00:00" && att_out!="00:00:00" ) {
		    		
		    		 leave_approval_status="No Request";
		    	 } 
		    	
		    	 else if(FirstObj.get(DATEON+"_LEAVEON")!=null &&  att_out!="00:00:00" && att_in!="00:00:00") {
		    		 
		    		 System.out.println("  ***************** --- CHECK LEAVE TAKEN OR NOT----  ***************** ");
		    		 if( FirstObj.get(DATEON+"_LEAVEON")!=null  ){
		    			 
		    			 
			    		// System.out.println(" DATEON---->LEAVE " +FirstObj.get(DATEON+"_leaveon") +">>>>>>>..."+FirstObj.get(DATEON+"_leave_type"));
			    		// DAYTYPE=FirstObj.get(DATEON+"_leave_type").toString();
			    		 
			    		 if(FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("1st Half") || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("2nd Half") ){
		    				 
		    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
		    				 
		    				 System.out.println(FirstObj.get(DATEON+"_LEAVE_TYPE").toString());
		    				 
		    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString();
		    				 
		    				 
   		    		        DISPLAYCOLOR=COLORS.get("LEAVE").toString().split("&")[0];
      		    		    DISPLAYNAME=COLORS.get("LEAVE").toString().split("&")[1];
		    			 }else{
		    			 
		    				 DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString();
		    			 }
		    			 
			    		 
			    		 
			    		 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("A")) {
	   					 leave_approval_status="Approved";
	   					 DAF=	"none";
	   				 }
			    		 else	 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("R")) {
	   					 leave_approval_status="Rejected";
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

		    	
		    	
		    	
		    	 else if(DAYTYPE.equalsIgnoreCase("WDAY") && att_in.equalsIgnoreCase("00:00:00") && att_out.equalsIgnoreCase("00:00:00")) {
		    		 
		    		 System.out.println("  ***************** --- CHECK LEAVE TAKEN OR NOT----  ***************** ");
		    		 if( FirstObj.get(DATEON+"_leaveon")!=null  ){
			    		// System.out.println(" DATEON---->LEAVE " +FirstObj.get(DATEON+"_leaveon") +">>>>>>>..."+FirstObj.get(DATEON+"_leave_type"));
			    		 DAYTYPE=FirstObj.get(DATEON+"_leave_type").toString();
			    		 
			    		 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("A")) {
	   					 leave_approval_status="Approved";
	   					 DAF=	"none";
	   				 }
			    		 else	 if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("R")) {
	   					 leave_approval_status="Rejected";
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
		    	 else if(DAYTYPE.equalsIgnoreCase("WDAY") && att_in.equalsIgnoreCase(att_out) && att_out!="00:00:00" && att_in!="00:00:00" && FirstObj.get(DATEON+"_FLAG")==null){
			    		Deduction_Hours=4;
			    		DEDCOLOR=	 "#F7D358";
			    		 DAF=	" ";
			    		  
  			    		  DISPLAYCOLOR=COLORS.get("REQUEST").toString().split("&")[0];
    		    		  DISPLAYNAME=COLORS.get("REQUEST").toString().split("&")[1];
			    	}
		    	 else if(DAYTYPE.equalsIgnoreCase("WDAY") && att_in!="00:00:00" && att_out!="00:00:00" ) {
		    		 
		    		 
		    		
		    		 
		    		// System.out.println("  ***************** --- CALCULATE ATTENDANCE ----  *****************  "+DATEON);
		    		 leave_approval_status="No Request";
		    		if(FirstObj.get(DATEON+"_HALF_DAY")==null || FirstObj.get(DATEON+"_HALF_DAY").toString().equalsIgnoreCase("false")) {
		    			// System.out.println(att_in+"   ---  HALF_DAY null----  "+FirstObj.get(DATEON+"_FLAG"));
		    			 
		    			
		    			 str_Deduction_Hours= Attenance_backup.AhmecheckingAttendance(att_in , att_out, net_hours ,DAYTYPE,in_9_10_counter);
		    			 

			    			ded_arr=str_Deduction_Hours.split(":");
			    			Deduction_Hours=Integer.parseInt(ded_arr[0]);
			    			deductionMts=Integer.parseInt(ded_arr[1]);
			    			
		    			 DAF="none";
		    			 if(Deduction_Hours>0 || deductionMts>0 && FirstObj.get(DATEON+"_FLAG")==null ) {
		    				 DEDCOLOR=	 "#F7D358";
		    				 DAF="";
		    				 
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
		    			
		    		}
		    			
		    		else if(FirstObj.get(DATEON+"_HALF_DAY")!=null && FirstObj.get(DATEON+"_HALF_DAY").toString()!="false" && DAYTYPE.equalsIgnoreCase("WDAY")){
		    			
		    			//DAYTYPE=FirstObj.get(DATEON+"_HALF_DAY").toString();
		    			
		    			DAYTYPE=FirstObj.get(DATEON+"_LEAVE_TYPE").toString()+" ( "+FirstObj.get(DATEON+"_HALF_DAY").toString() +" )";
		    			str_Deduction_Hours= Attenance_backup.AhmecheckingAttendance( att_in , att_out, net_hours ,DAYTYPE,in_9_10_counter);

		    			ded_arr=str_Deduction_Hours.split(":");
		    			Deduction_Hours=Integer.parseInt(ded_arr[0]);// Deduction_Hours,deductionMts
		    			deductionMts=Integer.parseInt(ded_arr[1]);
		    			
		    			
		    			 if(Deduction_Hours>0 || deductionMts>0 && FirstObj.get(DATEON+"_FLAG")==null ) {
		    				 DEDCOLOR=	 "#F7D358";
		    			 }
		    			 else  if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("A") &&  FirstObj.get(DATEON+"_MANAGER_STATUS")!=null) {
		    				 leave_approval_status="Approved";
		    				 Deduction_Hours=0;
		    				// DEDCOLOR= "#00ffbf";
		    				 DAF=	"none";
		    				 DISPLAYCOLOR=COLORS.get("REQUESTACCEPTED").toString().split("&")[0];
	   				 }
		    			 else if(FirstObj.get(DATEON+"_MANAGER_STATUS").toString().equalsIgnoreCase("R") &&  FirstObj.get(DATEON+"_MANAGER_STATUS")!=null) {
		    				 leave_approval_status="Rejected";
	   					// DEDCOLOR= "#ffb3ff";
	   					 DAF=	"none";
	   					 DISPLAYCOLOR=COLORS.get("REJECTEDREQUEST").toString().split("&")[0];
	   				 }
	   				 else {
	   					 leave_approval_status="Pending";
	   					 DAF=	" ";
	   					// DEDCOLOR= "#fffcff";
	   					 
	   					DISPLAYCOLOR=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[0];
	   				 }
		    		}
		    		else if(FirstObj.get(DATEON+"_HALF_DAY").toString()!="false" && DAYTYPE.equalsIgnoreCase("WDAY") &&  FirstObj.get(DATEON+"_FLAG")!=null) {
		    			
		    			
		    			
		    			
		    			str_Deduction_Hours=  Attenance_backup.AhmecheckingAttendance(att_in , att_out, net_hours ,DAYTYPE,in_9_10_counter);
		    			
		    			ded_arr=str_Deduction_Hours.split(":");
		    			Deduction_Hours=Integer.parseInt(ded_arr[0]);
		    			deductionMts=Integer.parseInt(ded_arr[1]);
		    			
		    			 if(Deduction_Hours>0 || deductionMts>0 || FirstObj.get(DATEON+"_FLAG")==null) {
		    				 DEDCOLOR=	 "#F7D358";
		    				 DAF="";
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
		    	
		    	   if(Deduction_Hours>0 && DATEON_VIEW.equalsIgnoreCase(comparedateTodayformat)){
					
					  Deduction_Hours=0;
					  DAF=	"none";
				 }
		    	    
		    	   if(in_9_10_counter<=3 && in_9_10_counter>0 && LogInH==9 && LogInM<=10 && LogInH!=00 && LogInH!=10 &&  LogInM>0 &&  Deduction_Hours==0&&deductionMts==0) {
			    		 DEDCOLOR= "#C2BEC2"; 
			    		// Deduction_Hours=0;
			    		  
			    		 DISPLAYCOLOR=COLORS.get("REQUESTACCEPTED").toString().split("&")[0];
					} /*
						 * else { DEDCOLOR= ""; }
						 */
		    	     
		    	   if(deductionMts==59 && deductionMts>=00) {
		    		   total_dedMints=total_dedMints+deductionMts;
		    		   total_dedHours=  total_dedHours+Deduction_Hours+1;
		    		   deductionMts=0;
		    	   }
		    	   else if(deductionMts>59 && deductionMts>=00){
		    		   int hours =  60/ 60; //since both are ints, you get an int
		    			int minutes = 100 % 60;
		    			deductionMts=minutes;
		    			  total_dedMints=total_dedMints+deductionMts;
		    		   total_dedHours=  total_dedHours+Deduction_Hours+hours;
		    	   }
		    	   else {
		    		   if(deductionMts<59 && deductionMts>=00){
		    			   total_dedHours=  total_dedHours+Deduction_Hours;
		    			   total_dedMints=total_dedMints+deductionMts;
		    		   }
		    	   }
		    	   
		    	   ////Deduction_Hours, deductionMts
		    	if(Deduction_Hours>0 && deductionMts>0) {
		    		str_total_dedHours=Deduction_Hours+"hrs "+deductionMts+"mins" ;
		    	}
		    	else if (Deduction_Hours>0 && deductionMts==0) {
		    		str_total_dedHours=Deduction_Hours+"hrs";
		    	}
		    	else if (Deduction_Hours==00 && deductionMts>0) {
		    		str_total_dedHours=deductionMts+"mins";
		    		
		    	}else {
		    		if (Deduction_Hours==00 && deductionMts==0) {
		    			str_total_dedHours="0";
			    	}
		    		
		    	}
		    	
		    	String str_dedHours="0";
		    	
		    	// Total HOURS CALCULATION
		    	if(total_dedHours>0 && total_dedMints>0) {
		    		str_dedHours=total_dedHours+"hrs "+total_dedMints+"mins" ;
		    		
		    		System.out.println(str_dedHours+"str_dedHours");
		    	}
		    	else if (total_dedHours>0 && total_dedMints==0) {
		    		str_dedHours=total_dedHours+"hrs";
		    	}
		    	else if (total_dedHours==00 && total_dedMints>0) {
		    		str_dedHours=total_dedMints+"mins";
		    		
		    	}else {
		    		if (total_dedHours==00 && deductionMts==0) {
		    			str_dedHours="0";
			    	}
		    		
		    	}
		    	
		    	
		    	
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
	    		    
	    		    System.out.println("******LEAVEEE*****"+DISPLAYNAME);
	    		    
	    		    
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
		    	
		     
		    	
		    	
		    	
		    	   System.out.println(str_dedHours);
		    	//attendance_obj.put("DATE", DATEON);
  		    	attendance_obj.put("date", DATEON_VIEW);
   		    	//DATEON_VIEW
   		    	
   		    	attendance_obj.put("in", att_in);
   		    	attendance_obj.put("out", att_out);
		    	
		    	//attendance_obj.put("PERDAY", net_hours);
		    	attendance_obj.put("netHours", net_hours_disp);
		    	
		    	attendance_obj.put("DEDHOURS",str_dedHours );
		    	
		    	attendance_obj.put("dayType", DAYTYPE);
		    	attendance_obj.put("Adjustments", "");
		    	attendance_obj.put("DAREQ", leave_approval_status);
		    	attendance_obj.put("DEDCOLOR", DEDCOLOR);
		    	
		    	
		    	attendance_obj.put("DISPLAYCOLOR", DISPLAYCOLOR);
   		    	attendance_obj.put("DISPLAYNAME", DISPLAYNAME);
   		    	
   		    	
   		    	//// IN OUT HL/WOFF/SL/--- 
   		    	
   		    	attendance_obj.put("DISPLAYINOUT", DISPLAYINOUT);
		    	
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
		    	if(flexi.equalsIgnoreCase("1") && att_req ){//&& att_req
		    		attendance_obj.put("DAF",DAF);
		    	}
		    	else{
		    		attendance_obj.put("DAF","none");
		    	}
		    	
		    	//attendance_obj.put("DAF",DAF);
		    	
		    	
		    	attendance_obj.put("LESSHRS", str_total_dedHours);
		    	attendance_obj.put("PLS", "NA");
		    	
		    	if(Deduction_Hours>=1&&DISPLAYNAME.equalsIgnoreCase("Request"))
   		    	{
   		    		LABLE="1";
   		    	}
   		    	
   		    	attendance_obj.put("LABLE", LABLE);
   		    	//attendance_obj.put("DAF",DAF);
   		    	
   		    	System.out.println(str_Deduction_Hours+"str_Deduction_Hours");
   		    	
   		    	//if(str_Deduction_Hours.split(":")[0].equalsIgnoreCase("0"))
   		    			
   		    	
   		    	attendance_obj.put("LESSHRS", str_total_dedHours);
		    	
		    	attendance_obj.put("INNER", DATEON+"#"+att_in+"#"+att_out+"#"+net_hours+"#"+str_Deduction_Hours);
		    	
		    	final_attendance_arr.add(attendance_obj);

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

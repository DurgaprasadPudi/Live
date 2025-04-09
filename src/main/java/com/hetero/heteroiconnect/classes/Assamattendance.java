package com.hetero.heteroiconnect.classes;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


  @Repository
	public class Assamattendance {


	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	  boolean requestflag = false;
	
	  JSONArray final_attendance_arr=null;
	  double TotalNetDed_hours = 0.0D;
      int looHalfHourCount = 1;
      JSONObject Doj = null;
      
	  
		@SuppressWarnings("unused")
		public  synchronized JSONArray proc_with_resultset(String login,String formdate,String TodayDate,String BUID,Map<String,String> COLORS,String comparedateTodayformat) throws Exception {  
			  final_attendance_arr=new JSONArray();
			  Doj = new JSONObject();
	                  StringBuffer Request_Enable = new StringBuffer();
	                  requestflag = false;
	                  String EnableDays = "2";
	                 
         			    
	                  // Back days Enable 
	                  
			         Request_Enable.append(" select distinct dateon from test_mum.tbl_attendance_date_limit_shift ");
		             Request_Enable.append(" where DATEON  between date_ADD(date_format(current_date()-1,'%Y-%m-%d'),INTERVAL -" + EnableDays + " DAY) and date_format(current_date()-1,'%Y-%m-%d') and STATUS=1002");
			         Request_Enable.append(" union ");
			         Request_Enable.append(" select distinct transactiondate from hclhrm_prod_others.tbl_attendance_adjustments ");
			         Request_Enable.append(" where transactiondate between " +formdate+ " and " +TodayDate+ " ");
			         Request_Enable.append(" and employeeid=" +login+ "  ");
		 
			         ResultSet resultSet =null;
		           		
			        //resultSet =resultSet.executeQuery();  
			         Map<Object, Object> UserMap = new HashMap<Object, Object>();
			         
			         jdbcTemplate.query(Request_Enable.toString(), new ResultSetExtractor<Map<Object, Object>>() {
			        	 @Override
			        	 public Map<Object, Object> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			                 while (resultSet.next()) {
			                     UserMap.put(resultSet.getString("dateon"), resultSet.getString("dateon"));
			                 }
							return UserMap;
			             }
			         });
			  
			         ////Hours Data
			         
			         String HoursData="select employeeid,transactiondate,time(transactiontime_in) as transactiontime_in ,time(transactiontime_out) as transactiontime_out ,   nettime,status,ifnull(remark,'Personal') as remark FROM `procedure`.tbl_employee_logs_allbu   where employeeid=" +login+ " and   transactiondate between '" +formdate+ "' and  '" +TodayDate+ "'   and time(nettime)>time('00:10:00')  ";      
			         
			        jdbcTemplate.query(HoursData.toString(), new ResultSetExtractor<Map<Object, Object>>() {
			        	 @Override
			        	 public Map<Object, Object> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			            	 while (resultSet.next()) {
						         UserMap.put(String.valueOf(resultSet.getString("transactiondate")) + "_HOURS", resultSet.getString("transactiondate"));
						         if (resultSet.getString("status").equalsIgnoreCase("1001")) {
						            UserMap.put(String.valueOf(resultSet.getString("transactiondate")) + "_HOURSF", resultSet.getString("transactiondate"));
						       }
						   }
							return UserMap; 
			             }
			         });
			         
			        
			          
			   StringBuffer Req_Adj = new StringBuffer();
			  Req_Adj.append("SELECT EMPLOYEEID,TRANSACTIONDATE,FLAG");
		      Req_Adj.append("  FROM");
			  Req_Adj.append(" HCLHRM_PROD_OTHERS.TBL_ATTENDANCE_ADJUSTMENTS WHERE EMPLOYEEID=" +login);
			  Req_Adj.append(" AND TRANSACTIONDATE BETWEEN '" +formdate+ "'  AND '" +TodayDate+ "' ");
			 
			 
			  jdbcTemplate.query(Req_Adj.toString(), new ResultSetExtractor<Map<Object, Object>>() {
				      @Override
		        	 public Map<Object, Object> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
		            	 while (resultSet.next()) {
		            		 UserMap.put(resultSet.getString(2), resultSet.getString(2));
					   }
						return UserMap; 
		             }
		         });
			  
			  
			  
			  ////
			  
			  
			   /// Main Query
		        /// String Main_Query="select A.DATEON AS DAY ,A.ATT_IN AS FIN,A.ATT_OUT AS FOUT, A.NET_HOURS AS PERDAY , ifnull(D.FLAG,'NA') AS ATT_FLAG , if(A.DATEON=ifnull(C.LEAVEON,'0000-00-00'),C.LEAVE_TYPE, A.DAYTYPE) AS DAYTYPE, IF(A.DATEON=ifnull(C.LEAVEON,'0000-00-00'),if(C.Manager_status='A','Approved','Pending'), IF( ifnull(D.FLAG,'No Request')='A','Approved', if(ifnull(D.FLAG,'No Request')='R','Reject', if(ifnull(D.FLAG,'No Request')='P','Processed','No Request' )))) STATUS, D.FLAG, if(A.DATEON=ifnull(C.LEAVEON,'0000-00-00'), concat(C.LEAVE_TYPE,' / ',C.LEAVE_COUNT,' DAY') , A.DAYTYPE) AS DAYTYPE2, ifnull(A.SHIFT,'General') AS SHIFT, CASE WHEN A.SHIFT='Morning Shift' THEN   TIMEDIFF(A.NET_HOURS,'08:00:00') WHEN A.SHIFT='Second Shift'  THEN   TIMEDIFF(A.NET_HOURS,'08:00:00') WHEN A.SHIFT='Night Shift'   THEN   TIMEDIFF(A.NET_HOURS,'08:00:00') WHEN A.SHIFT='General'       THEN   TIMEDIFF(A.NET_HOURS,'09:00:00') WHEN A.SHIFT  in ('evening Shift','Evening Shift','Ev')  THEN   TIMEDIFF(A.NET_HOURS,'12:00:00') ELSE concat('#',TIMEDIFF(A.NET_HOURS,'09:00:00')) END AS DIFFHOURS, CASE when  A.SHIFT!='Night Shift'   && A.SHIFT!='Morning Shift' && A.SHIFT!='First Shift'   && A.SHIFT!='Second Shift'  && A.att_in!='00:00:00'     && A.SHIFT not in ('evening Shift','Evening Shift','Ev')&& A.att_in!=A.att_out && ifnull(c.half_day,'NA')  NOT IN ('1st Half','2nd Half') THEN  if(A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 09:01:00') && A.att_in!=A.att_out,0.0, if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 09:01:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 09:16:00') && A.att_in!=A.att_out ,'0.5', if( A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 09:16:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 09:31:00') && A.att_in!=A.att_out,1.0, if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 09:31:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 10:01:00') && A.att_in!=A.att_out,2.0,4.0) ))) WHEN A.SHIFT!='Night Shift'    && A.SHIFT!='Morning Shift'  && A.SHIFT!='First Shift'    &&  A.SHIFT!='Second Shift'   && A.att_in!='00:00:00'      && A.att_in!=A.att_out       && A.SHIFT not in ('evening Shift','Evening Shift','Ev')         && ifnull(c.half_day,'NA')   IN ('1st Half') THEN if(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00')),IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00')))>='04:00:00' && A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 14:01:00') && A.att_in!=A.att_out,0.0, if(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00')),IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00')))>='04:00:00' && A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 14:01:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 14:16:00') && A.att_in!=A.att_out ,'0.5', if(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00')),IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00')))>='04:00:00' && A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 14:16:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 14:31:00') && A.att_in!=A.att_out,1.0, if(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00')),IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00')))>='04:00:00' && A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 14:31:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 14:01:00') && A.att_in!=A.att_out,2.0,4.0) ))) WHEN A.SHIFT!='Night Shift'     && A.SHIFT!='Morning Shift'   && A.SHIFT!='First Shift'     && A.SHIFT!='Second Shift'    && A.att_in!='00:00:00'       && A.att_in!=A.att_out        && A.SHIFT not in ('evening Shift','Evening Shift','Ev')             && ifnull(c.half_day,'NA')    IN ('2nd Half') THEN if(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00')),IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00')))>='04:00:00' && A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 09:01:00') && A.att_in!=A.att_out,0.0, if(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00')),IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00')))>='04:00:00'  && A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 09:01:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 09:16:00') && A.att_in!=A.att_out ,'0.5', if(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00')),IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00')))>='04:00:00'  && A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 09:16:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 09:31:00') && A.att_in!=A.att_out,1.0, if(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00')),IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00')))>='04:00:00' && A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 09:31:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 10:01:00') && A.att_in!=A.att_out,2.0,4.0) ))) WHEN  A.SHIFT='Morning Shift'     && A.att_in!='00:00:00'        && A.att_in!=A.att_out         && ifnull(c.half_day,'NA')     NOT IN ('1st Half','2nd Half') THEN if(A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 06:01:00') && A.att_in!=A.att_out ,'0.0', if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 06:01:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 06:16:00') && A.att_in!=A.att_out  ,'0.5', if(A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 06:16:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 06:31:00') && A.att_in!=A.att_out ,'1.0', if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 06:31:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 07:01:00') && A.att_in!=A.att_out,'2.0','4.0'))  )) WHEN A.SHIFT='Morning Shift' || A.SHIFT='First Shift' && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') IN ('1st Half','2nd Half') THEN  IF(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00')), IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 06:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 06:00:00')))>='04:00:00','0.0','4.0')  WHEN A.SHIFT='Second Shift'  && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') IN ('1st Half','2nd Half') THEN  IF(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 22:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 22:00:00')), IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00')))>='04:00:00','0.0','4.0')  WHEN A.SHIFT='Night Shift' && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') IN ('1st Half','2nd Half') THEN  IF(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon + interval 1 day,'%Y-%m-%d 06:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon + interval 1 day,'%Y-%m-%d 06:00:00')), IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 22:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 22:00:00')))>='04:00:00','0.0','4.0')   WHEN  A.SHIFT='Second Shift' && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') NOT IN ('1st Half','2nd Half') THEN if(A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 14:01:00') && A.att_in!=A.att_out ,0.0, if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 14:01:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 14:16:00') && A.att_in!=A.att_out  ,'0.5', if(A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 14:16:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 14:31:00') && A.att_in!=A.att_out ,1.0, if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 14:31:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 15:01:00') && A.att_in!=A.att_out ,2.0,4.0)))) WHEN A.SHIFT='Night Shift' && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') NOT IN ('1st Half','2nd Half') THEN if(A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 22:01:00') && A.att_in!=A.att_out ,0.0, if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 22:01:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 22:16:00') && A.att_in!=A.att_out  ,'0.5', if(A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 22:16:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 22:31:00') && A.att_in!=A.att_out ,1.0, if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 22:31:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 23:01:00') && A.att_in!=A.att_out ,2.0,4.0)))) WHEN A.SHIFT  in ('evening Shift','Evening Shift','Ev') && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') NOT IN ('1st Half','2nd Half') THEN if(A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 18:01:00') && A.att_in!=A.att_out ,0.0, if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 18:01:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 18:16:00') && A.att_in!=A.att_out  ,'0.5', if(A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 18:16:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 18:31:00') && A.att_in!=A.att_out ,1.0, if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 18:31:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 18:01:00') && A.att_in!=A.att_out ,2.0,4.0)))) WHEN A.SHIFT in ('evening Shift','Evening Shift','Ev') && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA')  IN ('1st Half','2nd Half') THEN IF(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon + interval 1 day,'%Y-%m-%d 06:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon + interval 1 day,'%Y-%m-%d 06:00:00')), IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00')))>='04:00:00','0.0','4.0')  WHEN A.att_in!='00:00:00' AND A.att_in=A.att_out THEN '4.0' WHEN  A.att_in='00:00:00' AND A.att_out='00:00:00' THEN '0.0' ELSE  '0.0' END AS DEDHOURS_NET, CASE   WHEN A.SHIFT='Morning Shift' THEN    IF(TIMEDIFF(A.NET_HOURS,'08:00:00')<'00:00:00','true',  'false') WHEN A.SHIFT='Second Shift'  THEN    IF(TIMEDIFF(A.NET_HOURS,'08:00:00')<'00:00:00','true',  'false') WHEN A.SHIFT='Night Shift'   THEN    IF(TIMEDIFF(A.NET_HOURS,'08:00:00')<'00:00:00','true',  'false') WHEN A.SHIFT='General'       THEN    IF(TIMEDIFF(A.NET_HOURS,'09:00:00')<'00:00:00','true',  'false') WHEN A.SHIFT  in ('evening Shift','Evening Shift','Ev')           THEN    IF(TIMEDIFF(A.NET_HOURS,'12:00:00')<'00:00:00','true',  'false') ELSE  IF(TIMEDIFF(A.NET_HOURS,'09:00:00')<'00:00:00','true','false')  END AS DEDHOURS , date_format(A.DATEON,'%d-%m-%Y') AS DAYVIEW , date_format(now(),'%d-%m-%Y') As Newdate , date_format(now() + interval -" + EnableDays + " DAY,'%d-%m-%Y') As Newdate1 , xx.DED_EV_HOURS , if(ifnull(c.half_day,'NA')  NOT IN ('1st Half','2nd Half') , xx.DED_EV_HOURS,'0.0') AS DED_EV_HOURS, if(ifnull(c.half_day,'NA')  NOT IN ('1st Half','2nd Half') , if(abs(xx.DED_EV_HOURS)<'01:00:00' and xx.DED_EV_HOURS<'00:00:00' ,'1.0', concat(HOUR(xx.DED_EV_HOURS),'.',MINUTE(xx.DED_EV_HOURS))),'0.0' ) AS END_DED_HR from test_mum.tbl_att_leave_in_out_status_report A join hclhrm_prod.tbl_employee_primary pr on pr.employeesequenceno=A.employeeid and A.PAYPERIOD=0 left join hclhrm_prod_others.tbl_emp_leave_report C ON C.employeeid=pr.employeesequenceno AND ifnull(C.LEAVEON,'0000-00-00')=A.DATEON AND C.MANAGER_STATUS IN ('A','P') and C.STATUS=1001 left join hclhrm_prod_others.tbl_emp_attn_req D ON D.EMPLOYEEID=pr.employeesequenceno AND D.REQ_DATE=A.DATEON AND  D.REQ_TYPE='AR' AND D.STATUS=1001 left join ( select dateon, if(ATT_OUT=ATT_IN AND ATT_IN!='00:00:00' , '00:00:00', if(ifnull(SHIFT,'General')='General',IF(ACT_OUT<DATE_FORMAT(dateon,'%Y-%m-%d 18:00:00'), TIMEDIFF(ACT_OUT,DATE_FORMAT(dateon,'%Y-%m-%d 18:00:00')),'00:00:000'), if(SHIFT='Morning Shift',IF(ACT_OUT<DATE_FORMAT(dateon,'%Y-%m-%d 14:00:00'), TIMEDIFF(ACT_OUT,DATE_FORMAT(dateon,'%Y-%m-%d 14:00:00')),'00:00:000'), if(SHIFT='Second Shift',IF(ACT_OUT<DATE_FORMAT(dateon,'%Y-%m-%d 22:00:00'), TIMEDIFF(ACT_OUT,DATE_FORMAT(dateon,'%Y-%m-%d 22:00:00')),'00:00:000'), if(SHIFT='Night Shift',IF(ACT_OUT < DATE_FORMAT(dateon + interval 1 day ,'%Y-%m-%d 06:00:00'), TIMEDIFF(ACT_OUT,DATE_FORMAT(dateon + interval 1 day,'%Y-%m-%d 06:00:00')),'00:00:000'), if(SHIFT in ('evening Shift','Evening Shift','Ev'),IF(ACT_OUT < DATE_FORMAT(dateon + interval 1 day ,'%Y-%m-%d 06:00:00'), TIMEDIFF(ACT_OUT,DATE_FORMAT(dateon + interval 1 day,'%Y-%m-%d 06:00:00')),'00:00:000'),'00:00:00' )))))) DED_EV_HOURS from test_mum.tbl_att_leave_in_out_status_report where employeeid='"+login+"' and dateon  between  '"+formdate+"' and '"+TodayDate+"' ) xx on xx.dateon=A.dateon where pr.employeesequenceno in('"+login+"') and a.dateon between  '"+formdate+"' and '"+TodayDate+"' order by a.dateon ";
		        
		         String Main_Query="select A.DATEON AS DAY ,A.ATT_IN AS FIN,A.ATT_OUT AS FOUT, A.NET_HOURS AS PERDAY , ifnull(D.FLAG,'NA') AS ATT_FLAG ,    if(A.DATEON=ifnull(C.LEAVEON,'0000-00-00'),C.LEAVE_TYPE, A.DAYTYPE) AS DAYTYPE,     IF(A.DATEON=ifnull(C.LEAVEON,'0000-00-00'),if(C.Manager_status='A','Approved','Pending'),     IF(     ifnull(D.FLAG,'No Request')='A','Approved',     if(ifnull(D.FLAG,'No Request')='R','Reject',     if(ifnull(D.FLAG,'No Request')='P','Processed','No Request'     )))) STATUS,     D.FLAG,     if(A.DATEON=ifnull(C.LEAVEON,'0000-00-00'),     concat(C.LEAVE_TYPE,' / ',C.LEAVE_COUNT,' DAY')     , A.DAYTYPE) AS DAYTYPE2,     ifnull(A.SHIFT,'General') AS SHIFT,     CASE     WHEN A.SHIFT='Morning Shift' THEN   TIMEDIFF(A.NET_HOURS,'08:00:00')     WHEN A.SHIFT='Second Shift'  THEN   TIMEDIFF(A.NET_HOURS,'08:00:00')     WHEN A.SHIFT='Night Shift'   THEN   TIMEDIFF(A.NET_HOURS,'08:00:00')     WHEN A.SHIFT='General'       THEN   TIMEDIFF(A.NET_HOURS,'09:00:00')     WHEN A.SHIFT  in ('evening Shift','Evening Shift','Ev')  THEN   TIMEDIFF(A.NET_HOURS,'12:00:00')     ELSE concat('#',TIMEDIFF(A.NET_HOURS,'09:00:00'))     END AS DIFFHOURS,     CASE     when      A.SHIFT!='Night Shift'   &&     A.SHIFT!='Morning Shift' &&     A.SHIFT!='First Shift'   &&     A.SHIFT!='Second Shift'  &&     A.att_in!='00:00:00'     &&     A.SHIFT not in ('evening Shift','Evening Shift','Ev') &&     A.att_in!=A.att_out      &&     ifnull(c.half_day,'NA')  NOT IN ('1st Half','2nd Half')     THEN      if(A.ACT_IN<=if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(a.dateon,'%Y-%m-%d 09:01:00'),DATE_FORMAT(a.dateon,'%Y-%m-%d 08:01:00'))  && A.att_in!=A.att_out,0.0,     if(A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(a.dateon,'%Y-%m-%d 09:01:00'),DATE_FORMAT(a.dateon,'%Y-%m-%d 08:01:00')) &&     A.ACT_IN<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(a.dateon,'%Y-%m-%d 09:16:00'),DATE_FORMAT(a.dateon,'%Y-%m-%d 08:16:00')) && A.att_in!=A.att_out ,'0.5',     if( A.ACT_IN>=if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(a.dateon,'%Y-%m-%d 09:16:00'),DATE_FORMAT(a.dateon,'%Y-%m-%d 08:16:00')) &&     A.ACT_IN<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(a.dateon,'%Y-%m-%d 09:31:00'),DATE_FORMAT(a.dateon,'%Y-%m-%d 08:31:00')) && A.att_in!=A.att_out,1.0,     if(A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(a.dateon,'%Y-%m-%d 09:31:00'),DATE_FORMAT(a.dateon,'%Y-%m-%d 08:31:00'))    && A.ACT_IN<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(a.dateon,'%Y-%m-%d 10:01:00'),DATE_FORMAT(a.dateon,'%Y-%m-%d 09:01:00')) && A.att_in!=A.att_out,2.0,4.0)     )))     WHEN     A.SHIFT!='Night Shift'    &&     A.SHIFT!='Morning Shift'  &&     A.SHIFT!='First Shift'    &&      A.SHIFT!='Second Shift'   &&     A.att_in!='00:00:00'      &&     A.att_in!=A.att_out       &&     A.SHIFT not in ('evening Shift','Evening Shift','Ev') &&     ifnull(c.half_day,'NA')   IN ('1st Half')     THEN    if(TIMEDIFF(IF(A.ACT_OUT< if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 17:00:00')),A.ACT_OUT,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(a.dateon,'%Y-%m-%d 18:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 17:00:00'))),    IF(A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00')),A.ACT_IN,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'))))>='04:00:00' &&     A.ACT_IN<=if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(a.dateon,'%Y-%m-%d 14:01:00'),DATE_FORMAT(a.dateon,'%Y-%m-%d 13:01:00') ) && A.att_in!=A.att_out,0.0,    if(TIMEDIFF(IF(A.ACT_OUT<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 17:00:00')),A.ACT_OUT,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 17:00:00'))),IF(A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00')),A.ACT_IN,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'))))>='04:00:00' &&    A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:01:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:01:00')) &&     A.ACT_IN<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:16:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:16:00')) && A.att_in!=A.att_out ,'0.5',     if(TIMEDIFF(IF(A.ACT_OUT<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 17:00:00')),A.ACT_OUT,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 17:00:00'))),IF(A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00')),A.ACT_IN,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'))))>='04:00:00' &&     A.ACT_IN>=if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:16:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:16:00')) &&     A.ACT_IN<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:31:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:31:00')) && A.att_in!=A.att_out,1.0,     if(TIMEDIFF(IF(A.ACT_OUT<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 17:00:00')),A.ACT_OUT,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 17:00:00'))),    IF(A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00')),A.ACT_IN,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'))))>='04:00:00' &&     A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:31:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:31:00')) &&     A.ACT_IN<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 14:01:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 13:01:00')) && A.att_in!=A.att_out,2.0,4.0)     )))     WHEN     A.SHIFT!='Night Shift'     &&     A.SHIFT!='Morning Shift'   &&     A.SHIFT!='First Shift'     &&     A.SHIFT!='Second Shift'    &&     A.att_in!='00:00:00'       &&     A.att_in!=A.att_out        &&     A.SHIFT not in ('evening Shift','Evening Shift','Ev') &&     ifnull(c.half_day,'NA')    IN ('2nd Half')     THEN     if(TIMEDIFF(IF(A.ACT_OUT<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 12:00:00')),A.ACT_OUT,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 12:00:00'))),    IF(A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:00:00')),A.ACT_IN,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:00:00'))))>='04:00:00' &&     A.ACT_IN<=if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:00:00')) && A.att_in!=A.att_out,0.0,     if(TIMEDIFF(IF(A.ACT_OUT< if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 12:00:00'))  ,A.ACT_OUT,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 12:00:00'))),    IF(A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:00:00')),A.ACT_IN,    if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:00:00'))))>='04:00:00'  &&     A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:01:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:01:00')) &&     A.ACT_IN<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:16:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:16:00')) && A.att_in!=A.att_out ,'0.5',     if(TIMEDIFF(IF(A.ACT_OUT<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 12:00:00')),A.ACT_OUT,    if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 12:00:00'))),    IF(A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:00:00')),A.ACT_IN,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:00:00'))))>='04:00:00'  &&     A.ACT_IN>=if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:16:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:16:00')) &&     A.ACT_IN<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:31:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:31:00')) && A.att_in!=A.att_out,1.0,     if(TIMEDIFF(IF(A.ACT_OUT<if(A.dateon<'2021-05-06',DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 12:00:00')),A.ACT_OUT,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 13:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 12:00:00'))),    IF(A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:00:00')),A.ACT_IN,if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:00:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:00:00'))))>='04:00:00' &&     A.ACT_IN>if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 09:31:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 08:31:00')) &&     A.ACT_IN<if(a.dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(A.dateon,'%Y-%m-%d 10:01:00'),DATE_FORMAT(A.dateon,'%Y-%m-%d 09:01:00')) &&     A.att_in!=A.att_out,2.0,4.0)     )))     WHEN      A.SHIFT='Morning Shift'     &&     A.att_in!='00:00:00'        &&     A.att_in!=A.att_out         &&     ifnull(c.half_day,'NA')     NOT IN ('1st Half','2nd Half')     THEN     if(A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 06:01:00') && A.att_in!=A.att_out ,'0.0',     if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 06:01:00') &&     A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 06:16:00') &&     A.att_in!=A.att_out  ,'0.5',     if(A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 06:16:00') &&     A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 06:31:00') &&     A.att_in!=A.att_out ,'1.0',     if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 06:31:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 07:01:00')     && A.att_in!=A.att_out,'2.0','4.0'))      ))     WHEN     A.SHIFT='Morning Shift' || A.SHIFT='First Shift' && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') IN ('1st Half','2nd Half')     THEN      IF(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00')),     IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 06:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 06:00:00')))>='04:00:00','0.0','4.0')    WHEN     A.SHIFT='Second Shift'  && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') IN ('1st Half','2nd Half')     THEN      IF(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon,'%Y-%m-%d 22:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon,'%Y-%m-%d 22:00:00')),     IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 14:00:00')))>='04:00:00','0.0','4.0')    WHEN     A.SHIFT='Night Shift' && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') IN ('1st Half','2nd Half')     THEN      IF(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon + interval 1 day,'%Y-%m-%d 06:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon + interval 1 day,'%Y-%m-%d 06:00:00')),     IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 22:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 22:00:00')))>='04:00:00','0.0','4.0')    WHEN      A.SHIFT='Second Shift' && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') NOT IN ('1st Half','2nd Half')     THEN     if(A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 14:01:00') && A.att_in!=A.att_out ,0.0,     if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 14:01:00') &&     A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 14:16:00')     && A.att_in!=A.att_out  ,'0.5',     if(A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 14:16:00')     && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 14:31:00')     && A.att_in!=A.att_out ,1.0,     if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 14:31:00')     && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 15:01:00')     && A.att_in!=A.att_out ,2.0,4.0))))     WHEN     A.SHIFT='Night Shift' && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') NOT IN ('1st Half','2nd Half')     THEN     if(A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 22:01:00') && A.att_in!=A.att_out ,0.0,     if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 22:01:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 22:16:00')     && A.att_in!=A.att_out  ,'0.5',     if(A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 22:16:00')     && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 22:31:00')     && A.att_in!=A.att_out ,1.0,     if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 22:31:00')     && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 23:01:00')     && A.att_in!=A.att_out ,2.0,4.0))))     WHEN     A.SHIFT  in ('evening Shift','Evening Shift','Ev') && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA') NOT IN ('1st Half','2nd Half')     THEN     if(A.ACT_IN<=DATE_FORMAT(a.dateon,'%Y-%m-%d 18:01:00') && A.att_in!=A.att_out ,0.0,     if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 18:01:00') && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 18:16:00')     && A.att_in!=A.att_out  ,'0.5',     if(A.ACT_IN>=DATE_FORMAT(a.dateon,'%Y-%m-%d 18:16:00')     && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 18:31:00')     && A.att_in!=A.att_out ,1.0,     if(A.ACT_IN>DATE_FORMAT(a.dateon,'%Y-%m-%d 18:31:00')     && A.ACT_IN<DATE_FORMAT(a.dateon,'%Y-%m-%d 18:01:00')     && A.att_in!=A.att_out ,2.0,4.0))))     WHEN     A.SHIFT in ('evening Shift','Evening Shift','Ev') && A.att_in!='00:00:00' && A.att_in!=A.att_out && ifnull(c.half_day,'NA')  IN ('1st Half','2nd Half')     THEN     IF(TIMEDIFF(IF(A.ACT_OUT<DATE_FORMAT(A.dateon + interval 1 day,'%Y-%m-%d 06:00:00'),A.ACT_OUT,DATE_FORMAT(A.dateon + interval 1 day,'%Y-%m-%d 06:00:00')),     IF(A.ACT_IN>DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00'),A.ACT_IN,DATE_FORMAT(A.dateon,'%Y-%m-%d 18:00:00')))>='04:00:00','0.0','4.0')    WHEN     A.att_in!='00:00:00' AND A.att_in=A.att_out     THEN     '4.0'     WHEN      A.att_in='00:00:00' AND A.att_out='00:00:00'     THEN     '0.0'     ELSE      '0.0'     END AS DEDHOURS_NET,     CASE       WHEN A.SHIFT='Morning Shift' THEN    IF(TIMEDIFF(A.NET_HOURS,'08:00:00')<'00:00:00','true',  'false')     WHEN A.SHIFT='Second Shift'  THEN    IF(TIMEDIFF(A.NET_HOURS,'08:00:00')<'00:00:00','true',  'false')     WHEN A.SHIFT='Night Shift'   THEN    IF(TIMEDIFF(A.NET_HOURS,'08:00:00')<'00:00:00','true',  'false')     WHEN A.SHIFT='General'       THEN    IF(TIMEDIFF(A.NET_HOURS,'09:00:00')<'00:00:00','true',  'false')     WHEN A.SHIFT  in ('evening Shift','Evening Shift','Ev')              THEN        IF(TIMEDIFF(A.NET_HOURS,'12:00:00')<'00:00:00','true',  'false')     ELSE  IF(TIMEDIFF(A.NET_HOURS,'09:00:00')<'00:00:00','true','false')  END AS DEDHOURS ,     date_format(A.DATEON,'%d-%m-%Y') AS DAYVIEW , date_format(now(),'%d-%m-%Y') As Newdate , date_format(now() + interval -0 DAY,'%d-%m-%Y') As Newdate1 ,     xx.DED_EV_HOURS ,     if(ifnull(c.half_day,'NA')  NOT IN ('1st Half','2nd Half') , xx.DED_EV_HOURS,'0.0') AS DED_EV_HOURS,     if(ifnull(c.half_day,'NA')  NOT IN ('1st Half','2nd Half') ,     if(abs(xx.DED_EV_HOURS)<'01:00:00' and xx.DED_EV_HOURS<'00:00:00' ,'1.0',     concat(HOUR(xx.DED_EV_HOURS),'.',MINUTE(xx.DED_EV_HOURS))),'0.0'     ) AS END_DED_HR     from test_mum.tbl_att_leave_in_out_status_report A     join hclhrm_prod.tbl_employee_primary pr on pr.employeesequenceno=A.employeeid    left join (   select EMPLOYEEID,ifnull(LEAVEON,'0000-00-00') AS LEAVEON ,LEAVE_COUNT,LEAVE_TYPE,MANAGER_STATUS,STATUS,HALF_DAY from hclhrm_prod_others.tbl_emp_leave_report   where employeeid='"+login+"' and LEAVEON between '"+formdate+"' and '"+TodayDate+"' and MANAGER_STATUS IN ('A','P') and STATUS=1001   )C on c.EMPLOYEEID=A.EMPLOYEEID and c.LEAVEON=A.dateon  left join (   select employeeid,REQ_TYPE,ifnull(REQ_DATE,'0000-00-00') AS REQ_DATE  ,message,flag from hclhrm_prod_others.tbl_emp_attn_req where EMPLOYEEID='"+login+"'   and REQ_DATE between '"+formdate+"' and '"+TodayDate+"' and REQ_TYPE='AR' AND STATUS=1001   ) D on D.EMPLOYEEID=A.employeeid and D.REQ_DATE=A.dateon   left join (     select employeeid,dateon,     if(ATT_OUT=ATT_IN AND ATT_IN!='00:00:00' , '00:00:00',     if(ifnull(SHIFT,'General')='General',   IF(ACT_OUT< if(dateon not between '2021-05-06' and '2021-06-09',DATE_FORMAT(dateon,'%Y-%m-%d 18:00:00'),DATE_FORMAT(dateon,'%Y-%m-%d 17:00:00')),     TIMEDIFF(ACT_OUT,if(dateon not between '2021-05-06' and '2021-06-09' ,DATE_FORMAT(dateon,'%Y-%m-%d 18:00:00'),DATE_FORMAT(dateon,'%Y-%m-%d 17:00:00'))),'00:00:000'),     if(SHIFT='Morning Shift',IF(ACT_OUT<DATE_FORMAT(dateon,'%Y-%m-%d 14:00:00'),     TIMEDIFF(ACT_OUT,DATE_FORMAT(dateon,'%Y-%m-%d 14:00:00')),'00:00:000'),     if(SHIFT='Second Shift',IF(ACT_OUT<DATE_FORMAT(dateon,'%Y-%m-%d 22:00:00'),     TIMEDIFF(ACT_OUT,DATE_FORMAT(dateon,'%Y-%m-%d 22:00:00')),'00:00:000'),     if(SHIFT='Night Shift',IF(ACT_OUT < DATE_FORMAT(dateon + interval 1 day ,'%Y-%m-%d 06:00:00'),     TIMEDIFF(ACT_OUT,DATE_FORMAT(dateon + interval 1 day,'%Y-%m-%d 06:00:00')),'00:00:000'),    if(SHIFT in ('evening Shift','Evening Shift','Ev'),IF(ACT_OUT < DATE_FORMAT(dateon + interval 1 day ,'%Y-%m-%d 06:00:00'),     TIMEDIFF(ACT_OUT,DATE_FORMAT(dateon + interval 1 day,'%Y-%m-%d 06:00:00')),'00:00:000'),'00:00:00'     )))))) DED_EV_HOURS     from test_mum.tbl_att_leave_in_out_status_report where employeeid='"+login+"'    and dateon between '"+formdate+"' and '"+TodayDate+"'   ) xx on xx.dateon=A.dateon and xx.employeeid=a.employeeid   where pr.employeesequenceno in('"+login+"') and a.employeeid='"+login+"'   and a.dateon between '"+formdate+"' and '"+TodayDate+"'    order by a.dateon ";  
		         
		         //System.out.println(Main_Query.toString());
		         jdbcTemplate.query(Main_Query.toString(), new ResultSetExtractor<JSONArray>() {
		        	 
		        	 
		          String DISPLAYCOLOR="none";
		       	  String DISPLAYNAME="none";
		       	  String DISPLAYINOUT="none";
		       	    String LABLE="0";
		        	 
		        	 @Override
		        	 public JSONArray extractData(ResultSet Res) throws SQLException, DataAccessException {
		                 while (Res.next()) {
		                	Doj = new JSONObject();
		                	 
		     			    Doj.put("date", Res.getString("DAYVIEW"));
		     			    Doj.put("in", Res.getString("FIN"));
		     			    Doj.put("out", Res.getString("FOUT"));
		     			    Doj.put("netHours", Res.getString("PERDAY"));
		     			    Doj.put("INNER", String.valueOf(Res.getString("DAY")) + "#" + Res.getString("FIN") + "#" + Res.getString("FOUT") + "#" + Res.getString("PERDAY"));
		     			    Doj.put("dayType", Res.getString("DAYTYPE"));
		     			    Doj.put("DAREQ", Res.getString("STATUS"));
		     			    Doj.put("DAFH", "none");
		     			    Doj.put("DAFMAIN", "false");
		     			    Doj.put("ROCOLOR", "");
		     			    
		     			  
		     			    
		     			    UserMap.put(Res.getString("Newdate1"), Res.getString("Newdate1"));
 
		     			    Doj.put("DEDHOURS_NET", Res.getString("DEDHOURS_NET"));
		     			    Doj.put("DEDHOURS_NET_FINAL", Integer.valueOf(0));
 
		     			    double DEDHOURS_NET = 0.0D;
		     			    double DEDHOURS_OUT = 0.0D;
		     			    double DEDHOURS_IN = 0.0D;
		     			    boolean inoutflg = false;
		     			    boolean inoutflg_half = false;
		     			    try {
		     			          DEDHOURS_OUT = Double.parseDouble(Res.getString("END_DED_HR").toString());
		     			          DEDHOURS_NET = Double.parseDouble(Res.getString("DEDHOURS_NET").toString()) + DEDHOURS_OUT;
		     			          DEDHOURS_IN = Double.parseDouble(Res.getString("DEDHOURS_NET").toString());
		     			          
		     			          if (Res.getString("SHIFT") != null && Res.getString("SHIFT").equalsIgnoreCase("Morning Shift")) {
		     			                if (DEDHOURS_NET > 8.0D || DEDHOURS_NET > 8.0D) {
		     			                    DEDHOURS_NET = 8.0D;
		     			                }
		     			          } else if (Res.getString("SHIFT") != null && Res.getString("SHIFT").equalsIgnoreCase("Second Shift")) {
		     			                if (DEDHOURS_NET > 8.0D || DEDHOURS_NET > 8.0D) {
		     			                    DEDHOURS_NET = 8.0D;
		     			                }
		     			          } else if (Res.getString("SHIFT") != null && Res.getString("SHIFT").equalsIgnoreCase("Night Shift")) {
		     			                if (DEDHOURS_NET > 8.0D || DEDHOURS_NET > 8.0D) {
		     			                    DEDHOURS_NET = 8.0D;
		     			                  
		     			                }
		     			              
		     			          }
		     			          else if (Res.getString("SHIFT") != null && (Res.getString("SHIFT").equalsIgnoreCase("Ev") || Res.getString("SHIFT").equalsIgnoreCase("evening Shift") || Res.getString("SHIFT").equalsIgnoreCase("Evening Shift"))) {
		     			                if (DEDHOURS_NET > 12.0D || DEDHOURS_NET > 12.0D) {
		     			                    DEDHOURS_NET = 12.0D;
		     			                }
		     			          }
		     			          else if (DEDHOURS_NET > 9.0D || DEDHOURS_NET > 9.0D) {
		     			                DEDHOURS_NET = 9.0D;
		     			          } 


		     			          
		     			          if (Res.getString("DAYVIEW").toString().equalsIgnoreCase(Res.getString("Newdate").toString()) || 
		     			                  Res.getString("ATT_FLAG").equalsIgnoreCase("A")) {
		     			                
		     			                Doj.put("DEDHOURS_NET", Double.valueOf(0.0D));
		     			                DEDHOURS_NET = 0.0D;
		     			          } 

		     			          
		     			          TotalNetDed_hours += DEDHOURS_NET;
		     			         // TotalNetDed_hours=Math.round(TotalNetDed_hours);
		     			         
		     			    } catch (Exception ers) {
		     			          System.out.println("DEDHOURS_NET ::" + ers.getMessage());
		     			    } 
		     			    
		     			  
		     			    
		     			  //  System.out.println(Res.getString("STATUS")+"11111");
		     			    
		     			    if (DEDHOURS_NET > 0.0D&&Res.getString("STATUS").equalsIgnoreCase("Request")) {

		     			          Doj.put("ROCOLOR", "#F7D358");
		     			          inoutflg = true;
		     			         DISPLAYCOLOR=COLORS.get("REQUEST").toString().split("&")[0];
             		    		 DISPLAYNAME=COLORS.get("REQUEST").toString().split("&")[1];

		     			        System.out.println("DED"+DEDHOURS_NET);
		     			        
		     			       
		     			    }
		     			    else {


		     			          
		     			          inoutflg = false;
		     			          Doj.put("ROCOLOR", "");
		     			          
		     			         DISPLAYCOLOR=COLORS.get("WORKINGDAY").toString().split("&")[0];
             		    		// DISPLAYNAME=COLORS.get("WORKINGDAY").toString().split("&")[1];
		     			          //DISPLAYNAME=COLORS.get("WORKINGDAY").toString().split("&")[1];
		     			         
		     			         DISPLAYNAME=Res.getString("STATUS");
		     			        
             		    		 DISPLAYINOUT="block";
		     			    } 
		     			   // System.out.println("TotalNetDed_hours1::" + TotalNetDed_hours);
		     			    if (DEDHOURS_IN <= 0.5D && DEDHOURS_IN > 0.0D && DEDHOURS_OUT <= 0.0D && looHalfHourCount <= 2) {
		     			          inoutflg_half = true;
		     			          looHalfHourCount++;
		     			          if (TotalNetDed_hours >= DEDHOURS_IN) {
		     			                TotalNetDed_hours -= DEDHOURS_IN;
		     			          }
		     			          Doj.put("ROCOLOR", "");
		     			    } else {
		     			          inoutflg_half = false;
		     			    } 
		     			    //System.out.println("TotalNetDed_hours2::" + TotalNetDed_hours);
		     			  DecimalFormat df1 = new DecimalFormat( "#.##" );
		     			    Doj.put("DEDHOURS_NET", df1.format(DEDHOURS_NET));

		     			 


		     			    
		     			    try {
		     			          Doj.put("LESSHRS", Res.getString("DIFFHOURS").replace("#", ""));
		     			    }
		     			    catch (Exception err) {
		     			          Doj.put("LESSHRS", "00:00:00");
		     			          System.out.println("DIFFHOURS ~~~" + err);
		     			    } 
		     			    if (Res.getString("FIN").equalsIgnoreCase("00:00:00") && Res.getString("FOUT").equalsIgnoreCase("00:00:00"))
		     			    {
		     			          Doj.put("LESSHRS", "00:00:00");
		     			    }
		     			    
		     			    Doj.put("SHIFT", Res.getString("SHIFT"));
		     			    
		     			    if (Res.getString("SHIFT") != null && Res.getString("SHIFT").equalsIgnoreCase("Morning Shift")) {
		     			          Doj.put("SHIFT", "A");
		     			    } else if (Res.getString("SHIFT") != null && Res.getString("SHIFT").equalsIgnoreCase("Second Shift")) {
		     			          Doj.put("SHIFT", "B");
		     			    } else if (Res.getString("SHIFT") != null && Res.getString("SHIFT").equalsIgnoreCase("Night Shift")) {
		     			          Doj.put("SHIFT", "C");
		     			    } else if (Res.getString("SHIFT") != null && (Res.getString("SHIFT").equalsIgnoreCase("Ev") || Res.getString("SHIFT").equalsIgnoreCase("evening Shift") || Res.getString("SHIFT").equalsIgnoreCase("Evening Shift"))) {
		     			          Doj.put("SHIFT", "Ev");
		     			    } else {
		     			          Doj.put("SHIFT", "G");
		     			    } 




		     			    
		     			    boolean flagRequest = false;


		     			    
		     			    try {
		     			          flagRequest = RequestEnabled(Res.getString("DAY").toString(), UserMap.get("FROMDATE").toString(), UserMap.get("TODATE").toString());
		     			    } catch (Exception exception) {}

		     			    
		     			    if ((Res.getString("DAYTYPE").equalsIgnoreCase("WDAY") || Res.getString("DAYTYPE").equalsIgnoreCase("WOFF") || 
		     			              Res.getString("DAYTYPE").equalsIgnoreCase("HL")) && Res.getString("STATUS").equalsIgnoreCase("No Request") && 
		     			              !Res.getString("FIN").equalsIgnoreCase("00:00:00") && !Res.getString("FOUT").equalsIgnoreCase("00:00:00") && (
		     			               Res.getString("DEDHOURS").equalsIgnoreCase("true") || inoutflg)) {

		     			          
		     			          if (!Res.getString("DAYVIEW").toString().equalsIgnoreCase(Res.getString("Newdate").toString()))
		     			          {
    
		     			                Doj.put("DAF", " ");
		     			                LABLE="1";
		     			          }
		     			          else {
		     			                
		     			                Doj.put("DAF", "none");
		     			          } 
		     			          if (UserMap.get(Res.getString("DAY")) != null && UserMap.get(Res.getString("DAY")).toString().equalsIgnoreCase(Res.getString("DAY")) && 
		     			                 !Res.getString("DAYVIEW").toString().equalsIgnoreCase(Res.getString("Newdate").toString())) {

		     			                
		     			                if (inoutflg_half) {
		     			                  Doj.put("DAF", "none");
		     			                   Doj.put("DAFMAIN", "false");
		     			                } else {
		     			                   Doj.put("DAF", " ");
		     			                  LABLE="1";
		     			                   Doj.put("DAFMAIN", "true");
		     			                } 
		     			          } else {
		     			                
		     			                Doj.put("DAF", "none");
		     			          } 
		     			          
		     			          if (UserMap.get(String.valueOf(Res.getString("DAY")) + "_HOURS") != null && UserMap.get(String.valueOf(Res.getString("DAY")) + "_HOURS").toString().equalsIgnoreCase(Res.getString("DAY"))) {
		     			                Doj.put("DAFH", "");
		     			          }
		     			    } else {
		     			          
		     			          Doj.put("DAF", "none");
		     			    } 
		     			    
		     			    if (UserMap.get(String.valueOf(Res.getString("DAY")) + "_HOURS") != null && UserMap.get(String.valueOf(Res.getString("DAY")) + "_HOURS").toString().equalsIgnoreCase(Res.getString("DAY")) && 
		     			              UserMap.get(Res.getString("DAY")) != null && UserMap.get(Res.getString("DAY")).toString().equalsIgnoreCase(Res.getString("DAY")) && 
		     			              Res.getString("STATUS").equalsIgnoreCase("No Request")) {
		     			          
		     			          Doj.put("DAFH", "");
		     			          LABLE="1";
		     			          if (UserMap.get(String.valueOf(Res.getString("DAY")) + "_HOURSF") != null && UserMap.get(String.valueOf(Res.getString("DAY")) + "_HOURSF").toString().equalsIgnoreCase(Res.getString("DAY"))) {
		     			                Doj.put("DAF", "");
		     			                LABLE="1";
		     			          }
		     			    } 

		     			    
		     			    if (UserMap.get(String.valueOf(Res.getString("DAY")) + "_HOURS") != null && UserMap.get(String.valueOf(Res.getString("DAY")) + "_HOURS").toString().equalsIgnoreCase(Res.getString("DAY")))
		     			    {
		     			          Doj.put("DAFH", "");
		     			    }
		     			    
		     			    if (UserMap.get(String.valueOf(Res.getString("DAY")) + "_HOURS") != null && UserMap.get(String.valueOf(Res.getString("DAY")) + "_HOURS").toString().equalsIgnoreCase(Res.getString("DAY")))
		     			    {
		     			          Doj.put("DAFH", "");
		     			    }


 
		     			    Doj.put("TOTAL_DD_HOURS", df1.format(TotalNetDed_hours));
		     			    
		     			    Doj.put("ROCOLOR", "");
		     			    
		     			    
		     			    
		     			    if(Res.getString("DAYTYPE").equalsIgnoreCase("WOFF"))
		     			    {
		     			    	     DISPLAYCOLOR=COLORS.get("WOFF").toString().split("&")[0];
	             		    		 DISPLAYNAME=COLORS.get("WOFF").toString().split("&")[1];
		     			    }
		     			    
		     			     if(Res.getString("DAYTYPE").equalsIgnoreCase("HL"))
		     			    {
		     			    	     DISPLAYCOLOR=COLORS.get("HL").toString().split("&")[0];
	             		    		 DISPLAYNAME=COLORS.get("HL").toString().split("&")[1];
		     			    }
		     			     
		     			     /// 
		     			     
		     			     if(DEDHOURS_NET==0.0D)
		     			     {
		     			    	 LABLE="0";
		     			     }
		     			     
		     			     
		     			     
		     			     if(Res.getString("STATUS").equalsIgnoreCase("Approved"))
		     			     {
		     			    	 
		     			    	  if(!Res.getString("DAYTYPE").equalsIgnoreCase("WDAY")&&!Res.getString("DAYTYPE").equalsIgnoreCase("HL"))
				     			    {
				     			    	  DISPLAYCOLOR=COLORS.get("LEAVE").toString().split("&")[0];
		              		    		  DISPLAYNAME="Approved";
				     			    } 
				     			    
				     			   if(Res.getString("DAYTYPE").equalsIgnoreCase("WDAY"))
				     			    {
				     				  DISPLAYCOLOR=COLORS.get("REQUESTACCEPTED").toString().split("&")[0];
                 		    		  DISPLAYNAME=COLORS.get("REQUESTACCEPTED").toString().split("&")[1];
				     			    } 
		     			     }
		     			     else if(Res.getString("STATUS").equalsIgnoreCase("Processed")||Res.getString("STATUS").equalsIgnoreCase("Pending"))
		     			     {
		     			    	  DISPLAYCOLOR=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[0];
        		    		      DISPLAYNAME=COLORS.get("PENDINGFORAPPROVAL").toString().split("&")[1];
		     			     }
		     			     
		     			    else if(Res.getString("STATUS").equalsIgnoreCase("Reject"))
		     			     {
		     			    	  DISPLAYCOLOR=COLORS.get("REQUESTACCEPTED").toString().split("&")[0];
       		    		          DISPLAYNAME=COLORS.get("REQUESTACCEPTED").toString().split("&")[1];
		     			     }
		     			  
		     			    
		     			   // Reject
		     			     
		     			     
		     			     
		     			     
		     			     
		     			     
		     			     
		     			     
		     			    if(LABLE.equalsIgnoreCase("1")&&DEDHOURS_NET!=0.0D)
	           		    	{
	           		    		
	           		    	   DISPLAYCOLOR=COLORS.get("REQUEST").toString().split("&")[0];
         		    		   DISPLAYNAME=COLORS.get("REQUEST").toString().split("&")[1];
	           		    	}
		     			     
		     			    
		     			    
		     			   if(comparedateTodayformat.equalsIgnoreCase(Res.getString("DAYVIEW"))&&Res.getString("DAYTYPE").equalsIgnoreCase("WDAY")&&(Res.getString("FIN")!="00:00:00"||Res.getString("FOUT")!="00:00:00"))
	           		    	{
	  		    		       DISPLAYCOLOR=COLORS.get("INOFFICE").toString().split("&")[0];
	 		    		       DISPLAYNAME=COLORS.get("INOFFICE").toString().split("&")[1];
	 		    		       DISPLAYINOUT="block";
	 		    		       
	 		    		       if(Res.getString("FIN")!="00:00:00"&&Res.getString("FOUT")!="00:00:00")
	 		    		       {
	 		    		    	  if(Res.getString("FIN").equalsIgnoreCase(Res.getString("FOUT")))
	 		    		    	  {
	 		    		    		 //att_out="00:00:00";  
	 		    		    		 
	 		    		    		  Doj.put("out","00:00:00");
	 		    		    	  }
	 		    		       }
	 		    		       
	           		    	} 
	           		    	else
	         		    	 {
	         		    		 /// Same punch daily
	       	    		    	  if(Res.getString("FIN").equalsIgnoreCase(Res.getString("FOUT")))
	       	    		    	  {
	       	    		    		  Doj.put("out","00:00:00");  
	       	    		    	  }
	       	    		       
	         		    	 }
		     			    
		     			    
		     			    
		     			    
		     			    
		     			    
		     			    /// Attendance Requeste
		     			   
		     			   
		     			
		     			    
		     			    
		     			    
		     			    
		     			    
		     			   
		     			   Doj.put("LABLE", LABLE);
		     			   Doj.put("DISPLAYCOLOR", DISPLAYCOLOR);
		     			   Doj.put("DISPLAYNAME", DISPLAYNAME);
		     			   Doj.put("DISPLAYINOUT", DISPLAYINOUT);
		                	 
		     			   final_attendance_arr.add(Doj); 
		                	 
		                      
		                 }
						return final_attendance_arr;
		             }
		         });

		         jdbcTemplate.getDataSource().getConnection().close();
		
		         return final_attendance_arr; 
}
		
		 public static boolean RequestEnabled(String attnDate, String fromPayperiod, String toPayperiod) {
				    Date d1 = null;
				     Date d2 = null;
				    Date d3 = null;
				   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				    boolean isBetween = false;
			    try {
				  d1 = format.parse(fromPayperiod);
			      d2 = format.parse(toPayperiod);
				     d3 = format.parse(attnDate);
				     } catch (Exception e) {
				     e.printStackTrace();
				    } 
				 
				      
			    if (d3.compareTo(d1) >= 0 && d3.compareTo(d2) <= 0) {
				       isBetween = true;
			    }
				    
			     System.out.println(String.valueOf(attnDate) + "~" + fromPayperiod + "~" + toPayperiod + "~" + isBetween);
				    
				 return isBetween;
			   }
}
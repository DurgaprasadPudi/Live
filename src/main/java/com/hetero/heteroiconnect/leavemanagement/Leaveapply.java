package com.hetero.heteroiconnect.leavemanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;

@Repository
public class Leaveapply {
  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Autowired  
  private DataSource dataSource;
  
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }
  
  KeyHolder holder = (KeyHolder)new GeneratedKeyHolder();
  
  int PeandingLeave = 0;
  
  int Unic_Leave = 0;
  
  int lopins_count = 0;
  
  String EMPLOYEEID_LOP = null, LOP_ydate = null;
  
  String Fy_Year = "2025";
  
  public Map ApplyLeave(Leave leaves) throws SQLException {
    String OTHFLAG = "N";
    boolean Holiday_dates_for_month_flg = false;
    boolean ConnFlag = true;
    int User_Auth = 0;
    JSONArray values = new JSONArray();
    Map<Object, Object> Leave_New = new HashMap<>();
    ArrayList LeaveUnicInsertion = new ArrayList();
    Map<Object, Object> LeaveUnicInsertion_map = new HashMap<>();
    double Holiday_Count=0,LeaveCount=0 ,Leave_Diff=0;
	 
    boolean insertionFlag = false;
    StringBuffer Holiday_dates_for_month = new StringBuffer();
    double DayMode = 0;
    int LOP_ID = 0;
    int User_Auth_auth = 1;
    double prev_holidays_woff = 0, prev_btw_workingdays = 0, feature_holidays_woff = 0;
    double feature_btw_workingdays = 0;
    String BuID = "";
    String Leave_Type = null, from_date = null, to_date = null, HalfDay = null, Hal_date = null;
    String compoff = null, comm_date = null, to_mail = null, cc_mail = null, subject = null, reason = null, FROMDATE = null, TODATE = null;
    String Atten_Req_Message = "Failed to process your request please contact system admin.";
    String Flag = "0";
    String HR_ATT = null, HR_ATT_USER = null, From_loc = null, To_loc = null;
    int newyearflag_f = 0;
    int newyearflag_t = 0;
    int newyearflag_f_old = 0;
    int newyearflag_t_old = 0;
    String username = null;
    Map<Object, Object> EMAILDATA = new HashMap<>();
    String HR_MAIL = " ", EMP_MAIL = " ";
    try {
      HR_MAIL = EMAILDATA.get("BULOC").toString();
    } catch (Exception Err) {
      System.out.println("Email fetching Error" + Err);
    } 
    ResourceBundle bundle_info = null;
    try {
      this.Fy_Year = bundle_info.getString("Fy_Year");
    } catch (Exception exception) {}
    Map<Object, Object> EmpData = new HashMap<>();
    double maxleave = 0;
    System.out.println(leaves.toString());
    Leave_Type = leaves.getLeave_type();
    from_date = leaves.getFrom_date();
    to_date = leaves.getTo_date();
    HalfDay = leaves.getHalfday();
    Hal_date = leaves.getHal_date();
    if (!Hal_date.equalsIgnoreCase("0000-00-00") && HalfDay.equalsIgnoreCase("false"))
      HalfDay = "1st Half"; 
    compoff = leaves.getCompoff();
    comm_date = leaves.getComm_date();
    to_mail = leaves.getTo_mail();
    cc_mail = leaves.getCc_mail();
    subject = leaves.getSubject();
    reason = leaves.getReason();
    HR_ATT = leaves.getHr_att();
    HR_ATT_USER = leaves.getHr_att_user();
    From_loc = leaves.getFrom_loc();
    To_loc = leaves.getTo_loc();
    OTHFLAG = leaves.getOthflag();
    EMP_MAIL = leaves.getEmpEmail();
    username = leaves.getEmpID();
    maxleave = leaves.getMaxleave();
    System.out.println(String.valueOf(OTHFLAG) + "###############");
    HR_ATT = null;
    if (HR_ATT == null)
      HR_ATT = "OLD"; 
    if (HR_ATT == "OLD")
      try {
        if (subject == null)
          subject = "NA"; 
        if (Leave_Type.equalsIgnoreCase("OD")) {
          subject = subject.concat(" (").concat(Leave_Type).concat(":").concat(From_loc).concat("-" + To_loc).concat(")");
        } else {
          subject = subject.concat(" (").concat(Leave_Type).concat(")");
        } 
      } catch (Exception Err) {
        Err.printStackTrace();
      }  
    try {
      to_mail = to_mail.replaceAll(";", ",");
      cc_mail = cc_mail.replaceAll(";", ",");
    } catch (Exception Err) {
      System.out.println("Error At Leave Management::" + Err);
    } 
    Map<Object, Object> FetchRc = new HashMap<>();
    StringBuffer biodata = new StringBuffer();
    String message = null;
    String LIMIT_LEAVE_ICONN_YEAR = null;
    String LIMIT_LEAVE_ICONN_YEAR_MSG = null;
    String LIMIT_LEAVE_ICONN_YEAR_OLD = null;
    String LIMIT_LEAVE_ICONN_YEAR_MSG_OLD = null;
    System.out.println("Leave_Type" + Leave_Type);
    StringBuffer StrReverse = new StringBuffer();
    
    StrReverse.append(" select date_format(STR_TO_DATE('" + from_date + "' ,'%d-%m-%Y'),'%Y-%m-%d') , date_format(STR_TO_DATE('" + to_date + "' ,'%d-%m-%Y'),'%Y-%m-%d'), ifnull(date_format(STR_TO_DATE('" + Hal_date + "' ,'%d-%m-%Y'),'%Y-%m-%d'),'0000-00-00') ");
     StrReverse.append(" , if(date_format(STR_TO_DATE('" + from_date + "' ,'%d-%m-%Y'),'%Y-%m-%d')>='" + LIMIT_LEAVE_ICONN_YEAR + "' ,1,0) , if(date_format(STR_TO_DATE('" + to_date + "' ,'%d-%m-%Y'),'%Y-%m-%d')>='" + LIMIT_LEAVE_ICONN_YEAR + "' ,1,0) ,  ");
     StrReverse.append(" if(date_format(STR_TO_DATE('" + from_date + "' ,'%d-%m-%Y'),'%Y-%m-%d')<'" + LIMIT_LEAVE_ICONN_YEAR_OLD + "' ,1,0) , if(date_format(STR_TO_DATE('" + to_date + "' ,'%d-%m-%Y'),'%Y-%m-%d')<'" + LIMIT_LEAVE_ICONN_YEAR_OLD + "' ,1,0)  ");
     StrReverse.append(" from dual ");
    
//    StrReverse.append("SELECT DATE_FORMAT(STR_TO_DATE('" + from_date + "', '%Y-%m-%d'), '%Y-%m-%d'), ");
//    StrReverse.append("DATE_FORMAT(STR_TO_DATE('" + to_date + "', '%Y-%m-%d'), '%Y-%m-%d'), ");
//    StrReverse.append("IFNULL(DATE_FORMAT(STR_TO_DATE('" + Hal_date + "', '%Y-%m-%d'), '%Y-%m-%d'), '0000-00-00'), ");
//    StrReverse.append("IF(DATE_FORMAT(STR_TO_DATE('" + from_date + "', '%Y-%m-%d'), '%Y-%m-%d') >= '" + LIMIT_LEAVE_ICONN_YEAR + "', 1, 0), ");
//    StrReverse.append("IF(DATE_FORMAT(STR_TO_DATE('" + to_date + "', '%Y-%m-%d'), '%Y-%m-%d') >= '" + LIMIT_LEAVE_ICONN_YEAR + "', 1, 0), ");
//    StrReverse.append("IF(DATE_FORMAT(STR_TO_DATE('" + from_date + "', '%Y-%m-%d'), '%Y-%m-%d') < '" + LIMIT_LEAVE_ICONN_YEAR_OLD + "', 1, 0), ");
//    StrReverse.append("IF(DATE_FORMAT(STR_TO_DATE('" + to_date + "', '%Y-%m-%d'), '%Y-%m-%d') < '" + LIMIT_LEAVE_ICONN_YEAR_OLD + "', 1, 0) ");
//    StrReverse.append("FROM dual;");

    
    
   
    System.out.println(StrReverse.toString());
    try {
      List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(StrReverse.toString());
      for (Map<String, Object> row : rows) {
        to_date = row.get(Integer.valueOf(2)).toString();
        Hal_date = row.get(Integer.valueOf(3)).toString();
        newyearflag_f = Integer.parseInt(row.get(Integer.valueOf(4)).toString());
        newyearflag_t = Integer.parseInt(row.get(Integer.valueOf(5)).toString());
        newyearflag_f_old = Integer.parseInt(row.get(Integer.valueOf(6)).toString());
        newyearflag_t_old = Integer.parseInt(row.get(Integer.valueOf(7)).toString());
      } 
    } catch (Exception err) {
      System.out.println("Exception at reverse DUrga" + err);
    } 
    StringBuffer payperiod = new StringBuffer();
    StringBuffer Leave_Monthly_validation = new StringBuffer();
    String Company_id = leaves.getBuid();
    payperiod.append(" SELECT if('" + from_date + "' > '" + to_date + "',1,0) As DateValidation,if('" + from_date + "'<= now() AND '" + to_date + "'<= now() ,1,0) As DateValidationSL, ifnull(FROMDATE,'0000-00-00') FROMDATE ,ifnull(TODATE,'0000-00-00') TODATE FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES ");
    payperiod.append(" WHERE BUSINESSUNITID IN (" + Company_id + ") AND ");
    payperiod.append(" TRANSACTIONDURATION IN (SELECT MAX(TRANSACTIONDURATION) FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES where BUSINESSUNITID=" + Company_id + " ) AND TRANSACTIONTYPEID=1 ");
    System.out.println("payperiod::" + payperiod.toString());
    try {
      List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(payperiod.toString());
      for (Map<String, Object> row : rows) {
        EmpData.put("FROMDATE", row.get("FROMDATE").toString());
        EmpData.put("TODATE", row.get("TODATE").toString());
        EmpData.put("TODATE_VALID", row.get("DateValidation").toString());
        if (Leave_Type.equalsIgnoreCase("SL")) {
          EmpData.put("DATE_VALID_SL", row.get("DateValidationSL").toString());
          continue;
        } 
        EmpData.put("DATE_VALID_SL", "1");
      } 
    } catch (Exception err) {
      System.out.println("Error At Get Leave Quota" + err);
    } 
    Map<Object, Object> clmap = new HashMap<>();
    clmap.put("LEAVEADD", Integer.valueOf(0));
    if (Leave_Type != null && Leave_Type.equalsIgnoreCase("CL"))
      try {
        clmap = CL_Validation(leaves);
        System.out.println(clmap + "HI I AM IN CL VALIDATION");
      } catch (Exception err) {
        err.printStackTrace();
      }  
    boolean Evvalid = false;
    String BU_Rights="";
    int EL_Valid = 0;
    StringBuffer EL_Validation = new StringBuffer();
    if (Leave_Type != null && Leave_Type.equalsIgnoreCase("EL")) {
     // EL_Validation.append(" SELECT count(*) COUNT FROM hclhrm_prod_others.tbl_emp_attn_req A ");
     // EL_Validation.append("  join hclhrm_prod_others.tbl_emp_leave_req b on b.rid=A.rid ");
     // EL_Validation.append("  where A.employeeid=" + leaves.getEmpID() + " and b.leave_type='EL' ");
     // EL_Validation.append("  and date_format(A.req_date,'%Y')=" + this.Fy_Year + " and a.flag in('P','A') ;  ");
      
      EL_Validation.append(" SELECT count(*) COUNT,BU.CALLNAME FROM hclhrm_prod_others.tbl_emp_attn_req A ");
      EL_Validation.append("  join hclhrm_prod_others.tbl_emp_leave_req b on b.rid=A.rid ");
      EL_Validation.append("  left join hclhrm_prod.tbl_employee_primary P ON P.employeesequenceno=A.employeeid");
      EL_Validation.append("  left join hcladm_prod.tbl_businessunit BU on BU.BUSINESSUNITID=p.COMPANYID ");
      EL_Validation.append("  where A.employeeid=" + leaves.getEmpID() + " and b.leave_type='EL' ");
      EL_Validation.append("  and date_format(A.req_date,'%Y')=" + this.Fy_Year + " and a.flag in('P','A') ;  ");
       
      
      System.err.println(EL_Validation.toString()+"EL_Validation");
      try {
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(EL_Validation.toString());
        for (Map<String, Object> row : rows) {
          EL_Valid = Integer.parseInt(row.get("COUNT").toString());
          BU_Rights =row.get("CALLNAME").toString();
          
          //// HYD All BU'S 
          
          if(BU_Rights.equalsIgnoreCase("MUM"))
          {
        	  if (EL_Valid >= 5)
        		  
                  Evvalid = true; 
                
                System.out.println("HHHHHH DURGA PRASAD");
            
        	  
          }
          else {
        	   
        	  if (EL_Valid >= 3)
                  Evvalid = true; 
                
                System.out.println("HHHHHH DURGA PRASAD");
             
          }
         
        
        }
      } catch (Exception err) {
        System.out.println("Error At Get Leave Quota" + err);
      } 
    } else {
      Evvalid = false;
    } 
    Leave_Monthly_validation.append(" SELECT COUNT(*) COUNT");
    Leave_Monthly_validation.append(" FROM ");
    Leave_Monthly_validation.append(" HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A ");
    Leave_Monthly_validation.append(" LEFT JOIN ");
    Leave_Monthly_validation.append(" HCLHRM_PROD_OTHERS.TBL_EMP_ATTN_REQ C ON A.EMPLOYEESEQUENCENO=C.EMPLOYEEID ");
    Leave_Monthly_validation.append(" LEFT JOIN ");
    Leave_Monthly_validation.append(" HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_REQ D ON C.RID=D.RID ");
    Leave_Monthly_validation.append(" LEFT JOIN ");
    Leave_Monthly_validation.append(" HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_REPORT E ON C.RID=E.RID ");
    Leave_Monthly_validation.append(" WHERE ");
    Leave_Monthly_validation.append(" A.EMPLOYEESEQUENCENO IN (" + leaves.getEmpID() + ") AND C.FLAG IN ('P','A') AND C.STATUS=1001  AND");
    Leave_Monthly_validation.append(" E.LEAVEON BETWEEN '" + EmpData.get("FROMDATE").toString() + "' AND '" + EmpData.get("TODATE").toString() + "' AND D.LEAVE_TYPE NOT IN ('LOP','OD','WFH','CF') AND D.LEAVE_TYPE='" + Leave_Type + "' ");
    System.out.println("Leave_Monthly_validation::" + Leave_Monthly_validation.toString());
    try {
      List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(Leave_Monthly_validation.toString());
      for (Map<String, Object> row : rows)
        EmpData.put("MONTH_EXIST_LEAVE", row.get("COUNT").toString()); 
    } catch (Exception err) {
      System.out.println("Error At Get Leave Quota" + err);
    } 
    StringBuffer NewEmplFlag = new StringBuffer();
    NewEmplFlag.append(" select a.employeeid,if(ifnull(B.leavetypeid,0)=15,B.leavetypeid,0) leaveid,date_format(now(),'%Y') ydate from hclhrm_prod.tbl_employee_primary a ");
    NewEmplFlag.append(" left join( ");
    NewEmplFlag.append(" select leavetypeid,employeeid from hclhrm_prod_others.tbl_emp_leave_quota where leavetypeid=15 and status=1001 ");
    NewEmplFlag.append(" group by employeeid ");
    NewEmplFlag.append(" )B on b.employeeid=a.employeeid where a.employeesequenceno in(" + leaves.getEmpID() + ") ");
    if (Leave_Type.equalsIgnoreCase("LOP"))
      try {
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(NewEmplFlag.toString());
        for (Map<String, Object> row : rows) {
          LOP_ID = Integer.parseInt(row.get("leaveid").toString());
          this.EMPLOYEEID_LOP = row.get("employeeid").toString();
          this.LOP_ydate = row.get("ydate").toString();
        } 
      } catch (Exception err) {
        System.out.println("Error At Get Leave Quota" + err);
      }  
    if (LOP_ID == 0 && this.EMPLOYEEID_LOP != null && Leave_Type.equalsIgnoreCase("LOP")) {
       
    	try {
			   
			 jdbcTemplate.update(
					    new PreparedStatementCreator() {
					        public PreparedStatement createPreparedStatement(Connection connection)
					            throws SQLException {
					        	
					        	connection.setAutoCommit(false);

					            PreparedStatement statement = connection.prepareStatement("INSERT INTO HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_QUOTA (EMPLOYEEID, LEAVETYPEID, YEAR, QUANTITY, AVAILABLEQTY, USEDQTY, HOLD, DAYMODE, MAXLEAVE, BACKDATE, COUNT_WOFF, COUNT_HOLIDAY, STATUS, LOGID, CREATEDBY, LEAVE_MODE) "
					+ "VALUES(?,15,?,0.0,0.0,0.0,0.0,1,90,4,0,0.0,1001,0,0,'OP')");
					            //statement.setLong(1, beginning); set parameters you need in your insert

					            
					            statement.setString(1,EMPLOYEEID_LOP);
					            statement.setString(2,""+Fy_Year+""); // for year changes
								lopins_count= statement.executeUpdate();
								System.out.println("lopins_count" +lopins_count);
								if(lopins_count>0){
									//conn.setAutoCommit(true);
									connection.commit();
								}else{
									connection.rollback();
								}   
					            
					            
					            return statement;
					        }
					    });
			
			 
			
		 } catch(Exception err){
			System.out.println("HI This Is New"+err);
		}
			
			
    } else {
      this.lopins_count = 1;
    } 
    if (LOP_ID == 15)
      this.lopins_count = 1; 
    StringBuffer Leave_Quota = new StringBuffer();
    StringBuffer From_toDaysCal = new StringBuffer();
    Leave_Quota.append(" select B.employeeid,C.employeesequenceno,trim(A.SHORTNAME) SHORTNAME,B.quantity , B.AVAILABLEQTY , B.HOLD, B.quantity+B.HOLD as totalavl, b.USEDQTY, trim(A.NAME) Fullname,B.DAYMODE,if(B.daymode=0,if(B.availableqty<=B.maxleave,B.availableqty,B.maxleave),B.maxleave) MAXLEAVE_C ,B.COUNT_WOFF,B.BACKDATE, B.Leavetypeid , ifnull(B.FOR_MONTH,0) FOR_MONTH from  hclhrm_prod.tbl_leave_type A,  ");
    Leave_Quota.append(" hclhrm_prod_others.tbl_emp_leave_quota B, ");
    Leave_Quota.append(" hclhrm_prod.tbl_employee_primary C ");
    Leave_Quota.append(" where B.employeeid=C.employeeid and C.employeesequenceno in(" + leaves.getEmpID() + ") and ");
    Leave_Quota.append(" B.Leavetypeid=A.Leavetypeid  and b.status=1001 and A.SHORTNAME='" + Leave_Type + "' ");
    boolean LevFlag = false;
    try {
      System.out.println("******" + Leave_Quota.toString());
      List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(Leave_Quota.toString());
      for (Map<String, Object> row : rows) {
        Leave_New.put(String.valueOf(leaves.getEmpID()) + "_EMPID", row.get("employeeid"));
        Leave_New.put(String.valueOf(leaves.getEmpID()) + "_SEQ", row.get("employeesequenceno"));
        Leave_New.put(String.valueOf(leaves.getEmpID()) + "_" + row.get("Leavetypeid"), row.get("AVAILABLEQTY"));
        Leave_New.put(String.valueOf(leaves.getEmpID()) + "_AVAILABLEQTY", row.get("AVAILABLEQTY"));
        Leave_New.put(String.valueOf(leaves.getEmpID()) + "COUNT_WOFF", row.get("COUNT_WOFF"));
        Leave_New.put(String.valueOf(leaves.getEmpID()) + "_DAYMODE", row.get("DAYMODE"));
        Leave_New.put("LEAVE_TYPE_ID", row.get("Leavetypeid"));
        Leave_New.put("FOR_MONTH", row.get("FOR_MONTH"));
        Leave_New.put("MAXLEAVE", row.get("MAXLEAVE_C"));
      } 
      System.out.println(Leave_New.toString());
    } catch (Exception err) {
      System.out.println("Error At Get Leave Quota" + err);
    } 
    if (BuID.equalsIgnoreCase("151") || BuID.equalsIgnoreCase("161")) {
      From_toDaysCal.append(" select selected_date,IF(DAYNAME(selected_date)='Saturday','WOFF','WDAY') DAYTYPE, ");
      From_toDaysCal.append(" ifnull(V2.holidaydate,'0000-00-00') holidaydate,  ");
      From_toDaysCal.append(" ((datediff('" + to_date + "','" + from_date + "')+1)-(if('" + to_date + "'='" + Hal_date + "' OR '" + from_date + "'='" + Hal_date + "' ,0.5,0))) AS DAYESDIFF,'" + from_date + "' as fromdate,'" + to_date + "' as todate, ");
      From_toDaysCal.append(" if(DAYNAME(selected_date)='Saturday' && selected_date=ifnull(V2.holidaydate,'0000-00-00'),1,if(DAYNAME(selected_date)='Saturday',1,if(selected_date=ifnull(V2.holidaydate,'0000-00-00'),1,0))) AS HW  ");
    } else {
      From_toDaysCal.append(" select selected_date,IF(DAYNAME(selected_date)='SUNDAY','WOFF','WDAY') DAYTYPE, ");
      From_toDaysCal.append(" ifnull(V2.holidaydate,'0000-00-00') holidaydate,  ");
      From_toDaysCal.append(" ((datediff('" + to_date + "','" + from_date + "')+1)-(if('" + to_date + "'='" + Hal_date + "' OR '" + from_date + "'='" + Hal_date + "' ,0.5,0))) AS DAYESDIFF,'" + from_date + "' as fromdate,'" + to_date + "' as todate, ");
      From_toDaysCal.append(" if(DAYNAME(selected_date)='SUNDAY' && selected_date=ifnull(V2.holidaydate,'0000-00-00'),1,if(DAYNAME(selected_date)='SUNDAY',1,if(selected_date=ifnull(V2.holidaydate,'0000-00-00'),1,0))) AS HW  ");
    } 
    From_toDaysCal.append(" , '" + HalfDay + "' As HALFDAY, '" + Hal_date + "' As Hal_date FROM  ");
    From_toDaysCal.append(" (select adddate('1970-01-01',t4.i*10000 + t3.i*1000 + t2.i*100 + t1.i*10 + t0.i) selected_date from  ");
    From_toDaysCal.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0,  ");
    From_toDaysCal.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, ");
    From_toDaysCal.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2,  ");
    From_toDaysCal.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3,  ");
    From_toDaysCal.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v ");
    From_toDaysCal.append("  left join(   ");
    
    
   // From_toDaysCal.append("  SELECT DISTINCT b.HOLIDAYDATE 'holidaydate' FROM hclhrm_prod.tbl_employee_primary a ");
    //From_toDaysCal.append("  left join hclhrm_prod.tbl_holidays b on a.companyid=b.businessunitid  where employeesequenceno  in(" + leaves.getEmpID() + ") and b.statusid=1001  ");
   
    
    From_toDaysCal.append(" SELECT DISTINCT hd.holidaydate 'holidaydate' ");
    From_toDaysCal.append(" FROM hclhrm_prod.tbl_employee_primary p ");
    From_toDaysCal.append(" JOIN hclhrm_prod.tbl_employee_professional_details info ON info.employeeid = p.employeeid ");
    From_toDaysCal.append(" JOIN hcllcm_prod.tbl_location A ON A.LOCATIONID = info.worklocationid ");
    From_toDaysCal.append(" JOIN hcllcm_prod.tbl_location b ON A.parent = b.locationid AND b.locationtype = 1 ");
    From_toDaysCal.append(" JOIN hclhrm_prod.tbl_holiday_location_mapping map ON b.locationid = map.stateid AND map.BUSINESSUNITID = P.companyid ");
    From_toDaysCal.append(" JOIN hclhrm_prod.tbl_holidays hd ON map.holidayid = hd.holidayid ");
    From_toDaysCal.append(" WHERE hd.statusid = 1001 ");
    From_toDaysCal.append(" AND p.employeesequenceno = "+leaves.getEmpID()+"");

	 
    From_toDaysCal.append(" UNION ALL ");
 
    From_toDaysCal.append(" SELECT hd.holidaydate 'holidaydate' ");
    From_toDaysCal.append(" FROM hclhrm_prod.tbl_employee_primary p ");
    From_toDaysCal.append(" JOIN hclhrm_prod.tbl_holidays hd ON hd.businessunitid = p.companyid ");
    From_toDaysCal.append(" LEFT JOIN hclhrm_prod.tbl_holiday_location_mapping map ON map.holidayid = hd.holidayid ");
    From_toDaysCal.append(" WHERE hd.statusid = 1001 ");
    From_toDaysCal.append(" AND p.employeesequenceno = "+leaves.getEmpID()+" ");
	From_toDaysCal.append(" AND map.holidayid IS NULL ");
	From_toDaysCal.append(" AND NOT EXISTS ( ");
	From_toDaysCal.append(" SELECT 1");
	From_toDaysCal.append(" FROM hclhrm_prod.tbl_employee_primary p2 ");
	From_toDaysCal.append(" JOIN hclhrm_prod.tbl_employee_professional_details info2 ON info2.employeeid = p2.employeeid ");
	From_toDaysCal.append(" JOIN hcllcm_prod.tbl_location A2 ON A2.LOCATIONID = info2.worklocationid ");
	From_toDaysCal.append(" JOIN hcllcm_prod.tbl_location b2 ON A2.parent = b2.locationid AND b2.locationtype = 1 ");
	From_toDaysCal.append(" JOIN hclhrm_prod.tbl_holiday_location_mapping map2 ON b2.locationid = map2.stateid AND map2.BUSINESSUNITID = P2.companyid ");
	From_toDaysCal.append(" JOIN hclhrm_prod.tbl_holidays hd2 ON map2.holidayid = hd2.holidayid ");
	From_toDaysCal.append(" WHERE hd2.statusid = 1001 ");
	From_toDaysCal.append(" AND p2.employeesequenceno = "+leaves.getEmpID()+" ");
	From_toDaysCal.append(") ");
    
    ///////
    
    From_toDaysCal.append("  UNION ALL ");
    From_toDaysCal.append(" SELECT t.HOLIDAYDATE 'holidaydate' FROM  test.tbl_emp_saturday_policy st ");
    From_toDaysCal.append(" left join hclhrm_prod.tbl_employee_primary pp on pp.employeesequenceno=st.employee_seq ");
    From_toDaysCal.append(" left join test.tbl_holidays_special t on  pp.companyid=t.businessunitid  where pp.employeesequenceno in(" + leaves.getEmpID() + ") and t.statusid=1001  UNION ALL SELECT SP.HOLIDAYDATE FROM test.tbl_saturday_special SP where SP.HOLIDAYTYPEID in(SELECT COUNT(*) FROM test.tbl_emp_saturday_policy where employee_seq=" + leaves.getEmpID() + ") ");
    From_toDaysCal.append(" ) V2 on V2.holidaydate=V.selected_date ");
    From_toDaysCal.append(" where selected_date between '" + from_date + "' and '" + to_date + "' ");
    System.out.println("Query:: " + From_toDaysCal.toString());
    System.out.println("From_toDaysCal:: " + From_toDaysCal.toString());
    try {
      List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(From_toDaysCal.toString());
      for (Map<String, Object> row : rows) {
        Leave_Diff = Double.parseDouble(row.get("DAYESDIFF").toString());
        String SleDate = row.get("selected_date").toString();
        LeaveUnicInsertion.add(row.get("selected_date"));
        LeaveUnicInsertion_map.put(String.valueOf(SleDate) + "_DAYTYPE", row.get("DAYTYPE").toString());
        LeaveUnicInsertion_map.put(String.valueOf(SleDate) + "_fromdate", row.get("fromdate").toString());
        LeaveUnicInsertion_map.put(String.valueOf(SleDate) + "_todate", row.get("todate").toString());
        LeaveUnicInsertion_map.put(String.valueOf(SleDate) + "_HALFDAY", row.get("HALFDAY").toString());
        LeaveUnicInsertion_map.put(String.valueOf(SleDate) + "_Hal_date", row.get("Hal_date").toString());
        LeaveUnicInsertion_map.put(String.valueOf(SleDate) + "_subject", subject);
        LeaveUnicInsertion_map.put(String.valueOf(SleDate) + "_reason", reason);
        if (Double.parseDouble(Leave_New.get(String.valueOf(leaves.getEmpID()) + "COUNT_WOFF").toString()) > 0)
          Holiday_Count += Double.parseDouble(row.get("HW").toString()); 
        if (row.get("DAYTYPE").toString().equalsIgnoreCase("WOFF") || row.get("Hal_date") != null) {
          Holiday_dates_for_month_flg = true;
          Holiday_dates_for_month.append(row.get("selected_date"));
          Holiday_dates_for_month.append(", ");
        } 
      } 
    } catch (Exception err) {
      System.out.println("Quey From_toDaysCal Error At Get Leave Quota" + err);
    } 
    try {
      if (Holiday_dates_for_month_flg)
        Holiday_dates_for_month.append("0000-00-00"); 
    } catch (Exception err) {
      System.out.println("Holiday_dates_for_month::" + Holiday_dates_for_month.toString());
    } 
    StringBuffer PendingLeave = new StringBuffer();
    StringBuffer PendingApprovals = new StringBuffer();
    PendingLeave.append(" SELECT count(*) FROM hclhrm_prod_others.tbl_emp_attn_req  ");
    PendingLeave.append(" WHERE ");
    PendingLeave.append(" EMPLOYEEID in (" + leaves.getEmpID() + ") AND ");
    PendingLeave.append(" REQ_TYPE IN ('LR') AND FLAG IN ('P') ");
    PendingApprovals.append(" SELECT IF(COUNT(*)>1,1,COUNT(*))CNT FROM");
    PendingApprovals.append(" (SELECT B.EMPLOYEESEQUENCENO EmpCode,B.CALLNAME EmpName,DATE_FORMAT(SELECTED_DATE,'%d-%m-%Y') Day,A.FLAG,E.LEAVE_TYPE,");
    if (BuID.equalsIgnoreCase("151") || BuID.equalsIgnoreCase("161")) {
      PendingApprovals.append(" IF(SELECTED_DATE='Saturday','WOFF',");
    } else {
      PendingApprovals.append(" IF(SELECTED_DATE='SUNDAY','WOFF',");
    } 
    PendingApprovals.append(" E.LEAVE_TYPE) CODE FROM");
    PendingApprovals.append(" HCLHRM_PROD_OTHERS.TBL_EMP_ATTN_REQ A");
    PendingApprovals.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY B ON A.EMPLOYEEID=B.EMPLOYEESEQUENCENO");
    PendingApprovals.append(" LEFT JOIN HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_REQ E ON A.RID=E.RID,");
    PendingApprovals.append(" (SELECT ADDDATE('" + from_date + "', INTERVAL @i:=@i+1 DAY) AS SELECTED_DATE");
    PendingApprovals.append(" FROM (");
    PendingApprovals.append(" SELECT a.a");
    PendingApprovals.append(" FROM (SELECT 0 AS a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) AS a");
    PendingApprovals.append(" CROSS JOIN (SELECT 0 AS a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) AS b");
    PendingApprovals.append(" CROSS JOIN (SELECT 0 AS a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) AS c");
    PendingApprovals.append(" ) a");
    PendingApprovals.append(" JOIN (SELECT @i := -1) r1");
    PendingApprovals.append(" WHERE");
    PendingApprovals.append(" @i < DATEDIFF('" + to_date + "', '" + from_date + "'))Q");
    PendingApprovals.append(" where SELECTED_DATE between");
    PendingApprovals.append(" DATE_FORMAT(A.FROM_DATE,'%Y-%m-%d') and DATE_FORMAT(A.TO_DATE,'%Y-%m-%d')");
    PendingApprovals.append(" AND A.FLAG IN ('A','P') AND A.REQ_TYPE='LR' AND A.STATUS=1001 AND A.RID>73 AND A.EMPLOYEEID IN (" + leaves.getEmpID() + ")");
    PendingApprovals.append(" GROUP BY A.RID,SELECTED_DATE ORDER BY A.RID,SELECTED_DATE)L");
    System.out.println("JJJJ" + PendingApprovals);
    try {
      List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(PendingApprovals.toString());
      for (Map<String, Object> row : rows)
        this.Unic_Leave = Integer.parseInt(row.get("CNT").toString()); 
    } catch (Exception err) {
      System.out.println("0 From_toDaysCal Error At Get Leave Quota" + err);
    } 
    System.out.println("Error Trace 1");
    double for_month_limit = Double.parseDouble(Leave_New.get("FOR_MONTH").toString());
    System.out.println("###########" + Leave_New.get("MAXLEAVE").toString());
    System.out.println("Error Trace 1.1");
    double maxleave_month_limit = Double.parseDouble(Leave_New.get("MAXLEAVE").toString());
    System.out.println("Error Trace 1.2");
    double for_month_day_mode = Double.parseDouble(Leave_New.get(String.valueOf(leaves.getEmpID()) + "_DAYMODE").toString());
    System.out.println("Error Trace 1.3");
    boolean check_leave_month = false;
    double btw_payperiod_leaves = 0;
    double newbtw_payperiod_leaves = 0;
    System.out.println("Error Trace 2");
    StringBuffer Probitionary_emp_leave_check = new StringBuffer();
    StringBuffer check_lev_btw_payperiod = new StringBuffer();
    Probitionary_emp_leave_check.append(" SELECT sum(ifnull(LEAVE_COUNT_BT_DAYS,0)) as count FROM hclhrm_prod_others.tbl_emp_leave_report  ");
    Probitionary_emp_leave_check.append(" WHERE LEAVEON BETWEEN '" + EmpData.get("FROMDATE").toString() + "'  AND '" + EmpData.get("TODATE").toString() + "' AND ");
    Probitionary_emp_leave_check.append(" LEAVE_TYPE NOT IN ('OD','LOP','WFH','CF') AND ");
    Probitionary_emp_leave_check.append(" MANAGER_STATUS IN ('P','A') AND ");
    Probitionary_emp_leave_check.append(" EMPLOYEEID in(" + username + ") AND LEAVE_TYPE in('CL','SL','EL') ");
    System.out.println("Probitionary_emp_leave_check::" + Probitionary_emp_leave_check.toString());
    check_lev_btw_payperiod.append(" SELECT if(b.selected_date='" + Hal_date + "',0.5,1 ) as  selected_date FROM (  ");
    check_lev_btw_payperiod.append(" (select adddate('1970-01-01',t4.i*10000 + t3.i*1000 + t2.i*100 + t1.i*10 + t0.i) selected_date from ");
    check_lev_btw_payperiod.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0, ");
    check_lev_btw_payperiod.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, ");
    check_lev_btw_payperiod.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, ");
    check_lev_btw_payperiod.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3, ");
    check_lev_btw_payperiod.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) ");
    check_lev_btw_payperiod.append(" ) a ");
    check_lev_btw_payperiod.append(" LEFT JOIN ");
    check_lev_btw_payperiod.append(" ( ");
    check_lev_btw_payperiod.append(" SELECT selected_date FROM ( ");
    check_lev_btw_payperiod.append(" (select adddate('1970-01-01',t4.i*10000 + t3.i*1000 + t2.i*100 + t1.i*10 + t0.i) selected_date from ");
    check_lev_btw_payperiod.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0, ");
    check_lev_btw_payperiod.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, ");
    check_lev_btw_payperiod.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, ");
    check_lev_btw_payperiod.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3, ");
    check_lev_btw_payperiod.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) V ");
    check_lev_btw_payperiod.append(" ) WHERE selected_date BETWEEN '" + EmpData.get("FROMDATE").toString() + "' and '" + EmpData.get("TODATE").toString() + "' ");
    check_lev_btw_payperiod.append(" )b ON a.selected_date=b.selected_date ");
    check_lev_btw_payperiod.append(" WHERE A.selected_date BETWEEN '" + from_date + "' and '" + to_date + "' and b.selected_date is not null ");
    if (Leave_New.get(String.valueOf(username) + "COUNT_WOFF").toString().equalsIgnoreCase("1"))
      check_lev_btw_payperiod.append(" and A.selected_date not in(" + Holiday_dates_for_month.toString() + ") "); 
    System.out.println(String.valueOf(for_month_limit) + "SAVE MODE" + for_month_day_mode);
    if (for_month_limit == 1 && for_month_day_mode == 0) {
      try {
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(Probitionary_emp_leave_check.toString());
        System.out.println(rows);
        for (Map<String, Object> row : rows)
          btw_payperiod_leaves = Double.parseDouble(row.get("count").toString()); 
      } catch (Exception err) {
        System.out.println("prob check: pFrom_toDaysCal Error At Get Leave Quota ::" + err);
      } 
      System.out.println("check_lev_btw_payperiod::" + Probitionary_emp_leave_check.toString());
      try {
        System.out.println(check_lev_btw_payperiod.toString());
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(check_lev_btw_payperiod.toString());
        System.out.println(rows);
        for (Map<String, Object> row : rows)
          newbtw_payperiod_leaves += Double.parseDouble(row.get("selected_date").toString()); 
      } catch (Exception err) {
        System.out.println("chk lev bet days From_toDaysCal Error At Get Leave Quota" + err);
      } 
    } 
    double combination = btw_payperiod_leaves + newbtw_payperiod_leaves;
    System.out.println(String.valueOf(btw_payperiod_leaves) + "~SAVE POINT 2~" + newbtw_payperiod_leaves);
    System.out.println(String.valueOf(combination) + "~SAVE POINT 2.1~" + maxleave_month_limit);
    if (for_month_limit == 1) {
      if (combination <= maxleave_month_limit) {
        System.out.println(String.valueOf(combination) + "~SAVE POINT 3~" + maxleave_month_limit);
        check_leave_month = true;
      } else if (combination > maxleave_month_limit) {
        check_leave_month = false;
      } else {
        System.out.println(" False Condition at check_leave_month ");
        check_leave_month = false;
      } 
    } else {
      check_leave_month = true;
    } 
    boolean holidayexistpreview = false, holidayexistfeature = false;
    StringBuffer preview_feature_leaves = new StringBuffer();
    StringBuffer less_fromdate = new StringBuffer();
    StringBuffer max_todate = new StringBuffer();
    EmpData.put("lessmindate", "0");
    EmpData.put("lessmintype", "0");
    EmpData.put("gratertype", "0");
    EmpData.put("graterdate", "0");
    preview_feature_leaves.append(" SELECT LR.EMPLOYEEID,ifnull(LR.TO_DATE,0) as lessmindate,ifnull(LR.LEAVE_TYPE,0) lessmintype,ifnull(AR.FROM_DATE,0) graterdate ,ifnull(AR.LEAVE_TYPE,0) gratertype  ");
    preview_feature_leaves.append(" FROM (SELECT A.EMPLOYEEID,A.TO_DATE,B.LEAVE_TYPE ");
    preview_feature_leaves.append(" FROM HCLHRM_PROD_OTHERS.TBL_EMP_ATTN_REQ A ");
    preview_feature_leaves.append(" LEFT JOIN HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_REQ B ON A.RID=B.RID ");
    preview_feature_leaves.append(" WHERE A.EMPLOYEEID in(" + username + ") AND A.REQ_TYPE='LR' AND A.FLAG IN ('P','A') and B.LEAVE_TYPE not in ('OD','LOP','WFH','CF') ");
    preview_feature_leaves.append(" GROUP BY TO_DATE HAVING MAX(A.TO_DATE) < '" + from_date + "'  ORDER BY A.TO_DATE DESC LIMIT 1 ");
    preview_feature_leaves.append(" ) LR ");
    preview_feature_leaves.append(" LEFT JOIN ( ");
    preview_feature_leaves.append(" SELECT A.EMPLOYEEID,A.FROM_DATE,B.LEAVE_TYPE ");
    preview_feature_leaves.append(" FROM HCLHRM_PROD_OTHERS.TBL_EMP_ATTN_REQ A ");
    preview_feature_leaves.append(" LEFT JOIN HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_REQ B ON A.RID=B.RID ");
    preview_feature_leaves.append(" WHERE A.EMPLOYEEID in(" + username + ")  AND A.REQ_TYPE='LR' AND A.FLAG IN ('P','A') and B.LEAVE_TYPE not in ('OD','LOP','WFH','CF') ");
    preview_feature_leaves.append(" GROUP BY A.TO_DATE HAVING MAX(A.TO_DATE) >'" + to_date + "'  ORDER BY A.TO_DATE DESC LIMIT 1 ");
    preview_feature_leaves.append(" ) AR ON LR.EMPLOYEEID=AR.EMPLOYEEID ");
    try {
      List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(preview_feature_leaves.toString());
      for (Map<String, Object> row : rows) {
        EmpData.put("lessmindate", row.get("lessmindate").toString());
        EmpData.put("lessmintype", row.get("lessmintype").toString());
        EmpData.put("gratertype", row.get("gratertype").toString());
        EmpData.put("graterdate", row.get("graterdate").toString());
        if (!row.get("lessmindate").toString().equalsIgnoreCase("0"))
          holidayexistpreview = true; 
        if (!row.get("graterdate").toString().equalsIgnoreCase("0"))
          holidayexistfeature = true; 
      } 
    } catch (Exception err) {
      System.out.println("Less Min Date Cal -From_toDaysCal Error At Get Leave Quota" + err);
    } 
    if (BuID.equalsIgnoreCase("151") || BuID.equalsIgnoreCase("161")) {
      less_fromdate.append(" select selected_date,IF(DAYNAME(selected_date)='Saturday','WOFF','WDAY') DAYTYPE, ");
    } else {
      less_fromdate.append(" select selected_date,IF(DAYNAME(selected_date)='SUNDAY','WOFF','WDAY') DAYTYPE, ");
    } 
    if (BuID.equalsIgnoreCase("151") || BuID.equalsIgnoreCase("161")) {
      less_fromdate.append(" IF(IF(DAYNAME(selected_date)='Saturday','WOFF','WDAY')='WDAY',1,0) STATUS,V2.holidaydate, ");
      less_fromdate.append(" if(DAYNAME(selected_date)='Saturday' && selected_date=V2.holidaydate,1,if(DAYNAME(selected_date)='Saturday',1,if(selected_date=V2.holidaydate,1,0))) AS HW ");
    } else {
      less_fromdate.append(" IF(IF(DAYNAME(selected_date)='SUNDAY','WOFF','WDAY')='WDAY',1,0) STATUS,V2.holidaydate, ");
      less_fromdate.append(" if(DAYNAME(selected_date)='SUNDAY' && selected_date=V2.holidaydate,1,if(DAYNAME(selected_date)='SUNDAY',1,if(selected_date=V2.holidaydate,1,0))) AS HW ");
    } 
    less_fromdate.append(" FROM ");
    less_fromdate.append(" (select adddate('1970-01-01',t4.i*10000 + t3.i*1000 + t2.i*100 + t1.i*10 + t0.i) selected_date from ");
    less_fromdate.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0, ");
    less_fromdate.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, ");
    less_fromdate.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, ");
    less_fromdate.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3, ");
    less_fromdate.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v ");
    less_fromdate.append(" left join( ");
    
   // less_fromdate.append("  SELECT DISTINCT b.HOLIDAYDATE 'holidaydate' FROM hclhrm_prod.tbl_employee_primary a ");
   // less_fromdate.append("  left join hclhrm_prod.tbl_holidays b on a.companyid=b.businessunitid  where employeesequenceno  in(" + username + ") and b.statusid=1001  ");
     
    //// Location Based added 
    		less_fromdate.append(" SELECT DISTINCT hd.holidaydate 'holidaydate' ");
    		less_fromdate.append(" FROM hclhrm_prod.tbl_employee_primary p ");
    		less_fromdate.append(" JOIN hclhrm_prod.tbl_employee_professional_details info ON info.employeeid = p.employeeid ");
    		less_fromdate.append(" JOIN hcllcm_prod.tbl_location A ON A.LOCATIONID = info.worklocationid ");
    		less_fromdate.append(" JOIN hcllcm_prod.tbl_location b ON A.parent = b.locationid AND b.locationtype = 1 ");
    		less_fromdate.append(" JOIN hclhrm_prod.tbl_holiday_location_mapping map ON b.locationid = map.stateid AND map.BUSINESSUNITID = P.companyid ");
    		less_fromdate.append(" JOIN hclhrm_prod.tbl_holidays hd ON map.holidayid = hd.holidayid ");
    		less_fromdate.append(" WHERE hd.statusid = 1001 ");
    		less_fromdate.append(" AND p.employeesequenceno = "+username+"");

    		 
    		less_fromdate.append(" UNION ALL ");
 
    		less_fromdate.append(" SELECT hd.holidaydate 'holidaydate' ");
    		less_fromdate.append(" FROM hclhrm_prod.tbl_employee_primary p ");
    		less_fromdate.append(" JOIN hclhrm_prod.tbl_holidays hd ON hd.businessunitid = p.companyid ");
    		less_fromdate.append(" LEFT JOIN hclhrm_prod.tbl_holiday_location_mapping map ON map.holidayid = hd.holidayid ");
    		less_fromdate.append(" WHERE hd.statusid = 1001 ");
    		less_fromdate.append(" AND p.employeesequenceno = "+username+" ");
    		less_fromdate.append(" AND map.holidayid IS NULL ");
    		less_fromdate.append(" AND NOT EXISTS ( ");
    		less_fromdate.append(" SELECT 1");
    		less_fromdate.append(" FROM hclhrm_prod.tbl_employee_primary p2 ");
    		less_fromdate.append(" JOIN hclhrm_prod.tbl_employee_professional_details info2 ON info2.employeeid = p2.employeeid ");
    		less_fromdate.append(" JOIN hcllcm_prod.tbl_location A2 ON A2.LOCATIONID = info2.worklocationid ");
    		less_fromdate.append(" JOIN hcllcm_prod.tbl_location b2 ON A2.parent = b2.locationid AND b2.locationtype = 1 ");
    		less_fromdate.append(" JOIN hclhrm_prod.tbl_holiday_location_mapping map2 ON b2.locationid = map2.stateid AND map2.BUSINESSUNITID = P2.companyid ");
    		less_fromdate.append(" JOIN hclhrm_prod.tbl_holidays hd2 ON map2.holidayid = hd2.holidayid ");
    		less_fromdate.append(" WHERE hd2.statusid = 1001 ");
    		less_fromdate.append(" AND p2.employeesequenceno = "+username+" ");
    		less_fromdate.append(") ");

    		 

    /////
    
    less_fromdate.append("  UNION ALL ");
    less_fromdate.append(" SELECT t.HOLIDAYDATE 'holidaydate' FROM  test.tbl_emp_saturday_policy st ");
    less_fromdate.append(" left join hclhrm_prod.tbl_employee_primary pp on pp.employeesequenceno=st.employee_seq ");
    less_fromdate.append(" left join test.tbl_holidays_special t on  pp.companyid=t.businessunitid  where pp.employeesequenceno in(" + username + ") and t.statusid=1001  UNION ALL SELECT SP.HOLIDAYDATE FROM test.tbl_saturday_special SP where SP.HOLIDAYTYPEID in(SELECT COUNT(*) FROM test.tbl_emp_saturday_policy where employee_seq=" + username + ")");
    less_fromdate.append(" )V2 on V2.holidaydate=V.selected_date ");
    less_fromdate.append(" where selected_date between '" + EmpData.get("lessmindate").toString() + "'+INTERVAL 1 DAY and '" + from_date + "'+INTERVAL -1 DAY ");
   
    System.out.println("less_fromdate::" + less_fromdate.toString());
    if (BuID.equalsIgnoreCase("151") || BuID.equalsIgnoreCase("161")) {
      max_todate.append(" select selected_date,IF(DAYNAME(selected_date)='Saturday','WOFF','WDAY') DAYTYPE, ");
      max_todate.append(" IF(IF(DAYNAME(selected_date)='Saturday','WOFF','WDAY')='WDAY',1,0) STATUS,V2.holidaydate, ");
      max_todate.append(" if(DAYNAME(selected_date)='Saturday' && selected_date=V2.holidaydate,1,if(DAYNAME(selected_date)='Saturday',1,if(selected_date=V2.holidaydate,1,0))) AS HW ");
    } else {
      max_todate.append(" select selected_date,IF(DAYNAME(selected_date)='SUNDAY','WOFF','WDAY') DAYTYPE, ");
      max_todate.append(" IF(IF(DAYNAME(selected_date)='SUNDAY','WOFF','WDAY')='WDAY',1,0) STATUS,V2.holidaydate, ");
      max_todate.append(" if(DAYNAME(selected_date)='SUNDAY' && selected_date=V2.holidaydate,1,if(DAYNAME(selected_date)='SUNDAY',1,if(selected_date=V2.holidaydate,1,0))) AS HW ");
    } 
    max_todate.append(" FROM ");
    max_todate.append(" (select adddate('1970-01-01',t4.i*10000 + t3.i*1000 + t2.i*100 + t1.i*10 + t0.i) selected_date from ");
    max_todate.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0, ");
    max_todate.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, ");
    max_todate.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, ");
    max_todate.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3, ");
    max_todate.append(" (select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v ");
    max_todate.append(" left join( ");
    
   // max_todate.append("  SELECT DISTINCT b.HOLIDAYDATE 'holidaydate' FROM hclhrm_prod.tbl_employee_primary a ");
   // max_todate.append("  left join hclhrm_prod.tbl_holidays b on a.companyid=b.businessunitid  where employeesequenceno  in(" + username + ") and b.statusid=1001  ");
    
    //// Location Based added 
    
    max_todate.append(" SELECT DISTINCT hd.holidaydate 'holidaydate' ");
    max_todate.append(" FROM hclhrm_prod.tbl_employee_primary p ");
    max_todate.append(" JOIN hclhrm_prod.tbl_employee_professional_details info ON info.employeeid = p.employeeid ");
    max_todate.append(" JOIN hcllcm_prod.tbl_location A ON A.LOCATIONID = info.worklocationid ");
    max_todate.append(" JOIN hcllcm_prod.tbl_location b ON A.parent = b.locationid AND b.locationtype = 1 ");
    max_todate.append(" JOIN hclhrm_prod.tbl_holiday_location_mapping map ON b.locationid = map.stateid AND map.BUSINESSUNITID = P.companyid ");
    max_todate.append(" JOIN hclhrm_prod.tbl_holidays hd ON map.holidayid = hd.holidayid ");
    max_todate.append(" WHERE hd.statusid = 1001 ");
    max_todate.append(" AND p.employeesequenceno = "+username+"");

	 
    max_todate.append(" UNION ALL ");

	 
    max_todate.append(" SELECT hd.holidaydate 'holidaydate' ");
    max_todate.append(" FROM hclhrm_prod.tbl_employee_primary p ");
    max_todate.append(" JOIN hclhrm_prod.tbl_holidays hd ON hd.businessunitid = p.companyid ");
    max_todate.append(" LEFT JOIN hclhrm_prod.tbl_holiday_location_mapping map ON map.holidayid = hd.holidayid ");
    max_todate.append(" WHERE hd.statusid = 1001 ");
    max_todate.append(" AND p.employeesequenceno = "+username+" ");
    max_todate.append(" AND map.holidayid IS NULL ");
    max_todate.append(" AND NOT EXISTS ( ");
    max_todate.append(" SELECT 1");
    max_todate.append(" FROM hclhrm_prod.tbl_employee_primary p2 ");
    max_todate.append(" JOIN hclhrm_prod.tbl_employee_professional_details info2 ON info2.employeeid = p2.employeeid ");
    max_todate.append(" JOIN hcllcm_prod.tbl_location A2 ON A2.LOCATIONID = info2.worklocationid ");
    max_todate.append(" JOIN hcllcm_prod.tbl_location b2 ON A2.parent = b2.locationid AND b2.locationtype = 1 ");
    max_todate.append(" JOIN hclhrm_prod.tbl_holiday_location_mapping map2 ON b2.locationid = map2.stateid AND map2.BUSINESSUNITID = P2.companyid ");
    max_todate.append(" JOIN hclhrm_prod.tbl_holidays hd2 ON map2.holidayid = hd2.holidayid ");
    max_todate.append(" WHERE hd2.statusid = 1001 ");
    max_todate.append(" AND p2.employeesequenceno = "+username+" ");
    max_todate.append(" ) ");
    
    //////
    
    max_todate.append("  UNION ALL ");
    max_todate.append(" SELECT t.HOLIDAYDATE 'holidaydate' FROM  test.tbl_emp_saturday_policy st ");
    max_todate.append(" left join hclhrm_prod.tbl_employee_primary pp on pp.employeesequenceno=st.employee_seq ");
    max_todate.append(" left join test.tbl_holidays_special t on  pp.companyid=t.businessunitid  where pp.employeesequenceno in(" + username + ") and t.statusid=1001  UNION ALL SELECT SP.HOLIDAYDATE FROM test.tbl_saturday_special SP where SP.HOLIDAYTYPEID in(SELECT COUNT(*) FROM test.tbl_emp_saturday_policy where employee_seq=" + username + ") ");
    max_todate.append(" )V2 on V2.holidaydate=V.selected_date ");
    max_todate.append(" where selected_date between '" + to_date + "' +INTERVAL 1 DAY and '" + EmpData.get("graterdate").toString() + "'+INTERVAL -1 DAY ");
   
    System.out.println("less_fromdate::" + max_todate.toString());
    if (holidayexistpreview)
      try {
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(less_fromdate.toString());
        for (Map<String, Object> row : rows) {
          if (Double.parseDouble(row.get("HW").toString()) > 0) {
            prev_holidays_woff += Double.parseDouble(row.get("HW").toString());
            continue;
          } 
          if (Double.parseDouble(row.get("HW").toString()) == 0)
            prev_btw_workingdays++; 
        } 
      } catch (Exception err) {
        System.out.println("4From_toDaysCal Error At Get Leave Quota" + err);
      }  
    if (holidayexistfeature)
      try {
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(max_todate.toString());
        for (Map<String, Object> row : rows) {
          if (Double.parseDouble(row.get("HW").toString()) > 0) {
            feature_holidays_woff += Double.parseDouble(row.get("HW").toString());
            continue;
          } 
          if (Double.parseDouble(row.get("HW").toString()) == 0)
            feature_btw_workingdays++; 
        } 
      } catch (Exception err) {
        System.out.println("3From_toDaysCal Error At Get Leave Quota" + err);
      }  
    boolean prev_flag_of_leave = false, feature_flag_of_leave = false, prev_combflag_of_leave = false, feature_combflag_of_leave = false;
    String Message = null;
    if (holidayexistpreview && !Leave_Type.equalsIgnoreCase("LOP") && !Leave_Type.equalsIgnoreCase("OD") && !Leave_Type.equalsIgnoreCase("WFH") && !Leave_Type.equalsIgnoreCase("CF")) {
      System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 1" + feature_holidays_woff);
      if (prev_btw_workingdays == 0 && prev_holidays_woff > 0) {
        System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 1.1" + feature_holidays_woff);
        System.out.println(String.valueOf(prev_btw_workingdays) + "~test 1~" + prev_holidays_woff);
        if (EmpData.get("lessmintype").toString().equalsIgnoreCase(Leave_Type)) {
          System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 1.1.1" + feature_holidays_woff);
          prev_combflag_of_leave = true;
        } else if ((EmpData.get("lessmintype").toString().equalsIgnoreCase("SL") && Leave_Type.equalsIgnoreCase("EL")) || (EmpData.get("lessmintype").toString().equalsIgnoreCase("EL") && Leave_Type.equalsIgnoreCase("SL"))) {
          System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 1.1.2" + feature_holidays_woff);
          prev_combflag_of_leave = true;
          Holiday_Count = 0;
        } else {
          System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 1.1.3" + feature_holidays_woff);
          if (Leave_Type.equalsIgnoreCase("OD") || Leave_Type.equalsIgnoreCase("LOP") || Leave_Type.equalsIgnoreCase("CF")) {
            prev_combflag_of_leave = true;
          } else {
            prev_combflag_of_leave = false;
            Message = "Combination of " + EmpData.get("lessmintype").toString() + " & " + Leave_Type + "  not applicable please check with previous leave .. ";
          } 
        } 
      } else if (prev_btw_workingdays > 0 && (prev_holidays_woff == 0 || prev_holidays_woff > 0)) {
        System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 3" + feature_holidays_woff);
        prev_combflag_of_leave = true;
      } else if (prev_btw_workingdays == 0 && prev_holidays_woff == 0) {
        System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 2" + feature_holidays_woff);
        if (EmpData.get("lessmintype").toString().equalsIgnoreCase(Leave_Type)) {
          prev_combflag_of_leave = true;
        } else if ((EmpData.get("lessmintype").toString().equalsIgnoreCase("SL") && Leave_Type.equalsIgnoreCase("EL")) || (EmpData.get("lessmintype").toString().equalsIgnoreCase("EL") && Leave_Type.equalsIgnoreCase("SL"))) {
          prev_combflag_of_leave = true;
          Holiday_Count = 0;
        } else {
          System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 3" + feature_holidays_woff);
          if (Leave_Type.equalsIgnoreCase("OD") || Leave_Type.equalsIgnoreCase("LOP") || Leave_Type.equalsIgnoreCase("WFH") || Leave_Type.equalsIgnoreCase("CF")) {
            prev_combflag_of_leave = true;
          } else {
            prev_combflag_of_leave = false;
            Message = "Combination of " + EmpData.get("lessmintype").toString() + " & " + Leave_Type + "  not applicable please check with previous leave .. ";
          } 
        } 
      } 
      System.out.println(String.valueOf(prev_combflag_of_leave) + "::1less_fromdate::" + prev_flag_of_leave);
      System.out.println(String.valueOf(prev_btw_workingdays) + "::Dates::" + prev_holidays_woff);
      if (prev_combflag_of_leave) {
        System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 4" + feature_holidays_woff);
        if (prev_btw_workingdays == 0 && prev_holidays_woff > 0 && !Leave_Type.equalsIgnoreCase("LOP") && !Leave_Type.equalsIgnoreCase("OD") && !Leave_Type.equalsIgnoreCase("CL") && !Leave_Type.equalsIgnoreCase("WFH") && !Leave_Type.equalsIgnoreCase("CF")) {
          System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 4.1" + feature_holidays_woff);
          prev_flag_of_leave = false;
          Message = " Immediate Leave, after week off/Holiday not eligible,should consider week off&Holiday also OR Cancel previous leave and choose proper dates";
        } else if (prev_btw_workingdays > 0 && (prev_holidays_woff > 0 || prev_holidays_woff == 0) && !Leave_Type.equalsIgnoreCase("LOP") && !Leave_Type.equalsIgnoreCase("OD") && !Leave_Type.equalsIgnoreCase("CL") && !Leave_Type.equalsIgnoreCase("WFH") && !Leave_Type.equalsIgnoreCase("CF")) {
          prev_flag_of_leave = true;
          System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 4.2" + feature_holidays_woff);
        } else if ((prev_btw_workingdays == 0 || prev_btw_workingdays > 0) && (prev_holidays_woff == 0 || prev_holidays_woff > 0) && Leave_Type.equalsIgnoreCase("CL")) {
          System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 4.3" + feature_holidays_woff);
          prev_flag_of_leave = true;
        } else if (prev_btw_workingdays == 0 && prev_holidays_woff == 0) {
          System.out.println(String.valueOf(feature_btw_workingdays) + "BABUV 4.4" + feature_holidays_woff);
          prev_flag_of_leave = true;
        } 
        System.out.println(String.valueOf(prev_combflag_of_leave) + "::21less_fromdate::" + prev_flag_of_leave);
      } 
    } else {
      prev_combflag_of_leave = true;
      prev_flag_of_leave = true;
    } 
    if (holidayexistfeature && !Leave_Type.equalsIgnoreCase("LOP") && !Leave_Type.equalsIgnoreCase("OD") && !Leave_Type.equalsIgnoreCase("WFH") && !Leave_Type.equalsIgnoreCase("CF")) {
      System.out.println(String.valueOf(feature_btw_workingdays) + "BABU 1" + feature_holidays_woff);
      if (feature_btw_workingdays == 0 && feature_holidays_woff > 0) {
        if (EmpData.get("gratertype").toString().equalsIgnoreCase(Leave_Type)) {
          feature_combflag_of_leave = true;
        } else if ((EmpData.get("gratertype").toString().equalsIgnoreCase("SL") && Leave_Type.equalsIgnoreCase("EL")) || (EmpData.get("gratertype").toString().equalsIgnoreCase("EL") && Leave_Type.equalsIgnoreCase("SL"))) {
          feature_combflag_of_leave = true;
          Holiday_Count = 0;
        } else {
          feature_combflag_of_leave = false;
          Message = "Combination of " + EmpData.get("gratertype").toString() + " & " + Leave_Type + "  not applicable please check with previous leave .. ";
        } 
      } else if (feature_btw_workingdays > 0 && (feature_holidays_woff == 0 || feature_holidays_woff > 0)) {
        System.out.println(String.valueOf(feature_btw_workingdays) + "BABU 2" + feature_holidays_woff);
        feature_combflag_of_leave = true;
      } else if (feature_btw_workingdays == 0 && feature_holidays_woff == 0) {
        System.out.println(String.valueOf(feature_btw_workingdays) + "BABU 3" + feature_holidays_woff);
        if (EmpData.get("gratertype").toString().equalsIgnoreCase(Leave_Type)) {
          System.out.println(String.valueOf(EmpData.get("gratertype").toString()) + "BABU 3.1 " + feature_holidays_woff);
          feature_combflag_of_leave = true;
        } else if ((EmpData.get("gratertype").toString().equalsIgnoreCase("SL") && Leave_Type.equalsIgnoreCase("EL")) || (EmpData.get("gratertype").toString().equalsIgnoreCase("EL") && Leave_Type.equalsIgnoreCase("SL"))) {
          System.out.println(String.valueOf(EmpData.get("gratertype").toString()) + "BABU 3.2 " + feature_holidays_woff);
          feature_combflag_of_leave = true;
          Holiday_Count = 0;
        } else {
          System.out.println(String.valueOf(EmpData.get("gratertype").toString()) + "BABU 3.3 " + feature_holidays_woff);
          feature_combflag_of_leave = false;
          try {
            Message = "Combination of " + EmpData.get("gratertype").toString() + " & " + Leave_Type + "  not applicable please check with previous leave ..! ";
          } catch (Exception ewr) {
            System.out.println("ewr::" + ewr);
          } 
        } 
      } 
      System.out.println(String.valueOf(feature_combflag_of_leave) + "::feature 1less_fromdate::" + feature_flag_of_leave);
      System.out.println(String.valueOf(feature_btw_workingdays) + "::feature Dates::" + feature_holidays_woff);
      if (feature_combflag_of_leave) {
        System.out.println(String.valueOf(feature_btw_workingdays) + "BABU 4" + feature_holidays_woff);
        if (feature_btw_workingdays == 0 && feature_holidays_woff > 0 && !Leave_Type.equalsIgnoreCase("LOP") && !Leave_Type.equalsIgnoreCase("OD") && !Leave_Type.equalsIgnoreCase("CL") && !Leave_Type.equalsIgnoreCase("WFH") && !Leave_Type.equalsIgnoreCase("CF")) {
          feature_flag_of_leave = false;
          Message = " Immediate Leave , after week off/Holiday not eligible,should consider week off & Holiday also OR Cancel previous leave and choose proper dates..!";
        } else if (feature_btw_workingdays > 0 && (feature_holidays_woff > 0 || feature_holidays_woff == 0) && !Leave_Type.equalsIgnoreCase("LOP") && !Leave_Type.equalsIgnoreCase("OD") && !Leave_Type.equalsIgnoreCase("CL") && !Leave_Type.equalsIgnoreCase("WFH") && !Leave_Type.equalsIgnoreCase("CF")) {
          feature_flag_of_leave = true;
        } else if ((feature_btw_workingdays == 0 || feature_btw_workingdays > 0) && (feature_holidays_woff == 0 || feature_holidays_woff > 0) && Leave_Type.equalsIgnoreCase("CL")) {
          feature_flag_of_leave = true;
        } else if (feature_btw_workingdays == 0 && feature_holidays_woff == 0) {
          feature_flag_of_leave = true;
        } 
        System.out.println(String.valueOf(feature_combflag_of_leave) + "::feature21less_fromdate::" + feature_flag_of_leave);
      } 
    } else {
      feature_combflag_of_leave = true;
      feature_flag_of_leave = true;
    } 
    try {
      System.out.println(String.valueOf(Integer.parseInt(EmpData.get("DATE_VALID_SL").toString())) + "1");
      System.out.println(String.valueOf(Integer.parseInt(EmpData.get("TODATE_VALID").toString())) + "2");
      System.out.println(String.valueOf(check_leave_month) + "3");
      System.out.println(String.valueOf(holidayexistpreview) + "4");
      System.out.println(String.valueOf(prev_combflag_of_leave) + "5");
      System.out.println(String.valueOf(prev_flag_of_leave) + "6");
      System.out.println(String.valueOf(holidayexistfeature) + "7");
      System.out.println(String.valueOf(feature_flag_of_leave) + "8");
      System.out.println(String.valueOf(feature_combflag_of_leave) + "9");
      System.out.println(String.valueOf(this.lopins_count) + "10");
      System.out.println(String.valueOf(this.Unic_Leave) + "11");
      System.out.println(String.valueOf(username) + "12");
      System.out.println(String.valueOf(maxleave) + "maxleavemaxleavemaxleave");
      int FYCLSLCHECK = 0;
      if ((Leave_Type.equalsIgnoreCase("CL") || Leave_Type.equalsIgnoreCase("SL")) && !this.Fy_Year.equalsIgnoreCase(to_date.split("-")[0]))
        FYCLSLCHECK = 1; 
      System.out.println(FYCLSLCHECK);
      if (User_Auth_auth == 1 && Integer.parseInt(EmpData.get("DATE_VALID_SL").toString()) == 1 && 
        Integer.parseInt(EmpData.get("TODATE_VALID").toString()) == 0 && 
        check_leave_month && (!holidayexistpreview || (prev_combflag_of_leave && prev_flag_of_leave)) && (
        !holidayexistfeature || (feature_flag_of_leave && feature_combflag_of_leave)) && 
        this.lopins_count == 1 && this.Unic_Leave == 0 && username != null && !Evvalid && FYCLSLCHECK == 0) {
        double Leav_avail = 0, MAX_LEAVE_ = 0, CL_DUMM_LEAV = 0, cl_leave_validation = Double.parseDouble(clmap.get("LEAVEADD").toString());
        boolean maxleaveflag = true;
        try {
          MAX_LEAVE_ = maxleave;
          System.out.println(String.valueOf(maxleave) + "maxleavemaxleavemaxleave");
        } catch (Exception err) {
          System.out.println("err ::" + err);
          MAX_LEAVE_ = 0;
        } 
        try {
          System.out.println("MAX_LEAVE_ ::" + MAX_LEAVE_);
          System.out.println("Holiday_Count ::" + Holiday_Count);
          System.out.println("cl_leave_validation ::" + cl_leave_validation);
          if (Leave_Type.equalsIgnoreCase("CL")) {
            LeaveCount = Leave_Diff - Holiday_Count;
            CL_DUMM_LEAV = LeaveCount;
            LeaveCount = Leave_Diff - Holiday_Count + cl_leave_validation;
          } else {
            LeaveCount = Leave_Diff - Holiday_Count;
          } 
          if (LeaveCount < 0)
            LeaveCount = 0; 
          System.out.println("MAX_LEAVE_ ::" + MAX_LEAVE_);
          System.out.println("maxleaveflag ::" + maxleaveflag);
          if (Leave_Type.equalsIgnoreCase("CL")) {
            if (LeaveCount > 3) {
              maxleaveflag = false;
            } else if (LeaveCount <= 3) {
              LeaveCount = CL_DUMM_LEAV;
              if (DayMode == 0 && MAX_LEAVE_ < LeaveCount)
                maxleaveflag = false; 
            } 
          } else if (DayMode == 0 && MAX_LEAVE_ < LeaveCount) {
            maxleaveflag = false;
          } 
          System.out.println("MAX_LEAVE_ ::" + MAX_LEAVE_);
          System.out.println("CL_DUMM_LEAV ::" + CL_DUMM_LEAV);
          System.out.println("Holiday_Count ::" + Holiday_Count);
          System.out.println("cl_leave_validation ::" + cl_leave_validation);
          System.out.println("maxleaveflag ::" + maxleaveflag);
          System.out.println("LeaveCount ::" + LeaveCount);
          DayMode = Double.parseDouble(Leave_New.get(String.valueOf(username) + "_DAYMODE").toString());
          Leav_avail = Double.parseDouble(Leave_New.get(String.valueOf(username) + "_AVAILABLEQTY").toString());
          if (DayMode == 0 && Leav_avail >= LeaveCount && LeaveCount > 0 && maxleaveflag) {
            System.out.println("flag1" + LeaveCount);
            insertionFlag = true;
          } else if (DayMode > 0 && LeaveCount > 0) {
            System.out.println("flag2" + LeaveCount);
            insertionFlag = true;
            maxleaveflag = true;
          } else {
            System.out.println("in faild condition flag3 :: " + LeaveCount);
            if (LeaveCount <= 0) {
              Atten_Req_Message = "Improper date selection, please check with holiday & week off .  ";
              insertionFlag = false;
            } else if (!maxleaveflag) {
              if (!maxleaveflag && Leave_Type.equalsIgnoreCase("CL")) {
                Atten_Req_Message = "CL Limit is Exceed Please check previous applied CL-leave and woff & HL ";
              } else {
                Atten_Req_Message = "Improper date selection / Limit Exceed, elegible leave quantity is " + MAX_LEAVE_ + "  ";
              } 
            } else {
              Atten_Req_Message = "Request Not processed  :Eligible quantity " + Leav_avail + " , Applied Leave Count should be <= " + Leav_avail + " ";
              insertionFlag = false;
            } 
          } 
        } catch (Exception err) {
          System.out.println("Exception At Validation  " + err);
        } 
        System.out.println(String.valueOf(Leav_avail) + "~LEAVE MGM ~" + LeaveCount);
        if (insertionFlag & OTHFLAG.equalsIgnoreCase("N")) {
          if (Leave_Type.equalsIgnoreCase("CL"))
            LeaveCount = CL_DUMM_LEAV; 
          try (Connection conn = this.dataSource.getConnection();) {
            Random rand = new Random();
            int nRand = rand.nextInt(200000) + 12000;
            PreparedStatement ps = null;
            PreparedStatement ps_1 = null;
            PreparedStatement ps_leaveQuota = null;
            PreparedStatement ps_SingleDates = null;
             
            conn.setAutoCommit(false);
            int count = 0;
            if (HR_ATT.equalsIgnoreCase("OLD")) {
              ps = conn.prepareStatement("insert into hclhrm_prod_others.tbl_emp_attn_req(EMPLOYEEID,FROM_DATE,TO_DATE,SUBJECT,TO_EMAIL,CC_EMAIL,MESSAGE,REQ_TYPE,REQ_DATE,RANDOMID,HR_CC_MAIL,EMPMAIL) values(?,?,?,?,?,?,?,'LR',curdate(),?,?,?)", new String[] { "RID" });
              ps.setString(1, username);
              ps.setString(2, from_date);
              ps.setString(3, to_date);
              ps.setString(4, subject);
              ps.setString(5, to_mail);
              ps.setString(6, cc_mail);
              ps.setString(7, reason);
              ps.setInt(8, nRand);
              ps.setString(9, HR_MAIL);
              ps.setString(10, EMP_MAIL);
            } 
            if (HR_ATT.equalsIgnoreCase("HRATT")) {
              ps = conn.prepareStatement("insert into hclhrm_prod_others.tbl_emp_attn_req(EMPLOYEEID,FROM_DATE,TO_DATE,SUBJECT,TO_EMAIL,CC_EMAIL,MESSAGE,REQ_TYPE,REQ_DATE,RANDOMID,MAIL_STATUS,FLAG,COMMENTS,APPROVEDBY) values(?,?,?,?,?,?,?,'LR',curdate(),?,?,?,?,?)", new String[] { "RID" });
              ps.setString(1, HR_ATT_USER);
              ps.setString(2, from_date);
              ps.setString(3, to_date);
              ps.setString(4, subject);
              ps.setString(5, to_mail);
              ps.setString(6, cc_mail);
              ps.setString(7, reason);
              ps.setInt(8, nRand);
              ps.setString(9, "S");
              ps.setString(10, "A");
              ps.setString(11, "Auto Approved By HR");
              ps.setString(12, username);
            } 
            count = ps.executeUpdate();
            Long primaryKey = Long.valueOf(0L);
            ResultSet generatedKeys = null;
            try {
              generatedKeys = ps.getGeneratedKeys();
              if (generatedKeys != null && generatedKeys.next()) {
                primaryKey = Long.valueOf(generatedKeys.getLong(1));
                System.out.println("primaryKey:::" + primaryKey);
              } 
            } catch (Exception Genkey) {
              System.out.println("Genkey:::" + Genkey);
            } finally {
              generatedKeys.close();
            } 
            if (HR_ATT.equalsIgnoreCase("OLD")) {
              ps_1 = conn.prepareStatement("insert into hclhrm_prod_others.tbl_emp_Leave_req(EMPLOYEEID,FROM_DATE,TO_DATE,SUBJECT,TO_EMAIL,CC_EMAIL,MESSAGE,REQ_TYPE,REQ_DATE,RANDOMID,HALF_DAY_STATUS,HALF_DAY_DATE,COMPOFF_DAY_STATUS,COMPOFF_DAY_DATE,Leave_Type,RID,from_loc,to_loc,TOTA_DAYS) values(?,?,?,?,?,?,?,'LR',curdate(),?,?,?,?,?,?,?,?,?,?)");
              ps_1.setString(1, username);
              ps_1.setString(2, from_date);
              ps_1.setString(3, to_date);
              ps_1.setString(4, subject);
              ps_1.setString(5, to_mail);
              ps_1.setString(6, cc_mail);
              ps_1.setString(7, reason);
              ps_1.setInt(8, nRand);
              ps_1.setString(9, HalfDay);
              ps_1.setString(10, Hal_date);
              ps_1.setString(11, compoff);
              ps_1.setString(12, comm_date);
              ps_1.setString(13, Leave_Type);
              ps_1.setLong(14, primaryKey.longValue());
              ps_1.setString(15, From_loc);
              ps_1.setString(16, To_loc);
              ps_1.setString(17, ""+LeaveCount+"");
              if (DayMode == 0) {
                ps_leaveQuota = conn.prepareStatement("update hclhrm_prod_others.tbl_emp_leave_quota set USEDQTY=USEDQTY+?,AVAILABLEQTY=AVAILABLEQTY-? where EMPLOYEEID=? AND LEAVETYPEID=? and status=1001 limit 1");
                ps_leaveQuota.setString(1, ""+LeaveCount+"");
                ps_leaveQuota.setString(2, ""+LeaveCount+"");
                ps_leaveQuota.setString(3, Leave_New.get(String.valueOf(username) + "_EMPID").toString());
                ps_leaveQuota.setString(4, Leave_New.get("LEAVE_TYPE_ID").toString());
              } else {
                ps_leaveQuota = conn.prepareStatement("update hclhrm_prod_others.tbl_emp_leave_quota set USEDQTY=USEDQTY+?  where EMPLOYEEID=? AND LEAVETYPEID=? and status=1001 limit 1");
                ps_leaveQuota.setString(1, ""+LeaveCount+"");
                ps_leaveQuota.setString(2, Leave_New.get(String.valueOf(username) + "_EMPID").toString());
                ps_leaveQuota.setString(3, Leave_New.get("LEAVE_TYPE_ID").toString());
              } 
              Iterator singledate = LeaveUnicInsertion.iterator();
              ps_SingleDates = conn.prepareStatement("INSERT INTO hclhrm_prod_others.tbl_emp_leave_report (EMPLOYEEID, RID, LEAVEON, FROM_DATE, TO_DATE, LEAVE_COUNT, LEAVE_COUNT_BT_DAYS, LEAVE_TYPE, HALF_DAY,DAYSTATUS) VALUES (?,?,?,?,?,?,?,?,?,?)");
              System.out.println(String.valueOf(LeaveUnicInsertion_map.toString()) + "MAP DATA");
              while (singledate.hasNext()) {
                String SinglDate = singledate.next().toString();
                String DayType = LeaveUnicInsertion_map.get(String.valueOf(SinglDate) + "_DAYTYPE").toString();
                String fromdate = LeaveUnicInsertion_map.get(String.valueOf(SinglDate) + "_fromdate").toString();
                String todate = LeaveUnicInsertion_map.get(String.valueOf(SinglDate) + "_todate").toString();
                String Halfday = LeaveUnicInsertion_map.get(String.valueOf(SinglDate) + "_HALFDAY").toString();
                String half_day_date = LeaveUnicInsertion_map.get(String.valueOf(SinglDate) + "_Hal_date").toString();
                String Subject = LeaveUnicInsertion_map.get(String.valueOf(SinglDate) + "_subject").toString();
                String Reason = LeaveUnicInsertion_map.get(String.valueOf(SinglDate) + "_reason").toString();
                System.out.println(String.valueOf(half_day_date) + "~~~~" + SinglDate);
                ps_SingleDates.setString(1, username);
                ps_SingleDates.setLong(2, primaryKey.longValue());
                ps_SingleDates.setString(3, SinglDate);
                ps_SingleDates.setString(4, fromdate);
                ps_SingleDates.setString(5, todate);
                if (SinglDate.equalsIgnoreCase(half_day_date)) {
                  ps_SingleDates.setString(6, "0.5");
                  ps_SingleDates.setString(9, Halfday);
                } else {
                  ps_SingleDates.setString(6, "1");
                  ps_SingleDates.setString(9, "false");
                } 
                ps_SingleDates.setString(7, ""+LeaveCount+"");
                ps_SingleDates.setString(8, Leave_Type);
                ps_SingleDates.setString(10, DayType);
                ps_SingleDates.addBatch();
              } 
            } else if (HR_ATT.equalsIgnoreCase("HRATT")) {
              ps_1 = conn.prepareStatement("insert into hclhrm_prod_others.tbl_emp_Leave_req(EMPLOYEEID,FROM_DATE,TO_DATE,SUBJECT,TO_EMAIL,CC_EMAIL,MESSAGE,REQ_TYPE,REQ_DATE,RANDOMID,HALF_DAY_STATUS,HALF_DAY_DATE,COMPOFF_DAY_STATUS,COMPOFF_DAY_DATE,Leave_Type,RID) values(?,?,?,?,?,?,?,'LR',curdate(),?,?,?,?,?,?,?)");
              ps_1.setString(1, HR_ATT_USER);
              ps_1.setString(2, from_date);
              ps_1.setString(3, to_date);
              ps_1.setString(4, subject);
              ps_1.setString(5, to_mail);
              ps_1.setString(6, cc_mail);
              ps_1.setString(7, reason);
              ps_1.setInt(8, nRand);
              ps_1.setString(9, HalfDay);
              ps_1.setString(10, Hal_date);
              ps_1.setString(11, compoff);
              ps_1.setString(12, comm_date);
              ps_1.setString(13, Leave_Type);
              ps_1.setLong(14, primaryKey.longValue());
              if (DayMode == 0) {
                ps_leaveQuota = conn.prepareStatement("update hclhrm_prod_others.tbl_emp_leave_quota set USEDQTY=USEDQTY+?,AVAILABLEQTY=AVAILABLEQTY-? where EMPLOYEEID=? AND LEAVETYPEID=? and status=1001 limit 1");
                ps_leaveQuota.setString(1, ""+LeaveCount+"");
                ps_leaveQuota.setString(2, ""+LeaveCount+"");
                ps_leaveQuota.setString(3, Leave_New.get(String.valueOf(username) + "_EMPID").toString());
                ps_leaveQuota.setString(4, Leave_New.get("LEAVE_TYPE_ID").toString());
              } else {
                ps_leaveQuota = conn.prepareStatement("update hclhrm_prod_others.tbl_emp_leave_quota set USEDQTY=USEDQTY+?  where EMPLOYEEID=? AND LEAVETYPEID=? and status=1001 limit 1");
                ps_leaveQuota.setString(1, ""+LeaveCount+"");
                ps_leaveQuota.setString(2, Leave_New.get(String.valueOf(username) + "_EMPID").toString());
                ps_leaveQuota.setString(3, Leave_New.get("LEAVE_TYPE_ID").toString());
              } 
            } 
            int count1 = ps_1.executeUpdate();
            int count2 = ps_leaveQuota.executeUpdate();
            int[] Batchup = null;
            try {
              Batchup = ps_SingleDates.executeBatch();
            } catch (SQLException err) {
              conn.rollback();
              Atten_Req_Message = "Your Leave process failed please try again..!";
              err.printStackTrace();
            } 
            System.out.println(Batchup + "add Batch Count::" + count);
            System.out.println(Batchup + "add Batch length::" + Batchup.length);
            if (count > 0 && count1 > 0 && count2 > 0 && Batchup.length >= LeaveCount) {
              Atten_Req_Message = "Your leave successfully processed..";
              try {
                Flag = "1";
                conn.commit();
              } catch (SQLException erd) {
                System.out.println("1-Exception at ERD" + erd);
                conn.rollback();
                Atten_Req_Message = "Your Leave process failed please try again..!";
              } catch (Exception erd) {
                System.out.println("1-Exception at ERD" + erd);
                conn.rollback();
                Atten_Req_Message = "Your Leave process failed please try again..!";
              } 
            } else {
              conn.rollback();
              Atten_Req_Message = "Your Leave process failed please  try again/contact system admin..";
            } 
          } catch (Exception e2) {
            Atten_Req_Message = "Failed to process your request Please try again/ contact system admin.";
            e2.printStackTrace();
          } 
          System.out.println(String.valueOf(OTHFLAG) + "*********1st********8");
        } else {
          System.out.println(String.valueOf(OTHFLAG) + "*********2nd********8");
          if (!OTHFLAG.equalsIgnoreCase("N"))
            if (Leave_Type.equalsIgnoreCase("OD") || Leave_Type.equalsIgnoreCase("CF")) {
              Atten_Req_Message = " Number of days considered " + LeaveCount + " ";
            } else if (Leave_Type.equalsIgnoreCase("LOP")) {
              Atten_Req_Message = " Number of days considered " + LeaveCount + " ";
            } else if (Leave_Type.equalsIgnoreCase("WFH")) {
              Atten_Req_Message = " Number of days considered " + LeaveCount + " ";
            } else if (!maxleaveflag && Leave_Type.equalsIgnoreCase("CL")) {
              Atten_Req_Message = "CL Limit is Exceed Please check previous applied leave and woff & HL ";
            } else {
              if (Leave_Type.equalsIgnoreCase("CL"))
                LeaveCount = CL_DUMM_LEAV; 
              Atten_Req_Message = " Number of days considered " + LeaveCount + " & it will be deducted from your leave balance ";
            }  
        } 
      } else if (User_Auth_auth == 0) {
        Atten_Req_Message = "Please logout & try again / contact admin..!";
      } else if (Evvalid && Leave_Type.equalsIgnoreCase("EL")) {
    	
if(BU_Rights.equalsIgnoreCase("MUM"))
{
	Atten_Req_Message = " EL Applied Limit Exceed for this financial year. Ex: Applied Limit is  0->5 times for year..! ";
}
else {
	Atten_Req_Message = " EL Applied Limit Exceed for this financial year. Ex: Applied Limit is  0->3 times for year..! ";
}
        
        
      } else if (Integer.parseInt(EmpData.get("TODATE_VALID").toString()) == 1) {
        Atten_Req_Message = "improper Date selection Please Check & Re-process.";
      } else if (this.Unic_Leave > 0) {
        Atten_Req_Message = "Duplicate Dates not allowed Please Check & Re-process.";
      } else if (!check_leave_month) {
        Atten_Req_Message = "Monthly leave quota exceed..!";
      } else if (holidayexistpreview && !prev_combflag_of_leave) {
        Atten_Req_Message = Message;
      } else if (holidayexistfeature && !prev_flag_of_leave) {
        Atten_Req_Message = Message;
      } else if (Integer.parseInt(EmpData.get("DATE_VALID_SL").toString()) == 0 && Leave_Type.equalsIgnoreCase("SL")) {
        Atten_Req_Message = "SL Dates Should be <= Current Date (Please select previous dates to apply SL)  ";
      } else if (!feature_combflag_of_leave) {
        Atten_Req_Message = Message;
      } else if (!prev_combflag_of_leave) {
        Atten_Req_Message = Message;
      } else if (!prev_flag_of_leave) {
        Atten_Req_Message = Message;
      } else if (!feature_flag_of_leave) {
        Atten_Req_Message = Message;
      } else if (newyearflag_t == 1 || newyearflag_f == 1) {
        Atten_Req_Message = " Leaves are not enabled for Year 2022 ";
        Atten_Req_Message = LIMIT_LEAVE_ICONN_YEAR_MSG;
      } else if (newyearflag_t_old == 1 || newyearflag_f_old == 1) {
        Atten_Req_Message = " Leaves are disabled for Year 2021 ";
        Atten_Req_Message = LIMIT_LEAVE_ICONN_YEAR_MSG_OLD;
      } else if (FYCLSLCHECK == 1) {
        Atten_Req_Message = " Leaves are disabled for Year  " + to_date.split("-")[0];
      } else {
        Atten_Req_Message = "Authentication failed please Re-login/contact admin..!";
      } 
      System.out.println(String.valueOf(Atten_Req_Message) + "**************88");
    } catch (Exception Errr) {
      System.out.println("Exception At Last Error Code " + Errr);
      Atten_Req_Message = "invalid credientails please relogin/contact admin";
    } finally {
      try {
        //this.jdbcTemplate.getDataSource().getConnection().close();
      } catch (Exception e) {
        e.printStackTrace();
      } 
    } 
    Map<Object, Object> hm = new HashMap<>();
    hm.put("Message", Atten_Req_Message);
    hm.put("Flag", Flag);
    return hm;
  }
  
  public Map CL_Validation(Leave leaves) {
    StringBuffer buff = new StringBuffer();
    buff.append("select procedure.Get_leave_Back_count('" + leaves.getEmpID() + "','" + leaves.getFrom_date() + "') + procedure.Get_leave_Back_count_fast('" + leaves.getEmpID() + "','" + leaves.getTo_date() + "') as Leave_Add  from dual ");
    Map<Object, Object> clmap = new HashMap<>();
    clmap.put("LEAVEADD", "0");
    List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(buff.toString());
    for (Map<String, Object> row : rows)
      clmap.put("LEAVEADD", row.get("Leave_Add")); 
    return clmap;
  }
}

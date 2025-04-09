package com.hetero.heteroiconnect.assessment;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
public class AssessmentRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	//@Autowired
	//private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public synchronized JSONArray Initateassement() throws SQLException {
	 
		JSONArray empJson_arr = new JSONArray();
		try {
			StringBuffer query = new StringBuffer();
			StringBuilder query1 = new StringBuilder();
			StringBuilder empList = new StringBuilder();

			StringBuilder update_hr_tbl = new StringBuilder();
			StringBuilder update_mngr_tbl = new StringBuilder();
			StringBuilder update_assess_tbl = new StringBuilder();
			 
			
			query1.append(" SELECT distinct prob_emp_id, ");
			query1.append(" IF(DATE_ADD(DATE_FORMAT(LUPDATED,'%Y-%m-%d'),INTERVAL PROB_EXTEND_M0NTH Month)<=DATE_FORMAT(now(),'%Y-%m-%d'),1,0) AS 'FLAG' ");
			query1.append(" FROM assessment_form.tbl_assessment_approved_details  where status=1004; ");
			System.out.println(query1.toString() + "...extended ");

			
			 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(query1.toString());
			
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  
				  if (row.get("FLAG").toString().equalsIgnoreCase("1")) {
						empList.append(row.get("prob_emp_id").toString());
						empList.append(",");
					}
			  }
			  
			  empList.append("0");
	 

			System.out.println(empList + " empList");

			update_hr_tbl.append(
					" update assessment_form.tbl_hr_processingempinfo set status=1006,rec_status=1002 where emp_id in ("
							+ empList + "); ");
			update_mngr_tbl.append(
					" UPDATE assessment_form.tbl_manager_approvedlist SET status=1006,rec_status=1002 where prob_emp_id in ("
							+ empList + "); ");
			update_assess_tbl.append(
					" update assessment_form.tbl_assessment_approved_details set status=1006,rec_status=1002 where prob_emp_id  in ("
							+ empList + "); ");

			 
			int i=0,i1=0,i2=0;
			
			i =  jdbcTemplate.update(update_hr_tbl.toString());
			i1 = jdbcTemplate.update(update_mngr_tbl.toString());
			i2 = jdbcTemplate.update(update_assess_tbl.toString());

			 
			System.out.println(i);
			System.out.println(i1);
			System.out.println(i2);
			 
			query.append(
					" select distinct p.employeesequenceno emplid, p.callname 'CALLNAME',ifnull(C.NAME,'--')'BU', depart.name as Department,  co.NAME 'FieldType', ");
			query.append(" IF(p.companyid IN (15, 16, 33, 34) AND depart.departmentid IN (58, 82) and  h.employeesequenceno=10581, IFNULL(10447, '--'), IFNULL(h.employeesequenceno, '--')) AS 'nextApproverId', IF(p.companyid IN (15, 16, 33, 34) AND depart.departmentid IN (58, 82) and h.employeesequenceno=10581, 'C S REDDY', IFNULL(h.CALLNAME, '--')) AS 'NEXT_APPROVER' ,  t.name 'empType', ");
			//query.append(" ifnull(h.employeesequenceno,'--')'nextApproverId', ifnull(h.CALLNAME,'--')'NEXT_APPROVER',  t.name 'empType', ");
			query.append(
					" ifnull(date_format(b.dateofjoin,'%d-%m-%Y'),'--')'DOJ', B.dateofjoin, ifnull(date_format(DATE_ADD(B.dateofjoin, INTERVAL 6 month ),'%d-%m-%Y'),'--')'DOC' , ");
			query.append(
					" DATE_ADD(B.dateofjoin, INTERVAL 6 month ), TIMESTAMPDIFF(MONTH, B.dateofjoin, curdate()) as sam_maonth ");
			query.append(" from hclhrm_prod.tbl_employee_primary p ");
			query.append(" left join HCLHRM_PROD.tbl_employment_types t on t.employmenttypeid=p.employmenttypeid ");
			query.append(" left join HCLHRM_PROD.TBL_EMPLOYEE_PROFILE b on b.employeeid=p.employeeid ");
			query.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS d ON p.EMPLOYEEID=d.EMPLOYEEID ");
			query.append(" left join hcladm_prod.tbl_department ");
			query.append("  as depart on depart.DEPARTMENTID=d.DEPARTMENTID ");
			query.append(" LEFT JOIN hclhrm_prod.tbl_employee_primary h ON d.managerid=h.EMPLOYEEID ");
			query.append(" LEFT JOIN hcladm_prod.tbl_businessunit C ON C.BUSINESSUNITID=p.COMPANYID ");
			query.append(" LEFT JOIN hcladm_prod.tbl_costcenter co ON co.costcenterid=p.costcenterid ");
			query.append(
					" where  p.status in (1001,1401,1092) and c.callname in ('hyd')  and p.companyid!=25 and p.EMPLOYMENTTYPEID=2 ");
			query.append(
					" and co.name='OFFICE' and TIMESTAMPDIFF(MONTH, B.dateofjoin, DATE_ADD(curdate(),INTERVAL 30 DAY))>=6 ");
			 
			query.append(
					" and p.employeesequenceno not in(SELECT emp_id FROM assessment_form.tbl_hr_processingempinfo ");
			query.append(" where emp_id not in (0) and status  in (1002,1003,1005,1004)) ");

			//

			System.out.println(query);
			 
			 List<Map<String, Object>>  rowss = jdbcTemplate.queryForList(query.toString());
			 
			 for (@SuppressWarnings("rawtypes") Map row : rowss) {
				    JSONObject jsonObj1 = new JSONObject();
					jsonObj1.put("EMPID", row.get("emplid"));
					jsonObj1.put("FullName", row.get("CALLNAME"));
					jsonObj1.put("DOJ",row.get("DOJ"));
					// jsonObj1.put("Active_Status", rs.getString("Active_Status"));
					jsonObj1.put("FieldType",row.get("FieldType"));
					jsonObj1.put("empType",row.get("empType"));
					jsonObj1.put("BU", row.get("BU"));
					jsonObj1.put("Department", row.get("Department"));
					jsonObj1.put("isSelected", false);
					empJson_arr.add(jsonObj1);
			  }
 
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				// Stmt.close();
				// update_hr_Stmt.close();
				// update_mngr_Stmt.close();
				// update_assess_Stmt.close();
				// rs.close();
				// rs2.close();
				// conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		//jdbcTemplate.getDataSource().getConnection().close();
		return empJson_arr;
	}

	public synchronized JSONArray AssessmentAprovalReport() throws SQLException {

		//Connection conn = dataSource.getConnection();

		JSONArray hrFinalReport_arr = new JSONArray();
		//java.sql.ResultSet rs = null;
		int rowcount = 0;
		//java.sql.PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null;
		//System.out.println("conn~~~~~~~~~" + conn);

		//if (conn != null) {
			try {

				StringBuffer empList_query = new StringBuffer();
				 
				empList_query.append(
						" SELECT distinct  a.EMP_ID, a.EMP_NAME, a.EMPLOYEMENY_TYPE, a.FIELD_TYPE, a.BU, DATE_FORMAT(STR_TO_DATE(DOJ, '%d-%m-%Y'), '%d-%b-%Y') AS DOJ, a.DEPARTMENT, a.STATUS ,DATE_FORMAT(DATE_ADD(STR_TO_DATE(a.DOJ, '%d-%m-%Y'), INTERVAL 6 MONTH), '%d-%b-%Y') AS ActualProbationEndDate, ");
				empList_query.append(" if(a.Next_ApproverID!=''&& a.approved_by='10447','No Next Level', ");
				empList_query.append(
						"	CONCAT('Pending-at','-(',p.employeesequenceno,'-',p.CALLNAME,')'))as Next_Approver, ");
				empList_query.append(" DATE_FORMAT(a.Initiated_Date,'%d-%b-%Y') as Initiated_Date , ");
				empList_query.append(" IF(a.status=1002,CONCAT('Processed-by','-(',10452,'-','HR',')'), ");
				empList_query.append(
						" IF(a.status=1003,CONCAT('Processed-by','-(',approvedBy.employeesequenceno,'-',approvedBy.CALLNAME,')'), ");
				empList_query.append(
						" IF(a.status=1005,CONCAT('Completed-by','-(',approvedBy.employeesequenceno,'-',approvedBy.CALLNAME,')'), ");
				empList_query.append(
						" CONCAT('Extended-by','-(',approvedBy.employeesequenceno,'-',approvedBy.CALLNAME,')','-',extened_month, ");
				empList_query.append(
						" if(extened_month=1,'Month','Months'))))) ProcessedBy , DATE_FORMAT(a.LUPDATED,'%d-%m-%Y') as LUPDATED ");
				empList_query.append(" FROM assessment_form.tbl_hr_processingempinfo a ");
				empList_query
						.append(" left join  hclhrm_prod.tbl_employee_primary p1 on p1.employeesequenceno=a.EMP_ID ");
				empList_query.append(
						" left join hclhrm_prod.tbl_employee_primary p on p.employeesequenceno=a.next_approverid ");
				empList_query.append(
						" left join hclhrm_prod.tbl_employee_primary approvedBy on approvedBy.employeesequenceno=a.approved_by ");
				empList_query.append(" left join (SELECT prob_emp_id,sum(PROB_EXTEND_M0NTH) as extened_month ");
				empList_query.append(" FROM assessment_form.tbl_assessment_approved_details where status=1004 ");
				empList_query.append(" group by prob_emp_id) extendmonth on extendmonth.prob_emp_id=a.emp_id "
						+ "where  a.status in(1002, 1003,1005,1004) and p1.employmenttypeid=2 and p1.status in (1001,1401,1092) ORDER BY STR_TO_DATE(a.DOJ, '%d-%m-%Y') + INTERVAL 6 MONTH DESC");

				System.out.println(empList_query);
				 List<Map<String, Object>>  rowss = jdbcTemplate.queryForList(empList_query.toString());
				 for (@SuppressWarnings("rawtypes") Map row : rowss) {
					   JSONObject obj = new JSONObject();
					    obj.put("EMP_NAME", row.get("EMP_NAME"));
						obj.put("EMP_ID", row.get("EMP_ID"));
						obj.put("EMP_TYPE",row.get("EMPLOYEMENY_TYPE"));
						obj.put("FIELD_TYPE",row.get("FIELD_TYPE"));
						obj.put("BU",row.get("BU"));
						obj.put("DOJ", row.get("DOJ"));
						obj.put("DEPARTMENT",row.get("DEPARTMENT"));
						obj.put("STATUS",row.get("STATUS"));
						obj.put("Next_Approver", row.get("Next_Approver"));
						obj.put("Initiated_Date", row.get("Initiated_Date"));
						obj.put("LUPDATED",  row.get("LUPDATED"));
						obj.put("ProcessedBy", row.get("ProcessedBy"));
						obj.put("ActualProbationEndDate", row.get("ActualProbationEndDate"));
						
						hrFinalReport_arr.add(obj);
				 }

//				while (rs.next()) {
//					JSONObject obj = new JSONObject();
//					obj.put("EMP_NAME", rs.getString("EMP_NAME"));
//					obj.put("EMP_ID", rs.getString("EMP_ID"));
//					obj.put("EMP_TYPE", rs.getString("EMPLOYEMENY_TYPE"));
//					obj.put("FIELD_TYPE", rs.getString("FIELD_TYPE"));
//					obj.put("BU", rs.getString("BU"));
//					obj.put("DOJ", rs.getString("DOJ"));
//					obj.put("DEPARTMENT", rs.getString("DEPARTMENT"));
//					obj.put("STATUS", rs.getString("STATUS"));
//					obj.put("Next_Approver", rs.getString("Next_Approver"));
//					obj.put("Initiated_Date", rs.getString("Initiated_Date"));
//					obj.put("LUPDATED", rs.getString("LUPDATED"));
//					obj.put("ProcessedBy", rs.getString("ProcessedBy"));
//					hrFinalReport_arr.add(obj);
//
//				}
				System.out.println(" hrFinalReport_arr Query..........." + hrFinalReport_arr);

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					//stmt1.close();

					//rs.close();
					//conn.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		//}
		//jdbcTemplate.getDataSource().getConnection().close();
		return hrFinalReport_arr;
	}

	public synchronized JSONArray PermanentReport() throws SQLException {
		// Connection conn = dataSource.getConnection();
		JSONArray permanent_empList_arr = new JSONArray();
		// java.sql.ResultSet rs = null;
		int rowcount = 0;
	//	java.sql.PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null;
//       	System.out.println("conn~~~~~~~~~" +conn);

		//if (conn != null) {
			try {

				StringBuffer PermanentReport = new StringBuffer();

				PermanentReport.append(
						" SELECT distinct  a.EMP_ID, a.EMP_NAME, a.EMPLOYEMENY_TYPE, a.FIELD_TYPE, a.BU, a.DOJ, a.DEPARTMENT, a.STATUS , ");
				PermanentReport.append( 
						" DATE_FORMAT(DATE_ADD(STR_TO_DATE(a.DOJ, '%d-%m-%Y'), INTERVAL 6 MONTH), '%d-%m-%Y') AS ActualProbationEndDate,DATE_FORMAT(a.Initiated_Date,'%d-%m-%Y') as Initiated_Date, DATE_FORMAT(a.LUPDATED,'%d-%m-%Y') as LUPDATED , ");
				PermanentReport.append(" if(a.status=1005 ,'PERMANENT' ,'NA') as name, b.APPROVER_BY as ProcessedBy, ");
				PermanentReport.append(
						" b.PROB_EXTEND_M0NTH, b.IMMEDIATE_CMNTS as reason from assessment_form.tbl_hr_processingempinfo a ");
				PermanentReport.append(" join  hclhrm_prod.tbl_employee_primary p1 on p1.employeesequenceno=a.EMP_ID ");
				PermanentReport.append(
						" left join assessment_form.tbl_manager_approvedlist m on m.prob_emp_id=a.emp_id and m.manager_id =a.approved_by ");
				PermanentReport.append(
						" left join  assessment_form.tbl_assessment_approved_details b on b.prob_emp_id=a.emp_id and b.approver_by=a.approved_by ");
				PermanentReport
						.append(" left join hclhrm_prod.tbl_employee_primary p on p.employeesequenceno=a.approved_by ");
				PermanentReport
						.append(" where a.status=1005 and p1.employmenttypeid=2 and p1.status in (1001,1401,1092) ");

				System.out.println(PermanentReport);

				// stmt1 = conn.prepareStatement(PermanentReport.toString());
				
				 List<Map<String, Object>>  rowss = jdbcTemplate.queryForList(PermanentReport.toString());
				// rs = stmt1.executeQuery();
				 
				 for (@SuppressWarnings("rawtypes") Map row : rowss) {
					 
					    JSONObject obj = new JSONObject();
						obj.put("EMP_NAME", row.get("EMP_NAME"));
						obj.put("EMP_ID",row.get("EMP_ID"));
						obj.put("EMP_TYPE", row.get("EMPLOYEMENY_TYPE"));
						obj.put("FIELD_TYPE",row.get("FIELD_TYPE"));
						obj.put("BU", row.get("BU"));
						obj.put("DOJ",  row.get("DOJ"));
						obj.put("DEPARTMENT", row.get("DEPARTMENT"));
						obj.put("STATUS", row.get("STATUS"));
						obj.put("name", row.get("name"));
						obj.put("exnd_month", row.get("PROB_EXTEND_M0NTH"));
						obj.put("reason",  row.get("reason"));
						obj.put("Initiated_Date", row.get("Initiated_Date"));
						obj.put("LUPDATED",row.get("LUPDATED"));
						obj.put("ActualProbationEndDate",row.get("ActualProbationEndDate"));
						
						permanent_empList_arr.add(obj);
				 }
			 

				//System.out.println(" hrFinalReport_arr Query..........." + permanent_empList_arr);

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					// stmt1.close();

					// rs.close();
					// conn.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		//}
		//jdbcTemplate.getDataSource().getConnection().close();
		return permanent_empList_arr;
	}

	public synchronized JSONArray extendedReport() throws SQLException {
		JSONArray extended_empList_arr = new JSONArray();
			try {
				StringBuffer ExtendempList_query = new StringBuffer();
				ExtendempList_query.append(
						" SELECT distinct a.EMP_ID, a.EMP_NAME, a.EMPLOYEMENY_TYPE, a.FIELD_TYPE, a.BU, a.DOJ, a.DEPARTMENT, a.STATUS , ");
				ExtendempList_query.append(
						" DATE_FORMAT(a.Initiated_Date,'%d-%m-%Y') as Initiated_Date, DATE_FORMAT(a.LUPDATED,'%d-%m-%Y') as LUPDATED , ");
				ExtendempList_query.append(
						" if(a.status=1004 ,concat(p.employeesequenceno,'-',p.callname) ,'NA') as name, b.APPROVER_BY as ProcessedBy, ");
				ExtendempList_query.append(
						" b.PROB_EXTEND_M0NTH, b.IMMEDIATE_CMNTS as reason from assessment_form.tbl_hr_processingempinfo a ");
				ExtendempList_query.append(
						" left join assessment_form.tbl_manager_approvedlist m on m.prob_emp_id=a.emp_id and m.manager_id =a.approved_by ");
				ExtendempList_query.append(
						" left join  assessment_form.tbl_assessment_approved_details b on b.prob_emp_id=a.emp_id and b.approver_by=a.approved_by ");
				ExtendempList_query
						.append(" join hclhrm_prod.tbl_employee_primary p on p.employeesequenceno=a.approved_by ");
				ExtendempList_query
						.append(" join  hclhrm_prod.tbl_employee_primary p1 on p1.employeesequenceno=a.EMP_ID ");
				ExtendempList_query
						.append(" where a.status=1004 and p1.employmenttypeid=2 and p1.status in (1001,1401,1092)  ");

				System.out.println(ExtendempList_query);

				
				 List<Map<String, Object>>  rowss = jdbcTemplate.queryForList(ExtendempList_query.toString());
					// rs = stmt1.executeQuery();
					 for (@SuppressWarnings("rawtypes") Map row : rowss) {
						    JSONObject obj = new JSONObject();
							obj.put("EMP_NAME", row.get("EMP_NAME"));
							obj.put("EMP_ID",row.get("EMP_ID"));
							obj.put("EMP_TYPE",row.get("EMPLOYEMENY_TYPE"));
							obj.put("FIELD_TYPE", row.get("FIELD_TYPE"));
							obj.put("BU",row.get("BU"));
							obj.put("DOJ",row.get("DOJ"));
							obj.put("DEPARTMENT",row.get("DEPARTMENT"));
							obj.put("STATUS", row.get("STATUS"));
							obj.put("name", row.get("name"));
							obj.put("exnd_month",row.get("PROB_EXTEND_M0NTH"));
							obj.put("reason", row.get("reason"));
							obj.put("Initiated_Date",row.get("Initiated_Date"));
							obj.put("LUPDATED", row.get("LUPDATED"));
							extended_empList_arr.add(obj);
					 }
				

				//System.out.println(" hrFinalReport_arr Query..........." + extended_empList_arr);

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		// }
		// jdbcTemplate.getDataSource().getConnection().close();
		return extended_empList_arr;
	}

	@SuppressWarnings("unchecked")
	public synchronized int hrProcessingEmp(String userid, String session_userid) throws SQLException {
		// Connection conn = dataSource.getConnection();
		List<Long> list = new ArrayList<Long>();
		int counter = 0, counter2 = 0;
		long hr_sno = 0;
		List<Long> list_hr = new ArrayList<Long>();
		// Assessment_Email_update email_update= new Assessment_Email_update();

		// java.sql.PreparedStatement Stmt = null;
		// java.sql.PreparedStatement Stmt2 = null;
		// java.sql.ResultSet rs = null;
		// java.sql.ResultSet rs2 = null;

		Map map = new HashMap();
		String hod_email = "NA";
		//java.sql.PreparedStatement Stmt3 = null;
		try {
			StringBuffer query = new StringBuffer();
			StringBuffer query2 = new StringBuffer();
			StringBuffer query3 = new StringBuffer();
			query.append(
					"  select p.employeesequenceno as emplid, p.callname 'CALLNAME',ifnull(C.NAME,'--')'BU', depart.name as Department, ");
			query.append(
					" ifnull(f.email, 'NA') as EMP_Email, co.NAME 'FieldType', ifnull(h.employeesequenceno,'--')'mngrId', ifnull(h.CALLNAME,'--')'mngrName', ");
			query.append(" t.name 'empType', "
					+ " IF(p.companyid IN (15, 16, 33, 34) AND depart.departmentid IN (58, 82) and xy.EMPID=10581, IFNULL(10447, '--'),IFNULL(xy.EMPID, '--')) AS  nextApproverId, "
					+ " IF(p.companyid IN (15, 16, 33, 34) AND depart.departmentid IN (58, 82) and xy.EMPID=10581, IFNULL('10447-C S REDDY', '--'),IFNULL(xy.EMP_NAME, '--')) AS NEXT_APPROVER,"
					+ " y.hod_email, ");
			query.append(" IF(p.companyid IN (15, 16, 33, 34) AND depart.departmentid IN (58, 82) and xy.EMPID=10581, IFNULL('C S REDDY', '--'),IFNULL(xy.Name, '--')) AS NEXT_APPROVER_Name , "
					+ " ifnull(date_format(b.dateofjoin,'%d-%m-%Y'),'--')'DOJ', ");
			
			query.append(" IF(p.companyid IN (15, 16, 33, 34) AND depart.departmentid IN (58, 82) and xy.EMPID=10581, IFNULL('csreddy@heterohealthcare.com', '--'), IFNULL(ifnull(xy.Next_approver_Email,'sangeetha.b@heterohealthcare.com'), '--')) AS Next_approver_Email , ");
			
			query.append("  ifnull(date_format(DATE_ADD(B.dateofjoin, INTERVAL 6 month ),'%d-%m-%Y'),'--')'DOC' ");
			query.append("  from hclhrm_prod.tbl_employee_primary p ");
			query.append(" left join HCLHRM_PROD.tbl_employment_types t on t.employmenttypeid=p.employmenttypeid ");
			query.append("  left join HCLHRM_PROD.TBL_EMPLOYEE_PROFILE b on b.employeeid=p.employeeid ");
			query.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS d ON p.EMPLOYEEID=d.EMPLOYEEID ");
			query.append(" left join hcladm_prod.tbl_department as depart on depart.DEPARTMENTID=d.DEPARTMENTID ");
			query.append(" LEFT JOIN hclhrm_prod.tbl_employee_primary h ON d.managerid=h.EMPLOYEEID ");
			query.append(" LEFT JOIN hcladm_prod.tbl_businessunit C ON C.BUSINESSUNITID=p.COMPANYID ");
			query.append(" LEFT JOIN hcladm_prod.tbl_costcenter co ON co.costcenterid=p.costcenterid ");
			query.append(" left join hclhrm_prod.tbl_employee_professional_contact f on f.employeeid= p.employeeid ");
			query.append(" left join( ");
			query.append(
					" select if(count(*)>0,h.employeesequenceno,'10452') AS EMPID, cc.email as Next_approver_Email, ");
			query.append(" ifnull(h.callname ,'Sangeetha') as Name, ");
			query.append(" if(count(*)>0,concat(h.employeesequenceno,'-',h.callname),'10452') AS EMP_NAME ");
			query.append(" from hclhrm_prod.tbl_employee_primary p ");
			query.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON d.EMPLOYEEID=p.EMPLOYEEID ");
			query.append(" LEFT JOIN hclhrm_prod.tbl_employee_primary H ON D.managerid=H.EMPLOYEEID ");
			query.append(" left join hclhrm_prod.tbl_employee_professional_contact cc on cc.employeeid=D.managerid ");
			query.append(" where  p.employeesequenceno='" + userid + "' and H.status=1001 and P.STATUS=1001 ");
			query.append(" and h.employeesequenceno not in (10423,10160,10182,20206) ");
			query.append(" ) xy on 1=1 ");
			query.append(" left join(select  procedure.get_hod_id_name(employeesequenceno) as hod_email ");
			query.append(" from hclhrm_prod.tbl_employee_primary where employeesequenceno='" + userid
					+ "' and STATUS=1001 ");
			query.append(" ) y on 1=1 ");
			query.append("  where  p.status in (1001) and c.callname in ('hyd')  and p.companyid!=25 and ");
			query.append("  p.employeesequenceno ='" + userid + "'; ");

			System.out.println(query.toString());
			
			query2.append(" INSERT INTO assessment_form.tbl_hr_processingempinfo( EMP_NAME, EMP_ID, ");
			query2.append(" EMPLOYEMENY_TYPE, FIELD_TYPE, BU, DOJ, DEPARTMENT, STATUS, LUPDATED, Next_ApproverID,");
			query2.append(
					" Next_ApproverName, Next_Approver, Next_approver_Email, Initiated_Date,HOD_EMAIL_ID ) VALUES  ");
			query2.append(" (?, ?, ?,?, ?, ?, ?, 1002, NOW(),?,?,?,?,NOW(),?); ");

			
			 List<Map<String, Object>>  rowss = jdbcTemplate.queryForList(query.toString());
			 
			
			// Stmt = conn.prepareStatement(query.toString());
			// rs = Stmt.executeQuery();
			//Stmt2 = conn.prepareStatement(query2.toString(), new String[] { "MASTER_SNO" });
			// Stmt3=conn.prepareStatement(query3.toString());
			
			
			 KeyHolder keyHolder = new GeneratedKeyHolder();
 
			 for (@SuppressWarnings("rawtypes") Map row : rowss) {
				   // JSONObject obj = new JSONObject();
				     String str = row.get("CALLNAME").toString(); // .out.println(str ); EMP_Email
				     hod_email = row.get("hod_email").toString();
					 hod_email = hod_email.substring(hod_email.lastIndexOf("~") + 1);
					  
					 counter=jdbcTemplate.update(
				              connection -> {
				            	   
									 
				                  PreparedStatement ps = connection.prepareStatement(query2.toString(), new String[]{"MASTER_SNO"});
				                  ps.setString(1,  row.get("CALLNAME").toString());
				                  ps.setString(2,  row.get("emplid").toString());
				                  ps.setString(3,  row.get("empType").toString());
				                  ps.setString(4,  row.get("FieldType").toString());
				                  ps.setString(5,  row.get("BU").toString());
				  				  ps.setString(6,  row.get("DOJ").toString());
				  				  ps.setString(7,  row.get("Department").toString());
				  				  ps.setString(8,  row.get("nextApproverId").toString());
				  				  ps.setString(9,  row.get("NEXT_APPROVER_Name").toString());
				  				  ps.setString(10, row.get("NEXT_APPROVER").toString());
				  				  ps.setString(11, row.get("Next_approver_Email").toString());
				  				  ps.setString(12, row.get("hod_email").toString().substring(row.get("hod_email").toString().lastIndexOf("~") + 1)); 
				                  return ps;
				              }, keyHolder);

				      Number key = keyHolder.getKey();
				      System.out.println("Newly persisted customer generated id: " + key.longValue());
				      System.out.println("-- loading customer by id --");
				     // System.out.println(loadCustomerById(key.longValue())); 
				      
				        hr_sno = key.longValue();
						list.add(hr_sno);
						list.add((long) 99990.0);
					//	System.out.println(counter + "Generated  sno: " + rs2.getInt(1));
						
						if (counter > 0 && hr_sno > 0) {
							 
							map.put("employeeid",  row.get("emplid"));
							map.put("next_approvals",row.get("nextApproverId"));
							map.put("next_approvalemail",row.get("Next_approver_Email"));
							map.put("EMP_Email",row.get("EMP_Email"));

							int hr_count = hrInsertion(hr_sno, 0, map, userid, session_userid, 1002);
 
							if (counter > 0 && hr_count > 0) {
								System.out.println("INSERTED IN TWO TABLES>>>>>>>>>>>>>>>>>>>>>>>>>>");

							} else {
								//conn.rollback();
								counter = 0;
							}
						}	
			 }
			 
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		finally {
			try {
				// Stmt.close();
				//Stmt2.close();
				//conn.close();
				//rs.close();
				//rs2.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		//jdbcTemplate.getDataSource().getConnection().close();
		return counter;
	}

	public synchronized int hrInsertion(long hr_sno, long mngr_sno, Map map, String userid, String session_userid,
			int status) throws SQLException {

	//	Connection conn = dataSource.getConnection();
	 	int rowcount = 0;
		String counter_hr = "";
		JSONArray nextApprovals = new JSONArray();

		java.sql.ResultSet rs = null;
		System.out.println(hr_sno + "::::::::::::::::::::::::::::::::" + mngr_sno);
		// int rowcount=0;
		// java.sql.PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null;
		// System.out.println("conn~~~~~~~~~" +conn);

		System.out.println("conn~~~~~~~~~ entered in method ");

		 
		String hod_email = "NA";
		long Hr_SNO = 0l, Hr_master_SNO = 0;

		// if (conn != null) {
			try {
				System.out.println("conn~~~~~~~~~ entered in try ");
				StringBuffer query = new StringBuffer();
				StringBuffer query_select = new StringBuffer();
				StringBuffer query_update = new StringBuffer();
				StringBuffer hod_query = new StringBuffer();
			 
				hod_query.append(" select  procedure.get_hod_id_name(employeesequenceno) as hod_email ");
				hod_query.append(" from hclhrm_prod.tbl_employee_primary where employeesequenceno='" + userid + "' ");
				
				
			  List<Map<String, Object>>  rowss = jdbcTemplate.queryForList(hod_query.toString());
			  
			  
			  for (@SuppressWarnings("rawtypes") Map row : rowss) {
				   
				     hod_email = row.get("hod_email").toString();
					System.out.println(hod_email + ".....hod_emailS");
					//hod_email = rs.getString("hod_email");
					hod_email = hod_email.substring(hod_email.lastIndexOf("~") + 1);
			  }
				 
				query_select.append(
						" select count(prob_emp_id) as count,Hr_Master_SNO from assessment_form.tbl_assessment_hrprocess_email ");
				query_select.append(" where manager_id=" + session_userid + " and  prob_emp_id='" + userid
						+ "' and email_status=1001; ");

				
				  List<Map<String, Object>>  row1 = jdbcTemplate.queryForList(query_select.toString());
				  
				  System.out.println(row1);
				  
				  for (@SuppressWarnings("rawtypes") Map row : row1) {
					  
					  
					    counter_hr = row.get("count").toString();
					    if(row.get("Hr_Master_SNO")!=null)
					    {
					    	Hr_master_SNO = Long.parseLong(row.get("Hr_Master_SNO").toString());
					    }
						
						if (Hr_master_SNO > 0) {
							Hr_SNO = 0;
						} else {
							Hr_SNO = mngr_sno;
						}
				  }
				
				//System.out.println(query_select.toString());
			//	stmt2 = conn.prepareStatement(query_select.toString());
				//rs = stmt2.executeQuery();
//				if (rs != null && rs.next()) {
//					counter_hr = rs.getString("count");
//					Hr_master_SNO = rs.getLong("Hr_Master_SNO");
//					if (Hr_master_SNO > 0) {
//						Hr_SNO = 0;
//					} else {
//						Hr_SNO = mngr_sno;
//					}
//
//				}
				  
				  query_update.append(
							" update assessment_form.tbl_assessment_hrprocess_email set email_status=1002 , LDATED=now() ,HOD_EMAIL_ID='"
									+ hod_email + "' ");
					query_update.append(" where manager_id='" + session_userid + "' and email_status=1001 and prob_emp_id='"
							+ userid + "' ");
					query_update.append(" and Hr_Master_SNO=" + Hr_master_SNO + " and Mngr_sno=" + Hr_SNO + "; ");
				 

				
				if (counter_hr.equalsIgnoreCase("0") && counter_hr != null) {
					System.out.println("conn~~~~~~~~~ entered in if 0 ");
					//stmt1 = conn.prepareStatement(query.toString());
				//	System.out.println(map + "asessment email update  class .....::::");
					/***  ***/
//					stmt1.setString(1, map.get("employeeid").toString());
//					stmt1.setString(2, map.get("next_approvals").toString());
//					stmt1.setString(3, map.get("next_approvalemail").toString());
//					stmt1.setString(4, "NA"); // map.get("EMP_Email").toString()
//					stmt1.setString(5, "sangeetha.b@heterohealthcare.com");
//					stmt1.setString(6, "1001");
//					stmt1.setLong(7, hr_sno);
//					stmt1.setLong(8, 0);
//					// stmt1.setString(9,email_date);
//					// stmt1.setString(10,email_date);
//					stmt1.setString(9, "0000-00-00");
//					stmt1.setString(10, hod_email);
					
					//hr_sno=hr_sno;
					mngr_sno=0;
				}

				else if (counter_hr.equalsIgnoreCase("1") && counter_hr != null) {

					System.out.println(query_update.toString() + "conn~~~~~~~~~ entered in if 1 ");
					// stmt3 = conn.prepareStatement(query_update.toString());
					rowcount = jdbcTemplate.update(query_update.toString());;
					 
					if (rowcount > 0 && status == 1003) {
//						stmt1 = conn.prepareStatement(query.toString());
//
//						stmt1.setString(1, map.get("employeeid").toString());
//						stmt1.setString(2, map.get("next_approvals").toString());
//						stmt1.setString(3, map.get("next_approvalemail").toString());
//						stmt1.setString(4, "NA");
//						stmt1.setString(5, "sangeetha.b@heterohealthcare.com");
//						stmt1.setString(6, "1001");
//						stmt1.setLong(7, 0);
//						stmt1.setLong(8, mngr_sno);
//						// stmt1.setString(9,email_date);
//						// stmt1.setString(10,email_date);
//						stmt1.setString(9, "0000-00-00");
//						stmt1.setString(10, hod_email);
//						rowcount = stmt1.executeUpdate();
//						
						hr_sno=0;
						//mngr_sno=mngr_sno;
					}
				}
				
				
				
				query.append(
						" insert into assessment_form.tbl_assessment_hrprocess_email ( Prob_emp_id, Manager_Id, Manager_email, ");
				query.append(" Emp_Email, Hr_email, Email_Status, Hr_Master_SNO, LDATED , Mngr_sno, ");
				query.append(" Email_Date, Next_Email_Date, MAIL_SENT_TIME,HOD_EMAIL_ID)"
						+ " values ( '"+map.get("employeeid").toString()+"', '"+map.get("next_approvals").toString()+"','"+map.get("next_approvalemail").toString()+"', ");
				query.append(" 'NA','sangeetha.b@heterohealthcare.com', '1001', '"+hr_sno+"', now(),'"+mngr_sno+"',curdate(),curdate(),'0000-00-00','"+hod_email+"'); ");
				
				rowcount = jdbcTemplate.update(query.toString());

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			finally {
				try {
					// conn.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		//}
		//jdbcTemplate.getDataSource().getConnection().close();
		return rowcount;
	}

	public synchronized String checking_access(String user_id) throws SQLException {
		// Connection conn = dataSource.getConnection();
		String access_str = "";
		 
		try {
			StringBuffer query = new StringBuffer();
			query.append(" SELECT    if (count(*)>0,A.emp_id, 0) as conut ");
			query.append(" FROM assessment_form.tbl_hr_processingempinfo A ");
			query.append(" left join assessment_form.tbl_assessment_approved_details B on  B.MASTER_SNO=A.MNGR_SNO ");
			query.append(" where  A.status in(1002,1003,1005,1004) and  A.NEXT_APPROVERID='" + user_id
					+ "' or  B.MANAGER_ID='" + user_id + "' ");
			query.append(" or B.status in( 1003,1005,1004) and A.MNGR_SNO!=B.MASTER_SNO; ");

			System.out.println(query);
			 List<Map<String, Object>>  row1 = jdbcTemplate.queryForList(query.toString());
			  for (@SuppressWarnings("rawtypes") Map row : row1) {
				  access_str = row.get("conut").toString();
			  }
		 
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			 
		}
		return access_str;
	}

	public synchronized JSONArray probationaryListToMngr(String empID) throws SQLException {
		// Connection conn = dataSource.getConnection();
		JSONArray probaListToMngr_arr = new JSONArray();
		//java.sql.ResultSet rs = null;
		int rowcount = 0;
		//java.sql.PreparedStatement stmt1 = null;
		// System.out.println("conn~~~~~~~~~" +conn);

//		if (conn != null) {
			try {

				StringBuffer query_fillform = new StringBuffer();
 
				query_fillform.append(
						" Select distinct a.EMP_ID, a.MASTER_SNO, a.EMP_NAME,  a.EMPLOYEMENY_TYPE, a.FIELD_TYPE, ");
				query_fillform
						.append(" a.BU, a.DOJ,  a.DEPARTMENT,a.STATUS,   a.Next_ApproverID, a.Next_ApproverName ,");
				query_fillform.append(
						" if(a.status=1004,'VIEW',IF(a.status=1005&&APPROVED_BY=10447,'VIEW','FORMFILL')) as ApprovedBY");
				query_fillform.append(" from assessment_form.tbl_hr_processingempinfo a");
				query_fillform.append(" join hclhrm_prod.tbl_employee_primary p on p.employeesequenceno=a.EMP_ID");
				query_fillform.append(" where  next_approverid='" + empID + "'");
				query_fillform.append(
						" and a.status!=1006 and a.rec_status=1001 and a.EMP_ID not in(select a.EMP_ID  from assessment_form.tbl_hr_processingempinfo a");
				query_fillform.append(
						" join assessment_form.tbl_assessment_approved_details b on b.prob_emp_id=a.emp_id and  A.MNGR_SNO=B.MASTER_SNO");
				query_fillform.append(" where b.approver_by='" + empID
						+ "' and b.rec_status=1001) and p.employmenttypeid=2 and p.status in (1001,1401,1092)");
				query_fillform.append(
						" union all select a.EMP_ID, a.MASTER_SNO,  a.EMP_NAME,  a.EMPLOYEMENY_TYPE, a.FIELD_TYPE,");
				query_fillform.append(
						" a.BU, a.DOJ, a.DEPARTMENT,a.STATUS,  a.Next_ApproverID, a.Next_ApproverName,'VIEW' as ApprovedBY");
				query_fillform.append(" from assessment_form.tbl_hr_processingempinfo a");
				query_fillform.append(
						" join assessment_form.tbl_assessment_approved_details b on b.prob_emp_id=a.emp_id and  A.MNGR_SNO=B.MASTER_SNO");
				query_fillform.append(" join hclhrm_prod.tbl_employee_primary p on p.employeesequenceno=a.EMP_ID");
				query_fillform.append(" where b.approver_by='" + empID
						+ "' and b.rec_status=1001 and p.employmenttypeid=2 and p.status in (1001,1401,1092)");
				
				System.out.println(query_fillform.toString());
				 
				 List<Map<String, Object>>  row1 = jdbcTemplate.queryForList(query_fillform.toString());
				  for (@SuppressWarnings("rawtypes") Map row : row1) {
					  
					    JSONObject obct1 = new JSONObject();
						obct1.put("MASTER_SNO", row.get("MASTER_SNO"));
						obct1.put("EMP_NAME", row.get("EMP_NAME"));
						obct1.put("EMP_ID", row.get("EMP_ID"));
						obct1.put("EMP_TYPE", row.get("EMPLOYEMENY_TYPE"));
						obct1.put("FIELD_TYPE", row.get("FIELD_TYPE"));
						obct1.put("BU", row.get("BU"));
						obct1.put("DOJ", row.get("DOJ"));
						obct1.put("DEPARTMENT", row.get("DEPARTMENT"));
						obct1.put("STATUS", row.get("STATUS"));
						obct1.put("Next_ApproverID", row.get("Next_ApproverID")); // MNGR_STATUS,MNGR_SNO
						obct1.put("approver_by",row.get("ApprovedBY"));
						
						probaListToMngr_arr.add(obct1);
				  }
				System.out.println(probaListToMngr_arr);
			} catch (Exception ex) {
				System.out.println(ex + "    ...::probationaryListToMngr method ");

			} finally {
				try {
					//stmt1.close();

					//rs.close();
					//conn.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		//}
		//jdbcTemplate.getDataSource().getConnection().close();
		return probaListToMngr_arr;

	}

	public synchronized JSONObject assesmentfromview(String empID, String session_empID, String mode)
			throws SQLException {
		//Connection conn = dataSource.getConnection();
		JSONObject obct1 = new JSONObject();
		System.out.println(empID + "........." + session_empID);
		//java.sql.ResultSet rs = null;
		//int rowcount = 0;
		//java.sql.PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null;
		// System.out.println("conn~~~~~~~~~" +conn);

//		if (conn != null) {

			try {
				StringBuffer query = new StringBuffer();
				if (mode != null && mode.equalsIgnoreCase("VIEW")) {

					query.append(
							" SELECT a.MASTER_SNO, a.MANAGER_ID, a.PROB_EMP_ID, a.OVERALL_RATING, a.FUNCTIONAL_RATING, a.TRAINING_RATING,   ");
					query.append(
							" a.HOD_EXCEPTIONAL_CMNTS, a.IMMEDIATE_CMNTS, a.OTHER_CMNTS, a.PROB_STATUS, a.PROB_EXTEND_M0NTH, a.STATUS, ");
					query.append(" a.approver_by as APPROVER_BY , a.NEXT_APPROVER,n.Next_ApproverName  ");
					query.append(" FROM assessment_form.tbl_assessment_approved_details a ");
					query.append(
							" left join assessment_form.tbl_manager_approvedlist m on m.PROB_EMP_ID=a.PROB_EMP_ID and m.master_sno=a.master_sno ");
					query.append(
							" left join assessment_form.tbl_hr_processingempinfo n on n.EMP_ID=a.PROB_EMP_ID and  n.mngr_sno=a.master_sno ");
					query.append(" where   m.status in (1003, 1004, 1005) and a.PROB_EMP_ID='" + empID
							+ "' and a.approver_by='" + session_empID + "' ");
					// if(a.approver_by='"+session_empID+"','VIEW','FORM FILL')
				} else {
					query.append(
							" SELECT a.MASTER_SNO, a.MANAGER_ID, a.PROB_EMP_ID, a.OVERALL_RATING, a.FUNCTIONAL_RATING, a.TRAINING_RATING, ");
					query.append(
							" a.HOD_EXCEPTIONAL_CMNTS, a.IMMEDIATE_CMNTS, a.OTHER_CMNTS, a.PROB_STATUS, a.PROB_EXTEND_M0NTH, a.STATUS, ");
					query.append(
							" a.APPROVER_BY, a.NEXT_APPROVER FROM assessment_form.tbl_assessment_approved_details a ");
					query.append(
							" left join assessment_form.tbl_manager_approvedlist m on m.PROB_EMP_ID=a.PROB_EMP_ID and m.master_sno=a.master_sno ");
					query.append("where   m.status=1003 and a.PROB_EMP_ID='" + empID + "' and a.NEXT_APPROVER='"
							+ session_empID + "'; ");
				}
//				System.out.println(mode + "~~~~~~~~~~~~~~~~~~~~~" + query.toString());
//				stmt1 = conn.prepareStatement(query.toString());
//				rs = stmt1.executeQuery();
				
				 List<Map<String, Object>>  row1 = jdbcTemplate.queryForList(query.toString());
				  for (@SuppressWarnings("rawtypes") Map row : row1) {
					    obct1.put("MASTER_SNO", row.get("MASTER_SNO"));
						obct1.put("EMP_ID",row.get("PROB_EMP_ID"));
						obct1.put("OVERALL_RATING", row.get("OVERALL_RATING"));
						obct1.put("FUNCTIONAL_RATING", row.get("FUNCTIONAL_RATING"));
						obct1.put("TRAINING_RATING", row.get("TRAINING_RATING"));
						obct1.put("HOD_EXCEP_CMNTS", row.get("HOD_EXCEPTIONAL_CMNTS"));
						obct1.put("IMMEDIATE_CMNTS", row.get("IMMEDIATE_CMNTS"));
						obct1.put("OTHER_CMNTS", row.get("OTHER_CMNTS"));
						obct1.put("PROB_STATUS", row.get("PROB_STATUS"));
						obct1.put("PROB_EXTEND_M0NTH", row.get("PROB_EXTEND_M0NTH"));
						obct1.put("STATUS", row.get("STATUS")); // MNGR_STATUS,MNGR_SNO
						obct1.put("APPROVER_BY", row.get("APPROVER_BY"));
						obct1.put("NEXT_APPROVER", row.get("Next_APPROVER")); // Next_ApproverName
				  }
				 
				// System.out.println(nextLevelMngr_arr);
			} catch (Exception ex) {
				System.out.println(ex + "    ...::singleEmp_History_arr method ");

			} finally {
				try {
					//stmt1.close();
					//rs.close();
					//conn.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		//}
		// jdbcTemplate.getDataSource().getConnection().close();
		return obct1;

	}

	public synchronized JSONObject Assementemployeeprofiledata(String empID) throws SQLException {
		//Connection conn = dataSource.getConnection();
		JSONObject Emp_detailsToMngr_obj = new JSONObject();
		//java.sql.ResultSet rs = null;
		int rowcount = 0;
		//java.sql.PreparedStatement stmt1 = null;
		// System.out.println("conn~~~~~~~~~" +conn);

		//if (conn != null) {
			try {

				StringBuffer empList_query = new StringBuffer();
				empList_query.append(
						" SELECT * FROM ((SELECT A.EMPLOYEESEQUENCENO EmpId,A.CALLNAME EmpName,DATE_FORMAT(B.DATEOFJOIN,'%d-%m-%Y') DOJ,");
				empList_query.append(
						" GROUP_CONCAT(ifnull(QUALIFICATIONID,'Incomplete'))EduDetails,E.NAME Dept,F.NAME Desig,G.NAME Bu,");
				empList_query.append(
						" IFNULL(EX.EXPERIENCE,0.0)Expe,CAST((DATEDIFF(CURDATE(),B.DATEOFJOIN)+1)/365 AS DECIMAL(5,1))HeteroEx,");
				empList_query.append(
						" IFNULL((EX.EXPERIENCE),0.0) + (CAST((DATEDIFF(CURDATE(),B.DATEOFJOIN)+1)/365 AS DECIMAL(5,1)))TotalEx");
				empList_query.append(" FROM");
				empList_query.append(" HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A");
				empList_query.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE B ON A.EMPLOYEEID=B.EMPLOYEEID");
				empList_query.append(
						" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS C ON B.EMPLOYEEID=C.EMPLOYEEID");
				empList_query
						.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_EDUCATION_DETAILS D ON C.EMPLOYEEID=D.EMPLOYEEID");
				empList_query.append(" LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT E ON C.DEPARTMENTID=E.DEPARTMENTID");
				empList_query.append(" LEFT JOIN HCLADM_PROD.TBL_DESIGNATION F ON C.DESIGNATIONID=F.DESIGNATIONID");
				empList_query.append(" LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT G ON A.COMPANYID=G.BUSINESSUNITID");
				empList_query.append(" LEFT JOIN");
				empList_query
						.append(" (SELECT T1.EMPLOYEEID,CAST(SUM(T2.EXPERIENCE)/12 AS DECIMAL(5,1)) EXPERIENCE FROM");
				empList_query.append(" HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY T1");
				empList_query.append(
						" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_EXPERIENCE_DETAILS T2 ON T1.EMPLOYEEID=T2.EMPLOYEEID");
				empList_query.append(" GROUP BY T1.EMPLOYEEID) EX ON EX.EMPLOYEEID=A.EMPLOYEEID");
				empList_query.append(" WHERE");
				empList_query.append(" A.EMPLOYEESEQUENCENO IN (" + empID + ") GROUP BY A.EMPLOYEEID))A;");
				System.out.println(empList_query);

				// stmt1 = conn.prepareStatement(empList_query.toString());
				// rs = stmt1.executeQuery();
				
				 List<Map<String, Object>>  row1 = jdbcTemplate.queryForList(empList_query.toString());
				  for (@SuppressWarnings("rawtypes") Map row : row1) {
					  
					    JSONObject obj = new JSONObject();
						obj.put("EmpID", row.get("EmpId"));
						obj.put("Empname", row.get("EmpName"));
						obj.put("DOJ", row.get("DOJ"));
						obj.put("EduDetails", row.get("EduDetails"));
						obj.put("Dept", row.get("Dept"));
						obj.put("Desig",row.get("Desig"));
						obj.put("Bu", row.get("Bu"));
						obj.put("Past_Expe", row.get("Expe"));
						obj.put("HeteroEx",row.get("HeteroEx"));
						obj.put("TotalEx", row.get("TotalEx"));
						Emp_detailsToMngr_obj = obj;
				  }

				 
				System.out.println(" Employee Experience..........." + Emp_detailsToMngr_obj);

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
				//	stmt1.close();

				//	rs.close();
				//	conn.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		//}
		//jdbcTemplate.getDataSource().getConnection().close();
		return Emp_detailsToMngr_obj;
	}

	public synchronized JSONArray assessmentfeedback(String empID, String session_Userid) throws SQLException {
		// String mode
		//Connection conn = dataSource.getConnection();
		JSONArray Emp_ApprovalsHistory_arr = new JSONArray();
		java.sql.ResultSet rs = null;
		int rowcount = 0;
	//	java.sql.PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null;
		// System.out.println("conn~~~~~~~~~" + conn);

		// if (conn != null) {
			try {
				StringBuffer query = new StringBuffer();

				/*
				 * select SNO from assessment_form.tbl_assessment_approved_details where sno<= (
				 * select sno from assessment_form.tbl_assessment_approved_details where
				 * manager_id in (10452) and PROB_EMP_ID=10515 ) and PROB_EMP_ID=10515;
				 */

				/* if(mode.equalsIgnoreCase("FormFill")){ */

				/*
				 * query.
				 * append(" SELECT a.MASTER_SNO, a.MANAGER_ID, a.PROB_EMP_ID, a.OVERALL_RATING, a.FUNCTIONAL_RATING, a.TRAINING_RATING, "
				 * ); query.
				 * append(" a.HOD_EXCEPTIONAL_CMNTS, a.IMMEDIATE_CMNTS, a.OTHER_CMNTS, a.PROB_STATUS, a.PROB_EXTEND_M0NTH, a.STATUS, "
				 * ); query.
				 * append(" a.APPROVER_BY, a.NEXT_APPROVER FROM assessment_form.tbl_assessment_approved_details a "
				 * ); query.
				 * append(" left join assessment_form.tbl_manager_approvedlist m on m.PROB_EMP_ID=a.PROB_EMP_ID and m.master_sno=a.master_sno "
				 * ); query.append(
				 * "where m.status not in (1003,1004,1005) and a.PROB_EMP_ID='"+empID+"' ");
				 */

				query.append(
						" SELECT a.MASTER_SNO, a.MANAGER_ID, a.PROB_EMP_ID, a.OVERALL_RATING, a.FUNCTIONAL_RATING, a.TRAINING_RATING,  ");
				query.append(
						" a.HOD_EXCEPTIONAL_CMNTS, a.IMMEDIATE_CMNTS, a.OTHER_CMNTS, a.PROB_STATUS, a.PROB_EXTEND_M0NTH, a.STATUS,p.callname, ");
				query.append(" a.APPROVER_BY, a.NEXT_APPROVER FROM assessment_form.tbl_assessment_approved_details a ");
				query.append(
						" left join assessment_form.tbl_manager_approvedlist m on m.PROB_EMP_ID=a.PROB_EMP_ID and m.master_sno=a.master_sno");
				query.append(" left join hclhrm_prod.tbl_employee_primary p on p.employeesequenceno=a.APPROVER_by ");
				query.append("  where m.status not in (1006) and ");
				query.append(" a.PROB_EMP_ID='" + empID + "' and ");
				query.append(" IF(m.status in (1005,1004),a.PROB_EMP_ID='" + empID + "', ");
				query.append(" A.SNO<= ");
				query.append(" (SELECT max(sno) AS MASTER_SNO FROM assessment_form.tbl_assessment_approved_details A ");
				query.append(" join  assessment_form.tbl_hr_processingempinfo B on B.MNGR_SNO=A.MASTER_SNO ");
				query.append(" where PROB_EMP_ID='" + empID + "' and A.NEXT_APPROVER=" + session_Userid
						+ " or  A.MANAGER_ID=" + session_Userid + ") ); ");
				// m.status not in (1003,1004,1005)
				System.out.println(query + "   ....all");
				//stmt1 = conn.prepareStatement(query.toString());
				// rs = stmt1.executeQuery();
				System.out.println(query);
				 List<Map<String, Object>>  row1 = jdbcTemplate.queryForList(query.toString());
				  for (@SuppressWarnings("rawtypes") Map row : row1) {
					    JSONObject obct1 = new JSONObject();
						obct1.put("MASTER_SNO", row.get("MASTER_SNO"));
						obct1.put("EMP_ID", row.get("PROB_EMP_ID"));
						obct1.put("OVERALL_RATING", row.get("OVERALL_RATING"));
						obct1.put("FUNCTIONAL_RATING", row.get("FUNCTIONAL_RATING"));
						obct1.put("TRAINING_RATING", row.get("TRAINING_RATING"));
						obct1.put("HOD_EXCEP_CMNTS",row.get("HOD_EXCEPTIONAL_CMNTS"));
						obct1.put("IMMEDIATE_CMNTS", row.get("IMMEDIATE_CMNTS"));
						obct1.put("OTHER_CMNTS", row.get("OTHER_CMNTS"));
						obct1.put("PROB_STATUS", row.get("PROB_STATUS"));
						obct1.put("PROB_EXTEND_M0NTH", row.get("PROB_EXTEND_M0NTH"));
						obct1.put("STATUS", row.get("STATUS")); // MNGR_STATUS,MNGR_SNO
						obct1.put("APPROVER_BY", row.get("APPROVER_BY"));
						obct1.put("NEXT_APPROVER", row.get("Next_APPROVER"));
						obct1.put("callname", row.get("callname"));
						Emp_ApprovalsHistory_arr.add(obct1);
				  }
				
 
			} catch (Exception ex) {
				System.out.println(ex + "    ...::Emp_ApprovalsHistory_arr method ");

			} finally {
				 

			}
		//}
		//jdbcTemplate.getDataSource().getConnection().close();
		return Emp_ApprovalsHistory_arr;

	}

	public synchronized String assessmentsubmit(Assesment ass) throws SQLException {

		String Response = "";
		// String Routing = request.getParameter("Routing");
		String employeeid = ass.getEmpID();
		// String userid1= request.getParameter("userid");//mode
		// String mode= request.getParameter("mode");

		//  sysout
		//  Connection conn = dataSource.getConnection();
		//  java.sql.ResultSet Res = null;
		long key = 0;
		Map map = new HashMap();
		int row_count = 0, row_count2 = 0, status = 1003;
		String probMonth_str = "", month_extened = "";

		try {

			// conn=null;
			// String Connectionstatus=null;
			 

			if (ass.getProbtnStatus() != null)// month_chk
			{
				probMonth_str = ass.getProbtnStatus();
				month_extened = ass.getProbtn_extndMonths();
			}

			if (employeeid != null && probMonth_str.equalsIgnoreCase("NO")) {
				status = 1004;
			} else if (employeeid.equalsIgnoreCase("10447") && probMonth_str.equalsIgnoreCase("NO")) {
				status = 1004;
			} else if (employeeid.equalsIgnoreCase("10447") && probMonth_str.equalsIgnoreCase("YES")) {
				status = 1005;
			}

			key = managerApproval_Insertion(ass, status);
			System.out.println(key + "  inserted");
			if (key > 0) {
				row_count = managerComments_Insertion(ass, key, status);
				System.out.println(row_count + " comments table inserted");
			} else {
				//conn.rollback();
				// request.setAttribute("submittedSMSsms", " Something Went wrong, Please
				// contact HR...!! " );
				Response = "Something Went wrong, Please contact HR...!!";

			}
			if (row_count > 0 && key > 0) {

				Response = "Your request has been submitted successfully..!!";
			} else {
				//conn.rollback();

				Response = "Something Went wrong, Please contact HR...!!";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex);
		} finally {
		//	conn.close();
		}
		// jdbcTemplate.getDataSource().getConnection().close();
		return Response;
	}

	public synchronized long managerApproval_Insertion(Assesment ass, int status) throws SQLException {

		//Connection conn = dataSource.getConnection();
		long mngrinsert_Count = 0;
		List<Long> list = new ArrayList<Long>();

		//java.sql.ResultSet rs1 = null, rs2 = null;
		int rowcount = 0, emp_count = 0;
		//java.sql.PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null;
		// System.out.println("conn~~~~~~~~~" +conn);

	//	if (conn != null) {
			try {
				String prob_emp = ass.getUserid();
				StringBuffer query_main = new StringBuffer();
				StringBuffer query11_select = new StringBuffer();
				StringBuffer query12_insert = new StringBuffer();
				StringBuffer query13_update = new StringBuffer();

				/*
				 * query_main.append(
				 * " update assessment_form.tbl_hr_processingempinfo set status=1003, Next_ApproverID='10332' , "
				 * ); query_main.append(
				 * " MNGR_SNO=8, LUPDATED=now() where emp_id='"+prob_emp+"'; ");
				 */
				//prob_emp_id,master_sno
				
				query11_select.append("select prob_emp_id,master_sno  FROM assessment_form.tbl_manager_approvedlist where prob_emp_id='"+prob_emp+"' and status!=1006 "); // "+prob_emp+"
				 
				System.out.println(query11_select.toString());
				//stmt1 = conn.prepareStatement(query11_select.toString());
				
				boolean flag=false;
				
			  List<Map<String, Object>>  rows = jdbcTemplate.queryForList(query11_select.toString());
				  for (@SuppressWarnings("rawtypes") Map row : rows) {
					  
					    System.out.println("Prasad----->"+row);
					    emp_count =Integer.parseInt(row.get("prob_emp_id").toString()) ;
						mngrinsert_Count =Long.parseLong(row.get("master_sno").toString());
						list.add(mngrinsert_Count);
						list.add((long) 99990.0);
						System.out.println(mngrinsert_Count + "Generated  sno: " + emp_count);
						
						
						flag=true;
				  }
				
				 
				 
				//rs1 = stmt1.executeQuery();

//				if (rs1 != null && rs1.next()) {
//					emp_count = rs1.getInt("EMP_Count");
//					mngrinsert_Count = rs1.getLong("SNO");
//					list.add(mngrinsert_Count);
//					list.add((long) 99990.0);
//					System.out.println(mngrinsert_Count + "Generated  sno: " + emp_count);
//				}

			
				
				 System.out.println(query13_update);
				 System.out.println(query_main);
				
				
				query12_insert.append(
						" insert into assessment_form.tbl_manager_approvedlist ( MANAGER_ID, PROB_EMP_ID, STATUS, ");
				query12_insert.append(" APPROVER_BY, NEXT_APPROVER, LUPDATED) values ( ?, ?, ?, ?, ?, now()); ");
				
				
				if (mngrinsert_Count == 0 && emp_count == 0) {
					 
					 KeyHolder keyHolder = new GeneratedKeyHolder();
					   
					 rowcount=jdbcTemplate.update(
					              connection -> {
					                  PreparedStatement ps = connection.prepareStatement(query12_insert.toString(), new String[]{"MASTER_SNO"});
					                  ps.setString(1,  ass.getEmpID());
					                  ps.setString(2,  ass.getUserid());
					                  ps.setInt(3, status);
					                  ps.setString(4, ass.getEmpID());
					                  ps.setString(5, ass.getNextapproval_ID());
					  				 
					  				  return ps;
					              }, keyHolder);

					      Number key = keyHolder.getKey();
					      System.out.println("Newly persisted customer generated id: " + key.longValue());
					      System.out.println("-- loading customer by id --");
					     // System.out.println(loadCustomerById(key.longValue())); 
					      
					        mngrinsert_Count = key.longValue();
							list.add(mngrinsert_Count);
							list.add((long) 99990.0);
					      
					     
					System.out.println(query12_insert + "....managerApproval_Insertion");
			
					System.out.println(mngrinsert_Count + "values inserted in master table................................");
 
					
					query_main.append(
							" update assessment_form.tbl_hr_processingempinfo set status='"+status+"', Next_ApproverID='"+ass.getNextapproval_ID()+"' , MNGR_SNO='"+mngrinsert_Count+"', Approved_by='"+ass.getEmpID()+"' , ");
					query_main.append(
							" LUPDATED=now(),Next_ApproverName='"+ass.getNextapproval_name()+"', Next_Approver='"+ ass.getNextapproval_ID_NAME()+"', Next_approver_Email='"+ass.getNextapproval_email()+"' where emp_id='"
									+ prob_emp + "' and status!=1006; ");
					
					rowcount =jdbcTemplate.update(query_main.toString());

				} 
				 
				else if (mngrinsert_Count > 0 && emp_count > 0) {
					
					
					
					query13_update.append(
							" UPDATE assessment_form.tbl_manager_approvedlist SET APPROVER_BY='"+ass.getEmpID()+"' ,NEXT_APPROVER='"+ass.getNextapproval_ID()+"' , ");
					query13_update.append(" LUPDATED=now(), MANAGER_ID='"+ass.getEmpID()+"', STATUS='"+status+"' where master_sno=" + mngrinsert_Count
							+ " and status!=1006;");
	 
					
					
					
					query_main.append(
							" update assessment_form.tbl_hr_processingempinfo set status='"+status+"', Next_ApproverID='"+ass.getNextapproval_ID()+"' , MNGR_SNO='"+mngrinsert_Count+"', Approved_by='"+ass.getEmpID()+"' , ");
					query_main.append(
							" LUPDATED=now(),Next_ApproverName='"+ass.getNextapproval_name()+"', Next_Approver='"+ ass.getNextapproval_ID_NAME()+"', Next_approver_Email='"+ass.getNextapproval_email()+"' where emp_id='"
									+ prob_emp + "' and status!=1006; ");
					
					
					System.out.println(query13_update + "...query13_update");

				
					System.out.println(query13_update);
					// rowcount = stmt2.executeUpdate();
					
					 
					rowcount =jdbcTemplate.update(query13_update.toString());

					
					rowcount =jdbcTemplate.update(query_main.toString());

					// **************** Manager Emai

				}
				
				
			 

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					//stmt1.close();
					//rs1.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		//}
		//jdbcTemplate.getDataSource().getConnection().close();
		return mngrinsert_Count;
	}

	public synchronized JSONArray nextApprovals(String empID) throws SQLException {

		JSONArray nextApprovals_arr = new JSONArray();
	//	Connection conn = dataSource.getConnection();
		// JSONObject nextApprovals= new JSONObject();

		//java.sql.ResultSet rs = null;
		int rowcount = 0;
		//java.sql.PreparedStatement stmt1 = null;
//			System.out.println("conn~~~~~~~~~" +conn);

		//if (conn != null) {
			try {
				StringBuffer query12 = new StringBuffer();
				// empID, name

//				query12.append(" select if(count(*)>0,h.employeesequenceno,'10447') AS EMPID,  ifnull(h.callname ,'CS Reddy') as callname ,  ");
//				query12.append(" ifnull(cc.email, 'csreddy@heterohealthcare.com') as Next_approver_Email, ");
//				query12.append(" if(count(*)>0,concat(h.employeesequenceno,'-',h.callname),'10447-HR') AS EMP_NAME, ");
//				query12.append(" if(count(*)=0,'YES','NO') AS ISHOD from hclhrm_prod.tbl_employee_primary p ");
//				query12.append(
//						" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON d.EMPLOYEEID=p.EMPLOYEEID ");
//				query12.append(" LEFT JOIN hclhrm_prod.tbl_employee_primary H ON D.managerid=H.EMPLOYEEID ");
//				query12.append(
//						" left join hclhrm_prod.tbl_employee_professional_contact cc on cc.employeeid=D.managerid ");
//				query12.append(" where  p.employeesequenceno=" + empID + " and H.status=1001 and P.STATUS=1001 ");
//				query12.append(" and h.employeesequenceno not in (10423,10160,10182,20206); ");
//				
				
				//StringBuffer query12 = new StringBuffer();

				query12.append("SELECT\n")
				    .append("    CASE\n")
				    .append("        WHEN h.employeesequenceno = 10581\n")
				    .append("            AND h.companyid IN (15, 16, 33, 34)\n")
				    .append("            AND depart.departmentid IN (58, 82) THEN '10447'\n")
				    .append("        ELSE IF(count(*) > 0, h.employeesequenceno, '10447')\n")
				    .append("    END AS EMPID,\n")
				    .append("    CASE\n")
				    .append("        WHEN h.employeesequenceno = 10581\n")
				    .append("            AND h.companyid IN (15, 16, 33, 34)\n")
				    .append("            AND depart.departmentid IN (58, 82) THEN 'C S REDDY'\n")
				    .append("        ELSE IFNULL(h.callname, 'CS Reddy')\n")
				    .append("    END AS callname,\n")
				    .append("    CASE\n")
				    .append("        WHEN h.employeesequenceno = 10581\n")
				    .append("            AND h.companyid IN (15, 16, 33, 34)\n")
				    .append("            AND depart.departmentid IN (58, 82) THEN 'csreddy@heterohealthcare.com'\n")
				    .append("        ELSE IFNULL(cc.email, 'csreddy@heterohealthcare.com')\n")
				    .append("    END AS Next_approver_Email,\n")
				    .append("    CASE\n")
				    .append("        WHEN p.employeesequenceno = 10581\n")
				    .append("            AND p.companyid IN (15, 16, 33, 34)\n")
				    .append("            AND depart.departmentid IN (58, 82) THEN '10447-C S REDDY'\n")
				    .append("        ELSE IF(count(*) > 0, CONCAT(h.employeesequenceno, '-', h.callname), '10447-HR')\n")
				    .append("    END AS EMP_NAME,\n")
				    .append("    CASE\n")
				    .append("        WHEN p.employeesequenceno = 10581\n")
				    .append("            AND p.companyid IN (15, 16, 33, 34)\n")
				    .append("            AND depart.departmentid IN (58, 82) THEN 'NO'\n")
				    .append("        ELSE IF(count(*) = 0, 'YES', 'NO')\n")
				    .append("    END AS ISHOD\n")
				    .append("FROM\n")
				    .append("    hclhrm_prod.tbl_employee_primary p\n")
				    .append("    LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON d.EMPLOYEEID = p.EMPLOYEEID\n")
				    .append("    LEFT JOIN hclhrm_prod.tbl_employee_primary H ON D.managerid = H.EMPLOYEEID\n")
				    .append("    LEFT JOIN hclhrm_prod.tbl_employee_professional_contact cc ON cc.employeeid = D.managerid\n")
				    .append("    LEFT JOIN hcladm_prod.tbl_department AS depart ON depart.DEPARTMENTID = d.DEPARTMENTID\n")
				    .append("WHERE\n")
				    .append("    p.employeesequenceno = 10447\n")
				    .append("    AND H.status = 1001\n")
				    .append("    AND P.STATUS = 1001\n")
				    .append("    AND h.employeesequenceno NOT IN (10423, 10160, 10182, 20206);");

				
				
				

				System.out.println(query12);
				
				
				 List<Map<String, Object>>  row1 = jdbcTemplate.queryForList(query12.toString());
				  for (@SuppressWarnings("rawtypes") Map row : row1) {
					  
					     JSONObject nextApprovals = new JSONObject();
						nextApprovals.put("nextapproval_ID", row.get("EMPID"));
						nextApprovals.put("ISHOD", row.get("ISHOD"));
						nextApprovals.put("nextapproval_name", row.get("callname"));
						nextApprovals.put("nextapproval_ID_NAME", row.get("EMP_NAME"));
						nextApprovals.put("nextapproval_email",  row.get("Next_approver_Email"));
						nextApprovals_arr.add(nextApprovals); // .add(nextApprovals);
					  
				  }
				 
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {

			}
	//	}

		//jdbcTemplate.getDataSource().getConnection().close();

		return nextApprovals_arr;
	}

	// List<Long> list= new ArrayList<Long>();
	@SuppressWarnings("unchecked")
	public synchronized int managerComments_Insertion(Assesment ass, long key, int status) throws SQLException {
		// Connection conn = dataSource.getConnection();
		int mngrinsert_count = 0, hr_count = 0;
	//	java.sql.ResultSet rs = null;
		int rowcount = 0;
		String hod_comnts = "", OTHER_CMNTS = "", UR_CMNTS = "NA";
		// java.sql.PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null;
		// System.out.println("conn~~~~~~~~~" +conn);

	//	if (conn != null) {
			try {
				StringBuffer query12 = new StringBuffer();
				StringBuffer functional_str = new StringBuffer();
				StringBuffer training_str = new StringBuffer();
				StringBuffer probMonth_str = new StringBuffer();
				// System.out.println(map.size() +" map size");
				String month_extened = "0";
				// System.out.println(map +"....map");

				if (ass.getWrkKnwldge() != null) { // myMap.get(key)!=null
					functional_str.append(ass.getWrkKnwldge());
					functional_str.append(",");

				}

				if (ass.getComments() != null) { // myMap.get(key)!=null

				}
				// ImmediateRemarks1
				if (ass.getAnlytclSkills() != null) {
					functional_str.append(ass.getAnlytclSkills());
					functional_str.append(",");
				}
				if (ass.getCommnctnSkills() != null) {
					functional_str.append(ass.getCommnctnSkills());
					functional_str.append(",");
				}
				if (ass.getIntrPrsnlSkills() != null) {
					functional_str.append(ass.getIntrPrsnlSkills());
					functional_str.append(",");

				}
				if (ass.getTeamWork() != null) {
					functional_str.append(ass.getTeamWork());
					functional_str.append(",");
				}
				if (ass.getAttitude_bhvr() != null) {
					functional_str.append(ass.getAttitude_bhvr());
					functional_str.append(",");
				}
				if (ass.getcGMP() != null) {
					functional_str.append(ass.getcGMP());
					functional_str.append(",");
				}
				if (ass.getOthersImprvmnts() != null) {
					// functional_str.append(map.get("area_chk8").toString());
					functional_str.append(ass.getOthersImprvmnts_Value());
					functional_str.append(",");
				}
				functional_str.append("0");

				// System.out.println(map.size());
				if (ass.getFunctnlTrng() != null) {
					training_str.append(ass.getFunctnlTrng());
					training_str.append(",");

				}
				if (ass.getTechnclTrng() != null) {
					training_str.append(ass.getTechnclTrng());
					training_str.append(",");
				}
				if (ass.getBhvrlTrng() != null) {
					training_str.append(ass.getBhvrlTrng());
					training_str.append(",");
				}
				if (ass.getOtherTrng() != null) {
					// training_str.append(map.get("training_chk4").toString());

					training_str.append(ass.getOtherTrngValue());
					training_str.append(",");

				}
				training_str.append("0");

				if ((ass.getProbtnStatus() != null) && (ass.getProbtnStatus()).equalsIgnoreCase("YES")) {
					probMonth_str.append(ass.getProbtnStatus());
					month_extened = "0";

				}
				if ((ass.getProbtnStatus() != null) && (ass.getProbtnStatus()).equalsIgnoreCase("NO")) {
					probMonth_str.append(ass.getProbtnStatus());
					month_extened = ass.getProbtn_extndMonths();
				}

				if (ass.getHOD_exptn_comments() == null) {
					hod_comnts = "NA";

				}
				if (ass.getHOD_exptn_comments() != null) {
					hod_comnts = ass.getHOD_exptn_comments();
				}

				if (ass.getComments() == null) {
					UR_CMNTS = "NA";

				}

				if (ass.getComments() != null) {
					UR_CMNTS = ass.getComments();

				}

				/*
				 * if(map.get("otherRemarks")==null){ OTHER_CMNTS="NA";
				 * 
				 * } if(map.get("otherRemarks")!=null){
				 * OTHER_CMNTS=map.get("otherRemarks").toString();
				 * 
				 * }
				 */

				query12.append(
						" insert into assessment_form.tbl_assessment_approved_details ( MASTER_SNO, MANAGER_ID,");
				query12.append(
						"PROB_EMP_ID, OVERALL_RATING, FUNCTIONAL_RATING, TRAINING_RATING, HOD_EXCEPTIONAL_CMNTS,");
				query12.append(
						"IMMEDIATE_CMNTS, OTHER_CMNTS, PROB_STATUS, PROB_EXTEND_M0NTH, STATUS, APPROVER_BY, NEXT_APPROVER, LUPDATED)");
				query12.append("values('"+key+"','"+ass.getEmpID()+"','"+ass.getUserid()+"','"+ass.getCategory()+"','"+functional_str.toString()+"','"+training_str.toString()+"','"+hod_comnts+"','"+UR_CMNTS+"','NA','"+probMonth_str.toString()+"', '"+month_extened+"','"+status+"', '"+ass.getEmpID()+"','"+ass.getNextapproval_ID()+"', now()); ");

				System.out.println(query12);

//				stmt1 = conn.prepareStatement(query12.toString());
//				stmt1.setLong(1, key);
//				stmt1.setString(2, ass.getEmpID());
//				stmt1.setString(3, ass.getUserid());
//				stmt1.setString(4, ass.getCategory());
//				stmt1.setString(5, functional_str.toString());
//				stmt1.setString(6, training_str.toString());
//				stmt1.setString(7, hod_comnts);
//				stmt1.setString(8, UR_CMNTS);
//				stmt1.setString(9, "NA");
//				stmt1.setString(10, probMonth_str.toString());
//				stmt1.setString(11, month_extened);
//				stmt1.setInt(12, status);
//				stmt1.setString(13, ass.getEmpID());
//				stmt1.setString(14, ass.getNextapproval_ID());

				//rowcount = stmt1.executeUpdate();
				 
				rowcount=jdbcTemplate.update(query12.toString());
				Map map = new HashMap();
				if (rowcount > 0) {
					map.put("employeeid", ass.getUserid());
					map.put("next_approvals", ass.getNextapproval_ID());
					map.put("next_approvalemail", ass.getNextapproval_email());
					// map.put("EMP_Email",rs.getString("EMP_Email").toString());
					hr_count = hrInsertion(0, key, map, ass.getUserid(), ass.getEmpID(), status);
					// int hr_count=hrInsertion(hr_sno,0, map , userid , session_userid ,1002);
					System.out.println(" IN HR TABLE DATA  INSERTED ...:::");

				} else {
					// conn.rollback();
					System.out.println(" IN HR TABLE DATA  ROLLBAK");
				}

				// System.out.println(mngrinsert_Count+"values inserted in master
				// table................................" );

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					//stmt1.close();
					//conn.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		//}
		//jdbcTemplate.getDataSource().getConnection().close();
		return rowcount;
	}

}

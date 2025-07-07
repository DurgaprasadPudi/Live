package com.hetero.heteroiconnect.masterreports;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hetero.heteroiconnect.hrassetrequests.Master;

@Repository
public class MasterReportRepository {

	private final JdbcTemplate jdbcTemplate;

	public MasterReportRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private void appendCondition(StringBuilder whereClause, String condition) {
		if (whereClause.length() > 0) {
			whereClause.append(" AND ");
		}
		whereClause.append(condition);
	}

	public List<EmployeeMasterDetailsDTO> getMasterDetails(MasterDetailsRequest request) {
		Map<String, EmployeeMasterDetailsDTO> employeeMap = new HashMap<>();
		List<Object> params = new ArrayList<>();
		StringBuilder whereClause = new StringBuilder();

		// buName condition
		if (request.getLocation() != null && !request.getLocation().isEmpty()) {
			//appendCondition(whereClause, "bu.callname = ? ");
			//params.add(request.getLocation());
			
			
			 if ("AZISTA".equalsIgnoreCase(request.getLocation())) {
			        appendCondition(whereClause, "bu.callname = 'HYD' and bu.code='AZISTA' AND bu.businessunitid NOT IN (44) ");
			    } 
			    else if ("AZISTA-BST".equalsIgnoreCase(request.getLocation())) {
			        appendCondition(whereClause, "bu.callname = 'HYD' AND bu.businessunitid IN (44) ");
			    } 
			    else if ("AZISTA-PROMOTERS".equalsIgnoreCase(request.getLocation())) {
			        appendCondition(whereClause, "bu.callname = 'TEP' AND bu.businessunitid IN (25) ");
			    } 
			    else {
			        appendCondition(whereClause, "bu.callname = ? ");
			        params.add(request.getLocation());
			    }
		}

		// bu (company IDs) condition
		if (request.getBu() != null && !request.getBu().isEmpty()) {
			String placeholders = request.getBu().stream().map(s -> "?").collect(Collectors.joining(","));
			appendCondition(whereClause, "A.companyid IN (" + placeholders + ")");
			params.addAll(request.getBu());
		}

		// status condition
		if (request.getStatus() != null && !request.getStatus().isEmpty()) {
			String placeholders = request.getStatus().stream().map(s -> "?").collect(Collectors.joining(","));
			appendCondition(whereClause, "A.status IN (" + placeholders + ")");
			params.addAll(request.getStatus());
		} else {
			appendCondition(whereClause, "A.status NOT IN ('1091','1082') ");
		}

		// Final where clause
		String finalWhereClause = whereClause.length() > 0 ? " WHERE " + whereClause : "";

		// 1. Basic Details Query
		String basicDetailsQuery = "SELECT A.EMPLOYEESEQUENCENO 'Emp ID', A.CALLNAME 'NAME', STATUS.NAME 'STATUS', "
				+ "co.NAME 'COSTCENTER', GEN.NAME 'GENDER', EMPLO.NAME 'EMPLOYMENT TYPE', "
				+ "IFNULL(STROKE.NAME,'--') 'INCREMENT TYPE', "
				+ "DATE_FORMAT(IFNULL(IF(A.STATUS in(1001,1092,1401),'0000-00-00',HR.LASTDATE),'0000-00-00'),'%d-%m-%Y') 'LWD', "
				+ "DATE_FORMAT(A.DATEOFBIRTH,'%d-%m-%Y') 'DOB', IFNULL(DATE_FORMAT(PROFILE.DATEOFJOIN,'%d-%m-%Y'),'') DOJ, "
				+ "BU.NAME 'DIVISION', IFNULL(DES.NAME,'') 'DESIGNATION', IFNULL(DEP.NAME,'') 'DEPARTMENT', "
				+ "IFNULL(MIS.MIS_DEPARTMENT,'') 'MIS_DEPARTMENT', "
				+ "CONCAT(MN.EMPLOYEESEQUENCENO,'-',MN.CALLNAME) 'Reportee', MS.NAME 'Reportee_Status' "
				+ "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A "
				+ "LEFT JOIN HCLADM_PROD.TBL_STATUS_CODES STATUS ON A.STATUS=STATUS.STATUS "
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID=BU.BUSINESSUNITID "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_profile PROFILE ON A.EMPLOYEEID=PROFILE.EMPLOYEEID "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON A.EMPLOYEEID=DD.EMPLOYEEID "
				+ "LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID=DEP.DEPARTMENTID "
				+ "LEFT JOIN HCLADM_PROD.TBL_DESIGNATION DES ON DD.DESIGNATIONID=DES.DESIGNATIONID "
				+ "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY MN ON DD.MANAGERID=MN.EMPLOYEEID "
				+ "LEFT JOIN hclhrm_prod.tbl_status_codes MS ON MS.STATUS=MN.STATUS "
				+ "LEFT JOIN (SELECT EMPLOYEEID, MAX(LASTWORKINGDATE) LASTDATE FROM HCLHRM_PROD.TBL_EMPLOYEE_HRACTIONS GROUP BY EMPLOYEEID) HR ON A.EMPLOYEEID=HR.EMPLOYEEID "
				+ "LEFT JOIN HCLHRM_PROD.tbl_employment_types EMPLO ON A.EMPLOYMENTTYPEID=EMPLO.EMPLOYMENTTYPEID "
				+ "LEFT JOIN HCLADM_PROD.tbl_increment_type STROKE ON DD.INCREMENTTYPEID=STROKE.INCREMENTTYPEID "
				+ "LEFT JOIN HCLADM_PROD.tbl_gender GEN ON A.GENDER=GEN.GENDER "
				+ "LEFT JOIN test.tbl_mis_department_mapping MIS ON A.EMPLOYEESEQUENCENO = MIS.EMPLOYEEID "
				+ "LEFT JOIN hcladm_prod.tbl_costcenter co ON co.COSTCENTERID=a.COSTCENTERID " + " " + finalWhereClause
				+ " " + "GROUP BY a.employeeid order by a.employeesequenceno ";

		jdbcTemplate.query(basicDetailsQuery, params.toArray(), rs -> {
			String empId = rs.getString("Emp ID");
			EmployeeMasterDetailsDTO dto = employeeMap.computeIfAbsent(empId, k -> new EmployeeMasterDetailsDTO());
			dto.setEmpId(empId);
			dto.setName(rs.getString("NAME"));
			dto.setStatus(rs.getString("STATUS"));
			dto.setCostCenter(rs.getString("COSTCENTER"));
			dto.setGender(rs.getString("GENDER"));
			dto.setEmploymentType(rs.getString("EMPLOYMENT TYPE"));
			dto.setIncrementType(rs.getString("INCREMENT TYPE"));
			dto.setLastWorkingDay(rs.getString("LWD"));
			dto.setDob(rs.getString("DOB"));
			dto.setDoj(rs.getString("DOJ"));
			dto.setDivision(rs.getString("DIVISION"));
			dto.setDesignation(rs.getString("DESIGNATION"));
			dto.setDepartment(rs.getString("DEPARTMENT"));
			dto.setMisDepartment(rs.getString("MIS_DEPARTMENT")); 
			dto.setReportee(rs.getString("Reportee"));
			dto.setReporteeStatus(rs.getString("Reportee_Status"));
			
			//System.out.println(rs.getString("MIS_DEPARTMENT"));
		});

		// 2. Bank & Contact Details
		String bankQuery = "SELECT A.EMPLOYEESEQUENCENO, IFNULL(BANK.BANKNAME, '') 'BANKNAME', IFNULL(BIFSC.BANKIFC,'') 'IFSC', "
				+ "IFNULL(OTHER.ACCOUNTNO,'') 'ACCOUNTNO', IFNULL(PRO.EMAIL,'') 'PROEMAIL', IFNULL(D.EMAIL,'') 'PEMAIL', "
				+ "IFNULL(OTHER.PFNO, '') PFNO, IFNULL(OTHER.ESINO, '') ESINO, IFNULL(OTHER.PFUAN, '') PFUAN, "
				+ "IFNULL(EMPST.STATE,'') STATE, IFNULL(HQLOC.NAME,'') HQ, IFNULL(RLOC.NAME,'') REGION, "
				+ "IFNULL(PRO.MOBILE,'') 'PROFESSIONAL_MOBILE', IFNULL(D.MOBILE,'') 'PERSONAL_MOBILE', "
				+ "IFNULL(D.MOBILE,'') 'PERSONAL PHONE', IFNULL(D.COMMUNICATIONADDRESS,'') COMMUNICATIONADDRESS "
				+ "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_personal_contact D ON A.EMPLOYEEID=D.EMPLOYEEID "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON A.EMPLOYEEID=DD.EMPLOYEEID "
				+ "LEFT JOIN test.empl_states EMPST ON A.EMPLOYEEID=EMPST.EMPLOYEEID "
				+ "LEFT JOIN HCLLCM_PROD.TBL_LOCATION RLOC ON DD.SUBLOCATIONID=RLOC.LOCATIONID "
				+ "LEFT JOIN HCLLCM_PROD.TBL_LOCATION HQLOC ON DD.WORKLOCATIONID=HQLOC.LOCATIONID "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_other_detail OTHER ON A.EMPLOYEEID=OTHER.EMPLOYEEID "
				+ "LEFT JOIN hclhrm_prod_others.tbl_emp_bank_ifc BIFSC ON A.EMPLOYEEID=BIFSC.EMPID "
				+ "LEFT JOIN hcladm_prod.tbl_bank_details BANK ON OTHER.BANKID=BANK.BANKID "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_contact PRO ON A.EMPLOYEEID=PRO.EMPLOYEEID "
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID=BU.BUSINESSUNITID " + " " + finalWhereClause
				+ " " + "GROUP BY a.employeeid";

		jdbcTemplate.query(bankQuery, params.toArray(), rs -> {
			String empId = rs.getString("EMPLOYEESEQUENCENO");
			EmployeeMasterDetailsDTO dto = employeeMap.computeIfAbsent(empId, k -> new EmployeeMasterDetailsDTO());
			dto.setBankName(rs.getString("BANKNAME"));
			dto.setIfsc(rs.getString("IFSC"));
			dto.setAccountNo(rs.getString("ACCOUNTNO"));
			dto.setProEmail(rs.getString("PROEMAIL"));
			dto.setpEmail(rs.getString("PEMAIL"));
			dto.setPfNo(rs.getString("PFNO"));
			dto.setEsiNo(rs.getString("ESINO"));
			dto.setPfUan(rs.getString("PFUAN"));
			dto.setState(rs.getString("STATE"));
			dto.setHq(rs.getString("HQ"));
			dto.setRegion(rs.getString("REGION"));
			dto.setProfessionalMobile(rs.getString("PROFESSIONAL_MOBILE"));
			dto.setPersonalMobile(rs.getString("PERSONAL_MOBILE"));
			dto.setPersonalPhone(rs.getString("PERSONAL PHONE"));
			dto.setCommunicationAddress(rs.getString("COMMUNICATIONADDRESS"));
		});

		// 3. Address & Identity Details
		String addressQuery = "SELECT " + "A.EMPLOYEESEQUENCENO, "
				+ "IFNULL(D.COMMUNICATIONADDRESS2, '') AS COMMUNICATIONADDRESS2, "
				+ "IFNULL(D.COMMUNICATIONADDRESS3, '') AS COMMUNICATIONADDRESS3, "
				+ "IFNULL(D.COMMUNICATIONADDRESS4, '') AS COMMUNICATIONADDRESS4, "
				+ "IFNULL(COMMLOC.NAME, '') AS COMMCITY, " + "IFNULL(COMMSTATELOC.NAME, '') AS COMMSTATE, "
				+ "IFNULL(D.COMMUNICATIONZIP, '') AS COMM_ZIP, "
				+ "IFNULL(D.PERMANENTADDRESS, '') AS PERMANENTADDRESS, "
				+ "IFNULL(D.PERMANENTADDRESS2, '') AS PERMANENTADDRESS2, "
				+ "IFNULL(D.PERMANENTADDRESS3, '') AS PERMANENTADDRESS3, "
				+ "IFNULL(D.PERMANENTADDRESS4, '') AS PERMANENTADDRESS4, " + "IFNULL(PLOC.NAME, '') AS PCITY, "
				+ "IFNULL(PSLOC.NAME, '') AS PSTATE, " + "IFNULL(D.PERMANENTZIP, '') AS PZIPCODE, "
				+ "IFNULL(INFO.PASSPORTNO, '') AS PASSPORTNO, " + "IFNULL(INFO.AADHAARCARDNO, '') AS AADHAARCARDNO, "
				+ "IFNULL(INFO.AADHAARUID, '') AS AADHAARUID, " + "IFNULL(INFO.AADHAARNAME, '') AS AADHAARNAME, "
				+ "IFNULL(INFO.PAN, '') AS PAN " + "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_personal_contact D ON A.EMPLOYEEID = D.EMPLOYEEID "
				+ "LEFT JOIN HCLLCM_PROD.TBL_LOCATION PLOC ON D.PERMANENTLOCATIONID = PLOC.LOCATIONID "
				+ "LEFT JOIN HCLLCM_PROD.TBL_LOCATION PSLOC ON PLOC.PARENT = PSLOC.LOCATIONID "
				+ "LEFT JOIN HCLLCM_PROD.TBL_LOCATION COMMLOC ON D.COMMUNICATIONLOCATIONID = COMMLOC.LOCATIONID "
				+ "LEFT JOIN HCLLCM_PROD.TBL_LOCATION COMMSTATELOC ON COMMLOC.PARENT = COMMSTATELOC.LOCATIONID "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_personalinfo INFO ON A.EMPLOYEEID = INFO.EMPLOYEEID "
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID = BU.BUSINESSUNITID " + " "
				+ finalWhereClause + " " + "GROUP BY A.employeeid";

		jdbcTemplate.query(addressQuery, params.toArray(), rs -> {
			String empId = rs.getString("EMPLOYEESEQUENCENO");
			EmployeeMasterDetailsDTO dto = employeeMap.computeIfAbsent(empId, k -> new EmployeeMasterDetailsDTO());
			dto.setCommunicationAddress2(rs.getString("COMMUNICATIONADDRESS2"));
			dto.setCommunicationAddress3(rs.getString("COMMUNICATIONADDRESS3"));
			dto.setCommunicationAddress4(rs.getString("COMMUNICATIONADDRESS4"));
			dto.setCommCity(rs.getString("COMMCITY"));
			dto.setCommState(rs.getString("COMMSTATE"));
			dto.setCommZip(rs.getString("COMM_ZIP"));
			dto.setPermanentAddress(rs.getString("PERMANENTADDRESS"));
			dto.setPermanentAddress2(rs.getString("PERMANENTADDRESS2"));
			dto.setPermanentAddress3(rs.getString("PERMANENTADDRESS3"));
			dto.setPermanentAddress4(rs.getString("PERMANENTADDRESS4"));
			dto.setpCity(rs.getString("PCITY"));
			dto.setpState(rs.getString("PSTATE"));
			dto.setpZipCode(rs.getString("PZIPCODE"));
			dto.setPassportNo(rs.getString("PASSPORTNO"));
			dto.setAadhaarCardNo(rs.getString("AADHAARCARDNO"));
			dto.setAadhaarUid(rs.getString("AADHAARUID"));
			dto.setAadhaarName(rs.getString("AADHAARNAME"));
			dto.setPan(rs.getString("PAN"));
			// dto.setPrevExp(rs.getString("Prev.Exp"));
			// dto.setCurExp(rs.getString("Cur.Exp"));
		});
		String eDetails = "SELECT " + "A.EMPLOYEESEQUENCENO, " + "A.EMPLOYEEID, "
				+ "IFNULL(IMPRS.assetpropertyvalue, '0.00') AS IMPRESTAMT, "
				+ "EDU.QUALIFICATION_DETAILS AS `Education Details` " + "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A "
				+ "LEFT JOIN ( " + "    SELECT employeeid, MAX(assetpropertyvalue) AS assetpropertyvalue "
				+ "    FROM hclhrm_prod.tbl_employee_assets " + "    WHERE assetid = 16 AND status = 1001 "
				+ "    GROUP BY employeeid " + ") IMPRS ON IMPRS.employeeid = A.EMPLOYEEID "
				+ "LEFT JOIN HCLHRM_PROD.employee_education_summary EDU " + "    ON A.EMPLOYEEID = EDU.EMPLOYEEID "
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID = BU.BUSINESSUNITID " + finalWhereClause
				+ " GROUP BY A.EMPLOYEEID";

		jdbcTemplate.query(eDetails, params.toArray(), rs -> {
			String empId = rs.getString("EMPLOYEESEQUENCENO");
			EmployeeMasterDetailsDTO dto = employeeMap.computeIfAbsent(empId, k -> new EmployeeMasterDetailsDTO());
			dto.setImprestAmt(rs.getString("IMPRESTAMT"));
			dto.setEducationDetails(rs.getString("Education Details"));
		});

		String experienceQuery = "SELECT\r\n" + "    a.employeesequenceno,\r\n"
				+ "    IFNULL(b.`Prev.Exp`, 0) AS pre_exp,\r\n" + "    IFNULL(b.`Cur.Exp`, 0) AS cur_exp\r\n"
				+ "FROM hclhrm_prod.tbl_employee_primary a\r\n"
				+ "join HCLHRM_PROD.employee_experience_summary b on b.`Emp ID` = a.employeesequenceno\r\n"
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON a.COMPANYID = BU.BUSINESSUNITID  " + finalWhereClause
				+ " GROUP BY a.EMPLOYEEID";
		jdbcTemplate.query(experienceQuery, params.toArray(), rs -> {
			String empId = rs.getString("employeesequenceno");
			EmployeeMasterDetailsDTO dto = employeeMap.computeIfAbsent(empId, k -> new EmployeeMasterDetailsDTO());
			dto.setPrevExp(rs.getString("pre_exp"));
			dto.setCurExp(rs.getString("cur_exp"));
		});

		String ctcQuery = "SELECT A.EMPLOYEESEQUENCENO, CTC.EFFECTIVEDATE, "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 22, CTCD.COMPONENTVALUE, 0)), '0') AS 'GROSS', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 24, CTCD.COMPONENTVALUE, 0)), '0') AS 'BASIC', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 132, CTCD.COMPONENTVALUE, 0)), '0') AS 'VDA', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 25, CTCD.COMPONENTVALUE, 0)), '0') AS 'HRA', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 26, CTCD.COMPONENTVALUE, 0)), '0') AS 'CA', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 27, CTCD.COMPONENTVALUE, 0)), '0') AS 'MEDICAL', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 28, CTCD.COMPONENTVALUE, 0)), '0') AS 'EDUCATION', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 29, CTCD.COMPONENTVALUE, 0)), '0') AS 'SPL ALLOW', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 18, CTCD.COMPONENTVALUE, 0)), '0') AS 'TRAVEL ALLOWANCE', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 70, CTCD.COMPONENTVALUE, 0)), '0') AS 'KIT ALLOWANCE-E', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 97, CTCD.COMPONENTVALUE, 0)), '0') AS 'LTA-E', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 124, CTCD.COMPONENTVALUE, 0)), '0') AS 'Other ALLOWANCE-E', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 98, CTCD.COMPONENTVALUE, 0)), '0') AS 'BONUS-E', "
				+ "(IFNULL(MAX(IF(CTCD.COMPONENTID = 24, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 25, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 26, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 27, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 28, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 29, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 18, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 70, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 97, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 98, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 124, CTCD.COMPONENTVALUE, 0)), 0)) AS 'E Gross', "
				+ "IFNULL(EMPST.STATE, '') AS 'PTSTATE', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 30, CTCD.COMPONENTVALUE, 0)), '0') AS 'PT-D', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 31, CTCD.COMPONENTVALUE, 0)), '0') AS 'PF-D', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 32, CTCD.COMPONENTVALUE, 0)), '0') AS 'ESI-D', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 33, CTCD.COMPONENTVALUE, 0)), '0') AS 'LTA-ANNUAL BENEFITS', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 34, CTCD.COMPONENTVALUE, 0)), '0') AS 'PF-ANNUAL BENEFITS', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 35, CTCD.COMPONENTVALUE, 0)), '0') AS 'ESI-ANNUAL BENEFITS', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 36, CTCD.COMPONENTVALUE, 0)), '0') AS 'BONUS-ANNUAL BENEFITS', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 62, CTCD.COMPONENTVALUE, 0)), '0') AS 'GRATUITY-ANNUAL BENEFITS', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 39, CTCD.COMPONENTVALUE, 0)), '0') AS 'ANNUAL BONUS-ANNUAL BENEFITS', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 63, CTCD.COMPONENTVALUE, 0)), '0') AS 'RETENTION BONUS', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 93, CTCD.COMPONENTVALUE, 0)), '0') AS 'MEDICAL PREMIUM-ANNUAL BENEFITS', "
				+ "IFNULL(MAX(IF(XX.COMPONENTID = 9001, XY.COMPONENTVALUE, 0)), '0') AS 'FUELANDMAINTENANCE', "
				+ "IFNULL(MAX(IF(XX.COMPONENTID = 9002, XY.COMPONENTVALUE, 0)), '0') AS 'OTHERCOMPONENTS', "
				+ "IFNULL(MAX(IF(XX.COMPONENTID = 9003, XY.COMPONENTVALUE, 0)), '0') AS 'MOBILE', "
				+ "IFNULL(MAX(IF(XX.COMPONENTID = 9004, XY.COMPONENTVALUE, 0)), '0') AS 'INTERNET', "
				+ "IFNULL(MAX(IF(XX.COMPONENTID = 9005, XY.COMPONENTVALUE, 0)), '0') AS 'HOUSERENT', "
				+ "IFNULL(MAX(IF(XX.COMPONENTID = 9006, XY.COMPONENTVALUE, 0)), '0') AS 'DRIVERSALARY', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 129, CTCD.COMPONENTVALUE, 0)), '0') AS 'VARIABLE PAY', "
				+ "IFNULL(MAX(IF(CTCD.COMPONENTID = 130, CTCD.COMPONENTVALUE, 0)), '0') AS 'Performance Linked Bonus (KRA)', "
				+ "(" + " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 22 THEN CTCD.COMPONENTVALUE * 12 END), 0) + "
				+ " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 97 THEN CTCD.COMPONENTVALUE * 12 END), 0) + "
				+ " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 98 THEN CTCD.COMPONENTVALUE * 12 END), 0) + "
				+ " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 124 THEN CTCD.COMPONENTVALUE * 12 END), 0) + "
				+ " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 34 THEN CTCD.COMPONENTVALUE END), 0) + "
				+ " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 35 THEN CTCD.COMPONENTVALUE END), 0) + "
				+ " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 36 THEN CTCD.COMPONENTVALUE END), 0) + "
				+ " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 33 THEN CTCD.COMPONENTVALUE END), 0) + "
				+ " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 39 THEN CTCD.COMPONENTVALUE END), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 63, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 62 THEN CTCD.COMPONENTVALUE END), 0) + "
				+ " IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 93 THEN CTCD.COMPONENTVALUE END), 0) + "
				+ " IFNULL(MAX(IF(XX.COMPONENTID = 9001, XY.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(XX.COMPONENTID = 9002, XY.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(XX.COMPONENTID = 9003, XY.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(XX.COMPONENTID = 9004, XY.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(XX.COMPONENTID = 9005, XY.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(XX.COMPONENTID = 9006, XY.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 129, CTCD.COMPONENTVALUE, 0)), 0) + "
				+ " IFNULL(MAX(IF(CTCD.COMPONENTID = 130, CTCD.COMPONENTVALUE, 0)), 0)) AS 'TOTAL_CTC' "
				+ "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A "
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID = BU.BUSINESSUNITID "
				+ "LEFT JOIN test.empl_states EMPST ON A.EMPLOYEEID = EMPST.EMPLOYEEID " + "LEFT JOIN ( "
				+ "SELECT EMPLOYEEID ctcempid, MAX(CTCTRANSACTIONID) Maxid, MAX(EFFECTIVEDATE) EFFECTIVEDATE "
				+ "FROM HCLHRM_PROD.TBL_EMPLOYEE_CTC GROUP BY EMPLOYEEID " + ") CTC ON CTC.ctcempid = A.EMPLOYEEID "
				+ "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_CTC_DETAILS CTCD ON CTCD.CTCTRANSACTIONID = CTC.Maxid "
				+ "LEFT JOIN test.fuel_maintenance XY ON XY.CTCTRANSACTIONID = CTC.Maxid AND XY.employeeid = A.EMPLOYEEID "
				+ "LEFT JOIN test.tbl_new_components XX ON XX.COMPONENTID = XY.COMPONENTID " + " " + finalWhereClause
				+ " " + "GROUP BY A.EMPLOYEEID, CTC.EFFECTIVEDATE";

		jdbcTemplate.query(ctcQuery, params.toArray(), rs -> {
			String empId = rs.getString("EMPLOYEESEQUENCENO");
			EmployeeMasterDetailsDTO dto = employeeMap.computeIfAbsent(empId, k -> new EmployeeMasterDetailsDTO());

			dto.setEffectiveDate(rs.getString("EFFECTIVEDATE"));
			dto.setGross(rs.getString("GROSS"));
			dto.setBasic(rs.getString("BASIC"));
			dto.setVda(rs.getString("VDA"));
			dto.setHra(rs.getString("HRA"));
			dto.setCa(rs.getString("CA"));
			dto.setMedical(rs.getString("MEDICAL"));
			dto.setEducation(rs.getString("EDUCATION"));
			dto.setSplAllow(rs.getString("SPL ALLOW"));
			dto.setTravelAllowance(rs.getString("TRAVEL ALLOWANCE"));
			dto.setKitAllowance(rs.getString("KIT ALLOWANCE-E"));
			dto.setLta(rs.getString("LTA-E"));
			dto.setOtherAllowance(rs.getString("Other ALLOWANCE-E"));
			dto.setBonus(rs.getString("BONUS-E"));
			dto.seteGross(rs.getString("E Gross"));
			dto.setPtState(rs.getString("PTSTATE"));
			dto.setPtD(rs.getString("PT-D"));
			dto.setPfD(rs.getString("PF-D"));
			dto.setEsiD(rs.getString("ESI-D"));
			dto.setLtaAnnualBenefits(rs.getString("LTA-ANNUAL BENEFITS"));
			dto.setPfAnnualBenefits(rs.getString("PF-ANNUAL BENEFITS"));
			dto.setEsiAnnualBenefits(rs.getString("ESI-ANNUAL BENEFITS"));
			dto.setBonusAnnualBenefits(rs.getString("BONUS-ANNUAL BENEFITS"));
			dto.setGratuityAnnualBenefits(rs.getString("GRATUITY-ANNUAL BENEFITS"));
			dto.setAnnualBonus(rs.getString("ANNUAL BONUS-ANNUAL BENEFITS"));
			dto.setRetentionBonus(rs.getString("RETENTION BONUS"));
			dto.setMedicalPremium(rs.getString("MEDICAL PREMIUM-ANNUAL BENEFITS"));
			dto.setFuelAndMaintenance(rs.getString("FUELANDMAINTENANCE"));
			dto.setOtherComponents(rs.getString("OTHERCOMPONENTS"));
			dto.setMobile(rs.getString("MOBILE"));
			dto.setInternet(rs.getString("INTERNET"));
			dto.setHouseRent(rs.getString("HOUSERENT"));
			dto.setDriverSalary(rs.getString("DRIVERSALARY"));
			dto.setVariablePay(rs.getString("VARIABLE PAY"));
			dto.setPerformanceLinkedBonus(rs.getString("Performance Linked Bonus (KRA)"));
			dto.setCtc(rs.getString("TOTAL_CTC"));
		});
		List<EmployeeMasterDetailsDTO> sortedList = employeeMap.values().stream()
				.sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getEmpId()))).collect(Collectors.toList());
		System.err.println("Sorted List Size: " + sortedList.size());
		return employeeMap.values().stream().sorted(Comparator.comparing(EmployeeMasterDetailsDTO::getEmpId))
				.collect(Collectors.toList());
	}

	public List<Master> getStatusCodes() {
		String sql = "SELECT status, name FROM HCLADM_PROD.TBL_STATUS_CODES";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Master(rs.getInt("status"), rs.getString("name")));
	}

}

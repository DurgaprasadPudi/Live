package com.hetero.heteroiconnect.masterreports;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MasterReportRepository {

	private final JdbcTemplate jdbcTemplate;

	public MasterReportRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<EmployeeMasterDetailsDTO> getMasterDetails(String buName) {
		Map<String, EmployeeMasterDetailsDTO> employeeMap = new HashMap<>();

		// 1. Basic Details Query
		String basicDetailsQuery = "SELECT A.EMPLOYEESEQUENCENO 'Emp ID', A.CALLNAME 'NAME', STATUS.NAME 'STATUS', "
				+ "co.NAME 'COSTCENTER', GEN.NAME 'GENDER', EMPLO.NAME 'EMPLOYMENT TYPE', "
				+ "IFNULL(STROKE.NAME,'--') 'INCREMENT TYPE', "
				+ "DATE_FORMAT(IFNULL(IF(A.STATUS in(1001,1092,1401),'0000-00-00',HR.LASTDATE),'0000-00-00'),'%d-%m-%Y') 'LWD', "
				+ "DATE_FORMAT(A.DATEOFBIRTH,'%d-%m-%Y') 'DOB', IFNULL(DATE_FORMAT(PROFILE.DATEOFJOIN,'%d-%m-%Y'),'') DOJ, "
				+ "BU.NAME 'DIVISION', IFNULL(DES.NAME,'') 'DESIGNATION', IFNULL(DEP.NAME,'') 'DEPARTMENT', "
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
				+ "LEFT JOIN hcladm_prod.tbl_costcenter co ON co.COSTCENTERID=a.COSTCENTERID "
				+ "WHERE bu.callname in (?) GROUP BY a.employeeid order by a.employeesequenceno ";

		jdbcTemplate.query(basicDetailsQuery, new Object[] { buName }, rs -> {
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
			dto.setReportee(rs.getString("Reportee"));
			dto.setReporteeStatus(rs.getString("Reportee_Status"));
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
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID=BU.BUSINESSUNITID "
				+ "WHERE bu.callname in (?) GROUP BY a.employeeid";

		jdbcTemplate.query(bankQuery, new Object[] { buName }, rs -> {
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
		String addressQuery = "SELECT A.EMPLOYEESEQUENCENO, IFNULL(D.COMMUNICATIONADDRESS2,'') COMMUNICATIONADDRESS2, "
				+ "IFNULL(D.COMMUNICATIONADDRESS3,'') COMMUNICATIONADDRESS3, IFNULL(D.COMMUNICATIONADDRESS4,'') COMMUNICATIONADDRESS4, "
				+ "IFNULL(COMMLOC.NAME,'') COMMCITY, IFNULL(COMMSTATELOC.NAME,'') COMMSTATE, IFNULL(D.COMMUNICATIONZIP,'') COMM_ZIP, "
				+ "IFNULL(D.PERMANENTADDRESS,'') PERMANENTADDRESS, IFNULL(D.PERMANENTADDRESS2,'') PERMANENTADDRESS2, "
				+ "IFNULL(D.PERMANENTADDRESS3,'') PERMANENTADDRESS3, IFNULL(D.PERMANENTADDRESS4,'') PERMANENTADDRESS4, "
				+ "IFNULL(PLOC.NAME,'') PCITY, IFNULL(PSLOC.NAME,'') PSTATE, IFNULL(D.PERMANENTZIP,'') PZIPCODE, "
				+ "IFNULL(INFO.PASSPORTNO,'') 'PASSPORTNO', IFNULL(INFO.AADHAARCARDNO,'') 'AADHAARCARDNO', "
				+ "IFNULL(INFO.AADHAARUID,'') 'AADHAARUID' " + "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_personal_contact D ON A.EMPLOYEEID=D.EMPLOYEEID "
				+ "LEFT JOIN HCLLCM_PROD.TBL_LOCATION PLOC ON D.PERMANENTLOCATIONID=PLOC.LOCATIONID "
				+ "LEFT JOIN HCLLCM_PROD.TBL_LOCATION PSLOC ON PLOC.PARENT=PSLOC.LOCATIONID "
				+ "LEFT JOIN HCLLCM_PROD.TBL_LOCATION COMMLOC ON D.COMMUNICATIONLOCATIONID=COMMLOC.LOCATIONID "
				+ "LEFT JOIN HCLLCM_PROD.TBL_LOCATION COMMSTATELOC ON COMMLOC.PARENT=COMMSTATELOC.LOCATIONID "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_personalinfo INFO ON A.EMPLOYEEID=INFO.EMPLOYEEID "
				+ "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID=BU.BUSINESSUNITID "
				+ "WHERE bu.callname in (?) GROUP BY a.employeeid";

		jdbcTemplate.query(addressQuery, new Object[] { buName }, rs -> {
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
		});

		List<EmployeeMasterDetailsDTO> sortedList = employeeMap.values().stream()
				.sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getEmpId()))).collect(Collectors.toList());

		System.err.println("Sorted List Size: " + sortedList.size());

		return employeeMap.values().stream().sorted(Comparator.comparing(EmployeeMasterDetailsDTO::getEmpId))
				.collect(Collectors.toList());

	}
}

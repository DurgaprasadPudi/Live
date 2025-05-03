
package com.hetero.heteroiconnect.requisition.forms;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RequisitionFormRepository {

	private final JdbcTemplate jdbcTemplate;

	public RequisitionFormRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String insertManpowerForm(ManpowerRequisitionFormDTO dto) {
		String sql = "INSERT INTO requisitionform.tbl_manpower_requisition_form "
				+ "(company_id, department, location, experience_required, job_title, qualification, "
				+ "req_job_description, skills, dept_head_sign, dept_head_name, dept_head_date, "
				+ "unit_head_sign, unit_head_name, unit_head_date, hr_comments, status, created_date_time, lupdate) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1001, now(), now())";

		int rows = jdbcTemplate.update(sql, dto.getCompanyId(), dto.getDepartment(), dto.getLocation(),
				dto.getExperienceRequired(), dto.getJobTitle(), dto.getQualification(), dto.getReqJobDescription(),
				dto.getSkills(), dto.getDeptHeadSign(), dto.getDeptHeadName(), dto.getDeptHeadDate(),
				dto.getUnitHeadSign(), dto.getUnitHeadName(), dto.getUnitHeadDate(), dto.getHrComments());

		if (rows > 0) {
			int lastReqId = getLastInsertedReqId();
			insertReplacedEmpDetails(dto, lastReqId);
			return "Manpower requisition form inserted successfully.";
		} else {
			return "Failed to insert manpower requisition form.";
		}
	}

	private int getLastInsertedReqId() {
		String sql = "SELECT LAST_INSERT_ID()";
		Integer lastReqId = jdbcTemplate.queryForObject(sql, Integer.class);
		return lastReqId != null ? lastReqId : 0;
	}

	private void insertReplacedEmpDetails(ManpowerRequisitionFormDTO dto, int lastReqId) {
		String sql = "INSERT INTO requisitionform.tbl_replaced_emp_details "
				+ "(employee_id, req_id, qualification, date_of_resignation, designation, experience, date_of_relieving, status, created_date_time, lupdate) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, 1001, now(), now())";
		jdbcTemplate.update(sql, dto.getEmployeeId(), lastReqId, dto.getReplacedQualification(),
				dto.getDateOfResignation(), dto.getDesignation(), dto.getExperience(), dto.getDateOfRelieving());
	}

	public EmployeeDetailsDTO getEmployeeDetails(int employeeId) {
		try {
			String sql = "SELECT\r\n" + "\r\n" + "    A.EMPLOYEESEQUENCENO 'Emp ID',\r\n" + "\r\n"
					+ "    A.CALLNAME 'NAME',\r\n" + "\r\n" + "    STATUS.NAME 'STATUS',\r\n" + "\r\n"
					+ "    co.NAME 'COSTCENTER',\r\n" + "\r\n" + "    GEN.NAME 'GENDER',\r\n" + "\r\n"
					+ "    EMPLO.NAME 'EMPLOYMENT TYPE', \r\n" + "\r\n"
					+ "    IFNULL(STROKE.NAME,'--') 'INCREMENT TYPE',\r\n" + "\r\n"
					+ "    DATE_FORMAT(IFNULL(IF(A.STATUS IN(1001,1092,1401),'0000-00-00',HR.LASTDATE),'0000-00-00'),'%Y-%m-%d') 'LWD',\r\n"
					+ "\r\n" + "    DATE_FORMAT(A.DATEOFBIRTH,'%d-%m-%Y') 'DOB',\r\n" + "\r\n"
					+ "    IFNULL(DATE_FORMAT(PROFILE.DATEOFRESIGNATION,'%Y-%m-%d'),'0000-00-00') 'DOR',\r\n" + "\r\n"
					+ "    IFNULL(DATE_FORMAT(PROFILE.DATEOFJOIN,'%d-%m-%Y'),'') 'DOJ',\r\n" + "\r\n"
					+ "    BU.NAME 'DIVISION',\r\n" + "\r\n" + "    IFNULL(DES.NAME,'') 'DESIGNATION',\r\n" + "\r\n"
					+ "    IFNULL(DEP.NAME,'') 'DEPARTMENT', \r\n" + "\r\n"
					+ "    CONCAT(MN.EMPLOYEESEQUENCENO,'-',MN.CALLNAME) 'Reportee',\r\n" + "\r\n"
					+ "    MS.NAME 'Reportee_Status', \r\n" + "\r\n" + "    IFNULL(BANK.BANKNAME,'') 'BANKNAME',\r\n"
					+ "\r\n" + "    IFNULL(BIFSC.BANKIFC,'') 'IFSC', \r\n" + "\r\n"
					+ "    IFNULL(OTHER.ACCOUNTNO,'') 'ACCOUNTNO',\r\n" + " \r\n"
					+ "    IFNULL(OTHER.PFNO,'') 'PFNO',\r\n" + "\r\n" + "    IFNULL(OTHER.ESINO,'') 'ESINO',\r\n"
					+ "\r\n" + "    IFNULL(OTHER.PFUAN,'') 'PFUAN',\r\n" + "\r\n"
					+ "    IFNULL(EMPST.STATE,'') 'STATE',\r\n" + "\r\n" + "    IFNULL(HQLOC.NAME,'') 'HQ',\r\n"
					+ "\r\n" + "    IFNULL(RLOC.NAME,'') 'REGION',\r\n" + " \r\n"
					+ "    IFNULL(D.MOBILE,'') 'PERSONAL_MOBILE',\r\n" + "\r\n"
					+ "    IFNULL(D.MOBILE,'') 'PERSONAL PHONE', \r\n" + "\r\n"
					+ "    IFNULL(D.COMMUNICATIONADDRESS,'') 'COMMUNICATIONADDRESS',\r\n" + "\r\n"
					+ "    IFNULL(D.COMMUNICATIONADDRESS2,'') 'COMMUNICATIONADDRESS2', \r\n" + "\r\n"
					+ "    IFNULL(D.COMMUNICATIONADDRESS3,'') 'COMMUNICATIONADDRESS3', \r\n" + "\r\n"
					+ "    IFNULL(D.COMMUNICATIONADDRESS4,'') 'COMMUNICATIONADDRESS4', \r\n" + "\r\n"
					+ "    IFNULL(COMMLOC.NAME,'') 'COMMCITY',\r\n" + "\r\n"
					+ "    IFNULL(COMMSTATELOC.NAME,'') 'COMMSTATE', \r\n" + "\r\n"
					+ "    IFNULL(D.COMMUNICATIONZIP,'') 'COMM_ZIP', \r\n" + "\r\n"
					+ "    IFNULL(D.PERMANENTADDRESS,'') 'PERMANENTADDRESS', \r\n" + "\r\n"
					+ "    IFNULL(D.PERMANENTADDRESS2,'') 'PERMANENTADDRESS2', \r\n" + "\r\n"
					+ "    IFNULL(D.PERMANENTADDRESS3,'') 'PERMANENTADDRESS3', \r\n" + "\r\n"
					+ "    IFNULL(D.PERMANENTADDRESS4,'') 'PERMANENTADDRESS4', \r\n" + "\r\n"
					+ "    IFNULL(PLOC.NAME,'') 'PCITY', \r\n" + "\r\n" + "    IFNULL(PSLOC.NAME,'') 'PSTATE',\r\n"
					+ "\r\n" + "    IFNULL(D.PERMANENTZIP,'') 'PZIPCODE',\r\n" + "\r\n"
					+ "    IFNULL(INFO.PASSPORTNO,'') 'PASSPORTNO', \r\n" + "\r\n"
					+ "    IFNULL(INFO.AADHAARCARDNO,'') 'AADHAARCARDNO', \r\n" + "\r\n"
					+ "    IFNULL(INFO.AADHAARUID,'') 'AADHAARUID', \r\n" + "\r\n"
					+ "    IFNULL(INFO.AADHAARNAME,'') 'AADHAARNAME', \r\n" + "\r\n"
					+ "    IFNULL(INFO.PAN,'') 'PAN', \r\n" + "\r\n"
					+ "    IFNULL(IMPRS.assetpropertyvalue,'0.00') 'IMPRESTAMT', \r\n" + "\r\n"
					+ "    QUALI.QUALIFICATION 'Education_Details',\r\n" + "\r\n"
					+ "    IFNULL(PREV.PREV,'0') 'Prev.Exp',\r\n" + "\r\n" + "    CONCAT(\r\n" + "\r\n"
					+ "        SUBSTRING_INDEX(\r\n" + "\r\n" + "            TIMESTAMPDIFF(\r\n" + "\r\n"
					+ "                MONTH, \r\n" + "\r\n" + "                DATEOFJOIN, \r\n" + "\r\n"
					+ "                IF(\r\n" + "\r\n"
					+ "                    HR.LASTDATE IS NULL OR HR.LASTDATE = '0000-00-00', \r\n" + "\r\n"
					+ "                    NOW(), \r\n" + "\r\n"
					+ "                    IF(A.STATUS IN(1001,1092,1401),NOW(),HR.LASTDATE)\r\n" + "\r\n"
					+ "                )\r\n" + "\r\n" + "            ) / 12,\r\n" + "\r\n" + "            '.', \r\n"
					+ "\r\n" + "            1\r\n" + "\r\n" + "        ), \r\n" + "\r\n" + "        '.', \r\n" + "\r\n"
					+ "        ROUND(\r\n" + "\r\n" + "            (\r\n" + "\r\n" + "                CONCAT(\r\n"
					+ "\r\n" + "                    '0.',\r\n" + "\r\n" + "                    SUBSTRING_INDEX(\r\n"
					+ "\r\n" + "                        TIMESTAMPDIFF(\r\n" + "\r\n"
					+ "                            MONTH, \r\n" + "\r\n" + "                            DATEOFJOIN,\r\n"
					+ "\r\n" + "                            IF(\r\n" + "\r\n"
					+ "                                HR.LASTDATE IS NULL OR HR.LASTDATE = '0000-00-00', \r\n" + "\r\n"
					+ "                                NOW(), \r\n" + "\r\n"
					+ "                                IF(A.STATUS IN(1001,1092,1401),NOW(),HR.LASTDATE)\r\n" + "\r\n"
					+ "                            )\r\n" + "\r\n" + "                        ) / 12, \r\n" + "\r\n"
					+ "                        '.', \r\n" + "\r\n" + "                        -1\r\n" + "\r\n"
					+ "                    )\r\n" + "\r\n" + "                )\r\n" + "\r\n"
					+ "            ) * 12, \r\n" + "\r\n" + "            0\r\n" + "\r\n" + "        )\r\n" + "\r\n"
					+ "    ) 'Cur.Exp'\r\n" + "\r\n" + "FROM \r\n" + "\r\n"
					+ "    HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A \r\n" + "\r\n"
					+ "    LEFT JOIN HCLADM_PROD.TBL_STATUS_CODES STATUS \r\n" + "\r\n"
					+ "        ON A.STATUS = STATUS.STATUS \r\n" + "\r\n"
					+ "    LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU \r\n" + "\r\n"
					+ "        ON A.COMPANYID = BU.BUSINESSUNITID \r\n" + "\r\n"
					+ "    LEFT JOIN hclhrm_prod.tbl_employee_personal_contact D \r\n" + "\r\n"
					+ "        ON A.EMPLOYEEID = D.EMPLOYEEID \r\n" + "\r\n"
					+ "    LEFT JOIN HCLLCM_PROD.TBL_LOCATION PLOC \r\n" + "\r\n"
					+ "        ON D.PERMANENTLOCATIONID = PLOC.LOCATIONID\r\n" + "\r\n"
					+ "    LEFT JOIN HCLLCM_PROD.TBL_LOCATION PSLOC \r\n" + "\r\n"
					+ "        ON PLOC.PARENT = PSLOC.LOCATIONID \r\n" + "\r\n"
					+ "    LEFT JOIN HCLLCM_PROD.TBL_LOCATION COMMLOC \r\n" + "\r\n"
					+ "        ON D.COMMUNICATIONLOCATIONID = COMMLOC.LOCATIONID \r\n" + "\r\n"
					+ "    LEFT JOIN HCLLCM_PROD.TBL_LOCATION COMMSTATELOC \r\n" + "\r\n"
					+ "        ON COMMLOC.PARENT = COMMSTATELOC.LOCATIONID \r\n" + "\r\n"
					+ "    LEFT JOIN hclhrm_prod.tbl_employee_profile PROFILE \r\n" + "\r\n"
					+ "        ON A.EMPLOYEEID = PROFILE.EMPLOYEEID \r\n" + "\r\n"
					+ "    LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD \r\n" + "\r\n"
					+ "        ON A.EMPLOYEEID = DD.EMPLOYEEID \r\n" + "\r\n"
					+ "    LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP \r\n" + "\r\n"
					+ "        ON DD.DEPARTMENTID = DEP.DEPARTMENTID \r\n" + "\r\n"
					+ "    LEFT JOIN HCLADM_PROD.TBL_DESIGNATION DES \r\n" + "\r\n"
					+ "        ON DD.DESIGNATIONID = DES.DESIGNATIONID \r\n" + "\r\n"
					+ "    LEFT JOIN test.empl_states EMPST \r\n" + "\r\n"
					+ "        ON A.EMPLOYEEID = EMPST.EMPLOYEEID \r\n" + "\r\n"
					+ "    LEFT JOIN HCLLCM_PROD.TBL_LOCATION RLOC \r\n" + "\r\n"
					+ "        ON DD.SUBLOCATIONID = RLOC.LOCATIONID \r\n" + "\r\n"
					+ "    LEFT JOIN HCLLCM_PROD.TBL_LOCATION HQLOC \r\n" + "\r\n"
					+ "        ON DD.WORKLOCATIONID = HQLOC.LOCATIONID \r\n" + "\r\n"
					+ "    LEFT JOIN hclhrm_prod.tbl_employee_other_detail OTHER \r\n" + "\r\n"
					+ "        ON A.EMPLOYEEID = OTHER.EMPLOYEEID \r\n" + "\r\n"
					+ "    LEFT JOIN hclhrm_prod_others.tbl_emp_bank_ifc BIFSC \r\n" + "\r\n"
					+ "        ON A.EMPLOYEEID = BIFSC.EMPID \r\n" + "\r\n"
					+ "    LEFT JOIN hcladm_prod.tbl_bank_details BANK \r\n" + "\r\n"
					+ "        ON OTHER.BANKID = BANK.BANKID \r\n" + "\r\n"
					+ "    LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY MN \r\n" + "\r\n"
					+ "        ON DD.MANAGERID = MN.EMPLOYEEID \r\n" + "\r\n"
					+ "    LEFT JOIN hclhrm_prod.tbl_status_codes MS \r\n" + "\r\n"
					+ "        ON MS.STATUS = MN.STATUS \r\n" + "\r\n"
					+ "    LEFT JOIN hclhrm_prod.tbl_employee_personalinfo INFO \r\n" + "\r\n"
					+ "        ON A.EMPLOYEEID = INFO.EMPLOYEEID \r\n" + "\r\n" + "    LEFT JOIN ( \r\n" + "\r\n"
					+ "        SELECT \r\n" + "\r\n" + "            EMPLOYEEID,\r\n" + "\r\n"
					+ "            MAX(LASTWORKINGDATE) LASTDATE \r\n" + "\r\n" + "        FROM \r\n" + "\r\n"
					+ "            HCLHRM_PROD.TBL_EMPLOYEE_HRACTIONS \r\n" + "\r\n" + "        GROUP BY \r\n" + "\r\n"
					+ "            EMPLOYEEID \r\n" + "\r\n" + "    ) HR \r\n" + "\r\n"
					+ "        ON A.EMPLOYEEID = HR.EMPLOYEEID \r\n" + "\r\n" + "    LEFT JOIN ( \r\n" + "\r\n"
					+ "        SELECT \r\n" + "\r\n" + "            assetpropertyvalue,\r\n" + "\r\n"
					+ "            issueddate,\r\n" + "\r\n" + "            returneddate,\r\n" + "\r\n"
					+ "            employeeid \r\n" + "\r\n" + "        FROM \r\n" + "\r\n"
					+ "            hclhrm_prod.tbl_employee_assets \r\n" + "\r\n" + "        WHERE \r\n" + "\r\n"
					+ "            assetid = 16 \r\n" + "\r\n" + "            AND status = 1001 \r\n" + "\r\n"
					+ "        GROUP BY \r\n" + "\r\n" + "            employeeid \r\n" + "\r\n" + "    ) IMPRS \r\n"
					+ "\r\n" + "        ON IMPRS.employeeid = A.employeeid \r\n" + "\r\n" + "    LEFT JOIN ( \r\n"
					+ "\r\n" + "        SELECT \r\n" + "\r\n" + "            EMPLOYEEID,\r\n" + "\r\n"
					+ "            GROUP_CONCAT(QUALIFICATIONID,'-',YEAROFPASSING) QUALIFICATION \r\n" + "\r\n"
					+ "        FROM \r\n" + "\r\n" + "            hclhrm_prod.tbl_employee_education_details \r\n"
					+ "\r\n" + "        GROUP BY \r\n" + "\r\n" + "            EMPLOYEEID \r\n" + "\r\n"
					+ "    ) QUALI \r\n" + "\r\n" + "        ON A.EMPLOYEEID = QUALI.EMPLOYEEID \r\n" + "\r\n"
					+ "    LEFT JOIN (\r\n" + "\r\n" + "        SELECT \r\n" + "\r\n" + "            EMPLOYEEID,\r\n"
					+ "\r\n" + "            CONCAT(\r\n" + "\r\n" + "                SUBSTRING_INDEX(\r\n" + "\r\n"
					+ "                    TIMESTAMPDIFF(MONTH, DATEOFJOIN, NOW()+1)/12,\r\n" + "\r\n"
					+ "                    '.',\r\n" + "\r\n" + "                    1\r\n" + "\r\n"
					+ "                ),\r\n" + "\r\n" + "                '.',\r\n" + "\r\n"
					+ "                ROUND(\r\n" + "\r\n" + "                    (\r\n" + "\r\n"
					+ "                        CONCAT(\r\n" + "\r\n" + "                            '0.',\r\n" + "\r\n"
					+ "                            SUBSTRING_INDEX(\r\n" + "\r\n"
					+ "                                TIMESTAMPDIFF(MONTH, DATEOFJOIN, NOW()+1)/12,\r\n" + "\r\n"
					+ "                                '.',\r\n" + "\r\n" + "                                -1\r\n"
					+ "\r\n" + "                            )\r\n" + "\r\n" + "                        )\r\n" + "\r\n"
					+ "                    )*12,\r\n" + "\r\n" + "                    0\r\n" + "\r\n"
					+ "                )\r\n" + "\r\n" + "            ) 'Exp' \r\n" + "\r\n" + "        FROM \r\n"
					+ "\r\n" + "            HCLHRM_PROD.TBL_EMPLOYEE_PROFILE\r\n" + "\r\n" + "        GROUP BY \r\n"
					+ "\r\n" + "            EMPLOYEEID\r\n" + "\r\n" + "    ) CUREXP \r\n" + "\r\n"
					+ "        ON A.EMPLOYEEID = CUREXP.EMPLOYEEID\r\n" + "\r\n" + "    LEFT JOIN ( \r\n" + "\r\n"
					+ "        SELECT \r\n" + "\r\n" + "            EMPLOYEEID,\r\n" + "\r\n"
					+ "            CONCAT(\r\n" + "\r\n"
					+ "                SUBSTRING_INDEX(SUM(EXPERIENCE)/12, '.',1),\r\n" + "\r\n"
					+ "                '.',\r\n" + "\r\n" + "                ROUND(\r\n" + "\r\n"
					+ "                    (\r\n" + "\r\n" + "                        CONCAT(\r\n" + "\r\n"
					+ "                            '0.',\r\n" + "\r\n"
					+ "                            SUBSTRING_INDEX(SUM(EXPERIENCE)/12, '.',-1)\r\n" + "\r\n"
					+ "                        )\r\n" + "\r\n" + "                    )*12,\r\n" + "\r\n"
					+ "                    0\r\n" + "\r\n" + "                )\r\n" + "\r\n"
					+ "            ) PREV,\r\n" + "\r\n" + "            SUM(EXPERIENCE) TOTALPREV \r\n" + "\r\n"
					+ "        FROM \r\n" + "\r\n" + "            hclhrm_prod.tbl_employee_experience_details \r\n"
					+ "\r\n" + "        GROUP BY \r\n" + "\r\n" + "            EMPLOYEEID \r\n" + "\r\n"
					+ "    ) PREV \r\n" + "\r\n" + "        ON A.EMPLOYEEID = PREV.EMPLOYEEID \r\n" + "\r\n"
					+ "    LEFT JOIN HCLHRM_PROD.tbl_employment_types EMPLO \r\n" + "\r\n"
					+ "        ON A.EMPLOYMENTTYPEID = EMPLO.EMPLOYMENTTYPEID\r\n" + "\r\n"
					+ "    LEFT JOIN HCLADM_PROD.tbl_increment_type STROKE \r\n" + "\r\n"
					+ "        ON DD.INCREMENTTYPEID = STROKE.INCREMENTTYPEID\r\n" + "\r\n"
					+ "    LEFT JOIN HCLADM_PROD.tbl_gender GEN\r\n" + "\r\n" + "        ON A.GENDER = GEN.GENDER\r\n"
					+ "\r\n" + "    LEFT JOIN hcladm_prod.tbl_costcenter co\r\n" + "\r\n"
					+ "        ON co.COSTCENTERID = A.COSTCENTERID\r\n" + "\r\n" + "WHERE\r\n" + "\r\n"
					+ "    A.EMPLOYEESEQUENCENO=?\r\n" + "\r\n" + "GROUP BY\r\n" + "\r\n" + "    A.employeeid\r\n"
					+ "\r\n" + "ORDER BY\r\n" + "\r\n" + "    A.EMPLOYEESEQUENCENO";

			return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
				double prevExp = rs.getObject("Prev.Exp") != null ? rs.getDouble("Prev.Exp") : 0.0;
				double curExp = rs.getObject("Cur.Exp") != null ? rs.getDouble("Cur.Exp") : 0.0;
				double totalExp = prevExp + curExp;
				BigDecimal roundedTotalExp = new BigDecimal(totalExp).setScale(1, RoundingMode.HALF_UP);
				String totalExpStr = roundedTotalExp.toString();
				return new EmployeeDetailsDTO(rs.getString("Emp ID"), rs.getString("NAME"),
						rs.getString("Education_Details"), rs.getString("DOR"), rs.getString("DESIGNATION"),
						totalExpStr, rs.getString("LWD"));
			}, employeeId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<MasterData> getBu(String name) {
		String sql = "select businessunitid, name, code from hcladm_prod.tbl_businessunit where code=?";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			return new MasterData(rs.getInt("businessunitid"), rs.getString("name"));
		}, name);
	}

	public List<MasterData> getTitle() {
		String sql = "select designationid,IFNULL(NULLIF(name,''),'NA') as name from HCLADM_PROD.TBL_DESIGNATION where status=1001 order by designationid";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			return new MasterData(rs.getInt("designationid"), rs.getString("name"));
		});
	}

	public List<MasterData> getDepartment() {
		String sql = "SELECT PARENTDEPARTMENTID AS departmentid, name " + "FROM hcladm_prod.tbl_department "
				+ "WHERE parentdepartmentid != 0 AND status = 1001 " + "ORDER BY PARENTDEPARTMENTID ASC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			return new MasterData(rs.getInt("departmentid"), rs.getString("name"));
		});
	}

	public int insertCompanyBasicDetails(CompanyBasicDetailsDTO dto) {
		String sql = "INSERT INTO requisitionform.tbl_company_basic_details (hod_id, department, location, effective_date, status) "
				+ "VALUES (?, ?, ?, ?, 1001)";
		jdbcTemplate.update(sql, Integer.parseInt(dto.getHodId()), dto.getDepartment(), dto.getLocation(),
				dto.getEffectiveDate());
		return getLastInsertedReqId();
	}

	public void insertEmployeeStrengthAndVacancies(
			List<EmployeeStrengthAndVacanciesDTO> employeeStrengthAndVacanciesDTO, int companyId) {
		String sql = "INSERT INTO requisitionform.employee_strength_and_vacancies (id, designation, present_strength, approved_vacancies, vacancy_a, vacancy_b, vacancy_c, vacancy_d, remarks, status, created_date_time) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1001, now())";
		employeeStrengthAndVacanciesDTO.forEach(empDTO -> jdbcTemplate.update(sql, companyId, empDTO.getDesignation(),
				empDTO.getPresentStrength(), empDTO.getApprovedVacancies(), empDTO.getVacancyA(), empDTO.getVacancyB(),
				empDTO.getVacancyC(), empDTO.getVacancyD(), empDTO.getRemarks()));
	}

	public List<MasterData> getDependentName(String name) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT employeesequenceno, callname ").append("FROM hclhrm_prod.tbl_employee_primary ")
				.append("WHERE employeeid NOT IN (0, 1) ").append("AND status IN (1001, 1061, 1092, 1401) ")
				.append("AND (callname LIKE ? OR employeesequenceno LIKE ?)");
		return jdbcTemplate.query(query.toString(), (rs, rowNum) -> {
			return new MasterData(rs.getInt("employeesequenceno"), rs.getString("callname"));
		}, "%" + name + "%", "%" + name + "%");
	}
}

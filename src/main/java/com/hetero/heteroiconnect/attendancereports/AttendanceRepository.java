package com.hetero.heteroiconnect.attendancereports;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AttendanceRepository {
	private JdbcTemplate jdbcTemplate;
	private static final String status = " A.status in(1001,1092,1401) ";

	public AttendanceRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<AttendanceLocationPojo> getLocations(int empId) {
		StringBuilder locationsQuery = new StringBuilder();
		locationsQuery.append(" SELECT location_id,location_name ");
		locationsQuery.append(" FROM test.tbl_attendance_locations a ");
		locationsQuery.append(" JOIN test.tbl_attendance_locations_access b ON a.location_id=b.locid ");
		locationsQuery.append(" WHERE b.empid=? and b.status=1001 and a.status=1001 ");

		return jdbcTemplate.query(locationsQuery.toString(), (rs, rowNum) -> {
			AttendanceLocationPojo locations = new AttendanceLocationPojo();
			locations.setId(rs.getInt("location_id"));
			locations.setName(rs.getString("location_name"));
			return locations;
		}, empId);
	}

	public List<Map<String, Object>> getData(AttendanceFilterPojo attendanceFilterPojo) {
		if (attendanceFilterPojo.getLocation() != null) {
			return fetchLocationData(attendanceFilterPojo);
		} else if (attendanceFilterPojo.getBu() != null && !attendanceFilterPojo.getBu().isEmpty()) {
			return fetchBuData(attendanceFilterPojo);
		} else if (attendanceFilterPojo.getEmpId() != null) {
			return fetchEmployeeData(attendanceFilterPojo);
		} else {
			throw new AttendanceDataFetchingException();
		}
	}

	public List<Map<String, Object>> fetchLocationData(AttendanceFilterPojo attendanceFilterPojo) {
		return getLocationAttendance(attendanceFilterPojo);
	}

	public List<Map<String, Object>> fetchBuData(AttendanceFilterPojo attendanceFilterPojo) {
		return getBUAttendace(attendanceFilterPojo);
	}

	public List<Map<String, Object>> fetchEmployeeData(AttendanceFilterPojo attendanceFilterPojo) {
		return getEmployeeAttendace(attendanceFilterPojo);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLocationAttendance(AttendanceFilterPojo attendanceFilterPojo) {
		StringBuilder attendanceQuery = new StringBuilder();
		LocalDate start = null;
		LocalDate end = null;
		List<String> dates = null;

		Map<String, Object> attendanceParams = getAttendanceDates(attendanceFilterPojo);

		start = (LocalDate) attendanceParams.get("start");
		end = (LocalDate) attendanceParams.get("end");
		dates = (List<String>) attendanceParams.get("dates");

		String assam1 = " where A.companyid in(15,16) ";
		String assam2 = " where A.companyid in(33,34) ";
		String hyd = " where BU.callname in('HYD') and A.companyid not in(15,16,33,34,17,42) and A.employeesequenceno not in (20206,10423) ";
		String mumbai = " where BU.callname in('MUM') and G.NAME='OFFICE' and A.employeesequenceno not in(10160,10179,10182,10302,101194,106957,201550,204936) ";
		String rd = " where BU.callname in('HYD') and A.companyid in(10) and A.employeesequenceno not in (20206,10423) ";
		String fmcg = " where BU.callname in('HYD') and A.companyid in(28) and dep.departmentid in (58,62) ";
		String composites = " where BU.callname in('HYD') and A.companyid in(42) ";
		String ahmedabad = " where A.companyid in(17) ";
		String moosapet = " where BU.callname in('HYD') and A.companyid in(24) and A.employeesequenceno not in (20206,10423) ";
		String bst = " where  A.companyid in(44) ";

		String locFilter = null;
		switch (attendanceFilterPojo.getLocation()) {
		case 1:
			locFilter = assam1;
			break;
		case 2:
			locFilter = assam2;
			break;
		case 3:
			locFilter = hyd;
			break;
		case 4:
			locFilter = fmcg;
			break;
		case 5:
			locFilter = composites;
			break;
		case 6:
			locFilter = rd;
			break;
		case 7:
			locFilter = moosapet;
			break;
		case 8:
			locFilter = ahmedabad;
			break;
		case 9:
			locFilter = mumbai;
			break;
		case 10:
			locFilter = bst;
			break;
		default:
			throw new AttendanceDataFetchingException();
		}

		addDynamicDatesToQuery(attendanceQuery, dates);
		addDynamicStatusCompanyEmpId(attendanceQuery, start, end, locFilter, false);

		return jdbcTemplate.queryForList(attendanceQuery.toString());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBUAttendace(AttendanceFilterPojo attendanceFilterPojo) {
		StringBuilder attendanceQuery = new StringBuilder();
		LocalDate start = null;
		LocalDate end = null;
		List<String> dates = null;

		Map<String, Object> attendanceParams = getAttendanceDates(attendanceFilterPojo);

		start = (LocalDate) attendanceParams.get("start");
		end = (LocalDate) attendanceParams.get("end");
		dates = (List<String>) attendanceParams.get("dates");

		String buFilter = " where A.companyid in("
				+ attendanceFilterPojo.getBu().stream().map(String::valueOf).collect(Collectors.joining(",")) + ") ";

		addDynamicDatesToQuery(attendanceQuery, dates);
		addDynamicStatusCompanyEmpId(attendanceQuery, start, end, buFilter, false);
		return jdbcTemplate.queryForList(attendanceQuery.toString());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEmployeeAttendace(AttendanceFilterPojo attendanceFilterPojo) {
		StringBuilder attendanceQuery = new StringBuilder();
		LocalDate start = null;
		LocalDate end = null;
		List<String> dates = null;

		Map<String, Object> attendanceParams = getAttendanceDates(attendanceFilterPojo);

		start = (LocalDate) attendanceParams.get("start");
		end = (LocalDate) attendanceParams.get("end");
		dates = (List<String>) attendanceParams.get("dates");

		String empFilter = " where A.employeesequenceno in (" + attendanceFilterPojo.getEmpId() + ") ";

		addDynamicDatesToQuery(attendanceQuery, dates);
		addDynamicStatusCompanyEmpId(attendanceQuery, start, end, empFilter, true);

		return jdbcTemplate.queryForList(attendanceQuery.toString());
	}

	public Map<String, Object> generateDates(AttendanceFilterPojo attendanceFilterPojo) {
		StringBuilder payPeriodDatesQuery = new StringBuilder();
		payPeriodDatesQuery.append(" SELECT ");
		payPeriodDatesQuery.append("    TRANSACTIONDURATION AS PAYPERIOD, ");
		payPeriodDatesQuery.append("    IF( ");
		payPeriodDatesQuery.append("        transactionduration < DATE_FORMAT(NOW(), '%Y%m'), ");
		payPeriodDatesQuery.append(
				"        MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01')), ");
		payPeriodDatesQuery.append("        IF( ");
		payPeriodDatesQuery
				.append("            SUM(IF(transactionduration = DATE_FORMAT(NOW(), '%Y%m'), '1', '0')) = 1, ");
		payPeriodDatesQuery.append(
				"            MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01')), ");
		payPeriodDatesQuery.append(
				"            MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01')) ");
		payPeriodDatesQuery.append("        ) ");
		payPeriodDatesQuery.append("    ) AS PAYPERIODMONTHNAME, ");
		payPeriodDatesQuery.append("    CONVERT(LEFT(TRANSACTIONDURATION, 4), CHAR) AS PAYPERIODYEAR, ");
		payPeriodDatesQuery.append("    FROMDATE AS PAYPERIODFROMDATE, ");
		payPeriodDatesQuery.append("    DATE_ADD( ");
		payPeriodDatesQuery.append("        FROMDATE, ");
		payPeriodDatesQuery.append("        INTERVAL DAY(LAST_DAY(DATE_ADD(FROMDATE, INTERVAL 1 MONTH)) - 1) DAY ");
		payPeriodDatesQuery.append("    ) AS PAYPERIODTODATE, ");
		payPeriodDatesQuery.append("    IF( ");
		payPeriodDatesQuery.append("        transactionduration < DATE_FORMAT(NOW(), '%Y%m'), ");
		payPeriodDatesQuery.append(
				"        MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01')), ");
		payPeriodDatesQuery.append("        IF( ");
		payPeriodDatesQuery
				.append("            SUM(IF(transactionduration = DATE_FORMAT(NOW(), '%Y%m'), '1', '0')) = 1, ");
		payPeriodDatesQuery.append(
				"            MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01')), ");
		payPeriodDatesQuery.append(
				"            MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01')) ");
		payPeriodDatesQuery.append("        ) ");
		payPeriodDatesQuery.append("    ) AS MONTHNAME, ");
		payPeriodDatesQuery.append("    CONVERT(LEFT(TRANSACTIONDURATION, 4), CHAR) AS YEAR, ");
		payPeriodDatesQuery.append(
				"    CONVERT(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01'), CHAR) AS FROMDATE, ");
		payPeriodDatesQuery.append(
				"    LAST_DAY(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01')) AS TODATE ");
		payPeriodDatesQuery.append("FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES ");
		payPeriodDatesQuery.append("WHERE transactiontypeid = 1 ");
		payPeriodDatesQuery.append(" AND businessunitid=? ");
		payPeriodDatesQuery.append(" AND transactionduration =? ");
		payPeriodDatesQuery.append(" GROUP BY TRANSACTIONDURATION ");
		payPeriodDatesQuery.append(" ORDER BY transactionduration DESC ");
		payPeriodDatesQuery.append(" LIMIT 12 ");

		LocalDate start = null;
		LocalDate end = null;
		int businessUnitId;

		if (attendanceFilterPojo.getIsDataBetween()) {
			start = attendanceFilterPojo.getFromDate();
			end = attendanceFilterPojo.getToDate();
		} else {
			if (attendanceFilterPojo.getIsPayPeriod()) {
				if (attendanceFilterPojo.getLocation() != null) {
					businessUnitId = (attendanceFilterPojo.getLocation() == 9) ? 23 : 11;
				} else if (attendanceFilterPojo.getBu() != null && !attendanceFilterPojo.getBu().isEmpty()) {
					businessUnitId = (attendanceFilterPojo.getCallName().equalsIgnoreCase("HYD")
							|| attendanceFilterPojo.getCallName().equalsIgnoreCase("ASSAM")) ? 11 : 23;
				} else if (attendanceFilterPojo.getEmpId() != null) {
					String userBuQuery = "SELECT companyid FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY WHERE EMPLOYEESEQUENCENO=?";
					businessUnitId = jdbcTemplate.queryForObject(userBuQuery, Integer.class,
							attendanceFilterPojo.getEmpId());
				} else {
					throw new AttendanceDataFetchingException("Insufficient data to determine Business Unit");
				}

				Map<String, Object> payrollDates = jdbcTemplate.queryForMap(payPeriodDatesQuery.toString(),
						businessUnitId,
						attendanceFilterPojo.getYear() + String.format("%02d", attendanceFilterPojo.getMonth()));

				if (businessUnitId == 23) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					start = LocalDate.parse((String) payrollDates.get("FROMDATE"), formatter);
					//end = LocalDate.parse((String) payrollDates.get("TODATE"), formatter);
					//start = ((Date) payrollDates.get("FROMDATE")).toLocalDate();
					 end = ((Date) payrollDates.get("TODATE")).toLocalDate();
				} else {
					start = ((Date) payrollDates.get("PAYPERIODFROMDATE")).toLocalDate();
					end = ((Date) payrollDates.get("PAYPERIODTODATE")).toLocalDate();
				}
			} else {
				YearMonth yearMonth = YearMonth.of(attendanceFilterPojo.getYear(), attendanceFilterPojo.getMonth());
				start = yearMonth.atDay(1);
				end = yearMonth.atEndOfMonth();
			}
		}
		Map<String, Object> dates = new HashMap<>();
		dates.put("startDate", start);
		dates.put("endDate", end);
		return dates;
	}

	private Map<String, Object> getAttendanceDates(AttendanceFilterPojo attendanceFilterPojo) {
		Map<String, Object> fromtoDates = generateDates(attendanceFilterPojo);

		LocalDate start = (LocalDate) fromtoDates.get("startDate");
		LocalDate end = (LocalDate) fromtoDates.get("endDate");

		List<String> dates = new ArrayList<>();
		LocalDate startDate = start;
		LocalDate endDate = end;
		while (!startDate.isAfter(endDate)) {
			dates.add(startDate.toString());
			startDate = startDate.plusDays(1);
		}

		LocalDate today = LocalDate.now();
		List<String> filteredDates = dates.stream()
				.filter(date -> LocalDate.parse(date).isBefore(today) || LocalDate.parse(date).isEqual(today))
				.collect(Collectors.toList());

		Map<String, Object> attendanceParams = new HashMap<>();
		attendanceParams.put("start", start);
		attendanceParams.put("end", end);
		attendanceParams.put("dates", filteredDates);
		return attendanceParams;
	}

	private void addDynamicDatesToQuery(StringBuilder attendanceQuery, List<String> dates) {
		attendanceQuery.append(
				" select A.employeesequenceno AS ID ,A.callname AS NAME,ST.NAME AS STATUS,DATE_FORMAT(HH.DATEOFJOIN,'%d-%m-%Y') AS DOJ, BU.name AS BU , IFNULL(DEP.CODE,'') AS DEPT,IFNULL(DES.NAME,'') 'DESIGNATION',G.NAME AS COST_CENTER, ");
		for (int i = 0; i < dates.size(); i++) {
			String ds = dates.get(i);

			attendanceQuery.append(" IFNULL(MAX( ");
			attendanceQuery.append(" CASE ");
			attendanceQuery.append(" WHEN b.DAYTYPE='HL' AND  b.dateon = '" + ds
					+ "' and DATE_FORMAT(IFNULL(d.leaveon, '0000-00-00'), '%Y-%m-%d')!='" + ds + ""
					+ "' THEN concat(b.ATT_IN ,' || ', b.ATT_OUT,'||',b.NET_HOURS,'||','HL') ");

			attendanceQuery.append(" WHEN b.DAYTYPE='HL' AND  b.dateon = '" + ds
					+ "' and DATE_FORMAT(IFNULL(d.leaveon, '0000-00-00'), '%Y-%m-%d')='" + ds + ""
					+ "' THEN concat(b.ATT_IN ,' || ', b.ATT_OUT,'||',b.NET_HOURS,'||', ");
			attendanceQuery.append(" if(leave_count='1',concat(LEAVE_TYPE,'/',MANAGER_STATUS), ");
			attendanceQuery.append(
					" if(half_day='false',concat(b.ATT_IN ,'||', b.ATT_OUT,'||',b.NET_HOURS,'||',LEAVE_TYPE,'(','1st Half',')/',MANAGER_STATUS), ");
			attendanceQuery.append(
					" concat(b.ATT_IN ,'||', b.ATT_OUT,'||',b.NET_HOURS,'||',LEAVE_TYPE,'(',d.half_day,')/',MANAGER_STATUS)))) ");

			attendanceQuery.append(" WHEN b.DAYTYPE='WOFF' AND  b.dateon = '" + ds
					+ "' and DATE_FORMAT(IFNULL(d.leaveon, '0000-00-00'), '%Y-%m-%d')!='" + ds + ""
					+ "' THEN concat(b.ATT_IN ,'||', b.ATT_OUT,'||',b.NET_HOURS,'||','WOFF') ");

			attendanceQuery.append(" WHEN b.DAYTYPE='WOFF' AND  b.dateon = '" + ds
					+ "' and DATE_FORMAT(IFNULL(d.leaveon, '0000-00-00'), '%Y-%m-%d')='" + ds + ""
					+ "' THEN concat(b.ATT_IN ,' || ', b.ATT_OUT,'||',b.NET_HOURS,'||', ");
			attendanceQuery.append(" if(leave_count='1',concat(LEAVE_TYPE,'/',MANAGER_STATUS), ");
			attendanceQuery.append(
					" if(half_day='false',concat(b.ATT_IN ,'||', b.ATT_OUT,'||',b.NET_HOURS,'||',LEAVE_TYPE,'(','1st Half',')/',MANAGER_STATUS), ");
			attendanceQuery.append(
					" concat(b.ATT_IN ,'||', b.ATT_OUT,'||',b.NET_HOURS,'||',LEAVE_TYPE,'(',d.half_day,')/',MANAGER_STATUS)))) ");

			attendanceQuery.append(" WHEN b.DAY_STATUS='P' AND  b.dateon = '" + ds + "' AND");
			attendanceQuery.append(" DATE_FORMAT(IFNULL(d.leaveon, '0000-00-00'), '%Y-%m-%d')!='" + ds + "' and");
			attendanceQuery.append(" DATE_FORMAT(IFNULL(c.REQ_DATE, '0000-00-00'), '%Y-%m-%d')!='" + ds + "' ");
			attendanceQuery.append(" THEN concat(b.ATT_IN ,'||', b.ATT_OUT,'||',b.NET_HOURS,'||--') ");

			attendanceQuery.append(" WHEN b.DAY_STATUS='P' AND  b.dateon = '" + ds + "' AND ");
			attendanceQuery.append(" DATE_FORMAT(IFNULL(c.REQ_DATE, '0000-00-00'), '%Y-%m-%d')=b.dateon ");
			attendanceQuery.append(" THEN concat(b.ATT_IN ,'||', b.ATT_OUT,'||',b.NET_HOURS,'|| AR') ");

			attendanceQuery.append(" WHEN DATE_FORMAT(IFNULL(d.leaveon, '0000-00-00'), '%Y-%m-%d')='" + ds
					+ "' AND b.dateon = '" + ds + " ' and D.leave_count='0.5' ");
			attendanceQuery.append(
					" THEN if(half_day='false',concat(b.ATT_IN ,'||', b.ATT_OUT,'||',b.NET_HOURS,'||',LEAVE_TYPE,'(','1st Half',')/',MANAGER_STATUS), ");
			attendanceQuery.append(
					" concat(b.ATT_IN ,'||', b.ATT_OUT,'||',b.NET_HOURS,'||',LEAVE_TYPE,'(',d.half_day,')/',MANAGER_STATUS)) ");

			attendanceQuery.append(" WHEN DATE_FORMAT(IFNULL(d.leaveon, '0000-00-00'), '%Y-%m-%d')='" + ds
					+ "' AND b.dateon = '" + ds + "' and D.leave_count='1' ");
			attendanceQuery.append(
					" THEN concat(b.ATT_IN ,'||', b.ATT_OUT,'||',b.NET_HOURS,'||(',LEAVE_TYPE,'/',MANAGER_STATUS,')' )");

			attendanceQuery
					.append(" WHEN b.ATT_IN='00:00:00' AND b.ATT_OUT='00:00:00' AND b.DAY_STATUS='A' AND b.dateon ='"
							+ ds + "' ");
			attendanceQuery.append(" THEN 'A' ");
			attendanceQuery.append(" WHEN DATE_FORMAT(HH.DATEOFJOIN,'%Y-%m-%d')> '" + ds + "' THEN '--' ");
			attendanceQuery.append(" END),'A') AS '" + ds + "'");

			if (i != dates.size() - 1) {
				attendanceQuery.append(",");
			}
		}
	}

	private void addDynamicStatusCompanyEmpId(StringBuilder attendanceQuery, LocalDate start, LocalDate end,
			String filter, boolean isForEmployee) {
		attendanceQuery.append(" FROM hclhrm_prod.tbl_employee_primary A ");
		attendanceQuery.append(
				" LEFT JOIN test_mum.tbl_att_leave_in_out_status_report B ON B.employeeid = A.employeesequenceno ");

		attendanceQuery.append(" LEFT JOIN( ");
		attendanceQuery.append(" select employeeid,REQ_DATE from hclhrm_prod_others.tbl_emp_attn_req ");
		attendanceQuery.append(" where REQ_DATE BETWEEN '" + start + "' AND '" + end + "' ");
		attendanceQuery.append(" and REQ_TYPE='AR' and employeeid in ( ");
		attendanceQuery.append(" select employeesequenceno from hclhrm_prod.tbl_employee_primary A");
		attendanceQuery.append(" LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID=BU.BUSINESSUNITID ");
		attendanceQuery
				.append(" LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON A.EMPLOYEEID=DD.EMPLOYEEID ");
		attendanceQuery.append(" LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID=DEP.DEPARTMENTID ");
		attendanceQuery.append(
				" LEFT JOIN HCLADM_PROD.TBL_COSTCENTER G ON A.COSTCENTERID=G.COSTCENTERID AND A.companyid=g.businessunitid ");
		attendanceQuery.append(filter);
		if (!isForEmployee) {
			attendanceQuery.append(" AND " + status);
		}
		attendanceQuery.append(" Order by A.employeesequenceno asc ");
		attendanceQuery.append(" )and flag!='R' ) C ");

		attendanceQuery.append(" on c.employeeid=A.employeesequenceno and c.REQ_DATE=b.dateon ");

		attendanceQuery.append(" LEFT JOIN( ");
		attendanceQuery.append(
				" select EMPLOYEEID,LEAVEON,leave_count,HALF_DAY,LEAVE_TYPE,MANAGER_STATUS from hclhrm_prod_others.tbl_emp_leave_report ");
		attendanceQuery.append(" where LEAVEON  BETWEEN '" + start + "' AND '" + end + "' ");
		attendanceQuery.append(" and MANAGER_STATUS in ('A','P') ");
		attendanceQuery.append(" and EMPLOYEEID in ( ");
		attendanceQuery.append(" select employeesequenceno from hclhrm_prod.tbl_employee_primary A");
		attendanceQuery.append(" LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID=BU.BUSINESSUNITID ");
		attendanceQuery
				.append(" LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON A.EMPLOYEEID=DD.EMPLOYEEID ");
		attendanceQuery.append(" LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID=DEP.DEPARTMENTID ");
		attendanceQuery.append(
				" LEFT JOIN HCLADM_PROD.TBL_COSTCENTER G ON A.COSTCENTERID=G.COSTCENTERID AND A.companyid=g.businessunitid ");
		attendanceQuery.append(filter);
		if (!isForEmployee) {
			attendanceQuery.append(" AND " + status);
		}
		attendanceQuery.append(" Order by A.employeesequenceno asc ");
		attendanceQuery.append(" ))D on D.employeeid=A.employeesequenceno and D.LEAVEON=b.dateon ");

		attendanceQuery.append(" LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID=BU.BUSINESSUNITID ");
		attendanceQuery
				.append(" LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON A.EMPLOYEEID=DD.EMPLOYEEID ");
		attendanceQuery.append(" LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID=DEP.DEPARTMENTID ");
		attendanceQuery.append(" LEFT JOIN HCLADM_PROD.TBL_DESIGNATION DES ON DD.DESIGNATIONID=DES.DESIGNATIONID ");
		attendanceQuery.append(
				" LEFT JOIN HCLADM_PROD.TBL_COSTCENTER G ON A.COSTCENTERID=G.COSTCENTERID AND A.companyid=g.businessunitid ");
		attendanceQuery.append(" LEFT JOIN hclhrm_prod.tbl_status_codes ST ON ST.STATUS=A.STATUS ");
		attendanceQuery.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE HH ON A.EMPLOYEEID=HH.EMPLOYEEID ");

		attendanceQuery.append(filter);
		if (!isForEmployee) {
			attendanceQuery.append(" AND " + status);
		}
		attendanceQuery.append(" AND B.dateon BETWEEN '" + start + "' AND '" + end + "' ");
		attendanceQuery.append(" GROUP BY ");
		attendanceQuery.append(" A.employeesequenceno, A.callname ");
		attendanceQuery.append(" Order by A.employeesequenceno asc ");
	}

	public List<AttendanceLocationPojo> getEmployeebusinessunit(int empId) {
		StringBuilder locationsQuery = new StringBuilder();

		locationsQuery.append("SELECT b.businessunitid as BUSINESSUNITID, b.name as NAME ");
		locationsQuery.append("FROM hclhrm_prod.tbl_employee_businessunit e ");
		locationsQuery.append("LEFT JOIN hclhrm_prod.tbl_employee_primary p ON p.employeeid = e.employeeid ");
		locationsQuery.append("LEFT JOIN hcladm_prod.tbl_businessunit b ON e.businessunitid = b.businessunitid ");
		locationsQuery.append("WHERE p.employeesequenceno = ?");

		return jdbcTemplate.query(locationsQuery.toString(), (rs, rowNum) -> {
			AttendanceLocationPojo locations = new AttendanceLocationPojo();
			locations.setId(rs.getInt("BUSINESSUNITID"));
			locations.setName(rs.getString("NAME"));
			return locations;
		}, empId);
	}

	public List<Map<String, Object>> getPayPeriodMonths(Integer empId, Integer location, List<Integer> bu,
			String callName, boolean isPayPeriod) {
		int businessUnitId;
		if (location != null) {
			businessUnitId = (location == 9) ? 23 : 11;
		} else if (bu != null && !bu.isEmpty()) {
			businessUnitId = ("HYD".equalsIgnoreCase(callName) || "ASSAM".equalsIgnoreCase(callName)) ? 11 : 23;
		} else if (empId != null) {
			String userBuQuery = "SELECT companyid FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY WHERE EMPLOYEESEQUENCENO=?";
			businessUnitId = jdbcTemplate.queryForObject(userBuQuery, Integer.class, empId);
		} else {
			throw new AttendanceDataFetchingException("Insufficient data to determine Business Unit");
		}

		// Build SQL Query
		StringBuilder query = new StringBuilder();
		query.append("SELECT ").append("  TRANSACTIONDURATION AS PAYPERIOD, ").append(
				"  MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01')) AS PAYPERIODMONTHNAME, ")
				.append("  CONVERT(LEFT(TRANSACTIONDURATION, 4), CHAR) AS PAYPERIODYEAR, ")
				.append("  FROMDATE AS PAYPERIODFROMDATE, ")
				.append("  DATE_ADD(FROMDATE, INTERVAL DAY(LAST_DAY(DATE_ADD(FROMDATE, INTERVAL 1 MONTH)) - 1) DAY) AS PAYPERIODTODATE, ")
				.append("  MONTHNAME(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01')) AS MONTHNAME, ")
				.append("  CONVERT(LEFT(TRANSACTIONDURATION, 4), CHAR) AS YEAR, ")
				.append("  CONVERT(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01'), CHAR) AS FROMDATE, ")
				.append("  LAST_DAY(CONCAT(LEFT(TRANSACTIONDURATION, 4), '-', RIGHT(TRANSACTIONDURATION, 2), '-01')) AS TODATE ")
				.append("FROM HCLHRM_PROD_OTHERS.TBL_ICONNECT_TRANSACTION_DATES ")
				.append("WHERE transactiontypeid = 1 ").append("AND businessunitid IN (?) ");

		List<Object> params = new ArrayList<>();
		params.add(businessUnitId);

		if (!isPayPeriod || businessUnitId == 23) {
			String currentPeriod = LocalDate.now().getYear() + String.format("%02d", LocalDate.now().getMonthValue());
			query.append("AND TRANSACTIONDURATION <= ? ");
			params.add(currentPeriod);
		}

		query.append("GROUP BY TRANSACTIONDURATION ").append("ORDER BY TRANSACTIONDURATION DESC ").append("LIMIT 12");

		// Execute Query and Transform Results
		return jdbcTemplate.queryForList(query.toString(), params.toArray()).stream().map(payPeriod -> {
			String monthName = Optional.ofNullable(payPeriod.get("PAYPERIODMONTHNAME")).map(Object::toString)
					.orElse("");
			String yearStr = Optional.ofNullable(payPeriod.get("YEAR")).map(Object::toString).orElse("");

			if (!monthName.isEmpty() && !yearStr.isEmpty()) {
				int monthNum = Month.valueOf(monthName.toUpperCase()).getValue();

				Map<String, Object> simplifiedMap = new HashMap<>();
				simplifiedMap.put("YEAR", Integer.parseInt(yearStr));
				simplifiedMap.put("MONTH", monthNum);
				simplifiedMap.put("PAYPERIODMONTHNAME", monthName.toUpperCase() + "-" + yearStr);

				return simplifiedMap;
			}
			return payPeriod;
		}).collect(Collectors.toList());
	}
	
	
	public List<Map<String, Object>> getReaderData(LocalDate fromDate, LocalDate toDate) {
		StringBuilder readerDataQuery = new StringBuilder(" ");
		List<Object> params = new ArrayList<>();
 
		readerDataQuery.append(" SELECT t.employeeid EMPID,ifnull(callname,'--') NAME, ");
		readerDataQuery.append(" date(transactiontime) DATE, r.readername READER ");
		readerDataQuery.append(" FROM unit_local_db.tbl_reader_log t ");
		readerDataQuery.append(" LEFT JOIN hclhrm_prod.tbl_employee_primary b ON t.employeeid=b.employeesequenceno ");
		readerDataQuery.append(" LEFT JOIN unit_local_db.tbl_reader r ON r.readerid=t.readerid ");
		readerDataQuery.append(" WHERE 1=1 ");
 
		// Date conditions
		if (fromDate != null) {
			if (toDate != null) {
				readerDataQuery
						.append(" AND t.readerid in (44,45,47,49,50) AND date(transactiontime) BETWEEN ? AND ? ");
				params.add(java.sql.Date.valueOf(fromDate));
				params.add(java.sql.Date.valueOf(toDate));
			} else {
				readerDataQuery.append(" AND t.readerid in (44,45,47,49,50) AND date(transactiontime) >= ? ");
				params.add(java.sql.Date.valueOf(fromDate));
			}
		} else {
			readerDataQuery.append(" AND date(transactiontime)=curdate() AND t.readerid in (44,45,47,49,50) ");
		}
		readerDataQuery.append(" GROUP BY t.employeeid,date(transactiontime) ");
 
		return jdbcTemplate.queryForList(readerDataQuery.toString(), params.toArray());
	}
	
	
	
}

package com.hetero.heteroiconnect.attendancereports;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AttendanceRepository {
	private JdbcTemplate jdbcTemplate;
	private static final String payPeriodDatesQuery = " SELECT fromdate,todate FROM hclhrm_prod_others.tbl_iconnect_transaction_dates WHERE businessunitid=? AND transactionduration=? ";
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
		LocalDate start = null;
		LocalDate end = null;
		if (attendanceFilterPojo.getIsDataBetween()) {
			start = attendanceFilterPojo.getFromDate();
			end = attendanceFilterPojo.getToDate();
		} else {
			if (attendanceFilterPojo.getIsPayPeriod()) {
				Map<String, Object> payrollDates = null;

				int businessUnitId;

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
					throw new AttendanceDataFetchingException();
				}

				payrollDates = jdbcTemplate.queryForMap(payPeriodDatesQuery, businessUnitId,
						attendanceFilterPojo.getYear() + String.format("%02d", attendanceFilterPojo.getMonth()));

				start = ((Date) payrollDates.get("fromdate")).toLocalDate();
				end = ((Date) payrollDates.get("todate")).toLocalDate();
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
}

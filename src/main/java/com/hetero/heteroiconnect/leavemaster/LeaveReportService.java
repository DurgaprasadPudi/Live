package com.hetero.heteroiconnect.leavemaster;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LeaveReportService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> getDistinctPayPeriod() {
		String sql = "SELECT DISTINCT(year) FROM hclhrm_prod_others.tbl_emp_leave_quota ORDER BY year DESC LIMIT 5";
		return jdbcTemplate.queryForList(sql);
	}

	public Object leaveReport(String payperioddate) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append("EMPLOYEESEQUENCENO 'EMPLOYEEID', ").append("A.CALLNAME as FULLNAME, ")
				.append("B.NAME 'BU_NAME', ").append("IF(pro.dateofjoin = '0000-00-00', NULL, pro.dateofjoin) AS DOJ, ")
				.append("s.name AS STATUS, ")
				.append("qu.year as payperioddate ,")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=1 THEN qu.QUANTITY END), '0') 'CL_QUANTITY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=1 THEN qu.USEDQTY END), '0') 'CL_USEQTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=1 THEN qu.AVAILABLEQTY END), '0') 'CL_AVAILABLEQTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=2 THEN qu.QUANTITY END), '0') 'SL_QUANTITY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=2 THEN qu.AVAILABLEQTY END), '0') 'SL_AVAILABLEQTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=2 THEN qu.USEDQTY END), '0') 'SL_USEDQTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=3 THEN qu.QUANTITY END), '0') 'MATERNITY_LEAVE_QUANTITY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=3 THEN qu.AVAILABLEQTY END), '0') 'MATERNITY_LEAVE_AVAILABLEQTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=3 THEN qu.USEDQTY END), '0') 'MATERNITY_LEAVE_USEDQTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=4 THEN qu.QUANTITY END), '0') 'EL_QUANTITY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=4 THEN qu.AVAILABLEQTY END), '0') 'EL_AVAILABLEQTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=4 THEN qu.USEDQTY END), '0') 'EL_USEDQTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=6 THEN qu.QUANTITY END), '0') 'MARRIAGE_LEAVE_QUANTITY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=6 THEN qu.AVAILABLEQTY END), '0') 'MARRIAGE_LEAVE_AVAILABLEQTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=6 THEN qu.USEDQTY END), '0') 'MARRIAGE_LEAVE_USEDQTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=7 THEN qu.USEDQTY END), '0') 'COFF', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=14 THEN qu.USEDQTY END), '0') 'ONDUTY', ")
				.append("IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=16 THEN qu.USEDQTY END), '0') 'WORKFROMHOME' ")
				.append("FROM ").append("hclhrm_prod.tbl_employee_primary a ").append("LEFT JOIN ")
				.append("hcladm_prod.tbl_status_codes s ON s.status = a.status ").append("LEFT JOIN ")
				.append("hcladm_prod.tbl_businessunit b ON b.businessunitid = a.companyid ").append("LEFT JOIN ")
				.append("hclhrm_prod_others.tbl_emp_leave_quota qu ON qu.employeeid = a.employeeid ")
				.append("LEFT JOIN ").append("hclhrm_prod.tbl_employee_profile pro ON pro.employeeid = a.employeeid ")
				.append("WHERE ").append("qu.year = ? ").append("GROUP BY ").append("a.employeeid");
		return jdbcTemplate.queryForList(sql.toString(), payperioddate);
	}


	public byte[] leaveReportDownload(String payPeriodDate) {
		String sql = "SELECT EMPLOYEESEQUENCENO 'EMPLOYEEID', A.CALLNAME AS FULLNAME, B.NAME 'BU_NAME', "
				+ "IF(pro.dateofjoin = '0000-00-00', NULL, pro.dateofjoin) AS DOJ,qu.year as PayperiodDate, s.name AS STATUS, "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=1 THEN qu.QUANTITY END), '0') 'CL_QUANTITY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=1 THEN qu.USEDQTY END), '0') 'CL_USEQTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=1 THEN qu.AVAILABLEQTY END), '0') 'CL_AVAILABLEQTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=2 THEN qu.QUANTITY END), '0') 'SL_QUANTITY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=2 THEN qu.USEDQTY END), '0') 'SL_USEDQTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=2 THEN qu.AVAILABLEQTY END), '0') 'SL_AVAILABLEQTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=3 THEN qu.QUANTITY END), '0') 'MATERNITY_LEAVE_QUANTITY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=3 THEN qu.USEDQTY END), '0') 'MATERNITY_LEAVE_USEDQTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=3 THEN qu.AVAILABLEQTY END), '0') 'MATERNITY_LEAVE_AVAILABLEQTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=4 THEN qu.QUANTITY END), '0') 'EL_QUANTITY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=4 THEN qu.USEDQTY END), '0') 'EL_USEDQTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=4 THEN qu.AVAILABLEQTY END), '0') 'EL_AVAILABLEQTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=6 THEN qu.QUANTITY END), '0') 'MARRIAGE_LEAVE_QUANTITY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=6 THEN qu.USEDQTY END), '0') 'MARRIAGE_LEAVE_USEDQTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=6 THEN qu.AVAILABLEQTY END), '0') 'MARRIAGE_LEAVE_AVAILABLEQTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=7 THEN qu.USEDQTY END), '0') 'COFF', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=14 THEN qu.USEDQTY END), '0') 'ONDUTY', "
				+ "IFNULL(MAX(CASE WHEN qu.LEAVETYPEID=16 THEN qu.USEDQTY END), '0') 'WORKFROMHOME' "
				+ "FROM hclhrm_prod.tbl_employee_primary a "
				+ "LEFT JOIN hcladm_prod.tbl_status_codes s ON s.status = a.status "
				+ "LEFT JOIN hcladm_prod.tbl_businessunit b ON b.businessunitid = a.companyid "
				+ "LEFT JOIN hclhrm_prod_others.tbl_emp_leave_quota qu ON qu.employeeid = a.employeeid "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_profile pro ON pro.employeeid = a.employeeid "
				+ "WHERE qu.year = ? GROUP BY a.employeeid";
		List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, payPeriodDate);
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Leave Report");
			Set<String> dynamicHeadersSet = new LinkedHashSet<>();
			if (!data.isEmpty()) {
				dynamicHeadersSet.addAll(data.get(0).keySet());
			}
			String[] dynamicHeaders = dynamicHeadersSet.toArray(new String[0]);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < dynamicHeaders.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(dynamicHeaders[i]);
				cell.setCellStyle(headerStyle);
			}
			sheet.createFreezePane(0, 1);
			int rowIdx = 1;
			for (Map<String, Object> rowData : data) {
				Row row = sheet.createRow(rowIdx++);
				int colIdx = 0;
				for (String header : dynamicHeaders) {
					Object value = rowData.getOrDefault(header, "");
					row.createCell(colIdx++).setCellValue(value != null ? value.toString() : "");
				}
			}
			for (int i = 0; i < dynamicHeaders.length; i++) {
				sheet.autoSizeColumn(i);
			}
			workbook.write(out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Failed to generate the leave report Excel file", e);
		}
	}


	public Object leaveReportDate(String employeeid, String fromDate, String toDate, String year) {
		if (year.isEmpty()) {
			StringBuffer query = new StringBuffer();
			query.append(" select a.employeeid AS EMPLOYEEID, g.callname as FULLNAME, ");
//			query.append("     a.to_email AS TOMAIL, ");
//			query.append("     a.cc_email AS CCMAIL, ");
			query.append("     a.subject AS SUBJECT, ");
			query.append("     a.req_date AS REQUEST_DATE, ");
			query.append("     a.from_date AS FROMDATE, ");
			query.append("     a.to_date AS TODATE, ");
			query.append("     a.message AS MESSAGE, ");
			query.append("     b.leave_count_bt_days AS LEAVE_COUNT, ");
			query.append("     b.leave_type AS LEAVE_TYPE ");
//			query.append("     b.manager_status, ");
//			query.append(" a.flag ");
			query.append(" FROM ");
			query.append("  hclhrm_prod_others.tbl_emp_attn_req a ");
			query.append("  join( ");
			query.append("  select ");
			query.append(" employeeid, ");
			query.append(" RID, ");
			query.append(" FROM_DATE,TO_DATE, ");
			query.append(" LEAVE_COUNT_BT_DAYS, ");
			query.append(" LEAVE_TYPE ,manager_status");
			query.append(" from hclhrm_prod_others.tbl_emp_leave_report ");
			query.append(" where leaveon between ? and ? and employeeid=?  and MANAGER_STATUS in('A','P') ");
			query.append(" group by RID,EMPLOYEEID  ");
			query.append("  ) B on B.RID=a.rid and B.EMPLOYEEID=A.EMPLOYEEID ");
		  query.append(" join hclhrm_prod.tbl_employee_primary g on a.employeeid = g.employeesequenceno ");
			query.append("  where a.employeeid=? ");

			return jdbcTemplate.queryForList(query.toString(), fromDate, toDate, employeeid, employeeid);
		} else {
			StringBuffer query = new StringBuffer();
			query.append(" select a.employeeid AS EMPLOYEEID, g.callname as FULLNAME, ");
//			query.append("     a.to_email AS TOMAIL, ");
//			query.append("     a.cc_email AS CCMAIL, ");
			query.append("     a.subject AS SUBJECT, ");
			query.append("     a.req_date AS REQUEST_DATE, ");
			query.append("     a.from_date AS FROMDATE, ");
			query.append("     a.to_date AS TODATE, ");
			query.append("     a.message AS MESSAGE, ");
			query.append("     b.leave_count_bt_days AS LEAVE_COUNT, ");
			query.append("     b.leave_type AS LEAVETYPE");
//			query.append("     b.manager_status, ");
//			query.append(" a.flag ");
			query.append(" FROM ");
			query.append("  hclhrm_prod_others.tbl_emp_attn_req a ");
			query.append("  join( ");
			query.append("  select ");
			query.append(" employeeid, ");
			query.append(" RID, ");
			query.append(" FROM_DATE,TO_DATE, ");
			query.append(" LEAVE_COUNT_BT_DAYS, ");
			query.append(" LEAVE_TYPE ,manager_status");
			query.append(" from hclhrm_prod_others.tbl_emp_leave_report ");
			query.append(" where year(leaveon)=? and employeeid=? and MANAGER_STATUS in('A','P') ");
			query.append(" group by RID,EMPLOYEEID  ");
			query.append("  ) B on B.RID=a.rid and B.EMPLOYEEID=A.EMPLOYEEID ");
			query.append(" join hclhrm_prod.tbl_employee_primary g on a.employeeid = g.employeesequenceno ");

			query.append("  where a.employeeid=? ");

			return jdbcTemplate.queryForList(query.toString(), year, employeeid, employeeid);
		}
	}



	public byte[] leaveReportDownload(String employeeid, String fromDate, String toDate, String year) {
	    StringBuilder query = new StringBuilder();
	    query.append("SELECT a.employeeid EMPLOYEEID, a.to_email TOMAIL, a.cc_email CCMAIL, a.subject SUBJECT, a.req_date REQ_DATE, ")
	         .append("a.message MESSAGE, b.leave_count_bt_days LEAVECOUNT, b.leave_type LEAVETYPE ")
//	         .append("b.manager_status, a.flag ")
	         .append("FROM hclhrm_prod_others.tbl_emp_attn_req a ")
	         .append("JOIN (SELECT employeeid, RID, LEAVE_COUNT_BT_DAYS, LEAVE_TYPE, manager_status ")
	         .append("FROM hclhrm_prod_others.tbl_emp_leave_report WHERE ");
	    if (year.isEmpty()) {
	        query.append("leaveon BETWEEN ? AND ? AND employeeid = ? ");
	    } else {
	        query.append("YEAR(leaveon) = ? AND employeeid = ? ");
	    }
	    query.append("GROUP BY RID, EMPLOYEEID) b ")
	         .append("ON b.RID = a.rid AND b.EMPLOYEEID = a.employeeid ")
	         .append("WHERE a.employeeid = ?");
	    Object[] params;
	    if (year.isEmpty()) {
	        params = new Object[]{fromDate, toDate, employeeid, employeeid};
	    } else {
	        params = new Object[]{year, employeeid, employeeid};
	    }
	    List<Map<String, Object>> data = jdbcTemplate.queryForList(query.toString(), params);
	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Leave Report");
	        if (data.isEmpty()) {
	            return new byte[0];  
	        }
	        Set<String> headersSet = data.get(0).keySet();
	        String[] headers = headersSet.toArray(new String[0]);
	        CellStyle headerStyle = workbook.createCellStyle();
	        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerStyle.setAlignment(HorizontalAlignment.CENTER);
	        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	        Row headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; i++) {
	            headerRow.createCell(i).setCellValue(headers[i]);
	            headerRow.getCell(i).setCellStyle(headerStyle);
	        }
	        int rowIdx = 1;
	        for (Map<String, Object> rowData : data) {
	            Row row = sheet.createRow(rowIdx++);
	            int colIdx = 0;
	            for (String header : headers) {
	                row.createCell(colIdx++).setCellValue(String.valueOf(rowData.getOrDefault(header, "")));
	            }
	        }
	        for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }
	    	System.out.println("sfgh");
	        workbook.write(out);
	        return out.toByteArray();
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to generate the leave report Excel file", e);
	    }
	}

}

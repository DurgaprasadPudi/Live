package com.hetero.heteroiconnect.existingduelist;

import java.sql.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
public class IncrementRepositoryImpl implements IncrementRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public JSONArray fecthincrement() throws Exception {

        String sql = "SELECT "
                + "A.EMPLOYEESEQUENCENO AS EMPLOYEEID, "
                + "A.CALLNAME AS NAME, "
                + "BU.NAME AS BusinessUnit, "
                + "CO.NAME AS COSTCENTER, "
                + "IFNULL(STROKE.NAME, '--') AS `INCREMENT TYPE`, "
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
                + " IFNULL(MAX(IF(CTCD.COMPONENTID = 124, CTCD.COMPONENTVALUE, 0)), 0)) AS `E Gross`, "
                + "CTC.EFFECTIVEDATE, "
                + "DATE_ADD(CTC.EFFECTIVEDATE, INTERVAL 1 YEAR) AS NextDueDate, "
                + "DATE_FORMAT(DATE_ADD(CTC.EFFECTIVEDATE, INTERVAL 1 YEAR), '%b-%Y') AS NextDueMonthYear, "
                + "CASE "
                + " WHEN MONTH(DATE_ADD(CTC.EFFECTIVEDATE, INTERVAL 1 YEAR)) BETWEEN 4 AND 6 THEN 'Q1' "
                + " WHEN MONTH(DATE_ADD(CTC.EFFECTIVEDATE, INTERVAL 1 YEAR)) BETWEEN 7 AND 9 THEN 'Q2' "
                + " WHEN MONTH(DATE_ADD(CTC.EFFECTIVEDATE, INTERVAL 1 YEAR)) BETWEEN 10 AND 12 THEN 'Q3' "
                + " ELSE 'Q4' END AS FY_Quarter, "
                + "CONCAT(TIMESTAMPDIFF(YEAR, CTC.EFFECTIVEDATE, CURDATE()), ' years ', "
                + "TIMESTAMPDIFF(MONTH, CTC.EFFECTIVEDATE, CURDATE()) % 12, ' months') AS TotalYearsMonthsCompleted, "
                + "PRO.DATEOFJOIN AS DOJ, "
                + "365 - (52 + COALESCE(HOLIDAY.HOLIDAYCOUNT, 0)) AS COMPANY_WORKING_DAYS, "
                + "COALESCE(LEAVES.USEDLEAVECOUNT, 0) AS USED, "
                + "COALESCE(LOP.LOPCOUNT, 0) AS LOP, "
                + "(365 - (52 + COALESCE(HOLIDAY.HOLIDAYCOUNT, 0))) - "
                + "(COALESCE(LEAVES.USEDLEAVECOUNT, 0) + COALESCE(LOP.LOPCOUNT, 0)) AS PHYSICAL_WORKING_DAYS, "
                + "CASE "
                + " WHEN ((365 - (52 + COALESCE(HOLIDAY.HOLIDAYCOUNT, 0))) - "
                + "(COALESCE(LEAVES.USEDLEAVECOUNT, 0) + COALESCE(LOP.LOPCOUNT, 0))) >= 240 "
                + " THEN CASE WHEN A.COMPANYID IN (15, 16, 33, 34) "
                + " THEN FLOOR(((365 - (52 + COALESCE(HOLIDAY.HOLIDAYCOUNT, 0))) - "
                + "(COALESCE(LEAVES.USEDLEAVECOUNT, 0) + COALESCE(LOP.LOPCOUNT, 0))) / 20) ELSE 15 END "
                + " ELSE 0 END AS ELIGIBLE_DAYS "
                + "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A "
                + "LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON A.COMPANYID = BU.BUSINESSUNITID "
                + "LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON A.EMPLOYEEID = DD.EMPLOYEEID "
                + "LEFT JOIN HCLADM_PROD.tbl_increment_type STROKE ON DD.INCREMENTTYPEID = STROKE.INCREMENTTYPEID "
                + "LEFT JOIN hcladm_prod.tbl_costcenter CO ON CO.COSTCENTERID = A.COSTCENTERID "
                + "LEFT JOIN hclhrm_prod.tbl_employee_profile PRO ON PRO.EMPLOYEEID = A.EMPLOYEEID "
                + "LEFT JOIN (SELECT EMPLOYEEID AS CTCEMPID, MAX(CTCTRANSACTIONID) AS MAXID, MAX(EFFECTIVEDATE) AS EFFECTIVEDATE "
                + "FROM HCLHRM_PROD.TBL_EMPLOYEE_CTC GROUP BY EMPLOYEEID) CTC ON CTC.CTCEMPID = A.EMPLOYEEID "
                + "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_CTC_DETAILS CTCD ON CTCD.CTCTRANSACTIONID = CTC.MAXID "
                + "LEFT JOIN (SELECT R.EMPLOYEEID, SUM(R.LEAVE_COUNT) AS USEDLEAVECOUNT FROM HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_REPORT R "
                + "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY P ON P.EMPLOYEESEQUENCENO = R.EMPLOYEEID "
                + "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE G ON G.EMPLOYEEID = P.EMPLOYEEID "
                + "WHERE R.MANAGER_STATUS IN ('A', 'P') AND R.LEAVE_TYPE IN ('CL', 'EL', 'SL') "
                + "AND R.LEAVEON BETWEEN G.DATEOFJOIN AND DATE_ADD(G.DATEOFJOIN, INTERVAL 12 MONTH) "
                + "GROUP BY R.EMPLOYEEID) LEAVES ON LEAVES.EMPLOYEEID = A.EMPLOYEESEQUENCENO "
                + "LEFT JOIN (SELECT PP.EMPLOYEESEQUENCENO, SUM(LOP.LOPCOUNT) AS LOPCOUNT FROM HCLHRM_PROD.TBL_EMPLOYEE_LOP LOP "
                + "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY PP ON PP.EMPLOYEEID = LOP.EMPLOYEEID "
                + "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE PRO ON PRO.EMPLOYEEID = PP.EMPLOYEEID "
                + "WHERE LOP.LOPTRANSACTIONID BETWEEN "
                + "CONCAT(YEAR(PRO.DATEOFJOIN), LPAD(MONTH(PRO.DATEOFJOIN), 2, '0')) "
                + "AND PERIOD_ADD(CONCAT(YEAR(PRO.DATEOFJOIN), LPAD(MONTH(PRO.DATEOFJOIN), 2, '0')), 12) "
                + "GROUP BY PP.EMPLOYEESEQUENCENO) LOP ON LOP.EMPLOYEESEQUENCENO = A.EMPLOYEESEQUENCENO "
                + "LEFT JOIN (SELECT HP.EMPLOYEESEQUENCENO, COUNT(*) AS HOLIDAYCOUNT FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY HP "
                + "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE HPRO ON HPRO.EMPLOYEEID = HP.EMPLOYEEID "
                + "LEFT JOIN HCLHRM_PROD.TBL_HOLIDAYS HL ON HL.BUSINESSUNITID = HP.COMPANYID "
                + "WHERE HL.HOLIDAYDATE BETWEEN HPRO.DATEOFJOIN AND DATE_ADD(HPRO.DATEOFJOIN, INTERVAL 12 MONTH) "
                + "GROUP BY HP.EMPLOYEESEQUENCENO) HOLIDAY ON HOLIDAY.EMPLOYEESEQUENCENO = A.EMPLOYEESEQUENCENO "
                + "WHERE BU.CALLNAME = 'HYD' AND A.STATUS IN (1001, 1092, 1401) "
                + "AND DATE_ADD(CTC.EFFECTIVEDATE, INTERVAL 1 YEAR) <= CURDATE() "
                + "GROUP BY A.EMPLOYEESEQUENCENO ORDER BY NextDueDate ASC";

        JSONArray resultArray = new JSONArray();

        jdbcTemplate.query(sql, (ResultSet rs) -> {
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("EMPLOYEEID", rs.getString("EMPLOYEEID"));
                obj.put("NAME", rs.getString("NAME"));
                obj.put("BusinessUnit", rs.getString("BusinessUnit"));
                obj.put("COSTCENTER", rs.getString("COSTCENTER"));
                obj.put("INCREMENT_TYPE", rs.getString("INCREMENT TYPE"));
                obj.put("E_Gross", rs.getDouble("E Gross"));
                obj.put("EFFECTIVEDATE", rs.getString("EFFECTIVEDATE"));
                obj.put("NextDueDate", rs.getString("NextDueDate"));
                obj.put("NextDueMonthYear", rs.getString("NextDueMonthYear"));
                obj.put("FY_Quarter", rs.getString("FY_Quarter"));
                obj.put("TotalYearsMonthsCompleted", rs.getString("TotalYearsMonthsCompleted"));
                obj.put("DOJ", rs.getString("DOJ"));
                obj.put("COMPANY_WORKING_DAYS", rs.getInt("COMPANY_WORKING_DAYS"));
                obj.put("USED", rs.getInt("USED"));
                obj.put("LOP", rs.getInt("LOP"));
                obj.put("PHYSICAL_WORKING_DAYS", rs.getInt("PHYSICAL_WORKING_DAYS"));
                obj.put("ELIGIBLE_DAYS", rs.getInt("ELIGIBLE_DAYS"));
                resultArray.add(obj);
            }
        });

        return resultArray;
    }
}

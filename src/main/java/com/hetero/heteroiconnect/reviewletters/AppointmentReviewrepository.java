package com.hetero.heteroiconnect.reviewletters;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Repository
public class AppointmentReviewrepository {

    @Autowired  
    private JdbcTemplate jdbcTemplate;

    public synchronized JSONArray Reviewletter() {
        JSONArray Reviewletter = new JSONArray();

        try {
            String sqlQuery = generateSQLQuery(); // Method to generate SQL query
            System.out.println("Generated SQL query: " + sqlQuery);

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlQuery);
            for (Map<String, Object> row : rows) {
                JSONObject obct1 = new JSONObject();
                obct1.put("EMPLOYEEID", row.get("EMPLOYEEID"));
                obct1.put("NAME", row.get("NAME"));
                obct1.put("BUNAME", row.get("BUNAME"));
                obct1.put("COSTCENTER", row.get("COSTCENTER"));
                obct1.put("DATEOFJOIN", formatDateString(row.get("DATEOFJOIN")));
                obct1.put("CODE", row.get("CODE"));
                obct1.put("MAILSTATUS", row.get("MAILSTATUS"));
                obct1.put("CREATEDDATE", row.get("CREATEDDATE"));
                obct1.put("CTCPERANNUM", row.get("CTCPERANNUM"));
                obct1.put("DATE24HOURS", row.get("DATE24HOURS"));
                obct1.put("DESIGNATIONCHECK", row.get("DESIGNATIONCHECK"));
                obct1.put("DEPARTMENTCHECK", row.get("DEPARTMENTCHECK"));
                obct1.put("CTCCHECK", row.get("CTCCHECK"));
                obct1.put("ADDRESSCHECK", row.get("ADDRESSCHECK"));
                
                obct1.put("FILEPATH", row.get("FILEPATH"));
                obct1.put("FLAG", row.get("FLAG"));
                obct1.put("UPLOADCTC", row.get("UPLOADCTC"));
                


                double threshold = 20.0; // Threshold value (assuming double for precision)

             // Assuming row.get("DIFF_UPLOADCTC_CTCPERANNUM") returns a Double
             Double diffUploadCtcCtcpPerAnnum = (Double) row.get("DIFF_UPLOADCTC_CTCPERANNUM");
             
             // Evaluate condition
             boolean isWithinThreshold = Math.abs(diffUploadCtcCtcpPerAnnum) <= threshold;

             // Convert boolean to string "true" or "false"
             String resultString = isWithinThreshold ? "TRUE" : "FALSE";

             // Print or use the resultString as needed
             System.out.println("Is within ±20 rupees: " + resultString);
             
             obct1.put("CTC_DIFF", resultString);
                
                obct1.put("DIFF_UPLOADCTC_CTCPERANNUM", row.get("DIFF_UPLOADCTC_CTCPERANNUM"));
                
                
                
                //obct1.put("GENRATEBUTTON", row.get("FILEPATH"));
               
                	  
                
                	 
                	
                	  if(row.get("DESIGNATIONCHECK").equals("TRUE")&&
                			row.get("DEPARTMENTCHECK").equals("TRUE")&&
                			row.get("CTCCHECK").equals("TRUE")&&
                			row.get("ADDRESSCHECK").equals("TRUE")&&row.get("FILEPATH").equals("0"))
                	{
                		obct1.put("VIEWBUTTON", "FALSE");
                		obct1.put("GENRATEBUTTON","TRUE");
                 	}
                 
                	else if(row.get("DESIGNATIONCHECK").equals("TRUE")&&
                			row.get("DEPARTMENTCHECK").equals("TRUE")&&
                			row.get("CTCCHECK").equals("TRUE")&&
                			row.get("ADDRESSCHECK").equals("TRUE")&&!row.get("FILEPATH").equals("0"))
                	{
                		
                		
                		obct1.put("VIEWBUTTON", "TRUE");
                		obct1.put("GENRATEBUTTON","FALSE");
                	}
                	 
                	else {
                          obct1.put("VIEWBUTTON","FALSE");
                		
                		 obct1.put("GENRATEBUTTON","FALSE");
                	}
                
  
                Reviewletter.add(obct1);
            }

            System.out.println("Resulting JSON array: " + Reviewletter);
        } catch (Exception ex) {
            System.out.println("Error in probationaryListToMngr method: " + ex.getMessage());
            ex.printStackTrace();
        }

        return Reviewletter;
    }

    // Method to generate the SQL query dynamically based on empID
    private String generateSQLQuery() {
        StringBuilder query = new StringBuilder();

        query.append("SELECT ");
        query.append("P.employeesequenceno AS EMPLOYEEID, ");
        query.append("P.callname AS NAME, ");
        query.append("BU.NAME AS BUNAME, ");
        query.append("PRO.DATEOFJOIN, ");
        query.append("COST.NAME AS COSTCENTER, ");
        query.append("BU.CODE AS CODE, ");
        query.append("A.MAILSTATUS AS MAILSTATUS, ");
        query.append("A.CREATEDDATE AS CREATEDDATE, ");
        query.append("A.CTCPERANNUM AS CTCPERANNUM, ");
        query.append("CASE WHEN DATE_ADD(A.CREATEDDATE, INTERVAL 24 HOUR) > NOW() THEN 'TRUE' ELSE 'FALSE' END AS DATE24HOURS, ");
        query.append("IF(IFNULL(D.DESIGNATIONID,'0')!='0','TRUE','FALSE') AS DESIGNATIONCHECK, ");
        query.append("IF(IFNULL(D.DEPARTMENTID,'0')!='0','TRUE','FALSE') AS DEPARTMENTCHECK, ");
        query.append("IF(IFNULL(CTC.Maxid,'0')!='0','TRUE','FALSE') AS CTCCHECK, ");
        query.append("IF(IFNULL(ADDRESS.PERMANENTADDRESS,'') != '', 'TRUE', 'FALSE') AS ADDRESSCHECK, ");
        query.append("A.FILEPATH, ");
        query.append("FLAG, ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 22 THEN CTCD.COMPONENTVALUE * 12 END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 97 THEN CTCD.COMPONENTVALUE * 12 END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 98 THEN CTCD.COMPONENTVALUE * 12 END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 124 THEN CTCD.COMPONENTVALUE * 12 END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 34 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 35 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 36 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 33 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 39 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 63 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 62 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 93 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9001, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9002, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9003, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9004, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9005, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9006, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(CTCD.COMPONENTID=129,CTCD.COMPONENTVALUE,0)),'0')+IFNULL(MAX(IF(CTCD.COMPONENTID=130,CTCD.COMPONENTVALUE,0)),'0') AS UPLOADCTC, ");
        query.append("( ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 22 THEN CTCD.COMPONENTVALUE * 12 END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 97 THEN CTCD.COMPONENTVALUE * 12 END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 98 THEN CTCD.COMPONENTVALUE * 12 END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 124 THEN CTCD.COMPONENTVALUE * 12 END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 34 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 35 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 36 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 33 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 39 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 63 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 62 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(CASE WHEN CTCD.COMPONENTID = 93 THEN CTCD.COMPONENTVALUE END), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9001, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9002, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9003, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9004, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9005, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(XX.COMPONENTID = 9006, XY.COMPONENTVALUE, 0)), 0) + ");
        query.append("IFNULL(MAX(IF(CTCD.COMPONENTID=129,CTCD.COMPONENTVALUE,0)),'0')+IFNULL(MAX(IF(CTCD.COMPONENTID=130,CTCD.COMPONENTVALUE,0)),'0') ");
        query.append(") - A.CTCPERANNUM AS DIFF_UPLOADCTC_CTCPERANNUM ");
        query.append("FROM ");
        query.append("test.appointmentletter_temp A ");
        query.append("LEFT JOIN hclhrm_prod.tbl_employee_primary P ON P.employeesequenceno = A.employeeid ");
        query.append("LEFT JOIN (SELECT EMPLOYEEID ctcempid, MAX(CTCTRANSACTIONID) Maxid FROM HCLHRM_PROD.TBL_EMPLOYEE_CTC GROUP BY employeeid) CTC ");
        query.append("ON CTC.ctcempid = P.EMPLOYEEID ");
        query.append("LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_CTC_DETAILS CTCD ON CTCD.CTCTRANSACTIONID = CTC.Maxid ");
        query.append("LEFT JOIN TEST.FUEL_MAINTENANCE XY ON XY.CTCTRANSACTIONID = CTC.Maxid AND XY.EMPLOYEEID = P.EMPLOYEEID ");
        query.append("LEFT JOIN TEST.TBL_NEW_COMPONENTS XX ON XX.COMPONENTID = XY.COMPONENTID ");
        query.append("LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE PRO ON PRO.EMPLOYEEID = P.EMPLOYEEID ");
        query.append("LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS D ON P.EMPLOYEEID = D.EMPLOYEEID ");
        query.append("LEFT JOIN hcladm_prod.tbl_costcenter COST ON P.COSTCENTERID = COST.COSTCENTERID ");
        query.append("LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT BU ON BU.BUSINESSUNITID = P.COMPANYID ");
        query.append("LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PERSONAL_CONTACT ADDRESS ON P.EMPLOYEEID = ADDRESS.EMPLOYEEID WHERE  BU.CALLNAME='HYD' and BU.BUSINESSUNITID!=25 and PRO.DATEOFJOIN >='2024-07-01' ");
        query.append("GROUP BY A.EMPLOYEEID  order by PRO.DATEOFJOIN DESC ;");


        query.toString();
 
      

        return query.toString();
    }
    
    public int GenRate(String Employeeid, String USEDRID,String Type) {
        String query = "UPDATE test.appointmentletter_temp " +
                       "SET PREVIEWBY = '"+Employeeid+"', FLAG = '"+Type+"' " +
                       "WHERE EMPLOYEEID = '"+USEDRID+"'";

        int count = 0;

         System.out.println("--->"+query);
        try {
            count = jdbcTemplate.update(query);
        } catch (Exception err) {
            System.out.println("Exception at reverse: " + err);
        }

        return count;
    }
    private String formatDateString(Object dateObject) {
        if (dateObject instanceof java.sql.Date) {
            java.sql.Date date = (java.sql.Date) dateObject;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as per your requirement
            return dateFormat.format(date);
        } else {
            return ""; // Handle null or unexpected types gracefully
        }
    }  
      
      
   

    public String checkFilePath(String employeeId) {
        String query = "SELECT FILEPATH FROM test.appointmentletter_temp WHERE employeeid = ? AND FLAG IN('S','P')";
            
        try {
            // Execute query using jdbcTemplate with prepared statement
            String filePath = jdbcTemplate.queryForObject(query, new Object[]{employeeId}, String.class);

            // Check if filePath is null or "0" 
            if (filePath == null || "0".equals(filePath)) {
                return "FALSE"; // Return "FALSE" if FILEPATH is null or '0'
            } else {
                return "TRUE"; // Return "TRUE" if FILEPATH is not '0'
            }
        } catch (EmptyResultDataAccessException e) {
            // Handle case where no rows are returned by the query
            System.out.println("No record found for employeeId: " + employeeId);
            return "FALSE"; // Return "FALSE" if no matching record found
        } catch (Exception err) { 
            // Catch any other exceptions
            System.out.println("Exception in checkFilePath method: " + err.getMessage());
            err.printStackTrace(); // Print stack trace for detailed error information
            return "FALSE"; // Return "FALSE" in case of any exception
        }
    }

     

}

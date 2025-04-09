package com.hetero.heteroiconnect.reviewletters;

import java.sql.Timestamp;
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
public class IncrementReviewrespository {

    @Autowired  
    private JdbcTemplate jdbcTemplate;
 
        public String checkletter(String employeeId) {
        	
        	   StringBuilder query = new StringBuilder();
               query.append("SELECT p.employeesequenceno AS EMPLOYEEID, ");
               query.append("p.callname AS EMPNAME, ");
               query.append("i.PREVIOUS_CTC, ");
               query.append("i.REVISED_CTC, ");
               query.append("i.RELEASE_DATE, BU.CODE, ");
               query.append("IF(curdate() >= i.RELEASE_DATE, 'TRUE', 'FALSE') AS VIEW, ");
               query.append("i.FLAG, i.LUPDATE ");
               query.append("FROM test.tbl_increment_letter i ");
               query.append("LEFT JOIN hclhrm_prod.tbl_employee_primary p ON p.employeesequenceno = i.employeeid ");
               query.append("LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.businessunitid = p.companyid ");
               query.append("WHERE i.FLAG = 'N' AND p.employeesequenceno = ?;");
            
            try {
            	 List<Map<String, Object>> result = jdbcTemplate.queryForList(query.toString(), employeeId);

                 // Check if result is empty
                 if (result.isEmpty()) {
                     return "FALSE"; // No matching record found, return "false"
                 } else {
                     // Get the value of the VIEW column from the first row
                     String viewValue = (String) result.get(0).get("VIEW");
                     return viewValue.equals("TRUE") ? "TRUE" : "FALSE";
                 }
            } catch (Exception err) { 
                System.out.println("Exception in checkFilePath method: " + err.getMessage());
                err.printStackTrace(); // Print stack trace for detailed error information
                return "FALSE"; // Return "false" in case of an exception or no matching record
            }
        }
        
        public synchronized JSONArray fetchIncrementLetterData() {
            StringBuilder query = new StringBuilder();
            query.append("SELECT p.employeesequenceno AS EMPLOYEEID, ");
            query.append("p.callname AS EMPNAME,BU.NAME BUNAME,COST.NAME COSTCENTER,");
            query.append("i.PREVIOUS_CTC, ");
            query.append("i.REVISED_CTC, ");
            query.append("i.RELEASE_DATE, BU.CODE, ");
            query.append("IF(curdate() >= i.RELEASE_DATE, 'TRUE', 'FALSE') AS VIEW, ");
            query.append("i.FLAG, i.LUPDATE ");
            query.append("FROM test.tbl_increment_letter i ");
            query.append("LEFT JOIN hclhrm_prod.tbl_employee_primary p ON p.employeesequenceno = i.employeeid ");
            query.append("LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.businessunitid = p.companyid "
            		+ " LEFT JOIN  hcladm_prod.tbl_costcenter COST ON P.COSTCENTERID = COST.COSTCENTERID");
            JSONArray jsonArray = new JSONArray();
                    
            try {
            	
            	
            	 List<Map<String, Object>> rows = jdbcTemplate.queryForList(query.toString());
                 // Create JSONArray to hold JSONObjects
            	 for (Map<String, Object> row : rows) {
                     JSONObject jsonObject = new JSONObject();
                     jsonObject.put("EMPLOYEEID", row.get("EMPLOYEEID"));
                     jsonObject.put("EMPNAME", row.get("EMPNAME"));
                     jsonObject.put("BUNAME", row.get("BUNAME"));
                     jsonObject.put("COSTCENTER", row.get("COSTCENTER"));
                     jsonObject.put("PREVIOUS_CTC", row.get("PREVIOUS_CTC"));
                     jsonObject.put("REVISED_CTC", row.get("REVISED_CTC"));

                     // Handle RELEASE_DATE
                     Object releaseDateObj = row.get("RELEASE_DATE");
                     if (releaseDateObj instanceof java.sql.Date) {
                         java.sql.Date sqlDate = (java.sql.Date) releaseDateObj;
                         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                         String releaseDateString = sdf.format(sqlDate);
                         jsonObject.put("RELEASE_DATE", releaseDateString);
                     } else {
                         jsonObject.put("RELEASE_DATE", null); // Handle null or unexpected type
                     }

                     // Handle LUPDATE
                     Object lupdateObj = row.get("LUPDATE");
                     if (lupdateObj instanceof Timestamp) {
                         Timestamp timestamp = (Timestamp) lupdateObj;
                         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                         String lupdateString = sdf.format(timestamp);
                         jsonObject.put("LUPDATE", lupdateString);
                     } else {
                         jsonObject.put("LUPDATE", null); // Handle null or unexpected type
                     }

                     jsonObject.put("CODE", row.get("CODE"));
                     jsonObject.put("VIEW", row.get("VIEW"));
                     jsonObject.put("FLAG", row.get("FLAG"));

                     jsonArray.add(jsonObject);
                 }
            } catch (Exception err) {
                System.out.println("Exception while fetching increment letter data: " + err.getMessage());
                err.printStackTrace();
                return null; // Handle or log the exception as needed
            }
            return jsonArray;
        }
        
        
        public String checkincrementrespository(String employeeId) {
            String query = "SELECT IF(CURDATE() >= i.RELEASE_DATE, 'TRUE', 'FALSE') AS VIEW " +
                           "FROM test.tbl_increment_letter i " +
                           "WHERE i.employeeid = ? AND i.FLAG = 'S'";
            
            try {
                // Execute query using jdbcTemplate with prepared statement
                String view = jdbcTemplate.queryForObject(query, new Object[]{employeeId}, String.class);

                return view; // Return the VIEW value obtained from the query

            } catch (EmptyResultDataAccessException e) {
                // Handle case where no rows are returned by the query
                System.out.println("No record found for employeeId: " + employeeId);
                return "FALSE"; // Return "FALSE" if no matching record found
            } catch (Exception err) {
                // Catch any other exceptions
                System.out.println("Exception in checkincrementrespository method: " + err.getMessage());
                err.printStackTrace(); // Print stack trace for detailed error information
                return "FALSE"; // Return "FALSE" in case of any exception
            }
        }

       
}

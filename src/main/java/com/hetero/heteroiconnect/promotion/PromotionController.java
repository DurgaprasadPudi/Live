package com.hetero.heteroiconnect.promotion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

 

@RestController
@RequestMapping("/promotionletters")
public class PromotionController {
	@Autowired
	private PromotionService promotionService;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@PostMapping(value = "/fetchemployee", produces = "application/json")
	public List<Map<String, Object>> getEmployees() {
		return promotionService.getEmployees();
	}

	@PostMapping("/fetchbyempid")
	public Object getByEmpid(@RequestParam int employeeseq) {
		return promotionService.getByEmpid(employeeseq);
	}

	@GetMapping(value = "/transfertypes", produces = "application/json")
	public Object getTransferTypes() {
		return promotionService.getTransferTypes();
	}

	@GetMapping(value = "/transferdetails", produces = "application/json")
	public Object getTransferDetails() {
		return promotionService.getTransferDetails();
	}

	@GetMapping(value = "/businessunit", produces = "application/json")
	public Object getBusinessUnit() {
		return promotionService.getBusinessUnit();
	}

	@GetMapping(value = "/departments", produces = "application/json")
	public Object getDepts() {
		return promotionService.getDepts();
	}

	@GetMapping(value = "/designation", produces = "application/json")
	public Object getDesigs() {
		return promotionService.getDesigs();
	}
//promotion
	@PostMapping(value = "/registration", produces = "application/json")
	public ResponseEntity<?> registation(@RequestBody PromotionRegistation promotionRegistation) {
		return ResponseEntity.ok(promotionService.registation(promotionRegistation));
	}
	@PostMapping(value = "/update/{transactionid}", produces = "application/json")
	public ResponseEntity<?> update(@PathVariable int transactionid,@RequestBody PromotionRegistation promotionRegistation) {
		return ResponseEntity.ok(promotionService.update(transactionid,promotionRegistation));
	}
	@GetMapping(value = "/delete/{transactionid}", produces = "application/json")
	public ResponseEntity<?> Delete(@PathVariable int transactionid) {
		return ResponseEntity.ok(promotionService.delete(transactionid));
	}
	
	@PostMapping(value = "/reporties",consumes = "multipart/form-data", produces = "application/json")
	public List<Map<String, Object>> getReporties(@RequestParam String search) {
		return promotionService.getReporties(search);
	}
	//confirmation
	@GetMapping(value="/confirmation/status",produces = "application/json")
	public Object getStatus() {
		return promotionService.getStatus();
	}
	@PostMapping(value="/confirmation/registation",produces = "application/json")
	public ResponseEntity<?> insertregistation(@RequestBody ConfirmationRegistation confirmationRegistation) {
		return ResponseEntity.ok(promotionService.insertregistation(confirmationRegistation));
	}
	@GetMapping(value="/confirmation/data",produces="application/json")
	public ResponseEntity<?> fetchConfirmationData(){
		return ResponseEntity.ok(promotionService.fetchConfirmationData());
	}
	@PostMapping(value = "/updateconfirmation/{employeeid}", produces = "application/json")
	public ResponseEntity<?> update(@PathVariable int employeeid,@RequestBody ConfirmationRegistation confirmationRegistation) {
		return ResponseEntity.ok(promotionService.updateconfirmation(employeeid,confirmationRegistation));
	}
	@GetMapping(value="/deleteconfirmation/{employeeid}/{employmenttypeid}",produces="application/json")
	public ResponseEntity<?> deleteEmployee(@PathVariable int employeeid,@PathVariable int employmenttypeid) {
		return ResponseEntity.ok(promotionService.deleteEmployee(employeeid,employmenttypeid));
	}
	  
	@RequestMapping(value = "/get/letter/{emp}/{Transactionid}", method = RequestMethod.GET)
	public Object getPromotionLetterPdf(@PathVariable("emp") String emp, @PathVariable("Transactionid") String Transactionid) {
	    try {
	    	
	    	System.out.println("call");
	        String query = "SELECT COUNT(*) AS COUNT, FILEPATH FROM hclhrm_prod.tbl_employee_transfers_temp temp "
	        		+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p on p.employeeid=temp.employeeid "
	                     + "WHERE p.employeesequenceno= " + emp + " AND temp.TRANSACTIONID = " + Transactionid;

	        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
	     

	            if (rows != null && !rows.isEmpty()) {
	                Map<String, Object> row = rows.get(0);
	                String count = row.get("COUNT").toString();
	                String filePath = (String) row.get("FILEPATH");

	                if ("1".equals(count) && filePath != null) {
	                    File file = new File(filePath);
	                    if (file.exists()) {
	                        FileInputStream fileInputStream = new FileInputStream(file);
	                        return IOUtils.toByteArray(fileInputStream);
	                    }
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    } 
	
	
	@PostMapping("/confirmationlink")
   	public LinkedHashMap<String, Object> incrementlink(@RequestBody String login) throws Exception {

		LinkedHashMap<String, Object> response = new LinkedHashMap<>();
		 try {
	            org.json.JSONObject object = new org.json.JSONObject(login);
	            String empID = object.getString("empID");

	            // Call service method to check FILEPATH condition
	            String View = promotionService.checkconfirmation(empID);

	            response.put("VIEW", View);
	        } catch (Exception e) {
	            response.put("error", "Error processing request");
	            e.printStackTrace(); // Log exception for debugging
	        }

	        return response;
	    
}

	
}

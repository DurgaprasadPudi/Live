package com.hetero.heteroiconnect.masterreports;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hetero.heteroiconnect.hrassetrequests.Master;

@RestController
@RequestMapping("/report")
public class MasterReportController {
	private MasterReportService masterReportService;

	public MasterReportController(MasterReportService masterReportService) {
		this.masterReportService = masterReportService;
	}

	@PostMapping("/masterdetails")
	public ResponseEntity<List<EmployeeMasterDetailsDTO>> getMasterDetails(@RequestBody MasterDetailsRequest request) {
		return ResponseEntity.ok(masterReportService.getMasterDetails(request));
	}

	@GetMapping(value = "/statuscode", produces = "application/json")
	public ResponseEntity<List<Master>> getStatusCodes() {
		return ResponseEntity.ok(masterReportService.getStatusCodes());
	}
	
	@PostMapping("/reportingdetails")	
	public ResponseEntity<List<EmployeeMasterDetailsDTO>> getMasterDetails(@RequestBody Map<String, String> request) {
	    // Get the comma-separated reporting IDs
	    String reportingIdsCsv = request.get("reportingIds"); // key must be "reportingIds"

	    List<EmployeeMasterDetailsDTO> result = masterReportService.getReportingMasterDetails(reportingIdsCsv);
	    return ResponseEntity.ok(result);
	}
	
	  @PostMapping("/subordinates")
	    public ResponseEntity<List<Integer>> getSubordinates(@RequestBody Map<String, Integer> request) {
	        Integer managerSeq = request.get("managerSeq");
	        if (managerSeq == null) {
	            return ResponseEntity.badRequest().build();
	        }

	        List<Integer> subordinates = masterReportService.getAllSubordinates(managerSeq);
	        return ResponseEntity.ok(subordinates);
	    }
	  
	  @PostMapping("/reportings")
	    public ResponseEntity<List<String>> getReportingEmployees(@RequestBody Map<String, Integer> request) {
	        Integer managerSeq = request.get("managerSeq"); // key must be "managerSeq"
	        if (managerSeq == null) {
	            return ResponseEntity.badRequest().build();
	        }

	        List<String> reportings = masterReportService.getReportingEmployees(managerSeq);
	        return ResponseEntity.ok(reportings);
	    }
	
	
}

package com.hetero.heteroiconnect.allowance;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

 


@RestController
@RequestMapping("/allowance")
public class AllowanceController {
	private final AllowanceService allowanceService;

	public AllowanceController(AllowanceService allowanceService) {
		this.allowanceService = allowanceService;
	}
	
	@PostMapping(value="/allemployeeinfo",consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> getAllAllowanceEmployeeInfo(@RequestParam String type) {
	    return ResponseEntity.ok(allowanceService.getAllAllowanceEmployeeInfo(type));
	}
	  

	@GetMapping(value = "/components", produces = "application/json")
	public ResponseEntity<?> getAllowanceComponents() {
		return ResponseEntity.ok(allowanceService.getComponent());
	}

	@PostMapping(value = "/registration", produces = "application/json")
	public ResponseEntity<ApiResponse> insertAllowances(@RequestBody List<EmployeeAllowanceDTO> dtoList) {
		try {
			String message = allowanceService.insertAllowancesByType(dtoList);
			ApiResponse response = new ApiResponse("SUCCESS", message, "200", null);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			ApiResponse response = new ApiResponse("FAILURE", e.getMessage(), "500", null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping(value = "/fetchby/employeeid", consumes = { "multipart/form-data" }, produces = "application/json")
	public List<EmployeeAllowanceDTO> getAllowancesByEmployeeid(@RequestParam int employeeid,
			@RequestParam String type) {
		return allowanceService.getAllowancesByEmployeeid(employeeid, type);
	}

	@PostMapping(value = "/data", consumes = "multipart/form-data", produces = "application/json")
	public List<EmployeeAllowanceDTO> getAllAllowances(@RequestParam String type) {
		return allowanceService.getAllAllowances(type);
	}

	@PostMapping(value = "/remove",consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<String> removeComponent(@RequestParam int id, @RequestParam String type, @RequestParam int updatedBy) {
		String result = allowanceService.getComponentRemove(id, type,updatedBy);
		return ResponseEntity.ok(result.toString());
	}

}

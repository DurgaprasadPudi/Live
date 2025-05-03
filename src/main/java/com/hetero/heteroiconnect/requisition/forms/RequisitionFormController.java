package com.hetero.heteroiconnect.requisition.forms;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/form")
public class RequisitionFormController {

	private final RequisitionFormService requisitionFormService;

	public RequisitionFormController(RequisitionFormService requisitionFormService) {
		this.requisitionFormService = requisitionFormService;
	}

	@PostMapping("/insertmpform")
	public ResponseEntity<ApiResponse> insertManpowerForm(@RequestBody ManpowerRequisitionFormDTO dto) {
		return ResponseEntity.ok(new ApiResponse(requisitionFormService.insertManpowerForm(dto)));
	}

	@GetMapping("/empdetails/{employeeId}")
	public ResponseEntity<EmployeeDetailsDTO> getEmployeeDetails(@PathVariable int employeeId) {
		return ResponseEntity.ok(requisitionFormService.getEmployeeDetails(employeeId));
	}

	@PostMapping(value = "/bu", produces = "application/json")
	public ResponseEntity<List<MasterData>> getBu(@RequestParam(name = "name") String name) {
		return ResponseEntity.ok(requisitionFormService.getBu(name));
	}

	@GetMapping(value = "/designation", produces = "application/json")
	public ResponseEntity<List<MasterData>> getTitle() {
		return ResponseEntity.ok(requisitionFormService.getTitle());
	}

	@GetMapping(value = "/department", produces = "application/json")
	public ResponseEntity<List<MasterData>> getDepartment() {
		return ResponseEntity.ok(requisitionFormService.getDepartment());
	}

	@PostMapping("/positions")
	public ResponseEntity<ApiResponse> insertPositions(@RequestBody CompanyBasicDetailsDTO dto) {
		return ResponseEntity.ok(new ApiResponse(requisitionFormService.insertPositions(dto)));
	}

	@PostMapping(value = "/name", produces = "application/json")
	public ResponseEntity<List<MasterData>> getDependentName(@RequestParam(name = "name") String name) {
		return ResponseEntity.ok(requisitionFormService.getDependentName(name));
	}

}

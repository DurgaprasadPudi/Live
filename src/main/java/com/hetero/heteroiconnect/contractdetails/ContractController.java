package com.hetero.heteroiconnect.contractdetails;

import java.io.IOException;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetero.heteroiconnect.requisition.forms.ApiResponse;
import com.hetero.heteroiconnect.worksheet.model.Master;

@RestController
@RequestMapping("/contractdetails")
public class ContractController {

	private final ContractService contractService;
	private final ObjectMapper objectMapper;

	public ContractController(ContractService contractService, ObjectMapper objectMapper) {
		this.contractService = contractService;
		this.objectMapper = objectMapper;
	}

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse> uploadContractEmployee(@RequestParam("data") String jsonData,
			@RequestParam("file") MultipartFile file) throws IOException {
		ContractPersonDetailsDTO dto = objectMapper.readValue(jsonData, ContractPersonDetailsDTO.class);
		return ResponseEntity.ok(new ApiResponse(contractService.uploadContractEmployee(dto, file)));
	}

	@GetMapping(value = "/company", produces = "application/json")
	public ResponseEntity<List<Master>> getCompany() {
		return ResponseEntity.ok(contractService.getCompany());
	}

	@GetMapping(value = "/contracts/{companyId}", produces = "application/json")
	public ResponseEntity<List<Master>> getContracts(@PathVariable int companyId) {
		return ResponseEntity.ok(contractService.getContracts(companyId));
	}

	@GetMapping(value = "/contracttypes/{contractId}/{companyId}", produces = "application/json")
	public ResponseEntity<List<ContractType>> getContractTypes(@PathVariable int contractId,
			@PathVariable int companyId) {
		return ResponseEntity.ok(contractService.getContractTypes(contractId, companyId));
	}

	@GetMapping(value = "/gender", produces = "application/json")
	@Cacheable("gender")
	public ResponseEntity<List<Master>> getGender() {
		return ResponseEntity.ok(contractService.getGender());
	}

	@GetMapping("/all")
	public ResponseEntity<List<ContractDetailsDTO>> getAllContractDetails() {
		return ResponseEntity.ok(contractService.getAllContractDetails());
	}

	@DeleteMapping("/deleteemployee/{id}")
	public ResponseEntity<ApiResponse> deleteEmployeeData(@PathVariable int id) {
		return ResponseEntity.ok(new ApiResponse(contractService.deleteEmployeeData(id)));
	}

	@PostMapping("/doeentry")
	public ResponseEntity<ApiResponse> updateDOE(@RequestParam int id, @RequestParam String dateOfExit,
			@RequestParam(required = false) String comment) {
		return ResponseEntity.ok(new ApiResponse(contractService.updateDOE(id, dateOfExit, comment)));
	}

}

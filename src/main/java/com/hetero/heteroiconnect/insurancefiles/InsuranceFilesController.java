package com.hetero.heteroiconnect.insurancefiles;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.requisition.forms.ApiResponse;

@RestController
@RequestMapping("/insurance")
public class InsuranceFilesController {

	private final InsuranceFilesService insuranceFilesService;

	public InsuranceFilesController(InsuranceFilesService insuranceFilesService) {
		this.insuranceFilesService = insuranceFilesService;
	}

	@PostMapping("/filesupload")
	public ResponseEntity<ApiResponse> uploadFiles(@RequestParam String type,
			@RequestParam("files") List<MultipartFile> files) {
		ApiResponse response = insuranceFilesService.uploadFiles(type, files);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/files/{type}")
	public ResponseEntity<List<InsuranceFileDTO>> getAllInsuranceFiles(@PathVariable("type") Integer type) {
		List<InsuranceFileDTO> files = insuranceFilesService.getAllInsuranceFiles(type);
		return ResponseEntity.ok(files);
	}

	@PostMapping("/emp/{loginId}")
	public ResponseEntity<InsuranceFileDTO> getEmployeeInsuranceDetails(@PathVariable("loginId") Integer loginId) {
		return ResponseEntity.ok(insuranceFilesService.getEmployeeInsuranceDetails(loginId));
	}

	@GetMapping("/usermanual")
	@Cacheable("usermanual")
	public ResponseEntity<Map<String, byte[]>> getUserManuals() {
		return ResponseEntity.ok(insuranceFilesService.getUserManuals());
	}

	@GetMapping("/forms")
	@Cacheable("forms")
	public ResponseEntity<Map<String, byte[]>> getHrForms() {
		return ResponseEntity.ok(insuranceFilesService.getHrForms());
	}
}

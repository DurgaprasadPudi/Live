package com.hetero.heteroiconnect.insurancefiles;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.requisition.forms.ApiResponse;
import com.hetero.heteroiconnect.worksheet.model.Master;

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
	public ResponseEntity<List<HrFormDTO>> getHrForms() {
		return ResponseEntity.ok(insuranceFilesService.getHrForms());
	}

	// Family Insurance
	/*
	 * @GetMapping("/familyinsuranceflag") public ResponseEntity<Boolean> getDates()
	 * { return ResponseEntity.ok(insuranceFilesService.getDates()); }
	 */  

	@GetMapping("insurancedata/{empId}")
	public ResponseEntity<EmployeeBasicDetailsDTO> getEmployeeDetails(@PathVariable int empId) {
		return ResponseEntity.ok(insuranceFilesService.getEmployeeDetails(empId));
	}

	@GetMapping(value = "/relation", produces = "application/json")
	public ResponseEntity<List<Master>> getRelation() {
		return ResponseEntity.ok(insuranceFilesService.getRelation());
	}

	@DeleteMapping("deletefamilymember/{familyMemberId}/{empId}")
	public ResponseEntity<ApiResponse> deleteFamilyMember(@PathVariable int familyMemberId, @PathVariable int empId) {
		return ResponseEntity.ok(insuranceFilesService.deleteFamilyMember(familyMemberId, empId));
	}

	@PostMapping("intrestflag/{familyMemberId}/{flag}")
	public ResponseEntity<ApiResponse> getIntrestFlag(@PathVariable int familyMemberId, @PathVariable int flag) {
		return ResponseEntity.ok(insuranceFilesService.getIntrestFlag(familyMemberId, flag));
	}

	@PostMapping("/uploadfamilymembers")
	public ResponseEntity<ApiResponse> saveFamilyMembers(@RequestBody UploadFamilyMembersDetails uploadDetails) {
		return ResponseEntity.ok(insuranceFilesService.saveFamilyMembers(uploadDetails));
	}

	@GetMapping(value = "/premiumdata", produces = "application/json")
	public ResponseEntity<List<EmployeeInsuranceCompleteDetailsDTO>> getInsurancePremiumDetails() {
		return ResponseEntity.ok(insuranceFilesService.getInsurancePremiumDetails());
	}

	@PostMapping(value = "/premiumdetailsfile", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<ApiResponse> uploadPremiumDetailsInfo(@RequestParam MultipartFile file) {
		return ResponseEntity.ok(insuranceFilesService.uploadPremiumDetailsInfo(file));
	}

	@PostMapping("updateinterestflag/{premiumInfoId}/{flag}")
	public ResponseEntity<ApiResponse> updateInterestStatus(@PathVariable int premiumInfoId, @PathVariable int flag) {
		return ResponseEntity.ok(insuranceFilesService.updateInterestStatus(premiumInfoId, flag));
	}

}

package com.hetero.heteroiconnect.bankinformation;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/bankinfo")
public class BankInformationController {

	private final BankInformationService bankInformationService;

	public BankInformationController(BankInformationService bankInformationService) {
		this.bankInformationService = bankInformationService;
	}

	@PostMapping("/upload/{bankId}")
	public ResponseEntity<Map<String, Object>> processEmployeeExcel(@PathVariable int bankId,
			@RequestParam("file") MultipartFile file, @RequestParam int loginId) {
		return ResponseEntity.ok(bankInformationService.processEmployeeExcel(file, bankId, loginId));
	}

	@GetMapping("/latestbankdetails/{bankId}/{loginId}")
	public ResponseEntity<?> getLatestEmployeeBankDetails(@PathVariable int bankId, @PathVariable int loginId) {
		return ResponseEntity.ok(bankInformationService.getLatestEmployeeBankDetails(bankId, loginId));
	}

	/*
	 * @PostMapping("/insert") public ResponseEntity<List<EmployeeBankDTO>>
	 * bulkInsert(@RequestBody List<EmployeeBankDTO> employeeBankList) { return
	 * ResponseEntity.ok(bankInformationService.bulkInsertEmployeeBankDetails(
	 * employeeBankList)); }
	 */

	@PostMapping("/insert")
	public ResponseEntity<Map<String, Object>> bulkInsert(@RequestBody List<EmployeeBankDTO> employeeBankList) {
		return ResponseEntity.ok(bankInformationService.bulkInsertEmployeeBankDetails(employeeBankList));
	}

	@GetMapping("/bankcount/{loginId}/{bankId}")
	public ResponseEntity<List<Map<String, Object>>> getDistinctExcelSheetNos(@PathVariable int loginId,
			@PathVariable int bankId) {
		return ResponseEntity.ok(bankInformationService.getDistinctExcelSheetNos(loginId, bankId));
	}

	@GetMapping("/overallbankdetails/{excelSheetNo}")
	public ResponseEntity<?> getOverallEmployeeBankDetails(@PathVariable int excelSheetNo) {
		return ResponseEntity.ok(bankInformationService.getOverallEmployeeBankDetails(excelSheetNo));
	}
}

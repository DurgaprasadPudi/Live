package com.hetero.heteroiconnect.masterreports;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class MasterReportController {
	private MasterReportService masterReportService;

	public MasterReportController(MasterReportService masterReportService) {
		this.masterReportService = masterReportService;
	}

	@PostMapping("/masterdetails")
	public ResponseEntity<List<EmployeeMasterDetailsDTO>> getMasterDetails(@RequestParam String buName) {
		System.out.println(" master called");
		return ResponseEntity.ok(masterReportService.getMasterDetails(buName));
	}
}

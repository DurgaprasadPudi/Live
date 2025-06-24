package com.hetero.heteroiconnect.masterreports;

import java.util.List;

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
}

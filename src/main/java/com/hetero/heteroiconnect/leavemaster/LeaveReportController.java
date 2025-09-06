package com.hetero.heteroiconnect.leavemaster;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

 

@RestController
public class LeaveReportController {

	@Autowired
	private LeaveReportService leaveReportService;

	// reports
	@GetMapping(value = "/payperiodyear", produces = "application/json")
	public ResponseEntity<Object> getDistinctPayperiod() {
		return ResponseEntity.ok(leaveReportService.getDistinctPayPeriod());
	}

	// leavebalance data
	@PostMapping(value = "/leavereport", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> leaveReport(@RequestParam("payperioddate") String payperioddate) {
		return ResponseEntity.ok(leaveReportService.leaveReport(payperioddate));
	}

//leave balance report
	@PostMapping(value = "/leavereportdownload", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<byte[]> fileDownload(@RequestParam("payPeriodDate") String payPeriodDate)
			throws SQLException, IOException {
		System.out.println(payPeriodDate);
		byte[] excelData = leaveReportService.leaveReportDownload(payPeriodDate);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "LeaveReport.xlsx");
		return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
	}

	// leave date wise
	@PostMapping(value = "/leavedate", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> leaveReportDate(@RequestParam("employeeid") String employeeid,
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate,
			@RequestParam("year") String year) {
		return ResponseEntity.ok(leaveReportService.leaveReportDate(employeeid, fromDate, toDate, year));
	}

	// leave date wise report
	@PostMapping(value = "/leaveDatedownload",produces = "application/json")
	public ResponseEntity<byte[]> leaveDatedownload(@RequestParam("employeeid") String employeeid,
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate,
			@RequestParam("year") String year) throws SQLException, IOException {
		byte[] excelData = leaveReportService.leaveReportDownload(employeeid, fromDate, toDate, year);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "LeaveDateReport.xlsx");
		return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
	}
}

package com.hetero.heteroiconnect.worksheet.report;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/worksheetsheetreport")
public class WorksheetDownlaodController {
	@Autowired
	WorksheetDownloadService worksheedownloadtService;
	
	@PostMapping(value="/getTeams",consumes = "multipart/form-data", produces = "application/json")
	public Object getTeams(@RequestParam String employeeid) {
		return worksheedownloadtService.getTeams(employeeid);
	}
	
	@PostMapping(value = "/download", consumes = "multipart/form-data", produces = "application/octet-stream")
	public ResponseEntity<byte[]> fileDownload(
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "employeeid") String employeeid,
			@RequestParam(value = "formDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "teamS", required = false) Integer teamS)
			throws SQLException, IOException, ExecutionException, InterruptedException {

		if (year == null) {
			year = 0;
		}
		if (month == null) {
			month = 0;
		}
		byte[] excelData = worksheedownloadtService.Download(year, month, employeeid, fromDate, toDate,teamS);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "worksheet.xlsx");

		return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
	}
	
	@PostMapping(value = "/self", consumes = "multipart/form-data", produces = "application/octet-stream")
	public ResponseEntity<byte[]> selfEmployeeDownload(
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "employeeid") String employeeid,
			@RequestParam(value = "formDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate)
			throws SQLException, IOException, ExecutionException, InterruptedException {

		if (year == null) {
			year = 0;
		}
		if (month == null) {
			month = 0;
		}
		byte[] excelData = worksheedownloadtService.selfEmployeeDownload(year, month, employeeid, fromDate, toDate);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "worksheet.xlsx");

		return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
	}
	@PostMapping(value="/getEmployees",consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<?> getEmployees(
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "employeeid") String employeeid,
			@RequestParam(value = "formDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {

		if (year == null) {
			year = 0;
		}
		if (month == null) {
			month = 0;
		}
		return new ResponseEntity<>(worksheedownloadtService.getEmployees(year, month, employeeid, fromDate, toDate), HttpStatus.OK);

	}

	@PostMapping(value="/getEmployeesbyid",consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<?> getEmployeesByEmployeeid(
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "employeeid") String employeeid,
			@RequestParam(value = "formDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {

		if (year == null) {
			year = 0;
		}
		if (month == null) {
			month = 0;
		}
		return new ResponseEntity<>(worksheedownloadtService.getEmployeesByEmployeeid(year, month, employeeid, fromDate, toDate), HttpStatus.OK);
	}

}

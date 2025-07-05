package com.hetero.heteroiconnect.attendancereports;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
	private AttendanceService attendanceService;

	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@PostMapping(value = "/locations", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<List<AttendanceLocationPojo>> getLocations(@RequestParam(name = "empId") int empId) {
		return ResponseEntity.ok(attendanceService.getLocations(empId));
	}

	@PostMapping(value = "/data", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> getData(@RequestBody AttendanceFilterPojo attendanceFilterPojo) {
		return ResponseEntity.ok(attendanceService.getData(attendanceFilterPojo));
	}

	@PostMapping(value = "/businessunit", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<List<AttendanceLocationPojo>> getEmployeebusinessunit(
			@RequestParam(name = "empId") int empId) {
		return ResponseEntity.ok(attendanceService.getEmployeebusinessunit(empId));
	}

	@PostMapping(value = "/payperiodmonths", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> getPayPeriodMonths(
			@RequestParam(name = "empId", required = false) Integer empId,
			@RequestParam(name = "location", required = false) Integer location,
			@RequestParam(name = "bu", required = false) List<Integer> bu,
			@RequestParam(name = "callName", required = false) String callName,
			@RequestParam(name = "isPayPeriod") boolean isPayPeriod) {
		return ResponseEntity.ok(attendanceService.getPayPeriodMonths(empId, location, bu, callName, isPayPeriod));
	}
}

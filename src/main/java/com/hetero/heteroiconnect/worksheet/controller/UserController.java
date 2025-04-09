package com.hetero.heteroiconnect.worksheet.controller;

import java.util.List;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hetero.heteroiconnect.worksheet.model.EmployeeSummary;
import com.hetero.heteroiconnect.worksheet.model.TotalWorkingHours;
import com.hetero.heteroiconnect.worksheet.model.UserWorksheet;
import com.hetero.heteroiconnect.worksheet.service.UserService;
 

@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(value = "/worksheetupload", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UserWorksheet> addUserData(@RequestBody UserWorksheet userWorksheet) {
		return ResponseEntity.ok(userService.addUserData(userWorksheet));
	}

	@GetMapping(value = "/dailyworksheet/{employeeId}", produces = "application/json")
	public ResponseEntity<TotalWorkingHours> getDailyWorkSheet(
			@PathVariable(name = "employeeId") int employeeId) {
		return ResponseEntity.ok(userService.getDailyWorkSheet(employeeId));
	}

	@PutMapping("/approval/{employeeId}")
	public ResponseEntity<String> submitForApproval(
			@PathVariable(name = "employeeId")  int employeeId) {
		return ResponseEntity.ok(userService.submitForApproval(employeeId));
	}

	@GetMapping(value = "/weeksummary/{employeeId}", produces = "application/json")
	public ResponseEntity<List<EmployeeSummary>> getUserWeekSummaryWorkSheet(
			@PathVariable(name = "employeeId") int employeeId) {
		return ResponseEntity.ok(userService.getUserWeekSummaryWorkSheet(employeeId));
	}

	@DeleteMapping(value = "/deletetask/{sno}", produces = "application/json")
	public ResponseEntity<String> deleteTask(
			@PathVariable(name = "sno") int sno) {
		return ResponseEntity.ok(userService.deleteTask(sno));
	}

	/*
	 * @PutMapping(value = "/worksheetupdate", consumes = "application/json",
	 * produces = "application/json") public ResponseEntity<UpdateWorkSheet>
	 * updateWorkSheet(@RequestBody @Valid UpdateWorkSheet updateWorkSheet) { return
	 * ResponseEntity.ok(userService.updateWorkSheet(updateWorkSheet)); }
	 */

}

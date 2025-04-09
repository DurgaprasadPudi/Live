package com.hetero.heteroiconnect.familydetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;





import com.hetero.heteroiconnect.familydetails.EmployeeService.EmployeeExistsException;
import com.hetero.heteroiconnect.familydetails.EmployeeService.FamilyMemberExistsException;

@RestController
@RequestMapping("/sindhuridetails")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService employeeService;

	@PostMapping(value = "empsubmit", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Map<String, Object>> insertEmpData(@RequestParam(name = "data") String data,
			@RequestParam("empFile") MultipartFile empFile,
			@RequestParam("familydetailsFile") List<MultipartFile> familydetailsFile,
			@RequestParam(name = "forms") String formsJson) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		EmployeeDataInsertDTO employeeData = null;
		List<FamilyDataInsertDTO> familyDataList = new ArrayList<>();

		try {
			employeeData = objectMapper.readValue(data, EmployeeDataInsertDTO.class);
			FamilyDataInsertDTO[] familyDataArray = objectMapper.readValue(formsJson, FamilyDataInsertDTO[].class);
			familyDataList = Arrays.asList(familyDataArray);

			for (FamilyDataInsertDTO familyData : familyDataList) {
				familyData.setEmpCode(employeeData.getEmpCode());
			}

		} catch (JsonProcessingException e) {
			logger.error("Invalid data format: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", "Invalid data format: " + e.getMessage()));
		}

		try {
			logger.info("Attempting to insert employee data for EmpCode: {}", employeeData.getEmpCode());
			employeeService.insertEmployeeData(employeeData, empFile);
			logger.info("Attempting to insert family data for EmpCode: {}", employeeData.getEmpCode());
			employeeService.insertFamilyData(familyDataList, familydetailsFile);
		} catch (EmployeeExistsException e) {
			logger.warn("Employee already exists: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", e.getMessage()));
		} catch (FamilyMemberExistsException e) {
			logger.warn("Family member already exists: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", e.getMessage()));
		} catch (Exception e) {
			logger.error("Failed to process data for EmpCode {}: {}", employeeData.getEmpCode(), e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "Failed to process data"));
		}

		Map<String, Object> response = new HashMap<>();
		response.put("employeeData", employeeData);
		response.put("familyData", familyDataList);

		logger.info("Successfully processed employee and family data for EmpCode: {}", employeeData.getEmpCode());
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "emptotaldata", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<?> getEmployeeWithFamily(@RequestParam String empCode) {
		logger.info("Request to get employee and family data for empCode: {}", empCode);
		EmployeeWithFamilyDTO employeeWithFamilyDTO = employeeService.getEmployeeWithFamilyData(empCode);

		Map<String, Object> response = new HashMap<>();

		if (employeeWithFamilyDTO == null) {
			logger.warn("Employee with empCode {} not found.", empCode);
			response.put("status", "Employee with empCode " + empCode + " not Exists");
			response.put("flag", "1002");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		logger.info("Successfully retrieved employee and family data for empCode: {}", empCode);
		response.put("flag", "1001");
		response.put("status", employeeWithFamilyDTO);

		return ResponseEntity.ok(response);
	}

	@PutMapping("deletefamilymember/{sno}")
	public ResponseEntity<?> deleteEmployee(@PathVariable int sno) {
		logger.info("Received request to delete employee with sno: {}", sno);

		boolean isDeleted = employeeService.deleteEmployeeBySno(sno);

		Map<String, Object> response = new HashMap<>();
		if (isDeleted) {
			logger.info("Employee with sno {} successfully deleted.", sno);
			response.put("message", "Employee with sno " + sno + " was successfully deleted.");
			response.put("status", HttpStatus.OK.value());
		} else {
			logger.warn("Failed to delete employee with sno {}. Employee may not exist or deletion failed.", sno);
			response.put("error", "Employee with sno " + sno + " not found or could not be deleted.");
			response.put("status", HttpStatus.NOT_FOUND.value());
		}
		return new ResponseEntity<>(response, isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "empupdate", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Map<String, Object>> updateEmpData(@RequestParam(name = "data") String data,
			@RequestParam("empFile") MultipartFile empFile) throws IOException {

		logger.info("Received update request for employee data.");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		EmployeeDataInsertDTO employeeData = null;
		try {
			employeeData = objectMapper.readValue(data, EmployeeDataInsertDTO.class);
			logger.info("Successfully parsed employee data for EmpCode: {}", employeeData.getEmpCode());
		} catch (JsonProcessingException e) {
			logger.error("Error parsing employee data: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", "Invalid data format"));
		}

		try {
			employeeService.updateEmployeeData(employeeData, empFile);
		} catch (Exception e) {
			logger.error("Error while updating employee data for EmpCode {}: {}", employeeData.getEmpCode(),
					e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "Failed to process data"));
		}

		Map<String, Object> response = new HashMap<>();
		response.put("employeeData", employeeData);
		logger.info("Employee data update completed successfully for EmpCode: {}", employeeData.getEmpCode());
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "insertfamilymembers", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Map<String, Object>> insertFamily(@RequestParam("empCode") String empCode,
			@RequestParam("familydetailsFile") List<MultipartFile> familydetailsFile,
			@RequestParam(name = "forms") String formsJson) throws IOException {

		logger.info("Received request to insert family members for EmpCode: {}", empCode);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		List<FamilyDataInsertDTO> familyDataList = new ArrayList<>();

		try {
			FamilyDataInsertDTO[] familyDataArray = objectMapper.readValue(formsJson, FamilyDataInsertDTO[].class);
			familyDataList = Arrays.asList(familyDataArray);

			for (FamilyDataInsertDTO familyData : familyDataList) {
				familyData.setEmpCode(empCode);
			}
			logger.info("Successfully parsed family data for EmpCode: {}", empCode);

		} catch (JsonProcessingException e) {
			logger.error("Error parsing family data for EmpCode {}: {}", empCode, e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", "Invalid data format"));
		}

		try {
			employeeService.insertFamilyMembers(familyDataList, familydetailsFile);
			logger.info("Successfully inserted family members for EmpCode: {}", empCode);
		} catch (FamilyMemberExistsException e) {
			logger.error("Family member already exists for EmpCode {}: {}", empCode, e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", e.getMessage()));
		} catch (Exception e) {
			logger.error("Failed to process data for EmpCode {}: {}", empCode, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "Failed to process data"));
		}

		Map<String, Object> response = new HashMap<>();
		response.put("familyData", familyDataList);
		return ResponseEntity.ok(response);
	}

}

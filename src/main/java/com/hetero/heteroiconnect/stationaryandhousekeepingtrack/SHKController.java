package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Validated
@RestController
@RequestMapping("shktrack")
public class SHKController {
	private SHKService shkService;

	public SHKController(SHKService shkService) {
		this.shkService = shkService;
	}

	@GetMapping(value = "items", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> getItems() {
		return ResponseEntity.ok(shkService.getItems());
	}

	@GetMapping(value = "stationary", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> getStationaryItems() {
		return ResponseEntity.ok(shkService.getStationaryItems());
	}

	@GetMapping(value = "housekeeping", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> getHouseKeepingItems() {
		return ResponseEntity.ok(shkService.getHouseKeepingItems());
	}

	@PostMapping(value = "raiserequest", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> raiseRequest(@RequestBody SHKRaiseRequest shkRaiseRequest) {
		return ResponseEntity.ok(shkService.raiseRequest(shkRaiseRequest));
	}

	@PostMapping(value = "parentrequesthistory", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> parentRequestHistory(@RequestBody Map<String, String> request) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fromDate = null;
		LocalDate toDate = null;

		try {
			Optional<String> fromDateStr = Optional.ofNullable(request.get("fromDateStr"));
			Optional<String> toDateStr = Optional.ofNullable(request.get("toDateStr"));

			if (fromDateStr.filter(s -> !s.isEmpty()).isPresent()) {
				fromDate = LocalDate.parse(fromDateStr.get(), formatter);
			}
			if (toDateStr.filter(s -> !s.isEmpty()).isPresent()) {
				toDate = LocalDate.parse(toDateStr.get(), formatter);
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'");
		}
		return ResponseEntity.ok(shkService.parentRequestHistory(fromDate, toDate));
	}

	@GetMapping(value = "childrequesthistory/{parentRequestId}", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> childRequestHistory(
			@PathVariable("parentRequestId") long requestId) {
		return ResponseEntity.ok(shkService.childRequestHistory(requestId));
	}

	@GetMapping(value = "parenttickets", produces = "application/json")
	public ResponseEntity<List<Long>> getParentsRequests() {
		return ResponseEntity.ok(shkService.getParentsRequests());
	}

	@PostMapping(value = "itemsupload/{parentRequestId}/{empId}", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> uploadItems(@PathVariable("parentRequestId") long requestId,
			@PathVariable("empId") int empId, @RequestParam(required = false) MultipartFile doc,
			@RequestParam String items) {
		List<SHKItems> itemList;
		try {
			ObjectMapper mapper = new ObjectMapper();
			itemList = mapper.readValue(items, new TypeReference<List<SHKItems>>() {
			});
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid format for items. Expected a JSON array of SHKItems.");
		}
		return ResponseEntity.ok(shkService.uploadItems(requestId, empId, doc, itemList));
	}

	@PostMapping(value = "parentregistrationhistory", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> parentRegistrationHistory(
			@RequestBody Map<String, String> request) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fromDate = null;
		LocalDate toDate = null;

		try {
			Optional<String> fromDateStr = Optional.ofNullable(request.get("fromDateStr"));
			Optional<String> toDateStr = Optional.ofNullable(request.get("toDateStr"));

			if (fromDateStr.filter(s -> !s.isEmpty()).isPresent()) {
				fromDate = LocalDate.parse(fromDateStr.get(), formatter);
			}
			if (toDateStr.filter(s -> !s.isEmpty()).isPresent()) {
				toDate = LocalDate.parse(toDateStr.get(), formatter);
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'");
		}
		return ResponseEntity.ok(shkService.parentRegistrationHistory(fromDate, toDate));
	}

	@GetMapping(value = "childregistrationhistory/{parentRequestId}", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> childRegistrationhistory(
			@PathVariable("parentRequestId") long requestId) {
		return ResponseEntity.ok(shkService.childRegistrationhistory(requestId));
	}

	@GetMapping(value = "inventory", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> getInventory(@RequestParam(required = false) String requestType) {
		return ResponseEntity.ok(shkService.getInventory(requestType));
	}

	@GetMapping(value = "empdetails/{empId}", produces = "application/json")
	public ResponseEntity<Map<String, Object>> getEmpDetails(@PathVariable("empId") int empId) {
		return ResponseEntity.ok(shkService.getEmpDetails(empId));
	}

	@GetMapping(value = "contractempdetails/{empId}", produces = "application/json")
	public ResponseEntity<Map<String, Object>> getContractorEmpDetails(@PathVariable("empId") int empId) {
		return ResponseEntity.ok(shkService.getEmpgetContractorEmpDetailsDetails(empId));
	}

	@GetMapping(value = "uploadsofsametype/{type}", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> getUploadsOfSameType(@PathVariable("type") int type) {
		return ResponseEntity.ok(shkService.getUploadsOfSameType(type));
	}

	@GetMapping(value = "raiseditems/{parentRequestId}", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> getRequestedItemTypes(
			@PathVariable("parentRequestId") long parentRequestId) {
		return ResponseEntity.ok(shkService.getRequestedItemTypes(parentRequestId));
	}

	@PostMapping(value = "assign", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> assign(@Valid @RequestBody AssignPojo request) {
		return ResponseEntity.ok(shkService.assign(request));
	}

	@GetMapping(value = "assignhistory", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> assignHistory() {
		return ResponseEntity.ok(shkService.assignHistory());
	}
}

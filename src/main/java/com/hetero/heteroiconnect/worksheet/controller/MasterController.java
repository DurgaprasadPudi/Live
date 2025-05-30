package com.hetero.heteroiconnect.worksheet.controller;

import java.util.List;

 

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hetero.heteroiconnect.worksheet.model.Master;
import com.hetero.heteroiconnect.worksheet.service.MasterService;

 

@RestController
//@RequestMapping("/master")
@RequestMapping("/worksheet")
public class MasterController {

	private MasterService masterService;

	public MasterController(MasterService masterService) {
		this.masterService = masterService;
	}

	@GetMapping(value = "/taskalignment", produces = "application/json")
	@Cacheable("taskalignments")
	public ResponseEntity<List<Master>> getTaskAlignment() {
		return ResponseEntity.ok(masterService.getTaskAlignment());
	}

	@GetMapping(value = "/category", produces = "application/json")
	@Cacheable("categories")
	public ResponseEntity<List<Master>> getCategory() {
		return ResponseEntity.ok(masterService.getCategory());
	}

	@GetMapping(value = "/priority", produces = "application/json")
	@Cacheable("priorities")
	public ResponseEntity<List<Master>> getPriority() {
		return ResponseEntity.ok(masterService.getPriority());
	}

	@GetMapping(value = "/tasktype", produces = "application/json")
	@Cacheable("tasktypes")
	public ResponseEntity<List<Master>> getTaskType() {
		return ResponseEntity.ok(masterService.getTaskType());
	}

	@GetMapping(value = "/outcome", produces = "application/json")
	@Cacheable("outcomes")
	public ResponseEntity<List<Master>> getOutcome() {
		return ResponseEntity.ok(masterService.getOutcome());
	}

	@GetMapping(value = "/plannedadhoc", produces = "application/json")
	@Cacheable("plannedadhoc")
	public ResponseEntity<List<Master>> getPlannedorAdhoc() {
		return ResponseEntity.ok(masterService.getPlannedorAdhoc());
	}

	@GetMapping(value = "/activity/{categoryId}", produces = "application/json")
	@Cacheable(value = "activities", key = "#categoryId")
	public ResponseEntity<List<Master>> getActivities(
			@PathVariable(name = "categoryId")  int categoryId) {
		return ResponseEntity.ok(masterService.getActivities(categoryId));
	}

	@GetMapping(value = "/workplace", produces = "application/json")
	@Cacheable("workplaces")
	public ResponseEntity<List<Master>> getWorkPlace() {
		return ResponseEntity.ok(masterService.getWorkPlace());
	}

//	@GetMapping(value = "/team/{reportingId}", produces = "application/json")
//	public ResponseEntity<List<Master>> getTeam(
//			@PathVariable(name = "reportingId") int reportingId,) {
//		return ResponseEntity.ok(masterService.getTeam(reportingId));
//	}  
 
	@GetMapping(value = "/team/{reportingId}/{loginby}", produces = "application/json")
	public ResponseEntity<List<Master>> getTeam(
	        @PathVariable(name = "reportingId") int reportingId,
	        @PathVariable(name = "loginby") int loginId) {
	    return ResponseEntity.ok(masterService.getTeam(reportingId, loginId));
	}


	@PostMapping(value = "/dependent", produces = "application/json")
	public ResponseEntity<List<Master>> getDependentName(
			@RequestParam(name = "name") String name) {
		return ResponseEntity.ok(masterService.getDependentName(name));
	}
	@GetMapping(value = "/dept/{reportingId}", produces = "application/json")
	public ResponseEntity<List<Master>> getDept(
			@PathVariable(name = "reportingId")  int reportingId) {
		return ResponseEntity.ok(masterService.getDept(reportingId));
	}
}

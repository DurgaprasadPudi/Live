package com.hetero.heteroiconnect.couriertracker;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/courier")
public class CourierTrackingController {

	private CourierTrackingService courierTrackingService;

	public CourierTrackingController(CourierTrackingService courierTrackingService) {
		this.courierTrackingService = courierTrackingService;
	}

	@GetMapping(value = "/typeofcourier", produces = "application/json")
	public ResponseEntity<?> getTypeOfCourier() {
		return ResponseEntity.ok(courierTrackingService.getTypeOfCouriers());
	}

	@PostMapping(value = "/fetch/data", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> getByEmpid(@RequestParam int employeeseq) {
		return ResponseEntity.ok(courierTrackingService.getByEmpid(employeeseq));
	}
	@PostMapping(value = "/sender/registration", produces = "application/json")
	public ResponseEntity<?> senderRegistration(@RequestBody CourierSenderTrackingDTO dto) {
		return ResponseEntity.ok(courierTrackingService.senderRegistration(dto));
	}

	@PostMapping(value = "/receiver/registration", produces = "application/json")
	public ResponseEntity<?> receiverRegistration(@RequestBody CourierReceiverTrackingDTO dto) {
		if (dto.getDocketNo() == null || dto.getDocketNo().trim().isEmpty()) {
			throw new DocketMissingException("Docket number is mandatory and cannot be empty.");
		}
		return ResponseEntity.ok(courierTrackingService.receiverRegistration(dto));
	}

	@PostMapping(value = "/sender/update", produces = "application/json")
	public ResponseEntity<?> editSenderRegistration(@RequestBody CourierSenderTrackingDTO dto) {
		return ResponseEntity.ok(courierTrackingService.editSenderRegistration(dto));
	}

	@PostMapping(value = "/receiver/update", produces = "application/json")
	public ResponseEntity<?> editReceiverRegistration(@RequestBody CourierReceiverTrackingDTO dto) {
		return ResponseEntity.ok(courierTrackingService.editReceiverRegistration(dto));
	}

	@GetMapping(value="/sendertracking",produces = "application/json")
	public ResponseEntity<Object> getSenderTrackingList(@RequestParam(defaultValue = "") String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return new ResponseEntity<>(courierTrackingService.findByAllFields(search, page, size), HttpStatus.OK);
	}
	@GetMapping(value="/receivertracking",produces = "application/json")
	public ResponseEntity<Object> getReceiverTrackingList(
	        @RequestParam(defaultValue = "") String search,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    return ResponseEntity.ok( courierTrackingService.getReceiverTrackingList(search, page, size));
	}
	
	@PostMapping(value = "/search", produces = "application/json")
	public  ResponseEntity<?> getSearch(@RequestParam String name) {
	    return ResponseEntity.ok(courierTrackingService.getSearch(name));
	}
	
	@GetMapping(value = "/sendertracking/report", produces = "application/json")
	public ResponseEntity<byte[]> senderTrackingReport() throws SQLException, IOException {
		byte[] excelData = courierTrackingService.courierTrackingService();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "courierTrackingSendingReport.xlsx");
		return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
	}

	@GetMapping(value = "/receivertracking/report", produces = "application/json")
	public ResponseEntity<byte[]> receiverTrackingReport() throws SQLException, IOException {
		byte[] excelData = courierTrackingService.receiverTrackingReport();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "receiverTrackingSendingReport.xlsx");
		return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
	}
}

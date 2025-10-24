package com.hetero.heteroiconnect.servicebilltracking;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/bill")
public class ServiceBillController {
	private final ServiceBill serviceBill;

	public ServiceBillController(ServiceBill serviceBill) {
		this.serviceBill = serviceBill;
	}

	@GetMapping(value = "/states", produces = "application/json")
	public List<Map<String, Object>> getStates() {
		return serviceBill.getStates();
	}

	@PostMapping(value = "/cities", produces = "application/json")
	public List<Map<String, Object>> getCities(@RequestParam("stateid") int stateId) {
		return serviceBill.getCities(stateId);
	}

	@GetMapping(value = "/businessunit", produces = "application/json")
	public Object getBusinessUnit() {
		return serviceBill.getBusinessUnit();
	}

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> saveServiceBill(@RequestPart("data") String dtoJson,
			@RequestPart(value = "invoiceFile", required = false) MultipartFile invoiceFile,
			@RequestPart(value = "chequeFile", required = false) MultipartFile chequeFile) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ServiceBillDTO dto = mapper.readValue(dtoJson, ServiceBillDTO.class);
		String result = serviceBill.saveServiceBill(dto, invoiceFile, chequeFile);
		return ResponseEntity.ok(result);
	}

	@PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> updateServiceBill(@RequestPart("data") String dtoJson,
			@RequestPart(value = "invoiceFile", required = false) MultipartFile invoiceFile,
			@RequestPart(value = "chequeFile", required = false) MultipartFile chequeFile) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		ServiceBillDTO dto = mapper.readValue(dtoJson, ServiceBillDTO.class);

		String result = serviceBill.updateServiceBill(dto, invoiceFile, chequeFile);
		return ResponseEntity.ok(result);
	}

	@PostMapping(value = "/fetch", produces = "application/json")
	public List<Map<String, Object>> getServiceBillData(@RequestParam int loginid,
			@RequestParam(required = false) String invoiceYearMonth) {
		return serviceBill.getServiceBillData(loginid, invoiceYearMonth);
	}

	@GetMapping("/files/{id}")
	public ResponseEntity<Map<String, Object>> downloadFiles(@PathVariable int id) {
		Map<String, Object> files = serviceBill.getFilesById(id);
		if (files.isEmpty() || (files.get("invoicebill") == null && files.get("cheque") == null)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", "No files found for ID: " + id));
		}
		return ResponseEntity.ok().header("Content-Type", "application/json").body(files);
	}

}

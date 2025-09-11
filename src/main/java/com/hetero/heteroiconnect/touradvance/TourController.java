package com.hetero.heteroiconnect.touradvance;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
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
@RequestMapping("/tour")
public class TourController {
	private TourServiceImpl tourService;

	public TourController(TourServiceImpl tourService) {
		this.tourService = tourService;
	}

	@GetMapping(value = "/master/travelmode", produces = "application/json")
	public ResponseEntity<Object> getmodeoftravel() {
		return ResponseEntity.ok(tourService.getModeOfTravel());
	}

	@GetMapping(value = "/master/tourtype", produces = "application/json")
	public ResponseEntity<Object> getTourType() {
		return ResponseEntity.ok(tourService.getTourType());
	}

	@GetMapping(value = "/master/amount", produces = "application/json")
	public ResponseEntity<Object> getAmountMode() {
		return ResponseEntity.ok(tourService.getAmountMode());
	}

	@GetMapping(value = "/master/bookingMode", produces = "application/json")
	public ResponseEntity<Object> getBookingMode() {
		return ResponseEntity.ok(tourService.getBookingsMode());
	}

	@GetMapping(value = "/master/SettlementMode", produces = "application/json")
	public ResponseEntity<Object> getSettlementMode() {
		return ResponseEntity.ok(tourService.getSettlementMode());
	}

	@GetMapping(value = "/master/billstatus", produces = "application/json")
	public ResponseEntity<Object> getBillStatus() {
		return ResponseEntity.ok(tourService.getBillStatus());
	}

	@PostMapping(value = "/fetch/data", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> getByEmpid(@RequestParam int employeeseq) throws Exception {
		return ResponseEntity.ok(tourService.getByEmpid(employeeseq));
	}

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
	public ResponseEntity<ApiResponse> saveTour(@RequestPart("dto") EmployeeTourDTO dto,
			@RequestParam(defaultValue = "false") boolean forceInsert,
			@RequestPart(value = "advanceBillFile", required = false) MultipartFile advanceBillFile) {

		if (advanceBillFile != null && advanceBillFile.getSize() > 10 * 1024 * 1024) {
			return ResponseEntity.badRequest().body(new ApiResponse("error", "File exceeds 10MB limit."));
		}
		int result = tourService.saveTourDetails(dto, forceInsert, advanceBillFile);

		if (result == -1) {
			ApiResponse response = new ApiResponse("conflict",
					"A tour already exists during this date range. Do you want to force insert?");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}

		ApiResponse success = new ApiResponse("success", "Tour saved successfully", result);
		return ResponseEntity.ok(success);
	}

	@PostMapping(value = "/search/hodby", produces = "application/json")
	public List<Master> getHodName(@RequestParam String name) {
		return tourService.getHodName(name);
	}

	@PostMapping(value = "/fetch/touremployees", produces = "application/json")
	public ResponseEntity<Object> getTourEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String searchTerm, @RequestParam int loginid) {
		return ResponseEntity.ok(tourService.getTourEmployees(page, size, searchTerm,loginid));
	}

	@PostMapping(value = "/finalsettlement", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> finalSettlement(@RequestPart("dto") TourUtilizationDTO dto,
			@RequestPart(value = "utilizationbill", required = false) MultipartFile utilizationbill) {
		if (utilizationbill != null && utilizationbill.getSize() > 10 * 1024 * 1024) {
			return ResponseEntity.badRequest().body(new ApiResponse("error", "File exceeds 10MB limit."));
		}
		return tourService.saveOrUpdateSettlement(dto, utilizationbill);
	}

	@PostMapping(value = "/fetchbytourid/{tourid}", produces = "application/json")
	public ResponseEntity<Object> getFetchById(@PathVariable int tourid) {
		return tourService.getFetchById(tourid);
	}

	@PostMapping(value = "/download", consumes = "multipart/form-data")
	public ResponseEntity<byte[]> getDownload(@RequestParam String fromDate, @RequestParam String toDate,@RequestParam int loginid)
			throws IOException {
		byte[] excelData = tourService.getExcelDownload(fromDate, toDate,loginid);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "TourDetails.xlsx");
		headers.setContentLength(excelData.length);
		return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
	}

	@PostMapping(value = "/fetch/finalsettlement", produces = "application/json")
	public ResponseEntity<Object> getFinalSettlementEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search,@RequestParam int loginid) {
		return ResponseEntity.ok(tourService.getFinalSettlementEmployees(page, size, search,loginid));
	}

	@PostMapping(value = "/conveyanceBill/save", consumes = {"multipart/form-data"})
	public ResponseEntity<?> saveConveyanceBill(
	        @RequestPart("billData") String billDataJson,
	        @RequestPart(value = "billFile", required = false) MultipartFile billFile,
	        @RequestPart(value = "chequeFile", required = false) MultipartFile chequeFile,
	        @RequestParam("createdBy") String createdBy
	) throws IOException {

	    EmployeeConveyanceBillDTO dto = new ObjectMapper().readValue(billDataJson, EmployeeConveyanceBillDTO.class);
	    Map<String, Object> response = tourService.saveConveyanceBill(dto, billFile, chequeFile, createdBy);
	    return ResponseEntity.status("success".equals(response.get("status")) ? 200 : 400)
	                         .body(response);
	}


	@PostMapping(value = "/fetch/conveyanceBill", produces = "application/json")
	public ResponseEntity<List<Map<String, Object>>> getConveyanceBillEmployees(@RequestParam int loginid) {
		return ResponseEntity.ok(tourService.getConveyanceBillEmployees(loginid));
	}

	@GetMapping("/conveyance/files/{id}")
	public ResponseEntity<Map<String, Object>> downloadFiles(@PathVariable int id) {
		Map<String, Object> files = tourService.getConveyanceFilesById(id);
		if (files.isEmpty() || (files.get("bill") == null && files.get("cheque") == null)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", "No files found for ID: " + id));
		}
		return ResponseEntity.ok().header("Content-Type", "application/json").body(files);
	}
	
	@GetMapping("/files/{tourid}")
	public ResponseEntity<Map<String, Object>> tourFiles(@PathVariable int tourid) {
		Map<String, Object> files = tourService.getTourById(tourid);
		return ResponseEntity.ok().header("Content-Type", "application/json").body(files);
	}

}

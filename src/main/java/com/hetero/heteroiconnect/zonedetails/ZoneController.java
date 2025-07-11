package com.hetero.heteroiconnect.zonedetails;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZoneController {
	@Autowired
	private ZoneServiceImpl zoneServiceImpl;

	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<ZoneEmployeeDTO> create(@RequestBody ZoneEmployeeDTO dto) {
		ZoneEmployeeDTO saved = zoneServiceImpl.saveEmployees(dto);
		return ResponseEntity.ok(saved);
	}

	@PutMapping(value = "/update", produces = "application/json")
	public ResponseEntity<ZoneEmployeeDTO> update(@RequestBody ZoneEmployeeDTO dto) {
		ZoneEmployeeDTO updated = zoneServiceImpl.updateEmployeeZone(dto);
		return ResponseEntity.ok(updated);
	}

	@GetMapping(value = "/citys", produces = "application/json")
	public List<Map<String, Object>> getCities() {
		return zoneServiceImpl.getCities();
	}
	
	@PostMapping(value = "/locations", produces = "application/json")
	public List<Map<String, Object>> getLocations(@RequestParam String cityId) {
		return zoneServiceImpl.getLocations(cityId);
	}
//	@PostMapping(value = "/companys", produces = "application/json")
//	public List<Map<String, Object>> getCompanys(@RequestParam String locationId) {
//		return zoneServiceImpl.getCompanys(locationId);
//	}
//	@PostMapping(value = "/buildings", produces = "application/json")
//	public List<Map<String, Object>> getBuilding(@RequestParam String locationId,@RequestParam String companyId) {
//		return zoneServiceImpl.getBuilding(locationId,companyId);
//	}
	@PostMapping(value = "/buildings", produces = "application/json")
	public List<Map<String, Object>> getBuilding(@RequestParam String locationId) {
		return zoneServiceImpl.getBuilding(locationId);
	}

	@PostMapping(value = "/floors", produces = "application/json")
	public List<Map<String, Object>> getFloor(@RequestParam String buildingId) {
		return zoneServiceImpl.getFloor(buildingId);
	}

	@PostMapping(value = "/zones", produces = "application/json")
	public List<Map<String, Object>> getZone(@RequestParam String floorId) {
		return zoneServiceImpl.getZone(floorId);
	}

	@GetMapping(value = "/fetch/employeedata", produces = "application/json")
	public Object fetchEmployees() {
		return zoneServiceImpl.getFetchEmployees();
	}
	@GetMapping("/download/excel")
	public ResponseEntity<byte[]> downloadExcel() {
	    byte[] excelData = zoneServiceImpl.getExcelDownload();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    headers.setContentDispositionFormData("attachment", "EmployeeZones.xlsx");
	    headers.setContentLength(excelData.length);

	    return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
	}


}

package com.hetero.heteroiconnect.fuelanddriverhistory;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hetero.heteroiconnect.requisition.forms.ApiResponse;

@RestController
@RequestMapping("/fuelanddriver")
public class FuelAndDriverController {
	private final FuelAndDriverService fuelAndDriverService;

	public FuelAndDriverController(FuelAndDriverService fuelAndDriverService) {
		this.fuelAndDriverService = fuelAndDriverService;
	}

	@PostMapping(value = "/details", consumes = "multipart/form-data")
	public List<FuelAndDriverResponseDTO> getFuelAndDriverDetails(@RequestParam String payPeriod) {
		return fuelAndDriverService.getFuelAndDriverDetails(payPeriod);
	}

	@GetMapping(value = "/payperiods")
	public List<Map<String, String>> getPayPeriods() {
		return fuelAndDriverService.getPayPeriodsWithMonthYear();
	}

	@PostMapping(value = "/add", consumes = "application/json")
	public ResponseEntity<ApiResponse> addFuelDriverHistory(List<FuelDriverHistoryInsertDTO> dto) {
		return ResponseEntity.ok(new ApiResponse(fuelAndDriverService.addFuelDriverHistory(dto)));
	}

}

package com.hetero.heteroiconnect.sfa_attendance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/sfa")
public class SfaController {
	private SfaService sfaService;
	@Value("${sfa.attendnace.url}")
	private String sfaUrl;

	public SfaController(SfaService sfaService) {
		this.sfaService = sfaService;
	}

	@GetMapping(value = "/employees", produces = "application/json")
	public ResponseEntity<List<String>> getEmployeeList() {
		return ResponseEntity.ok(sfaService.getEmployeeList());
	}

	@GetMapping(value = "/attendance", produces = "application/json")
	public ResponseEntity<Object> getAttendance() {
		Map<String, Object> dates = sfaService.getPayrollDates();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> requestEntity = new HttpEntity<>(dates, headers);

		// Create RestTemplate and disable error throwing on non-2xx
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return false;
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				// No operation
			}
		});

		try {
			@SuppressWarnings("rawtypes")
			ResponseEntity<Map> response = restTemplate.postForEntity(sfaUrl, requestEntity, Map.class);
			return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}

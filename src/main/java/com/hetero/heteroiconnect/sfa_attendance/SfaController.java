package com.hetero.heteroiconnect.sfa_attendance;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
	private ExecutorService executor = Executors.newFixedThreadPool(10);

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
			ResponseEntity<List<LinkedHashMap<String, Object>>> response = restTemplate.exchange(sfaUrl,
					HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<List<LinkedHashMap<String, Object>>>() {
					});

			List<LinkedHashMap<String, Object>> attendanceList = response.getBody();
			if (attendanceList == null)
				return ResponseEntity.status(response.getStatusCode()).body(Collections.emptyList());

			List<CompletableFuture<Map<String, Object>>> futures = attendanceList.stream()
					.map(bean -> CompletableFuture.supplyAsync(() -> sfaService.enrichRecord(bean), executor))
					.collect(Collectors.toList());

			List<Map<String, Object>> enhancedList = futures.stream().map(CompletableFuture::join)
					.collect(Collectors.toList());

			enhancedList.sort(Comparator.comparing(m -> m.get("EMPID").toString()));
			return ResponseEntity.status(response.getStatusCode()).body(enhancedList);

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("error", "Error processing attendance");
			errorMap.put("message", "Error processing attendance");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
		}
	}

}

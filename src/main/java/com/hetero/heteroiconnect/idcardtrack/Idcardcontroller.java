package com.hetero.heteroiconnect.idcardtrack;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class Idcardcontroller {

	private IDCardService iDCardService;   

	public Idcardcontroller(IDCardService iDCardService) {
		this.iDCardService = iDCardService;
	}

	@PostMapping(value = "/uploaddata", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ApiResponse> addIDCardIssuedDetails(@RequestBody  UploadDetails uploadDetails) {
		return ResponseEntity.ok(iDCardService.addIDCardIssuedDetails(uploadDetails));
	}
 
	@GetMapping("/fetchdetails/{status}")
	public ResponseEntity<List<FetchDetails>> getAllIDCardIssuedDetails(@PathVariable int status) {
		List<FetchDetails> list = iDCardService.getAllIDCardIssuedDetails(status);
		return ResponseEntity.ok(list);
	}
 
	@PostMapping(value = "/updateiddetails", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ApiResponse> updateIDDetails(@RequestBody   UploadDetails uploadDetails) {
		return ResponseEntity.ok(iDCardService.updateIDDetails(uploadDetails));
	}
 
	@PostMapping(value = "/getreissueddetails/{trackingId}", produces = "application/json")
	public ResponseEntity<List<ReissuedDetailsDTO>> updateIDDetails(@PathVariable String trackingId) {
		return ResponseEntity.ok(iDCardService.getReissuedDetails(trackingId));
	}
 
	
	
	
}

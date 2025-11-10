package com.hetero.heteroiconnect.iconnectrights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RightsAssignController {

	@Autowired
	RightsAssignService rightsAssignService;

	@GetMapping(value = "/rights", produces = "application/json")
	public ResponseEntity<Object> getRights() {
		return ResponseEntity.ok(rightsAssignService.getRights());
	}
    @PostMapping(value = "/assign", produces = "application/json")
    public ResponseEntity<Object> addRightAssign(@RequestBody List<RightsAssignDTO> rightsAssignDTOs) {
        Object result = rightsAssignService.addRightAssign(rightsAssignDTOs);
        return ResponseEntity.ok(result);
    }

	@PostMapping(value = "/assigned/data", produces = "application/json")
	public ResponseEntity<Object> getAssignedDataEmployeeid(@RequestParam int employeeid) {
		return ResponseEntity.ok(rightsAssignService.getAssignedDataEmployeeid(employeeid));
	}
	
	@GetMapping(value = "/getReqIPBotRights", produces = "application/json")
	public ResponseEntity<Object> getReqIPBotRights() {
		return ResponseEntity.ok(rightsAssignService.getReqIPBotRights());
	}
}

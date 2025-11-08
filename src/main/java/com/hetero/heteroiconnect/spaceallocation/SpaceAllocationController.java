package com.hetero.heteroiconnect.spaceallocation;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/space-allocation")
public class SpaceAllocationController {

	private final SpaceAllocationService service;

	public SpaceAllocationController(SpaceAllocationService service) {
		this.service = service;
	}

	@PostMapping("/insert")
	public ResponseEntity<List<SpaceAllocationDTO>> insertSpaceAllocations(
			@RequestBody List<SpaceAllocationDTO> dtoList) {
		return ResponseEntity.ok(service.insertSpaceAllocations(dtoList));
	}

	@GetMapping(value = { "/fetch", "/fetch/{raisedBy}" })
	public ResponseEntity<List<FetchSpaceAllocationDTO>> getSpaceAllocations(
			@PathVariable(value = "raisedBy", required = false) Integer raisedBy) {
		return ResponseEntity.ok(service.getSpaceAllocations(raisedBy));
	}

	@PostMapping("/update")
	public ResponseEntity<Map<String, Object>> updateSpaceAllocation(@RequestParam int allocationId,
			@RequestParam String comments) {
		return ResponseEntity.ok(service.updateStatusAndComments(allocationId, comments));
	}
}

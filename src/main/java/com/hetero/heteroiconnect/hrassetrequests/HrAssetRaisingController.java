
package com.hetero.heteroiconnect.hrassetrequests;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asset")
public class HrAssetRaisingController {

	private HrAssetRaisingService hrAssetRaisingService;

	public HrAssetRaisingController(HrAssetRaisingService hrAssetRaisingService) {
		this.hrAssetRaisingService = hrAssetRaisingService;
	}

	@GetMapping(value = "/bu/{loginId}", produces = "application/json")
	public ResponseEntity<List<Master>> getBusinessUnits(@PathVariable(name = "loginId") String loginId) {
		return ResponseEntity.ok(hrAssetRaisingService.getBusinessUnits(loginId));
	}

	@GetMapping(value = "/department", produces = "application/json")
	//@Cacheable("departments")
	public ResponseEntity<List<Master>> getDepartments() {
		return ResponseEntity.ok(hrAssetRaisingService.getDepartments());
	}

	@GetMapping(value = "/designation", produces = "application/json")
	//@Cacheable("designations")
	public ResponseEntity<List<Master>> getDesignations() {
		return ResponseEntity.ok(hrAssetRaisingService.getDesignations());
	}

	@GetMapping(value = "/manager", produces = "application/json")
	//@Cacheable("managers")
	public ResponseEntity<List<Master>> getManagers() {
		return ResponseEntity.ok(hrAssetRaisingService.getManagers());
	}

	@GetMapping(value = "/assettype", produces = "application/json")
	//@Cacheable("assettypes")
	public ResponseEntity<List<Master>> getAssetTypes() {
		return ResponseEntity.ok(hrAssetRaisingService.getAssetTypes());
	}

	@GetMapping(value = "/domain", produces = "application/json")
	//@Cacheable("domains")
	public ResponseEntity<List<Master>> getDomain() {
		return ResponseEntity.ok(hrAssetRaisingService.getDomain());
	}

	@PostMapping(value = "/raiseassets", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<String> employeeAssetsRaise(@RequestParam(name = "empData") String empData,
			@RequestParam(name = "assets") String assets) throws Exception {
		return hrAssetRaisingService.employeeAssetsRaise(empData, assets);
	}

	@PostMapping(value = "/list", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> getRaisedAssetsWithItems(@RequestBody HrFilter hrFilter) {
		try {
			List<AssetRequestDTO> raisedAssets = hrAssetRaisingService.getRaisedAssetsWithItems(hrFilter);
			return ResponseEntity.ok(raisedAssets);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch raised assets.");
		}
	}  

	@PostMapping("/acknowledge")
	public ResponseEntity<String> getAknowledgeStatus(@RequestParam String acknowledgeId,
			@RequestParam String requestId) {
		  
		System.out.println(requestId+"requestId");
		try {
			int updatedRows = hrAssetRaisingService.getAknowledgeStatus(acknowledgeId, requestId);
			if (updatedRows == 1) {
				return ResponseEntity.ok("Asset request updated successfully.");
			} else if (updatedRows == 1001) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("You're not authorized to acknowledge, please contact admin. ");
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("The request has already been processed.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the asset request: " + e.getMessage());
		}
	}

	@PostMapping(value = "/approvallist", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> getITPendingApprovals(@RequestBody ITFilter itFilter) {
		try {
			List<AssetRequestDTO> raisedAssets = hrAssetRaisingService.getITPendingApprovals(itFilter);
			return ResponseEntity.ok(raisedAssets);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to fetch approval assets:" + e.getMessage());
		}
	}

	@PostMapping(value = "/approvedlist", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> getITApprovalData(@RequestBody HrFilter hrFilter) {
		try {
			List<AssetRequestDTO> raisedAssets = hrAssetRaisingService.getITApprovalData(hrFilter);
			return ResponseEntity.ok(raisedAssets);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to fetch approved assets:" + e.getMessage());
		}
	}
}

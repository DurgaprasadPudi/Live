
package com.hetero.heteroiconnect.hrassetrequests;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface HrAssetRaisingService {
	public List<Master> getBusinessUnits(String loginId);

	public List<Master> getDepartments();

	public List<Master> getDesignations();

	public List<Master> getAssetTypes();

	public List<Master> getDomain();

	public List<Master> getManagers();

	public ResponseEntity<String> employeeAssetsRaise(String empData, String assets) throws Exception;

	public List<AssetRequestDTO> getRaisedAssetsWithItems(@RequestBody HrFilter hrFilter);

	public int getAknowledgeStatus(String acknowledgeId, String requestId);

	public List<AssetRequestDTO> getITPendingApprovals(@RequestBody ITFilter itFilter);

	public List<AssetRequestDTO> getITApprovalData(@RequestBody HrFilter hrFilter);

}

package com.hetero.heteroiconnect.couriertracker;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface CourierTrackingService {

	List<Map<String, Object>> getTypeOfCouriers();


	ResponseEntity<SuccessResponse> senderRegistration(CourierSenderTrackingDTO dto);

	ResponseEntity<SuccessResponse> receiverRegistration(CourierReceiverTrackingDTO dto);

	Object editSenderRegistration(CourierSenderTrackingDTO dto);

	Object editReceiverRegistration(CourierReceiverTrackingDTO dto);

	//Page<CourierSenderTrackingDTO> findByAllFields(String search, int page, int size);
	Page<CourierSenderTrackingDTO> findByAllFields(String search, int page, int size,int loginid);

	
	Page<CourierReceiverTrackingDTO> getReceiverTrackingList(String search, int page, int size,int loginid);


	Object getSearch(String name);

	byte[] courierTrackingService();

	byte[] receiverTrackingReport();

	Object getByEmpid(int employeeseq);
	
	

}

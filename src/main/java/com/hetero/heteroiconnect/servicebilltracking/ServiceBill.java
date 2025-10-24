package com.hetero.heteroiconnect.servicebilltracking;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ServiceBill {

	String saveServiceBill(ServiceBillDTO dto, MultipartFile invoiceFile, MultipartFile chequeFile);

	List<Map<String, Object>> getStates();

	List<Map<String, Object>> getCities(int cityid);

	Object getBusinessUnit();

	List<Map<String, Object>> getServiceBillData(int loginid, String invoiceYearMonth);

	Map<String, Object> getFilesById(int id);

	String updateServiceBill(ServiceBillDTO dto, MultipartFile invoiceFile, MultipartFile chequeFile);

}

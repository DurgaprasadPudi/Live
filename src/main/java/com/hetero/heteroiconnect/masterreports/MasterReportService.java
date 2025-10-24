package com.hetero.heteroiconnect.masterreports;

import java.util.List;

import com.hetero.heteroiconnect.hrassetrequests.Master;

public interface MasterReportService {
	List<EmployeeMasterDetailsDTO> getMasterDetails(MasterDetailsRequest request);

	public List<Master> getStatusCodes();
	
	List<EmployeeMasterDetailsDTO> getReportingMasterDetails(String reportingIdsCsv);
	
	List<Integer> getAllSubordinates(int managerSeq);
	
	 List<String> getReportingEmployees(int managerSeq);
	 
}

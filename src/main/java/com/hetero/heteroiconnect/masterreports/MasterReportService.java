package com.hetero.heteroiconnect.masterreports;

import java.util.List;

import com.hetero.heteroiconnect.hrassetrequests.Master;

public interface MasterReportService {
	List<EmployeeMasterDetailsDTO> getMasterDetails(MasterDetailsRequest request);

	public List<Master> getStatusCodes();
}

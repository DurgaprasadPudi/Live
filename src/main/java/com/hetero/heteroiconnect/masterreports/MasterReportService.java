package com.hetero.heteroiconnect.masterreports;

import java.util.List;

public interface MasterReportService {
	List<EmployeeMasterDetailsDTO> getMasterDetails(String buName);
}

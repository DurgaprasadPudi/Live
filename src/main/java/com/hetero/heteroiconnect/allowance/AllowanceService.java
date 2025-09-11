package com.hetero.heteroiconnect.allowance;

import java.util.List;

public interface AllowanceService {
	Object getAllAllowanceEmployeeInfo(String type);

	Object getComponent();

	List<EmployeeAllowanceDTO> getAllAllowances(String type);

	String getComponentRemove(int id, String type,int updatedBy);

	List<EmployeeAllowanceDTO> getAllowancesByEmployeeid(int employeeid, String type);

	String insertAllowancesByType(List<EmployeeAllowanceDTO> dtoList);

	

}

package com.hetero.heteroiconnect.requisition.forms;

import java.util.List;
   
public interface RequisitionFormService {
	String insertManpowerForm(ManpowerRequisitionFormDTO dto);

	public EmployeeDetailsDTO getEmployeeDetails(int employeeId);

	public List<MasterData> getBu(String name);

	public List<MasterData> getTitle();

	public List<MasterData> getDepartment();

	String insertPositions(CompanyBasicDetailsDTO dto);

	public List<MasterData> getDependentName(String name);

}

package com.hetero.heteroiconnect.familydetails;

import java.io.Serializable;
import java.util.List;

public class EmployeeWithFamilyDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EmployeeDataDTO employeeData;
	private List<FamilyDataDTO> familyDataList;

	public EmployeeWithFamilyDTO(EmployeeDataDTO employeeData, List<FamilyDataDTO> familyDataList) {
		this.employeeData = employeeData;
		this.familyDataList = familyDataList;
	}

	// Getters and Setters
	public EmployeeDataDTO getEmployeeData() {
		return employeeData;
	}

	public void setEmployeeData(EmployeeDataDTO employeeData) {
		this.employeeData = employeeData;
	}

	public List<FamilyDataDTO> getFamilyDataList() {
		return familyDataList;
	}

	public void setFamilyDataList(List<FamilyDataDTO> familyDataList) {
		this.familyDataList = familyDataList;
	}
}

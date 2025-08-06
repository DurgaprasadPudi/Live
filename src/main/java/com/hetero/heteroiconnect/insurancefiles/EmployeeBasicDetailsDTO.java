package com.hetero.heteroiconnect.insurancefiles;

import java.util.List;

public class EmployeeBasicDetailsDTO {
	private boolean flagData;
	private String empId;
	private String name;
	private String division;
	private String department;
	private String designation;
	private String gender;
	private String doj;
	private String dob;
	private String maritalStatus;
	private PremiumInfoDTO PremiumInfoDTO;
	private List<FamilyInsuranceDetailsDTO> familyInsuranceDetailsDTO;

	public EmployeeBasicDetailsDTO() {
		super();
	}

	public EmployeeBasicDetailsDTO(boolean flagData, String empId, String name, String division, String department,
			String designation, String gender, String doj, String dob, String maritalStatus,
			com.hetero.heteroiconnect.insurancefiles.PremiumInfoDTO premiumInfoDTO,
			List<FamilyInsuranceDetailsDTO> familyInsuranceDetailsDTO) {
		super();
		this.flagData = flagData;
		this.empId = empId;
		this.name = name;
		this.division = division;
		this.department = department;
		this.designation = designation;
		this.gender = gender;
		this.doj = doj;
		this.dob = dob;
		this.maritalStatus = maritalStatus;
		PremiumInfoDTO = premiumInfoDTO;
		this.familyInsuranceDetailsDTO = familyInsuranceDetailsDTO;
	}

	public boolean isFlagData() {
		return flagData;
	}

	public void setFlagData(boolean flagData) {
		this.flagData = flagData;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public PremiumInfoDTO getPremiumInfoDTO() {
		return PremiumInfoDTO;
	}

	public void setPremiumInfoDTO(PremiumInfoDTO premiumInfoDTO) {
		PremiumInfoDTO = premiumInfoDTO;
	}

	public List<FamilyInsuranceDetailsDTO> getFamilyInsuranceDetailsDTO() {
		return familyInsuranceDetailsDTO;
	}

	public void setFamilyInsuranceDetailsDTO(List<FamilyInsuranceDetailsDTO> familyInsuranceDetailsDTO) {
		this.familyInsuranceDetailsDTO = familyInsuranceDetailsDTO;
	}

}

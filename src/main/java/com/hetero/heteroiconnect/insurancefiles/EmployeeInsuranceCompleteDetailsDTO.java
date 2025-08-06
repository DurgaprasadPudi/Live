package com.hetero.heteroiconnect.insurancefiles;

import java.util.List;

public class EmployeeInsuranceCompleteDetailsDTO {

	private String empId;
	private String name;
	private String relation;
	private String division;
	private String department;
	private String designation;
	private String doj;
	private String gender;
	private String dob;
	private String age;
	private String maritalStatus;
	private PremiumInfoDTO premiumInfoDTO;
	private List<FamilyInsuranceCompleteDetailsDTO> familyInsuranceCompleteDetailsDTO;

	public EmployeeInsuranceCompleteDetailsDTO() {
		super();
	}

	public EmployeeInsuranceCompleteDetailsDTO(String empId, String name, String relation, String division,
			String department, String designation, String doj, String gender, String dob, String age,
			String maritalStatus, PremiumInfoDTO premiumInfoDTO,
			List<FamilyInsuranceCompleteDetailsDTO> familyInsuranceCompleteDetailsDTO) {
		super();
		this.empId = empId;
		this.name = name;
		this.relation = relation;
		this.division = division;
		this.department = department;
		this.designation = designation;
		this.doj = doj;
		this.gender = gender;
		this.dob = dob;
		this.age = age;
		this.maritalStatus = maritalStatus;
		this.premiumInfoDTO = premiumInfoDTO;
		this.familyInsuranceCompleteDetailsDTO = familyInsuranceCompleteDetailsDTO;
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

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
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

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public PremiumInfoDTO getPremiumInfoDTO() {
		return premiumInfoDTO;
	}

	public void setPremiumInfoDTO(PremiumInfoDTO premiumInfoDTO) {
		this.premiumInfoDTO = premiumInfoDTO;
	}

	public List<FamilyInsuranceCompleteDetailsDTO> getFamilyInsuranceCompleteDetailsDTO() {
		return familyInsuranceCompleteDetailsDTO;
	}

	public void setFamilyInsuranceCompleteDetailsDTO(
			List<FamilyInsuranceCompleteDetailsDTO> familyInsuranceCompleteDetailsDTO) {
		this.familyInsuranceCompleteDetailsDTO = familyInsuranceCompleteDetailsDTO;
	}

}

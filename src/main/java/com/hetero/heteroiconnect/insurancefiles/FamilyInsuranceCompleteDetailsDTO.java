package com.hetero.heteroiconnect.insurancefiles;

public class FamilyInsuranceCompleteDetailsDTO {

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
	private String sumInsurance;
	private String grossPremium;

	public FamilyInsuranceCompleteDetailsDTO() {
		super();
	}

	public FamilyInsuranceCompleteDetailsDTO(String empId, String name, String relation, String division,
			String department, String designation, String doj, String gender, String dob, String age,
			String maritalStatus, String sumInsurance, String grossPremium) {
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
		this.sumInsurance = sumInsurance;
		this.grossPremium = grossPremium;
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

	public String getSumInsurance() {
		return sumInsurance;
	}

	public void setSumInsurance(String sumInsurance) {
		this.sumInsurance = sumInsurance;
	}

	public String getGrossPremium() {
		return grossPremium;
	}

	public void setGrossPremium(String grossPremium) {
		this.grossPremium = grossPremium;
	}
}

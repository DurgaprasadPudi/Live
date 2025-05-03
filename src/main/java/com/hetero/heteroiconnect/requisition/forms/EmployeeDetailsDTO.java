package com.hetero.heteroiconnect.requisition.forms;

public class EmployeeDetailsDTO {

	private String employeeId;
	private String employeeName;
	private String qualification;
	private String dateOfResignation;
	private String designation;
	private String experience;
	private String dateOfRelieving;

	public EmployeeDetailsDTO(String employeeId, String employeeName, String qualification, String dateOfResignation,
			String designation, String experience, String dateOfRelieving) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.qualification = qualification;
		this.dateOfResignation = dateOfResignation;
		this.designation = designation;
		this.experience = experience;
		this.dateOfRelieving = dateOfRelieving;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getDateOfResignation() {
		return dateOfResignation;
	}

	public void setDateOfResignation(String dateOfResignation) {
		this.dateOfResignation = dateOfResignation;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getDateOfRelieving() {
		return dateOfRelieving;
	}

	public void setDateOfRelieving(String dateOfRelieving) {
		this.dateOfRelieving = dateOfRelieving;
	}

}
package com.hetero.heteroiconnect.allowance;

import java.util.List;

public class EmployeeAllowanceDTO {

	private Integer employeeId;
	private String type;
	private String employeeName;
	private String department;
	private String designation;
	private String businessUnit;
	private String createdBy;
	private String employeeType;
	private String  totalAllowanceAmount;
	private List<AllowanceComponent> components;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<AllowanceComponent> getComponents() {
		return components;
	}

	public void setComponents(List<AllowanceComponent> components) {
		this.components = components;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getTotalAllowanceAmount() {
		return totalAllowanceAmount;
	}

	public void setTotalAllowanceAmount(String totalAllowanceAmount) {
		this.totalAllowanceAmount = totalAllowanceAmount;
	}


}

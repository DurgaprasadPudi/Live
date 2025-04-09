package com.hetero.heteroiconnect.jobcode;

public class EmployeeJobcode {
	private String name;
	private String status;
	private String designation;
	private String department;
	private String bu;

	public String getName() {
		return name;
	}

	public EmployeeJobcode setName(String name) {
		this.name = name;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public EmployeeJobcode setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getDesignation() {
		return designation;
	}

	public EmployeeJobcode setDesignation(String designation) {
		this.designation = designation;
		return this;
	}

	public String getDepartment() {
		return department;
	}

	public EmployeeJobcode setDepartment(String department) {
		this.department = department;
		return this;
	}

	public String getBu() {
		return bu;
	}

	public EmployeeJobcode setBu(String bu) {
		this.bu = bu;
		return this;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", status=" + status + ", designation=" + designation + ", department="
				+ department + ", bu=" + bu + "]";
	}

}

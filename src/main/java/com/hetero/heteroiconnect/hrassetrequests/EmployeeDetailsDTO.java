package com.hetero.heteroiconnect.hrassetrequests;

import java.util.Date;

public class EmployeeDetailsDTO {
	private Integer requestId;
	private Integer empId;
	private String empName;
	private String department;
	private String designation;
	private String contactNum;
	private String reportingManager;
	private Date tentativeJoiningDate;
	private Integer loginId;
	private Integer bu;
	private String workLocation;

	public String getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
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

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	public Date getTentativeJoiningDate() {
		return tentativeJoiningDate;
	}

	public void setTentativeJoiningDate(Date tentativeJoiningDate) {
		this.tentativeJoiningDate = tentativeJoiningDate;
	}

	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	public Integer getBu() {
		return bu;
	}

	public void setBu(Integer bu) {
		this.bu = bu;
	}

}

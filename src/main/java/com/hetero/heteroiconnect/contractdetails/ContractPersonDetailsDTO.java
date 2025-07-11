package com.hetero.heteroiconnect.contractdetails;

public class ContractPersonDetailsDTO {

	private int companyId;
	private int contractId;
	private int employeeId;
	private String employeeName;
	private String doj;
	private int gender;
	private String department;
	private String permanentAddress;
	private String presentAddress;
	private String mobileNumber;
	private String aadharNumber;
	private String file;
	private String dob;

	public ContractPersonDetailsDTO() {
	}

	public ContractPersonDetailsDTO(int companyId, int contractId, int employeeId, String employeeName, String doj,
			int gender, String department, String permanentAddress, String presentAddress, String mobileNumber,
			String aadharNumber, String file, String dob) {
		super();
		this.companyId = companyId;
		this.contractId = contractId;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.doj = doj;
		this.gender = gender;
		this.department = department;
		this.permanentAddress = permanentAddress;
		this.presentAddress = presentAddress;
		this.mobileNumber = mobileNumber;
		this.aadharNumber = aadharNumber;
		this.file = file;
		this.dob = dob;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

}

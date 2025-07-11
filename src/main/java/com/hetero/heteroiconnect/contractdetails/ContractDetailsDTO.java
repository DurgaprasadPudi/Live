package com.hetero.heteroiconnect.contractdetails;

public class ContractDetailsDTO {
	private Integer contractDetailId;
	private Integer companyId;
	private String companyName;
	private Integer contractId;
	private String contractName;
	private String contractPersonMobileNumber;
	private Integer employeeId;
	private String employeeName;
	private String doj;
	private Integer genderId;
	private String genderName;
	private String department;
	private String permanentAddress;
	private String presentAddress;
	private String mobileNumber;
	private String aadharNumber;
	private String fileName;
	private byte[] file;
	private String createdDateTime;
	private String dob;

	public Integer getContractDetailId() {
		return contractDetailId;
	}

	public void setContractDetailId(Integer contractDetailId) {
		this.contractDetailId = contractDetailId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractPersonMobileNumber() {
		return contractPersonMobileNumber;
	}

	public void setContractPersonMobileNumber(String contractPersonMobileNumber) {
		this.contractPersonMobileNumber = contractPersonMobileNumber;
	}

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

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public ContractDetailsDTO(Integer contractDetailId, Integer companyId, String companyName, Integer contractId,
			String contractName, String contractPersonMobileNumber, Integer employeeId, String employeeName, String doj,
			Integer genderId, String genderName, String department, String permanentAddress, String presentAddress,
			String mobileNumber, String aadharNumber, String fileName, byte[] file, String createdDateTime,
			String dob) {
		super();
		this.contractDetailId = contractDetailId;
		this.companyId = companyId;
		this.companyName = companyName;
		this.contractId = contractId;
		this.contractName = contractName;
		this.contractPersonMobileNumber = contractPersonMobileNumber;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.doj = doj;
		this.genderId = genderId;
		this.genderName = genderName;
		this.department = department;
		this.permanentAddress = permanentAddress;
		this.presentAddress = presentAddress;
		this.mobileNumber = mobileNumber;
		this.aadharNumber = aadharNumber;
		this.fileName = fileName;
		this.file = file;
		this.createdDateTime = createdDateTime;
		this.dob = dob;
	}
}
